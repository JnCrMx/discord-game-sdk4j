#!/bin/sh

mkdir -p src/main/resources/native/linux/amd64
mkdir -p src/main/resources/native/windows/amd64
mkdir -p src/main/resources/native/windows/x86

version=$(git describe --tags --abbrev=0)

curl -L -o src/main/resources/native/windows/amd64/discord_game_sdk_jni.dll \
	https://github.com/JnCrMx/discord-game-sdk4j/releases/download/$version/windows-amd64-discord_game_sdk_jni.dll
curl -L -o src/main/resources/native/windows/x86/discord_game_sdk_jni.dll \
	https://github.com/JnCrMx/discord-game-sdk4j/releases/download/$version/windows-x86-discord_game_sdk_jni.dll
curl -L -o src/main/resources/native/linux/amd64/libdiscord_game_sdk_jni.so \
	https://github.com/JnCrMx/discord-game-sdk4j/releases/download/$version/linux-amd64-libdiscord_game_sdk_jni.so
