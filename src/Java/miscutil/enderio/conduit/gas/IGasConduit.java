package crazypants.enderio.conduit.gas;

import crazypants.enderio.conduit.IConduit;
import crazypants.enderio.conduit.IExtractor;
import mekanism.api.gas.IGasHandler;
import net.minecraftforge.common.util.ForgeDirection;

public abstract interface IGasConduit
  extends IConduit, IGasHandler, IExtractor
{
  public abstract boolean canOutputToDir(ForgeDirection paramForgeDirection);
  
  public abstract boolean isExtractingFromDir(ForgeDirection paramForgeDirection);
}
