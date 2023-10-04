package gregtech.api.ModernMaterials.Blocks.DumbBase.Special;

import gregtech.api.ModernMaterials.Blocks.DumbBase.Base.BaseTileEntity;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.jetbrains.annotations.NotNull;

public class MasterTESR extends TileEntitySpecialRenderer {

    @Override
    public void renderTileEntityAt(@NotNull TileEntity tile, double x, double y, double z, float timeSinceLastTick) {

        BaseTileEntity dumbTile = (BaseTileEntity) tile;

        int ID = dumbTile.getMaterialID();
        TileEntitySpecialRenderer TESR = ((IGetTESR) tile).getTESR(ID);
        TESR.renderTileEntityAt(tile, x, y, z, timeSinceLastTick);
    }
}
