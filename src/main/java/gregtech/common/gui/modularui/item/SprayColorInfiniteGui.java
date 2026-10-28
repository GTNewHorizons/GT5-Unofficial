package gregtech.common.gui.modularui.item;

import static gregtech.common.items.behaviors.BehaviourSprayColorInfinite.COLOR_SELECTIONS;
import static gregtech.common.items.behaviors.BehaviourSprayColorInfinite.CUSTOM_FLAG;
import static gregtech.common.items.behaviors.BehaviourSprayColorInfinite.MAX_FAVORITES;
import static gregtech.common.items.behaviors.BehaviourSprayColorInfinite.REMOVE_COLOR;
import static gregtech.common.items.behaviors.BehaviourSprayColorInfinite.formatHex;
import static gregtech.common.items.behaviors.BehaviourSprayColorInfinite.isCustomSelection;
import static gregtech.common.items.behaviors.BehaviourSprayColorInfinite.sendPacket;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.MCHelper;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.Rectangle;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.StringValue;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ColorPickerDialog;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.enums.Dyes;
import gregtech.api.items.MetaGeneratedItem;
import gregtech.api.modularui2.GTGuis;
import gregtech.api.net.GTPacketInfiniteSpraycan;
import gregtech.common.items.behaviors.BehaviourSprayColorInfinite;
import gregtech.common.modularui2.factory.SelectItemGuiBuilder;

public class SprayColorInfiniteGui {

    private static final int PANEL_WIDTH = 176;
    private static final int DYE_GRID_HEIGHT = 89;
    private static final int SWATCH_SIZE = 16;

    private final ItemStack itemStack;
    private final List<Integer> favorites;
    private String hexInput;

    public SprayColorInfiniteGui(ItemStack itemStack) {
        this.itemStack = itemStack;

        if (!(itemStack.getItem() instanceof MetaGeneratedItem)) {
            throw new RuntimeException("Tried to open the infinite spray can GUI with no can in main hand or offhand");
        }

        favorites = BehaviourSprayColorInfinite.getFavorites(itemStack);
        final int customColor = BehaviourSprayColorInfinite.getCustomColor(itemStack);
        hexInput = customColor == -1 ? "" : formatHex(customColor);
    }

    public ModularPanel build() {
        final ModularPanel panel = new SelectItemGuiBuilder(GTGuis.createPopUpPanel("infinite_spray"), COLOR_SELECTIONS)
            .setHeaderItem(itemStack)
            .setTitle(IKey.lang("gt.behaviour.paintspray.infinite.gui.header"))
            .setSelected(getColor(itemStack))
            .setCurrentItemWidgetCustomizer(
                widget -> widget.tooltipBuilder(
                    tooltip -> tooltip.clearText()
                        .add(getTooltip(BehaviourSprayColorInfinite.getSelection(itemStack)))))
            .setChoiceWidgetCustomizer((index, widget) -> {
                widget.tooltipBuilder(
                    tooltip -> tooltip.clearText()
                        .add(getTooltip(index))
                        .newLine()
                        .add(StatCollector.translateToLocal("gt.behaviour.paintspray.infinite.gui.favorite_hint")));
                widget.onMousePressed(mouseButton -> {
                    if (mouseButton == 1) {
                        toggleFavorite(index);
                        return true;
                    }
                    sendPacket(GTPacketInfiniteSpraycan.Action.SET_COLOR, index);
                    MCHelper.closeScreen();
                    return true;
                });
            })
            .build();

        panel.size(PANEL_WIDTH, DYE_GRID_HEIGHT + 66);
        addHexRow(panel, DYE_GRID_HEIGHT);
        addFavoritesRow(panel, DYE_GRID_HEIGHT + 20);
        panel.child(
            IKey.dynamic(this::getConnectsAsText)
                .asWidget()
                .pos(7, DYE_GRID_HEIGHT + 52)
                .height(10));

        return panel;
    }

    private void addHexRow(ModularPanel panel, int y) {
        panel.child(
            IKey.lang("gt.behaviour.paintspray.infinite.gui.hex")
                .asWidget()
                .pos(7, y + 4)
                .height(10));
        panel.child(
            new TextFieldWidget().setValidator(raw -> raw.startsWith("#") || raw.isEmpty() ? raw : "#" + raw)
                .value(new StringValue.Dynamic(() -> hexInput, value -> hexInput = value))
                .autoUpdateOnChange(true)
                .pos(32, y)
                .size(54, 16));
        final IPanelHandler colorPicker = IPanelHandler.simple(
            panel,
            (parent, player) -> new ColorPickerDialog(
                color -> hexInput = formatHex(color),
                Color.withAlpha(getPickerColor(), 255),
                false),
            true);
        final DynamicDrawable preview = new DynamicDrawable(
            () -> new Rectangle().color(Color.withAlpha(getPickerColor(), 255)));
        panel.child(
            new ButtonWidget<>().overlay(preview)
                .hoverOverlay(preview)
                .addTooltipLine(StatCollector.translateToLocal("gt.behaviour.paintspray.infinite.gui.picker.tooltip"))
                .onMousePressed(mouseButton -> {
                    colorPicker.deleteCachedPanel();
                    colorPicker.openPanel();
                    return true;
                })
                .pos(89, y)
                .size(16, 16));
        panel.child(
            new ButtonWidget<>().overlay(IKey.lang("gt.behaviour.paintspray.infinite.gui.apply"))
                .addTooltipLine(StatCollector.translateToLocal("gt.behaviour.paintspray.infinite.gui.apply.tooltip"))
                .onMousePressed(mouseButton -> {
                    final int rgb = parseHex(hexInput);
                    if (rgb == -1) return false;
                    sendPacket(GTPacketInfiniteSpraycan.Action.SET_COLOR, CUSTOM_FLAG | rgb);
                    MCHelper.closeScreen();
                    return true;
                })
                .pos(108, y)
                .size(32, 16));
        panel.child(
            new ButtonWidget<>().overlay(IKey.lang("gt.behaviour.paintspray.infinite.gui.save"))
                .addTooltipLine(StatCollector.translateToLocal("gt.behaviour.paintspray.infinite.gui.save.tooltip"))
                .onMousePressed(mouseButton -> {
                    final int rgb = parseHex(hexInput);
                    toggleFavorite(rgb == -1 ? BehaviourSprayColorInfinite.getSelection(itemStack) : CUSTOM_FLAG | rgb);
                    return true;
                })
                .pos(142, y)
                .size(27, 16));
    }

