package gregtech.api.items.armor.behaviors;

import java.util.Collections;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import gregtech.api.items.armor.ArmorActionManager;
import gregtech.api.items.armor.ArmorContext;

public class OmniMovementBehavior implements IArmorBehavior {

    public static final OmniMovementBehavior INSTANCE = new OmniMovementBehavior();
    private static final Set<SyncedKeybind> LISTENED_KEYS = Collections.singleton(
        ArmorActionManager.getAction("omni_movement")
            .getKeybind());

    @Override
    public void onKeyPressed(@NotNull ArmorContext context, SyncedKeybind keyPressed, boolean isDown) {
        if (!isDown) return;

        context.toggleBehavior(BehaviorName.OmniMovement);
    }

    @Override
    public BehaviorName getName() {
        return BehaviorName.OmniMovement;
    }

    @Override
    public Set<SyncedKeybind> getListenedKeys(@NotNull ArmorContext context) {
        return LISTENED_KEYS;
    }
}
