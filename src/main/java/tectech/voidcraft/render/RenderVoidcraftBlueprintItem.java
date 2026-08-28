package tectech.voidcraft.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.IItemRenderer;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import com.gtnewhorizon.gtnhlib.client.renderer.shader.ShaderProgram;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.common.render.shader.RenderState;
import gregtech.common.render.shader.ShaderHandle;
import gregtech.common.render.shader.SharedShaders;
import tectech.voidcraft.item.ItemVoidbaseBlueprint;
import tectech.voidcraft.item.ItemVoidcraft;
import tectech.voidcraft.ship.VoidcraftBlueprint;
import tectech.voidcraft.ship.VoidcraftNbt;

/**
 * Draws the blueprint items (the digitized Voidcraft and the Voidbase blueprint) as the ACTUAL 3D model of the
 * craft they carry — using the same VAO the in-flight fleet renderer draws (see {@link ShipModelBuilder} and
 * {@link VoidcraftShipModelCache}) — in inventory slots, in hand, and when dropped, instead of the single flat
 * texture. The Voidbase blueprint is drawn as a half-transparent cyan hologram; the Voidcraft is drawn solid.
 *
 * <p>
 * Registered per item via
 * {@link net.minecraftforge.client.MinecraftForgeClient#registerItemRenderer} (see
 * {@code ClientProxy#registerRenderInfo}). Forge places the model inside the standard 3D item-box transform it
 * applies for block-style item icons, so in slot/hand the model fills the item box ([0,1]³ centered at
 * (0.5, 0.5, 0.5)), scaled so the longest blueprint axis fits the slot; a dropped item is centered at the
 * entity origin instead.
 *
 * <p>
 * Stacks without a blueprint payload (empty or corrupt) are not claimed, so the vanilla flat icon renders for
 * them.
 */
@SideOnly(Side.CLIENT)
public class RenderVoidcraftBlueprintItem implements IItemRenderer {

    /** Fraction of the item box the model is scaled to (leaves a small margin inside the slot). */
    private static final double MODEL_FIT = 0.85;

    /** Scratch for the model-matrix composition (client render thread only). */
    private static final Matrix4f MODEL_MATRIX = new Matrix4f();

    /**
     * The Voidbase blueprint model is drawn as a half-transparent cyan hologram: the tint color and opacity below,
     * alpha-blended, with depth writes off so the shell is see-through in both directions.
     */
    private static final float HOLOGRAM_R = 0.4F;
    private static final float HOLOGRAM_G = 0.9F;
    private static final float HOLOGRAM_B = 1.0F;
    private static final float HOLOGRAM_ALPHA = 0.5F;

    /** Whether this renderer draws the Voidbase blueprint (station) item or the Voidcraft (ship) item. */
    private final boolean base;

    public RenderVoidcraftBlueprintItem(boolean base) {
        this.base = base;
    }

    /**
     * @param maxAxis the longest blueprint axis in cells
     * @return the per-cell scale that fits a model of that size into {@link #MODEL_FIT} of the item box (0 for
     *         a degenerate model)
     */
    public static double fitScale(int maxAxis) {
        return maxAxis <= 0 ? 0.0 : MODEL_FIT / maxAxis;
    }

    /**
     * @param stack the item stack
     * @return true if the stack carries a blueprint grid payload
     */
    public static boolean hasBlueprint(ItemStack stack) {
        NBTTagCompound nbt = stack == null ? null : stack.getTagCompound();
        return nbt != null && nbt.hasKey(VoidcraftNbt.TAG_GRID);
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        if (type != ItemRenderType.INVENTORY && type != ItemRenderType.EQUIPPED
            && type != ItemRenderType.EQUIPPED_FIRST_PERSON
            && type != ItemRenderType.ENTITY) {
            return false;
        }
        return hasBlueprint(item);
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        switch (type) {
            case INVENTORY:
                return helper == ItemRendererHelper.INVENTORY_BLOCK;
            case EQUIPPED:
            case EQUIPPED_FIRST_PERSON:
                return helper == ItemRendererHelper.EQUIPPED_BLOCK;
            case ENTITY:
                return helper == ItemRendererHelper.BLOCK_3D || helper == ItemRendererHelper.ENTITY_BOBBING;
            default:
                return false;
        }
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        if (!SharedShaders.ready()) {
            return;
        }
        VoidcraftBlueprint blueprint = base ? ItemVoidbaseBlueprint.getBlueprint(item)
            : ItemVoidcraft.getBlueprint(item);
        if (blueprint == null) {
            return;
        }
        ShipModel model = VoidcraftShipModelCache.get(blueprint);
        if (model == null || model.maxAxis() == 0) {
            return;
        }

        // Fill the item box (centered at (0.5, 0.5, 0.5) in the transform the caller set up) — or, for a
        // dropped item, center the model on the entity origin, which the caller already placed.
        final Matrix4f matrix = MODEL_MATRIX.identity();
        if (type != ItemRenderType.ENTITY) {
            matrix.translate(0.5F, 0.5F, 0.5F);
        }
        matrix.scale((float) fitScale(model.maxAxis()));
        // The blueprint cells span 0..n-1 on each axis — center them on the origin.
        matrix.translate(-(model.width - 1) / 2.0F, -(model.height - 1) / 2.0F, -(model.depth - 1) / 2.0F);

        // Culling off for the model (the hull is a hollow shell of blocks plus thin cover quads — the back
        // sides are depth-occluded by the cube volume, so disabling culling only guarantees the faces we want
        // actually draw).
        final boolean cull = GL11.glIsEnabled(GL11.GL_CULL_FACE);
        final long blend = RenderState.savedBlendFunc();
        final boolean depthMask = GL11.glGetBoolean(GL11.GL_DEPTH_WRITEMASK);
        final int alphaFunc = GL11.glGetInteger(GL11.GL_ALPHA_TEST_FUNC);
        final float alphaRef = GL11.glGetFloat(GL11.GL_ALPHA_TEST_REF);
        GL11.glDisable(GL11.GL_CULL_FACE);
        if (base) {
            // The Voidbase blueprint is drawn as a half-transparent cyan hologram: alpha-blended, alpha test
            // lowered so the 0.5-alpha fragments survive, depth writes off so the shell is see-through in
            // both directions.
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glDepthMask(false);
        } else {
            // The Voidcraft renders solid, like a standard 3D item icon (blend off, the stricter 0.5 alpha
            // test).
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.5F);
            GL11.glDisable(GL11.GL_BLEND);
        }
        try {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            Minecraft.getMinecraft()
                .getTextureManager()
                .bindTexture(TextureMap.locationBlocksTexture);
            final ShaderHandle shader = SharedShaders.textured();
            shader.use();
            if (base) {
                GL20.glUniform4f(shader.loc(SharedShaders.U_TINT), HOLOGRAM_R, HOLOGRAM_G, HOLOGRAM_B, HOLOGRAM_ALPHA);
            } else {
                GL20.glUniform4f(shader.loc(SharedShaders.U_TINT), 1.0F, 1.0F, 1.0F, 1.0F);
            }
            shader.uploadModel(matrix);
            model.vao.render();
            ShaderProgram.clear();
        } finally {
            GL11.glAlphaFunc(alphaFunc, alphaRef);
            GL11.glDepthMask(depthMask);
            RenderState.restoreBlendFunc(blend);
            RenderState.restore(GL11.GL_CULL_FACE, cull);
        }
    }
}
