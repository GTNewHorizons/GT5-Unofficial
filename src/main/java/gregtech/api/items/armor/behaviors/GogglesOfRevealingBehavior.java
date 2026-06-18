package gregtech.api.items.armor.behaviors;

import java.util.Collections;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import gregtech.api.items.armor.ArmorActionManager;
import gregtech.api.items.armor.ArmorContext;

public class GogglesOfRevealingBehavior implements IArmorBehavior {

    public static final GogglesOfRevealingBehavior INSTANCE = new GogglesOfRevealingBehavior();

    private static final Set<SyncedKeybind> LISTENED_KEYS = Collections.singleton(
        ArmorActionManager.getAction("goggles_of_revealing")
            .getKeybind());

    protected GogglesOfRevealingBehavior() {/**/}

    @Override
    public BehaviorName getName() {
        return BehaviorName.GogglesOfRevealing;
    }

    @Override
    public void onKeyPressed(@NotNull ArmorContext context, SyncedKeybind keyPressed, boolean isDown) {
        if (!isDown) return;

        context.toggleBehavior(BehaviorName.GogglesOfRevealing);
    }

    @Override
    public Set<SyncedKeybind> getListenedKeys(@NotNull ArmorContext context) {
        return LISTENED_KEYS;
    }
}
