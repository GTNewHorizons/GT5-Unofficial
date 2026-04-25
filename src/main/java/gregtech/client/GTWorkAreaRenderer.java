package gregtech.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
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

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc == null || mc.theWorld == null || mc.renderViewEntity == null) {
            return;
        }

        Entity camera = mc.renderViewEntity;

        double camX = camera.lastTickPosX + (camera.posX - camera.lastTickPosX) * event.partialTicks;
        double camY = camera.lastTickPosY + (camera.posY - camera.lastTickPosY) * event.partialTicks;
        double camZ = camera.lastTickPosZ + (camera.posZ - camera.lastTickPosZ) * event.partialTicks;

        for (Object object : mc.theWorld.loadedTileEntityList) {
            if (!(object instanceof IGregTechTileEntity gregTechTile)) {
                continue;
            }

            IMetaTileEntity metaTileEntity = gregTechTile.getMetaTileEntity();

            if (!(metaTileEntity instanceof MTEOreDrillingPlantBase oreDrill)) {
                continue;
            }

            if (!oreDrill.isWorkAreaShown()) {
                continue;
            }

            // Petit culling pour éviter de parcourir/rendre des choses trop loin.
            TileEntity tile = (TileEntity) gregTechTile;
            double dx = tile.xCoord + 0.5D - camera.posX;
            double dy = tile.yCoord + 0.5D - camera.posY;
            double dz = tile.zCoord + 0.5D - camera.posZ;

            if (dx * dx + dy * dy + dz * dz > 256D * 256D) {
                continue;
            }

            renderWorkArea(oreDrill.getWorkAreaAABB(), camX, camY, camZ);
        }
    }

    private void renderWorkArea(AxisAlignedBB worldBox, double camX, double camY, double camZ) {
        AxisAlignedBB box = worldBox.offset(-camX, -camY, -camZ);

        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);

        // Laisse ça activé si tu veux que la box soit cachée par le terrain.
        // Désactive-le si tu veux voir la zone à travers les blocs.
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDepthMask(false);

        GL11.glLineWidth(2.0F);
        RenderGlobal.drawOutlinedBoundingBox(box, 0x40B4FF);

        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }
}
