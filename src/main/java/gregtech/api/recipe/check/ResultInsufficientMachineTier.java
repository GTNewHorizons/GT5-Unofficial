package gregtech.api.recipe.check;

import java.util.Objects;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.StatCollector;

import javax.annotation.Nonnull;

import gregtech.api.util.GT_Utility;

public class ResultInsufficientMachineTier implements CheckRecipeResult {

    private int required;

    ResultInsufficientMachineTier(int required) {
        this.required = required;
    }

    @Override
    @Nonnull
    public String getID() {
        return "insufficient_machine_tier";
    }

    @Override
    public boolean wasSuccessful() {
        return false;
    }

    @Override
    @Nonnull
    public String getDisplayString() {
        return Objects.requireNonNull(StatCollector
            .translateToLocalFormatted("GT5U.gui.text.insufficient_machine_tier", GT_Utility.formatNumbers(required)));
    }

    @Override
    @Nonnull
    public CheckRecipeResult newInstance() {
        return new ResultInsufficientMachineTier(0);
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
        ResultInsufficientMachineTier that = (ResultInsufficientMachineTier) o;
        return required == that.required;
    }
}
