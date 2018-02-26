package gtPlusPlus.xmod.bop.blocks;

import net.minecraft.block.Block;

import gtPlusPlus.xmod.bop.blocks.rainforest.LeavesRainforestTree;
import gtPlusPlus.xmod.bop.blocks.rainforest.LogRainforestTree;
import gtPlusPlus.xmod.bop.blocks.rainforest.SaplingRainforestTree;

public class BOP_Block_Registrator {

	public static Block log_Rainforest;
	public static Block leaves_Rainforest;
	public static Block sapling_Rainforest;
	
	//Runs Each tree Type separately
	public static final void run(){
		registerTree_Rainforest();
	}
	
	
	private static final boolean registerTree_Rainforest(){
		log_Rainforest = new LogRainforestTree();
		leaves_Rainforest = new LeavesRainforestTree();
		sapling_Rainforest = new SaplingRainforestTree();		
		return true;
	}
	
	
	
}
