package gregtech.client.hud.elements;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.client.hud.HUDGui;

@SideOnly(Side.CLIENT)
public class DynamicItemElement extends WidgetElement<DynamicItemElement> implements Configurable {

    private final Supplier<ItemStack> stackSupplier;
    public float scale = 1.0f;
    private int offsetHorizontal = 0, offsetVertical = 0;

    public DynamicItemElement(Supplier<ItemStack> stackSupplier) {
        this.stackSupplier = stackSupplier;
        this.width = 16;
        this.height = 16;
    }

    @Override
    protected void renderSelf(Minecraft mc, FontRenderer fr, int baseX, int baseY, double mouseX, double mouseY) {
        ItemStack stack = stackSupplier.get();
        if (stack == null) return;

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);

        GL11.glTranslatef(absLeft(baseX) + offsetHorizontal, absTop(baseY) + offsetVertical, 0);
        GL11.glScalef(scale, scale, scale);

        RenderHelper.enableGUIStandardItemLighting();
        RenderItem renderItem = RenderItem.getInstance();
        renderItem.renderItemAndEffectIntoGUI(fr, mc.getTextureManager(), stack, 0, 0);
        RenderHelper.disableStandardItemLighting();

        GL11.glPopMatrix();
    }

    @Override
    public int configure(HUDGui.GuiConfigureElement gui, int yOff) {
        // spotless:off
        yOff = HUDGui.GuiConfigureElement.createFloatConfig(gui, "Scale:", scale, val -> { this.scale = (float) val; updateAfterConfigChange(); }, yOff);
        yOff = HUDGui.GuiConfigureElement.createIntConfig(gui, "Offset H:", offsetHorizontal, val -> { this.offsetHorizontal = val; updateAfterConfigChange(); }, yOff);
        yOff = HUDGui.GuiConfigureElement.createIntConfig(gui, "Offset V:", offsetVertical, val -> { this.offsetVertical = val; updateAfterConfigChange(); }, yOff);
        //spotless:on
        return yOff;
    }
}
