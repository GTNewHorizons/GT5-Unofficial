package gtPlusPlus.core.recipe;

import gregtech.api.util.GT_Utility;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.SubTag;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.item.ItemStack;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IOreRecipeRegistrator;

public class RECIPES_Extruder implements IOreRecipeRegistrator {
	
	public RECIPES_Extruder() {
		OrePrefixes.ingot.add((IOreRecipeRegistrator) this);
		OrePrefixes.dust.add((IOreRecipeRegistrator) this);
	}

	public void registerOre(final OrePrefixes aPrefix, final Materials aMaterial, final String aOreDictName,
			final String aModName, final ItemStack aStack) {
		if ((aMaterial == Materials.Glass || aMaterial == Materials.WroughtIron
				|| GT_OreDictUnificator.get(OrePrefixes.ingot, (Object) aMaterial, 1L) != null)
				&& !aMaterial.contains(SubTag.NO_SMELTING)) {
			final long aMaterialMass = aMaterial.getMass();
			final int tAmount = (int) (aPrefix.mMaterialAmount / 3628800L);
			if (tAmount > 0 && tAmount <= 64 && aPrefix.mMaterialAmount % 3628800L == 0L) {
				int tVoltageMultiplier = (aMaterial.mBlastFurnaceTemp >= 2800) ? 64 : 16;
				if (aMaterial.contains(SubTag.NO_SMASHING)) {
					tVoltageMultiplier /= 4;
				} else if (aPrefix.name().startsWith(OrePrefixes.dust.name())) {
					return;
				}				
				
				GT_Values.RA.addExtruderRecipe(GT_Utility.copyAmount(1L, new Object[]{aStack}),
						GregtechItemList.Shape_Extruder_SmallGear.get(0L, new Object[0]),
						GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, (Object) aMaterial.mSmeltInto, (long) tAmount),
						((int) Math.max(aMaterialMass * 5L * tAmount, tAmount)/4), 8 * tVoltageMultiplier);
				GT_Values.RA.addAlloySmelterRecipe(GT_Utility.copyAmount(2L, new Object[]{aStack}),
						GregtechItemList.Shape_Extruder_SmallGear.get(0L, new Object[0]),
						GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, (Object) aMaterial.mSmeltInto, (long) tAmount),
						((int) Math.max(aMaterialMass * 10L * tAmount, tAmount)/4), 2 * tVoltageMultiplier);
				
			}
		}
	}
}