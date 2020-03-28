package gtPlusPlus.api.objects.minecraft.multi;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import net.minecraft.item.ItemStack;

public class NoEUBonusMultiBehaviour extends SpecialMultiBehaviour {

	public NoEUBonusMultiBehaviour() {
		// Used by other mods which may wish to not obtain bonus outputs on their Sifting or Maceration recipes.
	}
	
	@Override
	public ItemStack getTriggerItem() {
		return GregtechItemList.Chip_MultiNerf_NoEuBonus.get(1);
	}

	@Override
	public String getTriggerItemTooltip() {
		return "Prevents EU discounts on GT++ multiblocks when used";
	}

	@Override
	public int getEUPercent() {
		return 0;
	}

}
