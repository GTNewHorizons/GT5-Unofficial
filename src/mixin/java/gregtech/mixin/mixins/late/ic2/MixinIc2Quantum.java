package gregtech.mixin.mixins.late.ic2;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;

import gregtech.api.hazards.Hazard;
import gregtech.api.hazards.IHazardProtector;
import ic2.core.item.armor.ItemArmorQuantumSuit;

@Mixin(value = ItemArmorQuantumSuit.class, remap = false)
public class MixinIc2Quantum implements IHazardProtector {

    @Override
    public boolean protectsAgainst(ItemStack itemStack, Hazard hazard) {
        return true;
    }
}
