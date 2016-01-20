package binnie.extrabees.worldgen;

import forestry.api.apiculture.IHiveDrop;
import java.util.ArrayList;
import java.util.List;

public enum EnumHiveType
{
  Water,  Rock,  Nether,  Marble;
  
  public List<IHiveDrop> drops = new ArrayList();
  
  private EnumHiveType() {}
}
