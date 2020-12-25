#include <discord_game_sdk.h>
#include <jni.h>

#include "EventHandler.h"
#include "Utils.h"

// activity_events
void on_activity_join(void* event_data, const char* secret)
{
	struct EventData* event_struct = (struct EventData*)event_data;
	
	JNIEnv* env;
	JavaVMAttachArgs args;
	args.version = JNI_VERSION_1_6;
	args.name = NULL;
	args.group = NULL;
	(*(event_struct->jvm))->AttachCurrentThread(event_struct->jvm, (void**)&env, &args);
	
	jstring secret_string = (*env)->NewStringUTF(env, secret);
	
	jclass clazz = (*env)->GetObjectClass(env, event_struct->handler);
	jmethodID method = (*env)->GetMethodID(env, clazz, "onActivityJoin", "(Ljava/lang/String;)V");
	(*env)->CallVoidMethod(env, event_struct->handler, method, secret_string);
	
	(*(event_struct->jvm))->DetachCurrentThread(event_struct->jvm);
}

void on_activity_spectate(void* event_data, const char* secret)
{
	struct EventData* event_struct = (struct EventData*)event_data;
	
	JNIEnv* env;
	JavaVMAttachArgs args;
	args.version = JNI_VERSION_1_6;
	args.name = NULL;
	args.group = NULL;
	(*(event_struct->jvm))->AttachCurrentThread(event_struct->jvm, (void**)&env, &args);
	
	jstring secret_string = (*env)->NewStringUTF(env, secret);
	
	jclass clazz = (*env)->GetObjectClass(env, event_struct->handler);
	jmethodID method = (*env)->GetMethodID(env, clazz, "onActivitySpectate", "(Ljava/lang/String;)V");
	(*env)->CallVoidMethod(env, event_struct->handler, method, secret_string);
	
	(*(event_struct->jvm))->DetachCurrentThread(event_struct->jvm);
}

void on_activity_join_request(void* event_data, struct DiscordUser* user)
{
	struct EventData* event_struct = (struct EventData*)event_data;
	
	JNIEnv* env;
	JavaVMAttachArgs args;
	args.version = JNI_VERSION_1_6;
	args.name = NULL;
	args.group = NULL;
	(*(event_struct->jvm))->AttachCurrentThread(event_struct->jvm, (void**)&env, &args);
	
	jclass user_class = (*env)->FindClass(env, "de/jcm/discordgamesdk/user/DiscordUser");
	jmethodID user_constructor = (*env)->GetMethodID(env, user_class, "<init>", 
		"(JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)V");
	jobject user_object = (*env)->NewObject(env, user_class, user_constructor,
		user->id,
		(*env)->NewStringUTF(env, user->username),
		(*env)->NewStringUTF(env, user->discriminator),
		(*env)->NewStringUTF(env, user->avatar),
		user->bot);
	
	jclass clazz = (*env)->GetObjectClass(env, event_struct->handler);
	jmethodID method = (*env)->GetMethodID(env, clazz, "onActivityJoinRequest", "(Lde/jcm/discordgamesdk/user/DiscordUser;)V");
	(*env)->CallVoidMethod(env, event_struct->handler, method, user_object);
	
	(*(event_struct->jvm))->DetachCurrentThread(event_struct->jvm);
}

// user_events
void on_current_user_update(void* event_data)
{
	struct EventData* event_struct = (struct EventData*)event_data;
	
	JNIEnv* env;
	JavaVMAttachArgs args;
	args.version = JNI_VERSION_1_6;
	args.name = NULL;
	args.group = NULL;
	(*(event_struct->jvm))->AttachCurrentThread(event_struct->jvm, (void**)&env, &args);
	
	jclass clazz = (*env)->GetObjectClass(env, event_struct->handler);
	jmethodID method = (*env)->GetMethodID(env, clazz, "onCurrentUserUpdate", "()V");
	(*env)->CallVoidMethod(env, event_struct->handler, method);
	
	(*(event_struct->jvm))->DetachCurrentThread(event_struct->jvm);
}

// overlay_events
void on_overlay_toggle(void* event_data, bool locked)
{
	struct EventData* event_struct = (struct EventData*)event_data;

	JNIEnv* env;
	JavaVMAttachArgs args;
	args.version = JNI_VERSION_1_6;
	args.name = NULL;
	args.group = NULL;
	(*(event_struct->jvm))->AttachCurrentThread(event_struct->jvm, (void**)&env, &args);

	jclass clazz = (*env)->GetObjectClass(env, event_struct->handler);
	jmethodID method = (*env)->GetMethodID(env, clazz, "onOverlayToggle", "(Z)V");
	(*env)->CallVoidMethod(env, event_struct->handler, method, locked);

	(*(event_struct->jvm))->DetachCurrentThread(event_struct->jvm);
}

// relationship_events
void on_relationship_refresh(void* event_data)
{
	struct EventData* event_struct = (struct EventData*)event_data;

	JNIEnv* env;
	JavaVMAttachArgs args;
	args.version = JNI_VERSION_1_6;
	args.name = NULL;
	args.group = NULL;
	(*(event_struct->jvm))->AttachCurrentThread(event_struct->jvm, (void**)&env, &args);

	jclass clazz = (*env)->GetObjectClass(env, event_struct->handler);
	jmethodID method = (*env)->GetMethodID(env, clazz, "onRelationshipRefresh", "()V");
	(*env)->CallVoidMethod(env, event_struct->handler, method);

	(*(event_struct->jvm))->DetachCurrentThread(event_struct->jvm);
}

