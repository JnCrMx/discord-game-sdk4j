cmake_minimum_required(VERSION 3.4.1)

find_package(JNI REQUIRED)
include_directories(${JNI_INCLUDE_DIRS})
include_directories(../../../discord_game_sdk/c)

add_library(discord_game_sdk_jni

			SHARED

			de_jcm_discordgamesdk_activity_Activity.c
			de_jcm_discordgamesdk_activity_ActivityAssets.c
			de_jcm_discordgamesdk_activity_ActivityParty.c
			de_jcm_discordgamesdk_activity_ActivityPartySize.c
			de_jcm_discordgamesdk_activity_ActivitySecrets.c
			de_jcm_discordgamesdk_activity_ActivityTimestamps.c
			de_jcm_discordgamesdk_ActivityManager.c
			de_jcm_discordgamesdk_CreateParams.c
			de_jcm_discordgamesdk_Core.c)
             
target_link_libraries(discord_game_sdk_jni dl)
