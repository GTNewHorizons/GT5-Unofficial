package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
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
            .itemInputs(ItemList.Solar_Cell_8V.get(1), GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Solar_Cell.get(4))
            .fluidInputs(Materials.SolderingAlloy.getMolten(288))
            .noFluidOutputs()
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(sSolarFactoryRecipes);

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
