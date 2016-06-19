package miscutil.core.block;

import miscutil.core.block.base.BasicBlock;
import miscutil.core.block.general.fluids.FluidRegistryHandler;
import miscutil.core.util.Utils;
import miscutil.gregtech.common.blocks.GregtechMetaCasingBlocks;
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

	public static Fluid fluidJackDaniels = new Fluid("fluidJackDaniels");
	public static Block blockFluidJackDaniels; 



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

		//Casing Blocks
		blockCasingsMisc = new GregtechMetaCasingBlocks();

		//Fluids
		FluidRegistryHandler.registerFluids();
		
	   // blockHeliumGenerator = GameRegistry.registerBlock(new HeliumGenerator(), "Helium_Collector");
       // blockNHG = GameRegistry.registerBlock(new Machine_NHG("blockNuclearFueledHeliumGenerator"), "blockNuclearFueledHeliumGenerator");
       // blockCharger = GameRegistry.registerBlock(new Machine_Charger("blockMachineCharger"), "blockMachineCharger");
        

		//WIP TODO
		//GameRegistry.registerBlock(blockGriefSaver, "blockGriefSaver");
	}

}