#include <discord_game_sdk.h>

#include "de_jcm_discordgamesdk_activity_ActivityPartySize.h"

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_activity_ActivityPartySize_setCurrentSize(JNIEnv *env, jobject object, jlong pointer, jint current_size)
{
	if(!pointer)
	{
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/NullPointerException"), "reference is null");
		return;
	}
	
	struct DiscordPartySize *size = (struct DiscordPartySize*) pointer;
	size->current_size = current_size;
}

JNIEXPORT jint JNICALL Java_de_jcm_discordgamesdk_activity_ActivityPartySize_getCurrentSize(JNIEnv *env, jobject object, jlong pointer)
{
	if(!pointer)
	{
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/NullPointerException"), "reference is null");
		return 0;
	}
	
	struct DiscordPartySize *size = (struct DiscordPartySize*) pointer;
	return size->current_size;
}

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_activity_ActivityPartySize_setMaxSize(JNIEnv *env, jobject object, jlong pointer, jint max_size)
{
	if(!pointer)
	{
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/NullPointerException"), "reference is null");
		return;
	}
	
	struct DiscordPartySize *size = (struct DiscordPartySize*) pointer;
	size->max_size = max_size;
}

JNIEXPORT jint JNICALL Java_de_jcm_discordgamesdk_activity_ActivityPartySize_getMaxSize(JNIEnv *env, jobject object, jlong pointer)
{
	if(!pointer)
	{
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/NullPointerException"), "reference is null");
		return 0;
	}
	
	struct DiscordPartySize *size = (struct DiscordPartySize*) pointer;
	return size->max_size;
}