package gtPlusPlus.core.gui.widget;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

import gregtech.mixin.interfaces.accessors.GuiTextFieldAccessor;
import gtPlusPlus.core.gui.machine.GUIVolumetricFlaskSetter;

public class GuiValueField extends GuiTextField {

    private final FontRenderer mFontRenderer;
    private final int mScreenLocationX;
    private final int mScreenLocationY;
    private final GUIVolumetricFlaskSetter mGUI;

    public GuiValueField(FontRenderer aFontRenderer, int aX, int aY, int aScreenLocationX, int aScreenLocationY,
        int aWidth, int aHeight, GUIVolumetricFlaskSetter aGUI) {
        super(aFontRenderer, aX, aY, aWidth, aHeight);
        mFontRenderer = aFontRenderer;
        mScreenLocationX = aScreenLocationX;
        mScreenLocationY = aScreenLocationY;
        mGUI = aGUI;
    }

    private int getLineScrollOffset() {
        return ((GuiTextFieldAccessor) this).gt5u$getLineScrollOffset();
    }

    public boolean clickInTextField(int aX, int aY) {
        return aX >= this.mScreenLocationX && aX < this.mScreenLocationX + this.width
            && aY >= this.mScreenLocationY
            && aY < this.mScreenLocationY + this.height;
    }

    /**
     * Args: x, y, buttonClicked
     */
    @Override
    public void mouseClicked(int aX, int aY, int aButton) {
        boolean aDidClick = clickInTextField(aX, aY);
        this.setFocused(aDidClick);
        if (isFocused()) {
            int l = aX - this.mScreenLocationX;
            if (getEnableBackgroundDrawing()) {
                l -= 4;
            }
            if (aButton == 0) {
                String s = this.mFontRenderer.trimStringToWidth(
                    this.getText()
                        .substring(getLineScrollOffset()),
                    this.getWidth());
                this.setCursorPosition(
                    this.mFontRenderer.trimStringToWidth(s, l)
                        .length() + getLineScrollOffset());
            } else if (aButton == 1) {
                mGUI.setText(0);
                mGUI.sendUpdateToServer();
                String s = this.mFontRenderer.trimStringToWidth(
                    this.getText()
                        .substring(getLineScrollOffset()),
                    this.getWidth());
                this.setCursorPosition(
                    this.mFontRenderer.trimStringToWidth(s, l)
                        .length() + getLineScrollOffset());
            }
        }
    }
}
