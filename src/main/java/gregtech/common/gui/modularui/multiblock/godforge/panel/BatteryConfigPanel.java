package gregtech.common.gui.modularui.multiblock.godforge.panel;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.common.gui.modularui.multiblock.godforge.sync.Panels;
import gregtech.common.gui.modularui.multiblock.godforge.sync.SyncHypervisor;
import gregtech.common.gui.modularui.multiblock.godforge.sync.SyncValues;

public class BatteryConfigPanel {

    private static final int SIZE_W = 78;
    private static final int SIZE_H = 52;

    public static ModularPanel openPanel(SyncHypervisor hypervisor) {
        ModularPanel panel = hypervisor.getModularPanel(Panels.BATTERY_CONFIG);

        panel.relative(hypervisor.getModularPanel(Panels.MAIN))
            .size(SIZE_W, SIZE_H)
            .leftRelOffset(1, -3)
            .topRelOffset(0, 129);

        // Header
        panel.child(
            IKey.lang("gt.blockmachines.multimachine.FOG.batteryinfo")
                .style(EnumChatFormatting.DARK_GRAY)
                .alignment(Alignment.CENTER)
                .asWidget()
                .width(SIZE_W - 8)
                .align(Alignment.TopCenter)
                .marginTop(5));

        // Textbox
        panel.child(
            new TextFieldWidget().setFormatAsInteger(true)
                .setNumbers(1, Integer.MAX_VALUE)
                .setTextAlignment(Alignment.CENTER)
                .value(SyncValues.MAX_BATTERY_CHARGE.create(hypervisor))
                .setTooltipOverride(true)
                .setScrollValues(1, 4, 64)
                .size(SIZE_W - 8, 18)
                .align(Alignment.BottomCenter)
                .marginBottom(9));

        return panel;
    }
}
