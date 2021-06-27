#include "PMDeadeye.h"
#include "PMSocket/PMSocketServer.h"
#include "PMStructsCommon.h"
#include "PMFNames.h"
#include "PMPUBG.h"

using namespace std;

SocketServer server;

#define maxplayerCount 30
#define maxitemsCount 40
#define maxvehicalcount 40
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
//    int PlayerID;
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
struct VehicalData{
    char Name[20];
    float Distance;
    PMVector2 Location;
};
struct Response {
    bool Success;
    int NearEnemy;
    int MyTeamID;
    int PlayerCount;
    int ItemsCount;
	int VehicalCount;
	VehicalData Vehical[maxvehicalcount];
	PlayerData Players[maxplayerCount];
	ItemData Items[maxitemsCount];
};

template<typename T>
T Read(kaddr i);

static const char* vehicles[][2] = {
        {"AirDropPlane", "AirDropPlane"},
		{"BP_VH_Buggy", "Buggy"},
		{"AirDropBox", "AirDrop"},
		{"PlayerDeadInventoryBox_C", "Crate"},
		{"VH_Dacia_", "Dacia"},
		{"BP_VH_Tuk", "Tuk Tuk"},
		{"Rony_01_C", "Rony"},
		{"Mirado_", "Mirado"},
		{"VH_UAZ", "UAZ"},
		{"VH_MiniBus", "MiniBus"},
		{"VH_Scooter", "Scooter"},
		{"VH_Motorcycle", "Bike"},
		{"VH_Motorcycle_", "Trike"},
		{"VH_Snowbike", "SnowBike"},
		{"VH_Snowmobile_C", "SnowMobile"},
		{"VH_BRDM", "BRDM"},
		{"LadaNiva_0", "LadaNiva"},
		{"PickUp_02_C", "Pickup"},
		{"AquaRail_1_C", "AquaRail"},
		{"VH_PG117", "Boat"},
		{"Motorglider", "Motor Glider"},
		{"BP_FerrisWheelCar_C", "Ferris Car"},

};

bool FindVehicleName(const string& object, string& res) {
	for (auto &vehicle : vehicles) {
		if (isContain(object, vehicle[0])) {
			res = string(vehicle[1]);
			return true;
		}
	}
	return false;
}

