package goodgenerator.util;

import static gregtech.api.recipe.RecipeMaps.chemicalReactorRecipes;
import static gregtech.api.recipe.RecipeMaps.multiblockChemicalReactorRecipes;

import java.util.HashSet;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import cpw.mods.fml.common.registry.GameRegistry;
import goodgenerator.items.GGMaterial;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;

public final class NaquadahRecipePatches {

    private NaquadahRecipePatches() {}

    public static void patchCropsNhRecipes() {
        if (!Mods.CropsNH.isModLoaded()) return;
        Item materialLeaf = GameRegistry.findItem(Mods.ModIDs.CROPS_NH, "materialLeaf");
        if (materialLeaf == null) return;
        patchMap(chemicalReactorRecipes, materialLeaf);
        patchMap(multiblockChemicalReactorRecipes, materialLeaf);
    }

    private static void patchMap(RecipeMap<?> recipeMap, Item materialLeaf) {

        HashSet<GTRecipe> remove = new HashSet<>();
        HashSet<GTRecipe> reAdd = new HashSet<>();
        for (GTRecipe recipe : recipeMap.getAllRecipes()) {
            if (!isCropsNhRecipe(recipe, materialLeaf) || recipe.mFluidOutputs == null) continue;
            GTRecipe patched = recipe.copy();
            boolean modified = false;
            for (int i = 0; i < patched.mFluidOutputs.length; i++) {
                FluidStack replacement = replaceNaquadahOutput(patched.mFluidOutputs[i]);
                if (replacement == null) continue;
                patched.mFluidOutputs[i] = replacement;
                modified = true;
            }
            if (modified) {
                remove.add(recipe);
                reAdd.add(patched);
            }
        }
        recipeMap.getBackend()
            .removeRecipes(remove);
        reAdd.forEach(recipeMap::add);
        if (!remove.isEmpty()) recipeMap.getBackend()
            .reInit();
    }

    private static FluidStack replaceNaquadahOutput(FluidStack output) {
        if (output == null) return null;
        if (output.isFluidEqual(Materials.Naquadah.getMolten(1))) {
            return GGMaterial.naquadahGoo.getFluidOrGas(output.amount * 2);
        }
        if (output.isFluidEqual(Materials.NaquadahEnriched.getMolten(1))) {
            return GGMaterial.enrichedNaquadahGoo.getFluidOrGas(output.amount * 2);
        }
        if (output.isFluidEqual(Materials.Naquadria.getMolten(1))) {
            return GGMaterial.naquadriaGoo.getFluidOrGas(output.amount * 2);
        }
        return null;
    }

    private static boolean isCropsNhRecipe(GTRecipe recipe, Item materialLeaf) {
        if (recipe.mInputs.length == 0 || recipe.mInputs[0] == null
            || recipe.mInputs[0].getItem() != materialLeaf
            || recipe.mInputs[0].getItemDamage() != 18) return false;
        for (ItemStack input : recipe.mInputs) {
            if (GTUtility
                .areStacksEqual(input, GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Naquadah, 1), true)
                || GTUtility.areStacksEqual(
                    input,
                    GTOreDictUnificator.get(OrePrefixes.crushed, Materials.NaquadahEnriched, 1),
                    true)
                || GTUtility
                    .areStacksEqual(input, GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Naquadria, 1), true))
                return true;
        }
        return false;
    }
}
