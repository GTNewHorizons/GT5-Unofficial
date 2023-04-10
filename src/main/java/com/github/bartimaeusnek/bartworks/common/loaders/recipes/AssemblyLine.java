package com.github.bartimaeusnek.bartworks.common.loaders.recipes;

import static gregtech.api.enums.Mods.GalactiGreg;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.github.bartimaeusnek.bartworks.util.BW_Util;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;

public class AssemblyLine implements Runnable {

    @Override
    public void run() {
        Fluid solderIndalloy = FluidRegistry.getFluid("molten.indalloy140") != null
                ? FluidRegistry.getFluid("molten.indalloy140")
                : FluidRegistry.getFluid("molten.solderingalloy");

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Pump_IV.get(1L),
                72000,
                new ItemStack[] { ItemList.Pump_IV.get(16),
                        GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Ultimate, 32L),
                        GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.HSSE, 16L),
                        GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.HSSE, 16L),
                        ItemList.Field_Generator_LuV.get(8) },
                new FluidStack[] { new FluidStack(solderIndalloy, 32 * 144),
                        Materials.Polytetrafluoroethylene.getMolten(32 * 144) },
                ItemRegistry.dehp,
                5000,
                BW_Util.getMachineVoltageFromTier(6));

        if (GalactiGreg.isModLoaded()) {
            GT_Values.RA.addAssemblylineRecipe(
                    ItemList.OreDrill4.get(1L),
                    BW_Util.getMachineVoltageFromTier(6),
                    new Object[] { ItemList.OreDrill4.get(1L),
                            GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Europium, 9L),
                            Materials.Europium.getPlates(3), ItemList.Electric_Motor_LuV.get(9L),
                            ItemList.Sensor_LuV.get(9L), ItemList.Field_Generator_LuV.get(9L),
                            GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Europium, 36L) },
                    new FluidStack[] { new FluidStack(solderIndalloy, 1440),
                            WerkstoffLoader.Neon.getFluidOrGas(20000), },
                    ItemRegistry.voidminer[0].copy(),
                    108000,
                    BW_Util.getMachineVoltageFromTier(6));
        }

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Machine_LuV_CircuitAssembler.get(1L),
                24000,
                new ItemStack[] { ItemList.Machine_LuV_CircuitAssembler.get(1L), ItemList.Robot_Arm_LuV.get(4L),
                        ItemList.Electric_Motor_LuV.get(4L), ItemList.Field_Generator_LuV.get(1L),
                        ItemList.Emitter_LuV.get(1L), ItemList.Sensor_LuV.get(1L), Materials.Chrome.getPlates(8) },
                new FluidStack[] { new FluidStack(solderIndalloy, 1440) },
                ItemRegistry.cal.copy(),
                24000,
                BW_Util.getMachineVoltageFromTier(6));
    }
}
