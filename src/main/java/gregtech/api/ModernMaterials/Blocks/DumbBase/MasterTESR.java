package gregtech.api.ModernMaterials.Blocks.DumbBase;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class MasterTESR extends TileEntitySpecialRenderer {

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {

        int ID = 2;
        TileEntitySpecialRenderer TESR = ((IGetTESR) tile).getTESR(ID);
        TESR.renderTileEntityAt(tile, x, y, z, timeSinceLastTick);

    }
}
