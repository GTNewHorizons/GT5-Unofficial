package gregtech.api.net;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.ByteBufUtils;
import gregtech.api.interfaces.INetworkUpdatableItem;
import gregtech.api.util.ISerializableObject;
import io.netty.buffer.ByteBuf;

/**
 * Client -> Server: send arbitrary data to server and update the currently held item.
 */
public class GT_Packet_UpdateItem extends GT_Packet_New {

    private NBTTagCompound tag;
    private EntityPlayerMP mPlayer;

    public GT_Packet_UpdateItem() {
        super(true);
    }

    public GT_Packet_UpdateItem(NBTTagCompound tag) {
        super(false);
        this.tag = tag;
    }

    @Override
    public byte getPacketID() {
        return 13;
    }

    @Override
    public void setINetHandler(INetHandler aHandler) {
        if (aHandler instanceof NetHandlerPlayServer) {
            mPlayer = ((NetHandlerPlayServer) aHandler).playerEntity;
        }
    }

    @Override
    public void encode(ByteBuf aOut) {
        ByteBufUtils.writeTag(aOut, tag);
    }

    @Override
    public GT_Packet_New decode(ByteArrayDataInput aData) {
        return new GT_Packet_UpdateItem(ISerializableObject.readCompoundTagFromGreggyByteBuf(aData));
    }

    @Override
    public void process(IBlockAccess aWorld) {
        if (mPlayer == null) return;
        ItemStack stack = mPlayer.inventory.getCurrentItem();
        if (stack != null && stack.getItem() instanceof INetworkUpdatableItem) {
            ((INetworkUpdatableItem) stack.getItem()).receive(stack, mPlayer, tag);
        }
    }
}
