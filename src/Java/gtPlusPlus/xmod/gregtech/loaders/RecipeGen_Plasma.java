package gtPlusPlus.xmod.gregtech.loaders;

import java.util.HashSet;
import java.util.Set;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;
import net.minecraft.item.ItemStack;

public class RecipeGen_Plasma extends RecipeGen_Base {

	public final static Set<RunnableWithInfo<Material>> mRecipeGenMap = new HashSet<RunnableWithInfo<Material>>();
	static {
		MaterialGenerator.mRecipeMapsToGenerate.put(mRecipeGenMap);
	}

	public RecipeGen_Plasma(final Material M){
		this.toGenerate = M;
		mRecipeGenMap.add(this);
	}

	@Override
	public void run() {
		generateRecipes(this.toGenerate);
	}

	private void generateRecipes(final Material material) {		
		// Cool Plasma
		ItemStack aPlasmaCell = material.getPlasmaCell(1);
		ItemStack aCell = material.getCell(1);
		if (material.getPlasmaCell(1) != null){
			GT_Values.RA.addFuel(GT_Utility.copyAmount(1L, aPlasmaCell), GT_Utility.getFluidForFilledItem(aPlasmaCell, true) == null ? GT_Utility.getContainerItem(aPlasmaCell, true) : null, (int) Math.max(1024L, 1024L * material.getMass()), 4);
        }
		if (material.getCell(1) != null && material.getPlasmaCell(1) != null){
			 GT_Values.RA.addVacuumFreezerRecipe(aPlasmaCell, aCell, (int) Math.max(material.getMass() * 2L, 1L));
		}
	}

}
