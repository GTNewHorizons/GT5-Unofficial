package gregtech.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.TextureStitchEvent;

import org.joml.Vector3d;
import org.lwjgl.opengl.GL11;

import com.github.bsideup.jabel.Desugar;
import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;
import com.gtnewhorizon.gtnhlib.util.CoordinatePacker;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import gregtech.api.enums.Mods;
import gregtech.api.util.GTUtility;
import it.unimi.dsi.fastutil.longs.LongList;

@EventBusSubscriber(side = Side.CLIENT)
public class GTPowerfailRenderer {

    /** {packed x,y,z coords for powerfailed machines} */
    public static LongList POWERFAILS;
    public static boolean DO_RENDER = true;

    private static IIcon POWERFAIL_ICON;

    @SubscribeEvent
    public static void onTextureDiscover(TextureStitchEvent.Pre event) {
        if (event.map.getTextureType() == 0) { // blocks
            POWERFAIL_ICON = event.map.registerIcon(Mods.GregTech.getResourcePath("icons", "powerfail"));
        }
    }

    @SubscribeEvent
    public static void onRenderWorld(RenderWorldLastEvent event) {
        if (POWERFAILS == null || !DO_RENDER) return;

        Tessellator tessellator = Tessellator.instance;

        tessellator.startDrawingQuads();

        EntityPlayer player = Minecraft.getMinecraft().thePlayer;

        Vec3 pos2 = player.getPosition(event.partialTicks);
        Vector3d pos = new Vector3d(pos2.xCoord, pos2.yCoord, pos2.zCoord);
        Vector3d temp = new Vector3d();

        tessellator.setTranslation(0, 0, 0);

        for (long coord : POWERFAILS) {
            double x = CoordinatePacker.unpackX(coord) + 0.5d;
            double y = CoordinatePacker.unpackY(coord) + 0.5d;
            double z = CoordinatePacker.unpackZ(coord) + 0.5d;

            double dist = GTUtility.clamp(pos.distance(x, y, z), 0, 32);

            if (dist < 4 || dist > 128) continue;

            double size = dist * 0.25;

            if (dist < 8d) {
                // Fade to zero when the player is less than 4 blocks away
                size *= GTUtility.linearCurve(dist, 4d, 0d, 8d, 1d);
            }

            if (dist > 16d) {
                // Fade to 25% when the player is more than 32 blocks away
                size *= GTUtility.linearCurve(dist, 16d, 1d, 32d, 0.25d);
            }

            Plane plane = Plane.lookingAt(temp.set(x, y, z), pos);

            plane.get(0.5 * size, 0.5 * size, temp);
            tessellator.addVertexWithUV(temp.x, temp.y, temp.z, POWERFAIL_ICON.getMaxU(), POWERFAIL_ICON.getMaxV());

            plane.get(0.5 * size, -0.5 * size, temp);
            tessellator.addVertexWithUV(temp.x, temp.y, temp.z, POWERFAIL_ICON.getMaxU(), POWERFAIL_ICON.getMinV());

            plane.get(-0.5 * size, -0.5 * size, temp);
            tessellator.addVertexWithUV(temp.x, temp.y, temp.z, POWERFAIL_ICON.getMinU(), POWERFAIL_ICON.getMinV());

            plane.get(-0.5 * size, 0.5 * size, temp);
            tessellator.addVertexWithUV(temp.x, temp.y, temp.z, POWERFAIL_ICON.getMinU(), POWERFAIL_ICON.getMaxV());
        }

        GL11.glBindTexture(
            GL11.GL_TEXTURE_2D,
            Minecraft.getMinecraft()
                .getTextureMapBlocks()
                .getGlTextureId());

        GL11.glDisable(GL11.GL_DEPTH_TEST);
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
