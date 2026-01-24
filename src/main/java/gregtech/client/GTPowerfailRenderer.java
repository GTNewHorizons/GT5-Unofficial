package gregtech.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.world.WorldEvent;

import org.joml.Vector3d;
import org.lwjgl.opengl.GL11;

import com.github.bsideup.jabel.Desugar;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.enums.Mods;
import gregtech.api.util.GTUtility;
import gregtech.common.config.Client;
import gregtech.common.data.GTPowerfailTracker;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

public class GTPowerfailRenderer {

    public IIcon powerfailIcon;

    public final Long2ObjectOpenHashMap<GTPowerfailTracker.Powerfail> powerfails = new Long2ObjectOpenHashMap<>();

    @SubscribeEvent
    public void onTextureDiscover(TextureStitchEvent.Pre event) {
        if (event.map.getTextureType() == 0) { // blocks
            powerfailIcon = event.map.registerIcon(Mods.GregTech.getResourcePath("icons", "powerfail"));
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        if (event.world.isRemote) {
            powerfails.clear();
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (powerfails.isEmpty()) return;
        if (!Client.render.renderPowerfailNotifications) return;

        Tessellator tessellator = Tessellator.instance;

        tessellator.startDrawingQuads();

        EntityPlayer player = Minecraft.getMinecraft().thePlayer;

        Vec3 pos2 = player.getPosition(event.partialTicks);
        Vector3d pos = new Vector3d(pos2.xCoord, pos2.yCoord, pos2.zCoord);
        Vector3d temp = new Vector3d();

        tessellator.setTranslation(0, 0, 0);

        long now = System.currentTimeMillis();

        for (GTPowerfailTracker.Powerfail p : powerfails.values()) {
            if (Client.render.powerfailNotificationTimeout > 0) {
                long elapsed = now - p.lastOccurrence.getTime();

                if (elapsed > Client.render.powerfailNotificationTimeout * 1000L) continue;
            }

            if (player.dimension != p.dim) continue;

            double x = p.x + 0.5d;
            double y = p.y + 0.5d;
            double z = p.z + 0.5d;

            double dist = pos.distance(x, y, z);

            if (dist < 4 || dist > 512) continue;

            double size = dist * 0.25 * Client.render.powerfailIconSize;

            if (dist < 16d) {
                // Fade to zero when the player is less than 2 blocks away
                size *= GTUtility.linearCurve(dist, 2d, 0d, 16d, 1d);
            }

            if (dist > 48) {
                // Fade to 25% when the player is more than 64 blocks away
                size *= GTUtility.linearCurve(dist, 48d, 1d, 64d, 0.25d);
            }

            Plane plane = Plane.lookingAt(temp.set(x, y, z), pos);

            plane.get(0.5 * size, 0.5 * size, temp);
            tessellator.addVertexWithUV(temp.x, temp.y, temp.z, powerfailIcon.getMaxU(), powerfailIcon.getMaxV());

            plane.get(0.5 * size, -0.5 * size, temp);
            tessellator.addVertexWithUV(temp.x, temp.y, temp.z, powerfailIcon.getMaxU(), powerfailIcon.getMinV());

            plane.get(-0.5 * size, -0.5 * size, temp);
            tessellator.addVertexWithUV(temp.x, temp.y, temp.z, powerfailIcon.getMinU(), powerfailIcon.getMinV());

            plane.get(-0.5 * size, 0.5 * size, temp);
            tessellator.addVertexWithUV(temp.x, temp.y, temp.z, powerfailIcon.getMinU(), powerfailIcon.getMaxV());
        }

        GL11.glBindTexture(
            GL11.GL_TEXTURE_2D,
            Minecraft.getMinecraft()
                .getTextureMapBlocks()
                .getGlTextureId());

        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA);
        tessellator.draw();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    @Desugar
    private record Plane(Vector3d centre, Vector3d s, Vector3d t) {

        public static Plane lookingAt(Vector3d centre, Vector3d pos) {
            Vector3d normal = new Vector3d();
            Vector3d relCentre = new Vector3d();

            relCentre.set(centre)
                .sub(pos);
            relCentre.normalize(normal);

            double radians = Math.atan2(normal.x, normal.z) + Math.PI / 2;

            Vector3d s = new Vector3d(0, 0, -1).rotateY(radians);

            Vector3d t = normal.cross(s, new Vector3d());

            return new Plane(relCentre, s, t);
        }

        public void get(double sk, double tk, Vector3d dest) {
            dest.x = centre.x + s.x * sk + t.x * tk;
            dest.y = centre.y + s.y * sk + t.y * tk;
            dest.z = centre.z + s.z * sk + t.z * tk;
        }
    }
}
