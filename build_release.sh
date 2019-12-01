#!/usr/bin/env bash
java -version

rm -rf out

./gradlew clean && ./gradlew :module_locker:assemblePrcRelease && ./gradlew :app:assemblePrcRelease
