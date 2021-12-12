package gtPlusPlus.xmod.thermalfoundation.block;

import cofh.core.fluid.BlockFluidCoFHBase;

public class TF_Blocks
{

	public static BlockFluidCoFHBase blockFluidPyrotheum;
	public static BlockFluidCoFHBase blockFluidCryotheum;
	public static BlockFluidCoFHBase blockFluidEnder;


	public static void preInit()
	{
		blockFluidPyrotheum = new TF_Block_Fluid_Pyrotheum();
		blockFluidCryotheum = new TF_Block_Fluid_Cryotheum();
		blockFluidEnder = new TF_Block_Fluid_Ender();
		blockFluidPyrotheum.preInit();
		blockFluidCryotheum.preInit();
		blockFluidEnder.preInit();
	}

	public static void init() {}

	public static void postInit()
	{

	}
}
