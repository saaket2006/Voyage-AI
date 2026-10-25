@echo off
setlocal enabledelayedexpansion

title Voyage-AI Launcher

echo ============================================================
echo   Voyage-AI -- Local Development Launcher
echo ============================================================
echo.

cd /d "%~dp0"

REM Ensure .env exists, copying from .env.example if necessary
if not exist .env (
    if exist .env.example (
        echo [INFO] .env not found. Creating from .env.example...
        copy .env.example .env >nul
    ) else (
        echo [WARN] .env.example not found. Using default environment configuration.
    )
)

REM ============================================================
REM STEP 1: MySQL Database [Port 3307 / 3306]
REM ============================================================
echo [1/3] Checking MySQL Database...

REM First check if Docker MySQL is already listening on port 3307
call :CHECK_PORT 3307
if "%PORT_OPEN%"=="1" (
    echo   - [OK] MySQL is running on port 3307 [canonical database: voyage].
    goto :STEP_BACKEND
)

REM Next check if local/host MySQL is listening on standard port 3306
call :CHECK_PORT 3306
if "%PORT_OPEN%"=="1" (
    echo   - [OK] MySQL is running on port 3306.
    goto :STEP_BACKEND
)

REM If neither port is active, attempt to start the container via Docker Compose
echo   - [INFO] MySQL is not running on port 3307 or 3306.
where.exe docker >nul 2>&1
if errorlevel 1 (
    echo   - [ERROR] Docker is not installed or not in PATH, and no local MySQL was found on port 3306.
    echo            Please start MySQL manually before proceeding.
    goto :STEP_BACKEND
)

echo   - [INFO] Starting MySQL container via Docker Compose...
docker compose -f infra/docker-compose.yml --env-file .env up -d mysql >nul 2>&1
if errorlevel 1 (
    docker compose -f infra/docker-compose.yml up -d mysql >nul 2>&1
)

echo   - [INFO] Waiting for MySQL container to become ready on port 3307...
set MYSQL_WAIT=0
:WAIT_MYSQL_LOOP
set /a MYSQL_WAIT+=1
call :CHECK_PORT 3307
if "%PORT_OPEN%"=="1" (
    echo   - [OK] MySQL is ready on port 3307 [canonical database: voyage].
    goto :STEP_BACKEND
)
if %MYSQL_WAIT% geq 30 (
    echo   - [ERROR] MySQL did not respond on port 3307 within 30 seconds.
    echo            Backend startup may fail if the database is not accessible.
    goto :STEP_BACKEND
)
timeout /t 1 /nobreak >nul
goto :WAIT_MYSQL_LOOP


REM ============================================================
REM STEP 2: Spring Boot Backend [Port 8081]
REM ============================================================
:STEP_BACKEND
echo.
echo [2/3] Checking Spring Boot Backend...

REM Check if backend is already healthy at http://localhost:8081/api/v1/health
call :CHECK_BACKEND_HEALTH
if "%BACKEND_HEALTHY%"=="1" (
    echo   - [OK] Backend service is running and healthy at http://localhost:8081
    goto :STEP_FRONTEND
)

REM Check if port 8081 is open but still initializing
call :CHECK_PORT 8081
if "%PORT_OPEN%"=="1" (
    echo   - [INFO] Backend is currently initializing on port 8081. Waiting for health endpoint...
    goto :WAIT_BACKEND_HEALTH
)

REM Backend is not running; start via Docker Compose if Docker is available
where.exe docker >nul 2>&1
if not errorlevel 1 (
    echo   - [INFO] Starting backend container via Docker Compose...
    docker compose -f infra/docker-compose.yml --env-file .env up -d backend >nul 2>&1
    if not errorlevel 1 (
        echo   - [INFO] Waiting for containerized backend to report healthy...
        goto :WAIT_BACKEND_HEALTH
    )
)

REM Docker unavailable or Docker startup failed: launch locally via Maven
echo   - [INFO] Starting Spring Boot backend locally in a separate window...
if exist backend\mvnw.cmd (
    start "Voyage-AI Backend" cmd /c "cd /d %~dp0backend && mvnw.cmd spring-boot:run"
) else (
    start "Voyage-AI Backend" cmd /c "cd /d %~dp0backend && mvn spring-boot:run"
)

