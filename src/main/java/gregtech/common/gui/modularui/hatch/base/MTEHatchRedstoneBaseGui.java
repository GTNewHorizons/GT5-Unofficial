package gregtech.common.gui.modularui.hatch.base;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.common.CommonWidgets;
import gregtech.common.tileentities.machines.multi.MTEHatchRedstoneBase;

public class MTEHatchRedstoneBaseGui<T extends MTEHatchRedstoneBase> extends MTEHatchBaseGui<T> {

    public MTEHatchRedstoneBaseGui(T machine) {
        super(machine);
    }

    @Override
    protected int getBasePanelHeight() {
        return 172;
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createContentSection(panel, syncManager).child(createContentColumn());
    }

    protected Flow createContentColumn() {
        return Flow.column()
            .child(createDirectionalButtonRow())
            .childIf(
                MTEHatchRedstoneBase.supportsInvertedSignal(),
                () -> CommonWidgets
                    .createInvertButtonRow(new BooleanSyncValue(machine::isInverted, machine::setInverted).allowC2S()))
            .coverChildren()
            .crossAxisAlignment(Alignment.CrossAxis.START)
            .childPadding(2);
    }

    protected Flow createDirectionalButtonRow() {
        BooleanSyncValue directionalSyncer = new BooleanSyncValue(machine::isDirectional, machine::setDirectional)
            .allowC2S();
        return Flow.row()
            .child(
                new ToggleButton().overlay(true, GTGuiTextures.OVERLAY_BUTTON_DIRECTIONAL_ON)
                    .overlay(false, GTGuiTextures.OVERLAY_BUTTON_DIRECTIONAL_OFF)
                    .value(directionalSyncer)
                    .size(16)
                    .addTooltip(true, StatCollector.translateToLocal("GT5U.gui.button.directional_on"))
                    .addTooltip(false, StatCollector.translateToLocal("GT5U.gui.button.directional_off")))
            .child(
                IKey.lang("GT5U.gui.button.directional")
                    .asWidget())
            .childPadding(2);
    }
}
