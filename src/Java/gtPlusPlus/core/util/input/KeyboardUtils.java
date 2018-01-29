package gtPlusPlus.core.util.input;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;

public class KeyboardUtils {

	public static boolean isCtrlKeyDown(){
		// prioritize CONTROL, but allow OPTION as well on Mac (note: GuiScreen's isCtrlKeyDown only checks for the OPTION key on Mac)
		boolean isCtrlKeyDown = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
		if (!isCtrlKeyDown && Minecraft.isRunningOnMac)
			isCtrlKeyDown = Keyboard.isKeyDown(Keyboard.KEY_LMETA) || Keyboard.isKeyDown(Keyboard.KEY_RMETA);
	
		return isCtrlKeyDown;
	}

	public static boolean isShiftKeyDown(){
		return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
		
	}
	
}
