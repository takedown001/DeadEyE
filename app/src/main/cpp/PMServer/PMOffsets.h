#ifndef OFFSETS_H
#define OFFSETS_H


namespace Offsets {
    enum Offsets {
        //All Versions
        GWorld = 0x6E208CC,
        GNames = 0x6E17138,
        PointerSize = 0x4,
        // BGMI
        UGWorld = 0x6E2192C,
        UGNames =  0x70D3994,

        //Struct Size
        FTransformSizeInGame = 0x30,
        //---------SDK-----------
        //Class: FNameEntry
        FNameEntryToNameString = 0x8,
        //Class: UObject
        UObjectToInternalIndex = 0x8,

        UObjectToFNameIndex = 0x10,

        //---------PUBG UEClasses-----------
        //Class: World
        WorldToPersistentLevel = 0x20,
        //Class: Level
        LevelToAActors = 0x70,
        //Class: PlayerController
        UAEPlayerControllerToPlayerKey = 0x61C,

        UAEPlayerControllerToTeamID = 0x634,
        //Class: PlayerCameraManager
        PlayerCameraManagerToCameraCacheEntry = 0x340,
        //Class: CameraCacheEntry
        CameraCacheEntryToMinimalViewInfo = 0x10,
        //Class: SceneComponent
        SceneComponentToComponentToWorld = 0x140, //not updated  //0x1AC //0x140
        //Class: SkeletalMeshComponent
        SkeletalMeshComponentToCachedComponentSpaceTransforms = 0x700,
        //Class: Actor
        ActorToRootComponent = 0x140,
        //Class: Character
        CharacterToMesh = 0x310,
        //Class: UAECharacter
        UAECharacterToPlayerName = 0x638,

        UAECharacterToPlayerKey = 0x650,

        UAECharacterToTeamID = 0x660,

        UAECharacterTobIsAI = 0x6D8,
        //Class: STExtraCharacter
        STExtraCharacterToHealth = 0x918,

    };
}

#endif