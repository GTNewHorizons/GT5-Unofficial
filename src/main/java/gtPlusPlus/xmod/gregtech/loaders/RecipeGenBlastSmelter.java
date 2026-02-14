package gtPlusPlus.xmod.gregtech.loaders;

import static gregtech.api.enums.GTValues.VP;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.alloyBlastSmelterRecipes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.recipe.RecipeCategories;
import gregtech.api.util.GTRecipeBuilder;
import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.material.MaterialStack;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.nuclear.MaterialsFluorides;
import gtPlusPlus.core.material.nuclear.MaterialsNuclides;
import gtPlusPlus.core.material.state.MaterialState;

public class RecipeGenBlastSmelter extends RecipeGenBase {

    public static final Set<RunnableWithInfo<Material>> mRecipeGenMap = new HashSet<>();

    static {
        MaterialGenerator.mRecipeMapsToGenerate.add(mRecipeGenMap);
    }

    public RecipeGenBlastSmelter(final Material M) {
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

        final Material[] badMaterials = { MaterialsFluorides.THORIUM_HEXAFLUORIDE,
            MaterialsFluorides.THORIUM_TETRAFLUORIDE, MaterialsAlloy.BLOODSTEEL, MaterialsNuclides.LiFBeF2ThF4UF4,
            MaterialsNuclides.LiFBeF2ZrF4UF4, MaterialsNuclides.LiFBeF2ZrF4U235, MaterialsAlloy.NITINOL_60 };
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

        // Make a simple one Material setup and log it for validity.
        inputStackCount = 1;
        fluidAmount = 144 * inputStackCount;
        Logger.WARNING(
            "[BAS] Adding an Alloy Blast Smelter Recipe for " + M.getDefaultLocalName()
                + ". Gives "
                + fluidAmount
                + "L of molten metal.");
        Logger.WARNING(
            "[BAS] tMaterial[0]: " + tStack
                .getDisplayName() + " Meta: " + tStack.getItemDamage() + ", Amount: " + tStack.stackSize);

        // Generate Recipes for all singular materials that can be made molten.
        if (M.requiresBlastFurnace()) {
            GTValues.RA.stdBuilder()
                .itemInputs(tStack)
                .circuit(1)
                .fluidOutputs(M.getFluidStack(fluidAmount))
                .duration(duration / (mTotalPartsCounter > 0 ? mTotalPartsCounter : 1))
                .eut(aVoltage)
                .recipeCategory(RecipeCategories.absNonAlloyRecipes)
                .addTo(alloyBlastSmelterRecipes);
        } else {
            Logger.WARNING("[BAS] Failed.");
        }

        if (tMaterial == null) {
            Logger.WARNING("[BAS] doTest: " + doTest + " | tMaterial == null: true");
            return;
        }

        // Reset the Variables for compounds if last recipe was a success.

        if (mMaterialListSize <= 1) {
            return;
        }
        // If this Material has some kind of compound list, proceed

        final gtPlusPlus.core.material.MaterialStack[] tempStack = new gtPlusPlus.core.material.MaterialStack[mMaterialListSize];

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
                    .getDefaultLocalName());
            Logger.WARNING(
                "[BAS] ADDING: " + xMaterial.getStackMaterial()
                    .getDefaultLocalName());
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
                .getState() != MaterialState.SOLID)
                || (M.getComposites()
                    .get(irc)
                    .getDustStack(r)) == null) {
                if (r > 0 && r <= 100) {
                    final int mathmatics = (r * 1000);
                    componentsFluid = M.getComposites()
                        .get(irc)
                        .getStackMaterial()
                        .getFluidStack(mathmatics);
                }
            } else {
                components[irc] = M.getComposites()
                    .get(irc)
                    .getUnificatedDustStack(r);
            }
        }

        // Decide whether to add a circuit
        boolean addCircuit = (mMaterialListSize < 9);
        if (addCircuit) {
            Logger.WARNING(
                "[BAS] Should have added a circuit. mMaterialListSize: " + mMaterialListSize
                    + " | circuit: Integrated Circuit "
                    + mMaterialListSize);
        } else {
            Logger.WARNING("[BAS] Did not add a circuit. mMaterialListSize: " + mMaterialListSize);
        }

        // Set Fluid output
        fluidAmount = 144 * inputStackCount;

        Logger.WARNING(
            "[BAS] Adding an Alloy Blast Smelter Recipe for " + M.getDefaultLocalName()
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
        GTRecipeBuilder builder = GTValues.RA.stdBuilder();
        List<ItemStack> inputs = new ArrayList<>(Arrays.asList(components));
        inputs.removeIf(Objects::isNull);
        components = inputs.toArray(new ItemStack[0]);

        builder.itemInputs(components);

        if (addCircuit) {
            builder.circuit(mMaterialListSize);
        }

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
