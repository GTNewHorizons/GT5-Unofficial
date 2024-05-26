package gregtech.api.net;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.items.GT_MetaGenerated_Tool;
import io.netty.buffer.ByteBuf;

public class GT_Packet_ToolSwitchMode extends GT_Packet_New {

    private EntityPlayerMP player;

    public GT_Packet_ToolSwitchMode() {
        super(true);
    }

    @Override
    public byte getPacketID() {
        return 20;
    }

    @Override
    public void encode(ByteBuf aOut) {

    }

    @Override
    public GT_Packet_New decode(ByteArrayDataInput aData) {
        return new GT_Packet_ToolSwitchMode();
    }

    @Override
    public void setINetHandler(INetHandler aHandler) {
        player = ((NetHandlerPlayServer) aHandler).playerEntity;
    }

    @Override
    public void process(IBlockAccess aWorld) {
        ItemStack currentItem = player.inventory.getCurrentItem();
        if (currentItem == null || (!(currentItem.getItem() instanceof GT_MetaGenerated_Tool item))) return;
        byte maxMode = item.getToolMaxMode(currentItem);
        if (maxMode <= 1) return;
        byte newMode = (byte) ((GT_MetaGenerated_Tool.getToolMode(currentItem) + 1) % maxMode);
        GT_MetaGenerated_Tool.setToolMode(currentItem, newMode);
        player.sendSlotContents(player.inventoryContainer, player.inventory.currentItem, currentItem);
    }
}
