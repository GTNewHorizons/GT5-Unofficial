package gregtech.api.items.armor;

import net.minecraft.client.Minecraft;

import org.lwjgl.input.Keyboard;

import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

public class ArmorKeybinds {

    public static final SyncedKeybind NIGHT_VISION_KEYBIND = SyncedKeybind
        .createConfigurable("key.gt.toggle_night_vision", "Gregtech Armor", Keyboard.KEY_R);
    public static final SyncedKeybind JETPACK_HOVER_KEYBIND = SyncedKeybind
        .createConfigurable("key.gt.toggle_jetpack_hover", "Gregtech Armor", Keyboard.KEY_G);
    public static final SyncedKeybind JETPACK_KEYBIND = SyncedKeybind
        .createConfigurable("key.gt.toggle_jetpack", "Gregtech Armor", Keyboard.KEY_F);
    public static final SyncedKeybind GOGGLES_OF_REVEALING_KEYBIND = SyncedKeybind
        .createConfigurable("key.gt.toggle_goggles_of_revealing", "Gregtech Armor", Keyboard.KEY_T);
    public static final SyncedKeybind INERTIA_CANCELING_KEYBIND = SyncedKeybind
        .createConfigurable("key.gt.toggle_inertiacanceling", "Gregtech Armor", Keyboard.KEY_T);
    public static final SyncedKeybind OMNI_MOVEMENT_KEYBIND = SyncedKeybind
        .createConfigurable("key.gt.toggle_omnimovement", "Gregtech Armor", Keyboard.KEY_T);
    public static final SyncedKeybind SPEED_INCREASE_KEYBIND = SyncedKeybind
        .createConfigurable("key.gt.speed_increase", "Gregtech Armor", Keyboard.KEY_EQUALS);
    public static final SyncedKeybind SPEED_DECREASE_KEYBIND = SyncedKeybind
        .createConfigurable("key.gt.speed_decrease", "Gregtech Armor", Keyboard.KEY_MINUS);
    public static final SyncedKeybind FORCE_FIELD_KEYBIND = SyncedKeybind
        .createConfigurable("key.gt.force_field", "Gregtech Armor", Keyboard.KEY_K);

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
