@echo off
echo ========================================
echo   Compiling Quiz Application
echo ========================================
echo.

echo Compiling Java files...
javac *.java

if %errorlevel% neq 0 (
    echo.
    echo ERROR: Compilation failed!
    echo Please check the error messages above.
    pause
    exit /b 1
)

echo.
echo ========================================
echo   Compilation Successful!
echo ========================================
echo.
echo What would you like to do?
echo.
echo 1. Run Gemini AI Setup
echo 2. Test Gemini Integration
echo 3. Run Quiz Application
echo 4. Exit
echo.

set /p choice="Enter your choice (1-4): "

if "%choice%"=="1" goto setup
if "%choice%"=="2" goto test
if "%choice%"=="3" goto run
if "%choice%"=="4" goto end

echo Invalid choice!
pause
exit /b 1

:setup
echo.
echo ========================================
echo   Running Gemini AI Setup
echo ========================================
echo.
java GeminiSetup
goto end

:test
echo.
echo ========================================
echo   Testing Gemini Integration
echo ========================================
echo.
java TestGeminiIntegration
goto end

:run
echo.
echo ========================================
echo   Running Quiz Application
echo ========================================
echo.
java Login
goto end

:end
echo.
pause
