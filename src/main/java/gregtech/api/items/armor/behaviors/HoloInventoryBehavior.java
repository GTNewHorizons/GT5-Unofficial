package gregtech.api.items.armor.behaviors;

import java.util.Set;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import gregtech.api.items.armor.ArmorActionManager;
import gregtech.api.items.armor.ArmorContext;

public class HoloInventoryBehavior implements IArmorBehavior {

    public static final HoloInventoryBehavior INSTANCE = new HoloInventoryBehavior();

    protected HoloInventoryBehavior() {/**/}

    @Override
    public BehaviorName getName() {
        return BehaviorName.HoloInventory;
    }

    @Override
    public void onKeyPressed(@NotNull ArmorContext context, SyncedKeybind keyPressed, boolean isDown) {
        if (!isDown) return;

        context.toggleBehavior(BehaviorName.HoloInventory);
    }

    @Override
    public Set<SyncedKeybind> getListenedKeys(@NotNull ArmorContext context) {
        return ArmorActionManager.getKeybindsForBehavior(getName());
    }
}
