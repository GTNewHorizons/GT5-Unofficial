package gregtech.api.items.armor.behaviors;

import static gregtech.loaders.ExtraIcons.creativeFlightAugment;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;

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
    public IIcon getModularArmorTexture() {
        return creativeFlightAugment;
    }

    @Override
    public void onArmorTick(@NotNull ArmorContext context) {
        if (!context.isRemote()) return;

        EntityPlayer player = context.getPlayer();

        // Constantly re-check because alloyFlying is reset when you travel between dimensions
        if (!player.capabilities.allowFlying) {
            player.capabilities.allowFlying = true;
            player.sendPlayerAbilities();
        }

        if (player.capabilities.isFlying) {
            if (!context.drainEnergy(75)) {
                player.capabilities.isFlying = false;
                player.sendPlayerAbilities();
            }
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
