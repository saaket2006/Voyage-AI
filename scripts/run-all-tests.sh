#!/usr/bin/env bash
# Voyage-AI Unified Local Automated Test Suite Runner (POSIX / Bash).
set -e

echo "============================================================"
echo "  Voyage-AI — Full Automated Test Suite Execution"
echo "============================================================"

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(dirname "$SCRIPT_DIR")"

echo ""
echo "[1/3] Running Backend Unit, Controller & Database Integration Tests..."
cd "$REPO_ROOT/backend"
./mvnw test

echo ""
echo "[2/3] Running Frontend Unit & Component Tests (Vitest)..."
cd "$REPO_ROOT/frontend"
npm run test:unit

echo ""
echo "[3/3] Running Java Playwright Browser, E2E & API Tests (Maven / JUnit 5)..."
cd "$REPO_ROOT/tests"
mvn test

echo ""
echo "============================================================"
echo "  ALL LOCAL TEST SUITES PASSED CLEANLY!"
echo "============================================================"
