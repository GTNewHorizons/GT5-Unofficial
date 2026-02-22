package gregtech.common.gui.modularui.multiblock.godforge.panel;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;

import org.apache.commons.lang3.mutable.MutableInt;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.Rectangle;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.IntValue;
import com.cleanroommc.modularui.value.StringValue;
import com.cleanroommc.modularui.value.sync.GenericSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.PagedWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.godforge.ForgeOfGodsGuiUtil;
import gregtech.common.gui.modularui.multiblock.godforge.data.ColorData;
import gregtech.common.gui.modularui.multiblock.godforge.data.StarColors;
import gregtech.common.gui.modularui.multiblock.godforge.sync.Panels;
import gregtech.common.gui.modularui.multiblock.godforge.sync.SyncActions;
import gregtech.common.gui.modularui.multiblock.godforge.sync.SyncHypervisor;
import gregtech.common.gui.modularui.multiblock.godforge.sync.SyncValues;
import tectech.thing.metaTileEntity.multi.godforge.color.ForgeOfGodsStarColor;
import tectech.thing.metaTileEntity.multi.godforge.color.StarColorSetting;
import tectech.thing.metaTileEntity.multi.godforge.color.StarColorStorage;

public class CustomStarColorPanel {

    protected static final int SIZE = 200;

    protected static final int RGB_PAGE_INDEX = 0;
    protected static final int HSV_PAGE_INDEX = 1;

    public static ModularPanel openPanel(SyncHypervisor hypervisor) {
        ModularPanel panel = hypervisor.getModularPanel(Panels.CUSTOM_STAR_COLOR);

        registerSyncValues(hypervisor);

        MutableInt editingIndex = new MutableInt(-1);
        ColorData colorData = new ColorData();

        panel.size(SIZE)
            .background(GTGuiTextures.BACKGROUND_GLOW_WHITE)
            .disableHoverBackground()
            .child(ForgeOfGodsGuiUtil.panelCloseButton())
            .onCloseAction(() -> {
                editingIndex.setValue(-1);
                colorData.reset();
            });

        // Title
        panel.child(
            IKey.lang("fog.cosmetics.starcolor")
                .style(EnumChatFormatting.GOLD)
                .alignment(Alignment.CENTER)
                .asWidget()
                .align(Alignment.TopCenter)
                .marginTop(9));

        Flow mainColumn = new Column().coverChildren()
            .marginTop(22)
            .alignX(0.5f);

        // Color rows
        PagedWidget.Controller controller = new PagedWidget.Controller();

        mainColumn.child(
            new PagedWidget<>().coverChildren()
                .marginBottom(5)
                .expanded()
                .controller(controller)
                .addPage(CustomStarColorSelector.createStarColorRGBPage(colorData))
                .addPage(CustomStarColorSelector.createStarColorHSVPage(colorData)));

        // Color preview
        mainColumn.child(CustomStarColorSelector.createColorPreviewRow(controller, colorData));

        // Add/Apply/Reset buttons
        mainColumn.child(createAddApplyResetRow(hypervisor, colorData, editingIndex));

        // Color list
        Flow colorListRow = new Row().size(184, 18)
            .marginBottom(4)
            .alignX(0.5f);

        for (int i = 0; i < ForgeOfGodsStarColor.MAX_COLORS; i++) {
            colorListRow.child(createColorButton(hypervisor, controller, colorData, i, editingIndex));
        }

        // Rate text field
        colorListRow.child(
            new TextFieldWidget().setFormatAsInteger(true)
                .setNumbers(1, 100)
                .setTextAlignment(Alignment.CENTER)
                .value(new IntValue.Dynamic(() -> {
                    ForgeOfGodsStarColor starColor = getClickedStarColor(hypervisor);
                    return starColor.getCycleSpeed();
                }, val -> {
                    ForgeOfGodsStarColor starColor = getClickedStarColor(hypervisor);
                    starColor.setCycleSpeed(val);
                }))
                .tooltip(t -> t.addLine(translateToLocal("fog.cosmetics.cyclespeed")))
                .tooltipShowUpTimer(TOOLTIP_DELAY)
                .size(21, 16)
                .marginLeft(1));
        mainColumn.child(colorListRow);

        // Custom name row
        mainColumn.child(createNameRow(hypervisor));

        // Export/Import/Delete/Save row
        mainColumn.child(createSaveDeleteRow(hypervisor));

        panel.child(mainColumn);
        return panel;
    }

    private static void registerSyncValues(SyncHypervisor hypervisor) {
        SyncValues.STAR_COLORS.registerFor(Panels.CUSTOM_STAR_COLOR, hypervisor);
        SyncValues.SELECTED_STAR_COLOR.registerFor(Panels.CUSTOM_STAR_COLOR, hypervisor);

        SyncActions.UPDATE_RENDERER.registerFor(Panels.CUSTOM_STAR_COLOR, hypervisor, hypervisor.getMultiblock());
    }

