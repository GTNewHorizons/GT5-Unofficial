package gtPlusPlus.xmod.gregtech.loaders;

import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.common.blocks.GregtechMetaCasingBlocks;
import gtPlusPlus.xmod.gregtech.common.blocks.GregtechMetaCasingBlocks2;
import gtPlusPlus.xmod.gregtech.common.blocks.GregtechMetaCasingBlocks3;
import gtPlusPlus.xmod.gregtech.common.blocks.GregtechMetaCasingBlocks4;
import gtPlusPlus.xmod.gregtech.common.blocks.GregtechMetaSpecialMultiCasings;
import gtPlusPlus.xmod.gregtech.common.blocks.GregtechMetaTieredCasingBlocks1;

public class Gregtech_Blocks {

	public static void run(){
		
		//Casing Blocks
		ModBlocks.blockCasingsMisc = new GregtechMetaCasingBlocks();
		ModBlocks.blockCasings2Misc = new GregtechMetaCasingBlocks2();
		ModBlocks.blockCasings3Misc = new GregtechMetaCasingBlocks3();
		ModBlocks.blockCasings4Misc = new GregtechMetaCasingBlocks4();

		ModBlocks.blockCasingsTieredGTPP = new GregtechMetaTieredCasingBlocks1();
		ModBlocks.blockSpecialMultiCasings = new GregtechMetaSpecialMultiCasings();

	}
}
