package binnie.core.machines;

import binnie.Binnie;
import binnie.core.language.ManagerLanguage;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.tileentity.TileEntity;

public abstract class MachinePackage
{
  private String uid;
  private boolean active = true;
  boolean powered = false;
  private int metadata = -1;
  private MachineGroup group;
  
  public String getUID()
  {
    return this.uid;
  }
  
  protected MachinePackage(String uid, boolean powered)
  {
    this.uid = uid;
    this.powered = powered;
  }
  
  public abstract void createMachine(Machine paramMachine);
  
  public abstract TileEntity createTileEntity();
  
  public abstract void register();
  
  public final String getDisplayName()
  {
    return Binnie.Language.localise(this.group.getMod(), "machine." + this.group.getShortUID() + "." + getUID());
  }
  
  public final Integer getMetadata()
  {
    return Integer.valueOf(this.metadata);
  }
  
  public void assignMetadata(int meta)
  {
    this.metadata = meta;
  }
  
  public MachineGroup getGroup()
  {
    return this.group;
  }
  
  public void setGroup(MachineGroup group)
  {
    this.group = group;
  }
  
  public abstract void renderMachine(Machine paramMachine, double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat, RenderBlocks paramRenderBlocks);
  
  public boolean isActive()
  {
    return this.active;
  }
  
  public void setActive(boolean active)
  {
    this.active = active;
  }
  
  public final String getInformation()
  {
    return Binnie.Language.localise(this.group.getMod(), "machine." + this.group.getShortUID() + "." + getUID() + ".info");
  }
}
