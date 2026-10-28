package gregtech.common.gui.modularui.multiblock;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatFluid;
import static gtnhintergalactic.tile.multi.elevatormodules.TileEntityModulePump.PUMP_BATCH_MAX;
import static gtnhintergalactic.tile.multi.elevatormodules.TileEntityModulePump.PUMP_BATCH_MIN;

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
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.drawable.Rectangle;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.SliderWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.ColorUtils;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.multiblock.base.TileEntityModuleBaseGui;
import gtnhintergalactic.recipe.SpacePumpingRecipes;
import gtnhintergalactic.tile.multi.elevatormodules.TileEntityModulePump;

public class TileEntityModulePumpGui extends TileEntityModuleBaseGui<TileEntityModulePump> {

    // properly needs to use GenericListSync
    private final List<IndexGasMapping> indexQueue = new CopyOnWriteArrayList<>();
    private List<ButtonWidget<?>> queueButtons = null;

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
            syncManager.syncedPanel("spacePumpUtility", true, (_, _) -> getSpacePumpUtilityPanel(parent, syncManager)));
    }

    protected List<IndexGasMapping> getIndexQueue() {
        return indexQueue;
    }

    @Override
    protected Flow createLeftPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        return Flow.row()
            .full()
            .child(createSpacePumpUtilityButton(parent, syncManager));
    }

    private IWidget createSpacePumpUtilityButton(ModularPanel parent, PanelSyncManager syncManager) {
        IPanelHandler spacePumpUtilityPanel = panelMap.get("spacePumpUtility");

        return new ButtonWidget<>().addTooltipLine(IKey.lang("tt.spacepump.utilitypanelButtonTooltip"))
            .overlay(
                GTGuiTextures.TT_OVERLAY_BUTTON_TARGET_ASTEROID.asIcon()
                    .size(16))
            .onMousePressed(_ -> {
                spacePumpUtilityPanel.togglePanel();
                return true;
            });
    }

    private ModularPanel getSpacePumpUtilityPanel(ModularPanel parent, PanelSyncManager syncManager) {
        return new ModularPanel("fluidList") {}.coverChildren()
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
                    .child(createBatchSlider(syncManager))
                    .child(
                        IKey.lang("tt.spacepump.pendingqueue")
                            .asWidget())
                    .child(queueRowAndApply(syncManager)));
    }

    // Selection grid for fluids
    private Grid generateGridFromRecipes(PanelSyncManager syncManager) {
        var grid = new Grid();
        int planet = 2;
        int gas = 0;
        int index = 0;

        var rowFluids = new ArrayList<IWidget>();
        rowFluids.add(
            IKey.lang("tt.spacepump.planettier", planet)
                .asWidget());
        for (Map.Entry<Pair<Integer, Integer>, FluidStack> entry : getSortedPumpRecipes()) {
            var planetGasPair = entry.getKey();
            var fluid = entry.getValue();

            if (planetGasPair.getLeft() > planet) {
                grid.row(rowFluids);
                rowFluids = new ArrayList<>();
                gas = 0;
                planet = planetGasPair.getLeft();
                rowFluids.add(
                    IKey.lang("tt.spacepump.planettier", planet)
                        .asWidget());
            }
            int gasDelta = Math.abs(planetGasPair.getRight() - gas);
            while (gasDelta > 1) {
                // stack null, as a spacer (mostly dev env)
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
                    .addLine(IKey.lang("tt.spacepump.rate", formatFluid(fluid.amount)))
                    .addLine(IKey.lang("tt.spacepump.ratemax", formatFluid(fluid.amount * multiblock.getParallels()))))
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
                    new Rectangle().color(ColorUtils.SPACE_ELEVATOR_SELECTOR_WHITELIST.getColor())
                        .hollow(2f)
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

    private Flow createBatchSlider(PanelSyncManager syncManager) {
        // potentially should use a second sync value temporarily, so it doesn't directly apply
        // and gets applied after the "apply" button was pressed
        IntSyncValue batchSyncer = syncManager.findSyncHandler("batch", IntSyncValue.class);
        SliderWidget slider = new SliderWidget().value(batchSyncer)
            .widthRel(0.75f)
            .bounds(PUMP_BATCH_MIN, PUMP_BATCH_MAX)
            .background(GuiTextures.MC_BUTTON)
            .addTooltipLine(IKey.lang("tt.spacepump.batchslider"));
        return Flow.row()
            .marginTop(16)
            .fullWidth()
            .coverChildrenHeight()
            .reverseLayout()
            .mainAxisAlignment(Alignment.MainAxis.SPACE_BETWEEN)
            .child(
                new TextFieldWidget().value(batchSyncer)
                    .numbersInt(PUMP_BATCH_MIN, PUMP_BATCH_MAX)
                    .setTextAlignment(Alignment.Center))
            .child(slider);
    }

    // selected fluids display
    private Flow queueRowAndApply(PanelSyncManager syncManager) {
        return Flow.row()
            .fullWidth()
            .coverChildrenHeight()
            .mainAxisAlignment(Alignment.MainAxis.SPACE_BETWEEN)
            .child(queue(syncManager))
            .child(apply(syncManager));
    }

    private Flow queue(PanelSyncManager syncManager) {
        List<IWidget> localQueueButtons = new ArrayList<>(multiblock.getParallelRecipes());
        queueButtons = new ArrayList<>(multiblock.getParallelRecipes());
        for (int i = 0; i < multiblock.getParallelRecipes(); i++) {
            ButtonWidget<?> button = createQueueButton(i);
            localQueueButtons.add(button);
            queueButtons.add(button);
        }
        return Flow.row()
            .coverChildren()
            .child(
                new Grid().coverChildren()
                    .minElementMargin(1, 1)
                    .row(localQueueButtons));
    }

    private ButtonWidget<?> createQueueButton(int i) {
        return new ButtonWidget<>().overlay(queueButtonOverlay(i))
            .onMousePressed(_ -> {
                queueButtons.forEach(b -> b.tooltip().markDirty());
                if (i >= getIndexQueue().size()) {
                    return false;
                }
                return null != getIndexQueue().remove(i);
            })
            .tooltipDynamic(t -> {
                t.clearText();
                t.addLine(IKey.lang("tt.spacepump.utilityremove"));
                var queue = getIndexQueue();
                if (i < queue.size()) {
                    var index = queue.get(i);
                    FluidStack fluidStack = SpacePumpingRecipes.RECIPES.get(Pair.of(index.planet, index.gas));
                    t.addLine(IKey.lang("tt.spacepump.utilityselected", fluidStack.getLocalizedName()));
                }
            });
    }

    private IDrawable queueButtonOverlay(int i) {
        return new DynamicDrawable(() -> {
            var queue = getIndexQueue();
            if (queue.size() > i) {
                var index = queue.get(i);
                FluidStack fluidStack = SpacePumpingRecipes.RECIPES.get(Pair.of(index.planet, index.gas));
                return new ItemDrawable(GTUtility.getFluidDisplayStack(fluidStack.getFluid())).asIcon()
                    .size(16);
            }
            return new ItemDrawable().asIcon()
                .size(16);
        });
    }

    private Flow apply(PanelSyncManager syncManager) {
        return Flow.row()
            .coverChildren()
            .mainAxisAlignment(Alignment.MainAxis.END)
            .child(
                new ButtonWidget<>().overlay(GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                    .onMousePressed(_ -> {
                        applyQueueToPumpParameters(syncManager);
                        return true;
                    })
                    .addTooltipLine(IKey.lang("tt.spacepump.utilityapply")));
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
        getIndexQueue().clear();
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
