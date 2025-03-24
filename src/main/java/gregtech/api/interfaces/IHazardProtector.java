package gregtech.api.interfaces;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.Hazards;

public interface IHazardProtector {

    boolean protectsAgainst(ItemStack itemStack, Hazards hazard);
}
