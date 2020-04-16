#include <stdlib.h>
#include <discord_game_sdk.h>

#include "de_jcm_discordgamesdk_RelationshipManager.h"
#include "Callback.h"

jobject create_java_relationship(JNIEnv *env, struct DiscordRelationship relationship)
{
	jclass relationship_class = (*env)->FindClass(env, "de/jcm/discordgamesdk/Relationship");
	jmethodID relationship_create = (*env)->GetStaticMethodID(env, relationship_class, "createRelationship", 
			"(ILde/jcm/discordgamesdk/DiscordUser;IJ)Lde/jcm/discordgamesdk/Relationship;");
	
	jclass user_class = (*env)->FindClass(env, "de/jcm/discordgamesdk/DiscordUser");
	jmethodID user_constructor = (*env)->GetMethodID(env, user_class, "<init>", 
			"(JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)V");
	jobject user_object = (*env)->NewObject(env, user_class, user_constructor,
			relationship.user.id,
			(*env)->NewStringUTF(env, relationship.user.username),
			(*env)->NewStringUTF(env, relationship.user.discriminator),
			(*env)->NewStringUTF(env, relationship.user.avatar),
			relationship.user.bot);
	
	struct DiscordActivity *activity = malloc(sizeof(struct DiscordActivity));
	memcpy(activity, &relationship.presence.activity, sizeof(struct DiscordActivity));
	
	jobject relationship_object = (*env)->CallStaticObjectMethod(env, relationship_class, relationship_create,
			relationship.type,
			user_object,
			relationship.presence.status,
			(uint64_t) activity);
	// not sure if we need this, but it seems to work
	return (*env)->NewWeakGlobalRef(env, relationship_object);
}

bool filter_predicate(void* filter_data, struct DiscordRelationship* relationship)
{
	struct CallbackData* cbd = (struct CallbackData*)filter_data;
	
	JNIEnv* env;
	JavaVMAttachArgs args;
	args.version = JNI_VERSION_1_6;
	args.name = NULL;
	args.group = NULL;
	(*(cbd->jvm))->AttachCurrentThread(cbd->jvm, (void**)&env, &args);
	
	jobject relationship_object = create_java_relationship(env, *relationship);
	
	jclass clazz = (*env)->GetObjectClass(env, cbd->callback);
	
	jmethodID method = (*env)->GetMethodID(env, clazz, "test", "(Ljava/lang/Object;)Z");
	jboolean filter_result = (*env)->CallBooleanMethod(env, cbd->callback, method, relationship_object);
	
	(*(cbd->jvm))->DetachCurrentThread(cbd->jvm);
	
	return filter_result;
}

JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_RelationshipManager_filter(JNIEnv *env, jobject object, jlong pointer, jobject filter)
{
	struct IDiscordRelationshipManager *relationship_manager = (struct IDiscordRelationshipManager*) pointer;
	
	struct CallbackData* cbd = malloc(sizeof(struct CallbackData));
	prepare_callback_data(env, filter, cbd);
	
	relationship_manager->filter(relationship_manager, cbd, filter_predicate);
}

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_RelationshipManager_count(JNIEnv *env, jobject object, jlong pointer)
{
	struct IDiscordRelationshipManager *relationship_manager = (struct IDiscordRelationshipManager*) pointer;
	
	int count;
	enum EDiscordResult result = relationship_manager->count(relationship_manager, &count);
	
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

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_RelationshipManager_get(JNIEnv *env, jobject object, jlong pointer, jlong userId)
{
	struct IDiscordRelationshipManager *relationship_manager = (struct IDiscordRelationshipManager*) pointer;
	
	struct DiscordRelationship relationship;
	enum EDiscordResult result = relationship_manager->get(relationship_manager, userId, &relationship);
	
	if(result == DiscordResult_Ok) // if everything went well, return the relationship
	{
		return create_java_relationship(env, relationship);
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

JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_RelationshipManager_getAt(JNIEnv *env, jobject object, jlong pointer, jint index)
{
	struct IDiscordRelationshipManager *relationship_manager = (struct IDiscordRelationshipManager*) pointer;
	
	struct DiscordRelationship relationship;
	enum EDiscordResult result = relationship_manager->get_at(relationship_manager, index, &relationship);
	
	if(result == DiscordResult_Ok) // if everything went well, return the relationship
	{
		return create_java_relationship(env, relationship);
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