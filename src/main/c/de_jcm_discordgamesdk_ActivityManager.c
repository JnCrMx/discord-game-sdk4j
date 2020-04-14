#include <stdlib.h>
#include <discord_game_sdk.h>

#include "de_jcm_discordgamesdk_CreateParams.h"

struct CallbackData {
	JavaVM* jvm;
	jobject callback;
};

void activity_callback(void* data, enum EDiscordResult result)
{
	struct CallbackData* cbd = (struct CallbackData*)data;
	
	JNIEnv* env;
	JavaVMAttachArgs args;
	args.version = JNI_VERSION_1_6;
	args.name = NULL;
	args.group = NULL;
	(*(cbd->jvm))->AttachCurrentThread(cbd->jvm, (void**)&env, &args);
	
	jclass result_clazz = (*env)->FindClass(env, "de/jcm/discordgamesdk/Result");
	jmethodID values_method = (*env)->GetStaticMethodID(env, result_clazz, "values", "()[Lde/jcm/discordgamesdk/Result;");
	jobjectArray values = (jobjectArray) (*env)->CallStaticObjectMethod(env, result_clazz, values_method);
	jobject result_object = (*env)->GetObjectArrayElement(env, values, result);
	
	jclass clazz = (*env)->GetObjectClass(env, cbd->callback);
	
	jmethodID method = (*env)->GetMethodID(env, clazz, "accept", "(Ljava/lang/Object;)V");
	(*env)->CallVoidMethod(env, cbd->callback, method, result_object);
	
	(*env)->DeleteGlobalRef(env, cbd->callback);
	
	(*(cbd->jvm))->DetachCurrentThread(cbd->jvm);
}

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_ActivityManager_updateActivity(JNIEnv *env, jobject object, jlong pointer, jlong activity_pointer, jobject callback)
{
	struct DiscordActivity *activity = (struct DiscordActivity*) activity_pointer;
	struct IDiscordActivityManager *activity_manager = (struct IDiscordActivityManager*) pointer;
	
	JavaVM* jvm = malloc(sizeof(jvm));
	(*env)->GetJavaVM(env, &jvm);
	
	struct CallbackData* cbd = malloc(sizeof(struct CallbackData));
	cbd->jvm = jvm;
	cbd->callback = (*env)->NewGlobalRef(env, callback);
	
	activity_manager->update_activity(activity_manager, activity, cbd, activity_callback);
}

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_ActivityManager_clearActivity(JNIEnv *env, jobject object, jlong pointer, jobject callback)
{
	struct IDiscordActivityManager *activity_manager = (struct IDiscordActivityManager*) pointer;
	
	JavaVM* jvm = malloc(sizeof(jvm));
	(*env)->GetJavaVM(env, &jvm);
	
	struct CallbackData* cbd = malloc(sizeof(struct CallbackData));
	cbd->jvm = jvm;
	cbd->callback = (*env)->NewGlobalRef(env, callback);
	
	activity_manager->clear_activity(activity_manager, cbd, activity_callback);
}
