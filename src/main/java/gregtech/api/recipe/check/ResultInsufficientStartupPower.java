package gregtech.api.recipe.check;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.util.Objects;

import javax.annotation.Nonnull;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import gregtech.api.enums.GTValues;

public class ResultInsufficientStartupPower implements CheckRecipeResult {

    private int required;
    private int machineTier;

    ResultInsufficientStartupPower(int required) {
        this(required, -1);
    }

    ResultInsufficientStartupPower(int required, int machineTier) {
        this.required = required;
        this.machineTier = machineTier;
    }

    private long getRequiredAmperage() {
        long voltage = GTValues.V[machineTier];
        return Math.max(1L, (required + voltage - 1L) / voltage);
    }

    private long getRequiredSixtyFourAmpHatches() {
        long requiredAmperage = getRequiredAmperage();
        return Math.max(1L, (requiredAmperage + 63L) / 64L);
    }

    private boolean hasAmperageEstimate() {
        return machineTier >= 0 && machineTier < GTValues.V.length;
    }

    @Override
    @Nonnull
    public @NotNull String getID() {
        return "insufficient_startup_power";
    }

    @Override
    public boolean wasSuccessful() {
        return false;
    }

    @Override
    public boolean persistsOnShutdown() {
        return hasAmperageEstimate();
    }

    @Override
    @Nonnull
    public @NotNull String getDisplayString() {
        if (hasAmperageEstimate()) {
            return Objects.requireNonNull(
                StatCollector.translateToLocalFormatted(
                    "GT5U.gui.text.recipe_result.insufficient_startup_power_with_amperage",
                    formatNumber(required),
                    formatNumber(getRequiredAmperage()),
                    formatNumber(getRequiredSixtyFourAmpHatches())));
        }
        return Objects.requireNonNull(
            StatCollector.translateToLocalFormatted(
                "GT5U.gui.text.recipe_result.insufficient_startup_power",
                formatNumber(required)));
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound tag) {
        tag.setInteger("required", required);
        tag.setInteger("machineTier", machineTier);
        return tag;
    }

    @Override
    public void readFromNBT(@NotNull NBTTagCompound tag) {
        required = tag.getInteger("required");
        machineTier = tag.hasKey("machineTier") ? tag.getInteger("machineTier") : -1;
    }

    @Override
    @Nonnull
    public @NotNull CheckRecipeResult newInstance() {
        return new ResultInsufficientStartupPower(0);
    }

    @Override
    public void encode(@Nonnull PacketBuffer buffer) {
        buffer.writeVarIntToBuffer(required);
        buffer.writeVarIntToBuffer(machineTier);
    }

    @Override
    public void decode(@Nonnull PacketBuffer buffer) {
        required = buffer.readVarIntFromBuffer();
        machineTier = buffer.readVarIntFromBuffer();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultInsufficientStartupPower that = (ResultInsufficientStartupPower) o;
        return required == that.required && machineTier == that.machineTier;
    }
}
