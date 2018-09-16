package gtPlusPlus.xmod.ob;

import java.util.HashMap;

import gtPlusPlus.api.objects.Logger;
import net.minecraft.item.ItemStack;
import openblocks.common.tileentity.TileEntitySprinkler;
import openmods.inventory.TileEntityInventory;

public class CustomSprinklerInventory extends TileEntityInventory {
	private final TileEntitySprinkler owner;

	public CustomSprinklerInventory(TileEntitySprinkler owner, String name, boolean isInvNameLocalized, int size) {
		super(owner, name, isInvNameLocalized, size);
		this.owner = owner;
		this.addCallback(new CallbackObject(this.owner));
	}

	ItemStack[] mFerts;

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		Logger.INFO("Inserting: "+itemstack != null ? itemstack.getDisplayName() : "Nothing");
		HashMap<Integer, ItemStack> mFerts = SprinklerHandler.getValidFerts();
		if (itemstack != null && mFerts != null && mFerts.size() > 0) {
			for (ItemStack u : mFerts.values()) {
				if (itemstack.isItemEqual(u)) {
					return true;
				}
			}
		}
		return false;
	}

}