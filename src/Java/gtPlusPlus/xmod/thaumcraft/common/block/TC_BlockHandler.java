package gtPlusPlus.xmod.thaumcraft.common.block;

import net.minecraft.block.Block;

public class TC_BlockHandler {

	static Block blockFurnace;
	
	public static void run(){
		blockFurnace = new BlockFastAlchemyFurnace();
	}
	
}
