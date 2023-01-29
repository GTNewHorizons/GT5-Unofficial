package kubatech.loaders.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public interface IProxyTileEntityProvider {

    TileEntity createTileEntity(World world);
}
