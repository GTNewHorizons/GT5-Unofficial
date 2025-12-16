package gregtech.common.render.items;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizon.gtnhlib.util.ItemRenderUtil;

import gregtech.api.enums.Dyes;
import gregtech.api.items.MetaGeneratedItem;
import gregtech.common.items.behaviors.BehaviourSprayColorInfinite;

public class InfiniteSprayCanRenderer implements IItemRenderer {

    @Override
    public boolean handleRenderType(final ItemStack item, final ItemRenderType type) {
        return type != ItemRenderType.FIRST_PERSON_MAP;
    }

    @Override
    public boolean shouldUseRenderHelper(final ItemRenderType type, final ItemStack item,
        final ItemRendererHelper helper) {
        return type == ItemRenderType.ENTITY && helper == ItemRendererHelper.ENTITY_BOBBING
            || (helper == ItemRendererHelper.ENTITY_ROTATION && Minecraft.getMinecraft().gameSettings.fancyGraphics);

    }

    @Override
    public void renderItem(final ItemRenderType type, final ItemStack item, final Object... data) {
        final Dyes dye = BehaviourSprayColorInfinite.getDye(item);
        final short[] modulation = dye.getRGBA();

        if (!(item.getItem() instanceof final MetaGeneratedItem mgItem)) {
            return;
        }

        final IIcon[] iconList = mgItem.mIconList[item.getItemDamage() - mgItem.mOffset];
        if (iconList == null || iconList.length < 4) {
            return;
        }

        final IIcon baseLayer = iconList[1];
        final IIcon lockLayer = iconList[2];
        final IIcon paintRegion = iconList[3];

        if (baseLayer == null || paintRegion == null || lockLayer == null) {
            return;
        }

        boolean locked = false;

        if (item.hasTagCompound()) {
            locked = item.getTagCompound()
                .getBoolean(BehaviourSprayColorInfinite.LOCK_NBT_TAG);
        }

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_ALPHA_TEST);

        ItemRenderUtil.renderItem(type, baseLayer);

        GL11.glColor4f(modulation[0] / 255.0F, modulation[1] / 255.0F, modulation[2] / 255.0F, 1);
        ItemRenderUtil.renderItem(type, paintRegion);
        GL11.glColor4f(1, 1, 1, 1);

        if (locked) {
            ItemRenderUtil.renderItem(type, lockLayer);
        }

        GL11.glDisable(GL11.GL_BLEND);
    }
}
