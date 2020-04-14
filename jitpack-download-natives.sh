#!/bin/sh

mkdir src/main/resources/

version=$(git describe --tags --abbrev=0)

curl -L -o src/main/resources/discord_game_sdk_jni.dll \
	https://github.com/JnCrMx/discord-game-sdk4j/releases/download/$version/discord_game_sdk_jni.dll
curl -L -o src/main/resources/libdiscord_game_sdk_jni.so \
	https://github.com/JnCrMx/discord-game-sdk4j/releases/download/$version/libdiscord_game_sdk_jni.so
