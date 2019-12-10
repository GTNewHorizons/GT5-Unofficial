package gtPlusPlus.api.objects.minecraft.multi;

import gtPlusPlus.core.recipe.common.CI;
import net.minecraft.item.ItemStack;

public class NoOutputBonusMultiBehaviour extends SpecialMultiBehaviour {

	public NoOutputBonusMultiBehaviour() {
		
	}
	
	@Override
	public ItemStack getTriggerItem() {
		return CI.getNumberedBioCircuit(22);
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
