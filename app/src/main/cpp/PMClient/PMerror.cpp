#include "PMDeadeye.h"
#include <PMESP.h>
#include <PMSocketClient.h>
#include "PMHacks.h"
#include "Security/enc.h"
using namespace std;

PMSocketClient client;
PMESP esp, fesp;

int startClient() {
    client = PMSocketClient();
    if (!client.Create()) {
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
Java_mobisocial_arcade_FloatLogo_DrawOn(JNIEnv *env, jclass clazz, jobject esp_view,
                                      jobject canvas) {
    esp = PMESP(env, esp_view, canvas);
    if (esp.isValid()){
        DrawESP(esp, esp.getWidth(), esp.getHeight());
    }
}



extern "C"
JNIEXPORT jint JNICALL
Java_mobisocial_arcade_FloatLogo_Init(JNIEnv *env, jclass thiz) {
    return startClient();
}extern "C"
JNIEXPORT void JNICALL
Java_mobisocial_arcade_FloatLogo_Stop(JNIEnv *env, jclass clazz) {
    return stopClient();
}extern "C"
JNIEXPORT void JNICALL
Java_mobisocial_arcade_FloatLogo_PremiumItemValue(JNIEnv *env, jclass clazz,
                                                           jint num, jboolean flag) {

    switch (num) {
        case 50:
            isMedKit = flag;
            break;
        case 51:
            isBandage = flag;
            break;
        case 52:
            isFirstAid = flag;
            break;
        case 53:
            isInjection = flag;
            break;
        case 54:
            isPainkiller = flag;
            break;
        case 55:
            isDrink = flag;
            break;
        case 56:
            isakm = flag;
            break;
        case 57:
            ism416 = flag;
            break;
        case 58:
            isAug = flag;
            break;
        case 59:
            ismk14 = flag;
            break;
        case 60:
            ismk47 = flag;
            break;
        case 61:
            issks = flag;
            break;
        case 62:
            isscarl = flag;
            break;
        case 63:
            iskar98 = flag;
            break;
        case 64:
            ism416a4 = flag;
            break;
        case 65:
            isG36C = flag;
            break;
        case 66:
            isQBZ = flag;
            break;
        case 67:
            isGroza = flag;
            break;
        case 68:
            isBizon = flag;
            break;
        case 69:
            isuzi = flag;
            break;
        case 70:
            ismp5k = flag;
            break;
        case 71:
            isUmp = flag;
            break;
        case 72:
            ismk14 = flag;
            break;
        case 73:
            ism249 = flag;
            break;
        case 74:
            isvector = flag;
            break;
        case 75:
            isdp28 = flag;
            break;
        case 76:
            isAWM = flag;
            break;
        case 77:
            isQBU = flag;
            break;
        case 78:
            isSLR = flag;
            break;
        case 79:
            isMini14 = flag;
            break;
        case 80:
            ism24 = flag;
            break;
        case 81:
            isvss = flag;
            break;
        case 82:
            iswin94 = flag;
            break;
        case 83:
            isssm = flag;
            break;
        case 84:
            isffm = flag;
            break;
        case 85:
            isACP = flag;
            break;
        case 86:
            is9mm = flag;
            break;
        case 87:
            is300magneum = flag;
            break;
        case 88:
            isarrow = flag;
            break;
        case 89:
            is12guage = flag;
            break;
        case 90:
            bagl1 = flag;
            break;
        case 91:
            bagl2 = flag;
            break;
        case 92:
            bagl3 = flag;
            break;
        case 93:
            helmet1 = flag;
            break;
        case 94:
            helmet2 = flag;
            break;
        case 95:
            helmet3 = flag;
            break;
        case 96:
            hollow = flag;
            break;
        case 97:
            Caneted = flag;
            break;
        case 98:
            Reddot = flag;
            break;
        case 99:
            is8x = flag;
            break;
        case 100:
            is4x = flag;
            break;
        case 101:
            is2x = flag;
            break;
        case 102:
            is3x = flag;
            break;
        case 103:
            is6x = flag;
            break;
        case 104:
            isflare = flag;
            break;
        case 105:
            Gilli = flag;
            break;
        case 106:
            Airdrop = flag;
            break;
        case 107:
            DropPlane = flag;
            break;
        case 108:
            Crate = flag;
            break;
        case 109:
            armor1 = flag;
            break;
        case 110:
            armor2 = flag;
            break;
        case 111:
            armor3 = flag;
            break;
        case 112:
            ism762 = flag;
            break;
        case 113:
            isgranade = flag;
            break;
        case 114:
            issmoke = flag;
            break;
        case 115:
            ismolo = flag;
            break;
        case 116:
            iswarning = flag;
            break;
        case 117:
            istommy = flag;
            break;
        case 118:
            isFAMAS =flag;
            break;
        case 119:
            isMosin =flag;
            break;
        case 120:
            isstickeybomb =flag;
            break;
        case 121:
            isspiketrap =flag;
            break;
        default:
            break;

    }

}
extern "C"
    JNIEXPORT void JNICALL
    Java_mobisocial_arcade_FloatLogo_PremiumVehicalValue(JNIEnv *env, jclass clazz,
                                                                  jint num,
                                                                  jboolean flag) {
        switch (num){
            case 20:
                isbuggy = flag;
                break;
            case 21:
                isUAZ = flag;
                break;
            case 22:
                isDacia = flag ;
                break;
            case 23:
                isTukTuk = flag;
                break;
            case 24:
                isRony = flag;
                break;
            case 25:
                isBike = flag;
                break;
            case 26:
                isBoat = flag;
                break;
            case 27:
                isjet = flag;
                break;
            case 28:
                isBus = flag;
                break;
//        case 29 :
//            isMonstertruck = flag;
//            break;
            case 30:
                isscooter = flag;
                break;
            case 31:
                istrick =flag;
                break;
            case 32:
                isLadaNiva = flag;
                break;
            case 33:
                isBRDM = flag;
                break;
            case 34:
                isMirado = flag;
                break;
            case 35 :
                issnowbike = flag;
                break;
            case 36 :
                issnowmobile =flag ;
                break;
            case 37:
                istruck = flag;
            case 38:
                ismotarglider =flag;
                break;
            case 39:
                isFerrisCar = flag;
                break;
            default:
                break;
        }

    }extern "C"
JNIEXPORT void JNICALL
Java_mobisocial_arcade_FloatLogo_PremiumValue(JNIEnv *env, jclass clazz, jint num,
                                                       jboolean flag) {
    switch (num){
        case 597:
            iscrosshair = flag;
            break;
        case 598:
            isfov =flag;
            break;
        case 599:
            isdaemon = flag;
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
            islinedown = flag;
            break;
        case 612:
            is3DPlayerBox =flag;
            break;
        case 614:
            isPlayerSkel = flag;
            break;
        case 615:
            islinecenter = flag;
            break;
        case 616:
            isteamid = flag;
            break;
        case 617:
            isverticlhealth =flag;
            break;
        default:
            break;
    }
}extern "C"
JNIEXPORT void JNICALL
Java_mobisocial_arcade_FloatLogo_Size(JNIEnv *env, jclass clazz, jint num,
                                               jfloat size) {
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

}

extern "C"
JNIEXPORT jstring JNICALL
Java_mobisocial_arcade_AESUtils_AES(JNIEnv *env, jclass thiz) {
    return env->NewStringUTF(OBS("tAeKpEcDe6410111"));
}