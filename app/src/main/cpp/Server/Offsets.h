#ifndef OFFSETS_H
#define OFFSETS_H

namespace Offsets {
    enum Offsets {
		//Global
		GWorld = 0x6BC94E8,
		GNames = 0x6BC03CC,
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
		SkeletalMeshComponentToCachedComponentSpaceTransforms = 0x6E0,
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
		STExtraCharacterToHealth = 0x830,
    };
}

#endif
