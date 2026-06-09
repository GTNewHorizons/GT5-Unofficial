package gregtech.common.gui.modularui.cover.base;

import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.UUID;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.covers.redstone.CoverAdvancedRedstoneTransmitterBase;

public class CoverAdvancedRedstoneTransmitterBaseGui<T extends CoverAdvancedRedstoneTransmitterBase>
    extends CoverAdvancedWirelessRedstoneBaseGui<T> {

    public CoverAdvancedRedstoneTransmitterBaseGui(T cover) {
        super(cover, false);
    }

    @Override
    protected Flow makeButtonRow(UUID uuid) {
        BooleanSyncValue invertedSyncer = new BooleanSyncValue(cover::isInverted, cover::setInverted).allowC2S();
        String textNormal = translateToLocal("gt.interact.desc.normal");
        String textInverted = translateToLocal("gt.interact.desc.inverted");
        IKey.renderer.setAlignment(Alignment.TopLeft, -1, -1);
        return super.makeButtonRow(uuid).child(
            Flow.row()
                .coverChildren()
                .fullHeight()
                .child(
                    new ToggleButton().value(invertedSyncer)
                        .size(16)
                        .overlay(true, GTGuiTextures.OVERLAY_BUTTON_REDSTONE_ON)
                        .overlay(false, GTGuiTextures.OVERLAY_BUTTON_REDSTONE_OFF)
                        .marginRight(4)
                        .marginLeft(4))
                .child(
                    new TextWidget<>(IKey.dynamic(() -> invertedSyncer.getBoolValue() ? textInverted : textNormal))
                        .height(16)
                        .width(getStringMaxWidth(textNormal, textInverted))));
    }

    // method for subclasses that have in-world functionality
    protected Flow physicalRow(BooleanSyncValue physicalSyncer) {
        String redstone0 = translateToLocal("gt.cover.wirelessdetector.redstone.0");
        String redstone1 = translateToLocal("gt.cover.wirelessdetector.redstone.1");
        IKey.renderer.setAlignment(Alignment.TopLeft, -1, -1);
        return Flow.row()
            .coverChildren(0, 16)
            .child(
                new ToggleButton().size(16)
                    .marginRight(2)
                    .value(physicalSyncer)
                    .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK))
            .child(
                new TextWidget<>(
                    IKey.dynamic(() -> translateToLocal(physicalSyncer.getValue() ? redstone1 : redstone0))).height(16)
                        .width(getStringMaxWidth(redstone0, redstone1)))
            .leftRel(0);
    }
}
