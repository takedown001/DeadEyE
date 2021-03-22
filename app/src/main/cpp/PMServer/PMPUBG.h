#ifndef STRUCTSPUBG_H
#define STRUCTSPUBG_H

using namespace std;

struct UObject {
    static int32 getIndex(kaddr object){
        return Read<int32>(object + Offsets::UObjectToInternalIndex);
    }

    static uint32 getNameID(kaddr object){
        return Read<uint32>(object + Offsets::UObjectToFNameIndex);
    }

    static string getName(kaddr object) {
        return GetFNameFromID(getNameID(object));
    }
    static string getUName(kaddr object) {
        return GetUFNameFromID(getNameID(object));
    }
    static string getLiteName(kaddr object) {
        return GetLiteNameFromID(getNameID(object));
    }
    static bool isInvalid(kaddr object){
        return (object == 0 || getIndex(object) == 0);
    }
};

struct World {
    static kaddr getPersistentLevel(kaddr uworld){
        return getPtr(uworld + Offsets::WorldToPersistentLevel);
    }
};

struct Level {
    static kaddr getActorList(kaddr level) {
        return getPtr(level + Offsets::LevelToAActors);
    }

    static kaddr getActor(kaddr actorList, int index) {
        return getPtr(actorList + (index * Offsets::PointerSize));
    }

    static int getActorsCount(kaddr level) {
        return Read<int>(level + Offsets::LevelToAActors + Offsets::PointerSize);
    }
};
struct LiteLevel {
    static kaddr getActorList(kaddr level) {
        return getPtr(level + Offsets::LevelToAActorsLite);
    }

    static kaddr getActor(kaddr actorList, int index) {
        return getPtr(actorList + (index * Offsets::PointerSize));
    }

    static int getActorsCount(kaddr level) {
        return Read<int>(level + Offsets::LevelToAActorsLite + Offsets::PointerSize);
    }
};
struct Actor {
    static kaddr getRootComponent(kaddr actor){
        return getPtr(actor + Offsets::ActorToRootComponent);
    }
};
struct LiteActor {
    static kaddr getRootComponent(kaddr actor){
        return getPtr(actor + Offsets::ActorToRootComponentLite);
    }
};
struct Character {
    static kaddr getMeshComponent(kaddr character){
        return getPtr(character + Offsets::CharacterToMesh);
    }
};
struct LiteCharacter {
    static kaddr getMeshComponent(kaddr character){
        return getPtr(character + Offsets::CharacterToMeshLite);
    }
};
struct SceneComponent {
    static PMVector3 getLocation(kaddr scenecomp){
        return Read<PMVector3>(scenecomp + Offsets::SceneComponentToComponentToWorld + 0x10);
    }

    static FTransform getComponentToWorld(kaddr scenecomp){
        return Read<FTransform>(scenecomp + Offsets::SceneComponentToComponentToWorld);
    }
};
struct LiteSceneComponent {
    static PMVector3 getLocation(kaddr scenecomp){
        return Read<PMVector3>(scenecomp + Offsets::SceneComponentToComponentToWorld + 0x10);
    }

    static FLiteTransform getComponentToWorld(kaddr scenecomp){
        return Read<FLiteTransform>(scenecomp + Offsets::SceneComponentToComponentToWorld);
    }
};
struct PlayerCameraManager {
    static MinimalViewInfo getPOV(kaddr camera){
        return Read<MinimalViewInfo>(camera + Offsets::PlayerCameraManagerToCameraCacheEntry + Offsets::CameraCacheEntryToMinimalViewInfo);
    }
};

struct PlayerController {
    static uint32 getPlayerKey(kaddr pc){
        return Read<uint32>(pc + Offsets::UAEPlayerControllerToPlayerKey);
    }

    static int getTeamID(kaddr pc){
        return Read<int>(pc + Offsets::UAEPlayerControllerToTeamID);
    }
};
struct LitePlayerController {
    static uint32 getLitePlayerKey(kaddr pc){
        return Read<uint32>(pc + Offsets::UAEPlayerControllerToPlayerKeyLite);
    }

    static int getLiteTeamID(kaddr pc){
        return Read<int>(pc + Offsets::UAEPlayerControllerToTeamIDLite);
    }
};
struct SkeletalMeshComponent {
    static kaddr getBoneArr(kaddr smc){
        return getPtr(smc + Offsets::SkeletalMeshComponentToCachedComponentSpaceTransforms);
    }

    static FTransform getBone(kaddr BoneArr, int index) {
        return FTransform::ReadTransform(BoneArr + (index * Offsets::FTransformSizeInGame));
    }
};
struct LiteSkeletalMeshComponent {
    static kaddr LitegetBoneArr(kaddr smc){
        return getPtr(smc + Offsets::SkeletalMeshComponentToCachedComponentSpaceTransformsLite);
    }
    static FLiteTransform getBone(kaddr BoneArr, int index) {
        return FLiteTransform::ReadLiteTransform(BoneArr + (index * Offsets::FTransformSizeInGame));
    }
};

struct Player {
    static uint32 getPlayerKey(kaddr pc){
        return Read<uint32>(pc + Offsets::UAECharacterToPlayerKey);
    }

    static FString getPlayerName(kaddr uae){
        return Read<FString>(uae + Offsets::UAECharacterToPlayerName);
    }

    static int getTeamID(kaddr uae){
        return Read<int>(uae + Offsets::UAECharacterToTeamID);
    }

    static bool IsAI(kaddr uae){
        return (Read<char>(uae + Offsets::UAECharacterTobIsAI) & 255) != 0;
    }
    static bool LiteIsAI(kaddr uae){
        return (Read<char>(uae + Offsets::UAECharacterTobIsAILite) & 255) != 0;
    }
    static float getHealth(kaddr stc){
        return Read<float>(stc + Offsets::STExtraCharacterToHealth);
    }
    static float LitegetHealth(kaddr stc){
        return Read<float>(stc + Offsets::STExtraCharacterToHealthLite);
    }
    static PMVector3 getBoneLoc(FMatrix c2wMatrix, kaddr boneArr, int index){
        FTransform BoneTrans = SkeletalMeshComponent::getBone(boneArr, index);
        FMatrix boneMatrix = TransformToMatrixWithScale(BoneTrans);
        return MarixToVector(MatrixMulti(boneMatrix, c2wMatrix));
    }
    static PMVector3 LitegetBoneLoc(FMatrix c2wMatrix, kaddr boneArr, int index){
        FLiteTransform BoneTrans = LiteSkeletalMeshComponent::getBone(boneArr, index);
        FMatrix boneMatrix = TransformToMatrixWithScaleLite(BoneTrans);
        return MarixToVector(MatrixMulti(boneMatrix, c2wMatrix));
    }
};

#endif
