@echo off
cls
echo ===============================================
echo    Spring Boot Movie Application Launcher
echo ===============================================
echo.

REM Sprawdzenie czy Java jest zainstalowana
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo BŁĄD: Java nie jest zainstalowana lub nie znajduje się w PATH
    echo Zainstaluj Java 8 lub nowszą wersję
    pause
    exit /b 1
)

echo Java zostala znaleziona:
java -version
echo.

REM Sprawdzenie czy Maven jest zainstalowany (opcjonalnie)
where mvn >nul 2>&1
if %errorlevel% equ 0 (
    echo Maven zostal znaleziony:
    mvn -version
    echo.
    set MAVEN_AVAILABLE=true
) else (
    echo Maven nie zostal znaleziony - będzie użyty Maven Wrapper
    set MAVEN_AVAILABLE=false
)

echo ===============================================
echo Uruchamianie aplikacji Spring Boot...
echo ===============================================
echo.

REM Sprawdzenie czy istnieje plik JAR
if exist "target\*.jar" (
    echo Znaleziono skompilowany plik JAR
    for %%f in (target\*.jar) do (
        echo Uruchamianie: %%f
        java -jar "%%f"
        goto :end
    )
)

REM Jeśli nie ma JAR-a, kompiluj i uruchom
echo Nie znaleziono skompilowanego JAR-a
echo Kompilowanie i uruchamianie aplikacji...
echo.

if "%MAVEN_AVAILABLE%"=="true" (
    echo Używanie Maven...
    call mvn clean compile
    if %errorlevel% neq 0 (
        echo BŁĄD: Kompilacja nieudana
        pause
        exit /b 1
    )
    echo.
    echo Uruchamianie aplikacji...
    call mvn spring-boot:run
) else (
    echo Używanie Maven Wrapper...
    if exist "mvnw.cmd" (
        call mvnw.cmd clean compile
        if %errorlevel% neq 0 (
            echo BŁĄD: Kompilacja nieudana
            pause
            exit /b 1
        )
        echo.
        echo Uruchamianie aplikacji...
        call mvnw.cmd spring-boot:run
    ) else (
        echo BŁĄD: Nie znaleziono Maven ani Maven Wrapper
        echo Upewnij się, że jesteś w głównym katalogu projektu
        pause
        exit /b 1
    )
)

:end
echo.
echo ===============================================
echo Aplikacja została zakończona
echo ===============================================
pause