package gregtech.common.render.items;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizon.gtnhlib.util.ItemRenderUtil;

import gregtech.api.items.MetaGeneratedItem;
import gregtech.common.config.Client;

public class GlitchEffectMetaItemRenderer implements IItemRenderer {

    public Random rand = new Random();

    final long frameTimeNanos = 10_000_000L;
    final int loopFrameCount = 200;
    final int glitchedDurationCount = 40;
    final int glitchMoveFrameCount = 5;

    double offsetRed = 0;
    double offsetCyan = 0;

    @Override
    public boolean handleRenderType(final ItemStack item, final ItemRenderType type) {
        return Client.render.renderGlitchFancy
            && (type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON
                || type == ItemRenderType.INVENTORY
                || type == ItemRenderType.ENTITY);

    }

    @Override
    public boolean shouldUseRenderHelper(final ItemRenderType type, final ItemStack item,
        final ItemRendererHelper helper) {
        return type == ItemRenderType.ENTITY && helper == ItemRendererHelper.ENTITY_BOBBING
            || (helper == ItemRendererHelper.ENTITY_ROTATION && Minecraft.getMinecraft().gameSettings.fancyGraphics);

    }

    @Override
    public void renderItem(final ItemRenderType type, final ItemStack item, final Object... data) {
        if (item.getItem() instanceof MetaGeneratedItem mgItem) {
            IIcon[] icons = mgItem.mIconList[item.getItemDamage() - mgItem.mOffset];
            if (icons != null && icons.length > 0 && icons[0] != null) {

                int currentFrame = (int) ((System.nanoTime() % (frameTimeNanos * loopFrameCount)) / frameTimeNanos);
                boolean timing = currentFrame <= glitchedDurationCount;

                if (timing && currentFrame % glitchMoveFrameCount == 0) {
                    offsetRed = rand.nextDouble() * 1.7 * Math.signum(rand.nextGaussian());
                    offsetCyan = rand.nextDouble() * 1.7 * Math.signum(rand.nextGaussian());
                }

                // Restore state before rendering the actual item
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glEnable(GL11.GL_ALPHA_TEST);

                ItemRenderUtil.renderItem(type, icons[0]);

                if (type == ItemRenderType.INVENTORY && timing) {
                    GL11.glDisable(GL11.GL_DEPTH_TEST);
                    GlitchEffectRenderer.applyCyanGlitchEffect(type, offsetCyan, icons[0]);
                    GL11.glEnable(GL11.GL_DEPTH_TEST);
                    GlitchEffectRenderer.applyRedGlitchEffect(type, offsetRed, icons[0]);
                }
                GL11.glDisable(GL11.GL_BLEND);
            }
        }
    }
}
