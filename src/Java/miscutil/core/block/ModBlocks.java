package miscutil.core.block;

import miscutil.core.block.base.BasicBlock;
import miscutil.core.block.base.BasicBlock.BlockTypes;
import miscutil.core.block.general.LightGlass;
import miscutil.core.block.general.fluids.FluidRegistryHandler;
import miscutil.core.block.machine.BlockGtFrameBox;
import miscutil.core.lib.CORE;
import miscutil.core.util.Utils;
import miscutil.core.xmod.gregtech.common.blocks.GregtechMetaCasingBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;
import cpw.mods.fml.common.registry.GameRegistry;

public final class ModBlocks {

	//Blocks
	public static Block blockBloodSteel;
	public static Block blockStaballoy;
	// WIP TODO public static Block blockToolBuilder;
	public static Block blockGriefSaver;
	public static Block blockCasingsMisc;
	public static Block blockHeliumGenerator;
	public static Block blockNHG;
	public static Block blockCharger;

	public static Block MatterFabricatorEffectBlock;

	public static Fluid fluidJackDaniels = new Fluid("fluidJackDaniels");
	public static Block blockFluidJackDaniels; 
	public static Block blockGtFrameSet1;
	public static Block blockGtFrameTantalloy60;
	public static Block blockGtFrameTantalloy61;



	public static void init() {
		Utils.LOG_INFO("Initializing Blocks.");
		//blockGriefSaver = new TowerDevice().setBlockName("blockGriefSaver").setCreativeTab(AddToCreativeTab.tabBlock).setBlockTextureName("blockDefault");

		registerBlocks(); 
	}

	public static void registerBlocks(){

		Utils.LOG_INFO("Registering Blocks.");

		//Blood Steel Block
		GameRegistry.registerBlock(blockBloodSteel = new BasicBlock("blockBloodSteel", Material.iron), "blockBloodSteel");

		//Staballoy Block
		GameRegistry.registerBlock(blockStaballoy = new BasicBlock("blockStaballoy", Material.iron), "blockStaballoy");

		//GameRegistry.registerBlock(MatterFabricatorEffectBlock = new MatterFabricatorEffectBlock(), "blockMF_Effect");

		GameRegistry.registerBlock(MatterFabricatorEffectBlock = new LightGlass(Material.glass, false).setHardness(0.1F).setBlockTextureName(CORE.MODID + ":" + "blockMFEffect").setStepSound(Block.soundTypeGlass), "blockMFEffect");


		//Casing Blocks
		blockCasingsMisc = new GregtechMetaCasingBlocks();

		//Fluids
		FluidRegistryHandler.registerFluids();

		// blockHeliumGenerator = GameRegistry.registerBlock(new HeliumGenerator(), "Helium_Collector");
		// blockNHG = GameRegistry.registerBlock(new Machine_NHG("blockNuclearFueledHeliumGenerator"), "blockNuclearFueledHeliumGenerator");
		// blockCharger = GameRegistry.registerBlock(new Machine_Charger("blockMachineCharger"), "blockMachineCharger");


		//WIP TODO
		//GameRegistry.registerBlock(blockGriefSaver, "blockGriefSaver");

		//GtFrames
		GameRegistry.registerBlock(blockGtFrameSet1 = new BlockGtFrameBox("blockGtFrameSet1", Material.iron ,BlockTypes.FRAME, true, Utils.rgbtoHexValue(68, 75, 66), Utils.rgbtoHexValue(68, 75, 166), Utils.rgbtoHexValue(122, 135, 196)), "blockGtFrameSet1");
		//GameRegistry.registerBlock(blockGtFrameTantalloy60 = new BlockBaseModular("blockGtFrameTantalloy60", BlockTypes.FRAME, Utils.rgbtoHexValue(68, 75, 166)), "blockGtFrameTantalloy60");
		//GameRegistry.registerBlock(blockGtFrameTantalloy61 = new BlockBaseModular("blockGtFrameTantalloy61", BlockTypes.FRAME, Utils.rgbtoHexValue(122, 135, 196)), "blockGtFrameTantalloy61");

	}

}