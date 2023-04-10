package common.recipeLoaders;

import com.github.technus.tectech.recipe.TT_recipeAdder;
import common.Blocks;
import cpw.mods.fml.common.Loader;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class ResearchableAssemblyLine implements Runnable{
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
            8000000,
            16,
            new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 4),
                GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Infinity, 24),
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.InfinityCatalyst, 32L),
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.InfinityCatalyst, 32L),
                new Object[] { OrePrefixes.circuit.get(Materials.Optical), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.Optical), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.Optical), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.Optical), 1 }, ItemList.ZPM3.get(8L),
                ItemList.Field_Generator_UEV.get(4), ItemList.Circuit_Wafer_PPIC.get(64),
                ItemList.Circuit_Wafer_PPIC.get(64), ItemList.Circuit_Wafer_SoC2.get(64),
                ItemList.Circuit_Parts_DiodeXSMD.get(64),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUEV, 64) },
            new FluidStack[] { new FluidStack(solderUEV, 9216), Materials.Quantium.getMolten(18432),
                Materials.Naquadria.getMolten(18432), Materials.SuperCoolant.getFluid(64000) },
            new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 8),
            250 * 20,
            (int) TierEU.RECIPE_UEV);

        if (Loader.isModLoaded("miscutils")) {
            // Insanely Ultimate Capacitor (UIV)
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 8),
                24_000_000,
                1_280,
                32_000_000,
                32,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.TranscendentMetal, 4),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.TranscendentMetal, 24),
                    GT_ModHandler.getModItem("miscutils", "itemPlateDoubleHypogen", 32),
                    GT_ModHandler.getModItem("miscutils", "itemPlateDoubleHypogen", 32),
                    new Object[] { OrePrefixes.circuit.get(Materials.Piko), 1 },
                    new Object[] { OrePrefixes.circuit.get(Materials.Piko), 1 },
                    new Object[] { OrePrefixes.circuit.get(Materials.Piko), 1 },
                    new Object[] { OrePrefixes.circuit.get(Materials.Piko), 1 }, ItemList.ZPM4.get(8L),
                    ItemList.Field_Generator_UIV.get(4), ItemList.Circuit_Wafer_QPIC.get(64),
                    ItemList.Circuit_Wafer_QPIC.get(64),
                    GT_ModHandler.getModItem("dreamcraft", "item.RawPicoWafer", 64),
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
                128_000_000,
                64,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.SpaceTime, 4),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.SpaceTime, 24),
                    GT_ModHandler.getModItem("miscutils", "itemPlateDoubleDragonblood", 32),
                    GT_ModHandler.getModItem("miscutils", "itemPlateDoubleDragonblood", 32),
                    new Object[] { OrePrefixes.circuit.get(Materials.Quantum), 1 },
                    new Object[] { OrePrefixes.circuit.get(Materials.Quantum), 1 },
                    new Object[] { OrePrefixes.circuit.get(Materials.Quantum), 1 },
                    new Object[] { OrePrefixes.circuit.get(Materials.Quantum), 1 }, ItemList.ZPM5.get(8L),
                    ItemList.Field_Generator_UMV.get(4), ItemList.Circuit_Wafer_QPIC.get(64),
                    ItemList.Circuit_Wafer_QPIC.get(64),
                    GT_ModHandler.getModItem("dreamcraft", "item.PicoWafer", 64),
                    ItemList.Circuit_Parts_DiodeXSMD.get(64), ItemList.Circuit_Parts_InductorXSMD.get(64),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUMV, 64) },
                new FluidStack[] { new FluidStack(solderUEV, 36_864),
                    new FluidStack(FluidRegistry.getFluid("molten.astraltitanium"), 36_864),
                    new FluidStack(FluidRegistry.getFluid("molten.celestialtungsten"), 36_864),
                    Materials.SuperCoolant.getFluid(256_000) },
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 10),
                350 * 20,
                (int) TierEU.RECIPE_UMV);
        }
    }
}