    private static ForgeOfGodsStarColor getClickedStarColor(SyncHypervisor hypervisor) {
        GenericSyncValue<ForgeOfGodsStarColor> starColorClickedSyncer = SyncValues.STAR_COLOR_CLICKED
            .lookupFrom(Panels.STAR_COSMETICS, hypervisor);
        return starColorClickedSyncer.getValue();
    }

    private static int getEditingColorIndex(SyncHypervisor hypervisor) {
        IntSyncValue editingIndexSyncer = SyncValues.STAR_COLOR_EDITING_INDEX
            .lookupFrom(Panels.STAR_COSMETICS, hypervisor);
        return editingIndexSyncer.getIntValue();
    }

    private static ParentWidget<?> createColorButton(SyncHypervisor hypervisor, PagedWidget.Controller rgbhsvController,
        ColorData colorData, int index, MutableInt editingIndex) {

        ParentWidget<?> parent = new ParentWidget<>().size(18);

        // Saved color
        parent.child(new DynamicDrawable(() -> {
            ForgeOfGodsStarColor newStarColor = getClickedStarColor(hypervisor);
            if (index < newStarColor.numColors()) {
                StarColorSetting color = newStarColor.getColor(index);
                return new Rectangle().color(Color.rgb(color.getColorR(), color.getColorG(), color.getColorB()));
            }
            return IDrawable.EMPTY;
        }).asWidget()
            .size(16)
            .margin(1));

        // Edit color button
        parent.child(
            new ButtonWidget<>().size(18)
                .background(GTGuiTextures.UNSELECTED_OPTION)
                .overlay(new DynamicDrawable(() -> {
                    if (index == editingIndex.intValue()) {
                        return GTGuiTextures.SLOT_OUTLINE_GREEN;
                    }
                    return IDrawable.EMPTY;
                }))
                .onMousePressed(d -> {
                    ForgeOfGodsStarColor newStarColor = getClickedStarColor(hypervisor);
                    if (d == 0) { // left click
                        // deselect if already selected
                        if (index == editingIndex.intValue()) {
                            editingIndex.setValue(-1);
                            return true;
                        }

                        // otherwise select if possible
                        if (index < newStarColor.numColors()) {
                            editingIndex.setValue(index);
                            colorData.updateFrom(newStarColor.getColor(index));
                        }
                    } else if (d == 1) { // right click
                        // remove color if possible and shift other colors down
                        newStarColor.removeColor(index);
                        if (index < newStarColor.numColors()) {
                            if (index == editingIndex.intValue()) {
                                // deselect if this index was selected
                                editingIndex.setValue(-1);
                            } else if (index > editingIndex.intValue()) {
                                // shift down the editing index if it was after this entry
                                editingIndex.setValue(editingIndex.intValue() - 1);
                            }
                        }
                    }
                    return true;
                })
                .clickSound(ForgeOfGodsGuiUtil.getButtonSound())
                .tooltipDynamic(t -> {
                    t.addLine(translateToLocal("fog.cosmetics.colorlist.tooltip.1"));
                    t.addLine(translateToLocal("fog.cosmetics.colorlist.tooltip.2"));

                    ForgeOfGodsStarColor newStarColor = getClickedStarColor(hypervisor);
                    if (index < newStarColor.numColors()) {
                        t.newLine(); // spacer
                        StarColorSetting color = newStarColor.getColor(index);
                        if (rgbhsvController.getActivePageIndex() == RGB_PAGE_INDEX) {
                            t.addLine(StarColors.RGB.RED.getTooltip(color.getColorR()));
                            t.addLine(StarColors.RGB.GREEN.getTooltip(color.getColorG()));
                            t.addLine(StarColors.RGB.BLUE.getTooltip(color.getColorB()));
                        } else {
                            int rgb = Color.rgb(color.getColorR(), color.getColorG(), color.getColorB());
                            t.addLine(StarColors.HSV.HUE.getTooltip(Color.getHue(rgb)));
                            t.addLine(StarColors.HSV.SATURATION.getTooltip(Color.getHSVSaturation(rgb)));
                            t.addLine(StarColors.HSV.VALUE.getTooltip(Color.getValue(rgb)));
                        }
                        t.addLine(StarColors.Extra.GAMMA.getTooltip(color.getGamma()));
                    }
                })
                .tooltipAutoUpdate(true)
                .tooltipShowUpTimer(TOOLTIP_DELAY));

        return parent;
    }

