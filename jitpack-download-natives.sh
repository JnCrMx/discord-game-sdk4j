#!/bin/sh

version=$(git describe --tags --abbrev=0)

# Windows
mkdir -p src/main/resources/native/windows/amd64
curl -L -o src/main/resources/native/windows/amd64/discord_game_sdk_jni.dll \
	https://github.com/JnCrMx/discord-game-sdk4j/releases/download/$version/windows-amd64-discord_game_sdk_jni.dll
mkdir -p src/main/resources/native/windows/x86
curl -L -o src/main/resources/native/windows/x86/discord_game_sdk_jni.dll \
	https://github.com/JnCrMx/discord-game-sdk4j/releases/download/$version/windows-x86-discord_game_sdk_jni.dll

# Linux
mkdir -p src/main/resources/native/linux/amd64
curl -L -o src/main/resources/native/linux/amd64/libdiscord_game_sdk_jni.so \
	https://github.com/JnCrMx/discord-game-sdk4j/releases/download/$version/linux-amd64-libdiscord_game_sdk_jni.so

# macOS
mkdir -p src/main/resources/native/macos/amd64
curl -L -o src/main/resources/native/macos/amd64/libdiscord_game_sdk_jni.dylib \
	https://github.com/JnCrMx/discord-game-sdk4j/releases/download/$version/macos-amd64-libdiscord_game_sdk_jni.dylib
