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
