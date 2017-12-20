package gtPlusPlus.core.item.base.rods;

import gregtech.api.enums.GT_Values;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.material.Material;
import net.minecraft.item.ItemStack;

public class BaseItemRod extends BaseItemComponent{

	public BaseItemRod(final Material material) {
		super(material, BaseItemComponent.ComponentTypes.ROD);
		this.addExtruderRecipe();
	}


	private void addExtruderRecipe(){
		Logger.WARNING("Adding cutter recipe for "+this.materialName+" Rods");

		final ItemStack stackStick = this.componentMaterial.getRod(1);
		final ItemStack stackBolt = this.componentMaterial.getBolt(4);

		GT_Values.RA.addCutterRecipe(
				stackStick,
				stackBolt,
				null,
				(int) Math.max(this.componentMaterial.getMass() * 2L, 1L),
				4);
	}

}
