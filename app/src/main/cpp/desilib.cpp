#include <jni.h>
#include <string>
#include "ESP.h"
#include "Hacks.h"

ESP espOverlay;
int type=1;
int utype=2;

extern "C" JNIEXPORT void JNICALL
Java_mobisocial_arcade_ESPView_DrawOn(JNIEnv *env, jclass , jobject espView, jobject canvas) {
    espOverlay = ESP(env, espView, canvas);
    if (espOverlay.isValid()){
        DrawESP(espOverlay, espOverlay.getWidth(), espOverlay.getHeight());
    }
}

extern "C" JNIEXPORT void JNICALL
Java_mobisocial_arcade_FloatLogo_Close(JNIEnv *,  jobject ) {
    Close();
}

extern "C" JNIEXPORT void JNICALL
Java_mobisocial_arcade_FloatLogo_PremiumValue(JNIEnv *,  jobject ,jint code,jboolean jboolean1) {
    switch((int)code){
        case 1:
            isPlayerBox = jboolean1;   break;
        case 2:
            isPlayerLine = jboolean1;  break;
        case 3:
            isPlayerDist = jboolean1;  break;
        case 4:
            isPlayerHealth = jboolean1;  break;
        case 5:
            isPlayerName = jboolean1;  break;
        case 6:
            isPlayerHead = jboolean1;  break;
        case 7:
            isr360Alert = jboolean1;  break;
        case 8:
            isSkelton = jboolean1;  break;
    }
}

extern "C" JNIEXPORT jboolean JNICALL
Java_mobisocial_arcade_FloatLogo_getReady(JNIEnv *, jclass ,int typeofgame) {
    int sockCheck=1;

    if (!Create()) {
        perror("Creation failed");
        return false;
    }
    setsockopt(sock,SOL_SOCKET,SO_REUSEADDR,&sockCheck, sizeof(int));
    if (!Bind()) {
        perror("Bind failed");
        return false;
    }

    if (!Listen()) {
        perror("Listen failed");
        return false;
    }
    if (Accept()) {
        SetValue sv{};
        sv.mode=typeofgame;
        sv.type=utype;
        send((void*)&sv,sizeof(sv));
        // Close();
        return true;
    }
}


extern "C"
JNIEXPORT jstring JNICALL
Java_mobisocial_arcade_AESUtils_AES(JNIEnv *env, jclass thiz) {
return env->NewStringUTF(OBS("tAeKpEcDe6410111"));
}
