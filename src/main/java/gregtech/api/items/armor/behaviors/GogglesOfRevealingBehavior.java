package gregtech.api.items.armor.behaviors;

import static gregtech.api.items.armor.ArmorKeybinds.GOGGLES_OF_REVEALING_KEYBIND;
import static gregtech.loaders.ExtraIcons.revealingAugment;

import java.util.Collections;
import java.util.Set;

import net.minecraft.util.IIcon;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import gregtech.api.items.armor.ArmorContext;

public class GogglesOfRevealingBehavior implements IArmorBehavior {

    public static final GogglesOfRevealingBehavior INSTANCE = new GogglesOfRevealingBehavior();

    protected GogglesOfRevealingBehavior() {/**/}

    @Override
    public BehaviorName getName() {
        return BehaviorName.GogglesOfRevealing;
    }

    @Override
    public IIcon getModularArmorTexture() {
        return revealingAugment;
    }

    @Override
    public void onKeyPressed(@NotNull ArmorContext context, SyncedKeybind keyPressed, boolean isDown) {
        if (!isDown) return;

        context.toggleBehavior(BehaviorName.GogglesOfRevealing);
    }

    @Override
    public Set<SyncedKeybind> getListenedKeys(@NotNull ArmorContext context) {
        return Collections.singleton(GOGGLES_OF_REVEALING_KEYBIND);
    }
}
