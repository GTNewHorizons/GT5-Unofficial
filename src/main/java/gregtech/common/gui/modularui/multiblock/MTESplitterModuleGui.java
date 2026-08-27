package gregtech.common.gui.modularui.multiblock;

import static gregtech.common.tileentities.machines.multi.nanochip.util.SplitterRule.FilterType.COLOR;
import static gregtech.common.tileentities.machines.multi.nanochip.util.SplitterRule.FilterType.ITEM;
import static gregtech.common.tileentities.machines.multi.nanochip.util.SplitterRule.FilterType.REDSTONE;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.io.IOException;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;

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
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widget.WidgetTree;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.Dialog;
import com.cleanroommc.modularui.widgets.DynamicSyncedWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.slot.PhantomItemSlot;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;
import com.gtnewhorizon.gtnhlib.integration.mui2.ClientTextField;

import cpw.mods.fml.relauncher.Side;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.common.CommonButtons;
import gregtech.api.util.GTUtility;
import gregtech.common.modularui2.widget.ColorGridWidget;
import gregtech.common.tileentities.machines.multi.nanochip.modules.MTESplitterModule;
import gregtech.common.tileentities.machines.multi.nanochip.util.SplitterRule;
import gregtech.common.tileentities.machines.multi.nanochip.util.SplitterRule.SplitterRuleAdapter;

public class MTESplitterModuleGui extends MTENanochipAssemblyModuleBaseGui<MTESplitterModule> {

    private static final SplitterRuleAdapter RULE_ADAPTER = new SplitterRuleAdapter();

    int scrollValue;
    ModularPanel subPanel;

