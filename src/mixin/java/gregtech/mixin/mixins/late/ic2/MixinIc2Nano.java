package gregtech.mixin.mixins.late.ic2;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;

import gregtech.api.hazards.Hazard;
import gregtech.api.hazards.IHazardProtector;
import ic2.core.item.armor.ItemArmorNanoSuit;

@Mixin(value = ItemArmorNanoSuit.class, remap = false)
public class MixinIc2Nano implements IHazardProtector {

    @Override
    public boolean protectsAgainst(ItemStack itemStack, Hazard hazard) {
        return true;
    }
}
