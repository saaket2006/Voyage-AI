<#
.SYNOPSIS
    Runs Black-box API Tests against local backend using Maven and JUnit 5.
#>
$ErrorActionPreference = "Stop"

$testsDir = Split-Path -Parent $PSScriptRoot
Push-Location $testsDir
try {
    Write-Host "Running Black-box API Tests..." -ForegroundColor Cyan
    mvn test -Dtest="*ApiTest"
} finally {
    Pop-Location
}
