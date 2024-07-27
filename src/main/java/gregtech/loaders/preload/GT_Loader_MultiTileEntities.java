package gregtech.loaders.preload;

import static gregtech.GTMod.GT_FML_LOGGER;

import com.gtnewhorizons.mutecore.MuTECore;
import com.gtnewhorizons.mutecore.SystemRegistrator;
import com.gtnewhorizons.mutecore.api.block.MultiTileEntityBlock;
import com.gtnewhorizons.mutecore.api.data.FluidOutputInventory;
import com.gtnewhorizons.mutecore.api.data.ItemInputInventory;
import com.gtnewhorizons.mutecore.api.data.ItemOutputInventory;
import com.gtnewhorizons.mutecore.api.data.TickData;
import com.gtnewhorizons.mutecore.api.registry.MultiTileEntityRegistry;
import com.gtnewhorizons.mutecore.api.tile.MultiTileEntity;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.multitileentity.enums.GT_MultiTileMachine;
import net.minecraft.block.material.Material;

public class GT_Loader_MultiTileEntities implements Runnable {

    // MuTE Registry Names
    public static final String COMPONENT_CASING_REGISTRY_NAME = "gt.multitileentity.component.casings";
    public static final String UPGRADE_CASING_REGISTRY_NAME = "gt.multitileentity.upgrade.casings";
    public static final String CASING_REGISTRY_NAME = "gt.multitileentity.casings";
    public static final String MACHINE_REGISTRY_NAME = "gt.multitileentity.controllers";

    public static MultiTileEntityRegistry MACHINE_REGISTRY;
    public static MultiTileEntityBlock MACHINE_BLOCK;

    // MuTE Registries
    @Override
    public void run() {
        GT_FML_LOGGER.info("GTMod: Registering MultiTileEntities");
        MACHINE_BLOCK = new MultiTileEntityBlock(Material.anvil);
        GameRegistry.registerBlock(MACHINE_BLOCK, "multitile_machine_block");
        MACHINE_REGISTRY = new MultiTileEntityRegistry(MACHINE_BLOCK);
        MultiTileEntityRegistry.registerRegistry(MACHINE_BLOCK, MACHINE_REGISTRY);
        registerMachines();
        registerCasings();
        registerComponentCasings();
    }

    private static void registerMachines() {
        MACHINE_REGISTRY.create(GT_MultiTileMachine.CokeOven.getId(), MultiTileEntity.class)
            .addComponents(new TickData(),
                new ItemInputInventory(3, 64),
                new ItemOutputInventory(3,64),
                new FluidOutputInventory(1, 24000));

    }

    private static void registerCasings() {
    }

    private static void registerComponentCasings() {
    }

    public static void registerRenders() {
    }

    public static void registerSystems() {
        SystemRegistrator.registerSystemsParallel();
    }
}