    private void addFavoritesRow(ModularPanel panel, int y) {
        panel.child(
            IKey.lang("gt.behaviour.paintspray.infinite.gui.favorites")
                .asWidget()
                .pos(7, y)
                .height(10));
        for (int i = 0; i < MAX_FAVORITES; i++) {
            final int slot = i;
            panel.child(
                new ButtonWidget<>()
                    .background(
                        new DynamicDrawable(
                            () -> slot < favorites.size()
                                ? new Rectangle().color(Color.withAlpha(getDisplayColor(favorites.get(slot)), 255))
                                : new Rectangle().color(Color.withAlpha(0x555555, 120))))
                    .tooltipDynamic(tooltip -> {
                        if (slot < favorites.size()) {
                            tooltip.addLine(getTooltip(favorites.get(slot)))
                                .addLine(
                                    StatCollector
                                        .translateToLocal("gt.behaviour.paintspray.infinite.gui.favorite_use"));
                        }
                    })
                    .onMousePressed(mouseButton -> {
                        if (slot >= favorites.size()) return false;
                        final int selection = favorites.get(slot);
                        if (mouseButton == 1) {
                            toggleFavorite(selection);
                            return true;
                        }
                        sendPacket(GTPacketInfiniteSpraycan.Action.SET_COLOR, selection);
                        MCHelper.closeScreen();
                        return true;
                    })
                    .pos(8 + i * (SWATCH_SIZE + 2), y + 12)
                    .size(SWATCH_SIZE, SWATCH_SIZE));
        }
    }

    private void toggleFavorite(int selection) {
        if (!favorites.remove(Integer.valueOf(selection))) {
            if (favorites.size() >= MAX_FAVORITES) {
                favorites.remove(0);
            }
            favorites.add(selection);
        }
        sendPacket(GTPacketInfiniteSpraycan.Action.TOGGLE_FAVORITE, selection);
    }

    private String getConnectsAsText() {
        final int rgb = parseHex(hexInput);
        if (rgb == -1) return "";
        return StatCollector.translateToLocalFormatted(
            "gt.behaviour.paintspray.infinite.gui.connects_as",
            Dyes.getNearest(rgb)
                .getLocalizedDyeName());
    }

    public byte getColor(ItemStack sprayCan) {
        final int selection = BehaviourSprayColorInfinite.getSelection(sprayCan);
        if (isCustomSelection(selection)) {
            return (byte) Dyes.getNearest(selection & 0xFFFFFF).mIndex;
        }
        return (byte) selection;
    }

    public String getTooltip(int selection) {
        if (isCustomSelection(selection)) {
            return StatCollector.translateToLocalFormatted(
                "gt.behaviour.paintspray.infinite.gui.custom",
                formatHex(selection),
                Dyes.getNearest(selection & 0xFFFFFF)
                    .getLocalizedDyeName());
        }
        return selection == REMOVE_COLOR
            ? StatCollector.translateToLocal("gt.behaviour.paintspray.infinite.gui.solvent")
            : Dyes.get(selection).mName;
    }

    private int getPickerColor() {
        final int rgb = parseHex(hexInput);
        return rgb != -1 ? rgb : getDisplayColor(BehaviourSprayColorInfinite.getSelection(itemStack));
    }

    private static int getDisplayColor(int selection) {
        if (isCustomSelection(selection)) return selection & 0xFFFFFF;
        if (selection == REMOVE_COLOR) return 0xE0E0E0;
        return Dyes.get(selection)
            .toInt();
    }

    public static int parseHex(String text) {
        if (text == null) return -1;
        String hex = text.trim();
        if (hex.startsWith("#")) hex = hex.substring(1);
        if (hex.length() == 3) {
            hex = "" + hex.charAt(0) + hex.charAt(0) + hex.charAt(1) + hex.charAt(1) + hex.charAt(2) + hex.charAt(2);
        }
        if (hex.length() != 6) return -1;
        try {
            return Integer.parseInt(hex, 16);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
