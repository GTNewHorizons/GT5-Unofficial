package gregtech.api.recipe.check;

import java.util.Objects;

import javax.annotation.Nonnull;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.util.GT_Utility;

public class ResultInsufficientHeat implements CheckRecipeResult {

    private int required;

    ResultInsufficientHeat(int required) {
        this.required = required;
    }

    @Override
    @Nonnull
    public @NotNull String getID() {
        return "insufficient_heat";
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
                "GT5U.gui.text.insufficient_heat",
                GT_Utility.formatNumbers(required),
                HeatingCoilLevel.getDisplayNameFromHeat(required, true)));
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
