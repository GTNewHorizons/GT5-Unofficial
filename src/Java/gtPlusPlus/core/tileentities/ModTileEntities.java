package gtPlusPlus.core.tileentities;

import cpw.mods.fml.common.registry.GameRegistry;
import gtPlusPlus.core.tileentities.general.TileEntityFirepit;
import gtPlusPlus.core.tileentities.general.TileEntityFishTrap;
import gtPlusPlus.core.tileentities.general.TileEntityHeliumGenerator;
import gtPlusPlus.core.tileentities.general.TileEntityInfiniteFluid;
import gtPlusPlus.core.tileentities.machines.TileEntityModularityTable;
import gtPlusPlus.core.tileentities.machines.TileEntityProjectTable;
import gtPlusPlus.core.tileentities.machines.TileEntityTradeTable;
import gtPlusPlus.core.tileentities.machines.TileEntityWorkbench;
import gtPlusPlus.core.tileentities.machines.TileEntityWorkbenchAdvanced;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.thaumcraft.common.tile.TileFastAlchemyFurnace;
import gtPlusPlus.xmod.thaumcraft.common.tile.TileFastArcaneAlembic;

public class ModTileEntities {

	public static void init() {
		Utils.LOG_INFO("Registering Tile Entities.");
		// GameRegistry.registerTileEntity(TileEntityReverter.class,
		// "TE_blockGriefSaver");
		// GameRegistry.registerTileEntity(TileEntityReverter.class, "Tower
		// Reverter");
		// GameRegistry.registerTileEntity(TileEntityNHG.class,
		// "NuclearFueledHeliumGenerator");
		// GameRegistry.registerTileEntity(TileEntityCharger.class,
		// "TE_Charger");
		GameRegistry.registerTileEntity(TileEntityHeliumGenerator.class, "HeliumGenerator");
		GameRegistry.registerTileEntity(TileEntityWorkbench.class, "TileWorkbench");
		GameRegistry.registerTileEntity(TileEntityWorkbenchAdvanced.class, "TileWorkbenchAdvanced");
		GameRegistry.registerTileEntity(TileEntityFishTrap.class, "TileFishTrap");
		GameRegistry.registerTileEntity(TileEntityFirepit.class, "TileFirePit");
		GameRegistry.registerTileEntity(TileEntityInfiniteFluid.class, "TileInfiniteFluid");
		GameRegistry.registerTileEntity(TileEntityProjectTable.class, "TileProjectTable");
		GameRegistry.registerTileEntity(TileEntityTradeTable.class, "TileTradeTable");
		GameRegistry.registerTileEntity(TileEntityModularityTable.class, "TileEntityModularityTable");
		GameRegistry.registerTileEntity(TileFastAlchemyFurnace.class, "TileFastAlchemyFurnace");
		GameRegistry.registerTileEntity(TileFastArcaneAlembic.class, "TileFastArcaneAlembic");

	}

}
