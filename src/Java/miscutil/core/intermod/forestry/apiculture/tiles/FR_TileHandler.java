package miscutil.core.intermod.forestry.apiculture.tiles;

import miscutil.core.intermod.forestry.apiculture.multiblock.TileAlvearyMutatron;
import cpw.mods.fml.common.registry.GameRegistry;

public class FR_TileHandler {

	public static void init(){
		// Inducers for swarmer
		//BeeManager.inducers.put(items.royalJelly.getItemStack(), 10);
		
		GameRegistry.registerTileEntity(TileAlvearyMutatron.class, "MiscUtils.AlvearyMutatron");

	}
	
}