:WAIT_BACKEND_HEALTH
set BACKEND_WAIT=0
:WAIT_BACKEND_LOOP
set /a BACKEND_WAIT+=1
call :CHECK_BACKEND_HEALTH
if "%BACKEND_HEALTHY%"=="1" (
    echo   - [OK] Backend is ready and healthy at http://localhost:8081
    goto :STEP_FRONTEND
)
if %BACKEND_WAIT% geq 35 (
    echo   - [WARN] Backend did not respond within the expected timeout.
    echo            It may still be compiling or starting up in the background.
    goto :STEP_FRONTEND
)
timeout /t 1 /nobreak >nul
goto :WAIT_BACKEND_LOOP


REM ============================================================
REM STEP 3: React + Vite Frontend [Port 5173]
REM ============================================================
:STEP_FRONTEND
echo.
echo [3/3] Checking React + Vite Frontend...

REM Check if frontend is already serving on port 5173
call :CHECK_FRONTEND
if "%FRONTEND_READY%"=="1" (
    echo   - [OK] Frontend is already running at http://localhost:5173
    goto :STEP_COMPLETE
)

REM Check if node_modules exists, install if missing
if not exist "frontend\node_modules\" (
    echo   - [INFO] frontend\node_modules not found. Installing npm packages...
    call npm install --prefix frontend
)

echo   - [INFO] Starting Vite frontend dev server in a separate window...
start "Voyage-AI Frontend" cmd /c "cd /d %~dp0frontend && npm run dev"

echo   - [INFO] Waiting for Vite frontend server to become ready...
set FRONTEND_WAIT=0
:WAIT_FRONTEND_LOOP
set /a FRONTEND_WAIT+=1
call :CHECK_FRONTEND
if "%FRONTEND_READY%"=="1" (
    echo   - [OK] Frontend is ready at http://localhost:5173
    goto :STEP_COMPLETE
)
if %FRONTEND_WAIT% geq 20 (
    echo   - [WARN] Frontend did not respond within 20 seconds. It may still be launching.
    goto :STEP_COMPLETE
)
timeout /t 1 /nobreak >nul
goto :WAIT_FRONTEND_LOOP


REM ============================================================
REM STEP 4: Completion Summary & Browser Launch
REM ============================================================
:STEP_COMPLETE
echo.
echo ============================================================
echo   Voyage-AI is ready and operational
echo ============================================================
echo   Frontend Application:  http://localhost:5173
echo   Backend REST API:      http://localhost:8081/api/v1
echo   Backend Health Check:  http://localhost:8081/api/v1/health
echo   Actuator Health:       http://localhost:8081/actuator/health
echo   Database:              voyage [MySQL 8.0 on port 3307]
echo ============================================================
echo.
echo Opening http://localhost:5173 in your default browser...
start http://localhost:5173
echo.
exit /b 0


REM ============================================================
REM SUBROUTINES / HELPER FUNCTIONS
REM ============================================================

:CHECK_PORT
REM Checks if TCP port %~1 is open on 127.0.0.1
set PORT_OPEN=0
powershell -NoProfile -ExecutionPolicy Bypass -Command "try { $s = New-Object System.Net.Sockets.TcpClient; $s.Connect('127.0.0.1', %~1); $s.Close(); exit 0 } catch { exit 1 }" >nul 2>&1
if not errorlevel 1 set PORT_OPEN=1
exit /b 0

:CHECK_BACKEND_HEALTH
REM Checks backend /api/v1/health endpoint
set BACKEND_HEALTHY=0
curl.exe -s -f http://localhost:8081/api/v1/health >nul 2>&1
if not errorlevel 1 set BACKEND_HEALTHY=1
exit /b 0

:CHECK_FRONTEND
REM Checks frontend on port 5173
set FRONTEND_READY=0
curl.exe -s -f http://localhost:5173 >nul 2>&1
if not errorlevel 1 (
    set FRONTEND_READY=1
    exit /b 0
)
REM Fallback to TCP port check if curl gets non-200 during Vite startup
call :CHECK_PORT 5173
if "%PORT_OPEN%"=="1" set FRONTEND_READY=1
exit /b 0
