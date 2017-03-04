package gtPlusPlus.xmod.gregtech.loaders;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gtPlusPlus.core.material.Material;

public class RecipeGen_AlloySmelter  implements Runnable{

	final Material toGenerate;

	public RecipeGen_AlloySmelter(final Material M){
		this.toGenerate = M;
	}

	@Override
	public void run() {
		generateRecipes(this.toGenerate);
	}

	public static void generateRecipes(final Material material){
		final int tVoltageMultiplier = material.getMeltingPointK() >= 2800 ? 60 : 15;

		//Nuggets
		GT_Values.RA.addAlloySmelterRecipe(
				material.getIngot(1),
				ItemList.Shape_Mold_Nugget.get(1),
				material.getNugget(9),
				(int) Math.max(material.getMass() * 2L, 1L),
				2 * tVoltageMultiplier);

		//Gears
		GT_Values.RA.addAlloySmelterRecipe(
				material.getIngot(8),
				ItemList.Shape_Mold_Gear.get(1),
				material.getGear(1),
				(int) Math.max(material.getMass() * 2L, 1L),
				2 * tVoltageMultiplier);

	}

}

