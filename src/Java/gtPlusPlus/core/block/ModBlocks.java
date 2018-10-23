package gtPlusPlus.core.block;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.base.BasicBlock.BlockTypes;
import gtPlusPlus.core.block.base.BlockBaseOre;
import gtPlusPlus.core.block.general.*;
import gtPlusPlus.core.block.general.antigrief.BlockWitherProof;
import gtPlusPlus.core.block.machine.*;
import gtPlusPlus.core.block.machine.bedrock.Mining_Head_Fake;
import gtPlusPlus.core.block.machine.bedrock.Mining_Pipe_Fake;
import gtPlusPlus.core.fluids.FluidRegistryHandler;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.plugin.villagers.block.BlockGenericSpawner;
import net.minecraftforge.fluids.Fluid;

public final class ModBlocks {

	public static Block blockCircuitProgrammer;
	public static Block blockFakeMiningPipe;
	public static Block blockFakeMiningHead;
	
	public static Block blockFishTrap;
	public static Block blockWorkbench;
	public static Block blockWorkbenchAdvanced;
	//Blocks
	//public static Block blockBloodSteel;
	//public static Block blockStaballoy;
	// WIP TODO public static Block blockToolBuilder;
	public static Block blockGriefSaver;
	
	public static Block blockCasingsMisc;
	public static Block blockCasings2Misc;
	public static Block blockCasings3Misc;
	public static Block blockCasings4Misc;
	
	public static Block blockMetaTileEntity;
	public static Block blockHeliumGenerator;
	public static Block blockNHG;
	public static Block blockCharger;

	public static Block MatterFabricatorEffectBlock;

	public static Fluid fluidSludge = new Fluid("fluid.sludge");
	public static Block blockFluidSludge;

	public static Block blockFirePit;

	public static Block blockOreFluorite;

	public static Block blockMiningExplosive;

	public static Block blockHellfire;
	public static Block blockInfiniteFLuidTank;
	public static Block blockProjectTable;
	public static Block blockTradeTable;
	public static Block blockModularTable;

	public static Block blockWitherGuard;
	public static Block blockXpConverter;
	public static Block blockCompressedObsidian;
	public static Block blockNet;

	public static Block blockPlayerDoorWooden;
	public static Block blockPlayerDoorIron;
	public static Block blockPlayerDoorCustom_Glass;
	public static Block blockPlayerDoorCustom_Ice;
	public static Block blockPlayerDoorCustom_Cactus;
	
	public static Block blockCustomMobSpawner;

	public static void init() {
		Logger.INFO("Initializing Blocks.");
		//blockGriefSaver = new TowerDevice().setBlockName("blockGriefSaver").setCreativeTab(AddToCreativeTab.tabBlock).setBlockTextureName("blockDefault");

		registerBlocks();
	}

	public static void registerBlocks(){

		Logger.INFO("Registering Blocks.");
		GameRegistry.registerBlock(MatterFabricatorEffectBlock = new LightGlass(Material.glass, false).setHardness(0.1F).setBlockTextureName(CORE.MODID + ":" + "blockMFEffect").setStepSound(Block.soundTypeGlass), "blockMFEffect");

		//Fluids
		FluidRegistryHandler.registerFluids();

		//Workbench
		blockWorkbench = new Machine_Workbench().setHardness(1.5F);
		blockWorkbenchAdvanced = new Machine_WorkbenchAdvanced().setHardness(2.5F);
		blockHeliumGenerator = new HeliumGenerator();
		blockFirePit = new FirePit();
		blockFishTrap = new FishTrap();
		blockInfiniteFLuidTank = new FluidTankInfinite();
		//blockOreFluorite = new BlockBaseOre.oldOreBlock("oreFluorite", "Fluorite", Material.rock, BlockTypes.ORE, Utils.rgbtoHexValue(120, 120, 30), 3);
		blockMiningExplosive = new MiningExplosives();
		blockHellfire = new HellFire();
		blockProjectTable =  new Machine_ProjectTable();
		blockTradeTable =  new Machine_TradeTable();
		blockModularTable =  new Machine_ModularityTable();
		blockWitherGuard = new BlockWitherProof();
		blockXpConverter = new BlockTankXpConverter();
		blockCompressedObsidian = new BlockCompressedObsidian();
		blockNet = new BlockNet();		

		blockFakeMiningPipe = new Mining_Pipe_Fake();
		blockFakeMiningHead = new Mining_Head_Fake();
		
		blockCircuitProgrammer = new CircuitProgrammer();
		
		blockPlayerDoorWooden = new PlayerDoors(Material.wood, "door_wood", true);
		blockPlayerDoorIron = new PlayerDoors(Material.iron, "door_iron", true);
		blockPlayerDoorCustom_Glass = new PlayerDoors(Material.glass, "door_glass", false);
		blockPlayerDoorCustom_Ice = new PlayerDoors(Material.ice, "door_ice", false);
		blockPlayerDoorCustom_Cactus = new PlayerDoors(Material.cactus, "door_cactus", false, 0.6f, Block.soundTypeGrass, "Cactus");

	}


}