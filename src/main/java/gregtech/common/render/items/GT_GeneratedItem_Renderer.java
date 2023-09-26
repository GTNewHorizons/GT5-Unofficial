package gregtech.common.render.items;

import static gregtech.api.enums.ItemList.Large_Fluid_Cell_Aluminium;
import static gregtech.api.enums.ItemList.Large_Fluid_Cell_Chrome;
import static gregtech.api.enums.ItemList.Large_Fluid_Cell_Iridium;
import static gregtech.api.enums.ItemList.Large_Fluid_Cell_Neutronium;
import static gregtech.api.enums.ItemList.Large_Fluid_Cell_Osmium;
import static gregtech.api.enums.ItemList.Large_Fluid_Cell_StainlessSteel;
import static gregtech.api.enums.ItemList.Large_Fluid_Cell_Steel;
import static gregtech.api.enums.ItemList.Large_Fluid_Cell_Titanium;
import static gregtech.api.enums.ItemList.Large_Fluid_Cell_TungstenSteel;
import static gregtech.api.enums.Mods.HodgePodge;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import com.mitchej123.hodgepodge.textures.IPatchedTextureAtlasSprite;

import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.items.GT_MetaGenerated_Item;
import gregtech.api.util.GT_Utility;
import gregtech.common.render.GT_RenderUtil;
import gregtech.loaders.ExtraIcons;

