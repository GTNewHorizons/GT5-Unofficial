package gtPlusPlus.xmod.gregtech.loaders;

import static gregtech.api.enums.GT_Values.VP;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.alloyBlastSmelterRecipes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.util.GT_RecipeBuilder;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.recipe.GTPPRecipeCategories;
import gtPlusPlus.core.material.ALLOY;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.material.MaterialStack;
import gtPlusPlus.core.material.nuclear.FLUORIDES;
import gtPlusPlus.core.material.nuclear.NUCLIDE;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class RecipeGen_BlastSmelter extends RecipeGen_Base {

    public static final Set<RunnableWithInfo<Material>> mRecipeGenMap = new HashSet<>();

    static {
        MaterialGenerator.mRecipeMapsToGenerate.put(mRecipeGenMap);
    }

    public RecipeGen_BlastSmelter(final Material M) {
        this.toGenerate = M;
        mRecipeGenMap.add(this);
    }

    @Override
    public void run() {
        generateARecipe(this.toGenerate);
    }

    private void generateARecipe(final Material M) {

        // Add a Blast Smelting Recipe, Let's go!
        ItemStack tStack = M.getDust(1);
        if (tStack == null) {
            return;
        }

        final Material[] badMaterials = { FLUORIDES.THORIUM_HEXAFLUORIDE, FLUORIDES.THORIUM_TETRAFLUORIDE,
            ALLOY.BLOODSTEEL, NUCLIDE.LiFBeF2ThF4UF4, NUCLIDE.LiFBeF2ZrF4UF4, NUCLIDE.LiFBeF2ZrF4U235 };
        for (final Material R : badMaterials) {
            if (M == R) {
                return;
            }
        }

        // Prepare some Variables
        ItemStack[] components;
        ArrayList<MaterialStack> tMaterial = new ArrayList<>();
        int inputStackCount = 0;
        int fluidAmount = 0;
        final boolean doTest = true;
        tMaterial = M.getComposites();

        // This Bad boy here is what dictates unique recipes.
        ItemStack circuitGT;

        int tier = Math.max(1, M.vTier);
        long aVoltage = VP[tier];

        // Set a duration
        int duration = 120 * tier * 10;

        if (tier <= 4) {
            duration = 20 * tier * 10;
        }

        int mMaterialListSize = 0;

        int mTotalPartsCounter = M.smallestStackSizeWhenProcessing;

        if (M.getComposites() != null) {
            for (final gtPlusPlus.core.material.MaterialStack ternkfsdf : M.getComposites()) {
                if (ternkfsdf != null) {
                    mMaterialListSize++;
                }
            }
        } else {
            mMaterialListSize = 1;
        }

        Logger.WARNING("[BAS] Size: " + mMaterialListSize);

        // Make a simple one Material MaterialStack[] and log it for validity.
        circuitGT = GT_Utility.getIntegratedCircuit(1);
        final ItemStack[] tItemStackTest = new ItemStack[] { circuitGT, tStack };
        inputStackCount = 1;
        fluidAmount = 144 * inputStackCount;
        Logger.WARNING(
            "[BAS] Adding an Alloy Blast Smelter Recipe for " + M.getLocalizedName()
                + ". Gives "
                + fluidAmount
                + "L of molten metal.");
        for (int das = 0; das < tItemStackTest.length; das++) {
            if (tItemStackTest[das] != null) {
                Logger.WARNING(
                    "[BAS] tMaterial[" + das
                        + "]: "
                        + tItemStackTest[das].getDisplayName()
                        + " Meta: "
                        + tItemStackTest[das].getItemDamage()
                        + ", Amount: "
                        + tItemStackTest[das].stackSize);
            }
        }

        final boolean hasMoreInputThanACircuit = (tItemStackTest.length > 1);

        // Generate Recipes for all singular materials that can be made molten.
        if (hasMoreInputThanACircuit) {
            if (M.requiresBlastFurnace()) {
                GT_Values.RA.stdBuilder()
                    .itemInputs(tItemStackTest)
                    .fluidOutputs(M.getFluidStack(fluidAmount))
                    .duration(duration / (mTotalPartsCounter > 0 ? mTotalPartsCounter : 1))
                    .eut(aVoltage)
                    .recipeCategory(GTPPRecipeCategories.absNonAlloyRecipes)
                    .addTo(alloyBlastSmelterRecipes);
            } else {
                Logger.WARNING("[BAS] Failed.");
            }
        } else {
            GT_Values.RA.stdBuilder()
                .itemInputs(tItemStackTest)
                .fluidOutputs(M.getFluidStack(fluidAmount))
                .eut(aVoltage)
                .duration(duration / (mTotalPartsCounter > 0 ? mTotalPartsCounter : 1) / 2)
                .addTo(alloyBlastSmelterRecipes);

            Logger.WARNING("[BAS] Success.");
            GT_Values.RA.stdBuilder()
                .itemInputs(ItemList.Shape_Mold_Ingot.get(0))
                .itemOutputs(M.getIngot(1))
                .fluidInputs(M.getFluidStack(144))
                .duration(duration / 2)
                .eut(60)
                .addTo(fluidSolidifierRecipes);

            Logger.WARNING("[BAS] Success, Also added a Fluid solidifier recipe.");

        }

        if (tMaterial == null) {
            Logger.WARNING("[BAS] doTest: " + doTest + " | tMaterial == null: true");
            return;
        }

        // Reset the Variables for compounds if last recipe was a success.
        inputStackCount = 0;

        if (mMaterialListSize <= 1) {
            return;
        }
        // If this Material has some kind of compound list, proceed

        final gtPlusPlus.core.material.MaterialStack[] tempStack = new gtPlusPlus.core.material.MaterialStack[mMaterialListSize];
        circuitGT = GT_Utility.getIntegratedCircuit(mMaterialListSize);

        // Builds me a MaterialStack[] from the MaterialList of M.
        int ooo = 0;
        for (final gtPlusPlus.core.material.MaterialStack xMaterial : M.getComposites()) {
            if (xMaterial == null) {
                ooo++;
                continue;
            }

            if (xMaterial.getStackMaterial() == null) {
                tempStack[ooo] = xMaterial;
                ooo++;
                continue;
            }

            Logger.WARNING(
                "[BAS] FOUND: " + xMaterial.getStackMaterial()
                    .getLocalizedName());
            Logger.WARNING(
                "[BAS] ADDING: " + xMaterial.getStackMaterial()
                    .getLocalizedName());
            tempStack[ooo] = xMaterial;
            ooo++;
        }

        // Builds me an ItemStack[] of the materials. - Without a circuit - this gets a good count for
        // the 144L fluid multiplier
        components = new ItemStack[9];
        inputStackCount = 0;
        FluidStack componentsFluid = null;
        for (int irc = 0; irc < M.getComposites()
            .size(); irc++) {
            if (M.getComposites()
                .get(irc) == null) {
                continue;
            }

            final int r = (int) M.vSmallestRatio[irc];
            inputStackCount = inputStackCount + r;
            if ((M.getComposites()
                .get(irc)
                .getStackMaterial()
                .getState() != MaterialState.SOLID) || !ItemUtils.checkForInvalidItems(
                    M.getComposites()
                        .get(irc)
                        .getDustStack(r))) {
                final int xr = r;
                if ((xr > 0) && (xr <= 100)) {
                    final int mathmatics = (r * 1000);
                    componentsFluid = FluidUtils.getFluidStack(
                        M.getComposites()
                            .get(irc)
                            .getStackMaterial()
                            .getFluidStack(mathmatics),
                        mathmatics);
                }
            } else {
                components[irc] = M.getComposites()
                    .get(irc)
                    .getUnificatedDustStack(r);
            }
        }

        // Adds a circuit
        if ((mMaterialListSize < 9) && (mMaterialListSize != 0)) {
            final ItemStack[] components_NoCircuit = components;
            // Builds me an ItemStack[] of the materials. - With a circuit
            components = new ItemStack[components_NoCircuit.length + 1];
            for (int fr = 0; fr < components.length; fr++) {
                if (fr == 0) {
                    components[0] = circuitGT;
                } else {
                    components[fr] = components_NoCircuit[fr - 1];
                }
            }
            Logger.WARNING(
                "[BAS] Should have added a circuit. mMaterialListSize: " + mMaterialListSize
                    + " | circuit: "
                    + components[0].getDisplayName());
        } else {
            Logger.WARNING("[BAS] Did not add a circuit. mMaterialListSize: " + mMaterialListSize);
        }

        // Set Fluid output
        fluidAmount = 144 * inputStackCount;

        Logger.WARNING(
            "[BAS] Adding an Alloy Blast Smelter Recipe for " + M.getLocalizedName()
                + " using it's compound dusts. This material has "
                + inputStackCount
                + " parts. Gives "
                + fluidAmount
                + "L of molten metal.");
        Logger.WARNING("[BAS] tMaterial.length: " + components.length + ".");
        for (int das = 0; das < components.length; das++) {
            if (components[das] != null) {
                Logger.WARNING(
                    "[BAS] tMaterial[" + das
                        + "]: "
                        + components[das].getDisplayName()
                        + " Meta: "
                        + components[das].getItemDamage()
                        + ", Amount: "
                        + components[das].stackSize);
            }
        }

        // Adds Recipe
        GT_RecipeBuilder builder = GT_Values.RA.stdBuilder();
        List<ItemStack> inputs = new ArrayList<>(Arrays.asList(components));
        inputs.removeIf(Objects::isNull);
        components = inputs.toArray(new ItemStack[0]);

        builder = builder.itemInputs(components);
        if (componentsFluid != null) {
            builder.fluidInputs(componentsFluid);
        }
        builder.fluidOutputs(M.getFluidStack(fluidAmount));

        if (M.requiresBlastFurnace()) {
            builder.eut(aVoltage);
        } else {
            builder.eut(aVoltage / 2);
        }
        builder.duration(duration)
            .addTo(alloyBlastSmelterRecipes);
        Logger.WARNING("[BAS] Success.");
    }
}
