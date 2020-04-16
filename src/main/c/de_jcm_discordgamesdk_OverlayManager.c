#include <stdlib.h>
#include <discord_game_sdk.h>

#include "de_jcm_discordgamesdk_OverlayManager.h"
#include "Callback.h"

JNIEXPORT jboolean JNICALL Java_de_jcm_discordgamesdk_OverlayManager_isEnabled(JNIEnv *env, jobject object, jlong pointer)
{
	struct IDiscordOverlayManager *overlay_manager = (struct IDiscordOverlayManager*) pointer;
	
	bool enabled;
	overlay_manager->is_enabled(overlay_manager, &enabled);
	
	return enabled;
}

JNIEXPORT jboolean JNICALL Java_de_jcm_discordgamesdk_OverlayManager_isLocked(JNIEnv *env, jobject object, jlong pointer)
{
	struct IDiscordOverlayManager *overlay_manager = (struct IDiscordOverlayManager*) pointer;
	
	bool locked;
	overlay_manager->is_locked(overlay_manager, &locked);
	
	return locked;
}

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_OverlayManager_setLocked(JNIEnv *env, jobject object, jlong pointer, jboolean locked, jobject callback)
{
	struct IDiscordOverlayManager *overlay_manager = (struct IDiscordOverlayManager*) pointer;
	
	struct CallbackData* cbd = malloc(sizeof(struct CallbackData));
	prepare_callback_data(env, callback, cbd);
	
	overlay_manager->set_locked(overlay_manager, locked, cbd, simple_callback);
}

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_OverlayManager_openActivityInvite(JNIEnv *env, jobject object, jlong pointer, jint type, jobject callback)
{
	struct IDiscordOverlayManager *overlay_manager = (struct IDiscordOverlayManager*) pointer;
	
	struct CallbackData* cbd = malloc(sizeof(struct CallbackData));
	prepare_callback_data(env, callback, cbd);
	
	// enum DiscordActivityActionType starts with index 1, so add 1 to translate from "normal" enum
	overlay_manager->open_activity_invite(overlay_manager, type+1, cbd, simple_callback);
}

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_OverlayManager_openGuildInvite(JNIEnv *env, jobject object, jlong pointer, jstring code, jobject callback)
{
	struct IDiscordOverlayManager *overlay_manager = (struct IDiscordOverlayManager*) pointer;
	
	struct CallbackData* cbd = malloc(sizeof(struct CallbackData));
	prepare_callback_data(env, callback, cbd);
	
	const char *nativeString = (*env)->GetStringUTFChars(env, code, 0);
	overlay_manager->open_guild_invite(overlay_manager, nativeString, cbd, simple_callback);
	(*env)->ReleaseStringUTFChars(env, code, nativeString);
}

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_OverlayManager_openVoiceSettings(JNIEnv *env, jobject object, jlong pointer, jobject callback)
{
	struct IDiscordOverlayManager *overlay_manager = (struct IDiscordOverlayManager*) pointer;
	
	struct CallbackData* cbd = malloc(sizeof(struct CallbackData));
	prepare_callback_data(env, callback, cbd);
	
	overlay_manager->open_voice_settings(overlay_manager, cbd, simple_callback);
}
