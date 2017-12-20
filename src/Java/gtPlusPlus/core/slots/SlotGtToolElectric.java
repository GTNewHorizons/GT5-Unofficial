package gtPlusPlus.core.slots;

import gregtech.api.items.GT_MetaGenerated_Tool;
import gtPlusPlus.api.objects.Logger;
import ic2.api.info.Info;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotGtToolElectric extends SlotGtTool {
	public int tier;
	private ItemStack content;

	public SlotGtToolElectric(final IInventory base, final int x, final int y, final int z, final int tier,
			final boolean allowRedstoneDust) {
		super(base, x, y, z);
		this.tier = tier;
		this.allowRedstoneDust = allowRedstoneDust;
	}

	public boolean accepts(final ItemStack stack) {
		if (stack == null) {
			return false;
		}
		if ((stack.getItem() == Items.redstone) && (!this.allowRedstoneDust)) {
			return false;
		}
		return (Info.itemEnergy.getEnergyValue(stack) > 0.0D)
				|| (ElectricItem.manager.discharge(stack, (1.0D / 0.0D), this.tier, true, true, true) > 0.0D);
	}

	public double discharge(final double amount, final boolean ignoreLimit) {
		if (amount <= 0.0D) {
			throw new IllegalArgumentException("Amount must be > 0.");
		}
		final ItemStack stack = this.get(0);
		if (stack == null) {
			return 0.0D;
		}
		double realAmount = ElectricItem.manager.discharge(stack, amount, this.tier, ignoreLimit, true, false);
		if (realAmount <= 0.0D) {
			realAmount = Info.itemEnergy.getEnergyValue(stack);
			if (realAmount <= 0.0D) {
				return 0.0D;
			}
			stack.stackSize -= 1;
			if (stack.stackSize <= 0) {
				this.put(0, null);
			}
		}
		return realAmount;
	}

	public void setTier(final int tier1) {
		this.tier = tier1;
	}

	public boolean allowRedstoneDust = true;

	public ItemStack get() {
		return this.get(0);
	}

	public ItemStack get(final int index) {
		return this.content;
	}

	public void put(final ItemStack content) {
		this.put(0, content);
	}

	public void put(final int index, final ItemStack content) {
		this.content = content;
		this.onChanged();
	}

	public void onChanged() {
	}

	@Override
	public boolean isItemValid(final ItemStack itemstack) {
		if ((itemstack.getItem() instanceof GT_MetaGenerated_Tool) || (itemstack.getItem() instanceof IElectricItem)) {
			Logger.WARNING(itemstack.getDisplayName() + " is a valid Tool.");
			return true;
		}
		Logger.WARNING(itemstack.getDisplayName() + " is not a valid Tool.");
		return false;
	}

}
