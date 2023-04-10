package com.github.technus.tectech.loader.recipe;

import com.github.technus.tectech.thing.CustomItemList;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class CircuitAssembler implements Runnable{
    @Override
    public void run() {
        Fluid solderUEV = FluidRegistry.getFluid("molten.mutatedlivingsolder") != null
                ? FluidRegistry.getFluid("molten.mutatedlivingsolder")
                : FluidRegistry.getFluid("molten.solderingalloy");

        // Optical Processor
        GT_Values.RA.addCircuitAssemblerRecipe(
                new ItemStack[] { ItemList.Optically_Perfected_CPU.get(1L),
                        ItemList.Optically_Compatible_Memory.get(2L), ItemList.Circuit_Parts_CapacitorXSMD.get(16L),
                        ItemList.Circuit_Parts_DiodeXSMD.get(16L), CustomItemList.DATApipe.get(4L),
                        GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.EnrichedHolmium, 16) },
                new FluidStack(solderUEV, 288),
                ItemList.Circuit_OpticalProcessor.get(1L),
                20 * 20,
                614400,
                true);

        // Parametrizer Memory Card
        GT_Values.RA.addCircuitAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Basic, 2),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Gold, 4), },
                Materials.Plastic.getMolten(72),
                CustomItemList.parametrizerMemory.get(1),
                200,
                480,
                true);
    }
}
