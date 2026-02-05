package tectech.thing.item;

import static tectech.rendering.EOH.EOHRenderingUtils.renderGORGEStar;
import static tectech.thing.block.RenderForgeOfGods.disableOpaqueColorInversion;
import static tectech.thing.block.RenderForgeOfGods.enableOpaqueColorInversion;
import static tectech.thing.block.RenderForgeOfGods.enablePseudoTransparentColorInversion;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

public class ItemRenderForgeOfGods implements IItemRenderer {

    @Override
    public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item,
        IItemRenderer.ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data) {
        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);

        // Hack
        World world = Minecraft.getMinecraft().theWorld;
        if (world == null) return;
        float time = world.getTotalWorldTime() * 0.2f;

        enableOpaqueColorInversion();
        renderGORGEStar(type, time, 0.82);
        disableOpaqueColorInversion();

        enablePseudoTransparentColorInversion();

        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }
}
