#ifndef PMSTRUCTSCOMM_H
#define PMSTRUCTSCOMM_H

#include "PMVector3.h"
#include "PMVector2.h"
#include "PMRect.h"

using namespace std;

#define maxplayerCount 30
#define maxitemsCount 40

enum Mode {
	InitMode = 1,
	ESPMode = 2,
	HackMode = 3,
	StopMode = 4,
};

struct Request {
	int Mode;
	int ScreenWidth;
	int ScreenHeight;
};

struct PlayerData {
	wchar_t PlayerName[30];
	bool isBot;
	int TeamID;
	float Health;
	float Distance;
	PMVector2 Body;
	PMVector2 Root;
	PMVector2 Head;
	PMVector2 Neck;
	PMVector2 Chest;
	PMVector2 Pelvis;
	PMVector2 LShoulder;
	PMVector2 RShoulder;
	PMVector2 LElbow;
	PMVector2 RElbow;
	PMVector2 LWrist;
	PMVector2 RWrist;
	PMVector2 LThigh;
	PMVector2 RThigh;
	PMVector2 LKnee;
	PMVector2 RKnee;
	PMVector2 LAnkle;
	PMVector2 RAnkle;
};

struct ItemData {
	char Name[20];
	bool isVehicle;
	bool isLootBox;
	bool isAirDrop;
	bool isLootItem;
	float Distance;
	PMVector2 Location;
};

struct Response {
	bool Success;
	int NearEnemy;
	int MyTeamID;
	int PlayerCount;
	int ItemsCount;
	PlayerData Players[maxplayerCount];
	ItemData Items[maxitemsCount];
};

#endif
