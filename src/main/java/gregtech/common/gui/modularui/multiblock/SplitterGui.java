package gregtech.common.gui.modularui.multiblock;

import static gregtech.common.tileentities.machines.multi.nanochip.util.SplitterRule.FilterType.*;

import net.minecraft.init.Items;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IIcon;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.BoolValue;
import com.cleanroommc.modularui.value.IntValue;
import com.cleanroommc.modularui.value.sync.DynamicSyncHandler;
import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.PhantomItemSlotSH;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widget.WidgetTree;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.DynamicSyncedWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.slot.PhantomItemSlot;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import cpw.mods.fml.relauncher.Side;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.modularui2.widget.ColorGridWidget;
import gregtech.common.tileentities.machines.multi.nanochip.modules.MTESplitterModule;
import gregtech.common.tileentities.machines.multi.nanochip.util.SplitterRule;
import gregtech.common.tileentities.machines.multi.nanochip.util.SplitterRule.SplitterRuleAdapter;

public class SplitterGui extends MTEMultiBlockBaseGui<MTESplitterModule> {

    private static final SplitterRuleAdapter RULE_ADAPTER = new SplitterRuleAdapter();

    int scrollValue;
    ModularPanel subPanel;

    public SplitterGui(MTESplitterModule multiblock) {
        super(multiblock);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.syncValue(
            "rules",
            GenericListSyncHandler.<SplitterRule>builder()
                .getter(() -> multiblock.rules)
                .setter(val -> {
                    multiblock.rules.clear();
                    multiblock.rules.addAll(val);
                })
                .adapter(RULE_ADAPTER)
                .build());

        syncManager.syncValue("scroll", new IntSyncValue(() -> scrollValue, value -> scrollValue = value));
    }

    @Override
    public ModularPanel build(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        ModularPanel panel = super.build(guiData, syncManager, uiSettings);
        syncManager.registerSyncedAction("refresh_dynamic", Side.SERVER, $ -> {
            DynamicSyncedWidget<?> dynamic = WidgetTree.findFirst(subPanel, DynamicSyncedWidget.class, $$ -> true);
            if (dynamic == null) return;
            DynamicSyncHandler dynamicHandler = (DynamicSyncHandler) dynamic.getSyncHandler();
            if (!dynamicHandler.isValid()) return;
            dynamicHandler.notifyUpdate($$ -> {});
        });
        return panel;
    }

