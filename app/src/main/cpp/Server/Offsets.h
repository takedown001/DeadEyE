#ifndef OFFSETS_H
#define OFFSETS_H

namespace Offsets {
    enum Offsets {
		//Global
		GWorld = 0x6333120,
		GNames = 0x65C1D64,
		PointerSize = 0x4,

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
		UAEPlayerControllerToPlayerKey = 0x58C,
		UAEPlayerControllerToTeamID = 0x5A4,
		//Class: PlayerCameraManager
		PlayerCameraManagerToCameraCacheEntry = 0x330,
		//Class: CameraCacheEntry
		CameraCacheEntryToMinimalViewInfo = 0x10,
		//Class: SceneComponent
		SceneComponentToComponentToWorld = 0x140,
		//Class: SkeletalMeshComponent
		SkeletalMeshComponentToCachedComponentSpaceTransforms = 0x6B8,
		//Class: Actor
		ActorToRootComponent = 0x138,
		//Class: Character
		CharacterToMesh = 0x308,
		//Class: UAECharacter
		UAECharacterToPlayerName = 0x5E8,
		UAECharacterToPlayerKey = 0x600,
		UAECharacterToTeamID = 0x610,
		UAECharacterTobIsAI = 0x680,
		//Class: STExtraCharacter
		STExtraCharacterToHealth = 0x788,
    };
}

#endif
