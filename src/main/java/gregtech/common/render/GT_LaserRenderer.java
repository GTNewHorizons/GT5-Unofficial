package gregtech.common.render;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ClientRegistry;
import gregtech.common.tileentities.render.TileLaser;

public class GT_LaserRenderer extends TileEntitySpecialRenderer {

    private float tickcount = 0;
    private double zoffset = 0.5F;

    private static final IModelCustom lasermodel = AdvancedModelLoader
        .loadModel(new ResourceLocation("gregtech", "textures/model/laser.obj"));;

    public GT_LaserRenderer() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileLaser.class, this);
    }

    private void maths() {
        float tc = tickcount / 20;
        zoffset = (0.6 - (Math.cos((4 * Math.PI * tc) - 2 * Math.PI) / 4)
            - (Math.cos((2 * Math.PI * tc) - Math.PI) / 4));
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {
        GL11.glPushMatrix();
        GL11.glTranslated(x + (tickcount / 20), y + 0.5, z + zoffset);
        maths();
        tickcount += 0.2F;
        if (tickcount >= 20) {
            tickcount = 0;
        }
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        bindTexture(new ResourceLocation("gregtech", "textures/model/laser.png"));
        lasermodel.renderAll();
        GL11.glPopMatrix();
    }
}
