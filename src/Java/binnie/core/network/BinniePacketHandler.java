package binnie.core.network;

import binnie.core.AbstractMod;
import binnie.core.network.packet.MessageBinnie;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public abstract class BinniePacketHandler
  implements IMessageHandler<MessageBinnie, IMessage>
{
  private IPacketProvider provider;
  
  public BinniePacketHandler(AbstractMod mod)
  {
    setProvider(mod);
  }
  
  public void setProvider(IPacketProvider provider)
  {
    this.provider = provider;
  }
  
  public IMessage onMessage(MessageBinnie message, MessageContext ctx)
  {
    try
    {
      int packetId = message.id;
      for (IPacketID id : this.provider.getPacketIDs()) {
        if (id.ordinal() == packetId)
        {
          id.onMessage(message, ctx);
          return null;
        }
      }
    }
    catch (Exception ex)
    {
      throw new RuntimeException(ex);
    }
    return null;
  }
}
