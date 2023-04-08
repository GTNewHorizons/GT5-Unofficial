package gtPlusPlus.core.tileentities;

import static gregtech.api.enums.Mods.Thaumcraft;

import cpw.mods.fml.common.registry.GameRegistry;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.general.BlockSuperLight.TileEntitySuperLight;
import gtPlusPlus.core.block.machine.Machine_SuperJukebox.TileEntitySuperJukebox;
import gtPlusPlus.core.tileentities.general.TileEntityCircuitProgrammer;
import gtPlusPlus.core.tileentities.general.TileEntityDecayablesChest;
import gtPlusPlus.core.tileentities.general.TileEntityEggBox;
import gtPlusPlus.core.tileentities.general.TileEntityFirepit;
import gtPlusPlus.core.tileentities.general.TileEntityFishTrap;
import gtPlusPlus.core.tileentities.general.TileEntityInfiniteFluid;
import gtPlusPlus.core.tileentities.general.TileEntityPlayerDoorBase;
import gtPlusPlus.core.tileentities.general.TileEntityVolumetricFlaskSetter;
import gtPlusPlus.core.tileentities.general.TileEntityXpConverter;
import gtPlusPlus.core.tileentities.machines.TileEntityAdvPooCollector;
import gtPlusPlus.core.tileentities.machines.TileEntityModularityTable;
import gtPlusPlus.core.tileentities.machines.TileEntityPestKiller;
import gtPlusPlus.core.tileentities.machines.TileEntityPooCollector;
import gtPlusPlus.core.tileentities.machines.TileEntityProjectTable;
import gtPlusPlus.core.tileentities.machines.TileEntityRoundRobinator;
import gtPlusPlus.core.tileentities.machines.TileEntityTradeTable;
import gtPlusPlus.plugin.villagers.tile.TileEntityGenericSpawner;
import gtPlusPlus.xmod.gregtech.common.Meta_GT_Proxy;

public class ModTileEntities {

    public static void init() {
        Logger.INFO("Registering Tile Entities.");
        GameRegistry.registerTileEntity(TileEntityPooCollector.class, "TileEntityPooCollector");
        GameRegistry.registerTileEntity(TileEntityAdvPooCollector.class, "TileEntityAdvPooCollector");
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
        GameRegistry.registerTileEntity(TileEntityPestKiller.class, "TileEntityPestKiller");
        GameRegistry.registerTileEntity(TileEntityRoundRobinator.class, "TileEntityRoundRobinator");
        GameRegistry.registerTileEntity(TileEntityEggBox.class, "TileEggBox");

        GameRegistry.registerTileEntity(TileEntityVolumetricFlaskSetter.class, "TileEntityVolumetricFlaskSetter");

        // Mod TEs
        if (Thaumcraft.isModLoaded()) {}

        blacklistTilesFromAcceleration();
    }

    private static void blacklistTilesFromAcceleration() {
        Meta_GT_Proxy.setTileEntityClassAsBlacklistedInWorldAccelerator(
                "gtPlusPlus.core.tileentities.general.TileEntityFishTrap");
        Meta_GT_Proxy.setTileEntityClassAsBlacklistedInWorldAccelerator(
                "gtPlusPlus.core.tileentities.general.TileEntityDecayablesChest");
        Meta_GT_Proxy
                .setTileEntityClassAsBlacklistedInWorldAccelerator("gtPlusPlus.core.tileentities.general.TileEggBox");
    }
}
