package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.Mods.SuperSolarPanels;
import static gregtech.api.util.GT_ModHandler.getModItem;
import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sSolarFactoryRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

public class SolarFactoryRecipes implements Runnable {

    @Override
    public void run() {
        // All the solar cells (1V - UV)
        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(NewHorizonsCoreMod.ID, "item.ReinforcedGlassPlate", 2),
                getModItem(NewHorizonsCoreMod.ID, "item.AluminiumIronPlate", 1),
                GT_ModHandler.getIC2Item("carbonPlate", 1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.RedAlloy, 1),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Basic), 1))
            .itemOutputs(ItemList.Solar_Cell.get(2))
            .noFluidInputs()
            .noFluidOutputs()
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(sSolarFactoryRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(NewHorizonsCoreMod.ID, "item.ReinforcedAluminiumIronPlate", 1),
                ItemList.Circuit_Silicon_Wafer.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Tin, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.GalliumArsenide, 2),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Good), 2))
            .itemOutputs(ItemList.Solar_Cell_8V.get(2))
            .noFluidInputs()
            .noFluidOutputs()
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(sSolarFactoryRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(NewHorizonsCoreMod.ID, "item.IrradiantReinforcedAluminiumPlate", 1),
                ItemList.Circuit_Silicon_Wafer2.get(4),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorMV, 4),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Advanced), 2))
            .itemOutputs(ItemList.Solar_Cell_LV.get(2))
            .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(288))
            .noFluidOutputs()
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sSolarFactoryRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(NewHorizonsCoreMod.ID, "item.IrradiantReinforcedTitaniumPlate", 1),
                ItemList.Circuit_Silicon_Wafer2.get(4),
                ItemList.Circuit_Chip_ULPIC.get(2),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorHV, 4),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Data), 2))
            .itemOutputs(ItemList.Solar_Cell_MV.get(2))
            .fluidInputs(Materials.Epoxid.getMolten(288))
            .noFluidOutputs()
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(sSolarFactoryRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(NewHorizonsCoreMod.ID, "item.IrradiantReinforcedTungstenPlate", 1),
                ItemList.Circuit_Silicon_Wafer2.get(4),
                ItemList.Circuit_Chip_LPIC.get(2),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorEV, 4),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Elite), 4),
                GT_OreDictUnificator.get(OrePrefixes.plate.get(Materials.IndiumGalliumPhosphide), 2))
            .itemOutputs(ItemList.Solar_Cell_HV.get(2))
            .noFluidInputs()
            .noFluidOutputs()
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(sSolarFactoryRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(NewHorizonsCoreMod.ID, "item.IrradiantReinforcedTungstenPlate", 1),
                ItemList.Circuit_Silicon_Wafer3.get(4),
                ItemList.Circuit_Chip_PIC.get(2),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 6),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Master), 4),
                GT_OreDictUnificator.get(OrePrefixes.plate.get(Materials.SiliconSG), 12))
            .itemOutputs(ItemList.Solar_Cell_EV.get(2))
            .fluidInputs(Materials.Polybenzimidazole.getMolten(288))
            .noFluidOutputs()
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(sSolarFactoryRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(NewHorizonsCoreMod.ID, "item.IrradiantReinforcedChromePlate", 1),
                ItemList.Circuit_Silicon_Wafer3.get(4),
                ItemList.Circuit_Silicon_Wafer2.get(4),
                ItemList.Circuit_Chip_HPIC.get(2),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 6),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Ultimate), 4),
                GT_OreDictUnificator.get(OrePrefixes.plate.get(Materials.SiliconSG), 16))
            .itemOutputs(ItemList.Solar_Cell_IV.get(2))
            .fluidInputs(Materials.Polybenzimidazole.getMolten(576))
            .noFluidOutputs()
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(sSolarFactoryRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(NewHorizonsCoreMod.ID, "item.IrradiantReinforcedChromePlate", 4),
                ItemList.Circuit_Silicon_Wafer4.get(4),
                ItemList.Circuit_Silicon_Wafer3.get(4),
                ItemList.Circuit_Chip_UHPIC.get(2),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 16),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Superconductor), 2),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Ultimate), 4),
                GT_OreDictUnificator.get(OrePrefixes.plate.get(Materials.SiliconSG), 20),
                getModItem(SuperSolarPanels.ID, "item.SuperSolarPanels.solarsplitter", 2))
            .itemOutputs(ItemList.Solar_Cell_LuV.get(2))
            .fluidInputs(Materials.Polybenzimidazole.getMolten(864))
            .noFluidOutputs()
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(sSolarFactoryRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(NewHorizonsCoreMod.ID, "item.IrradiantReinforcedNaquadriaPlate", 4),
                ItemList.Circuit_Silicon_Wafer5.get(2),
                ItemList.Circuit_Silicon_Wafer4.get(4),
                ItemList.Circuit_Chip_QPIC.get(2),
                ItemList.Circuit_Chip_NPIC.get(4),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUV, 24),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Infinite), 2),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Superconductor), 2))
            .itemOutputs(ItemList.Solar_Cell_ZPM.get(2))
            .fluidInputs(Materials.Polybenzimidazole.getMolten(1152))
            .noFluidOutputs()
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(sSolarFactoryRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                getModItem(NewHorizonsCoreMod.ID, "item.IrradiantReinforcedNaquadriaPlate", 4),
                ItemList.Circuit_Silicon_Wafer5.get(4),
                ItemList.Circuit_Wafer_PPIC.get(4),
                ItemList.Circuit_Chip_PPIC.get(4),
                ItemList.Circuit_Chip_CrystalSoC2.get(2),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 24),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Bio), 2),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Infinite), 2),
                GT_OreDictUnificator.get(OrePrefixes.plate.get(Materials.SiliconSG), 64))
            .itemOutputs(ItemList.Solar_Cell_UV.get(2))
            .fluidInputs(Materials.Polybenzimidazole.getMolten(2592))
            .noFluidOutputs()
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(sSolarFactoryRecipes);

        // All the direct cell upgrades (1V-UV)
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Solar_Cell.get(2),
                getModItem(NewHorizonsCoreMod.ID, "item.ReinforcedAluminiumIronPlate", 1),
                ItemList.Circuit_Silicon_Wafer.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Tin, 1),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Good), 1))
            .itemOutputs(ItemList.Solar_Cell_8V.get(2))
            .noFluidInputs()
            .noFluidOutputs()
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(sSolarFactoryRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Solar_Cell_8V.get(2),
                getModItem(NewHorizonsCoreMod.ID, "item.IrradiantReinforcedAluminiumPlate", 1),
                ItemList.Circuit_Silicon_Wafer2.get(2),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorMV, 2),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Advanced), 1))
            .itemOutputs(ItemList.Solar_Cell_LV.get(2))
            .fluidInputs(Materials.Plastic.getMolten(144))
            .noFluidOutputs()
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sSolarFactoryRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Solar_Cell_LV.get(2),
                getModItem(NewHorizonsCoreMod.ID, "item.IrradiantReinforcedTitaniumPlate", 1),
                ItemList.Circuit_Silicon_Wafer2.get(2),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorHV, 1),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Data), 1))
            .itemOutputs(ItemList.Solar_Cell_MV.get(2))
            .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(72))
            .noFluidOutputs()
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(sSolarFactoryRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Solar_Cell_MV.get(2),
                getModItem(NewHorizonsCoreMod.ID, "item.IrradiantReinforcedTungstenPlate", 1),
                ItemList.Circuit_Silicon_Wafer2.get(2),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorEV, 1),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Elite), 2))
            .itemOutputs(ItemList.Solar_Cell_HV.get(2))
            .fluidInputs(Materials.Epoxid.getMolten(144))
            .noFluidOutputs()
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(sSolarFactoryRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Solar_Cell_HV.get(2),
                getModItem(NewHorizonsCoreMod.ID, "item.IrradiantReinforcedTungstenSteelPlate", 1),
                ItemList.Circuit_Silicon_Wafer3.get(2),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 1),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Master), 2),
                GT_OreDictUnificator.get(OrePrefixes.plate.get(Materials.SiliconSG), 4))
            .itemOutputs(ItemList.Solar_Cell_EV.get(2))
            .fluidInputs(Materials.Polybenzimidazole.getMolten(144))
            .noFluidOutputs()
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(sSolarFactoryRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Solar_Cell_EV.get(2),
                getModItem(NewHorizonsCoreMod.ID, "item.IrradiantReinforcedChromePlate", 1),
                ItemList.Circuit_Silicon_Wafer3.get(4),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 2),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Ultimate), 2),
                GT_OreDictUnificator.get(OrePrefixes.plate.get(Materials.SiliconSG), 4))
            .itemOutputs(ItemList.Solar_Cell_IV.get(2))
            .fluidInputs(Materials.IndiumGalliumPhosphide.getMolten(288))
            .noFluidOutputs()
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(sSolarFactoryRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Solar_Cell_IV.get(2),
                getModItem(NewHorizonsCoreMod.ID, "item.IrradiantReinforcedChromePlate", 1),
                ItemList.Circuit_Silicon_Wafer4.get(3),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 6),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Superconductor), 2),
                GT_OreDictUnificator.get(OrePrefixes.plate.get(Materials.SiliconSG), 4),
                getModItem(SuperSolarPanels.ID, "item.SuperSolarPanels.solarsplitter", 1))
            .itemOutputs(ItemList.Solar_Cell_LuV.get(2))
            .fluidInputs(Materials.IndiumGalliumPhosphide.getMolten(288))
            .noFluidOutputs()
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(sSolarFactoryRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Solar_Cell_LuV.get(2),
                getModItem(NewHorizonsCoreMod.ID, "item.IrradiantReinforcedNaquadriaPlate", 1),
                ItemList.Circuit_Silicon_Wafer5.get(4),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUV, 8),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Superconductor), 4),
                GT_OreDictUnificator.get(OrePrefixes.plate.get(Materials.Polybenzimidazole), 4),
                getModItem(SuperSolarPanels.ID, "item.SuperSolarPanels.enderquantumcomponent", 1))
            .itemOutputs(ItemList.Solar_Cell_ZPM.get(2))
            .fluidInputs(Materials.Europium.getMolten(288))
            .noFluidOutputs()
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(sSolarFactoryRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Solar_Cell_ZPM.get(2),
                getModItem(NewHorizonsCoreMod.ID, "item.IrradiantReinforcedNeutroniumPlate", 1),
                getModItem(NewHorizonsCoreMod.ID, "item.PicoWafer", 2),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 12),
                GT_OreDictUnificator.get(OrePrefixes.circuit.get(Materials.Infinite), 4),
                GT_OreDictUnificator.get(OrePrefixes.block.get(Materials.SiliconSG), 9),
                getModItem(SuperSolarPanels.ID, "item.SuperSolarPanels.enderquantumcomponent", 2))
            .itemOutputs(ItemList.Solar_Cell_UV.get(2))
            .fluidInputs(Materials.Americium.getMolten(288))
            .noFluidOutputs()
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(sSolarFactoryRecipes);

        // All the Cells -> Solar Cover conversions
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Solar_Cell.get(4), ItemList.Circuit_Board_Coated_Basic.get(1))
            .itemOutputs(ItemList.Cover_SolarPanel.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(72))
            .noFluidOutputs()
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(sSolarFactoryRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Solar_Cell_8V.get(4), ItemList.Circuit_Board_Coated_Basic.get(1))
            .itemOutputs(ItemList.Cover_SolarPanel_8V.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(72))
            .noFluidOutputs()
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(sSolarFactoryRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Solar_Cell_LV.get(4), ItemList.Circuit_Board_Phenolic_Good.get(1))
            .itemOutputs(ItemList.Cover_SolarPanel_LV.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(72))
            .noFluidOutputs()
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sSolarFactoryRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Solar_Cell_MV.get(4), ItemList.Circuit_Board_Phenolic_Good.get(1))
            .itemOutputs(ItemList.Cover_SolarPanel_MV.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(72))
            .noFluidOutputs()
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sSolarFactoryRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Solar_Cell_HV.get(4), ItemList.Circuit_Board_Epoxy_Advanced.get(1))
            .itemOutputs(ItemList.Cover_SolarPanel_HV.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(72))
            .noFluidOutputs()
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(sSolarFactoryRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Solar_Cell_EV.get(4), ItemList.Circuit_Board_Epoxy_Advanced.get(1))
            .itemOutputs(ItemList.Cover_SolarPanel_EV.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(72))
            .noFluidOutputs()
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(sSolarFactoryRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Solar_Cell_IV.get(4), ItemList.Circuit_Board_Epoxy_Advanced.get(1))
            .itemOutputs(ItemList.Cover_SolarPanel_IV.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(72))
            .noFluidOutputs()
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(sSolarFactoryRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Solar_Cell_LuV.get(4), ItemList.Circuit_Board_Fiberglass_Advanced.get(1))
            .itemOutputs(ItemList.Cover_SolarPanel_LuV.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(72))
            .noFluidOutputs()
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(sSolarFactoryRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Solar_Cell_ZPM.get(4), ItemList.Circuit_Board_Multifiberglass_Elite.get(1))
            .itemOutputs(ItemList.Cover_SolarPanel_ZPM.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(72))
            .noFluidOutputs()
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(sSolarFactoryRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Solar_Cell_UV.get(4), ItemList.Circuit_Board_Multifiberglass_Elite.get(1))
            .itemOutputs(ItemList.Cover_SolarPanel_UV.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(72))
            .noFluidOutputs()
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(sSolarFactoryRecipes);

        // 1 of higher tier sell -> 1 of lower tier panel
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Solar_Cell_LV.get(1), GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Solar_Cell_8V.get(4))
            .fluidInputs(Materials.SolderingAlloy.getMolten(288))
            .noFluidOutputs()
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sSolarFactoryRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Solar_Cell_MV.get(1), GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Solar_Cell_LV.get(4))
            .fluidInputs(Materials.SolderingAlloy.getMolten(288))
            .noFluidOutputs()
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sSolarFactoryRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Solar_Cell_HV.get(1), GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Solar_Cell_MV.get(4))
            .fluidInputs(Materials.SolderingAlloy.getMolten(288))
            .noFluidOutputs()
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(sSolarFactoryRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Solar_Cell_EV.get(1), GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Solar_Cell_HV.get(4))
            .fluidInputs(Materials.SolderingAlloy.getMolten(288))
            .noFluidOutputs()
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_IV)
            .addTo(sSolarFactoryRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Solar_Cell_IV.get(1), GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Solar_Cell_EV.get(4))
            .fluidInputs(Materials.SolderingAlloy.getMolten(288))
            .noFluidOutputs()
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(sSolarFactoryRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Solar_Cell_LuV.get(1), GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Solar_Cell_IV.get(4))
            .fluidInputs(Materials.SolderingAlloy.getMolten(288))
            .noFluidOutputs()
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(sSolarFactoryRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Solar_Cell_ZPM.get(1), GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Solar_Cell_LuV.get(4))
            .fluidInputs(Materials.SolderingAlloy.getMolten(288))
            .noFluidOutputs()
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_UV)
            .addTo(sSolarFactoryRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Solar_Cell_UV.get(1), GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Solar_Cell_ZPM.get(4))
            .fluidInputs(Materials.SolderingAlloy.getMolten(288))
            .noFluidOutputs()
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(sSolarFactoryRecipes);

    }
}
