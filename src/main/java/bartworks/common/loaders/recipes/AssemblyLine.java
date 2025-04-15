package bartworks.common.loaders.recipes;

import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.*;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import bartworks.common.loaders.ItemRegistry;
import bartworks.system.material.WerkstoffLoader;
import goodgenerator.items.GGMaterial;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.recipe.Scanning;

public class AssemblyLine implements Runnable {

    @Override
    public void run() {
        Fluid solderIndalloy = FluidRegistry.getFluid("molten.indalloy140") != null
            ? FluidRegistry.getFluid("molten.indalloy140")
            : FluidRegistry.getFluid("molten.solderingalloy");

        // Void Miner Mk1
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.OreDrill4.get(1L))
            .metadata(SCANNING, new Scanning(2 * MINUTES, TierEU.RECIPE_LuV))
            .itemInputs(
                ItemList.OreDrill4.get(1L),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Tritanium, 9L),
                Materials.Europium.getPlates(3),
                ItemList.Electric_Motor_LuV.get(9L),
                ItemList.Sensor_LuV.get(9L),
                ItemList.Field_Generator_LuV.get(9L),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.Europium, 36L))
            .fluidInputs(new FluidStack(solderIndalloy, 1440), WerkstoffLoader.Neon.getFluidOrGas(20000))
            .itemOutputs(ItemRegistry.voidminer[0].copy())
            .eut(TierEU.RECIPE_LuV)
            .duration(5 * MINUTES)
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
                WerkstoffLoader.LuVTierMaterial.get(OrePrefixes.plate, 8))
            .fluidInputs(new FluidStack(solderIndalloy, 1440))
            .itemOutputs(ItemRegistry.cal.copy())
            .eut(TierEU.RECIPE_ZPM)
            .duration(60 * SECONDS)
            .addTo(AssemblyLine);

        // Mega Electric Blast Furnace
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Machine_Multi_BlastFurnace.get(1L))
            .metadata(SCANNING, new Scanning(2 * MINUTES, TierEU.RECIPE_IV))
            .itemInputs(
                ItemList.Machine_Multi_BlastFurnace.get(64L),
                ItemList.Machine_Multi_BlastFurnace.get(64L),
                ItemList.Machine_Multi_BlastFurnace.get(64L),
                ItemList.Machine_Multi_BlastFurnace.get(64L),
                ItemList.LuV_Coil.get(16L),
                ItemList.Conveyor_Module_LuV.get(4L, new Object() {}),
                new Object[] { OrePrefixes.circuit.get(Materials.LuV), 8 },
                new Object[] { OrePrefixes.circuit.get(Materials.EV), 16 },
                ItemList.Circuit_Chip_HPIC.get(16, new Object() {}),
                Materials.Osmiridium.getPlates(16),
                WerkstoffLoader.LuVTierMaterial.get(OrePrefixes.plate, 16),
                WerkstoffLoader.Ruridit.get(OrePrefixes.screw, 16))
            .fluidInputs(
                GGMaterial.lumiium.getMolten(1440),
                Materials.Mytryl.getMolten(1440),
                Materials.SolderingAlloy.getMolten(1440))
            .itemOutputs(ItemRegistry.megaMachines[0])
            .eut(TierEU.RECIPE_LuV)
            .duration(5 * MINUTES)
            .addTo(AssemblyLine);
    }
}
