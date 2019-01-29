package gtPlusPlus.core.tileentities;

import cpw.mods.fml.common.registry.GameRegistry;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.general.BlockSuperLight.TileEntitySuperLight;
import gtPlusPlus.core.block.machine.Machine_SuperJukebox.TileEntitySuperJukebox;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.tileentities.general.*;
import gtPlusPlus.core.tileentities.machines.*;
import gtPlusPlus.plugin.villagers.tile.TileEntityGenericSpawner;

public class ModTileEntities {

	public static void init() {
		Logger.INFO("Registering Tile Entities.");
		GameRegistry.registerTileEntity(TileEntityHeliumGenerator.class, "HeliumGenerator");
		GameRegistry.registerTileEntity(TileEntityWorkbench.class, "TileWorkbench");
		GameRegistry.registerTileEntity(TileEntityWorkbenchAdvanced.class, "TileWorkbenchAdvanced");
		GameRegistry.registerTileEntity(TileEntityFishTrap.class, "TileFishTrap");
		GameRegistry.registerTileEntity(TileEntityFirepit.class, "TileFirePit");
		GameRegistry.registerTileEntity(TileEntityInfiniteFluid.class, "TileInfiniteFluid");
		GameRegistry.registerTileEntity(TileEntityProjectTable.class, "TileProjectTable");
		GameRegistry.registerTileEntity(TileEntityTradeTable.class, "TileTradeTable");		
		GameRegistry.registerTileEntity(TileEntityModularityTable.class, "TileEntityModularityTable");
		GameRegistry.registerTileEntity(TileEntityXpConverter.class, "TileEntityXpConverter");
		GameRegistry.registerTileEntity(TileEntityGenericSpawner.class, "TileEntityGenericSpawner");
		GameRegistry.registerTileEntity(TileEntityCircuitProgrammer.class, "TileCircuitProgrammer");
		GameRegistry.registerTileEntity(TileEntityPlayerDoorBase.class, "TilePlayerDoorBase");
		GameRegistry.registerTileEntity(TileEntityDecayablesChest.class, "TileDecayablesChest");		
		GameRegistry.registerTileEntity(TileEntitySuperJukebox.class, "TileEntitySuperJukebox");
		GameRegistry.registerTileEntity(TileEntitySuperLight.class, "TileEntitySuperLight");

		
		//Mod TEs
		if (LoadedMods.Thaumcraft){
			
		}
	}

}
