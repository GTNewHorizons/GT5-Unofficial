package net.glease.ggfab;

import static gregtech.api.enums.GT_Values.RA;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

class ComponentRecipeLoader implements Runnable {

    @Override
    public void run() {
        Fluid solderIndalloy = FluidRegistry.getFluid("molten.indalloy140") != null
                ? FluidRegistry.getFluid("molten.indalloy140")
                : FluidRegistry.getFluid("molten.solderingalloy");
        RA.addAssemblylineRecipe(
                ItemList.Machine_Multi_Assemblyline.get(1L),
                96000,
                new Object[] { ItemList.Machine_Multi_Assemblyline.get(1L),
                        new Object[] { OrePrefixes.circuit.get(Materials.Master), 2 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Elite), 4 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Data), 8 },
                        ItemList.Automation_ChestBuffer_LuV.get(1L), },
                new FluidStack[] { new FluidStack(solderIndalloy, 1296), Materials.Lubricant.getFluid(2000) },
                GGItemList.AdvAssLine.get(1L),
                1200,
                6000);
        RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hatch_Input_Bus_IV.get(1L), ItemList.Emitter_IV.get(1L),
                        ItemList.Sensor_IV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Enderium, 1L),
                        GT_Utility.getIntegratedCircuit(12), },
                Materials.Polybenzimidazole.getMolten(144L),
                GGItemList.LinkedInputBus.get(1L),
                600,
                (int) GT_Values.VP[5]);
    }
}
