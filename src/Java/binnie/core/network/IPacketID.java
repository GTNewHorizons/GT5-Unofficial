package binnie.core.network;

import binnie.core.network.packet.MessageBinnie;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public abstract interface IPacketID
  extends IOrdinaled
{
  public abstract void onMessage(MessageBinnie paramMessageBinnie, MessageContext paramMessageContext);
}
