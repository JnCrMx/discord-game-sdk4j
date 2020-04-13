#include <stdlib.h>

#ifdef linux
#include <dlfcn.h>
#endif

#include <discord_game_sdk.h>

#include "de_jcm_discordgamesdk_Core.h"

void* handle = NULL;

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_Core_initDiscordNative(JNIEnv *env, jclass clazz, jstring path)
{
#ifdef linux
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
#endif
}

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_Core_create(JNIEnv *env, jobject object, jlong param_pointer)
{
	struct DiscordCreateParams *params = (struct DiscordCreateParams*) param_pointer;
	
	struct IDiscordCore* core;
	enum EDiscordResult result;
#ifdef linux
	dlerror(); // clear old error
	enum EDiscordResult (*create)(int, struct DiscordCreateParams*, struct IDiscordCore**) = dlsym(handle, "DiscordCreate");
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
	result = create(DISCORD_VERSION, params, &core);
#elif defined _WIN32
	result = DiscordCreate(DISCORD_VERSION, params, &core);
#endif

	if(result == DiscordResult_Ok) // if everything went well, return the pointer
	{
		jclass Long_class = (*env)->FindClass(env, "java/lang/Long");
		jmethodID valueOf_method = (*env)->GetStaticMethodID(env, Long_class, "valueOf", "(J)Ljava/lang/Long;");
		jobject return_object = (*env)->CallStaticObjectMethod(env, Long_class, valueOf_method, (uint64_t)core);

		return return_object;
	}
	else // otherwise return the result
	{
		jclass result_clazz = (*env)->FindClass(env, "de/jcm/discordgamesdk/Result");
		jmethodID values_method = (*env)->GetStaticMethodID(env, result_clazz, "values", "()[Lde/jcm/discordgamesdk/Result;");
		jobjectArray values = (jobjectArray) (*env)->CallStaticObjectMethod(env, result_clazz, values_method);
		jobject result_object = (*env)->GetObjectArrayElement(env, values, result);

		return result_object;
	}
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
