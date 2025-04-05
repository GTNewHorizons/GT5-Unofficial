package gregtech.mixin.mixins.late.avaritia;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;

import fox.spiteful.avaritia.items.ItemArmorInfinity;
import gregtech.api.hazards.Hazard;
import gregtech.api.hazards.IHazardProtector;

@Mixin(ItemArmorInfinity.class)
public class MixinInfinityArmor implements IHazardProtector {

    @Override
    public boolean protectsAgainst(ItemStack itemStack, Hazard hazard) {
        return true;
    }
}
