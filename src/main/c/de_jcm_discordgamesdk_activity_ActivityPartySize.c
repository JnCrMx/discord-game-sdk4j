#include <stdlib.h>
#include <discord_game_sdk.h>

#include "de_jcm_discordgamesdk_activity_ActivityPartySize.h"

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_activity_ActivityPartySize_setCurrentSize(JNIEnv *env, jobject object, jlong pointer, jint current_size)
{
	struct DiscordPartySize *size = (struct DiscordPartySize*) pointer;
	size->current_size = current_size;
}

JNIEXPORT jint JNICALL Java_de_jcm_discordgamesdk_activity_ActivityPartySize_getCurrentSize(JNIEnv *env, jobject object, jlong pointer)
{
	struct DiscordPartySize *size = (struct DiscordPartySize*) pointer;
	return size->current_size;
}

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_activity_ActivityPartySize_setMaxSize(JNIEnv *env, jobject object, jlong pointer, jint max_size)
{
	struct DiscordPartySize *size = (struct DiscordPartySize*) pointer;
	size->max_size = max_size;
}

JNIEXPORT jint JNICALL Java_de_jcm_discordgamesdk_activity_ActivityPartySize_getMaxSize(JNIEnv *env, jobject object, jlong pointer)
{
	struct DiscordPartySize *size = (struct DiscordPartySize*) pointer;
	return size->max_size;
}