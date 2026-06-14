package gregtech.api.items.armor;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;
import net.minecraft.util.IIcon;
import org.lwjgl.input.Keyboard;

import static gregtech.loaders.ExtraIcons.jetpackAugment;

public class ArmorActionManager {
    private static final Map<String, ArmorAction> REGISTRY = new LinkedHashMap<>();

    public static void init() {
        register(new gregtech.api.items.armor.ArmorAction(
            "nightvision",
            "Nightvision",
            jetpackAugment,
            SyncedKeybind.createConfigurable("key.armor.nv", "key.categories.gregtech_armor", Keyboard.KEY_N)
        ));

        register(new ArmorAction(
            "jetpack",
            "Jetpack",
            jetpackAugment,
            SyncedKeybind.createConfigurable("key.armor.jetpack", "key.categories.gregtech_armor", Keyboard.KEY_F)
        ));
    }

    private static void register(ArmorAction action) {
        REGISTRY.put(action.getId(), action);
    }

    public static ArmorAction getAction(String id) {
        return REGISTRY.get(id);
    }

    public static Collection<ArmorAction> getAllActions() {
        return REGISTRY.values();
    }
}
