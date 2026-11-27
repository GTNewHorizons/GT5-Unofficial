package gregtech.api.hazards;

import net.minecraft.item.ItemStack;

/// An item that is radioactive in its own right rather than through its material; see [ItemHazards#radiationLevel].
public interface IRadioactiveItem {

    int getRadiationLevel(ItemStack stack);
}
