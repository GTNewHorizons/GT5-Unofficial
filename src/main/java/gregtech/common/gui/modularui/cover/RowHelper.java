package gregtech.common.gui.modularui.cover;

import static net.minecraft.util.StatCollector.translateToLocal;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.covers.Invertable;

public class RowHelper {

    public static Flow makeInvertRedstoneRow(Invertable cover) {
        BooleanSyncValue isInvertedSyncValue = new BooleanSyncValue(cover::isInverted, cover::setInverted);
        return Flow.row()
            .name("invert_redstone")
            .child(
                new ToggleButton().value(isInvertedSyncValue)
                    .overlay(true, GTGuiTextures.OVERLAY_BUTTON_REDSTONE_ON)
                    .overlay(false, GTGuiTextures.OVERLAY_BUTTON_REDSTONE_OFF)
                    .size(16))
            .child(
                IKey.dynamic(
                    () -> isInvertedSyncValue.getValue() ? translateToLocal("gt.interact.desc.normal")
                        : translateToLocal("gt.interact.desc.inverted"))
                    .asWidget());
    }
}
