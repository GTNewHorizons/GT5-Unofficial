package gregtech.common.gui.modularui.multiblock;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DrawableStack;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.drawable.Rectangle;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.multiblock.base.TileEntityModuleBaseGui;
import gtnhintergalactic.recipe.SpacePumpingRecipes;
import gtnhintergalactic.tile.multi.elevatormodules.TileEntityModulePump;

public class TileEntityModulePumpGui extends TileEntityModuleBaseGui<TileEntityModulePump> {

    public static final int EXPECTED_PUMPING_SLOTS = 40;
    // properly needs to use GenericListSync
    private final List<IndexGasMapping> indexQueue = new CopyOnWriteArrayList<>();

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

    public List<IndexGasMapping> getIndexQueue() {
        return indexQueue;
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
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
                    .child(generateGridFromRecipes(syncManager))
                    .child(
                        new Widget<>().fullWidth()
                            .height(2)
                            .marginTop(2)
                            .marginBottom(4))
                    .child(queueRowAndApply(syncManager)));
    }

    // Selection grid for fluids
    private IWidget generateGridFromRecipes(PanelSyncManager syncManager) {
        var grid = new Grid();
        int planet = 2;
        int gas = 0;
        int index = 0;
        // int pumpRecipes = Math.max(EXPECTED_PUMPING_SLOTS, SpacePumpingRecipes.RECIPES.size());

        var rowFluids = new ArrayList<IWidget>();
        rowFluids.add(new TextWidget<>(IKey.lang("tt.spacepump.planettier", planet)));
        for (Map.Entry<Pair<Integer, Integer>, FluidStack> entry : getSortedPumpRecipes()) {
            var planetGasPair = entry.getKey();
            var fluid = entry.getValue();

            if (planetGasPair.getLeft() > planet) {
                grid.row(rowFluids);
                rowFluids = new ArrayList<>();
                gas = 0;
                planet = planetGasPair.getLeft();
                rowFluids.add(new TextWidget<>(IKey.lang("tt.spacepump.planettier", planet)));
            }
            int gasDelta = Math.abs(planetGasPair.getRight() - gas);
            while (gasDelta > 1) {
                // stack null
                gasDelta--;
                rowFluids.add(createFluidUtilityButton(syncManager, index, null, 0, 0));
                index++;
            }
            gas = planetGasPair.getRight();
            rowFluids.add(createFluidUtilityButton(syncManager, index, fluid, planet, gas));
            index++;
        }
        grid.row(rowFluids);

        return grid.coverChildren()
            .minElementMargin(1, 2);
    }

    private ButtonWidget<?> createFluidUtilityButton(PanelSyncManager syncManager, int i, FluidStack fluid, int planet,
        int gas) {

        if (fluid == null) {
            return new ButtonWidget<>();
        }

        var mapping = new IndexGasMapping(i, planet, gas);

        return new ButtonWidget<>()
            .overlay(createButtonOverlay(syncManager, mapping, GTUtility.getFluidDisplayStack(fluid.getFluid())))
            .tooltipBuilder(
                t -> t.addLine(IKey.str(EnumChatFormatting.RED + fluid.getLocalizedName()))
                    .addLine(IKey.lang("tt.spacepump.rate", fluid.amount))
                    .addLine(IKey.lang("tt.spacepump.ratemax", fluid.amount * multiblock.getMaxParallelRecipes())))
            .onMousePressed(_ -> {
                if (getIndexQueue().size() >= multiblock.getParallelRecipes()) {
                    getIndexQueue().removeFirst();
                }
                getIndexQueue().add(mapping);
                return true;
            });
    }

    private IDrawable createButtonOverlay(PanelSyncManager syncManager, IndexGasMapping i, ItemStack fluid) {
        return new DynamicDrawable(() -> {
            if (getIndexQueue().contains(i)) {
                return new DrawableStack(
                    new Rectangle().color(Color.rgb(0, 255, 0))
                        .asIcon()
                        .size(16),
                    new ItemDrawable(fluid).asIcon()
                        .size(14));
            } else {
                return new ItemDrawable(fluid).asIcon()
                    .size(14);
            }
        });
    }

    // selected fluids display
    private IWidget queueRowAndApply(PanelSyncManager syncManager) {
        return Flow.row()
            .fullWidth()
            .coverChildrenHeight()
            .mainAxisAlignment(Alignment.MainAxis.SPACE_BETWEEN)
            .child(queue(syncManager))
            .child(apply(syncManager));
    }

    private IWidget queue(PanelSyncManager syncManager) {
        List<IWidget> queueButtons = new ArrayList<>();
        queueButtons.add(new TextWidget<>(IKey.lang("tt.spacepump.pendingqueue")));
        for (int i = 0; i < multiblock.getParallelRecipes(); i++) {
            queueButtons.add(createQueueButton(i));
        }
        return Flow.row()
            .coverChildren()
            .child(
                new Grid().coverChildren()
                    .minElementMargin(1, 1)
                    .row(queueButtons));
    }

    private ButtonWidget<?> createQueueButton(int i) {
        return new ButtonWidget<>().overlay(queueButtonOverlay(i))
            .onMousePressed(_ -> {
                if (i >= getIndexQueue().size()) {
                    return false;
                }
                return null != getIndexQueue().remove(i);
            });
    }

    private IDrawable queueButtonOverlay(int i) {
        return new DynamicDrawable(() -> {
            if (getIndexQueue().size() > i) {
                var index = getIndexQueue().get(i);
                FluidStack fluidStack = SpacePumpingRecipes.RECIPES.get(Pair.of(index.planet, index.gas));
                return new ItemDrawable(GTUtility.getFluidDisplayStack(fluidStack.getFluid())).asIcon()
                    .size(16);
            }
            return new ItemDrawable().asIcon()
                .size(16);
        });
    }

    private IWidget apply(PanelSyncManager syncManager) {
        return Flow.row()
            .coverChildren()
            .mainAxisAlignment(Alignment.MainAxis.END)
            .crossAxisAlignment(Alignment.CrossAxis.CENTER)
            .child(new TextWidget<>(IKey.lang("tt.spacepump.utilityapply")))
            .child(
                new ButtonWidget<>().overlay(GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                    .onMousePressed(_ -> {
                        applyQueueToPumpParameters(syncManager);
                        return true;
                    }));
    }

    private void applyQueueToPumpParameters(PanelSyncManager syncManager) {
        IPanelHandler spacePumpUtilityPanel = panelMap.get("spacePumpUtility");
        for (int i = 0; i < multiblock.getParallelRecipes(); i++) {
            var planetTierSync = syncManager.findSyncHandler("recipe" + i + ".planetType", IntSyncValue.class);
            var gasSync = syncManager.findSyncHandler("recipe" + i + ".gasType", IntSyncValue.class);
            var parallelSync = syncManager.findSyncHandler("recipe" + i + ".parallel", IntSyncValue.class);

            parallelSync.setValue(64);
            if (i >= getIndexQueue().size()) {
                planetTierSync.setValue(0);
                gasSync.setValue(0);
            } else {
                var index = getIndexQueue().get(i);
                planetTierSync.setValue(index.planet);
                gasSync.setValue(index.gas);
            }
        }
        spacePumpUtilityPanel.closePanel();
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

    private record IndexGasMapping(int index, int planet, int gas) {}
}
