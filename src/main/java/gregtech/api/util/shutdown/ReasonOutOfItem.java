package gregtech.api.util.shutdown;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.util.Objects;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

public class ReasonOutOfItem implements ShutDownReason {

    private ItemStack requiredItem;

    ReasonOutOfItem(@NotNull ItemStack requiredItem) {
        this.requiredItem = requiredItem;
    }

    @NotNull
    @Override
    public String getID() {
        return "out_of_item";
    }

    @NotNull
    @Override
    public String getDisplayString() {
        return Objects.requireNonNull(
            StatCollector.translateToLocalFormatted(
                "GT5U.gui.text.out_of_item",
                requiredItem.getDisplayName(),
                formatNumber(requiredItem.stackSize)));
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound tag) {
        return requiredItem.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(@NotNull NBTTagCompound tag) {
        requiredItem.readFromNBT(tag);
    }

    @NotNull
    @Override
    public ShutDownReason newInstance() {
        return new ReasonOutOfItem(new ItemStack(Items.feather, 0));
    }

    @Override
    public void encode(@NotNull PacketBuffer buffer) {
        try {
            buffer.writeItemStackToBuffer(requiredItem);
        } catch (Exception ignored) {}
    }

    @Override
    public void decode(PacketBuffer buffer) {
        try {
            requiredItem = buffer.readItemStackFromBuffer();
        } catch (Exception ignored) {}
    }

    @Override
    public boolean wasCritical() {
        return true;
    }

    @Override
    public String getKey() {
        return getID();
    }
}
