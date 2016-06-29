package miscutil.core.intermod.forestry.apiculture.tiles;

import miscutil.core.intermod.forestry.apiculture.multiblock.TileAlvearyMutatron;
import miscutil.core.lib.CORE;
import cpw.mods.fml.common.registry.GameRegistry;

public class FR_TileHandler {

	public static void init(){
		if (CORE.DEBUG){
			GameRegistry.registerTileEntity(TileAlvearyMutatron.class, "MiscUtils.AlvearyMutatron");
		}
	}	
}
