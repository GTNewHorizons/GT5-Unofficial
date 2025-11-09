package gregtech.api.interfaces.tileentity;

import net.minecraft.tileentity.TileEntity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * custom TESR provider for MTE
 */
public interface ITESRProvider {

    @SideOnly(Side.CLIENT)
    void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick);
}
