package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.benderRecipes;
import static gregtech.api.recipe.RecipeMaps.cutterRecipes;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.latheRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class ProcessingStick implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingStick() {
        OrePrefixes.stick.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        // Blacklist materials which are handled by Werkstoff loader
        if (aMaterial == Materials.Salt || aMaterial == Materials.RockSalt
            || aMaterial == Materials.Spodumene
            || aMaterial == Materials.Calcium
            || aMaterial == Materials.Magnesia) return;

        if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
            GTModHandler.addCraftingRecipe(
                GTOreDictUnificator.get(OrePrefixes.springSmall, aMaterial, 1L),
                GTModHandler.RecipeBits.BUFFERED,
                new Object[] { " s ", "fPx", 'P', OrePrefixes.stick.get(aMaterial) });
        }
        if (!aMaterial.contains(gregtech.api.enums.SubTag.NO_WORKING)) {

            if ((aMaterial.contains(SubTag.CRYSTAL) ? GTOreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L)
                : GTOreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L)) != null
                && GTOreDictUnificator.get(OrePrefixes.dustSmall, aMaterial.mMacerateInto, 1L) != null) {
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        aMaterial.contains(SubTag.CRYSTAL) ? GTOreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L)
                            : GTOreDictUnificator.get(OrePrefixes.ingot, aMaterial, 1L))
                    .itemOutputs(
                        GTOreDictUnificator.get(OrePrefixes.stick, aMaterial, 1L),
                        GTOreDictUnificator.get(OrePrefixes.dustSmall, aMaterial.mMacerateInto, 2L))
                    .duration(((int) Math.max(aMaterial.getMass() * 5L, 1L)) * TICKS)
                    .eut(calculateRecipeEU(aMaterial, 16))
                    .addTo(latheRecipes);
            }

            if (GTOreDictUnificator.get(OrePrefixes.bolt, aMaterial, 1L) != null) {

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.bolt, aMaterial, 4L))
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

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.bolt, aMaterial, 4L))
                    .fluidInputs(
                        GTModHandler.getDistilledWater(
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

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.bolt, aMaterial, 4L))
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

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, aStack))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.bolt, aMaterial, 4L))
                    .fluidInputs(
                        Materials.DimensionallyShiftedSuperfluid.getFluid(
                            Math.max(
                                1,
                                Math.min(
                                    10,
                                    ((int) Math.max(aMaterial.getMass() * 2L, 1L)) * calculateRecipeEU(aMaterial, 4)
                                        / 4000))))
                    .duration(((int) Math.max(aMaterial.getMass() * 2L / 2.5, 1L)) * TICKS)
                    .eut(calculateRecipeEU(aMaterial, 4))
                    .addTo(cutterRecipes);
            }

            if ((aMaterial.mUnifiable) && (aMaterial.mMaterialInto == aMaterial)) {
                if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.stick, aMaterial, 2L),
                        GTModHandler.RecipeBits.BITS_STD,
                        new Object[] { "s", "X", 'X', OrePrefixes.stickLong.get(aMaterial) });
                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.stick, aMaterial, 1L),
                        GTModHandler.RecipeBits.BITS_STD,
                        new Object[] { "f ", " X", 'X', OrePrefixes.ingot.get(aMaterial) });
                }
            }
        }
        if (!aMaterial.contains(gregtech.api.enums.SubTag.NO_SMASHING)) {
            // bender recipe
            {
                if (GTOreDictUnificator.get(OrePrefixes.springSmall, aMaterial, 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, aStack))
                        .circuit(1)
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.springSmall, aMaterial, 2L))
                        .duration(5 * SECONDS)
                        .eut(calculateRecipeEU(aMaterial, 8))
                        .addTo(benderRecipes);
                }
            }

            if (GTOreDictUnificator.get(OrePrefixes.stickLong, aMaterial, 1L) != null) {
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(2, aStack))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.stickLong, aMaterial, 1L))
                    .duration(Math.max(aMaterial.getMass(), 1L))
                    .eut(calculateRecipeEU(aMaterial, 16))
                    .addTo(hammerRecipes);
            }
        }
    }
}
