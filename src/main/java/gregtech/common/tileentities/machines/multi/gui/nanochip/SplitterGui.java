package gregtech.common.tileentities.machines.multi.gui.nanochip;

import static gregtech.api.modularui2.GTGuis.createPopUpPanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.cleanroommc.modularui.screen.UISettings;
import net.minecraft.network.PacketBuffer;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IIcon;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.serialization.IByteBufAdapter;
import com.cleanroommc.modularui.value.sync.GenericSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widget.WidgetTree;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.google.common.collect.ImmutableList;

import gregtech.api.enums.Dyes;
import gregtech.api.metatileentity.implementations.gui.MTEMultiBlockBaseGui;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.modularui2.widget.ColorGridWidget;
import gregtech.common.tileentities.machines.multi.nanochip.modules.Splitter;

public class SplitterGui extends MTEMultiBlockBaseGui {

    private final Splitter base;

    public SplitterGui(Splitter base) {
        super(base);
        this.base = base;
    }

    public ModularPanel build(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        ModularPanel ui = super.build(data, syncManager, uiSettings);
        IPanelHandler popupPanel = syncManager.panel("popup", (m, h) -> createRuleManagerPanel(syncManager), true);
        GenericSyncValue<Map<Integer, Splitter.ColorRule>> listSyncer = new GenericSyncValue<>(
            () -> base.colorMap,
            map -> { base.colorMap = map; },
            new ColorMapAdapter());
        syncManager.syncValue("rules", 0, listSyncer);

        return ui.child(new ButtonWidget<>().onMousePressed(mouseButton -> {
            popupPanel.openPanel();
            return popupPanel.isPanelOpen();
        })
            .background(GTGuiTextures.BUTTON_STANDARD)
            .tooltip(tooltip -> tooltip.add("Add Rule"))
            .pos(153, 5)
            .size(18, 18));
    }

    public ModularPanel createRuleManagerPanel(PanelSyncManager syncManager) {
        ModularPanel ui = createPopUpPanel("gt:splitter:rules_manager", false, false);

        ListWidget<IWidget, ?> list = new ListWidget<>();
        list.childSeparator(IIcon.EMPTY_2PX);
        list.size(168, 138);
        list.pos(4, 21);

        // Add existing rules
        for (Map.Entry<Integer, Splitter.ColorRule> entry : base.colorMap.entrySet()) {
            int id = entry.getKey();
            Splitter.ColorRule rule = entry.getValue();
            if (rule == null) continue;
            Byte input = rule.getInputColor();
            List<Byte> outputs = rule.getOutputColors();
            // spotless:off
            list.child(createColorManager(syncManager, Stream.of(input).collect(Collectors.toList()), outputs, id));
            // spotless:on
        }

        return ui.child(list)
            .child(new ButtonWidget<>().onMousePressed(mouseButton -> {
                list.child(createColorManager(syncManager, null, null, null));
                WidgetTree.resize(ui);
                return true;
            })
                .pos(80, 4)
                .size(16, 16)
                .overlay(GuiTextures.ADD)
                .tooltip(tooltip -> tooltip.add("Add new Rule")))
            .posRel(0.75F, 0.5F);
    }

    public IWidget createColorManager(PanelSyncManager syncManager, List<Byte> inputSelected, List<Byte> outputSelected,
        Integer indexOverride) {
        ColorGridWidget inputGrid = new ColorGridWidget();
        ColorGridWidget outputGrid = new ColorGridWidget();
        ColorGridSelector selector = new ColorGridSelector(syncManager, indexOverride);

        return selector
            // Arrow icon
            .child(
                GTGuiTextures.PROGRESSBAR_ARROW_STANDARD.getSubArea(0F, 0F, 1F, 0.5F)
                    .asWidget()
                    .size(20, 18)
                    .posRel(0.5F, 0.5F))
            .setInputGrid(
                (ColorGridWidget) inputGrid.setInitialSelected(inputSelected)
                    .setMaxSelected(1)
                    .build()
                    .pos(5, 17))
            .setOutputGrid(
                (ColorGridWidget) outputGrid.setInitialSelected(outputSelected)
                    .setMaxSelected(16)
                    .build()
                    .pos(121, 17))
            // Input grid color display
            .child(
                IKey.dynamic(() -> inputGrid.getName(0))
                    .asWidget()
                    .scale(0.8F)
                    .alignment(Alignment.Center)
                    .size(42, 8)
                    .pos(4, 5))
            // Output grid color display
            .child(
                // spotless makes this look vile and disgusting and abominable and atrocious and yucky and horrid and
                // offensive to the eyes and nasty and foul and repugnant and abhorrent and deplorable and nauseating
                // and Dirty
                // spotless:off
                IKey.dynamic(() -> switch (outputGrid.getAmountSelected()) {
                        case 0: yield "None";
                        case 1: yield outputGrid.getName(0);
                        default: yield "[Hover]";
                    })
                    // spotless:on
                    .asWidget()
                    .tooltipAutoUpdate(true)
                    .tooltipBuilder(t -> getInfo(t, outputGrid))
                    .scale(0.8F)
                    .alignment(Alignment.Center)
                    .size(42, 8)
                    .pos(120, 5))
            // Delete button
            .child(
                new ButtonWidget<>().tooltip(t -> t.add("Delete"))
                    .onMousePressed(a -> {
                        selector.removeColorData();
                        WidgetTree.resize(selector.getParent());
                        return true;
                    })
                    .overlay(GTGuiTextures.OVERLAY_BUTTON_CROSS)
                    .pos(80, 5)
                    .size(8, 8))
            .size(166, 58)
            .background(GTGuiTextures.BACKGROUND_POPUP_STANDARD);
    }

