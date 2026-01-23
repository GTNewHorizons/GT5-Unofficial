package gregtech.api.recipe.check;

import gregtech.api.enums.Dyes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.StatCollector;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ResultNoValidOutputColor implements CheckRecipeResult {
    private byte color;

    ResultNoValidOutputColor(byte color) {
        this.color = color;
    }

    @Override
    public boolean wasSuccessful() {
        return false;
    }

    @Override
    public @NotNull String getID() {
        return "no_valid_output_color";
    }

    @Override
    public @NotNull String getDisplayString() {
        String dyeName = Dyes.get(color).getLocalizedDyeName();
        return Objects.requireNonNull(
            StatCollector.translateToLocalFormatted(
                "GT5U.gui.text.nac.no_valid_hatch_color",
                dyeName
            ));
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound tag) {
        tag.setByte("color", color);
        return tag;
    }

    @Override
    public void readFromNBT(@NotNull NBTTagCompound tag) {
        color = tag.getByte("color");
    }

    @Override
    public @NotNull CheckRecipeResult newInstance() {
        return new ResultNoValidOutputColor((byte) 0);
    }

    @Override
    public void encode(@NotNull PacketBuffer buffer) {
        buffer.writeByte(color);
    }

    @Override
    public void decode(PacketBuffer buffer) {
        color = buffer.readByte();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultNoValidOutputColor that = (ResultNoValidOutputColor) o;
        return color == that.color;
    }
}
