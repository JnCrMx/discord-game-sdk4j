#ifndef CALLBACK_H
#define CALLBACK_H

#include <jni.h>
#include <discord_game_sdk.h>

struct CallbackData {
	JavaVM* jvm;
	jobject callback;
};

void simple_callback(void* data, enum EDiscordResult result);
void prepare_callback_data(JNIEnv *env, jobject callback, struct CallbackData* cbd);

#endif