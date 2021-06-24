#!/bin/bash

git describe --tags --exact-match > /dev/null 2>/dev/null
if [ $? -ne 0 ]; then
	 echo "`tput setaf 1``tput bold`no Git tag found for current commit`tput sgr0`"
	 exit 2
fi

gitversion=$(git describe --tags --abbrev=0 | tr -d "v")
mvnversion=$(grep -P '<version>[\d\.]+<\/version>' pom.xml | head -n1 | sed 's/<version>\([0-9\.]*\)<\/version>/\1/g' | tr -d "\t")

if [ "$gitversion" != "$mvnversion" ]; then
	echo "`tput setaf 1``tput bold`Git tag $gitversion does not match Maven version $mvnversion`tput sgr0`"
	exit 2
fi

mvn -DskipTests=true clean package

check_and_copy ()
{
	src=$1
	dst=$2
	
	stat $src > /dev/null 2>/dev/null
	if [ $? -ne 0 ]; then
		echo "`tput setaf 1``tput bold`file not found: $src`tput sgr0`"
		exit 2
	fi
	
	cp $src $dst
	if [ $? -ne 0 ]; then
		echo "`tput setaf 1``tput bold`could not copy $src to $dst`tput sgr0`"
		exit 2
	fi
}

release=target/release

mkdir -p $release

# JARs
check_and_copy target/discord-game-sdk4j-0.5.3.jar $release/discord-game-sdk4j-0.5.3.jar
check_and_copy target/discord-game-sdk4j-0.5.3-javadoc.jar $release/discord-game-sdk4j-0.5.3-javadoc.jar
check_and_copy target/discord-game-sdk4j-0.5.3-sources.jar $release/discord-game-sdk4j-0.5.3-sources.jar

# Windows
check_and_copy target/classes/native/windows/amd64/discord_game_sdk_jni.dll $release/windows-amd64-discord_game_sdk_jni.dll
check_and_copy target/classes/native/windows/x86/discord_game_sdk_jni.dll $release/windows-x86-discord_game_sdk_jni.dll

# Linux
check_and_copy target/classes/native/linux/amd64/libdiscord_game_sdk_jni.so $release/linux-amd64-libdiscord_game_sdk_jni.so

# macOS
check_and_copy target/classes/native/macos/amd64/libdiscord_game_sdk_jni.dylib $release/macos-amd64-libdiscord_game_sdk_jni.dylib

echo "`tput setaf 2``tput bold`Success! Release artifacts for version $gitversion can now be found in $release`tput sgr0`"
