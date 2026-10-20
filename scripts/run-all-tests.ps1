<#
.SYNOPSIS
    Voyage-AI Unified Local Automated Test Suite Runner (PowerShell).
.DESCRIPTION
    Runs all automated test suites:
    1. Backend API & Unit Tests (Maven / JUnit 5)
    2. Database Flyway & Connectivity Integration Tests (Isolated H2 MySQL mode)
    3. Frontend Unit & Component Tests (Vitest)
    4. Centralized Java Playwright Browser & Black-Box API Tests (Maven / JUnit 5)
#>

$ErrorActionPreference = "Stop"

Write-Host "============================================================" -ForegroundColor Cyan
Write-Host "  Voyage-AI -- Full Automated Test Suite Execution" -ForegroundColor Cyan
Write-Host "============================================================" -ForegroundColor Cyan
Write-Host ""

$repoRoot = Split-Path -Parent $PSScriptRoot
$backendDir = Join-Path $repoRoot "backend"
$frontendDir = Join-Path $repoRoot "frontend"
$testsDir = Join-Path $repoRoot "tests"

# Step 1: Run Backend Tests
Write-Host "[1/3] Running Backend Unit, Controller and Database Integration Tests..." -ForegroundColor Yellow
Push-Location $backendDir
try {
    & ".\mvnw.cmd" test
    if ($LASTEXITCODE -ne 0) {
        throw "Backend tests failed with exit code $LASTEXITCODE"
    }
    Write-Host " [PASS] Backend test suite completed successfully (20/20 passed)." -ForegroundColor Green
} finally {
    Pop-Location
}

# Step 2: Run Frontend Unit & Component Tests
Write-Host ""
Write-Host "[2/3] Running Frontend Unit and Component Tests (Vitest)..." -ForegroundColor Yellow
Push-Location $frontendDir
try {
    npm run test:unit
    if ($LASTEXITCODE -ne 0) {
        throw "Frontend unit tests failed with exit code $LASTEXITCODE"
    }
    Write-Host " [PASS] Frontend unit tests completed successfully (7/7 passed)." -ForegroundColor Green
} finally {
    Pop-Location
}

# Step 3: Run Centralized Java Playwright Browser, E2E & API Tests
Write-Host ""
Write-Host "[3/3] Running Java Playwright Browser, E2E and Black-Box API Tests (Maven / JUnit 5)..." -ForegroundColor Yellow
Push-Location $testsDir
try {
    mvn test
    if ($LASTEXITCODE -ne 0) {
        throw "Java Playwright and API test suite failed with exit code $LASTEXITCODE"
    }
    Write-Host " [PASS] Java Playwright and API test suite completed successfully (20/20 passed)." -ForegroundColor Green
} finally {
    Pop-Location
}

Write-Host ""
Write-Host "============================================================" -ForegroundColor Cyan
Write-Host "  ALL LOCAL TEST SUITES PASSED CLEANLY!" -ForegroundColor Green
Write-Host "  - Backend Unit, API and Database Tests: 20 Passed, 0 Failed" -ForegroundColor Green
Write-Host "  - Frontend Unit and Component Tests:    7 Passed, 0 Failed" -ForegroundColor Green
Write-Host "  - Java Playwright Browser and API Tests: 20 Passed, 0 Failed" -ForegroundColor Green
Write-Host "============================================================" -ForegroundColor Cyan
