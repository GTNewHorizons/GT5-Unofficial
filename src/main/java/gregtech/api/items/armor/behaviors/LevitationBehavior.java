package gregtech.api.items.armor.behaviors;

import java.util.Set;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import gregtech.api.items.armor.ArmorActionManager;
import gregtech.api.items.armor.ArmorContext;

public class LevitationBehavior implements IArmorBehavior {

    public static final LevitationBehavior INSTANCE = new LevitationBehavior();

    protected LevitationBehavior() {}

    @Override
    public BehaviorName getName() {
        return BehaviorName.Levitation;
    }

    @Override
    public void onKeyPressed(@NotNull ArmorContext context, SyncedKeybind keyPressed, boolean isDown) {
        if (!isDown) return;

        context.toggleBehavior(BehaviorName.Levitation);
    }

    @Override
    public Set<SyncedKeybind> getListenedKeys(@NotNull ArmorContext context) {
        return ArmorActionManager.getKeybindsForBehavior(getName());
    }
}
