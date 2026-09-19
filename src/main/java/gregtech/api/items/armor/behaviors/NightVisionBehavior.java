package gregtech.api.items.armor.behaviors;

import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import gregtech.api.items.armor.ArmorActionManager;
import gregtech.api.items.armor.ArmorContext;

public class NightVisionBehavior implements IArmorBehavior {

    public static final NightVisionBehavior INSTANCE = new NightVisionBehavior();

    protected NightVisionBehavior() {/**/}

    @Override
    public BehaviorName getName() {
        return BehaviorName.NightVision;
    }

    @Override
    public void onKeyPressed(@NotNull ArmorContext context, SyncedKeybind keyPressed, boolean isDown) {
        if (!isDown) return;

        context.toggleBehavior(BehaviorName.NightVision);
    }

    @Override
    public Set<SyncedKeybind> getListenedKeys(@NotNull ArmorContext context) {
        return ArmorActionManager.getKeybindsForBehavior(getName());
    }

    @Override
    public void onArmorTick(@NotNull ArmorContext context) {
        if (context.isRemote()) return;
        EntityPlayer player = context.getPlayer();

        if (context.isBehaviorActive(BehaviorName.NightVision) && context.getArmorState().charge > 1) {
            int combinedLight = context.getWorld()
                .getBlockLightValue(
                    MathHelper.floor_double(player.posX),
                    MathHelper.floor_double(player.posY),
                    MathHelper.floor_double(player.posZ));


            if (combinedLight < 7) {
                context.drainEnergy(2);
            }

            player.removePotionEffect(Potion.blindness.id);
            player.addPotionEffect(new PotionEffect(Potion.nightVision.id, 999999, 0, true));
        } else {
            removeArmorNightVision(context);
        }
    }

    @Override
    public void onArmorUnequip(@NotNull ArmorContext context) {
        removeArmorNightVision(context);
    }

    // Only removes NV if it was applied by the armor (ambient=true); leaves potion NV untouched.
    private static void removeArmorNightVision(ArmorContext context) {
        PotionEffect effect = context.getPlayer()
            .getActivePotionEffect(Potion.nightVision);
        if (effect != null && effect.getIsAmbient()) {
            context.getPlayer()
                .removePotionEffect(Potion.nightVision.id);
        }
    }
}
