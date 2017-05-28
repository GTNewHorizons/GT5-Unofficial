package gtPlusPlus.core.block;

import cpw.mods.fml.common.registry.GameRegistry;
import gtPlusPlus.core.block.base.BasicBlock.BlockTypes;
import gtPlusPlus.core.block.base.BlockBaseOre;
import gtPlusPlus.core.block.general.*;
import gtPlusPlus.core.block.machine.*;
import gtPlusPlus.core.fluids.FluidRegistryHandler;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;

public final class ModBlocks {

	public static Block blockFishTrap;
	public static Block blockWorkbench;
	public static Block blockWorkbenchAdvanced;
	//Blocks
	//public static Block blockBloodSteel;
	//public static Block blockStaballoy;
	// WIP TODO public static Block blockToolBuilder;
	public static Block blockGriefSaver;
	public static Block blockCasingsMisc;
	public static Block blockMetaTileEntity;
	public static Block blockHeliumGenerator;
	public static Block blockNHG;
	public static Block blockCharger;

	public static Block MatterFabricatorEffectBlock;

	public static Fluid fluidJackDaniels = new Fluid("fluidJackDaniels");
	public static Block blockFluidJackDaniels;
	public static Block blockCasings2Misc;

	public static Block blockFirePit;

	public static Block blockOreFluorite;
	
	public static Block blockMiningExplosive;
	
	public static Block blockHellfire;

	public static void init() {
		Utils.LOG_INFO("Initializing Blocks.");
		//blockGriefSaver = new TowerDevice().setBlockName("blockGriefSaver").setCreativeTab(AddToCreativeTab.tabBlock).setBlockTextureName("blockDefault");

		registerBlocks();
	}

	public static void registerBlocks(){

		Utils.LOG_INFO("Registering Blocks.");
		GameRegistry.registerBlock(MatterFabricatorEffectBlock = new LightGlass(Material.glass, false).setHardness(0.1F).setBlockTextureName(CORE.MODID + ":" + "blockMFEffect").setStepSound(Block.soundTypeGlass), "blockMFEffect");

		//Fluids
		FluidRegistryHandler.registerFluids();

		//Workbench
		blockWorkbench = new Machine_Workbench().setHardness(1.5F);
		blockWorkbenchAdvanced = new Machine_WorkbenchAdvanced().setHardness(2.5F);
		blockHeliumGenerator = new HeliumGenerator();
		blockFirePit = new FirePit();
		blockFishTrap = new FishTrap();
		blockOreFluorite = new BlockBaseOre("oreFluorite", "Fluorite", Material.rock, BlockTypes.ORE, Utils.rgbtoHexValue(120, 120, 30), 3);
		blockMiningExplosive = new MiningExplosives();
		blockHellfire = new HellFire();

	}


}