public class GT_GeneratedItem_Renderer implements IItemRenderer {

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON
            || type == ItemRenderType.INVENTORY
            || type == ItemRenderType.ENTITY;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return type == ItemRenderType.ENTITY;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack aStack, Object... data) {
        short aMetaData = (short) aStack.getItemDamage();
        if (!(aStack.getItem() instanceof GT_MetaGenerated_Item aItem)) return;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        IIcon tIcon;
        if (aItem.mIconList[(aMetaData - aItem.mOffset)].length > 1) {
            Long[] tStats = aItem.mElectricStats.get(aMetaData);

            if ((tStats != null) && (tStats[3] < 0L)) {
                long tCharge = aItem.getRealCharge(aStack);

                if (tCharge <= 0L) {
                    tIcon = aItem.mIconList[(aMetaData - aItem.mOffset)][1];
                } else {

                    if (tCharge >= tStats[0]) {
                        tIcon = aItem.mIconList[(aMetaData - aItem.mOffset)][8];
                    } else {
                        tIcon = aItem.mIconList[(aMetaData - aItem.mOffset)][(7
                            - (int) Math.max(0L, Math.min(5L, (tStats[0] - tCharge) * 6L / tStats[0])))];
                    }
                }
            } else {
                tIcon = aItem.mIconList[(aMetaData - aItem.mOffset)][0];
            }
        } else {
            tIcon = aItem.mIconList[(aMetaData - aItem.mOffset)][0];
        }

        if (tIcon == null) tIcon = Textures.ItemIcons.RENDERING_ERROR.getIcon();

        markNeedsAnimationUpdate(tIcon);

        ItemList largeFluidCell = getLargeFluidCell(aStack);
        if (largeFluidCell != null) {
            renderLargeFluidCellExtraParts(type, largeFluidCell, aStack);
        }

        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        if (type.equals(IItemRenderer.ItemRenderType.INVENTORY)) {
            GT_RenderUtil.renderItemIcon(tIcon, 16.0D, 0.001D, 0.0F, 0.0F, -1.0F);
        } else {
            ItemRenderer.renderItemIn2D(
                Tessellator.instance,
                tIcon.getMaxU(),
                tIcon.getMinV(),
                tIcon.getMinU(),
                tIcon.getMaxV(),
                tIcon.getIconWidth(),
                tIcon.getIconHeight(),
                0.0625F);
        }
        GL11.glDisable(GL11.GL_BLEND);
    }

    @Nullable
    private static ItemList getLargeFluidCell(ItemStack stack) {
        if (isSame(Large_Fluid_Cell_Steel, stack)) return Large_Fluid_Cell_Steel;
        if (isSame(Large_Fluid_Cell_Aluminium, stack)) return Large_Fluid_Cell_Aluminium;
        if (isSame(Large_Fluid_Cell_TungstenSteel, stack)) return Large_Fluid_Cell_TungstenSteel;
        if (isSame(Large_Fluid_Cell_StainlessSteel, stack)) return Large_Fluid_Cell_StainlessSteel;
        if (isSame(Large_Fluid_Cell_Titanium, stack)) return Large_Fluid_Cell_Titanium;
        if (isSame(Large_Fluid_Cell_Chrome, stack)) return Large_Fluid_Cell_Chrome;
        if (isSame(Large_Fluid_Cell_Iridium, stack)) return Large_Fluid_Cell_Iridium;
        if (isSame(Large_Fluid_Cell_Osmium, stack)) return Large_Fluid_Cell_Osmium;
        if (isSame(Large_Fluid_Cell_Neutronium, stack)) return Large_Fluid_Cell_Neutronium;

        return null;
    }

    private void renderLargeFluidCellExtraParts(IItemRenderer.ItemRenderType type, ItemList item, ItemStack stack) {

        IIcon inner;
        if (item == Large_Fluid_Cell_Steel) inner = ExtraIcons.steelLargeCellInner;
        else if (item == Large_Fluid_Cell_Aluminium) inner = ExtraIcons.aluminiumLargeCellInner;
        else if (item == Large_Fluid_Cell_StainlessSteel) inner = ExtraIcons.stainlesssteelLargeCellInner;
        else if (item == Large_Fluid_Cell_Titanium) inner = ExtraIcons.titaniumLargeCellInner;
        else if (item == Large_Fluid_Cell_TungstenSteel) inner = ExtraIcons.tungstensteelLargeCellInner;
        else if (item == Large_Fluid_Cell_Iridium) inner = ExtraIcons.iridiumLargeCellInner;
        else if (item == Large_Fluid_Cell_Osmium) inner = ExtraIcons.osmiumLargeCellInner;
        else if (item == Large_Fluid_Cell_Chrome) inner = ExtraIcons.chromiumLargeCellInner;
        else inner = ExtraIcons.neutroniumLargeCellInner;

        // Empty inner side
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
        markNeedsAnimationUpdate(inner);
        if (type.equals(ItemRenderType.INVENTORY)) {
            GT_RenderUtil.renderItemIcon(inner, 16.0D, -0.001D, 0.0F, 0.0F, -1.0F);
        } else {
            ItemRenderer.renderItemIn2D(
                Tessellator.instance,
                inner.getMaxU(),
                inner.getMinV(),
                inner.getMinU(),
                inner.getMaxV(),
                inner.getIconWidth(),
                inner.getIconHeight(),
                0.0625F);
        }

        FluidStack fluidStack = GT_Utility.getFluidForFilledItem(stack, true);

        if (fluidStack != null && fluidStack.getFluid() != null) {
            IIcon fluidIcon = fluidStack.getFluid()
                .getIcon(fluidStack);
            if (fluidIcon == null) {
                fluidIcon = Textures.ItemIcons.RENDERING_ERROR.getIcon();
            }
            int fluidColor = fluidStack.getFluid()
                .getColor(fluidStack);

            Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
            markNeedsAnimationUpdate(fluidIcon);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glDepthFunc(GL11.GL_EQUAL);
            GL11.glColor3ub((byte) (fluidColor >> 16), (byte) (fluidColor >> 8), (byte) fluidColor);
            if (type.equals(ItemRenderType.INVENTORY)) {
                GT_RenderUtil.renderItemIcon(fluidIcon, 16.0D, -0.001D, 0.0F, 0.0F, -1.0F);
            } else {
                ItemRenderer.renderItemIn2D(
                    Tessellator.instance,
                    fluidIcon.getMaxU(),
                    fluidIcon.getMinV(),
                    fluidIcon.getMinU(),
                    fluidIcon.getMaxV(),
                    fluidIcon.getIconWidth(),
                    fluidIcon.getIconHeight(),
                    0.0625F);
            }

            GL11.glColor3ub((byte) -1, (byte) -1, (byte) -1);
            GL11.glDepthFunc(GL11.GL_LEQUAL);
        }
    }

    private static boolean isSame(ItemList item, ItemStack stack) {
        ItemStack internal = item.getInternalStack_unsafe();
        if (GT_Utility.isStackInvalid(internal)) return false;

        return internal.getItem() == stack.getItem() && internal.getItemDamage() == stack.getItemDamage();
    }

    protected void markNeedsAnimationUpdate(IIcon icon) {
        if (HodgePodge.isModLoaded() && icon instanceof IPatchedTextureAtlasSprite) {
            ((IPatchedTextureAtlasSprite) icon).markNeedsAnimationUpdate();
        }
    }
}
