package gtnhintergalactic.tile.multi.gui;

import static gregtech.api.enums.Mods.GregTech;
import static gtnhintergalactic.recipe.SpaceMiningRecipes.MINING_DRILLS;
import static gtnhintergalactic.recipe.SpaceMiningRecipes.MINING_DRONES;
import static gtnhintergalactic.recipe.SpaceMiningRecipes.MINING_RODS;
import static gtnhintergalactic.recipe.SpaceMiningRecipes.asteroidDistanceMap;
import static gtnhintergalactic.recipe.SpaceMiningRecipes.uniqueAsteroidList;
import static java.util.stream.Collectors.toList;
import static tectech.Reference.MODID;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DrawableStack;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.drawable.Rectangle;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.network.NetworkUtils;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widget.SingleChildWidget;
import com.cleanroommc.modularui.widget.WidgetTree;
import com.cleanroommc.modularui.widget.sizer.Area;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import akka.japi.Pair;
import gregtech.api.enums.Materials;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.common.modularui2.widget.SlotLikeButtonWidget;
import gtnhintergalactic.GTNHIntergalactic;
import gtnhintergalactic.recipe.AsteroidData;
import gtnhintergalactic.recipe.IGRecipeMaps;
import gtnhintergalactic.recipe.SpaceMiningData;
import gtnhintergalactic.recipe.SpaceMiningRecipes;
import gtnhintergalactic.tile.multi.elevatormodules.TileEntityModuleMiner;

public class TileEntityModuleMinerGui extends TileEntityModuleBaseGui {

    private final TileEntityModuleMiner spaceMiner;
    private final List<IPanelHandler> asteroidPanels = new ArrayList<>(uniqueAsteroidList.size());
    private int startCache;
    private int endCache;
    private boolean cycleCache;
    private int droneCache;
    private boolean filterCache[] = new boolean[64];
    private boolean checked[] = new boolean[64];

    public TileEntityModuleMinerGui(MTEMultiBlockBase base) {
        super(base);
        this.spaceMiner = (TileEntityModuleMiner) base;
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager) {
        IntSyncValue distanceSyncer = (IntSyncValue) syncManager.getSyncHandler("distanceParameter:0");
        IntSyncValue cycleDistanceSyncer = (IntSyncValue) syncManager.getSyncHandler("cycleDistanceParameter:0");
        BooleanSyncValue cycleSyncer = (BooleanSyncValue) syncManager.getSyncHandler("cycleParameter:0");
        ListWidget<IWidget, ?> minerInfo = new ListWidget<>().child(
            IKey.dynamic(
                () -> EnumChatFormatting.WHITE + "Distance: "
                    + EnumChatFormatting.GREEN
                    + (cycleSyncer.getValue() ? cycleDistanceSyncer.getValue() : distanceSyncer.getValue()))
                .asWidget());
        minerInfo.onUpdateListener((unused) -> {
            if (com.gtnewhorizons.modularui.common.internal.network.NetworkUtils.isClient()) {
                WidgetTree.resize(minerInfo);
            }
        });
        return minerInfo.children(super.createTerminalTextWidget(syncManager).getChildren());
    }

    @Override
    public IWidget createPanelGap(PanelSyncManager syncManager, ModularPanel parent) {
        UITexture whitelist = UITexture.builder()
            .location(GTNHIntergalactic.MODID, "gui/overlay_button/whitelist")
            .imageSize(16, 16)
            .build();
        UITexture blacklist = UITexture.builder()
            .location(GTNHIntergalactic.MODID, "gui/overlay_button/blacklist")
            .imageSize(16, 16)
            .build();
        Flow panelGap = new Row().widthRel(1)
            .paddingRight(6)
            .paddingLeft(4)
            .height(textBoxToInventoryGap)
            .mainAxisAlignment(Alignment.MainAxis.END);

        BooleanSyncValue whitelistSyncer = new BooleanSyncValue(
            () -> spaceMiner.isWhitelisted,
            val -> spaceMiner.isWhitelisted = val);
        syncManager.syncValue("whitelistDisplay", whitelistSyncer);
        IPanelHandler whitelistPanel = syncManager
            .panel("whiteList", (p_syncManager, syncHandler) -> openFilterPanel(p_syncManager, syncHandler), true);
        panelGap.child(
            new ButtonWidget<>().size(18, 18)
                .marginRight(4)
                .overlay(new DynamicDrawable(() -> {
                    if (spaceMiner.isWhitelisted) return whitelist;
                    return blacklist;
                }))
                .tooltipBuilder(t -> t.addLine(IKey.lang("tt.spaceminer.filterButtonTooltip")))
                .onMousePressed(mouseData -> {
                    if (!whitelistPanel.isPanelOpen()) {
                        whitelistPanel.openPanel();
                    } else {
                        whitelistPanel.closePanel();
                    }
                    return true;
                }));
        createAsteroidUtilityPanel(parent, syncManager, panelGap);
        return panelGap;
    }

    private void createAsteroidUtilityPanel(ModularPanel panel, PanelSyncManager syncManager, Flow panelGap) {

        UITexture spaceMinerConfigTexture = UITexture.fullImage(MODID, "gui/overlay_button/asteroid");
        ButtonWidget spaceMinerConfig = new ButtonWidget();
        spaceMinerConfig
            .tooltip(
                new RichTooltip(spaceMinerConfig).add(IKey.lang("tt.spaceminer.asteroidutilitypanelButtonTooltip")))
            .overlay(
                spaceMinerConfigTexture.asIcon()
                    .size(16, 16));
        spaceMinerConfig.size(18, 18)
            .marginRight(2);

        IPanelHandler spaceMinerConfigPanel = syncManager.panel(
            "asteroidList",
            (p_syncManager, syncHandler) -> getAsteroidUtilityPanel(panel, p_syncManager, syncHandler),
            true);
        spaceMinerConfig.onMousePressed(mouseData -> {
            if (!spaceMinerConfigPanel.isPanelOpen()) {
                spaceMinerConfigPanel.openPanel();
            } else {
                spaceMinerConfigPanel.closePanel();
            }
            return true;
        });
        panelGap.child(spaceMinerConfig);
    }

