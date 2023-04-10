package com.github.technus.tectech.loader.recipe;

import static com.github.technus.tectech.loader.recipe.BaseRecipeLoader.getItemContainer;
import static com.github.technus.tectech.loader.recipe.BaseRecipeLoader.getOrDefault;
import static gregtech.api.enums.GT_Values.RA;

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
        RA.addAssemblylineRecipe(
                ItemList.Hatch_DataAccess_EV.get(1),
                20000,
                new Object[] { CustomItemList.Machine_Multi_Switch.get(1),
                        new Object[] { OrePrefixes.circuit.get(Materials.Master), 2 }, ItemList.Tool_DataOrb.get(1),
                        ItemList.Cover_Screen.get(1), },
                new FluidStack[] { new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000),
                        Materials.Hydrogen.getGas(1000), },
                CustomItemList.Machine_Multi_DataBank.get(1),
                12000,
                14000);

        // Hollow Casing
        GT_Values.RA.addAssemblylineRecipe(
                CustomItemList.eM_Containment.get(1),
                7500,
                new ItemStack[] { CustomItemList.eM_Containment.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Europium, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.Plutonium, 4),
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Lead, 8),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Uranium, 16),
                        GT_OreDictUnificator
                                .get(OrePrefixes.screw, getOrDefault("Quantium", Materials.Europium), 16), },
                new FluidStack[] { getOrDefault("Trinium", Materials.Osmium).getMolten(1296),
                        Materials.Osmium.getMolten(1296), new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000),
                        Materials.Argon.getGas(1000), },
                CustomItemList.eM_Hollow.get(2),
                200,
                200000);

        // EM Coil
        GT_Values.RA.addAssemblylineRecipe(
                CustomItemList.eM_Hollow.get(1),
                7500,
                new ItemStack[] { CustomItemList.eM_Hollow.get(1), ItemList.Casing_Fusion_Coil.get(2),
                        ItemList.Casing_Coil_NaquadahAlloy.get(2),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Europium, 64),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Europium, 64), },
                new FluidStack[] { Materials.Glass.getMolten(2304), Materials.Silicone.getMolten(1872),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000),
                        getOrDefault("Trinium", Materials.Osmium).getMolten(1296), },
                CustomItemList.eM_Coil.get(4),
                800,
                200000);

        // Object Holder
        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Hatch_Input_Bus_ZPM.get(1),
                10000,
                new Object[] { ItemList.Hatch_Input_Bus_ZPM.get(1), CustomItemList.eM_Computer_Bus.get(1),
                        ItemList.Emitter_ZPM.get(8), ItemList.Robot_Arm_ZPM.get(1), ItemList.Electric_Motor_ZPM.get(2),
                        new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 1 },
                        new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.cableGt02, Materials.Naquadah, 2) },
                        new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Naquadah, 16) },
                        CustomItemList.DATApipe.get(2), },
                new FluidStack[] { Materials.UUMatter.getFluid(500), Materials.Iridium.getMolten(1296),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 1000) },
                CustomItemList.holder_Hatch.get(1),
                1200,
                100000);

        // Quantum Computer
        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Tool_DataOrb.get(1),
                20000,
                new Object[] { CustomItemList.Machine_Multi_Switch.get(1),
                        new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 2 },
                        ItemList.Tool_DataOrb.get(1), ItemList.Cover_Screen.get(1),
                        new ItemStack[] {
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUV, 8) },
                        CustomItemList.DATApipe.get(8), },
                new FluidStack[] { Materials.UUMatter.getFluid(1000), Materials.Iridium.getMolten(1296),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000), Materials.Hydrogen.getGas(1000), },
                CustomItemList.Machine_Multi_Computer.get(1),
                12000,
                100000);

        // Research Station
        GT_Values.RA.addAssemblylineRecipe(
                getItemContainer("ScannerZPM").get(1),
                80000,
                new Object[] { CustomItemList.Machine_Multi_Switch.get(1), ItemList.Sensor_ZPM.get(8),
                        new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 4 },
                        ItemList.Field_Generator_ZPM.get(1), ItemList.Electric_Motor_ZPM.get(2),
                        new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.cableGt02, Materials.Naquadah, 4) },
                        new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Naquadah, 32) },
                        CustomItemList.DATApipe.get(16), },
                new FluidStack[] { Materials.UUMatter.getFluid(1000), Materials.Iridium.getMolten(1296),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000), Materials.Osmium.getMolten(1296), },
                CustomItemList.Machine_Multi_Research.get(1),
                12000,
                100000);

        // Multi Infuser
        GT_Values.RA.addAssemblylineRecipe(
                CustomItemList.Machine_Multi_Transformer.get(1),
                7500,
                new ItemStack[] { CustomItemList.Machine_Multi_Transformer.get(1), CustomItemList.eM_Coil.get(8),
                        CustomItemList.eM_Power.get(8),
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.NeodymiumMagnetic, 16), },
                new FluidStack[] { Materials.Electrum.getMolten(2592), Materials.Europium.getMolten(1872),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000), },
                CustomItemList.Machine_Multi_Infuser.get(1),
                8000,
                200000);
    }
}
