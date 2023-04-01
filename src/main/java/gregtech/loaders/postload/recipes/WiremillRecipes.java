package gregtech.loaders.postload.recipes;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sWiremillRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;
import static gregtech.api.util.GT_Utility.calculateRecipeEU;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.GT_Mod;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.*;

public class WiremillRecipes implements Runnable {

    // directly copied from GT code but converted to RA2 format
    void registerWiremillRecipes(Materials materials, int baseDuration, int eut, OrePrefixes prefix1,
            OrePrefixes prefix2, int multiplier) {
        GT_Values.RA.stdBuilder()
                    .itemInputs(GT_OreDictUnificator.get(prefix1, materials, 1L), GT_Utility.getIntegratedCircuit(1))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.wireGt01, materials, multiplier))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(baseDuration)
                    .eut(eut)
                    .addTo(sWiremillRecipes);

        GT_Values.RA.stdBuilder()
                    .itemInputs(
                            GT_OreDictUnificator.get(prefix1, materials, 2L / multiplier),
                            GT_Utility.getIntegratedCircuit(2))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.wireGt02, materials, 1L))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration((int) (baseDuration * 1.5f))
                    .eut(eut)
                    .addTo(sWiremillRecipes);

        GT_Values.RA.stdBuilder()
                    .itemInputs(
                            GT_OreDictUnificator.get(prefix1, materials, 4L / multiplier),
                            GT_Utility.getIntegratedCircuit(4))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.wireGt04, materials, 1L))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(baseDuration * 2)
                    .eut(eut)
                    .addTo(sWiremillRecipes);

        GT_Values.RA.stdBuilder()
                    .itemInputs(
                            GT_OreDictUnificator.get(prefix1, materials, 8L / multiplier),
                            GT_Utility.getIntegratedCircuit(8))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.wireGt08, materials, 1L))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration((int) (baseDuration * 2.5f))
                    .eut(eut)
                    .addTo(sWiremillRecipes);

        GT_Values.RA.stdBuilder()
                    .itemInputs(
                            GT_OreDictUnificator.get(prefix1, materials, 12L / multiplier),
                            GT_Utility.getIntegratedCircuit(12))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.wireGt12, materials, 1L))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(baseDuration * 3)
                    .eut(eut)
                    .addTo(sWiremillRecipes);

        GT_Values.RA.stdBuilder()
                    .itemInputs(
                            GT_OreDictUnificator.get(prefix1, materials, 16L / multiplier),
                            GT_Utility.getIntegratedCircuit(16))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.wireGt16, materials, 1L))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration((int) (baseDuration * 3.5f))
                    .eut(eut)
                    .addTo(sWiremillRecipes);

        GT_Values.RA.stdBuilder()
                    .itemInputs(
                            GT_OreDictUnificator.get(prefix2, materials, 2L / multiplier),
                            GT_Utility.getIntegratedCircuit(1))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.wireGt01, materials, 1L))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(baseDuration / 2)
                    .eut(eut)
                    .addTo(sWiremillRecipes);

        GT_Values.RA.stdBuilder()
                    .itemInputs(
                            GT_OreDictUnificator.get(prefix2, materials, 4L / multiplier),
                            GT_Utility.getIntegratedCircuit(2))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.wireGt02, materials, 1L))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(baseDuration)
                    .eut(eut)
                    .addTo(sWiremillRecipes);

        GT_Values.RA.stdBuilder()
                    .itemInputs(
                            GT_OreDictUnificator.get(prefix2, materials, 8L / multiplier),
                            GT_Utility.getIntegratedCircuit(4))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.wireGt04, materials, 1L))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration((int) (baseDuration * 1.5f))
                    .eut(eut)
                    .addTo(sWiremillRecipes);

        GT_Values.RA.stdBuilder()
                    .itemInputs(
                            GT_OreDictUnificator.get(prefix2, materials, 16L / multiplier),
                            GT_Utility.getIntegratedCircuit(8))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.wireGt08, materials, 1L))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(baseDuration * 2)
                    .eut(eut)
                    .addTo(sWiremillRecipes);

        GT_Values.RA.stdBuilder()
                    .itemInputs(
                            GT_OreDictUnificator.get(prefix2, materials, 24L / multiplier),
                            GT_Utility.getIntegratedCircuit(12))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.wireGt12, materials, 1L))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration((int) (baseDuration * 2.5f))
                    .eut(eut)
                    .addTo(sWiremillRecipes);

        GT_Values.RA.stdBuilder()
                    .itemInputs(
                            GT_OreDictUnificator.get(prefix2, materials, 32L / multiplier),
                            GT_Utility.getIntegratedCircuit(16))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.wireGt16, materials, 1L))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(baseDuration * 3)
                    .eut(eut)
                    .addTo(sWiremillRecipes);

        GT_Values.RA.stdBuilder()
                    .itemInputs(GT_OreDictUnificator.get(prefix1, materials, 1L), GT_Utility.getIntegratedCircuit(3))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.wireFine, materials, 4L * multiplier))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(baseDuration)
                    .eut(eut)
                    .addTo(sWiremillRecipes);

        GT_Values.RA.stdBuilder()
                    .itemInputs(GT_OreDictUnificator.get(prefix2, materials, 1L), GT_Utility.getIntegratedCircuit(3))
                    .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.wireFine, materials, 2L * multiplier))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(baseDuration / 2)
                    .eut(eut)
                    .addTo(sWiremillRecipes);
    }

    void registerWiremillRecipes(Materials aMaterial, int baseDuration, int aEUt) {
        registerWiremillRecipes(
                aMaterial,
                baseDuration,
                calculateRecipeEU(aMaterial, aEUt),
                OrePrefixes.ingot,
                OrePrefixes.stick,
                2);
    }

    @Override
    public void run() {

        registerWiremillRecipes(Materials.Graphene, 20 * SECONDS, 2, OrePrefixes.dust, OrePrefixes.stick, 1);

        registerWiremillRecipes(Materials.SpaceTime, 20 * SECONDS, 32_000);

        GT_Values.RA.stdBuilder()
                    .itemInputs(GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Polycaprolactam, 1L))
                    .itemOutputs(new ItemStack(Items.string, 32))
                    .noFluidInputs()
                    .noFluidOutputs()
                    .duration(4 * SECONDS)
                    .eut(48)
                    .addTo(sWiremillRecipes);

        if (!GT_Mod.gregtechproxy.mDisableIC2Cables) {
            GT_Values.RA.stdBuilder()
                        .itemInputs(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Copper, 1L))
                        .itemOutputs(GT_ModHandler.getIC2Item("copperCableItem", 3L))
                        .noFluidInputs()
                        .noFluidOutputs()
                        .duration(5 * SECONDS)
                        .eut(2)
                        .addTo(sWiremillRecipes);

            GT_Values.RA.stdBuilder()
                        .itemInputs(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.AnnealedCopper, 1L))
                        .itemOutputs(GT_ModHandler.getIC2Item("copperCableItem", 3L))
                        .noFluidInputs()
                        .noFluidOutputs()
                        .duration(5 * SECONDS)
                        .eut(2)
                        .addTo(sWiremillRecipes);

            GT_Values.RA.stdBuilder()
                        .itemInputs(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Tin, 1L))
                        .itemOutputs(GT_ModHandler.getIC2Item("tinCableItem", 4L))
                        .noFluidInputs()
                        .noFluidOutputs()
                        .duration(7 * SECONDS + 10 * TICKS)
                        .eut(1)
                        .addTo(sWiremillRecipes);

            GT_Values.RA.stdBuilder()
                        .itemInputs(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 1L))
                        .itemOutputs(GT_ModHandler.getIC2Item("ironCableItem", 6L))
                        .noFluidInputs()
                        .noFluidOutputs()
                        .duration(10 * SECONDS)
                        .eut(2)
                        .addTo(sWiremillRecipes);

            GT_Values.RA.stdBuilder()
                        .itemInputs(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 1L))
                        .itemOutputs(GT_ModHandler.getIC2Item("ironCableItem", 6L))
                        .noFluidInputs()
                        .noFluidOutputs()
                        .duration(10 * SECONDS)
                        .eut(2)
                        .addTo(sWiremillRecipes);

            GT_Values.RA.stdBuilder()
                        .itemInputs(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Gold, 1L))
                        .itemOutputs(GT_ModHandler.getIC2Item("goldCableItem", 6L))
                        .noFluidInputs()
                        .noFluidOutputs()
                        .duration(10 * SECONDS)
                        .eut(1)
                        .addTo(sWiremillRecipes);
        }
    }
}
