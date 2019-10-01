#!/usr/bin/env bash
 openssl enc -aes-256-cbc -d -K $K -iv $IV -in android-key-enc -out android.jks