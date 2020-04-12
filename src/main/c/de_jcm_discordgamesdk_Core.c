#include <stdlib.h>
#include <discord_game_sdk.h>

#include "de_jcm_discordgamesdk_Core.h"

JNIEXPORT jlong JNICALL Java_de_jcm_discordgamesdk_Core_create(JNIEnv *env, jobject object, jlong param_pointer)
{
	struct DiscordCreateParams *params = (struct DiscordCreateParams*) param_pointer;
	
	struct IDiscordCore* core;
	
	DiscordCreate(DISCORD_VERSION, params, &core);
	
	return (uint64_t) core;
}

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_Core_destroy(JNIEnv *env, jobject object, jlong pointer)
{
	struct IDiscordCore* core = (struct IDiscordCore*) pointer;
	core->destroy(core);
}

JNIEXPORT jlong JNICALL Java_de_jcm_discordgamesdk_Core_getActivityManager(JNIEnv *env, jobject object, jlong pointer)
{
	struct IDiscordCore* core = (struct IDiscordCore*) pointer;
	return (uint64_t) core->get_activity_manager(core);
}

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_Core_runCallbacks(JNIEnv *env, jobject object, jlong pointer)
{
	struct IDiscordCore* core = (struct IDiscordCore*) pointer;
	core->run_callbacks(core);
}
