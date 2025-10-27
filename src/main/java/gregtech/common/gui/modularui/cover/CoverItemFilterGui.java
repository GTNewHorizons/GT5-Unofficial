package gregtech.common.gui.modularui.cover;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.slot.PhantomItemSlot;

import gregtech.api.modularui2.CoverGuiData;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.covers.CoverItemFilter;
import gregtech.common.covers.modes.FilterType;
import gregtech.common.gui.modularui.cover.base.CoverBaseGui;
import gregtech.common.modularui2.widget.builder.EnumRowBuilder;

public class CoverItemFilterGui extends CoverBaseGui<CoverItemFilter> {

    public CoverItemFilterGui(CoverItemFilter cover) {
        super(cover);
    }

    @Override
    protected String getGuiId() {
        return "cover.item_filter";
    }

    @Override
    public void addUIWidgets(PanelSyncManager syncManager, Flow column, CoverGuiData data) {
        EnumSyncValue<FilterType> filterTypeSyncValue = new EnumSyncValue<>(
            FilterType.class,
            cover::getFilterType,
            cover::setFilterType);
        syncManager.syncValue("filter_type", filterTypeSyncValue);

        column.child(
            Flow.column()
                .coverChildren()
                .crossAxisAlignment(Alignment.CrossAxis.START)
                .marginLeft(WIDGET_MARGIN)
                .child(
                    Flow.row()
                        .coverChildren()
                        .childPadding(WIDGET_MARGIN)
                        .child(
                            new EnumRowBuilder<>(FilterType.class).value(filterTypeSyncValue)
                                .overlay(GTGuiTextures.OVERLAY_BUTTON_WHITELIST, GTGuiTextures.OVERLAY_BUTTON_BLACKLIST)
                                .build())
                        .child(
                            IKey.lang("gt.interact.desc.Item_Filter.CheckMode")
                                .asWidget()))
                .child(
                    IKey.lang("gt.interact.desc.Item_Filter.Filter")
                        .asWidget()
                        .marginTop(WIDGET_MARGIN))
                .child(
                    new PhantomItemSlot().slot(new ModularSlot(cover.getFilter(), 0))
                        .marginTop(WIDGET_MARGIN)));
    }

}
