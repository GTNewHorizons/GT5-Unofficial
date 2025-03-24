package gregtech.api.interfaces;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.Hazard;

public interface IHazardProtector {

    boolean protectsAgainst(ItemStack itemStack, Hazard hazard);
}
