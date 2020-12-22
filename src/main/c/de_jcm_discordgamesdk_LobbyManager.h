/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class de_jcm_discordgamesdk_LobbyManager */

#ifndef _Included_de_jcm_discordgamesdk_LobbyManager
#define _Included_de_jcm_discordgamesdk_LobbyManager
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     de_jcm_discordgamesdk_LobbyManager
 * Method:    getLobbyCreateTransaction
 * Signature: (J)Ljava/lang/Object;
 */
JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_LobbyManager_getLobbyCreateTransaction
  (JNIEnv *, jobject, jlong);

/*
 * Class:     de_jcm_discordgamesdk_LobbyManager
 * Method:    getLobbyUpdateTransaction
 * Signature: (JJ)Ljava/lang/Object;
 */
JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_LobbyManager_getLobbyUpdateTransaction
  (JNIEnv *, jobject, jlong, jlong);

/*
 * Class:     de_jcm_discordgamesdk_LobbyManager
 * Method:    createLobby
 * Signature: (JJLjava/util/function/BiConsumer;)V
 */
JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_LobbyManager_createLobby
  (JNIEnv *, jobject, jlong, jlong, jobject);

/*
 * Class:     de_jcm_discordgamesdk_LobbyManager
 * Method:    updateLobby
 * Signature: (JJJLjava/util/function/Consumer;)V
 */
JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_LobbyManager_updateLobby
  (JNIEnv *, jobject, jlong, jlong, jlong, jobject);

/*
 * Class:     de_jcm_discordgamesdk_LobbyManager
 * Method:    deleteLobby
 * Signature: (JJLjava/util/function/Consumer;)V
 */
JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_LobbyManager_deleteLobby
  (JNIEnv *, jobject, jlong, jlong, jobject);

/*
 * Class:     de_jcm_discordgamesdk_LobbyManager
 * Method:    connectLobby
 * Signature: (JJLjava/lang/String;Ljava/util/function/BiConsumer;)V
 */
JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_LobbyManager_connectLobby
  (JNIEnv *, jobject, jlong, jlong, jstring, jobject);

/*
 * Class:     de_jcm_discordgamesdk_LobbyManager
 * Method:    connectLobbyWithActivitySecret
 * Signature: (JLjava/lang/String;Ljava/util/function/BiConsumer;)V
 */
JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_LobbyManager_connectLobbyWithActivitySecret
  (JNIEnv *, jobject, jlong, jstring, jobject);

/*
 * Class:     de_jcm_discordgamesdk_LobbyManager
 * Method:    disconnectLobby
 * Signature: (JJLjava/util/function/Consumer;)V
 */
JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_LobbyManager_disconnectLobby
  (JNIEnv *, jobject, jlong, jlong, jobject);

/*
 * Class:     de_jcm_discordgamesdk_LobbyManager
 * Method:    getLobby
 * Signature: (JJ)Ljava/lang/Object;
 */
JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_LobbyManager_getLobby
  (JNIEnv *, jobject, jlong, jlong);

/*
 * Class:     de_jcm_discordgamesdk_LobbyManager
 * Method:    getLobbyActivitySecret
 * Signature: (JJ)Ljava/lang/Object;
 */
JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_LobbyManager_getLobbyActivitySecret
  (JNIEnv *, jobject, jlong, jlong);

/*
 * Class:     de_jcm_discordgamesdk_LobbyManager
 * Method:    getLobbyMetadataValue
 * Signature: (JJLjava/lang/String;)Ljava/lang/Object;
 */
JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_LobbyManager_getLobbyMetadataValue
  (JNIEnv *, jobject, jlong, jlong, jstring);

/*
 * Class:     de_jcm_discordgamesdk_LobbyManager
 * Method:    getSearchQuery
 * Signature: (J)Ljava/lang/Object;
 */
JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_LobbyManager_getSearchQuery
  (JNIEnv *, jobject, jlong);

/*
 * Class:     de_jcm_discordgamesdk_LobbyManager
 * Method:    search
 * Signature: (JJLjava/util/function/Consumer;)V
 */
JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_LobbyManager_search
  (JNIEnv *, jobject, jlong, jlong, jobject);

/*
 * Class:     de_jcm_discordgamesdk_LobbyManager
 * Method:    lobbyCount
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_de_jcm_discordgamesdk_LobbyManager_lobbyCount
  (JNIEnv *, jobject, jlong);

/*
 * Class:     de_jcm_discordgamesdk_LobbyManager
 * Method:    getLobbyId
 * Signature: (JI)Ljava/lang/Object;
 */
JNIEXPORT jobject JNICALL Java_de_jcm_discordgamesdk_LobbyManager_getLobbyId
  (JNIEnv *, jobject, jlong, jint);

/*
 * Class:     de_jcm_discordgamesdk_LobbyManager
 * Method:    connectVoice
 * Signature: (JJLjava/util/function/Consumer;)V
 */
JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_LobbyManager_connectVoice
  (JNIEnv *, jobject, jlong, jlong, jobject);

/*
 * Class:     de_jcm_discordgamesdk_LobbyManager
 * Method:    disconnectVoice
 * Signature: (JJLjava/util/function/Consumer;)V
 */
JNIEXPORT void JNICALL Java_de_jcm_discordgamesdk_LobbyManager_disconnectVoice
  (JNIEnv *, jobject, jlong, jlong, jobject);

#ifdef __cplusplus
}
#endif
#endif