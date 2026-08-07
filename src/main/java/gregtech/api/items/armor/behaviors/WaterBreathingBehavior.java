package gregtech.api.items.armor.behaviors;

import net.minecraft.entity.player.EntityPlayer;

import org.jetbrains.annotations.NotNull;

import gregtech.api.items.armor.ArmorContext;

public class WaterBreathingBehavior implements IArmorBehavior {

    public static final WaterBreathingBehavior INSTANCE = new WaterBreathingBehavior();

    protected WaterBreathingBehavior() {}

    @Override
    public BehaviorName getName() {
        return BehaviorName.WaterBreathing;
    }

    @Override
    public void onArmorTick(@NotNull ArmorContext context) {
        if (context.isRemote()) return;

        EntityPlayer player = context.getPlayer();

        if (player.isInWater()) {
            if (player.getAir() <= 1 && context.drainEnergy(5000)) {
                player.setAir(300);
            }
        }
    }
}
