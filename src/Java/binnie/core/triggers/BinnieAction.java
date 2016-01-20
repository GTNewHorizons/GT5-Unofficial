package binnie.core.triggers;

import binnie.Binnie;
import binnie.core.AbstractMod;
import binnie.core.BinnieCore;
import binnie.core.resource.BinnieIcon;
import binnie.core.resource.ManagerResource;
import buildcraft.api.statements.IActionExternal;
import buildcraft.api.statements.IStatement;
import buildcraft.api.statements.IStatementContainer;
import buildcraft.api.statements.IStatementParameter;
import buildcraft.api.statements.StatementManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

class BinnieAction
  implements IActionExternal
{
  private static int incrementalID = 800;
  public static BinnieAction actionPauseProcess;
  public static BinnieAction actionCancelTask;
  private String desc;
  private BinnieIcon icon;
  private String tag;
  private int id = 0;
  
  BinnieAction(String desc, String tag, String iconFile)
  {
    this(desc, tag, BinnieCore.instance, iconFile);
  }
  
  private BinnieAction(String desc, String tag, AbstractMod mod, String iconFile)
  {
    this.id = (incrementalID++);
    this.tag = tag;
    StatementManager.registerStatement(this);
    this.icon = Binnie.Resource.getItemIcon(mod, iconFile);
    this.desc = desc;
  }
  
  public String getDescription()
  {
    return this.desc;
  }
  
  public String getUniqueTag()
  {
    return this.tag;
  }
  
  @SideOnly(Side.CLIENT)
  public IIcon getIcon()
  {
    return this.icon.getIcon();
  }
  
  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister iconRegister)
  {
    this.icon.registerIcon(iconRegister);
  }
  
  public int maxParameters()
  {
    return 0;
  }
  
  public int minParameters()
  {
    return 0;
  }
  
  public IStatementParameter createParameter(int index)
  {
    return null;
  }
  
  public IStatement rotateLeft()
  {
    return this;
  }
  
  public void actionActivate(TileEntity target, ForgeDirection side, IStatementContainer source, IStatementParameter[] parameters) {}
}
