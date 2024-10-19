package gregtech.mixin.mixins.late.ic2;

import net.minecraft.entity.EntityLivingBase;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import gregtech.api.util.GTUtility;
import ic2.core.item.armor.ItemArmorHazmat;

@Mixin(value = ItemArmorHazmat.class, remap = false)
public class MixinIc2Hazmat {

    /**
     * @author Sphyix
     * @reason Hazmat - IC2 logic superseded by GT check
     */
    @Overwrite
    public static boolean hasCompleteHazmat(EntityLivingBase entity) {
        return GTUtility.isWearingFullRadioHazmat(entity);
    }
}
