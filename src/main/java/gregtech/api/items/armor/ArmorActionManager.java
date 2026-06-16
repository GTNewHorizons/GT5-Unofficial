package gregtech.api.items.armor;

import static gregtech.loaders.ExtraIcons.jetpackAugment;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;

import org.lwjgl.input.Keyboard;

import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import gregtech.api.items.armor.behaviors.BehaviorName;

public class ArmorActionManager {

    private static final Map<String, ArmorAction> REGISTRY = new LinkedHashMap<>();
    private static final Map<String, SyncedKeybind> KEYBIND_REGISTRY = new LinkedHashMap<>();

    public static void init() {
        register(
            new ArmorAction(
                "nightvision",
                "Nightvision",
                jetpackAugment,
                true,
                SyncedKeybind.createConfigurable("key.gt.toggle_night_vision", "Gregtech Armor", Keyboard.KEY_R),
                BehaviorName.NightVision));

        register(
            new ArmorAction(
                "jetpack_hover",
                "Jetpack Hover",
                jetpackAugment,
                true,
                SyncedKeybind.createConfigurable("key.gt.toggle_jetpack_hover", "Gregtech Armor", Keyboard.KEY_G),
                BehaviorName.JetpackHover));

        register(
            new ArmorAction(
                "jetpack",
                "Jetpack",
                jetpackAugment,
                true,
                SyncedKeybind.createConfigurable("key.gt.toggle_jetpack", "Gregtech Armor", Keyboard.KEY_F),
                BehaviorName.Jetpack

            ));

        register(
            new ArmorAction(
                "goggles_of_revealing",
                "Goggles of Revealing",
                jetpackAugment,
                true,
                SyncedKeybind
                    .createConfigurable("key.gt.toggle_goggles_of_revealing", "Gregtech Armor", Keyboard.KEY_T),
                BehaviorName.GogglesOfRevealing));

        register(
            new ArmorAction(
                "inertia_canceling",
                "Inertia Canceling",
                jetpackAugment,
                true,
                SyncedKeybind.createConfigurable("key.gt.toggle_inertiacanceling", "Gregtech Armor", Keyboard.KEY_J),
                BehaviorName.InertiaCanceling));

        register(
            new ArmorAction(
                "omni_movement",
                "Omni Movement",
                jetpackAugment,
                true,
                SyncedKeybind.createConfigurable("key.gt.toggle_omnimovement", "Gregtech Armor", Keyboard.KEY_V),
                BehaviorName.OmniMovement));

        register(
            new ArmorAction(
                "speed_increase",
                "Speed Increase",
                jetpackAugment,
                false,
                SyncedKeybind.createConfigurable("key.gt.speed_increase", "Gregtech Armor", Keyboard.KEY_EQUALS),
                BehaviorName.SpeedBoost));

        register(
            new ArmorAction(
                "speed_decrease",
                "Speed Decrease",
                jetpackAugment,
                false,
                SyncedKeybind.createConfigurable("key.gt.speed_decrease", "Gregtech Armor", Keyboard.KEY_MINUS),
                BehaviorName.SpeedBoost));

        register(
            new ArmorAction(
                "force_field",
                "Force Field",
                jetpackAugment,
                true,
                SyncedKeybind.createConfigurable("key.gt.force_field", "Gregtech Armor", Keyboard.KEY_K),
                BehaviorName.ForceField));

        register(
            new ArmorAction(
                "holo_inventory",
                "Holo Inventory",
                jetpackAugment,
                true,
                SyncedKeybind.createConfigurable("key.gt.toggle_holo_inventory", "Gregtech Armor", Keyboard.KEY_H),
                BehaviorName.HoloInventory));

        // Keybinds

        register(
            "open_radial_menu",
            SyncedKeybind.createConfigurable("key.gt.open_radial_menu", "Gregtech Armor", Keyboard.KEY_O));

        register(
            "VANILLA_JUMP",
            SyncedKeybind.createFromMC(() -> () -> Minecraft.getMinecraft().gameSettings.keyBindJump));

        register(
            "VANILLA_SNEAK",
            SyncedKeybind.createFromMC(() -> () -> Minecraft.getMinecraft().gameSettings.keyBindSneak));

        register(
            "VANILLA_FORWARD",
            SyncedKeybind.createFromMC(() -> () -> Minecraft.getMinecraft().gameSettings.keyBindForward));

        register(
            "VANILLA_BACKWARD",
            SyncedKeybind.createFromMC(() -> () -> Minecraft.getMinecraft().gameSettings.keyBindBack));

        register(
            "VANILLA_LEFT",
            SyncedKeybind.createFromMC(() -> () -> Minecraft.getMinecraft().gameSettings.keyBindLeft));

        register(
            "VANILLA_RIGHT",
            SyncedKeybind.createFromMC(() -> () -> Minecraft.getMinecraft().gameSettings.keyBindRight));

    }

    private static void register(String id, SyncedKeybind keybind) {
        KEYBIND_REGISTRY.put(id, keybind);
    }

    private static void register(ArmorAction action) {
        REGISTRY.put(action.getId(), action);
    }

    public static ArmorAction getAction(String id) {
        return REGISTRY.get(id);
    }

    public static SyncedKeybind getKeybind(String id) {
        return KEYBIND_REGISTRY.get(id);
    }

    public static Collection<ArmorAction> getAllActions() {
        return REGISTRY.values();
    }
}
