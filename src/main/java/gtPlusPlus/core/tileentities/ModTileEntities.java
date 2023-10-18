package gtPlusPlus.core.tileentities;

import cpw.mods.fml.common.registry.GameRegistry;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.general.BlockSuperLight.TileEntitySuperLight;
import gtPlusPlus.core.block.machine.Machine_SuperJukebox.TileEntitySuperJukebox;
import gtPlusPlus.core.tileentities.general.TileEntityCircuitProgrammer;
import gtPlusPlus.core.tileentities.general.TileEntityDecayablesChest;
import gtPlusPlus.core.tileentities.general.TileEntityFishTrap;
import gtPlusPlus.core.tileentities.general.TileEntityInfiniteFluid;
import gtPlusPlus.core.tileentities.general.TileEntityVolumetricFlaskSetter;
import gtPlusPlus.core.tileentities.machines.TileEntityAdvPooCollector;
import gtPlusPlus.core.tileentities.machines.TileEntityPestKiller;
import gtPlusPlus.core.tileentities.machines.TileEntityPooCollector;
import gtPlusPlus.core.tileentities.machines.TileEntityProjectTable;

public class ModTileEntities {

    public static void init() {
        Logger.INFO("Registering Tile Entities.");
        GameRegistry.registerTileEntity(TileEntityPooCollector.class, "TileEntityPooCollector");
        GameRegistry.registerTileEntity(TileEntityAdvPooCollector.class, "TileEntityAdvPooCollector");
        GameRegistry.registerTileEntity(TileEntityFishTrap.class, "TileFishTrap");
        GameRegistry.registerTileEntity(TileEntityInfiniteFluid.class, "TileInfiniteFluid");
        GameRegistry.registerTileEntity(TileEntityProjectTable.class, "TileProjectTable");
        GameRegistry.registerTileEntity(TileEntityCircuitProgrammer.class, "TileCircuitProgrammer");
        GameRegistry.registerTileEntity(TileEntityDecayablesChest.class, "TileDecayablesChest");
        GameRegistry.registerTileEntity(TileEntitySuperJukebox.class, "TileEntitySuperJukebox");
        GameRegistry.registerTileEntity(TileEntitySuperLight.class, "TileEntitySuperLight");
        GameRegistry.registerTileEntity(TileEntityPestKiller.class, "TileEntityPestKiller");

        GameRegistry.registerTileEntity(TileEntityVolumetricFlaskSetter.class, "TileEntityVolumetricFlaskSetter");
    }

}
