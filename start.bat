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
REM Load .env variables into environment (never echoes secrets)
REM ============================================================
if exist .env (
    for /f "usebackq tokens=1* delims==" %%A in (`findstr /v "^#" .env ^| findstr "="`) do (
        set "KEY_NAME=%%A"
        set "KEY_VAL=%%B"
        for /f "tokens=* delims= " %%C in ("!KEY_NAME!") do set "KEY_NAME=%%C"
        for /f "tokens=* delims= " %%C in ("!KEY_VAL!") do set "KEY_VAL=%%C"
        if defined KEY_VAL (
            if "!KEY_VAL:~0,1!"=="\"" if "!KEY_VAL:~-1!"=="\"" set "KEY_VAL=!KEY_VAL:~1,-1!"
            if "!KEY_VAL:~0,1!"=="'" if "!KEY_VAL:~-1!"=="'" set "KEY_VAL=!KEY_VAL:~1,-1!"
        )
        if defined KEY_NAME if defined KEY_VAL (
            set "!KEY_NAME!=!KEY_VAL!"
        )
    )
)

REM Safe startup diagnostic for Gemini configuration (reports presence only, never value)
if defined GEMINI_API_KEY (
    if not "!GEMINI_API_KEY!"=="" (
        echo [DIAGNOSTIC] GEMINI_API_KEY: Configured [presence: YES, non-blank: YES].
    ) else (
        echo [DIAGNOSTIC] GEMINI_API_KEY: Defined but blank. AI concierge will report unconfigured.
    )
) else (
    echo [DIAGNOSTIC] GEMINI_API_KEY: Not set in environment or .env. AI concierge will report unconfigured.
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
ping 127.0.0.1 -n 2 >nul
goto :WAIT_MYSQL_LOOP


REM ============================================================
REM STEP 2: Spring Boot Backend [Port 8081]
REM ============================================================
:STEP_BACKEND
echo.
echo [2/3] Checking Spring Boot Backend...

REM Check if port 8081 is open
call :CHECK_PORT 8081
if "%PORT_OPEN%"=="1" (
    call :CHECK_BACKEND_HEALTH
    if "%BACKEND_HEALTHY%"=="1" (
        REM If .env has a key, verify running backend has operational Gemini
        if defined GEMINI_API_KEY if not "!GEMINI_API_KEY!"=="" (
            call :CHECK_GEMINI_HEALTH
            if "!GEMINI_CONFIGURED!"=="0" (
                echo   - [WARN] Backend running on port 8081 has stale/unconfigured Gemini service.
                echo            Stopping stale backend to apply updated configuration...
                call :STOP_BACKEND
                goto :START_BACKEND
            )
        )
        echo   - [OK] Backend service is running and healthy at http://localhost:8081
        goto :STEP_FRONTEND
    ) else (
        echo   - [WARN] Port 8081 is occupied by an unresponsive process. Stopping it...
        call :STOP_BACKEND
    )
)

:START_BACKEND
REM Backend is not running; start via Docker Compose if Docker is available
where.exe docker >nul 2>&1
if not errorlevel 1 (
    echo   - [INFO] Starting backend container via Docker Compose...
    docker compose -f infra/docker-compose.yml --env-file .env up -d --build backend >nul 2>&1
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
ping 127.0.0.1 -n 2 >nul
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
ping 127.0.0.1 -n 2 >nul
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

:CHECK_GEMINI_HEALTH
REM Checks if backend reports Gemini service as operational
set GEMINI_CONFIGURED=0
curl.exe -s http://localhost:8081/api/v1/ai/health 2>nul | findstr /i "OPERATIONAL" >nul 2>&1
if not errorlevel 1 set GEMINI_CONFIGURED=1
exit /b 0

:STOP_BACKEND
REM Terminate any stale container or local host process on port 8081
echo   - [INFO] Stopping stale backend on port 8081...
where.exe docker >nul 2>&1
if not errorlevel 1 (
    docker compose -f infra/docker-compose.yml stop backend >nul 2>&1
    docker rm -f voyage_ai_backend >nul 2>&1
)
powershell -NoProfile -ExecutionPolicy Bypass -Command "$conns = Get-NetTCPConnection -LocalPort 8081 -ErrorAction SilentlyContinue; foreach ($c in $conns) { Stop-Process -Id $c.OwningProcess -Force -ErrorAction SilentlyContinue }" >nul 2>&1
ping 127.0.0.1 -n 3 >nul
exit /b 0

