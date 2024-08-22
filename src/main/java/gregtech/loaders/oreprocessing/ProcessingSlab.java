package gregtech.loaders.oreprocessing;

import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.recipe.RecipeMaps.chemicalBathRecipes;
import static gregtech.api.util.RecipeBuilder.SECONDS;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_Utility;

public class ProcessingSlab implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingSlab() {
        OrePrefixes.slab.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        if (aOreDictName.startsWith("slabWood")) {
            if (Railcraft.isModLoaded()) {
                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(3, aStack))
                    .itemOutputs(ItemList.RC_Tie_Wood.get(3L))
                    .fluidInputs(Materials.Creosote.getFluid(300L))
                    .duration(10 * SECONDS)
                    .eut(4)
                    .addTo(chemicalBathRecipes);
            }
        }
    }
}