void on_relationship_update(void* event_data, struct DiscordRelationship* relationship)
{
	struct EventData* event_struct = (struct EventData*)event_data;

	JNIEnv* env;
	JavaVMAttachArgs args;
	args.version = JNI_VERSION_1_6;
	args.name = NULL;
	args.group = NULL;
	(*(event_struct->jvm))->AttachCurrentThread(event_struct->jvm, (void**)&env, &args);

	jobject relationship_object = create_java_relationship(env, *relationship);

	jclass clazz = (*env)->GetObjectClass(env, event_struct->handler);
	jmethodID method = (*env)->GetMethodID(env, clazz, "onRelationshipUpdate", "(Lde/jcm/discordgamesdk/user/Relationship;)V");
	(*env)->CallVoidMethod(env, event_struct->handler, method, relationship_object);

	(*(event_struct->jvm))->DetachCurrentThread(event_struct->jvm);
}

// lobby_events
void on_lobby_update(void* event_data, long lobby_id)
{
	struct EventData* event_struct = (struct EventData*)event_data;

	JNIEnv* env;
	JavaVMAttachArgs args;
	args.version = JNI_VERSION_1_6;
	args.name = NULL;
	args.group = NULL;
	(*(event_struct->jvm))->AttachCurrentThread(event_struct->jvm, (void**)&env, &args);

	jclass clazz = (*env)->GetObjectClass(env, event_struct->handler);
	jmethodID method = (*env)->GetMethodID(env, clazz, "onLobbyUpdate", "(J)V");
	(*env)->CallVoidMethod(env, event_struct->handler, method, lobby_id);

	(*(event_struct->jvm))->DetachCurrentThread(event_struct->jvm);
}

void on_lobby_delete(void* event_data, long lobby_id, int reason)
{
	struct EventData* event_struct = (struct EventData*)event_data;

	JNIEnv* env;
	JavaVMAttachArgs args;
	args.version = JNI_VERSION_1_6;
	args.name = NULL;
	args.group = NULL;
	(*(event_struct->jvm))->AttachCurrentThread(event_struct->jvm, (void**)&env, &args);

	jclass clazz = (*env)->GetObjectClass(env, event_struct->handler);
	jmethodID method = (*env)->GetMethodID(env, clazz, "onLobbyDelete", "(JI)V");
	(*env)->CallVoidMethod(env, event_struct->handler, method, lobby_id, reason);

	(*(event_struct->jvm))->DetachCurrentThread(event_struct->jvm);
}

void on_member_connect(void* event_data, long lobby_id, long user_id)
{
	struct EventData* event_struct = (struct EventData*)event_data;

	JNIEnv* env;
	JavaVMAttachArgs args;
	args.version = JNI_VERSION_1_6;
	args.name = NULL;
	args.group = NULL;
	(*(event_struct->jvm))->AttachCurrentThread(event_struct->jvm, (void**)&env, &args);

	jclass clazz = (*env)->GetObjectClass(env, event_struct->handler);
	jmethodID method = (*env)->GetMethodID(env, clazz, "onMemberConnect", "(JJ)V");
	(*env)->CallVoidMethod(env, event_struct->handler, method, lobby_id, user_id);

	(*(event_struct->jvm))->DetachCurrentThread(event_struct->jvm);
}

void on_member_update(void* event_data, long lobby_id, long user_id)
{
	struct EventData* event_struct = (struct EventData*)event_data;

	JNIEnv* env;
	JavaVMAttachArgs args;
	args.version = JNI_VERSION_1_6;
	args.name = NULL;
	args.group = NULL;
	(*(event_struct->jvm))->AttachCurrentThread(event_struct->jvm, (void**)&env, &args);

	jclass clazz = (*env)->GetObjectClass(env, event_struct->handler);
	jmethodID method = (*env)->GetMethodID(env, clazz, "onMemberUpdate", "(JJ)V");
	(*env)->CallVoidMethod(env, event_struct->handler, method, lobby_id, user_id);

	(*(event_struct->jvm))->DetachCurrentThread(event_struct->jvm);
}

void on_member_disconnect(void* event_data, long lobby_id, long user_id)
{
	struct EventData* event_struct = (struct EventData*)event_data;

	JNIEnv* env;
	JavaVMAttachArgs args;
	args.version = JNI_VERSION_1_6;
	args.name = NULL;
	args.group = NULL;
	(*(event_struct->jvm))->AttachCurrentThread(event_struct->jvm, (void**)&env, &args);

	jclass clazz = (*env)->GetObjectClass(env, event_struct->handler);
	jmethodID method = (*env)->GetMethodID(env, clazz, "onMemberDisconnect", "(JJ)V");
	(*env)->CallVoidMethod(env, event_struct->handler, method, lobby_id, user_id);

	(*(event_struct->jvm))->DetachCurrentThread(event_struct->jvm);
}

void on_network_message(void* event_data, long lobby_id, long user_id, uint8_t channel_id, uint8_t* data, int data_length)
{
	struct EventData* event_struct = (struct EventData*)event_data;

	JNIEnv* env;
	JavaVMAttachArgs args;
	args.version = JNI_VERSION_1_6;
	args.name = NULL;
	args.group = NULL;
	(*(event_struct->jvm))->AttachCurrentThread(event_struct->jvm, (void**)&env, &args);

	jclass clazz = (*env)->GetObjectClass(env, event_struct->handler);
	jmethodID method = (*env)->GetMethodID(env, clazz, "onNetworkMessage", "(JJB[B)V");

	jbyteArray array = (*env)->NewByteArray(env, data_length);
	(*env)->SetByteArrayRegion(env, array, 0, data_length, data);

	(*env)->CallVoidMethod(env, event_struct->handler, method, lobby_id, user_id, channel_id, array);
}
