package miscutil.core.multiblock.test;

import miscutil.core.multiblock.abstracts.AbstractMultiblockLogic;
import miscutil.core.multiblock.abstracts.AbstractMultiblockTE;
import miscutil.core.multiblock.base.interfaces.IBaseMultiblockController;
import net.minecraft.util.ChunkCoordinates;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.multiblock.IAlvearyComponent;
import forestry.core.access.IRestrictedAccess;
import forestry.core.gui.IHintSource;
import forestry.core.network.IStreamableGui;
import forestry.core.tiles.IClimatised;
import forestry.core.tiles.ITitled;

public abstract class TileAlveary
  extends AbstractMultiblockTE<AbstractMultiblockLogic>
  implements IBeeHousing, IAlvearyComponent, IRestrictedAccess, IStreamableGui, ITitled, IClimatised, IHintSource
{
	
	 public static enum Type
	  {
	    PLAIN,  ENTRANCE,  SWARMER,  FAN,  HEATER,  HYGRO,  STABILIZER,  SIEVE;
	    
	    public static final Type[] VALUES = values();
	    
	    private Type() {}
	  }
	
  private final String unlocalizedTitle;
  protected TileAlveary()
  {
   this(Type.PLAIN);
  }
  
  protected TileAlveary(Type type)
  {
    super(new AbstractMultiblockLogic());
    this.unlocalizedTitle = ("tile.for.alveary." + type.ordinal() + ".name");
  }
  
  public int getIcon(int side)
  {
    return 0;
  }
  
  @Override
public void onMachineAssembled(IBaseMultiblockController multiblockController, ChunkCoordinates minCoord, ChunkCoordinates maxCoord)
  {
    if (this.worldObj.isRemote) {
      this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }
    this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, getBlockType());
    markDirty();
  }
  
  @Override
public void onMachineBroken()
  {
    if (this.worldObj.isRemote) {
      this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }
    this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, getBlockType());
    markDirty();
  }
  
}
