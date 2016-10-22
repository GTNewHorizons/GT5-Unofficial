package gtPlusPlus.core.item.base.rotors;

import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.UtilsItems;
import gtPlusPlus.core.util.recipe.UtilsRecipe;
import net.minecraft.item.ItemStack;

public class BaseItemRotor extends BaseItemComponent{

	public BaseItemRotor(Material material) {
		super(material, BaseItemComponent.ComponentTypes.ROTOR);
		generateRecipe();
	}

	public void generateRecipe(){
		Utils.LOG_WARNING("Adding recipe for "+materialName+" Rotors");
		ItemStack tempOutputStack = this.componentMaterial.getPlate(1);
		ItemStack screwStack = this.componentMaterial.getScrew(1);
		ItemStack ringStack = this.componentMaterial.getRing(1);	
		UtilsRecipe.addShapedGregtechRecipe(
				tempOutputStack, "craftingToolHardHammer", tempOutputStack,
				screwStack, ringStack, "craftingToolFile",
				tempOutputStack, "craftingToolScrewdriver", tempOutputStack,
				UtilsItems.getSimpleStack(this));
	}

}
