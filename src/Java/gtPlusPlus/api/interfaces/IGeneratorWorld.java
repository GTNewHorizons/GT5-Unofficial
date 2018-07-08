package gtPlusPlus.api.interfaces;

import java.util.Random;
import net.minecraft.world.World;

public abstract interface IGeneratorWorld
{
  public abstract boolean generate(World paramWorld, Random paramRandom, int paramInt1, int paramInt2);
  
  public abstract void initiate();
  
  public abstract int getExtentX();
  
  public abstract int getExtentZ();
  
  public abstract int getRange();
}
