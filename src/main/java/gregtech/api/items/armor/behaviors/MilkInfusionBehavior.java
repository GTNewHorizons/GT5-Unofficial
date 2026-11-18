package gregtech.api.items.armor.behaviors;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import org.jetbrains.annotations.NotNull;

import gregtech.api.items.armor.ArmorContext;
import gregtech.mixin.interfaces.accessors.PotionAccessor;

public class MilkInfusionBehavior implements IArmorBehavior {

    public static final MilkInfusionBehavior INSTANCE = new MilkInfusionBehavior();

    @Override
    public BehaviorName getName() {
        return BehaviorName.MilkInfusion;
    }

    @Override
    public void onArmorTick(@NotNull ArmorContext context) {
        if (context.isRemote()) return;
        EntityPlayer player = context.getPlayer();

        if (player.ticksExisted % 10 != 0) {
            return;
        }

        final List<Integer> potionToRemove = new ArrayList<>();
        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            final Potion potion = Potion.potionTypes[potionEffect.getPotionID()];
            if (potion instanceof PotionAccessor && ((PotionAccessor) potion).gt5u$isBadEffect()) {
                potionToRemove.add(potion.id);
            }
        }

        for (Integer i : potionToRemove) {
            player.removePotionEffect(i);
        }

        if (!potionToRemove.isEmpty()) {
            player.worldObj.playSoundAtEntity(player, "random.drink", 1.0F, 1.1F);
        }
    }
}
