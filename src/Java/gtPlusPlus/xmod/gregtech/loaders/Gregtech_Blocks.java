package gtPlusPlus.xmod.gregtech.loaders;

import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.common.blocks.GregtechBlockMachines;
import gtPlusPlus.xmod.gregtech.common.blocks.GregtechMetaCasingBlocks;

public class Gregtech_Blocks {

	public static void run(){

		//Casing Blocks
		ModBlocks.blockCasingsMisc = new GregtechMetaCasingBlocks();
		ModBlocks.blockMetaTileEntity = new GregtechBlockMachines();
	}

}
