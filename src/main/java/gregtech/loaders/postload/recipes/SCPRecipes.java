package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.GraviSuite;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.scpRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.STACKS;
import static gregtech.api.util.GTRecipeConstants.AssemblyLine;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GTRecipeConstants.SCANNING;

import net.minecraft.item.ItemStack;

import bartworks.common.loaders.ItemRegistry;
import bartworks.system.material.WerkstoffLoader;
import goodgenerator.util.ItemRefer;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.api.util.recipe.Scanning;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class SCPRecipes implements Runnable {

    @Override
    public void run() {
        ItemStack SuperconductorMV = GTUtility
            .copyAmountUnsafe(3 * 64, GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorMV, 1));
        ItemStack SuperconductorHV = GTUtility
            .copyAmountUnsafe(6 * 64, GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorHV, 1));
        ItemStack SuperconductorEV = GTUtility
            .copyAmountUnsafe(9 * 64, GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorEV, 1));
        ItemStack SuperconductorIV = GTUtility
            .copyAmountUnsafe(12 * 64, GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 1));
        ItemStack SuperconductorLuV = GTUtility
            .copyAmountUnsafe(15 * 64, GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 1));
        ItemStack SuperconductorZPM = GTUtility
            .copyAmountUnsafe(18 * 64, GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 1));
        ItemStack SuperconductorUV = GTUtility
            .copyAmountUnsafe(21 * 64, GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUV, 1));
        ItemStack SuperconductorUHV = GTUtility
            .copyAmountUnsafe(24 * 64, GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 1));
        ItemStack SuperconductorUEV = GTUtility
            .copyAmountUnsafe(27 * 64, GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEV, 1));
        ItemStack SuperconductorUIV = GTUtility
            .copyAmountUnsafe(30 * 64, GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 1));
        ItemStack SuperconductorUMV = GTUtility
            .copyAmountUnsafe(33 * 64, GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, 1));
        ItemStack MVF = GTUtility.copyAmount(16, ItemRegistry.megaMachines[1]);
        ItemStack coolingCore = GraviSuite.isModLoaded() ? getModItem(GraviSuite.ID, "itemSimpleItem", 8, 2)
            : ItemList.Tool_DataStick.get(1);

        // Superconductor Processor Controller
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemRefer.Component_Assembly_Line.get(1))
            .metadata(SCANNING, new Scanning(15 * SECONDS, TierEU.RECIPE_UV))
            .itemInputs(
                ItemRefer.Precise_Assembler.get(16),
                MVF,
                ItemList.FluidSolidifierUV.get(16),
                ItemList.WiremillUV.get(16),
                ItemRefer.Precise_Electronic_Unit_T4.get(16),
                GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Neutronium, 32),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Americium, 4),
                ItemList.Conveyor_Module_UV.get(16),
                ItemList.Electric_Pump_UV.get(16),
                new Object[] { OrePrefixes.circuit.get(Materials.UHV), 16 })
            .itemOutputs(ItemList.SuperConductorProcessor.get(1))
            .fluidInputs(
                Materials.Lubricant.getFluid(10000),
                MaterialsAlloy.INDALLOY_140.getFluidStack(15 * STACKS + 40 * INGOTS),
                WerkstoffLoader.Oganesson.getFluidOrGas(2880))
            .duration(120 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(AssemblyLine);

        // Quantum Convection Casing
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GregtechItemList.Casing_AdvancedVacuum.get(1))
            .metadata(SCANNING, new Scanning(15 * SECONDS, TierEU.RECIPE_UV))
            .itemInputs(
                GregtechItemList.Casing_AdvancedVacuum.get(4),
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 16 },
                ItemList.Naquarite_Universal_Insulator_Foil.get(8),
                coolingCore,
                ItemList.Reactor_Coolant_Sp_6.get(1),
                ItemList.Electric_Pump_UV.get(4))
            .itemOutputs(ItemList.SCP_Casing.get(1))
            .fluidInputs(
                Materials.SuperCoolant.getFluid(100000L),
                Materials.MysteriousCrystal.getMolten(50000L),
                Materials.SuperfluidHelium.getFluid(25000L))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(AssemblyLine);

        // Booster Housing
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GregtechItemList.Bus_Milling_Balls.get(1))
            .metadata(SCANNING, new Scanning(150 * SECONDS, TierEU.RECIPE_UHV))
            .itemInputs(
                CI.getTieredGTPPMachineCasing(8, 8),
                ItemList.Field_Generator_UV.get(16),
                GregtechItemList.Energy_Core_ZPM.get(8),
                GTOreDictUnificator.get(OrePrefixes.round, Materials.Vanadiumtriindinid, 64),
                GTOreDictUnificator.get(
                    OrePrefixes.plateDense,
                    Materials.Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid,
                    16),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Tetranaquadahdiindiumhexaplatiumosminid, 16),
                GTOreDictUnificator.get(OrePrefixes.spring, Materials.Longasssuperconductornameforuvwire, 16),
                new Object[] { OrePrefixes.circuit.get(Materials.UHV), 16 })
            .itemOutputs(ItemList.Hatch_Booster.get(1))
            .fluidInputs(
                MaterialsElements.STANDALONE.GRANITE.getFluidStack(64 * INGOTS),
                MaterialsAlloy.BOTMIUM.getFluidStack(32 * INGOTS),
                MaterialsAlloy.PIKYONIUM.getFluidStack(16 * INGOTS),
                WerkstoffLoader.TantalumHafniumCarbide.getMolten(1152))
            .duration(333 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(AssemblyLine);

        // Tier 1 Boosters
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Superconducting_Magnet_Solenoid_ZPM.get(16),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Pentacadmiummagnesiumhexaoxid, 12),
                ItemList.Field_Generator_HV.get(4))
            .itemOutputs(ItemList.Booster_MV.get(1))
            .fluidInputs(Materials.SuperfluidHelium.getFluid(10000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Superconducting_Magnet_Solenoid_ZPM.get(16),
                GTOreDictUnificator
                    .get(OrePrefixes.plateSuperdense, Materials.Titaniumonabariumdecacoppereikosaoxid, 12),
                ItemList.Field_Generator_EV.get(4))
            .itemOutputs(ItemList.Booster_HV.get(1))
            .fluidInputs(Materials.SuperfluidHelium.getFluid(10000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Superconducting_Magnet_Solenoid_ZPM.get(16),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Uraniumtriplatinid, 12),
                ItemList.Field_Generator_IV.get(4))
            .itemOutputs(ItemList.Booster_EV.get(1))
            .fluidInputs(Materials.SuperfluidHelium.getFluid(10000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Superconducting_Magnet_Solenoid_ZPM.get(16),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Vanadiumtriindinid, 12),
                ItemList.Field_Generator_LuV.get(4))
            .itemOutputs(ItemList.Booster_IV.get(1))
            .fluidInputs(Materials.SuperfluidHelium.getFluid(10000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Superconducting_Magnet_Solenoid_ZPM.get(16),
                GTOreDictUnificator.get(
                    OrePrefixes.plateSuperdense,
                    Materials.Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid,
                    12),
                ItemList.Field_Generator_ZPM.get(4))
            .itemOutputs(ItemList.Booster_LuV.get(1))
            .fluidInputs(Materials.SuperfluidHelium.getFluid(10000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Superconducting_Magnet_Solenoid_ZPM.get(16),
                GTOreDictUnificator
                    .get(OrePrefixes.plateSuperdense, Materials.Tetranaquadahdiindiumhexaplatiumosminid, 12),
                ItemList.Field_Generator_UV.get(4))
            .itemOutputs(ItemList.Booster_ZPM.get(1))
            .fluidInputs(Materials.SuperfluidHelium.getFluid(10000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(assemblerRecipes);

        // Tier 2 Boosters
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Superconducting_Magnet_Solenoid_UHV.get(32),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Longasssuperconductornameforuvwire, 24),
                ItemList.Field_Generator_UHV.get(16))
            .itemOutputs(ItemList.Booster_UV.get(1))
            .fluidInputs(Materials.SuperfluidHelium.getFluid(100000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Superconducting_Magnet_Solenoid_UHV.get(32),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Longasssuperconductornameforuhvwire, 24),
                ItemList.Field_Generator_UEV.get(16))
            .itemOutputs(ItemList.Booster_UHV.get(1))
            .fluidInputs(Materials.SuperfluidHelium.getFluid(100000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .addTo(assemblerRecipes);

        // Tier 3 Boosters
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Superconducting_Magnet_Solenoid_UEV.get(64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.SuperconductorUEVBase, 48),
                ItemList.Field_Generator_UIV.get(64))
            .itemOutputs(ItemList.Booster_UEV.get(1))
            .fluidInputs(Materials.SpaceTime.getMolten(10000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UMV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Superconducting_Magnet_Solenoid_UIV.get(64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.SuperconductorUIVBase, 48),
                ItemList.Field_Generator_UMV.get(64))
            .itemOutputs(ItemList.Booster_UIV.get(1))
            .fluidInputs(Materials.SpaceTime.getMolten(100000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UXV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Superconducting_Magnet_Solenoid_UMV.get(64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.SuperconductorUMVBase, 48),
                ItemList.Field_Generator_UXV.get(64))
            .itemOutputs(ItemList.Booster_UMV.get(1))
            .fluidInputs(Materials.SpaceTime.getMolten(1000000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MAX)
            .addTo(assemblerRecipes);

        // Scon w/Helium input (MV-UHV)

        // MV
        GTValues.RA.stdBuilder()
            .itemInputsUnsafe(
                GTUtility.copyAmountUnsafe(
                    2 * 64,
                    GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.StainlessSteel, 2)),
                GTUtility.copyAmountUnsafe(1 * 64, ItemList.Electric_Pump_MV.get(1)))
            .itemOutputs(SuperconductorMV)
            .fluidInputs(
                (Materials.Pentacadmiummagnesiumhexaoxid.getMolten(72L * 3 * 64)),
                Materials.Helium.getGas(2_000 * 64))
            .duration(20 * 48 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(scpRecipes);

        // HV
        GTValues.RA.stdBuilder()
            .itemInputsUnsafe(
                GTUtility
                    .copyAmountUnsafe(4 * 64, GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Titanium, 4)),
                GTUtility.copyAmountUnsafe(1 * 64, ItemList.Electric_Pump_HV.get(1)))
            .itemOutputs(SuperconductorHV)
            .fluidInputs(
                (Materials.Titaniumonabariumdecacoppereikosaoxid.getMolten(72L * 6 * 64)),
                Materials.Helium.getGas(4_000 * 64))
            .duration(20 * 48 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(scpRecipes);

        // EV
        GTValues.RA.stdBuilder()
            .itemInputsUnsafe(
                GTUtility.copyAmountUnsafe(
                    6 * 64,
                    GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.TungstenSteel, 6)),
                GTUtility.copyAmountUnsafe(1 * 64, ItemList.Electric_Pump_EV.get(1)))
            .itemOutputs(SuperconductorEV)
            .fluidInputs((Materials.Uraniumtriplatinid.getMolten(72L * 9 * 64)), Materials.Helium.getGas(6_000 * 64))
            .duration(20 * 48 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(scpRecipes);

        // IV
        GTValues.RA.stdBuilder()
            .itemInputsUnsafe(
                GTUtility.copyAmountUnsafe(
                    8 * 64,
                    GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.NiobiumTitanium, 8)),
                GTUtility.copyAmountUnsafe(1 * 64, ItemList.Electric_Pump_IV.get(1)))
            .itemOutputs(SuperconductorIV)
            .fluidInputs((Materials.Vanadiumtriindinid.getMolten(72L * 12 * 64)), Materials.Helium.getGas(8_000 * 64))
            .duration(40 * 48 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(scpRecipes);

        // Luv
        GTValues.RA.stdBuilder()
            .itemInputsUnsafe(
                GTUtility
                    .copyAmountUnsafe(10 * 64, GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Enderium, 10)),
                GTUtility.copyAmountUnsafe(1 * 64, ItemList.Electric_Pump_LuV.get(1)))
            .itemOutputs(SuperconductorLuV)
            .fluidInputs(
                (Materials.Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid.getMolten(72L * 15 * 64)),
                Materials.Helium.getGas(12_000 * 64))
            .duration(40 * 48 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(scpRecipes);

        // ZPM
        GTValues.RA.stdBuilder()
            .itemInputsUnsafe(
                GTUtility
                    .copyAmountUnsafe(12 * 64, GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Naquadah, 12)),
                GTUtility.copyAmountUnsafe(1 * 64, ItemList.Electric_Pump_ZPM.get(1)))
            .itemOutputs(SuperconductorZPM)
            .fluidInputs(
                (Materials.Tetranaquadahdiindiumhexaplatiumosminid.getMolten(72L * 18 * 64)),
                Materials.Helium.getGas(16_000 * 64))
            .duration(80 * 48 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(scpRecipes);

        // UV
        GTValues.RA.stdBuilder()
            .itemInputsUnsafe(
                GTUtility
                    .copyAmountUnsafe(14 * 64, GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Neutronium, 14)),
                GTUtility.copyAmountUnsafe(1 * 64, ItemList.Electric_Pump_UV.get(1)))
            .itemOutputs(SuperconductorUV)
            .fluidInputs(
                (Materials.Longasssuperconductornameforuvwire.getMolten(72L * 21 * 64)),
                Materials.Helium.getGas(20_000 * 64))
            .duration(80 * 48 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(scpRecipes);

        // UHV
        GTValues.RA.stdBuilder()
            .itemInputsUnsafe(
                GTUtility
                    .copyAmountUnsafe(16 * 64, GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Bedrockium, 16)),
                GTUtility.copyAmountUnsafe(1 * 64, ItemList.Electric_Pump_UHV.get(1)))
            .itemOutputs(SuperconductorUHV)
            .fluidInputs(
                (Materials.Longasssuperconductornameforuhvwire.getMolten(72L * 24 * 64)),
                Materials.Helium.getGas(24_000 * 64))
            .duration(160 * 48 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(scpRecipes);

        // Scon w/Liquid Helium input (MV-UEV)

        // MV
        GTValues.RA.stdBuilder()
            .itemInputsUnsafe(
                GTUtility.copyAmountUnsafe(
                    2 * 64,
                    GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.StainlessSteel, 2)),
                GTUtility.copyAmountUnsafe(1 * 64, ItemList.Electric_Pump_MV.get(1)))
            .itemOutputs(SuperconductorMV)
            .fluidInputs(
                (Materials.Pentacadmiummagnesiumhexaoxid.getMolten(72L * 3 * 64)),
                WerkstoffLoader.LiquidHelium.getFluidOrGas(2_000 * 64))
            .duration(16 * 48 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(scpRecipes);

        // HV
        GTValues.RA.stdBuilder()
            .itemInputsUnsafe(
                GTUtility
                    .copyAmountUnsafe(4 * 64, GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Titanium, 4)),
                GTUtility.copyAmountUnsafe(1 * 64, ItemList.Electric_Pump_HV.get(1)))
            .itemOutputs(SuperconductorHV)
            .fluidInputs(
                (Materials.Titaniumonabariumdecacoppereikosaoxid.getMolten(72L * 6 * 64)),
                WerkstoffLoader.LiquidHelium.getFluidOrGas(4_000 * 64))
            .duration(16 * 48 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(scpRecipes);

        // EV
        GTValues.RA.stdBuilder()
            .itemInputsUnsafe(
                GTUtility.copyAmountUnsafe(
                    6 * 64,
                    GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.TungstenSteel, 6)),
                GTUtility.copyAmountUnsafe(1 * 64, ItemList.Electric_Pump_EV.get(1)))
            .itemOutputs(SuperconductorEV)
            .fluidInputs(
                (Materials.Uraniumtriplatinid.getMolten(72L * 9 * 64)),
                WerkstoffLoader.LiquidHelium.getFluidOrGas(6_000 * 64))
            .duration(16 * 48 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(scpRecipes);

        // IV
        GTValues.RA.stdBuilder()
            .itemInputsUnsafe(
                GTUtility.copyAmountUnsafe(
                    8 * 64,
                    GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.NiobiumTitanium, 8)),
                GTUtility.copyAmountUnsafe(1 * 64, ItemList.Electric_Pump_IV.get(1)))
            .itemOutputs(SuperconductorIV)
            .fluidInputs(
                (Materials.Vanadiumtriindinid.getMolten(72L * 12 * 64)),
                WerkstoffLoader.LiquidHelium.getFluidOrGas(8_000 * 64))
            .duration(32 * 48 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(scpRecipes);

        // LuV
        GTValues.RA.stdBuilder()
            .itemInputsUnsafe(
                GTUtility
                    .copyAmountUnsafe(10 * 64, GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Enderium, 10)),
                GTUtility.copyAmountUnsafe(1 * 64, ItemList.Electric_Pump_LuV.get(1)))
            .itemOutputs(SuperconductorLuV)
            .fluidInputs(
                (Materials.Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid.getMolten(72L * 15 * 64)),
                WerkstoffLoader.LiquidHelium.getFluidOrGas(12_000 * 64))
            .duration(32 * 48 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(scpRecipes);

        // ZPM
        GTValues.RA.stdBuilder()
            .itemInputsUnsafe(
                GTUtility
                    .copyAmountUnsafe(12 * 64, GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Naquadah, 12)),
                GTUtility.copyAmountUnsafe(1 * 64, ItemList.Electric_Pump_ZPM.get(1)))
            .itemOutputs(SuperconductorZPM)
            .fluidInputs(
                (Materials.Tetranaquadahdiindiumhexaplatiumosminid.getMolten(72L * 18 * 64)),
                WerkstoffLoader.LiquidHelium.getFluidOrGas(16_000 * 64))
            .duration(64 * 48 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(scpRecipes);

        // UV
        GTValues.RA.stdBuilder()
            .itemInputsUnsafe(
                GTUtility
                    .copyAmountUnsafe(14 * 64, GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Neutronium, 14)),
                GTUtility.copyAmountUnsafe(1 * 64, ItemList.Electric_Pump_UV.get(1)))
            .itemOutputs(SuperconductorUV)
            .fluidInputs(
                (Materials.Longasssuperconductornameforuvwire.getMolten(72L * 21 * 64)),
                WerkstoffLoader.LiquidHelium.getFluidOrGas(20_000 * 64))
            .duration(64 * 48 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(scpRecipes);

        // UHV
        GTValues.RA.stdBuilder()
            .itemInputsUnsafe(
                GTUtility
                    .copyAmountUnsafe(16 * 64, GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Bedrockium, 16)),
                GTUtility.copyAmountUnsafe(1 * 64, ItemList.Electric_Pump_UHV.get(1)))
            .itemOutputs(SuperconductorUHV)
            .fluidInputs(
                (Materials.Longasssuperconductornameforuhvwire.getMolten(72L * 24 * 64)),
                WerkstoffLoader.LiquidHelium.getFluidOrGas(24_000 * 64))
            .duration(128 * 48 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(scpRecipes);

        // UEV
        GTValues.RA.stdBuilder()
            .itemInputsUnsafe(
                GTUtility
                    .copyAmountUnsafe(18 * 64, GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Infinity, 18)),
                GTUtility.copyAmountUnsafe(1 * 64, ItemList.Electric_Pump_UEV.get(1)))
            .itemOutputs(SuperconductorUEV)
            .fluidInputs(
                (Materials.SuperconductorUEVBase.getMolten(72L * 27 * 64)),
                WerkstoffLoader.LiquidHelium.getFluidOrGas(28_000 * 64))
            .duration(160 * 48 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(scpRecipes);

        // Scon w/SpaceTime input (MV-UMV)

        // MV
        GTValues.RA.stdBuilder()
            .itemInputsUnsafe(
                GTUtility.copyAmountUnsafe(
                    2 * 64,
                    GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.StainlessSteel, 2)),
                GTUtility.copyAmountUnsafe(1 * 64, ItemList.Electric_Pump_MV.get(1)))
            .itemOutputs(SuperconductorMV)
            .fluidInputs(
                Materials.Pentacadmiummagnesiumhexaoxid.getMolten(72L * 3 * 64),
                Materials.SpaceTime.getMolten(4 * 64))
            .duration(10 * 48 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(scpRecipes);

        // HV
        GTValues.RA.stdBuilder()
            .itemInputsUnsafe(
                GTUtility
                    .copyAmountUnsafe(4 * 64, GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Titanium, 4)),
                GTUtility.copyAmountUnsafe(1 * 64, ItemList.Electric_Pump_HV.get(1)))
            .itemOutputs(SuperconductorHV)
            .fluidInputs(
                Materials.Titaniumonabariumdecacoppereikosaoxid.getMolten(72L * 6 * 64),
                Materials.SpaceTime.getMolten(8 * 64))
            .duration(10 * 48 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(scpRecipes);

        // EV
        GTValues.RA.stdBuilder()
            .itemInputsUnsafe(
                GTUtility.copyAmountUnsafe(
                    6 * 64,
                    GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.TungstenSteel, 6)),
                GTUtility.copyAmountUnsafe(1 * 64, ItemList.Electric_Pump_EV.get(1)))
            .itemOutputs(SuperconductorEV)
            .fluidInputs(Materials.Uraniumtriplatinid.getMolten(72L * 9 * 64), Materials.SpaceTime.getMolten(12 * 64))
            .duration(10 * 48 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(scpRecipes);

        // IV
        GTValues.RA.stdBuilder()
            .itemInputsUnsafe(
                GTUtility.copyAmountUnsafe(
                    8 * 64,
                    GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.NiobiumTitanium, 8)),
                GTUtility.copyAmountUnsafe(1 * 64, ItemList.Electric_Pump_IV.get(1)))
            .itemOutputs(SuperconductorIV)
            .fluidInputs(Materials.Vanadiumtriindinid.getMolten(72L * 12 * 64), Materials.SpaceTime.getMolten(16 * 64))
            .duration(20 * 48 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(scpRecipes);

        // LuV
        GTValues.RA.stdBuilder()
            .itemInputsUnsafe(
                GTUtility
                    .copyAmountUnsafe(10 * 64, GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Enderium, 10)),
                GTUtility.copyAmountUnsafe(1 * 64, ItemList.Electric_Pump_LuV.get(1)))
            .itemOutputs(SuperconductorLuV)
            .fluidInputs(
                Materials.Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid.getMolten(72L * 15 * 64),
                Materials.SpaceTime.getMolten(24 * 64))
            .duration(20 * 48 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(scpRecipes);

        // ZPM
        GTValues.RA.stdBuilder()
            .itemInputsUnsafe(
                GTUtility
                    .copyAmountUnsafe(12 * 64, GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Naquadah, 12)),
                GTUtility.copyAmountUnsafe(1 * 64, ItemList.Electric_Pump_ZPM.get(1)))
            .itemOutputs(SuperconductorZPM)
            .fluidInputs(
                Materials.Tetranaquadahdiindiumhexaplatiumosminid.getMolten(72L * 18 * 64),
                Materials.SpaceTime.getMolten(32 * 64))
            .duration(40 * 48 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(scpRecipes);

        // UV
        GTValues.RA.stdBuilder()
            .itemInputsUnsafe(
                GTUtility
                    .copyAmountUnsafe(14 * 64, GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Neutronium, 14)),
                GTUtility.copyAmountUnsafe(1 * 64, ItemList.Electric_Pump_UV.get(1)))
            .itemOutputs(SuperconductorUV)
            .fluidInputs(
                Materials.Longasssuperconductornameforuvwire.getMolten(72L * 21 * 64),
                Materials.SpaceTime.getMolten(40 * 64))
            .duration(40 * 48 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(scpRecipes);

        // UHV
        GTValues.RA.stdBuilder()
            .itemInputsUnsafe(
                GTUtility
                    .copyAmountUnsafe(16 * 64, GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Bedrockium, 16)),
                GTUtility.copyAmountUnsafe(1 * 64, ItemList.Electric_Pump_UHV.get(1)))
            .itemOutputs(SuperconductorUHV)
            .fluidInputs(
                Materials.Longasssuperconductornameforuhvwire.getMolten(72L * 24 * 64),
                Materials.SpaceTime.getMolten(48 * 64))
            .duration(80 * 48 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(scpRecipes);

        // UEV
        GTValues.RA.stdBuilder()
            .itemInputsUnsafe(
                GTUtility
                    .copyAmountUnsafe(18 * 64, GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Infinity, 18)),
                GTUtility.copyAmountUnsafe(1 * 64, ItemList.Electric_Pump_UEV.get(1)))
            .itemOutputs(SuperconductorUEV)
            .fluidInputs(
                Materials.SuperconductorUEVBase.getMolten(72L * 27 * 64),
                Materials.SpaceTime.getMolten(56 * 64))
            .duration(80 * 48 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(scpRecipes);

        // UIV
        GTValues.RA.stdBuilder()
            .itemInputsUnsafe(
                GTUtility.copyAmountUnsafe(
                    20 * 64,
                    GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.TranscendentMetal, 20)),
                GTUtility.copyAmountUnsafe(1 * 64, ItemList.Electric_Pump_UIV.get(1L)))
            .itemOutputs(SuperconductorUIV)
            .fluidInputs(
                Materials.SuperconductorUIVBase.getMolten(72L * 30 * 64),
                Materials.SpaceTime.getMolten(68 * 64))
            .duration(80 * 48 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .addTo(scpRecipes);

        // UMV
        GTValues.RA.stdBuilder()
            .itemInputsUnsafe(
                GTUtility
                    .copyAmountUnsafe(22 * 64, GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.SpaceTime, 22)),
                GTUtility.copyAmountUnsafe(1 * 64, ItemList.Electric_Pump_UMV.get(1)))
            .itemOutputs(SuperconductorUMV)
            .fluidInputs(
                Materials.SuperconductorUMVBase.getMolten(72L * 33 * 64),
                Materials.SpaceTime.getMolten(72 * 64))
            .duration(160 * 48 * SECONDS)
            .eut(TierEU.RECIPE_UMV)
            .addTo(scpRecipes);
    }
}
