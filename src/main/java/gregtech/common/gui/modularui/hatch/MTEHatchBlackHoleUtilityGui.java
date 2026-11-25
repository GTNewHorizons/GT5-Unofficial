package gregtech.common.gui.modularui.hatch;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.hatch.base.MTEHatchRedstoneBaseGui;
import gregtech.common.tileentities.machines.multi.compressor.MTEHatchBlackHoleUtility;

public class MTEHatchBlackHoleUtilityGui extends MTEHatchRedstoneBaseGui<MTEHatchBlackHoleUtility> {

    public MTEHatchBlackHoleUtilityGui(MTEHatchBlackHoleUtility hatch) {
        super(hatch);
    }

    @Override
    protected Flow createContentColumn() {
        return super.createContentColumn().child(createInvertButtonRow());
    }

    private Flow createInvertButtonRow() {
        BooleanSyncValue invertedSyncer = new BooleanSyncValue(machine::getMode, machine::setMode).allowC2S();
        return Flow.row()
            .child(
                new ToggleButton().value(invertedSyncer)
                    .overlay(true, GTGuiTextures.OVERLAY_BUTTON_REDSTONE_ON)
                    .overlay(false, GTGuiTextures.OVERLAY_BUTTON_ANALOG))
            .child(
                IKey.dynamic(
                    () -> StatCollector.translateToLocal(
                        invertedSyncer.getValue() ? "GT5U.gui.text.static_mode" : "GT5U.gui.text.pulse_mode"))
                    .asWidget())
            .coverChildren()
            .childPadding(2);
    }
}
