package gtPlusPlus.xmod.thermalfoundation.block;

import cofh.core.fluid.BlockFluidCoFHBase;

public class TF_Blocks {

	public static BlockFluidCoFHBase	blockFluidPyrotheum;
	public static BlockFluidCoFHBase	blockFluidCryotheum;

	public static void init() {
	}

	public static void postInit() {

	}

	public static void preInit() {
		TF_Blocks.blockFluidPyrotheum = new TF_Block_Fluid_Pyrotheum();
		TF_Blocks.blockFluidCryotheum = new TF_Block_Fluid_Cryotheum();
		TF_Blocks.blockFluidPyrotheum.preInit();
		TF_Blocks.blockFluidCryotheum.preInit();
	}
}
