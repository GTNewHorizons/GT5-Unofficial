package gregtech.loaders.preload;

import static gregtech.GTMod.GT_FML_LOGGER;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.gtnewhorizon.structurelib.alignment.constructable.IMultiblockInfoContainer;
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
import com.gtnewhorizons.mutecore.api.item.MultiTileEntityItem;
import com.gtnewhorizons.mutecore.api.registry.MultiTileEntityRegistry;
import com.gtnewhorizons.mutecore.api.tile.MultiTileEntity;

import blockrenderer6343.integration.structurelib.StructureCompatNEIHandler;
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
import gregtech.api.util.MultiblockTooltipBuilder;

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
            .addComponents(
                new ItemInputInventory(3, 64),
                new ItemOutputInventory(3, 64),
                new FluidOutputInventory(1, 24000),
                new Structure(CokeOvenStructureHandler.class),
                ExtendedFacing.NORTH_NORMAL_NONE,
                new TooltipComponent(new MultiblockTooltipBuilder().addMachineType("Coke Oven")
                    .addInfo("Used for charcoal")
                    .beginStructureBlock(3, 3, 3, true)
                    .addCasingInfoExactly("Coke Oven Bricks", 25, false)
                    .addPollutionAmount(10)
                    .toolTipFinisher(GT_Values.AuthorBlueWeabo)))
            .gui((e, sm) -> {return new ModularPanel("cokeOven");})
            .tooltipClass(TooltipComponent.class)
            .unlocalizedName("coke.oven.gt5u")
            .register();
        MACHINE_REGISTRY.create(1, MultiTileEntity.class)
            .addComponents(
                new ItemInputInventory(3, 64),
                new ItemOutputInventory(3, 64),
                new Structure(CokeOvenStructureHandler.class),
                ExtendedFacing.NORTH_NORMAL_NONE,
                new TooltipComponent(new GT_Multiblock_Tooltip_Builder().addMachineType("Coke Oven")
                    .addInfo("Used for charcoal")
                    .beginStructureBlock(3, 3, 3, true)
                    .addCasingInfoExactly("Coke Oven Bricks", 25, false)
                    .addPollutionAmount(10)
                    .toolTipFinisher(GT_Values.AuthorBlueWeabo)))
            .gui((e, sm) -> {return new ModularPanel("cokeOven");})
            .tooltipClass(TooltipComponent.class)
            .unlocalizedName("macerator.gt5u")
            .register();
    }

    private static class MultiTileEntityInfo implements IMultiblockInfoContainer<MultiTileEntity> {

        @Override
        public void construct(ItemStack stackSize, boolean hintsOnly, MultiTileEntity tileEntity,
                ExtendedFacing aSide) {
            Entity entity = tileEntity.getEntity();
            if (!entity.has(Structure.class)) return;
            Structure struct = entity.get(Structure.class);
            StructureHandler structH;
            try {
                structH = struct.getHandlerClass().getConstructor(Entity.class).newInstance(entity);
            } catch  (Exception ing){
                return;
            }
            structH.construct(stackSize, hintsOnly);
        }

        @Override
        public int survivalConstruct(ItemStack stackSize, int elementBudge, ISurvivalBuildEnvironment env,
                MultiTileEntity tileEntity, ExtendedFacing aSide) {
            Entity entity = tileEntity.getEntity();
            if (!entity.has(Structure.class)) return -1;
            Structure struct = entity.get(Structure.class);
            StructureHandler structH;
            try {
                structH = struct.getHandlerClass().getConstructor(Entity.class).newInstance(entity);
            } catch  (Exception ing){
                return -1;
            }
            return structH.survivalConstruct(stackSize, elementBudge, env);
        }

        @Override
        public String[] getDescription(ItemStack stackSize) {
            Item item = stackSize.getItem();
            if (!(item instanceof MultiTileEntityItem muteItem)) return new String[0];

            MultiTileEntityBlock muBlock = (MultiTileEntityBlock) Block.getBlockFromItem(muteItem);
            Entity entity = muBlock.getRegistry().getMultiTileContainer(stackSize.getItemDamage()).getOriginalEntity();
            if (!entity.has(Structure.class)) return new String[0];
            Structure struct = entity.get(Structure.class);
            StructureHandler structH;
            try {
                structH = struct.getHandlerClass().getConstructor(Entity.class).newInstance(entity);
            } catch  (Exception ing){
                return new String[0];
            }
            return structH.getStructureDescription(stackSize);
        }


    }

    private static class CokeOvenStructureHandler extends StructureHandler {
        public static final IStructureDefinition<Entity> STRUCTURE = StructureDefinition.<Entity>builder().addShape("main", new String[][] { { "AAA", "A~A", "AAA" }, { "AAA", "A-A", "AAA" }, { "AAA", "AAA", "AAA" } }).addElement('A', StructureUtility.ofBlock(GregTech_API.sBlockCasings8, 1)).build();

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
            return STRUCTURE;
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
