package gtPlusPlus.xmod.thermalfoundation.fluid;

import cofh.core.util.fluid.DispenserEmptyBucketHandler;
import cofh.core.util.fluid.DispenserFilledBucketHandler;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.xmod.thermalfoundation.item.TF_Items;
import net.minecraft.block.BlockDispenser;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class TF_Fluids
{
	public static Fluid fluidPyrotheum;
	public static Fluid fluidCryotheum;
	public static Fluid fluidEnder;

	public static void preInit()
	{
		if (!LoadedMods.ThermalFoundation){
			Logger.INFO("Adding in our own versions of Thermal Foundation Fluids - Non-GT");
			final Fluid pyrotheum = FluidRegistry.getFluid("pyrotheum");
			final Fluid cryotheum = FluidRegistry.getFluid("cryotheum");
			final Fluid ender = FluidRegistry.getFluid("ender");

			if (pyrotheum == null){
				Logger.INFO("Registering Blazing Pyrotheum as it does not exist.");
				fluidPyrotheum = new Fluid("pyrotheum").setLuminosity(15).setDensity(2000).setViscosity(1200).setTemperature(4000).setRarity(EnumRarity.rare);
				registerFluid(fluidPyrotheum, "pyrotheum");
			}
			else {
				Logger.INFO("Registering Blazing Pyrotheum as it is an already existing Fluid.");
				fluidPyrotheum = pyrotheum;
			}
			if (cryotheum == null){
				Logger.INFO("Registering Gelid Cryotheum as it does not exist.");
				fluidCryotheum = new Fluid("cryotheum").setLuminosity(0).setDensity(4000).setViscosity(3000).setTemperature(50).setRarity(EnumRarity.rare);
				registerFluid(fluidCryotheum, "cryotheum");
			}
			else {
				Logger.INFO("Registering Gelid Cryotheum as it is an already existing Fluid.");
				fluidCryotheum = cryotheum;
			}
			
			if (ender == null){
				Logger.INFO("Registering Resonant Ender as it does not exist.");
				fluidEnder = new Fluid("ender").setLuminosity(3).setDensity(4000).setViscosity(3000).setTemperature(300).setRarity(EnumRarity.uncommon);
			    registerFluid(fluidEnder, "ender");
			}
			else {
				Logger.INFO("Registering Resonant Ender as it is an already existing Fluid.");
				fluidEnder = ender;
			}
		}
		else {
			Logger.INFO("Thermal Foundation is already loaded, no need to add our own Cryotheum/Pyrotheum.");
		}
	}

	public static void init() {}

	public static void postInit() {}

	public static void registerFluid(final Fluid paramFluid, final String paramString)
	{
		if (!FluidRegistry.isFluidRegistered(paramString)) {
			FluidRegistry.registerFluid(paramFluid);
		}
	}

	public static void registerDispenserHandlers()
	{
		BlockDispenser.dispenseBehaviorRegistry.putObject(TF_Items.itemBucket, new DispenserFilledBucketHandler());
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.bucket, new DispenserEmptyBucketHandler());
	}
}
