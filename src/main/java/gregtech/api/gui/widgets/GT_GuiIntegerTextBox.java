package gregtech.api.gui.widgets;

import java.awt.*;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;

import gregtech.api.interfaces.IGuiScreen;

public class GT_GuiIntegerTextBox extends GuiTextField implements IGuiScreen.IGuiElement {

    private final int x0, y0;
    private final IGuiScreen gui;
    public final int id;
    private boolean enabled;

    public GT_GuiIntegerTextBox(IGuiScreen gui, int id, int x, int y, int width, int height) {
        super(Minecraft.getMinecraft().fontRenderer, x, y, width, height);
        super.setText("");
        this.id = id;
        x0 = x;
        y0 = y;
        this.gui = gui;
        enabled = true;
        gui.addElement(this);
    }

    @Override
    public void onInit() {
        xPosition = x0 + gui.getGuiLeft();
        yPosition = y0 + gui.getGuiTop();
    }

    @Override
    public void draw(int mouseX, int mouseY, float parTicks) {
        super.drawTextBox();
    }

    public Rectangle getBounds() {
        return new Rectangle(x0, y0, width, height);
    }

    public boolean validChar(char c, int key) {
        return Character.isDigit(c);
    }

    @Override
    public boolean textboxKeyTyped(char c, int key) {
        if (validChar(c, key) || c == 1
            || c == 3
            || c == 22
            || c == 24
            || key == 14
            || key == 199
            || key == 203
            || key == 205
            || key == 207
            || key == 211) {
            return super.textboxKeyTyped(c, key);
        }
        return false;
    }

    @Override
    public void setEnabled(boolean p_146184_1_) {
        super.setEnabled(p_146184_1_);
        enabled = p_146184_1_;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