    public MTESplitterModuleGui(MTESplitterModule multiblock) {
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
                .build()
                .allowC2S());

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
        IPanelHandler rulesPopup = syncManager
            .syncedPanel("popup", true, (m, h) -> createRuleManagerPanel(syncManager));
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
                .backgroundOverlay(GuiTextures.GEAR)
                .disableHoverBackground()
                .tooltip(tooltip -> tooltip.add(IKey.lang("GT5U.tooltip.nac.hatch.splitter.rules_manager"))));
    }

    public ModularPanel createRuleManagerPanel(PanelSyncManager syncManager) {
        ModularPanel ui = subPanel = new ModularPanel("gt:splitter:rules_manager").child(
            CommonButtons.panelCloseButton()
                .background(GTGuiTextures.BUTTON_NANOCHIP));
        var rulesSyncer = (GenericListSyncHandler<SplitterRule>) syncManager.findSyncHandler("rules");

        final DynamicSyncHandler rulesHandler = new DynamicSyncHandler()
            .widgetProvider((manager, $) -> createRuleManagerList(rulesSyncer, manager));

        // spotless:off
        return ui
            .size(200, 170)
            .child(Flow.column()
                .child(new ButtonWidget<>()
                    .onMousePressed(mouseButton -> {
                        multiblock.rules.add(new SplitterRule());
                        rulesSyncer.notifyUpdate();
                        syncManager.callSyncedAction("refresh_dynamic", $ -> {});
                        return true;
                    })
                    .marginTop(4)
                    .overlay(GuiTextures.ADD)
                    .tooltip(tooltip -> tooltip.add(IKey.lang("GT5U.tooltip.nac.hatch.splitter.add_rule"))))
                .child(new DynamicSyncedWidget<>()
                    .syncHandler(rulesHandler)
                    .coverChildren())
                .childPadding(8)
                .coverChildren());
        // spotless:on
    }

    public IWidget createRuleManagerList(GenericListSyncHandler<SplitterRule> rulesSyncer,
        PanelSyncManager syncManager) {
        syncManager.registerSyncedAction("set_item_rename", Side.SERVER, buf -> {
            try {
                int ruleIdx = buf.readInt();
                int slotIdx = buf.readInt();
                String name = buf.readStringFromBuffer(50);

                SplitterRule rule = multiblock.rules.get(ruleIdx);
                ItemStack stack = rule.filterStacks.getStacks()
                    .get(slotIdx);
                if (stack == null) return;

                // Copy and insert to ensure proper code flow for things
                // like onContentsChanged and similar contracts
                stack = stack.copy();
                if (name.isEmpty()) {
                    stack.func_135074_t();
                } else {
                    stack.setStackDisplayName(name);
                }
                rule.filterStacks.setStackInSlot(slotIdx, stack);
            } catch (IOException ignored) {}
        });

        return new WorkaroundListWidget()
            .children(multiblock.rules.size(), i -> createRuleManagerRow(rulesSyncer, syncManager, i))
            .childSeparator(IIcon.EMPTY_2PX)
            .size(200, 138);
    }

    public IWidget createRuleManagerRow(GenericListSyncHandler<SplitterRule> rulesSyncer, PanelSyncManager syncManager,
        int index) {
        Widget<?> inputColorGrid = createColorGrid(rulesSyncer, index, true);
        Widget<?> redstoneSelector = createRedstoneSelector(rulesSyncer, index);
        Widget<?> itemFilter = createItemFilter(syncManager, rulesSyncer, index);
        Widget<?> outputColorGrid = createColorGrid(rulesSyncer, index, false);

        // spotless:off
        return new ParentWidget<>()
            .background(GTGuiTextures.BACKGROUND_NANOCHIP_RULE_POPUP)
            .widthRel(1F)
            .height(102)
            .margin(4, 8, 4, 4)

            .child(Flow.column()
                .widthRel(0.9F)
                .coverChildrenHeight()
                .marginTop(6)
                .leftRel(0.5F)
                .child(Flow.row()
                    .widthRel(1.0F)
                    .coverChildrenHeight()

                    // X button
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
                        .tooltip(t -> t.add(IKey.lang("GT5U.tooltip.nac.hatch.splitter.rule.remove")))
                        .leftRel(0.5F)
                        .size(8))

                    // Color/Redstone/Item selector buttons
                    .child(Flow.row()
                        .child(createSelectorButton(rulesSyncer, index, COLOR)
                            .tooltip(t -> t.add(IKey.lang("GT5U.tooltip.nac.hatch.splitter.rule.Color")))
                            .overlay(new ItemDrawable(Items.dye, 10)))
                        .child(createSelectorButton(rulesSyncer, index, REDSTONE)
                            .tooltip(t -> t.add(IKey.lang("GT5U.tooltip.nac.hatch.splitter.rule.redstone")))
                            .overlay(new ItemDrawable(Items.redstone)))
                        .child(createSelectorButton(rulesSyncer, index, ITEM)
                            .tooltip(t -> t.add(IKey.lang("GT5U.tooltip.nac.hatch.splitter.rule.item")))
                            .overlay(IKey.str("I")))
                        .childPadding(3)
                        .topRel(0.1F)
                        .marginLeft(8)
                        .coverChildren()))

                // Input -> Output section
                .child(Flow.row()
                    .height(72) // for a 4x4 of item slots
                    .widthRel(1.0F)
                    .leftRel(0.5F)
                    .marginTop(2)
                    // Input section
                    .child(new ParentWidget<>()
                        .size(72)
                        .child(inputColorGrid.posRel(0.5F, 0.5F))
                        .child(redstoneSelector.posRel(0.5F, 0.5F))
                        .child(itemFilter.posRel(0.5F, 0.5F)))
                    // Middle section (arrow)
                    .child(GTGuiTextures.PICTURE_NANOCHIP_ARROW
                        .asWidget()
                        .posRel(0.5F, 0.5F)
                        .size(20, 18))
                    // Output section
                    .child(new ParentWidget<>()
                        .size(72)
                        .leftRel(1.0F)
                        .anchorLeft(1.0F)
                        .child(outputColorGrid.posRel(0.5F, 0.5F)))));
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

    private Widget<?> createColorGrid(GenericListSyncHandler<SplitterRule> syncer, int index, boolean input) {
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

    private Widget<?> createRedstoneSelector(GenericListSyncHandler<SplitterRule> syncer, int index) {
        SplitterRule rule = multiblock.rules.get(index);

        // spotless:off
        return Flow.column()
            .child(IKey.lang("GT5U.gui.text.nac.splitter.channel").asWidget())
            .child(new TextFieldWidget()
                .value(new IntValue.Dynamic(
                    () -> rule.redstoneMode.channel,
                    val -> {
                        rule.redstoneMode.channel = val;
                        syncer.notifyUpdate();
                    }))
                .setFormatAsInteger(true)
                .size(52, 12))
            .child(IKey.lang("GT5U.gui.text.nac.splitter.strength").asWidget())
            .child(new TextFieldWidget()
                .value(new IntValue.Dynamic(
                    () -> rule.redstoneMode.level,
                    val -> {
                        rule.redstoneMode.level = val;
                        syncer.notifyUpdate();
                    }))
                .numbersInt(0, 15)
                .formatAsInteger(true)
                .size(52, 12))
            .setEnabledIf(f -> rule.enabledWidget == REDSTONE)
            .coverChildren();
        // spotless:on
    }

    private Widget<?> createItemFilter(PanelSyncManager syncManager, GenericListSyncHandler<SplitterRule> rulesSyncer,
        int index) {
        MutableInt ruleIdx = new MutableInt(0);
        MutableInt slotIdx = new MutableInt(0);

        IPanelHandler renamePopup = syncManager
            .syncedPanel("rename_popup", true, (m, h) -> createRenamePopup(syncManager, ruleIdx, slotIdx));

        SplitterRule rule = multiblock.rules.get(index);

        return SlotGroupWidget.builder()
            .matrix("IIII", "IIII", "IIII", "IIII")
            .key('I', i -> new PhantomItemSlot() {

                @Override
                public @NotNull Result onMousePressed(int mouseButton) {
                    // Middle-mouse click
                    if (mouseButton == 2 && rule.filterStacks.getStackInSlot(i) != null) {
                        ruleIdx.setValue(index);
                        slotIdx.setValue(i);
                        renamePopup.openPanel();
                        return Result.SUCCESS;
                    }
                    return super.onMousePressed(mouseButton);
                }
            }.syncHandler(
                syncManager.getOrCreateSyncHandler(
                    "items",
                    (index * 9) + i,
                    PhantomItemSlotSH.class,
                    () -> new PhantomItemSlotSH(
                        new ModularSlot(rule.filterStacks, i).accessibility(true, false)
                            .changeListener(
                                (newItem, onlyAmountChanged, client, init) -> {
                                    if (client) rulesSyncer.notifyUpdate();
                                }))))
                .addTooltipLine(
                    EnumChatFormatting.AQUA + translateToLocal("GT5U.gui.text.nac.splitter.custom_name_desc")))
            .build()
            .setEnabledIf(f -> rule.enabledWidget == ITEM);
    }

    private ModularPanel createRenamePopup(PanelSyncManager syncManager, MutableInt ruleIdx, MutableInt slotIdx) {
        ClientTextField textField = new ClientTextField() {

            @Override
            public void afterInit() {
                super.afterInit();
                // Set the initial text field text to the custom name, if it already has one
                SplitterRule rule = multiblock.rules.get(ruleIdx.getValue());
                ItemStack stack = rule.filterStacks.getStackInSlot(slotIdx.getValue());
                String customName = GTUtility.getStackCustomName(stack);
                this.handler.clear();
                if (customName != null && !customName.isEmpty()) {
                    setText(customName);
                    this.handler.markAll();
                }
            }
        };

        Dialog<String> dialog = new Dialog<>("rename_popup", name -> {
            if (name == null) return;
            SplitterRule rule = multiblock.rules.get(ruleIdx.getValue());
            ItemStack stack = rule.filterStacks.getStackInSlot(slotIdx.getValue());
            if (stack != null) {
                if (name.isEmpty()) {
                    stack.func_135074_t();
                } else {
                    stack.setStackDisplayName(name);
                }
            }
            syncManager.callSyncedAction("set_item_rename", buf -> {
                try {
                    buf.writeInt(ruleIdx.getValue());
                    buf.writeInt(slotIdx.getValue());
                    buf.writeStringToBuffer(name);
                } catch (IOException ignored) {}
            });
        }) {

            @Override
            public boolean onKeyPressed(char typedChar, int keyCode) {
                if (keyCode == Keyboard.KEY_RETURN) {
                    this.closeWith(textField.getText());
                    return true;
                }
                return super.onKeyPressed(typedChar, keyCode);
            }
        };

        dialog.setDisablePanelsBelow(true)
            .setDraggable(false)
            .background(GTGuiTextures.BACKGROUND_NANOCHIP_RULE_POPUP)
            .size(150, 62)
            .child(
                CommonButtons.panelCloseButton()
                    .background(GTGuiTextures.BUTTON_NANOCHIP))
            .child(
                Flow.column()
                    .marginTop(6)
                    .sizeRel(1.0F, 0.9F)
                    .child(
                        IKey.lang("GT5U.gui.text.nac.splitter.custom_name_header")
                            .asWidget())
                    .child(
                        textField.size(70, 14)
                            .background(GTGuiTextures.BUTTON_NANOCHIP_PRESSED)
                            .setFocusOnGuiOpen(true)
                            .horizontalCenter()
                            .marginTop(4))
                    .child(
                        Flow.row()
                            .coverChildrenHeight()
                            .width(92)
                            .anchorBottom(0.0F)
                            .bottomRel(0.0F)
                            .marginBottom(6)
                            .leftRel(0.5F)
                            .child(
                                new ButtonWidget<>().size(45, 16)
                                    .marginRight(1)
                                    .overlay(IKey.lang("GT5U.gui.text.nac.splitter.custom_name_confirm"))
                                    .onMousePressed(mouse -> {
                                        // Leave the text field text as the current name for next time
                                        dialog.closeWith(textField.getText());
                                        return true;
                                    }))
                            .child(
                                new ButtonWidget<>().size(45, 16)
                                    .leftRel(1.0F)
                                    .anchorLeft(1.0F)
                                    .marginLeft(1)
                                    .overlay(IKey.lang("GT5U.gui.text.nac.splitter.custom_name_cancel"))
                                    .onMousePressed(mouse -> {
                                        // Clear the text field text since there is no longer a custom name
                                        textField.setText("");
                                        dialog.closeWith(null);
                                        return true;
                                    }))));

        return dialog;
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
