package gregtech.crossmod.ae2;

import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

import com.gtnewhorizon.gtnhlib.chat.AbstractChatComponentCustom;
import com.gtnewhorizon.gtnhlib.chat.customcomponents.AbstractChatComponentBuffer;

import cpw.mods.fml.common.network.ByteBufUtils;
import gregtech.api.enums.Mods;
import gregtech.api.metatileentity.CommonBaseMetaTileEntity;
import gregtech.common.config.Gregtech;

/**
 * Carries the non-consumed items of a machine, molds and shapes and the like, so that both their display names and the
 * viewing client's own {@link Gregtech.Machines#itemSlotsSuffixFormat} are resolved on the client. Names are built on
 * the server otherwise, where client language files are not loaded.
 */
public class ChatComponentNonConsumedItemsSuffix
    extends AbstractChatComponentBuffer<ChatComponentNonConsumedItemsSuffix> {

    public List<ItemStack> items = new ArrayList<>();

    public ChatComponentNonConsumedItemsSuffix() {}

    public ChatComponentNonConsumedItemsSuffix(List<ItemStack> items) {
        this.items = items;
    }

    @Override
    public String getID() {
        return Mods.GregTech.ID + ":ChatComponentNonConsumedItemsSuffix";
    }

    @Override
    protected AbstractChatComponentCustom copySelf() {
        List<ItemStack> copy = new ArrayList<>(items.size());
        for (ItemStack stack : items) {
            copy.add(stack == null ? null : stack.copy());
        }
        return new ChatComponentNonConsumedItemsSuffix(copy);
    }

    @Override
    public String getUnformattedTextForChat() {
        String joined = items.stream()
            .filter(stack -> stack != null)
            .map(CommonBaseMetaTileEntity::getShortItemDisplayName)
            .collect(Collectors.joining(", "));
        if (joined.isEmpty()) return "";
        try {
            return String.format(Gregtech.machines.itemSlotsSuffixFormat, joined);
        } catch (IllegalFormatException e) {
            return "";
        }
    }

    @Override
    public void encode(PacketBuffer buf) {
        buf.writeVarIntToBuffer(items.size());
        for (ItemStack stack : items) {
            ByteBufUtils.writeItemStack(buf, stack);
        }
    }

    @Override
    public void decode(PacketBuffer buf) {
        final int size = buf.readVarIntFromBuffer();
        items = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            items.add(ByteBufUtils.readItemStack(buf));
        }
    }
}
