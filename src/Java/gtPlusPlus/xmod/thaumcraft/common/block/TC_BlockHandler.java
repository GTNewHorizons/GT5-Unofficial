package gtPlusPlus.xmod.thaumcraft.common.block;

import net.minecraft.block.Block;

public class TC_BlockHandler {

	public static Block blockFastAlchemyFurnace;
	public static Block blockFastArcaneAlembic;

	public static void run(){
		blockFastAlchemyFurnace = new BlockFastAlchemyFurnace();
		blockFastArcaneAlembic = new BlockFastArcaneAlembic();
	}

}
