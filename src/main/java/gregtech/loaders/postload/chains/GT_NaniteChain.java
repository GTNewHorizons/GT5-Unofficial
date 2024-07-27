package gregtech.loaders.postload.chains;

import static gregtech.api.enums.Mods.BartWorks;
import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.Mods.GoodGenerator;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.recipe.RecipeMaps.nanoForgeRecipes;
import static gregtech.api.util.GT_ModHandler.getModItem;
import static gregtech.api.util.GT_RecipeBuilder.HOURS;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeConstants.AssemblyLine;
import static gregtech.api.util.GT_RecipeConstants.NANO_FORGE_TIER;
import static gregtech.api.util.GT_RecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GT_RecipeConstants.RESEARCH_TIME;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_OreDictUnificator;

public class GT_NaniteChain {

    public static void run() {

        ItemStack aUVTierLens = getModItem(NewHorizonsCoreMod.ID, "item.MysteriousCrystalLens", 0);
        ItemStack aUHVTierLens = getModItem(NewHorizonsCoreMod.ID, "item.ChromaticLens", 0);
        ItemStack aUEVTierLens = getModItem(NewHorizonsCoreMod.ID, "item.RadoxPolymerLens", 0);
        ItemStack aUIVTierLens = ItemList.EnergisedTesseract.get(0);
        ItemStack aUMVTierLens = GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Dilithium, 0, false);

