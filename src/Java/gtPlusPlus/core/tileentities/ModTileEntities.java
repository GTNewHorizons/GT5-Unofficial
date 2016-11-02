package gtPlusPlus.core.tileentities;

import gtPlusPlus.core.tileentities.machines.TileEntityWorkbench;
import gtPlusPlus.core.tileentities.machines.TileEntityWorkbenchAdvanced;
import gtPlusPlus.core.util.Utils;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModTileEntities {

	
	public static void init(){
		Utils.LOG_INFO("Registering Tile Entities.");
		//GameRegistry.registerTileEntity(TileEntityReverter.class, "TE_blockGriefSaver");
		//GameRegistry.registerTileEntity(TileEntityReverter.class, "Tower Reverter");
		//GameRegistry.registerTileEntity(TileEntityNHG.class, "NuclearFueledHeliumGenerator");
		//GameRegistry.registerTileEntity(TileEntityCharger.class, "TE_Charger");
       // GameRegistry.registerTileEntity(TileEntityHeliumGenerator.class, "Helium");
		GameRegistry.registerTileEntity(TileEntityWorkbench.class, "TileWorkbench");
		GameRegistry.registerTileEntity(TileEntityWorkbenchAdvanced.class, "TileWorkbenchAdvanced");
	}
	
}
