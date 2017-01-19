package gtPlusPlus.xmod.gregtech.loaders;

import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.common.blocks.GregtechMetaCasingBlocks;
import gtPlusPlus.xmod.gregtech.common.blocks.GregtechMetaCasingBlocks2;

public class Gregtech_Blocks {

	public static void run(){

		//Casing Blocks
		ModBlocks.blockCasingsMisc = new GregtechMetaCasingBlocks();
		ModBlocks.blockCasings2Misc = new GregtechMetaCasingBlocks2();
		
	}
}
