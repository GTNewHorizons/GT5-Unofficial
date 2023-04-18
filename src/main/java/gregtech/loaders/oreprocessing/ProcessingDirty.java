package gregtech.loaders.oreprocessing;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sChemicalBathRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sHammerRecipes;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

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
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmount(1L, aStack))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dustImpure, aMaterial.mMacerateInto, 1L))
            .noFluidInputs()
            .noFluidOutputs()
            .duration(10)
            .eut(16)
            .addTo(sHammerRecipes);

        GT_ModHandler.addPulverisationRecipe(
            GT_Utility.copyAmount(1L, aStack),
            GT_OreDictUnificator.get(
                OrePrefixes.dustImpure,
                aMaterial.mMacerateInto,
                GT_OreDictUnificator.get(OrePrefixes.dust, aMaterial.mMacerateInto, 1L),
                1L),
            GT_OreDictUnificator.get(
                OrePrefixes.dust,
                GT_Utility.selectItemInList(0, aMaterial.mMacerateInto, aMaterial.mOreByProducts),
                1L),
            10,
            false);
        GT_ModHandler.addOreWasherRecipe(
            GT_Utility.copyAmount(1L, aStack),
            new int[] { 10000, 1111, 10000 },
            1000,
            GT_OreDictUnificator.get(
                aPrefix == OrePrefixes.crushed ? OrePrefixes.crushedPurified : OrePrefixes.dustPure,
                aMaterial,
                1L),
            GT_OreDictUnificator.get(
                OrePrefixes.dust,
                GT_Utility.selectItemInList(0, aMaterial.mMacerateInto, aMaterial.mOreByProducts),
                1L),
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1L));

        OrePrefixes prefix = aPrefix == OrePrefixes.crushed ? OrePrefixes.crushedCentrifuged : OrePrefixes.dust;

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.copyAmount(1L, aStack))
            .itemOutputs(
                GT_OreDictUnificator.get(prefix, aMaterial, 1L),
                GT_OreDictUnificator.get(
                    OrePrefixes.dust,
                    GT_Utility.selectItemInList(1, aMaterial.mMacerateInto, aMaterial.mOreByProducts),
                    1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1L))
            .outputChances(10000, 1111, 10000)
            .noFluidInputs()
            .noFluidOutputs()
            .duration(25 * SECONDS)
            .eut(48)
            .addTo(sThermalCentrifugeRecipes);

        addChemicalBathRecipes(aMaterial, aStack, aPrefix);

        for (Materials tMaterial : aMaterial.mOreByProducts) {
            addChemicalBathRecipes(tMaterial, aStack, aPrefix);
        }
    }

    private void addChemicalBathRecipes(Materials material, ItemStack stack, OrePrefixes prefix) {
        OrePrefixes chemicalBathPrefix = prefix == OrePrefixes.crushed ? OrePrefixes.crushedPurified
            : OrePrefixes.dustPure;

        if (material.contains(SubTag.WASHING_MERCURY)) {
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.copyAmount(1L, stack))
                .itemOutputs(
                    GT_OreDictUnificator.get(chemicalBathPrefix, material, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.dust, material.mMacerateInto, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1L))
                .outputChances(10000, 7000, 4000)
                .fluidInputs(Materials.Mercury.getFluid(1000L))
                .noFluidOutputs()
                .duration(40 * SECONDS)
                .eut(8)
                .addTo(sChemicalBathRecipes);
        }
        if (material.contains(SubTag.WASHING_MERCURY_99_PERCENT)) {
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.copyAmount(1L, stack))
                .itemOutputs(
                    GT_OreDictUnificator.get(chemicalBathPrefix, material, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.dust, material.mMacerateInto, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1L))
                .outputChances(10000, 9900, 4000)
                .fluidInputs(Materials.Mercury.getFluid(1000L))
                .noFluidOutputs()
                .duration(40 * SECONDS)
                .eut(8)
                .addTo(sChemicalBathRecipes);
        }
        if (material.contains(SubTag.WASHING_SODIUMPERSULFATE)) {
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.copyAmount(1L, stack))
                .itemOutputs(
                    GT_OreDictUnificator.get(chemicalBathPrefix, material, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.dust, material.mMacerateInto, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1L))
                .outputChances(10000, 7000, 4000)
                .fluidInputs(Materials.SodiumPersulfate.getFluid(100L))
                .noFluidOutputs()
                .duration(40 * SECONDS)
                .eut(8)
                .addTo(sChemicalBathRecipes);
        }
    }
}
