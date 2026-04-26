package gregtech.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.common.tileentities.machines.multi.MTEOreDrillingPlantBase;

@SideOnly(Side.CLIENT)
public class GTWorkAreaRenderer {

    private static final int WORK_AREA_COLOR = 0x40B4FF;

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc == null || mc.theWorld == null || mc.renderViewEntity == null) {
            return;
        }

        Entity camera = mc.renderViewEntity;

        double cameraX = camera.lastTickPosX + (camera.posX - camera.lastTickPosX) * event.partialTicks;
        double cameraY = camera.lastTickPosY + (camera.posY - camera.lastTickPosY) * event.partialTicks;
        double cameraZ = camera.lastTickPosZ + (camera.posZ - camera.lastTickPosZ) * event.partialTicks;

        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();

        /*
         * Important:
         * Après ce translate, on dessine en coordonnées monde normales.
         * Donc les AxisAlignedBB passées à RenderGlobal.drawOutlinedBoundingBox
         * ne doivent PAS être offsetées par -cameraX/Y/Z.
         */
        GL11.glTranslated(-cameraX, -cameraY, -cameraZ);

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);

        // Pour debug, je recommande de garder le depth test activé au début.
        // Si tu veux voir la zone à travers les blocs ensuite, remets glDisable(GL_DEPTH_TEST).
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDepthMask(false);

        GL11.glLineWidth(2.0F);

        for (Object object : mc.theWorld.loadedTileEntityList) {
            if (!(object instanceof IGregTechTileEntity)) {
                continue;
            }

            IGregTechTileEntity gregTechTile = (IGregTechTileEntity) object;
            IMetaTileEntity metaTileEntity = gregTechTile.getMetaTileEntity();

            if (!(metaTileEntity instanceof MTEOreDrillingPlantBase)) {
                continue;
            }

            MTEOreDrillingPlantBase oreDrill = (MTEOreDrillingPlantBase) metaTileEntity;

            if (!oreDrill.isWorkAreaShown()) {
                continue;
            }

            AxisAlignedBB workArea = oreDrill.getWorkAreaAABB();
            if (workArea == null) {
                continue;
            }

            RenderGlobal.drawOutlinedBoundingBox(workArea, WORK_AREA_COLOR);
        }

        GL11.glDepthMask(true);

        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }
}
