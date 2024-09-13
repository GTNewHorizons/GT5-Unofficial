package gregtech.common.render.items;

import static gregtech.api.enums.Mods.GregTech;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.IIconRegistration;
import gregtech.common.items.behaviors.BehaviourSprayColorInfinite;
import gregtech.common.render.GTRenderUtil;

public class InfiniteSprayCanRenderer implements IItemRenderer, IIconRegistration {

    private IIcon baseLayer;
    private IIcon paintRegion;
    private IIcon lockLayer;

    @Override
    public boolean handleRenderType(final ItemStack item, final ItemRenderType type) {
        return type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON
            || type == ItemRenderType.INVENTORY
            || type == ItemRenderType.ENTITY;

    }

    @Override
    public boolean shouldUseRenderHelper(final ItemRenderType type, final ItemStack item,
        final ItemRendererHelper helper) {
        return type == ItemRenderType.ENTITY && helper == ItemRendererHelper.ENTITY_BOBBING
            || (helper == ItemRendererHelper.ENTITY_ROTATION && Minecraft.getMinecraft().gameSettings.fancyGraphics);

    }

    @Override
    public void registerIcons(final IIconRegister iconRegister, final String basePath) {
        baseLayer = iconRegister.registerIcon(GregTech.getResourcePath(basePath + "/" + "base"));
        // noinspection SpellCheckingInspection
        paintRegion = iconRegister.registerIcon(GregTech.getResourcePath(basePath + "/" + "paintregion"));
        lockLayer = iconRegister.registerIcon(GregTech.getResourcePath(basePath + "/" + "locked"));
    }

    @Override
    public void renderItem(final ItemRenderType type, final ItemStack item, final Object... data) {
        if (baseLayer == null || paintRegion == null || lockLayer == null) {
            throw new RuntimeException("registerIcons not called!");
        }

        Dyes dye = BehaviourSprayColorInfinite.getDye(item);
        short[] modulation = dye.getRGBA();
        boolean locked = false;

        if (item.hasTagCompound()) {
            locked = item.getTagCompound()
                .getBoolean(BehaviourSprayColorInfinite.LOCK_NBT_TAG);
        }

        GTRenderUtil.renderItem(type, baseLayer);

        GL11.glColor3f(modulation[0] / 255.0F, modulation[1] / 255.0F, modulation[2] / 255.0F);
        GTRenderUtil.renderItem(type, paintRegion);
        GL11.glColor3f(255, 255, 255);

        if (locked) {
            GTRenderUtil.renderItem(type, lockLayer);
        }
    }
}
