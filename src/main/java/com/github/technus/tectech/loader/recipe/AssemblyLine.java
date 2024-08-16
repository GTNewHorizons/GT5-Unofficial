package com.github.technus.tectech.loader.recipe;

import static com.github.technus.tectech.loader.recipe.BaseRecipeLoader.getOrDefault;
import static gregtech.api.util.GT_RecipeBuilder.HOURS;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeConstants.AssemblyLine;
import static gregtech.api.util.GT_RecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GT_RecipeConstants.RESEARCH_TIME;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.github.technus.tectech.thing.CustomItemList;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;

public class AssemblyLine implements Runnable {

    @Override
    public void run() {
        // Data Bank
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Hatch_DataAccess_EV.get(1))
            .metadata(RESEARCH_TIME, 16 * MINUTES + 20 * SECONDS)
            .itemInputs(
                CustomItemList.Machine_Multi_Switch.get(1),
                new Object[] { OrePrefixes.circuit.get(Materials.LuV), 2 },
                ItemList.Tool_DataOrb.get(1),
                ItemList.Cover_Screen.get(1))
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000), Materials.Hydrogen.getGas(1000))
            .itemOutputs(CustomItemList.Machine_Multi_DataBank.get(1))
            .eut(14_000)
            .duration(10 * MINUTES)
            .addTo(AssemblyLine);

        // Hollow Casing
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, CustomItemList.eM_Containment.get(1))
            .metadata(RESEARCH_TIME, 6 * MINUTES + 15 * SECONDS)
            .itemInputs(
                CustomItemList.eM_Containment.get(1),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Europium, 2),
                GT_OreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.Plutonium, 4),
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Lead, 8),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Uranium, 16),
                GT_OreDictUnificator.get(OrePrefixes.screw, getOrDefault("Quantium", Materials.Europium), 16))
            .fluidInputs(
                getOrDefault("Trinium", Materials.Osmium).getMolten(1296),
                Materials.Osmium.getMolten(1296),
                new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000),
                Materials.Argon.getGas(1000))
            .itemOutputs(CustomItemList.eM_Hollow.get(2))
            .eut(200_000)
            .duration(10 * SECONDS)
            .addTo(AssemblyLine);

        // EM Coil
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, CustomItemList.eM_Hollow.get(1))
            .metadata(RESEARCH_TIME, 6 * MINUTES + 15 * SECONDS)
            .itemInputs(
                CustomItemList.eM_Hollow.get(1),
                ItemList.Casing_Fusion_Coil.get(2),
                ItemList.Casing_Coil_NaquadahAlloy.get(2),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Europium, 64),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Europium, 64))
            .fluidInputs(
                Materials.Glass.getMolten(2304),
                Materials.Silicone.getMolten(1872),
                new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000),
                getOrDefault("Trinium", Materials.Osmium).getMolten(1296))
            .itemOutputs(CustomItemList.eM_Coil.get(4))
            .eut(200_000)
            .duration(40 * SECONDS)
            .addTo(AssemblyLine);

        // Object Holder
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Hatch_Input_Bus_ZPM.get(1))
            .metadata(RESEARCH_TIME, 8 * MINUTES + 20 * SECONDS)
            .itemInputs(
                ItemList.Hatch_Input_Bus_ZPM.get(1),
                CustomItemList.eM_Computer_Bus.get(1),
                ItemList.Emitter_ZPM.get(8),
                ItemList.Robot_Arm_ZPM.get(1),
                ItemList.Electric_Motor_ZPM.get(2),
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 1 },
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.cableGt02, Materials.Naquadah, 2) },
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Naquadah, 16) },
                CustomItemList.DATApipe.get(2))
            .fluidInputs(
                Materials.UUMatter.getFluid(500),
                Materials.Iridium.getMolten(1296),
                new FluidStack(FluidRegistry.getFluid("ic2coolant"), 1000))
            .itemOutputs(CustomItemList.holder_Hatch.get(1))
            .eut(100_000)
            .duration(10 * MINUTES)
            .addTo(AssemblyLine);

        // Quantum Computer
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Tool_DataOrb.get(1))
            .metadata(RESEARCH_TIME, 16 * MINUTES + 40 * SECONDS)
            .itemInputs(
                CustomItemList.Machine_Multi_Switch.get(1),
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 2 },
                ItemList.Tool_DataOrb.get(1),
                ItemList.Cover_Screen.get(1),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUV, 8) },
                CustomItemList.DATApipe.get(8))
            .fluidInputs(
                Materials.UUMatter.getFluid(1000),
                Materials.Iridium.getMolten(1296),
                new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000),
                Materials.Hydrogen.getGas(1000))
            .itemOutputs(CustomItemList.Machine_Multi_Computer.get(1))
            .eut(100_000)
            .duration(10 * MINUTES)
            .addTo(AssemblyLine);

        // Research Station
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.ScannerZPM.get(1))
            .metadata(RESEARCH_TIME, 1 * HOURS + 6 * MINUTES + 40 * SECONDS)
            .itemInputs(
                CustomItemList.Machine_Multi_Switch.get(1),
                ItemList.Sensor_ZPM.get(8),
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 4 },
                ItemList.Field_Generator_ZPM.get(1),
                ItemList.Electric_Motor_ZPM.get(2),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.cableGt02, Materials.Naquadah, 4) },
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Naquadah, 32) },
                CustomItemList.DATApipe.get(16))
            .fluidInputs(
                Materials.UUMatter.getFluid(1000),
                Materials.Iridium.getMolten(1296),
                new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000),
                Materials.Osmium.getMolten(1296))
            .itemOutputs(CustomItemList.Machine_Multi_Research.get(1))
            .eut(100_000)
            .duration(10 * MINUTES)
            .addTo(AssemblyLine);

        // Multi Infuser
        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, CustomItemList.Machine_Multi_Transformer.get(1))
            .metadata(RESEARCH_TIME, 6 * MINUTES + 15 * SECONDS)
            .itemInputs(
                CustomItemList.Machine_Multi_Transformer.get(1),
                CustomItemList.eM_Coil.get(8),
                CustomItemList.eM_Power.get(8),
                GT_OreDictUnificator.get(OrePrefixes.screw, Materials.NeodymiumMagnetic, 16))
            .fluidInputs(
                Materials.Electrum.getMolten(2592),
                Materials.Europium.getMolten(1872),
                new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000))
            .itemOutputs(CustomItemList.Machine_Multi_Infuser.get(1))
            .eut(200_000)
            .duration(6 * MINUTES + 40 * SECONDS)
            .addTo(AssemblyLine);
    }
}
