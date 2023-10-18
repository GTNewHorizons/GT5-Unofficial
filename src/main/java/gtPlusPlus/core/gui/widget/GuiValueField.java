package gtPlusPlus.core.gui.widget;

import java.lang.reflect.Field;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

import gtPlusPlus.core.gui.machine.GUI_VolumetricFlaskSetter;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.preloader.CORE_Preloader;

public class GuiValueField extends GuiTextField {

    private final FontRenderer mFontRenderer;
    private final int mScreenLocationX;
    private final int mScreenLocationY;
    private final GUI_VolumetricFlaskSetter mGUI;

    public GuiValueField(FontRenderer aFontRenderer, int aX, int aY, int aScreenLocationX, int aScreenLocationY,
            int aWidth, int aHeight, GUI_VolumetricFlaskSetter aGUI) {
        super(aFontRenderer, aX, aY, aWidth, aHeight);
        mFontRenderer = aFontRenderer;
        mScreenLocationX = aScreenLocationX;
        mScreenLocationY = aScreenLocationY;
        mGUI = aGUI;
    }

    @Override
    public boolean isFocused() {
        return super.isFocused();
    }

    public boolean isBackgroundDrawingEnabled() {
        Field enableBackgroundDrawing = ReflectionUtils.getField(
                GuiTextField.class,
                !CORE_Preloader.DEV_ENVIRONMENT ? "field_146215_m" : "enableBackgroundDrawing");
        if (enableBackgroundDrawing != null) {
            return ReflectionUtils.getFieldValue(enableBackgroundDrawing, this);
        }
        return true;
    }

    public int getLineScrollOffset() {
        Field lineScrollOffset = ReflectionUtils
                .getField(GuiTextField.class, !CORE_Preloader.DEV_ENVIRONMENT ? "field_146225_q" : "lineScrollOffset");
        if (lineScrollOffset != null) {
            return (int) ReflectionUtils.getFieldValue(lineScrollOffset, this);
        }
        return 0;
    }

    public boolean didClickInTextField(int aX, int aY) {
        mGUI.log("Clicked at X:" + aX + ", Y:" + aY);
        boolean aDidClick = aX >= this.mScreenLocationX && aX < this.mScreenLocationX + this.width
                && aY >= this.mScreenLocationY
                && aY < this.mScreenLocationY + this.height;
        mGUI.log("Did click in textbox? " + aDidClick);
        mGUI.log("Expected Region: X:" + mScreenLocationX + "-" + (this.mScreenLocationX + this.width));
        mGUI.log("Expected Region: Y:" + mScreenLocationY + "-" + (this.mScreenLocationY + this.height));
        return aDidClick;
    }

    /**
     * Args: x, y, buttonClicked
     */
    @Override
    public void mouseClicked(int aX, int aY, int aButton) {
        boolean aDidClick = didClickInTextField(aX, aY);

        mGUI.log("Did click inside text box? " + aDidClick);
        mGUI.log("Focus 1: " + this.isFocused());
        this.setFocused(aDidClick);
        mGUI.log("Focus 2: " + this.isFocused());
        if (isFocused()) {
            int l = aX - this.mScreenLocationX;
            if (isBackgroundDrawingEnabled()) {
                l -= 4;
            }
            if (aButton == 0) {
                mGUI.log("Left clicked in text box.");
                String s = this.mFontRenderer
                        .trimStringToWidth(this.getText().substring(getLineScrollOffset()), this.getWidth());
                this.setCursorPosition(this.mFontRenderer.trimStringToWidth(s, l).length() + getLineScrollOffset());
            } else if (aButton == 1) {
                mGUI.log("Right clicked in text box.");
                mGUI.setText(0);
                mGUI.sendUpdateToServer();
                String s = this.mFontRenderer
                        .trimStringToWidth(this.getText().substring(getLineScrollOffset()), this.getWidth());
                this.setCursorPosition(this.mFontRenderer.trimStringToWidth(s, l).length() + getLineScrollOffset());
            }
        } else {
            mGUI.log("Clicked, but no focus.");
        }
    }
}
