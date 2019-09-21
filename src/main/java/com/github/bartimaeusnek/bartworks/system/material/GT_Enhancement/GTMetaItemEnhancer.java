package com.github.bartimaeusnek.bartworks.system.material.GT_Enhancement;

import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import cpw.mods.fml.common.Loader;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.stream.Collectors;

public class GTMetaItemEnhancer {
   static List<Materials> NoMetaValue;


    static{
        if (Loader.isModLoaded("Forestry")) {
            NoMetaValue = Materials.getMaterialsMap().values().stream().filter(m -> m.mMetaItemSubID == -1).collect(Collectors.toList());
            Item moltenCapsuls = new BWGTMetaItems(WerkstoffLoader.capsuleMolten, null);
            Item capsuls = new BWGTMetaItems(OrePrefixes.capsule, NoMetaValue);

            Materials[] values = Materials.values();
            for (int i = 0, valuesLength = values.length; i < valuesLength; i++) {
                Materials m = values[i];
                if (m.mStandardMoltenFluid != null && GT_OreDictUnificator.get(WerkstoffLoader.cellMolten, m, 1) != null) {
                    final FluidContainerRegistry.FluidContainerData emptyData = new FluidContainerRegistry.FluidContainerData(m.getMolten(144), new ItemStack(moltenCapsuls, 1, i), GT_ModHandler.getModItem("Forestry", "refractoryEmpty", 1));
                    FluidContainerRegistry.registerFluidContainer(emptyData);
                    GT_Utility.addFluidContainerData(emptyData);
                    GT_Values.RA.addFluidCannerRecipe(GT_ModHandler.getModItem("Forestry", "refractoryEmpty", 1), new ItemStack(moltenCapsuls, 1, i), m.getMolten(144), GT_Values.NF);
                    GT_Values.RA.addFluidCannerRecipe(new ItemStack(moltenCapsuls, 1, i), GT_Values.NI, GT_Values.NF, m.getMolten(144));
                }
                if (m.hasCorrespondingFluid() || m.hasCorrespondingGas()) {
                    Fluid f = m.hasCorrespondingFluid() ? m.getFluid(1).getFluid() : m.getGas(1).getFluid();
                    final FluidContainerRegistry.FluidContainerData emptyData = new FluidContainerRegistry.FluidContainerData(new FluidStack(f, 1000), new ItemStack(capsuls, 1, i), GT_ModHandler.getModItem("Forestry", "waxCapsule", 1));
                    FluidContainerRegistry.registerFluidContainer(emptyData);
                    GT_Utility.addFluidContainerData(emptyData);
                    GT_Values.RA.addFluidCannerRecipe(GT_ModHandler.getModItem("Forestry", "waxCapsule", 1), new ItemStack(capsuls, 1, i), new FluidStack(f, 1000), GT_Values.NF);
                    GT_Values.RA.addFluidCannerRecipe(new ItemStack(capsuls, 1, i), GT_Values.NI, GT_Values.NF, new FluidStack(f, 1000));
                }
            }
            for (int i = 0, valuesLength = NoMetaValue.size(); i < valuesLength; i++) {
                Materials m = NoMetaValue.get(i);
                if (m.getFluid(1) != null || m.getGas(1) != null) {
                    Fluid f = m.hasCorrespondingFluid() ? m.getFluid(1).getFluid() : m.getGas(1).getFluid();
                    final FluidContainerRegistry.FluidContainerData emptyData = new FluidContainerRegistry.FluidContainerData(new FluidStack(f, 1000), new ItemStack(capsuls, 1, i + 1001), GT_ModHandler.getModItem("Forestry", "waxCapsule", 1));
                    FluidContainerRegistry.registerFluidContainer(emptyData);
                    GT_Utility.addFluidContainerData(emptyData);
                    GT_Values.RA.addFluidCannerRecipe(GT_ModHandler.getModItem("Forestry", "waxCapsule", 1), new ItemStack(capsuls, 1, i + 1001), new FluidStack(f, 1000), GT_Values.NF);
                    GT_Values.RA.addFluidCannerRecipe(new ItemStack(capsuls, 1, i + 1001), GT_Values.NI, GT_Values.NF, new FluidStack(f, 1000));
                }
            }
        }
    }

}
