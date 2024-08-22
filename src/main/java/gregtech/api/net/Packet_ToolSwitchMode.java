package gregtech.api.net;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.items.MetaGeneratedTool;
import io.netty.buffer.ByteBuf;

public class Packet_ToolSwitchMode extends Packet_New {

    private EntityPlayerMP player;

    public Packet_ToolSwitchMode() {
        super(true);
    }

    @Override
    public byte getPacketID() {
        return PacketTypes.TOOL_SWITCH_MODE.id;
    }

    @Override
    public void encode(ByteBuf aOut) {

    }

    @Override
    public Packet_New decode(ByteArrayDataInput aData) {
        return new Packet_ToolSwitchMode();
    }

    @Override
    public void setINetHandler(INetHandler aHandler) {
        player = ((NetHandlerPlayServer) aHandler).playerEntity;
    }

    @Override
    public void process(IBlockAccess aWorld) {
        ItemStack currentItem = player.inventory.getCurrentItem();
        if (currentItem == null || (!(currentItem.getItem() instanceof MetaGeneratedTool item))) return;
        byte maxMode = item.getToolMaxMode(currentItem);
        if (maxMode <= 1) return;
        byte newMode = (byte) ((MetaGeneratedTool.getToolMode(currentItem) + 1) % maxMode);
        MetaGeneratedTool.setToolMode(currentItem, newMode);
        player.sendSlotContents(player.inventoryContainer, player.inventory.currentItem, currentItem);
    }
}
