#include <stdlib.h>
#include <dlfcn.h>
#include <discord_game_sdk.h>

#include "de_jcm_discordgamesdk_Core.h"

void* handle = NULL;

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_Core_initDiscordNative(JNIEnv *env, jclass clazz, jstring path)
{
	if(handle)
	{
		if(dlclose(handle) != 0) // close the handle if it already exists
		{
			(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/InternalError"), dlerror());
			return;
		}
		handle = NULL;
	}
	
	const char *nativeString = (*env)->GetStringUTFChars(env, path, 0);
	
	handle = dlopen(nativeString, RTLD_LAZY);
	
	(*env)->ReleaseStringUTFChars(env, path, nativeString);
	
	if(!handle)
	{
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/UnsatisfiedLinkError"), dlerror());
		return;
	}
}

JNIEXPORT jlong JNICALL Java_de_jcm_discordgamesdk_Core_create(JNIEnv *env, jobject object, jlong param_pointer)
{
	struct DiscordCreateParams *params = (struct DiscordCreateParams*) param_pointer;
	
	struct IDiscordCore* core;

	dlerror(); // clear old error
	void (*create)(int, struct DiscordCreateParams*, struct IDiscordCore**) = dlsym(handle, "DiscordCreate");
	char* error = dlerror(); // get new error
	if(error) // check for error
	{
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/UnsatisfiedLinkError"), error);
		/*
		 * The return value doesn't matter, because we actually throw an exception.
		 * But we need a return statement, due to the fact that ThrowNew doesn't end the method.
		 */
		return 0;
	}
	create(DISCORD_VERSION, params, &core);
	
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
