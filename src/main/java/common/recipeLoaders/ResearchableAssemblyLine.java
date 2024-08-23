package common.recipeLoaders;

import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gtPlusPlus.core.material.ELEMENT.STANDALONE.DRAGON_METAL;
import static gtPlusPlus.core.material.ELEMENT.STANDALONE.HYPOGEN;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.github.technus.tectech.recipe.TT_recipeAdder;
import common.Blocks;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;

public class ResearchableAssemblyLine implements Runnable {

    @Override
    public void run() {
        final Fluid solderUEV = FluidRegistry.getFluid("molten.mutatedlivingsolder") != null
            ? FluidRegistry.getFluid("molten.mutatedlivingsolder")
            : FluidRegistry.getFluid("molten.solderingalloy");

        // Extremely Ultimate Capacitor (UEV)
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 5),
            1200000,
            128,
            (int) TierEU.RECIPE_UEV,
            16,
            new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 4),
                GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Infinity, 24),
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.InfinityCatalyst, 32L),
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.InfinityCatalyst, 32L),
                new Object[] { OrePrefixes.circuit.get(Materials.UIV), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.UIV), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.UIV), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.UIV), 1 }, ItemList.ZPM3.get(8L),
                ItemList.Field_Generator_UEV.get(4), ItemList.Circuit_Wafer_PPIC.get(64),
                ItemList.Circuit_Wafer_PPIC.get(64), ItemList.Circuit_Wafer_SoC2.get(64),
                ItemList.Circuit_Parts_DiodeXSMD.get(64),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUEV, 64) },
            new FluidStack[] { new FluidStack(solderUEV, 9216), Materials.Quantium.getMolten(18432),
                Materials.Naquadria.getMolten(18432), Materials.SuperCoolant.getFluid(64000) },
            new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 8),
            250 * 20,
            (int) TierEU.RECIPE_UEV);

        // Insanely Ultimate Capacitor (UIV)
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 8),
            24_000_000,
            1_280,
            (int) TierEU.RECIPE_UIV,
            32,
            new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.TranscendentMetal, 4),
                GT_OreDictUnificator.get(OrePrefixes.screw, MaterialsUEVplus.TranscendentMetal, 24),
                HYPOGEN.getPlateDouble(32), HYPOGEN.getPlateDouble(32),
                new Object[] { OrePrefixes.circuit.get(Materials.UMV), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.UMV), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.UMV), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.UMV), 1 }, ItemList.ZPM4.get(8L),
                ItemList.Field_Generator_UIV.get(4), ItemList.Circuit_Wafer_QPIC.get(64),
                ItemList.Circuit_Wafer_QPIC.get(64),
                GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.RawPicoWafer", 64),
                ItemList.Circuit_Parts_DiodeXSMD.get(64), ItemList.Circuit_Parts_InductorXSMD.get(32),
                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.SuperconductorUIV, 64) },
            new FluidStack[] { new FluidStack(solderUEV, 18_432),
                new FluidStack(FluidRegistry.getFluid("molten.celestialtungsten"), 18432),
                Materials.Quantium.getMolten(18_432), Materials.SuperCoolant.getFluid(128_000) },
            new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 9),
            300 * 20,
            (int) TierEU.RECIPE_UIV);

        // Mega Ultimate Capacitor (UMV)
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 9),
            480_000_000,
            12_288,
            (int) TierEU.RECIPE_UMV,
            64,
            new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.SpaceTime, 4),
                GT_OreDictUnificator.get(OrePrefixes.screw, MaterialsUEVplus.SpaceTime, 24),
                DRAGON_METAL.getPlateDouble(32), DRAGON_METAL.getPlateDouble(32),
                new Object[] { OrePrefixes.circuit.get(Materials.UXV), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.UXV), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.UXV), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.UXV), 1 }, ItemList.ZPM5.get(8L),
                ItemList.Field_Generator_UMV.get(4), ItemList.Circuit_Wafer_QPIC.get(64),
                ItemList.Circuit_Wafer_QPIC.get(64),
                GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.PicoWafer", 64),
                ItemList.Circuit_Parts_DiodeXSMD.get(64), ItemList.Circuit_Parts_InductorXSMD.get(64),
                GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUMV, 64) },
            new FluidStack[] { new FluidStack(solderUEV, 36_864),
                new FluidStack(FluidRegistry.getFluid("molten.astraltitanium"), 36_864),
                new FluidStack(FluidRegistry.getFluid("molten.celestialtungsten"), 36_864),
                Materials.SuperCoolant.getFluid(256_000) },
            new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 10),
            350 * 20,
            (int) TierEU.RECIPE_UMV);

        // Ultimate Capacitor (UHV)
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 4),
            12000,
            16,
            300000,
            3,
            new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 4),
                GT_OreDictUnificator.get(OrePrefixes.screw, Materials.CosmicNeutronium, 24),
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Neutronium, 32L),
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Neutronium, 32L),
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 1 }, ItemList.ZPM2.get(8L),
                ItemList.Field_Generator_UHV.get(4), ItemList.Circuit_Wafer_UHPIC.get(64),
                ItemList.Circuit_Wafer_UHPIC.get(64), ItemList.Circuit_Wafer_SoC2.get(32),
                ItemList.Circuit_Parts_DiodeASMD.get(64),
                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUHV, 64) },
            new FluidStack[] { new FluidStack(solderUEV, 4608), Materials.Naquadria.getMolten(9216),
                new FluidStack(FluidRegistry.getFluid("ic2coolant"), 32000) },
            new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 5),
            4000,
            1600000);
    }
}
