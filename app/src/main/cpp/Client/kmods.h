#ifndef KMODS_H
#define KMODS_H

#include <jni.h>
#include <string>
#include <cstdlib>
#include <unistd.h>
#include <sys/mman.h>
#include <android/log.h>
#include "StructsCommon.h"
#include "Log.h"

bool isESP = false;
//Player
bool isPlayerName = false;
bool isPlayerDist = false;
bool isPlayerHealth = false;
bool isTeamMateShow = false;
bool isPlayerBox = false;
bool isPlayerLine = false;
bool isPlayer360 = false;
bool isNearEnemy= false;
bool isPlayerSkel= false;
float playerTextSize = 15;
//Items
bool isVehicle = false;
bool isLootBox = false;
bool isAirDrop = false;
bool isLootItems = false;
bool isItemName = false;
bool isItemDist = false;
float itemTextSize = 15;

int startClient();
bool isConnected();
void stopClient();
bool initServer();
bool stopServer();
Response getData(int screenWidth, int screenHeight);

#endif
