package gregtech.api.items.armor.behaviors;

import net.minecraft.entity.player.EntityPlayer;

import org.jetbrains.annotations.NotNull;

import gregtech.api.hazards.Hazard;
import gregtech.api.items.armor.ArmorContext;

public class FireImmunityBehavior implements IArmorBehavior {

    public static FireImmunityBehavior INSTANCE = new FireImmunityBehavior();

    @Override
    public BehaviorName getName() {
        return BehaviorName.FireImmunity;
    }

    @Override
    public boolean protectsAgainstFully(@NotNull ArmorContext context, Hazard hazard) {
        // Protect against extreme temperatures.
        // Protects fully - this behavior only needs to be on the leggings to work.
        return switch (hazard) {
            case BIOLOGICAL -> false;
            case FROST -> true;
            case HEAT -> true;
            case RADIOLOGICAL -> false;
            case ELECTRICAL -> false;
            case GAS -> false;
            case SPACE -> false;
        };
    }

    @Override
    public void onArmorTick(@NotNull ArmorContext context) {
        if (context.isRemote()) return;

        EntityPlayer player = context.getPlayer();

        if (context.drainEnergy(2)) {
            player.isImmuneToFire = true;

            if (player.isBurning()) {
                player.extinguish();
            }
        } else {
            player.isImmuneToFire = false;
        }
    }

    @Override
    public void onArmorUnequip(@NotNull ArmorContext context) {
        context.getPlayer().isImmuneToFire = false;
    }
}
