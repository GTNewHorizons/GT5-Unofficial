package gtPlusPlus.xmod.gregtech.loaders;

import gregtech.api.enums.*;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.math.MathUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class RecipeGen_BlastSmelterGT_Ex implements IOreRecipeRegistrator {

	private final OrePrefixes[] mSmeltingPrefixes = {
			OrePrefixes.crushed,
			OrePrefixes.ingot,
			OrePrefixes.crushedPurified,
			OrePrefixes.crushedCentrifuged,
			OrePrefixes.dust,
			OrePrefixes.dustPure,
			OrePrefixes.dustImpure,
			OrePrefixes.dustRefined,
			OrePrefixes.dustSmall,
			OrePrefixes.dustTiny
	};

	public RecipeGen_BlastSmelterGT_Ex() {
		for (OrePrefixes tPrefix : this.mSmeltingPrefixes) tPrefix.add(this);
	}

	public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName, ItemStack aStack) {
		switch (aPrefix) {
		case dust:			
			ItemStack tDustStack;
			if ((null != (tDustStack = GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial.mSmeltInto, 1L))) && (!aMaterial.contains(SubTag.NO_SMELTING))) {
				if (aMaterial.mBlastFurnaceRequired) {
					addBlastRecipe(GT_Utility.copyAmount(1L, new Object[]{aStack}), null, null, null, aMaterial.mBlastFurnaceTemp > 1750 ? GT_OreDictUnificator.get(OrePrefixes.ingotHot, aMaterial.mSmeltInto, tDustStack, 1L) : GT_Utility.copyAmount(1L, new Object[]{tDustStack}), null, (int) Math.max(aMaterial.getMass() / 40L, 1L) * aMaterial.mBlastFurnaceTemp, 120, aMaterial);
					if (aMaterial.mBlastFurnaceTemp <= 1000) {
						GT_ModHandler.addRCBlastFurnaceRecipe(GT_Utility.copyAmount(1L, new Object[]{aStack}), GT_Utility.copyAmount(1L, new Object[]{tDustStack}), aMaterial.mBlastFurnaceTemp);
					}
				}
			}
		case ingot:
			if ((null != (tDustStack = GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial.mSmeltInto, 1L))) && (!aMaterial.contains(SubTag.NO_SMELTING))) {
				if (aMaterial.mBlastFurnaceRequired) {
					addBlastRecipe(GT_Utility.copyAmount(1L, new Object[]{aStack}), null, null, null, aMaterial.mBlastFurnaceTemp > 1750 ? GT_OreDictUnificator.get(OrePrefixes.ingotHot, aMaterial.mSmeltInto, tDustStack, 1L) : GT_Utility.copyAmount(1L, new Object[]{tDustStack}), null, (int) Math.max(aMaterial.getMass() / 40L, 1L) * aMaterial.mBlastFurnaceTemp, 120, aMaterial);
				}
			}
			break;
		case dustSmall:           
			if (aMaterial.mBlastFurnaceRequired) {
				addBlastRecipe(GT_Utility.copyAmount(4L, new Object[]{aStack}), null, null, null, aMaterial.mBlastFurnaceTemp > 1750 ? GT_OreDictUnificator.get(OrePrefixes.ingotHot, aMaterial.mSmeltInto, GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial.mSmeltInto, 1L), 1L) : GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial.mSmeltInto, 1L), null, (int) Math.max(aMaterial.getMass() / 40L, 1L) * aMaterial.mBlastFurnaceTemp, 120, aMaterial);
			}
			break;
		case dustTiny:               
			if (!aMaterial.contains(gregtech.api.enums.SubTag.NO_SMELTING)) {
				if (aMaterial.mBlastFurnaceRequired) {
					addBlastRecipe(GT_Utility.copyAmount(9L, new Object[]{aStack}), null, null, null, aMaterial.mBlastFurnaceTemp > 1750 ? GT_OreDictUnificator.get(OrePrefixes.ingotHot, aMaterial.mSmeltInto, GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial.mSmeltInto, 1L), 1L) : GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial.mSmeltInto, 1L), null, (int) Math.max(aMaterial.getMass() / 40L, 1L) * aMaterial.mBlastFurnaceTemp, 120, aMaterial);

				}
			}
			break;
		default:
			if (!aMaterial.contains(SubTag.NO_SMELTING)) {
				if ((aMaterial.mBlastFurnaceRequired) || (aMaterial.mDirectSmelting.mBlastFurnaceRequired)) {
					addBlastRecipe(GT_Utility.copyAmount(1L, new Object[]{aStack}), null, null, null, aMaterial.mBlastFurnaceTemp > 1750 ? GT_OreDictUnificator.get(OrePrefixes.ingotHot, aMaterial, GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L), 1L) : GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L), null, (int) Math.max(aMaterial.getMass() / 4L, 1L) * aMaterial.mBlastFurnaceTemp, 120, aMaterial);
					if (aMaterial.mBlastFurnaceTemp <= 1000)
						GT_ModHandler.addRCBlastFurnaceRecipe(GT_Utility.copyAmount(1L, new Object[]{aStack}), GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L), aMaterial.mBlastFurnaceTemp * 2);
				}
			}
			break;
		}
	}

	public boolean addBlastRecipe(ItemStack input1, ItemStack input2,
			FluidStack fluid1, FluidStack fluid2, ItemStack output1,
			ItemStack output2, int time, int euCost, Materials smeltInto) {

		//Set up variables.
		ItemStack[] components;
		int count = 0;

		if (smeltInto == Materials._NULL){
			//If the material is null then we probably don't want to try.
			return false;
		}
		if (input1 != null && input2 != null){
			count = 2;
		}
		else if (input1 == null && input2 == null){
			//If both inputs are null, then we don't want to try.
			return false;
		}
		else if (input1 == null || input2 == null){
			count = 1;
		}
		//Set up input components.
		ItemStack configCircuit = ItemUtils.getGregtechCircuit(count);
		components = new ItemStack[]{configCircuit, input1, input2};
		if (fluid1 != null || fluid2 != null){
			//If it uses an input fluid, we cannot handle this. So let's not try. (Annealed copper for example)
			//return false;
			if (fluid1 != null && fluid2 != null){
				//Cannot handle two input fluids
				return false;
			}
			
			FluidStack mInputfluidstack;
			mInputfluidstack = (fluid1 != null) ? fluid1 : fluid2;
			
			//Try with new handler
			//Add Blast Smelter Recipe.
			return CORE.RA.addBlastSmelterRecipe(
					components,
					mInputfluidstack,
					smeltInto.mSmeltInto.getMolten(144L),
					100,
					MathUtils.roundToClosestInt(time*0.8),
					euCost); // EU Cost

		}

		//Add Blast Smelter Recipe.
		return CORE.RA.addBlastSmelterRecipe(
				components,
				smeltInto.mSmeltInto.getMolten(144L),
				100,
				MathUtils.roundToClosestInt(time*0.8),
				euCost); // EU Cost

	}

}