#include <stdlib.h>
#include <discord_game_sdk.h>

#include "de_jcm_discordgamesdk_lobby_LobbyMemberTransaction.h"

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_lobby_LobbyMemberTransaction_setMetadata
  (JNIEnv *env, jobject object, jlong pointer, jstring key, jstring value)
{
	struct IDiscordLobbyMemberTransaction *transaction = (struct IDiscordLobbyMemberTransaction*) pointer;

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

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_lobby_LobbyMemberTransaction_deleteMetadata
  (JNIEnv *env, jobject object, jlong pointer, jstring key)
{
	struct IDiscordLobbyMemberTransaction *transaction = (struct IDiscordLobbyMemberTransaction*) pointer;

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
