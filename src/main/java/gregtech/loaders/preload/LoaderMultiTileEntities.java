package gregtech.loaders.preload;

import static gregtech.GTMod.GT_FML_LOGGER;

import net.minecraft.block.material.Material;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.gtnewhorizon.structurelib.alignment.constructable.IMultiblockInfoContainer;
import com.gtnewhorizons.mutecore.SystemRegistrator;
import com.gtnewhorizons.mutecore.api.block.MultiTileEntityBlock;
import com.gtnewhorizons.mutecore.api.data.FluidOutputInventory;
import com.gtnewhorizons.mutecore.api.data.ItemInputInventory;
import com.gtnewhorizons.mutecore.api.data.ItemOutputInventory;
import com.gtnewhorizons.mutecore.api.item.MultiTileEntityItem;
import com.gtnewhorizons.mutecore.api.registry.ComponentsCreator;
import com.gtnewhorizons.mutecore.api.registry.MultiTileEntityRegistry;
import com.gtnewhorizons.mutecore.api.tile.MultiTileEntity;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.MachineType;
import gregtech.api.multitileentity.GTBaseMuTERender;
import gregtech.api.multitileentity.MachineGUI;
import gregtech.api.multitileentity.data.Structure;
import gregtech.api.multitileentity.data.TooltipComponent;
import gregtech.api.multitileentity.enums.GT_MultiTileMachine;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.tileentities.machines.multiblock.MultiTileEntityInfo;
import gregtech.common.tileentities.machines.multiblock.coke_oven.CokeOvenData;
import gregtech.common.tileentities.machines.multiblock.coke_oven.CokeOvenStructureHandler;

public class LoaderMultiTileEntities implements Runnable {

    // MuTE Registry Names
    public static final String COMPONENT_CASING_REGISTRY_NAME = "gt.multitileentity.component.casings";
    public static final String UPGRADE_CASING_REGISTRY_NAME = "gt.multitileentity.upgrade.casings";
    public static final String CASING_REGISTRY_NAME = "gt.multitileentity.casings";
    public static final String MACHINE_REGISTRY_NAME = "gt.multitileentity.controllers";

    // MuTE Registries
    public static MultiTileEntityRegistry MACHINE_REGISTRY;
    public static MultiTileEntityBlock MACHINE_BLOCK;

    @Override
    public void run() {
        GT_FML_LOGGER.info("GTMod: Registering MultiTileEntities");
        IMultiblockInfoContainer.registerTileClass(MultiTileEntity.class, new MultiTileEntityInfo());
        MACHINE_BLOCK = new MultiTileEntityBlock(Material.anvil);
        GameRegistry.registerBlock(MACHINE_BLOCK, MultiTileEntityItem.class, MACHINE_REGISTRY_NAME);
        MACHINE_REGISTRY = new MultiTileEntityRegistry(MACHINE_BLOCK);
        MultiTileEntityRegistry.registerRegistry(MACHINE_BLOCK, MACHINE_REGISTRY);
        registerMachines();
        registerCasings();
        registerComponentCasings();
    }

    private static void registerMachines() {
        MACHINE_REGISTRY.create(GT_MultiTileMachine.CokeOven.getId(), MultiTileEntity.class)
            .componentsCreator(
                new ComponentsCreator().component(() -> new ItemInputInventory(3, 64))
                    .component(() -> new ItemOutputInventory(3, 64))
                    .component(() -> new FluidOutputInventory(1, 24000))
                    .component(() -> new Structure(CokeOvenStructureHandler.class))
                    .component(
                        () -> new TooltipComponent(
                            new MultiblockTooltipBuilder().addMachineType("Coke Oven")
                                .addInfo("Used for charcoal")
                                .beginStructureBlock(3, 3, 3, true)
                                .addCasingInfoExactly("Coke Oven Bricks", 25, false)
                                .addPollutionAmount(10)
                                .toolTipFinisher("")))
                    .component(() -> new CokeOvenData())
                    .build())
            .gui(new MachineGUI())
            .tooltipClass(TooltipComponent.class)
            .unlocalizedName("coke.oven.gt5u")
            .register();
    }

    private static void registerCasings() {}

    private static void registerComponentCasings() {}

    /**
     * Registered on the client side
     */
    public static void registerRenders() {
        MACHINE_REGISTRY.registerRender(GT_MultiTileMachine.CokeOven.getId(), new GTBaseMuTERender("cokeOven"));
    }

    public static void registerSystems() {
        SystemRegistrator.registerSystems();
    }

}