static const char* impitems[][2] = {
		{"FirstAidbox_Pickup", "MedKit"},
		{"Firstaid_Pickup", "FirstAid"},
		{"Injection_Pickup", "Injection"},
		{"Pills_Pickup", "Painkiller"},
		{"Drink_Pickup", "Drink"},
		{"Bandage_Pickup", "Bandage"},
		{"PickUp_BP_Armor_Lv3", "Armor L3"}, //C
		{"PickUp_BP_Helmet_Lv3", "Helmet L3"}, //C
		{"PickUp_BP_Bag_Lv3", "Bag L3"}, //C
		{"BP_MZJ_QX_Pickup_C", "HoloSignt"},
		{"BP_MZJ_HD_Pickup", "RedDot"},
		{"BP_MZJ_8X_Pickup", "8x"},
		{"BP_MZJ_6X_Pickup", "6x"},
		{"BP_MZJ_4X_Pickup", "4x"},
		{"BP_Grenade_Shoulei_Weapon_Wrapper", "Grenade"}, //C
		{"BP_Grenade_Burn_Weapon_Wrapper", "Molotov"},
		{"BP_Grenade_Smoke_Weapon_Wrapper", "Smoke"},
		{"GasCan", "GasCan"},
		{"BP_Ghillie_", "Ghillie"},

		// NEW ITEMS
		{"BP_MZJ_SideRMR_Pickup", "Canted Sight"},
		{"BP_Rifle_FAMAS_Wrapper_C", "FAMAS"},
		{"BP_Sniper_Mosin_Wrapper_C", "Mosin Nagant"},
//Location
		{"BP_Grenade_Sticky_Bomb_C", "Sticky Bomb"},
// Warning
		{"Skill_Grenade_Sticky_Bomb_C", "Sticky BombW"},

//Location
		{"BP_ProjSpikeTrap_BP_C", "Spike Trap"},
// Warning
		{"Skill_ProjSpikeTrap_BP_C", "Spike TrapW"},

		{"PlayerDeadInventoryBox_C", "Crate"},
		{"BP_Sniper_AWM_Wrapper_C", "AWM"},
		{"BP_Rifle_AKM_Wrapper_C", "AKM"},
		{"BP_Sniper_M24_Wrapper_C", "M24"},
		{"BP_MZJ_3X_Pickup", "3x"},
		{"BP_Pistol_Flaregun_Wrapper_C", "FlareGun"},
		{"BP_Rifle_M416_Wrapper_C", "M416"},
		{"BP_Rifle_QBZ_Wrapper_C", "QBZ"},
		{"BP_AirDropBox_C", "AirDrop"},
		{"BP_AirDropPlane_C", "Plane"},
		// MORE NEW ITEMS
        {"BP_Rifle_SCAR_Wrapper_C", "SCAR L"},
        {"BP_Rifle_M762_Wrapper_C", "M762"},
        {"BP_Rifle_M16A4_Wrapper_C", "M16A4"},
        {"BP_Rifle_Mk47_Wrapper_C", "Mk47 Mutant"},
        {"BP_Rifle_G36_Wrapper_C", "G36C"},
        {"BP_Rifle_Groza_Wrapper_C", "Groza"},
        {"BP_Rifle_AUG_Wrapper_C", "AUG A3"},
        {"BP_Sniper_QBU_Wrapper_C", "QBU"},
        {"BP_Sniper_SLR_Wrapper_C", "SLR"},
        {"BP_Sniper_SKS_Wrapper_C", "SKS"},
        {"BP_Sniper_Mini14_Wrapper_C", "Mini14"},
        {"BP_Sniper_Kar98k_Wrapper_C", "Kar98k"},
        {"BP_Sniper_VSS_Wrapper_C", "VSS"},
        {"BP_Sniper_Win94_Wrapper_C", "Win94"},
        {"BP_Ammo_762mm_Pickup_C", "7.62mm"},
        {"BP_Ammo_45ACP_Pickup_C", "45ACP"},
        {"BP_Ammo_556mm_Pickup_C", "5.56mm"},
        {"BP_Ammo_9mm_Pickup_C", "9mm"},
        {"BP_Ammo_300Magnum_Pickup_C", "300Magnum"},
        {"PickUp_BP_Armor_Lv2", "Armor L2"},
        {"PickUp_BP_Armor_Lv1", "Armor L1"},
        {"PickUp_BP_Helmet_Lv2", "Helmet L2"},
        {"PickUp_BP_Helmet_Lv1", "Helmet L1"},
        {"PickUp_BP_Bag_Lv2", "Bag L2"},
        {"PickUp_BP_Bag_Lv1", "Bag L1"},
        // Grenade Warning Change [ Accordingly ]
        {"BP_Grenade_Shoulei_C", "Grenadew"},
        {"BP_Grenade_Burn_C", "Molotovw"},
        // MORE MORE ITEMS SMG :/
		{"BP_MachineGun_PP19_Wrapper_C", "PP19Bizon"},
		{"BP_MachineGun_TommyGun_Wrapper_C", "Tommy Gun"},
		{"BP_MachineGun_Vector_Wrapper_C", "Vector"},
		{"BP_MachineGun_Uzi_Wrapper_C", "Uzi"},
		{"BP_MachineGun_MP5K_Wrapper_C", "MP5K"},
		{"BP_MachineGun_UMP9_Wrapper_C", "UMP9"},
		{"BP_Other_DP28_Wrapper_C", "DP28"},
		{"BP_Other_M249_Wrapper_C", "M249"},
		{"BP_WEP_Mk14_Pickup_C", "MK14"},
};

bool FindItemName(const string& object, string& res) {
	for (auto &items : impitems) {
		if (isContain(object, items[0])) {
			res = string(items[1]);
			return true;
		}
	}
	return false;
}

