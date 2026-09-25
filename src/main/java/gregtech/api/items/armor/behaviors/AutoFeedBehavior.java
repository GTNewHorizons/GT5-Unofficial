package gregtech.api.items.armor.behaviors;

import java.util.Set;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import gregtech.api.items.armor.ArmorActionManager;
import gregtech.api.items.armor.ArmorContext;
import gregtech.api.items.armor.ui.autofeed.AutoFeedUI;

public class AutoFeedBehavior implements IArmorBehavior {

    public static final AutoFeedBehavior INSTANCE = new AutoFeedBehavior();

    protected AutoFeedBehavior() {}

    @Override
    public BehaviorName getName() {
        return BehaviorName.AutoFeed;
    }

    @Override
    public Set<SyncedKeybind> getListenedKeys(@NotNull ArmorContext context) {
        return ArmorActionManager.getKeybindsForBehavior(getName());
    }

    @Override
    public void onKeyPressed(@NotNull ArmorContext context, SyncedKeybind keyPressed, boolean isDown) {
        if (!isDown) return;

        AutoFeedUI.INSTANCE.open(context.getPlayer());
    }

}
