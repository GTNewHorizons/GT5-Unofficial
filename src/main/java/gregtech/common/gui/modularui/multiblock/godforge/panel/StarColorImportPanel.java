package gregtech.common.gui.modularui.multiblock.godforge.panel;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.util.EnumChatFormatting;

import org.apache.commons.lang3.mutable.MutableObject;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.Rectangle;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.StringValue;
import com.cleanroommc.modularui.value.sync.GenericSyncValue;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.godforge.ForgeOfGodsGuiUtil;
import gregtech.common.gui.modularui.multiblock.godforge.data.StarColors;
import gregtech.common.gui.modularui.multiblock.godforge.sync.Panels;
import gregtech.common.gui.modularui.multiblock.godforge.sync.SyncHypervisor;
import gregtech.common.gui.modularui.multiblock.godforge.sync.SyncValues;
import tectech.thing.metaTileEntity.multi.godforge.color.ForgeOfGodsStarColor;
import tectech.thing.metaTileEntity.multi.godforge.color.StarColorSetting;

public class StarColorImportPanel {

    private static final int SIZE_W = 200;
    private static final int SIZE_H = 100;

    public static ModularPanel openPanel(SyncHypervisor hypervisor) {
        ModularPanel panel = hypervisor.getModularPanel(Panels.STAR_COLOR_IMPORT);

        MutableObject<ForgeOfGodsStarColor> importedColor = new MutableObject<>();
        MutableObject<String> importedColorStr = new MutableObject<>("");

        panel.size(SIZE_W, SIZE_H)
            .background(GTGuiTextures.BACKGROUND_GLOW_WHITE_HALF)
            .disableHoverBackground()
            .child(ForgeOfGodsGuiUtil.panelCloseButton())
            .onCloseAction(() -> {
                importedColor.setValue(null);
                importedColorStr.setValue("");
            });

        // Title
        panel.child(
            IKey.lang("fog.cosmetics.importer.import")
                .style(EnumChatFormatting.GOLD)
                .alignment(Alignment.CENTER)
                .asWidget()
                .align(Alignment.TopCenter)
                .marginTop(9));

        Flow mainColumn = new Column().coverChildren()
            .marginTop(23)
            .alignX(0.5f);

        // Serialized star color text field
        mainColumn.child(
            new TextFieldWidget().size(184, 18)
                .setTextAlignment(Alignment.CenterLeft)
                .setMaxLength(Short.MAX_VALUE)
                .value(new StringValue.Dynamic(importedColorStr::getValue, val -> {
                    importedColorStr.setValue(val);
                    if (val == null || val.isEmpty()) {
                        importedColor.setValue(null);
                        return;
                    }
                    importedColor.setValue(ForgeOfGodsStarColor.deserialize(val));
                }))
                .setTooltipOverride(true)
                .alignX(0.5f)
                .marginBottom(4));

        // Color preview row
        mainColumn.child(createPreviewRow(importedColor));

        // Validator string
        mainColumn.child(IKey.dynamic(() -> {
            ForgeOfGodsStarColor color = importedColor.getValue();
            String colorStr = importedColorStr.getValue();
            if (color == null) {
                if (colorStr == null || colorStr.isEmpty()) {
                    // don't display if there is no text at all yet
                    return "";
                }
                return EnumChatFormatting.RED + translateToLocal("fog.cosmetics.importer.error");
            }
            return EnumChatFormatting.GREEN + translateToLocal("fog.cosmetics.importer.valid");
        })
            .alignment(Alignment.CENTER)
            .asWidget()
            .alignX(0.5f)
            .size(184, 15));

        // Reset/Apply row
        mainColumn.child(createApplyResetRow(hypervisor, importedColor, importedColorStr));

        panel.child(mainColumn);
        return panel;
    }

