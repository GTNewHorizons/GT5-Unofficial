package binnie.extrabees.config;

import binnie.core.mod.config.ConfigFile;
import binnie.core.mod.config.ConfigProperty;
import binnie.core.mod.config.PropBoolean;
import binnie.core.mod.config.PropInteger;

@ConfigFile(filename="/config/forestry/extrabees/main.conf")
public class ConfigurationMain
{
  @ConfigProperty(key="canQuarryMineHives")
  @PropBoolean
  public static boolean canQuarryMineHives = true;
  @ConfigProperty(key="waterHiveRate")
  @PropInteger
  public static int waterHiveRate = 1;
  @ConfigProperty(key="rockHiveRate")
  @PropInteger
  public static int rockHiveRate = 2;
  @ConfigProperty(key="netherHiveRate")
  @PropInteger
  public static int netherHiveRate = 2;
  @ConfigProperty(key="marbleHiveRate")
  @PropInteger
  public static int marbleHiveRate = 2;
}
