#ifndef PMDEADEYE_H
#define PMDEADEYE_H

#include <jni.h>
#include <string>
#include <cstdlib>
#include <unistd.h>
#include <sys/mman.h>
#include <android/log.h>
#include "PMStructsCommon.h"
#include "PMLog.h"
bool isfov=false;
bool isdaemon =false;
bool iscrosshair =false;
//Player
bool isPlayerName = false;
bool isPlayerDist = false;
bool isteamid = false;
bool isPlayerUID = false;
bool isPlayerHealth = false;
bool isTeamMateShow = false;
bool isPlayerBox = false;
bool isPlayerLine = false;
bool isPlayer360 = false;
bool isNearEnemy= false;
bool isPlayerSkel= false;
bool islinecenter = false;
bool is3DPlayerBox = false;
bool islinedown = false;
float playerTextSize = 15;
bool isEnemyWeapon = false;
bool isItemName = false;
bool isItemDist = false;
float itemTextSize = 15;
bool isverticlhealth =false;

//vehicals
bool ismotarglider = false;
bool isFerrisCar = false;
bool isbuggy = false;
bool isUAZ = false;
bool istrick = false;
bool isDacia = false;
bool isTukTuk = false;
bool isRony = false;
bool isBike = false;
bool isBoat = false;
bool isjet = false;
bool isBus = false;
bool isscooter = false;
bool isLadaNiva = false;
bool isBRDM = false;
bool isMirado = false;
bool issnowbike = false;
bool issnowmobile = false;
bool istruck = false;

//items
// health
bool isMedKit = false;
bool isFirstAid = false;
bool isInjection = false;
bool isPainkiller = false;
bool isDrink = false;
bool isBandage = false;

//warning
bool iswarning = false;

//weapons
bool isMosin = false;
bool isFAMAS = false;
bool isakm = false;
bool ism416 = false;
bool isAug = false;
bool ismk47 = false ;
bool issks = false;
bool isscarl = false;
bool iskar98 = false;
bool ism416a4 = false;
bool isG36C = false;
bool isQBZ = false;
bool isGroza = false;
bool isBizon = false;
bool isuzi = false;
bool ismp5k = false;
bool isUmp = false;
bool ismk14= false;
bool ism249 = false;
bool isvector = false;
bool isdp28 =false;
bool isAWM = false;
bool isQBU = false;
bool isSLR = false;
bool isMini14 = false;
bool ism24 = false;
bool isvss = false;
bool iswin94 = false;
bool ism762 =false;
bool istommy =false;

//ammo
bool isssm =false;
bool isffm =false;
bool isACP =false;
bool is9mm =false;
bool is300magneum =false;
bool isarrow =false;
bool is12guage =false;

//armos
bool bagl1= false;
bool bagl2= false;
bool bagl3= false;
bool helmet1= false;
bool helmet2= false;
bool helmet3= false;
bool armor3 =false;
bool armor2 = false;
bool armor1 = false;

//scopes
bool hollow = false;
bool Caneted = false;
bool Reddot = false;
bool is8x = false;
bool is4x = false;
bool is2x = false;
bool is3x = false;
bool is6x = false;

//special

bool isflare = false;
bool Gilli = false;
bool Airdrop = false;
bool DropPlane = false;
bool Crate = false;
//throwadble
bool isspiketrap = false;
bool isstickeybomb =false;
bool isgranade = false;
bool issmoke = false;
bool ismolo = false;
int startClient();
bool isConnected();
void stopClient();
bool initServer();
bool stopServer();
Response getData(int screenWidth, int screenHeight);

#endif
