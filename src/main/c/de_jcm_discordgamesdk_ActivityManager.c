#include <stdlib.h>
#include <discord_game_sdk.h>

#include "de_jcm_discordgamesdk_ActivityManager.h"
#include "Callback.h"

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_ActivityManager_updateActivity(JNIEnv *env, jobject object, jlong pointer, jlong activity_pointer, jobject callback)
{
	struct DiscordActivity *activity = (struct DiscordActivity*) activity_pointer;
	struct IDiscordActivityManager *activity_manager = (struct IDiscordActivityManager*) pointer;
	
	struct CallbackData* cbd = malloc(sizeof(struct CallbackData));
	prepare_callback_data(env, callback, cbd);
	
	activity_manager->update_activity(activity_manager, activity, cbd, simple_callback);
}

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_ActivityManager_clearActivity(JNIEnv *env, jobject object, jlong pointer, jobject callback)
{
	struct IDiscordActivityManager *activity_manager = (struct IDiscordActivityManager*) pointer;
	
	struct CallbackData* cbd = malloc(sizeof(struct CallbackData));
	prepare_callback_data(env, callback, cbd);
	
	activity_manager->clear_activity(activity_manager, cbd, simple_callback);
}
