package binnie.core.proxy;

import binnie.core.gui.IBinnieGUID;
import binnie.core.network.packet.MessageBase;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;

abstract interface IBinnieModProxy
  extends IProxyCore
{
  public abstract void openGui(IBinnieGUID paramIBinnieGUID, EntityPlayer paramEntityPlayer, int paramInt1, int paramInt2, int paramInt3);
  
  public abstract void sendToAll(MessageBase paramMessageBase);
  
  public abstract void sendToPlayer(MessageBase paramMessageBase, EntityPlayer paramEntityPlayer);
  
  public abstract void sendToServer(MessageBase paramMessageBase);
  
  public abstract IIcon getIcon(IIconRegister paramIIconRegister, String paramString);
}
