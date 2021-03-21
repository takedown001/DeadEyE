#include "PMDeadeye.h"
#include <PMESP.h>
#include <PMSocketClient.h>
#include "PMHacks.h"

using namespace std;

PMSocketClient client;
PMESP espOverlay;

int startClient(){
    client = PMSocketClient();
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



JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    return JNI_VERSION_1_6;
}

JNIEXPORT void JNICALL JNI_OnUnload(JavaVM *vm, void *reserved) {}

extern "C"
JNIEXPORT void JNICALL
Java_com_Gcc_Deadeye_FloatLogo_PremiumValue(JNIEnv *env, jclass clazz, jint num,
                                            jboolean flag) {
    switch (num){
        case 600:
            isESP = flag;
            break;
        case 601:
            isPlayerName = flag;
            break;
        case 602:
            isPlayerHealth = flag;
            break;
        case 603:
            isPlayerDist = flag;
            break;
        case 604:
            isTeamMateShow = flag;
            break;
        case 605:
            isPlayerLine = flag;
            break;
        case 606:
            isPlayerBox = flag;
            break;
        case 607:
            isPlayer360 = flag;
            break;
        case 608:
            isNearEnemy = flag;
            break;
        case 609:
            isItemName = flag;
            break;
        case 610:
            isItemDist = flag;
            break;
        case 611:
            isVehicle = flag;
            break;
        case 612:
            isLootBox = flag;
            break;
        case 613:
            isLootItems = flag;
            break;
        case 614:
            isPlayerSkel = flag;
            break;
        case 615:
            isAirDrop = flag;
            break;
        case 616:
            isteamid = flag;
        default:
            break;
    }
}



extern "C"
JNIEXPORT jint JNICALL
Java_com_Gcc_Deadeye_Overlay_Init(JNIEnv *env, jclass clazz) {
    return startClient();
}
extern "C"
JNIEXPORT void JNICALL
Java_com_Gcc_Deadeye_Overlay_Size(JNIEnv *env, jclass clazz, jint num, jfloat size) {
    switch (num){
        case 999:
            playerTextSize = size;
            break;
        case 1000:
            itemTextSize = size;
            break;
        default:
            break;
    }
}extern "C"
JNIEXPORT void JNICALL
Java_com_Gcc_Deadeye_Overlay_Stop(JNIEnv *env, jclass clazz) {
    stopClient();
}extern "C"
JNIEXPORT jint JNICALL
Java_com_Gcc_Deadeye_Free_FreeOverlay_Init(JNIEnv *env, jclass clazz) {
    return startClient();
}



extern "C"
JNIEXPORT void JNICALL
Java_com_Gcc_Deadeye_Free_FreeOverlay_Stop(JNIEnv *env, jclass clazz) {
    stopClient();
}extern "C"
JNIEXPORT void JNICALL
Java_com_Gcc_Deadeye_Free_FreeOverlay_SettingValue(JNIEnv *env, jclass clazz, jint i, jboolean flag) {
    switch (i){
        case -1:
            isfree = flag;
            break;
        case 85:
            isESP = flag;
            break;
        case 86:
            isPlayerName = flag;
            break;
        case 87:
            isPlayerHealth = flag;
            break;
        case 88:
            isPlayerDist = flag;
            break;
      //  case 90:
            isPlayerLine = flag;
            break;
        case 91:
            isPlayerBox = flag;
            break;
        case 93:
            isNearEnemy = flag;
            break;
        case 96:
            isVehicle = flag;
            break;
        case 97:
            isLootBox = flag;
            break;
        case 92:
            isItemName = flag;
            break;
        case 95:
            isItemDist = flag;
            break;
     //   case 99:
            isPlayerSkel = flag;
            break;
        case 101:
            isfov = flag;
        default:
            break;
    }
}extern "C"
JNIEXPORT void JNICALL
Java_com_Gcc_Deadeye_ESPView_DrawOn(JNIEnv *env, jclass clazz, jobject esp_view, jobject canvas) {
    espOverlay = PMESP(env, esp_view, canvas);
    if (espOverlay.isValid()){
        DrawESP(espOverlay, espOverlay.getWidth(), espOverlay.getHeight());
    }
}
