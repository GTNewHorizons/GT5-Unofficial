package gregtech.common.render.items;

import static gregtech.api.enums.Mods.HodgePodge;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import com.mitchej123.hodgepodge.textures.IPatchedTextureAtlasSprite;

import codechicken.lib.render.TextureUtils;
import gregtech.api.interfaces.IGT_ItemWithMaterialRenderer;
import gregtech.api.util.GTUtility;
import gregtech.common.render.GTRenderUtil;

public class GeneratedMaterialRenderer implements IItemRenderer {

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON
            || type == ItemRenderType.INVENTORY
            || type == ItemRenderType.ENTITY;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return type == ItemRenderType.ENTITY && helper == ItemRendererHelper.ENTITY_BOBBING
            || (helper == ItemRendererHelper.ENTITY_ROTATION && Minecraft.getMinecraft().gameSettings.fancyGraphics);
    }

    /**
     * Handle special fluid display rendering. Return false if does not need such kind of handling. Note: annotations
     * should not be rendered here. Only render the fluid texture. Parameters are values passed from
     * {@link IItemRenderer#renderItem(ItemRenderType, ItemStack, Object...)} verbatim. Do not modify the argument.
     * <p>
     * While this is called, BLEND and ALPHA_TEST is on. It is expected that these remain enabled while exit.
     *
     * @return true if did special fluid display rendering. false otherwise.
     */
    public boolean renderFluidDisplayItem(ItemRenderType type, ItemStack aStack, Object... data) {
        return false;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack aStack, Object... data) {
        short aMetaData = (short) aStack.getItemDamage();
        if (!(aStack.getItem() instanceof IGT_ItemWithMaterialRenderer aItem)) return;

        int passes = 1;
        if (aItem.requiresMultipleRenderPasses()) {
            passes = aItem.getRenderPasses(aMetaData);
        }

        for (int pass = 0; pass < passes; pass++) {
            IIcon tIcon = aItem.getIcon(aMetaData, pass);
            IIcon tOverlay = aItem.getOverlayIcon(aMetaData, pass);
            FluidStack aFluid = GTUtility.getFluidForFilledItem(aStack, true);

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glEnable(GL11.GL_ALPHA_TEST);

            if (tIcon != null) {
                markNeedsAnimationUpdate(tIcon);
                renderRegularItem(type, aStack, tIcon, aFluid == null, pass, data);
            }

            if (tOverlay != null && aFluid != null && aFluid.getFluid() != null) {
                IIcon fluidIcon = aFluid.getFluid()
                    .getIcon(aFluid);
                if (fluidIcon != null) {
                    markNeedsAnimationUpdate(fluidIcon);
                    // Adds colour to a cells fluid. Does not colour full fluid icons as shown in NEI etc.
                    renderContainedFluid(type, aFluid, fluidIcon);
                }
            }

            if (tOverlay != null) {
                GL11.glColor3f(1.0F, 1.0F, 1.0F);
                TextureUtils.bindAtlas(aItem.getSpriteNumber());
                markNeedsAnimationUpdate(tOverlay);
                renderItemOverlay(type, tOverlay);
            }

            GL11.glDisable(GL11.GL_BLEND);
        }
    }

    protected void renderRegularItem(ItemRenderType type, ItemStack aStack, IIcon icon, boolean shouldModulateColor,
        int pass, Object... data) {
        renderRegularItem(type, aStack, icon, shouldModulateColor);
    }

    protected void renderRegularItem(ItemRenderType type, ItemStack aStack, IIcon icon, boolean shouldModulateColor) {
        if (!(aStack.getItem() instanceof IGT_ItemWithMaterialRenderer aItem)) return;

        if (shouldModulateColor) {
            short[] tModulation = aItem.getRGBa(aStack);
            GL11.glColor3f(tModulation[0] / 255.0F, tModulation[1] / 255.0F, tModulation[2] / 255.0F);
        }

        GTRenderUtil.renderItem(type, icon);
    }

    protected void renderContainedFluid(ItemRenderType type, FluidStack aFluidStack, IIcon fluidIcon) {
        Fluid aFluid = aFluidStack.getFluid();
        int tColor = aFluid.getColor(aFluidStack);
        GL11.glColor3f((tColor >> 16 & 0xFF) / 255.0F, (tColor >> 8 & 0xFF) / 255.0F, (tColor & 0xFF) / 255.0F);
        TextureUtils.bindAtlas(aFluid.getSpriteNumber());

        GL11.glDepthFunc(GL11.GL_EQUAL);
        GTRenderUtil.renderItem(type, fluidIcon);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
    }

    protected void renderItemOverlay(ItemRenderType type, IIcon overlay) {
        GTRenderUtil.renderItem(type, overlay);
    }

    protected void markNeedsAnimationUpdate(IIcon icon) {
        if (HodgePodge.isModLoaded() && icon instanceof IPatchedTextureAtlasSprite) {
            ((IPatchedTextureAtlasSprite) icon).markNeedsAnimationUpdate();
        }
    }
}
