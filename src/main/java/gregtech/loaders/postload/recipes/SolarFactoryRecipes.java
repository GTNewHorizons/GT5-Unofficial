package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.Mods.SuperSolarPanels;
import static gregtech.api.recipe.RecipeMaps.solarFactoryRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.SOLAR_FACTORY_WAFER_DATA;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.recipe.SolarFactoryRecipeData;

// If you want to make a new recipe for Solar Factory, you'll need to make a hidden one and a fake one.
// The fake one should include the wafer you use, the hidden one without it. Hidden recipes must have a metadata value.

// Recipe metadata values represent the minimum tier and the amount of wafers respectively.
// Metadata is only required on the hidden recipes.

// If the recipe you are making doesn't use a wafer, just make it without any metadata.
public class SolarFactoryRecipes implements Runnable {

    private final Fluid solderIndalloy;

    public SolarFactoryRecipes() {
        solderIndalloy = FluidRegistry.getFluid("molten.indalloy140");
    }

    public void run() {

        // Fake recipes for NEI
        GTValues.RA.stdBuilder()
            .itemInputs(
                getModItem(NewHorizonsCoreMod.ID, "item.IrradiantReinforcedAluminiumPlate", 2),
                ItemList.Circuit_Silicon_Wafer2.get(4),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorMV, 4),
                GTOreDictUnificator.get(OrePrefixes.circuit.get(Materials.HV), 2))
            .itemOutputs(ItemList.Cover_SolarPanel_LV.get(1))
            .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(288))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .fake()
            .addTo(solarFactoryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                getModItem(NewHorizonsCoreMod.ID, "item.IrradiantReinforcedTitaniumPlate", 2),
                ItemList.Circuit_Silicon_Wafer2.get(4),
                ItemList.Circuit_Chip_ULPIC.get(4),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorHV, 4),
                GTOreDictUnificator.get(OrePrefixes.circuit.get(Materials.EV), 2))
            .itemOutputs(ItemList.Cover_SolarPanel_MV.get(1))
            .fluidInputs(Materials.Epoxid.getMolten(576))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .fake()
            .addTo(solarFactoryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                getModItem(NewHorizonsCoreMod.ID, "item.IrradiantReinforcedTungstenPlate", 4),
                ItemList.Circuit_Silicon_Wafer2.get(4),
                ItemList.Circuit_Chip_LPIC.get(4),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorEV, 8),
                GTOreDictUnificator.get(OrePrefixes.circuit.get(Materials.IV), 4),
                GTOreDictUnificator.get(OrePrefixes.plate.get(Materials.IndiumGalliumPhosphide), 8))
            .itemOutputs(ItemList.Cover_SolarPanel_HV.get(1))
            .fluidInputs(Materials.Epoxid.getMolten(1152))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .fake()
            .addTo(solarFactoryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                getModItem(NewHorizonsCoreMod.ID, "item.IrradiantReinforcedTungstenSteelPlate", 4),
                ItemList.Circuit_Silicon_Wafer3.get(4),
                ItemList.Circuit_Chip_PIC.get(4),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 8),
                GTOreDictUnificator.get(OrePrefixes.circuit.get(Materials.LuV), 4),
                GTOreDictUnificator.get(OrePrefixes.plate.get(Materials.SiliconSG), 16))
            .itemOutputs(ItemList.Cover_SolarPanel_EV.get(1))
            .fluidInputs(Materials.Polybenzimidazole.getMolten(576))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .fake()
            .addTo(solarFactoryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                getModItem(NewHorizonsCoreMod.ID, "item.IrradiantReinforcedChromePlate", 4),
                ItemList.Circuit_Silicon_Wafer3.get(8),
                ItemList.Circuit_Chip_HPIC.get(4),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 10),
                GTOreDictUnificator.get(OrePrefixes.circuit.get(Materials.ZPM), 4),
                GTOreDictUnificator.get(OrePrefixes.block.get(Materials.SiliconSG), 4))
            .itemOutputs(ItemList.Cover_SolarPanel_IV.get(1))
            .fluidInputs(Materials.Polybenzimidazole.getMolten(1152))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .fake()
            .addTo(solarFactoryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                getModItem(NewHorizonsCoreMod.ID, "item.IrradiantReinforcedChromePlate", 4),
                ItemList.Circuit_Silicon_Wafer4.get(8),
                ItemList.Circuit_Chip_UHPIC.get(8),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 24),
                GTOreDictUnificator.get(OrePrefixes.circuit.get(Materials.UV), 2),
                GTOreDictUnificator.get(OrePrefixes.circuit.get(Materials.ZPM), 6),
                GTOreDictUnificator.get(OrePrefixes.block.get(Materials.SiliconSG), 8),
                getModItem(SuperSolarPanels.ID, "solarsplitter", 4, 0))
            .itemOutputs(ItemList.Cover_SolarPanel_LuV.get(1))
            .fluidInputs(Materials.Polybenzimidazole.getMolten(1728))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .fake()
            .addTo(solarFactoryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                getModItem(NewHorizonsCoreMod.ID, "item.IrradiantReinforcedNaquadriaPlate", 8),
                ItemList.Circuit_Silicon_Wafer5.get(8),
                ItemList.Circuit_Wafer_QPIC.get(4),
                ItemList.Circuit_Chip_NPIC.get(8),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUV, 32),
                GTOreDictUnificator.get(OrePrefixes.circuit.get(Materials.UHV), 2),
                GTOreDictUnificator.get(OrePrefixes.circuit.get(Materials.UV), 4))
            .itemOutputs(ItemList.Cover_SolarPanel_ZPM.get(1))
            .fluidInputs(Materials.Polybenzimidazole.getMolten(2304))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .fake()
            .addTo(solarFactoryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                getModItem(NewHorizonsCoreMod.ID, "item.IrradiantReinforcedNaquadriaPlate", 8),
                ItemList.Circuit_Silicon_Wafer5.get(8),
                getModItem(NewHorizonsCoreMod.ID, "item.PicoWafer", 8),
                ItemList.Circuit_Chip_PPIC.get(8),
                ItemList.Circuit_Chip_CrystalSoC2.get(8),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 12),
                GTOreDictUnificator.get(OrePrefixes.circuit.get(Materials.UEV), 2),
                GTOreDictUnificator.get(OrePrefixes.circuit.get(Materials.UHV), 4),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense.get(Materials.SiliconSG), 4))
            .itemOutputs(ItemList.Cover_SolarPanel_UV.get(1))
            .fluidInputs(new FluidStack(solderIndalloy, 1152))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .fake()
            .addTo(solarFactoryRecipes);

        // Real recipes for the multi, hidden from NEI
        GTValues.RA.stdBuilder()
            .itemInputs(
                getModItem(NewHorizonsCoreMod.ID, "item.IrradiantReinforcedAluminiumPlate", 2),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorMV, 4),
                GTOreDictUnificator.get(OrePrefixes.circuit.get(Materials.HV), 2))
            .itemOutputs(ItemList.Cover_SolarPanel_LV.get(1))
            .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(288))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(SOLAR_FACTORY_WAFER_DATA, new SolarFactoryRecipeData(2, 4))
            .hidden()
            .addTo(solarFactoryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                getModItem(NewHorizonsCoreMod.ID, "item.IrradiantReinforcedTitaniumPlate", 2),
                ItemList.Circuit_Chip_ULPIC.get(4),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorHV, 4),
                GTOreDictUnificator.get(OrePrefixes.circuit.get(Materials.EV), 2))
            .itemOutputs(ItemList.Cover_SolarPanel_MV.get(1))
            .fluidInputs(Materials.Epoxid.getMolten(576))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(SOLAR_FACTORY_WAFER_DATA, new SolarFactoryRecipeData(2, 4))
            .hidden()
            .addTo(solarFactoryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                getModItem(NewHorizonsCoreMod.ID, "item.IrradiantReinforcedTungstenPlate", 4),
                ItemList.Circuit_Chip_LPIC.get(4),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorEV, 8),
                GTOreDictUnificator.get(OrePrefixes.circuit.get(Materials.IV), 4),
                GTOreDictUnificator.get(OrePrefixes.plate.get(Materials.IndiumGalliumPhosphide), 8))
            .itemOutputs(ItemList.Cover_SolarPanel_HV.get(1))
            .fluidInputs(Materials.Polybenzimidazole.getMolten(576))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .metadata(SOLAR_FACTORY_WAFER_DATA, new SolarFactoryRecipeData(2, 4))
            .hidden()
            .addTo(solarFactoryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                getModItem(NewHorizonsCoreMod.ID, "item.IrradiantReinforcedTungstenSteelPlate", 4),
                ItemList.Circuit_Chip_PIC.get(4),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 8),
                GTOreDictUnificator.get(OrePrefixes.circuit.get(Materials.LuV), 4),
                GTOreDictUnificator.get(OrePrefixes.plate.get(Materials.SiliconSG), 16))
            .itemOutputs(ItemList.Cover_SolarPanel_EV.get(1))
            .fluidInputs(Materials.Polybenzimidazole.getMolten(576))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .metadata(SOLAR_FACTORY_WAFER_DATA, new SolarFactoryRecipeData(3, 4))
            .hidden()
            .addTo(solarFactoryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                getModItem(NewHorizonsCoreMod.ID, "item.IrradiantReinforcedChromePlate", 4),
                ItemList.Circuit_Chip_HPIC.get(4),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 10),
                GTOreDictUnificator.get(OrePrefixes.circuit.get(Materials.ZPM), 4),
                GTOreDictUnificator.get(OrePrefixes.block.get(Materials.SiliconSG), 4))
            .itemOutputs(ItemList.Cover_SolarPanel_IV.get(1))
            .fluidInputs(Materials.Polybenzimidazole.getMolten(1152))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(SOLAR_FACTORY_WAFER_DATA, new SolarFactoryRecipeData(3, 8))
            .hidden()
            .addTo(solarFactoryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                getModItem(NewHorizonsCoreMod.ID, "item.IrradiantReinforcedChromePlate", 4),
                ItemList.Circuit_Chip_UHPIC.get(8),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 24),
                GTOreDictUnificator.get(OrePrefixes.circuit.get(Materials.UV), 2),
                GTOreDictUnificator.get(OrePrefixes.circuit.get(Materials.ZPM), 6),
                GTOreDictUnificator.get(OrePrefixes.block.get(Materials.SiliconSG), 8),
                getModItem(SuperSolarPanels.ID, "solarsplitter", 4, 0))
            .itemOutputs(ItemList.Cover_SolarPanel_LuV.get(1))
            .fluidInputs(Materials.Polybenzimidazole.getMolten(1728))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(SOLAR_FACTORY_WAFER_DATA, new SolarFactoryRecipeData(4, 8))
            .hidden()
            .addTo(solarFactoryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                getModItem(NewHorizonsCoreMod.ID, "item.IrradiantReinforcedNaquadriaPlate", 8),
                ItemList.Circuit_Wafer_QPIC.get(4),
                ItemList.Circuit_Chip_NPIC.get(8),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUV, 32),
                GTOreDictUnificator.get(OrePrefixes.circuit.get(Materials.UHV), 2),
                GTOreDictUnificator.get(OrePrefixes.circuit.get(Materials.UV), 4))
            .itemOutputs(ItemList.Cover_SolarPanel_ZPM.get(1))
            .fluidInputs(Materials.Polybenzimidazole.getMolten(2304))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(SOLAR_FACTORY_WAFER_DATA, new SolarFactoryRecipeData(5, 8))
            .hidden()
            .addTo(solarFactoryRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                getModItem(NewHorizonsCoreMod.ID, "item.IrradiantReinforcedNaquadriaPlate", 8),
                getModItem(NewHorizonsCoreMod.ID, "item.PicoWafer", 8),
                ItemList.Circuit_Chip_PPIC.get(8),
                ItemList.Circuit_Chip_CrystalSoC2.get(8),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 12),
                GTOreDictUnificator.get(OrePrefixes.circuit.get(Materials.UEV), 2),
                GTOreDictUnificator.get(OrePrefixes.circuit.get(Materials.UHV), 4),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense.get(Materials.SiliconSG), 4))
            .itemOutputs(ItemList.Cover_SolarPanel_UV.get(1))
            .fluidInputs(new FluidStack(solderIndalloy, 1152))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .metadata(SOLAR_FACTORY_WAFER_DATA, new SolarFactoryRecipeData(5, 8))
            .hidden()
            .addTo(solarFactoryRecipes);
    }
}
