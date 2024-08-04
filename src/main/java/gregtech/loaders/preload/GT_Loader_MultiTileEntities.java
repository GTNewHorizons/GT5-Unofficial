package gregtech.loaders.preload;

import static gregtech.GTMod.GT_FML_LOGGER;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureUtility;
import com.gtnewhorizons.mutecore.SystemRegistrator;
import com.gtnewhorizons.mutecore.api.block.MultiTileEntityBlock;
import com.gtnewhorizons.mutecore.api.data.FluidOutputInventory;
import com.gtnewhorizons.mutecore.api.data.ItemInputInventory;
import com.gtnewhorizons.mutecore.api.data.ItemOutputInventory;
import com.gtnewhorizons.mutecore.api.gui.MuTEGUI;
import com.gtnewhorizons.mutecore.api.registry.MultiTileEntityRegistry;
import com.gtnewhorizons.mutecore.api.tile.MultiTileEntity;
import dev.dominion.ecs.api.Entity;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.multitileentity.StructureHandler;
import gregtech.api.multitileentity.data.Structure;
import gregtech.api.multitileentity.data.TooltipComponent;
import gregtech.api.multitileentity.enums.GT_MultiTileCasing;
import gregtech.api.multitileentity.enums.GT_MultiTileMachine;
import gregtech.api.multitileentity.multiblock.casing.Glasses;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_StructureUtility;

public class GT_Loader_MultiTileEntities implements Runnable {

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
        MACHINE_BLOCK = new MultiTileEntityBlock(Material.anvil);
        GameRegistry.registerBlock(MACHINE_BLOCK, "multitile_machine_block");
        MACHINE_REGISTRY = new MultiTileEntityRegistry(MACHINE_BLOCK);
        MultiTileEntityRegistry.registerRegistry(MACHINE_BLOCK, MACHINE_REGISTRY);
        registerMachines();
        registerCasings();
        registerComponentCasings();
    }

    private static void registerMachines() {
        final GT_Multiblock_Tooltip_Builder cokeOvenTooltip = new GT_Multiblock_Tooltip_Builder();
        cokeOvenTooltip.addMachineType("Coke Oven")
            .addInfo("Used for charcoal")
            .beginStructureBlock(3, 3, 3, true)
            .addCasingInfoExactly("Coke Oven Bricks", 25, false)
            .addPollutionAmount(10)
            .toolTipFinisher(GT_Values.AuthorBlueWeabo);
        MACHINE_REGISTRY.create(GT_MultiTileMachine.CokeOven.getId(), MultiTileEntity.class)
            .addComponents(
                new ItemInputInventory(3, 64),
                new ItemOutputInventory(3, 64),
                new FluidOutputInventory(1, 24000),
                new Structure(CokeOvenStructureHandler.class),
                ExtendedFacing.NORTH_NORMAL_NONE,
                new TooltipComponent(cokeOvenTooltip))
            .gui((e, sm) -> {return new ModularPanel("cokeOven");})
            .tooltipClass(TooltipComponent.class)
            .register();

    }

    private static class CokeOvenStructureHandler extends StructureHandler {

        public CokeOvenStructureHandler(Entity entity) {
            super(entity);
        }

        @Override
        public void construct(ItemStack stackSize, boolean hintsOnly) {
            construct(stackSize, "main", 1, 1, 0, hintsOnly);
        }

        @Override
        public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
            return survivalConstruct(stackSize, "main", 1, 1, 0, elementBudget, env);
        }

        @Override
        public IStructureDefinition<Entity> getStructureDefinition() {
            return StructureDefinition.<Entity>builder().addShape("main", new String[][] { { "AAA", "A~A", "AAA" }, { "AAA", "A-A", "AAA" }, { "AAA", "AAA", "AAA" } }).addElement('A', StructureUtility.ofBlock(GregTech_API.sBlockCasings8, 1)).build();
        }
    }

    private static void registerCasings() {}

    private static void registerComponentCasings() {}

    public static void registerRenders() {
        MACHINE_REGISTRY.registerRender(GT_MultiTileMachine.CokeOven.getId(), (e, rb, x, y, z, w) -> {});
    }

    public static void registerSystems() {
        SystemRegistrator.registerSystemsParallel();
    }
}
