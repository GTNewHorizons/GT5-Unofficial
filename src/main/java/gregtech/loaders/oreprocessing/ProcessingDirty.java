package gregtech.loaders.oreprocessing;

import static gregtech.api.enums.GTValues.RA;
import static gregtech.api.recipe.RecipeMaps.chemicalBathRecipes;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.recipe.RecipeMaps.oreWasherRecipes;
import static gregtech.api.recipe.RecipeMaps.thermalCentrifugeRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class ProcessingDirty implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingDirty() {
        OrePrefixes.clump.add(this);
        OrePrefixes.shard.add(this);
        OrePrefixes.crushed.add(this);
        OrePrefixes.dirtyGravel.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        net.minecraft.item.ItemStack aStack) {
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, aStack))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dustImpure, aMaterial.mMacerateInto, 1L))
            .duration(10)
            .eut(16)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, aStack))
            .itemOutputs(
                GTOreDictUnificator.get(
                    OrePrefixes.dustImpure,
                    aMaterial.mMacerateInto,
                    GTOreDictUnificator.get(OrePrefixes.dust, aMaterial.mMacerateInto, 1L),
                    1L),
                GTOreDictUnificator.get(
                    OrePrefixes.dust,
                    GTUtility.selectItemInList(0, aMaterial.mMacerateInto, aMaterial.mOreByProducts),
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
                    GTUtility.selectItemInList(0, aMaterial.mMacerateInto, aMaterial.mOreByProducts),
                    1L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1L))
            .outputChances(100_00, 11_11, 100_00)
            .fluidInputs(GTModHandler.getWater(1000))
            .duration(25 * SECONDS)
            .eut(16)
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
                    GTUtility.selectItemInList(0, aMaterial.mMacerateInto, aMaterial.mOreByProducts),
                    1L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1L))
            .outputChances(100_00, 11_11, 100_00)
            .fluidInputs(GTModHandler.getDistilledWater(200))
            .duration(15 * SECONDS)
            .eut(16)
            .addTo(oreWasherRecipes);

        OrePrefixes prefix = aPrefix == OrePrefixes.crushed ? OrePrefixes.crushedCentrifuged : OrePrefixes.dust;

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, aStack))
            .itemOutputs(
                GTOreDictUnificator.get(prefix, aMaterial, 1L),
                GTOreDictUnificator.get(
                    OrePrefixes.dust,
                    GTUtility.selectItemInList(1, aMaterial.mMacerateInto, aMaterial.mOreByProducts),
                    1L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1L))
            .outputChances(10000, 1111, 10000)
            .duration(25 * SECONDS)
            .eut(48)
            .addTo(thermalCentrifugeRecipes);

        addChemicalBathRecipes(aMaterial, aMaterial, aStack, aPrefix);

        for (Materials tMaterial : aMaterial.mOreByProducts) {
            addChemicalBathRecipes(aMaterial, tMaterial, aStack, aPrefix);
        }
    }

    private void addChemicalBathRecipes(Materials material, Materials byproduct, ItemStack stack, OrePrefixes prefix) {
        OrePrefixes chemicalBathPrefix = prefix == OrePrefixes.crushed ? OrePrefixes.crushedPurified
            : OrePrefixes.dustPure;

        if (byproduct.contains(SubTag.WASHING_MERCURY)) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, stack))
                .itemOutputs(
                    GTOreDictUnificator.get(chemicalBathPrefix, material, 1L),
                    GTOreDictUnificator.get(OrePrefixes.dust, byproduct.mMacerateInto, 1L),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1L))
                .outputChances(10000, 7000, 4000)
                .fluidInputs(Materials.Mercury.getFluid(1000L))
                .duration(40 * SECONDS)
                .eut(8)
                .addTo(chemicalBathRecipes);
        }
        if (byproduct.contains(SubTag.WASHING_MERCURY_99_PERCENT)) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, stack))
                .itemOutputs(
                    GTOreDictUnificator.get(chemicalBathPrefix, material, 1L),
                    GTOreDictUnificator.get(OrePrefixes.dust, byproduct.mMacerateInto, 1L),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1L))
                .outputChances(10000, 9900, 4000)
                .fluidInputs(Materials.Mercury.getFluid(1000L))
                .duration(40 * SECONDS)
                .eut(8)
                .addTo(chemicalBathRecipes);
        }
        if (byproduct.contains(SubTag.WASHING_SODIUMPERSULFATE)) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, stack))
                .itemOutputs(
                    GTOreDictUnificator.get(chemicalBathPrefix, material, 1L),
                    GTOreDictUnificator.get(OrePrefixes.dust, byproduct.mMacerateInto, 1L),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1L))
                .outputChances(10000, 7000, 4000)
                .fluidInputs(Materials.SodiumPersulfate.getFluid(100L))
                .duration(40 * SECONDS)
                .eut(8)
                .addTo(chemicalBathRecipes);
        }
    }
}
