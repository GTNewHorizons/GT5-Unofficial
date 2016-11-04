package gtPlusPlus.core.fluids;

import cpw.mods.fml.common.registry.GameRegistry;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.block.general.fluids.BlockFluidJackDaniels;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidRegistryHandler {

	private static void fluidJackDaniels() {
		// testFluid
		ModBlocks.fluidJackDaniels.setLuminosity(12);
		ModBlocks.fluidJackDaniels.setDensity(1200);
		ModBlocks.fluidJackDaniels.setTemperature(420);
		ModBlocks.fluidJackDaniels.setViscosity(750);
		ModBlocks.fluidJackDaniels.setGaseous(true);
		ModBlocks.fluidJackDaniels.setUnlocalizedName("fluidJackDaniels");
		FluidRegistry.registerFluid(ModBlocks.fluidJackDaniels);
		ModBlocks.blockFluidJackDaniels = new BlockFluidJackDaniels(ModBlocks.fluidJackDaniels, Material.water)
				.setBlockName("fluidBlockJackDaniels");
		GameRegistry.registerBlock(ModBlocks.blockFluidJackDaniels,
				CORE.MODID + "_" + ModBlocks.blockFluidJackDaniels.getUnlocalizedName().substring(5));
		ModBlocks.fluidJackDaniels.setUnlocalizedName(ModBlocks.blockFluidJackDaniels.getUnlocalizedName());
	}

	// Fluids
	/**
	 * 
	 * Luminosity .setLuminosity(luminosity) How much light does the fluid emit.
	 * Default: 0, Lava uses 15 Density .setDensity(density) How dense is the
	 * fluid, the only effect is whether or not a fluid replaces another fluid
	 * when they flow into each other. Default: 1000, the density of water at 4
	 * degrees Celsius in kg/m³ Temperature .setTemperature(temp) How hot, or
	 * cold is the fluid. Has currently no effect. Default: 295, the "normal"
	 * room temperature in degrees Kelvin, this is approximately 72°F or 22°C.
	 * Viscosity .setViscosity(viscosity) How thick the fluid is. Determines how
	 * fast it flows. Default: 1000 for water, lava uses 6000 Is Gaseous
	 * .setGaseous(boolean) Indicates if the fluid is gaseous. Used for
	 * rendering. Default: false
	 * 
	 */

	public static void registerFluids() {
		FluidRegistryHandler.run();
	}

	@SuppressWarnings("unused")
	private static void run() {
		FluidRegistryHandler.fluidJackDaniels();
	}

}
