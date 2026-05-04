package gregtech.common.gui.modularui.multiblock;

import static gtnhintergalactic.recipe.SpaceMiningRecipes.MINING_DRILLS;
import static gtnhintergalactic.recipe.SpaceMiningRecipes.MINING_DRONES;
import static gtnhintergalactic.recipe.SpaceMiningRecipes.MINING_RODS;
import static gtnhintergalactic.recipe.SpaceMiningRecipes.asteroidDistanceMap;
import static gtnhintergalactic.recipe.SpaceMiningRecipes.uniqueAsteroidList;
import static gtnhintergalactic.tile.multi.elevatormodules.TileEntityModuleMiner.CYCLE_DISTANCE_PARAMETER;
import static gtnhintergalactic.tile.multi.elevatormodules.TileEntityModuleMiner.CYCLE_PARAMETER;
import static gtnhintergalactic.tile.multi.elevatormodules.TileEntityModuleMiner.DISTANCE_PARAMETER;
import static gtnhintergalactic.tile.multi.elevatormodules.TileEntityModuleMiner.RANGE_PARAMETER;
import static gtnhintergalactic.tile.multi.elevatormodules.TileEntityModuleMiner.STEP_PARAMETER;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IGuiAction;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DrawableStack;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.drawable.Rectangle;
import com.cleanroommc.modularui.network.NetworkUtils;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import akka.japi.Pair;
import gregtech.api.enums.Materials;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.multiblock.base.TileEntityModuleBaseGui;
import gregtech.common.modularui2.widget.SlotLikeButtonWidget;
import gtnhintergalactic.recipe.AsteroidData;
import gtnhintergalactic.recipe.IGRecipeMaps;
import gtnhintergalactic.recipe.SpaceMiningData;
import gtnhintergalactic.recipe.SpaceMiningRecipes;
import gtnhintergalactic.tile.multi.elevatormodules.TileEntityModuleMiner;
import tectech.thing.metaTileEntity.multi.base.parameter.BooleanParameter;
import tectech.thing.metaTileEntity.multi.base.parameter.IntegerParameter;

public class TileEntityModuleMinerGui extends TileEntityModuleBaseGui<TileEntityModuleMiner> {

    private int startCache;
    private int endCache;
    private boolean cycleCache;
    private int droneCache;
    private final boolean[] filterCache = new boolean[64];
    private boolean[] checked = new boolean[64];
    private boolean isAsteroidPanelForFilter;
    private boolean isDroneSelectorForOptimizer;

    private SlotLikeButtonWidget droneSelectorButtonUtilityPanel;
    private ButtonWidget<?> droneSelectorButtonOptimizer;
    private SlotLikeButtonWidget droneSelectorButtonCalculator;

    public TileEntityModuleMinerGui(TileEntityModuleMiner multiblock) {
        super(multiblock);
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        IntSyncValue distanceSyncer = syncManager.findSyncHandler("distanceParameter", IntSyncValue.class);
        IntSyncValue cycleDistanceSyncer = syncManager.findSyncHandler("cycleDistanceParameter", IntSyncValue.class);
        BooleanSyncValue cycleSyncer = syncManager.findSyncHandler("cycleParameter", BooleanSyncValue.class);

        ListWidget<IWidget, ?> minerInfo = new ListWidget<>().child(
            IKey.dynamic(
                () -> EnumChatFormatting.WHITE + GTUtility.translate("tt.spaceminer.textFieldDistance")
                    + ": "
                    + EnumChatFormatting.GREEN
                    + (cycleSyncer.getValue() ? cycleDistanceSyncer.getValue() : distanceSyncer.getValue()))
                .asWidget()
                .marginBottom(2)
                .leftRel(0));
        return minerInfo.children(super.createTerminalTextWidget(syncManager, parent).getChildren());
    }

