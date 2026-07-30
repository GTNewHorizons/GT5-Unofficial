package gregtech.loaders.postload.chains;

import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.recipe.RecipeMaps.nanoForgeRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.STACKS;
import static gregtech.api.util.GTRecipeConstants.AssemblyLine;
import static gregtech.api.util.GTRecipeConstants.NANO_FORGE_TIER;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GTRecipeConstants.SCANNING;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.Circuits;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.BlockShapes;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.api.util.recipe.Scanning;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class NaniteChain {

    public static void run() {

        ItemStack aUVTierLens = getModItem(NewHorizonsCoreMod.ID, "MysteriousCrystalLens", 0);
        ItemStack aUHVTierLens = getModItem(NewHorizonsCoreMod.ID, "ChromaticLens", 0);
        ItemStack aUEVTierLens = getModItem(NewHorizonsCoreMod.ID, "RadoxPolymerLens", 0);
        ItemStack aUIVTierLens = ItemList.EnergisedTesseract.get(0);
        ItemStack aUMVTierLens = GTOreDictUnificator.get(OrePrefixes.lens, Materials.Dilithium, 0, false);

        // Nano Forge
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Carbon, 1))
            .metadata(SCANNING, new Scanning(2 * MINUTES + 30 * SECONDS, TierEU.RECIPE_ZPM))
            .itemInputs(
                ItemList.Hull_UV.get(16),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Carbon, 16),
                ItemList.Field_Generator_ZPM.get(16),
                ItemList.Conveyor_Module_UV.get(16),
                ItemList.Electric_Motor_UV.get(32),
                new Object[] { Circuits.LuV.getIngredient(), 16 },
                GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.Naquadah, 32),
                MaterialLibAPI.getStack(Materials.NaquadahAlloy, Shapes.plateSuperdense, 4))
            .fluidInputs(
                MaterialUtils.anyFluid(Materials.Indalloy140, 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.HSSS, FluidShapes.fluidMolten, (int) (32 * INGOTS)),
                MaterialLibAPI.getFluidStack(Materials.Osmiridium, FluidShapes.fluidMolten, (int) (16 * INGOTS)))
            .itemOutputs(ItemList.NanoForge.get(1))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * MINUTES)
            .addTo(AssemblyLine);

        // Carbon Nanite Recipe Before Nano Forge
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Circuit_Crystalmainframe.get(1))
            .metadata(SCANNING, new Scanning(2 * MINUTES, TierEU.RECIPE_ZPM))
            .itemInputs(
                new Object[] { Circuits.UV.getIngredient(), 16 },
                ItemList.Robot_Arm_UV.get(16),
                ItemList.Circuit_Chip_Stemcell.get(32),
                MaterialLibAPI.getStack(Materials.NaquadahAlloy, Shapes.ring, 32),
                MaterialLibAPI.getStack(Materials.NaquadahAlloy, Shapes.stick, 16),
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, 64))
            .fluidInputs(
                MaterialUtils.fluid(Materials.UUMatter, 10_000),
                MaterialUtils.anyFluid(Materials.Indalloy140, 32 * INGOTS))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Carbon, 2))
            .eut(TierEU.RECIPE_UV)
            .duration(50 * SECONDS)
            .addTo(AssemblyLine);

        /*
         * General Rules for making nanite recipes: 1. Never make a nanite that takes a long time to make and only gives
         * 1, just to be consumed. 2. Nanites meant to be consumed should either have a short duration or a big output.
         * 3. Nanites which aren't consumed should have a long duration and output less than 16. 4. Nanites should
         * always take UUM as a fluid and a lot of power to make.
         */

        // Carbon Nanites - Used to make more Nano Forge Controllers
        GTValues.RA.stdBuilder()
            .itemInputs(
                aUVTierLens,
                MaterialLibAPI.getStack(Materials.Carbon, BlockShapes.blockCasingAdvanced, 8),
                ItemList.Circuit_Chip_SoC.get(64))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Carbon, 64))
            .fluidInputs(MaterialUtils.fluid(Materials.UUMatter, 200_000))
            .metadata(NANO_FORGE_TIER, 1)
            .duration(8 * MINUTES + 20 * SECONDS)
            .eut(10_000_000)
            .addTo(nanoForgeRecipes);

        // Silver Nanites - Used in Tier 2 PCB Factory to improve board production
        GTValues.RA.stdBuilder()
            .itemInputs(
                aUEVTierLens,
                MaterialLibAPI.getStack(Materials.Silver, BlockShapes.block, 8),
                ItemList.Circuit_Chip_SoC.get(16))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Silver, 1))
            .fluidInputs(MaterialUtils.fluid(Materials.UUMatter, 200_000))
            .metadata(NANO_FORGE_TIER, 2)
            .duration(12 * MINUTES + 30 * SECONDS)
            .eut(10_000_000)
            .addTo(nanoForgeRecipes);

        // Neutronium Nanites - Used to upgrade the Nano Forge to Tier 2
        GTValues.RA.stdBuilder()
            .itemInputs(
                aUHVTierLens,
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Neutronium, 8),
                ItemList.Circuit_Chip_SoC2.get(64),
                ItemList.Circuit_Chip_SoC2.get(32))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Neutronium, 1))
            .fluidInputs(MaterialUtils.fluid(Materials.UUMatter, 200_000))
            .metadata(NANO_FORGE_TIER, 1)
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(100_000_000)
            .addTo(nanoForgeRecipes);

        // Glowstone Nanites - Used in the optical circuit line
        GTValues.RA.stdBuilder()
            .itemInputs(
                aUEVTierLens,
                GregtechItemList.DoubleCompressedGlowstone.get(8),
                ItemList.Circuit_Chip_SoC2.get(64))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Glowstone, 64))
            .fluidInputs(MaterialUtils.fluid(Materials.UUMatter, 50_000))
            .metadata(NANO_FORGE_TIER, 2)
            .duration(3 * MINUTES + 20 * SECONDS)
            .eut(50_000_000)
            .addTo(nanoForgeRecipes);

        // Gold Nanites - Used in Tier 3 PCB Factory to improve board production
        GTValues.RA.stdBuilder()
            .itemInputs(
                aUMVTierLens,
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Gold, 8),
                ItemList.Circuit_Chip_SoC.get(16))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Gold, 1))
            .fluidInputs(MaterialUtils.fluid(Materials.UUMatter, 300_000))
            .metadata(NANO_FORGE_TIER, 3)
            .duration(16 * MINUTES + 40 * SECONDS)
            .eut(100_000_000)
            .addTo(nanoForgeRecipes);

        // Transcendent Metal Nanites - Used to upgrade the Nano Forge to Tier 3
        GTValues.RA.stdBuilder()
            .itemInputs(
                aUIVTierLens,
                GTOreDictUnificator.get(OrePrefixes.block, Materials.TranscendentMetal, 8),
                ItemList.Circuit_Chip_SoC2.get(64),
                ItemList.Circuit_Chip_SoC2.get(64),
                ItemList.Circuit_Chip_SoC2.get(64))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.nanite, Materials.TranscendentMetal, 1))
            .fluidInputs(MaterialUtils.fluid(Materials.UUMatter, 2_000_000))
            .metadata(NANO_FORGE_TIER, 2)
            .duration(12 * MINUTES + 30 * SECONDS)
            .eut(1_000_000_000)
            .addTo(nanoForgeRecipes);

        // Six-Phased Copper Nanites - Used in Phononic Crystal production for the godforge
        GTValues.RA.stdBuilder()
            .itemInputs(
                aUIVTierLens,
                MaterialLibAPI.getStack(Materials.SixPhasedCopper, BlockShapes.block, 16),
                ItemList.Circuit_Chip_SoC2.get(64),
                ItemList.Circuit_Chip_SoC2.get(64),
                ItemList.Circuit_Chip_SoC2.get(64))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.nanite, Materials.SixPhasedCopper, 8))
            .fluidInputs(
                MaterialUtils.fluid(Materials.UUMatter, 500_000),
                MaterialUtils.fluid(Materials.DimensionallyTranscendentResidue, 50_000),
                MaterialLibAPI.getFluidStack(Materials.Creon, FluidShapes.fluidMolten, (int) (8 * STACKS)))
            .metadata(NANO_FORGE_TIER, 3)
            .duration(100 * SECONDS)
            .eut(2_000_000_000)
            .addTo(nanoForgeRecipes);

        // White Dwarf Matter Nanites. Used to make Magnetohydrodynamically constrained star matter.
        GTValues.RA.stdBuilder()
            .itemInputs(
                aUMVTierLens,
                GTUtility.copyAmount(0, MaterialLibAPI.getStack(Materials.MagnetoResonatic, Shapes.lens, 1)), // Magneto
                // lens
                GregtechItemList.Laser_Lens_Special.get(0), // Quantum Anomaly, couldn't find any better
                                                            // naming
                GTOreDictUnificator.get(OrePrefixes.block, Materials.WhiteDwarfMatter, 8),
                ItemList.Circuit_Chip_APIC.get(64),
                ItemList.Circuit_Parts_Crystal_Chip_Wetware.get(64))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.nanite, Materials.WhiteDwarfMatter, 4))
            .fluidInputs(
                MaterialUtils.fluid(Materials.UUMatter, 500_000),
                MaterialLibAPI.getFluidStack(Materials.RawStarMatter, FluidShapes.fluidLiquid, 50_000),
                MaterialUtils.molten(Materials.spatialFluid, 5 * INGOTS))
            .metadata(NANO_FORGE_TIER, 3)
            .duration(12 * MINUTES + 30 * SECONDS)
            .eut(2_000_000_000)
            .addTo(nanoForgeRecipes);

        // Black Dwarf Matter Nanites. Used to make Magnetohydrodynamically constrained star matter.
        GTValues.RA.stdBuilder()
            .itemInputs(
                aUMVTierLens,
                GTUtility.copyAmount(0, MaterialLibAPI.getStack(Materials.MagnetoResonatic, Shapes.lens, 1)), // Magneto
                // lens
                GregtechItemList.Laser_Lens_Special.get(0), // Quantum Anomaly, couldn't find any better
                                                            // naming
                GTOreDictUnificator.get(OrePrefixes.block, Materials.BlackDwarfMatter, 8),
                ItemList.Circuit_Chip_APIC.get(64),
                ItemList.Circuit_Parts_Crystal_Chip_Wetware.get(64))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.nanite, Materials.BlackDwarfMatter, 4))
            .fluidInputs(
                MaterialUtils.fluid(Materials.UUMatter, 500_000),
                MaterialLibAPI.getFluidStack(Materials.RawStarMatter, FluidShapes.fluidLiquid, 50_000),
                MaterialUtils.molten(Materials.temporalFluid, 5 * INGOTS))
            .metadata(NANO_FORGE_TIER, 3)
            .duration(12 * MINUTES + 30 * SECONDS)
            .eut(2_000_000_000)
            .addTo(nanoForgeRecipes);

        // Universium nanites.
        GTValues.RA.stdBuilder()
            .itemInputs(
                aUMVTierLens,
                GTUtility.copyAmount(0, MaterialLibAPI.getStack(Materials.MagnetoResonatic, Shapes.lens, 1)), // Magneto
                // lens
                // Quantum Anomaly, couldn't find any better naming.
                GregtechItemList.Laser_Lens_Special.get(0),
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Universium, 8),
                ItemList.Circuit_Chip_APIC.get(64),
                ItemList.Circuit_Parts_Chip_Bioware.get(64))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Universium, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.SpaceTime, FluidShapes.fluidMolten, (int) (1 * INGOTS)),
                MaterialLibAPI.getFluidStack(Materials.Infinity, FluidShapes.fluidMolten, (int) (4 * INGOTS)),
                MaterialLibAPI.getFluidStack(Materials.PrimordialMatter, FluidShapes.fluidLiquid, 64_000))
            .metadata(NANO_FORGE_TIER, 3)
            .duration(12 * MINUTES + 30 * SECONDS)
            .eut(2_000_000_000)
            .addTo(nanoForgeRecipes);

        // Eternity nanites
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.lens, Materials.Forcicium, 0, false),
                // Quantum Anomaly
                GregtechItemList.Laser_Lens_Special.get(0),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.TranscendentMetal, 1),
                MaterialLibAPI.getStack(Materials.Eternity, BlockShapes.block, 8),
                ItemList.Circuit_Chip_APIC.get(64),
                ItemList.Timepiece.get(4))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Eternity, 4))
            .fluidInputs(
                MaterialUtils.molten(Materials.spatialFluid, 8 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.ExcitedDTSC, FluidShapes.fluidLiquid, 50_000),
                MaterialLibAPI.getFluidStack(Materials.PrimordialMatter, FluidShapes.fluidLiquid, 64_000))
            .metadata(NANO_FORGE_TIER, 3)
            .duration(12 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_MAX)
            .addTo(nanoForgeRecipes);

        // MagMatter nanites, currently only used in the production of Stargates.
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.lens, Materials.Forcicium, 0, false),
                GTOreDictUnificator.get(OrePrefixes.lens, Materials.Forcillium, 0, false),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Universium, 1),
                MaterialLibAPI.getStack(Materials.Magmatter, BlockShapes.block, 8),
                ItemList.Circuit_Chip_YPIC.get(64),
                Circuits.MAX.get(1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Magmatter, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.QuarkGluonPlasma, FluidShapes.fluidLiquid, 100_000),
                MaterialLibAPI.getFluidStack(Materials.PhononMedium, FluidShapes.fluidLiquid, 64_000),
                MaterialLibAPI.getFluidStack(Materials.PrimordialMatter, FluidShapes.fluidLiquid, 128_000))
            .metadata(NANO_FORGE_TIER, 4)
            .duration(16 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_MAX)
            .addTo(nanoForgeRecipes);
    }
}
