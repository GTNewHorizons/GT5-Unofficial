package gregtech.common.gui.modularui.multiblock.godforge.panel;

import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.api.widget.Interactable;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.DynamicSyncHandler;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.DynamicSyncedWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.godforge.data.Panels;
import gregtech.common.gui.modularui.multiblock.godforge.data.SyncValues;
import gregtech.common.gui.modularui.multiblock.godforge.util.ForgeOfGodsGuiUtil;
import gregtech.common.gui.modularui.multiblock.godforge.util.SyncHypervisor;
import tectech.thing.metaTileEntity.multi.godforge.color.ForgeOfGodsStarColor;
import tectech.thing.metaTileEntity.multi.godforge.color.StarColorStorage;
import tectech.thing.metaTileEntity.multi.godforge.util.ForgeOfGodsData;

public class StarCosmeticsPanel {

    private static final int SIZE = 200;

    public static ModularPanel openPanel(SyncHypervisor hypervisor) {
        ModularPanel panel = hypervisor.getModularPanel(Panels.STAR_COSMETICS);

        registerSyncValues(hypervisor);

        panel.size(SIZE)
            .background(GTGuiTextures.BACKGROUND_GLOW_WHITE)
            .disableHoverBackground()
            .child(ForgeOfGodsGuiUtil.panelCloseButton());

        // Title
        panel.child(
            IKey.str(EnumChatFormatting.GOLD + translateToLocal("fog.cosmetics.header"))
                .alignment(Alignment.CENTER)
                .asWidget()
                .alignX(0.5f)
                .marginTop(8));

        // Color options
        Flow colorColumn = new Column().coverChildren()
            .marginTop(28)
            .marginLeft(9);

        // Header
        colorColumn.child(
            IKey.str(
                EnumChatFormatting.GOLD + "" + EnumChatFormatting.UNDERLINE + translateToLocal("fog.cosmetics.color"))
                .alignment(Alignment.CenterLeft)
                .asWidget());

        // Color selector list
        IntSyncValue starColorClickedSyncer = SyncValues.STAR_COLOR_CLICKED
            .lookupFrom(Panels.STAR_COSMETICS, hypervisor);
        IPanelHandler customStarColorPanel = Panels.CUSTOM_STAR_COLOR.getFrom(Panels.STAR_COSMETICS, hypervisor);

        // todo fix this
        DynamicSyncHandler handler = new DynamicSyncHandler().widgetProvider(($, $$) -> {
            ForgeOfGodsData data = hypervisor.getData();
            StarColorStorage starColors = data.getStarColors();
            // todo move scroll bar to the left instead of the right
            ListWidget<IWidget, ?> colorList = new ListWidget<>().size(80, 147);

            // Star colors
            for (int i = 0; i < starColors.size(); i++) {
                ForgeOfGodsStarColor starColor = starColors.getByIndex(i);
                colorList.child(createStarColorRow(starColor, i, hypervisor));
            }

            // Create new star color button
            Flow newStarColorRow = new Row().coverChildren();
            newStarColorRow.child(
                new ButtonWidget<>().size(16)
                    .disableHoverBackground()
                    .overlay(
                        IKey.str(EnumChatFormatting.DARK_GRAY + "+")
                            .alignment(Alignment.CENTER))
                    .onMousePressed(d -> {
                        starColorClickedSyncer.setValue(-1);
                        if (!customStarColorPanel.isPanelOpen()) {
                            customStarColorPanel.openPanel();
                        }
                        return true;
                    })
                    .clickSound(ForgeOfGodsGuiUtil.getButtonSound())
                    .tooltip(t -> t.addLine(translateToLocal("fog.cosmetics.starcolor"))));

            // "Custom..." text
            newStarColorRow.child(
                IKey.str(EnumChatFormatting.GOLD + translateToLocal("fog.cosmetics.customstarcolor"))
                    .alignment(Alignment.CenterLeft)
                    .asWidget()
                    .size(60, 16)
                    .marginLeft(4));

            colorList.child(newStarColorRow);

            return colorList;
        });

        colorColumn.child(
            new DynamicSyncedWidget<>().coverChildren()
                .marginTop(10)
                .syncHandler(handler));
        panel.child(colorColumn);

        // Misc options

        return panel;
    }

    private static void registerSyncValues(SyncHypervisor hypervisor) {
        SyncValues.STAR_COLORS.registerFor(Panels.STAR_COSMETICS, hypervisor);
        SyncValues.STAR_COLOR_CLICKED.registerFor(Panels.STAR_COSMETICS, hypervisor);
        SyncValues.SELECTED_STAR_COLOR.registerFor(Panels.STAR_COSMETICS, hypervisor);
    }

    private static Flow createStarColorRow(ForgeOfGodsStarColor starColor, int index, SyncHypervisor hypervisor) {
        IPanelHandler customStarColorPanel = Panels.CUSTOM_STAR_COLOR.getFrom(Panels.STAR_COSMETICS, hypervisor);

        StringSyncValue selectedStarColorSyncer = SyncValues.SELECTED_STAR_COLOR
            .lookupFrom(Panels.STAR_COSMETICS, hypervisor);
        IntSyncValue starColorClickedSyncer = SyncValues.STAR_COLOR_CLICKED
            .lookupFrom(Panels.STAR_COSMETICS, hypervisor);

        Flow row = new Row().coverChildren()
            .marginBottom(4);

        row.child(
            new ButtonWidget<>().size(16)
                .disableHoverBackground()
                .background(new DynamicDrawable(() -> {
                    if (starColor.getName()
                        .equals(selectedStarColorSyncer.getValue())) {
                        return GTGuiTextures.BUTTON_STANDARD_PRESSED;
                    }
                    return GTGuiTextures.BUTTON_STANDARD;
                }))
                .overlay(starColor.getDrawable())
                .onMousePressed(d -> {
                    if (Interactable.hasShiftDown() && !starColor.isPresetColor()) {
                        // If shift is held, open color editor for this preset, if not a default preset
                        starColorClickedSyncer.setValue(index);
                        if (!customStarColorPanel.isPanelOpen()) {
                            customStarColorPanel.openPanel();
                        }
                    } else {
                        // Otherwise select this color
                        selectedStarColorSyncer.setValue(starColor.getName());
                        // todo update renderer
                    }
                    return true;
                })
                .clickSound(ForgeOfGodsGuiUtil.getButtonSound())
                .tooltip(t -> {
                    t.addLine(translateToLocal("fog.cosmetics.selectcolor.tooltip.1"));
                    t.addLine(translateToLocal("fog.cosmetics.selectcolor.tooltip.2"));
                }));

        row.child(
            IKey.dynamic(() -> EnumChatFormatting.GOLD + starColor.getLocalizedName())
                .alignment(Alignment.CenterLeft)
                .asWidget()
                .size(60, 16)
                .marginLeft(4));

        return row;
    }
}
