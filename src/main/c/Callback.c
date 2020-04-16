#include <stdlib.h>

#include "Callback.h"

void simple_callback(void* data, enum EDiscordResult result)
{
	struct CallbackData* cbd = (struct CallbackData*)data;
	
	JNIEnv* env;
	JavaVMAttachArgs args;
	args.version = JNI_VERSION_1_6;
	args.name = NULL;
	args.group = NULL;
	(*(cbd->jvm))->AttachCurrentThread(cbd->jvm, (void**)&env, &args);
	
	jclass result_clazz = (*env)->FindClass(env, "de/jcm/discordgamesdk/Result");
	jmethodID values_method = (*env)->GetStaticMethodID(env, result_clazz, "values", "()[Lde/jcm/discordgamesdk/Result;");
	jobjectArray values = (jobjectArray) (*env)->CallStaticObjectMethod(env, result_clazz, values_method);
	jobject result_object = (*env)->GetObjectArrayElement(env, values, result);
	
	jclass clazz = (*env)->GetObjectClass(env, cbd->callback);
	
	jmethodID method = (*env)->GetMethodID(env, clazz, "accept", "(Ljava/lang/Object;)V");
	(*env)->CallVoidMethod(env, cbd->callback, method, result_object);
	
	(*env)->DeleteGlobalRef(env, cbd->callback);
	
	(*(cbd->jvm))->DetachCurrentThread(cbd->jvm);
}

void prepare_callback_data(JNIEnv *env, jobject callback, struct CallbackData* cbd)
{
	JavaVM* jvm = malloc(sizeof(jvm));
	(*env)->GetJavaVM(env, &jvm);
	
	cbd->jvm = jvm;
	cbd->callback = (*env)->NewGlobalRef(env, callback);
}
