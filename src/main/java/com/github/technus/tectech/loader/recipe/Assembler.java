package com.github.technus.tectech.loader.recipe;

import static com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry.bw_realglas;
import static com.github.technus.tectech.loader.recipe.BaseRecipeLoader.getItemContainer;
import static com.github.technus.tectech.loader.recipe.BaseRecipeLoader.getOrDefault;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.RecipeBuilder.MINUTES;
import static gregtech.api.util.RecipeBuilder.SECONDS;
import static gregtech.api.util.RecipeBuilder.TICKS;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.github.technus.tectech.thing.CustomItemList;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.gtnewhorizon.structurelib.StructureLibAPI;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.OreDictUnificator;

public class Assembler implements Runnable {

    @Override
    public void run() {

        cleanroomRecipes();

        for (int i = 0; i <= 15; i++) {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.getIntegratedCircuit(i + 1),
                    OreDictUnificator.get(OrePrefixes.dust, Materials.Cobalt, 1))
                .itemOutputs(new ItemStack(StructureLibAPI.getBlockHint(), 1, i))
                .fluidInputs(Materials.Aluminium.getMolten(864))
                .duration(1 * SECONDS + 12 * TICKS)
                .eut(TierEU.RECIPE_MV)
                .addTo(assemblerRecipes);
        }

        // Quantum Glass
        GT_Values.RA.stdBuilder()
            .itemInputs(CustomItemList.eM_Containment.get(1), GT_ModHandler.getIC2Item("reinforcedGlass", 1L))
            .itemOutputs(new ItemStack(QuantumGlassBlock.INSTANCE, 1))
            .fluidInputs(getOrDefault("Trinium", Materials.Osmium).getMolten(576))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(assemblerRecipes);

