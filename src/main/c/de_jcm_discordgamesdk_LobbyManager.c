#include <stdlib.h>
#include <discord_game_sdk.h>

#include "de_jcm_discordgamesdk_LobbyManager.h"
#include "Callback.h"

void create_lobby_callback(void* data, enum EDiscordResult result, struct DiscordLobby* lobby)
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

	// prepare lobby object
	jclass lobby_class = (*env)->FindClass(env, "de/jcm/discordgamesdk/lobby/Lobby");
	jmethodID lobby_constructor = (*env)->GetMethodID(env, lobby_class, "<init>", "(JIJLjava/lang/String;IZ)V");
	jobject lobby_object = (*env)->NewObject(env, lobby_class, lobby_constructor,
		lobby->id, lobby->type, lobby->owner_id,
		(*env)->NewStringUTF(env, lobby->secret),
		lobby->capacity, lobby->locked);

	// call the callback
	jclass clazz = (*env)->GetObjectClass(env, cbd->callback);
	jmethodID method = (*env)->GetMethodID(env, clazz, "accept", "(Ljava/lang/Object;Ljava/lang/Object;)V");
	(*env)->CallVoidMethod(env, cbd->callback, method, result_object, lobby_object);

	// clean up stuff
	(*env)->DeleteGlobalRef(env, cbd->callback);
	(*(cbd->jvm))->DetachCurrentThread(cbd->jvm);

	free(cbd);
}

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_LobbyManager_getLobbyCreateTransaction
  (JNIEnv *env, jobject object, jlong pointer)
{
	struct IDiscordLobbyManager *lobby_manager = (struct IDiscordLobbyManager*) pointer;
	struct IDiscordLobbyTransaction *transaction;

	enum EDiscordResult result = lobby_manager->get_lobby_create_transaction(lobby_manager, &transaction);
	if(result == DiscordResult_Ok) // if everything went well, return the transaction
	{
		jclass transaction_class = (*env)->FindClass(env, "de/jcm/discordgamesdk/lobby/LobbyTransaction");
		jmethodID transaction_constructor = (*env)->GetMethodID(env, transaction_class, "<init>", "(J)V");
		jobject transaction_object = (*env)->NewObject(env, transaction_class, transaction_constructor,
			(uint64_t) transaction);
		return transaction_object;
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

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_LobbyManager_createLobby
  (JNIEnv *env, jobject object, jlong pointer, jlong transactionPointer, jobject callback)
{
	struct IDiscordLobbyManager *lobby_manager = (struct IDiscordLobbyManager*) pointer;
	struct IDiscordLobbyTransaction *transaction = (struct IDiscordLobbyTransaction*) transactionPointer;

	struct CallbackData* cbd = malloc(sizeof(struct CallbackData));
	prepare_callback_data(env, callback, cbd);

	lobby_manager->create_lobby(lobby_manager, transaction, cbd, create_lobby_callback);
}