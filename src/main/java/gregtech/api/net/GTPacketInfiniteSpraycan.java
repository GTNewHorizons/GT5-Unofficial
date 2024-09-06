package gregtech.api.net;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.enums.SoundResource;
import gregtech.api.items.MetaBaseItem;
import gregtech.api.util.GTUtility;
import gregtech.common.items.behaviors.BehaviourSprayColorInfinite;
import io.netty.buffer.ByteBuf;

public class GTPacketInfiniteSpraycan extends GTPacketNew {

    private boolean wasSneaking;
    private EntityPlayerMP player;

    public GTPacketInfiniteSpraycan() {
        super(true);
    }

    public GTPacketInfiniteSpraycan(boolean wasSneaking) {
        super(false);
        this.wasSneaking = wasSneaking;
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.INFINITE_SPRAYCAN.id;
    }

    @Override
    public void encode(final ByteBuf aOut) {
        aOut.writeBoolean(wasSneaking);
    }

    @Override
    public GTPacketNew decode(final ByteArrayDataInput aData) {
        return new GTPacketInfiniteSpraycan(aData.readBoolean());
    }

    @Override
    public void setINetHandler(final INetHandler aHandler) {
        player = ((NetHandlerPlayServer) aHandler).playerEntity;
    }

    @Override
    public void process(final IBlockAccess aWorld) {
        ItemStack currentItemStack = player.inventory.getCurrentItem();
        if (currentItemStack != null && currentItemStack.getItem() instanceof MetaBaseItem item) {
            item.forEachBehavior(currentItemStack, behavior -> {
                if (behavior instanceof BehaviourSprayColorInfinite spraycanBehavior) {
                    spraycanBehavior.setNewColor(currentItemStack, wasSneaking);
                    player.sendSlotContents(player.inventoryContainer, player.inventory.currentItem, currentItemStack);

                    GTUtility.sendSoundToPlayers(
                        player.worldObj,
                        SoundResource.GT_SPRAYCAN_SHAKE,
                        1.0F,
                        1,
                        (int) player.posX,
                        (int) player.posY,
                        (int) player.posZ);

                    return true;
                }

                return false;
            });
        }
    }
}
