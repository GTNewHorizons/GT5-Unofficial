package tectech.rendering.EOH;

import static tectech.rendering.EOH.EOHRenderingUtils.renderBlockInWorld;
import static tectech.rendering.EOH.EOHRenderingUtils.renderOuterSpaceShell;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import tectech.Reference;
import tectech.thing.block.TileEntityEyeOfHarmony;

public class EOHTileEntitySR extends TileEntitySpecialRenderer {

    public static final ResourceLocation STAR_LAYER_0 = new ResourceLocation(Reference.MODID, "models/StarLayer0.png");
    public static final ResourceLocation STAR_LAYER_1 = new ResourceLocation(Reference.MODID, "models/StarLayer1.png");
    public static final ResourceLocation STAR_LAYER_2 = new ResourceLocation(Reference.MODID, "models/StarLayer2.png");

    private static final float STAR_RESCALE = 0.2f;
    private static final float SPEED_SCALE = 0.1f; // keep your old tuning

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {

        if (!(tile instanceof TileEntityEyeOfHarmony te)) return;

        World world = te.getWorldObj();
        if (world == null) return; // Just in-case

        // Smooth global animation clock
        float time = world.getTotalWorldTime() + partialTicks;

        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);

        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);

        // For LOD calculations.
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        renderOuterSpaceShell(player.getDistance(x, y, z));

        renderOrbitObjects(te, time);

        EOHRenderingUtils.renderEOHStar(IItemRenderer.ItemRenderType.INVENTORY, time, te.getStarSize());

        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }

    private void renderOrbitObjects(TileEntityEyeOfHarmony te, float time) {

        var objects = te.getOrbitingObjects();

        if (objects == null || objects.isEmpty()) {
            te.generateImportantInfo();
            objects = te.getOrbitingObjects();

            if (objects == null || objects.isEmpty()) return;
        }

        for (TileEntityEyeOfHarmony.OrbitingObject obj : objects) {
            renderOrbit(te, obj, time);
        }
    }

    private void renderOrbit(TileEntityEyeOfHarmony te, TileEntityEyeOfHarmony.OrbitingObject obj, float time) {

        GL11.glPushMatrix();

        // Precompute angles

        float orbitAngle = (obj.orbitSpeed * SPEED_SCALE * time) % 360f;
        float spinAngle = (obj.rotationSpeed * SPEED_SCALE * time) % 360f;

        double distance = -0.2f - obj.distance - STAR_RESCALE * te.getStarSize();

        GL11.glRotatef(obj.zAngle, 0, 0, 1);
        GL11.glRotatef(obj.xAngle, 1, 0, 0);

        // Orbit around the star
        GL11.glRotatef(orbitAngle, 0F, 1F, 0F);

        // Move outward from center
        GL11.glTranslated(distance, 0, 0);

        // Local planet spin
        GL11.glRotatef(spinAngle, 0F, 1F, 0F);

        bindTexture(TextureMap.locationBlocksTexture);
        renderBlockInWorld(obj.block, 0, obj.scale);

        GL11.glPopMatrix();
    }
}