    private ModularPanel openFilterPanel(PanelSyncManager syncManager, IPanelHandler syncHandler) {

        ModularPanel panel = new ModularPanel("whitelist") {

            @Override
            public boolean isDraggable() {
                return false;
            }
        };
        IntSyncValue distanceParameterSyncer = new IntSyncValue(
            spaceMiner.distanceParameter::getValue,
            spaceMiner.distanceParameter::setValue);
        syncManager.syncValue("distanceParamerter", distanceParameterSyncer);
        IntSyncValue droneTierSyncer = new IntSyncValue(
            () -> spaceMiner.currentDroneMask <= 0 ? -1
                : (int) Math.round((Math.log(spaceMiner.currentDroneMask) / Math.log(2))));
        syncManager.syncValue("droneTier", droneTierSyncer);

        panel.size(18 * 9 + 14 + 185, 18 * 8 + 6)
            .top(30)
            .left(10);

        Flow panelRow = new Row().sizeRel(1);
        createFilterSlotsAndButtons(panelRow, syncManager, distanceParameterSyncer, droneTierSyncer);
        createFilterConfiguration(panelRow, syncManager, panel, distanceParameterSyncer, droneTierSyncer);

        return panel.child(panelRow);
    }

    private void createFilterSlotsAndButtons(Flow panelRow, PanelSyncManager syncManager,
        IntSyncValue distanceParameterSyncer, IntSyncValue droneTierSyncer) {
        UITexture whitelist = UITexture.builder()
            .location(GTNHIntergalactic.MODID, "gui/overlay_button/whitelist")
            .imageSize(16, 16)
            .build();
        UITexture blacklist = UITexture.builder()
            .location(GTNHIntergalactic.MODID, "gui/overlay_button/blacklist")
            .imageSize(16, 16)
            .build();

        BooleanSyncValue isWhiteListedSyncer = new BooleanSyncValue(
            () -> spaceMiner.isWhitelisted,
            val -> spaceMiner.isWhitelisted = val);
        syncManager.syncValue("isWhiteListed", isWhiteListedSyncer);

        StringBuilder bla;
        String[] matrix = new String[8];
        for (int i = 0; i < 8; i++) {
            bla = new StringBuilder();
            for (int j = 0; j < 8; j++) {
                bla.append('S');
            }
            matrix[i] = bla.toString();
        }

        SlotGroupWidget slotGroup = SlotGroupWidget.builder()
            .matrix(matrix)
            .key('S', index -> spaceMiner.filterSlots[index].background(new DynamicDrawable(() -> {
                if (spaceMiner.filterModularSlots[index].getStack() != null
                    && filterContainsOre(spaceMiner.filterModularSlots[index].getStack())
                    && currentOresContainThis(
                        spaceMiner.filterModularSlots[index].getStack(),
                        distanceParameterSyncer.getValue(),
                        droneTierSyncer.getValue(),
                        syncManager,
                        index)) {
                    return new DrawableStack(
                        GuiTextures.SLOT_ITEM,
                        new Rectangle().setColor(spaceMiner.isWhitelisted ? Color.rgb(0, 255, 0) : Color.rgb(255, 0, 0))
                            .asIcon()
                            .size(16, 16));
                } else {
                    return GuiTextures.SLOT_ITEM;
                }
            })))
            .build();
        panelRow.child(
            slotGroup.marginLeft(3)
                .marginRight(4))
            .child(
                new Column().heightRel(1)
                    .width(18)
                    .child(
                        new ButtonWidget<>().size(18, 18)
                            .marginTop(3)
                            .marginRight(4)
                            .marginBottom(4)
                            .overlay(new DynamicDrawable(() -> {
                                if (isWhiteListedSyncer.getValue()) {
                                    return whitelist;
                                } else {
                                    return blacklist;
                                }
                            }))
                            .tooltipBuilder(t -> t.addLine(IKey.lang("tt.spaceminer.togglefilter")))
                            .onMousePressed(mouseData -> {
                                isWhiteListedSyncer.setValue(!isWhiteListedSyncer.getBoolValue());
                                return true;
                            }))
                    .child(
                        new ButtonWidget<>().size(18, 18)
                            .marginBottom(4)
                            .overlay(IKey.str("C"))
                            .onMousePressed(mouseData -> {
                                for (int i = 0; i < 64; i++) {
                                    spaceMiner.filterModularSlots[i].putStack(null);
                                    spaceMiner.filterSlots[i].getSyncHandler()
                                        .updateFromClient(null, 0);
                                }
                                return true;
                            })));
    }

    private boolean currentOresContainThis(ItemStack stack, int distance, int droneTier, PanelSyncManager syncManager,
        int index) {
        if (distance < 0 || droneTier < 0) return false;
        Map<Integer, List<Pair<Integer, GTRecipe>>> asteroids = asteroidDistanceMap.get(droneTier);

        PanelSyncManager rootManager = syncManager.getModularSyncManager()
            .getPanelSyncManager("MTEMultiblockBase");
        IntSyncValue rangeSyncer = (IntSyncValue) rootManager.getSyncHandler("rangeParameter:0");
        BooleanSyncValue cycleSyncer = (BooleanSyncValue) rootManager.getSyncHandler("cycleParameter:0");

        // results cached as this is a pretty expensive search

        int start = distance - (cycleSyncer.getValue() ? rangeSyncer.getValue() : 0);
        int end = distance + (cycleSyncer.getValue() ? rangeSyncer.getValue() : 0);

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
                        .getMetadata(IGRecipeMaps.MODULE_TIER) <= spaceMiner.tModuleTier)
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

