package gregtech.common.render;

import static gregtech.api.enums.Mods.GregTech;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.common.tileentities.machines.multi.drone.GT_MetaTileEntity_DroneCentre;

@SideOnly(Side.CLIENT)
public class GT_RenderDrone extends TileEntitySpecialRenderer {

    private static final ResourceLocation DroneTexture = new ResourceLocation(GregTech.ID, "textures/model/drone.png");
    private static final IModelCustom Drone = AdvancedModelLoader
        .loadModel(new ResourceLocation(GregTech.ID, "textures/model/drone.obj"));

    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {
        if (!(tile instanceof IGregTechTileEntity te
            && te.getMetaTileEntity() instanceof GT_MetaTileEntity_DroneCentre centre)) return;
        if (!centre.getBaseMetaTileEntity()
            .isActive()) return;
        final float size = 1.0f;
        GL11.glPushMatrix();
        addDircetion(centre, x, y, z);
        renderDrone(size);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        addDircetion(centre, x, y, z);
        renderBlade(centre, size);
        GL11.glPopMatrix();
    }

    private void addDircetion(GT_MetaTileEntity_DroneCentre centre, double x, double y, double z) {
        switch (centre.getBaseMetaTileEntity()
            .getFrontFacing()) {
            case EAST -> GL11.glTranslated(x - 1.5, y + 1, z + 0.5);
            case WEST -> GL11.glTranslated(x + 2.5, y + 1, z + 0.5);
            case NORTH -> GL11.glTranslated(x + 0.5, y + 1, z + 2.5);
            case SOUTH -> GL11.glTranslated(x + 0.5, y + 1, z - 1.5);
        }
    }

    private void renderDrone(double size) {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.bindTexture(DroneTexture);
        GL11.glScaled(size, size, size);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
        Drone.renderOnly("drone", "box", "main");
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_LIGHTING);
    }

    private void renderBlade(GT_MetaTileEntity_DroneCentre centre, double size) {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.bindTexture(DroneTexture);
        GL11.glScaled(size, size, size);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
        GL11.glPushMatrix();
        GL11.glTranslated(-0.7d * size, -1 * size, -0.7 * size);
        GL11.glRotated(centre.rotation, 0, 1, 0);
        GL11.glTranslated(0.7d * size, 1 * size, 0.7 * size);
        Drone.renderOnly("blade2");
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslated(-0.7d * size, -1 * size, 0.7 * size);
        GL11.glRotated(centre.rotation, 0, 1, 0);
        GL11.glTranslated(0.7d * size, 1 * size, -0.7 * size);
        Drone.renderOnly("blade3");
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslated(0.7d * size, -1 * size, -0.7 * size);
        GL11.glRotated(centre.rotation, 0, 1, 0);
        GL11.glTranslated(-0.7d * size, 1 * size, 0.7 * size);
        Drone.renderOnly("blade1");
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslated(0.7d * size, -1 * size, 0.7 * size);
        GL11.glRotated(centre.rotation, 0, 1, 0);
        GL11.glTranslated(-0.7d * size, 1 * size, -0.7 * size);
        Drone.renderOnly("blade4");
        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_LIGHTING);
    }
}
