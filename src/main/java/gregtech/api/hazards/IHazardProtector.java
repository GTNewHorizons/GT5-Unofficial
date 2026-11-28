package gregtech.api.hazards;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.Nullable;

public interface IHazardProtector {

    boolean protectsAgainst(ItemStack itemStack, Hazard hazard);

    default boolean protectsAgainst(@Nullable EntityLivingBase entity, ItemStack itemStack, Hazard hazard) {
        return protectsAgainst(itemStack, hazard);
    }

    /// Checks if this armor provides protection regardless of whether the other pieces also provide protection. I.E.
    /// your leggings provide full protection against heat, but no other armor pieces do, which protects the player from
    /// heat.
    default boolean protectsAgainstFully(@Nullable EntityLivingBase entity, ItemStack itemStack, Hazard hazard) {
        return false;
    }
}
