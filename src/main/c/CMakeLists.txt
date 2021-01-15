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
			de_jcm_discordgamesdk_Core.c
			de_jcm_discordgamesdk_ImageManager.c
			de_jcm_discordgamesdk_lobby_LobbyTransaction.c
			de_jcm_discordgamesdk_lobby_LobbyMemberTransaction.c
			de_jcm_discordgamesdk_lobby_LobbySearchQuery.c
			de_jcm_discordgamesdk_LobbyManager.c
			de_jcm_discordgamesdk_NetworkManager.c
			de_jcm_discordgamesdk_OverlayManager.c
			de_jcm_discordgamesdk_RelationshipManager.c
			de_jcm_discordgamesdk_UserManager.c
			de_jcm_discordgamesdk_VoiceManager.c

			Callback.c
			EventHandler.c)

if(UNIX)
	target_link_libraries(discord_game_sdk_jni dl)
endif(UNIX)

if(MSVC)
	add_library(discord_game_sdk SHARED IMPORTED)
	set_target_properties(discord_game_sdk
		PROPERTIES
		IMPORTED_IMPLIB
		${CMAKE_SOURCE_DIR}/../../../discord_game_sdk/lib/x86_64/discord_game_sdk.dll.lib)
	target_link_libraries(discord_game_sdk_jni discord_game_sdk)
endif(MSVC)
