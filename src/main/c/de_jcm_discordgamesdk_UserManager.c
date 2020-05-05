#include <stdlib.h>
#include <discord_game_sdk.h>

#include "de_jcm_discordgamesdk_UserManager.h"
#include "Callback.h"

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_UserManager_getCurrentUser(JNIEnv *env, jobject object, jlong pointer)
{
	if(!pointer)
	{
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/NullPointerException"), "reference is null");
		return NULL;
	}
	
	struct IDiscordUserManager *user_manager = (struct IDiscordUserManager*) pointer;
	
	struct DiscordUser user;
	enum EDiscordResult result = user_manager->get_current_user(user_manager, &user);
	
	if(result == DiscordResult_Ok) // if everything went well, return the user
	{
		jclass user_class = (*env)->FindClass(env, "de/jcm/discordgamesdk/user/DiscordUser");
		jmethodID user_constructor = (*env)->GetMethodID(env, user_class, "<init>", 
			"(JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)V");
		jobject user_object = (*env)->NewObject(env, user_class, user_constructor,
			user.id,
			(*env)->NewStringUTF(env, user.username),
			(*env)->NewStringUTF(env, user.discriminator),
			(*env)->NewStringUTF(env, user.avatar),
			user.bot);
		return user_object;
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

void user_callback(void* data, enum EDiscordResult result, struct DiscordUser* user)
{
	struct CallbackData* cbd = (struct CallbackData*)data;
	
	JNIEnv* env;
	JavaVMAttachArgs args;
	args.version = JNI_VERSION_1_6;
	args.name = NULL;
	args.group = NULL;
	(*(cbd->jvm))->AttachCurrentThread(cbd->jvm, (void**)&env, &args);
	
	// prepare result
	jclass result_clazz = (*env)->FindClass(env, "de/jcm/discordgamesdk/Result");
	jmethodID values_method = (*env)->GetStaticMethodID(env, result_clazz, "values", "()[Lde/jcm/discordgamesdk/Result;");
	jobjectArray values = (jobjectArray) (*env)->CallStaticObjectMethod(env, result_clazz, values_method);
	jobject result_object = (*env)->GetObjectArrayElement(env, values, result);
	
	// prepare user
	jobject user_object = NULL;
	if(user) // not sure if we can get NULL as an argument
	{
		jclass user_class = (*env)->FindClass(env, "de/jcm/discordgamesdk/user/DiscordUser");
		jmethodID user_constructor = (*env)->GetMethodID(env, user_class, "<init>", 
			"(JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)V");
		user_object = (*env)->NewObject(env, user_class, user_constructor,
			user->id,
			(*env)->NewStringUTF(env, user->username),
			(*env)->NewStringUTF(env, user->discriminator),
			(*env)->NewStringUTF(env, user->avatar),
			user->bot);
	}
	
	// call the callback
	jclass clazz = (*env)->GetObjectClass(env, cbd->callback);
	
	jmethodID method = (*env)->GetMethodID(env, clazz, "accept", "(Ljava/lang/Object;Ljava/lang/Object;)V");
	(*env)->CallVoidMethod(env, cbd->callback, method, result_object, user_object);
	
	(*env)->DeleteGlobalRef(env, cbd->callback);
	(*(cbd->jvm))->DetachCurrentThread(cbd->jvm);
}

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_UserManager_getUser(JNIEnv *env, jobject object, jlong pointer, jlong userId, jobject callback)
{
	if(!pointer)
	{
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/NullPointerException"), "reference is null");
		return;
	}
	
	struct IDiscordUserManager *user_manager = (struct IDiscordUserManager*) pointer;
	
	struct CallbackData* cbd = malloc(sizeof(struct CallbackData));
	prepare_callback_data(env, callback, cbd);
	
	user_manager->get_user(user_manager, userId, cbd, user_callback);
}

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_UserManager_getCurrentUserPremiumType(JNIEnv *env, jobject object, jlong pointer)
{
	if(!pointer)
	{
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/NullPointerException"), "reference is null");
		return NULL;
	}
	
	struct IDiscordUserManager *user_manager = (struct IDiscordUserManager*) pointer;
	
	enum EDiscordPremiumType premium_type;
	enum EDiscordResult result = user_manager->get_current_user_premium_type(user_manager, &premium_type);
	
	if(result == DiscordResult_Ok) // if everything went well, return the index of the premium type
	{
		jclass Integer_class = (*env)->FindClass(env, "java/lang/Integer");
		jmethodID valueOf_method = (*env)->GetStaticMethodID(env, Integer_class, "valueOf", "(I)Ljava/lang/Integer;");
		jobject return_object = (*env)->CallStaticObjectMethod(env, Integer_class, valueOf_method, premium_type);

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

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_UserManager_currentUserHasFlag(JNIEnv *env, jobject object, jlong pointer, jint flag)
{
	if(!pointer)
	{
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/NullPointerException"), "reference is null");
		return NULL;
	}
	
	struct IDiscordUserManager *user_manager = (struct IDiscordUserManager*) pointer;
	
	bool has_flag;
	enum EDiscordResult result = user_manager->current_user_has_flag(user_manager, flag, &has_flag);
	
	if(result == DiscordResult_Ok) // if everything went well, return if the user has the flag set
	{
		jclass Boolean_class = (*env)->FindClass(env, "java/lang/Boolean");
		jmethodID valueOf_method = (*env)->GetStaticMethodID(env, Boolean_class, "valueOf", "(Z)Ljava/lang/Boolean;");
		jobject return_object = (*env)->CallStaticObjectMethod(env, Boolean_class, valueOf_method, has_flag);

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
