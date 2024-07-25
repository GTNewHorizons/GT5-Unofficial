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

    private static char facing = 'r';

    private static final IModelCustom lasermodel = AdvancedModelLoader
        .loadModel(new ResourceLocation("gregtech", "textures/model/laser.obj"));;

    public GT_LaserRenderer() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileLaser.class, this);
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        bindTexture(new ResourceLocation("gregtech","textures/model/laser.png"));
        lasermodel.renderAll();
        GL11.glPopMatrix();
    }
}
