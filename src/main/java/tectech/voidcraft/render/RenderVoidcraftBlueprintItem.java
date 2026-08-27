package tectech.voidcraft.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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
        VoidcraftBlueprint blueprint = base ? ItemVoidbaseBlueprint.getBlueprint(item)
            : ItemVoidcraft.getBlueprint(item);
        if (blueprint == null) {
            return;
        }
        ShipModel model = VoidcraftShipModelCache.get(blueprint);
        if (model == null || model.maxAxis() == 0) {
            return;
        }

        boolean lighting = GL11.glIsEnabled(GL11.GL_LIGHTING);
        boolean cull = GL11.glIsEnabled(GL11.GL_CULL_FACE);
        boolean blend = GL11.glIsEnabled(GL11.GL_BLEND);
        boolean depthMask = GL11.glGetBoolean(GL11.GL_DEPTH_WRITEMASK);
        GL11.glPushMatrix();
        try {
            double s = fitScale(model.maxAxis());
            // Fill the item box (centered at (0.5, 0.5, 0.5) in the transform the caller set up) — or, for a
            // dropped item, center the model on the entity origin, which the caller already placed.
            if (type != ItemRenderType.ENTITY) {
                GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            }
            GL11.glScalef((float) s, (float) s, (float) s);
            // The blueprint cells span 0..n-1 on each axis — center them on the origin.
            GL11.glTranslatef(-(model.width - 1) / 2.0F, -(model.height - 1) / 2.0F, -(model.depth - 1) / 2.0F);

            Minecraft.getMinecraft()
                .getTextureManager()
                .bindTexture(TextureMap.locationBlocksTexture);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glDisable(GL11.GL_CULL_FACE);
            if (base) {
                // The Voidbase blueprint is drawn as a half-transparent cyan hologram: alpha-blended, alpha
                // test lowered so the 0.5-alpha fragments survive, depth writes off so the shell is
                // see-through in both directions.
                GL11.glColor4f(HOLOGRAM_R, HOLOGRAM_G, HOLOGRAM_B, HOLOGRAM_ALPHA);
                GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glDepthMask(false);
            } else {
                // The Voidcraft renders solid, like a standard 3D item icon.
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glAlphaFunc(GL11.GL_GREATER, 0.5F);
                GL11.glDisable(GL11.GL_BLEND);
            }

            model.vao.render();
        } finally {
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            if (cull) {
                GL11.glEnable(GL11.GL_CULL_FACE);
            } else {
                GL11.glDisable(GL11.GL_CULL_FACE);
            }
            if (blend) {
                GL11.glEnable(GL11.GL_BLEND);
            } else {
                GL11.glDisable(GL11.GL_BLEND);
            }
            GL11.glDepthMask(depthMask);
            if (!lighting) {
                GL11.glDisable(GL11.GL_LIGHTING);
            }
            GL11.glPopMatrix();
        }
    }
}
