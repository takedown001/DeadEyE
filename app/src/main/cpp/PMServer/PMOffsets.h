#ifndef OFFSETS_H
#define OFFSETS_H


namespace Offsets {
	enum Offsets {
		//Others
		GWorld = 0x6CB087C,
		GNames = 0x6F5AA94,
		PointerSize = 0x4,
        UGWorld = 0x6CB08FC,
        UGNames =  0x6F5AB14,
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
		UAEPlayerControllerToPlayerKey = 0x59C,
		UAEPlayerControllerToTeamID = 0x5B4,
		//Class: PlayerCameraManager
		PlayerCameraManagerToCameraCacheEntry = 0x330,
		//Class: CameraCacheEntry
		CameraCacheEntryToMinimalViewInfo = 0x10,
		//Class: SceneComponent
		SceneComponentToComponentToWorld = 0x140,
		//Class: SkeletalMeshComponent
		SkeletalMeshComponentToCachedComponentSpaceTransforms = 0x6F0,
		//Class: Actor
		ActorToRootComponent = 0x138,
		//Class: Character
		CharacterToMesh = 0x308,
		//Class: UAECharacter
		UAECharacterToPlayerName = 0x5F8,
		UAECharacterToPlayerKey = 0x610,
		UAECharacterToTeamID = 0x620,
		UAECharacterTobIsAI = 0x694,
		//Class: STExtraCharacter
		STExtraCharacterToHealth = 0x880,
	};
}

#endif