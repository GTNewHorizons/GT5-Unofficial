package miscutil.enderio.conduit.gas;

import mekanism.api.gas.IGasHandler;
import net.minecraftforge.common.util.ForgeDirection;
import crazypants.enderio.conduit.IConduit;
import crazypants.enderio.conduit.IExtractor;

public abstract interface IGasConduit
  extends IConduit, IGasHandler, IExtractor
{
  public abstract boolean canOutputToDir(ForgeDirection paramForgeDirection);
  
  public abstract boolean isExtractingFromDir(ForgeDirection paramForgeDirection);
}
