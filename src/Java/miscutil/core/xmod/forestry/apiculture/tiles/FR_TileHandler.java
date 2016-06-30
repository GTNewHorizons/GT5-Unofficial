package miscutil.core.xmod.forestry.apiculture.tiles;

import miscutil.core.lib.CORE;
import miscutil.core.xmod.forestry.apiculture.multiblock.TileAlvearyMutatron;
import cpw.mods.fml.common.registry.GameRegistry;

public class FR_TileHandler {

	public static void init(){
		if (CORE.DEBUG){
			GameRegistry.registerTileEntity(TileAlvearyMutatron.class, "MiscUtils.AlvearyMutatron");
		}
	}	
}
