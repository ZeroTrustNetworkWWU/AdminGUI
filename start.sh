#!/bin/bash

# Ensure gradlew has execute permissions
chmod +x gradlew

./gradlew build

if [ $? -ne 0 ]; then
 echo "Failed to build the Gradle project."
 exit 1
fi

./gradlew run
