package gtPlusPlus.xmod.forestry.bees.alveary.gui;

import net.minecraft.item.ItemStack;
import forestry.api.apiculture.BeeManager;
import forestry.core.inventory.InventoryAdapterTile;
import forestry.core.utils.ItemStackUtil;
import gtPlusPlus.xmod.forestry.bees.alveary.TileAlvearyFrameHousing;

public class InventoryFrameHousing extends InventoryAdapterTile<TileAlvearyFrameHousing>
{
	public InventoryFrameHousing(TileAlvearyFrameHousing alvearyFrame)
	{
		super(alvearyFrame, 1, "FrameHousingInv");
	}

	@Override
	public boolean canSlotAccept(int slotIndex, ItemStack itemStack)
	{
		return ItemStackUtil.containsItemStack(BeeManager.inducers.keySet(), itemStack);
	}
}
