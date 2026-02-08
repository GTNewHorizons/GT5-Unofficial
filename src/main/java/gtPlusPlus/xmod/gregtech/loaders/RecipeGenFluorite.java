package gtPlusPlus.xmod.gregtech.loaders;

import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.chemicalBathRecipes;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.recipe.RecipeMaps.thermalCentrifugeRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalDehydratorRecipes;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.material.nuclear.MaterialsFluorides;

public class RecipeGenFluorite extends RecipeGenBase {

    public static final Set<RunnableWithInfo<Material>> mRecipeGenMap = new HashSet<>();

    static {
        MaterialGenerator.mRecipeMapsToGenerate.add(mRecipeGenMap);
    }

    public RecipeGenFluorite(final Material material) {
        this.toGenerate = material;
        mRecipeGenMap.add(this);

        GTModHandler.addCraftingRecipe(
            material.getDustPurified(1),
            new Object[] { "h  ", "P  ", "   ", 'P', material.getCrushedPurified(1) });

        GTModHandler.addCraftingRecipe(
            material.getDustImpure(1),
            new Object[] { "h  ", "C  ", "   ", 'C', material.getCrushed(1) });

        GTModHandler.addCraftingRecipe(
            material.getDust(1),
            new Object[] { "h  ", "C  ", "   ", 'C', material.getCrushedCentrifuged(1) });

        final ItemStack normalDust = material.getDust(1);
        final ItemStack smallDust = material.getSmallDust(1);
        final ItemStack tinyDust = material.getTinyDust(1);

        GTModHandler.addCraftingRecipe(normalDust, new Object[] { "TTT", "TTT", "TTT", 'T', tinyDust });
        GTModHandler.addCraftingRecipe(material.getTinyDust(9), new Object[] { "D  ", "   ", "   ", 'D', normalDust });

        GTModHandler.addCraftingRecipe(normalDust, new Object[] { "SS ", "SS ", "   ", 'S', smallDust });
        GTModHandler.addCraftingRecipe(material.getSmallDust(4), new Object[] { " D ", "   ", "   ", 'D', normalDust });
    }

    @Override
    public void run() {
        generateRecipes(this.toGenerate);
    }

    public static void generateRecipes(final Material material) {
        ItemStack tinyDustA = MaterialsFluorides.FLUORITE.getTinyDust(1);
        ItemStack tinyDustB = MaterialsFluorides.FLUORITE.getTinyDust(1);
        ItemStack matDust = MaterialsFluorides.FLUORITE.getDust(1);
        ItemStack matDustA = MaterialsFluorides.FLUORITE.getDust(1);

        // Allow ore dusts to be packaged
        if (material.getSmallDust(1) != null && material.getTinyDust(1) != null) {
            RecipeGenDustGeneration.generatePackagerRecipes(material);
        }

        // Macerate ore to Crushed
        GTValues.RA.stdBuilder()
            .itemInputs(material.getOre(1))
            .itemOutputs(material.getCrushed(2))
            .duration(20 * SECONDS)
            .eut(8)
            .addTo(maceratorRecipes);

        // Macerate raw ore to Crushed
        GTValues.RA.stdBuilder()
            .itemInputs(material.getRawOre(1))
            .itemOutputs(material.getCrushed(2))
            .duration(20 * SECONDS)
            .eut(8)
            .addTo(maceratorRecipes);

        // Macerate Centrifuged to Pure Dust
        GTValues.RA.stdBuilder()
            .itemInputs(material.getCrushedCentrifuged(1))
            .itemOutputs(matDust, matDustA)
            .outputChances(100_00, 10_00)
            .duration(20 * SECONDS)
            .eut(8)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(material.getCrushedPurified(1))
            .itemOutputs(material.getCrushedCentrifuged(1), tinyDustA, Materials.Stone.getDust(1))
            .duration(25 * SECONDS)
            .eut(48)
            .addTo(thermalCentrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsFluorides.FLUORITE.getCrushed(1))
            .itemOutputs(
                MaterialsFluorides.FLUORITE.getCrushedPurified(4),
                MaterialsFluorides.FLUORITE.getDustImpure(2),
                MaterialsFluorides.FLUORITE.getDustPurified(1))
            .outputChances(100_00, 50_00, 10_00)
            .fluidInputs(Materials.Hydrogen.getGas(1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(material.getCrushedCentrifuged(1))
            .itemOutputs(matDust)
            .duration(10 * TICKS)
            .eut(4)
            .addTo(hammerRecipes);

        // Purified Dust to Clean
        GTValues.RA.stdBuilder()
            .itemInputs(material.getDustPurified(1))
            .itemOutputs(matDust, tinyDustA)
            .eut(8)
            .duration((int) Math.max(1L, material.getMass() * 8L))
            .addTo(centrifugeRecipes);

        // Impure Dust to Clean
        GTValues.RA.stdBuilder()
            .itemInputs(material.getDustImpure(1))
            .itemOutputs(matDust, tinyDustB)
            .eut(8)
            .duration((int) Math.max(1L, material.getMass() * 8L))
            .addTo(centrifugeRecipes);

        // CaF2 + H2SO4 -> CaSO4(solid) + 2 HF
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsFluorides.FLUORITE.getDust(37))
            .itemOutputs(
                Materials.Gypsum.getDust(15),
                Materials.Silver.getDust(1),
                Materials.Gold.getDust(2),
                Materials.Tin.getDust(1),
                Materials.Copper.getDust(2))
            .outputChances(100_00, 10_00, 10_00, 30_00, 20_00)
            .fluidInputs(Materials.SulfuricAcid.getFluid(8_000))
            .fluidOutputs(Materials.HydrofluoricAcid.getFluid(16_000))
            .eut(TierEU.RECIPE_HV / 2)
            .duration(10 * MINUTES)
            .addTo(chemicalDehydratorRecipes);
    }
}
