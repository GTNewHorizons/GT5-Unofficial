package bartworks.common.loaders.recipes;

import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.AssemblyLine;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GTRecipeConstants.SCANNING;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.common.loaders.ItemRegistry;
import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.recipe.Scanning;
import gtPlusPlus.core.material.MaterialsAlloy;

@SuppressWarnings({ "PointlessArithmeticExpression" })
public class AssemblyLine implements Runnable {

    @Override
    public void run() {
        // Void Miner Mk1
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.OreDrill4.get(1L))
            .metadata(SCANNING, new Scanning(2 * MINUTES, TierEU.RECIPE_LuV))
            .itemInputs(
                ItemList.OreDrill4.get(1L),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Netherite, 4L),
                MaterialLibAPI.getStack(Materials2Materials.Europium, Materials2Shapes.shapePlate, (int) (3)),
                ItemList.Electric_Motor_LuV.get(9L),
                ItemList.Sensor_LuV.get(9L),
                ItemList.Field_Generator_LuV.get(9L),
                MaterialLibAPI.getStack(Materials2Materials.Europium, Materials2Shapes.shapeScrew, (int) (36L)))
            .fluidInputs(
                MaterialsAlloy.INDALLOY_140.getFluidStack(10 * INGOTS),
                WerkstoffLoader.Neon.getFluidOrGas(20_000))
            .itemOutputs(ItemRegistry.voidminer[0].copy())
            .eut(TierEU.RECIPE_LuV)
            .duration(2 * MINUTES)
            .addTo(AssemblyLine);

        // Circuit Assembly Line
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Machine_LuV_CircuitAssembler.get(1L))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 40 * SECONDS, TierEU.RECIPE_IV))
            .itemInputs(
                ItemList.Machine_LuV_CircuitAssembler.get(1L),
                ItemList.Robot_Arm_LuV.get(4L),
                ItemList.Electric_Motor_LuV.get(4L),
                ItemList.Field_Generator_LuV.get(1L),
                ItemList.Emitter_LuV.get(1L),
                ItemList.Sensor_LuV.get(1L),
                WerkstoffLoader.RhodiumPlatedPalladium.get(OrePrefixes.plate, 8))
            .fluidInputs(MaterialsAlloy.INDALLOY_140.getFluidStack(10 * INGOTS))
            .itemOutputs(ItemRegistry.cal.copy())
            .eut(TierEU.RECIPE_ZPM)
            .duration(60 * SECONDS)
            .addTo(AssemblyLine);

        // PCB Cooling Tower
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.ReinforcedPhotolithographicFrameworkCasing.get(1L))
            .metadata(SCANNING, new Scanning(3 * MINUTES + 20 * SECONDS, TierEU.ZPM))
            .itemInputs(
                ItemList.ReinforcedPhotolithographicFrameworkCasing.get(4L),
                ItemList.Casing_Coil_Superconductor.get(16L),
                new Object[] { OrePrefixes.circuit.get(Materials.UHV), 2L },
                MaterialLibAPI.getStack(Materials2Materials.DamascusSteel, Materials2Shapes.shapeFoil, (int) (16)),
                ItemList.Electric_Pump_UV.get(1L),
                ItemList.Sensor_UV.get(4L),
                MaterialLibAPI.getStack(Materials2Materials.Infinity, Materials2Shapes.shapeWireFine, (int) (32)))
            .fluidInputs(
                MaterialsAlloy.INDALLOY_140.getFluidStack(32 * INGOTS),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SuperCoolant,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (8000)))
            .itemOutputs(ItemList.PCBCoolingTower.get(1L))
            .eut(TierEU.RECIPE_UV)
            .duration(300 * SECONDS)
            .addTo(AssemblyLine);

        // PCB Bio Chamber
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Circuit_Board_Wetware_Extreme.get(1L))
            .metadata(SCANNING, new Scanning(3 * MINUTES + 20 * SECONDS, TierEU.ZPM))
            .itemInputs(
                ItemList.Casing_CleanStainlessSteel.get(4L),
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 2L },
                ItemList.Circuit_Chip_Stemcell.get(64L),
                ItemList.FluidRegulator_ZPM.get(1L),
                ItemList.Electric_Pump_ZPM.get(1L),
                ItemList.Sensor_ZPM.get(4L),
                MaterialLibAPI.getStack(Materials2Materials.Holmium, Materials2Shapes.shapeWireFine, (int) (32)))
            .fluidInputs(
                MaterialsAlloy.INDALLOY_140.getFluidStack(32 * INGOTS),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.GrowthMediumSterilized,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (27648)))
            .itemOutputs(ItemList.PCBBioChamber.get(1L))
            .eut(TierEU.RECIPE_UV)
            .duration(300 * SECONDS)
            .addTo(AssemblyLine);
    }
}
