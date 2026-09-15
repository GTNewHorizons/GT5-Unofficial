package gtPlusPlus.core.network.handler;

import net.minecraft.entity.player.EntityPlayer;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public abstract class AbstractServerMessageHandler<T extends IMessage> extends AbstractMessageHandler<T> {

    @Override
    public final IMessage handleClientMessage(EntityPlayer player, T message, MessageContext ctx) {
        return null;
    }
}
