#include <discord_game_sdk.h>

#include "de_jcm_discordgamesdk_activity_Activity.h"

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_activity_ActivitySecrets_setMatchSecret(JNIEnv *env, jobject object, jlong pointer, jstring secret)
{
	struct DiscordActivitySecrets *secrets = (struct DiscordActivitySecrets*) pointer;
	
	const char *nativeString = (*env)->GetStringUTFChars(env, secret, 0);
	strcpy(secrets->match, nativeString);
	(*env)->ReleaseStringUTFChars(env, secret, nativeString);
}

JNIEXPORT jstring JNICALL Java_de_jcm_discordgamesdk_activity_ActivitySecrets_getMatchSecret(JNIEnv *env, jobject object, jlong pointer)
{
	struct DiscordActivitySecrets *secrets = (struct DiscordActivitySecrets*) pointer;
	
	return (*env)->NewStringUTF(env, secrets->match);
}


JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_activity_ActivitySecrets_setJoinSecret(JNIEnv *env, jobject object, jlong pointer, jstring secret)
{
	struct DiscordActivitySecrets *secrets = (struct DiscordActivitySecrets*) pointer;
	
	const char *nativeString = (*env)->GetStringUTFChars(env, secret, 0);
	strcpy(secrets->join, nativeString);
	(*env)->ReleaseStringUTFChars(env, secret, nativeString);
}

JNIEXPORT jstring JNICALL Java_de_jcm_discordgamesdk_activity_ActivitySecrets_getJoinSecret(JNIEnv *env, jobject object, jlong pointer)
{
	struct DiscordActivitySecrets *secrets = (struct DiscordActivitySecrets*) pointer;
	
	return (*env)->NewStringUTF(env, secrets->join);
}

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_activity_ActivitySecrets_setSpectateSecret(JNIEnv *env, jobject object, jlong pointer, jstring secret)
{
	struct DiscordActivitySecrets *secrets = (struct DiscordActivitySecrets*) pointer;
	
	const char *nativeString = (*env)->GetStringUTFChars(env, secret, 0);
	strcpy(secrets->spectate, nativeString);
	(*env)->ReleaseStringUTFChars(env, secret, nativeString);
}

JNIEXPORT jstring JNICALL Java_de_jcm_discordgamesdk_activity_ActivitySecrets_getSpectateSecret(JNIEnv *env, jobject object, jlong pointer)
{
	struct DiscordActivitySecrets *secrets = (struct DiscordActivitySecrets*) pointer;
	
	return (*env)->NewStringUTF(env, secrets->spectate);
}