    private static Flow createAddApplyResetRow(SyncHypervisor hypervisor, ColorData colorData,
        MutableInt editingIndex) {
        Flow row = new Row().size(76, 15)
            .childPadding(2)
            .marginBottom(3)
            .alignX(0.5f);

        // Add/Apply button
        row.child(
            new ButtonWidget<>().size(37, 15)
                .onMousePressed(d -> {
                    ForgeOfGodsStarColor starColor = getClickedStarColor(hypervisor);
                    StarColorSetting setting = colorData.createSetting();
                    if (editingIndex.intValue() >= 0 && editingIndex.intValue() < starColor.numColors()) {
                        // insert into the list, we are editing an existing color
                        starColor.setColor(editingIndex.intValue(), setting);
                    } else {
                        // add a new color at the end of the list
                        starColor.addColor(setting);
                    }
                    return true;
                })
                .background(GTGuiTextures.BUTTON_OUTLINE_HOLLOW)
                .overlay(IKey.dynamic(() -> {
                    if (editingIndex.intValue() >= 0) {
                        return translateToLocal("fog.cosmetics.applycolor");
                    }
                    return translateToLocal("fog.cosmetics.addcolor");
                })
                    .alignment(Alignment.CENTER))
                .clickSound(ForgeOfGodsGuiUtil.getButtonSound())
                .tooltipDynamic(t -> {
                    if (editingIndex.intValue() >= 0) {
                        t.addLine(translateToLocal("fog.cosmetics.applycolor.tooltip"));
                        return;
                    }
                    t.addLine(translateToLocal("fog.cosmetics.addcolor.tooltip"));
                })
                .tooltipAutoUpdate(true)
                .tooltipShowUpTimer(TOOLTIP_DELAY));

        // Reset button
        row.child(
            new ButtonWidget<>().size(37, 15)
                .onMousePressed(d -> {
                    // Reset color to previously saved color if possible
                    ForgeOfGodsStarColor starColor = getClickedStarColor(hypervisor);
                    if (editingIndex.intValue() >= 0 && starColor.numColors() < editingIndex.intValue()) {
                        StarColorSetting setting = starColor.getColor(editingIndex.intValue());
                        colorData.updateFrom(setting);
                        return true;
                    }

                    // Otherwise reset to defaults
                    colorData.reset();
                    return true;
                })
                .background(GTGuiTextures.BUTTON_OUTLINE_HOLLOW)
                .overlay(
                    IKey.lang("fog.cosmetics.resetcolor")
                        .alignment(Alignment.CENTER))
                .clickSound(ForgeOfGodsGuiUtil.getButtonSound())
                .tooltip(t -> t.addLine(translateToLocal("fog.cosmetics.resetcolor.tooltip")))
                .tooltipShowUpTimer(TOOLTIP_DELAY));

        return row;
    }

    private static Flow createNameRow(SyncHypervisor hypervisor) {
        Flow row = new Row().size(184, 16)
            .marginBottom(3)
            .alignX(0.5f);

        // Name header
        row.child(
            IKey.lang("fog.cosmetics.starcolorname")
                .style(EnumChatFormatting.GOLD)
                .alignment(Alignment.CenterLeft)
                .asWidget()
                .size(92, 16));

        // Name text field
        row.child(
            new TextFieldWidget().size(92, 16)
                .value(new StringValue.Dynamic(() -> {
                    ForgeOfGodsStarColor starColor = getClickedStarColor(hypervisor);
                    return starColor.getName();
                }, val -> {
                    ForgeOfGodsStarColor starColor = getClickedStarColor(hypervisor);
                    starColor.setName(val);
                }))
                .setTextAlignment(Alignment.CenterLeft)
                .setTextColor(0xFFFFAA00)
                .setMaxLength(15)
                .tooltip(t -> {
                    t.addLine(translateToLocal("fog.cosmetics.starcolorname.tooltip.1"));
                    t.addLine(translateToLocal("fog.cosmetics.starcolorname.tooltip.2"));
                })
                .tooltipShowUpTimer(TOOLTIP_DELAY));

        return row;
    }

