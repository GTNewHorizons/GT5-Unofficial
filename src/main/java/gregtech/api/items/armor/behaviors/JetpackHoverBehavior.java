package gregtech.api.items.armor.behaviors;

import java.util.Collections;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import gregtech.api.items.armor.ArmorContext;
import gregtech.api.items.armor.ArmorKeybinds;

public class JetpackHoverBehavior implements IArmorBehavior {

    public static final JetpackHoverBehavior INSTANCE = new JetpackHoverBehavior();

    protected JetpackHoverBehavior() {

    }

    @Override
    public BehaviorName getName() {
        return BehaviorName.JetpackHover;
    }

    @Override
    public void onKeyPressed(@NotNull ArmorContext context, SyncedKeybind keyPressed, boolean isDown) {
        if (!isDown) return;

        context.toggleBehavior(BehaviorName.JetpackHover);
    }

    @Override
    public Set<SyncedKeybind> getListenedKeys(@NotNull ArmorContext context) {
        return Collections.singleton(ArmorKeybinds.JETPACK_HOVER_KEYBIND);
    }
}
