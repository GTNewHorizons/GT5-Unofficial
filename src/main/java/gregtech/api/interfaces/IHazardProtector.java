package gregtech.api.interfaces;

import gregtech.api.enums.Hazards;
import net.minecraft.item.ItemStack;

public interface IHazardProtector {
    boolean protectsAgainst(ItemStack itemStack, Hazards hazard);
}
