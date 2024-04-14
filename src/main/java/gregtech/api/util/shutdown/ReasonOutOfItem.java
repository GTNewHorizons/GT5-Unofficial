package gregtech.api.util.shutdown;

import static gregtech.api.util.GT_Utility.formatNumbers;

import java.util.Objects;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
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
                formatNumbers(requiredItem.stackSize)));
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
}
