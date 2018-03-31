package gtPlusPlus.xmod.sc2.modules.workers.tools;

import net.minecraft.item.ItemStack;

import vswe.stevescarts.Carts.MinecartModular;

public class ModuleExoticFarmerUpgraded extends ModuleExoticFarmer {

	public ModuleExoticFarmerUpgraded(MinecartModular cart) {
		super(cart);
	}

	@Override
	protected int getRange() {
		return 5;
	}

	@Override
	public int getMaxDurability() {
		return Integer.MAX_VALUE;
	}

	@Override
	public String getRepairItemName() {
		return null;
	}

	@Override
	public int getRepairItemUnits(ItemStack p0) {
		return 0;
	}

	@Override
	public int getRepairSpeed() {
		return 0;
	}

	@Override
	public boolean useDurability() {
		return false;
	}

}
