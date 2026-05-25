package gregtech.api.items.armor.behaviors;

import static gregtech.api.items.armor.ArmorKeybinds.HOLO_INVENTORY_KEYBIND;
import static gregtech.loaders.ExtraIcons.holoInventoryAugment;

import java.util.Collections;
import java.util.Set;

import net.minecraft.util.IIcon;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import gregtech.api.items.armor.ArmorContext;

public class HoloInventoryBehavior implements IArmorBehavior {

    public static final HoloInventoryBehavior INSTANCE = new HoloInventoryBehavior();
    private static final Set<SyncedKeybind> LISTENED_KEYS = Collections.singleton(HOLO_INVENTORY_KEYBIND);

    protected HoloInventoryBehavior() {/**/}

    @Override
    public BehaviorName getName() {
        return BehaviorName.HoloInventory;
    }

    @Override
    public IIcon getModularArmorTexture() {
        return holoInventoryAugment;
    }

    @Override
    public void onKeyPressed(@NotNull ArmorContext context, SyncedKeybind keyPressed, boolean isDown) {
        if (!isDown) return;

        context.toggleBehavior(BehaviorName.HoloInventory);
    }

    @Override
    public Set<SyncedKeybind> getListenedKeys(@NotNull ArmorContext context) {
        return LISTENED_KEYS;
    }
}
