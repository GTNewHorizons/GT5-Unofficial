package gregtech.api.items.armor;

import net.minecraft.client.Minecraft;

import org.lwjgl.input.Keyboard;

import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

public class ArmorKeybinds {

    public static final SyncedKeybind NIGHT_VISION_KEYBIND = SyncedKeybind
        .createConfigurable("gregtech.toggle_night_vision", "key.categories.gameplay", Keyboard.KEY_R);
    public static final SyncedKeybind JETPACK_HOVER_KEYBIND = SyncedKeybind
        .createConfigurable("gregtech.toggle_jetpack_hover", "key.categories.gameplay", Keyboard.KEY_G);
    public static final SyncedKeybind JETPACK_KEYBIND = SyncedKeybind
        .createConfigurable("gregtech.toggle_jetpack", "key.categories.gameplay", Keyboard.KEY_F);
    public static final SyncedKeybind GOGGLES_OF_REVEALING_KEYBIND = SyncedKeybind
        .createConfigurable("gregtech.toggle_goggles_of_revealing", "key.categories.gameplay", Keyboard.KEY_T);

    public static final SyncedKeybind VANILLA_JUMP = SyncedKeybind
        .createFromMC(() -> () -> Minecraft.getMinecraft().gameSettings.keyBindJump);
    public static final SyncedKeybind VANILLA_SNEAK = SyncedKeybind
        .createFromMC(() -> () -> Minecraft.getMinecraft().gameSettings.keyBindSneak);
    public static final SyncedKeybind VANILLA_FORWARD = SyncedKeybind
        .createFromMC(() -> () -> Minecraft.getMinecraft().gameSettings.keyBindForward);
    public static final SyncedKeybind VANILLA_BACK = SyncedKeybind
        .createFromMC(() -> () -> Minecraft.getMinecraft().gameSettings.keyBindBack);
    public static final SyncedKeybind VANILLA_LEFT = SyncedKeybind
        .createFromMC(() -> () -> Minecraft.getMinecraft().gameSettings.keyBindLeft);
    public static final SyncedKeybind VANILLA_RIGHT = SyncedKeybind
        .createFromMC(() -> () -> Minecraft.getMinecraft().gameSettings.keyBindRight);
}
