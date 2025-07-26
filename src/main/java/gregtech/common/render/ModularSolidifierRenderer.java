package gregtech.common.render;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import gregtech.common.tileentities.render.TileEntityModularSolidifierRenderer;

public class ModularSolidifierRenderer extends TileEntitySpecialRenderer {

    private static boolean initialized;

    private void init() {
        // spotless:off

        //add models, and textures
        initialized = true;
        //spotless:on
    }

    private void renderModularSolidifier(TileEntityModularSolidifierRenderer tile, double x, double y, double z) {
        // renders everything
        if (tile.isM1Set()) renderModule1(x, y, z, tile.getRgbm1());
        if (tile.isM2Set()) renderModule2(x, y, z, tile.getRgbm2());
        if (tile.isM3Set()) renderModule3(x, y, z, tile.getRgbm3());
        if (tile.isM4Set()) renderModule4(x, y, z, tile.getRgbm4());
    }

    private void renderModule4(double x, double y, double z, float[] rgbArray) {}

    private void renderModule3(double x, double y, double z, float[] rgbArray) {}

    private void renderModule2(double x, double y, double z, float[] rgbArray) {}

    private void renderModule1(double x, double y, double z, float[] rgbArray) {}

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {
        if (!(tile instanceof TileEntityModularSolidifierRenderer modularSolidifier)) return;

        if (!initialized) {
            init();
            if (!initialized) return;
        }

        renderModularSolidifier(modularSolidifier, x, y, z);
    }
}
