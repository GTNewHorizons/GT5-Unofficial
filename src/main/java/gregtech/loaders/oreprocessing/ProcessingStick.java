package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.benderRecipes;
import static gregtech.api.recipe.RecipeMaps.cutterRecipes;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.latheRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTUtility.calculateRecipeEU;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class ProcessingStick implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public static ProcessingStick INSTANCE;

    public ProcessingStick() {
        INSTANCE = this;
        OrePrefixes.stick.add(this);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Materials material, String oreDictName, String modName,
        ItemStack stack) {
        registerOre(prefix, MU.material(material), oreDictName, modName, stack);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        Materials legacyMaterial = MU.materialOf(material);
        if (legacyMaterial == null) return;

        // Blacklist materials which are handled by Werkstoff loader
        if (legacyMaterial == Materials.Salt || legacyMaterial == Materials.RockSalt
            || legacyMaterial == Materials.Spodumene
            || legacyMaterial == Materials.Calcium
            || legacyMaterial == Materials.Magnesia) return;

        {
            Integer processingTierEU = material.getProperty(GTMaterialProperties.PROCESSING_MATERIAL_TIER_EU);
            if ((processingTierEU == null ? 0 : processingTierEU) < TierEU.IV) {
                GTModHandler.addCraftingRecipe(
                    GTOreDictUnificator.get(OrePrefixes.springSmall, material, 1L),
                    GTModHandler.RecipeBits.BUFFERED,
                    new Object[] { " s ", "fPx", 'P', MU.craftIngredient(OrePrefixes.stick, material) });
            }
        }
        if (!MU.hasFlag(material, GTMaterialFlag.NO_WORKING)) {

            if ((MU.hasFlag(material, GTMaterialFlag.CRYSTAL) ? GTOreDictUnificator.get(OrePrefixes.gem, material, 1L)
                : GTOreDictUnificator.get(OrePrefixes.ingot, material, 1L)) != null
                && GTOreDictUnificator.get(OrePrefixes.dustSmall, MU.macerateInto(material), 1L) != null) {
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        MU.hasFlag(material, GTMaterialFlag.CRYSTAL)
                            ? GTOreDictUnificator.get(OrePrefixes.gem, material, 1L)
                            : GTOreDictUnificator.get(OrePrefixes.ingot, material, 1L))
                    .itemOutputs(
                        GTOreDictUnificator.get(OrePrefixes.stick, material, 1L),
                        GTOreDictUnificator.get(OrePrefixes.dustSmall, MU.macerateInto(material), 2L))
                    .duration(((int) Math.max(MU.mass(material) * 5L, 1L)) * TICKS)
                    .eut(calculateRecipeEU(material, 16))
                    .addTo(latheRecipes);
            }

            if (GTOreDictUnificator.get(OrePrefixes.bolt, material, 1L) != null) {

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, stack))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.bolt, material, 4L))
                    .fluidInputs(
                        Materials.Water.getFluid(
                            Math.max(
                                4,
                                Math.min(
                                    1000,
                                    2 * ((int) Math.max(MU.mass(material) * 2L, 1L))
                                        * calculateRecipeEU(material, 4)
                                        / 320))))
                    .duration(2 * ((int) Math.max(MU.mass(material) * 2L, 1L)) * TICKS)
                    .eut(calculateRecipeEU(material, 4))
                    .addTo(cutterRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, stack))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.bolt, material, 4L))
                    .fluidInputs(
                        GTModHandler.getDistilledWater(
                            Math.max(
                                3,
                                Math.min(
                                    750,
                                    2 * ((int) Math.max(MU.mass(material) * 2L, 1L))
                                        * calculateRecipeEU(material, 4)
                                        / 426))))
                    .duration(2 * ((int) Math.max(MU.mass(material) * 2L, 1L)) * TICKS)
                    .eut(calculateRecipeEU(material, 4))
                    .addTo(cutterRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, stack))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.bolt, material, 4L))
                    .fluidInputs(
                        MaterialLibAPI.getFluidStack(
                            Materials2Materials.Lubricant,
                            Materials2FluidShapes.fluidLiquid,
                            (int) (Math.max(
                                1,
                                Math.min(
                                    250,
                                    ((int) Math.max(MU.mass(material) * 2, 1)) * calculateRecipeEU(material, 4)
                                        / 1280)))))
                    .duration(((int) Math.max(MU.mass(material) * 2L, 1L)) * TICKS)
                    .eut(calculateRecipeEU(material, 4))
                    .addTo(cutterRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(1, stack))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.bolt, material, 4L))
                    .fluidInputs(
                        Materials.DimensionallyShiftedSuperfluid.getFluid(
                            Math.max(
                                1,
                                Math.min(
                                    10,
                                    ((int) Math.max(MU.mass(material) * 2L, 1L)) * calculateRecipeEU(material, 4)
                                        / 4000))))
                    .duration(((int) Math.max(MU.mass(material) * 2L / 2.5, 1L)) * TICKS)
                    .eut(calculateRecipeEU(material, 4))
                    .addTo(cutterRecipes);
            }

            if (!Boolean.FALSE.equals(material.getProperty(GTMaterialProperties.UNIFIABLE))
                && (legacyMaterial.mMaterialInto == legacyMaterial)) {
                Integer processingTierEU = material.getProperty(GTMaterialProperties.PROCESSING_MATERIAL_TIER_EU);
                if ((processingTierEU == null ? 0 : processingTierEU) < TierEU.IV) {
                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.stick, material, 2L),
                        GTModHandler.RecipeBits.BITS_STD,
                        new Object[] { "s", "X", 'X', MU.craftIngredient(OrePrefixes.stickLong, material) });
                    GTModHandler.addCraftingRecipe(
                        GTOreDictUnificator.get(OrePrefixes.stick, material, 1L),
                        GTModHandler.RecipeBits.BITS_STD,
                        new Object[] { "f ", " X", 'X', MU.craftIngredient(OrePrefixes.ingot, material) });
                }
            }
        }
        if (!MU.hasFlag(material, GTMaterialFlag.NO_SMASHING)) {
            // bender recipe
            {
                if (GTOreDictUnificator.get(OrePrefixes.springSmall, material, 1L) != null) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(GTUtility.copyAmount(1, stack))
                        .circuit(1)
                        .itemOutputs(GTOreDictUnificator.get(OrePrefixes.springSmall, material, 2L))
                        .duration(5 * SECONDS)
                        .eut(calculateRecipeEU(material, 8))
                        .addTo(benderRecipes);
                }
            }

            if (GTOreDictUnificator.get(OrePrefixes.stickLong, material, 1L) != null) {
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.copyAmount(2, stack))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.stickLong, material, 1L))
                    .duration(Math.max(MU.mass(material), 1L))
                    .eut(calculateRecipeEU(material, 16))
                    .addTo(hammerRecipes);
            }
        }
    }
}
