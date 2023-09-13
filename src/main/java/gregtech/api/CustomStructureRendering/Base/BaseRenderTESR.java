package gregtech.api.CustomStructureRendering.Base;

import gregtech.api.CustomStructureRendering.Base.Util.RenderHelper;
import gregtech.api.CustomStructureRendering.Structures.BaseModelStructure;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class BaseRenderTESR extends TileEntitySpecialRenderer {

    public static final String MODEL_NAME_NBT_TAG = "modelName";

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {
        if (!(tile instanceof BaseRenderTileEntity trophyTileEntity)) return;

        RenderHelper.renderModel(tile.getWorldObj(), x, y, z, getModel(trophyTileEntity.modelName));
    }

    protected BaseModelStructure getModel(String modelName) {
        return null;
    }

}
