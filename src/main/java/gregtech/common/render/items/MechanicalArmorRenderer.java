package gregtech.common.render.items;

import static gregtech.api.enums.Dyes.dyeGreen;
import static gregtech.api.enums.Dyes.dyeLightBlue;
import static gregtech.api.enums.Dyes.dyeOrange;
import static gregtech.api.enums.Dyes.dyeRed;
import static gregtech.api.enums.Dyes.dyeWhite;
import static gregtech.api.items.armor.MechArmorAugmentRegistries.coresMap;
import static gregtech.api.items.armor.MechArmorAugmentRegistries.framesMap;
import static gregtech.api.util.GTUtility.getOrCreateNbtCompound;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

import org.lwjgl.opengl.GL11;

import gregtech.api.enums.ItemList;
import gregtech.api.items.armor.behaviors.IArmorBehavior;
import gregtech.common.items.armor.MechArmorBase;
import gregtech.common.render.GTRenderUtil;

public class MechanicalArmorRenderer implements IItemRenderer {

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

        String core;
        String frame;
        short frameR = -1, frameG = -1, frameB = -1;

        NBTTagCompound tag = getOrCreateNbtCompound(item);
        core = tag.getString("core");
        frame = tag.getString("frame");

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_ALPHA_TEST);

        GTRenderUtil.renderItem(type, baseLayer);

        // TODO: remove unnecessary second check thats just here to make my test world not crash
        if (!frame.isEmpty() && framesMap.containsKey(frame)) {
            short[] frameColor = framesMap.get(frame).color;
            GL11.glColor4f(frameColor[0] / 255.0F, frameColor[1] / 255.0F, frameColor[2] / 255.0F, 1);
            GTRenderUtil.renderItem(type, frameLayer);
        }

        if (!core.isEmpty() && coresMap.containsKey(core)) {
            short[] modulation = dyeWhite.getRGBA();
            switch (coresMap.get(core).tier) {
                case 1 -> modulation = dyeRed.getRGBA();
                case 2 -> modulation = dyeGreen.getRGBA();
                case 3 -> modulation = dyeLightBlue.getRGBA();
                case 4 -> modulation = dyeOrange.getRGBA();
            }
            GL11.glColor4f(modulation[0] / 255.0F, modulation[1] / 255.0F, modulation[2] / 255.0F, 1);
            GTRenderUtil.renderItem(type, coreLayer);
            GL11.glColor4f(1, 1, 1, 1);
        }

        for (IArmorBehavior behavior : armorItem.getBehaviors()) {
            if (tag.hasKey(behavior.getMainNBTTag()) && behavior.getModularArmorTexture() != null)
                GTRenderUtil.renderItem(type, behavior.getModularArmorTexture());
        }

        GL11.glDisable(GL11.GL_BLEND);
    }

}