bool isOutsideSafeZone(PMVector2 pos) {
	if (pos.Y < 0) {
		return true;
	}
	if (pos.X > (float)SWidth) {
		return true;
	}
	if (pos.Y > (float)SHeight) {
		return true;
	}
	return pos.X < 0;
}


void createUDataList(Response& response) {
	response.Success = false;
	response.PlayerCount = 0;
	response.ItemsCount = 0;

	kaddr gworld = getRealOffset(Offsets::UGWorld);
	if(gworld == 0){ return; }

	kaddr uworld = getPtr(gworld);
	if(UObject::isInvalid(uworld)){ return; }

	kaddr level = World::getPersistentLevel(uworld);
	if (UObject::isInvalid(level)) { return; }

	kaddr actorList = Level::getActorList(level);
	if (actorList == 0) { return; }

	int nearEnemy = 0;
	int localTeamID = 0;
	uint32 localPKey = 0;
	MinimalViewInfo POV = MinimalViewInfo();

	for (int i = 0; i < Level::getActorsCount(level); i++) {
		kaddr actor = Level::getActor(actorList, i);
		if (UObject::isInvalid(actor)) { continue; }

		//LOGE("Actor[%i]: %x | %s", i, actor, UObject::getName(actor).c_str());

		string oname = UObject::getUName(actor);

		if(isContain(oname, "BP_STExtraPlayerController")){
			localTeamID = PlayerController::getTeamID(actor);
			localPKey = PlayerController::getPlayerKey(actor);
			continue;
		}

		if(isContain(oname, "BP_PlayerCameraManager")){
			POV = PlayerCameraManager::getPOV(actor);
			continue;
		}
		if(localPKey == 0 || POV.FOV == 0){ continue; }

		kaddr RootComp = Actor::getRootComponent(actor);
		if (UObject::isInvalid(RootComp)) { continue; }

		PMVector3 Location = SceneComponent::getLocation(RootComp);
		float distance = (PMVector3::Distance(Location, POV.Location) / 100.0f);

		if (isContain(oname, "BP_PlayerPawn_") && !isContain(oname, "BP_PlayerPawn_Statue_")) {
			if (response.PlayerCount == maxplayerCount) {
				continue;
			}

			PlayerData* data = &response.Players[response.PlayerCount];

			if(distance > 500) { continue; }

			if(localPKey == Player::getPlayerKey(actor)){ continue;	}

			int teamID = Player::getTeamID(actor);
//			int PlayerID = Player::getPlayerID(actor);
			if(localTeamID != teamID){
				nearEnemy++;
			}

			kaddr Mesh = Character::getMeshComponent(actor);
			if (UObject::isInvalid(Mesh)) { continue; }

			FTransform c2wTransform = SceneComponent::getComponentToWorld(Mesh);
			FMatrix c2wMatrix = TransformToMatrixWithScale(c2wTransform);

			kaddr BoneArr = SkeletalMeshComponent::getBoneArr(Mesh);
			if (BoneArr == 0) { continue; }

			FString pname = Player::getPlayerName(actor);

			wmemcpy(data->PlayerName, pname.w_str(), pname.Count);
			data->isBot = Player::IsAI(actor);
			data->TeamID = teamID;
//			data->PlayerID = PlayerID;
			data->Health = Player::getHealth(actor);
			data->Distance = distance;
			data->Body = WorldToScreen(Location, POV, SWidth, SHeight);
			data->Root = WorldToScreen(Player::getBoneLoc(c2wMatrix, BoneArr, 0) - PMVector3(0, 0, 7), POV, SWidth, SHeight);
			data->Head = WorldToScreen(Player::getBoneLoc(c2wMatrix, BoneArr, 6) + PMVector3(0, 0, 22), POV, SWidth, SHeight);
			data->Neck = WorldToScreen(Player::getBoneLoc(c2wMatrix, BoneArr, 6) + PMVector3(0, 0, 10), POV, SWidth, SHeight);
			data->Chest = WorldToScreen(Player::getBoneLoc(c2wMatrix, BoneArr, 5), POV, SWidth, SHeight);
			data->Pelvis = WorldToScreen(Player::getBoneLoc(c2wMatrix, BoneArr, 2), POV, SWidth, SHeight);
			data->LShoulder = WorldToScreen(Player::getBoneLoc(c2wMatrix, BoneArr, 12), POV, SWidth, SHeight);
			data->RShoulder = WorldToScreen(Player::getBoneLoc(c2wMatrix, BoneArr, 33), POV, SWidth, SHeight);
			data->LElbow = WorldToScreen(Player::getBoneLoc(c2wMatrix, BoneArr, 13), POV, SWidth, SHeight);
			data->RElbow = WorldToScreen(Player::getBoneLoc(c2wMatrix, BoneArr, 34), POV, SWidth, SHeight);
			data->LWrist = WorldToScreen(Player::getBoneLoc(c2wMatrix, BoneArr, 64), POV, SWidth, SHeight);
			data->RWrist = WorldToScreen(Player::getBoneLoc(c2wMatrix, BoneArr, 63), POV, SWidth, SHeight);
			data->LThigh = WorldToScreen(Player::getBoneLoc(c2wMatrix, BoneArr, 53), POV, SWidth, SHeight);
			data->RThigh = WorldToScreen(Player::getBoneLoc(c2wMatrix, BoneArr, 57), POV, SWidth, SHeight);
			data->LKnee = WorldToScreen(Player::getBoneLoc(c2wMatrix, BoneArr, 54), POV, SWidth, SHeight);
			data->RKnee = WorldToScreen(Player::getBoneLoc(c2wMatrix, BoneArr, 58), POV, SWidth, SHeight);
			data->LAnkle = WorldToScreen(Player::getBoneLoc(c2wMatrix, BoneArr, 55), POV, SWidth, SHeight);
			data->RAnkle = WorldToScreen(Player::getBoneLoc(c2wMatrix, BoneArr, 59), POV, SWidth, SHeight);

			response.PlayerCount++;
		}

//		else if (isContain(oname, "PlayerDeadInventoryBox")) {
//			if (response.ItemsCount == maxitemsCount) {
//				continue;
//			}
//
//			PMVector2 SLoc = WorldToScreen(Location, POV, SWidth, SHeight);
//			if (isOutsideSafeZone(SLoc)) { continue; }
//
//			ItemData* data = &response.Items[response.ItemsCount];
//
//			if(distance > 500) { continue; }
//
//			strncpy(data->Name, "LootBox", 7);
//			data->isVehicle = false;
//			data->isLootBox = true;
//			data->isAirDrop = false;
//			data->isLootItem = false;
//			data->Distance = distance;
//			data->Location = SLoc;
//
//			response.ItemsCount++;
//		} else if(isContain(oname, "AirDropBox")) {
//			if (response.ItemsCount == maxitemsCount) {
//				continue;
//			}
//
//			PMVector2 SLoc = WorldToScreen(Location, POV, SWidth, SHeight);
//			if (isOutsideSafeZone(SLoc)) { continue; }
//
//			if(distance > 1000) { continue; }
//
//			ItemData* data = &response.Items[response.ItemsCount];
//
//			strncpy(data->Name, "AirDrop", 6);
//			data->isVehicle = false;
//			data->isLootBox = false;
//			data->isAirDrop = true;
//			data->isLootItem = false;
//			data->Distance = distance;
//			data->Location = SLoc;
//
//			response.ItemsCount++;
//		}
        else {
            string itemName;
            if(FindVehicleName(oname,itemName)){
                if (response.VehicalCount == maxvehicalcount) {
                    continue;
                }

                PMVector2 SLoc = WorldToScreen(Location, POV, SWidth, SHeight);
                if (isOutsideSafeZone(SLoc)) { continue; }

                if(distance < 10 || distance > 800) { continue; }

                VehicalData* data = &response.Vehical[response.VehicalCount];

                strncpy(data->Name, itemName.data(), itemName.size());
                data->Distance = distance;
                data->Location = SLoc;
                response.VehicalCount++;

            } else if(FindItemName(oname, itemName)){
                if (response.ItemsCount == maxitemsCount) {
                    continue;
                }

                PMVector2 SLoc = WorldToScreen(Location, POV, SWidth, SHeight);
                if (isOutsideSafeZone(SLoc)) { continue; }

                if(distance > 800) { continue; }

                ItemData* data = &response.Items[response.ItemsCount];

                strncpy(data->Name, itemName.data(), itemName.size());
                data->isVehicle = false;
                data->isLootBox = false;
                data->isAirDrop = false;
                data->isLootItem = true;
                data->Distance = distance;
                data->Location = SLoc;

                response.ItemsCount++;
            }
        }
	}

	response.Success = true;
	response.NearEnemy = nearEnemy;
	response.MyTeamID = localTeamID;
}

