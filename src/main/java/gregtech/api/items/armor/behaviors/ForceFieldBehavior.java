package gregtech.api.items.armor.behaviors;

import static gregtech.api.items.armor.ArmorKeybinds.FORCE_FIELD_KEYBIND;
import static gregtech.loaders.ExtraIcons.forceFieldAugment;

import java.util.Collections;
import java.util.Set;

import net.minecraft.util.IIcon;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import gregtech.api.items.armor.ArmorContext;

public class ForceFieldBehavior implements IArmorBehavior {

    public static ForceFieldBehavior INSTANCE = new ForceFieldBehavior();

    @Override
    public BehaviorName getName() {
        return BehaviorName.ForceField;
    }

    @Override
    public IIcon getModularArmorTexture() {
        return forceFieldAugment;
    }

    @Override
    public void onKeyPressed(@NotNull ArmorContext context, SyncedKeybind keyPressed, boolean isDown) {
        if (!isDown) return;

        context.getArmorState()
            .toggle(context, BehaviorName.ForceField);
    }

    @Override
    public Set<SyncedKeybind> getListenedKeys(@NotNull ArmorContext context) {
        return Collections.singleton(FORCE_FIELD_KEYBIND);
    }
}
