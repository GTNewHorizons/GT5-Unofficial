package gregtech.api.items.armor.behaviors;

import java.util.Set;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import gregtech.api.items.armor.ArmorActionManager;
import gregtech.api.items.armor.ArmorContext;

public class HasteBehavior implements IArmorBehavior {

    public static final HasteBehavior INSTANCE = new HasteBehavior();

    protected HasteBehavior() {}

    @Override
    public BehaviorName getName() {
        return BehaviorName.Haste;
    }

    @Override
    public Set<SyncedKeybind> getListenedKeys(@NotNull ArmorContext context) {
        return ArmorActionManager.getKeybindsForBehavior(getName());
    }

    @Override
    public void onKeyPressed(@NotNull ArmorContext context, SyncedKeybind keyPressed, boolean isDown) {
        if (!isDown) return;
        context.toggleBehavior(BehaviorName.Haste);
    }

    @Override
    public void onArmorTick(@NotNull ArmorContext context) {
        if (context.isRemote()) return;

        if (context.isBehaviorActive(BehaviorName.Haste) && context.drainEnergy(4)) {
            context.getPlayer()
                .addPotionEffect(new PotionEffect(Potion.digSpeed.id, 40, 1, true));
        } else {
            removeArmorHaste(context);
        }
    }

    @Override
    public void onArmorUnequip(@NotNull ArmorContext context) {
        removeArmorHaste(context);
    }

    private static void removeArmorHaste(ArmorContext context) {
        PotionEffect effect = context.getPlayer()
            .getActivePotionEffect(Potion.digSpeed);
        if (effect != null && effect.getIsAmbient()) {
            context.getPlayer()
                .removePotionEffect(Potion.digSpeed.id);
        }
    }
}