    private void createFilterConfiguration(Flow panelRow, PanelSyncManager syncManager, ModularPanel panel,
        IntSyncValue distanceParameterSyncer, IntSyncValue droneTierSyncer) {
        UITexture disabled = UITexture.builder()
            .location(GregTech.ID, "gui/overlay_button/disable")
            .imageSize(16, 16)
            .build();

        Flow resultColumn = new Column().heightRel(1)
            .width(185)
            .padding(4);
        resultColumn.child(
            new Row().widthRel(1)
                .coverChildrenHeight()
                .marginBottom(4)
                .child(
                    IKey.str("Currently minable asteroids for ")
                        .asWidget())
                .child(new DynamicDrawable(() -> {
                    if (droneTierSyncer.getValue() < 0) return disabled;
                    return new ItemDrawable(MINING_DRONES[droneTierSyncer.getValue()]);
                }).asWidget()
                    .tooltipBuilder(t -> {
                        if (droneTierSyncer.getValue() < 0) t.addLine(IKey.str("No valid inputs!"));
                        else t.addFromItem(MINING_DRONES[droneTierSyncer.getValue()]);
                    })));

        Flow asteroidButtonColumn = new Column().widthRel(1)
            .marginTop(4);
        resultColumn.child(asteroidButtonColumn);
        panelRow.child(resultColumn);

        droneTierSyncer.setChangeListener(() -> {
            asteroidButtonColumn.getChildren()
                .clear();
            generateAsteroidButtons(
                asteroidButtonColumn,
                droneTierSyncer.getValue(),
                distanceParameterSyncer.getValue(),
                syncManager);
            if (NetworkUtils.isClient()) {
                WidgetTree.resize(panelRow);
            }
        });
    }

    private void generateAsteroidButtons(Flow resultColumn, int droneTier, int distance, PanelSyncManager syncManager) {
        if (droneTier < 0) return;

        Map<Integer, List<Pair<Integer, GTRecipe>>> asteroids = asteroidDistanceMap.get(droneTier);

        PanelSyncManager rootManager = syncManager.getModularSyncManager()
            .getPanelSyncManager("MTEMultiblockBase");
        IntSyncValue distanceSyncer = (IntSyncValue) rootManager.getSyncHandler("distanceParameter:0");
        IntSyncValue rangeSyncer = (IntSyncValue) rootManager.getSyncHandler("rangeParameter:0");
        BooleanSyncValue cycleSyncer = (BooleanSyncValue) rootManager.getSyncHandler("cycleParameter:0");

        int start = distanceSyncer.getValue() - (cycleSyncer.getValue() ? rangeSyncer.getValue() : 0);
        int end = distanceSyncer.getValue() + (cycleSyncer.getValue() ? rangeSyncer.getValue() : 0);

        Flow asteroidRow = new Row().widthRel(1)
            .height(18)
            .marginBottom(4);

        int cnt = 0;

        Set<Integer> visited = new HashSet();
        for (int i = start; i <= end; i++) {
            List<Pair<Integer, GTRecipe>> asteroidsAtDistance = asteroids.get(i)
                .stream()
                .filter(
                    asteroid -> asteroid.second()
                        .getMetadata(IGRecipeMaps.MODULE_TIER) <= spaceMiner.tModuleTier)
                .collect(toList());

            for (Pair<Integer, GTRecipe> asteroid : asteroidsAtDistance) {
                int index = asteroid.first();
                if (visited.contains(index)) continue;
                visited.add(index);
                AsteroidData data = uniqueAsteroidList.get(index);

                IPanelHandler asteroidInfoPanel = (IPanelHandler) syncManager.getModularSyncManager()
                    .getPanelSyncManager("MTEMultiblockBase")
                    .getSyncHandler("asteroidInfoPanel" + index);

                ItemStack oreItem = data.outputItems != null ? data.outputItems[0]
                    : GTOreDictUnificator.get(data.orePrefixes, data.output[0], 1);
                ButtonWidget asteroidButton = new ButtonWidget<>().size(18, 18)
                    .background(new DynamicDrawable(() -> {
                        if (filterContainsAsteroidOre(data)) {
                            return new DrawableStack(
                                GuiTextures.BUTTON_CLEAN,
                                new Rectangle()
                                    .setColor(spaceMiner.isWhitelisted ? Color.rgb(0, 255, 0) : Color.rgb(255, 0, 0))
                                    .asIcon()
                                    .size(16, 16));
                        } else {
                            return GuiTextures.BUTTON_CLEAN;
                        }
                    }))
                    .overlay(
                        new ItemDrawable(oreItem).asIcon()
                            .size(16, 16))
                    .tooltipBuilder(
                        t -> t.addLine(IKey.str(EnumChatFormatting.RED + data.getAsteroidNameLocalized()))
                            .addLine(IKey.lang("tt.spaceminer.asteroidButtonTooltipInfo"))
                            .addLine(
                                IKey.lang("tt.spaceminer.asteroidButtonFilter")
                                    .color(Color.rgb(180, 10, 10))))
                    .onMousePressed(mouseData -> {
                        if (mouseData == 1) {
                            addAsteroidToFiler(data);
                            return true;
                        }
                        if (!asteroidInfoPanel.isPanelOpen()) {
                            asteroidInfoPanel.openPanel();
                        } else {
                            asteroidInfoPanel.closePanel();
                        }
                        return true;
                    });
                asteroidButton.marginRight(2);
                asteroidRow.child(asteroidButton);
                if ((cnt + 1) % 8 == 0 || cnt == uniqueAsteroidList.size() - 1) {
                    resultColumn.child(asteroidRow);
                    asteroidRow = new Row().widthRel(1)
                        .height(18)
                        .marginBottom(4);
                }
                cnt++;
            } ;
        }

    }

