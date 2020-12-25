#include <stdlib.h>
#include <discord_game_sdk.h>

#include "de_jcm_discordgamesdk_CreateParams.h"
#include "EventHandler.h"

JNIEXPORT jlong JNICALL Java_de_jcm_discordgamesdk_CreateParams_allocate(JNIEnv *env, jobject object)
{
	struct DiscordCreateParams *params = malloc(sizeof(struct DiscordCreateParams));
	DiscordCreateParamsSetDefault(params);
	return (uint64_t) params;
}

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_CreateParams_free(JNIEnv *env, jobject object, jlong pointer)
{
	struct DiscordCreateParams *params = (struct DiscordCreateParams*) pointer;
	
	if(params->activity_events)
		free(params->activity_events);
	if(params->user_events)
		free(params->user_events);
	if(params->overlay_events)
		free(params->overlay_events);
	if(params->lobby_events)
		free(params->lobby_events);
	free(params);
}

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_CreateParams_setClientID(JNIEnv *env, jobject object, jlong pointer, jlong client_id)
{
	struct DiscordCreateParams *params = (struct DiscordCreateParams*) pointer;
	params->client_id = client_id;
}

JNIEXPORT jlong JNICALL Java_de_jcm_discordgamesdk_CreateParams_getClientID(JNIEnv *env, jobject object, jlong pointer)
{
	struct DiscordCreateParams *params = (struct DiscordCreateParams*) pointer;
	return params->client_id;
}

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_CreateParams_setFlags(JNIEnv *env, jobject object, jlong pointer, jlong flags)
{
	struct DiscordCreateParams *params = (struct DiscordCreateParams*) pointer;
	params->flags = flags;
}

JNIEXPORT jlong JNICALL Java_de_jcm_discordgamesdk_CreateParams_getFlags(JNIEnv *env, jobject object, jlong pointer)
{
	struct DiscordCreateParams *params = (struct DiscordCreateParams*) pointer;
	return params->flags;
}

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_CreateParams_registerEventHandler(JNIEnv *env, jobject object, jlong pointer, jobject handler)
{
	struct DiscordCreateParams *params = (struct DiscordCreateParams*) pointer;
	
	// event_data
	JavaVM* jvm = malloc(sizeof(jvm));
	(*env)->GetJavaVM(env, &jvm);
	
	struct EventData* event_data = malloc(sizeof(struct EventData));
	event_data->jvm = jvm;
	event_data->handler = (*env)->NewGlobalRef(env, handler);
	
	params->event_data = event_data;
	
	// activities events
	struct IDiscordActivityEvents *activity_events = malloc(sizeof(struct IDiscordActivityEvents));
	memset(activity_events, 0, sizeof(struct IDiscordActivityEvents));
	
	activity_events->on_activity_join = on_activity_join;
	activity_events->on_activity_spectate = on_activity_spectate;
	activity_events->on_activity_join_request = on_activity_join_request;
	
	params->activity_events = activity_events;
	
	// user_events
	struct IDiscordUserEvents *user_events = malloc(sizeof(struct IDiscordUserEvents));
	memset(user_events, 0, sizeof(struct IDiscordUserEvents));
	
	user_events->on_current_user_update = on_current_user_update;
	
	params->user_events = user_events;

	// overlay_events
	struct IDiscordOverlayEvents *overlay_events = malloc(sizeof(struct IDiscordOverlayEvents));
	memset(overlay_events, 0, sizeof(struct IDiscordOverlayEvents));

	overlay_events->on_toggle = on_overlay_toggle;

	params->overlay_events = overlay_events;

	// relationship_events
	struct IDiscordRelationshipEvents *relationship_events = malloc(sizeof(struct IDiscordRelationshipEvents));
	memset(relationship_events, 0, sizeof(struct IDiscordRelationshipEvents));

	relationship_events->on_refresh = on_relationship_refresh;
	relationship_events->on_relationship_update = on_relationship_update;

	params->relationship_events = relationship_events;

	// lobby_events
	struct IDiscordLobbyEvents *lobby_events = malloc(sizeof(struct IDiscordLobbyEvents));
	memset(lobby_events, 0, sizeof(struct IDiscordLobbyEvents));

	lobby_events->on_lobby_update = on_lobby_update;
	lobby_events->on_lobby_delete = on_lobby_delete;
	lobby_events->on_member_connect = on_member_connect;
	lobby_events->on_member_update = on_member_update;
	lobby_events->on_member_disconnect = on_member_disconnect;
	lobby_events->on_network_message = on_network_message;

	params->lobby_events = lobby_events;
}

JNIEXPORT jlong JNICALL Java_de_jcm_discordgamesdk_CreateParams_getDefaultFlags(JNIEnv *env, jclass clazz)
{
	return DiscordCreateFlags_Default;
}
