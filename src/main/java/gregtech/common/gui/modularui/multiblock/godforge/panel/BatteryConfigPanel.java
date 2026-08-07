package gregtech.common.gui.modularui.multiblock.godforge.panel;

import java.math.BigInteger;
import java.util.regex.Pattern;

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
    private static final BigInteger MIN_BATTERY_CHARGE = BigInteger.ONE;
    private static final BigInteger MAX_BATTERY_CHARGE = BigInteger.valueOf(Long.MAX_VALUE);
    private static final String MIN_BATTERY_CHARGE_TEXT = MIN_BATTERY_CHARGE.toString();
    private static final String MAX_BATTERY_CHARGE_TEXT = MAX_BATTERY_CHARGE.toString();
    private static final Pattern WHOLE_NUMBER_PATTERN = Pattern.compile("[0-9]*");

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
                .topRel(0)
                .horizontalCenter()
                .marginTop(5));

        // Textbox
        panel.child(
            new TextFieldWidget().formatAsInteger(true)
                .setPattern(WHOLE_NUMBER_PATTERN)
                .setValidator(BatteryConfigPanel::validateBatteryChargeText)
                .setMaxLength(MAX_BATTERY_CHARGE_TEXT.length())
                .setTextAlignment(Alignment.CENTER)
                .value(SyncValues.MAX_BATTERY_CHARGE.create(hypervisor))
                .setTooltipOverride(true)
                .scrollValues(1, 64, 4, 16)
                .size(SIZE_W - 8, 18)
                .bottomRel(0)
                .horizontalCenter()
                .marginBottom(9));

        return panel;
    }

    static String validateBatteryChargeText(String text) {
        if (text == null || text.isEmpty()) return MIN_BATTERY_CHARGE_TEXT;

        try {
            BigInteger batteryCharge = new BigInteger(text);
            if (batteryCharge.compareTo(MIN_BATTERY_CHARGE) < 0) return MIN_BATTERY_CHARGE_TEXT;
            if (batteryCharge.compareTo(MAX_BATTERY_CHARGE) > 0) return MAX_BATTERY_CHARGE_TEXT;
            return batteryCharge.toString();
        } catch (NumberFormatException ignored) {
            return MIN_BATTERY_CHARGE_TEXT;
        }
    }
}
