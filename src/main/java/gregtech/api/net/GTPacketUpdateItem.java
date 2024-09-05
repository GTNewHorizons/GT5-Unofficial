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
public class GTPacketUpdateItem extends GTPacketNew {

    private NBTTagCompound tag;
    private EntityPlayerMP mPlayer;

    public GTPacketUpdateItem() {
        super(true);
    }

    public GTPacketUpdateItem(NBTTagCompound tag) {
        super(false);
        this.tag = tag;
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.UPDATE_ITEM.id;
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
    public GTPacketNew decode(ByteArrayDataInput aData) {
        return new GTPacketUpdateItem(ISerializableObject.readCompoundTagFromGreggyByteBuf(aData));
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
