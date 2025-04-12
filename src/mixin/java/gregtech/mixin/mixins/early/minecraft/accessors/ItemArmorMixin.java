package gregtech.mixin.mixins.early.minecraft.accessors;

import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;

import gregtech.api.hazards.Hazard;
import gregtech.api.hazards.IHazardProtector;

@Mixin(ItemArmor.class)
public class ItemArmorMixin implements IHazardProtector {

    @Override
    public boolean protectsAgainst(ItemStack itemStack, Hazard hazard) {
        return ((ItemArmor) (Object) this).getArmorMaterial() == ArmorMaterial.CHAIN && hazard == Hazard.ELECTRICAL;
    }
}
