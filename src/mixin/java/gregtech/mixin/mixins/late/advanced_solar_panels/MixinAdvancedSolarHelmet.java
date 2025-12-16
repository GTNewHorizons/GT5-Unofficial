package gregtech.mixin.mixins.late.advanced_solar_panels;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;

import advsolar.common.items.ItemAdvancedSolarHelmet;
import gregtech.api.hazards.Hazard;
import gregtech.api.hazards.IHazardProtector;

@Mixin(ItemAdvancedSolarHelmet.class)
public class MixinAdvancedSolarHelmet implements IHazardProtector {

    @Override
    public boolean protectsAgainst(ItemStack itemStack, Hazard hazard) {
        return true;
    }
}
