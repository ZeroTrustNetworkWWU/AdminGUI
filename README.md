# AdminGUI

## Description:
The AdminGUI project aims to provide a user-friendly interface for managing the Zero Trust Network. This README provides an overview of the project structure and how to navigate through its components.

## Prerequisites:
- Java 11 or higher
- Git (for cloning the repository)
- Gradle (only for installation new wrapper version)

## Installation / Using Gradle:
Since this project uses Gradle as its build system, you can build the project by running the following command:
```bash
./gradlew build
```

To run the project, you can use the following command:
```bash
./gradlew run
```

To update the gradle wrapper, you can run the following command:
```bash
gradle ./gradlew wrapper --gradle-version latest
```
This will require that you have Gradle installed on your system.

## Structure:
The project is structured as follows:
- `src/jvmMain/kotlin`: Contains the backend code for the AdminGUI. This server is responsible for handling requests from the frontend and communicating with the TrustEngine.
- `src/jsMain/kotlin`: Contains the frontend code for the AdminGUI. This code is responsible for rendering the user interface and sending requests to the backend. It is written in Kotlin and uses React as the UI library.
- `src/commonMain/kotlin`: Contains shared code that can be used by both the frontend and backend. This code functions as a bridge between the two parts of the project and allows common data definitions to be shared.

The backend code is written in Kotlin and uses Ktor as the web server framework. The frontend code is also written in Kotlin and uses React as the UI library. To add dependencies and update versions, you can modify the `build.gradle.kts` file.

## Setup:
The AdminGUI works tangentially with the TrustEngine and EdgeNode projects so generally the best way to run the AdminGUI is with the `StartServers.bat` script in the root directory of the project. This script will start the TrustEngine, EdgeNode, and AdminGUI servers.

If you want to run the AdminGUI server individually, you can do so by running the following command:
```bash
./gradlew run
```
This will start the AdminGUI backend server which handles requests from the frontend. and serving the frontend.



