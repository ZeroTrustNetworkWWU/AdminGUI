call gradlew build

if %errorlevel% neq 0 (
    echo Failed to build the Gradle project.
    exit /b %errorlevel%
)

start cmd /c gradlew run