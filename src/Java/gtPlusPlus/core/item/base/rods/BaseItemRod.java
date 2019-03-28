package gtPlusPlus.core.item.base.rods;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class BaseItemRod extends BaseItemComponent{

	public BaseItemRod(final Material material) {
		super(material, BaseItemComponent.ComponentTypes.ROD);
		this.addCutterRecipe(material);
		this.addLatheRecipe(material);
	}


	private void addCutterRecipe(Material material){
		Logger.WARNING("Adding cutter recipe for "+this.materialName+" Rods");
		final ItemStack stackStick = this.componentMaterial.getRod(1);
		final ItemStack stackBolt = this.componentMaterial.getBolt(4);
		
		if (ItemUtils.checkForInvalidItems(new ItemStack[] {stackStick, stackBolt}))
			GT_Values.RA.addCutterRecipe(
					stackStick,
					stackBolt,
					null,
					(int) Math.max(this.componentMaterial.getMass() * 2L, 1L),
					material.vVoltageMultiplier);
	}


	private void addLatheRecipe(Material material){
		Logger.WARNING("Adding recipe for "+this.materialName+" Rod");
		ItemStack boltStack = this.componentMaterial.getIngot(1);
		if (ItemUtils.checkForInvalidItems(boltStack)){
			GT_Values.RA.addLatheRecipe(
					boltStack,
					ItemUtils.getSimpleStack(this),
					material.getSmallDust(2),
					(int) Math.max(this.componentMaterial.getMass() / 8L, 1L),
					material.vVoltageMultiplier);
		}
	}

}
