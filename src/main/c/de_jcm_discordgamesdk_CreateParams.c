#include <stdlib.h>
#include <discord_game_sdk.h>

#include "de_jcm_discordgamesdk_CreateParams.h"

JNIEXPORT jlong JNICALL Java_de_jcm_discordgamesdk_CreateParams_allocate(JNIEnv *env, jobject object)
{
	struct DiscordCreateParams *params = malloc(sizeof(struct DiscordCreateParams));
	DiscordCreateParamsSetDefault(params);
	return (uint64_t) params;
}

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_CreateParams_free(JNIEnv *env, jobject object, jlong pointer)
{
	free((void*)pointer);
}

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_CreateParams_setClientID(JNIEnv *env, jobject object, jlong pointer, jlong client_id)
{
	struct DiscordCreateParams *params = (struct DiscordCreateParams*) pointer;
	params->client_id = client_id;
}

JNIEXPORT jlong JNICALL Java_de_jcm_discordgamesdk_CreateParams_getClientID(JNIEnv *env, jobject object, jlong pointer)
{
	struct DiscordCreateParams *params = (struct DiscordCreateParams*) pointer;
	return params->client_id;
}

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_CreateParams_setFlags(JNIEnv *env, jobject object, jlong pointer, jlong flags)
{
	struct DiscordCreateParams *params = (struct DiscordCreateParams*) pointer;
	params->flags = flags;
}

JNIEXPORT jlong JNICALL Java_de_jcm_discordgamesdk_CreateParams_getFlags(JNIEnv *env, jobject object, jlong pointer)
{
	struct DiscordCreateParams *params = (struct DiscordCreateParams*) pointer;
	return params->flags;
}

JNIEXPORT jlong JNICALL Java_de_jcm_discordgamesdk_CreateParams_getDefaultFlags(JNIEnv *env, jclass clazz)
{
	return DiscordCreateFlags_Default;
}
