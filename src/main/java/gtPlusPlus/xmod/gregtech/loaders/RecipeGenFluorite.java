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
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.material.nuclear.MaterialsFluorides;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.core.util.minecraft.RecipeUtils;

public class RecipeGenFluorite extends RecipeGenBase {

    public static final Set<RunnableWithInfo<Material>> mRecipeGenMap = new HashSet<>();

    static {
        MaterialGenerator.mRecipeMapsToGenerate.put(mRecipeGenMap);
    }

    public RecipeGenFluorite(final Material material) {
        this.toGenerate = material;
        mRecipeGenMap.add(this);

        /**
         * Shaped Crafting
         */
        RecipeUtils.addShapedRecipe(
            CI.craftingToolHammer_Hard,
            null,
            null,
            material.getCrushedPurified(1),
            null,
            null,
            null,
            null,
            null,
            material.getDustPurified(1));

        RecipeUtils.addShapedRecipe(
            CI.craftingToolHammer_Hard,
            null,
            null,
            material.getCrushed(1),
            null,
            null,
            null,
            null,
            null,
            material.getDustImpure(1));

        RecipeUtils.addShapedRecipe(
            CI.craftingToolHammer_Hard,
            null,
            null,
            material.getCrushedCentrifuged(1),
            null,
            null,
            null,
            null,
            null,
            material.getDust(1));

        final ItemStack normalDust = material.getDust(1);
        final ItemStack smallDust = material.getSmallDust(1);
        final ItemStack tinyDust = material.getTinyDust(1);

        if (RecipeUtils.addShapedRecipe(
            tinyDust,
            tinyDust,
            tinyDust,
            tinyDust,
            tinyDust,
            tinyDust,
            tinyDust,
            tinyDust,
            tinyDust,
            normalDust)) {
            Logger.WARNING("9 Tiny dust to 1 Dust Recipe: " + material.getLocalizedName() + " - Success");
        } else {
            Logger.WARNING("9 Tiny dust to 1 Dust Recipe: " + material.getLocalizedName() + " - Failed");
        }

        if (RecipeUtils
            .addShapedRecipe(normalDust, null, null, null, null, null, null, null, null, material.getTinyDust(9))) {
            Logger.WARNING("9 Tiny dust from 1 Recipe: " + material.getLocalizedName() + " - Success");
        } else {
            Logger.WARNING("9 Tiny dust from 1 Recipe: " + material.getLocalizedName() + " - Failed");
        }

        if (RecipeUtils
            .addShapedRecipe(smallDust, smallDust, null, smallDust, smallDust, null, null, null, null, normalDust)) {
            Logger.WARNING("4 Small dust to 1 Dust Recipe: " + material.getLocalizedName() + " - Success");
        } else {
            Logger.WARNING("4 Small dust to 1 Dust Recipe: " + material.getLocalizedName() + " - Failed");
        }

        if (RecipeUtils
            .addShapedRecipe(null, normalDust, null, null, null, null, null, null, null, material.getSmallDust(4))) {
            Logger.WARNING("4 Small dust from 1 Dust Recipe: " + material.getLocalizedName() + " - Success");
        } else {
            Logger.WARNING("4 Small dust from 1 Dust Recipe: " + material.getLocalizedName() + " - Failed");
        }
    }

    @Override
    public void run() {
        generateRecipes(this.toGenerate);
    }

