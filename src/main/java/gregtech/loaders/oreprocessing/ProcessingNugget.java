package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.alloySmelterRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.util.GTRecipeBuilder.NUGGETS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MU;
import gregtech.api.material.MaterialUtils;
import gregtech.api.recipe.RecipeCategories;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeRegistrator;
import gregtech.api.util.GTUtility;

public class ProcessingNugget implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public static ProcessingNugget INSTANCE;

    public ProcessingNugget() {
        INSTANCE = this;
        OrePrefixes.nugget.add(this);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        if (MaterialUtils.hasFlag(material, GTMaterialFlag.SMELTING_TO_GEM)
            && GTOreDictUnificator.get(OrePrefixes.gem, MaterialUtils.smeltInto(material), 1L) != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(9, stack), ItemList.Shape_Mold_Ball.get(0L))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, MaterialUtils.smeltInto(material), 1L))
                .duration(10 * SECONDS)
                .eut(calculateRecipeEU(material, 2))
                .addTo(alloySmelterRecipes);
        }

        if ((!MaterialUtils.hasFlag(material, GTMaterialFlag.SMELTING_TO_GEM))
            && GTOreDictUnificator.get(OrePrefixes.ingot, MaterialUtils.smeltInto(material), 1L) != null
            && material != Materials2Materials.Aluminium) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(9, stack), ItemList.Shape_Mold_Ingot.get(0L))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingot, MaterialUtils.smeltInto(material), 1L))
                .duration(10 * SECONDS)
                .eut(calculateRecipeEU(material, 2))
                .recipeCategory(RecipeCategories.alloySmelterMolding)
                .addTo(alloySmelterRecipes);
        }

        if (MaterialUtils.hasMolten(material)) {
            if (!(material == Materials2Materials.AnnealedCopper || material == Materials2Materials.CastIron)) {
                GTValues.RA.stdBuilder()
                    .itemInputs(ItemList.Shape_Mold_Nugget.get(0L))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.nugget, material, 1L))
                    .fluidInputs(MaterialUtils.molten(material, 1 * NUGGETS))
                    .duration(16 * TICKS)
                    .eut(calculateRecipeEU(material, 4))
                    .addTo(fluidSolidifierRecipes);
            }
        }

        GTRecipeRegistrator.registerReverseFluidSmelting(stack, material, prefix.getMaterialAmount(), null, true);
        GTRecipeRegistrator
            .registerReverseMacerating(stack, material, prefix.getMaterialAmount(), null, null, null, false, true);
        if (!MaterialUtils.hasFlag(material, GTMaterialFlag.NO_SMELTING)
            && GTOreDictUnificator.get(OrePrefixes.ingot, material, 1L) != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.ingot, material, 1L),
                    ItemList.Shape_Mold_Nugget.get(0L))
                .itemOutputs(GTUtility.copyAmount(9, stack))
                .duration(5 * SECONDS)
                .eut(calculateRecipeEU(material, 1))
                .recipeCategory(RecipeCategories.alloySmelterMolding)
                .addTo(alloySmelterRecipes);
            Integer processingTierEU = material.getProperty(GTMaterialProperties.PROCESSING_MATERIAL_TIER_EU);
            if ((processingTierEU == null ? 0 : processingTierEU) < TierEU.IV) {
                GTModHandler.addCraftingRecipe(
                    GTOreDictUnificator.get(OrePrefixes.nugget, material, 8L),
                    GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { "sI ", 'I', MU.craftIngredient(OrePrefixes.ingot, material) });
            }
        }
    }
}
