package gtPlusPlus.xmod.gregtech.loaders;

import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.common.blocks.GregtechMetaCasingBlocks;
import gtPlusPlus.xmod.gregtech.common.blocks.GregtechMetaCasingBlocks2;

public class Gregtech_Blocks {

	public static void run(){

		Utils.LOG_INFO("Expanding Gregtech Texture Array from 128 -> 1024.");
		boolean didExpand = TAE.hookGtTextures();
		Utils.LOG_INFO("Did Texture Array expand correctly? "+didExpand);
		Utils.LOG_INFO("|======| Texture Array New Length: "+Textures.BlockIcons.CASING_BLOCKS.length+" |======|");
		
		//Casing Blocks
		ModBlocks.blockCasingsMisc = new GregtechMetaCasingBlocks();
		ModBlocks.blockCasings2Misc = new GregtechMetaCasingBlocks2();

	}
}
