package kekztech.common.recipeLoaders;

import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.STACKS;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.Circuits;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.enums.materials.TEBlockShapes;
import gregtech.api.util.GTModHandler;
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
            new Object[] { MaterialLibAPI.getStack(Materials.CosmicNeutronium, TEBlockShapes.frameGt, 4),
                MaterialLibAPI.getStack(Materials.CosmicNeutronium, Shapes.screw, 24),
                MaterialLibAPI.getStack(Materials.Neutronium, Shapes.plateDouble, 64),
                new Object[] { Circuits.UEV.getIngredient(), 4 }, ItemList.ZPM2.get(8L),
                ItemList.Field_Generator_UHV.get(4), ItemList.Circuit_Wafer_UHPIC.get(64),
                ItemList.Circuit_Wafer_UHPIC.get(64), ItemList.Circuit_Wafer_SoC2.get(32),
                ItemList.Circuit_Parts_DiodeASMD.get(64),
                MaterialLibAPI.getStack(Materials.SuperconductorUHV, TEBlockShapes.wireGt02, 64) },
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials.MutatedLivingSolder, FluidShapes.fluidLiquid, 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Naquadria, FluidShapes.fluidMolten, STACKS),
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
            new Object[] { MaterialLibAPI.getStack(Materials.Infinity, TEBlockShapes.frameGt, 4),
                MaterialLibAPI.getStack(Materials.Infinity, Shapes.screw, 24),
                MaterialLibAPI.getStack(Materials.InfinityCatalyst, Shapes.plateDouble, 64),
                new Object[] { Circuits.UIV.getIngredient(), 4 }, ItemList.ZPM3.get(8L),
                ItemList.Field_Generator_UEV.get(4), ItemList.Circuit_Wafer_PPIC.get(64),
                ItemList.Circuit_Wafer_PPIC.get(64), ItemList.Circuit_Wafer_SoC2.get(64),
                ItemList.Circuit_Parts_DiodeXSMD.get(64),
                MaterialLibAPI.getStack(Materials.SuperconductorUEV, TEBlockShapes.wireGt04, 64) },
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials.MutatedLivingSolder, FluidShapes.fluidLiquid, 1 * STACKS),
                MaterialLibAPI.getFluidStack(Materials.Quantium, FluidShapes.fluidMolten, 2 * STACKS),
                MaterialLibAPI.getFluidStack(Materials.Naquadria, FluidShapes.fluidMolten, 2 * STACKS),
                MaterialLibAPI.getFluidStack(Materials.SuperCoolant, FluidShapes.fluidLiquid, 64_000) },
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
            new Object[] { MaterialLibAPI.getStack(Materials.TranscendentMetal, TEBlockShapes.frameGt, 4),
                MaterialLibAPI.getStack(Materials.TranscendentMetal, Shapes.screw, 24),
                MaterialLibAPI.getStack(Materials.Hypogen, Shapes.plateDouble, 64),
                new Object[] { Circuits.UMV.getIngredient(), 4 }, ItemList.ZPM4.get(8L),
                ItemList.Field_Generator_UIV.get(4), ItemList.Circuit_Wafer_QPIC.get(64),
                ItemList.Circuit_Wafer_QPIC.get(64), ItemList.Circuit_Parts_Crystal_Chip_Wetware.get(64),
                ItemList.Circuit_Parts_DiodeXSMD.get(64), ItemList.Circuit_Parts_InductorXSMD.get(32),
                MaterialLibAPI.getStack(Materials.SuperconductorUIV, TEBlockShapes.wireGt08, 64) },
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials.MutatedLivingSolder, FluidShapes.fluidLiquid, 2 * STACKS),
                MaterialLibAPI.getFluidStack(Materials.CelestialTungsten, FluidShapes.fluidMolten, 2 * STACKS),
                MaterialLibAPI.getFluidStack(Materials.Quantium, FluidShapes.fluidMolten, 2 * STACKS),
                MaterialLibAPI.getFluidStack(Materials.SuperCoolant, FluidShapes.fluidLiquid, 128_000) },
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
            new Object[] { MaterialLibAPI.getStack(Materials.SpaceTime, TEBlockShapes.frameGt, 4),
                MaterialLibAPI.getStack(Materials.SpaceTime, Shapes.screw, 24),
                MaterialLibAPI.getStack(Materials.Dragonblood, Shapes.plateDouble, 64),
                new Object[] { Circuits.UXV.getIngredient(), 4 }, ItemList.ZPM5.get(8L),
                ItemList.Field_Generator_UMV.get(4), ItemList.Circuit_Wafer_FPIC.get(64),
                ItemList.Circuit_Wafer_FPIC.get(64), ItemList.Circuit_Parts_Chip_Bioware.get(64),
                ItemList.Circuit_Parts_DiodeXSMD.get(64), ItemList.Circuit_Parts_InductorXSMD.get(64),
                MaterialLibAPI.getStack(Materials.SuperconductorUMV, TEBlockShapes.wireGt16, 64) },
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials.MutatedLivingSolder, FluidShapes.fluidLiquid, 4 * STACKS),
                new FluidStack(FluidRegistry.getFluid("molten.astraltitanium"), 4 * STACKS),
                MaterialLibAPI.getFluidStack(Materials.CelestialTungsten, FluidShapes.fluidMolten, 4 * STACKS),
                MaterialLibAPI.getFluidStack(Materials.SuperCoolant, FluidShapes.fluidLiquid, 256_000) },
            new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 10),
            360 * 20,
            (int) TierEU.RECIPE_UMV);

    }
}
