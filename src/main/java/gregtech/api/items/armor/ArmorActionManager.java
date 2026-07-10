package gregtech.api.items.armor;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;

import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import gregtech.api.items.armor.behaviors.BehaviorName;
import gregtech.common.items.armor.MechArmorBase;

public class ArmorActionManager {

    private static final Map<String, ArmorAction> REGISTRY = new LinkedHashMap<>();
    private static final Map<String, SyncedKeybind> KEYBIND_REGISTRY = new LinkedHashMap<>();
    private static final Map<BehaviorName, Set<SyncedKeybind>> BEHAVIOR_KEYS = new EnumMap<>(BehaviorName.class);

    public static void init() {
        register(
            new ArmorAction(
                "nightvision",
                "Nightvision",
                true,
                SyncedKeybind.createConfigurable("key.gt.toggle_night_vision", "Gregtech Armor", Keyboard.KEY_NONE),
                BehaviorName.NightVision));

        register(
            new ArmorAction(
                "jetpack_hover",
                "Jetpack Hover",
                true,
                SyncedKeybind.createConfigurable("key.gt.toggle_jetpack_hover", "Gregtech Armor", Keyboard.KEY_NONE),
                BehaviorName.JetpackHover));

        register(
            new ArmorAction(
                "jetpack",
                "Jetpack",
                true,
                SyncedKeybind.createConfigurable("key.gt.toggle_jetpack", "Gregtech Armor", Keyboard.KEY_NONE),
                BehaviorName.Jetpack

            ));

        register(
            new ArmorAction(
                "goggles_of_revealing",
                "Goggles of Revealing",
                true,
                SyncedKeybind
                    .createConfigurable("key.gt.toggle_goggles_of_revealing", "Gregtech Armor", Keyboard.KEY_NONE),
                BehaviorName.GogglesOfRevealing));

        register(
            new ArmorAction(
                "inertia_canceling",
                "Inertia Canceling",
                true,
                SyncedKeybind.createConfigurable("key.gt.toggle_inertiacanceling", "Gregtech Armor", Keyboard.KEY_NONE),
                BehaviorName.InertiaCanceling));

        register(
            new ArmorAction(
                "omni_movement",
                "Omni Movement",
                true,
                SyncedKeybind.createConfigurable("key.gt.toggle_omnimovement", "Gregtech Armor", Keyboard.KEY_NONE),
                BehaviorName.OmniMovement));

        register(
            new ArmorAction(
                "speed_increase",
                "Increase Speed",
                false,
                SyncedKeybind.createConfigurable("key.gt.speed_increase", "Gregtech Armor", Keyboard.KEY_NONE),
                BehaviorName.SpeedBoost));

        register(
            new ArmorAction(
                "speed_decrease",
                "Decrease Speed",
                false,
                SyncedKeybind.createConfigurable("key.gt.speed_decrease", "Gregtech Armor", Keyboard.KEY_NONE),
                BehaviorName.SpeedBoost));

        register(
            new ArmorAction(
                "force_field",
                "Force Field",
                true,
                SyncedKeybind.createConfigurable("key.gt.force_field", "Gregtech Armor", Keyboard.KEY_NONE),
                BehaviorName.ForceField));

        register(
            new ArmorAction(
                "holo_inventory",
                "Holo Inventory",
                true,
                SyncedKeybind.createConfigurable("key.gt.toggle_holo_inventory", "Gregtech Armor", Keyboard.KEY_NONE),
                BehaviorName.HoloInventory));

        register(
            new ArmorAction(
                "jump_increase",
                "Increase Jump boost",
                false,
                SyncedKeybind.createConfigurable("key.gt.jump_increase", "Gregtech Armor", Keyboard.KEY_NONE),
                BehaviorName.JumpBoost));

        register(
            new ArmorAction(
                "jump_decrease",
                "Decrease Jump Boost",
                false,
                SyncedKeybind.createConfigurable("key.gt.jump_decrease", "Gregtech Armor", Keyboard.KEY_NONE),
                BehaviorName.JumpBoost));



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

    public static Set<SyncedKeybind> getKeybindsForBehavior(BehaviorName name) {
        if (name == null) return Collections.emptySet();
        return Collections.unmodifiableSet(BEHAVIOR_KEYS.getOrDefault(name, Collections.emptySet()));
    }

    private static void register(String id, SyncedKeybind keybind) {
        KEYBIND_REGISTRY.put(id, keybind);
    }

    private static void register(ArmorAction action) {
        REGISTRY.put(action.getId(), action);

        if (action.getBehaviorName() != null && action.getKeybind() != null) {
            BEHAVIOR_KEYS.computeIfAbsent(action.getBehaviorName(), name -> new HashSet<>())
                .add(action.getKeybind());
        }
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

    /*
     * Checks if the given armor piece is the first (lowest-slot) MechArmor piece equipped. Used to prevent calling code
     * multiple times due to multiple armor pieces.
     */
    public static boolean isPrimaryArmorPiece(EntityPlayer player, Item itemToCheck) {
        for (int i = 0; i < 4; i++) {
            ItemStack piece = player.getCurrentArmor(i);
            if (piece != null && piece.getItem() instanceof MechArmorBase) {
                return piece.getItem() == itemToCheck;
            }
        }
        return false;
    }
}
