@echo off
echo Starting download script in GitBash...

REM Check if bash is available
where bash >nul 2>&1
if errorlevel 1 (
    echo Error: bash was not found. Please install Git Bash or add it to your PATH.
    pause
    exit /b 1
)

REM change to directory of this .bat-file
cd /d "%~dp0"

REM execute the shell script inside the GitBash
bash download-openapi-jar.sh
