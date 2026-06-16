package gregtech.api.items.armor.behaviors;

import static gregtech.loaders.ExtraIcons.forceFieldAugment;

import java.util.Collections;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import gregtech.api.items.armor.ArmorActionManager;
import gregtech.api.items.armor.ArmorContext;

public class ForceFieldBehavior implements IArmorBehavior {

    public static final ForceFieldBehavior INSTANCE = new ForceFieldBehavior();

    @Override
    public BehaviorName getName() {
        return BehaviorName.ForceField;
    }

    @Override
    public void onKeyPressed(@NotNull ArmorContext context, SyncedKeybind keyPressed, boolean isDown) {
        if (!isDown) return;

        context.toggleBehavior(BehaviorName.ForceField);
    }

    @Override
    public Set<SyncedKeybind> getListenedKeys(@NotNull ArmorContext context) {
        return Collections.singleton(
            ArmorActionManager.getAction("force_field")
                .getKeybind());
    }
}
