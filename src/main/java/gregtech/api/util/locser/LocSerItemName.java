package gregtech.api.util.locser;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

import com.cleanroommc.modularui.network.NetworkUtils;

public class LocSerItemName implements ILocSer {

    public ItemStack stack = null;

    public LocSerItemName() {}

    public LocSerItemName(ItemStack aStack) {
        stack = aStack;
    }

    @Override
    public void encode(PacketBuffer out) {
        NetworkUtils.writeStringSafe(out, getId());
        NetworkUtils.writeItemStack(out, stack);
    }

    @Override
    public void decode(PacketBuffer in) {
        this.stack = NetworkUtils.readItemStack(in);
    }

    @Override
    public String localize() {
        return stack.getDisplayName();
    }

    @Override
    public String getId() {
        return "gt:itemName";
    }

}
