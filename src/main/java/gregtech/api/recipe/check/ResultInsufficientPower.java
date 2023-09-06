package gregtech.api.recipe.check;

import java.util.Objects;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.StatCollector;

import javax.annotation.Nonnull;

import gregtech.api.util.GT_Utility;

public class ResultInsufficientPower implements CheckRecipeResult {

    private long required;

    ResultInsufficientPower(long required) {
        this.required = required;
    }

    @Override
    @Nonnull
    public String getID() {
        return "insufficient_power";
    }

    @Override
    public boolean wasSuccessful() {
        return false;
    }

    @Override
    @Nonnull
    public String getDisplayString() {
        return Objects.requireNonNull(StatCollector.translateToLocalFormatted(
            "GT5U.gui.text.insufficient_power",
            GT_Utility.formatNumbers(required),
            GT_Utility.getColoredTierNameFromVoltage(required)));
    }

    @Override
    @Nonnull
    public CheckRecipeResult newInstance() {
        return new ResultInsufficientPower(0);
    }

    @Override
    public void encode(@Nonnull PacketBuffer buffer) {
        buffer.writeLong(required);
    }

    @Override
    public void decode(@Nonnull PacketBuffer buffer) {
        required = buffer.readLong();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultInsufficientPower that = (ResultInsufficientPower) o;
        return required == that.required;
    }
}
