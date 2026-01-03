package gregtech.api.items.armor.behaviors;

import static gregtech.api.items.armor.ArmorKeybinds.OMNI_MOVEMENT_KEYBIND;

import java.util.Collections;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import gregtech.api.items.armor.ArmorContext;

public class OmniMovementBehavior implements IArmorBehavior {

    public static OmniMovementBehavior INSTANCE = new OmniMovementBehavior();

    @Override
    public void onKeyPressed(@NotNull ArmorContext context, SyncedKeybind keyPressed, boolean isDown) {
        if (!isDown) return;

        context.getArmorState()
            .toggle(context, BehaviorName.OmniMovement);
    }

    @Override
    public BehaviorName getName() {
        return BehaviorName.OmniMovement;
    }

    @Override
    public Set<SyncedKeybind> getListenedKeys(@NotNull ArmorContext context) {
        return Collections.singleton(OMNI_MOVEMENT_KEYBIND);
    }
}
