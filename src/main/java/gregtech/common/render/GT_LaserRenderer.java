package gregtech.common.render;

import cpw.mods.fml.client.registry.ClientRegistry;
import gregtech.common.tileentities.render.TileLaser;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

public class GT_LaserRenderer extends TileEntitySpecialRenderer {

    private static final IModelCustom lasermodel = AdvancedModelLoader
        .loadModel(new ResourceLocation("gregtech", "models/laser.obj"));;

    public GT_LaserRenderer() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileLaser.class, this);
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        lasermodel.renderAll();
        GL11.glPopMatrix();
    }
}
