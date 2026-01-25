package gregtech.api.net;

import java.nio.charset.StandardCharsets;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.enums.SoundResource;
import gregtech.api.util.GTUtility;
import gregtech.common.items.ItemToolbox;
import io.netty.buffer.ByteBuf;

public class GTPacketToolboxEvent extends GTPacket {

    private Action action;
    private int slot;
    private EntityPlayerMP player;

    public GTPacketToolboxEvent() {
        super();
    }

    public GTPacketToolboxEvent(final Action action, int slot) {
        super();
        this.action = action;
        this.slot = slot;
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.TOOLBOX_EVENT.id;
    }

    @Override
    public void encode(final ByteBuf buffer) {
        final byte[] name = action.name()
            .getBytes(StandardCharsets.UTF_8);

        buffer.writeInt(slot);
        buffer.writeInt(name.length);
        buffer.writeBytes(name);
    }

    @Override
    public GTPacket decode(final ByteArrayDataInput buffer) {
        final int slot = buffer.readInt();
        final int length = buffer.readInt();
        final byte[] name = new byte[length];
        buffer.readFully(name, 0, length);

        return new GTPacketToolboxEvent(
            GTPacketToolboxEvent.Action.valueOf(new String(name, StandardCharsets.UTF_8)),
            slot);
    }

    @Override
    public void process(final IBlockAccess world) {
        final ItemStack itemStack = player.inventory.getStackInSlot(slot);
        if (itemStack == null) {
            return;
        }

        action.execute(player, itemStack, slot);
    }

    @Override
    public void setINetHandler(final INetHandler handler) {
        player = ((NetHandlerPlayServer) handler).playerEntity;
    }

    public enum Action {

        UI_OPEN() {

            @Override
            void execute(final EntityPlayerMP player, final ItemStack itemStack, int slot) {
                final NBTTagCompound tag = itemStack.hasTagCompound() ? itemStack.getTagCompound()
                    : new NBTTagCompound();
                tag.setBoolean(ItemToolbox.TOOLBOX_OPEN_NBT_KEY, true);
                itemStack.setTagCompound(tag);

                player.inventory.setInventorySlotContents(slot, itemStack);
                GTUtility.sendSoundToPlayers(
                    player.worldObj,
                    SoundResource.GT_TOOLBOX_OPEN,
                    2.0F,
                    1,
                    player.posX,
                    player.posY,
                    player.posZ);
            }
        },

        CHANGE_ACTIVE_TOOL() {

            @Override
            void execute(final EntityPlayerMP player, final ItemStack itemStack, final int slot) {
                final NBTTagCompound tag = itemStack.hasTagCompound() ? itemStack.getTagCompound()
                    : new NBTTagCompound();
                tag.setInteger(ItemToolbox.CURRENT_TOOL_NBT_KEY, slot);
                player.inventory.setInventorySlotContents(slot, itemStack);

                // TODO: Play sound (toolbox jangling as you rummage around or something)
            }
        },

        ;

        abstract void execute(final EntityPlayerMP player, final ItemStack itemStack, final int slot);
    }
}