    private void addAsteroidToFiler(AsteroidData data) {
        int size = data.outputItems != null ? data.outputItems.length : data.output.length;
        List<Integer> visited = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            ItemStack ore = data.outputItems != null ? data.outputItems[i]
                : GTOreDictUnificator.get(data.orePrefixes, data.output[i], 1);
            if (!filterContainsOre(ore)) {
                int j = findFirstEmptySlot(visited);
                spaceMiner.filterModularSlots[j].putStack(ore);
                spaceMiner.filterSlots[j].getSyncHandler()
                    .updateFromClient(ore, 0);
                visited.add(j);
            }
        }
    }

    private ModularPanel getAsteroidUtilityPanel(ModularPanel parent, PanelSyncManager syncManager,
        IPanelHandler thisPanel) {
        UITexture calculatorTexture = UITexture.fullImage(MODID, "gui/overlay_button/calculator");

        Area parentArea = parent.getArea();
        ModularPanel panel = new ModularPanel("asteroidList") {

            @Override
            public boolean isDraggable() {
                return false;
            }
        };
        panel.size(175, ((uniqueAsteroidList.size() / 8) + 1 + 6) * 18 - 10)
            .pos(parentArea.x, parentArea.y)
            .padding(5);
        AtomicReference<String> search = new AtomicReference<>("");
        StringSyncValue textFieldSyncer = new StringSyncValue(search::get, search::set);

        AtomicInteger distanceFilter = new AtomicInteger(0);
        IntSyncValue distanceSyncer = new IntSyncValue(distanceFilter::get, distanceFilter::set);

        IntSyncValue distanceParameterSyncer = new IntSyncValue(
            spaceMiner.distanceParameter::getValue,
            spaceMiner.distanceParameter::setValue);
        syncManager.syncValue("distanceParamerter", distanceParameterSyncer);

        AtomicInteger moduleTierFilter = new AtomicInteger(0);
        IntSyncValue moduleTierFilterSyncer = new IntSyncValue(moduleTierFilter::get, moduleTierFilter::set);

        AtomicInteger droneFilter = new AtomicInteger(-1);
        IntSyncValue droneFilterSyncer = new IntSyncValue(droneFilter::get, droneFilter::set);
        syncManager.syncValue("droneFilter", droneFilterSyncer);

        AtomicInteger targetDroneTier = new AtomicInteger(-1);
        IntSyncValue targetDroneTierSyncer = new IntSyncValue(targetDroneTier::get, targetDroneTier::set);
        syncManager.syncValue("droneTarget", targetDroneTierSyncer);

        AtomicInteger selectedAsteroid = new AtomicInteger(0);
        IntSyncValue selectedAsteroidSyncer = new IntSyncValue(selectedAsteroid::get, selectedAsteroid::set);
        syncManager.syncValue("selectedAsteroid", selectedAsteroidSyncer);

        Flow asteroidColumn = new Column().sizeRel(1);
        Flow asteroidRow = new Row().widthRel(1)
            .height(18)
            .marginBottom(4);
        List<IPanelHandler> asteroidPanels = new ArrayList<>();
        List<IPanelHandler> droneSelectorPanels = new ArrayList<>();

        IPanelHandler droneSelectorPanel = syncManager.panel(
            "droneSelectorPanel",
            (p_syncManager, syncHandler) -> opendroneSelectorPanel(
                p_syncManager,
                syncHandler,
                droneFilterSyncer,
                "asteroidutilitypanel",
                panel,
                selectedAsteroidSyncer,
                distanceParameterSyncer),
            true);

        IPanelHandler droneSelectorPanelTarget = syncManager.panel(
            "droneSelectorPanelTarget",
            (p_syncManager, syncHandler) -> opendroneSelectorPanel(
                p_syncManager,
                syncHandler,
                targetDroneTierSyncer,
                "asteroidTarget",
                panel,
                selectedAsteroidSyncer,
                distanceParameterSyncer),
            true);

        IPanelHandler minerCalculator = syncManager.panel(
            "spaceMinerCalculator",
            (p_syncManager, syncHandler) -> openSpaceMinerCalculator(
                p_syncManager,
                syncHandler,
                parent,
                asteroidPanels,
                selectedAsteroidSyncer,
                distanceParameterSyncer),
            true);
        for (int i = 0; i < uniqueAsteroidList.size(); i++) {
            int finalI = i;

            AsteroidData data = uniqueAsteroidList.get(i);
            IPanelHandler asteroidutilitypanel = syncManager.panel(
                "asteroidutilitypanel" + finalI,
                (p_syncManager,
                    syncHandler) -> getAsteroidPanel(p_syncManager, syncHandler, finalI, droneSelectorPanelTarget),
                true);
            asteroidPanels.add(asteroidutilitypanel);

            ItemStack oreItem = data.outputItems != null ? data.outputItems[0]
                : GTOreDictUnificator.get(data.orePrefixes, data.output[0], 1);
            ButtonWidget asteroidButton = new ButtonWidget<>().size(18, 18)
                .overlay(new DynamicDrawable(() -> {
                    // Temporary solution for jank item rendering TODO: REMOVE THIS WHEN IT'S FIXED!
                    if (asteroidPanels.stream()
                        .anyMatch(IPanelHandler::isPanelOpen) || minerCalculator.isPanelOpen()) return null;
                    if (matchesFilters(
                        data,
                        textFieldSyncer.getValue(),
                        distanceSyncer.getValue(),
                        droneFilterSyncer.getValue(),
                        moduleTierFilterSyncer.getValue())) {
                        return new DrawableStack(
                            new Rectangle().setColor(Color.rgb(0, 255, 0))
                                .asIcon()
                                .size(16, 16),
                            new ItemDrawable(oreItem).asIcon()
                                .size(16, 16));
                    } else {
                        return new ItemDrawable(oreItem).asIcon()
                            .size(16, 16);
                    }
                }))
                .tooltipBuilder(
                    t -> t.addLine(IKey.str(EnumChatFormatting.RED + data.getAsteroidNameLocalized()))
                        .addLine(IKey.lang("tt.spaceminer.asteroidButtonTooltipInfo")))
                .onMousePressed(mouseData -> {
                    if (!asteroidutilitypanel.isPanelOpen()) {
                        selectedAsteroidSyncer.setValue(finalI);
                        asteroidutilitypanel.openPanel();
                    } else {
                        asteroidutilitypanel.closePanel();
                    }
                    return true;
                });
            asteroidButton.marginRight(2);
            asteroidRow.child(asteroidButton);
            if ((i + 1) % 8 == 0 || i == uniqueAsteroidList.size() - 1) {
                asteroidColumn.child(asteroidRow);
                asteroidRow = new Row().widthRel(1)
                    .height(18)
                    .marginBottom(4);
            } ;
        }
        asteroidColumn.child(
            new Column().widthRel(1)
                .height(18 * 4)
                // Text fields
                .child(
                    new Row().widthRel(1)
                        .coverChildrenHeight()
                        .child(
                            IKey.lang("tt.spaceminer.textFieldOre")
                                .asWidget()
                                .marginBottom(4)
                                .width(50)
                                .color(Color.WHITE.main))
                        .child(
                            new TextFieldWidget().size(60, 9)
                                .marginBottom(4)
                                .value(textFieldSyncer)))
                .child(
                    new Row().widthRel(1)
                        .coverChildrenHeight()
                        .child(
                            IKey.lang("tt.spaceminer.textFieldDistance")
                                .asWidget()
                                .marginBottom(4)
                                .width(50)
                                .color(Color.WHITE.main))
                        .child(
                            new TextFieldWidget().size(60, 9)
                                .marginBottom(4)
                                .value(distanceSyncer)
                                .setDefaultNumber(0)
                                .setNumbers(0, Integer.MAX_VALUE)))
                .child(
                    new Row().widthRel(1)
                        .coverChildrenHeight()
                        .child(
                            IKey.lang("tt.spaceminer.textFieldTier")
                                .asWidget()
                                .marginBottom(4)
                                .width(50)
                                .color(Color.WHITE.main))
                        .child(
                            new TextFieldWidget().size(60, 9)
                                .marginBottom(4)
                                .value(moduleTierFilterSyncer)
                                .setDefaultNumber(0)
                                .setNumbers(0, 3)))
                // Drone selection button
                .child(
                    new Row().widthRel(1)
                        .height(18)
                        .child(
                            new SlotLikeButtonWidget(
                                () -> droneFilterSyncer.getValue() >= 0 ? MINING_DRONES[droneFilterSyncer.getValue()]
                                    : null).size(18, 18)
                                        .marginBottom(4)
                                        .alignX(0)
                                        .onMousePressed(mouseData -> {
                                            if (!droneSelectorPanel.isPanelOpen()) {
                                                droneSelectorPanel.openPanel();
                                            } else {
                                                droneSelectorPanel.closePanel();
                                            }
                                            return true;
                                        })
                                        .align(Alignment.CenterLeft))
                        // Calculator Button
                        .child(
                            new ButtonWidget<>().size(18, 18)
                                .overlay(calculatorTexture)
                                .align(Alignment.CenterRight)
                                .tooltipBuilder(t -> t.addLine(IKey.lang("tt.spaceminer.calculatorButtonTooltip")))
                                .onMousePressed(mouseData -> {
                                    if (!minerCalculator.isPanelOpen()) {
                                        minerCalculator.openPanel();
                                    } else {
                                        minerCalculator.closePanel();
                                    }
                                    return true;
                                }))));
        panel.child(asteroidColumn);
        return panel;
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

    private ModularPanel getAsteroidPanel(PanelSyncManager syncManager, IPanelHandler thisPanel, int asteroidIndex,
        IPanelHandler droneSelectorPanel) {
        ModularPanel panel = new ModularPanel("asteroidutilitypanelrmationPanel" + asteroidIndex) {

            @Override
            public boolean isDraggable() {
                return false;
            }
        };
        AsteroidData data = uniqueAsteroidList.get(asteroidIndex);
        UITexture targetAsteroidTexture = UITexture.builder()
            .location(MODID, "gui/overlay_button/target_asteroid")
            .imageSize(16, 16)
            .build();

        int outputLength = data.output != null ? data.output.length : data.outputItems.length;
        panel
            .size(
                250,
                130 - (droneSelectorPanel == null ? 24 : 0)
                    + 22 * (((outputLength - 1) / 9) + ((data.maxDroneTier - data.minDroneTier - 1) / 10) + 1))
            .pos(140, 30)
            .padding(5);

        Flow column = new Column().sizeRel(1);

        // Ore Icon, Asteroid Name
        ItemStack oreItem = data.outputItems != null ? data.outputItems[0]
            : GTOreDictUnificator.get(data.orePrefixes, data.output[0], 1);
        column.child(
            new Row().widthRel(1)
                .height(18)
                .child(
                    new ItemDrawable(oreItem).asWidget()
                        .marginRight(5))
                .child(
                    IKey.str(EnumChatFormatting.DARK_RED + data.getAsteroidNameLocalized())
                        .asWidget())
                .marginBottom(4));
        // Can be mined by: X-Y Mining Drones
        Flow miningDroneRow = new Row().widthRel(1)
            .coverChildrenHeight();
        miningDroneRow.child(
            IKey.lang("tt.spaceminer.asteroidutilitypanel.miningDrones")
                .asWidget()
                .topRel(0, 9, 0));

        Flow droneDrawables = new Column().coverChildren()
            .marginBottom(4);

        Flow droneRow = new Row().widthRel(1)
            .height(18)
            .marginBottom(4);
        for (int i = data.minDroneTier; i <= data.maxDroneTier; i++) {
            ItemStack droneItem = MINING_DRONES[i];
            ItemStack droneRodItem = MINING_RODS[i];
            ItemStack droneDrillItem = MINING_DRILLS[i];
            int finalI = i;
            droneRow.child(
                new ItemDrawable(droneItem).asWidget()
                    .tooltipBuilder(
                        t -> t.addLine(IKey.str(droneItem.getDisplayName()))
                            .add(IKey.lang("tt.spaceminer.asteroidutilitypanel.uses"))
                            .add(IKey.str(" 4"))
                            .add(new ItemDrawable(droneRodItem))
                            .add(IKey.lang("tt.spaceminer.asteroidutilitypanel.and"))
                            .add(IKey.str(" 4"))
                            .add(new ItemDrawable(droneDrillItem))
                            .addLine(IKey.lang("tt.spaceminer.asteroidutilitypanel.parallelInfo"))
                            .addLine(
                                IKey.lang(
                                    "tt.spaceminer.asteroidutilitypanel.sizeForDrone",
                                    (int) (data.minSize + Math.pow(2, finalI - data.minDroneTier) - 1),
                                    (int) (data.maxSize + Math.pow(2, finalI - data.minDroneTier) - 1)))
                            .addLine(
                                IKey.lang(
                                    "tt.spaceminer.asteroidutilitypanel.miningTime",
                                    String.format(
                                        "%.2f",
                                        data.duration / (20 * Math.sqrt(finalI - data.minDroneTier + 1)))))));
            if ((i - data.minDroneTier + 1) % 10 == 0 || i == data.maxDroneTier) {
                droneDrawables.child(droneRow);
                droneRow = new Row().widthRel(1)
                    .height(18)
                    .marginBottom(4);
            }
        }
        miningDroneRow.child(droneDrawables);
        column.child(miningDroneRow);
        // Distance Information
        column.child(
            new SingleChildWidget<>().widthRel(1)
                .height(9)
                .child(
                    IKey.lang(
                        "tt.spaceminer.asteroidutilitypanel.distanceRange",
                        EnumChatFormatting.GREEN,
                        data.minDistance,
                        data.maxDistance,
                        EnumChatFormatting.RESET)
                        .asWidget())
                .marginBottom(4))
            // Computation
            .child(
                new SingleChildWidget<>().widthRel(1)
                    .height(9)
                    .child(
                        IKey.lang(
                            "tt.spaceminer.asteroidutilitypanel.computation",
                            EnumChatFormatting.BLUE,
                            data.computation)
                            .asWidget())
                    .marginBottom(4))
            // Module tier
            .child(
                new SingleChildWidget<>().widthRel(1)
                    .height(9)
                    .child(
                        IKey.lang("tt.spaceminer.asteroidutilitypanel.moduleTier", data.requiredModuleTier)
                            .asWidget())
                    .marginBottom(4));

        // Drops
        Flow drops = new Row().coverChildrenHeight();

        Flow dropsColumns = new Column().widthRel(1)
            .height(18)
            .marginBottom(4);

        Flow dropRow = new Row().widthRel(1)
            .height(18)
            .marginBottom(4);
        drops.child(
            IKey.lang("tt.spaceminer.asteroidutilitypanel.drops")
                .asWidget());
        int totalWeight = Arrays.stream(data.chances)
            .sum();
        for (int i = 0; i < outputLength; i++) {
            ItemStack ore = data.outputItems != null ? data.outputItems[i]
                : GTOreDictUnificator.get(data.orePrefixes, data.output[i], 1);
            int finalI = i;
            dropRow.child(new SlotLikeButtonWidget(ore).tooltipBuilder(t -> {
                t.addLine(IKey.str(ore.getDisplayName()))
                    .addLine(IKey.str(((double) data.chances[finalI] / totalWeight) * 100 + "%"));
                if (droneSelectorPanel == null)
                    t.addLine(IKey.str(EnumChatFormatting.DARK_GREEN + "Click to add this ore to the filter"));
            })
                .onMousePressed(mouseData -> {
                    if (droneSelectorPanel != null) return true;
                    if (filterContainsOre(ore)) return true;
                    int j = findFirstEmptySlot();
                    spaceMiner.filterModularSlots[j].putStack(ore);
                    spaceMiner.filterSlots[j].getSyncHandler()
                        .updateFromClient(ore, 0);
                    return true;
                })
                .background(new DynamicDrawable(() -> {
                    if (droneSelectorPanel != null) return GuiTextures.SLOT_ITEM;
                    if (filterContainsOre(ore)) {
                        return new DrawableStack(
                            GuiTextures.SLOT_ITEM,
                            new Rectangle()
                                .setColor(spaceMiner.isWhitelisted ? Color.rgb(0, 255, 0) : Color.rgb(255, 0, 0))
                                .asIcon()
                                .size(16, 16));
                    } else {
                        return GuiTextures.SLOT_ITEM;
                    }
                }))
                .marginRight(5));

            if ((i + 1) % 9 == 0 || i == outputLength - 1) {
                dropsColumns.child(dropRow);
                dropRow = new Row().widthRel(1)
                    .height(18)
                    .marginBottom(4);
            }
        }

        drops.child(dropsColumns);
        column.child(drops);
        column.child(
            new ButtonWidget<>().setEnabledIf(w -> droneSelectorPanel != null)
                .size(18, 18)
                .marginBottom(4)
                .alignX(0)
                .overlay(
                    targetAsteroidTexture.asIcon()
                        .size(16, 16))
                .tooltipBuilder(
                    t -> t.addLine(IKey.lang("tt.spaceminer.asteroidutilitypanel.targetAsteroidButtonTooltip")))
                .onMousePressed(mouseData -> {
                    droneSelectorPanel.openPanel();
                    return true;
                }));
        return panel.child(column);
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
            if (spaceMiner.filterInventory.getStackInSlot(i) != null && spaceMiner.filterInventory.getStackInSlot(i)
                .getDisplayName()
                .equals(ore.getDisplayName())) return true;
        }
        return false;
    }

    private int findFirstEmptySlot() {
        for (int i = 0; i < 64; i++) {
            if (spaceMiner.filterInventory.getStackInSlot(i) == null) {
                return i;
            }
        }
        return -1;
    }

    private int findFirstEmptySlot(List<Integer> visited) {
        for (int i = 0; i < 64; i++) {
            if (spaceMiner.filterInventory.getStackInSlot(i) == null && !visited.contains(i)) {
                return i;
            }
        }
        return -1;
    }

    private ModularPanel opendroneSelectorPanel(PanelSyncManager syncManager, IPanelHandler syncHandler,
        IntSyncValue syncer, String suffix, ModularPanel parent, IntSyncValue asteroidSyncer,
        IntSyncValue parameterSyncer) {
        Area parentArea = parent.getArea();
        ModularPanel panel = new ModularPanel("droneSelectorPanel" + suffix) {

            @Override
            public boolean isDraggable() {
                return false;
            }
        }.size(18 * 5 + 6, 6 + 18 * (MINING_DRONES.length / 5 + 1))
            .pos(
                parentArea.x + 2,
                parentArea.y + parentArea.height
                    - (suffix.equals("asteroidTarget") ? 74 : 36)
                    - (6 + 18 * (MINING_DRONES.length / 5 + 1)))
            .padding(3);
        Grid grid = new Grid();
        List<List<IWidget>> drones = new ArrayList<>();
        drones.add(new ArrayList<>());
        drones.get(0)
            .add(new SlotLikeButtonWidget(() -> null).onMousePressed(mouseData -> {
                syncer.setValue(-1);
                if (suffix.equals("asteroidTarget")) {
                    parameterSyncer.setValue(0);
                    parent.closeIfOpen(false);
                } else syncHandler.closePanel();
                return true;
            }));

        int row = 0;
        for (int i = 0; i < MINING_DRONES.length; i++) {
            ItemStack drone = MINING_DRONES[i];
            int finalI = i;
            drones.get(row)
                .add(new SlotLikeButtonWidget(drone).onMousePressed(mouseData -> {
                    syncer.setValue(finalI);
                    if (suffix.equals("asteroidTarget")) {
                        parameterSyncer.setValue(optimizeDistance(asteroidSyncer.getValue(), finalI));
                        parent.closeIfOpen(false);
                    } else syncHandler.closePanel();
                    return true;
                }));
            if (drones.get(row)
                .size() % 5 == 0 || i == MINING_DRONES.length - 1) {
                row++;
                drones.add(new ArrayList<>());
            }
        }
        grid.matrix(drones);

        return panel.child(grid.sizeRel(1));
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
                if (recipe.getMetadata(IGRecipeMaps.MODULE_TIER) > spaceMiner.tModuleTier) continue;
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
                if (recipe.getMetadata(IGRecipeMaps.MODULE_TIER) > spaceMiner.tModuleTier) continue;
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

    private ModularPanel openSpaceMinerCalculator(PanelSyncManager syncManager, IPanelHandler panelSyncHandler,
        ModularPanel parent, List<IPanelHandler> asteroidPanels, IntSyncValue selectedAsteroidSyncer,
        IntSyncValue distanceParameterSyncer) {
        Area parentArea = parent.getArea();
        ModularPanel panel = new ModularPanel("spaceMinerCalculator") {

            @Override
            public boolean isDraggable() {
                return false;
            }
        }.size(200, 164)
            .pos(parentArea.x, parentArea.y)
            .paddingTop(3)
            .paddingLeft(5)
            .paddingRight(5);
        UITexture nerdTexture = UITexture.fullImage(MODID, "gui/overlay_button/nerd");

        AtomicInteger distance = new AtomicInteger(0);
        IntSyncValue distanceSyncer = new IntSyncValue(distance::get, distance::set);

        AtomicInteger moduleTier = new AtomicInteger(0);
        IntSyncValue moduleTierSyncer = new IntSyncValue(moduleTier::get, moduleTier::set);

        AtomicInteger droneFilter = new AtomicInteger(-1);
        IntSyncValue droneSyncer = new IntSyncValue(droneFilter::get, droneFilter::set);
        syncManager.syncValue("droneSyncerCalculator", droneSyncer);

        IPanelHandler droneSelectorPanel = syncManager.panel(
            "droneSelectorPanelSpaceMiner",
            (p_syncManager, syncHandler) -> opendroneSelectorPanel(
                p_syncManager,
                syncHandler,
                droneSyncer,
                "calculator",
                panel,
                selectedAsteroidSyncer,
                distanceParameterSyncer),
            true);

        Flow column = new Column().sizeRel(1);
        ListWidget<IWidget, ?> outputListWidget = new ListWidget<>()
            .background(new DrawableStack(new Rectangle().setColor(Color.rgb(91, 110, 225))))
            .widthRel(1)
            .height(100)
            .marginBottom(4)
            .child(
                new Column().widthRel(1)
                    .child(
                        IKey.lang("tt.spaceminer.calculator.missing.distance")
                            .asWidget()
                            .setEnabledIf(w -> distance.get() <= 0)
                            .alignX(0)
                            .marginBottom(4))
                    .child(
                        IKey.lang("tt.spaceminer.calculator.missing.moduleTier")
                            .asWidget()
                            .setEnabledIf(w -> moduleTier.get() <= 0)
                            .alignX(0)
                            .marginBottom(4))
                    .child(
                        IKey.lang("tt.spaceminer.calculator.missing.drone")
                            .asWidget()
                            .setEnabledIf(w -> droneFilter.get() <= 0)
                            .alignX(0)
                            .marginBottom(4)));
        column.child(outputListWidget);
        column.child(
            new Column().widthRel(1)
                .height(18 * 4)
                .child(
                    new Row().widthRel(1)
                        .coverChildrenHeight()
                        .child(
                            IKey.lang("tt.spaceminer.textFieldDistance")
                                .asWidget()
                                .marginBottom(4)
                                .width(50)
                                .color(Color.WHITE.main))
                        .child(
                            new TextFieldWidget().marginBottom(4)
                                .size(60, 9)
                                .value(distanceSyncer)
                                .setDefaultNumber(0)
                                .setNumbers(0, Integer.MAX_VALUE)))
                .child(
                    new Row().widthRel(1)
                        .coverChildrenHeight()
                        .child(
                            IKey.lang("tt.spaceminer.textFieldTier")
                                .asWidget()
                                .marginBottom(4)
                                .width(50)
                                .color(Color.WHITE.main))
                        .child(
                            new TextFieldWidget().size(60, 9)
                                .value(moduleTierSyncer)
                                .setDefaultNumber(0)
                                .setNumbers(0, 3)))
                .child(
                    new Row().widthRel(1)
                        .height(18)
                        .child(
                            new SlotLikeButtonWidget(
                                () -> droneSyncer.getValue() >= 0 ? MINING_DRONES[droneSyncer.getValue()] : null)
                                    .size(18, 18)
                                    .marginBottom(4)
                                    .alignX(0)
                                    .onMousePressed(mouseData -> {
                                        if (!droneSelectorPanel.isPanelOpen()) {
                                            droneSelectorPanel.openPanel();
                                        } else {
                                            droneSelectorPanel.closePanel();
                                        }
                                        return true;
                                    })
                                    .align(Alignment.CenterLeft))
                        .child(
                            new ButtonWidget<>().size(18, 18)
                                .overlay(
                                    nerdTexture.asIcon()
                                        .size(16, 16))
                                .tooltipBuilder(t -> t.addLine(IKey.lang("tt.spaceminer.calculator.calculate")))
                                .align(Alignment.CenterRight)
                                .onMousePressed(mouseData -> {
                                    List<IWidget> output = calculateOutput(
                                        distanceSyncer.getValue(),
                                        moduleTierSyncer.getValue(),
                                        droneSyncer.getValue(),
                                        asteroidPanels,
                                        selectedAsteroidSyncer);
                                    outputListWidget.getChildren()
                                        .clear();
                                    outputListWidget.children(output);
                                    WidgetTree.resize(outputListWidget);
                                    return true;
                                }))));
        return panel.child(column);
    }

    private List<IWidget> calculateOutput(Integer distance, Integer moduleTier, Integer droneTier,
        List<IPanelHandler> asteroidPanels, IntSyncValue selectedAsteroidSyncer) {
        List<IWidget> listResult = new ArrayList<>();

        List<Pair<Integer, GTRecipe>> asteroids = SpaceMiningRecipes.asteroidDistanceMap.get(droneTier)
            .computeIfAbsent(distance, w -> new ArrayList<>())
            .stream()
            .filter(pair -> {
                GTRecipe recipe = pair.second();
                Integer requiredModuleTier = recipe.getMetadata(IGRecipeMaps.MODULE_TIER);
                assert requiredModuleTier != null;
                if (moduleTier < requiredModuleTier) return false;
                return true;
            })
            .sorted(
                (a, b) -> Objects.requireNonNull(
                    b.second()
                        .getMetadata(IGRecipeMaps.SPACE_MINING_DATA)).recipeWeight
                    - Objects.requireNonNull(
                        a.second()
                            .getMetadata(IGRecipeMaps.SPACE_MINING_DATA)).recipeWeight)
            .collect(toList());

        AtomicInteger weightSum = new AtomicInteger();
        asteroids.forEach(
            asteroid -> weightSum.addAndGet(
                Objects.requireNonNull(
                    asteroid.second()
                        .getMetadata(IGRecipeMaps.SPACE_MINING_DATA)).recipeWeight));

        for (Pair<Integer, GTRecipe> asteroidPair : asteroids) {
            GTRecipe asteroid = asteroidPair.second();
            SpaceMiningData data = asteroid.getMetadata(IGRecipeMaps.SPACE_MINING_DATA);
            ButtonWidget asteroidButton = new ButtonWidget<>().size(18, 18)
                .overlay(new DynamicDrawable(() -> {
                    // Temporary solution for jank item rendering TODO: REMOVE THIS WHEN IT'S FIXED!
                    if (asteroidPanels.stream()
                        .anyMatch(IPanelHandler::isPanelOpen)) return null;
                    return new ItemDrawable(asteroid.mOutputs[0]).asIcon()
                        .size(16, 16);
                }))
                .tooltipBuilder(
                    t -> t.addLine(IKey.str(EnumChatFormatting.DARK_RED + data.getAsteroidNameLocalized()))
                        .addLine(IKey.lang("tt.spaceminer.asteroidButtonTooltipInfo")))
                .onMousePressed(mouseData -> {
                    selectedAsteroidSyncer.setValue(asteroidPair.first());
                    asteroidPanels.get(asteroidPair.first())
                        .openPanel();
                    return true;
                });
            listResult.add(
                new Row().widthRel(1)
                    .height(18)
                    .child(
                        IKey.lang(
                            "tt.spaceminer.calculator.asteroidChance",
                            String.format("%.3f%%", ((double) data.recipeWeight / weightSum.get() * 100)))
                            .asWidget()
                            .marginRight(4))
                    .child(asteroidButton));
        }
        return listResult;
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        for (int i = 0; i < uniqueAsteroidList.size(); i++) {
            int finalI = i;
            IPanelHandler asteroidInfoPanel = syncManager.panel(
                "asteroidInfoPanel" + finalI,
                (p_syncManager, syncHandler) -> getAsteroidPanel(p_syncManager, syncHandler, finalI, null),
                true);
            asteroidPanels.add(asteroidInfoPanel);
        }

        syncManager.syncValue(
            "distanceParameter",
            new IntSyncValue(spaceMiner.distanceParameter::getValue, spaceMiner.distanceParameter::setValue));
        syncManager.syncValue(
            "cycleDistanceParameter",
            new IntSyncValue(spaceMiner.cycleDistanceParameter::getValue, spaceMiner.cycleDistanceParameter::setValue));
        syncManager.syncValue(
            "rangeParameter",
            new IntSyncValue(spaceMiner.rangeParameter::getValue, spaceMiner.rangeParameter::setValue));
        syncManager.syncValue(
            "stepParameter",
            new IntSyncValue(spaceMiner.stepParameter::getValue, spaceMiner.stepParameter::setValue));

        syncManager.syncValue("cycleParameter", new BooleanSyncValue(spaceMiner.cycleParameter::getValue, val -> {
            spaceMiner.cycleParameter.setValue(val);
            checked = new boolean[64];
        }));
    }
}