        // recipe for assline data hatches
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Hatch_DataAccess_EV.get(1), CustomItemList.dataIn_Hatch.get(1))
            .itemOutputs(CustomItemList.dataInAss_Hatch.get(1))
            .duration(1 * MINUTES + 42 * SECONDS + 8 * TICKS)
            .eut(12000)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Hatch_DataAccess_EV.get(1), CustomItemList.dataOut_Hatch.get(1))
            .itemOutputs(CustomItemList.dataOutAss_Hatch.get(1))
            .duration(1 * MINUTES + 42 * SECONDS + 8 * TICKS)
            .eut(12000)
            .addTo(assemblerRecipes);

        // High Power Casing
        GT_Values.RA.stdBuilder()
            .itemInputs(
                OreDictUnificator.get(OrePrefixes.frameGt, Materials.Iridium, 1),
                OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Iridium, 6),
                OreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 1),
                OreDictUnificator.get(OrePrefixes.wireFine, Materials.Cobalt, 16),
                OreDictUnificator.get(OrePrefixes.wireFine, Materials.Copper, 16),
                OreDictUnificator.get(OrePrefixes.wireGt02, Materials.NiobiumTitanium, 2))
            .itemOutputs(CustomItemList.eM_Power.get(1))
            .fluidInputs(Materials.TungstenSteel.getMolten(576))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);
        // Computer Casing
        GT_Values.RA.stdBuilder()
            .itemInputs(
                CustomItemList.eM_Power.get(1),
                OreDictUnificator.get(OrePrefixes.plate, Materials.StainlessSteel, 8),
                OreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 1),
                OreDictUnificator.get(OrePrefixes.wireGt02, Materials.NiobiumTitanium, 2))
            .itemOutputs(CustomItemList.eM_Computer_Casing.get(1))
            .fluidInputs(Materials.Aluminium.getMolten(1296))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);
        // Computer Vent Casing
        GT_Values.RA.stdBuilder()
            .itemInputs(
                OreDictUnificator.get(OrePrefixes.frameGt, Materials.StainlessSteel, 1),
                ItemList.Electric_Motor_IV.get(2),
                OreDictUnificator.get(OrePrefixes.rotor, Materials.StainlessSteel, 2),
                OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.StainlessSteel, 16),
                OreDictUnificator.get(OrePrefixes.plate, Materials.Copper, 16),
                OreDictUnificator
                    .get(OrePrefixes.wireGt01, getOrDefault("SuperconductorIV", Materials.SuperconductorUHV), 1))
            .itemOutputs(CustomItemList.eM_Computer_Vent.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(1296))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
        // Molecular Casing
        GT_Values.RA.stdBuilder()
            .itemInputs(
                CustomItemList.eM_Power.get(1),
                OreDictUnificator.get(OrePrefixes.plateDense, Materials.Osmiridium, 6),
                OreDictUnificator.get(OrePrefixes.foil, getOrDefault("Trinium", Materials.Osmium), 12),
                OreDictUnificator.get(OrePrefixes.screw, Materials.TungstenSteel, 24),
                OreDictUnificator.get(OrePrefixes.ring, Materials.TungstenSteel, 24),
                ItemList.Field_Generator_IV.get(1))
            .itemOutputs(CustomItemList.eM_Containment.get(1))
            .fluidInputs(Materials.Osmium.getMolten(1296))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(assemblerRecipes);
        // Tesla Base
        GT_Values.RA.stdBuilder()
            .itemInputs(
                OreDictUnificator.get(OrePrefixes.plate, Materials.NickelZincFerrite, 6),
                OreDictUnificator.get(OrePrefixes.frameGt, Materials.NickelZincFerrite, 1))
            .itemOutputs(CustomItemList.tM_TeslaBase.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);
        // Tesla Toroid
        GT_Values.RA.stdBuilder()
            .itemInputs(
                OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 6),
                OreDictUnificator.get(OrePrefixes.frameGt, Materials.Aluminium, 1))
            .itemOutputs(CustomItemList.tM_TeslaToroid.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(assemblerRecipes);
        // Tesla Secondary Windings
        GT_Values.RA.stdBuilder()
            .itemInputs(
                CustomItemList.teslaComponent.getWithDamage(8, 0),
                getItemContainer("MicaInsulatorFoil").get(12))
            .itemOutputs(CustomItemList.tM_TeslaSecondary.get(1))
            .fluidInputs(Materials.Silver.getMolten(144))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);
        // Tesla Primary Coils T0
        GT_Values.RA.stdBuilder()
            .itemInputs(
                OreDictUnificator.get(OrePrefixes.wireGt02, Materials.RedstoneAlloy, 8),
                getItemContainer("MicaInsulatorFoil").get(8))
            .itemOutputs(CustomItemList.tM_TeslaPrimary_0.get(1))
            .fluidInputs(Materials.RedAlloy.getMolten(144))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);
        // Tesla Primary Coils T1
        GT_Values.RA.stdBuilder()
            .itemInputs(
                OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorMV, 8),
                getItemContainer("MicaInsulatorFoil").get(12))
            .itemOutputs(CustomItemList.tM_TeslaPrimary_1.get(1))
            .fluidInputs(Materials.Magnesium.getMolten(144))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);
        // Tesla Primary Coils T2
        GT_Values.RA.stdBuilder()
            .itemInputs(
                OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorHV, 8),
                getItemContainer("MicaInsulatorFoil").get(16))
            .itemOutputs(CustomItemList.tM_TeslaPrimary_2.get(1))
            .fluidInputs(Materials.Barium.getMolten(144))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
        // Tesla Primary Coils T3
        GT_Values.RA.stdBuilder()
            .itemInputs(
                OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorEV, 8),
                getItemContainer("MicaInsulatorFoil").get(20))
            .itemOutputs(CustomItemList.tM_TeslaPrimary_3.get(1))
            .fluidInputs(Materials.Platinum.getMolten(144))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
        // Tesla Primary Coils T4
        GT_Values.RA.stdBuilder()
            .itemInputs(
                OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorIV, 8),
                getItemContainer("MicaInsulatorFoil").get(24))
            .itemOutputs(CustomItemList.tM_TeslaPrimary_4.get(1))
            .fluidInputs(Materials.Vanadium.getMolten(144))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        // Tesla Primary Coils T5
        GT_Values.RA.stdBuilder()
            .itemInputs(
                OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorLuV, 8),
                getItemContainer("MicaInsulatorFoil").get(28))
            .itemOutputs(CustomItemList.tM_TeslaPrimary_5.get(1))
            .fluidInputs(Materials.Indium.getMolten(144))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);
        // Tesla Primary Coils T6
        GT_Values.RA.stdBuilder()
            .itemInputs(
                OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorZPM, 8),
                getItemContainer("MicaInsulatorFoil").get(32))
            .itemOutputs(CustomItemList.tM_TeslaPrimary_6.get(1))
            .fluidInputs(Materials.Naquadah.getMolten(144))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

        // Dynamo Hatches
        {
            // Dynamo Hatches 4A
            {
                // Dynamo EV 4A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hatch_Dynamo_EV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Aluminium, 2),
                        OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 2))
                    .itemOutputs(CustomItemList.eM_dynamoMulti4_EV.get(1))
                    .fluidInputs(Materials.Silver.getMolten(144))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_HV)
                    .addTo(assemblerRecipes);
                // Dynamo IV 4A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hatch_Dynamo_IV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Tungsten, 2),
                        OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 2))
                    .itemOutputs(CustomItemList.eM_dynamoMulti4_IV.get(1))
                    .fluidInputs(Materials.Silver.getMolten(144))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_EV)
                    .addTo(assemblerRecipes);
                // Dynamo LuV 4A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hatch_Dynamo_LuV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.VanadiumGallium, 2),
                        OreDictUnificator
                            .get(OrePrefixes.plate, getOrDefault("Rhodium-PlatedPalladium", Materials.Chrome), 2))
                    .itemOutputs(CustomItemList.eM_dynamoMulti4_LuV.get(1))
                    .fluidInputs(Materials.Silver.getMolten(288))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_IV)
                    .addTo(assemblerRecipes);
                // Dynamo ZPM 4A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hatch_Dynamo_ZPM.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Naquadah, 2),
                        OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 2))
                    .itemOutputs(CustomItemList.eM_dynamoMulti4_ZPM.get(1))
                    .fluidInputs(Materials.Silver.getMolten(576))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_LuV)
                    .addTo(assemblerRecipes);
                // Dynamo UV 4A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hatch_Dynamo_UV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 2),
                        OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 2))
                    .itemOutputs(CustomItemList.eM_dynamoMulti4_UV.get(1))
                    .fluidInputs(Materials.Silver.getMolten(1152))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_ZPM)
                    .addTo(assemblerRecipes);
                // Dynamo UHV 4A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hatch_Dynamo_UHV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 2),
                        OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 2))
                    .itemOutputs(CustomItemList.eM_dynamoMulti4_UHV.get(1))
                    .fluidInputs(Materials.Silver.getMolten(2304))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_UV)
                    .addTo(assemblerRecipes);
                // Dynamo UEV 4A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hatch_Dynamo_UEV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Draconium, 2),
                        OreDictUnificator.get(OrePrefixes.plate, getOrDefault("Bedrockium", Materials.Neutronium), 2))
                    .itemOutputs(CustomItemList.eM_dynamoMulti4_UEV.get(1))
                    .fluidInputs(Materials.Silver.getMolten(4608))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_UHV)
                    .addTo(assemblerRecipes);
                // Dynamo UIV 4A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hatch_Dynamo_UIV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NetherStar, 2),
                        OreDictUnificator
                            .get(OrePrefixes.plate, getOrDefault("BlackPlutonium", Materials.Neutronium), 2))
                    .itemOutputs(CustomItemList.eM_dynamoMulti4_UIV.get(1))
                    .fluidInputs(Materials.Silver.getMolten(9216))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_UEV)
                    .addTo(assemblerRecipes);
                // Dynamo UMV 4A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hatch_Dynamo_UMV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Quantium, 2),
                        OreDictUnificator.get(OrePrefixes.plate, MaterialsUEVplus.SpaceTime, 2))
                    .itemOutputs(CustomItemList.eM_dynamoMulti4_UMV.get(1))
                    .fluidInputs(Materials.Silver.getMolten(9216))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_UIV)
                    .addTo(assemblerRecipes);
                // Dynamo UXV 4A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hatch_Dynamo_UXV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.BlackPlutonium, 2),
                        OreDictUnificator
                            .get(OrePrefixes.plate, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 1),
                        OreDictUnificator.get("plateShirabon", 1L))
                    .itemOutputs(CustomItemList.eM_dynamoMulti4_UXV.get(1))
                    .fluidInputs(Materials.Silver.getMolten(9216))
                    .duration(5 * SECONDS)
                    .eut((int) TierEU.RECIPE_UMV)
                    .addTo(assemblerRecipes);

            }

            // Dynamo Hatches 16A
            {
                // Dynamo EV 16A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Transformer_IV_EV.get(1),
                        CustomItemList.eM_dynamoMulti4_EV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Aluminium, 2),
                        OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 4))
                    .itemOutputs(CustomItemList.eM_dynamoMulti16_EV.get(1))
                    .fluidInputs(Materials.Electrum.getMolten(144))
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_HV)
                    .addTo(assemblerRecipes);
                // Dynamo IV 16A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Transformer_LuV_IV.get(1),
                        CustomItemList.eM_dynamoMulti4_IV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Tungsten, 2),
                        OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 4))
                    .itemOutputs(CustomItemList.eM_dynamoMulti16_IV.get(1))
                    .fluidInputs(Materials.Electrum.getMolten(144))
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_EV)
                    .addTo(assemblerRecipes);
                // Dynamo LuV 16A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Transformer_ZPM_LuV.get(1),
                        CustomItemList.eM_dynamoMulti4_LuV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt08, Materials.VanadiumGallium, 2),
                        OreDictUnificator
                            .get(OrePrefixes.plate, getOrDefault("Rhodium-PlatedPalladium", Materials.Chrome), 4))
                    .itemOutputs(CustomItemList.eM_dynamoMulti16_LuV.get(1))
                    .fluidInputs(Materials.Electrum.getMolten(288))
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_IV)
                    .addTo(assemblerRecipes);
                // Dynamo ZPM 16A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Transformer_UV_ZPM.get(1),
                        CustomItemList.eM_dynamoMulti4_ZPM.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Naquadah, 2),
                        OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 4))
                    .itemOutputs(CustomItemList.eM_dynamoMulti16_ZPM.get(1))
                    .fluidInputs(Materials.Electrum.getMolten(576))
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_LuV)
                    .addTo(assemblerRecipes);
                // Dynamo UV 16A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Transformer_MAX_UV.get(1),
                        CustomItemList.eM_dynamoMulti4_UV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NaquadahAlloy, 2),
                        OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 4))
                    .itemOutputs(CustomItemList.eM_dynamoMulti16_UV.get(1))
                    .fluidInputs(Materials.Electrum.getMolten(1152))
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_ZPM)
                    .addTo(assemblerRecipes);
                // Dynamo UHV 16A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Transformer_UEV_UHV.get(1),
                        CustomItemList.eM_dynamoMulti4_UHV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt08, Materials.SuperconductorUHV, 2),
                        OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 4))
                    .itemOutputs(CustomItemList.eM_dynamoMulti16_UHV.get(1))
                    .fluidInputs(Materials.Electrum.getMolten(2304))
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_UV)
                    .addTo(assemblerRecipes);
                // Dynamo UEV 16A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Transformer_UIV_UEV.get(1),
                        CustomItemList.eM_dynamoMulti4_UEV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Draconium, 2),
                        OreDictUnificator.get(OrePrefixes.plate, getOrDefault("Bedrockium", Materials.Neutronium), 4))
                    .itemOutputs(CustomItemList.eM_dynamoMulti16_UEV.get(1))
                    .fluidInputs(Materials.Electrum.getMolten(4608))
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_UHV)
                    .addTo(assemblerRecipes);
                // Dynamo UIV 16A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Transformer_UMV_UIV.get(1),
                        CustomItemList.eM_dynamoMulti4_UIV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NetherStar, 2),
                        OreDictUnificator
                            .get(OrePrefixes.plate, getOrDefault("BlackPlutonium", Materials.Neutronium), 4))
                    .itemOutputs(CustomItemList.eM_dynamoMulti16_UIV.get(1))
                    .fluidInputs(Materials.Electrum.getMolten(9216))
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_UEV)
                    .addTo(assemblerRecipes);
                // Dynamo UMV 16A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Transformer_UXV_UMV.get(1),
                        CustomItemList.eM_dynamoMulti4_UMV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Quantium, 2),
                        OreDictUnificator.get(OrePrefixes.plate, MaterialsUEVplus.SpaceTime, 4))
                    .itemOutputs(CustomItemList.eM_dynamoMulti16_UMV.get(1))
                    .fluidInputs(Materials.Electrum.getMolten(9216))
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_UIV)
                    .addTo(assemblerRecipes);
                // Dynamo UXV 16A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Transformer_MAX_UXV.get(1),
                        CustomItemList.eM_dynamoMulti4_UXV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt08, Materials.BlackPlutonium, 2),
                        OreDictUnificator
                            .get(OrePrefixes.plate, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 2),
                        OreDictUnificator.get("plateShirabon", 2L))
                    .itemOutputs(CustomItemList.eM_dynamoMulti16_UXV.get(1))
                    .fluidInputs(Materials.Electrum.getMolten(9216))
                    .duration(10 * SECONDS)
                    .eut((int) TierEU.RECIPE_UMV)
                    .addTo(assemblerRecipes);

            }

            // Dynamo Hatches 64A
            {
                // Dynamo EV 64A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.WetTransformer_IV_EV.get(1),
                        CustomItemList.eM_dynamoMulti16_EV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Aluminium, 2),
                        OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 6))
                    .itemOutputs(CustomItemList.eM_dynamoMulti64_EV.get(1))
                    .fluidInputs(Materials.Tungsten.getMolten(144))
                    .duration(20 * SECONDS)
                    .eut(TierEU.RECIPE_HV)
                    .addTo(assemblerRecipes);
                // Dynamo IV 64A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.WetTransformer_LuV_IV.get(1),
                        CustomItemList.eM_dynamoMulti16_IV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Tungsten, 2),
                        OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 6))
                    .itemOutputs(CustomItemList.eM_dynamoMulti64_IV.get(1))
                    .fluidInputs(Materials.Tungsten.getMolten(144))
                    .duration(20 * SECONDS)
                    .eut(TierEU.RECIPE_EV)
                    .addTo(assemblerRecipes);
                // Dynamo LuV 64A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.WetTransformer_ZPM_LuV.get(1),
                        CustomItemList.eM_dynamoMulti16_LuV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt12, Materials.VanadiumGallium, 2),
                        OreDictUnificator
                            .get(OrePrefixes.plate, getOrDefault("Rhodium-PlatedPalladium", Materials.Chrome), 6))
                    .itemOutputs(CustomItemList.eM_dynamoMulti64_LuV.get(1))
                    .fluidInputs(Materials.Tungsten.getMolten(288))
                    .duration(20 * SECONDS)
                    .eut(TierEU.RECIPE_IV)
                    .addTo(assemblerRecipes);
                // Dynamo ZPM 64A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.WetTransformer_UV_ZPM.get(1),
                        CustomItemList.eM_dynamoMulti16_ZPM.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Naquadah, 2),
                        OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 6))
                    .itemOutputs(CustomItemList.eM_dynamoMulti64_ZPM.get(1))
                    .fluidInputs(Materials.Tungsten.getMolten(576))
                    .duration(20 * SECONDS)
                    .eut(TierEU.RECIPE_LuV)
                    .addTo(assemblerRecipes);
                // Dynamo UV 64A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.WetTransformer_UHV_UV.get(1),
                        CustomItemList.eM_dynamoMulti16_UV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt12, Materials.NaquadahAlloy, 2),
                        OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 6))
                    .itemOutputs(CustomItemList.eM_dynamoMulti64_UV.get(1))
                    .fluidInputs(Materials.Tungsten.getMolten(1152))
                    .duration(20 * SECONDS)
                    .eut(TierEU.RECIPE_ZPM)
                    .addTo(assemblerRecipes);
                // Dynamo UHV 64A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.WetTransformer_UEV_UHV.get(1),
                        CustomItemList.eM_dynamoMulti16_UHV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt12, Materials.SuperconductorUHV, 2),
                        OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 6))
                    .itemOutputs(CustomItemList.eM_dynamoMulti64_UHV.get(1))
                    .fluidInputs(Materials.Tungsten.getMolten(2304))
                    .duration(20 * SECONDS)
                    .eut(TierEU.RECIPE_UHV)
                    .addTo(assemblerRecipes);
                // Dynamo UEV 64A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.WetTransformer_UIV_UEV.get(1),
                        CustomItemList.eM_dynamoMulti16_UEV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Draconium, 2),
                        OreDictUnificator.get(OrePrefixes.plate, getOrDefault("Bedrockium", Materials.Neutronium), 6))
                    .itemOutputs(CustomItemList.eM_dynamoMulti64_UEV.get(1))
                    .fluidInputs(Materials.Tungsten.getMolten(4608))
                    .duration(20 * SECONDS)
                    .eut(TierEU.RECIPE_UHV)
                    .addTo(assemblerRecipes);
                // Dynamo UIV 64A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.WetTransformer_UMV_UIV.get(1),
                        CustomItemList.eM_dynamoMulti16_UIV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt12, Materials.NetherStar, 2),
                        OreDictUnificator
                            .get(OrePrefixes.plate, getOrDefault("BlackPlutonium", Materials.Neutronium), 6))
                    .itemOutputs(CustomItemList.eM_dynamoMulti64_UIV.get(1))
                    .fluidInputs(Materials.Tungsten.getMolten(9216))
                    .duration(20 * SECONDS)
                    .eut(TierEU.RECIPE_UEV)
                    .addTo(assemblerRecipes);
                // Dynamo UMV 64A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.WetTransformer_UXV_UMV.get(1),
                        CustomItemList.eM_dynamoMulti16_UMV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Quantium, 2),
                        OreDictUnificator.get(OrePrefixes.plate, MaterialsUEVplus.SpaceTime, 6))
                    .itemOutputs(CustomItemList.eM_dynamoMulti64_UMV.get(1))
                    .fluidInputs(Materials.Tungsten.getMolten(9216))
                    .duration(20 * SECONDS)
                    .eut(TierEU.RECIPE_UIV)
                    .addTo(assemblerRecipes);
                // Dynamo UXV 64A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.WetTransformer_MAX_UXV.get(1),
                        CustomItemList.eM_dynamoMulti16_UXV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt12, Materials.BlackPlutonium, 2),
                        OreDictUnificator
                            .get(OrePrefixes.plate, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 3),
                        OreDictUnificator.get("plateShirabon", 3L))
                    .itemOutputs(CustomItemList.eM_dynamoMulti64_UXV.get(1))
                    .fluidInputs(Materials.Tungsten.getMolten(9216))
                    .duration(20 * SECONDS)
                    .eut((int) TierEU.RECIPE_UMV)
                    .addTo(assemblerRecipes);

            }
        }

        // Energy Hatches
        {
            // Energy Hatches 4A
            {
                // Energy Hatch EV 4A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hatch_Energy_EV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Aluminium, 2),
                        OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 2))
                    .itemOutputs(CustomItemList.eM_energyMulti4_EV.get(1))
                    .fluidInputs(Materials.Silver.getMolten(144))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_HV)
                    .addTo(assemblerRecipes);
                // Energy Hatch IV 4A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hatch_Energy_IV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Tungsten, 2),
                        OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 2))
                    .itemOutputs(CustomItemList.eM_energyMulti4_IV.get(1))
                    .fluidInputs(Materials.Silver.getMolten(144))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_EV)
                    .addTo(assemblerRecipes);
                // Energy Hatch LuV 4A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hatch_Energy_LuV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.VanadiumGallium, 2),
                        OreDictUnificator
                            .get(OrePrefixes.plate, getOrDefault("Rhodium-PlatedPalladium", Materials.Chrome), 2))
                    .itemOutputs(CustomItemList.eM_energyMulti4_LuV.get(1))
                    .fluidInputs(Materials.Silver.getMolten(288))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_IV)
                    .addTo(assemblerRecipes);
                // Energy Hatch ZPM 4A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hatch_Energy_ZPM.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Naquadah, 2),
                        OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 2))
                    .itemOutputs(CustomItemList.eM_energyMulti4_ZPM.get(1))
                    .fluidInputs(Materials.Silver.getMolten(576))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_LuV)
                    .addTo(assemblerRecipes);
                // Energy Hatch UV 4A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hatch_Energy_UV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 2),
                        OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 2))
                    .itemOutputs(CustomItemList.eM_energyMulti4_UV.get(1))
                    .fluidInputs(Materials.Silver.getMolten(1152))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_ZPM)
                    .addTo(assemblerRecipes);
                // Energy Hatch UHV 4A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hatch_Energy_UHV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 2),
                        OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 2))
                    .itemOutputs(CustomItemList.eM_energyMulti4_UHV.get(1))
                    .fluidInputs(Materials.Silver.getMolten(2304))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_UV)
                    .addTo(assemblerRecipes);
                // Energy Hatch UEV 4A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hatch_Energy_UEV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Draconium, 2),
                        OreDictUnificator.get(OrePrefixes.plate, getOrDefault("Bedrockium", Materials.Neutronium), 2))
                    .itemOutputs(CustomItemList.eM_energyMulti4_UEV.get(1))
                    .fluidInputs(Materials.Silver.getMolten(4608))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_UHV)
                    .addTo(assemblerRecipes);
                // Energy Hatch UIV 4A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hatch_Energy_UIV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NetherStar, 2),
                        OreDictUnificator
                            .get(OrePrefixes.plate, getOrDefault("BlackPlutonium", Materials.Neutronium), 2))
                    .itemOutputs(CustomItemList.eM_energyMulti4_UIV.get(1))
                    .fluidInputs(Materials.Silver.getMolten(9216))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_UEV)
                    .addTo(assemblerRecipes);
                // Energy Hatch UMV 4A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hatch_Energy_UMV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Quantium, 2),
                        OreDictUnificator.get(OrePrefixes.plate, MaterialsUEVplus.SpaceTime, 2))
                    .itemOutputs(CustomItemList.eM_energyMulti4_UMV.get(1))
                    .fluidInputs(Materials.Silver.getMolten(9216))
                    .duration(5 * SECONDS)
                    .eut(TierEU.RECIPE_UIV)
                    .addTo(assemblerRecipes);
                // Energy Hatch UXV 4A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hatch_Energy_UXV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.BlackPlutonium, 2),
                        OreDictUnificator
                            .get(OrePrefixes.plate, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 1),
                        OreDictUnificator.get("plateShirabon", 1L))
                    .itemOutputs(CustomItemList.eM_energyMulti4_UXV.get(1))
                    .fluidInputs(Materials.Silver.getMolten(9216))
                    .duration(5 * SECONDS)
                    .eut((int) TierEU.RECIPE_UMV)
                    .addTo(assemblerRecipes);

            }

            // Energy Hatches 16A
            {
                // Energy Hatch EV 16A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Transformer_IV_EV.get(1),
                        CustomItemList.eM_energyMulti4_EV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Aluminium, 2),
                        OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 4))
                    .itemOutputs(CustomItemList.eM_energyMulti16_EV.get(1))
                    .fluidInputs(Materials.Electrum.getMolten(144))
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_HV)
                    .addTo(assemblerRecipes);
                // Energy Hatch IV 16A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Transformer_LuV_IV.get(1),
                        CustomItemList.eM_energyMulti4_IV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Tungsten, 2),
                        OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 4))
                    .itemOutputs(CustomItemList.eM_energyMulti16_IV.get(1))
                    .fluidInputs(Materials.Electrum.getMolten(144))
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_EV)
                    .addTo(assemblerRecipes);
                // Energy Hatch LuV 16A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Transformer_ZPM_LuV.get(1),
                        CustomItemList.eM_energyMulti4_LuV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt08, Materials.VanadiumGallium, 2),
                        OreDictUnificator
                            .get(OrePrefixes.plate, getOrDefault("Rhodium-PlatedPalladium", Materials.Chrome), 4))
                    .itemOutputs(CustomItemList.eM_energyMulti16_LuV.get(1))
                    .fluidInputs(Materials.Electrum.getMolten(288))
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_IV)
                    .addTo(assemblerRecipes);
                // Energy Hatch ZPM 16A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Transformer_UV_ZPM.get(1),
                        CustomItemList.eM_energyMulti4_ZPM.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Naquadah, 2),
                        OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 4))
                    .itemOutputs(CustomItemList.eM_energyMulti16_ZPM.get(1))
                    .fluidInputs(Materials.Electrum.getMolten(576))
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_LuV)
                    .addTo(assemblerRecipes);
                // Energy Hatch UV 16A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Transformer_MAX_UV.get(1),
                        CustomItemList.eM_energyMulti4_UV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NaquadahAlloy, 2),
                        OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 4))
                    .itemOutputs(CustomItemList.eM_energyMulti16_UV.get(1))
                    .fluidInputs(Materials.Electrum.getMolten(1152))
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_ZPM)
                    .addTo(assemblerRecipes);
                // Energy Hatch UHV 16A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Transformer_UEV_UHV.get(1),
                        CustomItemList.eM_energyMulti4_UHV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt08, Materials.SuperconductorUHV, 2),
                        OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 4))
                    .itemOutputs(CustomItemList.eM_energyMulti16_UHV.get(1))
                    .fluidInputs(Materials.Electrum.getMolten(2304))
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_UV)
                    .addTo(assemblerRecipes);
                // Energy Hatch UEV 16A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Transformer_UIV_UEV.get(1),
                        CustomItemList.eM_energyMulti4_UEV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Draconium, 2),
                        OreDictUnificator.get(OrePrefixes.plate, getOrDefault("Bedrockium", Materials.Neutronium), 4))
                    .itemOutputs(CustomItemList.eM_energyMulti16_UEV.get(1))
                    .fluidInputs(Materials.Electrum.getMolten(4608))
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_UHV)
                    .addTo(assemblerRecipes);
                // Energy Hatch UIV 16A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Transformer_UMV_UIV.get(1),
                        CustomItemList.eM_energyMulti4_UIV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NetherStar, 2),
                        OreDictUnificator
                            .get(OrePrefixes.plate, getOrDefault("BlackPlutonium", Materials.Neutronium), 4))
                    .itemOutputs(CustomItemList.eM_energyMulti16_UIV.get(1))
                    .fluidInputs(Materials.Electrum.getMolten(9216))
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_UEV)
                    .addTo(assemblerRecipes);
                // Energy Hatch UMV 16A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Transformer_UXV_UMV.get(1),
                        CustomItemList.eM_energyMulti4_UMV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Quantium, 2),
                        OreDictUnificator.get(OrePrefixes.plate, MaterialsUEVplus.SpaceTime, 4))
                    .itemOutputs(CustomItemList.eM_energyMulti16_UMV.get(1))
                    .fluidInputs(Materials.Electrum.getMolten(9216))
                    .duration(10 * SECONDS)
                    .eut(TierEU.RECIPE_UIV)
                    .addTo(assemblerRecipes);
                // Energy Hatch UXV 16A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Transformer_MAX_UXV.get(1),
                        CustomItemList.eM_energyMulti4_UXV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt08, Materials.BlackPlutonium, 2),
                        OreDictUnificator
                            .get(OrePrefixes.plate, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 2),
                        OreDictUnificator.get("plateShirabon", 2L))
                    .itemOutputs(CustomItemList.eM_energyMulti16_UXV.get(1))
                    .fluidInputs(Materials.Electrum.getMolten(9216))
                    .duration(10 * SECONDS)
                    .eut((int) TierEU.RECIPE_UMV)
                    .addTo(assemblerRecipes);

            }

            // Energy Hatches 64A
            {
                // Energy Hatch EV 64A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.WetTransformer_IV_EV.get(1),
                        CustomItemList.eM_energyMulti16_EV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Aluminium, 2),
                        OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 6))
                    .itemOutputs(CustomItemList.eM_energyMulti64_EV.get(1))
                    .fluidInputs(Materials.Tungsten.getMolten(144))
                    .duration(20 * SECONDS)
                    .eut(TierEU.RECIPE_HV)
                    .addTo(assemblerRecipes);
                // Energy Hatch IV 64A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.WetTransformer_LuV_IV.get(1),
                        CustomItemList.eM_energyMulti16_IV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Tungsten, 2),
                        OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 6))
                    .itemOutputs(CustomItemList.eM_energyMulti64_IV.get(1))
                    .fluidInputs(Materials.Tungsten.getMolten(144))
                    .duration(20 * SECONDS)
                    .eut(TierEU.RECIPE_EV)
                    .addTo(assemblerRecipes);
                // Energy Hatch LuV 64A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.WetTransformer_ZPM_LuV.get(1),
                        CustomItemList.eM_energyMulti16_LuV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt12, Materials.VanadiumGallium, 2),
                        OreDictUnificator
                            .get(OrePrefixes.plate, getOrDefault("Rhodium-PlatedPalladium", Materials.Chrome), 6))
                    .itemOutputs(CustomItemList.eM_energyMulti64_LuV.get(1))
                    .fluidInputs(Materials.Tungsten.getMolten(288))
                    .duration(20 * SECONDS)
                    .eut(TierEU.RECIPE_IV)
                    .addTo(assemblerRecipes);
                // Energy Hatch ZPM 64A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.WetTransformer_UV_ZPM.get(1),
                        CustomItemList.eM_energyMulti16_ZPM.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Naquadah, 2),
                        OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 6))
                    .itemOutputs(CustomItemList.eM_energyMulti64_ZPM.get(1))
                    .fluidInputs(Materials.Tungsten.getMolten(576))
                    .duration(20 * SECONDS)
                    .eut(TierEU.RECIPE_LuV)
                    .addTo(assemblerRecipes);
                // Energy Hatch UV 64A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.WetTransformer_UHV_UV.get(1),
                        CustomItemList.eM_energyMulti16_UV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt12, Materials.NaquadahAlloy, 2),
                        OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 6))
                    .itemOutputs(CustomItemList.eM_energyMulti64_UV.get(1))
                    .fluidInputs(Materials.Tungsten.getMolten(1152))
                    .duration(20 * SECONDS)
                    .eut(TierEU.RECIPE_ZPM)
                    .addTo(assemblerRecipes);
                // Energy Hatch UHV 64A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.WetTransformer_UEV_UHV.get(1),
                        CustomItemList.eM_energyMulti16_UHV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt12, Materials.SuperconductorUHV, 2),
                        OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 6))
                    .itemOutputs(CustomItemList.eM_energyMulti64_UHV.get(1))
                    .fluidInputs(Materials.Tungsten.getMolten(2304))
                    .duration(20 * SECONDS)
                    .eut(TierEU.RECIPE_UV)
                    .addTo(assemblerRecipes);
                // Energy Hatch UEV 64A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.WetTransformer_UIV_UEV.get(1),
                        CustomItemList.eM_energyMulti16_UEV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Draconium, 2),
                        OreDictUnificator.get(OrePrefixes.plate, getOrDefault("Bedrockium", Materials.Neutronium), 6))
                    .itemOutputs(CustomItemList.eM_energyMulti64_UEV.get(1))
                    .fluidInputs(Materials.Tungsten.getMolten(4608))
                    .duration(20 * SECONDS)
                    .eut(TierEU.RECIPE_UHV)
                    .addTo(assemblerRecipes);
                // Energy Hatch UIV 64A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.WetTransformer_UMV_UIV.get(1),
                        CustomItemList.eM_energyMulti16_UIV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt12, Materials.NetherStar, 2),
                        OreDictUnificator
                            .get(OrePrefixes.plate, getOrDefault("BlackPlutonium", Materials.Neutronium), 6))
                    .itemOutputs(CustomItemList.eM_energyMulti64_UIV.get(1))
                    .fluidInputs(Materials.Tungsten.getMolten(9216))
                    .duration(20 * SECONDS)
                    .eut(TierEU.RECIPE_UEV)
                    .addTo(assemblerRecipes);
                // Energy Hatch UMV 64A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.WetTransformer_UXV_UMV.get(1),
                        CustomItemList.eM_energyMulti16_UMV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Quantium, 2),
                        OreDictUnificator.get(OrePrefixes.plate, MaterialsUEVplus.SpaceTime, 6))
                    .itemOutputs(CustomItemList.eM_energyMulti64_UMV.get(1))
                    .fluidInputs(Materials.Tungsten.getMolten(9216))
                    .duration(20 * SECONDS)
                    .eut(TierEU.RECIPE_UIV)
                    .addTo(assemblerRecipes);
                // Energy Hatch UXV 64A
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.WetTransformer_MAX_UXV.get(1),
                        CustomItemList.eM_energyMulti16_UXV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt12, Materials.BlackPlutonium, 2),
                        OreDictUnificator
                            .get(OrePrefixes.plate, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 3),
                        OreDictUnificator.get("plateShirabon", 3L))
                    .itemOutputs(CustomItemList.eM_energyMulti64_UXV.get(1))
                    .fluidInputs(Materials.Tungsten.getMolten(9216))
                    .duration(20 * SECONDS)
                    .eut((int) TierEU.RECIPE_UMV)
                    .addTo(assemblerRecipes);

            }
        }

        // Buck Converter IV-UIV
        // Buck Converter IV
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Transformer_LuV_IV.get(1),
                getItemContainer("Display").get(1),
                OreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 2),
                OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 2),
                OreDictUnificator.get(OrePrefixes.wireGt16, Materials.TungstenSteel, 4),
                new ItemStack(bw_realglas, 2, 2))
            .itemOutputs(CustomItemList.Machine_BuckConverter_IV.get(1))
            .fluidInputs(Materials.TungstenSteel.getMolten(288))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        // Buck Converter LuV
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Transformer_ZPM_LuV.get(1),
                getItemContainer("Display").get(1),
                OreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 2),
                OreDictUnificator.get(OrePrefixes.plate, getOrDefault("Rhodium-PlatedPalladium", Materials.Chrome), 2),
                OreDictUnificator.get(OrePrefixes.wireGt16, Materials.NiobiumTitanium, 4),
                new ItemStack(bw_realglas, 2, 3))
            .itemOutputs(CustomItemList.Machine_BuckConverter_LuV.get(1))
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("molten.rhodium-plated palladium"), 288))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);
        // Buck Converter ZPM
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Transformer_UV_ZPM.get(1),
                getItemContainer("Display").get(1),
                OreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 2),
                OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 2),
                OreDictUnificator.get(OrePrefixes.wireGt16, Materials.TungstenSteel, 4),
                new ItemStack(bw_realglas, 2, 4))
            .itemOutputs(CustomItemList.Machine_BuckConverter_ZPM.get(1))
            .fluidInputs(Materials.Iridium.getMolten(288))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);
        // Buck Converter UV
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Transformer_MAX_UV.get(1),
                getItemContainer("Display").get(1),
                OreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 2),
                OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 2),
                OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Naquadah, 4),
                new ItemStack(bw_realglas, 2, 5))
            .itemOutputs(CustomItemList.Machine_BuckConverter_UV.get(1))
            .fluidInputs(Materials.Osmium.getMolten(288))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(assemblerRecipes);
        // Buck Converter UHV
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Transformer_UEV_UHV.get(1),
                getItemContainer("Display").get(1),
                OreDictUnificator.get(OrePrefixes.circuit, Materials.UHV, 2),
                OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 2),
                OreDictUnificator.get(OrePrefixes.wireGt16, Materials.ElectrumFlux, 4),
                new ItemStack(bw_realglas, 4, 5))
            .itemOutputs(CustomItemList.Machine_BuckConverter_UHV.get(1))
            .fluidInputs(Materials.Neutronium.getMolten(288))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(assemblerRecipes);
        // Buck Converter UEV
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Transformer_UIV_UEV.get(1),
                getItemContainer("Display").get(1),
                OreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 2),
                OreDictUnificator.get(OrePrefixes.plate, getOrDefault("Bedrockium", Materials.Neutronium), 2),
                OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Bedrockium, 4),
                new ItemStack(bw_realglas, 8, 5))
            .itemOutputs(CustomItemList.Machine_BuckConverter_UEV.get(1))
            .fluidInputs(getOrDefault("Bedrockium", Materials.Neutronium).getMolten(288))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(assemblerRecipes);
        // Buck Converter UIV
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Transformer_UMV_UIV.get(1),
                getItemContainer("Display").get(1),
                OreDictUnificator.get(OrePrefixes.circuit, Materials.UIV, 2),
                OreDictUnificator.get(OrePrefixes.plate, getOrDefault("BlackPlutonium", Materials.Neutronium), 2),
                OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Draconium, 4),
                new ItemStack(bw_realglas, 16, 5))
            .itemOutputs(CustomItemList.Machine_BuckConverter_UIV.get(1))
            .fluidInputs(getOrDefault("BlackPlutonium", Materials.Neutronium).getMolten(288))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(assemblerRecipes);

        // Laser Dynamo
        {
            // Laser Dynamo IV-UXV 256/t
            {
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_IV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1),
                        ItemList.Emitter_IV.get(1),
                        ItemList.Electric_Pump_IV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt01, Materials.TungstenSteel, 2),
                        GT_Utility.getIntegratedCircuit(1))
                    .itemOutputs(CustomItemList.eM_dynamoTunnel1_IV.get(1))
                    .duration(50 * SECONDS)
                    .eut(TierEU.RECIPE_IV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_LuV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1),
                        ItemList.Emitter_LuV.get(1),
                        ItemList.Electric_Pump_LuV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt01, Materials.VanadiumGallium, 2),
                        GT_Utility.getIntegratedCircuit(1))
                    .itemOutputs(CustomItemList.eM_dynamoTunnel1_LuV.get(1))
                    .duration(50 * SECONDS)
                    .eut(TierEU.RECIPE_LuV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_ZPM.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1),
                        ItemList.Emitter_ZPM.get(1),
                        ItemList.Electric_Pump_ZPM.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Naquadah, 2),
                        GT_Utility.getIntegratedCircuit(1))
                    .itemOutputs(CustomItemList.eM_dynamoTunnel1_ZPM.get(1))
                    .duration(50 * SECONDS)
                    .eut(TierEU.RECIPE_ZPM)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1),
                        ItemList.Emitter_UV.get(1),
                        ItemList.Electric_Pump_UV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt01, Materials.NaquadahAlloy, 2),
                        GT_Utility.getIntegratedCircuit(1))
                    .itemOutputs(CustomItemList.eM_dynamoTunnel1_UV.get(1))
                    .duration(50 * SECONDS)
                    .eut(TierEU.RECIPE_UV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_MAX.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1),
                        ItemList.Emitter_UHV.get(1),
                        ItemList.Electric_Pump_UHV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Bedrockium, 2),
                        GT_Utility.getIntegratedCircuit(1))
                    .itemOutputs(CustomItemList.eM_dynamoTunnel1_UHV.get(1))
                    .duration(50 * SECONDS)
                    .eut(TierEU.RECIPE_UHV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UEV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1),
                        ItemList.Emitter_UEV.get(1),
                        ItemList.Electric_Pump_UEV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Draconium, 2),
                        GT_Utility.getIntegratedCircuit(1))
                    .itemOutputs(CustomItemList.eM_dynamoTunnel1_UEV.get(1))
                    .duration(50 * SECONDS)
                    .eut(TierEU.RECIPE_UEV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UIV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1),
                        ItemList.Emitter_UIV.get(1),
                        ItemList.Electric_Pump_UIV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt01, Materials.NetherStar, 2),
                        GT_Utility.getIntegratedCircuit(1))
                    .itemOutputs(CustomItemList.eM_dynamoTunnel1_UIV.get(1))
                    .duration(50 * SECONDS)
                    .eut(TierEU.RECIPE_UIV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UMV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1),
                        ItemList.Emitter_UMV.get(1),
                        ItemList.Electric_Pump_UMV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Quantium, 2),
                        GT_Utility.getIntegratedCircuit(1))
                    .itemOutputs(CustomItemList.eM_dynamoTunnel1_UMV.get(1))
                    .duration(50 * SECONDS)
                    .eut(TierEU.RECIPE_UMV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UXV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1),
                        ItemList.Emitter_UXV.get(1),
                        ItemList.Electric_Pump_UXV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt01, Materials.BlackPlutonium, 2),
                        GT_Utility.getIntegratedCircuit(1))
                    .itemOutputs(CustomItemList.eM_dynamoTunnel1_UXV.get(1))
                    .duration(50 * SECONDS)
                    .eut(TierEU.RECIPE_UXV)
                    .addTo(assemblerRecipes);

            }

            // Laser Dynamo LuV-UXV 1024/t
            {
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_LuV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2),
                        ItemList.Emitter_LuV.get(2),
                        ItemList.Electric_Pump_LuV.get(2),
                        OreDictUnificator.get(OrePrefixes.wireGt02, Materials.VanadiumGallium, 4),
                        GT_Utility.getIntegratedCircuit(2))
                    .itemOutputs(CustomItemList.eM_dynamoTunnel2_LuV.get(1))
                    .duration(1 * MINUTES + 40 * SECONDS)
                    .eut(TierEU.RECIPE_LuV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_ZPM.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2),
                        ItemList.Emitter_ZPM.get(2),
                        ItemList.Electric_Pump_ZPM.get(2),
                        OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Naquadah, 4),
                        GT_Utility.getIntegratedCircuit(2))
                    .itemOutputs(CustomItemList.eM_dynamoTunnel2_ZPM.get(1))
                    .duration(1 * MINUTES + 40 * SECONDS)
                    .eut(TierEU.RECIPE_ZPM)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2),
                        ItemList.Emitter_UV.get(2),
                        ItemList.Electric_Pump_UV.get(2),
                        OreDictUnificator.get(OrePrefixes.wireGt02, Materials.NaquadahAlloy, 4),
                        GT_Utility.getIntegratedCircuit(2))
                    .itemOutputs(CustomItemList.eM_dynamoTunnel2_UV.get(1))
                    .duration(1 * MINUTES + 40 * SECONDS)
                    .eut(TierEU.RECIPE_UV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_MAX.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2),
                        ItemList.Emitter_UHV.get(2),
                        ItemList.Electric_Pump_UHV.get(2),
                        OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Bedrockium, 4),
                        GT_Utility.getIntegratedCircuit(2))
                    .itemOutputs(CustomItemList.eM_dynamoTunnel2_UHV.get(1))
                    .duration(1 * MINUTES + 40 * SECONDS)
                    .eut(TierEU.RECIPE_UHV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UEV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2),
                        ItemList.Emitter_UEV.get(2),
                        ItemList.Electric_Pump_UEV.get(2),
                        OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Draconium, 4),
                        GT_Utility.getIntegratedCircuit(2))
                    .itemOutputs(CustomItemList.eM_dynamoTunnel2_UEV.get(1))
                    .duration(1 * MINUTES + 40 * SECONDS)
                    .eut(TierEU.RECIPE_UEV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UIV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2),
                        ItemList.Emitter_UIV.get(2),
                        ItemList.Electric_Pump_UIV.get(2),
                        OreDictUnificator.get(OrePrefixes.wireGt02, Materials.NetherStar, 4),
                        GT_Utility.getIntegratedCircuit(2))
                    .itemOutputs(CustomItemList.eM_dynamoTunnel2_UIV.get(1))
                    .duration(1 * MINUTES + 40 * SECONDS)
                    .eut(TierEU.RECIPE_UIV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UMV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2),
                        ItemList.Emitter_UMV.get(2),
                        ItemList.Electric_Pump_UMV.get(2),
                        OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Quantium, 4),
                        GT_Utility.getIntegratedCircuit(2))
                    .itemOutputs(CustomItemList.eM_dynamoTunnel2_UMV.get(1))
                    .duration(1 * MINUTES + 40 * SECONDS)
                    .eut(TierEU.RECIPE_UMV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UXV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2),
                        ItemList.Emitter_UXV.get(2),
                        ItemList.Electric_Pump_UXV.get(2),
                        OreDictUnificator.get(OrePrefixes.wireGt02, Materials.BlackPlutonium, 4),
                        GT_Utility.getIntegratedCircuit(2))
                    .itemOutputs(CustomItemList.eM_dynamoTunnel2_UXV.get(1))
                    .duration(1 * MINUTES + 40 * SECONDS)
                    .eut(TierEU.RECIPE_UXV)
                    .addTo(assemblerRecipes);

            }

            // Laser Dynamo ZPM-UXV 4096/t
            {
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_ZPM.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4),
                        ItemList.Emitter_ZPM.get(4),
                        ItemList.Electric_Pump_ZPM.get(4),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Naquadah, 4),
                        GT_Utility.getIntegratedCircuit(3))
                    .itemOutputs(CustomItemList.eM_dynamoTunnel3_ZPM.get(1))
                    .duration(3 * MINUTES + 20 * SECONDS)
                    .eut(TierEU.RECIPE_ZPM)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4),
                        ItemList.Emitter_UV.get(4),
                        ItemList.Electric_Pump_UV.get(4),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 4),
                        GT_Utility.getIntegratedCircuit(3))
                    .itemOutputs(CustomItemList.eM_dynamoTunnel3_UV.get(1))
                    .duration(3 * MINUTES + 20 * SECONDS)
                    .eut(TierEU.RECIPE_UV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_MAX.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4),
                        ItemList.Emitter_UHV.get(4),
                        ItemList.Electric_Pump_UHV.get(4),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Bedrockium, 4),
                        GT_Utility.getIntegratedCircuit(3))
                    .itemOutputs(CustomItemList.eM_dynamoTunnel3_UHV.get(1))
                    .duration(3 * MINUTES + 20 * SECONDS)
                    .eut(TierEU.RECIPE_UHV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UEV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4),
                        ItemList.Emitter_UEV.get(4),
                        ItemList.Electric_Pump_UEV.get(4),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Draconium, 4),
                        GT_Utility.getIntegratedCircuit(3))
                    .itemOutputs(CustomItemList.eM_dynamoTunnel3_UEV.get(1))
                    .duration(3 * MINUTES + 20 * SECONDS)
                    .eut(TierEU.RECIPE_UEV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UIV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4),
                        ItemList.Emitter_UIV.get(4),
                        ItemList.Electric_Pump_UIV.get(4),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NetherStar, 4),
                        GT_Utility.getIntegratedCircuit(3))
                    .itemOutputs(CustomItemList.eM_dynamoTunnel3_UIV.get(1))
                    .duration(3 * MINUTES + 20 * SECONDS)
                    .eut(TierEU.RECIPE_UIV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UMV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4),
                        ItemList.Emitter_UMV.get(4),
                        ItemList.Electric_Pump_UMV.get(4),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Quantium, 4),
                        GT_Utility.getIntegratedCircuit(3))
                    .itemOutputs(CustomItemList.eM_dynamoTunnel3_UMV.get(1))
                    .duration(3 * MINUTES + 20 * SECONDS)
                    .eut(TierEU.RECIPE_UMV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UXV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4),
                        ItemList.Emitter_UXV.get(4),
                        ItemList.Electric_Pump_UXV.get(4),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.BlackPlutonium, 4),
                        GT_Utility.getIntegratedCircuit(3))
                    .itemOutputs(CustomItemList.eM_dynamoTunnel3_UXV.get(1))
                    .duration(3 * MINUTES + 20 * SECONDS)
                    .eut(TierEU.RECIPE_UXV)
                    .addTo(assemblerRecipes);

            }

            // Laser Dynamo UV-UXV 16384/t
            {
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8),
                        ItemList.Emitter_UV.get(8),
                        ItemList.Electric_Pump_UV.get(8),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 8),
                        GT_Utility.getIntegratedCircuit(4))
                    .itemOutputs(CustomItemList.eM_dynamoTunnel4_UV.get(1))
                    .duration(6 * MINUTES + 40 * SECONDS)
                    .eut(TierEU.RECIPE_UV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_MAX.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8),
                        ItemList.Emitter_UHV.get(8),
                        ItemList.Electric_Pump_UHV.get(8),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Bedrockium, 8),
                        GT_Utility.getIntegratedCircuit(4))
                    .itemOutputs(CustomItemList.eM_dynamoTunnel4_UHV.get(1))
                    .duration(6 * MINUTES + 40 * SECONDS)
                    .eut(TierEU.RECIPE_UHV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UEV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8),
                        ItemList.Emitter_UEV.get(8),
                        ItemList.Electric_Pump_UEV.get(8),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Draconium, 8),
                        GT_Utility.getIntegratedCircuit(4))
                    .itemOutputs(CustomItemList.eM_dynamoTunnel4_UEV.get(1))
                    .duration(6 * MINUTES + 40 * SECONDS)
                    .eut(TierEU.RECIPE_UEV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UIV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8),
                        ItemList.Emitter_UIV.get(8),
                        ItemList.Electric_Pump_UIV.get(8),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NetherStar, 8),
                        GT_Utility.getIntegratedCircuit(4))
                    .itemOutputs(CustomItemList.eM_dynamoTunnel4_UIV.get(1))
                    .duration(6 * MINUTES + 40 * SECONDS)
                    .eut(TierEU.RECIPE_UIV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UMV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4),
                        ItemList.Emitter_UMV.get(8),
                        ItemList.Electric_Pump_UMV.get(8),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Quantium, 8),
                        GT_Utility.getIntegratedCircuit(4))
                    .itemOutputs(CustomItemList.eM_dynamoTunnel4_UMV.get(1))
                    .duration(6 * MINUTES + 40 * SECONDS)
                    .eut(TierEU.RECIPE_UMV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UXV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4),
                        ItemList.Emitter_UXV.get(8),
                        ItemList.Electric_Pump_UXV.get(8),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.BlackPlutonium, 8),
                        GT_Utility.getIntegratedCircuit(4))
                    .itemOutputs(CustomItemList.eM_dynamoTunnel4_UXV.get(1))
                    .duration(6 * MINUTES + 40 * SECONDS)
                    .eut(TierEU.RECIPE_UXV)
                    .addTo(assemblerRecipes);

            }

            // Laser Dynamo UHV-UXV 65536/t
            {
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_MAX.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16),
                        ItemList.Emitter_UHV.get(16),
                        ItemList.Electric_Pump_UHV.get(16),
                        OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Bedrockium, 8),
                        GT_Utility.getIntegratedCircuit(5))
                    .itemOutputs(CustomItemList.eM_dynamoTunnel5_UHV.get(1))
                    .duration(13 * MINUTES + 20 * SECONDS)
                    .eut(TierEU.RECIPE_UHV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UEV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16),
                        ItemList.Emitter_UEV.get(16),
                        ItemList.Electric_Pump_UEV.get(16),
                        OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Draconium, 8),
                        GT_Utility.getIntegratedCircuit(5))
                    .itemOutputs(CustomItemList.eM_dynamoTunnel5_UEV.get(1))
                    .duration(13 * MINUTES + 20 * SECONDS)
                    .eut(TierEU.RECIPE_UEV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UIV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16),
                        ItemList.Emitter_UIV.get(16),
                        ItemList.Electric_Pump_UIV.get(16),
                        OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NetherStar, 8),
                        GT_Utility.getIntegratedCircuit(5))
                    .itemOutputs(CustomItemList.eM_dynamoTunnel5_UIV.get(1))
                    .duration(13 * MINUTES + 20 * SECONDS)
                    .eut(TierEU.RECIPE_UIV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UMV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16),
                        ItemList.Emitter_UMV.get(16),
                        ItemList.Electric_Pump_UMV.get(16),
                        OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Quantium, 8),
                        GT_Utility.getIntegratedCircuit(5))
                    .itemOutputs(CustomItemList.eM_dynamoTunnel5_UMV.get(1))
                    .duration(13 * MINUTES + 20 * SECONDS)
                    .eut(TierEU.RECIPE_UMV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UXV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16),
                        ItemList.Emitter_UXV.get(16),
                        ItemList.Electric_Pump_UXV.get(16),
                        OreDictUnificator.get(OrePrefixes.wireGt08, Materials.BlackPlutonium, 8),
                        GT_Utility.getIntegratedCircuit(5))
                    .itemOutputs(CustomItemList.eM_dynamoTunnel5_UXV.get(1))
                    .duration(13 * MINUTES + 20 * SECONDS)
                    .eut(TierEU.RECIPE_UXV)
                    .addTo(assemblerRecipes);

            }

            // Laser Dynamo UEV-UXV 262144/t
            {
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UEV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32),
                        ItemList.Emitter_UEV.get(32),
                        ItemList.Electric_Pump_UEV.get(32),
                        OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Draconium, 16),
                        GT_Utility.getIntegratedCircuit(6))
                    .itemOutputs(CustomItemList.eM_dynamoTunnel6_UEV.get(1))
                    .duration(26 * MINUTES + 40 * SECONDS)
                    .eut(TierEU.RECIPE_UEV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UIV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32),
                        ItemList.Emitter_UIV.get(32),
                        ItemList.Electric_Pump_UIV.get(32),
                        OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NetherStar, 16),
                        GT_Utility.getIntegratedCircuit(6))
                    .itemOutputs(CustomItemList.eM_dynamoTunnel6_UIV.get(1))
                    .duration(26 * MINUTES + 40 * SECONDS)
                    .eut(TierEU.RECIPE_UIV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UMV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32),
                        ItemList.Emitter_UMV.get(32),
                        ItemList.Electric_Pump_UMV.get(32),
                        OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Quantium, 16),
                        GT_Utility.getIntegratedCircuit(6))
                    .itemOutputs(CustomItemList.eM_dynamoTunnel6_UMV.get(1))
                    .duration(26 * MINUTES + 40 * SECONDS)
                    .eut(TierEU.RECIPE_UMV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UXV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32),
                        ItemList.Emitter_UXV.get(32),
                        ItemList.Electric_Pump_UXV.get(32),
                        OreDictUnificator.get(OrePrefixes.wireGt08, Materials.BlackPlutonium, 16),
                        GT_Utility.getIntegratedCircuit(6))
                    .itemOutputs(CustomItemList.eM_dynamoTunnel6_UXV.get(1))
                    .duration(26 * MINUTES + 40 * SECONDS)
                    .eut(TierEU.RECIPE_UXV)
                    .addTo(assemblerRecipes);

            }

            // Laser Dynamo UIV-UXV 1048576/t
            {
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UIV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
                        ItemList.Emitter_UIV.get(64),
                        ItemList.Electric_Pump_UIV.get(64),
                        OreDictUnificator.get(OrePrefixes.wireGt16, Materials.NetherStar, 16),
                        GT_Utility.getIntegratedCircuit(7))
                    .itemOutputs(CustomItemList.eM_dynamoTunnel7_UIV.get(1))
                    .duration(53 * MINUTES + 20 * SECONDS)
                    .eut(TierEU.RECIPE_UIV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UMV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
                        ItemList.Emitter_UMV.get(64),
                        ItemList.Electric_Pump_UMV.get(64),
                        OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Quantium, 16),
                        GT_Utility.getIntegratedCircuit(7))
                    .itemOutputs(CustomItemList.eM_dynamoTunnel7_UMV.get(1))
                    .duration(53 * MINUTES + 20 * SECONDS)
                    .eut(TierEU.RECIPE_UMV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UXV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
                        ItemList.Emitter_UXV.get(64),
                        ItemList.Electric_Pump_UXV.get(64),
                        OreDictUnificator.get(OrePrefixes.wireGt16, Materials.BlackPlutonium, 16),
                        GT_Utility.getIntegratedCircuit(7))
                    .itemOutputs(CustomItemList.eM_dynamoTunnel7_UXV.get(1))
                    .duration(53 * MINUTES + 20 * SECONDS)
                    .eut(TierEU.RECIPE_UXV)
                    .addTo(assemblerRecipes);

            }
        }

        // Laser Target
        {
            // Laser Target IV-UXV 256/t
            {
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_IV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1),
                        ItemList.Sensor_IV.get(1),
                        ItemList.Electric_Pump_IV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt01, Materials.TungstenSteel, 2),
                        GT_Utility.getIntegratedCircuit(1))
                    .itemOutputs(CustomItemList.eM_energyTunnel1_IV.get(1))
                    .duration(50 * SECONDS)
                    .eut(TierEU.RECIPE_IV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_LuV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1),
                        ItemList.Sensor_LuV.get(1),
                        ItemList.Electric_Pump_LuV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt01, Materials.VanadiumGallium, 2),
                        GT_Utility.getIntegratedCircuit(1))
                    .itemOutputs(CustomItemList.eM_energyTunnel1_LuV.get(1))
                    .duration(50 * SECONDS)
                    .eut(TierEU.RECIPE_LuV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_ZPM.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1),
                        ItemList.Sensor_ZPM.get(1),
                        ItemList.Electric_Pump_ZPM.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Naquadah, 2),
                        GT_Utility.getIntegratedCircuit(1))
                    .itemOutputs(CustomItemList.eM_energyTunnel1_ZPM.get(1))
                    .duration(50 * SECONDS)
                    .eut(TierEU.RECIPE_ZPM)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1),
                        ItemList.Sensor_UV.get(1),
                        ItemList.Electric_Pump_UV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt01, Materials.NaquadahAlloy, 2),
                        GT_Utility.getIntegratedCircuit(1))
                    .itemOutputs(CustomItemList.eM_energyTunnel1_UV.get(1))
                    .duration(50 * SECONDS)
                    .eut(TierEU.RECIPE_UV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_MAX.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1),
                        ItemList.Sensor_UHV.get(1),
                        ItemList.Electric_Pump_UHV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Bedrockium, 2),
                        GT_Utility.getIntegratedCircuit(1))
                    .itemOutputs(CustomItemList.eM_energyTunnel1_UHV.get(1))
                    .duration(50 * SECONDS)
                    .eut(TierEU.RECIPE_UHV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UEV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1),
                        ItemList.Sensor_UEV.get(1),
                        ItemList.Electric_Pump_UEV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Draconium, 2),
                        GT_Utility.getIntegratedCircuit(1))
                    .itemOutputs(CustomItemList.eM_energyTunnel1_UEV.get(1))
                    .duration(50 * SECONDS)
                    .eut(TierEU.RECIPE_UEV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UIV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1),
                        ItemList.Sensor_UIV.get(1),
                        ItemList.Electric_Pump_UIV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt01, Materials.NetherStar, 2),
                        GT_Utility.getIntegratedCircuit(1))
                    .itemOutputs(CustomItemList.eM_energyTunnel1_UIV.get(1))
                    .duration(50 * SECONDS)
                    .eut(TierEU.RECIPE_UIV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UMV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1),
                        ItemList.Sensor_UMV.get(1),
                        ItemList.Electric_Pump_UMV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Quantium, 2),
                        GT_Utility.getIntegratedCircuit(1))
                    .itemOutputs(CustomItemList.eM_energyTunnel1_UMV.get(1))
                    .duration(50 * SECONDS)
                    .eut(TierEU.RECIPE_UMV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UXV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1),
                        ItemList.Sensor_UXV.get(1),
                        ItemList.Electric_Pump_UXV.get(1),
                        OreDictUnificator.get(OrePrefixes.wireGt01, Materials.BlackPlutonium, 2),
                        GT_Utility.getIntegratedCircuit(1))
                    .itemOutputs(CustomItemList.eM_energyTunnel1_UXV.get(1))
                    .duration(50 * SECONDS)
                    .eut(TierEU.RECIPE_UXV)
                    .addTo(assemblerRecipes);

            }

            // Laser Target LuV-UXV 1024/t
            {
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_LuV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2),
                        ItemList.Sensor_LuV.get(2),
                        ItemList.Electric_Pump_LuV.get(2),
                        OreDictUnificator.get(OrePrefixes.wireGt02, Materials.VanadiumGallium, 4),
                        GT_Utility.getIntegratedCircuit(2))
                    .itemOutputs(CustomItemList.eM_energyTunnel2_LuV.get(1))
                    .duration(1 * MINUTES + 40 * SECONDS)
                    .eut(TierEU.RECIPE_LuV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_ZPM.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2),
                        ItemList.Sensor_ZPM.get(2),
                        ItemList.Electric_Pump_ZPM.get(2),
                        OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Naquadah, 4),
                        GT_Utility.getIntegratedCircuit(2))
                    .itemOutputs(CustomItemList.eM_energyTunnel2_ZPM.get(1))
                    .duration(1 * MINUTES + 40 * SECONDS)
                    .eut(TierEU.RECIPE_ZPM)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2),
                        ItemList.Sensor_UV.get(2),
                        ItemList.Electric_Pump_UV.get(2),
                        OreDictUnificator.get(OrePrefixes.wireGt02, Materials.NaquadahAlloy, 4),
                        GT_Utility.getIntegratedCircuit(2))
                    .itemOutputs(CustomItemList.eM_energyTunnel2_UV.get(1))
                    .duration(1 * MINUTES + 40 * SECONDS)
                    .eut(TierEU.RECIPE_UV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_MAX.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2),
                        ItemList.Sensor_UHV.get(2),
                        ItemList.Electric_Pump_UHV.get(2),
                        OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Bedrockium, 4),
                        GT_Utility.getIntegratedCircuit(2))
                    .itemOutputs(CustomItemList.eM_energyTunnel2_UHV.get(1))
                    .duration(1 * MINUTES + 40 * SECONDS)
                    .eut(TierEU.RECIPE_UHV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UEV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2),
                        ItemList.Sensor_UEV.get(2),
                        ItemList.Electric_Pump_UEV.get(2),
                        OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Draconium, 4),
                        GT_Utility.getIntegratedCircuit(2))
                    .itemOutputs(CustomItemList.eM_energyTunnel2_UEV.get(1))
                    .duration(1 * MINUTES + 40 * SECONDS)
                    .eut(TierEU.RECIPE_UEV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UIV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2),
                        ItemList.Sensor_UIV.get(2),
                        ItemList.Electric_Pump_UIV.get(2),
                        OreDictUnificator.get(OrePrefixes.wireGt02, Materials.NetherStar, 4),
                        GT_Utility.getIntegratedCircuit(2))
                    .itemOutputs(CustomItemList.eM_energyTunnel2_UIV.get(1))
                    .duration(1 * MINUTES + 40 * SECONDS)
                    .eut(TierEU.RECIPE_UIV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UMV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2),
                        ItemList.Sensor_UMV.get(2),
                        ItemList.Electric_Pump_UMV.get(2),
                        OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Quantium, 4),
                        GT_Utility.getIntegratedCircuit(2))
                    .itemOutputs(CustomItemList.eM_energyTunnel2_UMV.get(1))
                    .duration(1 * MINUTES + 40 * SECONDS)
                    .eut(TierEU.RECIPE_UMV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UXV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2),
                        ItemList.Sensor_UXV.get(2),
                        ItemList.Electric_Pump_UXV.get(2),
                        OreDictUnificator.get(OrePrefixes.wireGt02, Materials.BlackPlutonium, 4),
                        GT_Utility.getIntegratedCircuit(2))
                    .itemOutputs(CustomItemList.eM_energyTunnel2_UXV.get(1))
                    .duration(1 * MINUTES + 40 * SECONDS)
                    .eut(TierEU.RECIPE_UXV)
                    .addTo(assemblerRecipes);

            }

            // Laser Target ZPM-UXV 4096/t
            {
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_ZPM.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4),
                        ItemList.Sensor_ZPM.get(4),
                        ItemList.Electric_Pump_ZPM.get(4),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Naquadah, 4),
                        GT_Utility.getIntegratedCircuit(3))
                    .itemOutputs(CustomItemList.eM_energyTunnel3_ZPM.get(1))
                    .duration(3 * MINUTES + 20 * SECONDS)
                    .eut(TierEU.RECIPE_ZPM)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4),
                        ItemList.Sensor_UV.get(4),
                        ItemList.Electric_Pump_UV.get(4),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 4),
                        GT_Utility.getIntegratedCircuit(3))
                    .itemOutputs(CustomItemList.eM_energyTunnel3_UV.get(1))
                    .duration(3 * MINUTES + 20 * SECONDS)
                    .eut(TierEU.RECIPE_UV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_MAX.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4),
                        ItemList.Sensor_UHV.get(4),
                        ItemList.Electric_Pump_UHV.get(4),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Bedrockium, 4),
                        GT_Utility.getIntegratedCircuit(3))
                    .itemOutputs(CustomItemList.eM_energyTunnel3_UHV.get(1))
                    .duration(3 * MINUTES + 20 * SECONDS)
                    .eut(TierEU.RECIPE_UHV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UEV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4),
                        ItemList.Sensor_UEV.get(4),
                        ItemList.Electric_Pump_UEV.get(4),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Draconium, 4),
                        GT_Utility.getIntegratedCircuit(3))
                    .itemOutputs(CustomItemList.eM_energyTunnel3_UEV.get(1))
                    .duration(3 * MINUTES + 20 * SECONDS)
                    .eut(TierEU.RECIPE_UEV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UIV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4),
                        ItemList.Sensor_UIV.get(4),
                        ItemList.Electric_Pump_UIV.get(4),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NetherStar, 4),
                        GT_Utility.getIntegratedCircuit(3))
                    .itemOutputs(CustomItemList.eM_energyTunnel3_UIV.get(1))
                    .duration(3 * MINUTES + 20 * SECONDS)
                    .eut(TierEU.RECIPE_UIV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UMV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1),
                        ItemList.Sensor_UMV.get(4),
                        ItemList.Electric_Pump_UMV.get(4),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Quantium, 4),
                        GT_Utility.getIntegratedCircuit(3))
                    .itemOutputs(CustomItemList.eM_energyTunnel3_UMV.get(1))
                    .duration(3 * MINUTES + 20 * SECONDS)
                    .eut(TierEU.RECIPE_UMV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UXV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1),
                        ItemList.Sensor_UXV.get(4),
                        ItemList.Electric_Pump_UXV.get(4),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.BlackPlutonium, 4),
                        GT_Utility.getIntegratedCircuit(3))
                    .itemOutputs(CustomItemList.eM_energyTunnel3_UXV.get(1))
                    .duration(3 * MINUTES + 20 * SECONDS)
                    .eut(TierEU.RECIPE_UXV)
                    .addTo(assemblerRecipes);

            }

            // Laser Target UV-UXV 16384/t
            {
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8),
                        ItemList.Sensor_UV.get(8),
                        ItemList.Electric_Pump_UV.get(8),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 8),
                        GT_Utility.getIntegratedCircuit(4))
                    .itemOutputs(CustomItemList.eM_energyTunnel4_UV.get(1))
                    .duration(6 * MINUTES + 40 * SECONDS)
                    .eut(TierEU.RECIPE_UV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_MAX.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8),
                        ItemList.Sensor_UHV.get(8),
                        ItemList.Electric_Pump_UHV.get(8),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Bedrockium, 8),
                        GT_Utility.getIntegratedCircuit(4))
                    .itemOutputs(CustomItemList.eM_energyTunnel4_UHV.get(1))
                    .duration(6 * MINUTES + 40 * SECONDS)
                    .eut(TierEU.RECIPE_UHV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UEV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8),
                        ItemList.Sensor_UEV.get(8),
                        ItemList.Electric_Pump_UEV.get(8),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Draconium, 8),
                        GT_Utility.getIntegratedCircuit(4))
                    .itemOutputs(CustomItemList.eM_energyTunnel4_UEV.get(1))
                    .duration(6 * MINUTES + 40 * SECONDS)
                    .eut(TierEU.RECIPE_UEV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UIV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8),
                        ItemList.Sensor_UIV.get(8),
                        ItemList.Electric_Pump_UIV.get(8),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NetherStar, 8),
                        GT_Utility.getIntegratedCircuit(4))
                    .itemOutputs(CustomItemList.eM_energyTunnel4_UIV.get(1))
                    .duration(6 * MINUTES + 40 * SECONDS)
                    .eut(TierEU.RECIPE_UIV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UMV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8),
                        ItemList.Sensor_UMV.get(8),
                        ItemList.Electric_Pump_UMV.get(8),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Quantium, 8),
                        GT_Utility.getIntegratedCircuit(4))
                    .itemOutputs(CustomItemList.eM_energyTunnel4_UMV.get(1))
                    .duration(6 * MINUTES + 40 * SECONDS)
                    .eut(TierEU.RECIPE_UMV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UXV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8),
                        ItemList.Sensor_UXV.get(8),
                        ItemList.Electric_Pump_UXV.get(8),
                        OreDictUnificator.get(OrePrefixes.wireGt04, Materials.BlackPlutonium, 8),
                        GT_Utility.getIntegratedCircuit(4))
                    .itemOutputs(CustomItemList.eM_energyTunnel4_UXV.get(1))
                    .duration(6 * MINUTES + 40 * SECONDS)
                    .eut(TierEU.RECIPE_UXV)
                    .addTo(assemblerRecipes);

            }

            // Laser Target UHV-UXV 65536/t
            {
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_MAX.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16),
                        ItemList.Sensor_UHV.get(16),
                        ItemList.Electric_Pump_UHV.get(16),
                        OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Bedrockium, 8),
                        GT_Utility.getIntegratedCircuit(5))
                    .itemOutputs(CustomItemList.eM_energyTunnel5_UHV.get(1))
                    .duration(13 * MINUTES + 20 * SECONDS)
                    .eut(TierEU.RECIPE_UHV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UEV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16),
                        ItemList.Sensor_UEV.get(16),
                        ItemList.Electric_Pump_UEV.get(16),
                        OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Draconium, 8),
                        GT_Utility.getIntegratedCircuit(5))
                    .itemOutputs(CustomItemList.eM_energyTunnel5_UEV.get(1))
                    .duration(13 * MINUTES + 20 * SECONDS)
                    .eut(TierEU.RECIPE_UEV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UIV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16),
                        ItemList.Sensor_UIV.get(16),
                        ItemList.Electric_Pump_UIV.get(16),
                        OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NetherStar, 8),
                        GT_Utility.getIntegratedCircuit(5))
                    .itemOutputs(CustomItemList.eM_energyTunnel5_UIV.get(1))
                    .duration(13 * MINUTES + 20 * SECONDS)
                    .eut(TierEU.RECIPE_UIV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UMV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16),
                        ItemList.Sensor_UMV.get(16),
                        ItemList.Electric_Pump_UMV.get(16),
                        OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Quantium, 8),
                        GT_Utility.getIntegratedCircuit(5))
                    .itemOutputs(CustomItemList.eM_energyTunnel5_UMV.get(1))
                    .duration(13 * MINUTES + 20 * SECONDS)
                    .eut(TierEU.RECIPE_UMV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UXV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16),
                        ItemList.Sensor_UXV.get(16),
                        ItemList.Electric_Pump_UXV.get(16),
                        OreDictUnificator.get(OrePrefixes.wireGt08, Materials.BlackPlutonium, 8),
                        GT_Utility.getIntegratedCircuit(5))
                    .itemOutputs(CustomItemList.eM_energyTunnel5_UXV.get(1))
                    .duration(13 * MINUTES + 20 * SECONDS)
                    .eut(TierEU.RECIPE_UXV)
                    .addTo(assemblerRecipes);

            }

            // Laser Target UEV-UXV 262144/t
            {
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UEV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32),
                        ItemList.Sensor_UEV.get(32),
                        ItemList.Electric_Pump_UEV.get(32),
                        OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Draconium, 16),
                        GT_Utility.getIntegratedCircuit(6))
                    .itemOutputs(CustomItemList.eM_energyTunnel6_UEV.get(1))
                    .duration(26 * MINUTES + 40 * SECONDS)
                    .eut(TierEU.RECIPE_UEV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UIV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32),
                        ItemList.Sensor_UIV.get(32),
                        ItemList.Electric_Pump_UIV.get(32),
                        OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NetherStar, 16),
                        GT_Utility.getIntegratedCircuit(6))
                    .itemOutputs(CustomItemList.eM_energyTunnel6_UIV.get(1))
                    .duration(26 * MINUTES + 40 * SECONDS)
                    .eut(TierEU.RECIPE_UIV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UMV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32),
                        ItemList.Sensor_UMV.get(32),
                        ItemList.Electric_Pump_UMV.get(32),
                        OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Quantium, 16),
                        GT_Utility.getIntegratedCircuit(6))
                    .itemOutputs(CustomItemList.eM_energyTunnel6_UMV.get(1))
                    .duration(26 * MINUTES + 40 * SECONDS)
                    .eut(TierEU.RECIPE_UMV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UXV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32),
                        ItemList.Sensor_UXV.get(32),
                        ItemList.Electric_Pump_UXV.get(32),
                        OreDictUnificator.get(OrePrefixes.wireGt08, Materials.BlackPlutonium, 16),
                        GT_Utility.getIntegratedCircuit(6))
                    .itemOutputs(CustomItemList.eM_energyTunnel6_UXV.get(1))
                    .duration(26 * MINUTES + 40 * SECONDS)
                    .eut(TierEU.RECIPE_UXV)
                    .addTo(assemblerRecipes);

            }

            // Laser Target UIV-UXV 1048576/t
            {
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UIV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
                        ItemList.Sensor_UIV.get(64),
                        ItemList.Electric_Pump_UIV.get(64),
                        OreDictUnificator.get(OrePrefixes.wireGt16, Materials.NetherStar, 16),
                        GT_Utility.getIntegratedCircuit(7))
                    .itemOutputs(CustomItemList.eM_energyTunnel7_UIV.get(1))
                    .duration(53 * MINUTES + 20 * SECONDS)
                    .eut(TierEU.RECIPE_UIV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UMV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
                        ItemList.Sensor_UMV.get(64),
                        ItemList.Electric_Pump_UMV.get(64),
                        OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Quantium, 16),
                        GT_Utility.getIntegratedCircuit(7))
                    .itemOutputs(CustomItemList.eM_energyTunnel7_UMV.get(1))
                    .duration(53 * MINUTES + 20 * SECONDS)
                    .eut(TierEU.RECIPE_UMV)
                    .addTo(assemblerRecipes);
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        ItemList.Hull_UXV.get(1),
                        OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
                        ItemList.Sensor_UXV.get(64),
                        ItemList.Electric_Pump_UXV.get(64),
                        OreDictUnificator.get(OrePrefixes.wireGt16, Materials.BlackPlutonium, 16),
                        GT_Utility.getIntegratedCircuit(7))
                    .itemOutputs(CustomItemList.eM_energyTunnel7_UXV.get(1))
                    .duration(53 * MINUTES + 20 * SECONDS)
                    .eut(TierEU.RECIPE_UXV)
                    .addTo(assemblerRecipes);

            }
        }

        // Tesla Capacitor
        {
            // LV Tesla Capacitor
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Tin, 4),
                    OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 4),
                    OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 8),
                    OreDictUnificator.get(OrePrefixes.foil, Materials.Silicone, 8))
                .itemOutputs(CustomItemList.teslaCapacitor.getWithDamage(1, 0))
                .fluidInputs(Materials.Epoxid.getMolten(72))
                .duration(16 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);
            // MV Tesla Capacitor
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Copper, 4),
                    OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 6),
                    OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 12),
                    OreDictUnificator.get(OrePrefixes.foil, Materials.Silicone, 12))
                .itemOutputs(CustomItemList.teslaCapacitor.getWithDamage(1, 1))
                .fluidInputs(Materials.Epoxid.getMolten(144))
                .duration(16 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(assemblerRecipes);
            // HV Tesla Capacitor
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Gold, 4),
                    OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 8),
                    OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 16),
                    OreDictUnificator.get(OrePrefixes.foil, Materials.Silicone, 16))
                .itemOutputs(CustomItemList.teslaCapacitor.getWithDamage(1, 2))
                .fluidInputs(Materials.Epoxid.getMolten(216))
                .duration(16 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(assemblerRecipes);
            // EV Tesla Capacitor
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Aluminium, 4),
                    OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 10),
                    OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 20),
                    OreDictUnificator.get(OrePrefixes.foil, Materials.Silicone, 20))
                .itemOutputs(CustomItemList.teslaCapacitor.getWithDamage(1, 3))
                .fluidInputs(Materials.Epoxid.getMolten(288))
                .duration(16 * SECONDS)
                .eut(TierEU.RECIPE_EV)
                .addTo(assemblerRecipes);
            // IV Tesla Capacitor
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Tungsten, 4),
                    OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 12),
                    OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 24),
                    OreDictUnificator.get(OrePrefixes.foil, Materials.Silicone, 24))
                .itemOutputs(CustomItemList.teslaCapacitor.getWithDamage(1, 4))
                .fluidInputs(Materials.Epoxid.getMolten(360))
                .duration(16 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(assemblerRecipes);
            // LuV Tesla Capacitor
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    OreDictUnificator.get(OrePrefixes.cableGt01, Materials.HSSG, 4),
                    OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 14),
                    OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 28),
                    OreDictUnificator.get(OrePrefixes.foil, Materials.Silicone, 28))
                .itemOutputs(CustomItemList.teslaCapacitor.getWithDamage(1, 5))
                .fluidInputs(Materials.Epoxid.getMolten(432))
                .duration(16 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(assemblerRecipes);
            // ZPM Tesla Capacitor
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Naquadah, 4),
                    OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 16),
                    OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 32),
                    OreDictUnificator.get(OrePrefixes.foil, Materials.Silicone, 32))
                .itemOutputs(CustomItemList.teslaCapacitor.getWithDamage(1, 6))
                .fluidInputs(Materials.Epoxid.getMolten(504))
                .duration(16 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .addTo(assemblerRecipes);

        }

        // Tesla Cover
        GT_Values.RA.stdBuilder()
            .itemInputs(
                CustomItemList.teslaComponent.getWithDamage(4, 0),
                OreDictUnificator.get(OrePrefixes.circuit, Materials.HV, 2),
                OreDictUnificator.get(OrePrefixes.wireFine, Materials.Gold, 16),
                OreDictUnificator.get(OrePrefixes.plate, Materials.NickelZincFerrite, 2),
                OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 8))
            .itemOutputs(CustomItemList.teslaCover.getWithDamage(1, 0))
            .fluidInputs(Materials.Lead.getMolten(288))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                CustomItemList.teslaComponent.getWithDamage(4, 0),
                OreDictUnificator.get(OrePrefixes.circuit, Materials.HV, 2),
                OreDictUnificator.get(OrePrefixes.wireFine, Materials.Gold, 16),
                OreDictUnificator.get(OrePrefixes.plate, Materials.NickelZincFerrite, 2),
                OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 8))
            .itemOutputs(CustomItemList.teslaCover.getWithDamage(1, 0))
            .fluidInputs(Materials.Tin.getMolten(144))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                CustomItemList.teslaComponent.getWithDamage(4, 0),
                OreDictUnificator.get(OrePrefixes.circuit, Materials.HV, 2),
                OreDictUnificator.get(OrePrefixes.wireFine, Materials.Gold, 16),
                OreDictUnificator.get(OrePrefixes.plate, Materials.NickelZincFerrite, 2),
                OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 8))
            .itemOutputs(CustomItemList.teslaCover.getWithDamage(1, 0))
            .fluidInputs(Materials.SolderingAlloy.getMolten(72))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
        // Ultimate Tesla Cover
        GT_Values.RA.stdBuilder()
            .itemInputs(
                CustomItemList.teslaComponent.getWithDamage(4, 1),
                OreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 2),
                OreDictUnificator.get(OrePrefixes.wireFine, Materials.Tungsten, 16),
                OreDictUnificator.get(OrePrefixes.plate, Materials.NickelZincFerrite, 2),
                OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 8))
            .itemOutputs(CustomItemList.teslaCover.getWithDamage(1, 1))
            .fluidInputs(Materials.Lead.getMolten(288))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                CustomItemList.teslaComponent.getWithDamage(4, 1),
                OreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 2),
                OreDictUnificator.get(OrePrefixes.wireFine, Materials.Tungsten, 16),
                OreDictUnificator.get(OrePrefixes.plate, Materials.NickelZincFerrite, 2),
                OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 8))
            .itemOutputs(CustomItemList.teslaCover.getWithDamage(1, 1))
            .fluidInputs(Materials.Tin.getMolten(144))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                CustomItemList.teslaComponent.getWithDamage(4, 1),
                OreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 2),
                OreDictUnificator.get(OrePrefixes.wireFine, Materials.Tungsten, 16),
                OreDictUnificator.get(OrePrefixes.plate, Materials.NickelZincFerrite, 2),
                OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 8))
            .itemOutputs(CustomItemList.teslaCover.getWithDamage(1, 1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(72))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        // Ender Fluid Link Cover
        GT_Values.RA.stdBuilder()
            .itemInputs(
                OreDictUnificator.get(OrePrefixes.plateDense, Materials.Enderium, 4),
                ItemList.Sensor_LuV.get(1),
                ItemList.Emitter_LuV.get(1),
                ItemList.Electric_Pump_LuV.get(1))
            .itemOutputs(CustomItemList.enderLinkFluidCover.getWithDamage(1, 0))
            .fluidInputs(getOrDefault("Rhodium-PlatedPalladium", Materials.Chrome).getMolten(288))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);
        // Power Pass Upgrade Cover
        GT_Values.RA.stdBuilder()
            .itemInputs(CustomItemList.Machine_Multi_Transformer.get(1), GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(CustomItemList.powerPassUpgradeCover.getWithDamage(1, 0))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);
        // Tesla Winding Components
        GT_Values.RA.stdBuilder()
            .itemInputs(
                OreDictUnificator.get(OrePrefixes.wireFine, Materials.Electrum, 32),
                OreDictUnificator.get(OrePrefixes.ring, Materials.NickelZincFerrite, 8))
            .itemOutputs(CustomItemList.teslaComponent.getWithDamage(1, 0))
            .fluidInputs(Materials.Epoxid.getMolten(288))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);
        // Tesla Winding Components Ultimate (ADD BLOOD VARIANT)
        GT_Values.RA.stdBuilder()
            .itemInputs(
                OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 4),
                OreDictUnificator.get(OrePrefixes.ring, Materials.NickelZincFerrite, 8))
            .itemOutputs(CustomItemList.teslaComponent.getWithDamage(1, 1))
            .fluidInputs(Materials.Epoxid.getMolten(576))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        // Tesla Transceiver LV 1A
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_1by1_LV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_1by1_LV.get(1))
            .fluidInputs(Materials.Lead.getMolten(576))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_1by1_LV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_1by1_LV.get(1))
            .fluidInputs(Materials.Tin.getMolten(288))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_1by1_LV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_1by1_LV.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);
        // Tesla Transceiver MV 1A
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_1by1_MV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_1by1_MV.get(1))
            .fluidInputs(Materials.Lead.getMolten(576))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_1by1_MV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_1by1_MV.get(1))
            .fluidInputs(Materials.Tin.getMolten(288))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_1by1_MV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_1by1_MV.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);
        // Tesla Transceiver HV 1A
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_1by1_HV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_1by1_HV.get(1))
            .fluidInputs(Materials.Lead.getMolten(576))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_1by1_HV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_1by1_HV.get(1))
            .fluidInputs(Materials.Tin.getMolten(288))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_1by1_HV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_1by1_HV.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
        // Tesla Transceiver EV 1A
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_1by1_EV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_1by1_EV.get(1))
            .fluidInputs(Materials.Lead.getMolten(576))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_1by1_EV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_1by1_EV.get(1))
            .fluidInputs(Materials.Tin.getMolten(288))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_1by1_EV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_1by1_EV.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
        // Tesla Transceiver IV 1A
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_1by1_IV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_1by1_IV.get(1))
            .fluidInputs(Materials.Lead.getMolten(576))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_1by1_IV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_1by1_IV.get(1))
            .fluidInputs(Materials.Tin.getMolten(288))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_1by1_IV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_1by1_IV.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        // Tesla Transceiver LV 4A
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_2by2_LV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_2by2_LV.get(1))
            .fluidInputs(Materials.Lead.getMolten(576))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_2by2_LV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_2by2_LV.get(1))
            .fluidInputs(Materials.Tin.getMolten(288))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_2by2_LV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_2by2_LV.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);
        // Tesla Transceiver MV 4A
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_2by2_MV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_2by2_MV.get(1))
            .fluidInputs(Materials.Lead.getMolten(576))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_2by2_MV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_2by2_MV.get(1))
            .fluidInputs(Materials.Tin.getMolten(288))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_2by2_MV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_2by2_MV.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);
        // Tesla Transceiver HV 4A
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_2by2_HV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_2by2_HV.get(1))
            .fluidInputs(Materials.Lead.getMolten(576))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_2by2_HV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_2by2_HV.get(1))
            .fluidInputs(Materials.Tin.getMolten(288))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_2by2_HV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_2by2_HV.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
        // Tesla Transceiver EV 4A
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_2by2_EV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_2by2_EV.get(1))
            .fluidInputs(Materials.Lead.getMolten(576))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_2by2_EV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_2by2_EV.get(1))
            .fluidInputs(Materials.Tin.getMolten(288))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_2by2_EV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_2by2_EV.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
        // Tesla Transceiver IV 4A
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_2by2_IV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_2by2_IV.get(1))
            .fluidInputs(Materials.Lead.getMolten(576))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_2by2_IV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_2by2_IV.get(1))
            .fluidInputs(Materials.Tin.getMolten(288))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_2by2_IV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_2by2_IV.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        // Tesla Transceiver LV 9A
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_3by3_LV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_3by3_LV.get(1))
            .fluidInputs(Materials.Lead.getMolten(576))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_3by3_LV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_3by3_LV.get(1))
            .fluidInputs(Materials.Tin.getMolten(288))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_3by3_LV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_3by3_LV.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);
        // Tesla Transceiver MV 9A
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_3by3_MV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_3by3_MV.get(1))
            .fluidInputs(Materials.Lead.getMolten(576))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_3by3_LV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_3by3_MV.get(1))
            .fluidInputs(Materials.Tin.getMolten(288))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_3by3_MV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_3by3_MV.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);
        // Tesla Transceiver HV 9A
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_3by3_HV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_3by3_HV.get(1))
            .fluidInputs(Materials.Lead.getMolten(576))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_3by3_HV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_3by3_HV.get(1))
            .fluidInputs(Materials.Tin.getMolten(288))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_3by3_HV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_3by3_HV.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
        // Tesla Transceiver EV 9A
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_3by3_EV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_3by3_EV.get(1))
            .fluidInputs(Materials.Lead.getMolten(576))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_3by3_EV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_3by3_EV.get(1))
            .fluidInputs(Materials.Tin.getMolten(288))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_3by3_EV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_3by3_EV.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
        // Tesla Transceiver IV 9A
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_3by3_IV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_3by3_IV.get(1))
            .fluidInputs(Materials.Lead.getMolten(576))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_3by3_IV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_3by3_IV.get(1))
            .fluidInputs(Materials.Tin.getMolten(288))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_3by3_IV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_3by3_IV.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        // Tesla Transceiver LV 16A
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_4by4_LV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_4by4_LV.get(1))
            .fluidInputs(Materials.Lead.getMolten(576))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_4by4_LV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_4by4_LV.get(1))
            .fluidInputs(Materials.Tin.getMolten(288))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_4by4_LV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_4by4_LV.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);
        // Tesla Transceiver MV 16A
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_4by4_MV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_4by4_MV.get(1))
            .fluidInputs(Materials.Lead.getMolten(576))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_4by4_MV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_4by4_MV.get(1))
            .fluidInputs(Materials.Tin.getMolten(288))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_4by4_MV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_4by4_MV.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);
        // Tesla Transceiver HV 16A
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_4by4_HV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_4by4_HV.get(1))
            .fluidInputs(Materials.Lead.getMolten(576))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_4by4_HV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_4by4_HV.get(1))
            .fluidInputs(Materials.Tin.getMolten(288))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_4by4_HV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_4by4_HV.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
        // Tesla Transceiver EV 16A
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_4by4_EV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_4by4_EV.get(1))
            .fluidInputs(Materials.Lead.getMolten(576))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_4by4_EV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_4by4_EV.get(1))
            .fluidInputs(Materials.Tin.getMolten(288))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_4by4_EV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_4by4_EV.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
        // Tesla Transceiver IV 16A
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_4by4_IV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_4by4_IV.get(1))
            .fluidInputs(Materials.Lead.getMolten(576))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_4by4_IV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_4by4_IV.get(1))
            .fluidInputs(Materials.Tin.getMolten(288))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Battery_Buffer_4by4_IV.get(1), CustomItemList.teslaCover.getWithDamage(1, 0))
            .itemOutputs(CustomItemList.Machine_TeslaCoil_4by4_IV.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        // Tesla Tower
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_ModHandler.getIC2Item("teslaCoil", 1),
                CustomItemList.tM_TeslaSecondary.get(4),
                OreDictUnificator.get(OrePrefixes.plate, Materials.NickelZincFerrite, 4),
                OreDictUnificator.get(OrePrefixes.circuit, Materials.HV, 4),
                ItemList.Upgrade_Overclocker.get(4))
            .itemOutputs(CustomItemList.Machine_Multi_TeslaCoil.get(1))
            .fluidInputs(Materials.Silver.getMolten(576))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
        // Microwave Grinder
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Machine_HV_Microwave.get(1),
                OreDictUnificator.get(OrePrefixes.plate, Materials.StainlessSteel, 4),
                OreDictUnificator.get(OrePrefixes.circuit, Materials.HV, 4),
                OreDictUnificator.get(OrePrefixes.wireFine, Materials.AnnealedCopper, 16),
                ItemList.Upgrade_Overclocker.get(4))
            .itemOutputs(CustomItemList.Machine_Multi_Microwave.get(1))
            .fluidInputs(Materials.Copper.getMolten(576))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
        // Active Transformer
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.WetTransformer_ZPM_LuV.get(1),
                getItemContainer("HighEnergyFlowCircuit").get(1),
                OreDictUnificator
                    .get(OrePrefixes.wireGt01, getOrDefault("SuperconductorLuV", Materials.SuperconductorUHV), 16),
                ItemList.valueOf("Circuit_Chip_UHPIC")
                    .get(2))
            .itemOutputs(CustomItemList.Machine_Multi_Transformer.get(1))
            .fluidInputs(Materials.TungstenSteel.getMolten(576))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);
        // Network Switch
        GT_Values.RA.stdBuilder()
            .itemInputs(
                CustomItemList.Machine_Multi_Transformer.get(1),
                OreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 4),
                OreDictUnificator.get(OrePrefixes.wireFine, Materials.Cobalt, 64),
                OreDictUnificator.get(OrePrefixes.wireFine, Materials.Copper, 64),
                CustomItemList.DATApipe.get(4))
            .itemOutputs(CustomItemList.Machine_Multi_Switch.get(1))
            .fluidInputs(Materials.Iridium.getMolten(1296))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

    }

    public void cleanroomRecipes() {
        // Data
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Parts_GlassFiber.get(8),
                OreDictUnificator.get(OrePrefixes.foil, Materials.Silver, 8))
            .itemOutputs(CustomItemList.DATApipe.get(1))
            .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(144))
            .requiresCleanRoom()
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);
        // Data Casing
        GT_Values.RA.stdBuilder()
            .itemInputs(CustomItemList.DATApipe.get(1), ItemList.Casing_LuV.get(1))
            .itemOutputs(CustomItemList.DATApipeBlock.get(1))
            .requiresCleanRoom()
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);
        // Laser
        GT_Values.RA.stdBuilder()
            .itemInputs(
                CustomItemList.DATApipe.get(1),
                GT_ModHandler.getIC2Item("reinforcedGlass", 1L),
                OreDictUnificator.get(OrePrefixes.foil, Materials.Osmiridium, 2))
            .itemOutputs(CustomItemList.LASERpipe.get(1))
            .requiresCleanRoom()
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(assemblerRecipes);
        // Laser Casing
        GT_Values.RA.stdBuilder()
            .itemInputs(CustomItemList.LASERpipe.get(1), ItemList.Casing_LuV.get(1))
            .itemOutputs(CustomItemList.LASERpipeBlock.get(1))
            .requiresCleanRoom()
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);
        // Advanced Computer Casing
        GT_Values.RA.stdBuilder()
            .itemInputs(
                CustomItemList.eM_Computer_Casing.get(1),
                OreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 1),
                OreDictUnificator.get(OrePrefixes.wireFine, Materials.Cobalt, 64),
                OreDictUnificator.get(OrePrefixes.wireFine, Materials.Electrum, 64),
                OreDictUnificator
                    .get(OrePrefixes.wireGt02, getOrDefault("SuperconductorLuV", Materials.SuperconductorUHV), 4))
            .itemOutputs(CustomItemList.eM_Computer_Bus.get(1))
            .fluidInputs(Materials.Iridium.getMolten(1296))
            .requiresCleanRoom()
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);
        // Data Input
        GT_Values.RA.stdBuilder()
            .itemInputs(
                CustomItemList.eM_Computer_Casing.get(1),
                ItemList.Hatch_Input_Bus_LuV.get(1),
                OreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 1),
                CustomItemList.DATApipe.get(2))
            .itemOutputs(CustomItemList.dataIn_Hatch.get(1))
            .fluidInputs(Materials.Iridium.getMolten(1296))
            .requiresCleanRoom()
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);
        // Data Output
        GT_Values.RA.stdBuilder()
            .itemInputs(
                CustomItemList.eM_Computer_Casing.get(1),
                ItemList.Hatch_Output_Bus_LuV.get(1),
                OreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 1),
                CustomItemList.DATApipe.get(2))
            .itemOutputs(CustomItemList.dataOut_Hatch.get(1))
            .fluidInputs(Materials.Iridium.getMolten(1296))
            .requiresCleanRoom()
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);
        // Rack
        GT_Values.RA.stdBuilder()
            .itemInputs(
                CustomItemList.eM_Computer_Bus.get(1),
                ItemList.Hatch_Input_Bus_ZPM.get(1),
                OreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 2),
                CustomItemList.DATApipe.get(4))
            .itemOutputs(CustomItemList.rack_Hatch.get(1))
            .fluidInputs(Materials.Iridium.getMolten(1296))
            .requiresCleanRoom()
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);
        // Uncertainty
        GT_Values.RA.stdBuilder()
            .itemInputs(
                CustomItemList.eM_Computer_Casing.get(1),
                OreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 1),
                CustomItemList.DATApipe.get(16),
                ItemList.Cover_Screen.get(1),
                new ItemStack(Blocks.stone_button, 16),
                GT_Utility.getIntegratedCircuit(4))
            .itemOutputs(CustomItemList.Uncertainty_Hatch.get(1))
            .fluidInputs(Materials.Iridium.getMolten(2592))
            .requiresCleanRoom()
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);
        // Uncertainty X
        GT_Values.RA.stdBuilder()
            .itemInputs(
                CustomItemList.eM_Computer_Casing.get(1),
                OreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 1),
                CustomItemList.DATApipe.get(32),
                ItemList.Cover_Screen.get(1),
                new ItemStack(Blocks.stone_button, 16),
                GT_Utility.getIntegratedCircuit(5))
            .itemOutputs(CustomItemList.UncertaintyX_Hatch.get(1))
            .fluidInputs(Materials.Iridium.getMolten(2592))
            .requiresCleanRoom()
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);
        // Capacitor Hatch
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_Bus_HV.get(1),
                OreDictUnificator.get(OrePrefixes.plate, Materials.NickelZincFerrite, 4),
                OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Gold, 4))
            .itemOutputs(CustomItemList.capacitor_Hatch.get(1))
            .fluidInputs(Materials.Silver.getMolten(576))
            .requiresCleanRoom()
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Hatch_Output_Bus_HV.get(1),
                OreDictUnificator.get(OrePrefixes.plate, Materials.NickelZincFerrite, 4),
                OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Gold, 4))
            .itemOutputs(CustomItemList.capacitor_Hatch.get(1))
            .fluidInputs(Materials.Silver.getMolten(576))
            .requiresCleanRoom()
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
    }
}
