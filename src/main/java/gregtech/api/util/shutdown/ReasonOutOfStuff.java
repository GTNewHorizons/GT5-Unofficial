package gregtech.api.util.shutdown;

import static gregtech.api.util.GT_Utility.formatNumbers;

import java.util.Objects;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

public class ReasonOutOfStuff implements ShutDownReason {

    private String required;
    private int amount;

    ReasonOutOfStuff(@NotNull String required, int amount) {
        this.required = required;
        this.amount = amount;
    }

    @NotNull
    @Override
    public String getID() {
        return "out_of_stuff";
    }

    @NotNull
    @Override
    public String getDisplayString() {
        return Objects.requireNonNull(
            StatCollector.translateToLocalFormatted("GT5U.gui.text.out_of_stuff", required, formatNumbers(amount)));
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound tag) {
        tag.setString("required", required);
        tag.setInteger("amount", amount);
        return tag;
    }

    @Override
    public void readFromNBT(@NotNull NBTTagCompound tag) {
        required = tag.getString("required");
        tag.setInteger("amount", amount);
    }

    @NotNull
    @Override
    public ShutDownReason newInstance() {
        return new ReasonOutOfStuff("stuff", 1);
    }

    @Override
    public void encode(@NotNull PacketBuffer buffer) {
        buffer.writeInt(amount);
        try {
            buffer.writeStringToBuffer(required);
        } catch (Exception ignored) {}
    }

    @Override
    public void decode(PacketBuffer buffer) {
        amount = buffer.readInt();
        try {
            required = buffer.readStringFromBuffer(32768);
        } catch (Exception ignored) {}
    }

    @Override
    public boolean wasCritical() {
        return true;
    }
}
