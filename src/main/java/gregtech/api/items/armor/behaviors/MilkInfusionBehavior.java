package gregtech.api.items.armor.behaviors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import org.jetbrains.annotations.NotNull;

import gregtech.api.items.armor.ArmorContext;

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

        Collection<PotionEffect> activeEffects = player.getActivePotionEffects();
        if (activeEffects.isEmpty()) return;

        List<PotionEffect> effectsToProcess = new ArrayList<>(activeEffects);
        boolean effectRemoved = false;

        for (PotionEffect effect : effectsToProcess) {
            int effectID = effect.getPotionID();

            if (effectID >= 0 && effectID < Potion.potionTypes.length) {
                Potion potion = Potion.potionTypes[effectID];

                if (potion != null && potion.isBadEffect()) {
                    if (context.drainEnergy(5000)) {
                        player.removePotionEffect(effectID);
                        effectRemoved = true;
                    }
                }
            }
        }
        if (effectRemoved) {
            player.worldObj.playSoundAtEntity(player, "random.drink", 1.0F, 1.1F);
        }
    }
}
