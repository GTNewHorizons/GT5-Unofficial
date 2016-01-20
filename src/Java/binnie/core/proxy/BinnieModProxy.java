package binnie.core.proxy;

import binnie.Binnie;
import binnie.core.AbstractMod;
import binnie.core.BinnieCore;
import binnie.core.gui.IBinnieGUID;
import binnie.core.language.ManagerLanguage;
import binnie.core.network.packet.MessageBase;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.IIcon;

public class BinnieModProxy
  implements IBinnieModProxy
{
  private AbstractMod mod;
  
  public BinnieModProxy(AbstractMod mod)
  {
    this.mod = mod;
  }
  
  public void openGui(IBinnieGUID ID, EntityPlayer player, int x, int y, int z)
  {
    BinnieCore.proxy.openGui(this.mod, ID.ordinal(), player, x, y, z);
  }
  
  public void sendToAll(MessageBase packet)
  {
    this.mod.getNetworkWrapper().sendToAll(packet.GetMessage());
  }
  
  public void sendToPlayer(MessageBase packet, EntityPlayer entityplayer)
  {
    if ((entityplayer instanceof EntityPlayerMP)) {
      this.mod.getNetworkWrapper().sendTo(packet.GetMessage(), (EntityPlayerMP)entityplayer);
    }
  }
  
  public void sendToServer(MessageBase packet)
  {
    this.mod.getNetworkWrapper().sendToServer(packet.GetMessage());
  }
  
  public IIcon getIcon(IIconRegister register, String string)
  {
    return BinnieCore.proxy.getIcon(register, this.mod.getModID(), string);
  }
  
  public void preInit() {}
  
  public void init() {}
  
  public void postInit() {}
  
  public String localise(String string)
  {
    return Binnie.Language.localise(this.mod, string);
  }
  
  public String localiseOrBlank(String string)
  {
    return Binnie.Language.localiseOrBlank(this.mod, string);
  }
}
