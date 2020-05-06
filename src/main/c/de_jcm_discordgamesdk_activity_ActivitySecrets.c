#include <string.h>
#include <discord_game_sdk.h>

#include "de_jcm_discordgamesdk_activity_ActivitySecrets.h"

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_activity_ActivitySecrets_setMatchSecret(JNIEnv *env, jobject object, jlong pointer, jstring secret)
{
	if(!pointer)
	{
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/NullPointerException"), "reference is null");
		return;
	}
	
	struct DiscordActivitySecrets *secrets = (struct DiscordActivitySecrets*) pointer;
	
	const char *nativeString = (*env)->GetStringUTFChars(env, secret, 0);
	strncpy(secrets->match, nativeString, 127);
	secrets->match[127] = 0;
	(*env)->ReleaseStringUTFChars(env, secret, nativeString);
}

JNIEXPORT jstring JNICALL Java_de_jcm_discordgamesdk_activity_ActivitySecrets_getMatchSecret(JNIEnv *env, jobject object, jlong pointer)
{
	if(!pointer)
	{
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/NullPointerException"), "reference is null");
		return NULL;
	}
	
	struct DiscordActivitySecrets *secrets = (struct DiscordActivitySecrets*) pointer;
	
	return (*env)->NewStringUTF(env, secrets->match);
}


JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_activity_ActivitySecrets_setJoinSecret(JNIEnv *env, jobject object, jlong pointer, jstring secret)
{
	if(!pointer)
	{
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/NullPointerException"), "reference is null");
		return;
	}
	
	struct DiscordActivitySecrets *secrets = (struct DiscordActivitySecrets*) pointer;
	
	const char *nativeString = (*env)->GetStringUTFChars(env, secret, 0);
	strncpy(secrets->join, nativeString, 127);
	secrets->join[127] = 0;
	(*env)->ReleaseStringUTFChars(env, secret, nativeString);
}

JNIEXPORT jstring JNICALL Java_de_jcm_discordgamesdk_activity_ActivitySecrets_getJoinSecret(JNIEnv *env, jobject object, jlong pointer)
{
	if(!pointer)
	{
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/NullPointerException"), "reference is null");
		return NULL;
	}
	
	struct DiscordActivitySecrets *secrets = (struct DiscordActivitySecrets*) pointer;
	
	return (*env)->NewStringUTF(env, secrets->join);
}

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_activity_ActivitySecrets_setSpectateSecret(JNIEnv *env, jobject object, jlong pointer, jstring secret)
{
	if(!pointer)
	{
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/NullPointerException"), "reference is null");
		return;
	}
	
	struct DiscordActivitySecrets *secrets = (struct DiscordActivitySecrets*) pointer;
	
	const char *nativeString = (*env)->GetStringUTFChars(env, secret, 0);
	strncpy(secrets->spectate, nativeString, 127);
	secrets->spectate[127] = 0;
	(*env)->ReleaseStringUTFChars(env, secret, nativeString);
}

JNIEXPORT jstring JNICALL Java_de_jcm_discordgamesdk_activity_ActivitySecrets_getSpectateSecret(JNIEnv *env, jobject object, jlong pointer)
{
	if(!pointer)
	{
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/NullPointerException"), "reference is null");
		return NULL;
	}
	
	struct DiscordActivitySecrets *secrets = (struct DiscordActivitySecrets*) pointer;
	
	return (*env)->NewStringUTF(env, secrets->spectate);
}