    public void getInfo(RichTooltip t, ColorGridWidget grid) {
        List<Byte> selected = grid.getSelected();
        int amount = selected.size();
        if (amount < 2) return;
        t.pos(RichTooltip.Pos.ABOVE)
            .add("Currently selected:\n");
        for (int i = 0; i < amount; i++) {
            boolean shouldNewLine = (((i - 2) % 3) == 0);
            Dyes color = Dyes.get(selected.get(i));
            String name = color.getLocalizedDyeName();
            if (color == Dyes.dyeBlack) color = Dyes.dyeGray;
            t.add(color.formatting + name + IKey.RESET + (shouldNewLine ? "\n" : ", "));
        }
    }

    private class ColorGridSelector extends ParentWidget<ColorGridSelector> {

        int id;
        ColorGridWidget inputGrid;
        ColorGridWidget outputGrid;
        PanelSyncManager manager;
        GenericSyncValue<Map<Integer, Splitter.ColorRule>> colorMapSyncer;

        public ColorGridSelector(PanelSyncManager syncManager, Integer indexOverride) {
            super();
            manager = syncManager;
            colorMapSyncer = (GenericSyncValue<Map<Integer, Splitter.ColorRule>>) syncManager.getSyncHandler("rules:0");
            if (indexOverride == null) {
                while (base.colorMap.get(id) != null) {
                    id++;
                }
            } else id = indexOverride;
        }

        @Override
        public void onInit() {
            super.onInit();
            saveColorData();
        }

        public void removeSelector() {
            IWidget widget = getParent();
            if (!(widget instanceof ListWidget list)) return;
            list.getChildren()
                .remove(this);
        }

        public ColorGridSelector setInputGrid(ColorGridWidget widget) {
            inputGrid = widget.onButtonToggled(this::saveColorData);
            return this.child(inputGrid);
        }

        public ColorGridSelector setOutputGrid(ColorGridWidget widget) {
            outputGrid = widget.onButtonToggled(this::saveColorData);
            return this.child(outputGrid);
        }

        private void saveColorData() {
            if (manager.isClient()) {
                base.colorMap.put(id, thisAsRule());
                colorMapSyncer.setValue(base.colorMap);
            }
        }

        private void removeColorData() {
            if (manager.isClient()) {
                base.colorMap.remove(id);
                colorMapSyncer.setValue(base.colorMap);
            }
            removeSelector();
        }

        public Splitter.ColorRule thisAsRule() {
            List<Byte> input = inputGrid.getSelected();
            List<Byte> output = outputGrid.getSelected();
            if (input.isEmpty()) input = ImmutableList.of((byte) -1);
            if (output.isEmpty()) output = ImmutableList.of((byte) -1);
            return new Splitter.ColorRule(input.get(0), output);
        }
    }

    private static class ColorMapAdapter implements IByteBufAdapter<Map<Integer, Splitter.ColorRule>> {

        @Override
        public Map<Integer, Splitter.ColorRule> deserialize(PacketBuffer buffer) {
            Map<Integer, Splitter.ColorRule> list = new HashMap<>();
            int size = buffer.readInt();
            for (int i = 0; i < size; i++) {
                int id = buffer.readInt();
                Byte input = buffer.readByte();
                List<Byte> outputs = new ArrayList<>();
                int ruleCount = buffer.readInt();
                for (int j = 0; j < ruleCount; j++) {
                    outputs.add(buffer.readByte());
                }
                list.put(id, new Splitter.ColorRule(input, outputs));
            }
            return list;
        }

        @Override
        public void serialize(PacketBuffer buffer, Map<Integer, Splitter.ColorRule> map) {
            buffer.writeInt(map.size());
            for (Map.Entry<Integer, Splitter.ColorRule> entry : map.entrySet()) {
                Splitter.ColorRule rule = entry.getValue();
                Byte input = rule.getInputColor();
                List<Byte> outputs = rule.getOutputColors();
                buffer.writeInt(entry.getKey());
                buffer.writeByte(input);
                buffer.writeInt(outputs.size());
                for (Byte dye : outputs) {
                    buffer.writeByte(dye);
                }
            }
        }

        @Override
        public boolean areEqual(Map<Integer, Splitter.ColorRule> t1, Map<Integer, Splitter.ColorRule> t2) {
            return t1.equals(t2);
        }
    }

}
