package gregtech.api.gui.widgets;

import java.awt.Rectangle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import org.lwjgl.opengl.GL11;

import gregtech.api.interfaces.IGuiScreen;

public class GT_GuiIconButton extends GuiButton implements IGuiScreen.IGuiElement {

    public static final int DEFAULT_WIDTH = 16;
    public static final int DEFAULT_HEIGHT = 16;

    protected GT_GuiIcon icon;
    private final int x0;
    private final int y0;
    protected final IGuiScreen gui;

    private GT_GuiTooltip tooltip;

    public GT_GuiIconButton(IGuiScreen gui, int id, int x, int y, GT_GuiIcon icon) {
        super(id, x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, "");
        this.gui = gui;
        this.icon = icon;
        this.x0 = x;
        this.y0 = y;
        gui.addElement(this);
    }

    @Override
    public void onInit() {
        if (tooltip != null) gui.addToolTip(tooltip);
        xPosition = x0 + gui.getGuiLeft();
        yPosition = y0 + gui.getGuiTop();
    }

    @Override
    public void draw(int mouseX, int mouseY, float parTicks) {
        drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.tooltip != null) this.tooltip.enabled = true;

        if (this.visible) {
            // moused over
            this.field_146123_n = mouseX >= this.xPosition && mouseY >= this.yPosition
                && mouseX < this.xPosition + width
                && mouseY < this.yPosition + height;

            mouseDragged(mc, mouseX, mouseY);

            GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            int x = xPosition;
            int y = yPosition;
            if (!this.field_146123_n) {
                // GL11.glColor4f(200F/255F, 210F/255F, 1, 1);
            } else GL11.glColor4f(1, 1, 1, 1);

            GT_GuiIcon.render(getButtonTexture(this.field_146123_n), x, y, width, height, 0, true);

            GL11.glColor4f(1, 1, 1, 1);
            if (icon != null) {
                GT_GuiIcon.render(icon, x, y, width, height, 0, true);
            }

            GL11.glPopAttrib();
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        this.gui.clearSelectedButton();
        if (mousePressed(Minecraft.getMinecraft(), mouseX, mouseY)) this.gui.buttonClicked(this);
    }

    public GT_GuiIcon getButtonTexture(boolean mouseOver) {
        if (!enabled) return GT_GuiIcon.BUTTON_DISABLED;
        if (this.equals(this.gui.getSelectedButton()))
            return mouseOver ? GT_GuiIcon.BUTTON_HIGHLIGHT_DOWN : GT_GuiIcon.BUTTON_DOWN;

        return mouseOver ? GT_GuiIcon.BUTTON_HIGHLIGHT : GT_GuiIcon.BUTTON_NORMAL;
    }

    public GT_GuiIcon getIcon() {
        return icon;
    }

    public GT_GuiIconButton setIcon(GT_GuiIcon icon) {
        this.icon = icon;
        return this;
    }

    public GT_GuiTooltip getTooltip() {
        return tooltip;
    }

    public GT_GuiIconButton setTooltipText(String... text) {
        if (tooltip == null) tooltip = new GT_GuiTooltip(getBounds(), text);
        else tooltip.setToolTipText(text);
        return this;
    }

    public Rectangle getBounds() {
        return new Rectangle(x0, y0, width, height);
    }
}
