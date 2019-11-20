#!/usr/bin/env bash
java -version


rm -rf out

./gradlew clean

./gradlew :android_framework:base:idlGen --stacktrace
./gradlew :android_framework:base:build --stacktrace

./gradlew :android_framework:services:build --stacktrace

./gradlew :app:assembleRelease --stacktrace
