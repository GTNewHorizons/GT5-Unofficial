package binnie.core.network.packet;

import binnie.core.network.BinnieCorePacketID;
import net.minecraft.nbt.NBTTagCompound;

public class MessageCraftGUI
  extends MessageNBT
{
  public MessageCraftGUI(MessageBinnie message)
  {
    super(message);
  }
  
  public MessageCraftGUI(NBTTagCompound action)
  {
    super(BinnieCorePacketID.CraftGUIAction.ordinal(), action);
  }
}
