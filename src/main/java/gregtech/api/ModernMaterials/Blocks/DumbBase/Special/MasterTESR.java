package gregtech.api.ModernMaterials.Blocks.DumbBase.Special;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ModernMaterials.Blocks.DumbBase.BaseMaterialBlock.BaseMaterialBlock;

public class MasterTESR extends TileEntitySpecialRenderer {

    @Override
    public void renderTileEntityAt(@NotNull TileEntity tile, double x, double y, double z, float timeSinceLastTick) {

        BaseMaterialBlock block = (BaseMaterialBlock) tile.getBlockType();
        int ID = block.getMaterialID(tile.getBlockMetadata());

        block.getBlockEnum()
            .getSpecialRenderer(ID)
            .renderTileEntityAt(tile, x, y, z, timeSinceLastTick);
    }
}
