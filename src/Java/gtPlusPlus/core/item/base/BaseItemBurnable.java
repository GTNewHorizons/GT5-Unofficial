package gtPlusPlus.core.item.base;

import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class BaseItemBurnable extends CoreItem {
	
	protected final int meta;
	
	public BaseItemBurnable(String unlocalizedName, String displayName, CreativeTabs creativeTab,
			int stackSize, int maxDmg, String description, String oredictName, int burnTime, int meta) {
		super(unlocalizedName, creativeTab, stackSize, maxDmg, description);
		this.itemName = displayName;
		this.meta = meta;
		if (oredictName != null && !oredictName.equals("")){
			registerOrdictionary(oredictName);
		}		
		registerFuel(burnTime);
		
	}
	
	public void registerFuel(int burn){
		CORE.burnables.add(new Pair<Integer, ItemStack>(burn, ItemUtils.getSimpleStack(this, 1)));
	}
	
	public final void registerOrdictionary(String name){
		ItemUtils.addItemToOreDictionary(ItemUtils.getSimpleStack(this), name);		
	}

	@Override
	public int getDamage(ItemStack stack) {
		return this.meta;
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return false;
	}

}
