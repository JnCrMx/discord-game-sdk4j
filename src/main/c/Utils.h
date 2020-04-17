#ifndef UTILS_H
#define UTILS_H

#include <stdlib.h>
#include <discord_game_sdk.h>

jobject create_java_relationship(JNIEnv *env, struct DiscordRelationship relationship);

#endif