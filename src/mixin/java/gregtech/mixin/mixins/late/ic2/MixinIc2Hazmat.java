package gregtech.mixin.mixins.late.ic2;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import gregtech.api.hazards.Hazard;
import gregtech.api.hazards.HazardProtection;
import gregtech.api.hazards.IHazardProtector;
import ic2.core.item.armor.ItemArmorHazmat;

@Mixin(value = ItemArmorHazmat.class, remap = false)
public class MixinIc2Hazmat implements IHazardProtector {

    /**
     * @author Sphyix
     * @reason Hazmat - IC2 logic superseded by GT check
     */
    @Overwrite
    public static boolean hasCompleteHazmat(EntityLivingBase entity) {
        return HazardProtection.isWearingFullRadioHazmat(entity);
    }

    @Override
    public boolean protectsAgainst(ItemStack itemStack, Hazard hazard) {
        return true;
    }
}
