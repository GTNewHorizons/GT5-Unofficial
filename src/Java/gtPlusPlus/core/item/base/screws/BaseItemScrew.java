package gtPlusPlus.core.item.base.screws;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class BaseItemScrew extends BaseItemComponent{

	public BaseItemScrew(final Material material) {
		super(material, BaseItemComponent.ComponentTypes.SCREW);
		this.addLatheRecipe();
	}

	private void addLatheRecipe(){
		Logger.WARNING("Adding recipe for "+this.materialName+" Screws");
		ItemStack boltStack = this.componentMaterial.getBolt(1);
		if (ItemUtils.checkForInvalidItems(boltStack)){
			GT_Values.RA.addLatheRecipe(
					boltStack,
					ItemUtils.getSimpleStack(this),
					null,
					(int) Math.max(this.componentMaterial.getMass() / 8L, 1L),
					4);
		}
	}

}
