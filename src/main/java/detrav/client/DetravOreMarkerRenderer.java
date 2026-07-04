package detrav.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import galacticgreg.api.enums.DimensionDef;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.StoneType;
import gregtech.api.enums.TextureSet;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.client.renderer.BillboardRenderHelper;
import gregtech.client.renderer.BillboardRenderHelper.Plane;
import gregtech.common.config.Client;
import gtneioreplugin.util.DimensionHelper;

/**
 * Renders the temporary ore markers triggered from the Detrav scanner GUI as in world GT ore
 * icons, or as a coloured diamond when the material cannot be resolved. Markers expire after
 * {@link Client.Render#detravOreMarkerTimeout} seconds.
 */
public class DetravOreMarkerRenderer {

    /** Markers further than this (blocks) from the camera are skipped. */
    private static final double MAX_RENDER_DIST = 256d;

    private static final List<DetravOreMarker> markers = new ArrayList<>();

    /** Adds a marker, or removes the existing one at the same block (toggle). */
    public static void toggleMarker(int dim, int x, int y, int z, String name, int color, String materialName) {
        for (Iterator<DetravOreMarker> it = markers.iterator(); it.hasNext();) {
            DetravOreMarker m = it.next();
            if (m.dim == dim && m.x == x && m.y == y && m.z == z) {
                it.remove();
                return;
            }
        }
        markers.add(new DetravOreMarker(dim, x, y, z, name, color, resolveIcon(materialName)));
    }

    public static void clear() {
        markers.clear();
    }

    /** Live view of the current markers, for the in-GUI map overlay. */
    public static List<DetravOreMarker> getMarkers() {
        return markers;
    }

    /** Resolves the GT ore icon for a material internal name, mirroring VisualProspecting's ore-vein icon. */
    @Nullable
    private static IIconContainer resolveIcon(String materialName) {
        if (materialName == null || materialName.isEmpty()) return null;
        IOreMaterial mat = IOreMaterial.findMaterial(materialName);
        if (mat == null) return null;
        TextureSet ts = mat.getTextureSet();
        if (ts == null) return null;
        int idx = OrePrefixes.ore.getTextureIndex();
        if (idx < 0 || idx >= ts.mTextures.length) return null;
        return ts.mTextures[idx];
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        if (event.world.isRemote) clear();
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (markers.isEmpty()) return;

        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player == null) return;

        int dim = player.worldObj.provider.dimensionId;
        Vec3 eyeVec = player.getPosition(event.partialTicks);
        Vector3d eye = new Vector3d(eyeVec.xCoord, eyeVec.yCoord, eyeVec.zCoord);
        Vector3d centre = new Vector3d();

        long now = System.currentTimeMillis();
        long lifetimeMs = Client.render.detravOreMarkerTimeout * 1000L;

        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        Tessellator.instance.setTranslation(0, 0, 0);

        IIcon background = resolveBackground(player.worldObj);

        for (Iterator<DetravOreMarker> it = markers.iterator(); it.hasNext();) {
            DetravOreMarker m = it.next();

            if (lifetimeMs > 0 && now - m.createdAt > lifetimeMs) {
                it.remove();
                continue;
            }
            if (m.dim != dim) continue;

            double dist = eye.distance(m.x + 0.5d, m.y + 0.5d, m.z + 0.5d);
            if (dist > MAX_RENDER_DIST) continue;

            Plane plane = Plane.lookingAt(centre.set(m.x + 0.5d, m.y + 0.5d, m.z + 0.5d), eye);
            float half = (float) Math.max(0.25d, dist * 0.03d);

            IIconContainer ore = m.ore;
            if (ore == null || !renderIcon(plane, half, m.color, ore, background)) {
                renderDiamond(plane, half, m);
            }

            renderLabel(m.x + 0.5d - eye.x, m.y + 0.5d - eye.y, m.z + 0.5d - eye.z, dist, m);
        }