    public static void generateRecipes(final Material material) {

        int tVoltageMultiplier = MaterialUtils.getVoltageForTier(material.vTier);

        final ItemStack dustStone = ItemUtils.getItemStackOfAmountFromOreDict("dustStone", 1);

        ItemStack tinyDustA = MaterialsFluorides.FLUORITE.getTinyDust(1);
        ItemStack tinyDustB = MaterialsFluorides.FLUORITE.getTinyDust(1);
        ItemStack matDust = MaterialsFluorides.FLUORITE.getDust(1);
        ItemStack matDustA = MaterialsFluorides.FLUORITE.getDust(1);

        /**
         * Package
         */
        // Allow ore dusts to be packaged
        if (ItemUtils.checkForInvalidItems(material.getSmallDust(1))
            && ItemUtils.checkForInvalidItems(material.getTinyDust(1))) {
            RecipeGenDustGeneration.generatePackagerRecipes(material);
        }

        /**
         * Macerate
         */
        // Macerate ore to Crushed
        GTValues.RA.stdBuilder()
            .itemInputs(material.getOre(1))
            .itemOutputs(material.getCrushed(2))
            .duration(20 * SECONDS)
            .eut(tVoltageMultiplier / 2)
            .addTo(maceratorRecipes);

        Logger.MATERIALS("[Macerator] Added Recipe: 'Macerate ore to Crushed ore'");

        // Macerate raw ore to Crushed
        GTValues.RA.stdBuilder()
            .itemInputs(material.getRawOre(1))
            .itemOutputs(material.getCrushed(2))
            .duration(20 * SECONDS)
            .eut(tVoltageMultiplier / 2)
            .addTo(maceratorRecipes);

        Logger.MATERIALS("[Macerator] Added Recipe: 'Macerate raw ore to Crushed ore'");

        // Macerate Centrifuged to Pure Dust
        GTValues.RA.stdBuilder()
            .itemInputs(material.getCrushedCentrifuged(1))
            .itemOutputs(matDust, matDustA)
            .outputChances(100_00, 10_00)
            .duration(20 * SECONDS)
            .eut(tVoltageMultiplier / 2)
            .addTo(maceratorRecipes);

        Logger.MATERIALS("[Macerator] Added Recipe: 'Macerate Centrifuged ore to Pure Dust'");

        GTValues.RA.stdBuilder()
            .itemInputs(material.getCrushedPurified(1))
            .itemOutputs(material.getCrushedCentrifuged(1), tinyDustA, dustStone)
            .duration(25 * SECONDS)
            .eut(48)
            .addTo(thermalCentrifugeRecipes);

        Logger.MATERIALS(
            "[ThermalCentrifuge] Added Recipe: 'Washed ore to Centrifuged Ore' | Input: "
                + material.getCrushedPurified(1)
                    .getDisplayName()
                + " | Outputs: "
                + material.getCrushedCentrifuged(1)
                    .getDisplayName()
                + ", "
                + tinyDustA.getDisplayName()
                + ", "
                + dustStone.getDisplayName()
                + ".");

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsFluorides.FLUORITE.getCrushed(2))
            .itemOutputs(
                MaterialsFluorides.FLUORITE.getCrushedPurified(8),
                MaterialsFluorides.FLUORITE.getDustImpure(4),
                MaterialsFluorides.FLUORITE.getDustPurified(2))
            .outputChances(100_00, 50_00, 10_00)
            .fluidInputs(FluidUtils.getFluidStack("hydrogen", 2000))
            .duration(30 * SECONDS)
            .eut(240)
            .addTo(chemicalBathRecipes);

        /**
         * Forge Hammer
         */

        GTValues.RA.stdBuilder()
            .itemInputs(material.getCrushedCentrifuged(1))
            .itemOutputs(matDust)
            .duration(10 * TICKS)
            .eut(tVoltageMultiplier / 4)
            .addTo(hammerRecipes);

        Logger.MATERIALS("[ForgeHammer] Added Recipe: 'Crushed Centrifuged to Pure Dust'");

        /**
         * Centrifuge
         */
        // Purified Dust to Clean
        GTValues.RA.stdBuilder()
            .itemInputs(material.getDustPurified(1))
            .itemOutputs(matDust, tinyDustA)
            .eut(tVoltageMultiplier / 2)
            .duration((int) Math.max(1L, material.getMass() * 8L))
            .addTo(centrifugeRecipes);

        Logger.MATERIALS("[Centrifuge] Added Recipe: Purified Dust to Clean Dust");

        // Impure Dust to Clean
        GTValues.RA.stdBuilder()
            .itemInputs(material.getDustImpure(1))
            .itemOutputs(matDust, tinyDustB)
            .eut(tVoltageMultiplier / 2)
            .duration((int) Math.max(1L, material.getMass() * 8L))
            .addTo(centrifugeRecipes);

        Logger.MATERIALS("[Centrifuge] Added Recipe: Inpure Dust to Clean Dust");

        // CaF2 + H2SO4 â†’ CaSO4(solid) + 2 HF
        FluidStack aGregtechHydro = FluidUtils.getFluidStack("hydrofluoricacid_gt5u", 16000);
        if (aGregtechHydro == null) {
            aGregtechHydro = FluidUtils.getFluidStack("hydrofluoricacid", 16000);
        }
        GTValues.RA.stdBuilder()
            .itemInputs(CI.getNumberedAdvancedCircuit(5), MaterialsFluorides.FLUORITE.getDust(37))
            .itemOutputs(
                ItemUtils.getItemStackOfAmountFromOreDict("dustCalciumSulfate", 15),
                ItemUtils.getItemStackOfAmountFromOreDict("dustSilver", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustGold", 2),
                ItemUtils.getItemStackOfAmountFromOreDict("dustTin", 1),
                ItemUtils.getItemStackOfAmountFromOreDict("dustCopper", 2))
            .outputChances(100_00, 10_00, 10_00, 30_00, 20_00)
            .fluidInputs(FluidUtils.getFluidStack("sulfuricacid", 8000))
            .fluidOutputs(aGregtechHydro)
            .eut(240)
            .duration(10 * MINUTES)
            .addTo(chemicalDehydratorRecipes);
    }
}
