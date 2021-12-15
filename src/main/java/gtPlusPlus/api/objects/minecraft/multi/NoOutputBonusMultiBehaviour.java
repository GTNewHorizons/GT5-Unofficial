package gtPlusPlus.api.objects.minecraft.multi;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import net.minecraft.item.ItemStack;

public class NoOutputBonusMultiBehaviour extends SpecialMultiBehaviour {

	public NoOutputBonusMultiBehaviour() {
		// Used by other mods which may wish to not obtain bonus outputs on their Sifting or Maceration recipes.
	}
	
	@Override
	public ItemStack getTriggerItem() {
		return GregtechItemList.Chip_MultiNerf_NoOutputBonus.get(1);
	}

	@Override
	public String getTriggerItemTooltip() {
		return "Prevents bonus output % on GT++ multiblocks when used";
	}

	@Override
	public int getOutputChanceRoll() {
		return 10000;
	}

}
