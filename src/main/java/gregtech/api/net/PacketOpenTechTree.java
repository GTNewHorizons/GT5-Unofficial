package gregtech.api.net;

import net.minecraft.entity.player.EntityPlayerMP;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import gregtech.common.misc.techtree.gui.TechTreeGuiFactory;
import io.netty.buffer.ByteBuf;

public class PacketOpenTechTree implements IMessage, IMessageHandler<PacketOpenTechTree, IMessage> {

    @Override
    public void fromBytes(ByteBuf buf) {
        // No need to do anything here
    }

    @Override
    public void toBytes(ByteBuf buf) {
        // No need to do anything here
    }

    @Override
    public IMessage onMessage(PacketOpenTechTree message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        TechTreeGuiFactory.open(player, player.serverPosX, player.serverPosY, player.serverPosZ);
        return null;
    }
}
