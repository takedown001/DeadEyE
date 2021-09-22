//
// Created by XCODE on 9/14/2021.
//

#ifndef OFFSETS_H
#define OFFSETS_H


namespace Offsets {
    enum Offsets {
        //Others
        GWorld = 0x7172AFC,
        GNames = 0x742D894,
        PointerSize = 0x4,
        //BGMI
        UGWorld = 0x7172AFC,
        UGNames =  0x742D894,

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
        UAEPlayerControllerToPlayerKey = 0x62C,
        UAEPlayerControllerToTeamID = 0x644,
        //Class: PlayerCameraManager
        PlayerCameraManagerToCameraCacheEntry = 0x350,
        //Class: CameraCacheEntry
        CameraCacheEntryToMinimalViewInfo = 0x10,
        //Class: SceneComponent
        SceneComponentToComponentToWorld = 0x150,
        //Class: SkeletalMeshComponent
        SkeletalMeshComponentToCachedComponentSpaceTransforms = 0x724,
        //Class: Actor
        ActorToRootComponent = 0x14C,
        //Class: Character
        CharacterToMesh = 0x320,
        //Class: UAECharacter
        UAECharacterToPlayerName = 0x648,
        UAECharacterToPlayerKey = 0x660,
        UAECharacterToTeamID = 0x670,
        UAECharacterTobIsAI = 0x6E8,
        UAECharacterPlayerUID = 0x664,
        //Class: STExtraCharacter
        STExtraCharacterToHealth = 0x928,


    };
}

#endif