package gregtech.api.recipe.check;

import java.util.Objects;

import javax.annotation.Nonnull;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.StatCollector;

import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.util.GT_Utility;

public class ResultInsufficientHeat implements CheckRecipeResult {

    private int required;

    ResultInsufficientHeat(int required) {
        this.required = required;
    }

    @Override
    @Nonnull
    public String getID() {
        return "insufficient_heat";
    }

    @Override
    public boolean wasSuccessful() {
        return false;
    }

    @Override
    @Nonnull
    public String getDisplayString() {
        return Objects.requireNonNull(
            StatCollector.translateToLocalFormatted(
                "GT5U.gui.text.insufficient_heat",
                GT_Utility.formatNumbers(required),
                HeatingCoilLevel.getDisplayNameFromHeat(required, true)));
    }

    @Override
    @Nonnull
    public CheckRecipeResult newInstance() {
        return new ResultInsufficientHeat(0);
    }

    @Override
    public void encode(@Nonnull PacketBuffer buffer) {
        buffer.writeVarIntToBuffer(required);
    }

    @Override
    public void decode(@Nonnull PacketBuffer buffer) {
        required = buffer.readVarIntFromBuffer();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultInsufficientHeat that = (ResultInsufficientHeat) o;
        return required == that.required;
    }
}
