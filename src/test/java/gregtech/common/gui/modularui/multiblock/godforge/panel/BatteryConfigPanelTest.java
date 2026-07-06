package gregtech.common.gui.modularui.multiblock.godforge.panel;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class BatteryConfigPanelTest {

    @Test
    public void batteryChargeValidatorKeepsLongMaxAsIntegerText() {
        assertEquals("9223372036854775807", BatteryConfigPanel.validateBatteryChargeText("9223372036854775807"));
    }

    @Test
    public void batteryChargeValidatorClampsOutOfRangeValues() {
        assertEquals("1", BatteryConfigPanel.validateBatteryChargeText(""));
        assertEquals("1", BatteryConfigPanel.validateBatteryChargeText("0"));
        assertEquals("9223372036854775807", BatteryConfigPanel.validateBatteryChargeText("9223372036854775808"));
    }
}
