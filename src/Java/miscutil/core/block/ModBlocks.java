package miscutil.core.block;

import miscutil.core.block.fluids.BlocktestFluid;
import miscutil.core.lib.CORE;
import miscutil.core.tileentities.TileEntityHeliumGenerator;
import miscutil.core.util.Utils;
import miscutil.gregtech.common.blocks.GregtechMetaCasingBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public final class ModBlocks {

	//Blocks
	public static Block blockBloodSteel;
	public static Block blockStaballoy;
	// WIP TODO public static Block blockToolBuilder;
	public static Block blockGriefSaver;
	public static Block blockCasingsMisc;
    public static Block blockHeliumGenerator;

	
	
	
	//Fluids
	/**
	 * 
	 * Luminosity .setLuminosity(luminosity)
 		How much light does the fluid emit. Default: 0, Lava uses 15
		Density .setDensity(density)
		How dense is the fluid, the only effect is whether or not a fluid replaces another fluid when they flow into each other. Default: 1000, the density of water at 4 degrees Celsius in kg/m³
		Temperature .setTemperature(temp)
		How hot, or cold is the fluid. Has currently no effect. Default: 295, the "normal" room temperature in degrees Kelvin, this is approximately 72°F or 22°C.
		Viscosity .setViscosity(viscosity)
		How thick the fluid is. Determines how fast it flows. Default: 1000 for water, lava uses 6000
		Is Gaseous .setGaseous(boolean)
		Indicates if the fluid is gaseous. Used for rendering. Default: false
	 * 
	 */

	public static Fluid testFluid = new Fluid("testFluid");
	public static Block testFluidBlock; 



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
		//testFluid
		testFluid.setLuminosity(12);
		testFluid.setDensity(1200);
		testFluid.setTemperature(420);
		testFluid.setViscosity(750);
		testFluid.setGaseous(true);
		FluidRegistry.registerFluid(testFluid);
		testFluidBlock = new BlocktestFluid(testFluid, Material.water).setBlockName("yourFluid");
		GameRegistry.registerBlock(testFluidBlock, CORE.MODID + "_" + testFluidBlock.getUnlocalizedName().substring(5));
		testFluid.setUnlocalizedName(testFluidBlock.getUnlocalizedName());
		
		blockHeliumGenerator = GameRegistry.registerBlock(new HeliumGenerator(), "Helium_Collector");
        GameRegistry.registerTileEntity(TileEntityHeliumGenerator.class, "Helium");

		//WIP TODO
		//GameRegistry.registerBlock(blockGriefSaver, "blockGriefSaver");
	}

}