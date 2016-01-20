package binnie.extrabees.worldgen;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class MaterialBeehive
  extends Material
{
  public MaterialBeehive()
  {
    super(MapColor.stoneColor);
    setRequiresTool();
    setImmovableMobility();
  }
  
  public boolean isOpaque()
  {
    return true;
  }
}
