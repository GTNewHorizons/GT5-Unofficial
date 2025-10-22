package gregtech.common.gui.modularui.cover.base;

import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;

import gregtech.api.modularui2.CoverGuiData;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.common.covers.CoverFacadeBase;

public class CoverFacadeBaseGui extends CoverBaseGui<CoverFacadeBase> {

    public CoverFacadeBaseGui(CoverFacadeBase cover) {
        super(cover);
    }

    @Override
    protected String getGuiId() {
        return "cover.facade";
    }

    @Override
    public void addUIWidgets(PanelSyncManager syncManager, Flow column, CoverGuiData data) {
        column.child(
            new Grid().marginLeft(WIDGET_MARGIN)
                .coverChildren()
                .minElementMarginRight(WIDGET_MARGIN)
                .minElementMarginBottom(2)
                .minElementMarginTop(0)
                .minElementMarginLeft(0)
                .alignment(Alignment.CenterLeft)
                .row(
                    new ToggleButton().value(new BooleanSyncValue(cover::getRedstonePass, cover::setRedstonePass))
                        .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                        .overlay(false, GTGuiTextures.OVERLAY_BUTTON_CROSS)
                        .size(16),
                    IKey.lang("gt.interact.desc.facade.Redstone")
                        .asWidget())
                .row(
                    new ToggleButton().value(new BooleanSyncValue(cover::getEnergyPass, cover::setEnergyPass))
                        .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                        .overlay(false, GTGuiTextures.OVERLAY_BUTTON_CROSS)
                        .size(16),
                    IKey.lang("gt.interact.desc.facade.Energy")
                        .asWidget())
                .row(
                    new ToggleButton().value(new BooleanSyncValue(cover::getFluidPass, cover::setFluidPass))
                        .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                        .overlay(false, GTGuiTextures.OVERLAY_BUTTON_CROSS)
                        .size(16),
                    IKey.lang("gt.interact.desc.facade.Fluids")
                        .asWidget())
                .row(
                    new ToggleButton().value(new BooleanSyncValue(cover::getItemPass, cover::setItemPass))
                        .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                        .overlay(false, GTGuiTextures.OVERLAY_BUTTON_CROSS)
                        .size(16),
                    IKey.lang("gt.interact.desc.facade.Items")
                        .asWidget()));
    }

    @Override
    protected void addTitleToUI(Flow column) {
        ItemStack coverItem = cover.getStack();
        if (coverItem == null) return;
        column.child(
            Flow.row()
                .coverChildren()
                .marginBottom(4)
                .child(new com.cleanroommc.modularui.drawable.ItemDrawable(coverItem).asWidget())
                .child(
                    new com.cleanroommc.modularui.widgets.TextWidget<>(coverItem.getDisplayName()).marginLeft(4)
                        .widgetTheme(GTWidgetThemes.TEXT_TITLE)));
    }

}
