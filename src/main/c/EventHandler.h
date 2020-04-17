#ifndef EVENT_HANDLER_H
#define EVENT_HANDLER_H

struct EventData {
	JavaVM* jvm;
	jobject handler;
};

// activity_events
void on_activity_join(void* event_data, const char* secret);
void on_activity_spectate(void* event_data, const char* secret);
void on_activity_join_request(void* event_data, struct DiscordUser* user);

// user_events
void on_current_user_update(void* event_data);

// overlay_events
void on_overlay_toggle(void* event_data, bool locked);

// relationship_events
void on_relationship_refresh(void* event_data);
void on_relationship_update(void* event_data, struct DiscordRelationship* relationship);

#endif