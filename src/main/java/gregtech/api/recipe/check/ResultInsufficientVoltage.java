package gregtech.api.recipe.check;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.StatCollector;

import gregtech.api.util.GT_Utility;

public class ResultInsufficientVoltage implements CheckRecipeResult {

    private long required;

    ResultInsufficientVoltage(long required) {
        this.required = required;
    }

    @Override
    public String getID() {
        return "insufficient_voltage";
    }

    @Override
    public boolean wasSuccessful() {
        return false;
    }

    @Override
    public String getDisplayString() {
        return StatCollector.translateToLocalFormatted(
            "GT5U.gui.text.insufficient_voltage",
            GT_Utility.formatNumbers(required),
            GT_Utility.getColoredTierNameFromVoltage(required));
    }

    @Override
    public CheckRecipeResult newInstance() {
        return new ResultInsufficientVoltage(0);
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeLong(required);
    }

    @Override
    public void decode(PacketBuffer buffer) {
        required = buffer.readLong();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultInsufficientVoltage that = (ResultInsufficientVoltage) o;
        return required == that.required;
    }
}