    @Override
    protected Flow createLeftPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        return Flow.row()
            .fullWidth()
            .height(this.getTextBoxToInventoryGap())
            .child(createFilterButton())
            .child(createCalculatorButton())
            .child(createSpaceMinerUtilityButton(parent, syncManager));
    }

    @Override
    protected Flow createRightPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        return Flow.row();
    }

    private IWidget createFilterButton() {
        IPanelHandler filterConfigurationPanel = panelMap.get("filterConfiguration");
        return new ButtonWidget<>().overlay(new DynamicDrawable(() -> {
            if (multiblock.isWhitelisted) return GTGuiTextures.TT_OVERLAY_BUTTON_WHITELIST;
            return GTGuiTextures.TT_OVERLAY_BUTTON_BLACKLIST;
        }))
            .tooltipBuilder(t -> t.addLine(IKey.lang("tt.spaceminer.filterButtonTooltip")))
            .onMousePressed(mouseData -> {
                if (!filterConfigurationPanel.isPanelOpen()) {
                    filterConfigurationPanel.openPanel();
                } else {
                    filterConfigurationPanel.closePanel();
                }
                return true;
            });
    }

    private IWidget createCalculatorButton() {
        IPanelHandler minerCalculator = panelMap.get("spaceMinerCalculator");
        return new ButtonWidget<>().overlay(
            GTGuiTextures.TT_OVERLAY_BUTTON_CALCULATOR.asIcon()
                .size(16))
            .tooltipBuilder(t -> t.addLine(IKey.lang("tt.spaceminer.calculatorButtonTooltip")))
            .onMousePressed(mouseData -> {
                if (!minerCalculator.isPanelOpen()) {
                    minerCalculator.openPanel();
                } else {
                    minerCalculator.closePanel();
                }
                return true;
            });
    }

    private IWidget createSpaceMinerUtilityButton(ModularPanel parent, PanelSyncManager syncManager) {
        IPanelHandler spaceMinerUtilityPanel = panelMap.get("spaceMinerUtility");
        IntSyncValue droneFilterSyncer = syncManager.findSyncHandler("droneFilter", IntSyncValue.class);

        return new ButtonWidget<>()
            .tooltipBuilder(t -> t.add(IKey.lang("tt.spaceminer.asteroidutilitypanelButtonTooltip")))
            .overlay(
                GTGuiTextures.TT_OVERLAY_BUTTON_UTILITY_PANEL.asIcon()
                    .size(16))

            .onMousePressed(mouseData -> {
                if (!spaceMinerUtilityPanel.isPanelOpen()) {
                    droneFilterSyncer.setValue(-1);
                    spaceMinerUtilityPanel.openPanel();
                } else {
                    spaceMinerUtilityPanel.closePanel();
                }
                return true;
            });
    }

    private ModularPanel openFilterPanel(PanelSyncManager syncManager, IPanelHandler syncHandler) {
        ModularPanel panel = new ModularPanel("filterConfig") {

            @Override
            public boolean isDraggable() {
                return false;
            }
        };
        return panel.coverChildren()
            .posRel(0.5f, 0.3f)
            .child(
                Flow.row()
                    .coverChildren()
                    .child(createFilterPanelSlotsAndButtons(syncManager))
                    .child(createFilterPanelConfiguration(syncManager, panel)));
    }

    private IWidget createFilterPanelSlotsAndButtons(PanelSyncManager syncManager) {
        return Flow.row()
            .coverChildren()
            .padding(4, 3)
            .childPadding(4)
            .child(createFilterSlotGroup(syncManager))
            .child(
                Flow.column()
                    .fullHeight()
                    .coverChildrenWidth()
                    .childPadding(4)
                    .child(createToggleWhitelistButton(syncManager))
                    .child(createClearFilterButton()));
    }

    private IWidget createFilterSlotGroup(PanelSyncManager syncManager) {
        StringBuilder builder;
        String[] matrix = new String[8];
        for (int i = 0; i < 8; i++) {
            builder = new StringBuilder();
            for (int j = 0; j < 8; j++) {
                builder.append('S');
            }
            matrix[i] = builder.toString();
        }

        return SlotGroupWidget.builder()
            .matrix(matrix)
            .key('S', index -> multiblock.filterSlots[index].background(createFilterSlotBackground(index, syncManager)))
            .build();
    }

    private IDrawable createFilterSlotBackground(int index, PanelSyncManager syncManager) {
        IntSyncValue distanceParameterSyncer = syncManager.findSyncHandler("distanceParameter", IntSyncValue.class);
        IntSyncValue droneTierSyncer = syncManager.findSyncHandler("droneTier", IntSyncValue.class);

        return new DynamicDrawable(() -> {
            if (multiblock.filterModularSlots[index].getStack() != null
                && filterContainsOre(multiblock.filterModularSlots[index].getStack())
                && currentOresContainThis(
                    multiblock.filterModularSlots[index].getStack(),
                    distanceParameterSyncer.getValue(),
                    droneTierSyncer.getValue(),
                    syncManager,
                    index)) {
                return new DrawableStack(
                    GuiTextures.SLOT_ITEM,
                    new Rectangle().color(multiblock.isWhitelisted ? Color.rgb(0, 255, 0) : Color.rgb(255, 0, 0))
                        .asIcon()
                        .size(16));
            } else {
                return GuiTextures.SLOT_ITEM;
            }
        });
    }

    private IWidget createToggleWhitelistButton(PanelSyncManager syncManager) {
        BooleanSyncValue isWhiteListedSyncer = syncManager.findSyncHandler("isWhiteListed", BooleanSyncValue.class);

        return new ButtonWidget<>().overlay(new DynamicDrawable(() -> {
            if (isWhiteListedSyncer.getValue()) {
                return GTGuiTextures.TT_OVERLAY_BUTTON_WHITELIST;
            } else {
                return GTGuiTextures.TT_OVERLAY_BUTTON_BLACKLIST;
            }
        }))
            .tooltipBuilder(t -> t.addLine(IKey.lang("tt.spaceminer.toggleFilter")))
            .onMousePressed(mouseData -> {
                isWhiteListedSyncer.setValue(!isWhiteListedSyncer.getBoolValue());
                return true;
            });
    }

    private IWidget createClearFilterButton() {
        return new ButtonWidget<>().overlay(
            GTGuiTextures.TT_OVERLAY_BUTTON_TRASH_CAN.asIcon()
                .size(16))
            .hoverOverlay(
                GTGuiTextures.TT_OVERLAY_BUTTON_TRASH_CAN_HOVER.asIcon()
                    .size(16))
            .tooltipBuilder(t -> t.addLine(IKey.lang("tt.spaceminer.clearFilter")))
            .onMousePressed(mouseData -> {
                for (int i = 0; i < 64; i++) {
                    multiblock.filterModularSlots[i].putStack(null);
                    multiblock.filterSlots[i].getSyncHandler()
                        .updateFromClient(null, 0);
                }
                return true;
            });
    }

    private boolean currentOresContainThis(ItemStack stack, int distance, int droneTier, PanelSyncManager syncManager,
        int index) {
        if (distance <= 0 || droneTier < 0) return false;
        Map<Integer, List<Pair<Integer, GTRecipe>>> asteroids = asteroidDistanceMap.get(droneTier);

        IntSyncValue rangeSyncer = syncManager.findSyncHandler("rangeParameter", IntSyncValue.class);
        BooleanSyncValue cycleSyncer = syncManager.findSyncHandler("cycleParameter", BooleanSyncValue.class);

        // results cached as this is a pretty expensive search
        int range = cycleSyncer.getValue() ? rangeSyncer.getValue() : 0;
        int start = Math.max(1, distance - range);
        int end = Math.min((int) TileEntityModuleMiner.MAX_DISTANCE, distance + range);

        if (checked[index] && droneCache == droneTier
            && cycleCache == cycleSyncer.getValue()
            && startCache == start
            && endCache == end) {
            return filterCache[index];
        }
        checked[index] = true;
        startCache = start;
        endCache = end;
        droneCache = droneTier;
        cycleCache = cycleSyncer.getValue();

        Set<Integer> visited = new HashSet();
        for (int i = start; i <= end; i++) {
            List<Pair<Integer, GTRecipe>> asteroidsAtDistance = asteroids.get(i)
                .stream()
                .filter(
                    asteroid -> asteroid.second()
                        .getMetadata(IGRecipeMaps.MODULE_TIER) <= multiblock.getModuleTier())
                .collect(toList());
            for (Pair<Integer, GTRecipe> asteroid : asteroidsAtDistance) {
                if (visited.contains(asteroid.first())) continue;
                if (Arrays.stream(asteroid.second().mOutputs)
                    .anyMatch(
                        output -> output.getDisplayName()
                            .equals(stack.getDisplayName()))) {
                    filterCache[index] = true;
                    return true;
                }
                visited.add(asteroid.first());
            }
        }

        filterCache[index] = false;
        return false;
    }

    private IWidget createFilterPanelConfiguration(PanelSyncManager syncManager, ModularPanel panel) {

        IntSyncValue distanceParameterSyncer = syncManager.findSyncHandler("distanceParameter", IntSyncValue.class);
        IntSyncValue droneTierSyncer = syncManager.findSyncHandler("droneTier", IntSyncValue.class);

        Flow resultColumn = Flow.column()
            .childPadding(3)
            .fullHeight()
            .width(185)
            .padding(4)
            .child(
                Flow.row()
                    .fullWidth()
                    .coverChildrenHeight()
                    .child(
                        IKey.lang("tt.spaceminer.filter.minableAsteroids")
                            .asWidget())
                    .child(createFilterPanelDroneDisplay(syncManager)));

        Flow asteroidButtonColumn = Flow.column()
            .fullWidth();
        generateMinableAsteroids(asteroidButtonColumn, syncManager);
        resultColumn.child(asteroidButtonColumn);

        droneTierSyncer.setChangeListener(() -> {
            generateMinableAsteroids(asteroidButtonColumn, syncManager);
            if (NetworkUtils.isClient()) {
                panel.scheduleResize();
            }
        });
        distanceParameterSyncer.setChangeListener(() -> {
            generateMinableAsteroids(asteroidButtonColumn, syncManager);
            if (NetworkUtils.isClient()) {
                panel.scheduleResize();
            }
        });

        return resultColumn;
    }

    private IWidget createFilterPanelDroneDisplay(PanelSyncManager syncManager) {
        IntSyncValue droneTierSyncer = syncManager.findSyncHandler("droneTier", IntSyncValue.class);

        return new DynamicDrawable(() -> {
            if (droneTierSyncer.getValue() < 0) return GTGuiTextures.TT_OVERLAY_BUTTON_FILTER_NO_DRONE;
            return new ItemDrawable(MINING_DRONES[droneTierSyncer.getValue()]);
        }).asWidget()
            .tooltipBuilder(t -> {
                if (droneTierSyncer.getValue() < 0) t.addLine(IKey.lang("tt.spaceminer.filter.noValidInputs"));
                else t.addFromItem(MINING_DRONES[droneTierSyncer.getValue()]);
            });
    }

    private void generateMinableAsteroids(Flow asteroidButtonColumn, PanelSyncManager syncManager) {
        asteroidButtonColumn.removeAll();
        generateAsteroidButtons(asteroidButtonColumn, syncManager);
    }

    private void generateAsteroidButtons(Flow resultColumn, PanelSyncManager syncManager) {
        IntSyncValue droneTierSyncer = syncManager.findSyncHandler("droneTier", IntSyncValue.class);
        IntSyncValue distanceSyncer = syncManager.findSyncHandler("distanceParameter", IntSyncValue.class);
        IntSyncValue rangeSyncer = syncManager.findSyncHandler("rangeParameter", IntSyncValue.class);
        BooleanSyncValue cycleSyncer = syncManager.findSyncHandler("cycleParameter", BooleanSyncValue.class);

        int droneTier = droneTierSyncer.getValue();
        int distance = distanceSyncer.getValue();

        if (droneTier < 0 || distance == 0) return;
        int range = cycleSyncer.getValue() ? rangeSyncer.getValue() : 0;

        Map<Integer, List<Pair<Integer, GTRecipe>>> asteroids = asteroidDistanceMap.get(droneTier);

        int start = Math.max(1, distance - range);
        int end = Math.min((int) TileEntityModuleMiner.MAX_DISTANCE, distance + range);

        Set<Integer> visited = new HashSet<>();
        for (int i = start; i <= end; i++) {
            List<Pair<Integer, GTRecipe>> asteroidsAtDistance = asteroids.get(i)
                .stream()
                .filter(
                    asteroid -> asteroid.second()
                        .getMetadata(IGRecipeMaps.MODULE_TIER) <= multiblock.getModuleTier())
                .collect(toList());

            for (Pair<Integer, GTRecipe> asteroid : asteroidsAtDistance) visited.add(asteroid.first());
        }

        Grid asteroidGrid = new Grid()
            .gridOfWidthElements(
                9,
                visited,
                ($x, $y, $index,
                    asteroidIndex) -> createAsteroidButton(uniqueAsteroidList.get(asteroidIndex), asteroidIndex))
            .coverChildren()
            .minElementMargin(1, 2);

        resultColumn.child(asteroidGrid);
    }

    private ButtonWidget<?> createAsteroidButton(AsteroidData data, int index) {
        ItemStack oreItem = data.outputItems != null ? data.outputItems[0]
            : GTOreDictUnificator.get(data.orePrefixes, data.output[0], 1);
        return new ButtonWidget<>().background(createAsteroidButtonBackground(data))
            .overlay(
                new ItemDrawable(oreItem).asIcon()
                    .size(16))
            .tooltipBuilder(createAsteroidButtonTooltip(data))
            .onMousePressed(createAsteroidButtonOnMousePressed(index, data));
    }

    private IDrawable createAsteroidButtonBackground(AsteroidData data) {
        return new DynamicDrawable(() -> {
            if (filterContainsAsteroidOre(data)) {
                return new DrawableStack(
                    GuiTextures.BUTTON_CLEAN,
                    new Rectangle().color(multiblock.isWhitelisted ? Color.rgb(0, 255, 0) : Color.rgb(255, 0, 0))
                        .asIcon()
                        .size(16));
            } else {
                return GuiTextures.BUTTON_CLEAN;
            }
        });
    }

    private Consumer<RichTooltip> createAsteroidButtonTooltip(AsteroidData data) {
        return t -> t.addLine(IKey.str(EnumChatFormatting.RED + data.getAsteroidNameLocalized()))
            .addLine(IKey.lang("tt.spaceminer.asteroidButtonTooltipInfo"))
            .addLine(
                IKey.lang("tt.spaceminer.asteroidButtonFilter")
                    .color(Color.rgb(180, 10, 10)));
    }

    private IGuiAction.MousePressed createAsteroidButtonOnMousePressed(int index, AsteroidData data) {
        IPanelHandler asteroidInfoPanel = panelMap.get("asteroidInfoPanel" + index);
        return mouseData -> {
            if (mouseData == 1) {
                addAsteroidToFiler(data);
                return true;
            }
            if (!asteroidInfoPanel.isPanelOpen()) {
                isAsteroidPanelForFilter = true;
                asteroidInfoPanel.openPanel();
            } else {
                asteroidInfoPanel.closePanel();
            }
            return true;
        };
    }

    private void addAsteroidToFiler(AsteroidData data) {
        int size = data.outputItems != null ? data.outputItems.length : data.output.length;
        List<Integer> visited = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            ItemStack ore = data.outputItems != null ? data.outputItems[i]
                : GTOreDictUnificator.get(data.orePrefixes, data.output[i], 1);
            if (!filterContainsOre(ore)) {
                int j = findFirstEmptySlot(visited);
                multiblock.filterModularSlots[j].putStack(ore);
                multiblock.filterSlots[j].getSyncHandler()
                    .updateFromClient(ore, 0);
                visited.add(j);
            }
        }
    }

    private ModularPanel getSpaceMinerUtilityPanel(ModularPanel parent, PanelSyncManager syncManager) {
        return new ModularPanel("asteroidList") {

            @Override
            public boolean isDraggable() {
                return false;
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
                    .child(createUtilityPanelAsteroidGrid(syncManager))
                    .child(createOreInputRow(syncManager))
                    .child(createDistanceInputRow(syncManager))
                    .child(createTierInputRow(syncManager))
                    .child(createUtilityPanelDroneSelector(syncManager)));
    }

    private Grid createUtilityPanelAsteroidGrid(PanelSyncManager syncManager) {
        return new Grid()
            .gridOfWidthElements(
                8,
                uniqueAsteroidList,
                ($x, $y, index, $element) -> createUtilityPanelAsteroidButton(index, syncManager))
            .coverChildren()
            .minElementMargin(1, 2);
    }

    private ButtonWidget<?> createUtilityPanelAsteroidButton(int i, PanelSyncManager syncManager) {
        IntSyncValue selectedAsteroidSyncer = syncManager.findSyncHandler("selectedAsteroid", IntSyncValue.class);
        AsteroidData data = uniqueAsteroidList.get(i);
        IPanelHandler asteroidInfoPanel = (IPanelHandler) syncManager.getSyncHandlerFromMapKey("asteroidInfoPanel" + i);

        return new ButtonWidget<>().overlay(createUtilityPanelAsteroidButtonOverlay(data, syncManager))
            .tooltipBuilder(
                t -> t.addLine(IKey.str(EnumChatFormatting.RED + data.getAsteroidNameLocalized()))
                    .addLine(IKey.lang("tt.spaceminer.asteroidButtonTooltipInfo")))
            .onMousePressed(mouseData -> {
                if (!asteroidInfoPanel.isPanelOpen()) {
                    selectedAsteroidSyncer.setValue(i);
                    isAsteroidPanelForFilter = false;
                    asteroidInfoPanel.openPanel();
                } else {
                    asteroidInfoPanel.closePanel();
                }
                return true;
            });
    }

    private IDrawable createUtilityPanelAsteroidButtonOverlay(AsteroidData data, PanelSyncManager syncManager) {
        StringSyncValue oreFilterSyncer = syncManager.findSyncHandler("oreFilter", StringSyncValue.class);
        IntSyncValue distanceSyncer = syncManager.findSyncHandler("distanceFilter", IntSyncValue.class);
        IntSyncValue droneFilterSyncer = syncManager.findSyncHandler("droneFilter", IntSyncValue.class);
        IntSyncValue moduleTierFilterSyncer = syncManager.findSyncHandler("moduleTierFilter", IntSyncValue.class);

        ItemStack oreItem = data.outputItems != null ? data.outputItems[0]
            : GTOreDictUnificator.get(data.orePrefixes, data.output[0], 1);

        return new DynamicDrawable(() -> {
            if (matchesFilters(
                data,
                oreFilterSyncer.getValue(),
                distanceSyncer.getValue(),
                droneFilterSyncer.getValue(),
                moduleTierFilterSyncer.getValue())) {
                return new DrawableStack(
                    new Rectangle().color(Color.rgb(0, 255, 0))
                        .asIcon()
                        .size(16),
                    new ItemDrawable(oreItem).asIcon()
                        .size(16));
            } else {
                return new ItemDrawable(oreItem).asIcon()
                    .size(16);
            }
        });
    }

    private Flow createOreInputRow(PanelSyncManager syncManager) {
        StringSyncValue oreFilterSyncer = syncManager.findSyncHandler("oreFilter", StringSyncValue.class);
        return Flow.row()
            .coverChildren()
            .child(
                IKey.lang("tt.spaceminer.textFieldOre")
                    .asWidget()
                    .width(50)
                    .color(Color.WHITE.main))
            .child(
                new TextFieldWidget().size(60, 9)
                    .value(oreFilterSyncer));
    }

    private Flow createDistanceInputRow(PanelSyncManager syncManager) {
        IntSyncValue distanceSyncer = syncManager.findSyncHandler("distanceFilter", IntSyncValue.class);
        return Flow.row()
            .coverChildren()
            .child(
                IKey.lang("tt.spaceminer.textFieldDistance")
                    .asWidget()
                    .width(50)
                    .color(Color.WHITE.main))
            .child(
                new TextFieldWidget().size(60, 9)
                    .value(distanceSyncer)
                    .setDefaultNumber(0)
                    .setNumbers(0, Integer.MAX_VALUE));
    }

    private Flow createTierInputRow(PanelSyncManager syncManager) {
        IntSyncValue moduleTierFilterSyncer = syncManager.findSyncHandler("moduleTierFilter", IntSyncValue.class);
        return Flow.row()
            .coverChildren()
            .child(
                IKey.lang("tt.spaceminer.textFieldTier")
                    .asWidget()
                    .width(50)
                    .color(Color.WHITE.main))
            .child(
                new TextFieldWidget().size(60, 9)
                    .value(moduleTierFilterSyncer)
                    .setDefaultNumber(0)
                    .setNumbers(0, 3));
    }

    private SlotLikeButtonWidget createUtilityPanelDroneSelector(PanelSyncManager syncManager) {
        IPanelHandler droneSelectorPanel = panelMap.get("droneSelectorUtilityPanel");
        IntSyncValue droneFilterSyncer = syncManager.findSyncHandler("droneFilter", IntSyncValue.class);

        droneSelectorButtonUtilityPanel = new SlotLikeButtonWidget(
            () -> droneFilterSyncer.getValue() >= 0 ? MINING_DRONES[droneFilterSyncer.getValue()] : null)
                .onMousePressed(mouseData -> {
                    if (!droneSelectorPanel.isPanelOpen()) {
                        isDroneSelectorForOptimizer = false;
                        droneSelectorPanel.openPanel();
                    } else {
                        droneSelectorPanel.closePanel();
                    }
                    return true;
                });
        return droneSelectorButtonUtilityPanel;
    }

    private boolean matchesFilters(AsteroidData data, String oreFilter, int distanceFilter, int droneFilter,
        int moduleTier) {
        boolean result = true;
        boolean checkedAny = false;
        if (!oreFilter.isEmpty()) {
            result = asteroidContainsOre(data, oreFilter);
            checkedAny = true;
        }
        if (distanceFilter > 0) {
            result = result && asteroidMatchesDistance(data, distanceFilter);
            checkedAny = true;
        }
        if (droneFilter >= 0) {
            result = result && asteroidMatchesDroneRange(data, droneFilter);
            checkedAny = true;
        }
        if (moduleTier > 0) {
            result = result && asteroidMatchesModuleTier(data, moduleTier);
            checkedAny = true;
        }
        return result && checkedAny;
    }

    private boolean asteroidMatchesModuleTier(AsteroidData data, int moduleTier) {
        return moduleTier >= data.requiredModuleTier;
    }

    private boolean asteroidMatchesDroneRange(AsteroidData data, int droneFilter) {
        return droneFilter >= data.minDroneTier && droneFilter <= data.maxDroneTier;
    }

    private boolean asteroidMatchesDistance(AsteroidData data, Integer value) {
        return value >= data.minDistance && value <= data.maxDistance;
    }

    private boolean asteroidContainsOre(AsteroidData data, String stringValue) {
        if (stringValue.isEmpty()) return false;
        if (data.output != null) {
            for (Materials output : data.output) {
                ItemStack itemOutput = GTOreDictUnificator.get(data.orePrefixes, output, 1);
                if (itemOutput.getDisplayName()
                    .toLowerCase()
                    .contains(stringValue.toLowerCase())) return true;
            }
        } else {
            for (ItemStack itemOutput : data.outputItems) {
                if (itemOutput.getDisplayName()
                    .toLowerCase()
                    .contains(stringValue.toLowerCase())) return true;
            }
        }

        return false;
    }

    private ModularPanel getAsteroidInformationPanel(int asteroidIndex, ModularPanel parent) {
        AsteroidData data = uniqueAsteroidList.get(asteroidIndex);

        return new ModularPanel("asteroidInformationPanel" + asteroidIndex) {

            @Override
            public boolean isDraggable() {
                return false;
            }
        }.relative(parent)
            .topRel(0)
            .leftRel(0)
            .padding(5)
            .coverChildren()
            .child(
                Flow.column()
                    .coverChildren()
                    .childPadding(4)
                    .crossAxisAlignment(Alignment.CrossAxis.START)
                    .child(createInformationPanelHeader(data))
                    .child(createMinedByRow(data))
                    .child(createDistanceText(data))
                    .child(createComputationText(data))
                    .child(createRequirementText(data))
                    .child(createDropsDisplay(data))
                    .childIf(!isAsteroidPanelForFilter, this::createSelectorButton));
    }

    private IWidget createInformationPanelHeader(AsteroidData data) {
        ItemStack oreItem = data.outputItems != null ? data.outputItems[0]
            : GTOreDictUnificator.get(data.orePrefixes, data.output[0], 1);
        return Flow.row()
            .coverChildrenWidth()
            .childPadding(5)
            .height(18)
            .child(new ItemDrawable(oreItem).asWidget())
            .child(
                IKey.str(EnumChatFormatting.DARK_RED + data.getAsteroidNameLocalized())
                    .asWidget());
    }

    private IWidget createMinedByRow(AsteroidData data) {
        Flow miningDroneRow = Flow.row()
            .coverChildren();

        List<IWidget> droneList = IntStream.rangeClosed(data.minDroneTier, data.maxDroneTier)
            .mapToObj(i -> {
                ItemStack droneItem = MINING_DRONES[i];
                ItemStack droneRodItem = MINING_RODS[i];
                ItemStack droneDrillItem = MINING_DRILLS[i];
                return createDroneDisplay(data, droneItem, droneRodItem, droneDrillItem, i);
            })
            .collect(Collectors.toList());

        Grid droneGrid = new Grid().gridOfWidthElements(10, droneList, ($x, $y, $i, element) -> element)
            .coverChildren()
            .minElementMargin(1, 2);

        miningDroneRow.child(
            IKey.lang("tt.spaceminer.asteroidutilitypanel.miningDrones")
                .asWidget())
            .child(droneGrid);
        return miningDroneRow;
    }

    private IWidget createDroneDisplay(AsteroidData data, ItemStack droneItem, ItemStack droneRodItem,
        ItemStack droneDrillItem, int i) {
        return new ItemDrawable(droneItem).asWidget()
            .tooltipBuilder(
                t -> t.addLine(IKey.str(droneItem.getDisplayName()))
                    .add(IKey.lang("tt.spaceminer.asteroidutilitypanel.itemUsage"))
                    .add(new ItemDrawable(droneRodItem))
                    .addLine(new ItemDrawable(droneDrillItem))
                    .addLine(
                        IKey.lang(
                            "tt.spaceminer.asteroidutilitypanel.sizeForDrone",
                            (int) (data.minSize + Math.pow(2, i - data.minDroneTier) - 1),
                            (int) (data.maxSize + Math.pow(2, i - data.minDroneTier) - 1)))
                    .addLine(
                        IKey.lang(
                            "tt.spaceminer.asteroidutilitypanel.miningTime",
                            String.format("%.2f", data.duration / (20 * Math.sqrt(i - data.minDroneTier + 1))))));
    }

    private IWidget createDistanceText(AsteroidData data) {
        return IKey
            .lang(
                "tt.spaceminer.asteroidutilitypanel.distanceRange",
                EnumChatFormatting.GREEN,
                data.minDistance,
                data.maxDistance,
                EnumChatFormatting.RESET)
            .asWidget();
    }

    private IWidget createComputationText(AsteroidData data) {
        return IKey.lang("tt.spaceminer.asteroidutilitypanel.computation", EnumChatFormatting.BLUE, data.computation)
            .asWidget()
            .marginBottom(4);
    }

    private IWidget createRequirementText(AsteroidData data) {
        return IKey.lang("tt.spaceminer.asteroidutilitypanel.moduleTier", data.requiredModuleTier)
            .asWidget();
    }

    private IWidget createDropsDisplay(AsteroidData data) {
        int dropCount = data.output != null ? data.output.length : data.outputItems.length;
        int totalWeight = Arrays.stream(data.chances)
            .sum();

        Flow dropRow = Flow.row()
            .coverChildren()
            .childPadding(4);

        dropRow.child(
            IKey.lang("tt.spaceminer.asteroidutilitypanel.drops")
                .asWidget());

        List<IWidget> dropList = IntStream.range(0, dropCount)
            .mapToObj(i -> createDropDisplayWidget(data, i, totalWeight))
            .collect(toList());

        dropRow.child(
            new Grid().gridOfWidthElements(9, dropList, ($x, $y, $i, element) -> element)
                .coverChildren()
                .minElementMargin(2));

        return dropRow;
    }

    private IWidget createDropDisplayWidget(AsteroidData data, int i, int totalWeight) {
        ItemStack ore = data.outputItems != null ? data.outputItems[i]
            : GTOreDictUnificator.get(data.orePrefixes, data.output[i], 1);

        return new SlotLikeButtonWidget(ore).tooltipBuilder(t -> createDropDisplayTooltip(t, i, ore, data, totalWeight))
            .onMousePressed(createDropDisplayOnMousePressed(ore))
            .background(createDropDisplayBackground(ore));
    }

    private void createDropDisplayTooltip(RichTooltip t, int i, ItemStack ore, AsteroidData data, int totalWeight) {
        t.addLine(IKey.str(ore.getDisplayName()))
            .addLine(IKey.str(String.format("%.2f%%", ((double) data.chances[i] / totalWeight) * 100)));
        if (isAsteroidPanelForFilter)
            t.addLine(IKey.str(EnumChatFormatting.DARK_GREEN + GTUtility.translate("tt.spaceminer.filter.addOre")));
    }

    private IGuiAction.MousePressed createDropDisplayOnMousePressed(ItemStack ore) {
        return mouseData -> {
            if (!isAsteroidPanelForFilter) return true;
            if (filterContainsOre(ore)) return true;
            int firstEmptySlot = findFirstEmptySlot();
            multiblock.filterModularSlots[firstEmptySlot].putStack(ore);
            multiblock.filterSlots[firstEmptySlot].getSyncHandler()
                .updateFromClient(ore, 0);
            return true;
        };
    }

    private IDrawable createDropDisplayBackground(ItemStack ore) {
        return new DynamicDrawable(() -> {
            if (!isAsteroidPanelForFilter) return GuiTextures.SLOT_ITEM;
            if (filterContainsOre(ore)) {
                return new DrawableStack(
                    GuiTextures.SLOT_ITEM,
                    new Rectangle().color(multiblock.isWhitelisted ? Color.rgb(0, 255, 0) : Color.rgb(255, 0, 0))
                        .asIcon()
                        .size(16));
            } else {
                return GuiTextures.SLOT_ITEM;
            }
        });
    }

    private IWidget createSelectorButton() {
        droneSelectorButtonOptimizer = new ButtonWidget<>();
        IPanelHandler droneSelectorPanel = panelMap.get("droneSelectorOptimizer");
        return droneSelectorButtonOptimizer.overlay(
            GTGuiTextures.TT_OVERLAY_BUTTON_TARGET_ASTEROID.asIcon()
                .size(16))
            .tooltipBuilder(t -> t.addLine(IKey.lang("tt.spaceminer.asteroidutilitypanel.targetAsteroidButtonTooltip")))
            .onMousePressed(mouseData -> {
                isDroneSelectorForOptimizer = true;
                droneSelectorPanel.openPanel();
                return true;
            });
    }

    private boolean filterContainsAsteroidOre(AsteroidData data) {
        int size = data.outputItems != null ? data.outputItems.length : data.output.length;
        for (int i = 0; i < size; i++) {
            ItemStack ore = data.outputItems != null ? data.outputItems[i]
                : GTOreDictUnificator.get(data.orePrefixes, data.output[i], 1);
            if (filterContainsOre(ore)) return true;
        }
        return false;

    }

    private boolean filterContainsOre(ItemStack ore) {
        for (int i = 0; i < 64; i++) {
            if (multiblock.filterInventory.getStackInSlot(i) != null && multiblock.filterInventory.getStackInSlot(i)
                .getDisplayName()
                .equals(ore.getDisplayName())) return true;
        }
        return false;
    }

    private int findFirstEmptySlot() {
        for (int i = 0; i < 64; i++) {
            if (multiblock.filterInventory.getStackInSlot(i) == null) {
                return i;
            }
        }
        return -1;
    }

    private int findFirstEmptySlot(List<Integer> visited) {
        for (int i = 0; i < 64; i++) {
            if (multiblock.filterInventory.getStackInSlot(i) == null && !visited.contains(i)) {
                return i;
            }
        }
        return -1;
    }

    private ModularPanel openDroneSelectorPanel(PanelSyncManager syncManager, IPanelHandler syncHandler,
        IWidget relative, String suffix) {

        return new ModularPanel("droneSelectorPanel" + suffix) {

            @Override
            public boolean isDraggable() {
                return false;
            }
        }.coverChildren()
            .relative(relative)
            .topRel(0, 0, 1)
            .padding(3)
            .child(
                new Grid().coverChildren()
                    .gridOfWidthElements(
                        5,
                        createDroneSelectorList(syncManager, syncHandler),
                        ($x, $y, $i, element) -> element));
    }

    private List<IWidget> createDroneSelectorList(PanelSyncManager syncManager, IPanelHandler syncHandler) {
        IntSyncValue distanceParameterSyncer = syncManager.findSyncHandler("distanceParameter", IntSyncValue.class);
        IntSyncValue droneFilterSyncer = syncManager.findSyncHandler("droneFilter", IntSyncValue.class);
        IntSyncValue selectedAsteroidSyncer = syncManager.findSyncHandler("selectedAsteroid", IntSyncValue.class);

        List<IWidget> drones = IntStream.range(0, MINING_DRONES.length)
            .mapToObj(
                i -> createDroneButton(
                    MINING_DRONES[i],
                    i,
                    distanceParameterSyncer,
                    droneFilterSyncer,
                    selectedAsteroidSyncer,
                    syncHandler))
            .collect(toList());

        drones.add(
            0,
            createNoDroneButton(distanceParameterSyncer, droneFilterSyncer, selectedAsteroidSyncer, syncHandler));

        return drones;
    }

    private IWidget createNoDroneButton(IntSyncValue distanceParameterSyncer, IntSyncValue droneFilterSyncer,
        IntSyncValue selectedAsteroidSyncer, IPanelHandler syncHandler) {
        return new SlotLikeButtonWidget(() -> null).onMousePressed(mouseData -> {
            droneFilterSyncer.setValue(-1);
            if (isDroneSelectorForOptimizer) {
                distanceParameterSyncer.setValue(0);
                panelMap.get("spaceMinerUtility")
                    .closePanel();
                panelMap.get("asteroidInfoPanel" + selectedAsteroidSyncer.getValue())
                    .closePanel();
                panelMap.get("spaceMinerCalculator")
                    .closePanel();
            }
            syncHandler.closePanel();
            return true;
        });
    }

    private IWidget createDroneButton(ItemStack drone, int i, IntSyncValue distanceParameterSyncer,
        IntSyncValue droneFilterSyncer, IntSyncValue selectedAsteroidSyncer, IPanelHandler syncHandler) {
        return new SlotLikeButtonWidget(drone).onMousePressed(mouseData -> {
            droneFilterSyncer.setValue(i);
            if (isDroneSelectorForOptimizer) {
                distanceParameterSyncer.setValue(optimizeDistance(selectedAsteroidSyncer.getValue(), i));
                panelMap.get("spaceMinerUtility")
                    .closePanel();
                panelMap.get("asteroidInfoPanel" + selectedAsteroidSyncer.getValue())
                    .closePanel();
                panelMap.get("spaceMinerCalculator")
                    .closePanel();
            }
            syncHandler.closePanel();
            return true;
        });
    }

    private Integer optimizeDistance(int asteroidIndex, int droneTier) {

        AsteroidData targetAsteroid = uniqueAsteroidList.get(asteroidIndex);
        Map<Integer, List<Pair<Integer, GTRecipe>>> asteroids = SpaceMiningRecipes.asteroidDistanceMap.get(droneTier);

        int bestDistance = 0;
        double bestScore = 0;

        for (Map.Entry<Integer, List<Pair<Integer, GTRecipe>>> asteroidList : asteroids.entrySet()) {
            int distance = asteroidList.getKey();
            double totalWeight = 0;
            double totalTime = 0;
            List<Pair<Integer, GTRecipe>> asteroidsAtDistance = asteroidList.getValue();

            int thisAsteroidIndex = -1;
            int size = asteroidList.getValue()
                .size();
            // gather total weight, try to find target asteroid
            for (int i = 0; i < size; i++) {
                int recipeAsteroidIndex = asteroidsAtDistance.get(i)
                    .first();
                GTRecipe recipe = asteroidsAtDistance.get(i)
                    .second();
                SpaceMiningData data = recipe.getMetadata(IGRecipeMaps.SPACE_MINING_DATA);
                if (recipe.getMetadata(IGRecipeMaps.MODULE_TIER) > multiblock.getModuleTier()) continue;
                totalWeight += data.recipeWeight;
                if (recipeAsteroidIndex == asteroidIndex) thisAsteroidIndex = recipeAsteroidIndex;
            }

            for (int i = 0; i < size; i++) {
                int recipeAsteroidIndex = asteroidsAtDistance.get(i)
                    .first();
                AsteroidData recipeAsteroid = uniqueAsteroidList.get(recipeAsteroidIndex);
                GTRecipe recipe = asteroidsAtDistance.get(i)
                    .second();
                SpaceMiningData data = recipe.getMetadata(IGRecipeMaps.SPACE_MINING_DATA);
                if (recipe.getMetadata(IGRecipeMaps.MODULE_TIER) > multiblock.getModuleTier()) continue;
                totalTime += recipe.mDuration * (data.recipeWeight / totalWeight);
            }
            if (thisAsteroidIndex < 0) continue;
            double score = ((targetAsteroid.duration / Math.sqrt(droneTier - targetAsteroid.minDroneTier + 1))
                * (targetAsteroid.recipeWeight / totalWeight)) / totalTime;

            if (score > bestScore) {
                bestScore = score;
                bestDistance = distance;
            }

        }
        return bestDistance;
    }

    private ModularPanel getSpaceMinerCalculator(PanelSyncManager syncManager, IPanelHandler panelSyncHandler,
        ModularPanel parent) {
        IntSyncValue selectedAsteroidSyncer = syncManager.findSyncHandler("selectedAsteroid", IntSyncValue.class);
        AtomicInteger distance = new AtomicInteger(0);
        IntSyncValue distanceSyncer = new IntSyncValue(distance::get, distance::set);

        AtomicInteger moduleTier = new AtomicInteger(0);
        IntSyncValue moduleTierSyncer = new IntSyncValue(moduleTier::get, moduleTier::set);

        IntSyncValue droneSyncer = syncManager.findSyncHandler("droneFilter", IntSyncValue.class);
        droneSelectorButtonCalculator = new SlotLikeButtonWidget(
            () -> droneSyncer.getValue() >= 0 ? MINING_DRONES[droneSyncer.getValue()] : null);
        IPanelHandler droneSelectorPanel = panelMap.get("droneSelectorCalculator");

        ListWidget<IWidget, ?> outputListWidget = new ListWidget<>()
            .background(new DrawableStack(new Rectangle().color(Color.rgb(91, 110, 225))))
            .fullWidth()
            .height(100)
            .padding(2)
            .child(
                Flow.column()
                    .childPadding(4)
                    .crossAxisAlignment(Alignment.CrossAxis.START)
                    .fullWidth()
                    .child(createTextPrompt("tt.spaceminer.calculator.missing.distance", w -> distance.get() <= 0))
                    .child(createTextPrompt("tt.spaceminer.calculator.missing.moduleTier", w -> moduleTier.get() <= 0))
                    .child(
                        createTextPrompt("tt.spaceminer.calculator.missing.drone", w -> droneSyncer.getValue() < 0)));

        return new ModularPanel("spaceMinerCalculator") {

            @Override
            public boolean isDraggable() {
                return false;
            }
        }.size(200, 160)
            .relative(parent)
            .topRel(0)
            .leftRel(0)
            .padding(4)
            .child(
                Flow.column()
                    .fullWidth()
                    .coverChildrenHeight()
                    .crossAxisAlignment(Alignment.CrossAxis.START)
                    .childPadding(4)
                    .child(outputListWidget)
                    .child(createCalculatorDistanceInput(distanceSyncer))
                    .child(createCalculatorTierInput(moduleTierSyncer))
                    .child(
                        createCalculatorDroneInput(
                            droneSelectorPanel,
                            distanceSyncer,
                            moduleTierSyncer,
                            droneSyncer,
                            selectedAsteroidSyncer,
                            outputListWidget)));
    }

    private IWidget createTextPrompt(String langKey, Predicate enableIf) {
        return IKey.lang(langKey)
            .asWidget()
            .setEnabledIf(enableIf);
    }

    private IWidget createCalculatorDistanceInput(IntSyncValue distanceSyncer) {
        return Flow.row()
            .coverChildren()
            .child(
                IKey.lang("tt.spaceminer.textFieldDistance")
                    .asWidget()
                    .width(50)
                    .color(Color.WHITE.main))
            .child(
                new TextFieldWidget().size(60, 9)
                    .value(distanceSyncer)
                    .setDefaultNumber(0)
                    .setNumbers(0, Integer.MAX_VALUE));
    }

    private IWidget createCalculatorTierInput(IntSyncValue moduleTierSyncer) {
        return Flow.row()
            .coverChildren()
            .child(
                IKey.lang("tt.spaceminer.textFieldTier")
                    .asWidget()
                    .width(50)
                    .color(Color.WHITE.main))
            .child(
                new TextFieldWidget().size(60, 9)
                    .value(moduleTierSyncer)
                    .setDefaultNumber(0)
                    .setNumbers(0, 3));
    }

    private IWidget createCalculatorDroneInput(IPanelHandler droneSelectorPanel, IntSyncValue distanceSyncer,
        IntSyncValue moduleTierSyncer, IntSyncValue droneSyncer, IntSyncValue selectedAsteroidSyncer,
        ListWidget<IWidget, ?> outputListWidget) {
        return Flow.row()
            .fullWidth()
            .coverChildrenHeight()
            .mainAxisAlignment(Alignment.MainAxis.SPACE_BETWEEN)
            .child(droneSelectorButtonCalculator.onMousePressed(mouseData -> {
                if (!droneSelectorPanel.isPanelOpen()) {
                    isDroneSelectorForOptimizer = false;
                    droneSelectorPanel.openPanel();
                } else {
                    droneSelectorPanel.closePanel();
                }
                return true;
            }))
            .child(
                new ButtonWidget<>().overlay(
                    GTGuiTextures.TT_OVERLAY_BUTTON_CALCULATE.asIcon()
                        .size(16))
                    .tooltipBuilder(t -> t.addLine(IKey.lang("tt.spaceminer.calculator.calculate")))
                    .onMousePressed(mouseData -> {
                        List<IWidget> output = calculateOutput(
                            distanceSyncer.getValue(),
                            moduleTierSyncer.getValue(),
                            droneSyncer.getValue(),
                            selectedAsteroidSyncer);
                        outputListWidget.removeAll();
                        outputListWidget.children(output);
                        outputListWidget.scheduleResize();
                        return true;
                    }));
    }

    private List<IWidget> calculateOutput(Integer distance, Integer moduleTier, Integer droneTier,
        IntSyncValue selectedAsteroidSyncer) {
        List<IWidget> listResult = new ArrayList<>();

        List<Pair<Integer, GTRecipe>> asteroids = getReachableAsteroids(distance, moduleTier, droneTier);
        AtomicInteger weightSum = getAsteroidWeightSum(asteroids);

        for (Pair<Integer, GTRecipe> asteroidPair : asteroids) {
            GTRecipe asteroid = asteroidPair.second();
            SpaceMiningData data = asteroid.getMetadata(IGRecipeMaps.SPACE_MINING_DATA);
            listResult.add(
                Flow.row()
                    .fullWidth()
                    .coverChildrenHeight()
                    .child(createCalculatedAsteroidButton(asteroid, selectedAsteroidSyncer, asteroidPair, data))
                    .child(
                        IKey.lang(
                            "tt.spaceminer.calculator.asteroidChance",
                            String.format("%.3f%%", ((double) data.recipeWeight / weightSum.get() * 100)))
                            .asWidget()
                            .marginLeft(4)));
        }
        return listResult;
    }

    private List<Pair<Integer, GTRecipe>> getReachableAsteroids(Integer distance, Integer moduleTier,
        Integer droneTier) {
        return droneTier < 0 ? new ArrayList<>()
            : SpaceMiningRecipes.asteroidDistanceMap.get(droneTier)
                .computeIfAbsent(distance, w -> new ArrayList<>())
                .stream()
                .filter(pair -> {
                    GTRecipe recipe = pair.second();
                    Integer requiredModuleTier = recipe.getMetadata(IGRecipeMaps.MODULE_TIER);
                    assert requiredModuleTier != null;
                    return moduleTier >= requiredModuleTier;
                })
                .sorted(
                    (a, b) -> Objects.requireNonNull(
                        b.second()
                            .getMetadata(IGRecipeMaps.SPACE_MINING_DATA)).recipeWeight
                        - Objects.requireNonNull(
                            a.second()
                                .getMetadata(IGRecipeMaps.SPACE_MINING_DATA)).recipeWeight)
                .collect(toList());
    }

    private AtomicInteger getAsteroidWeightSum(List<Pair<Integer, GTRecipe>> asteroids) {
        AtomicInteger result = new AtomicInteger();
        asteroids.forEach(
            asteroid -> result.addAndGet(
                Objects.requireNonNull(
                    asteroid.second()
                        .getMetadata(IGRecipeMaps.SPACE_MINING_DATA)).recipeWeight));

        return result;
    }

    private IWidget createCalculatedAsteroidButton(GTRecipe asteroid, IntSyncValue selectedAsteroidSyncer,
        Pair<Integer, GTRecipe> asteroidPair, SpaceMiningData data) {
        return new ButtonWidget<>().overlay(
            new DynamicDrawable(
                () -> new ItemDrawable(asteroid.mOutputs[0]).asIcon()
                    .size(16)))
            .tooltipBuilder(
                t -> t.addLine(IKey.str(EnumChatFormatting.DARK_RED + data.getAsteroidNameLocalized()))
                    .addLine(IKey.lang("tt.spaceminer.asteroidButtonTooltipInfo")))
            .onMousePressed(mouseData -> {
                selectedAsteroidSyncer.setValue(asteroidPair.first());
                isAsteroidPanelForFilter = false;
                panelMap.get("asteroidInfoPanel" + asteroidPair.first())
                    .openPanel();
                return true;
            });
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        IntSyncValue distanceParameterSyncer = new IntSyncValue(
            () -> (int) multiblock.parameterMap.get(DISTANCE_PARAMETER)
                .getValue(),
            val -> ((IntegerParameter) multiblock.parameterMap.get(DISTANCE_PARAMETER)).setValue(val));

        syncManager.syncValue("distanceParameter", distanceParameterSyncer);
        syncManager.syncValue(
            "cycleDistanceParameter",
            new IntSyncValue(
                () -> (int) multiblock.parameterMap.get(CYCLE_DISTANCE_PARAMETER)
                    .getValue(),
                val -> ((IntegerParameter) multiblock.parameterMap.get(CYCLE_DISTANCE_PARAMETER)).setValue(val)));
        syncManager.syncValue(
            "rangeParameter",
            new IntSyncValue(
                () -> (int) multiblock.parameterMap.get(RANGE_PARAMETER)
                    .getValue(),
                val -> ((IntegerParameter) multiblock.parameterMap.get(RANGE_PARAMETER)).setValue(val)));
        syncManager.syncValue(
            "stepParameter",
            new IntSyncValue(
                () -> (int) multiblock.parameterMap.get(STEP_PARAMETER)
                    .getValue(),
                val -> ((IntegerParameter) multiblock.parameterMap.get(STEP_PARAMETER)).setValue(val)));

        syncManager.syncValue(
            "cycleParameter",
            new BooleanSyncValue(
                () -> (boolean) multiblock.parameterMap.get(CYCLE_PARAMETER)
                    .getValue(),
                val -> {
                    ((BooleanParameter) multiblock.parameterMap.get(CYCLE_PARAMETER)).setValue(val);
                    checked = new boolean[64];
                }));

        BooleanSyncValue isWhiteListedSyncer = new BooleanSyncValue(
            () -> multiblock.isWhitelisted,
            val -> multiblock.isWhitelisted = val);
        syncManager.syncValue("isWhiteListed", isWhiteListedSyncer);

        syncManager.syncValue("distanceParameter", distanceParameterSyncer);
        IntSyncValue droneTierSyncer = new IntSyncValue(
            () -> multiblock.currentDroneMask <= 0 ? -1
                : (int) Math.round((Math.log(multiblock.currentDroneMask) / Math.log(2))));
        syncManager.syncValue("droneTier", droneTierSyncer);

        AtomicInteger droneFilter = new AtomicInteger(-1);
        IntSyncValue droneFilterSyncer = new IntSyncValue(droneFilter::get, droneFilter::set);
        syncManager.syncValue("droneFilter", droneFilterSyncer);

        AtomicInteger targetDroneTier = new AtomicInteger(-1);
        IntSyncValue targetDroneTierSyncer = new IntSyncValue(targetDroneTier::get, targetDroneTier::set);
        syncManager.syncValue("droneTarget", targetDroneTierSyncer);

        AtomicInteger selectedAsteroid = new AtomicInteger(0);
        IntSyncValue selectedAsteroidSyncer = new IntSyncValue(selectedAsteroid::get, selectedAsteroid::set);
        syncManager.syncValue("selectedAsteroid", selectedAsteroidSyncer);

        AtomicReference<String> oreFilter = new AtomicReference<>("");
        StringSyncValue oreFilterSyncer = new StringSyncValue(oreFilter::get, oreFilter::set);
        syncManager.syncValue("oreFilter", oreFilterSyncer);

        AtomicInteger distanceFilter = new AtomicInteger(0);
        IntSyncValue distanceFilterSyncer = new IntSyncValue(distanceFilter::get, distanceFilter::set);
        syncManager.syncValue("distanceFilter", distanceFilterSyncer);

        AtomicInteger moduleTierFilter = new AtomicInteger(0);
        IntSyncValue moduleTierFilterSyncer = new IntSyncValue(moduleTierFilter::get, moduleTierFilter::set);
        syncManager.syncValue("moduleTierFilter", moduleTierFilterSyncer);

    }

    @Override
    protected void initPanelMap(ModularPanel parent, PanelSyncManager syncManager) {
        panelMap.put(
            "spaceMinerUtility",
            syncManager.syncedPanel(
                "spaceMinerUtility",
                true,
                (p_syncManager, syncHandler) -> getSpaceMinerUtilityPanel(parent, syncManager)));
        panelMap.put(
            "filterConfiguration",
            syncManager.syncedPanel(
                "filterConfiguration",
                true,
                (p_syncManager, syncHandler) -> openFilterPanel(syncManager, syncHandler)));
        for (int i = 0; i < uniqueAsteroidList.size(); i++) {
            int finalI = i;
            panelMap.put(
                "asteroidInfoPanel" + finalI,
                syncManager.syncedPanel(
                    "asteroidInfoPanel" + finalI,
                    true,
                    (p_syncManager, syncHandler) -> getAsteroidInformationPanel(finalI, parent)));
        }
        panelMap.put(
            "spaceMinerCalculator",
            syncManager.syncedPanel(
                "spaceMinerCalculator",
                true,
                (p_syncManager, syncHandler) -> getSpaceMinerCalculator(syncManager, syncHandler, parent)));

        panelMap.put(
            "droneSelectorCalculator",
            syncManager.syncedPanel(
                "droneSelectorPanelCalculator",
                true,
                (p_syncManager, syncHandler) -> openDroneSelectorPanel(
                    syncManager,
                    syncHandler,
                    droneSelectorButtonCalculator,
                    "1")));
        panelMap.put(
            "droneSelectorUtilityPanel",
            syncManager.syncedPanel(
                "droneSelectorPanelUtilityPanel",
                true,
                (p_syncManager, syncHandler) -> openDroneSelectorPanel(
                    syncManager,
                    syncHandler,
                    droneSelectorButtonUtilityPanel,
                    "2")));
        panelMap.put(
            "droneSelectorOptimizer",
            syncManager.syncedPanel(
                "droneSelectorPanelOptimizer",
                true,
                (p_syncManager, syncHandler) -> openDroneSelectorPanel(
                    syncManager,
                    syncHandler,
                    droneSelectorButtonOptimizer,
                    "3")));
    }
}
