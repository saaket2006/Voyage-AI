<#
.SYNOPSIS
    Runs Java Playwright Browser Tests using Maven and JUnit 5.
#>
$ErrorActionPreference = "Stop"

$testsDir = Split-Path -Parent $PSScriptRoot
Push-Location $testsDir
try {
    Write-Host "Running Java Playwright Browser Tests..." -ForegroundColor Cyan
    mvn test -Dtest="*Browser*Test,NavigationTest,DestinationsDiscoveryTest,DestinationDetailModalTest,ResponsiveTest"
} finally {
    Pop-Location
}
