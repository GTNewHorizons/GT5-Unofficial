package gregtech.common.gui.modularui.multiblock;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.item.LimitingItemStackHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.slot.PhantomItemSlot;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.multiblock.base.TileEntityModuleBaseGui;
import gtnhintergalactic.recipe.SpacePumpingRecipes;
import gtnhintergalactic.tile.multi.elevatormodules.TileEntityModulePump;

public class TileEntityModulePumpGui extends TileEntityModuleBaseGui<TileEntityModulePump> {

    public static final int EXPECTED_PUMPING_SLOTS = 40;

    public TileEntityModulePumpGui(TileEntityModulePump multiblock) {
        super(multiblock);
    }

    private static List<Map.Entry<Pair<Integer, Integer>, FluidStack>> SORTED_PUMPING_RECIPES = null;

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        ListWidget<IWidget, ?> pumpInfo = new ListWidget<>();

        return pumpInfo.children(super.createTerminalTextWidget(syncManager, parent).getChildren());
    }

    @Override
    protected void initPanelMap(ModularPanel parent, PanelSyncManager syncManager) {
        panelMap.put(
            "spacePumpUtility",
            syncManager.syncedPanel(
                "spacePumpUtility",
                true,
                (p_syncManager, syncHandler) -> getSpacePumpUtilityPanel(parent, syncManager)));
    }

    @Override
    protected Flow createLeftPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        return Flow.row()
            .fullWidth()
            .height(this.getTextBoxToInventoryGap())
            .child(createSpacePumpUtilityButton(parent, syncManager));
    }

    private IWidget createSpacePumpUtilityButton(ModularPanel parent, PanelSyncManager syncManager) {
        IPanelHandler spacePumpUtilityPanel = panelMap.get("spacePumpUtility");

        return new ButtonWidget<>().tooltipBuilder(t -> t.add(IKey.lang("tt.spacepump.utilitypanelButtonTooltip")))
            .overlay(
                GTGuiTextures.TT_OVERLAY_BUTTON_TARGET_ASTEROID.asIcon()
                    .size(16))

            .onMousePressed(mouseData -> {
                if (!spacePumpUtilityPanel.isPanelOpen()) {
                    spacePumpUtilityPanel.openPanel();
                } else {
                    spacePumpUtilityPanel.closePanel();
                }
                return true;
            });
    }

    private ModularPanel getSpacePumpUtilityPanel(ModularPanel parent, PanelSyncManager syncManager) {
        return new ModularPanel("fluidList") {

            @Override
            public boolean isDraggable() {
                return true;
            }
        }.coverChildren()
            .relative(parent)
            .topRel(0)
            .leftRel(0)
            .padding(5)
            .child(
                Flow.column()
                    .coverChildren()
                    .crossAxisAlignment(Alignment.CrossAxis.START)
                    .childPadding(4)
                    .child(generateGridFromRecipes()));
    }

    private IWidget generateGridFromRecipes() {
        var slotBuilder = SlotGroupWidget.builder();
        //
        int planet = 2;
        int gas = 0;
        int rowFluids = 0;
        int pumpRecipes = Math.max(EXPECTED_PUMPING_SLOTS, SpacePumpingRecipes.RECIPES.size());
        PhantomItemSlot[] allFluids = new PhantomItemSlot[pumpRecipes];
        ModularSlot[] allSlots = new ModularSlot[pumpRecipes];
        int index = 0;
        var inventory = new LimitingItemStackHandler(pumpRecipes, 1);

        for (Map.Entry<Pair<Integer, Integer>, FluidStack> entry : getSortedPumpRecipes()) {
            var planetGasPair = entry.getKey();
            var fluid = entry.getValue();

            if (planetGasPair.getLeft() > planet) {
                slotBuilder.row("F".repeat(rowFluids));
                rowFluids = 0;
                gas = 0;
                planet = planetGasPair.getLeft();
            }
            int gasDelta = Math.abs(planetGasPair.getRight() - gas);
            while (gasDelta > 1) {
                var mslot = new ModularSlot(inventory, index);
                mslot.putStack(null);
                allSlots[index] = mslot;
                allFluids[index] = new PhantomItemSlot().slot(mslot);
                index++;
                gasDelta--;
                rowFluids++;
            }
            rowFluids++;
            gas = planetGasPair.getRight();
            var mslot = new ModularSlot(inventory, index);
            mslot.putStack(GTUtility.getFluidDisplayStack(fluid.getFluid()));
            allSlots[index] = mslot;
            allFluids[index] = new PhantomItemSlot().slot(mslot);
            index++;
        }
        if (rowFluids > 0) {
            slotBuilder.row("F".repeat(rowFluids));
        }

        slotBuilder.key('F', idx -> allFluids[idx]);
        return slotBuilder.build();
    }

    private static List<Map.Entry<Pair<Integer, Integer>, FluidStack>> getSortedPumpRecipes() {
        if (SORTED_PUMPING_RECIPES == null) {
            SORTED_PUMPING_RECIPES = SpacePumpingRecipes.RECIPES.entrySet()
                .stream()
                .sorted(
                    Comparator.comparingInt(
                        (Map.Entry<Pair<Integer, Integer>, FluidStack> entry) -> entry.getKey()
                            .getLeft())
                        .thenComparingInt(
                            entry -> entry.getKey()
                                .getRight()))
                .toList();
        }
        return SORTED_PUMPING_RECIPES;
    }
}
