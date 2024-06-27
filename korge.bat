@ECHO OFF

SETLOCAL EnableDelayedExpansion

SET INSTALLER_VERSION=0.0.4
SET INSTALLER_URL=https://github.com/korlibs/compiler.korge.org/releases/download/v%INSTALLER_VERSION%/korge-kotlin-compiler-all.tar.xz
SET INSTALLER_SHA256=f059e3756c8762ce9b9eedc8a8ec569ea0d480726bfa81299ef34998dc27d6fd

SET KORGEDIR=%USERPROFILE%\.korge
SET JAVA=%KORGEDIR%\jre-21\bin\java.exe
SET INSTALLER_PATH=%KORGEDIR%\compiler
SET INSTALLER_LOCAL_FILE=%INSTALLER_PATH%\korge-kotlin-compiler-all.%INSTALLER_VERSION%.jar
MKDIR "%INSTALLER_PATH%" 2> NUL

IF NOT EXIST "%INSTALLER_LOCAL_FILE%" (
    CALL :DOWNLOAD_FILE "%INSTALLER_URL%" "%INSTALLER_LOCAL_FILE%.tar.xz" "%INSTALLER_SHA256%"
    CALL :EXTRACT_TAR "%INSTALLER_LOCAL_FILE%.tar.xz" "%INSTALLER_PATH%" 0 "%INSTALLER_LOCAL_FILE%"
    COPY /Y "%INSTALLER_PATH%\korge-kotlin-compiler-all.jar" %INSTALLER_LOCAL_FILE% > NUL 2> NUL
)

IF NOT EXIST "%JAVA%" (
    IF "%PROCESSOR_ARCHITECTURE%" == "ARM64" (
        CALL :DOWNLOAD_FILE "https://github.com/korlibs/universal-jre/releases/download/0.0.1/microsoft-jre-21.0.3-windows-aarch64.tar.xz" "%KORGEDIR%\jre-21.tar.xz" "8F18060960FD7935D76C79BBB643B1779440B73AE9715153A3BA332B1B8A2348"
    ) ELSE (
        CALL :DOWNLOAD_FILE "https://github.com/korlibs/universal-jre/releases/download/0.0.1/microsoft-jre-21.0.3-windows-x64.tar.xz" "%KORGEDIR%\jre-21.tar.xz" "6D16528A2201DCBE0ADDB0622F5CBE0CD6FA84AE937D3830FC1F74B32132C37B"
    )
    CALL :EXTRACT_TAR "%KORGEDIR%\jre-21.tar.xz" "%KORGEDIR%\jre-21" 1 "%JAVA%"
)

REM CALL :NORMALIZE_PATH "."
REM echo %RETVAL%
REM "%JAVA%" "-Duser.dir=%RETVAL%" -jar "%INSTALLER_LOCAL_FILE%" %*
"%JAVA%" -jar "%INSTALLER_LOCAL_FILE%" %*

EXIT /B

:DOWNLOAD_FILE
    SET URL=%~1
    SET LOCAL_PATH=%~2
    SET EXPECTED_SHA256=%~3

    IF EXIST "%LOCAL_PATH%" (
        EXIT /B
    )

    IF NOT EXIST "%LOCAL_PATH%.tmp" (
        echo Downloading %URL% into %LOCAL_PATH%
        curl -sL "%URL%" -o "%LOCAL_PATH%.tmp"
    )
    powershell -NoProfile -ExecutionPolicy Bypass -Command "(Get-Filehash -Path '%LOCAL_PATH:\=\\%.tmp' -Algorithm SHA256).Hash" > "%LOCAL_PATH%.sha256"
    SET /p DOWNLOAD_SHA256=<"%LOCAL_PATH%.sha256"

    IF /i "%DOWNLOAD_SHA256%" == "%EXPECTED_SHA256%" (
        MOVE "%LOCAL_PATH%.tmp" "%LOCAL_PATH%" > NUL 2> NUL
    ) ELSE (
        ECHO ERROR downloading %URL%, SHA256=%DOWNLOAD_SHA256%, but expected SHA256=%EXPECTED_SHA256%
        EXIT /B -1
    )
EXIT /b

:EXTRACT_TAR
    SET INPUT_FILE=%~1
    SET OUT=%~2
    SET STRIP_COMPONENTS=%~3
    SET CHECK_EXISTS=%~4

    IF EXIST %CHECK_EXISTS% (
        EXIT /B
    )

    MKDIR "%OUT%" > NUL 2> NUL
    echo Extracting %INPUT_FILE%...
    tar --strip-components %STRIP_COMPONENTS% -C "%OUT%" -xf "%INPUT_FILE%"
EXIT /b

:NORMALIZE_PATH
  SET RETVAL=%~f1
EXIT /B