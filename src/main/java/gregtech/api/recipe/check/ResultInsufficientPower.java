package gregtech.api.recipe.check;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.util.Objects;

import javax.annotation.Nonnull;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import gregtech.api.util.GTUtility;

public class ResultInsufficientPower implements CheckRecipeResult {

    private long required;
    private long current;

    ResultInsufficientPower(long required, long current) {
        this.required = required;
        this.current = current;
    }

    @Override
    @Nonnull
    public @NotNull String getID() {
        return "insufficient_power";
    }

    @Override
    public boolean wasSuccessful() {
        return false;
    }

    @Override
    @Nonnull
    public @NotNull String getDisplayString() {
        if (!hasReportableCurrent()) {
            return Objects.requireNonNull(
                StatCollector.translateToLocalFormatted(
                    "GT5U.gui.text.recipe_result.insufficient_power",
                    formatNumber(required),
                    GTUtility.getColoredTierNameFromVoltage(required)));
        }
        return Objects.requireNonNull(
            StatCollector.translateToLocalFormatted(
                "GT5U.gui.text.recipe_result.insufficient_power_with_current",
                formatNumber(required),
                GTUtility.getColoredTierNameFromVoltage(required),
                formatNumber(current),
                GTUtility.getColoredTierNameFromVoltage(current)));
    }

    private boolean hasReportableCurrent() {
        return current > 0 && current < required;
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound tag) {
        tag.setLong("required", required);
        tag.setLong("current", current);
        return tag;
    }

    @Override
    public void readFromNBT(@NotNull NBTTagCompound tag) {
        required = tag.getLong("required");
        current = tag.getLong("current");
    }

    @Override
    @Nonnull
    public @NotNull CheckRecipeResult newInstance() {
        return new ResultInsufficientPower(0, 0);
    }

    @Override
    public void encode(@Nonnull PacketBuffer buffer) {
        buffer.writeLong(required);
        buffer.writeLong(current);
    }

    @Override
    public void decode(@Nonnull PacketBuffer buffer) {
        required = buffer.readLong();
        current = buffer.readLong();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultInsufficientPower that = (ResultInsufficientPower) o;
        return required == that.required && current == that.current;
    }
}
