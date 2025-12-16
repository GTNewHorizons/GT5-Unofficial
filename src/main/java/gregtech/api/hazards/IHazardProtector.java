package gregtech.api.hazards;

import net.minecraft.item.ItemStack;

public interface IHazardProtector {

    boolean protectsAgainst(ItemStack itemStack, Hazard hazard);
}
