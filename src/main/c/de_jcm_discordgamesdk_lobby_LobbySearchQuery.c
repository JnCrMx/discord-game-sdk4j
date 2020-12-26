#include <stdlib.h>
#include <discord_game_sdk.h>

#include "de_jcm_discordgamesdk_lobby_LobbySearchQuery.h"

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_lobby_LobbySearchQuery_filter
  (JNIEnv *env, jobject object, jlong pointer, jstring key, jint comparison, jint cast, jstring value)
{
	struct IDiscordLobbySearchQuery *query = (struct IDiscordLobbySearchQuery*) pointer;

	const char *nativeKey = (*env)->GetStringUTFChars(env, key, 0);
	DiscordMetadataKey metadata_key;
	strcpy(metadata_key, nativeKey);
	(*env)->ReleaseStringUTFChars(env, key, nativeKey);

	const char *nativeValue = (*env)->GetStringUTFChars(env, value, 0);
	DiscordMetadataValue metadata_value;
	strcpy(metadata_value, nativeValue);
	(*env)->ReleaseStringUTFChars(env, value, nativeValue);

	enum EDiscordResult result = query->filter(query, metadata_key, comparison, cast, metadata_value);

	jclass result_clazz = (*env)->FindClass(env, "de/jcm/discordgamesdk/Result");
	jmethodID values_method = (*env)->GetStaticMethodID(env, result_clazz, "values", "()[Lde/jcm/discordgamesdk/Result;");
	jobjectArray values = (jobjectArray) (*env)->CallStaticObjectMethod(env, result_clazz, values_method);
	jobject result_object = (*env)->GetObjectArrayElement(env, values, result);
	return result_object;
}

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_lobby_LobbySearchQuery_sort
  (JNIEnv *env, jobject object, jlong pointer, jstring key, jint cast, jstring value)
{
	struct IDiscordLobbySearchQuery *query = (struct IDiscordLobbySearchQuery*) pointer;

	const char *nativeKey = (*env)->GetStringUTFChars(env, key, 0);
	DiscordMetadataKey metadata_key;
	strcpy(metadata_key, nativeKey);
	(*env)->ReleaseStringUTFChars(env, key, nativeKey);

	const char *nativeValue = (*env)->GetStringUTFChars(env, value, 0);
	DiscordMetadataValue metadata_value;
	strcpy(metadata_value, nativeValue);
	(*env)->ReleaseStringUTFChars(env, value, nativeValue);

	enum EDiscordResult result = query->sort(query, metadata_key, cast, metadata_value);

	jclass result_clazz = (*env)->FindClass(env, "de/jcm/discordgamesdk/Result");
	jmethodID values_method = (*env)->GetStaticMethodID(env, result_clazz, "values", "()[Lde/jcm/discordgamesdk/Result;");
	jobjectArray values = (jobjectArray) (*env)->CallStaticObjectMethod(env, result_clazz, values_method);
	jobject result_object = (*env)->GetObjectArrayElement(env, values, result);
	return result_object;
}

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_lobby_LobbySearchQuery_limit
  (JNIEnv *env, jobject object, jlong pointer, jint limit)
{
	struct IDiscordLobbySearchQuery *query = (struct IDiscordLobbySearchQuery*) pointer;

	enum EDiscordResult result = query->limit(query, limit);

	jclass result_clazz = (*env)->FindClass(env, "de/jcm/discordgamesdk/Result");
	jmethodID values_method = (*env)->GetStaticMethodID(env, result_clazz, "values", "()[Lde/jcm/discordgamesdk/Result;");
	jobjectArray values = (jobjectArray) (*env)->CallStaticObjectMethod(env, result_clazz, values_method);
	jobject result_object = (*env)->GetObjectArrayElement(env, values, result);
	return result_object;
}

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_lobby_LobbySearchQuery_distance
  (JNIEnv *env, jobject object, jlong pointer, jint distance)
{
	struct IDiscordLobbySearchQuery *query = (struct IDiscordLobbySearchQuery*) pointer;

	enum EDiscordResult result = query->distance(query, distance);

	jclass result_clazz = (*env)->FindClass(env, "de/jcm/discordgamesdk/Result");
	jmethodID values_method = (*env)->GetStaticMethodID(env, result_clazz, "values", "()[Lde/jcm/discordgamesdk/Result;");
	jobjectArray values = (jobjectArray) (*env)->CallStaticObjectMethod(env, result_clazz, values_method);
	jobject result_object = (*env)->GetObjectArrayElement(env, values, result);
	return result_object;
}
