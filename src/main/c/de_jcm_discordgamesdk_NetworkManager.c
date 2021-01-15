#include <stdlib.h>
#include <discord_game_sdk.h>

#include "de_jcm_discordgamesdk_LobbyManager.h"

JNIEXPORT jlong JNICALL Java_de_jcm_discordgamesdk_NetworkManager_getPeerId
  (JNIEnv *env, jobject object, jlong pointer)
{
	struct IDiscordNetworkManager *network_manager = (struct IDiscordNetworkManager*) pointer;

	DiscordNetworkPeerId peer_id;
	network_manager->get_peer_id(network_manager, &peer_id);

	return peer_id;
}

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_NetworkManager_flush
  (JNIEnv *env, jobject object, jlong pointer)
{
	struct IDiscordNetworkManager *network_manager = (struct IDiscordNetworkManager*) pointer;

	enum EDiscordResult result = network_manager->flush(network_manager);

	jclass result_clazz = (*env)->FindClass(env, "de/jcm/discordgamesdk/Result");
	jmethodID values_method = (*env)->GetStaticMethodID(env, result_clazz, "values", "()[Lde/jcm/discordgamesdk/Result;");
	jobjectArray values = (jobjectArray) (*env)->CallStaticObjectMethod(env, result_clazz, values_method);
	jobject result_object = (*env)->GetObjectArrayElement(env, values, result);

	return result_object;
}

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_NetworkManager_openPeer
  (JNIEnv *env, jobject object, jlong pointer, jlong peerId, jstring routeData)
{
	struct IDiscordNetworkManager *network_manager = (struct IDiscordNetworkManager*) pointer;

	const char *nativeRoute = (*env)->GetStringUTFChars(env, routeData, 0);

	enum EDiscordResult result = network_manager->open_peer(network_manager, peerId, nativeRoute);

	(*env)->ReleaseStringUTFChars(env, routeData, nativeRoute);

	jclass result_clazz = (*env)->FindClass(env, "de/jcm/discordgamesdk/Result");
	jmethodID values_method = (*env)->GetStaticMethodID(env, result_clazz, "values", "()[Lde/jcm/discordgamesdk/Result;");
	jobjectArray values = (jobjectArray) (*env)->CallStaticObjectMethod(env, result_clazz, values_method);
	jobject result_object = (*env)->GetObjectArrayElement(env, values, result);

	return result_object;
}

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_NetworkManager_updatePeer
  (JNIEnv *env, jobject object, jlong pointer, jlong peerId, jstring routeData)
{
	struct IDiscordNetworkManager *network_manager = (struct IDiscordNetworkManager*) pointer;

	const char *nativeRoute = (*env)->GetStringUTFChars(env, routeData, 0);

	enum EDiscordResult result = network_manager->update_peer(network_manager, peerId, nativeRoute);

	(*env)->ReleaseStringUTFChars(env, routeData, nativeRoute);

	jclass result_clazz = (*env)->FindClass(env, "de/jcm/discordgamesdk/Result");
	jmethodID values_method = (*env)->GetStaticMethodID(env, result_clazz, "values", "()[Lde/jcm/discordgamesdk/Result;");
	jobjectArray values = (jobjectArray) (*env)->CallStaticObjectMethod(env, result_clazz, values_method);
	jobject result_object = (*env)->GetObjectArrayElement(env, values, result);

	return result_object;
}

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_NetworkManager_closePeer
  (JNIEnv *env, jobject object, jlong pointer, jlong peerId)
{
	struct IDiscordNetworkManager *network_manager = (struct IDiscordNetworkManager*) pointer;

	enum EDiscordResult result = network_manager->close_peer(network_manager, peerId);

	jclass result_clazz = (*env)->FindClass(env, "de/jcm/discordgamesdk/Result");
	jmethodID values_method = (*env)->GetStaticMethodID(env, result_clazz, "values", "()[Lde/jcm/discordgamesdk/Result;");
	jobjectArray values = (jobjectArray) (*env)->CallStaticObjectMethod(env, result_clazz, values_method);
	jobject result_object = (*env)->GetObjectArrayElement(env, values, result);

	return result_object;
}

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_NetworkManager_openChannel
  (JNIEnv *env, jobject object, jlong pointer, jlong peerId, jbyte channelId, jboolean reliable)
{
	struct IDiscordNetworkManager *network_manager = (struct IDiscordNetworkManager*) pointer;

	enum EDiscordResult result = network_manager->open_channel(network_manager, peerId, channelId, reliable);

	jclass result_clazz = (*env)->FindClass(env, "de/jcm/discordgamesdk/Result");
	jmethodID values_method = (*env)->GetStaticMethodID(env, result_clazz, "values", "()[Lde/jcm/discordgamesdk/Result;");
	jobjectArray values = (jobjectArray) (*env)->CallStaticObjectMethod(env, result_clazz, values_method);
	jobject result_object = (*env)->GetObjectArrayElement(env, values, result);

	return result_object;
}

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_NetworkManager_closeChannel
  (JNIEnv *env, jobject object, jlong pointer, jlong peerId, jbyte channelId)
{
	struct IDiscordNetworkManager *network_manager = (struct IDiscordNetworkManager*) pointer;

	enum EDiscordResult result = network_manager->close_channel(network_manager, peerId, channelId);

	jclass result_clazz = (*env)->FindClass(env, "de/jcm/discordgamesdk/Result");
	jmethodID values_method = (*env)->GetStaticMethodID(env, result_clazz, "values", "()[Lde/jcm/discordgamesdk/Result;");
	jobjectArray values = (jobjectArray) (*env)->CallStaticObjectMethod(env, result_clazz, values_method);
	jobject result_object = (*env)->GetObjectArrayElement(env, values, result);

	return result_object;
}

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_NetworkManager_sendMessage
  (JNIEnv *env, jobject object, jlong pointer, jlong peerId, jbyte channelId, jbyteArray array, jint offset, jint length)
{
	struct IDiscordNetworkManager *network_manager = (struct IDiscordNetworkManager*) pointer;

	uint8_t* data = (uint8_t*) malloc(length);
	(*env)->GetByteArrayRegion(env, array, offset, length, data);

	enum EDiscordResult result = network_manager->send_message(network_manager, peerId, channelId, data, length);

	free(data);

	jclass result_clazz = (*env)->FindClass(env, "de/jcm/discordgamesdk/Result");
	jmethodID values_method = (*env)->GetStaticMethodID(env, result_clazz, "values", "()[Lde/jcm/discordgamesdk/Result;");
	jobjectArray values = (jobjectArray) (*env)->CallStaticObjectMethod(env, result_clazz, values_method);
	jobject result_object = (*env)->GetObjectArrayElement(env, values, result);

	return result_object;
}
