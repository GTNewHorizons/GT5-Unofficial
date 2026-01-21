package kekztech.common.recipeLoaders;

import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.STACKS;
import static gtPlusPlus.core.material.MaterialsElements.STANDALONE.DRAGON_METAL;
import static gtPlusPlus.core.material.MaterialsElements.STANDALONE.HYPOGEN;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsElements;
import kekztech.common.Blocks;
import tectech.recipe.TTRecipeAdder;

public class ResearchableAssemblyLine implements Runnable {

    @Override
    public void run() {
        // Ultimate Capacitor (UHV)
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 4),
            12000,
            16,
            300000,
            3,
            new Object[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 4),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.CosmicNeutronium, 24),
                GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.Neutronium, 64L),
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 4 }, ItemList.ZPM2.get(8L),
                ItemList.Field_Generator_UHV.get(4), ItemList.Circuit_Wafer_UHPIC.get(64),
                ItemList.Circuit_Wafer_UHPIC.get(64), ItemList.Circuit_Wafer_SoC2.get(32),
                ItemList.Circuit_Parts_DiodeASMD.get(64),
                GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUHV, 64) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(32 * INGOTS),
                Materials.Naquadria.getMolten(1 * STACKS), GTModHandler.getIC2Coolant(32_000) },
            new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 5),
            4000,
            1600000);

        // Extremely Ultimate Capacitor (UEV)
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 5),
            1200000,
            128,
            (int) TierEU.RECIPE_UEV,
            16,
            new Object[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 4),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.Infinity, 24),
                GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.InfinityCatalyst, 64L),
                new Object[] { OrePrefixes.circuit.get(Materials.UIV), 4 }, ItemList.ZPM3.get(8L),
                ItemList.Field_Generator_UEV.get(4), ItemList.Circuit_Wafer_PPIC.get(64),
                ItemList.Circuit_Wafer_PPIC.get(64), ItemList.Circuit_Wafer_SoC2.get(64),
                ItemList.Circuit_Parts_DiodeXSMD.get(64),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUEV, 64) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(1 * STACKS),
                Materials.Quantium.getMolten(2 * STACKS), Materials.Naquadria.getMolten(2 * STACKS),
                Materials.SuperCoolant.getFluid(64_000) },
            new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 8),
            250 * 20,
            (int) TierEU.RECIPE_UEV);

        // Insanely Ultimate Capacitor (UIV)
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 8),
            24_000_000,
            1_280,
            (int) TierEU.RECIPE_UIV,
            32,
            new Object[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.TranscendentMetal, 4),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.TranscendentMetal, 24), HYPOGEN.getPlateDouble(64),
                new Object[] { OrePrefixes.circuit.get(Materials.UMV), 4 }, ItemList.ZPM4.get(8L),
                ItemList.Field_Generator_UIV.get(4), ItemList.Circuit_Wafer_QPIC.get(64),
                ItemList.Circuit_Wafer_QPIC.get(64), GTModHandler.getModItem(NewHorizonsCoreMod.ID, "RawPicoWafer", 64),
                ItemList.Circuit_Parts_DiodeXSMD.get(64), ItemList.Circuit_Parts_InductorXSMD.get(32),
                GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.SuperconductorUIV, 64) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(2 * STACKS),
                MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFluidStack(2 * STACKS),
                Materials.Quantium.getMolten(2 * STACKS), Materials.SuperCoolant.getFluid(128_000) },
            new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 9),
            300 * 20,
            (int) TierEU.RECIPE_UIV);

        // Mega Ultimate Capacitor (UMV)
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 9),
            480_000_000,
            12_288,
            (int) TierEU.RECIPE_UMV,
            64,
            new Object[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SpaceTime, 4),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.SpaceTime, 24), DRAGON_METAL.getPlateDouble(64),
                new Object[] { OrePrefixes.circuit.get(Materials.UXV), 4 }, ItemList.ZPM5.get(8L),
                ItemList.Field_Generator_UMV.get(4), ItemList.Circuit_Wafer_QPIC.get(64),
                ItemList.Circuit_Wafer_QPIC.get(64), GTModHandler.getModItem(NewHorizonsCoreMod.ID, "PicoWafer", 64),
                ItemList.Circuit_Parts_DiodeXSMD.get(64), ItemList.Circuit_Parts_InductorXSMD.get(64),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUMV, 64) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(4 * STACKS),
                new FluidStack(FluidRegistry.getFluid("molten.astraltitanium"), 4 * STACKS),
                MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFluidStack(4 * STACKS),
                Materials.SuperCoolant.getFluid(256_000) },
            new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 10),
            360 * 20,
            (int) TierEU.RECIPE_UMV);

    }
}
