package gregtech.common.render.items;

import static gregtech.api.enums.Dyes.dyeGreen;
import static gregtech.api.enums.Dyes.dyeLightBlue;
import static gregtech.api.enums.Dyes.dyeRed;
import static gregtech.api.enums.Dyes.dyeWhite;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

import org.lwjgl.opengl.GL11;

import gregtech.api.enums.ItemList;
import gregtech.common.items.armor.MechArmorBase;
import gregtech.common.render.GTRenderUtil;

public class MechanicalArmorRenderer implements IItemRenderer {

    public MechanicalArmorRenderer() {
        MinecraftForgeClient.registerItemRenderer(ItemList.Mechanical_Helmet.getItem(), this);
        MinecraftForgeClient.registerItemRenderer(ItemList.Mechanical_Chestplate.getItem(), this);
        MinecraftForgeClient.registerItemRenderer(ItemList.Mechanical_Leggings.getItem(), this);
        MinecraftForgeClient.registerItemRenderer(ItemList.Mechanical_Boots.getItem(), this);
    }

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
    public void renderItem(final ItemRenderType type, final ItemStack item, final Object... data) {
        if (!(item.getItem() instanceof MechArmorBase armorItem)) return;

        IIcon baseLayer = item.getIconIndex();
        IIcon coreLayer = armorItem.getCoreIcon();
        IIcon frameLayer = armorItem.getFrameIcon();

        if (baseLayer == null || coreLayer == null) {
            return;
        }

        int coreTier = 0;
        short frameR = -1, frameG = -1, frameB = -1;

        if (item.hasTagCompound()) {
            NBTTagCompound tag = item.getTagCompound();
            coreTier = tag.getInteger("core");

            frameR = tag.getShort("frameR");
            frameG = tag.getShort("frameG");
            frameB = tag.getShort("frameB");
        }

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_ALPHA_TEST);

        GTRenderUtil.renderItem(type, baseLayer);

        if (frameR != -1) {
            GL11.glColor4f(frameR / 255.0F, frameG / 255.0F, frameB/ 255.0F, 1);
            GTRenderUtil.renderItem(type, frameLayer);
        }

        if (coreTier != 0) {
            short[] modulation = dyeWhite.getRGBA();
            switch (coreTier) {
                case 1 -> modulation = dyeGreen.getRGBA();
                case 2 -> modulation = dyeLightBlue.getRGBA();
                case 3 -> modulation = dyeRed.getRGBA();
            }
            GL11.glColor4f(modulation[0] / 255.0F, modulation[1] / 255.0F, modulation[2] / 255.0F, 1);
            GTRenderUtil.renderItem(type, coreLayer);
        }

        GL11.glDisable(GL11.GL_BLEND);
    }

}
