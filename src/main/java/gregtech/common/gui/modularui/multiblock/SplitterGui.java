package gregtech.common.gui.modularui.multiblock;

import static gregtech.common.tileentities.machines.multi.nanochip.util.SplitterRule.FilterType.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.network.PacketBuffer;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IIcon;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.serialization.IByteBufAdapter;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.DynamicSyncHandler;
import com.cleanroommc.modularui.value.sync.GenericSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.PhantomItemSlotSH;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widget.EmptyWidget;
import com.cleanroommc.modularui.widget.ParentWidget;
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

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTLog;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.modularui2.widget.ColorGridWidget;
import gregtech.common.tileentities.machines.multi.nanochip.modules.MTESplitterModule;
import gregtech.common.tileentities.machines.multi.nanochip.util.SplitterRule;

public class SplitterGui extends MTEMultiBlockBaseGui<MTESplitterModule> {

    int scrollValue;

    public SplitterGui(MTESplitterModule multiblock) {
        super(multiblock);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.syncValue(
            "rules",
            0,
            new GenericSyncValue<>(
                () -> multiblock.rules,
                map -> { multiblock.rules = map; },
                new SplitterRuleListAdapter()));
        // syncManager.syncValue("rules", 0, GenericListSyncHandler.<SplitterRule>builder()
        // .getter(() -> multiblock.rules)
        // .setter(list -> multiblock.rules = list)
        // .serializer((packet, rule) -> packet.writeNBTTagCompoundToBuffer(rule.saveToNBT()))
        // .deserializer(packet -> SplitterRule.loadFromNBT(packet.readNBTTagCompoundFromBuffer()))
        // .build());
        // syncManager.syncValue("rules", 0, new GenericListSyncHandler<>(
        // () -> multiblock.rules,
        // list -> multiblock.rules = list,
        // packet -> SplitterRule.loadFromNBT(packet.readNBTTagCompoundFromBuffer()),
        // (packet, rule) -> packet.writeNBTTagCompoundToBuffer(rule.saveToNBT()),
        // SplitterRule::equals,
        // null
        // ));
        syncManager.syncValue("scroll", new IntSyncValue(() -> scrollValue, value -> scrollValue = value));
    }

