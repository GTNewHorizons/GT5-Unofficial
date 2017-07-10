package gtPlusPlus.core.item.base;

import cpw.mods.fml.common.IFuelHandler;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.item.ItemUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class BaseItemBurnable extends CoreItem implements IFuelHandler{

	final int burnTime;
	
	public BaseItemBurnable(String unlocalizedName, String displayName, CreativeTabs creativeTab,
			int stackSize, int maxDmg, String description, String oredictName, int burnTime) {
		super(unlocalizedName, creativeTab, stackSize, maxDmg, description);
		this.burnTime = burnTime;
		this.itemName = displayName;
		
		if (!oredictName.equals("") || oredictName != null){
			registerOrdictionary(oredictName);
		}		
		registerFuel();
	}

	@Override
	public int getBurnTime(ItemStack fuel) {
		return this.burnTime;
	}
	
	public void registerFuel(){
		CORE.burnables.add(this);
	}
	
	public void registerOrdictionary(String name){
		ItemUtils.addItemToOreDictionary(ItemUtils.getSimpleStack(this), name);		
	}

}
