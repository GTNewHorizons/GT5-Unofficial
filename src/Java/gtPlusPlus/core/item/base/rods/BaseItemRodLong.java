package gtPlusPlus.core.item.base.rods;

import gregtech.api.enums.GT_Values;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.item.ItemStack;

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

		final String tempStick = this.unlocalName.replace("itemRodLong", "stick");
		final String tempStickLong = this.unlocalName.replace("itemRodLong", "stickLong");
		final ItemStack stackStick = ItemUtils.getItemStackOfAmountFromOreDict(tempStick, 1);
		final ItemStack stackLong = ItemUtils.getItemStackOfAmountFromOreDict(tempStickLong, 1);

		final ItemStack temp = stackStick;
		temp.stackSize = 2;

		GT_Values.RA.addForgeHammerRecipe(
				temp,
				stackLong,
				(int) Math.max(this.componentMaterial.getMass(), 1L),
				16);

		GT_Values.RA.addCutterRecipe(
				stackLong,
				temp,
				null,
				(int) Math.max(this.componentMaterial.getMass(), 1L),
				4);
	}

}
