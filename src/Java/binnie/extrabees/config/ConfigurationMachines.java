package binnie.extrabees.config;

import binnie.core.mod.config.ConfigFile;
import binnie.core.mod.config.ConfigProperty;
import binnie.core.mod.config.PropDouble;
import binnie.core.mod.config.PropPercentage;

@ConfigFile(filename="/config/forestry/extrabees/machines.conf")
public class ConfigurationMachines
{
  @ConfigProperty(key="isolatorConsumptionChance", comment={"Percentage chance of Isolator consuming bee, in x%."})
  @PropPercentage
  public static int isolatorConsumptionChance = 30;
  @ConfigProperty(key="geneticErrorModifier", comment={"Modifier that changes how severe splicer/inoculator erros are.", "0 would result in no errors, even for awful serums.", "2.0 would result in twice as severe errors"})
  @PropDouble
  public static double geneticErrorModifier = 1.0D;
}
