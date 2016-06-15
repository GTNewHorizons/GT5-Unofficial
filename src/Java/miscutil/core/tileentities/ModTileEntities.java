package miscutil.core.tileentities;

import miscutil.core.block.heliumgen.tileentity.TileEntityHeliumGenerator;
import miscutil.core.util.Utils;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModTileEntities {

	
	public static void init(){
		Utils.LOG_INFO("Registering Tile Entities.");
		//GameRegistry.registerTileEntity(TileEntityReverter.class, "TE_blockGriefSaver");
		GameRegistry.registerTileEntity(TileEntityReverter.class, "Tower Reverter");
		GameRegistry.registerTileEntity(TileEntityNHG.class, "NuclearFueledHeliumGenerator");
        GameRegistry.registerTileEntity(TileEntityHeliumGenerator.class, "Helium");
	}
	
}
