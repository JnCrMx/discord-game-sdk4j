#include <stdlib.h>
#include <discord_game_sdk.h>

#include "de_jcm_discordgamesdk_ImageManager.h"
#include "Callback.h"

void fill_handle(struct DiscordImageHandle *handle, enum EDiscordImageType type, int64_t id, uint32_t size)
{
	handle->type = type;
	handle->id = id;
	handle->size = size;
}

void image_callback(void* data, enum EDiscordResult result, struct DiscordImageHandle handle)
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
	
	// prepare image handle
	jobject handle_object = NULL;
	jclass handle_class = (*env)->FindClass(env, "de/jcm/discordgamesdk/image/ImageHandle");
	jmethodID handle_constructor = (*env)->GetMethodID(env, handle_class, "<init>", 
		"(IJI)V");
	handle_object = (*env)->NewObject(env, handle_class, handle_constructor,
		handle.type,
		handle.id,
		handle.size);
	
	// call the callback
	jclass clazz = (*env)->GetObjectClass(env, cbd->callback);
	
	jmethodID method = (*env)->GetMethodID(env, clazz, "accept", "(Ljava/lang/Object;Ljava/lang/Object;)V");
	(*env)->CallVoidMethod(env, cbd->callback, method, result_object, handle_object);
	
	(*env)->DeleteGlobalRef(env, cbd->callback);
	(*(cbd->jvm))->DetachCurrentThread(cbd->jvm);
}

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_ImageManager_fetch(JNIEnv *env, jobject object, jlong pointer,
	jint type, jlong id, jint size, jboolean refresh, jobject callback)
{
	if(!pointer)
	{
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/NullPointerException"), "reference is null");
		return;
	}
	
	struct IDiscordImageManager *image_manager = (struct IDiscordImageManager*) pointer;
	
	struct DiscordImageHandle handle;
	fill_handle(&handle, type, id, size);
	
	struct CallbackData* cbd = malloc(sizeof(struct CallbackData));
	prepare_callback_data(env, callback, cbd);
	
	image_manager->fetch(image_manager, handle, refresh, cbd, image_callback);
}

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_ImageManager_getDimensions(JNIEnv *env, jobject object, jlong pointer,
	jint type, jlong id, jint size)
{
	if(!pointer)
	{
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/NullPointerException"), "reference is null");
		return NULL;
	}
	
	struct IDiscordImageManager *image_manager = (struct IDiscordImageManager*) pointer;
	
	struct DiscordImageHandle handle;
	fill_handle(&handle, type, id, size);
	
	struct DiscordImageDimensions dimensions;
	enum EDiscordResult result = image_manager->get_dimensions(image_manager, handle, &dimensions);
	
	if(result == DiscordResult_Ok) // if everything went well, return the dimensions
	{
		jclass dimensions_class = (*env)->FindClass(env, "de/jcm/discordgamesdk/image/ImageDimensions");
		jmethodID dimensions_constructor = (*env)->GetMethodID(env, dimensions_class, "<init>", "(II)V");
		jobject dimensions_object = (*env)->NewObject(env, dimensions_class, dimensions_constructor,
			dimensions.width, dimensions.height);
		return dimensions_object;
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

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_ImageManager_getData(JNIEnv *env, jobject object, jlong pointer,
	jint type, jlong id, jint size, jint data_length)
{
	if(!pointer)
	{
		(*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/NullPointerException"), "reference is null");
		return NULL;
	}
	
	struct IDiscordImageManager *image_manager = (struct IDiscordImageManager*) pointer;
	
	struct DiscordImageHandle handle;
	fill_handle(&handle, type, id, size);
	
	uint8_t* data = malloc(data_length);
	enum EDiscordResult result = image_manager->get_data(image_manager, handle, data, data_length);
	if(result == DiscordResult_Ok) // if everything went well, return the image data
	{
		jbyteArray data_array = (*env)->NewByteArray(env, data_length);
		(*env)->SetByteArrayRegion(env, data_array, 0, data_length, data);
		
		free(data); // free the allocated space after copying the data to the array
		
		return data_array;
	}
	else // otherwise return the result
	{
		free(data); // free the allocated space if operation failed
		
		jclass result_clazz = (*env)->FindClass(env, "de/jcm/discordgamesdk/Result");
		jmethodID values_method = (*env)->GetStaticMethodID(env, result_clazz, "values", "()[Lde/jcm/discordgamesdk/Result;");
		jobjectArray values = (jobjectArray) (*env)->CallStaticObjectMethod(env, result_clazz, values_method);
		jobject result_object = (*env)->GetObjectArrayElement(env, values, result);

		return result_object;
	}
}