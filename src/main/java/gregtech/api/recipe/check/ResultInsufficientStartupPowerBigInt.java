package gregtech.api.recipe.check;

import static util.Util.toStandardForm;

import java.math.BigInteger;
import java.util.Objects;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

public class ResultInsufficientStartupPowerBigInt implements CheckRecipeResult {

    private String required;

    public ResultInsufficientStartupPowerBigInt(BigInteger required) {
        this.required = toStandardForm(required);
    }

    @NotNull
    @Override
    public String getID() {
        return "insufficient_startup_power_bigint";
    }

    @Override
    public boolean wasSuccessful() {
        return false;
    }

    @NotNull
    @Override
    public String getDisplayString() {
        return Objects.requireNonNull(
            StatCollector.translateToLocalFormatted("GT5U.gui.text.insufficient_startup_power", required));
    }

    @NotNull
    @Override
    public CheckRecipeResult newInstance() {
        return new ResultInsufficientStartupPowerBigInt(BigInteger.ZERO);
    }

    @Override
    public void encode(@NotNull PacketBuffer buffer) {
        try {
            buffer.writeStringToBuffer(required);
        } catch (Exception ignored) {}

    }

    @Override
    public void decode(PacketBuffer buffer) {
        try {
            required = buffer.readStringFromBuffer(32768);
        } catch (Exception ignored) {}
    }
}
