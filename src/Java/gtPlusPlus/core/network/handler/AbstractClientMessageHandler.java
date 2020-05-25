package gtPlusPlus.core.network.handler;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayer;

public abstract class AbstractClientMessageHandler<T extends IMessage> extends AbstractMessageHandler<T> {
	
	public final IMessage handleServerMessage(EntityPlayer player, T message, MessageContext ctx) {
		return null;
	}
	
}
