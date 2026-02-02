package gtPlusPlus.core.tileentities;

import cpw.mods.fml.common.registry.GameRegistry;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.general.BlockSuperLight.TileEntitySuperLight;
import gtPlusPlus.core.tileentities.general.TileEntityCircuitProgrammer;
import gtPlusPlus.core.tileentities.general.TileEntityDecayablesChest;
import gtPlusPlus.core.tileentities.general.TileEntityFishTrap;
import gtPlusPlus.core.tileentities.general.TileEntityInfiniteFluid;
import gtPlusPlus.core.tileentities.general.TileEntityVolumetricFlaskSetter;
import gtPlusPlus.core.tileentities.machines.TileEntityPestKiller;

public class ModTileEntities {

    public static void init() {
        Logger.INFO("Registering Tile Entities.");
        GameRegistry.registerTileEntity(TileEntityFishTrap.class, "TileFishTrap");
        GameRegistry.registerTileEntity(TileEntityInfiniteFluid.class, "TileInfiniteFluid");
        GameRegistry.registerTileEntity(TileEntityCircuitProgrammer.class, "TileCircuitProgrammer");
        GameRegistry.registerTileEntity(TileEntityDecayablesChest.class, "TileDecayablesChest");
        GameRegistry.registerTileEntity(TileEntitySuperLight.class, "TileEntitySuperLight");
        GameRegistry.registerTileEntity(TileEntityPestKiller.class, "TileEntityPestKiller");

        GameRegistry.registerTileEntity(TileEntityVolumetricFlaskSetter.class, "TileEntityVolumetricFlaskSetter");
    }

}