    private static Flow createSaveDeleteRow(SyncHypervisor hypervisor) {
        IPanelHandler importerPanel = Panels.STAR_COLOR_IMPORT.getFrom(Panels.CUSTOM_STAR_COLOR, hypervisor);

        Flow row = new Row().size(154, 15)
            .childPadding(2)
            .alignX(0.5f);

        // Export button
        row.child(
            new ButtonWidget<>().size(37, 15)
                .onMousePressed(d -> {
                    ForgeOfGodsStarColor starColor = getClickedStarColor(hypervisor);
                    if (starColor.numColors() == 0) return true;
                    if (Desktop.isDesktopSupported()) {
                        String output = starColor.serializeToString();
                        Clipboard clipboard = Toolkit.getDefaultToolkit()
                            .getSystemClipboard();
                        clipboard.setContents(new StringSelection(output), null);
                        hypervisor.getPlayer()
                            .addChatComponentMessage(
                                new ChatComponentTranslation("fog.cosmetics.exportcolors.message"));
                    } else {
                        hypervisor.getPlayer()
                            .addChatComponentMessage(new ChatComponentTranslation("fog.cosmetics.exportcolors.failed"));
                    }
                    return true;
                })
                .background(GTGuiTextures.BUTTON_OUTLINE_HOLLOW)
                .overlay(
                    IKey.lang("fog.cosmetics.exportcolors")
                        .alignment(Alignment.CENTER))
                .clickSound(ForgeOfGodsGuiUtil.getButtonSound())
                .tooltip(t -> t.addLine(translateToLocal("fog.cosmetics.exportcolors.tooltip")))
                .tooltipShowUpTimer(TOOLTIP_DELAY));

        // Import button
        row.child(
            new ButtonWidget<>().size(37, 15)
                .onMousePressed(d -> {
                    if (!importerPanel.isPanelOpen()) {
                        importerPanel.openPanel();
                    }
                    return true;
                })
                .background(GTGuiTextures.BUTTON_OUTLINE_HOLLOW)
                .overlay(
                    IKey.lang("fog.cosmetics.importcolors")
                        .alignment(Alignment.CENTER))
                .clickSound(ForgeOfGodsGuiUtil.getButtonSound())
                .tooltip(t -> t.addLine(translateToLocal("fog.cosmetics.importcolors.tooltip")))
                .tooltipShowUpTimer(TOOLTIP_DELAY));

        // Delete button
        row.child(
            new ButtonWidget<>().size(37, 15)
                .onMousePressed(d -> {
                    StringSyncValue selectedStarColor = SyncValues.SELECTED_STAR_COLOR
                        .lookupFrom(Panels.CUSTOM_STAR_COLOR, hypervisor);

                    ForgeOfGodsStarColor starColor = getClickedStarColor(hypervisor);
                    StarColorStorage starColors = hypervisor.getData()
                        .getStarColors();

                    starColors.drop(starColor);
                    SyncValues.STAR_COLORS.notifyUpdateFrom(Panels.CUSTOM_STAR_COLOR, hypervisor);
                    if (selectedStarColor.getStringValue()
                        .equals(starColor.getName())) {
                        // set to default if the deleted color was selected
                        selectedStarColor.setValue(ForgeOfGodsStarColor.DEFAULT.getName());
                        SyncActions.UPDATE_RENDERER.callFrom(Panels.CUSTOM_STAR_COLOR, hypervisor, null);
                    }
                    hypervisor.getModularPanel(Panels.CUSTOM_STAR_COLOR)
                        .closeIfOpen();
                    return true;
                })
                .background(GTGuiTextures.BUTTON_OUTLINE_HOLLOW)
                .overlay(
                    IKey.lang("fog.cosmetics.deletecolors")
                        .alignment(Alignment.CENTER))
                .clickSound(ForgeOfGodsGuiUtil.getButtonSound())
                .tooltip(t -> t.addLine(translateToLocal("fog.cosmetics.deletecolors.tooltip")))
                .tooltipShowUpTimer(TOOLTIP_DELAY));

        // Save button
        row.child(
            new ButtonWidget<>().size(37, 15)
                .onMousePressed(d -> {
                    ForgeOfGodsStarColor starColor = getClickedStarColor(hypervisor);
                    int editingIndex = getEditingColorIndex(hypervisor);
                    StarColorStorage starColors = hypervisor.getData()
                        .getStarColors();

                    if (starColor.numColors() == 0) return true;
                    if (editingIndex >= 0) {
                        starColors.insert(starColor, editingIndex);
                        SyncValues.STAR_COLORS.notifyUpdateFrom(Panels.CUSTOM_STAR_COLOR, hypervisor);
                        SyncValues.SELECTED_STAR_COLOR.lookupFrom(Panels.CUSTOM_STAR_COLOR, hypervisor)
                            .setValue(starColor.getName());
                        SyncActions.UPDATE_RENDERER.callFrom(Panels.CUSTOM_STAR_COLOR, hypervisor, null);
                    } else {
                        starColors.store(starColor);
                        SyncValues.STAR_COLORS.notifyUpdateFrom(Panels.CUSTOM_STAR_COLOR, hypervisor);
                    }
                    hypervisor.getModularPanel(Panels.CUSTOM_STAR_COLOR)
                        .closeIfOpen();
                    return true;
                })
                .background(GTGuiTextures.BUTTON_OUTLINE_HOLLOW)
                .overlay(
                    IKey.lang("fog.cosmetics.savecolors")
                        .alignment(Alignment.CENTER))
                .clickSound(ForgeOfGodsGuiUtil.getButtonSound())
                .tooltip(t -> t.addLine(translateToLocal("fog.cosmetics.savecolors.tooltip")))
                .tooltipShowUpTimer(TOOLTIP_DELAY));

        return row;
    }
}
