package binnie.core.mod.config;

import binnie.core.AbstractMod;
import binnie.core.BinnieCore;
import binnie.core.proxy.BinnieProxy;
import java.io.File;
import net.minecraftforge.common.config.Configuration;

class BinnieConfiguration
  extends Configuration
{
  public AbstractMod mod;
  private String filename;
  
  public BinnieConfiguration(String filename, AbstractMod mod)
  {
    super(new File(BinnieCore.proxy.getDirectory(), filename));
    this.mod = mod;
    this.filename = filename;
  }
}
