package gregtech.common.render;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import gregtech.api.metatileentity.BaseMetaTileEntity;

public class BaseMetaTileEntityRenderer extends TileEntitySpecialRenderer {

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float timeSinceLastTick) {
        if (!(te instanceof BaseMetaTileEntity baseTE)) return;
        if (baseTE.getMetaTileEntity() instanceof IMTERenderer renderer) {
            renderer.renderTESR(x, y, z, timeSinceLastTick);
        }
    }
}
