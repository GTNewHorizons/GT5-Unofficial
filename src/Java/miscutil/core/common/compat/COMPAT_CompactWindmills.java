package miscutil.core.common.compat;

import static miscutil.core.common.compat.COMPAT_HANDLER.RemoveRecipeQueue;



public class COMPAT_CompactWindmills {

	public static void OreDict(){
		run();		
	}

	private static final void run(){
		RemoveRecipeQueue.add("CompactWindmills:WOOL");
		RemoveRecipeQueue.add("CompactWindmills:WOOD");
		RemoveRecipeQueue.add("CompactWindmills:ALLOY");
		RemoveRecipeQueue.add("CompactWindmills:CARBON");
		RemoveRecipeQueue.add("CompactWindmills:IRIDIUM");
	}
}
