package kekztech.common.recipeLoaders;

import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.STACKS;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.Circuits;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2Backings;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
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
            new Object[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials2Materials.CosmicNeutronium, 4),
                MaterialLibAPI.getStack(Materials2Materials.CosmicNeutronium, Materials2Shapes.screw, (int) (24)),
                MaterialLibAPI.getStack(Materials2Materials.Neutronium, Materials2Shapes.plateDouble, (int) (64)),
                new Object[] { Circuits.UEV.getIngredient(), 4 }, ItemList.ZPM2.get(8L),
                ItemList.Field_Generator_UHV.get(4), ItemList.Circuit_Wafer_UHPIC.get(64),
                ItemList.Circuit_Wafer_UHPIC.get(64), ItemList.Circuit_Wafer_SoC2.get(32),
                ItemList.Circuit_Parts_DiodeASMD.get(64),
                GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials2Backings.SuperconductorUHV, 64) },
            new FluidStack[] { MaterialUtils.legacyGtppFluid(Materials2Materials.MutatedLivingSolder, 32 * INGOTS),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Naquadria,
                    Materials2FluidShapes.fluidMolten,
                    (int) (1 * STACKS)),
                GTModHandler.getIC2Coolant(32_000) },
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
            new Object[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials2Materials.Infinity, 4),
                MaterialLibAPI.getStack(Materials2Materials.Infinity, Materials2Shapes.screw, (int) (24)),
                MaterialLibAPI.getStack(Materials2Materials.InfinityCatalyst, Materials2Shapes.plateDouble, (int) (64)),
                new Object[] { Circuits.UIV.getIngredient(), 4 }, ItemList.ZPM3.get(8L),
                ItemList.Field_Generator_UEV.get(4), ItemList.Circuit_Wafer_PPIC.get(64),
                ItemList.Circuit_Wafer_PPIC.get(64), ItemList.Circuit_Wafer_SoC2.get(64),
                ItemList.Circuit_Parts_DiodeXSMD.get(64),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials2Backings.SuperconductorUEV, 64) },
            new FluidStack[] { MaterialUtils.legacyGtppFluid(Materials2Materials.MutatedLivingSolder, 1 * STACKS),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Quantium, Materials2FluidShapes.fluidMolten, (int) (2 * STACKS)),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Naquadria,
                    Materials2FluidShapes.fluidMolten,
                    (int) (2 * STACKS)),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SuperCoolant,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (64_000)) },
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
            new Object[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials2Materials.TranscendentMetal, 4),
                MaterialLibAPI.getStack(Materials2Materials.TranscendentMetal, Materials2Shapes.screw, (int) (24)),
                MaterialLibAPI.getStack(Materials2Materials.Hypogen, Materials2Shapes.plateDouble, 64),
                new Object[] { Circuits.UMV.getIngredient(), 4 }, ItemList.ZPM4.get(8L),
                ItemList.Field_Generator_UIV.get(4), ItemList.Circuit_Wafer_QPIC.get(64),
                ItemList.Circuit_Wafer_QPIC.get(64), ItemList.Circuit_Parts_Crystal_Chip_Wetware.get(64),
                ItemList.Circuit_Parts_DiodeXSMD.get(64), ItemList.Circuit_Parts_InductorXSMD.get(32),
                GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials2Backings.SuperconductorUIV, 64) },
            new FluidStack[] { MaterialUtils.legacyGtppFluid(Materials2Materials.MutatedLivingSolder, 2 * STACKS),
                MaterialUtils.legacyGtppFluid(Materials2Materials.CelestialTungsten, 2 * STACKS),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Quantium, Materials2FluidShapes.fluidMolten, (int) (2 * STACKS)),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SuperCoolant,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (128_000)) },
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
            new Object[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials2Materials.SpaceTime, 4),
                MaterialLibAPI.getStack(Materials2Materials.SpaceTime, Materials2Shapes.screw, (int) (24)),
                MaterialLibAPI.getStack(Materials2Materials.Dragonblood, Materials2Shapes.plateDouble, 64),
                new Object[] { Circuits.UXV.getIngredient(), 4 }, ItemList.ZPM5.get(8L),
                ItemList.Field_Generator_UMV.get(4), ItemList.Circuit_Wafer_FPIC.get(64),
                ItemList.Circuit_Wafer_FPIC.get(64), ItemList.Circuit_Parts_Chip_Bioware.get(64),
                ItemList.Circuit_Parts_DiodeXSMD.get(64), ItemList.Circuit_Parts_InductorXSMD.get(64),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials2Backings.SuperconductorUMV, 64) },
            new FluidStack[] { MaterialUtils.legacyGtppFluid(Materials2Materials.MutatedLivingSolder, 4 * STACKS),
                new FluidStack(FluidRegistry.getFluid("molten.astraltitanium"), 4 * STACKS),
                MaterialUtils.legacyGtppFluid(Materials2Materials.CelestialTungsten, 4 * STACKS),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SuperCoolant,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (256_000)) },
            new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 10),
            360 * 20,
            (int) TierEU.RECIPE_UMV);

    }
}
