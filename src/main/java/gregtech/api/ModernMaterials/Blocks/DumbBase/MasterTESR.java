package gregtech.api.ModernMaterials.Blocks.DumbBase;

import gregtech.api.ModernMaterials.Blocks.DumbBase.Simple.DumbTileEntity;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.jetbrains.annotations.NotNull;

public class MasterTESR extends TileEntitySpecialRenderer {

    @Override
    public void renderTileEntityAt(@NotNull TileEntity tile, double x, double y, double z, float timeSinceLastTick) {

        DumbTileEntity dumbTile = (DumbTileEntity) tile;

        int ID = dumbTile.getMaterialID();
        TileEntitySpecialRenderer TESR = ((IGetTESR) tile).getTESR(ID);
        TESR.renderTileEntityAt(tile, x, y, z, timeSinceLastTick);
    }
}