void createDataList(Response& response) {
    response.Success = false;
    response.PlayerCount = 0;
    response.ItemsCount = 0;


    kaddr gworld = getRealOffset(Offsets::GWorld);
    if(gworld == 0){ return; }

    kaddr uworld = getPtr(gworld);
    if(UObject::isInvalid(uworld)){ return; }

    kaddr level = World::getPersistentLevel(uworld);
    if (UObject::isInvalid(level)) { return; }

    kaddr actorList = Level::getActorList(level);
    if (actorList == 0) { return; }

    int nearEnemy = 0;
    int localTeamID = 0;
    uint32 localPKey = 0;
    MinimalViewInfo POV = MinimalViewInfo();

    for (int i = 0; i < Level::getActorsCount(level); i++) {
        kaddr actor = Level::getActor(actorList, i);
        if (UObject::isInvalid(actor)) { continue; }

        //LOGE("Actor[%i]: %x | %s", i, actor, UObject::getName(actor).c_str());

        string oname = UObject::getName(actor);

        if(isContain(oname, "BP_STExtraPlayerController")){
            localTeamID = PlayerController::getTeamID(actor);
            localPKey = PlayerController::getPlayerKey(actor);
            continue;
        }

        if(isContain(oname, "BP_PlayerCameraManager")){
            POV = PlayerCameraManager::getPOV(actor);
            continue;
        }

        if(localPKey == 0 || POV.FOV == 0){ continue; }

        kaddr RootComp = Actor::getRootComponent(actor);
        if (UObject::isInvalid(RootComp)) { continue; }

        PMVector3 Location = SceneComponent::getLocation(RootComp);
        float distance = (PMVector3::Distance(Location, POV.Location) / 100.0f);

        if (isContain(oname, "BP_PlayerPawn_") && !isContain(oname, "BP_PlayerPawn_Statue_")) {
            if (response.PlayerCount == maxplayerCount) {
                continue;
            }

            PlayerData* data = &response.Players[response.PlayerCount];

            if(distance > 500) { continue; }

            if(localPKey == Player::getPlayerKey(actor)){ continue;	}

			int teamID = Player::getTeamID(actor);
//            int PlayerID = Player::getPlayerID(actor);
            if(localTeamID != teamID){
                nearEnemy++;
            }

			kaddr Mesh = Character::getMeshComponent(actor);
			if (UObject::isInvalid(Mesh)) { continue; }

			FTransform c2wTransform = SceneComponent::getComponentToWorld(Mesh);
			FMatrix c2wMatrix = TransformToMatrixWithScale(c2wTransform);

			kaddr BoneArr = SkeletalMeshComponent::getBoneArr(Mesh);
			if (BoneArr == 0) { continue; }

			FString pname = Player::getPlayerName(actor);

			wmemcpy(data->PlayerName, pname.w_str(), pname.Count);
            data->isBot = Player::IsAI(actor);
            data->TeamID = teamID;
//			data->PlayerID = PlayerID;
            data->Health = Player::getHealth(actor);
            data->Distance = distance;
            data->Body = WorldToScreen(Location, POV, SWidth, SHeight);
            data->Root = WorldToScreen(Player::getBoneLoc(c2wMatrix, BoneArr, 0) - PMVector3(0, 0, 7), POV, SWidth, SHeight);
            data->Head = WorldToScreen(Player::getBoneLoc(c2wMatrix, BoneArr, 6) + PMVector3(0, 0, 22), POV, SWidth, SHeight);
            data->Neck = WorldToScreen(Player::getBoneLoc(c2wMatrix, BoneArr, 6) + PMVector3(0, 0, 10), POV, SWidth, SHeight);
            data->Chest = WorldToScreen(Player::getBoneLoc(c2wMatrix, BoneArr, 5), POV, SWidth, SHeight);
            data->Pelvis = WorldToScreen(Player::getBoneLoc(c2wMatrix, BoneArr, 2), POV, SWidth, SHeight);
            data->LShoulder = WorldToScreen(Player::getBoneLoc(c2wMatrix, BoneArr, 12), POV, SWidth, SHeight);
            data->RShoulder = WorldToScreen(Player::getBoneLoc(c2wMatrix, BoneArr, 33), POV, SWidth, SHeight);
            data->LElbow = WorldToScreen(Player::getBoneLoc(c2wMatrix, BoneArr, 13), POV, SWidth, SHeight);
            data->RElbow = WorldToScreen(Player::getBoneLoc(c2wMatrix, BoneArr, 34), POV, SWidth, SHeight);
            data->LWrist = WorldToScreen(Player::getBoneLoc(c2wMatrix, BoneArr, 64), POV, SWidth, SHeight);
            data->RWrist = WorldToScreen(Player::getBoneLoc(c2wMatrix, BoneArr, 63), POV, SWidth, SHeight);
            data->LThigh = WorldToScreen(Player::getBoneLoc(c2wMatrix, BoneArr, 53), POV, SWidth, SHeight);
            data->RThigh = WorldToScreen(Player::getBoneLoc(c2wMatrix, BoneArr, 57), POV, SWidth, SHeight);
            data->LKnee = WorldToScreen(Player::getBoneLoc(c2wMatrix, BoneArr, 54), POV, SWidth, SHeight);
            data->RKnee = WorldToScreen(Player::getBoneLoc(c2wMatrix, BoneArr, 58), POV, SWidth, SHeight);
            data->LAnkle = WorldToScreen(Player::getBoneLoc(c2wMatrix, BoneArr, 55), POV, SWidth, SHeight);
            data->RAnkle = WorldToScreen(Player::getBoneLoc(c2wMatrix, BoneArr, 59), POV, SWidth, SHeight);

            response.PlayerCount++;
        }

//        else if (isContain(oname, "PlayerDeadInventoryBox")) {
//            if (response.ItemsCount == maxitemsCount) {
//                continue;
//            }
//
//            PMVector2 SLoc = WorldToScreen(Location, POV, SWidth, SHeight);
//			if (isOutsideSafeZone(SLoc)) { continue; }
//
//            ItemData* data = &response.Items[response.ItemsCount];
//
//            if(distance > 500) { continue; }
//
//            strncpy(data->Name, "LootBox", 7);
//            data->isVehicle = false;
//            data->isLootBox = true;
//            data->isAirDrop = false;
//            data->isLootItem = false;
//            data->Distance = distance;
//            data->Location = SLoc;
//
//            response.ItemsCount++;
//        } else if(isContain(oname, "AirDropBox")) {
//            if (response.ItemsCount == maxitemsCount) {
//                continue;
//            }
//
//            PMVector2 SLoc = WorldToScreen(Location, POV, SWidth, SHeight);
//            if (isOutsideSafeZone(SLoc)) { continue; }
//
//            if(distance > 1000) { continue; }
//
//            ItemData* data = &response.Items[response.ItemsCount];
//
//            strncpy(data->Name, "AirDrop", 6);
//            data->isVehicle = false;
//            data->isLootBox = false;
//            data->isAirDrop = true;
//            data->isLootItem = false;
//            data->Distance = distance;
//            data->Location = SLoc;
//
//            response.ItemsCount++;
//        }

        else {
			string itemName;
			if(FindVehicleName(oname,itemName)){
				if (response.VehicalCount == maxvehicalcount) {
					continue;
				}

				PMVector2 SLoc = WorldToScreen(Location, POV, SWidth, SHeight);
				if (isOutsideSafeZone(SLoc)) { continue; }

				if(distance < 10 || distance > 800) { continue; }

				VehicalData* data = &response.Vehical[response.VehicalCount];

				strncpy(data->Name, itemName.data(), itemName.size());
				data->Distance = distance;
				data->Location = SLoc;
				response.VehicalCount++;

            } else if(FindItemName(oname, itemName)){
                if (response.ItemsCount == maxitemsCount) {
                    continue;
                }

				PMVector2 SLoc = WorldToScreen(Location, POV, SWidth, SHeight);
				if (isOutsideSafeZone(SLoc)) { continue; }

                if(distance > 800) { continue; }

                ItemData* data = &response.Items[response.ItemsCount];

                strncpy(data->Name, itemName.data(), itemName.size());
                data->isVehicle = false;
                data->isLootBox = false;
                data->isAirDrop = false;
                data->isLootItem = true;
                data->Distance = distance;
                data->Location = SLoc;

                response.ItemsCount++;
            }
        }
    }



    response.Success = true;
    response.NearEnemy = nearEnemy;
    response.MyTeamID = localTeamID;
	}





