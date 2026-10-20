<#
.SYNOPSIS
    Voyage-AI Real-Provider Smoke Test Runner (PowerShell).
.DESCRIPTION
    Executes live smoke tests targeting the locally running Spring Boot backend
    and the real Open-Meteo weather integration without mocking.
#>

$ErrorActionPreference = "Stop"

Write-Host "============================================================" -ForegroundColor Cyan
Write-Host "  Voyage-AI -- Real-Service Integration Smoke Test" -ForegroundColor Cyan
Write-Host "============================================================" -ForegroundColor Cyan

$repoRoot = Split-Path -Parent $PSScriptRoot
$testsDir = Join-Path $repoRoot "tests"

Push-Location $testsDir
try {
    mvn test -Psmoke
    if ($LASTEXITCODE -ne 0) {
        throw "Smoke tests failed with exit code $LASTEXITCODE"
    }
    Write-Host ""
    Write-Host "[PASS] Real-service integration smoke test succeeded!" -ForegroundColor Green
} finally {
    Pop-Location
}
