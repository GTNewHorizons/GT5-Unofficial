package binnie.extrabees.apiary;

import binnie.Binnie;
import binnie.core.BinnieCore;
import binnie.core.genetics.ManagerGenetics;
import binnie.core.machines.Machine;
import binnie.core.machines.TileEntityMachine;
import binnie.core.proxy.BinnieProxy;
import binnie.extrabees.apiary.machine.AlvearyMachine.AlvearyPackage;
import forestry.api.apiculture.IAlvearyComponent;
import forestry.api.apiculture.IBee;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.apiculture.IBeeListener;
import forestry.api.apiculture.IBeeModifier;
import forestry.api.apiculture.IBeeRoot;
import forestry.api.core.IStructureLogic;
import forestry.api.core.ITileStructure;
import forestry.api.genetics.IIndividual;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileExtraBeeAlveary
  extends TileEntityMachine
  implements IAlvearyComponent, IBeeModifier, IBeeListener
{
  boolean init = false;
  IStructureLogic structureLogic;
  private boolean isMaster;
  protected int masterX;
  protected int masterZ;
  
  public void updateEntity()
  {
    super.updateEntity();
    if (!BinnieCore.proxy.isSimulating(this.worldObj)) {
      return;
    }
    if (this.worldObj.getWorldTime() % 200L == 0L)
    {
      if ((!isIntegratedIntoStructure()) || (isMaster())) {
        validateStructure();
      }
      ITileStructure master = getCentralTE();
      if (master == null) {
        return;
      }
      if (getBeeListener() != null) {
        ((IAlvearyComponent)master).registerBeeListener(getBeeListener());
      }
      if (getBeeModifier() != null) {
        ((IAlvearyComponent)master).registerBeeModifier(getBeeModifier());
      }
      this.init = true;
    }
  }
  
  public void readFromNBT(NBTTagCompound nbttagcompound)
  {
    super.readFromNBT(nbttagcompound);
    
    this.isMaster = nbttagcompound.getBoolean("IsMaster");
    this.masterX = nbttagcompound.getInteger("MasterX");
    this.masterY = nbttagcompound.getInteger("MasterY");
    this.masterZ = nbttagcompound.getInteger("MasterZ");
    if (this.isMaster) {
      makeMaster();
    }
    this.structureLogic.readFromNBT(nbttagcompound);
    
    updateAlvearyBlocks();
    
    this.init = false;
  }
  
  public void writeToNBT(NBTTagCompound nbttagcompound)
  {
    super.writeToNBT(nbttagcompound);
    
    nbttagcompound.setBoolean("IsMaster", this.isMaster);
    nbttagcompound.setInteger("MasterX", this.masterX);
    nbttagcompound.setInteger("MasterY", this.masterY);
    nbttagcompound.setInteger("MasterZ", this.masterZ);
    

    this.structureLogic.writeToNBT(nbttagcompound);
  }
  
  AlvearyMachine.AlvearyPackage getAlvearyPackage()
  {
    return (AlvearyMachine.AlvearyPackage)getMachine().getPackage();
  }
  
  public TileExtraBeeAlveary()
  {
    this.structureLogic = Binnie.Genetics.getBeeRoot().createAlvearyStructureLogic(this);
  }
  
  public TileExtraBeeAlveary(AlvearyMachine.AlvearyPackage alvearyPackage)
  {
    super(alvearyPackage);
    this.structureLogic = Binnie.Genetics.getBeeRoot().createAlvearyStructureLogic(this);
  }
  
  public String getTypeUID()
  {
    return this.structureLogic.getTypeUID();
  }
  
  protected int masterY = -99;
  
  public void makeMaster() {}
  
  public void onStructureReset()
  {
    setCentralTE(null);
    this.isMaster = false;
    this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    updateAlvearyBlocks();
  }
  
  public ITileStructure getCentralTE()
  {
    if ((this.worldObj == null) || (!isIntegratedIntoStructure())) {
      return null;
    }
    if (!isMaster())
    {
      TileEntity tile = this.worldObj.getTileEntity(this.masterX, this.masterY, this.masterZ);
      if ((tile instanceof ITileStructure))
      {
        ITileStructure master = (ITileStructure)this.worldObj.getTileEntity(this.masterX, this.masterY, this.masterZ);
        if (master.isMaster()) {
          return master;
        }
        return null;
      }
      return null;
    }
    return this;
  }
  
  public void validateStructure()
  {
    this.structureLogic.validateStructure();
    updateAlvearyBlocks();
  }
  
  private boolean isSameTile(TileEntity tile)
  {
    return (tile.xCoord == this.xCoord) && (tile.yCoord == this.yCoord) && (tile.zCoord == this.zCoord);
  }
  
  public void setCentralTE(TileEntity tile)
  {
    if ((tile == null) || (tile == this) || (isSameTile(tile)))
    {
      this.masterX = (this.masterZ = 0);
      this.masterY = -99;
      updateAlvearyBlocks();
      return;
    }
    this.isMaster = false;
    this.masterX = tile.xCoord;
    this.masterY = tile.yCoord;
    this.masterZ = tile.zCoord;
    

    markDirty();
    if (getBeeListener() != null) {
      ((IAlvearyComponent)tile).registerBeeListener(getBeeListener());
    }
    if (getBeeModifier() != null) {
      ((IAlvearyComponent)tile).registerBeeModifier(getBeeModifier());
    }
    updateAlvearyBlocks();
  }
  
  public boolean isMaster()
  {
    return this.isMaster;
  }
  
  protected boolean hasMaster()
  {
    return this.masterY >= 0;
  }
  
  public boolean isIntegratedIntoStructure()
  {
    return (this.isMaster) || (this.masterY >= 0);
  }
  
  public void registerBeeModifier(IBeeModifier modifier) {}
  
  public void removeBeeModifier(IBeeModifier modifier) {}
  
  public void addTemperatureChange(float change, float boundaryDown, float boundaryUp) {}
  
  public void addHumidityChange(float change, float boundaryDown, float boundaryUp) {}
  
  public boolean hasFunction()
  {
    return true;
  }
  
  public IBeeModifier getBeeModifier()
  {
    return (IBeeModifier)getMachine().getInterface(IBeeModifier.class);
  }
  
  public IBeeListener getBeeListener()
  {
    return (IBeeListener)getMachine().getInterface(IBeeListener.class);
  }
  
  public float getTerritoryModifier(IBeeGenome genome, float currentModifier)
  {
    return getBeeModifier() == null ? 1.0F : getBeeModifier().getTerritoryModifier(genome, currentModifier);
  }
  
  public float getMutationModifier(IBeeGenome genome, IBeeGenome mate, float currentModifier)
  {
    return getBeeModifier() == null ? 1.0F : getBeeModifier().getMutationModifier(genome, mate, currentModifier);
  }
  
  public float getLifespanModifier(IBeeGenome genome, IBeeGenome mate, float currentModifier)
  {
    return getBeeModifier() == null ? 1.0F : getBeeModifier().getLifespanModifier(genome, mate, currentModifier);
  }
  
  public float getProductionModifier(IBeeGenome genome, float currentModifier)
  {
    return getBeeModifier() == null ? 1.0F : getBeeModifier().getProductionModifier(genome, currentModifier);
  }
  
  public float getFloweringModifier(IBeeGenome genome, float currentModifier)
  {
    return getBeeModifier() == null ? 1.0F : getBeeModifier().getFloweringModifier(genome, currentModifier);
  }
  
  public boolean isSealed()
  {
    return getBeeModifier() == null ? false : getBeeModifier().isSealed();
  }
  
  public boolean isSelfLighted()
  {
    return getBeeModifier() == null ? false : getBeeModifier().isSelfLighted();
  }
  
  public boolean isSunlightSimulated()
  {
    return getBeeModifier() == null ? false : getBeeModifier().isSunlightSimulated();
  }
  
  public boolean isHellish()
  {
    return getBeeModifier() == null ? false : getBeeModifier().isHellish();
  }
  
  public void registerBeeListener(IBeeListener event) {}
  
  public void removeBeeListener(IBeeListener event) {}
  
  public void onQueenChange(ItemStack queen)
  {
    if (getBeeListener() != null) {
      getBeeListener().onQueenChange(queen);
    }
  }
  
  public void wearOutEquipment(int amount)
  {
    if (getBeeListener() != null) {
      getBeeListener().wearOutEquipment(amount);
    }
  }
  
  public void onQueenDeath(IBee queen)
  {
    if (getBeeListener() != null) {
      getBeeListener().onQueenDeath(queen);
    }
  }
  
  public void onPostQueenDeath(IBee queen)
  {
    if (getBeeListener() != null) {
      getBeeListener().onPostQueenDeath(queen);
    }
  }
  
  public boolean onPollenRetrieved(IBee queen, IIndividual pollen, boolean isHandled)
  {
    return false;
  }
  
  public boolean onEggLaid(IBee queen)
  {
    return false;
  }
  
  public float getGeneticDecay(IBeeGenome genome, float currentModifier)
  {
    return 1.0F;
  }
  
  public IBeeHousing getBeeHousing()
  {
    return getCentralTE() == null ? null : (IBeeHousing)getCentralTE();
  }
  
  List<TileEntity> tiles = new ArrayList();
  
  public List<TileEntity> getAlvearyBlocks()
  {
    updateAlvearyBlocks();
    return this.tiles;
  }
  
  private void updateAlvearyBlocks()
  {
    this.tiles.clear();
    if (getCentralTE() != null)
    {
      ITileStructure struct = getCentralTE();
      if (!struct.isIntegratedIntoStructure()) {
        return;
      }
      TileEntity central = (TileEntity)struct;
      for (int x = -2; x <= 2; x++) {
        for (int z = -2; z <= 2; z++) {
          for (int y = -2; y <= 2; y++)
          {
            TileEntity tile = getWorldObj().getTileEntity(this.xCoord + x, this.yCoord + y, this.zCoord + z);
            if ((tile != null) && ((tile instanceof ITileStructure)) && (((ITileStructure)tile).getCentralTE() == struct)) {
              this.tiles.add(tile);
            }
          }
        }
      }
    }
  }
  
  public ISidedInventory getStructureInventory()
  {
    return (ISidedInventory)getMachine().getInterface(ISidedInventory.class);
  }
}
