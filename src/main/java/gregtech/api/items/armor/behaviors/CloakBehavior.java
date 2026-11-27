package gregtech.api.items.armor.behaviors;

import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import gregtech.api.items.armor.ArmorActionManager;
import gregtech.api.items.armor.ArmorContext;

public class CloakBehavior implements IArmorBehavior {

    public static final CloakBehavior INSTANCE = new CloakBehavior();

    private static final double EU_PER_TICK = 8;
    private static final int INVISIBILITY_DURATION_TICKS = 40;

    protected CloakBehavior() {}

    @Override
    public BehaviorName getName() {
        return BehaviorName.Cloak;
    }

    @Override
    public void onKeyPressed(@NotNull ArmorContext context, SyncedKeybind keyPressed, boolean isDown) {
        if (!isDown) return;

        context.toggleBehavior(BehaviorName.Cloak);
    }

    @Override
    public Set<SyncedKeybind> getListenedKeys(@NotNull ArmorContext context) {
        return ArmorActionManager.getKeybindsForBehavior(getName());
    }

    @Override
    public void onArmorTick(@NotNull ArmorContext context) {
        if (context.isRemote()) return;

        EntityPlayer player = context.getPlayer();
        boolean cloaked = context.isBehaviorActive(BehaviorName.Cloak) && context.drainEnergy(EU_PER_TICK);

        if (cloaked) {
            applyInvisibility(player);
        } else {
            removeCloakInvisibility(player);
        }
    }

    @Override
    public void onArmorUnequip(@NotNull ArmorContext context) {
        removeCloakInvisibility(context.getPlayer());
    }

    private static void applyInvisibility(EntityPlayer player) {
        player.addPotionEffect(new PotionEffect(Potion.invisibility.id, INVISIBILITY_DURATION_TICKS, 0, true));
    }

    private static void removeCloakInvisibility(EntityPlayer player) {
        PotionEffect effect = player.getActivePotionEffect(Potion.invisibility);
        if (isCloakInvisibility(effect)) {
            player.removePotionEffect(Potion.invisibility.id);
        }
    }

    private static boolean isCloakInvisibility(PotionEffect effect) {
        return effect != null && effect.getIsAmbient() && effect.getDuration() <= INVISIBILITY_DURATION_TICKS;
    }
}
