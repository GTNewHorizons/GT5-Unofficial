package gregtech.api.items.armor;

import org.lwjgl.input.Keyboard;

import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

public class ArmorKeybinds {
    public static final SyncedKeybind NIGHT_VISION_KEY = SyncedKeybind
        .createConfigurable("gregtech.toggle_night_vision", "key.categories.gameplay", Keyboard.KEY_F);
}
