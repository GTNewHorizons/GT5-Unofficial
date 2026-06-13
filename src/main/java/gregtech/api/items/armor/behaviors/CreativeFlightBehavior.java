package gregtech.api.items.armor.behaviors;

import net.minecraft.entity.player.EntityPlayer;

import org.jetbrains.annotations.NotNull;

import gregtech.api.items.armor.ArmorContext;

public class CreativeFlightBehavior implements IArmorBehavior {

    public static final CreativeFlightBehavior INSTANCE = new CreativeFlightBehavior();

    protected CreativeFlightBehavior() {/**/}

    @Override
    public BehaviorName getName() {
        return BehaviorName.CreativeFlight;
    }

    @Override
    public void onArmorTick(@NotNull ArmorContext context) {
        EntityPlayer player = context.getPlayer();

        boolean hasEnergy = !player.capabilities.isFlying || context.drainEnergy(75);

        if (hasEnergy) {
            player.fallDistance = 0;
            // Constantly re-check because allowFlying is reset when you travel between dimensions
            if (!player.capabilities.allowFlying) {
                player.capabilities.allowFlying = true;
                player.sendPlayerAbilities();
            }
        } else if (!player.capabilities.isCreativeMode) {
            player.capabilities.isFlying = false;
            player.capabilities.allowFlying = false;
            player.sendPlayerAbilities();
        }
    }

    @Override
    public void onArmorUnequip(@NotNull ArmorContext context) {
        EntityPlayer player = context.getPlayer();

        if (!player.capabilities.isCreativeMode) {
            player.capabilities.allowFlying = false;
            player.capabilities.isFlying = false;
            player.sendPlayerAbilities();
        }
    }
}
