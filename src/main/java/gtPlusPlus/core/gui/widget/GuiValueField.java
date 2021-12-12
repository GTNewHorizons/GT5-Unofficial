package gtPlusPlus.core.gui.widget;

import java.lang.reflect.Field;

import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public class GuiValueField extends GuiTextField {

	private final FontRenderer mFontRenderer;
	private final int mScreenLocationX;
	private final int mScreenLocationY;
	
	public GuiValueField(FontRenderer aFontRenderer, int aX, int aY, int aScreenLocationX, int aScreenLocationY, int aWidth, int aHeight) {
		super(aFontRenderer, aX, aY, aWidth, aHeight);
		mFontRenderer = aFontRenderer;
		mScreenLocationX = aScreenLocationX;
		mScreenLocationY = aScreenLocationY;
	}
	
	public boolean canLoseFocus() {		
		Field canLoseFocus = ReflectionUtils.getField(GuiTextField.class, "canLoseFocus");
		if (canLoseFocus != null) {
			return (boolean) ReflectionUtils.getFieldValue(canLoseFocus, this);
		}		
		return true;
	}
	
	public boolean isFocused() {		
		Field isFocused = ReflectionUtils.getField(GuiTextField.class, "isFocused");
		if (isFocused != null) {
			return (boolean) ReflectionUtils.getFieldValue(isFocused, this);
		}		
		return false;
	}
	
	public boolean isBackgroundDrawingEnabled() {		
		Field enableBackgroundDrawing = ReflectionUtils.getField(GuiTextField.class, "enableBackgroundDrawing");
		if (enableBackgroundDrawing != null) {
			return (boolean) ReflectionUtils.getFieldValue(enableBackgroundDrawing, this);
		}		
		return true;
	}
	public int getLineScrollOffset() {		
		Field lineScrollOffset = ReflectionUtils.getField(GuiTextField.class, "lineScrollOffset");
		if (lineScrollOffset != null) {
			return (int) ReflectionUtils.getFieldValue(lineScrollOffset, this);
		}		
		return 0;
	}
	
    /**
     * Args: x, y, buttonClicked
     */
    public void mouseClicked(int aX, int aY, int aButton){
    	
        boolean flag = aX >= this.mScreenLocationX && aX < this.mScreenLocationX + this.width && aY >= this.mScreenLocationY && aY < this.mScreenLocationY + this.height;

        //Logger.INFO("Clicked X:"+aX);
        //Logger.INFO("Clicked Y:"+aY);
        //Logger.INFO("ScreenPos X:"+mScreenLocationX);
        //Logger.INFO("ScreenPos Y:"+mScreenLocationY);
        //Logger.INFO("Render X:"+xPosition);
        //Logger.INFO("Render Y:"+yPosition);
        
        if (canLoseFocus())
        {
            this.setFocused(flag);
        }

        if (isFocused() && aButton == 0)
        {
            int l = aX - this.mScreenLocationX;

            if (isBackgroundDrawingEnabled())
            {
                l -= 4;
            }

            String s = this.mFontRenderer.trimStringToWidth(this.getText().substring(getLineScrollOffset()), this.getWidth());
            this.setCursorPosition(this.mFontRenderer.trimStringToWidth(s, l).length() + getLineScrollOffset());
        }
    }

}
