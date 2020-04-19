#include <stdlib.h>
#include <discord_game_sdk.h>

#include "de_jcm_discordgamesdk_activity_Activity.h"

JNIEXPORT jlong JNICALL Java_de_jcm_discordgamesdk_activity_Activity_allocate(JNIEnv *env, jobject object)
{
	struct DiscordActivity *activity = malloc(sizeof(struct DiscordActivity));
	memset(activity, 0, sizeof(struct DiscordActivity));
	return (uint64_t) activity;
}

JNIEXPORT jlong JNICALL Java_de_jcm_discordgamesdk_activity_Activity_getApplicationId(JNIEnv *env, jobject object, jlong pointer)
{
	struct DiscordActivity *activity = (struct DiscordActivity*) pointer;
	
	return activity->application_id;
}

JNIEXPORT jstring JNICALL Java_de_jcm_discordgamesdk_activity_Activity_getName(JNIEnv *env, jobject object, jlong pointer)
{
	struct DiscordActivity *activity = (struct DiscordActivity*) pointer;
	
	return (*env)->NewStringUTF(env, activity->name);
}

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_activity_Activity_setState(JNIEnv *env, jobject object, jlong pointer, jstring state)
{
	struct DiscordActivity *activity = (struct DiscordActivity*) pointer;
	
	const char *nativeString = (*env)->GetStringUTFChars(env, state, 0);
	strcpy(activity->state, nativeString);
	(*env)->ReleaseStringUTFChars(env, state, nativeString);
}

JNIEXPORT jstring JNICALL Java_de_jcm_discordgamesdk_activity_Activity_getState(JNIEnv *env, jobject object, jlong pointer)
{
	struct DiscordActivity *activity = (struct DiscordActivity*) pointer;
	
	return (*env)->NewStringUTF(env, activity->state);
}

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_activity_Activity_setDetails(JNIEnv *env, jobject object, jlong pointer, jstring details)
{
	struct DiscordActivity *activity = (struct DiscordActivity*) pointer;
	
	const char *nativeString = (*env)->GetStringUTFChars(env, details, 0);
	strcpy(activity->details, nativeString);
	(*env)->ReleaseStringUTFChars(env, details, nativeString);
}

JNIEXPORT jstring JNICALL Java_de_jcm_discordgamesdk_activity_Activity_getDetails(JNIEnv *env, jobject object, jlong pointer)
{
	struct DiscordActivity *activity = (struct DiscordActivity*) pointer;
	
	return (*env)->NewStringUTF(env, activity->details);
}

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_activity_Activity_setType(JNIEnv *env, jobject object, jlong pointer, jint type)
{
	struct DiscordActivity *activity = (struct DiscordActivity*) pointer;
	
	activity->type=type;
}

JNIEXPORT jint JNICALL Java_de_jcm_discordgamesdk_activity_Activity_getType(JNIEnv *env, jobject object, jlong pointer)
{
	struct DiscordActivity *activity = (struct DiscordActivity*) pointer;
	
	return activity->type;
}

JNIEXPORT jlong JNICALL Java_de_jcm_discordgamesdk_activity_Activity_getTimestamps(JNIEnv *env, jobject object, jlong pointer)
{
	struct DiscordActivity *activity = (struct DiscordActivity*) pointer;
	return (uint64_t) &(activity->timestamps);
}

JNIEXPORT jlong JNICALL Java_de_jcm_discordgamesdk_activity_Activity_getAssets(JNIEnv *env, jobject object, jlong pointer)
{
	struct DiscordActivity *activity = (struct DiscordActivity*) pointer;
	return (uint64_t) &(activity->assets);
}

JNIEXPORT jlong JNICALL Java_de_jcm_discordgamesdk_activity_Activity_getParty(JNIEnv *env, jobject object, jlong pointer)
{
	struct DiscordActivity *activity = (struct DiscordActivity*) pointer;
	return (uint64_t) &(activity->party);
}

JNIEXPORT jlong JNICALL Java_de_jcm_discordgamesdk_activity_Activity_getSecrets(JNIEnv *env, jobject object, jlong pointer)
{
	struct DiscordActivity *activity = (struct DiscordActivity*) pointer;
	return (uint64_t) &(activity->secrets);
}

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_activity_Activity_free(JNIEnv *env, jclass clazz, jlong pointer)
{
	free((void*)pointer);
}
