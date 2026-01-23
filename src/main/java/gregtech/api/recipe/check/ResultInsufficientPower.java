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

    ResultInsufficientPower(long required) {
        this.required = required;
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
        return Objects.requireNonNull(
            StatCollector.translateToLocalFormatted(
                "GT5U.gui.text.insufficient_power",
                formatNumber(required),
                GTUtility.getColoredTierNameFromVoltage(required)));
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound tag) {
        tag.setLong("required", required);
        return tag;
    }

    @Override
    public void readFromNBT(@NotNull NBTTagCompound tag) {
        required = tag.getLong("required");
    }

    @Override
    @Nonnull
    public @NotNull CheckRecipeResult newInstance() {
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
