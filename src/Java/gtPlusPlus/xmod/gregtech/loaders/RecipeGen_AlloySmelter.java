package gtPlusPlus.xmod.gregtech.loaders;

import java.util.HashSet;
import java.util.Set;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;

import gtPlusPlus.core.material.Material;

public class RecipeGen_AlloySmelter  implements Runnable{

	public static final Set<Runnable> mRecipeGenMap = new HashSet<Runnable>();
	final Material toGenerate;

	public RecipeGen_AlloySmelter(final Material M){
		this.toGenerate = M;
		mRecipeGenMap.add(this);
	}

	@Override
	public void run() {
		generateRecipes(this.toGenerate);
	}

	private void generateRecipes(final Material material){
		final int tVoltageMultiplier = material.getMeltingPointK() >= 2800 ? 60 : 15;

		//Nuggets
		GT_Values.RA.addAlloySmelterRecipe(
				material.getIngot(1),
				ItemList.Shape_Mold_Nugget.get(0),
				material.getNugget(9),
				(int) Math.max(material.getMass() * 2L, 1L),
				2 * tVoltageMultiplier);

		//Gears
		GT_Values.RA.addAlloySmelterRecipe(
				material.getIngot(8),
				ItemList.Shape_Mold_Gear.get(0),
				material.getGear(1),
				(int) Math.max(material.getMass() * 2L, 1L),
				2 * tVoltageMultiplier);
		
		//Ingot
		GT_Values.RA.addAlloySmelterRecipe(
				material.getNugget(9),
				ItemList.Shape_Mold_Ingot.get(0),
				material.getIngot(1),
				(int) Math.max(material.getMass() * 2L, 1L),
				2 * tVoltageMultiplier);

	}

}