    @Override
    protected Flow createRightPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        IPanelHandler rulesPopup = syncManager.panel("popup", (m, h) -> createRuleManagerPanel(syncManager), true);
        return super.createRightPanelGapRow(parent, syncManager)
            .child(new ButtonWidget<>().onMousePressed(mouseButton -> {
                if (!rulesPopup.isPanelOpen()) {
                    rulesPopup.openPanel();

                    syncManager.callSyncedAction("refresh_dynamic", $ -> {});
                } else {
                    rulesPopup.closePanel();
                }
                return true;
            })
                .background(GTGuiTextures.BUTTON_STANDARD, GuiTextures.GEAR)
                .disableHoverBackground()
                .tooltip(tooltip -> tooltip.add("Open Rules manager"))
                .size(18));
    }

    public ModularPanel createRuleManagerPanel(PanelSyncManager syncManager) {
        ModularPanel ui = subPanel = new ModularPanel("gt:splitter:rules_manager")
            .child(ButtonWidget.panelCloseButton());
        var rulesSyncer = (GenericListSyncHandler<SplitterRule>) syncManager.findSyncHandler("rules");

        final DynamicSyncHandler rulesHandler = new DynamicSyncHandler()
            .widgetProvider((manager, $) -> createRuleManagerList(rulesSyncer, manager));

        // spotless:off
        return ui
            .size(200, 170)
            .child(new Column()
                .child(new ButtonWidget<>()
                    .onMousePressed(mouseButton -> {
                        multiblock.rules.add(new SplitterRule());
                        rulesSyncer.notifyUpdate();
                        syncManager.callSyncedAction("refresh_dynamic", $ -> {});
                        return true;
                    })
                    .size(18)
                    .overlay(GuiTextures.ADD)
                    .tooltip(tooltip -> tooltip.add("Add new Rule")))
                .child(new DynamicSyncedWidget<>()
                    .syncHandler(rulesHandler)
                    .coverChildren())
                .childPadding(8)
                .coverChildren());
        // spotless:on
    }

    public IWidget createRuleManagerList(GenericListSyncHandler<SplitterRule> rulesSyncer,
        PanelSyncManager syncManager) {
        return new WorkaroundListWidget()
            .children(multiblock.rules.size(), i -> createRuleManagerRow(rulesSyncer, syncManager, i))
            .childSeparator(IIcon.EMPTY_2PX)
            .size(200, 138);
    }

    public IWidget createRuleManagerRow(GenericListSyncHandler<SplitterRule> rulesSyncer, PanelSyncManager syncManager,
        int index) {
        IWidget inputColorGrid = createColorGrid(rulesSyncer, index, true);
        IWidget redstoneSelector = createRedstoneSelector(rulesSyncer, index);
        IWidget itemFilter = createItemFilter(syncManager, rulesSyncer, index);
        IWidget outputColorGrid = createColorGrid(rulesSyncer, index, false);

        // spotless:off
        return new ParentWidget<>()
            .child(new Column()
                .child(new Row()
                    .child(createSelectorButton(rulesSyncer, index, COLOR)
                        .tooltip(t -> t.add("Color"))
                        .overlay(new ItemDrawable(Items.dye, 10)))
                    .child(createSelectorButton(rulesSyncer, index, REDSTONE)
                        .tooltip(t -> t.add("Redstone"))
                        .overlay(new ItemDrawable(Items.redstone)))
                    .child(createSelectorButton(rulesSyncer, index, ITEM)
                        .tooltip(t -> t.add("Item"))
                        .overlay(IKey.str("I")))
                    .childPadding(3)
                    .coverChildren())
                .child(inputColorGrid)
                .child(redstoneSelector)
                .child(itemFilter)
                .collapseDisabledChild()
                .childPadding(2)
                .posRel(0.15F, 0.5F)
                .size(60, 59))
            .child(new ParentWidget<>()
                .child(new ButtonWidget<>()
                    .onMousePressed(a -> {
                        multiblock.rules.remove(index);
                        rulesSyncer.notifyUpdate();
                        syncManager
                            .getModularSyncManager()
                            .getMainPSM()
                            .callSyncedAction("refresh_dynamic", $ -> {});
                        return true;
                    })
                    .overlay(GTGuiTextures.OVERLAY_BUTTON_CROSS)
                    .tooltip(t -> t.add("Remove Rule"))
                    .posRel(0.5F, 0.1F)
                    .size(8))
                .child(GTGuiTextures.PROGRESSBAR_ARROW_STANDARD.getSubArea(0F, 0F, 1F, 0.5F)
                    .asWidget()
                    .topRel(0.5F)
                    .size(20, 18))
                .center()
                .coverChildrenWidth()
                .heightRel(1F))
            .child(new Column()
                .child(outputColorGrid)
                .posRel(0.8F, 0.5F)
                .marginTop(23)
                .coverChildren())
            .background(GTGuiTextures.BACKGROUND_NANOCHIP_RULE_POPUP)
            .widthRel(1F)
            .height(70)
            .margin(4, 8, 4, 4);
        // spotless:on
    }

    private ToggleButton createSelectorButton(GenericListSyncHandler<SplitterRule> syncer, int i,
        SplitterRule.FilterType type) {
        SplitterRule rule = multiblock.rules.get(i);

        // spotless:off
        return new ToggleButton()
            .value(new BoolValue.Dynamic(
                () -> rule.enabledWidget == type,
                bool -> {
                    if (bool) {
                        rule.enabledWidget = type;
                        syncer.notifyUpdate();
                    }
                }))
            .size(16);
        // spotless:on
    }

    private IWidget createColorGrid(GenericListSyncHandler<SplitterRule> syncer, int index, boolean input) {
        SplitterRule rule = multiblock.rules.get(index);

        return new ColorGridWidget().onButtonToggled(selected -> {
            if (input) {
                rule.inputColors = selected;
            } else rule.outputColors = selected;
            syncer.notifyUpdate();
        })
            .setInitialSelected(input ? rule.inputColors : rule.outputColors)
            .build()
            .setEnabledIf(f -> !input || rule.enabledWidget == COLOR);
    }

    private IWidget createRedstoneSelector(GenericListSyncHandler<SplitterRule> syncer, int index) {
        SplitterRule rule = multiblock.rules.get(index);

        // spotless:off
        return new Column()
            .child(IKey.str("Channel").asWidget())
            .child(new TextFieldWidget()
                .value(new IntValue.Dynamic(
                    () -> rule.redstoneMode.channel,
                    val -> {
                        rule.redstoneMode.channel = val;
                        syncer.notifyUpdate();
                    }))
                .setFormatAsInteger(true)
                .size(52, 12))
            .child(IKey.str("Strength").asWidget())
            .child(new TextFieldWidget()
                .value(new IntValue.Dynamic(
                    () -> rule.redstoneMode.level,
                    val -> {
                        rule.redstoneMode.level = val;
                        syncer.notifyUpdate();
                    }))
                .setNumbers(0, 15)
                .setFormatAsInteger(true)
                .size(52, 12))
            .setEnabledIf(f -> rule.enabledWidget == REDSTONE)
            .coverChildren();
        // spotless:on
    }

    private IWidget createItemFilter(PanelSyncManager syncManager, GenericListSyncHandler<SplitterRule> rulesSyncer,
        int index) {
        SplitterRule rule = multiblock.rules.get(index);

        return SlotGroupWidget.builder()
            .matrix("III", "III")
            .key(
                'I',
                i -> new PhantomItemSlot().syncHandler(
                    syncManager.getOrCreateSyncHandler(
                        "items",
                        (index * 6) + i,
                        PhantomItemSlotSH.class,
                        () -> new PhantomItemSlotSH(
                            new ModularSlot(rule.filterStacks, i).accessibility(true, false)
                                .changeListener(
                                    (newItem, onlyAmountChanged, client, init) -> {
                                        if (client) rulesSyncer.notifyUpdate();
                                    })))))
            .build()
            .setEnabledIf(f -> rule.enabledWidget == ITEM);
    }

    // A workaround class so that when the Splitter's Rules list changes and the rule manager's ListWidget is rebuilt
    // the scroll value can be preserved instead of resetting to 0
    private class WorkaroundListWidget extends ListWidget<IWidget, WorkaroundListWidget> {

        public WorkaroundListWidget() {
            super();
        }

        public boolean shouldScroll = true;

        @Override
        public void postResize() {
            super.postResize();
            // This check exists so that if the widget is resized again, such as when the panel is moved, we dont set
            // the scroll back to the original value
            if (shouldScroll) {
                getScrollData().scrollTo(getScrollArea(), scrollValue);
                shouldScroll = false;
            }
        }

        @Override
        public void dispose() {
            super.dispose();
            scrollValue = getScrollData().getScroll();
        }
    }
}
