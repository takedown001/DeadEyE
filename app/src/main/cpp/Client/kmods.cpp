#include "kmods.h"
#include <ESP.h>
#include <SocketClient.h>
#include "Hacks.h"

using namespace std;

SocketClient client;
ESP espOverlay;

int startClient(){
    client = SocketClient();
    if(!client.Create()){
        LOGE("CE:1");
        return -1;
    }
    if(!client.Connect()){
        LOGE("CE:2");
        return -1;
    }
    if(!initServer()){
        LOGE("CE:3");
        return -1;
    }
    return 0;
}

bool isConnected(){
    return client.connected;
}

void stopClient(){
    if(client.created && isConnected()){
        stopServer();
        client.Close();
    }
}

bool initServer(){
    Request request{InitMode, 0, 0};
    int code = client.send((void*) &request, sizeof(request));
    if(code > 0){
        Response response{};
        size_t length = client.receive((void*) &response);
        if(length > 0){
            return response.Success;
        }
    }
    return false;
}

bool stopServer(){
    Request request{StopMode, 0, 0};
    int code = client.send((void*) &request, sizeof(request));
    if(code > 0){
        Response response{};
        size_t length = client.receive((void*) &response);
        if(length > 0){
            return response.Success;
        }
    }
    return false;
}

Response getData(int screenWidth, int screenHeight){
    Request request{ESPMode, screenWidth, screenHeight};
    int code = client.send((void*) &request, sizeof(request));
    if(code > 0){
        Response response{};
        size_t length = client.receive((void*) &response);
        if(length > 0){
            return response;
        }
    }
    Response response{false, 0};
    return response;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_Gcc_Deadeye_Overlay_Init(JNIEnv *env, jclass type) {
    return startClient();
}

extern "C"
JNIEXPORT void JNICALL
Java_com_Gcc_Deadeye_Overlay_DrawOn(JNIEnv *env, jclass type, jobject espView, jobject canvas) {
    espOverlay = ESP(env, espView, canvas);
    if (espOverlay.isValid()){
        DrawESP(espOverlay, espOverlay.getWidth(), espOverlay.getHeight());
    }
}


extern "C"
JNIEXPORT void JNICALL
Java_com_Gcc_Deadeye_Floater_Switch(JNIEnv *env, jclass type, jint num, jboolean flag) {
    switch (num){
        case 0:
            isESP = flag;
            break;
        case 1:
            isPlayerName = flag;
            break;
        case 2:
            isPlayerHealth = flag;
            break;
        case 3:
            isPlayerDist = flag;
            break;
        case 4:
            isTeamMateShow = flag;
            break;
        case 5:
            isPlayerLine = flag;
            break;
        case 6:
            isPlayerBox = flag;
            break;
        case 7:
            isPlayer360 = flag;
            break;
        case 8:
            isNearEnemy = flag;
            break;
        case 9:
            isItemName = flag;
            break;
        case 10:
            isItemDist = flag;
            break;
        case 11:
            isVehicle = flag;
            break;
        case 12:
            isLootBox = flag;
            break;
        case 13:
            isLootItems = flag;
            break;
        case 14:
            isPlayerSkel = flag;
            break;
        case 15:
            isAirDrop = flag;
            break;
        default:
            break;
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_Gcc_Deadeye_Overlay_Size(JNIEnv *env, jclass type, jint num, jfloat size) {
    switch (num){
        case 0:
            playerTextSize = size;
            break;
        case 1:
            itemTextSize = size;
            break;
        default:
            break;
    }
}


JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    return JNI_VERSION_1_6;
}

JNIEXPORT void JNICALL JNI_OnUnload(JavaVM *vm, void *reserved) {}



extern "C"
JNIEXPORT void JNICALL
Java_com_Gcc_Deadeye_Overlay_Stop(JNIEnv *env, jobject thiz) {
    stopClient();
}
extern "C"
JNIEXPORT void JNICALL
Java_com_Gcc_Deadeye_FloatLogo_SettingValue(JNIEnv *env, jobject thiz, jint num,
                                            jboolean flag) {
    switch (num){
        case 0:
            isESP = flag;
            break;
        case 1:
            isPlayerName = flag;
            break;
        case 2:
            isPlayerHealth = flag;
            break;
        case 3:
            isPlayerDist = flag;
            break;
        case 4:
            isTeamMateShow = flag;
            break;
        case 5:
            isPlayerLine = flag;
            break;
        case 6:
            isPlayerBox = flag;
            break;
        case 7:
            isPlayer360 = flag;
            break;
        case 8:
            isNearEnemy = flag;
            break;
        case 9:
            isItemName = flag;
            break;
        case 10:
            isItemDist = flag;
            break;
        case 11:
            isVehicle = flag;
            break;
        case 12:
            isLootBox = flag;
            break;
        case 13:
            isLootItems = flag;
            break;
        case 14:
            isPlayerSkel = flag;
            break;
        case 15:
            isAirDrop = flag;
            break;
        default:
            break;
    }
}