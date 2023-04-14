package com.github.technus.tectech.loader.recipe;

import static com.github.technus.tectech.loader.recipe.BaseRecipeLoader.getItemContainer;
import static com.github.technus.tectech.loader.recipe.BaseRecipeLoader.getOrDefault;
import static gregtech.api.enums.GT_Values.RA;
import static gregtech.api.enums.Mods.BartWorks;
import static gregtech.api.util.GT_ModHandler.getModItem;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.github.technus.tectech.thing.CustomItemList;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.gtnewhorizon.structurelib.StructureLibAPI;

import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

public class Assembler implements Runnable {

    @Override
    public void run() {
        cleanroomRecipes();

        for (int i = 0; i <= 15; i++) {
            RA.addAssemblerRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(i + 1),
                            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Cobalt, 1) },
                    Materials.Aluminium.getMolten(864),
                    new ItemStack(StructureLibAPI.getBlockHint(), 1, i),
                    32,
                    120);
        }

        // Quantum Glass
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { CustomItemList.eM_Containment.get(1),
                        GT_ModHandler.getIC2Item("reinforcedGlass", 1L) },
                getOrDefault("Trinium", Materials.Osmium).getMolten(576),
                new ItemStack(QuantumGlassBlock.INSTANCE, 1),
                200,
                500000);

        // recipe for ass line data hatches
        RA.addAssemblerRecipe(
                ItemList.Hatch_DataAccess_EV.get(1),
                CustomItemList.dataIn_Hatch.get(1),
                CustomItemList.dataInAss_Hatch.get(1),
                2048,
                12000);
        RA.addAssemblerRecipe(
                ItemList.Hatch_DataAccess_EV.get(1),
                CustomItemList.dataOut_Hatch.get(1),
                CustomItemList.dataOutAss_Hatch.get(1),
                2048,
                12000);

        // High Power Casing
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Iridium, 1),
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Iridium, 6),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 1),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Cobalt, 16),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Copper, 16),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.NiobiumTitanium, 2) },
                Materials.TungstenSteel.getMolten(576),
                CustomItemList.eM_Power.get(1),
                100,
                30720);

        // Computer Casing
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { CustomItemList.eM_Power.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.StainlessSteel, 8),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Ultimate, 1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.NiobiumTitanium, 2) },
                Materials.Aluminium.getMolten(1296),
                CustomItemList.eM_Computer_Casing.get(1),
                200,
                122880);
        // Computer Vent Casing
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.StainlessSteel, 1),
                        ItemList.Electric_Motor_IV.get(2),
                        GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.StainlessSteel, 2),
                        GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.StainlessSteel, 16),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Copper, 16),
                        GT_OreDictUnificator.get(
                                OrePrefixes.wireGt01,
                                getOrDefault("SuperconductorIV", Materials.SuperconductorUHV),
                                1) },
                Materials.SolderingAlloy.getMolten(1296),
                CustomItemList.eM_Computer_Vent.get(1),
                100,
                1920);

        // Molecular Casing
        GT_Values.RA
                .addAssemblerRecipe(
                        new ItemStack[] { CustomItemList.eM_Power.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Osmiridium, 6),
                                GT_OreDictUnificator
                                        .get(OrePrefixes.foil, getOrDefault("Trinium", Materials.Osmium), 12),
                                GT_OreDictUnificator.get(OrePrefixes.screw, Materials.TungstenSteel, 24),
                                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.TungstenSteel, 24),
                                ItemList.Field_Generator_IV.get(1) },
                        Materials.Osmium.getMolten(1296),
                        CustomItemList.eM_Containment.get(1),
                        800,
                        500000);

        // Tesla Base
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NickelZincFerrite, 6),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.NickelZincFerrite, 1) },
                null,
                CustomItemList.tM_TeslaBase.get(1),
                50,
                16);

        // Tesla Toroid
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 6),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Aluminium, 1) },
                null,
                CustomItemList.tM_TeslaToroid.get(1),
                50,
                16);

        // Tesla Secondary Windings
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { CustomItemList.teslaComponent.getWithDamage(8, 0),
                        getItemContainer("MicaInsulatorFoil").get(12) },
                Materials.Silver.getMolten(144),
                CustomItemList.tM_TeslaSecondary.get(1),
                200,
                120);

        // Tesla Primary Coils T0
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.RedstoneAlloy, 8),
                        getItemContainer("MicaInsulatorFoil").get(8) },
                Materials.RedAlloy.getMolten(144),
                CustomItemList.tM_TeslaPrimary_0.get(1),
                200,
                30);

        // Tesla Primary Coils T1
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorMV, 8),
                        getItemContainer("MicaInsulatorFoil").get(12) },
                Materials.Magnesium.getMolten(144),
                CustomItemList.tM_TeslaPrimary_1.get(1),
                200,
                120);

        // Tesla Primary Coils T2
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorHV, 8),
                        getItemContainer("MicaInsulatorFoil").get(16) },
                Materials.Barium.getMolten(144),
                CustomItemList.tM_TeslaPrimary_2.get(1),
                200,
                480);

        // Tesla Primary Coils T3
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorEV, 8),
                        getItemContainer("MicaInsulatorFoil").get(20) },
                Materials.Platinum.getMolten(144),
                CustomItemList.tM_TeslaPrimary_3.get(1),
                200,
                1920);

        // Tesla Primary Coils T4
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorIV, 8),
                        getItemContainer("MicaInsulatorFoil").get(24) },
                Materials.Vanadium.getMolten(144),
                CustomItemList.tM_TeslaPrimary_4.get(1),
                200,
                7680);

        // Tesla Primary Coils T5
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorLuV, 8),
                        getItemContainer("MicaInsulatorFoil").get(28) },
                Materials.Indium.getMolten(144),
                CustomItemList.tM_TeslaPrimary_5.get(1),
                200,
                30720);

        // Tesla Primary Coils T6
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorZPM, 8),
                        getItemContainer("MicaInsulatorFoil").get(32) },
                Materials.Naquadah.getMolten(144),
                CustomItemList.tM_TeslaPrimary_6.get(1),
                200,
                122880);

        // Dynamo Hatches
        {
            // Dynamo Hatches 4A
            {
                // Dynamo EV 4A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hatch_Dynamo_EV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Aluminium, 2),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 2) },
                        Materials.Silver.getMolten(144),
                        CustomItemList.eM_dynamoMulti4_EV.get(1),
                        100,
                        480);

                // Dynamo IV 4A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hatch_Dynamo_IV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Tungsten, 2),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 2) },
                        Materials.Silver.getMolten(144),
                        CustomItemList.eM_dynamoMulti4_IV.get(1),
                        100,
                        1920);

                // Dynamo LuV 4A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hatch_Dynamo_LuV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.VanadiumGallium, 2),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Chrome, 2) },
                        Materials.Silver.getMolten(288),
                        CustomItemList.eM_dynamoMulti4_LuV.get(1),
                        100,
                        7680);

                // Dynamo ZPM 4A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hatch_Dynamo_ZPM.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Naquadah, 2),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 2) },
                        Materials.Silver.getMolten(576),
                        CustomItemList.eM_dynamoMulti4_ZPM.get(1),
                        100,
                        30720);

                // Dynamo UV 4A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hatch_Dynamo_UV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 2),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 2) },
                        Materials.Silver.getMolten(1152),
                        CustomItemList.eM_dynamoMulti4_UV.get(1),
                        100,
                        122880);

                // Dynamo UHV 4A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hatch_Dynamo_MAX.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 2),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 2) },
                        Materials.Silver.getMolten(2304),
                        CustomItemList.eM_dynamoMulti4_UHV.get(1),
                        100,
                        500000);

                // Dynamo UEV 4A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hatch_Dynamo_UEV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Draconium, 2),
                                GT_OreDictUnificator
                                        .get(OrePrefixes.plate, getOrDefault("Bedrockium", Materials.Neutronium), 2) },
                        Materials.Silver.getMolten(4608),
                        CustomItemList.eM_dynamoMulti4_UEV.get(1),
                        100,
                        2000000);

                // Dynamo UIV 4A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hatch_Dynamo_UIV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NetherStar, 2),
                                GT_OreDictUnificator.get(
                                        OrePrefixes.plate,
                                        getOrDefault("BlackPlutonium", Materials.Neutronium),
                                        2) },
                        Materials.Silver.getMolten(9216),
                        CustomItemList.eM_dynamoMulti4_UIV.get(1),
                        100,
                        8000000);

                // Dynamo UMV 4A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hatch_Dynamo_UMV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Quantium, 2),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.SpaceTime, 2) },
                        Materials.Silver.getMolten(9216),
                        CustomItemList.eM_dynamoMulti4_UMV.get(1),
                        100,
                        32000000);

                // Dynamo UXV 4A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hatch_Dynamo_UXV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.BlackPlutonium, 2),
                                GT_OreDictUnificator.get(
                                        OrePrefixes.plate,
                                        Materials.MagnetohydrodynamicallyConstrainedStarMatter,
                                        1),
                                GT_OreDictUnificator.get("plateShirabon", 1L) },
                        Materials.Silver.getMolten(9216),
                        CustomItemList.eM_dynamoMulti4_UXV.get(1),
                        100,
                        (int) TierEU.RECIPE_UMV);
            }

            // Dynamo Hatches 16A
            {
                // Dynamo EV 16A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { CustomItemList.eM_dynamoMulti4_EV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Aluminium, 2),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 4) },
                        Materials.Electrum.getMolten(144),
                        CustomItemList.eM_dynamoMulti16_EV.get(1),
                        200,
                        480);

                // Dynamo IV 16A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Transformer_LuV_IV.get(1), CustomItemList.eM_dynamoMulti4_IV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Tungsten, 2),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 4) },
                        Materials.Electrum.getMolten(144),
                        CustomItemList.eM_dynamoMulti16_IV.get(1),
                        200,
                        1920);

                // Dynamo LuV 16A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Transformer_ZPM_LuV.get(1),
                                CustomItemList.eM_dynamoMulti4_LuV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.VanadiumGallium, 2),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Chrome, 4) },
                        Materials.Electrum.getMolten(288),
                        CustomItemList.eM_dynamoMulti16_LuV.get(1),
                        200,
                        7680);

                // Dynamo ZPM 16A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Transformer_UV_ZPM.get(1), CustomItemList.eM_dynamoMulti4_ZPM.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Naquadah, 2),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 4) },
                        Materials.Electrum.getMolten(576),
                        CustomItemList.eM_dynamoMulti16_ZPM.get(1),
                        200,
                        30720);

                // Dynamo UV 16A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Transformer_MAX_UV.get(1), CustomItemList.eM_dynamoMulti4_UV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NaquadahAlloy, 2),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 4) },
                        Materials.Electrum.getMolten(1152),
                        CustomItemList.eM_dynamoMulti16_UV.get(1),
                        200,
                        122880);

                // Dynamo UHV 16A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Transformer_UEV_UHV").get(1),
                                CustomItemList.eM_dynamoMulti4_UHV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.SuperconductorUHV, 2),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 4) },
                        Materials.Electrum.getMolten(2304),
                        CustomItemList.eM_dynamoMulti16_UHV.get(1),
                        200,
                        500000);

                // Dynamo UEV 16A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Transformer_UIV_UEV").get(1),
                                CustomItemList.eM_dynamoMulti4_UEV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Draconium, 2),
                                GT_OreDictUnificator
                                        .get(OrePrefixes.plate, getOrDefault("Bedrockium", Materials.Neutronium), 4) },
                        Materials.Electrum.getMolten(4608),
                        CustomItemList.eM_dynamoMulti16_UEV.get(1),
                        200,
                        2000000);

                // Dynamo UIV 16A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Transformer_UMV_UIV").get(1),
                                CustomItemList.eM_dynamoMulti4_UIV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NetherStar, 2),
                                GT_OreDictUnificator.get(
                                        OrePrefixes.plate,
                                        getOrDefault("BlackPlutonium", Materials.Neutronium),
                                        4) },
                        Materials.Electrum.getMolten(9216),
                        CustomItemList.eM_dynamoMulti16_UIV.get(1),
                        200,
                        8000000);

                // Dynamo UMV 16A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Transformer_UXV_UMV").get(1),
                                CustomItemList.eM_dynamoMulti4_UMV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Quantium, 2),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.SpaceTime, 4) },
                        Materials.Electrum.getMolten(9216),
                        CustomItemList.eM_dynamoMulti16_UMV.get(1),
                        200,
                        32000000);

                // Dynamo UXV 16A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Transformer_MAX_UXV").get(1),
                                CustomItemList.eM_dynamoMulti4_UXV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.BlackPlutonium, 2),
                                GT_OreDictUnificator.get(
                                        OrePrefixes.plate,
                                        Materials.MagnetohydrodynamicallyConstrainedStarMatter,
                                        2),
                                GT_OreDictUnificator.get("plateShirabon", 2L) },
                        Materials.Electrum.getMolten(9216),
                        CustomItemList.eM_dynamoMulti16_UXV.get(1),
                        200,
                        (int) TierEU.RECIPE_UMV);
            }

            // Dynamo Hatches 64A
            {
                // Dynamo EV 64A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { CustomItemList.eM_dynamoMulti16_EV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Aluminium, 2),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 6) },
                        Materials.Tungsten.getMolten(144),
                        CustomItemList.eM_dynamoMulti64_EV.get(1),
                        400,
                        480);

                // Dynamo IV 64A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("WetTransformer_LuV_IV").get(1),
                                CustomItemList.eM_dynamoMulti16_IV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Tungsten, 2),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 6) },
                        Materials.Tungsten.getMolten(144),
                        CustomItemList.eM_dynamoMulti64_IV.get(1),
                        400,
                        1920);

                // Dynamo LuV 64A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("WetTransformer_ZPM_LuV").get(1),
                                CustomItemList.eM_dynamoMulti16_LuV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.VanadiumGallium, 2),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Chrome, 6) },
                        Materials.Tungsten.getMolten(288),
                        CustomItemList.eM_dynamoMulti64_LuV.get(1),
                        400,
                        7680);

                // Dynamo ZPM 64A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("WetTransformer_UV_ZPM").get(1),
                                CustomItemList.eM_dynamoMulti16_ZPM.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Naquadah, 2),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 6) },
                        Materials.Tungsten.getMolten(576),
                        CustomItemList.eM_dynamoMulti64_ZPM.get(1),
                        400,
                        30720);

                // Dynamo UV 64A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("WetTransformer_UHV_UV").get(1),
                                CustomItemList.eM_dynamoMulti16_UV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.NaquadahAlloy, 2),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 6) },
                        Materials.Tungsten.getMolten(1152),
                        CustomItemList.eM_dynamoMulti64_UV.get(1),
                        400,
                        122880);

                // Dynamo UHV 64A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("WetTransformer_UEV_UHV").get(1),
                                CustomItemList.eM_dynamoMulti16_UHV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.SuperconductorUHV, 2),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 6) },
                        Materials.Tungsten.getMolten(2304),
                        CustomItemList.eM_dynamoMulti64_UHV.get(1),
                        400,
                        2000000);

                // Dynamo UEV 64A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("WetTransformer_UIV_UEV").get(1),
                                CustomItemList.eM_dynamoMulti16_UEV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Draconium, 2),
                                GT_OreDictUnificator
                                        .get(OrePrefixes.plate, getOrDefault("Bedrockium", Materials.Neutronium), 6) },
                        Materials.Tungsten.getMolten(4608),
                        CustomItemList.eM_dynamoMulti64_UEV.get(1),
                        400,
                        2000000);

                // Dynamo UIV 64A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("WetTransformer_UMV_UIV").get(1),
                                CustomItemList.eM_dynamoMulti16_UIV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.NetherStar, 2),
                                GT_OreDictUnificator.get(
                                        OrePrefixes.plate,
                                        getOrDefault("BlackPlutonium", Materials.Neutronium),
                                        6) },
                        Materials.Tungsten.getMolten(9216),
                        CustomItemList.eM_dynamoMulti64_UIV.get(1),
                        400,
                        8000000);

                // Dynamo UMV 64A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("WetTransformer_UXV_UMV").get(1),
                                CustomItemList.eM_dynamoMulti16_UMV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Quantium, 2),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.SpaceTime, 6) },
                        Materials.Tungsten.getMolten(9216),
                        CustomItemList.eM_dynamoMulti64_UMV.get(1),
                        400,
                        32000000);

                // Dynamo UXV 64A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("WetTransformer_MAX_UXV").get(1),
                                CustomItemList.eM_dynamoMulti16_UXV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.BlackPlutonium, 2),
                                GT_OreDictUnificator.get(
                                        OrePrefixes.plate,
                                        Materials.MagnetohydrodynamicallyConstrainedStarMatter,
                                        3),
                                GT_OreDictUnificator.get("plateShirabon", 3L) },
                        Materials.Tungsten.getMolten(9216),
                        CustomItemList.eM_dynamoMulti64_UXV.get(1),
                        400,
                        (int) TierEU.RECIPE_UMV);
            }
        }

        // Energy Hatches
        {
            // Energy Hatches 4A
            {
                // Energy Hatch EV 4A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hatch_Energy_EV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Aluminium, 2),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 2) },
                        Materials.Silver.getMolten(144),
                        CustomItemList.eM_energyMulti4_EV.get(1),
                        100,
                        480);

                // Energy Hatch IV 4A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hatch_Energy_IV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Tungsten, 2),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 2) },
                        Materials.Silver.getMolten(144),
                        CustomItemList.eM_energyMulti4_IV.get(1),
                        100,
                        1920);

                // Energy Hatch LuV 4A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hatch_Energy_LuV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.VanadiumGallium, 2),
                                GT_OreDictUnificator.get(
                                        OrePrefixes.plate,
                                        getOrDefault("Rhodium-PlatedPalladium", Materials.Chrome),
                                        2) },
                        Materials.Silver.getMolten(288),
                        CustomItemList.eM_energyMulti4_LuV.get(1),
                        100,
                        7680);

                // Energy Hatch ZPM 4A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hatch_Energy_ZPM.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Naquadah, 2),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 2) },
                        Materials.Silver.getMolten(576),
                        CustomItemList.eM_energyMulti4_ZPM.get(1),
                        100,
                        30720);

                // Energy Hatch UV 4A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hatch_Energy_UV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 2),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 2) },
                        Materials.Silver.getMolten(1152),
                        CustomItemList.eM_energyMulti4_UV.get(1),
                        100,
                        122880);

                // Energy Hatch UHV 4A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hatch_Energy_MAX.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 2),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 2) },
                        Materials.Silver.getMolten(2304),
                        CustomItemList.eM_energyMulti4_UHV.get(1),
                        100,
                        500000);

                // Energy Hatch UEV 4A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hatch_Energy_UEV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Draconium, 2),
                                GT_OreDictUnificator
                                        .get(OrePrefixes.plate, getOrDefault("Bedrockium", Materials.Neutronium), 2) },
                        Materials.Silver.getMolten(4608),
                        CustomItemList.eM_energyMulti4_UEV.get(1),
                        100,
                        2000000);

                // Energy Hatch UIV 4A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hatch_Energy_UIV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NetherStar, 2),
                                GT_OreDictUnificator.get(
                                        OrePrefixes.plate,
                                        getOrDefault("BlackPlutonium", Materials.Neutronium),
                                        2) },
                        Materials.Silver.getMolten(9216),
                        CustomItemList.eM_energyMulti4_UIV.get(1),
                        100,
                        8000000);

                // Energy Hatch UMV 4A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hatch_Energy_UMV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Quantium, 2),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.SpaceTime, 2) },
                        Materials.Silver.getMolten(9216),
                        CustomItemList.eM_energyMulti4_UMV.get(1),
                        100,
                        32000000);

                // Energy Hatch UXV 4A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hatch_Energy_UXV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.BlackPlutonium, 2),
                                GT_OreDictUnificator.get(
                                        OrePrefixes.plate,
                                        Materials.MagnetohydrodynamicallyConstrainedStarMatter,
                                        1),
                                GT_OreDictUnificator.get("plateShirabon", 1L) },
                        Materials.Silver.getMolten(9216),
                        CustomItemList.eM_energyMulti4_UXV.get(1),
                        100,
                        (int) TierEU.RECIPE_UMV);
            }

            // Energy Hatches 16A
            {
                // Energy Hatch EV 16A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { CustomItemList.eM_energyMulti4_EV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Aluminium, 2),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 4) },
                        Materials.Electrum.getMolten(144),
                        CustomItemList.eM_energyMulti16_EV.get(1),
                        200,
                        480);

                // Energy Hatch IV 16A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Transformer_LuV_IV.get(1), CustomItemList.eM_energyMulti4_IV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Tungsten, 2),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 4) },
                        Materials.Electrum.getMolten(144),
                        CustomItemList.eM_energyMulti16_IV.get(1),
                        200,
                        1920);

                // Energy Hatch LuV 16A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Transformer_ZPM_LuV.get(1),
                                CustomItemList.eM_energyMulti4_LuV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.VanadiumGallium, 2),
                                GT_OreDictUnificator.get(
                                        OrePrefixes.plate,
                                        getOrDefault("Rhodium-PlatedPalladium", Materials.Chrome),
                                        4) },
                        Materials.Electrum.getMolten(288),
                        CustomItemList.eM_energyMulti16_LuV.get(1),
                        200,
                        7680);

                // Energy Hatch ZPM 16A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Transformer_UV_ZPM.get(1), CustomItemList.eM_energyMulti4_ZPM.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Naquadah, 2),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 4) },
                        Materials.Electrum.getMolten(576),
                        CustomItemList.eM_energyMulti16_ZPM.get(1),
                        200,
                        30720);

                // Energy Hatch UV 16A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Transformer_MAX_UV.get(1), CustomItemList.eM_energyMulti4_UV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NaquadahAlloy, 2),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 4) },
                        Materials.Electrum.getMolten(1152),
                        CustomItemList.eM_energyMulti16_UV.get(1),
                        200,
                        122880);

                // Energy Hatch UHV 16A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Transformer_UEV_UHV").get(1),
                                CustomItemList.eM_energyMulti4_UHV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.SuperconductorUHV, 2),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 4) },
                        Materials.Electrum.getMolten(2304),
                        CustomItemList.eM_energyMulti16_UHV.get(1),
                        200,
                        500000);

                // Energy Hatch UEV 16A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Transformer_UIV_UEV").get(1),
                                CustomItemList.eM_energyMulti4_UEV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Draconium, 2),
                                GT_OreDictUnificator
                                        .get(OrePrefixes.plate, getOrDefault("Bedrockium", Materials.Neutronium), 4) },
                        Materials.Electrum.getMolten(4608),
                        CustomItemList.eM_energyMulti16_UEV.get(1),
                        200,
                        2000000);

                // Energy Hatch UIV 16A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Transformer_UMV_UIV").get(1),
                                CustomItemList.eM_energyMulti4_UIV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NetherStar, 2),
                                GT_OreDictUnificator.get(
                                        OrePrefixes.plate,
                                        getOrDefault("BlackPlutonium", Materials.Neutronium),
                                        4) },
                        Materials.Electrum.getMolten(9216),
                        CustomItemList.eM_energyMulti16_UIV.get(1),
                        200,
                        8000000);

                // Energy Hatch UMV 16A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Transformer_UXV_UMV").get(1),
                                CustomItemList.eM_energyMulti4_UMV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Quantium, 2),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.SpaceTime, 4) },
                        Materials.Electrum.getMolten(9216),
                        CustomItemList.eM_energyMulti16_UMV.get(1),
                        200,
                        32000000);

                // Energy Hatch UXV 16A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Transformer_MAX_UXV").get(1),
                                CustomItemList.eM_energyMulti4_UXV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.BlackPlutonium, 2),
                                GT_OreDictUnificator.get(
                                        OrePrefixes.plate,
                                        Materials.MagnetohydrodynamicallyConstrainedStarMatter,
                                        2),
                                GT_OreDictUnificator.get("plateShirabon", 2L) },
                        Materials.Electrum.getMolten(9216),
                        CustomItemList.eM_energyMulti16_UXV.get(1),
                        200,
                        (int) TierEU.RECIPE_UMV);
            }

            // Energy Hatches 64A
            {
                // Energy Hatch EV 64A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { CustomItemList.eM_energyMulti16_EV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Aluminium, 2),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 6) },
                        Materials.Tungsten.getMolten(144),
                        CustomItemList.eM_energyMulti64_EV.get(1),
                        400,
                        480);

                // Energy Hatch IV 64A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("WetTransformer_LuV_IV").get(1),
                                CustomItemList.eM_energyMulti16_IV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Tungsten, 2),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 6) },
                        Materials.Tungsten.getMolten(144),
                        CustomItemList.eM_energyMulti64_IV.get(1),
                        400,
                        1920);

                // Energy Hatch LuV 64A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("WetTransformer_ZPM_LuV").get(1),
                                CustomItemList.eM_energyMulti16_LuV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.VanadiumGallium, 2),
                                GT_OreDictUnificator.get(
                                        OrePrefixes.plate,
                                        getOrDefault("Rhodium-PlatedPalladium", Materials.Chrome),
                                        6) },
                        Materials.Tungsten.getMolten(288),
                        CustomItemList.eM_energyMulti64_LuV.get(1),
                        400,
                        7680);

                // Energy Hatch ZPM 64A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("WetTransformer_UV_ZPM").get(1),
                                CustomItemList.eM_energyMulti16_ZPM.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Naquadah, 2),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 6) },
                        Materials.Tungsten.getMolten(576),
                        CustomItemList.eM_energyMulti64_ZPM.get(1),
                        400,
                        30720);

                // Energy Hatch UV 64A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("WetTransformer_UHV_UV").get(1),
                                CustomItemList.eM_energyMulti16_UV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.NaquadahAlloy, 2),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 6) },
                        Materials.Tungsten.getMolten(1152),
                        CustomItemList.eM_energyMulti64_UV.get(1),
                        400,
                        122880);

                // Energy Hatch UHV 64A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("WetTransformer_UEV_UHV").get(1),
                                CustomItemList.eM_energyMulti16_UHV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.SuperconductorUHV, 2),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 6) },
                        Materials.Tungsten.getMolten(2304),
                        CustomItemList.eM_energyMulti64_UHV.get(1),
                        400,
                        500000);

                // Energy Hatch UEV 64A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("WetTransformer_UIV_UEV").get(1),
                                CustomItemList.eM_energyMulti16_UEV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Draconium, 2),
                                GT_OreDictUnificator
                                        .get(OrePrefixes.plate, getOrDefault("Bedrockium", Materials.Neutronium), 6) },
                        Materials.Tungsten.getMolten(4608),
                        CustomItemList.eM_energyMulti64_UEV.get(1),
                        400,
                        2000000);

                // Energy Hatch UIV 64A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("WetTransformer_UMV_UIV").get(1),
                                CustomItemList.eM_energyMulti16_UIV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.NetherStar, 2),
                                GT_OreDictUnificator.get(
                                        OrePrefixes.plate,
                                        getOrDefault("BlackPlutonium", Materials.Neutronium),
                                        6) },
                        Materials.Tungsten.getMolten(9216),
                        CustomItemList.eM_energyMulti64_UIV.get(1),
                        400,
                        8000000);

                // Energy Hatch UMV 64A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("WetTransformer_UXV_UMV").get(1),
                                CustomItemList.eM_energyMulti16_UMV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Quantium, 2),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.SpaceTime, 6) },
                        Materials.Tungsten.getMolten(9216),
                        CustomItemList.eM_energyMulti64_UMV.get(1),
                        400,
                        32000000);

                // Energy Hatch UXV 64A
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("WetTransformer_MAX_UXV").get(1),
                                CustomItemList.eM_energyMulti16_UXV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.BlackPlutonium, 2),
                                GT_OreDictUnificator.get(
                                        OrePrefixes.plate,
                                        Materials.MagnetohydrodynamicallyConstrainedStarMatter,
                                        3),
                                GT_OreDictUnificator.get("plateShirabon", 3L) },
                        Materials.Tungsten.getMolten(9216),
                        CustomItemList.eM_energyMulti64_UXV.get(1),
                        400,
                        (int) TierEU.RECIPE_UMV);

            }
        }

        // Buck Converter IV-UIV
        if (BartWorks.isModLoaded()) {
            // Buck Converter IV
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { ItemList.Transformer_LuV_IV.get(1), getItemContainer("Display").get(1),
                            GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Elite, 2),
                            GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 2),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.TungstenSteel, 4),
                            getModItem(BartWorks.ID, "BW_GlasBlocks", 2L, 2) },
                    Materials.TungstenSteel.getMolten(288),
                    CustomItemList.Machine_BuckConverter_IV.get(1),
                    100,
                    7680);

            // Buck Converter LuV
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { ItemList.Transformer_ZPM_LuV.get(1), getItemContainer("Display").get(1),
                            GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 2),
                            GT_OreDictUnificator.get(
                                    OrePrefixes.plate,
                                    getOrDefault("Rhodium-PlatedPalladium", Materials.Chrome),
                                    2),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.NiobiumTitanium, 4),
                            getModItem(BartWorks.ID, "BW_GlasBlocks", 2L, 3) },
                    new FluidStack(FluidRegistry.getFluid("molten.rhodium-plated palladium"), 288),
                    CustomItemList.Machine_BuckConverter_LuV.get(1),
                    100,
                    30720);

            // Buck Converter ZPM
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { ItemList.Transformer_UV_ZPM.get(1), getItemContainer("Display").get(1),
                            GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Ultimate, 2),
                            GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 2),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.TungstenSteel, 4),
                            getModItem(BartWorks.ID, "BW_GlasBlocks", 2L, 4) },
                    Materials.Iridium.getMolten(288),
                    CustomItemList.Machine_BuckConverter_ZPM.get(1),
                    100,
                    122880);

            // Buck Converter UV
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { ItemList.Transformer_MAX_UV.get(1), getItemContainer("Display").get(1),
                            GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.SuperconductorUHV, 2),
                            GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 2),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Naquadah, 4),
                            getModItem(BartWorks.ID, "BW_GlasBlocks", 2L, 5) },
                    Materials.Osmium.getMolten(288),
                    CustomItemList.Machine_BuckConverter_UV.get(1),
                    100,
                    500000);

            // Buck Converter UHV
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { getItemContainer("Transformer_UEV_UHV").get(1),
                            getItemContainer("Display").get(1),
                            GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Infinite, 2),
                            GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 2),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.ElectrumFlux, 4),
                            getModItem(BartWorks.ID, "BW_GlasBlocks", 4L, 5) },
                    Materials.Neutronium.getMolten(288),
                    CustomItemList.Machine_BuckConverter_UHV.get(1),
                    100,
                    2000000);

            // Buck Converter UEV
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { getItemContainer("Transformer_UIV_UEV").get(1),
                            getItemContainer("Display").get(1),
                            GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Bio, 2),
                            GT_OreDictUnificator
                                    .get(OrePrefixes.plate, getOrDefault("Bedrockium", Materials.Neutronium), 2),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Bedrockium, 4),
                            getModItem(BartWorks.ID, "BW_GlasBlocks", 8L, 5) },
                    getOrDefault("Bedrockium", Materials.Neutronium).getMolten(288),
                    CustomItemList.Machine_BuckConverter_UEV.get(1),
                    100,
                    8000000);

            // Buck Converter UIV
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { getItemContainer("Transformer_UMV_UIV").get(1),
                            getItemContainer("Display").get(1),
                            GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Optical, 2),
                            GT_OreDictUnificator
                                    .get(OrePrefixes.plate, getOrDefault("BlackPlutonium", Materials.Neutronium), 2),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Draconium, 4),
                            getModItem(BartWorks.ID, "BW_GlasBlocks", 16L, 5) },
                    getOrDefault("BlackPlutonium", Materials.Neutronium).getMolten(288),
                    CustomItemList.Machine_BuckConverter_UIV.get(1),
                    200,
                    8000000);
        }

        // Laser Dynamo
        {
            // Laser Dynamo IV-UXV 256/t
            {
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_IV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1),
                                ItemList.Emitter_IV.get(1), ItemList.Electric_Pump_IV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.TungstenSteel, 2),
                                GT_Utility.getIntegratedCircuit(1) },
                        null,
                        CustomItemList.eM_dynamoTunnel1_IV.get(1),
                        1000,
                        7680);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_LuV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1),
                                ItemList.Emitter_LuV.get(1), ItemList.Electric_Pump_LuV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.VanadiumGallium, 2),
                                GT_Utility.getIntegratedCircuit(1) },
                        null,
                        CustomItemList.eM_dynamoTunnel1_LuV.get(1),
                        1000,
                        30720);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_ZPM.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1),
                                ItemList.Emitter_ZPM.get(1), ItemList.Electric_Pump_ZPM.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Naquadah, 2),
                                GT_Utility.getIntegratedCircuit(1) },
                        null,
                        CustomItemList.eM_dynamoTunnel1_ZPM.get(1),
                        1000,
                        122880);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_UV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1),
                                ItemList.Emitter_UV.get(1), ItemList.Electric_Pump_UV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.NaquadahAlloy, 2),
                                GT_Utility.getIntegratedCircuit(1) },
                        null,
                        CustomItemList.eM_dynamoTunnel1_UV.get(1),
                        1000,
                        500000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_MAX.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1),
                                ItemList.Emitter_UHV.get(1), ItemList.Electric_Pump_UHV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Bedrockium, 2),
                                GT_Utility.getIntegratedCircuit(1) },
                        null,
                        CustomItemList.eM_dynamoTunnel1_UHV.get(1),
                        1000,
                        2000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UEV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1),
                                ItemList.Emitter_UEV.get(1), ItemList.Electric_Pump_UEV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Draconium, 2),
                                GT_Utility.getIntegratedCircuit(1) },
                        null,
                        CustomItemList.eM_dynamoTunnel1_UEV.get(1),
                        1000,
                        8000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UIV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1),
                                ItemList.Emitter_UIV.get(1), ItemList.Electric_Pump_UIV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.NetherStar, 2),
                                GT_Utility.getIntegratedCircuit(1) },
                        null,
                        CustomItemList.eM_dynamoTunnel1_UIV.get(1),
                        1000,
                        32000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UMV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1),
                                ItemList.Emitter_UMV.get(1), ItemList.Electric_Pump_UMV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Quantium, 2),
                                GT_Utility.getIntegratedCircuit(1) },
                        null,
                        CustomItemList.eM_dynamoTunnel1_UMV.get(1),
                        1000,
                        128000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UXV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1),
                                ItemList.Emitter_UXV.get(1), ItemList.Electric_Pump_UXV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.BlackPlutonium, 2),
                                GT_Utility.getIntegratedCircuit(1) },
                        null,
                        CustomItemList.eM_dynamoTunnel1_UXV.get(1),
                        1000,
                        (int) TierEU.RECIPE_UXV);
            }

            // Laser Dynamo IV-UXV 1024/t
            {
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_IV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2),
                                ItemList.Emitter_IV.get(2), ItemList.Electric_Pump_IV.get(2),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.TungstenSteel, 4),
                                GT_Utility.getIntegratedCircuit(2) },
                        null,
                        CustomItemList.eM_dynamoTunnel2_IV.get(1),
                        2000,
                        7680);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_LuV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2),
                                ItemList.Emitter_LuV.get(2), ItemList.Electric_Pump_LuV.get(2),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.VanadiumGallium, 4),
                                GT_Utility.getIntegratedCircuit(2) },
                        null,
                        CustomItemList.eM_dynamoTunnel2_LuV.get(1),
                        2000,
                        30720);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_ZPM.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2),
                                ItemList.Emitter_ZPM.get(2), ItemList.Electric_Pump_ZPM.get(2),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Naquadah, 4),
                                GT_Utility.getIntegratedCircuit(2) },
                        null,
                        CustomItemList.eM_dynamoTunnel2_ZPM.get(1),
                        2000,
                        122880);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_UV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2),
                                ItemList.Emitter_UV.get(2), ItemList.Electric_Pump_UV.get(2),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.NaquadahAlloy, 4),
                                GT_Utility.getIntegratedCircuit(2) },
                        null,
                        CustomItemList.eM_dynamoTunnel2_UV.get(1),
                        2000,
                        500000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_MAX.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2),
                                ItemList.Emitter_UHV.get(2), ItemList.Electric_Pump_UHV.get(2),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Bedrockium, 4),
                                GT_Utility.getIntegratedCircuit(2) },
                        null,
                        CustomItemList.eM_dynamoTunnel2_UHV.get(1),
                        2000,
                        2000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UEV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2),
                                ItemList.Emitter_UEV.get(2), ItemList.Electric_Pump_UEV.get(2),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Draconium, 4),
                                GT_Utility.getIntegratedCircuit(2) },
                        null,
                        CustomItemList.eM_dynamoTunnel2_UEV.get(1),
                        2000,
                        8000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UIV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2),
                                ItemList.Emitter_UIV.get(2), ItemList.Electric_Pump_UIV.get(2),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.NetherStar, 4),
                                GT_Utility.getIntegratedCircuit(2) },
                        null,
                        CustomItemList.eM_dynamoTunnel2_UIV.get(1),
                        2000,
                        32000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UMV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2),
                                ItemList.Emitter_UMV.get(2), ItemList.Electric_Pump_UMV.get(2),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Quantium, 4),
                                GT_Utility.getIntegratedCircuit(2) },
                        null,
                        CustomItemList.eM_dynamoTunnel2_UMV.get(1),
                        2000,
                        128000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UXV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2),
                                ItemList.Emitter_UXV.get(2), ItemList.Electric_Pump_UXV.get(2),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.BlackPlutonium, 4),
                                GT_Utility.getIntegratedCircuit(2) },
                        null,
                        CustomItemList.eM_dynamoTunnel2_UXV.get(1),
                        2000,
                        (int) TierEU.RECIPE_UXV);
            }

            // Laser Dynamo IV-UXV 4096/t
            {
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_IV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4),
                                ItemList.Emitter_IV.get(4), ItemList.Electric_Pump_IV.get(4),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.TungstenSteel, 4),
                                GT_Utility.getIntegratedCircuit(3) },
                        null,
                        CustomItemList.eM_dynamoTunnel3_IV.get(1),
                        4000,
                        7680);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_LuV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4),
                                ItemList.Emitter_LuV.get(4), ItemList.Electric_Pump_LuV.get(4),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.VanadiumGallium, 4),
                                GT_Utility.getIntegratedCircuit(3) },
                        null,
                        CustomItemList.eM_dynamoTunnel3_LuV.get(1),
                        4000,
                        30720);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_ZPM.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4),
                                ItemList.Emitter_ZPM.get(4), ItemList.Electric_Pump_ZPM.get(4),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Naquadah, 4),
                                GT_Utility.getIntegratedCircuit(3) },
                        null,
                        CustomItemList.eM_dynamoTunnel3_ZPM.get(1),
                        4000,
                        122880);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_UV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4),
                                ItemList.Emitter_UV.get(4), ItemList.Electric_Pump_UV.get(4),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 4),
                                GT_Utility.getIntegratedCircuit(3) },
                        null,
                        CustomItemList.eM_dynamoTunnel3_UV.get(1),
                        4000,
                        500000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_MAX.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4),
                                ItemList.Emitter_UHV.get(4), ItemList.Electric_Pump_UHV.get(4),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Bedrockium, 4),
                                GT_Utility.getIntegratedCircuit(3) },
                        null,
                        CustomItemList.eM_dynamoTunnel3_UHV.get(1),
                        4000,
                        2000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UEV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4),
                                ItemList.Emitter_UEV.get(4), ItemList.Electric_Pump_UEV.get(4),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Draconium, 4),
                                GT_Utility.getIntegratedCircuit(3) },
                        null,
                        CustomItemList.eM_dynamoTunnel3_UEV.get(1),
                        4000,
                        8000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UIV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4),
                                ItemList.Emitter_UIV.get(4), ItemList.Electric_Pump_UIV.get(4),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NetherStar, 4),
                                GT_Utility.getIntegratedCircuit(3) },
                        null,
                        CustomItemList.eM_dynamoTunnel3_UIV.get(1),
                        4000,
                        32000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UMV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4),
                                ItemList.Emitter_UMV.get(4), ItemList.Electric_Pump_UMV.get(4),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Quantium, 4),
                                GT_Utility.getIntegratedCircuit(3) },
                        null,
                        CustomItemList.eM_dynamoTunnel3_UMV.get(1),
                        4000,
                        128000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UXV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4),
                                ItemList.Emitter_UXV.get(4), ItemList.Electric_Pump_UXV.get(4),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.BlackPlutonium, 4),
                                GT_Utility.getIntegratedCircuit(3) },
                        null,
                        CustomItemList.eM_dynamoTunnel3_UXV.get(1),
                        4000,
                        (int) TierEU.RECIPE_UXV);
            }

            // Laser Dynamo IV-UXV 16384/t
            {
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_IV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8),
                                ItemList.Emitter_IV.get(8), ItemList.Electric_Pump_IV.get(8),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.TungstenSteel, 8),
                                GT_Utility.getIntegratedCircuit(4) },
                        null,
                        CustomItemList.eM_dynamoTunnel4_IV.get(1),
                        8000,
                        7680);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_LuV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8),
                                ItemList.Emitter_LuV.get(8), ItemList.Electric_Pump_LuV.get(8),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.VanadiumGallium, 8),
                                GT_Utility.getIntegratedCircuit(4) },
                        null,
                        CustomItemList.eM_dynamoTunnel4_LuV.get(1),
                        8000,
                        30720);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_ZPM.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8),
                                ItemList.Emitter_ZPM.get(8), ItemList.Electric_Pump_ZPM.get(8),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Naquadah, 8),
                                GT_Utility.getIntegratedCircuit(4) },
                        null,
                        CustomItemList.eM_dynamoTunnel4_ZPM.get(1),
                        8000,
                        122880);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_UV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8),
                                ItemList.Emitter_UV.get(8), ItemList.Electric_Pump_UV.get(8),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 8),
                                GT_Utility.getIntegratedCircuit(4) },
                        null,
                        CustomItemList.eM_dynamoTunnel4_UV.get(1),
                        8000,
                        500000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_MAX.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8),
                                ItemList.Emitter_UHV.get(8), ItemList.Electric_Pump_UHV.get(8),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Bedrockium, 8),
                                GT_Utility.getIntegratedCircuit(4) },
                        null,
                        CustomItemList.eM_dynamoTunnel4_UHV.get(1),
                        8000,
                        2000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UEV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8),
                                ItemList.Emitter_UEV.get(8), ItemList.Electric_Pump_UEV.get(8),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Draconium, 8),
                                GT_Utility.getIntegratedCircuit(4) },
                        null,
                        CustomItemList.eM_dynamoTunnel4_UEV.get(1),
                        8000,
                        8000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UIV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8),
                                ItemList.Emitter_UIV.get(8), ItemList.Electric_Pump_UIV.get(8),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NetherStar, 8),
                                GT_Utility.getIntegratedCircuit(4) },
                        null,
                        CustomItemList.eM_dynamoTunnel4_UIV.get(1),
                        8000,
                        32000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UMV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4),
                                ItemList.Emitter_UMV.get(8), ItemList.Electric_Pump_UMV.get(8),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Quantium, 8),
                                GT_Utility.getIntegratedCircuit(4) },
                        null,
                        CustomItemList.eM_dynamoTunnel4_UMV.get(1),
                        8000,
                        128000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UXV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4),
                                ItemList.Emitter_UXV.get(8), ItemList.Electric_Pump_UXV.get(8),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.BlackPlutonium, 8),
                                GT_Utility.getIntegratedCircuit(4) },
                        null,
                        CustomItemList.eM_dynamoTunnel4_UXV.get(1),
                        8000,
                        (int) TierEU.RECIPE_UXV);
            }

            // Laser Dynamo IV-UXV 65536/t
            {
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_IV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16),
                                ItemList.Emitter_IV.get(16), ItemList.Electric_Pump_IV.get(16),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.TungstenSteel, 8),
                                GT_Utility.getIntegratedCircuit(5) },
                        null,
                        CustomItemList.eM_dynamoTunnel5_IV.get(1),
                        16000,
                        7680);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_LuV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16),
                                ItemList.Emitter_LuV.get(16), ItemList.Electric_Pump_LuV.get(16),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.VanadiumGallium, 8),
                                GT_Utility.getIntegratedCircuit(5) },
                        null,
                        CustomItemList.eM_dynamoTunnel5_LuV.get(1),
                        16000,
                        30720);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_ZPM.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16),
                                ItemList.Emitter_ZPM.get(16), ItemList.Electric_Pump_ZPM.get(16),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Naquadah, 8),
                                GT_Utility.getIntegratedCircuit(5) },
                        null,
                        CustomItemList.eM_dynamoTunnel5_ZPM.get(1),
                        16000,
                        122880);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_UV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16),
                                ItemList.Emitter_UV.get(16), ItemList.Electric_Pump_UV.get(16),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NaquadahAlloy, 8),
                                GT_Utility.getIntegratedCircuit(5) },
                        null,
                        CustomItemList.eM_dynamoTunnel5_UV.get(1),
                        16000,
                        500000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_MAX.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16),
                                ItemList.Emitter_UHV.get(16), ItemList.Electric_Pump_UHV.get(16),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Bedrockium, 8),
                                GT_Utility.getIntegratedCircuit(5) },
                        null,
                        CustomItemList.eM_dynamoTunnel5_UHV.get(1),
                        16000,
                        2000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UEV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16),
                                ItemList.Emitter_UEV.get(16), ItemList.Electric_Pump_UEV.get(16),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Draconium, 8),
                                GT_Utility.getIntegratedCircuit(5) },
                        null,
                        CustomItemList.eM_dynamoTunnel5_UEV.get(1),
                        16000,
                        8000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UIV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16),
                                ItemList.Emitter_UIV.get(16), ItemList.Electric_Pump_UIV.get(16),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NetherStar, 8),
                                GT_Utility.getIntegratedCircuit(5) },
                        null,
                        CustomItemList.eM_dynamoTunnel5_UIV.get(1),
                        16000,
                        32000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UMV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16),
                                ItemList.Emitter_UMV.get(16), ItemList.Electric_Pump_UMV.get(16),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Quantium, 8),
                                GT_Utility.getIntegratedCircuit(5) },
                        null,
                        CustomItemList.eM_dynamoTunnel5_UMV.get(1),
                        16000,
                        128000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UXV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16),
                                ItemList.Emitter_UXV.get(16), ItemList.Electric_Pump_UXV.get(16),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.BlackPlutonium, 8),
                                GT_Utility.getIntegratedCircuit(5) },
                        null,
                        CustomItemList.eM_dynamoTunnel5_UXV.get(1),
                        16000,
                        (int) TierEU.RECIPE_UXV);
            }

            // Laser Dynamo IV-UXV 262144/t
            {
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_IV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32),
                                ItemList.Emitter_IV.get(32), ItemList.Electric_Pump_IV.get(32),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.TungstenSteel, 16),
                                GT_Utility.getIntegratedCircuit(6) },
                        null,
                        CustomItemList.eM_dynamoTunnel6_IV.get(1),
                        32000,
                        7680);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_LuV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32),
                                ItemList.Emitter_LuV.get(32), ItemList.Electric_Pump_LuV.get(32),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.VanadiumGallium, 16),
                                GT_Utility.getIntegratedCircuit(6) },
                        null,
                        CustomItemList.eM_dynamoTunnel6_LuV.get(1),
                        32000,
                        30720);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_ZPM.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32),
                                ItemList.Emitter_ZPM.get(32), ItemList.Electric_Pump_ZPM.get(32),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Naquadah, 16),
                                GT_Utility.getIntegratedCircuit(6) },
                        null,
                        CustomItemList.eM_dynamoTunnel6_ZPM.get(1),
                        32000,
                        122880);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_UV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32),
                                ItemList.Emitter_UV.get(32), ItemList.Electric_Pump_UV.get(32),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NaquadahAlloy, 16),
                                GT_Utility.getIntegratedCircuit(6) },
                        null,
                        CustomItemList.eM_dynamoTunnel6_UV.get(1),
                        32000,
                        500000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_MAX.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32),
                                ItemList.Emitter_UHV.get(32), ItemList.Electric_Pump_UHV.get(32),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Bedrockium, 16),
                                GT_Utility.getIntegratedCircuit(6) },
                        null,
                        CustomItemList.eM_dynamoTunnel6_UHV.get(1),
                        32000,
                        2000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UEV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32),
                                ItemList.Emitter_UEV.get(32), ItemList.Electric_Pump_UEV.get(32),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Draconium, 16),
                                GT_Utility.getIntegratedCircuit(6) },
                        null,
                        CustomItemList.eM_dynamoTunnel6_UEV.get(1),
                        32000,
                        8000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UIV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32),
                                ItemList.Emitter_UIV.get(32), ItemList.Electric_Pump_UIV.get(32),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NetherStar, 16),
                                GT_Utility.getIntegratedCircuit(6) },
                        null,
                        CustomItemList.eM_dynamoTunnel6_UIV.get(1),
                        32000,
                        32000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UMV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32),
                                ItemList.Emitter_UMV.get(32), ItemList.Electric_Pump_UMV.get(32),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Quantium, 16),
                                GT_Utility.getIntegratedCircuit(6) },
                        null,
                        CustomItemList.eM_dynamoTunnel6_UMV.get(1),
                        32000,
                        128000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UXV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32),
                                ItemList.Emitter_UXV.get(32), ItemList.Electric_Pump_UXV.get(32),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.BlackPlutonium, 16),
                                GT_Utility.getIntegratedCircuit(6) },
                        null,
                        CustomItemList.eM_dynamoTunnel6_UXV.get(1),
                        32000,
                        (int) TierEU.RECIPE_UXV);
            }

            // Laser Dynamo IV-UXV 1048576/t
            {
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_IV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
                                ItemList.Emitter_IV.get(64), ItemList.Electric_Pump_IV.get(64),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.TungstenSteel, 16),
                                GT_Utility.getIntegratedCircuit(7) },
                        null,
                        CustomItemList.eM_dynamoTunnel7_IV.get(1),
                        64000,
                        7680);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_LuV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
                                ItemList.Emitter_LuV.get(64), ItemList.Electric_Pump_LuV.get(64),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.VanadiumGallium, 16),
                                GT_Utility.getIntegratedCircuit(7) },
                        null,
                        CustomItemList.eM_dynamoTunnel7_LuV.get(1),
                        64000,
                        30720);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_ZPM.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
                                ItemList.Emitter_ZPM.get(64), ItemList.Electric_Pump_ZPM.get(64),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Naquadah, 16),
                                GT_Utility.getIntegratedCircuit(7) },
                        null,
                        CustomItemList.eM_dynamoTunnel7_ZPM.get(1),
                        64000,
                        122880);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_UV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
                                ItemList.Emitter_UV.get(64), ItemList.Electric_Pump_UV.get(64),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.NaquadahAlloy, 16),
                                GT_Utility.getIntegratedCircuit(7) },
                        null,
                        CustomItemList.eM_dynamoTunnel7_UV.get(1),
                        64000,
                        500000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_MAX.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
                                ItemList.Emitter_UHV.get(64), ItemList.Electric_Pump_UHV.get(64),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Bedrockium, 16),
                                GT_Utility.getIntegratedCircuit(7) },
                        null,
                        CustomItemList.eM_dynamoTunnel7_UHV.get(1),
                        64000,
                        2000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UEV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
                                ItemList.Emitter_UEV.get(64), ItemList.Electric_Pump_UEV.get(64),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Draconium, 16),
                                GT_Utility.getIntegratedCircuit(7) },
                        null,
                        CustomItemList.eM_dynamoTunnel7_UEV.get(1),
                        64000,
                        8000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UIV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
                                ItemList.Emitter_UIV.get(64), ItemList.Electric_Pump_UIV.get(64),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.NetherStar, 16),
                                GT_Utility.getIntegratedCircuit(7) },
                        null,
                        CustomItemList.eM_dynamoTunnel7_UIV.get(1),
                        64000,
                        32000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UMV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
                                ItemList.Emitter_UMV.get(64), ItemList.Electric_Pump_UMV.get(64),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Quantium, 16),
                                GT_Utility.getIntegratedCircuit(7) },
                        null,
                        CustomItemList.eM_dynamoTunnel7_UMV.get(1),
                        64000,
                        128000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UXV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
                                ItemList.Emitter_UXV.get(64), ItemList.Electric_Pump_UXV.get(64),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.BlackPlutonium, 16),
                                GT_Utility.getIntegratedCircuit(7) },
                        null,
                        CustomItemList.eM_dynamoTunnel7_UXV.get(1),
                        64000,
                        (int) TierEU.RECIPE_UXV);
            }
        }

        // Laser Target
        {
            // Laser Target IV-UXV 256/t
            {
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_IV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1),
                                ItemList.Sensor_IV.get(1), ItemList.Electric_Pump_IV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.TungstenSteel, 2),
                                GT_Utility.getIntegratedCircuit(1) },
                        null,
                        CustomItemList.eM_energyTunnel1_IV.get(1),
                        1000,
                        7680);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_LuV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1),
                                ItemList.Sensor_LuV.get(1), ItemList.Electric_Pump_LuV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.VanadiumGallium, 2),
                                GT_Utility.getIntegratedCircuit(1) },
                        null,
                        CustomItemList.eM_energyTunnel1_LuV.get(1),
                        1000,
                        30720);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_ZPM.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1),
                                ItemList.Sensor_ZPM.get(1), ItemList.Electric_Pump_ZPM.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Naquadah, 2),
                                GT_Utility.getIntegratedCircuit(1) },
                        null,
                        CustomItemList.eM_energyTunnel1_ZPM.get(1),
                        1000,
                        122880);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_UV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1),
                                ItemList.Sensor_UV.get(1), ItemList.Electric_Pump_UV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.NaquadahAlloy, 2),
                                GT_Utility.getIntegratedCircuit(1) },
                        null,
                        CustomItemList.eM_energyTunnel1_UV.get(1),
                        1000,
                        500000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_MAX.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1),
                                ItemList.Sensor_UHV.get(1), ItemList.Electric_Pump_UHV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Bedrockium, 2),
                                GT_Utility.getIntegratedCircuit(1) },
                        null,
                        CustomItemList.eM_energyTunnel1_UHV.get(1),
                        1000,
                        2000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UEV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1),
                                ItemList.Sensor_UEV.get(1), ItemList.Electric_Pump_UEV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Draconium, 2),
                                GT_Utility.getIntegratedCircuit(1) },
                        null,
                        CustomItemList.eM_energyTunnel1_UEV.get(1),
                        1000,
                        8000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UIV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1),
                                ItemList.Sensor_UIV.get(1), ItemList.Electric_Pump_UIV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.NetherStar, 2),
                                GT_Utility.getIntegratedCircuit(1) },
                        null,
                        CustomItemList.eM_energyTunnel1_UIV.get(1),
                        1000,
                        32000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UMV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1),
                                ItemList.Sensor_UMV.get(1), ItemList.Electric_Pump_UMV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Quantium, 2),
                                GT_Utility.getIntegratedCircuit(1) },
                        null,
                        CustomItemList.eM_energyTunnel1_UMV.get(1),
                        1000,
                        128000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UXV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1),
                                ItemList.Sensor_UXV.get(1), ItemList.Electric_Pump_UXV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.BlackPlutonium, 2),
                                GT_Utility.getIntegratedCircuit(1) },
                        null,
                        CustomItemList.eM_energyTunnel1_UXV.get(1),
                        1000,
                        (int) TierEU.RECIPE_UXV);
            }

            // Laser Target IV-UXV 1024/t
            {
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_IV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2),
                                ItemList.Sensor_IV.get(2), ItemList.Electric_Pump_IV.get(2),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.TungstenSteel, 4),
                                GT_Utility.getIntegratedCircuit(2) },
                        null,
                        CustomItemList.eM_energyTunnel2_IV.get(1),
                        2000,
                        7680);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_LuV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2),
                                ItemList.Sensor_LuV.get(2), ItemList.Electric_Pump_LuV.get(2),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.VanadiumGallium, 4),
                                GT_Utility.getIntegratedCircuit(2) },
                        null,
                        CustomItemList.eM_energyTunnel2_LuV.get(1),
                        2000,
                        30720);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_ZPM.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2),
                                ItemList.Sensor_ZPM.get(2), ItemList.Electric_Pump_ZPM.get(2),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Naquadah, 4),
                                GT_Utility.getIntegratedCircuit(2) },
                        null,
                        CustomItemList.eM_energyTunnel2_ZPM.get(1),
                        2000,
                        122880);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_UV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2),
                                ItemList.Sensor_UV.get(2), ItemList.Electric_Pump_UV.get(2),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.NaquadahAlloy, 4),
                                GT_Utility.getIntegratedCircuit(2) },
                        null,
                        CustomItemList.eM_energyTunnel2_UV.get(1),
                        2000,
                        500000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_MAX.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2),
                                ItemList.Sensor_UHV.get(2), ItemList.Electric_Pump_UHV.get(2),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Bedrockium, 4),
                                GT_Utility.getIntegratedCircuit(2) },
                        null,
                        CustomItemList.eM_energyTunnel2_UHV.get(1),
                        2000,
                        2000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UEV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2),
                                ItemList.Sensor_UEV.get(2), ItemList.Electric_Pump_UEV.get(2),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Draconium, 4),
                                GT_Utility.getIntegratedCircuit(2) },
                        null,
                        CustomItemList.eM_energyTunnel2_UEV.get(1),
                        2000,
                        8000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UIV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2),
                                ItemList.Sensor_UIV.get(2), ItemList.Electric_Pump_UIV.get(2),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.NetherStar, 4),
                                GT_Utility.getIntegratedCircuit(2) },
                        null,
                        CustomItemList.eM_energyTunnel2_UIV.get(1),
                        2000,
                        32000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UMV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2),
                                ItemList.Sensor_UMV.get(2), ItemList.Electric_Pump_UMV.get(2),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Quantium, 4),
                                GT_Utility.getIntegratedCircuit(2) },
                        null,
                        CustomItemList.eM_energyTunnel2_UMV.get(1),
                        2000,
                        128000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UXV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2),
                                ItemList.Sensor_UXV.get(2), ItemList.Electric_Pump_UXV.get(2),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.BlackPlutonium, 4),
                                GT_Utility.getIntegratedCircuit(2) },
                        null,
                        CustomItemList.eM_energyTunnel2_UXV.get(1),
                        2000,
                        (int) TierEU.RECIPE_UXV);
            }

            // Laser Target IV-UXV 4096/t
            {
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_IV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4),
                                ItemList.Sensor_IV.get(4), ItemList.Electric_Pump_IV.get(4),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.TungstenSteel, 4),
                                GT_Utility.getIntegratedCircuit(3) },
                        null,
                        CustomItemList.eM_energyTunnel3_IV.get(1),
                        4000,
                        7680);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_LuV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4),
                                ItemList.Sensor_LuV.get(4), ItemList.Electric_Pump_LuV.get(4),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.VanadiumGallium, 4),
                                GT_Utility.getIntegratedCircuit(3) },
                        null,
                        CustomItemList.eM_energyTunnel3_LuV.get(1),
                        4000,
                        30720);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_ZPM.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4),
                                ItemList.Sensor_ZPM.get(4), ItemList.Electric_Pump_ZPM.get(4),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Naquadah, 4),
                                GT_Utility.getIntegratedCircuit(3) },
                        null,
                        CustomItemList.eM_energyTunnel3_ZPM.get(1),
                        4000,
                        122880);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_UV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4),
                                ItemList.Sensor_UV.get(4), ItemList.Electric_Pump_UV.get(4),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 4),
                                GT_Utility.getIntegratedCircuit(3) },
                        null,
                        CustomItemList.eM_energyTunnel3_UV.get(1),
                        4000,
                        500000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_MAX.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4),
                                ItemList.Sensor_UHV.get(4), ItemList.Electric_Pump_UHV.get(4),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Bedrockium, 4),
                                GT_Utility.getIntegratedCircuit(3) },
                        null,
                        CustomItemList.eM_energyTunnel3_UHV.get(1),
                        4000,
                        2000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UEV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4),
                                ItemList.Sensor_UEV.get(4), ItemList.Electric_Pump_UEV.get(4),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Draconium, 4),
                                GT_Utility.getIntegratedCircuit(3) },
                        null,
                        CustomItemList.eM_energyTunnel3_UEV.get(1),
                        4000,
                        8000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UIV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4),
                                ItemList.Sensor_UIV.get(4), ItemList.Electric_Pump_UIV.get(4),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NetherStar, 4),
                                GT_Utility.getIntegratedCircuit(3) },
                        null,
                        CustomItemList.eM_energyTunnel3_UIV.get(1),
                        4000,
                        32000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UMV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1),
                                ItemList.Sensor_UMV.get(4), ItemList.Electric_Pump_UMV.get(4),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Quantium, 4),
                                GT_Utility.getIntegratedCircuit(3) },
                        null,
                        CustomItemList.eM_energyTunnel3_UMV.get(1),
                        4000,
                        128000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UXV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1),
                                ItemList.Sensor_UXV.get(4), ItemList.Electric_Pump_UXV.get(4),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.BlackPlutonium, 4),
                                GT_Utility.getIntegratedCircuit(3) },
                        null,
                        CustomItemList.eM_energyTunnel3_UXV.get(1),
                        4000,
                        (int) TierEU.RECIPE_UXV);
            }

            // Laser Target IV-UXV 16384/t
            {
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_IV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8),
                                ItemList.Sensor_IV.get(8), ItemList.Electric_Pump_IV.get(8),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.TungstenSteel, 8),
                                GT_Utility.getIntegratedCircuit(4) },
                        null,
                        CustomItemList.eM_energyTunnel4_IV.get(1),
                        8000,
                        7680);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_LuV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8),
                                ItemList.Sensor_LuV.get(8), ItemList.Electric_Pump_LuV.get(8),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.VanadiumGallium, 8),
                                GT_Utility.getIntegratedCircuit(4) },
                        null,
                        CustomItemList.eM_energyTunnel4_LuV.get(1),
                        8000,
                        30720);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_ZPM.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8),
                                ItemList.Sensor_ZPM.get(8), ItemList.Electric_Pump_ZPM.get(8),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Naquadah, 8),
                                GT_Utility.getIntegratedCircuit(4) },
                        null,
                        CustomItemList.eM_energyTunnel4_ZPM.get(1),
                        8000,
                        122880);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_UV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8),
                                ItemList.Sensor_UV.get(8), ItemList.Electric_Pump_UV.get(8),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 8),
                                GT_Utility.getIntegratedCircuit(4) },
                        null,
                        CustomItemList.eM_energyTunnel4_UV.get(1),
                        8000,
                        500000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_MAX.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8),
                                ItemList.Sensor_UHV.get(8), ItemList.Electric_Pump_UHV.get(8),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Bedrockium, 8),
                                GT_Utility.getIntegratedCircuit(4) },
                        null,
                        CustomItemList.eM_energyTunnel4_UHV.get(1),
                        8000,
                        2000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UEV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8),
                                ItemList.Sensor_UEV.get(8), ItemList.Electric_Pump_UEV.get(8),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Draconium, 8),
                                GT_Utility.getIntegratedCircuit(4) },
                        null,
                        CustomItemList.eM_energyTunnel4_UEV.get(1),
                        8000,
                        8000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UIV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8),
                                ItemList.Sensor_UIV.get(8), ItemList.Electric_Pump_UIV.get(8),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NetherStar, 8),
                                GT_Utility.getIntegratedCircuit(4) },
                        null,
                        CustomItemList.eM_energyTunnel4_UIV.get(1),
                        8000,
                        32000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UMV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8),
                                ItemList.Sensor_UMV.get(8), ItemList.Electric_Pump_UMV.get(8),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Quantium, 8),
                                GT_Utility.getIntegratedCircuit(4) },
                        null,
                        CustomItemList.eM_energyTunnel4_UMV.get(1),
                        8000,
                        128000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UXV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8),
                                ItemList.Sensor_UXV.get(8), ItemList.Electric_Pump_UXV.get(8),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.BlackPlutonium, 8),
                                GT_Utility.getIntegratedCircuit(4) },
                        null,
                        CustomItemList.eM_energyTunnel4_UXV.get(1),
                        8000,
                        (int) TierEU.RECIPE_UXV);
            }

            // Laser Target IV-UXV 65536/t
            {
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_IV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16),
                                ItemList.Sensor_IV.get(16), ItemList.Electric_Pump_IV.get(16),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.TungstenSteel, 8),
                                GT_Utility.getIntegratedCircuit(5) },
                        null,
                        CustomItemList.eM_energyTunnel5_IV.get(1),
                        16000,
                        7680);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_LuV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16),
                                ItemList.Sensor_LuV.get(16), ItemList.Electric_Pump_LuV.get(16),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.VanadiumGallium, 8),
                                GT_Utility.getIntegratedCircuit(5) },
                        null,
                        CustomItemList.eM_energyTunnel5_LuV.get(1),
                        16000,
                        30720);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_ZPM.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16),
                                ItemList.Sensor_ZPM.get(16), ItemList.Electric_Pump_ZPM.get(16),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Naquadah, 8),
                                GT_Utility.getIntegratedCircuit(5) },
                        null,
                        CustomItemList.eM_energyTunnel5_ZPM.get(1),
                        16000,
                        122880);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_UV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16),
                                ItemList.Sensor_UV.get(16), ItemList.Electric_Pump_UV.get(16),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NaquadahAlloy, 8),
                                GT_Utility.getIntegratedCircuit(5) },
                        null,
                        CustomItemList.eM_energyTunnel5_UV.get(1),
                        16000,
                        500000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_MAX.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16),
                                ItemList.Sensor_UHV.get(16), ItemList.Electric_Pump_UHV.get(16),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Bedrockium, 8),
                                GT_Utility.getIntegratedCircuit(5) },
                        null,
                        CustomItemList.eM_energyTunnel5_UHV.get(1),
                        16000,
                        2000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UEV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16),
                                ItemList.Sensor_UEV.get(16), ItemList.Electric_Pump_UEV.get(16),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Draconium, 8),
                                GT_Utility.getIntegratedCircuit(5) },
                        null,
                        CustomItemList.eM_energyTunnel5_UEV.get(1),
                        16000,
                        8000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UIV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16),
                                ItemList.Sensor_UIV.get(16), ItemList.Electric_Pump_UIV.get(16),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NetherStar, 8),
                                GT_Utility.getIntegratedCircuit(5) },
                        null,
                        CustomItemList.eM_energyTunnel5_UIV.get(1),
                        16000,
                        32000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UMV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16),
                                ItemList.Sensor_UMV.get(16), ItemList.Electric_Pump_UMV.get(16),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Quantium, 8),
                                GT_Utility.getIntegratedCircuit(5) },
                        null,
                        CustomItemList.eM_energyTunnel5_UMV.get(1),
                        16000,
                        128000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UXV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16),
                                ItemList.Sensor_UXV.get(16), ItemList.Electric_Pump_UXV.get(16),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.BlackPlutonium, 8),
                                GT_Utility.getIntegratedCircuit(5) },
                        null,
                        CustomItemList.eM_energyTunnel5_UXV.get(1),
                        16000,
                        (int) TierEU.RECIPE_UXV);
            }

            // Laser Target IV-UXV 262144/t
            {
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_IV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32),
                                ItemList.Sensor_IV.get(32), ItemList.Electric_Pump_IV.get(32),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.TungstenSteel, 16),
                                GT_Utility.getIntegratedCircuit(6) },
                        null,
                        CustomItemList.eM_energyTunnel6_IV.get(1),
                        32000,
                        7680);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_LuV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32),
                                ItemList.Sensor_LuV.get(32), ItemList.Electric_Pump_LuV.get(32),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.VanadiumGallium, 16),
                                GT_Utility.getIntegratedCircuit(6) },
                        null,
                        CustomItemList.eM_energyTunnel6_LuV.get(1),
                        32000,
                        30720);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_ZPM.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32),
                                ItemList.Sensor_ZPM.get(32), ItemList.Electric_Pump_ZPM.get(32),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Naquadah, 16),
                                GT_Utility.getIntegratedCircuit(6) },
                        null,
                        CustomItemList.eM_energyTunnel6_ZPM.get(1),
                        32000,
                        122880);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_UV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32),
                                ItemList.Sensor_UV.get(32), ItemList.Electric_Pump_UV.get(32),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NaquadahAlloy, 16),
                                GT_Utility.getIntegratedCircuit(6) },
                        null,
                        CustomItemList.eM_energyTunnel6_UV.get(1),
                        32000,
                        500000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_MAX.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32),
                                ItemList.Sensor_UHV.get(32), ItemList.Electric_Pump_UHV.get(32),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Bedrockium, 16),
                                GT_Utility.getIntegratedCircuit(6) },
                        null,
                        CustomItemList.eM_energyTunnel6_UHV.get(1),
                        32000,
                        2000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UEV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32),
                                ItemList.Sensor_UEV.get(32), ItemList.Electric_Pump_UEV.get(32),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Draconium, 16),
                                GT_Utility.getIntegratedCircuit(6) },
                        null,
                        CustomItemList.eM_energyTunnel6_UEV.get(1),
                        32000,
                        8000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UIV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32),
                                ItemList.Sensor_UIV.get(32), ItemList.Electric_Pump_UIV.get(32),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NetherStar, 16),
                                GT_Utility.getIntegratedCircuit(6) },
                        null,
                        CustomItemList.eM_energyTunnel6_UIV.get(1),
                        32000,
                        32000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UMV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32),
                                ItemList.Sensor_UMV.get(32), ItemList.Electric_Pump_UMV.get(32),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Quantium, 16),
                                GT_Utility.getIntegratedCircuit(6) },
                        null,
                        CustomItemList.eM_energyTunnel6_UMV.get(1),
                        32000,
                        128000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UXV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32),
                                ItemList.Sensor_UXV.get(32), ItemList.Electric_Pump_UXV.get(32),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.BlackPlutonium, 16),
                                GT_Utility.getIntegratedCircuit(6) },
                        null,
                        CustomItemList.eM_energyTunnel6_UXV.get(1),
                        32000,
                        (int) TierEU.RECIPE_UXV);
            }

            // Laser Target IV-UXV 1048576/t
            {
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_IV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
                                ItemList.Sensor_IV.get(64), ItemList.Electric_Pump_IV.get(64),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.TungstenSteel, 16),
                                GT_Utility.getIntegratedCircuit(7) },
                        null,
                        CustomItemList.eM_energyTunnel7_IV.get(1),
                        64000,
                        7680);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_LuV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
                                ItemList.Sensor_LuV.get(64), ItemList.Electric_Pump_LuV.get(64),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.VanadiumGallium, 16),
                                GT_Utility.getIntegratedCircuit(7) },
                        null,
                        CustomItemList.eM_energyTunnel7_LuV.get(1),
                        64000,
                        30720);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_ZPM.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
                                ItemList.Sensor_ZPM.get(64), ItemList.Electric_Pump_ZPM.get(64),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Naquadah, 16),
                                GT_Utility.getIntegratedCircuit(7) },
                        null,
                        CustomItemList.eM_energyTunnel7_ZPM.get(1),
                        64000,
                        122880);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_UV.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
                                ItemList.Sensor_UV.get(64), ItemList.Electric_Pump_UV.get(64),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.NaquadahAlloy, 16),
                                GT_Utility.getIntegratedCircuit(7) },
                        null,
                        CustomItemList.eM_energyTunnel7_UV.get(1),
                        64000,
                        500000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { ItemList.Hull_MAX.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
                                ItemList.Sensor_UHV.get(64), ItemList.Electric_Pump_UHV.get(64),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Bedrockium, 16),
                                GT_Utility.getIntegratedCircuit(7) },
                        null,
                        CustomItemList.eM_energyTunnel7_UHV.get(1),
                        64000,
                        2000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UEV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
                                ItemList.Sensor_UEV.get(64), ItemList.Electric_Pump_UEV.get(64),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Draconium, 16),
                                GT_Utility.getIntegratedCircuit(7) },
                        null,
                        CustomItemList.eM_energyTunnel7_UEV.get(1),
                        64000,
                        8000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UIV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
                                ItemList.Sensor_UIV.get(64), ItemList.Electric_Pump_UIV.get(64),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.NetherStar, 16),
                                GT_Utility.getIntegratedCircuit(7) },
                        null,
                        CustomItemList.eM_energyTunnel7_UIV.get(1),
                        64000,
                        32000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UMV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
                                ItemList.Sensor_UMV.get(64), ItemList.Electric_Pump_UMV.get(64),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Quantium, 16),
                                GT_Utility.getIntegratedCircuit(7) },
                        null,
                        CustomItemList.eM_energyTunnel7_UMV.get(1),
                        64000,
                        128000000);
                GT_Values.RA.addAssemblerRecipe(
                        new ItemStack[] { getItemContainer("Hull_UXV").get(1),
                                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
                                ItemList.Sensor_UXV.get(64), ItemList.Electric_Pump_UXV.get(64),
                                GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.BlackPlutonium, 16),
                                GT_Utility.getIntegratedCircuit(7) },
                        null,
                        CustomItemList.eM_energyTunnel7_UXV.get(1),
                        64000,
                        (int) TierEU.RECIPE_UXV);
            }
        }

        // Parameterizer
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { CustomItemList.eM_Computer_Casing.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 1),
                        CustomItemList.DATApipe.get(4), ItemList.Cover_Screen.get(1),
                        new ItemStack(Blocks.stone_button, 16), GT_Utility.getIntegratedCircuit(1), },
                Materials.Iridium.getMolten(2592),
                CustomItemList.Parametrizer_Hatch.get(1),
                800,
                122880);

        // Parametrizer X
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { CustomItemList.eM_Computer_Casing.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Ultimate, 1),
                        CustomItemList.DATApipe.get(6), ItemList.Cover_Screen.get(1),
                        new ItemStack(Blocks.stone_button, 32), GT_Utility.getIntegratedCircuit(2), },
                Materials.Iridium.getMolten(2592),
                CustomItemList.ParametrizerX_Hatch.get(1),
                800,
                122880);

        // Parametrizer tXt
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { CustomItemList.eM_Computer_Casing.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Bio, 1), CustomItemList.DATApipe.get(8),
                        ItemList.Cover_Screen.get(2), new ItemStack(Blocks.stone_button, 64),
                        GT_Utility.getIntegratedCircuit(3), },
                Materials.Iridium.getMolten(2592),
                CustomItemList.ParametrizerTXT_Hatch.get(1),
                800,
                122880);

        // Tesla Capacitor
        {
            // LV Tesla Capacitor
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Tin, 4),
                            GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 4),
                            GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 8),
                            GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Silicone, 8), },
                    Materials.Epoxid.getMolten(72),
                    CustomItemList.teslaCapacitor.getWithDamage(1, 0),
                    320,
                    30);
            // MV Tesla Capacitor
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Copper, 4),
                            GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 6),
                            GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 12),
                            GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Silicone, 12), },
                    Materials.Epoxid.getMolten(144),
                    CustomItemList.teslaCapacitor.getWithDamage(1, 1),
                    320,
                    120);
            // HV Tesla Capacitor
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Gold, 4),
                            GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 8),
                            GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 16),
                            GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Silicone, 16), },
                    Materials.Epoxid.getMolten(216),
                    CustomItemList.teslaCapacitor.getWithDamage(1, 2),
                    320,
                    480);
            // EV Tesla Capacitor
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Aluminium, 4),
                            GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 10),
                            GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 20),
                            GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Silicone, 20), },
                    Materials.Epoxid.getMolten(288),
                    CustomItemList.teslaCapacitor.getWithDamage(1, 3),
                    320,
                    1920);
            // IV Tesla Capacitor
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Tungsten, 4),
                            GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 12),
                            GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 24),
                            GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Silicone, 24), },
                    Materials.Epoxid.getMolten(360),
                    CustomItemList.teslaCapacitor.getWithDamage(1, 4),
                    320,
                    7680);
            // LuV Tesla Capacitor
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.HSSG, 4),
                            GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 14),
                            GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 28),
                            GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Silicone, 28), },
                    Materials.Epoxid.getMolten(432),
                    CustomItemList.teslaCapacitor.getWithDamage(1, 5),
                    320,
                    30720);
            // ZPM Tesla Capacitor
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Naquadah, 4),
                            GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 16),
                            GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 32),
                            GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Silicone, 32), },
                    Materials.Epoxid.getMolten(504),
                    CustomItemList.teslaCapacitor.getWithDamage(1, 6),
                    320,
                    122880);
        }

        // Tesla Cover
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { CustomItemList.teslaComponent.getWithDamage(4, 0),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 2),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Gold, 16),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NickelZincFerrite, 2),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 8), },
                Materials.Lead.getMolten(288),
                CustomItemList.teslaCover.getWithDamage(1, 0),
                320,
                480);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { CustomItemList.teslaComponent.getWithDamage(4, 0),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 2),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Gold, 16),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NickelZincFerrite, 2),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 8), },
                Materials.Tin.getMolten(144),
                CustomItemList.teslaCover.getWithDamage(1, 0),
                320,
                480);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { CustomItemList.teslaComponent.getWithDamage(4, 0),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 2),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Gold, 16),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NickelZincFerrite, 2),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 8), },
                Materials.SolderingAlloy.getMolten(72),
                CustomItemList.teslaCover.getWithDamage(1, 0),
                320,
                480);
        // Ultimate Tesla Cover
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { CustomItemList.teslaComponent.getWithDamage(4, 1),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 2),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Tungsten, 16),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NickelZincFerrite, 2),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 8), },
                Materials.Lead.getMolten(288),
                CustomItemList.teslaCover.getWithDamage(1, 1),
                320,
                7680);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { CustomItemList.teslaComponent.getWithDamage(4, 1),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 2),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Tungsten, 16),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NickelZincFerrite, 2),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 8), },
                Materials.Tin.getMolten(144),
                CustomItemList.teslaCover.getWithDamage(1, 1),
                320,
                7680);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { CustomItemList.teslaComponent.getWithDamage(4, 1),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 2),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Tungsten, 16),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NickelZincFerrite, 2),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 8), },
                Materials.SolderingAlloy.getMolten(72),
                CustomItemList.teslaCover.getWithDamage(1, 1),
                320,
                7680);

        // Ender Fluid Link Cover
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Enderium, 4),
                        ItemList.Sensor_LuV.get(1), ItemList.Emitter_LuV.get(1), ItemList.Electric_Pump_LuV.get(1), },
                Materials.Chrome.getMolten(288),
                CustomItemList.enderLinkFluidCover.getWithDamage(1, 0),
                320,
                30720);

        // Power Pass Upgrade Cover
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { CustomItemList.Machine_Multi_Transformer.get(1), GT_Utility.getIntegratedCircuit(1) },
                null,
                CustomItemList.powerPassUpgradeCover.getWithDamage(1, 0),
                320,
                30720);

        // Tesla Winding Components
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Electrum, 32),
                        GT_OreDictUnificator.get(OrePrefixes.ring, Materials.NickelZincFerrite, 8), },
                Materials.Epoxid.getMolten(288),
                CustomItemList.teslaComponent.getWithDamage(1, 0),
                320,
                30);

        // Tesla Winding Components Ultimate (ADD BLOOD VARIANT)
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 4),
                        GT_OreDictUnificator.get(OrePrefixes.ring, Materials.NickelZincFerrite, 8), },
                Materials.Epoxid.getMolten(576),
                CustomItemList.teslaComponent.getWithDamage(1, 1),
                320,
                7680);

        // Tesla Transceiver LV 1A
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_1by1_LV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_1by1_LV.get(1),
                400,
                30);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_1by1_LV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_1by1_LV.get(1),
                400,
                30);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_1by1_LV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_1by1_LV.get(1),
                400,
                30);
        // Tesla Transceiver MV 1A
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_1by1_MV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_1by1_MV.get(1),
                400,
                120);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_1by1_MV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_1by1_MV.get(1),
                400,
                120);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_1by1_MV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_1by1_MV.get(1),
                400,
                120);
        // Tesla Transceiver HV 1A
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_1by1_HV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_1by1_HV.get(1),
                400,
                480);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_1by1_HV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_1by1_HV.get(1),
                400,
                480);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_1by1_HV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_1by1_HV.get(1),
                400,
                480);
        // Tesla Transceiver EV 1A
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_1by1_EV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_1by1_EV.get(1),
                400,
                1920);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_1by1_EV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_1by1_EV.get(1),
                400,
                1920);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_1by1_EV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_1by1_EV.get(1),
                400,
                1920);
        // Tesla Transceiver IV 1A
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_1by1_IV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_1by1_IV.get(1),
                400,
                7680);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_1by1_IV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_1by1_IV.get(1),
                400,
                7680);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_1by1_IV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_1by1_IV.get(1),
                400,
                7680);
        // Tesla Transceiver LV 4A
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_2by2_LV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_2by2_LV.get(1),
                400,
                30);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_2by2_LV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_2by2_LV.get(1),
                400,
                30);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_2by2_LV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_2by2_LV.get(1),
                400,
                30);
        // Tesla Transceiver MV 4A
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_2by2_MV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_2by2_MV.get(1),
                400,
                120);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_2by2_MV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_2by2_MV.get(1),
                400,
                120);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_2by2_MV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_2by2_MV.get(1),
                400,
                120);
        // Tesla Transceiver HV 4A
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_2by2_HV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_2by2_HV.get(1),
                400,
                480);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_2by2_HV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_2by2_HV.get(1),
                400,
                480);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_2by2_HV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_2by2_HV.get(1),
                400,
                480);
        // Tesla Transceiver EV 4A
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_2by2_EV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_2by2_EV.get(1),
                400,
                1920);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_2by2_EV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_2by2_EV.get(1),
                400,
                1920);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_2by2_EV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_2by2_EV.get(1),
                400,
                1920);
        // Tesla Transceiver IV 4A
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_2by2_IV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_2by2_IV.get(1),
                400,
                7680);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_2by2_IV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_2by2_IV.get(1),
                400,
                7680);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_2by2_IV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_2by2_IV.get(1),
                400,
                7680);
        // Tesla Transceiver LV 9A
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_3by3_LV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_3by3_LV.get(1),
                400,
                30);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_3by3_LV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_3by3_LV.get(1),
                400,
                30);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_3by3_LV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_3by3_LV.get(1),
                400,
                30);
        // Tesla Transceiver MV 9A
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_3by3_MV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_3by3_MV.get(1),
                400,
                120);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_3by3_LV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_3by3_MV.get(1),
                400,
                120);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_3by3_MV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_3by3_MV.get(1),
                400,
                120);
        // Tesla Transceiver HV 9A
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_3by3_HV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_3by3_HV.get(1),
                400,
                480);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_3by3_HV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_3by3_HV.get(1),
                400,
                480);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_3by3_HV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_3by3_HV.get(1),
                400,
                480);
        // Tesla Transceiver EV 9A
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_3by3_EV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_3by3_EV.get(1),
                400,
                1920);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_3by3_EV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_3by3_EV.get(1),
                400,
                1920);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_3by3_EV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_3by3_EV.get(1),
                400,
                1920);
        // Tesla Transceiver IV 9A
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_3by3_IV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_3by3_IV.get(1),
                400,
                7680);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_3by3_IV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_3by3_IV.get(1),
                400,
                7680);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_3by3_IV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_3by3_IV.get(1),
                400,
                7680);
        // Tesla Transceiver LV 16A
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_4by4_LV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_4by4_LV.get(1),
                400,
                30);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_4by4_LV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_4by4_LV.get(1),
                400,
                30);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_4by4_LV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_4by4_LV.get(1),
                400,
                30);
        // Tesla Transceiver MV 16A
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_4by4_MV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_4by4_MV.get(1),
                400,
                120);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_4by4_MV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_4by4_MV.get(1),
                400,
                120);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_4by4_MV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_4by4_MV.get(1),
                400,
                120);
        // Tesla Transceiver HV 16A
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_4by4_HV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_4by4_HV.get(1),
                400,
                480);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_4by4_HV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_4by4_HV.get(1),
                400,
                480);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_4by4_HV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_4by4_HV.get(1),
                400,
                480);
        // Tesla Transceiver EV 16A
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_4by4_EV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_4by4_EV.get(1),
                400,
                1920);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_4by4_EV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_4by4_EV.get(1),
                400,
                1920);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_4by4_EV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_4by4_EV.get(1),
                400,
                1920);
        // Tesla Transceiver IV 16A
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_4by4_IV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_4by4_IV.get(1),
                400,
                7680);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_4by4_IV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_4by4_IV.get(1),
                400,
                7680);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_4by4_IV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_4by4_IV.get(1),
                400,
                7680);

        // Tesla Tower
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_ModHandler.getIC2Item("teslaCoil", 1), CustomItemList.tM_TeslaSecondary.get(4),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NickelZincFerrite, 4),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 4),
                        ItemList.Upgrade_Overclocker.get(4), },
                Materials.Silver.getMolten(576),
                CustomItemList.Machine_Multi_TeslaCoil.get(1),
                800,
                480);

        // Microwave Grinder
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Machine_HV_Microwave.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.StainlessSteel, 4),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 4),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.AnnealedCopper, 16),
                        ItemList.Upgrade_Overclocker.get(4), },
                Materials.Copper.getMolten(576),
                CustomItemList.Machine_Multi_Microwave.get(1),
                800,
                480);

        // Active Transformer
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("WetTransformer_ZPM_LuV").get(1),
                        getItemContainer("HighEnergyFlowCircuit").get(1),
                        GT_OreDictUnificator.get(
                                OrePrefixes.wireGt01,
                                getOrDefault("SuperconductorLuV", Materials.SuperconductorUHV),
                                16),
                        ItemList.valueOf("Circuit_Chip_UHPIC").get(2), },
                Materials.TungstenSteel.getMolten(576),
                CustomItemList.Machine_Multi_Transformer.get(1),
                400,
                30720);

        // Network Switch
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { CustomItemList.Machine_Multi_Transformer.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 4),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Cobalt, 64),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Copper, 64),
                        CustomItemList.DATApipe.get(4), },
                Materials.Iridium.getMolten(1296),
                CustomItemList.Machine_Multi_Switch.get(1),
                800,
                122880);

    }

    public void cleanroomRecipes() {
        // Data
        addAssemblerRecipeWithCleanroom(
                new ItemStack[] { ItemList.Circuit_Parts_GlassFiber.get(8),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Silver, 8) },
                Materials.Polytetrafluoroethylene.getMolten(144),
                CustomItemList.DATApipe.get(1),
                200,
                30720);

        // Data Casing
        addAssemblerRecipeWithCleanroom(
                new ItemStack[] { CustomItemList.DATApipe.get(1), ItemList.Casing_LuV.get(1) },
                null,
                CustomItemList.DATApipeBlock.get(1),
                20,
                30720);

        // Tunnel
        addAssemblerRecipeWithCleanroom(
                new ItemStack[] { CustomItemList.DATApipe.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Osmiridium, 4),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Osmium, 4),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUHV, 2),
                        ItemList.Field_Generator_MV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Ultimate, 1) },
                Materials.Osmium.getMolten(288),
                CustomItemList.EMpipe.get(1),
                400,
                500000);

        // Tunnel Casing
        addAssemblerRecipeWithCleanroom(
                new ItemStack[] { CustomItemList.EMpipe.get(1), ItemList.Casing_LuV.get(1) },
                null,
                CustomItemList.EMpipeBlock.get(1),
                20,
                30720);

        // Laser
        addAssemblerRecipeWithCleanroom(
                new ItemStack[] { CustomItemList.DATApipe.get(1), GT_ModHandler.getIC2Item("reinforcedGlass", 1L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Osmiridium, 2) },
                null,
                CustomItemList.LASERpipe.get(1),
                100,
                500000);

        // Laser Casing
        addAssemblerRecipeWithCleanroom(
                new ItemStack[] { CustomItemList.LASERpipe.get(1), ItemList.Casing_LuV.get(1) },
                null,
                CustomItemList.LASERpipeBlock.get(1),
                20,
                30720);

        // Advanced Computer Casing
        addAssemblerRecipeWithCleanroom(
                new ItemStack[] { CustomItemList.eM_Computer_Casing.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Ultimate, 1),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Cobalt, 64),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Electrum, 64),
                        GT_OreDictUnificator.get(
                                OrePrefixes.wireGt02,
                                getOrDefault("SuperconductorLuV", Materials.SuperconductorUHV),
                                4) },
                Materials.Iridium.getMolten(1296),
                CustomItemList.eM_Computer_Bus.get(1),
                200,
                122880);

        // Data Input
        addAssemblerRecipeWithCleanroom(
                new ItemStack[] { CustomItemList.eM_Computer_Casing.get(1), ItemList.Hatch_Input_Bus_LuV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 1),
                        CustomItemList.DATApipe.get(2) },
                Materials.Iridium.getMolten(1296),
                CustomItemList.dataIn_Hatch.get(1),
                200,
                122880);

        // Data Output
        addAssemblerRecipeWithCleanroom(
                new ItemStack[] { CustomItemList.eM_Computer_Casing.get(1), ItemList.Hatch_Output_Bus_LuV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 1),
                        CustomItemList.DATApipe.get(2) },
                Materials.Iridium.getMolten(1296),
                CustomItemList.dataOut_Hatch.get(1),
                200,
                122880);

        // Rack
        addAssemblerRecipeWithCleanroom(
                new ItemStack[] { CustomItemList.eM_Computer_Bus.get(1), ItemList.Hatch_Input_Bus_ZPM.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 2),
                        CustomItemList.DATApipe.get(4) },
                Materials.Iridium.getMolten(1296),
                CustomItemList.rack_Hatch.get(1),
                800,
                122880);

        // Uncertainty
        addAssemblerRecipeWithCleanroom(
                new ItemStack[] { CustomItemList.eM_Computer_Casing.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Ultimate, 1),
                        CustomItemList.DATApipe.get(16), ItemList.Cover_Screen.get(1),
                        new ItemStack(Blocks.stone_button, 16), GT_Utility.getIntegratedCircuit(4), },
                Materials.Iridium.getMolten(2592),
                CustomItemList.Uncertainty_Hatch.get(1),
                1200,
                122880);

        // Uncertainty X
        addAssemblerRecipeWithCleanroom(
                new ItemStack[] { CustomItemList.eM_Computer_Casing.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Bio, 1),
                        CustomItemList.DATApipe.get(32), ItemList.Cover_Screen.get(1),
                        new ItemStack(Blocks.stone_button, 16), GT_Utility.getIntegratedCircuit(5), },
                Materials.Iridium.getMolten(2592),
                CustomItemList.UncertaintyX_Hatch.get(1),
                1200,
                122880);

        // Elemental Input
        addAssemblerRecipeWithCleanroom(
                new ItemStack[] { CustomItemList.eM_Containment.get(1), ItemList.Hatch_Input_UV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Naquadah, 2),
                        ItemList.Sensor_UV.get(1) },
                Materials.Osmiridium.getMolten(1296),
                CustomItemList.eM_in_UV.get(1),
                800,
                500000);

        // Elemental Output
        addAssemblerRecipeWithCleanroom(
                new ItemStack[] { CustomItemList.eM_Containment.get(1), ItemList.Hatch_Output_UV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Naquadah, 2),
                        ItemList.Emitter_UV.get(1) },
                Materials.Osmiridium.getMolten(1296),
                CustomItemList.eM_out_UV.get(1),
                800,
                500000);

        // Overflow
        addAssemblerRecipeWithCleanroom(
                new ItemStack[] { CustomItemList.eM_Containment.get(1), ItemList.Hatch_Muffler_UV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Naquadah, 1),
                        ItemList.Field_Generator_UV.get(1) },
                Materials.Osmiridium.getMolten(1296),
                CustomItemList.eM_muffler_UV.get(1),
                800,
                500000);

        // Capacitor Hatch
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hatch_Input_Bus_HV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NickelZincFerrite, 4),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Gold, 4), },
                Materials.Silver.getMolten(576),
                CustomItemList.capacitor_Hatch.get(1),
                800,
                480);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hatch_Output_Bus_HV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NickelZincFerrite, 4),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Gold, 4), },
                Materials.Silver.getMolten(576),
                CustomItemList.capacitor_Hatch.get(1),
                800,
                480);

    }

    private void addAssemblerRecipeWithCleanroom(ItemStack[] items, FluidStack fluid, ItemStack output, int time,
            int eut) {
        GT_Values.RA.addAssemblerRecipe(items, fluid, output, time, eut, true);
    }
}
