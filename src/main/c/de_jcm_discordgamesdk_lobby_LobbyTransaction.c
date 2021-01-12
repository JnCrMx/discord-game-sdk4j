#include <stdlib.h>
#include <discord_game_sdk.h>

#include "de_jcm_discordgamesdk_lobby_LobbyTransaction.h"

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_lobby_LobbyTransaction_setType
  (JNIEnv *env, jobject object, jlong pointer, jint type)
{
	struct IDiscordLobbyTransaction *transaction = (struct IDiscordLobbyTransaction*) pointer;

	enum EDiscordResult result = transaction->set_type(transaction, type);

	jclass result_clazz = (*env)->FindClass(env, "de/jcm/discordgamesdk/Result");
	jmethodID values_method = (*env)->GetStaticMethodID(env, result_clazz, "values", "()[Lde/jcm/discordgamesdk/Result;");
	jobjectArray values = (jobjectArray) (*env)->CallStaticObjectMethod(env, result_clazz, values_method);
	jobject result_object = (*env)->GetObjectArrayElement(env, values, result);
	return result_object;
}

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_lobby_LobbyTransaction_setOwner
  (JNIEnv *env, jobject object, jlong pointer, jlong ownerId)
{
	struct IDiscordLobbyTransaction *transaction = (struct IDiscordLobbyTransaction*) pointer;

	enum EDiscordResult result = transaction->set_owner(transaction, ownerId);

	jclass result_clazz = (*env)->FindClass(env, "de/jcm/discordgamesdk/Result");
	jmethodID values_method = (*env)->GetStaticMethodID(env, result_clazz, "values", "()[Lde/jcm/discordgamesdk/Result;");
	jobjectArray values = (jobjectArray) (*env)->CallStaticObjectMethod(env, result_clazz, values_method);
	jobject result_object = (*env)->GetObjectArrayElement(env, values, result);
	return result_object;
}

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_lobby_LobbyTransaction_setCapacity
  (JNIEnv *env, jobject object, jlong pointer, jint capacity)
{
	struct IDiscordLobbyTransaction *transaction = (struct IDiscordLobbyTransaction*) pointer;

	enum EDiscordResult result = transaction->set_capacity(transaction, capacity);

	jclass result_clazz = (*env)->FindClass(env, "de/jcm/discordgamesdk/Result");
	jmethodID values_method = (*env)->GetStaticMethodID(env, result_clazz, "values", "()[Lde/jcm/discordgamesdk/Result;");
	jobjectArray values = (jobjectArray) (*env)->CallStaticObjectMethod(env, result_clazz, values_method);
	jobject result_object = (*env)->GetObjectArrayElement(env, values, result);
	return result_object;
}

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_lobby_LobbyTransaction_setMetadata
  (JNIEnv *env, jobject object, jlong pointer, jstring key, jstring value)
{
	struct IDiscordLobbyTransaction *transaction = (struct IDiscordLobbyTransaction*) pointer;

	const char *nativeKey = (*env)->GetStringUTFChars(env, key, 0);
	DiscordMetadataKey metadata_key;
	strcpy(metadata_key, nativeKey);
	(*env)->ReleaseStringUTFChars(env, key, nativeKey);

	const char *nativeValue = (*env)->GetStringUTFChars(env, value, 0);
	DiscordMetadataValue metadata_value;
	strcpy(metadata_value, nativeValue);
	(*env)->ReleaseStringUTFChars(env, value, nativeValue);

	enum EDiscordResult result = transaction->set_metadata(transaction, metadata_key, metadata_value);

	jclass result_clazz = (*env)->FindClass(env, "de/jcm/discordgamesdk/Result");
	jmethodID values_method = (*env)->GetStaticMethodID(env, result_clazz, "values", "()[Lde/jcm/discordgamesdk/Result;");
	jobjectArray values = (jobjectArray) (*env)->CallStaticObjectMethod(env, result_clazz, values_method);
	jobject result_object = (*env)->GetObjectArrayElement(env, values, result);
	return result_object;
}

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_lobby_LobbyTransaction_deleteMetadata
  (JNIEnv *env, jobject object, jlong pointer, jstring key)
{
	struct IDiscordLobbyTransaction *transaction = (struct IDiscordLobbyTransaction*) pointer;

	const char *nativeKey = (*env)->GetStringUTFChars(env, key, 0);
	DiscordMetadataKey metadata_key;
	strcpy(metadata_key, nativeKey);
	(*env)->ReleaseStringUTFChars(env, key, nativeKey);

	enum EDiscordResult result = transaction->delete_metadata(transaction, metadata_key);

	jclass result_clazz = (*env)->FindClass(env, "de/jcm/discordgamesdk/Result");
	jmethodID values_method = (*env)->GetStaticMethodID(env, result_clazz, "values", "()[Lde/jcm/discordgamesdk/Result;");
	jobjectArray values = (jobjectArray) (*env)->CallStaticObjectMethod(env, result_clazz, values_method);
	jobject result_object = (*env)->GetObjectArrayElement(env, values, result);
	return result_object;
}

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_lobby_LobbyTransaction_setLocked
  (JNIEnv *env, jobject object, jlong pointer, jboolean locked)
{
	struct IDiscordLobbyTransaction *transaction = (struct IDiscordLobbyTransaction*) pointer;

	enum EDiscordResult result = transaction->set_locked(transaction, locked);

	jclass result_clazz = (*env)->FindClass(env, "de/jcm/discordgamesdk/Result");
	jmethodID values_method = (*env)->GetStaticMethodID(env, result_clazz, "values", "()[Lde/jcm/discordgamesdk/Result;");
	jobjectArray values = (jobjectArray) (*env)->CallStaticObjectMethod(env, result_clazz, values_method);
	jobject result_object = (*env)->GetObjectArrayElement(env, values, result);
	return result_object;
}
