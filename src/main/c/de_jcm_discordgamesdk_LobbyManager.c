#include <stdlib.h>
#include <discord_game_sdk.h>

#include "de_jcm_discordgamesdk_LobbyManager.h"
#include "Callback.h"

void create_connect_lobby_callback(void* data, enum EDiscordResult result, struct DiscordLobby* lobby)
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

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_LobbyManager_getLobbyUpdateTransaction
  (JNIEnv *env, jobject object, jlong pointer, jlong lobbyId)
{
	struct IDiscordLobbyManager *lobby_manager = (struct IDiscordLobbyManager*) pointer;
	struct IDiscordLobbyTransaction *transaction;

	enum EDiscordResult result = lobby_manager->get_lobby_update_transaction(lobby_manager, lobbyId, &transaction);
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

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_LobbyManager_getMemberUpdateTransaction
  (JNIEnv *env, jobject object, jlong pointer, jlong lobbyId, jlong userId)
{
	struct IDiscordLobbyManager *lobby_manager = (struct IDiscordLobbyManager*) pointer;
	struct IDiscordLobbyMemberTransaction *transaction;

	enum EDiscordResult result = lobby_manager->get_member_update_transaction(lobby_manager, lobbyId, userId, &transaction);
	if(result == DiscordResult_Ok) // if everything went well, return the transaction
	{
		jclass transaction_class = (*env)->FindClass(env, "de/jcm/discordgamesdk/lobby/LobbyMemberTransaction");
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

	lobby_manager->create_lobby(lobby_manager, transaction, cbd, create_connect_lobby_callback);
}

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_LobbyManager_updateLobby
  (JNIEnv *env, jobject object, jlong pointer, jlong lobbyId, jlong transactionPointer, jobject callback)
{
	struct IDiscordLobbyManager *lobby_manager = (struct IDiscordLobbyManager*) pointer;
	struct IDiscordLobbyTransaction *transaction = (struct IDiscordLobbyTransaction*) transactionPointer;

	struct CallbackData* cbd = malloc(sizeof(struct CallbackData));
	prepare_callback_data(env, callback, cbd);

	lobby_manager->update_lobby(lobby_manager, lobbyId, transaction, cbd, simple_callback);
}

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_LobbyManager_deleteLobby
  (JNIEnv *env, jobject object, jlong pointer, jlong lobbyId, jobject callback)
{
	struct IDiscordLobbyManager *lobby_manager = (struct IDiscordLobbyManager*) pointer;

	struct CallbackData* cbd = malloc(sizeof(struct CallbackData));
	prepare_callback_data(env, callback, cbd);

	lobby_manager->delete_lobby(lobby_manager, lobbyId, cbd, simple_callback);
}

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_LobbyManager_connectLobby
  (JNIEnv *env, jobject object, jlong pointer, jlong lobbyId, jstring secret, jobject callback)
{
	struct IDiscordLobbyManager *lobby_manager = (struct IDiscordLobbyManager*) pointer;

	const char *nativeSecret = (*env)->GetStringUTFChars(env, secret, 0);
	DiscordLobbySecret lobby_secret;
	strcpy(lobby_secret, nativeSecret);
	(*env)->ReleaseStringUTFChars(env, secret, nativeSecret);

	struct CallbackData* cbd = malloc(sizeof(struct CallbackData));
	prepare_callback_data(env, callback, cbd);

	lobby_manager->connect_lobby(lobby_manager, lobbyId, lobby_secret, cbd, create_connect_lobby_callback);
}

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_LobbyManager_connectLobbyWithActivitySecret
  (JNIEnv *env, jobject object, jlong pointer, jstring activitySecret, jobject callback)
{
	struct IDiscordLobbyManager *lobby_manager = (struct IDiscordLobbyManager*) pointer;

	const char *nativeSecret = (*env)->GetStringUTFChars(env, activitySecret, 0);
	DiscordLobbySecret activity_secret;
	strcpy(activity_secret, nativeSecret);
	(*env)->ReleaseStringUTFChars(env, activitySecret, nativeSecret);

	struct CallbackData* cbd = malloc(sizeof(struct CallbackData));
	prepare_callback_data(env, callback, cbd);

	lobby_manager->connect_lobby_with_activity_secret(lobby_manager, activity_secret, cbd, create_connect_lobby_callback);
}

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_LobbyManager_disconnectLobby
  (JNIEnv *env, jobject object, jlong pointer, jlong lobbyId, jobject callback)
{
	struct IDiscordLobbyManager *lobby_manager = (struct IDiscordLobbyManager*) pointer;

	struct CallbackData* cbd = malloc(sizeof(struct CallbackData));
	prepare_callback_data(env, callback, cbd);

	lobby_manager->disconnect_lobby(lobby_manager, lobbyId, cbd, simple_callback);
}

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_LobbyManager_getLobby
  (JNIEnv *env, jobject object, jlong pointer, jlong lobbyId)
{
	struct IDiscordLobbyManager *lobby_manager = (struct IDiscordLobbyManager*) pointer;
	struct DiscordLobby lobby;

	enum EDiscordResult result = lobby_manager->get_lobby(lobby_manager, lobbyId, &lobby);
	if(result == DiscordResult_Ok) // if everything went well, return the lobby
	{
		jclass lobby_class = (*env)->FindClass(env, "de/jcm/discordgamesdk/lobby/Lobby");
		jmethodID lobby_constructor = (*env)->GetMethodID(env, lobby_class, "<init>", "(JIJLjava/lang/String;IZ)V");
		jobject lobby_object = (*env)->NewObject(env, lobby_class, lobby_constructor,
			lobby.id, lobby.type, lobby.owner_id,
			(*env)->NewStringUTF(env, lobby.secret),
			lobby.capacity, lobby.locked);
		return lobby_object;
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

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_LobbyManager_getLobbyActivitySecret
  (JNIEnv *env, jobject object, jlong pointer, jlong lobbyId)
{
	struct IDiscordLobbyManager *lobby_manager = (struct IDiscordLobbyManager*) pointer;
	DiscordLobbySecret activity_secret;

	enum EDiscordResult result = lobby_manager->get_lobby_activity_secret(lobby_manager, lobbyId, &activity_secret);
	if(result == DiscordResult_Ok) // if everything went well, return the lobby
	{
		return (*env)->NewStringUTF(env, activity_secret);
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

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_LobbyManager_getLobbyMetadataValue
  (JNIEnv *env, jobject object, jlong pointer, jlong lobbyId, jstring key)
{
	struct IDiscordLobbyManager *lobby_manager = (struct IDiscordLobbyManager*) pointer;

	const char *nativeKey = (*env)->GetStringUTFChars(env, key, 0);
	DiscordMetadataKey metadata_key;
	strcpy(metadata_key, nativeKey);
	(*env)->ReleaseStringUTFChars(env, key, nativeKey);

	DiscordMetadataValue metadata_value;

	enum EDiscordResult result = lobby_manager->get_lobby_metadata_value(lobby_manager, lobbyId, metadata_key, &metadata_value);
	if(result == DiscordResult_Ok) // if everything went well, return the value
	{
		return (*env)->NewStringUTF(env, metadata_value);
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

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_LobbyManager_getLobbyMetadataKey
  (JNIEnv *env, jobject object, jlong pointer, jlong lobbyId, jint index)
{
	struct IDiscordLobbyManager *lobby_manager = (struct IDiscordLobbyManager*) pointer;

	DiscordMetadataKey metadata_key;

	enum EDiscordResult result = lobby_manager->get_lobby_metadata_key(lobby_manager, lobbyId, index, &metadata_key);
	if(result == DiscordResult_Ok) // if everything went well, return the key
	{
		return (*env)->NewStringUTF(env, metadata_key);
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

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_LobbyManager_lobbyMetadataCount
  (JNIEnv *env, jobject object, jlong pointer, jlong lobbyId)
{
	struct IDiscordLobbyManager *lobby_manager = (struct IDiscordLobbyManager*) pointer;

	int count;

	enum EDiscordResult result = lobby_manager->lobby_metadata_count(lobby_manager, lobbyId, &count);
	if(result == DiscordResult_Ok) // if everything went well, return the count
	{
		jclass Integer_class = (*env)->FindClass(env, "java/lang/Integer");
		jmethodID valueOf_method = (*env)->GetStaticMethodID(env, Integer_class, "valueOf", "(I)Ljava/lang/Integer;");
		jobject return_object = (*env)->CallStaticObjectMethod(env, Integer_class, valueOf_method, count);

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

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_LobbyManager_memberCount
  (JNIEnv *env, jobject object, jlong pointer, jlong lobbyId)
{
	struct IDiscordLobbyManager *lobby_manager = (struct IDiscordLobbyManager*) pointer;

	int count;

	enum EDiscordResult result = lobby_manager->member_count(lobby_manager, lobbyId, &count);
	if(result == DiscordResult_Ok) // if everything went well, return the count
	{
		jclass Integer_class = (*env)->FindClass(env, "java/lang/Integer");
		jmethodID valueOf_method = (*env)->GetStaticMethodID(env, Integer_class, "valueOf", "(I)Ljava/lang/Integer;");
		jobject return_object = (*env)->CallStaticObjectMethod(env, Integer_class, valueOf_method, count);

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

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_LobbyManager_getMemberUserId
  (JNIEnv *env, jobject object, jlong pointer, jlong lobbyId, jint index)
{
	struct IDiscordLobbyManager *lobby_manager = (struct IDiscordLobbyManager*) pointer;

	DiscordUserId id;
	enum EDiscordResult result = lobby_manager->get_member_user_id(lobby_manager, lobbyId, index, &id);
	if(result == DiscordResult_Ok) // if everything went well, return the id
	{
		jclass Long_class = (*env)->FindClass(env, "java/lang/Long");
		jmethodID valueOf_method = (*env)->GetStaticMethodID(env, Long_class, "valueOf", "(J)Ljava/lang/Long;");
		jobject return_object = (*env)->CallStaticObjectMethod(env, Long_class, valueOf_method, id);

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

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_LobbyManager_getMemberUser
  (JNIEnv *env, jobject object, jlong pointer, jlong lobbyId, jlong userId)
{
	struct IDiscordLobbyManager *lobby_manager = (struct IDiscordLobbyManager*) pointer;

	struct DiscordUser user;
	enum EDiscordResult result = lobby_manager->get_member_user(lobby_manager, lobbyId, userId, &user);
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

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_LobbyManager_getMemberMetadataValue
  (JNIEnv *env, jobject object, jlong pointer, jlong lobbyId, jlong userId, jstring key)
{
	struct IDiscordLobbyManager *lobby_manager = (struct IDiscordLobbyManager*) pointer;

	const char *nativeKey = (*env)->GetStringUTFChars(env, key, 0);
	DiscordMetadataKey metadata_key;
	strcpy(metadata_key, nativeKey);
	(*env)->ReleaseStringUTFChars(env, key, nativeKey);

	DiscordMetadataValue metadata_value;

	enum EDiscordResult result = lobby_manager->get_member_metadata_value(lobby_manager, lobbyId, userId, metadata_key, &metadata_value);
	if(result == DiscordResult_Ok) // if everything went well, return the value
	{
		return (*env)->NewStringUTF(env, metadata_value);
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

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_LobbyManager_getMemberMetadataKey
  (JNIEnv *env, jobject object, jlong pointer, jlong lobbyId, jlong userId, jint index)
{
	struct IDiscordLobbyManager *lobby_manager = (struct IDiscordLobbyManager*) pointer;

	DiscordMetadataKey metadata_key;

	enum EDiscordResult result = lobby_manager->get_member_metadata_key(lobby_manager, lobbyId, userId, index, &metadata_key);
	if(result == DiscordResult_Ok) // if everything went well, return the key
	{
		return (*env)->NewStringUTF(env, metadata_key);
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

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_LobbyManager_memberMetadataCount
  (JNIEnv *env, jobject object, jlong pointer, jlong lobbyId, jlong userId)
{
	struct IDiscordLobbyManager *lobby_manager = (struct IDiscordLobbyManager*) pointer;

	int count;

	enum EDiscordResult result = lobby_manager->member_metadata_count(lobby_manager, lobbyId, userId, &count);
	if(result == DiscordResult_Ok) // if everything went well, return the count
	{
		jclass Integer_class = (*env)->FindClass(env, "java/lang/Integer");
		jmethodID valueOf_method = (*env)->GetStaticMethodID(env, Integer_class, "valueOf", "(I)Ljava/lang/Integer;");
		jobject return_object = (*env)->CallStaticObjectMethod(env, Integer_class, valueOf_method, count);

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

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_LobbyManager_updateMember
  (JNIEnv *env, jobject object, jlong pointer, jlong lobbyId, jlong userId, jlong transactionPointer, jobject callback)
{
	struct IDiscordLobbyManager *lobby_manager = (struct IDiscordLobbyManager*) pointer;
	struct IDiscordLobbyMemberTransaction *transaction = (struct IDiscordLobbyMemberTransaction*) transactionPointer;

	struct CallbackData* cbd = malloc(sizeof(struct CallbackData));
	prepare_callback_data(env, callback, cbd);

	lobby_manager->update_member(lobby_manager, lobbyId, userId, transaction, cbd, simple_callback);
}

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_LobbyManager_getSearchQuery
  (JNIEnv *env, jobject object, jlong pointer)
{
	struct IDiscordLobbyManager *lobby_manager = (struct IDiscordLobbyManager*) pointer;
	struct IDiscordLobbySearchQuery *query;

	enum EDiscordResult result = lobby_manager->get_search_query(lobby_manager, &query);
	if(result == DiscordResult_Ok) // if everything went well, return the transaction
	{
		jclass query_class = (*env)->FindClass(env, "de/jcm/discordgamesdk/lobby/LobbySearchQuery");
		jmethodID query_constructor = (*env)->GetMethodID(env, query_class, "<init>", "(J)V");
		jobject query_object = (*env)->NewObject(env, query_class, query_constructor,
			(uint64_t) query);
		return query_object;
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

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_LobbyManager_search
  (JNIEnv *env, jobject object, jlong pointer, jlong queryPointer, jobject callback)
{
	struct IDiscordLobbyManager *lobby_manager = (struct IDiscordLobbyManager*) pointer;
	struct IDiscordLobbySearchQuery *query= (struct IDiscordLobbySearchQuery*) queryPointer;

	struct CallbackData* cbd = malloc(sizeof(struct CallbackData));
	prepare_callback_data(env, callback, cbd);

	lobby_manager->search(lobby_manager, query, cbd, simple_callback);
}

JNIEXPORT jint JNICALL Java_de_jcm_discordgamesdk_LobbyManager_lobbyCount
  (JNIEnv *env, jobject object, jlong pointer)
{
	struct IDiscordLobbyManager *lobby_manager = (struct IDiscordLobbyManager*) pointer;

	int count;
	lobby_manager->lobby_count(lobby_manager, &count);
	return count;
}

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_LobbyManager_getLobbyId
  (JNIEnv *env, jobject object, jlong pointer, jint index)
{
	struct IDiscordLobbyManager *lobby_manager = (struct IDiscordLobbyManager*) pointer;

	DiscordLobbyId id;
	enum EDiscordResult result = lobby_manager->get_lobby_id(lobby_manager, index, &id);
	if(result == DiscordResult_Ok) // if everything went well, return the id
	{
		jclass Long_class = (*env)->FindClass(env, "java/lang/Long");
		jmethodID valueOf_method = (*env)->GetStaticMethodID(env, Long_class, "valueOf", "(J)Ljava/lang/Long;");
		jobject return_object = (*env)->CallStaticObjectMethod(env, Long_class, valueOf_method, id);

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

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_LobbyManager_connectVoice
  (JNIEnv *env, jobject object, jlong pointer, jlong lobbyId, jobject callback)
{
	struct IDiscordLobbyManager *lobby_manager = (struct IDiscordLobbyManager*) pointer;

	struct CallbackData* cbd = malloc(sizeof(struct CallbackData));
	prepare_callback_data(env, callback, cbd);

	lobby_manager->connect_voice(lobby_manager, lobbyId, cbd, simple_callback);
}

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_LobbyManager_disconnectVoice
  (JNIEnv *env, jobject object, jlong pointer, jlong lobbyId, jobject callback)
{
	struct IDiscordLobbyManager *lobby_manager = (struct IDiscordLobbyManager*) pointer;

	struct CallbackData* cbd = malloc(sizeof(struct CallbackData));
	prepare_callback_data(env, callback, cbd);

	lobby_manager->disconnect_voice(lobby_manager, lobbyId, cbd, simple_callback);
}

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_LobbyManager_connectNetwork
  (JNIEnv *env, jobject object, jlong pointer, jlong lobbyId)
{
	struct IDiscordLobbyManager *lobby_manager = (struct IDiscordLobbyManager*) pointer;

	enum EDiscordResult result = lobby_manager->connect_network(lobby_manager, lobbyId);

	jclass result_clazz = (*env)->FindClass(env, "de/jcm/discordgamesdk/Result");
	jmethodID values_method = (*env)->GetStaticMethodID(env, result_clazz, "values", "()[Lde/jcm/discordgamesdk/Result;");
	jobjectArray values = (jobjectArray) (*env)->CallStaticObjectMethod(env, result_clazz, values_method);
	jobject result_object = (*env)->GetObjectArrayElement(env, values, result);

	return result_object;
}

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_LobbyManager_disconnectNetwork
  (JNIEnv *env, jobject object, jlong pointer, jlong lobbyId)
{
	struct IDiscordLobbyManager *lobby_manager = (struct IDiscordLobbyManager*) pointer;

	enum EDiscordResult result = lobby_manager->disconnect_network(lobby_manager, lobbyId);

	jclass result_clazz = (*env)->FindClass(env, "de/jcm/discordgamesdk/Result");
	jmethodID values_method = (*env)->GetStaticMethodID(env, result_clazz, "values", "()[Lde/jcm/discordgamesdk/Result;");
	jobjectArray values = (jobjectArray) (*env)->CallStaticObjectMethod(env, result_clazz, values_method);
	jobject result_object = (*env)->GetObjectArrayElement(env, values, result);

	return result_object;
}

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_LobbyManager_flushNetwork
  (JNIEnv *env, jobject object, jlong pointer)
{
	struct IDiscordLobbyManager *lobby_manager = (struct IDiscordLobbyManager*) pointer;

	enum EDiscordResult result = lobby_manager->flush_network(lobby_manager);

	jclass result_clazz = (*env)->FindClass(env, "de/jcm/discordgamesdk/Result");
	jmethodID values_method = (*env)->GetStaticMethodID(env, result_clazz, "values", "()[Lde/jcm/discordgamesdk/Result;");
	jobjectArray values = (jobjectArray) (*env)->CallStaticObjectMethod(env, result_clazz, values_method);
	jobject result_object = (*env)->GetObjectArrayElement(env, values, result);

	return result_object;
}

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_LobbyManager_openNetworkChannel
  (JNIEnv *env, jobject object, jlong pointer, jlong lobbyId, jbyte channelId, jboolean reliable)
{
	struct IDiscordLobbyManager *lobby_manager = (struct IDiscordLobbyManager*) pointer;

	enum EDiscordResult result = lobby_manager->open_network_channel(lobby_manager, lobbyId, channelId, reliable);

	jclass result_clazz = (*env)->FindClass(env, "de/jcm/discordgamesdk/Result");
	jmethodID values_method = (*env)->GetStaticMethodID(env, result_clazz, "values", "()[Lde/jcm/discordgamesdk/Result;");
	jobjectArray values = (jobjectArray) (*env)->CallStaticObjectMethod(env, result_clazz, values_method);
	jobject result_object = (*env)->GetObjectArrayElement(env, values, result);

	return result_object;
}

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_LobbyManager_sendNetworkMessage
  (JNIEnv *env, jobject object, jlong pointer, jlong lobbyId, jlong userId, jbyte channelId, jbyteArray array, jint offset, jint length)
{
	struct IDiscordLobbyManager *lobby_manager = (struct IDiscordLobbyManager*) pointer;

	uint8_t* data = (uint8_t*) malloc(length);
	(*env)->GetByteArrayRegion(env, array, offset, length, data);

	enum EDiscordResult result = lobby_manager->send_network_message(lobby_manager, lobbyId, userId, channelId, data, length);

	free(data);

	jclass result_clazz = (*env)->FindClass(env, "de/jcm/discordgamesdk/Result");
	jmethodID values_method = (*env)->GetStaticMethodID(env, result_clazz, "values", "()[Lde/jcm/discordgamesdk/Result;");
	jobjectArray values = (jobjectArray) (*env)->CallStaticObjectMethod(env, result_clazz, values_method);
	jobject result_object = (*env)->GetObjectArrayElement(env, values, result);

	return result_object;
}