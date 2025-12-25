package gtPlusPlus.core.util.sys;

import net.minecraft.client.Minecraft;

import org.lwjgl.input.Keyboard;

public class KeyboardUtils {

    public static boolean isCtrlKeyDown() {
        try {
            if (!Keyboard.isCreated()) {
                return false;
            }
            // prioritize CONTROL, but allow OPTION as well on Mac (note: GuiScreen's isCtrlKeyDown only checks for the
            // OPTION key on Mac)
            boolean isCtrlKeyDown = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)
                || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
            if (!isCtrlKeyDown && Minecraft.isRunningOnMac)
                isCtrlKeyDown = Keyboard.isKeyDown(Keyboard.KEY_LMETA) || Keyboard.isKeyDown(Keyboard.KEY_RMETA);

            return isCtrlKeyDown;
        } catch (Exception t) {
            return false;
        }
    }

    public static boolean isShiftKeyDown() {
        try {
            if (!Keyboard.isCreated()) {
                return false;
            }
            return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
        } catch (Exception t) {
            return false;
        }
    }
}
