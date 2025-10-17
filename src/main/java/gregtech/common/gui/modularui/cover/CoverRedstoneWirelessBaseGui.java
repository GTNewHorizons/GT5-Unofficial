package gregtech.common.gui.modularui.cover;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.CoverGuiData;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.covers.CoverRedstoneWirelessBase;
import gregtech.common.gui.modularui.cover.base.CoverBaseGui;

public class CoverRedstoneWirelessBaseGui extends CoverBaseGui<CoverRedstoneWirelessBase> {

    public CoverRedstoneWirelessBaseGui(CoverRedstoneWirelessBase cover) {
        super(cover);
    }

    @Override
    protected int getGUIWidth() {
        return 200;
    }

    @Override
    public void addUIWidgets(PanelSyncManager syncManager, Flow column, CoverGuiData data) {
        column.child(positionRow(createFrequencyRow()))
            .child(positionRow(createRedstoneModeRow()));
    }

    private Flow createFrequencyRow() {
        IntSyncValue frequencySyncer = new IntSyncValue(cover::getFrequency, cover::setFrequency);

        return Flow.row()
            .marginBottom(4)
            .child(createFrequencyInputField(frequencySyncer))
            .child(
                IKey.lang("gt.interact.desc.freq")
                    .asWidget());
    }

    private IWidget createFrequencyInputField(IntSyncValue frequencySyncer) {
        return makeNumberField(88).marginRight(2)
            .height(12)
            .setNumbers(0, CoverRedstoneWirelessBase.MAX_CHANNEL)
            .value(frequencySyncer);
    }

    private Flow createRedstoneModeRow() {
        BooleanSyncValue privateChannelSyncer = new BooleanSyncValue(cover::isPrivateChannel, cover::setPrivateChannel);
        return Flow.row()
            .child(createPrivacyModeButton(privateChannelSyncer))
            .child(
                IKey.lang("gt.interact.desc.RedstoneWirelessBase.Use_Private_Freq")
                    .asWidget());
    }

    private IWidget createPrivacyModeButton(BooleanSyncValue privateChannelSyncer) {
        return new ToggleButton().size(16, 16)
            .marginRight(2)
            .value(privateChannelSyncer)
            .overlay(false, GTGuiTextures.OVERLAY_BUTTON_CROSS)
            .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK);
    }
}
