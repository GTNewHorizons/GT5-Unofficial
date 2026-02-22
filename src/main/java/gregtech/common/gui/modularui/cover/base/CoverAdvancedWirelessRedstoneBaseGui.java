package gregtech.common.gui.modularui.cover.base;

import java.util.UUID;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.modularui2.CoverGuiData;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.covers.redstone.CoverAdvancedWirelessRedstoneBase;

public class CoverAdvancedWirelessRedstoneBaseGui<T extends CoverAdvancedWirelessRedstoneBase> extends CoverBaseGui<T> {

    public CoverAdvancedWirelessRedstoneBaseGui(CoverAdvancedWirelessRedstoneBase cover) {
        super((T) cover);
    }

    public CoverAdvancedWirelessRedstoneBaseGui(CoverAdvancedWirelessRedstoneBase cover, boolean buttonRowSpacing) {
        this(cover);
        this.buttonRowSpacing = buttonRowSpacing;
    }

    protected boolean buttonRowSpacing = false;

    @Override
    public void addUIWidgets(PanelSyncManager syncManager, Flow column, CoverGuiData data) {
        StringSyncValue frequencySyncer = new StringSyncValue(cover::getFrequency, cover::setFrequency);
        syncManager.syncValue("frequency", frequencySyncer);
        UUID uuid = data.getPlayer()
            .getUniqueID();
        column.child(makeFrequencyRow().paddingTop(10))
            .child(makeButtonRow(uuid))
            .child(makeThirdFlow(syncManager, data));

    }

    @Override
    protected int getGUIWidth() {
        return 250;
    }

    @Override
    protected int getGUIHeight() {
        return 120;
    }

    protected Flow makeFrequencyRow() {
        return Flow.row()
            .height(16)
            .child(
                new TextFieldWidget().syncHandler("frequency")
                    .height(12)
                    .width(88)
                    .marginRight(2))
            .child(new TextWidget(IKey.lang("gt.interact.desc.freq")))
            .marginBottom(4);
    }

    protected Flow makeButtonRow(UUID uuid) {
        return Flow.row()
            .height(20)
            .child(
                new ToggleButton().size(16, 16)
                    .value(new BooleanSyncValue(cover::getPrivacyState, b -> cover.syncPrivacyState(b, uuid)))
                    .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                    .overlay(false, GTGuiTextures.OVERLAY_BUTTON_CROSS)
                    .marginRight(buttonRowSpacing ? 74 : 2))
            .child(new TextWidget<>(IKey.lang("gt.interact.desc.privfreq")).marginRight(20))
            .marginBottom(4);
    }

    // allows for overriding in subclasses for better ui positioning
    // this is where anything specific to the cover goes.
    protected Flow makeThirdFlow(PanelSyncManager syncManager, CoverGuiData data) {
        return Flow.row()
            .size(1, 1);
    }

}
