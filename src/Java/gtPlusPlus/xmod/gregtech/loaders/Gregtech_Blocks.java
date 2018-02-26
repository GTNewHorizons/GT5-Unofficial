package gtPlusPlus.xmod.gregtech.loaders;

import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.common.blocks.GregtechMetaCasingBlocks;
import gtPlusPlus.xmod.gregtech.common.blocks.GregtechMetaCasingBlocks2;
import gtPlusPlus.xmod.gregtech.common.blocks.GregtechMetaCasingBlocks3;

public class Gregtech_Blocks {

	public static void run(){

		Logger.INFO("Expanding Gregtech Texture Array from 128 -> 1024.");
		boolean didExpand = TAE.hookGtTextures();
		Logger.INFO("Did Texture Array expand correctly? "+didExpand);
		Logger.INFO("|======| Texture Array New Length: "+Textures.BlockIcons.CASING_BLOCKS.length+" |======|");
		
		//Casing Blocks
		ModBlocks.blockCasingsMisc = new GregtechMetaCasingBlocks();
		ModBlocks.blockCasings2Misc = new GregtechMetaCasingBlocks2();
		ModBlocks.blockCasings3Misc = new GregtechMetaCasingBlocks3();

	}
}
