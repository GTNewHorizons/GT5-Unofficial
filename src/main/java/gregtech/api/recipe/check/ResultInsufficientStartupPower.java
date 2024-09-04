package gregtech.api.recipe.check;

import java.util.Objects;

import javax.annotation.Nonnull;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import gregtech.api.util.GTUtility;

public class ResultInsufficientStartupPower implements CheckRecipeResult {

    private int required;

    ResultInsufficientStartupPower(int required) {
        this.required = required;
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
    @Nonnull
    public @NotNull String getDisplayString() {
        return Objects.requireNonNull(
            StatCollector.translateToLocalFormatted(
                "GT5U.gui.text.insufficient_startup_power",
                GTUtility.formatNumbers(required)));
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound tag) {
        tag.setInteger("required", required);
        return tag;
    }

    @Override
    public void readFromNBT(@NotNull NBTTagCompound tag) {
        required = tag.getInteger("required");
    }

    @Override
    @Nonnull
    public @NotNull CheckRecipeResult newInstance() {
        return new ResultInsufficientStartupPower(0);
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
        ResultInsufficientStartupPower that = (ResultInsufficientStartupPower) o;
        return required == that.required;
    }
}
