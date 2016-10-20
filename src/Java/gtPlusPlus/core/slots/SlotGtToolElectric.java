package gtPlusPlus.core.slots;

import gregtech.api.items.GT_MetaGenerated_Tool;
import gtPlusPlus.core.util.Utils;
import ic2.api.info.Info;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotGtToolElectric extends SlotGtTool {
	public int tier;
	private ItemStack content;

	public SlotGtToolElectric(IInventory base, int x, int y, int z, int tier, boolean allowRedstoneDust)
	{
		super(base, x, y, z);
		this.tier = tier;
		this.allowRedstoneDust = allowRedstoneDust;
	}

	public boolean accepts(ItemStack stack)
	{
		if (stack == null) {
			return false;
		}
		if ((stack.getItem() == Items.redstone) && (!this.allowRedstoneDust)) {
			return false;
		}
		return (Info.itemEnergy.getEnergyValue(stack) > 0.0D) || (ElectricItem.manager.discharge(stack, (1.0D / 0.0D), this.tier, true, true, true) > 0.0D);
	}

	public double discharge(double amount, boolean ignoreLimit)
	{
		if (amount <= 0.0D) {
			throw new IllegalArgumentException("Amount must be > 0.");
		}
		ItemStack stack = get(0);
		if (stack == null) {
			return 0.0D;
		}
		double realAmount = ElectricItem.manager.discharge(stack, amount, this.tier, ignoreLimit, true, false);
		if (realAmount <= 0.0D)
		{
			realAmount = Info.itemEnergy.getEnergyValue(stack);
			if (realAmount <= 0.0D) {
				return 0.0D;
			}
			stack.stackSize -= 1;
			if (stack.stackSize <= 0) {
				put(0, null);
			}
		}
		return realAmount;
	}

	public void setTier(int tier1)
	{
		this.tier = tier1;
	}

	public boolean allowRedstoneDust = true;

	public ItemStack get()
	{
		return get(0);
	}

	public ItemStack get(int index)
	{
		return this.content;
	}

	public void put(ItemStack content)
	{
		put(0, content);
	}

	public void put(int index, ItemStack content)
	{
		this.content = content;
		onChanged();
	}

	public void onChanged() {}

	@Override
	public boolean isItemValid(ItemStack itemstack) {
		if (itemstack.getItem() instanceof GT_MetaGenerated_Tool || itemstack.getItem() instanceof IElectricItem){
			Utils.LOG_WARNING(itemstack.getDisplayName()+" is a valid Tool.");
			return true;
		}
		Utils.LOG_WARNING(itemstack.getDisplayName()+" is not a valid Tool.");		
		return false;
	}

}
