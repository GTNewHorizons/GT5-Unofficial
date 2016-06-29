package miscutil.core.intermod.thermalfoundation.fluid;

import miscutil.core.intermod.thermalfoundation.item.TF_Items;
import net.minecraft.block.BlockDispenser;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import cofh.core.util.fluid.DispenserEmptyBucketHandler;
import cofh.core.util.fluid.DispenserFilledBucketHandler;

public class TF_Fluids
{
	public static Fluid fluidPyrotheum;
	public static Fluid fluidCryotheum;

	public static void preInit()
	{
		
		if (FluidRegistry.getFluid("pyrotheum") == null){
			fluidPyrotheum = new Fluid("pyrotheum").setLuminosity(15).setDensity(2000).setViscosity(1200).setTemperature(4000).setRarity(EnumRarity.rare);
			registerFluid(fluidPyrotheum, "pyrotheum");
		}
		else {
			fluidPyrotheum = FluidRegistry.getFluid("pyrotheum");
		}
		if (FluidRegistry.getFluid("cryotheum") == null){
			fluidCryotheum = new Fluid("cryotheum").setLuminosity(0).setDensity(4000).setViscosity(3000).setTemperature(50).setRarity(EnumRarity.rare);
			registerFluid(fluidCryotheum, "cryotheum");	
		}
		else {
			fluidCryotheum = FluidRegistry.getFluid("cryotheum");
		}
	}

	public static void init() {}

	public static void postInit() {}

	public static void registerFluid(Fluid paramFluid, String paramString)
	{
		if (!FluidRegistry.isFluidRegistered(paramString)) {
			FluidRegistry.registerFluid(paramFluid);
		}
		paramFluid = FluidRegistry.getFluid(paramString);
	}

	public static void registerDispenserHandlers()
	{
		BlockDispenser.dispenseBehaviorRegistry.putObject(TF_Items.itemBucket, new DispenserFilledBucketHandler());
		BlockDispenser.dispenseBehaviorRegistry.putObject(Items.bucket, new DispenserEmptyBucketHandler());
	}
}
