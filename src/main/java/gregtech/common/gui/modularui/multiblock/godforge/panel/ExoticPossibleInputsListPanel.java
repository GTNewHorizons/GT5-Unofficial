package gregtech.common.gui.modularui.multiblock.godforge.panel;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.fluid.FluidTanksHandler;
import com.cleanroommc.modularui.utils.fluid.IFluidTanksHandler;
import com.cleanroommc.modularui.utils.item.IItemHandlerModifiable;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.cleanroommc.modularui.value.sync.SyncHandlers;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.slot.FluidSlot;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.google.common.collect.ImmutableList;

import gregtech.api.enums.Materials;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.multiblock.godforge.ForgeOfGodsGuiUtil;
import gregtech.common.gui.modularui.multiblock.godforge.sync.Modules;
import gregtech.common.gui.modularui.multiblock.godforge.sync.Panels;
import gregtech.common.gui.modularui.multiblock.godforge.sync.SyncHypervisor;
import tectech.loader.recipe.Godforge;

public class ExoticPossibleInputsListPanel {

    private static final int ROW_SLOTS_WIDTH = 16;

    private static final int SIZE_W = ROW_SLOTS_WIDTH * 18 + 12;

    public static ModularPanel openPanel(SyncHypervisor hypervisor) {
        ModularPanel panel = hypervisor.getModularPanel(Modules.EXOTIC, Panels.EXOTIC_POSSIBLE_INPUTS_LIST);

        int qgpItemSize = Godforge.exoticModulePlasmaItemMap.size();
        int qgpFluidSize = Godforge.exoticModulePlasmaFluidMap.size();
        int magmatterItemSize = Godforge.exoticModuleMagmatterItemMap.size();
        int magmatterFluidSize = 2;

        int qgpRows = (int) Math.ceil(1.0f * (qgpItemSize + qgpFluidSize) / ROW_SLOTS_WIDTH);
        int magmatterRows = (int) Math.ceil(1.0f * (magmatterItemSize + magmatterFluidSize) / ROW_SLOTS_WIDTH);

        int panelHeight = (qgpRows + magmatterRows) * 18 + 35; // plus a little extra for the headers and stuff

        panel.size(SIZE_W, panelHeight)
            .child(ForgeOfGodsGuiUtil.panelCloseButtonStandard());

        Flow column = new Column().coverChildren()
            .margin(6);

        column.child(createQGPGroup(qgpItemSize, qgpFluidSize, qgpRows).marginBottom(3));
        column.child(createMagmatterGroup(magmatterItemSize, magmatterFluidSize, magmatterRows));
        panel.child(column);

        return panel;
    }

    private static Flow createQGPGroup(int itemSize, int fluidSize, int rows) {
        IItemHandlerModifiable itemHandler = new ItemStackHandler(itemSize);
        IFluidTanksHandler fluidHandler = new FluidTanksHandler(fluidSize, 128000);

        List<ItemStack> possibleItems = new ArrayList<>(Godforge.exoticModulePlasmaItemMap.keySet());
        List<FluidStack> possibleFluids = new ArrayList<>(Godforge.exoticModulePlasmaFluidMap.keySet());

        // Fill handlers
        for (int i = 0; i < itemSize; i++) {
            itemHandler.setStackInSlot(i, GTUtility.copyAmount(1, possibleItems.get(i)));
        }
        for (int i = 0; i < fluidSize; i++) {
            fluidHandler.setFluidInTank(
                i,
                possibleFluids.get(i)
                    .getFluid(),
                1);
        }

        Flow column = new Column().size(18 * ROW_SLOTS_WIDTH, rows * 18 + 10)
            .alignX(0.5f);

        // Title
        column.child(
            IKey.lang("gt.blockmachines.multimachine.FOG.possibleinputsqgp")
                .style(EnumChatFormatting.BLACK)
                .alignment(Alignment.CENTER)
                .asWidget()
                .height(9)
                .widthRel(1)
                .marginBottom(1));

        // Input rows
        column.child(createRows(itemHandler, fluidHandler, rows));

        return column;
    }

    private static Flow createMagmatterGroup(int itemSize, int fluidSize, int rows) {
        IItemHandlerModifiable itemHandler = new ItemStackHandler(itemSize);
        IFluidTanksHandler fluidHandler = new FluidTanksHandler(fluidSize, 128000);

        List<ItemStack> possibleItems = new ArrayList<>(Godforge.exoticModuleMagmatterItemMap.keySet());
        List<FluidStack> possibleFluids = ImmutableList.of(Materials.Space.getMolten(1), Materials.Time.getMolten(1));

        // Fill handlers
        for (int i = 0; i < itemSize; i++) {
            itemHandler.setStackInSlot(i, GTUtility.copyAmount(1, possibleItems.get(i)));
        }
        for (int i = 0; i < fluidSize; i++) {
            fluidHandler.setFluidInTank(
                i,
                possibleFluids.get(i)
                    .getFluid(),
                1);
        }

        Flow column = new Column().size(18 * ROW_SLOTS_WIDTH, rows * 18 + 10)
            .alignX(0.5f);

        // Title
        column.child(
            IKey.lang("gt.blockmachines.multimachine.FOG.possibleinputsmagmatter")
                .style(EnumChatFormatting.BLACK)
                .alignment(Alignment.CENTER)
                .asWidget()
                .height(9)
                .widthRel(1)
                .marginBottom(1));

        // Input rows
        column.child(createRows(itemHandler, fluidHandler, rows));

        return column;
    }

    private static Flow createRows(IItemHandlerModifiable items, IFluidTanksHandler fluids, int rows) {
        Flow column = new Column().coverChildren();

        int itemIndex = 0;
        int fluidIndex = 0;
        for (int rowIndex = 0; rowIndex < rows; rowIndex++) {
            Flow row = new Row().size(18 * ROW_SLOTS_WIDTH, 18)
                .alignX(0.5f);

            for (int slotIndex = 0; slotIndex < ROW_SLOTS_WIDTH; slotIndex++) {
                if (itemIndex < items.getSlots()) {
                    row.child(new ItemSlot().slot(new ModularSlot(items, itemIndex).accessibility(false, false)));
                    itemIndex++;
                } else if (fluidIndex < fluids.getTanks()) {
                    row.child(
                        new FluidSlot().syncHandler(
                            SyncHandlers.fluidSlot(fluids.getFluidTank(fluidIndex))
                                .canDrainSlot(false)
                                .canFillSlot(false)
                                .controlsAmount(false)));
                    fluidIndex++;
                } else break;
            }
            column.child(row);
        }
        return column;
    }
}
