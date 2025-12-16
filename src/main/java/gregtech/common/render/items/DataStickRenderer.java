package gregtech.common.render.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import gregtech.api.util.AssemblyLineUtils;

// borrow form ae2
public class DataStickRenderer implements IItemRenderer {

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        final boolean isShiftHeld = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
        final boolean shouldSwitch = item.hasTagCompound() && item.getTagCompound()
            .hasKey("output");
        return type == ItemRenderType.INVENTORY && isShiftHeld && shouldSwitch;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return false;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        final ItemStack itemStack = AssemblyLineUtils.getDataStickOutput(item);
        final Minecraft mc = Minecraft.getMinecraft();
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT);
        RenderHelper.enableGUIStandardItemLighting();
        RenderItem.getInstance()
            .renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), itemStack, 0, 0);
        RenderHelper.disableStandardItemLighting();
        GL11.glPopAttrib();
    }
}
