package gtPlusPlus.xmod.gregtech.common.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.SideOnly;
import gregtech.common.items.ItemVolumetricFlask;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import ic2.core.util.DrawUtil;

@SideOnly(cpw.mods.fml.relauncher.Side.CLIENT)
public final class FlaskRenderer implements net.minecraftforge.client.IItemRenderer {

    public FlaskRenderer() {
        MinecraftForgeClient.registerItemRenderer(GregtechItemList.VOLUMETRIC_FLASK_8k.getItem(), this);
        MinecraftForgeClient.registerItemRenderer(GregtechItemList.VOLUMETRIC_FLASK_32k.getItem(), this);
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type != ItemRenderType.FIRST_PERSON_MAP;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper) {
        return type == ItemRenderType.ENTITY;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        ItemVolumetricFlask cell = (ItemVolumetricFlask) item.getItem();

        int aType = cell.getMaxCapacity() == 8000 ? 0 : 1;
        IIcon icon = item.getIconIndex();
        GL11.glEnable(3042);
        GL11.glEnable(3008);
        if (type.equals(ItemRenderType.ENTITY)) {
            GL11.glRotated(180.0D, 0.0D, 0.0D, 1.0D);
            GL11.glRotated(90.0D, 0.0D, 1.0D, 0.0D);
            GL11.glTranslated(-0.5D, -0.6D, 0.0D);
        } else if (type.equals(ItemRenderType.EQUIPPED_FIRST_PERSON)) {
            GL11.glTranslated(1.0D, 1.0D, 0.0D);
            GL11.glRotated(180.0D, 0.0D, 0.0D, 1.0D);
        } else if (type.equals(ItemRenderType.EQUIPPED)) {
            GL11.glRotated(180.0D, 0.0D, 0.0D, 1.0D);
            GL11.glTranslated(-1.0D, -1.0D, 0.0D);
        }

        FluidStack fs = cell.getFluid(item);
        if (fs != null) {
            IIcon iconWindow = cell.iconWindow;
            IIcon fluidicon = fs.getFluid()
                .getIcon(fs);
            int fluidColor = fs.getFluid()
                .getColor(fs);
            Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
            GL11.glBlendFunc(0, 1);
            if (type.equals(ItemRenderType.INVENTORY)) {
                DrawUtil.renderIcon(iconWindow, 16.0D, 0.0D, 0.0F, 0.0F, -1.0F);
            } else {
                DrawUtil.renderIcon(iconWindow, 1.0D, -0.001D, 0.0F, 0.0F, 1.0F);
                DrawUtil.renderIcon(iconWindow, 1.0D, -0.0615D, 0.0F, 0.0F, -1.0F);
            }

            Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
            GL11.glBlendFunc(770, 771);
            GL11.glDepthFunc(514);
            GL11.glColor3ub((byte) (fluidColor >> 16), (byte) (fluidColor >> 8), (byte) fluidColor);
            if (type.equals(ItemRenderType.INVENTORY)) {
                DrawUtil.renderIcon(fluidicon, 16.0D, 0.0D, 0.0F, 0.0F, -1.0F);
            } else {
                DrawUtil.renderIcon(fluidicon, 1.0D, -0.001D, 0.0F, 0.0F, 1.0F);
                DrawUtil.renderIcon(fluidicon, 1.0D, -0.0615D, 0.0F, 0.0F, -1.0F);
            }

            GL11.glColor3ub((byte) -1, (byte) -1, (byte) -1);
            GL11.glDepthFunc(515);
        }

        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
        GL11.glBlendFunc(770, 771);
        if (type.equals(ItemRenderType.INVENTORY)) {
            DrawUtil.renderIcon(icon, 16.0D, 0.001D, 0.0F, 0.0F, -1.0F);
        } else {
            ItemRenderer.renderItemIn2D(
                Tessellator.instance,
                icon.getMaxU(),
                icon.getMinV(),
                icon.getMinU(),
                icon.getMaxV(),
                icon.getIconWidth(),
                icon.getIconHeight(),
                0.0625F);
        }
        GL11.glDisable(3008);
        GL11.glDisable(3042);
    }
}