    @Override
    protected Flow createRightPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        IPanelHandler rulesPopup = syncManager.panel("popup", (m, h) -> createRuleManagerPanel(syncManager), true);
        return super.createRightPanelGapRow(parent, syncManager)
            .child(new ButtonWidget<>().onMousePressed(mouseButton -> {
                if (!rulesPopup.isPanelOpen()) {
                    rulesPopup.openPanel();
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
        ModularPanel ui = new ModularPanel("gt:splitter:rules_manager").child(ButtonWidget.panelCloseButton());
        GenericSyncValue<List<SplitterRule>> rulesSyncer = (GenericSyncValue<List<SplitterRule>>) syncManager
            .getSyncHandlerFromMapKey("rules:0");
        multiblock.phantomHolder.setSize(
            rulesSyncer.getValue()
                .size() * 6);
        DynamicSyncHandler rulesHandler = new DynamicSyncHandler().widgetProvider((manager, packet) -> {
            if (packet == null) return new EmptyWidget();
            return createRuleManagerList(SplitterRuleListAdapter.deserializePacket(packet), manager);
        });
        rulesSyncer.setChangeListener(
            () -> rulesHandler
                .notifyUpdate(packet -> { SplitterRuleListAdapter.serializeList(packet, rulesSyncer.getValue()); }));
        // spotless:off
        // TODO: figure out a different solution to this because this sends an error to the log on the client because Reasons
        // TODO: and by this. lets just say that the dynamic widget doesnt start out with a child so we need to trigger it or give it an initial child
        rulesHandler.notifyUpdate(packet -> SplitterRuleListAdapter.serializeList(packet, rulesSyncer.getValue()));
        return ui
            .child(new Column()
                .child(new ButtonWidget<>().onMousePressed(mouseButton -> {
                        rulesSyncer.getValue().add(new SplitterRule(new ArrayList<>(), new ArrayList<>(), new SplitterRule.RedstoneMode(0, 15), null, COLOR));
                        rulesSyncer.setValue(rulesSyncer.getValue());
                        return true;
                    })
                    .size(18)
                    .overlay(GuiTextures.ADD)
                    .tooltip(tooltip -> tooltip.add("Add new Rule")))
                .child(new DynamicSyncedWidget<>()
                    .syncHandler(rulesHandler)
                    .coverChildren())
                .childPadding(8)
                .coverChildren())
            .coverChildren();
        // spotless:on
    }

    public IWidget createRuleManagerList(List<SplitterRule> rules, PanelSyncManager syncManager) {
        return new WorkaroundListWidget().children(rules.size(), i -> createRuleManagerRow(syncManager, i))
            .childSeparator(IIcon.EMPTY_2PX)
            .size(200, 138);
    }

    public IWidget createRuleManagerRow(PanelSyncManager syncManager, int index) {
        GenericSyncValue<List<SplitterRule>> rulesSyncer = (GenericSyncValue<List<SplitterRule>>) syncManager
            .getModularSyncManager()
            .getMainPSM()
            .getSyncHandlerFromMapKey("rules:0");
        List<SplitterRule> rules = rulesSyncer.getValue();
        SplitterRule rule = rules.get(index);

        // spotless:off
        IWidget inputColorGrid =  createColorGrid(rulesSyncer, index, true);
        IWidget redstoneSelector = createRedstoneSelector(syncManager, rulesSyncer, index);
        IWidget itemFilter = createItemFilter(syncManager, rulesSyncer, index);
        IWidget outputColorGrid = createColorGrid(rulesSyncer, index, false);

        return new ParentWidget<>()
            .child(new Column()
                .child(new Row()
                    .child(createSelectorButton(syncManager, rulesSyncer, index, COLOR)
                        .tooltip(t -> t.add("Color"))
                        .overlay(new ItemDrawable(Items.dye, 10)))
                    .child(createSelectorButton(syncManager, rulesSyncer, index, REDSTONE)
                        .tooltip(t -> t.add("Redstone"))
                        .overlay(new ItemDrawable(Items.redstone)))
                    .child(createSelectorButton(syncManager, rulesSyncer, index, ITEM)
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
                    .onMousePressed(a -> { rules.remove(index); rulesSyncer.setValue(rules); return true; })
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
            .background(GTGuiTextures.BACKGROUND_POPUP_STANDARD)
            .widthRel(1F)
            .height(70)
            .margin(4, 8, 4, 4);
        // spotless:on
    }

    private ToggleButton createSelectorButton(PanelSyncManager syncManager, GenericSyncValue<List<SplitterRule>> syncer,
        int i, SplitterRule.FilterType type) {
        List<SplitterRule> rules = syncer.getValue();
        SplitterRule rule = rules.get(i);
        // spotless:off
        return new ToggleButton()
            .value(syncManager.getOrCreateSyncHandler(type.name(), i, BooleanSyncValue.class,
                () -> new BooleanSyncValue(() -> rule.enabledWidget == type, bool -> {
                    if (bool) {
                        syncer.getValue().get(i).enabledWidget = type;
                        syncer.setValue(syncer.getValue());
                    }
                })))
            .size(16);
        // spotless:on
    }

    private IWidget createColorGrid(GenericSyncValue<List<SplitterRule>> syncer, int index, boolean input) {
        List<SplitterRule> rules = syncer.getValue();
        SplitterRule rule = rules.get(index);
        return new ColorGridWidget().onButtonToggled(selected -> {
            if (input) {
                rule.inputColors = selected;
            } else rule.outputColors = selected;
            rules.set(index, rule);
            syncer.setValue(rules);
        })
            .setInitialSelected(input ? rule.inputColors : rule.outputColors)
            .build()
            .setEnabledIf(f -> !input || rule.enabledWidget == COLOR);
    }

    private IWidget createRedstoneSelector(PanelSyncManager syncManager, GenericSyncValue<List<SplitterRule>> syncer,
        int index) {
        List<SplitterRule> rules = syncer.getValue();
        SplitterRule rule = rules.get(index);
        // spotless:off
        return new Column()
            .child(IKey.str("Channel").asWidget())
            .child(new TextFieldWidget()
                .value(syncManager.getOrCreateSyncHandler("channel", index, StringSyncValue.class,
                    () -> new StringSyncValue(() -> Integer.toString(rule.redstoneMode.channel), str -> {
                        rules.get(index).redstoneMode.channel = Integer.parseInt(str);
                        syncer.setValue(rules);
                    })))
                .setFormatAsInteger(true)
                .size(52, 12))
            .child(IKey.str("Strength").asWidget())
            .child(new TextFieldWidget()
                .value(syncManager.getOrCreateSyncHandler("level", index, StringSyncValue.class,
                    () -> new StringSyncValue(() -> Integer.toString(rule.redstoneMode.level), str -> {
                        rules.get(index).redstoneMode.level = Integer.parseInt(str);
                        syncer.setValue(rules);
                    })))
                .setNumbers(0, 15)
                .setFormatAsInteger(true)
                .size(52, 12))
            .setEnabledIf(f -> rule.enabledWidget == REDSTONE)
            .coverChildren();
        // spotless:on
    }

    private IWidget createItemFilter(PanelSyncManager syncManager, GenericSyncValue<List<SplitterRule>> syncer,
        int index) {
        List<SplitterRule> rules = syncer.getValue();
        SplitterRule rule = rules.get(index);
        // TODO: make it work
        return SlotGroupWidget.builder()
            .matrix("III", "III")
            .key('I', i -> {
                int realIndex = (index * 6) + i;
                return new PhantomItemSlot().syncHandler(
                    syncManager.getOrCreateSyncHandler(
                        "items",
                        realIndex,
                        PhantomItemSlotSH.class,
                        () -> new PhantomItemSlotSH(
                            new ModularSlot(multiblock.phantomHolder, realIndex)
                                .changeListener((newItem, onlyAmountChanged, client, init) -> {

                                })
                                .accessibility(true, false))));
            })
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

    private static class SplitterRuleListAdapter implements IByteBufAdapter<List<SplitterRule>> {

        @Override
        public List<SplitterRule> deserialize(PacketBuffer buffer) {
            return deserializePacket(buffer);
        }

        public static List<SplitterRule> deserializePacket(PacketBuffer buffer) {
            List<SplitterRule> list = new ArrayList<>();
            int size = buffer.readInt();
            for (int i = 0; i < size; i++) {
                try {
                    list.add(SplitterRule.loadFromNBT(buffer.readNBTTagCompoundFromBuffer()));
                } catch (IOException e) {
                    GTLog.err.println(e.getCause());
                }
            }
            return list;
        }

        @Override
        public void serialize(PacketBuffer buffer, List<SplitterRule> list) {
            serializeList(buffer, list);
        }

        public static void serializeList(PacketBuffer buffer, List<SplitterRule> list) {
            buffer.writeInt(list.size());
            for (SplitterRule rule : list) {
                try {
                    buffer.writeNBTTagCompoundToBuffer(rule.saveToNBT());
                } catch (IOException e) {
                    GTLog.err.println(e.getCause());
                }
            }
        }

        @Override
        public boolean areEqual(List<SplitterRule> t1, List<SplitterRule> t2) {
            return t1.equals(t2);
        }
    }

}
