#include <stdlib.h>
#include <discord_game_sdk.h>

#include "de_jcm_discordgamesdk_ActivityManager.h"
#include "Callback.h"

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_ActivityManager_registerCommand(JNIEnv *env, jobject object, jlong pointer, jstring command)
{
	struct IDiscordActivityManager *activity_manager = (struct IDiscordActivityManager*) pointer;
	
	const char *nativeString = (*env)->GetStringUTFChars(env, command, 0);
	enum EDiscordResult result = activity_manager->register_command(activity_manager, nativeString);
	(*env)->ReleaseStringUTFChars(env, command, nativeString);
	
	jclass result_clazz = (*env)->FindClass(env, "de/jcm/discordgamesdk/Result");
	jmethodID values_method = (*env)->GetStaticMethodID(env, result_clazz, "values", "()[Lde/jcm/discordgamesdk/Result;");
	jobjectArray values = (jobjectArray) (*env)->CallStaticObjectMethod(env, result_clazz, values_method);
	jobject result_object = (*env)->GetObjectArrayElement(env, values, result);

	return result_object;
}

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_ActivityManager_registerSteam(JNIEnv *env, jobject object, jlong pointer, jint steamId)
{
	struct IDiscordActivityManager *activity_manager = (struct IDiscordActivityManager*) pointer;
	
	enum EDiscordResult result = activity_manager->register_steam(activity_manager, steamId);
	
	jclass result_clazz = (*env)->FindClass(env, "de/jcm/discordgamesdk/Result");
	jmethodID values_method = (*env)->GetStaticMethodID(env, result_clazz, "values", "()[Lde/jcm/discordgamesdk/Result;");
	jobjectArray values = (jobjectArray) (*env)->CallStaticObjectMethod(env, result_clazz, values_method);
	jobject result_object = (*env)->GetObjectArrayElement(env, values, result);

	return result_object;
}

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

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_ActivityManager_sendRequestReply(JNIEnv *env, jobject object, jlong pointer, jlong userId, jint reply, jobject callback)
{
	struct IDiscordActivityManager *activity_manager = (struct IDiscordActivityManager*) pointer;
	
	struct CallbackData* cbd = malloc(sizeof(struct CallbackData));
	prepare_callback_data(env, callback, cbd);
	
	activity_manager->send_request_reply(activity_manager, userId, reply, cbd, simple_callback);
}

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_ActivityManager_sendInvite(JNIEnv *env, jobject object, jlong pointer, jlong userId, jint type, jstring content, jobject callback)
{
	struct IDiscordActivityManager *activity_manager = (struct IDiscordActivityManager*) pointer;
	
	struct CallbackData* cbd = malloc(sizeof(struct CallbackData));
	prepare_callback_data(env, callback, cbd);
	
	const char *native_content = (*env)->GetStringUTFChars(env, content, 0);
	activity_manager->send_invite(activity_manager, userId, type, native_content, cbd, simple_callback);
	(*env)->ReleaseStringUTFChars(env, content, native_content);
}

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_ActivityManager_acceptRequest(JNIEnv *env, jobject object, jlong pointer, jlong userId, jobject callback)
{
	struct IDiscordActivityManager *activity_manager = (struct IDiscordActivityManager*) pointer;
	
	struct CallbackData* cbd = malloc(sizeof(struct CallbackData));
	prepare_callback_data(env, callback, cbd);
	
	activity_manager->accept_invite(activity_manager, userId, cbd, simple_callback);
}
