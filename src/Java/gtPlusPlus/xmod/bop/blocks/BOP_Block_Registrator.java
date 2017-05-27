package gtPlusPlus.xmod.bop.blocks;

import gtPlusPlus.xmod.bop.blocks.rainforest.*;
import net.minecraft.block.Block;

public class BOP_Block_Registrator {

	static Block log_Rainforest;
	static Block Leaves_Rainforest;
	static Block sapling_Rainforest;
	
	//Runs Each tree Type separately
	public static final void run(){
		registerTree_Rainforest();
	}
	
	
	private static final boolean registerTree_Rainforest(){
		log_Rainforest = new LogRainforestTree();
		Leaves_Rainforest = new LeavesRainforestTree();
		sapling_Rainforest = new SaplingRainforestTree();		
		return true;
	}
	
	
	
}