        GL11.glColor4f(1F, 1F, 1F, 1F);
        GL11.glPopAttrib();
    }

    private boolean renderIcon(Plane plane, float half, int color, IIconContainer ore, IIcon background) {
        IIcon icon = ore.getIcon();
        ResourceLocation textureFile = ore.getTextureFile();
        if (icon == null || textureFile == null) return false;

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        Minecraft mc = Minecraft.getMinecraft();

        mc.getTextureManager()
            .bindTexture(TextureMap.locationBlocksTexture);
        iconQuad(plane, background, half, 0xFFFFFF);

        mc.getTextureManager()
            .bindTexture(textureFile);
        iconQuad(plane, icon, half, color);
        IIcon overlay = ore.getOverlayIcon();
        if (overlay != null) iconQuad(plane, overlay, half, 0xFFFFFF);
        return true;
    }

    private static IIcon resolveBackground(World world) {
        StoneType stone = StoneType.Stone;
        try {
            String dimName = DimensionDef.getDimensionName(world);
            if (dimName != null && DimensionHelper.INTERNAL_TO_FULL.containsKey(dimName)) {
                List<StoneType> types = DimensionHelper.getStoneTypes(dimName);
                if (!types.isEmpty() && types.getFirst() != null) stone = types.getFirst();
            }
        } catch (Exception ignored) {
            // unmapped/edge dimensions just use vanilla stone
        }
        IIcon icon = stone.getIcon(1);
        return icon != null ? icon : Blocks.stone.getIcon(0, 0);
    }

    /** Coloured diamond fallback when no ore icon is available. */
    private void renderDiamond(Plane plane, float half, DetravOreMarker m) {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        diamond(plane, half * 1.3F, 0x000000, 160);
        diamond(plane, half, m.color & 0xFFFFFF, 255);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    private void renderLabel(double dx, double dy, double dz, double dist, DetravOreMarker m) {
        FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
        float scale = (float) Math.max(0.02d, dist * 0.0025d);
        float lift = (float) Math.max(0.4d, dist * 0.045d);

        GL11.glPushMatrix();
        GL11.glTranslated(dx, dy + lift, dz);
        GL11.glRotatef(-RenderManager.instance.playerViewY, 0F, 1F, 0F);
        GL11.glRotatef(RenderManager.instance.playerViewX, 1F, 0F, 0F);
        GL11.glScalef(-scale, -scale, scale);

        int w = fr.getStringWidth(m.name);
        GL11.glDepthMask(false);
        fr.drawString(m.name, -w / 2, 0, 0xFFFFFFFF);
        GL11.glDepthMask(true);

        GL11.glColor4f(1F, 1F, 1F, 1F);
        GL11.glPopMatrix();
    }

    private static final Vector3d V = new Vector3d();

    /** Camera facing tinted icon based on {@code GTPowerfailRenderer}). */
    private static void iconQuad(Plane plane, IIcon icon, float half, int color) {
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        t.setColorOpaque_I(color & 0xFFFFFF);
        BillboardRenderHelper.addTexturedQuad(t, plane, icon, half, V);
        t.draw();
    }

    /** Camera facing untextured diamond fallback. */
    private static void diamond(Plane plane, float half, int rgb, int alpha) {
        Tessellator t = Tessellator.instance;
        t.startDrawing(GL11.GL_TRIANGLE_FAN);
        t.setColorRGBA_I(rgb & 0xFFFFFF, alpha);
        plane.get(0, 0, V);
        t.addVertex(V.x, V.y, V.z);
        plane.get(0, half, V);
        t.addVertex(V.x, V.y, V.z);
        plane.get(half, 0, V);
        t.addVertex(V.x, V.y, V.z);
        plane.get(0, -half, V);
        t.addVertex(V.x, V.y, V.z);
        plane.get(-half, 0, V);
        t.addVertex(V.x, V.y, V.z);
        plane.get(0, half, V);
        t.addVertex(V.x, V.y, V.z);
        t.draw();
    }

}
