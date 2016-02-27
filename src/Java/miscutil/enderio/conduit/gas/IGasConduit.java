package crazypants.enderio.conduit.gas;

import cpw.mods.fml.common.Optional.Interface;
import crazypants.enderio.conduit.IConduit;
import crazypants.enderio.conduit.IExtractor;
import mekanism.api.gas.IGasHandler;
import net.minecraftforge.common.util.ForgeDirection;

@Optional.Interface(iface="mekanism.api.gas.IGasHandler", modid="MekanismAPI|gas")
public abstract interface IGasConduit
  extends IConduit, IGasHandler, IExtractor
{
  public abstract boolean canOutputToDir(ForgeDirection paramForgeDirection);
  
  public abstract boolean isExtractingFromDir(ForgeDirection paramForgeDirection);
}
