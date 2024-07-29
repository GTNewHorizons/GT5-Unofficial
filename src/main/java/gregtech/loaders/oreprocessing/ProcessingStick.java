package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.benderRecipes;
import static gregtech.api.recipe.RecipeMaps.cutterRecipes;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.latheRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;
import static gregtech.api.util.GT_Utility.calculateRecipeEU;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Proxy;

public class ProcessingStick implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingStick() {
        OrePrefixes.stick.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
            GT_ModHandler.addCraftingRecipe(
                GT_OreDictUnificator.get(OrePrefixes.springSmall, aMaterial, 1L),
                GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] { " s ", "fPx", 'P', OrePrefixes.stick.get(aMaterial) });
        }
        if (!aMaterial.contains(gregtech.api.enums.SubTag.NO_WORKING)) {

            if ((aMaterial.contains(SubTag.CRYSTAL) ? GT_OreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L)
                : GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L)) != null
                && GT_OreDictUnificator.get(OrePrefixes.dustSmall, aMaterial.mMacerateInto, 1L) != null) {
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        aMaterial.contains(SubTag.CRYSTAL) ? GT_OreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L)
                            : GT_OreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L))
                    .itemOutputs(
                        GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dustSmall, aMaterial.mMacerateInto, 2L))
                    .duration(((int) Math.max(aMaterial.getMass() * 5L, 1L)) * TICKS)
                    .eut(calculateRecipeEU(aMaterial, 16))
                    .addTo(latheRecipes);
            }

            if (GT_OreDictUnificator.get(OrePrefixes.bolt, aMaterial, 1L) != null) {

                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1, aStack))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.bolt, aMaterial, 4L))
                    .fluidInputs(
                        Materials.Water.getFluid(
                            Math.max(
                                4,
                                Math.min(
                                    1000,
                                    2 * ((int) Math.max(aMaterial.getMass() * 2L, 1L))
                                        * calculateRecipeEU(aMaterial, 4)
                                        / 320))))
                    .duration(2 * ((int) Math.max(aMaterial.getMass() * 2L, 1L)) * TICKS)
                    .eut(calculateRecipeEU(aMaterial, 4))
                    .addTo(cutterRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1, aStack))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.bolt, aMaterial, 4L))
                    .fluidInputs(
                        GT_ModHandler.getDistilledWater(
                            Math.max(
                                3,
                                Math.min(
                                    750,
                                    2 * ((int) Math.max(aMaterial.getMass() * 2L, 1L))
                                        * calculateRecipeEU(aMaterial, 4)
                                        / 426))))
                    .duration(2 * ((int) Math.max(aMaterial.getMass() * 2L, 1L)) * TICKS)
                    .eut(calculateRecipeEU(aMaterial, 4))
                    .addTo(cutterRecipes);

                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1, aStack))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.bolt, aMaterial, 4L))
                    .fluidInputs(
                        Materials.Lubricant.getFluid(
                            Math.max(
                                1,
                                Math.min(
                                    250,
                                    ((int) Math.max(aMaterial.getMass() * 2L, 1L)) * calculateRecipeEU(aMaterial, 4)
                                        / 1280))))
                    .duration(((int) Math.max(aMaterial.getMass() * 2L, 1L)) * TICKS)
                    .eut(calculateRecipeEU(aMaterial, 4))
                    .addTo(cutterRecipes);
            }

            if ((aMaterial.mUnificatable) && (aMaterial.mMaterialInto == aMaterial)) {
                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                    GT_ModHandler.addCraftingRecipe(
                        GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial, 2L),
                        GT_Proxy.tBits,
                        new Object[] { "s", "X", 'X', OrePrefixes.stickLong.get(aMaterial) });
                    GT_ModHandler.addCraftingRecipe(
                        GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial, 1L),
                        GT_Proxy.tBits,
                        new Object[] { "f ", " X", 'X', OrePrefixes.ingot.get(aMaterial) });
                }
            }
        }
        if (!aMaterial.contains(gregtech.api.enums.SubTag.NO_SMASHING)) {
            // bender recipe
            {
                if (GT_OreDictUnificator.get(OrePrefixes.springSmall, aMaterial, 1L) != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_Utility.copyAmount(1, aStack), GT_Utility.getIntegratedCircuit(1))
                        .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.springSmall, aMaterial, 2L))
                        .duration(5 * SECONDS)
                        .eut(calculateRecipeEU(aMaterial, 8))
                        .addTo(benderRecipes);
                }
            }

            if (GT_OreDictUnificator.get(OrePrefixes.stickLong, aMaterial, 1L) != null) {
                GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(2, aStack))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.stickLong, aMaterial, 1L))
                    .duration(Math.max(aMaterial.getMass(), 1L))
                    .eut(calculateRecipeEU(aMaterial, 16))
                    .addTo(hammerRecipes);
            }
        }
    }
}
