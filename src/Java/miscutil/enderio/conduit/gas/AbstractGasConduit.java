package crazypants.enderio.conduit.gas;

import com.enderio.core.common.util.BlockCoord;
import com.enderio.core.common.util.DyeColor;
import cpw.mods.fml.common.Optional.Method;
import crazypants.enderio.conduit.AbstractConduit;
import crazypants.enderio.conduit.ConduitUtil;
import crazypants.enderio.conduit.ConnectionMode;
import crazypants.enderio.conduit.IConduit;
import crazypants.enderio.conduit.IConduitBundle;
import crazypants.enderio.machine.RedstoneControlMode;
import crazypants.enderio.machine.reservoir.TileReservoir;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import mekanism.api.gas.IGasHandler;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class AbstractGasConduit
  extends AbstractConduit
  implements IGasConduit
{
  protected final EnumMap<ForgeDirection, RedstoneControlMode> extractionModes = new EnumMap(ForgeDirection.class);
  protected final EnumMap<ForgeDirection, DyeColor> extractionColors = new EnumMap(ForgeDirection.class);
  protected final Map<ForgeDirection, Integer> externalRedstoneSignals = new HashMap();
  protected boolean redstoneStateDirty = true;
  
  @Optional.Method(modid="MekanismAPI|gas")
  public IGasHandler getExternalHandler(ForgeDirection direction)
  {
    IGasHandler con = GasUtil.getExternalGasHandler(getBundle().getWorld(), getLocation().getLocation(direction));
    return (con != null) && (!(con instanceof IConduitBundle)) ? con : null;
  }
  
  @Optional.Method(modid="MekanismAPI|gas")
  public IGasHandler getTankContainer(BlockCoord bc)
  {
    return GasUtil.getGasHandler(getBundle().getWorld(), bc);
  }
  
  public boolean canConnectToExternal(ForgeDirection direction, boolean ignoreDisabled)
  {
    IGasHandler h = getExternalHandler(direction);
    if (h == null) {
      return false;
    }
    return true;
  }
  
  public Class<? extends IConduit> getBaseConduitType()
  {
    return IGasConduit.class;
  }
  
  public boolean onNeighborBlockChange(Block blockId)
  {
    this.redstoneStateDirty = true;
    return super.onNeighborBlockChange(blockId);
  }
  
  public void setExtractionRedstoneMode(RedstoneControlMode mode, ForgeDirection dir)
  {
    this.extractionModes.put(dir, mode);
    this.redstoneStateDirty = true;
  }
  
  public RedstoneControlMode getExtractionRedstoneMode(ForgeDirection dir)
  {
    RedstoneControlMode res = (RedstoneControlMode)this.extractionModes.get(dir);
    if (res == null) {
      res = RedstoneControlMode.ON;
    }
    return res;
  }
  
  public void setExtractionSignalColor(ForgeDirection dir, DyeColor col)
  {
    this.extractionColors.put(dir, col);
  }
  
  public DyeColor getExtractionSignalColor(ForgeDirection dir)
  {
    DyeColor result = (DyeColor)this.extractionColors.get(dir);
    if (result == null) {
      return DyeColor.RED;
    }
    return result;
  }
  
  public boolean canOutputToDir(ForgeDirection dir)
  {
    if ((isExtractingFromDir(dir)) || (getConnectionMode(dir) == ConnectionMode.DISABLED)) {
      return false;
    }
    if (this.conduitConnections.contains(dir)) {
      return true;
    }
    if (!this.externalConnections.contains(dir)) {
      return false;
    }
    IGasHandler ext = getExternalHandler(dir);
    if ((ext instanceof TileReservoir))
    {
      TileReservoir tr = (TileReservoir)ext;
      return (!tr.isMultiblock()) || (!tr.isAutoEject());
    }
    return true;
  }
  
  protected boolean autoExtractForDir(ForgeDirection dir)
  {
    if (!isExtractingFromDir(dir)) {
      return false;
    }
    RedstoneControlMode mode = getExtractionRedstoneMode(dir);
    if (mode == RedstoneControlMode.IGNORE) {
      return true;
    }
    if (mode == RedstoneControlMode.NEVER) {
      return false;
    }
    if (this.redstoneStateDirty)
    {
      this.externalRedstoneSignals.clear();
      this.redstoneStateDirty = false;
    }
    DyeColor col = getExtractionSignalColor(dir);
    int signal = ConduitUtil.getInternalSignalForColor(getBundle(), col);
    if ((RedstoneControlMode.isConditionMet(mode, signal)) && (mode != RedstoneControlMode.OFF)) {
      return true;
    }
    return isConditionMetByExternalSignal(dir, mode, col);
  }
  
  private boolean isConditionMetByExternalSignal(ForgeDirection dir, RedstoneControlMode mode, DyeColor col)
  {
    int externalSignal = 0;
    if (col == DyeColor.RED)
    {
      Integer val = (Integer)this.externalRedstoneSignals.get(dir);
      if (val == null)
      {
        TileEntity te = getBundle().getEntity();
        externalSignal = te.getWorldObj().getStrongestIndirectPower(te.xCoord, te.yCoord, te.zCoord);
        this.externalRedstoneSignals.put(dir, Integer.valueOf(externalSignal));
      }
      else
      {
        externalSignal = val.intValue();
      }
    }
    return RedstoneControlMode.isConditionMet(mode, externalSignal);
  }
  
  public boolean isExtractingFromDir(ForgeDirection dir)
  {
    return getConnectionMode(dir) == ConnectionMode.INPUT;
  }
  
  public void writeToNBT(NBTTagCompound nbtRoot)
  {
    super.writeToNBT(nbtRoot);
    for (Map.Entry<ForgeDirection, RedstoneControlMode> entry : this.extractionModes.entrySet()) {
      if (entry.getValue() != null)
      {
        short ord = (short)((RedstoneControlMode)entry.getValue()).ordinal();
        nbtRoot.setShort("extRM." + ((ForgeDirection)entry.getKey()).name(), ord);
      }
    }
    for (Map.Entry<ForgeDirection, DyeColor> entry : this.extractionColors.entrySet()) {
      if (entry.getValue() != null)
      {
        short ord = (short)((DyeColor)entry.getValue()).ordinal();
        nbtRoot.setShort("extSC." + ((ForgeDirection)entry.getKey()).name(), ord);
      }
    }
  }
  
  public void readFromNBT(NBTTagCompound nbtRoot, short nbtVersion)
  {
    super.readFromNBT(nbtRoot, nbtVersion);
    for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
    {
      String key = "extRM." + dir.name();
      if (nbtRoot.hasKey(key))
      {
        short ord = nbtRoot.getShort(key);
        if ((ord >= 0) && (ord < RedstoneControlMode.values().length)) {
          this.extractionModes.put(dir, RedstoneControlMode.values()[ord]);
        }
      }
      key = "extSC." + dir.name();
      if (nbtRoot.hasKey(key))
      {
        short ord = nbtRoot.getShort(key);
        if ((ord >= 0) && (ord < DyeColor.values().length)) {
          this.extractionColors.put(dir, DyeColor.values()[ord]);
        }
      }
    }
  }
}
