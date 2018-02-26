package gtPlusPlus.core.fluids;

import static gtPlusPlus.core.block.ModBlocks.*;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.block.material.Material;

import gtPlusPlus.core.block.general.fluids.BlockFluidSludge;
import gtPlusPlus.core.lib.CORE;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidRegistryHandler {

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

	public static void registerFluids(){
		run();
	}

	private static void run(){
		fluidSludge();
	}

	private static void fluidSludge(){
		//testFluid
		fluidSludge.setLuminosity(8);
		fluidSludge.setDensity(8196);
		fluidSludge.setTemperature(295);
		fluidSludge.setViscosity(3000);
		fluidSludge.setGaseous(false);
		fluidSludge.setUnlocalizedName("fluid.sludge");
		FluidRegistry.registerFluid(fluidSludge);
		blockFluidSludge = new BlockFluidSludge(fluidSludge, Material.cactus).setBlockName("fluidBlockSludge");
		GameRegistry.registerBlock(blockFluidSludge, CORE.MODID + "_" + blockFluidSludge.getUnlocalizedName().substring(5));
		fluidSludge.setUnlocalizedName(blockFluidSludge.getUnlocalizedName());
	}

}
