package gtPlusPlus.core.item.base.rods;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class BaseItemRodLong extends BaseItemComponent{

	public BaseItemRodLong(final Material material) {
		super(material, BaseItemComponent.ComponentTypes.RODLONG);
		this.addExtruderRecipe();
	}

	@Override
	public String getItemStackDisplayName(final ItemStack p_77653_1_) {
		return ("Long "+this.materialName+ " Rod");
	}

	private void addExtruderRecipe(){
		Logger.WARNING("Adding recipe for Long "+this.materialName+" Rods");

		final ItemStack stackStick = this.componentMaterial.getRod(2);
		final ItemStack stackLong = this.componentMaterial.getLongRod(1);

		if (ItemUtils.checkForInvalidItems(new ItemStack[] {stackStick, stackLong}))
		GT_Values.RA.addForgeHammerRecipe(
				stackStick,
				stackLong,
				(int) Math.max(this.componentMaterial.getMass(), 1L),
				16);

		if (ItemUtils.checkForInvalidItems(new ItemStack[] {stackStick, stackLong}))
		GT_Values.RA.addCutterRecipe(
				stackLong,
				stackStick,
				null,
				(int) Math.max(this.componentMaterial.getMass(), 1L),
				4);
	}

}
