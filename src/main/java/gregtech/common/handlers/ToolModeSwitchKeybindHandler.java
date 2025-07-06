package gregtech.common.handlers;

import org.lwjgl.input.Keyboard;

import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import gregtech.api.items.MetaGeneratedTool;

public class ToolModeSwitchKeybindHandler {

    public static SyncedKeybind TOOL_MODE_SWITCH_KEYBIND;

    private ToolModeSwitchKeybindHandler() {}

    public static void init() {
        TOOL_MODE_SWITCH_KEYBIND = SyncedKeybind
            .createConfigurable("key.gt.tool_mode_switch", "Gregtech", Keyboard.KEY_PERIOD)
            .registerGlobalListener(MetaGeneratedTool::switchToolMode);
    }
}
