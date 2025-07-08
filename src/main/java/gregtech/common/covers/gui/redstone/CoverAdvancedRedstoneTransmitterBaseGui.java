package gregtech.common.covers.gui.redstone;

import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.UUID;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.covers.redstone.CoverAdvancedRedstoneTransmitterBase;

public class CoverAdvancedRedstoneTransmitterBaseGui<T extends CoverAdvancedRedstoneTransmitterBase>
    extends CoverAdvancedWirelessRedstoneBaseGui<T> {

    public CoverAdvancedRedstoneTransmitterBaseGui(CoverAdvancedRedstoneTransmitterBase cover) {
        super(cover, false);
    }

    @Override
    protected Flow makeButtonRow(UUID uuid) {
        BooleanSyncValue invertedSyncer = new BooleanSyncValue(cover::isInverted, cover::setInverted);
        return super.makeButtonRow(uuid).child(
            new Row().width(60)
                .heightRel(1)
                .child(

                    new ToggleButton().value(invertedSyncer)
                        .size(16, 16)
                        .overlay(true, GTGuiTextures.OVERLAY_BUTTON_REDSTONE_ON)
                        .overlay(false, GTGuiTextures.OVERLAY_BUTTON_REDSTONE_OFF)
                        .marginRight(4))
                .child(
                    new TextWidget(
                        IKey.dynamic(
                            () -> invertedSyncer.getBoolValue() ? translateToLocal("gt.interact.desc.inverted")
                                : translateToLocal("gt.interact.desc.normal"))).height(16)));
    }
}
