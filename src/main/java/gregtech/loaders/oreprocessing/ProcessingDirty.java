package gregtech.loaders.oreprocessing;

import static gregtech.api.enums.GTValues.RA;
import static gregtech.api.recipe.RecipeMaps.chemicalBathRecipes;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.recipe.RecipeMaps.oreWasherRecipes;
import static gregtech.api.recipe.RecipeMaps.thermalCentrifugeRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class ProcessingDirty implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public static ProcessingDirty INSTANCE;

    public ProcessingDirty() {
        INSTANCE = this;
        OrePrefixes.clump.add(this);
        OrePrefixes.shard.add(this);
        OrePrefixes.crushed.add(this);
        OrePrefixes.dirtyGravel.add(this);
    }

    private boolean didPersulfate = false;
    private boolean didMercury = false;

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        net.minecraft.item.ItemStack aStack) {
        if (MU.hasFlag(aMaterial, GTMaterialFlag.NO_ORE_PROCESSING)) {
            return;
        }

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, aStack))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dustImpure, MU.macerateInto(aMaterial), 1L))
            .duration(10)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, aStack))
            .itemOutputs(
                GTOreDictUnificator.get(
                    OrePrefixes.dustImpure,
                    MU.macerateInto(aMaterial),
                    GTOreDictUnificator.get(OrePrefixes.dust, MU.macerateInto(aMaterial), 1L),
                    1L),
                GTOreDictUnificator.get(
                    OrePrefixes.dust,
                    GTUtility.selectItemInList(0, MU.macerateInto(aMaterial), aMaterial.mOreByProducts),
                    1L))
            .outputChances(10000, 1000)
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, aStack))
            .itemOutputs(
                GTOreDictUnificator.get(
                    aPrefix == OrePrefixes.crushed ? OrePrefixes.crushedPurified : OrePrefixes.dustPure,
                    aMaterial,
                    1L),
                GTOreDictUnificator.get(
                    OrePrefixes.dust,
                    GTUtility.selectItemInList(0, MU.macerateInto(aMaterial), aMaterial.mOreByProducts),
                    1L),
                MaterialLibAPI.getStack(Materials2Materials.Stone, Materials2Shapes.dust, (int) (1)))
            .outputChances(100_00, 11_11, 100_00)
            .fluidInputs(Materials.Water.getFluid(1_000))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(oreWasherRecipes);

        RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, aStack))
            .itemOutputs(
                GTOreDictUnificator.get(
                    aPrefix == OrePrefixes.crushed ? OrePrefixes.crushedPurified : OrePrefixes.dustPure,
                    aMaterial,
                    1L),
                GTOreDictUnificator.get(
                    OrePrefixes.dust,
                    GTUtility.selectItemInList(0, MU.macerateInto(aMaterial), aMaterial.mOreByProducts),
                    1L),
                MaterialLibAPI.getStack(Materials2Materials.Stone, Materials2Shapes.dust, (int) (1)))
            .outputChances(100_00, 11_11, 100_00)
            .fluidInputs(GTModHandler.getDistilledWater(200))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(oreWasherRecipes);

        OrePrefixes prefix = aPrefix == OrePrefixes.crushed ? OrePrefixes.crushedCentrifuged : OrePrefixes.dust;

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, aStack))
            .itemOutputs(
                GTOreDictUnificator.get(prefix, aMaterial, 1L),
                GTOreDictUnificator.get(
                    OrePrefixes.dust,
                    GTUtility.selectItemInList(1, MU.macerateInto(aMaterial), aMaterial.mOreByProducts),
                    1L),
                MaterialLibAPI.getStack(Materials2Materials.Stone, Materials2Shapes.dust, (int) (1)))
            .outputChances(10000, 1111, 10000)
            .duration(25 * SECONDS)
            .eut(48)
            .addTo(thermalCentrifugeRecipes);

        didPersulfate = false;
        didMercury = false;

        addChemicalBathRecipes(aMaterial, aMaterial, aStack, aPrefix);

        for (Materials tMaterial : aMaterial.mOreByProducts) {
            addChemicalBathRecipes(aMaterial, tMaterial, aStack, aPrefix);
        }

    }

    private void addChemicalBathRecipes(Materials material, Materials byproduct, ItemStack stack, OrePrefixes prefix) {
        OrePrefixes chemicalBathPrefix = prefix == OrePrefixes.crushed ? OrePrefixes.crushedPurified
            : OrePrefixes.dustPure;

        if (MU.hasFlag(byproduct, GTMaterialFlag.WASHING_MERCURY) && !didMercury) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, stack))
                .itemOutputs(
                    GTOreDictUnificator.get(chemicalBathPrefix, material, 1L),
                    GTOreDictUnificator.get(OrePrefixes.dust, MU.macerateInto(byproduct), 1L),
                    MaterialLibAPI.getStack(Materials2Materials.Stone, Materials2Shapes.dust, (int) (1)))
                .outputChances(10000, 7000, 4000)
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Mercury, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
                .duration(40 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(chemicalBathRecipes);

            didMercury = true;
        }
        if (MU.hasFlag(byproduct, GTMaterialFlag.WASHING_MERCURY_99_PERCENT) && !didMercury) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, stack))
                .itemOutputs(
                    GTOreDictUnificator.get(chemicalBathPrefix, material, 1L),
                    GTOreDictUnificator.get(OrePrefixes.dust, MU.macerateInto(byproduct), 1L),
                    MaterialLibAPI.getStack(Materials2Materials.Stone, Materials2Shapes.dust, (int) (1)))
                .outputChances(10000, 9900, 4000)
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Mercury, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
                .duration(40 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(chemicalBathRecipes);

            didMercury = true;
        }
        if (MU.hasFlag(byproduct, GTMaterialFlag.WASHING_SODIUMPERSULFATE) && !didPersulfate) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, stack))
                .itemOutputs(
                    GTOreDictUnificator.get(chemicalBathPrefix, material, 1L),
                    GTOreDictUnificator.get(OrePrefixes.dust, MU.macerateInto(byproduct), 1L),
                    MaterialLibAPI.getStack(Materials2Materials.Stone, Materials2Shapes.dust, (int) (1)))
                .outputChances(10000, 7000, 4000)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.SodiumPersulfate,
                        Materials2FluidShapes.fluidLiquid,
                        (int) (100)))
                .duration(40 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(chemicalBathRecipes);

            didPersulfate = true;
        }
    }
}
