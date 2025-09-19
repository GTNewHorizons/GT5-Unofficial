package gregtech.common.render;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.ITESRProvider;

public class GTRendererTileEntity extends TileEntitySpecialRenderer {

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {
        if (tile instanceof IGregTechTileEntity gtTE
            && gtTE.getMetaTileEntity() instanceof ITESRProvider tesrProvider) {
            tesrProvider.renderTileEntityAt(tile, x, y, z, timeSinceLastTick);
        }
    }
}
