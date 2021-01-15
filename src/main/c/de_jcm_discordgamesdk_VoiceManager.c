#include <stdlib.h>
#include <string.h>
#include <discord_game_sdk.h>

#include "de_jcm_discordgamesdk_VoiceManager.h"
#include "Callback.h"

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_VoiceManager_getInputMode
  (JNIEnv *env, jobject object, jlong pointer)
{
	struct IDiscordVoiceManager *voice_manager = (struct IDiscordVoiceManager*) pointer;

	struct DiscordInputMode input_mode;
	enum EDiscordResult result = voice_manager->get_input_mode(voice_manager, &input_mode);

	if(result == DiscordResult_Ok) // if everything went well, return the input mode
	{
		jclass mode_class = (*env)->FindClass(env, "de/jcm/discordgamesdk/voice/VoiceInputMode");
		jmethodID mode_constructor = (*env)->GetMethodID(env, mode_class, "<init>", "(ILjava/lang/String;)V");
		jobject mode_object = (*env)->NewObject(env, mode_class, mode_constructor,
			input_mode.type, (*env)->NewStringUTF(env, input_mode.shortcut));
		return mode_object;
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

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_VoiceManager_setInputMode
  (JNIEnv *env, jobject object, jlong pointer, jobject inputMode, jobject callback)
{
	struct IDiscordVoiceManager *voice_manager = (struct IDiscordVoiceManager*) pointer;

	struct DiscordInputMode input_mode;

	jclass mode_class = (*env)->FindClass(env, "de/jcm/discordgamesdk/voice/VoiceInputMode");
	jmethodID mode_getNativeType = (*env)->GetMethodID(env, mode_class, "getNativeType", "()I");
	input_mode.type = (*env)->CallIntMethod(env, inputMode, mode_getNativeType);

	jmethodID mode_getShortcut = (*env)->GetMethodID(env, mode_class, "getShortcut", "()Ljava/lang/String;");
	jstring shortcut = (*env)->CallObjectMethod(env, inputMode, mode_getShortcut);
	const char *nativeShortcut = (*env)->GetStringUTFChars(env, shortcut, 0);
	strncpy(input_mode.shortcut, nativeShortcut, sizeof(input_mode.shortcut)-1);
	input_mode.shortcut[sizeof(input_mode.shortcut)-1] = '\0';
	(*env)->ReleaseStringUTFChars(env, shortcut, nativeShortcut);

	struct CallbackData* cbd = malloc(sizeof(struct CallbackData));
	prepare_callback_data(env, callback, cbd);

	voice_manager->set_input_mode(voice_manager, input_mode, cbd, simple_callback);
}

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_VoiceManager_isSelfMute
  (JNIEnv *env, jobject object, jlong pointer)
{
	struct IDiscordVoiceManager *voice_manager = (struct IDiscordVoiceManager*) pointer;

	bool self_mute;
	enum EDiscordResult result = voice_manager->is_self_mute(voice_manager, &self_mute);

	if(result == DiscordResult_Ok) // if everything went well, return the boolean
	{
		jclass Boolean_class = (*env)->FindClass(env, "java/lang/Boolean");
		jmethodID valueOf_method = (*env)->GetStaticMethodID(env, Boolean_class, "valueOf", "(Z)Ljava/lang/Boolean;");
		jobject return_object = (*env)->CallStaticObjectMethod(env, Boolean_class, valueOf_method, self_mute);
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

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_VoiceManager_setSelfMute
  (JNIEnv *env, jobject object, jlong pointer, jboolean self_mute)
{
	struct IDiscordVoiceManager *voice_manager = (struct IDiscordVoiceManager*) pointer;
	enum EDiscordResult result = voice_manager->set_self_mute(voice_manager, self_mute);

	jclass result_clazz = (*env)->FindClass(env, "de/jcm/discordgamesdk/Result");
	jmethodID values_method = (*env)->GetStaticMethodID(env, result_clazz, "values", "()[Lde/jcm/discordgamesdk/Result;");
	jobjectArray values = (jobjectArray) (*env)->CallStaticObjectMethod(env, result_clazz, values_method);
	jobject result_object = (*env)->GetObjectArrayElement(env, values, result);

	return result_object;
}

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_VoiceManager_isSelfDeaf
  (JNIEnv *env, jobject object, jlong pointer)
{
	struct IDiscordVoiceManager *voice_manager = (struct IDiscordVoiceManager*) pointer;

	bool self_deaf;
	enum EDiscordResult result = voice_manager->is_self_deaf(voice_manager, &self_deaf);

	if(result == DiscordResult_Ok) // if everything went well, return the boolean
	{
		jclass Boolean_class = (*env)->FindClass(env, "java/lang/Boolean");
		jmethodID valueOf_method = (*env)->GetStaticMethodID(env, Boolean_class, "valueOf", "(Z)Ljava/lang/Boolean;");
		jobject return_object = (*env)->CallStaticObjectMethod(env, Boolean_class, valueOf_method, self_deaf);
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

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_VoiceManager_setSelfDeaf
  (JNIEnv *env, jobject object, jlong pointer, jboolean self_deaf)
{
	struct IDiscordVoiceManager *voice_manager = (struct IDiscordVoiceManager*) pointer;
	enum EDiscordResult result = voice_manager->set_self_deaf(voice_manager, self_deaf);

	jclass result_clazz = (*env)->FindClass(env, "de/jcm/discordgamesdk/Result");
	jmethodID values_method = (*env)->GetStaticMethodID(env, result_clazz, "values", "()[Lde/jcm/discordgamesdk/Result;");
	jobjectArray values = (jobjectArray) (*env)->CallStaticObjectMethod(env, result_clazz, values_method);
	jobject result_object = (*env)->GetObjectArrayElement(env, values, result);

	return result_object;
}

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_VoiceManager_isLocalMute
  (JNIEnv *env, jobject object, jlong pointer, jlong userId)
{
	struct IDiscordVoiceManager *voice_manager = (struct IDiscordVoiceManager*) pointer;

	bool mute;
	enum EDiscordResult result = voice_manager->is_local_mute(voice_manager, userId, &mute);

	if(result == DiscordResult_Ok) // if everything went well, return the boolean
	{
		jclass Boolean_class = (*env)->FindClass(env, "java/lang/Boolean");
		jmethodID valueOf_method = (*env)->GetStaticMethodID(env, Boolean_class, "valueOf", "(Z)Ljava/lang/Boolean;");
		jobject return_object = (*env)->CallStaticObjectMethod(env, Boolean_class, valueOf_method, mute);
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

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_VoiceManager_setLocalMute
  (JNIEnv *env, jobject object, jlong pointer, jlong userId, jboolean mute)
{
	struct IDiscordVoiceManager *voice_manager = (struct IDiscordVoiceManager*) pointer;
	enum EDiscordResult result = voice_manager->set_local_mute(voice_manager, userId, mute);

	jclass result_clazz = (*env)->FindClass(env, "de/jcm/discordgamesdk/Result");
	jmethodID values_method = (*env)->GetStaticMethodID(env, result_clazz, "values", "()[Lde/jcm/discordgamesdk/Result;");
	jobjectArray values = (jobjectArray) (*env)->CallStaticObjectMethod(env, result_clazz, values_method);
	jobject result_object = (*env)->GetObjectArrayElement(env, values, result);

	return result_object;
}

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_VoiceManager_getLocalVolume
  (JNIEnv *env, jobject object, jlong pointer, jlong userId)
{
	struct IDiscordVoiceManager *voice_manager = (struct IDiscordVoiceManager*) pointer;

	uint8_t volume;
	enum EDiscordResult result = voice_manager->get_local_volume(voice_manager, userId, &volume);

	if(result == DiscordResult_Ok) // if everything went well, return the volume as an Integer
	{
		jclass Integer_class = (*env)->FindClass(env, "java/lang/Integer");
		jmethodID valueOf_method = (*env)->GetStaticMethodID(env, Integer_class, "valueOf", "(I)Ljava/lang/Integer;");
		jobject return_object = (*env)->CallStaticObjectMethod(env, Integer_class, valueOf_method, (int)volume);
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

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_VoiceManager_setLocalVolume
  (JNIEnv *env, jobject object, jlong pointer, jlong userId, jbyte volume)
{
	struct IDiscordVoiceManager *voice_manager = (struct IDiscordVoiceManager*) pointer;
	enum EDiscordResult result = voice_manager->set_local_volume(voice_manager, userId, (uint8_t)volume);

	jclass result_clazz = (*env)->FindClass(env, "de/jcm/discordgamesdk/Result");
	jmethodID values_method = (*env)->GetStaticMethodID(env, result_clazz, "values", "()[Lde/jcm/discordgamesdk/Result;");
	jobjectArray values = (jobjectArray) (*env)->CallStaticObjectMethod(env, result_clazz, values_method);
	jobject result_object = (*env)->GetObjectArrayElement(env, values, result);

	return result_object;
}
