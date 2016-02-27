package crazypants.enderio.conduit.gas;

import com.enderio.core.common.util.BlockCoord;
import net.minecraftforge.common.util.ForgeDirection;

public class GasOutput
{
  final ForgeDirection dir;
  final BlockCoord location;
  
  public GasOutput(BlockCoord bc, ForgeDirection dir)
  {
    this.dir = dir;
    this.location = bc;
  }
  
  public int hashCode()
  {
    int prime = 31;
    int result = 1;
    result = 31 * result + (this.location == null ? 0 : this.location.hashCode());
    result = 31 * result + (this.dir == null ? 0 : this.dir.hashCode());
    return result;
  }
  
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    GasOutput other = (GasOutput)obj;
    if (this.location == null)
    {
      if (other.location != null) {
        return false;
      }
    }
    else if (!this.location.equals(other.location)) {
      return false;
    }
    if (this.dir != other.dir) {
      return false;
    }
    return true;
  }
  
  public String toString()
  {
    return "GasOutput [dir=" + this.dir + ", location=" + this.location + "]";
  }
}