int main(int argc, char *argv[]) {
	if (!server.Create()) {
		LOGE("SE:1");
		return -1;
	}

	if (!server.Bind()) {
		LOGE("SE:2");
		return -1;
	}

	if (!server.Listen()) {
		LOGE("SE:3");
		return -1;
	}
	
	if(argc < 2){
		LOGE("SE:4");
		return -1;
	}

	if (server.Accept()) {
		int mode = atoi(argv[1]);
		if(mode == 1){
			target_pid = find_pid("com.tencent.ig");
			if (target_pid == -1) {
				LOGE("Can't find the process\n");
				return -1;
			}
		} else if(mode == 2){
			target_pid = find_pid("com.pubg.krmobile");
			if (target_pid == -1) {
				LOGE("Can't find the process\n");
				return -1;
			}
		} else if(mode == 3){
			target_pid = find_pid("com.vng.pubgmobile");
			if (target_pid == -1) {
				LOGE("Can't find the process\n");
				return -1;
			}
		} else if(mode == 4){
			target_pid = find_pid("com.rekoo.pubgm");
			if (target_pid == -1) {
				LOGE("Can't find the process\n");
				return -1;
			}
		} else if(mode == 5){
			target_pid = find_pid("com.pubg.imobile");
			if (target_pid == -1) {
				LOGE("Can't find the process\n");
				return -1;
			}
		} else {
			LOGE("Invalid Game Choice\n");
			return -1;
		}

		libbase = get_module_base(lib_name);
		if (libbase == 0) {
			LOGE("Can't find module\n");
			return -1;
		}

		LOGW("Base Address of %s Found At %x\n", lib_name, libbase);

		Request request{};
		while (server.receive((void*)& request) > 0) {
			Response response{};

			if (request.Mode == InitMode) {
				response.Success = (libbase > 0);
			}
			else if (request.Mode == ESPMode) {
				SWidth = request.ScreenWidth;
				SHeight = request.ScreenHeight;
                if(find_pid("com.pubg.imobile")!=-1) {
                    createUDataList(response);
                }
                else{
                    createDataList(response); // All Version

                }
			}
			else if (request.Mode == HackMode) {
				response.Success = true;
			}
			else if (request.Mode == StopMode) {
				response.Success = true;
				server.send((void*)& response, sizeof(response));
				break;
			}

			server.send((void*)&response, sizeof(response));
		}
	}
	
	return 0;
}






