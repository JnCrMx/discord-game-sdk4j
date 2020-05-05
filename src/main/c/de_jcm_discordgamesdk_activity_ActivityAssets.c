#include <discord_game_sdk.h>

#include "de_jcm_discordgamesdk_activity_ActivityAssets.h"

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_activity_ActivityAssets_setLargeImage(JNIEnv *env, jobject object, jlong pointer, jstring asset_key)
{
	if(!pointer)
	{
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/NullPointerException"), "reference is null");
		return;
	}
	
	struct DiscordActivityAssets *assets = (struct DiscordActivityAssets*) pointer;
	
	const char *nativeString = (*env)->GetStringUTFChars(env, asset_key, 0);
	strcpy(assets->large_image, nativeString);
	(*env)->ReleaseStringUTFChars(env, asset_key, nativeString);
}

JNIEXPORT jstring JNICALL Java_de_jcm_discordgamesdk_activity_ActivityAssets_getLargeImage(JNIEnv *env, jobject object, jlong pointer)
{
	if(!pointer)
	{
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/NullPointerException"), "reference is null");
		return NULL;
	}
	
	struct DiscordActivityAssets *assets = (struct DiscordActivityAssets*) pointer;
	
	return (*env)->NewStringUTF(env, assets->large_image);
}

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_activity_ActivityAssets_setLargeText(JNIEnv *env, jobject object, jlong pointer, jstring text)
{
	if(!pointer)
	{
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/NullPointerException"), "reference is null");
		return;
	}
	
	struct DiscordActivityAssets *assets = (struct DiscordActivityAssets*) pointer;
	
	const char *nativeString = (*env)->GetStringUTFChars(env, text, 0);
	strcpy(assets->large_text, nativeString);
	(*env)->ReleaseStringUTFChars(env, text, nativeString);
}

JNIEXPORT jstring JNICALL Java_de_jcm_discordgamesdk_activity_ActivityAssets_getLargeText(JNIEnv *env, jobject object, jlong pointer)
{
	if(!pointer)
	{
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/NullPointerException"), "reference is null");
		return NULL;
	}
	
	struct DiscordActivityAssets *assets = (struct DiscordActivityAssets*) pointer;
	
	return (*env)->NewStringUTF(env, assets->large_text);
}


JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_activity_ActivityAssets_setSmallImage(JNIEnv *env, jobject object, jlong pointer, jstring asset_key)
{
	if(!pointer)
	{
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/NullPointerException"), "reference is null");
		return;
	}
	
	struct DiscordActivityAssets *assets = (struct DiscordActivityAssets*) pointer;
	
	const char *nativeString = (*env)->GetStringUTFChars(env, asset_key, 0);
	strcpy(assets->small_image, nativeString);
	(*env)->ReleaseStringUTFChars(env, asset_key, nativeString);
}

JNIEXPORT jstring JNICALL Java_de_jcm_discordgamesdk_activity_ActivityAssets_getSmallImage(JNIEnv *env, jobject object, jlong pointer)
{
	if(!pointer)
	{
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/NullPointerException"), "reference is null");
		return NULL;
	}
	
	struct DiscordActivityAssets *assets = (struct DiscordActivityAssets*) pointer;
	
	return (*env)->NewStringUTF(env, assets->small_image);
}

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_activity_ActivityAssets_setSmallText(JNIEnv *env, jobject object, jlong pointer, jstring text)
{
	if(!pointer)
	{
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/NullPointerException"), "reference is null");
		return;
	}
	
	struct DiscordActivityAssets *assets = (struct DiscordActivityAssets*) pointer;
	
	const char *nativeString = (*env)->GetStringUTFChars(env, text, 0);
	strcpy(assets->small_text, nativeString);
	(*env)->ReleaseStringUTFChars(env, text, nativeString);
}

JNIEXPORT jstring JNICALL Java_de_jcm_discordgamesdk_activity_ActivityAssets_getSmallText(JNIEnv *env, jobject object, jlong pointer)
{
	if(!pointer)
	{
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/NullPointerException"), "reference is null");
		return NULL;
	}
	
	struct DiscordActivityAssets *assets = (struct DiscordActivityAssets*) pointer;
	
	return (*env)->NewStringUTF(env, assets->small_text);
}
