package gtPlusPlus.xmod.gregtech.loaders;

import java.util.HashSet;
import java.util.Set;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;

import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class RecipeGen_AlloySmelter extends RecipeGen_Base {

	public final static Set<RunnableWithInfo<Material>> mRecipeGenMap = new HashSet<RunnableWithInfo<Material>>();
	static {
		MaterialGenerator.mRecipeMapsToGenerate.put(mRecipeGenMap);
	}

	public RecipeGen_AlloySmelter(final Material M){
		this.toGenerate = M;
		mRecipeGenMap.add(this);
	}

	@Override
	public void run() {
		generateRecipes(this.toGenerate);
	}

	private void generateRecipes(final Material material){
		final int tVoltageMultiplier = material.vVoltageMultiplier;

		
		
		//Nuggets
		if (ItemUtils.checkForInvalidItems(material.getIngot(1)) && ItemUtils.checkForInvalidItems(material.getNugget(1)))
		GT_Values.RA.addAlloySmelterRecipe(
				material.getIngot(1),
				ItemList.Shape_Mold_Nugget.get(0),
				material.getNugget(9),
				(int) Math.max(material.getMass() * 2L, 1L),
				2 * tVoltageMultiplier);

		//Gears
		if (ItemUtils.checkForInvalidItems(material.getIngot(1)) && ItemUtils.checkForInvalidItems(material.getGear(1)))
		GT_Values.RA.addAlloySmelterRecipe(
				material.getIngot(8),
				ItemList.Shape_Mold_Gear.get(0),
				material.getGear(1),
				(int) Math.max(material.getMass() * 2L, 1L),
				2 * tVoltageMultiplier);
		
		//Ingot
		if (ItemUtils.checkForInvalidItems(material.getIngot(1)) && ItemUtils.checkForInvalidItems(material.getNugget(1)))
		GT_Values.RA.addAlloySmelterRecipe(
				material.getNugget(9),
				ItemList.Shape_Mold_Ingot.get(0),
				material.getIngot(1),
				(int) Math.max(material.getMass() * 2L, 1L),
				2 * tVoltageMultiplier);

	}

}

