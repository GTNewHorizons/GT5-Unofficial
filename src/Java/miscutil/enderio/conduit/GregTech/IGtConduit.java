package miscutil.enderio.conduit.GregTech;

import net.minecraftforge.common.util.ForgeDirection;
import crazypants.enderio.conduit.IConduit;

public interface IGtConduit extends IConduit {

  boolean canOutputToDir(ForgeDirection dir);

  boolean isExtractingFromDir(ForgeDirection dir);

}
