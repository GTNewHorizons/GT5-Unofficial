package gregtech.common.render;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import cpw.mods.fml.client.registry.ClientRegistry;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.ITESRProvider;
import gregtech.api.metatileentity.BaseMetaTileEntity;

public class GTRendererTileEntity extends TileEntitySpecialRenderer {

    public GTRendererTileEntity() {
        ClientRegistry.bindTileEntitySpecialRenderer(BaseMetaTileEntity.class, this);
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {
        if (tile instanceof IGregTechTileEntity gtTE
            && gtTE.getMetaTileEntity() instanceof ITESRProvider tesrProvider) {
            tesrProvider.renderTileEntityAt(tile, x, y, z, timeSinceLastTick);
        }
    }
}