        // Carbon Nanite Recipe Before Nano Forge
        Fluid solderIndalloy = FluidRegistry.getFluid("molten.indalloy140") != null
            ? FluidRegistry.getFluid("molten.indalloy140")
            : FluidRegistry.getFluid("molten.solderingalloy");

        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, Materials.Carbon.getNanite(1))
            .metadata(RESEARCH_TIME, 1 * HOURS)
            .itemInputs(
                ItemList.Hull_UV.get(16),
                Materials.Carbon.getNanite(16),
                ItemList.Field_Generator_ZPM.get(16),
                ItemList.Conveyor_Module_UV.get(16),
                ItemList.Electric_Motor_UV.get(32),
                new Object[] { OrePrefixes.circuit.get(Materials.Master), 16 },
                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Naquadah, 32))
            .fluidInputs(
                new FluidStack(solderIndalloy, 144 * 32),
                Materials.HSSS.getMolten(144L * 32),
                Materials.Osmiridium.getMolten(144L * 16))
            .itemOutputs(ItemList.NanoForge.get(1))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * MINUTES)
            .addTo(AssemblyLine);

        GT_Values.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Circuit_Crystalmainframe.get(1))
            .metadata(RESEARCH_TIME, 2 * HOURS)
            .itemInputs(
                new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 16 },
                ItemList.Robot_Arm_UV.get(16),
                ItemList.Circuit_Chip_Stemcell.get(32),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.NaquadahAlloy, 32),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.NaquadahAlloy, 16),
                Materials.Carbon.getDust(64))
            .fluidInputs(Materials.UUMatter.getFluid(10000), new FluidStack(solderIndalloy, 144 * 32))
            .itemOutputs(Materials.Carbon.getNanite(2))
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
        GT_Values.RA.stdBuilder()
            .itemInputs(
                aUVTierLens,
                getModItem(BartWorks.ID, "bw.werkstoffblockscasingadvanced.01", 8, 31776),
                ItemList.Circuit_Chip_SoC.get(64))
            .itemOutputs(Materials.Carbon.getNanite(64))
            .fluidInputs(Materials.UUMatter.getFluid(200_000))
            .metadata(NANO_FORGE_TIER, 1)
            .duration(8 * MINUTES + 20 * SECONDS)
            .eut(10_000_000)
            .addTo(nanoForgeRecipes);

        // Silver Nanites - Used in Tier 2 PCB Factory to improve board production
        GT_Values.RA.stdBuilder()
            .itemInputs(aUEVTierLens, Materials.Silver.getBlocks(8), ItemList.Circuit_Chip_SoC.get(16))
            .itemOutputs(Materials.Silver.getNanite(1))
            .fluidInputs(Materials.UUMatter.getFluid(200_000))
            .metadata(NANO_FORGE_TIER, 2)
            .duration(37 * MINUTES + 30 * SECONDS)
            .eut(10_000_000)
            .addTo(nanoForgeRecipes);

        // Neutronium Nanites - Used to upgrade the Nano Forge to Tier 2
        GT_Values.RA.stdBuilder()
            .itemInputs(
                aUHVTierLens,
                Materials.Neutronium.getBlocks(8),
                ItemList.Circuit_Chip_SoC2.get(64),
                ItemList.Circuit_Chip_SoC2.get(32))
            .itemOutputs(Materials.Neutronium.getNanite(1))
            .fluidInputs(Materials.UUMatter.getFluid(200_000))
            .metadata(NANO_FORGE_TIER, 1)
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(100_000_000)
            .addTo(nanoForgeRecipes);

        // Glowstone Nanites - Used in the optical circuit line
        GT_Values.RA.stdBuilder()
            .itemInputs(
                aUEVTierLens,
                getModItem(GTPlusPlus.ID, "blockCompressedObsidian", 8, 7), // Double compressed glowstone blocks (yes,
                                                                            // it's not obsidian)
                ItemList.Circuit_Chip_SoC2.get(64))
            .itemOutputs(Materials.Glowstone.getNanite(64))
            .fluidInputs(Materials.UUMatter.getFluid(50_000))
            .metadata(NANO_FORGE_TIER, 2)
            .duration(3 * MINUTES + 20 * SECONDS)
            .eut(50_000_000)
            .addTo(nanoForgeRecipes);

        // Gold Nanites - Used in Tier 3 PCB Factory to improve board production
        GT_Values.RA.stdBuilder()
            .itemInputs(aUMVTierLens, Materials.Gold.getBlocks(8), ItemList.Circuit_Chip_SoC.get(16))
            .itemOutputs(Materials.Gold.getNanite(1))
            .fluidInputs(Materials.UUMatter.getFluid(300_000))
            .metadata(NANO_FORGE_TIER, 3)
            .duration(16 * MINUTES + 40 * SECONDS)
            .eut(100_000_000)
            .addTo(nanoForgeRecipes);

        // Transcendent Metal Nanites - Used to upgrade the Nano Forge to Tier 3
        GT_Values.RA.stdBuilder()
            .itemInputs(
                aUIVTierLens,
                MaterialsUEVplus.TranscendentMetal.getBlocks(8),
                ItemList.Circuit_Chip_SoC2.get(64),
                ItemList.Circuit_Chip_SoC2.get(64),
                ItemList.Circuit_Chip_SoC2.get(64))
            .itemOutputs(MaterialsUEVplus.TranscendentMetal.getNanite(1))
            .fluidInputs(Materials.UUMatter.getFluid(2_000_000))
            .metadata(NANO_FORGE_TIER, 2)
            .duration(12 * MINUTES + 30 * SECONDS)
            .eut(1_000_000_000)
            .addTo(nanoForgeRecipes);

        // White Dwarf Matter Nanites. Used to make Magnetohydrodynamically constrained star matter.
        GT_Values.RA.stdBuilder()
            .itemInputs(
                aUMVTierLens,
                getModItem(BartWorks.ID, "gt.bwMetaGeneratedlens", 0, 36), // Magneto lens
                getModItem(GTPlusPlus.ID, "MU-metaitem.01", 0, 32105), // Quantum Anomaly, couldn't find any better
                                                                       // naming
                MaterialsUEVplus.WhiteDwarfMatter.getBlocks(8),
                getModItem(NewHorizonsCoreMod.ID, "item.PicoWafer", 32),
                getModItem(GoodGenerator.ID, "huiCircuit", 1, 4) // Paradox circuit
            )
            .itemOutputs(MaterialsUEVplus.WhiteDwarfMatter.getNanite(4))
            .fluidInputs(
                Materials.UUMatter.getFluid(500_000),
                MaterialsUEVplus.RawStarMatter.getFluid(50_000),
                MaterialsUEVplus.Space.getMolten(720))
            .metadata(NANO_FORGE_TIER, 3)
            .duration(12 * MINUTES + 30 * SECONDS)
            .eut(2_000_000_000)
            .addTo(nanoForgeRecipes);

        // Black Dwarf Matter Nanites. Used to make Magnetohydrodynamically constrained star matter.
        GT_Values.RA.stdBuilder()
            .itemInputs(
                aUMVTierLens,
                getModItem(BartWorks.ID, "gt.bwMetaGeneratedlens", 0, 36), // Magneto lens
                getModItem(GTPlusPlus.ID, "MU-metaitem.01", 0, 32105), // Quantum Anomaly, couldn't find any better
                                                                       // naming
                MaterialsUEVplus.BlackDwarfMatter.getBlocks(8),
                getModItem(NewHorizonsCoreMod.ID, "item.PicoWafer", 32),
                getModItem(GoodGenerator.ID, "huiCircuit", 1, 4) // Paradox circuit
            )
            .itemOutputs(MaterialsUEVplus.BlackDwarfMatter.getNanite(4))
            .fluidInputs(
                Materials.UUMatter.getFluid(500_000),
                MaterialsUEVplus.RawStarMatter.getFluid(50_000),
                MaterialsUEVplus.Time.getMolten(720))
            .metadata(NANO_FORGE_TIER, 3)
            .duration(12 * MINUTES + 30 * SECONDS)
            .eut(2_000_000_000)
            .addTo(nanoForgeRecipes);

        // Universium nanites.
        GT_Values.RA.stdBuilder()
            .itemInputs(
                aUMVTierLens,
                // Quantum Anomaly, couldn't find any better naming.
                getModItem(GTPlusPlus.ID, "MU-metaitem.01", 0, 32105),
                MaterialsUEVplus.Universium.getBlocks(8),
                ItemList.Optically_Perfected_CPU.get(16),
                ItemList.Optically_Compatible_Memory.get(16),
                getModItem(GoodGenerator.ID, "huiCircuit", 1, 4) // Paradox circuit
            )
            .itemOutputs(MaterialsUEVplus.Universium.getNanite(2))
            .fluidInputs(
                MaterialsUEVplus.SpaceTime.getMolten(144),
                Materials.Infinity.getMolten(576),
                MaterialsUEVplus.PrimordialMatter.getFluid(64_000))
            .metadata(NANO_FORGE_TIER, 3)
            .duration(12 * MINUTES + 30 * SECONDS)
            .eut(2_000_000_000)
            .addTo(nanoForgeRecipes);

        // Eternity nanites
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Forcicium, 0, false),
                // Quantum Anomaly
                getModItem(GTPlusPlus.ID, "MU-metaitem.01", 0, 32105),
                MaterialsUEVplus.TranscendentMetal.getNanite(1),
                MaterialsUEVplus.Eternity.getBlocks(8),
                getModItem(NewHorizonsCoreMod.ID, "item.PicoWafer", 32),
                ItemList.Timepiece.get(4))
            .itemOutputs(MaterialsUEVplus.Eternity.getNanite(4))
            .fluidInputs(
                MaterialsUEVplus.Space.getMolten(1152),
                MaterialsUEVplus.ExcitedDTSC.getFluid(50_000),
                MaterialsUEVplus.PrimordialMatter.getFluid(64_000))
            .metadata(NANO_FORGE_TIER, 3)
            .duration(12 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_MAX)
            .addTo(nanoForgeRecipes);

    }
}
