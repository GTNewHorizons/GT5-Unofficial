package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.scpRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.item.ItemStack;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

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

        // Scon w/SpaceTime input (MV-UHV)

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
