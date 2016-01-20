package binnie.core.resource;

import binnie.Binnie;
import binnie.core.AbstractMod;
import binnie.core.BinnieCore;
import binnie.core.proxy.BinnieProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class BinnieIcon
  extends BinnieResource
{
  public BinnieIcon(AbstractMod mod, ResourceType type, String path)
  {
    super(mod, type, path);
    this.textureSheet = (type == ResourceType.Block ? 0 : 1);
    Binnie.Resource.registerIcon(this);
  }
  
  private int textureSheet = 0;
  private IIcon icon = null;
  
  public IIcon getIcon()
  {
    return this.icon;
  }
  
  @SideOnly(Side.CLIENT)
  public IIcon getIcon(IIconRegister register)
  {
    registerIcon(register);
    return this.icon;
  }
  
  @SideOnly(Side.CLIENT)
  public void registerIcon(IIconRegister register)
  {
    this.icon = BinnieCore.proxy.getIcon(register, this.mod, this.path);
  }
  
  public int getTextureSheet()
  {
    return this.textureSheet;
  }
}