    private static Flow createPreviewRow(MutableObject<ForgeOfGodsStarColor> color) {
        Flow row = new Row().size(184, 18)
            .alignX(0.5f);

        // Color previews
        for (int i = 0; i < ForgeOfGodsStarColor.MAX_COLORS; i++) {
            row.child(createPreviewColor(color, i));
        }

        // Rate preview
        row.child(
            GTGuiTextures.BACKGROUND_TEXT_FIELD.asWidget()
                .overlay(IKey.dynamic(() -> {
                    ForgeOfGodsStarColor starColor = color.getValue();
                    if (starColor != null) {
                        return "" + starColor.getCycleSpeed();
                    }
                    return "";
                })
                    .color(Color.WHITE.main)
                    .alignment(Alignment.CENTER))
                .size(21, 16)
                .marginLeft(1)
                .tooltip(t -> t.addLine(translateToLocal("fog.cosmetics.cyclespeed")))
                .tooltipShowUpTimer(TOOLTIP_DELAY));

        return row;
    }

    private static IWidget createPreviewColor(MutableObject<ForgeOfGodsStarColor> color, int index) {
        return new DynamicDrawable(() -> {
            ForgeOfGodsStarColor starColor = color.getValue();
            if (starColor == null || index >= starColor.numColors()) {
                return GTGuiTextures.UNSELECTED_OPTION;
            }
            StarColorSetting setting = starColor.getColor(index);
            return new Rectangle().setColor(Color.rgb(setting.getColorR(), setting.getColorG(), setting.getColorB()))
                .asIcon()
                .size(16)
                .margin(1);
        }).asWidget()
            .size(18)
            .tooltipDynamic(t -> {
                ForgeOfGodsStarColor starColor = color.getValue();
                if (starColor != null && index < starColor.numColors()) {
                    StarColorSetting setting = starColor.getColor(index);
                    t.addLine(StarColors.RGB.RED.getTooltip(setting.getColorR()));
                    t.addLine(StarColors.RGB.GREEN.getTooltip(setting.getColorG()));
                    t.addLine(StarColors.RGB.BLUE.getTooltip(setting.getColorB()));
                    t.addLine(StarColors.Extra.GAMMA.getTooltip(setting.getGamma()));
                }
            })
            .tooltipAutoUpdate(true)
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    private static Flow createApplyResetRow(SyncHypervisor hypervisor, MutableObject<ForgeOfGodsStarColor> color,
        MutableObject<String> colorStr) {
        Flow row = new Row().size(76, 15)
            .childPadding(2)
            .alignX(0.5f);

        // Reset button
        row.child(
            new ButtonWidget<>().size(37, 15)
                .onMousePressed(d -> {
                    color.setValue(null);
                    colorStr.setValue("");
                    return true;
                })
                .background(GTGuiTextures.BUTTON_OUTLINE_HOLLOW)
                .overlay(
                    IKey.lang("fog.cosmetics.importer.reset")
                        .alignment(Alignment.CENTER))
                .clickSound(ForgeOfGodsGuiUtil.getButtonSound())
                .tooltip(t -> t.addLine(translateToLocal("fog.cosmetics.importer.reset.tooltip")))
                .tooltipShowUpTimer(TOOLTIP_DELAY));

        // Apply button
        row.child(
            new ButtonWidget<>().size(37, 15)
                .onMousePressed(d -> {
                    if (color.getValue() != null) {
                        GenericSyncValue<ForgeOfGodsStarColor> starColorClickedSyncer = SyncValues.STAR_COLOR_CLICKED
                            .lookupFrom(Panels.STAR_COSMETICS, hypervisor);
                        starColorClickedSyncer.setValue(color.getValue());
                        hypervisor.getModularPanel(Panels.STAR_COLOR_IMPORT)
                            .closeIfOpen();
                    }
                    return true;
                })
                .background(GTGuiTextures.BUTTON_OUTLINE_HOLLOW)
                .overlay(
                    IKey.lang("fog.cosmetics.importer.apply")
                        .alignment(Alignment.CENTER))
                .clickSound(ForgeOfGodsGuiUtil.getButtonSound())
                .tooltip(t -> t.addLine(translateToLocal("fog.cosmetics.importer.apply.tooltip")))
                .tooltipShowUpTimer(TOOLTIP_DELAY));

        return row;
    }
}
