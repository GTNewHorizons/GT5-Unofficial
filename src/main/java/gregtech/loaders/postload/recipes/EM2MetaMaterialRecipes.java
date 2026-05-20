package gregtech.loaders.postload.recipes;

import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.AssemblyLine;
import static gregtech.api.util.GTRecipeConstants.NANITE_TIERS;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GTRecipeConstants.SCANNING;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import bartworks.system.material.Werkstoff;
import goodgenerator.items.GGMaterial;
import gregtech.api.enums.CondensateType;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.NaniteTier;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.recipe.Scanning;
import gtPlusPlus.core.material.MaterialsElements;
import tectech.recipe.TecTechRecipeMaps;

/// EM2 Meta Material recipes — 8 materials, 3 or 4 tiers each, both BEC Assembler and AAL variants.
public class EM2MetaMaterialRecipes implements Runnable {

    private static final NaniteTier[] TIER_TO_NANITE = { NaniteTier.Carbon, NaniteTier.Silver, NaniteTier.Gold,
        NaniteTier.Transcendent, NaniteTier.SixPhasedCopper, NaniteTier.WhiteDwarf, NaniteTier.BlackDwarf,
        NaniteTier.Universium, NaniteTier.Eternity, NaniteTier.MagMatter };

    private static NaniteTier nanite(int tier) {
        return TIER_TO_NANITE[tier - 1];
    }

    @Override
    public void run() {
        registerShielding();
        registerWaveguide();
        registerEnergyConduit();
        registerWaveGate();
        registerWaveFocus();
        registerResonanceChamber();
        registerSensorArray();
        registerFieldManipulator();
    }

    private void registerShielding() {
        // 3-tier: UIV/UMV/UXV, 300s, "+2 per tier" nanites starting from T1 T4 T2 T1 T1
        ItemList[] outputs = { ItemList.MetaMaterial_Shielding1, ItemList.MetaMaterial_Shielding2,
            ItemList.MetaMaterial_Shielding3 };
        Materials[] foilMats = { Materials.ProtoHalkonite, Materials.Hexanite, Materials.MHDCSM };
        Object[] plateMats = { GGMaterial.tairitsu, Materials.TranscendentMetal, Materials.BlackDwarfMatter };
        Materials[] screwMats = { Materials.Churitsu, Materials.SpaceTime, Materials.Eternity };
        Materials[] boltMats = { Materials.Shijima, Materials.WhiteDwarfMatter, Materials.MagMatter };
        int[] condensateAmounts = { 2, 4, 8 };
        long[] euts = { TierEU.RECIPE_UIV, TierEU.RECIPE_UMV, TierEU.RECIPE_UXV };

        for (int t = 0; t < 3; t++) {
            ItemStack[] inputs = { ItemList.Naquarite_Universal_Insulator_Foil.get(1),
                GTOreDictUnificator.get(OrePrefixes.foil, foilMats[t], 4), platePart(plateMats[t], 1),
                GTOreDictUnificator.get(OrePrefixes.screw, screwMats[t], 8),
                GTOreDictUnificator.get(OrePrefixes.bolt, boltMats[t], 8) };
            NaniteTier[] nanites = { nanite(1 + 2 * t), nanite(4 + 2 * t), nanite(2 + 2 * t), nanite(1 + 2 * t),
                nanite(1 + 2 * t) };
            int chromaticAmt = condensateAmounts[t];
            int transcendentAmt = condensateAmounts[t];
            CondensateType[] becCondensates = { CondensateType.ChromaticGlass, CondensateType.TranscendentMetal };
            int[] becAmounts = { chromaticAmt, transcendentAmt };

            if (t == 0) {
                FluidStack[] aalFluids = {
                    MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getFluidStack(chromaticAmt * INGOTS),
                    Materials.TranscendentMetal.getMolten(transcendentAmt * INGOTS) };
                addBecAndAal(
                    outputs[t].get(1),
                    inputs,
                    nanites,
                    becCondensates,
                    becAmounts,
                    aalFluids,
                    300 * SECONDS,
                    euts[t]);
            } else {
                addBec(outputs[t].get(1), inputs, nanites, becCondensates, becAmounts, 300 * SECONDS, euts[t]);
            }
        }
    }

    private void registerWaveguide() {
        // 3-tier UIV/UMV/UXV 300s. Nanites: T1 T4 T2 T1, +2 per tier
        ItemList[] outputs = { ItemList.MetaMaterial_Waveguide1, ItemList.MetaMaterial_Waveguide2,
            ItemList.MetaMaterial_Waveguide3 };
        Materials[] longRodMats = { Materials.ProtoHalkonite, Materials.Hexanite, Materials.MHDCSM };
        Object[] fineWireMats = { GGMaterial.tairitsu, Materials.TranscendentMetal, Materials.BlackDwarfMatter };
        Materials[] foilMats = { Materials.Churitsu, Materials.SpaceTime, Materials.Eternity };
        Materials[] screwMats = { Materials.Shijima, Materials.WhiteDwarfMatter, Materials.MagMatter };
        int[] chromaticAmts = { 4, 6, 8 };
        int[] hypogenAmts = { 0, 2, 4 };
        long[] euts = { TierEU.RECIPE_UIV, TierEU.RECIPE_UMV, TierEU.RECIPE_UXV };

        for (int t = 0; t < 3; t++) {
            ItemStack[] inputs = { GTOreDictUnificator.get(OrePrefixes.stickLong, longRodMats[t], 1),
                fineWirePart(fineWireMats[t], 8), GTOreDictUnificator.get(OrePrefixes.foil, foilMats[t], 4),
                GTOreDictUnificator.get(OrePrefixes.screw, screwMats[t], 8) };
            NaniteTier[] nanites = { nanite(1 + 2 * t), nanite(4 + 2 * t), nanite(2 + 2 * t), nanite(1 + 2 * t) };
            CondensateType[] becCondensates = hypogenAmts[t] == 0
                ? new CondensateType[] { CondensateType.ChromaticGlass }
                : new CondensateType[] { CondensateType.ChromaticGlass, CondensateType.Hypogen };
            int[] becAmounts = hypogenAmts[t] == 0 ? new int[] { chromaticAmts[t] }
                : new int[] { chromaticAmts[t], hypogenAmts[t] };

            if (t == 0) {
                FluidStack[] aalFluids = {
                    MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getFluidStack(chromaticAmts[t] * INGOTS) };
                addBecAndAal(
                    outputs[t].get(1),
                    inputs,
                    nanites,
                    becCondensates,
                    becAmounts,
                    aalFluids,
                    300 * SECONDS,
                    euts[t]);
            } else {
                addBec(outputs[t].get(1), inputs, nanites, becCondensates, becAmounts, 300 * SECONDS, euts[t]);
            }
        }
    }

    private void registerEnergyConduit() {
        // 3-tier UIV/UMV/UXV 300s. Nanites: T4 T1 T2 T1, +2 per tier
        ItemList[] outputs = { ItemList.MetaMaterial_EnergyConduit1, ItemList.MetaMaterial_EnergyConduit2,
            ItemList.MetaMaterial_EnergyConduit3 };
        ItemStack[] superconductors = { GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUIV, 1),
            GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUMV, 1),
            ItemList.Thermal_Superconductor.get(1) };
        Object[] foilMats = { GGMaterial.tairitsu, Materials.TranscendentMetal, Materials.BlackDwarfMatter };
        Materials[] ringMats = { Materials.Churitsu, Materials.SpaceTime, Materials.Eternity };
        Materials[] screwMats = { Materials.Shijima, Materials.WhiteDwarfMatter, Materials.MagMatter };
        int[] chromaticAmts = { 4, 8, 16 };
        int[] spacetimeAmts = { 0, 2, 4 };
        long[] euts = { TierEU.RECIPE_UIV, TierEU.RECIPE_UMV, TierEU.RECIPE_UXV };

        for (int t = 0; t < 3; t++) {
            ItemStack[] inputs = { superconductors[t], foilPart(foilMats[t], 4),
                GTOreDictUnificator.get(OrePrefixes.ring, ringMats[t], 2),
                GTOreDictUnificator.get(OrePrefixes.screw, screwMats[t], 8) };
            NaniteTier[] nanites = { nanite(4 + 2 * t), nanite(1 + 2 * t), nanite(2 + 2 * t), nanite(1 + 2 * t) };
            CondensateType[] becCondensates = spacetimeAmts[t] == 0
                ? new CondensateType[] { CondensateType.ChromaticGlass }
                : new CondensateType[] { CondensateType.ChromaticGlass, CondensateType.SpaceTime };
            int[] becAmounts = spacetimeAmts[t] == 0 ? new int[] { chromaticAmts[t] }
                : new int[] { chromaticAmts[t], spacetimeAmts[t] };

            if (t == 0) {
                FluidStack[] aalFluids = {
                    MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getFluidStack(chromaticAmts[t] * INGOTS) };
                addBecAndAal(
                    outputs[t].get(1),
                    inputs,
                    nanites,
                    becCondensates,
                    becAmounts,
                    aalFluids,
                    300 * SECONDS,
                    euts[t]);
            } else {
                addBec(outputs[t].get(1), inputs, nanites, becCondensates, becAmounts, 300 * SECONDS, euts[t]);
            }
        }
    }

    private void registerWaveGate() {
        // 3-tier UIV/UMV/UXV 300s. Nanites: T4 T1 T2 T1, +2 per tier
        ItemList[] outputs = { ItemList.MetaMaterial_WaveGate1, ItemList.MetaMaterial_WaveGate2,
            ItemList.MetaMaterial_WaveGate3 };
        Materials[] ringMats = { Materials.ProtoHalkonite, Materials.Hexanite, Materials.MHDCSM };
        Object[] foilMats = { GGMaterial.tairitsu, Materials.TranscendentMetal, Materials.BlackDwarfMatter };
        Materials[] smallGearMats = { Materials.Churitsu, Materials.SpaceTime, Materials.Eternity };
        Materials[] smallScrewMats = { Materials.Shijima, Materials.WhiteDwarfMatter, Materials.MagMatter };
        int[] chromaticAmts = { 4, 8, 16 };
        int[] dimShiftedAmts = { 2, 4, 8 };
        long[] euts = { TierEU.RECIPE_UIV, TierEU.RECIPE_UMV, TierEU.RECIPE_UXV };

        for (int t = 0; t < 3; t++) {
            ItemStack[] inputs = { GTOreDictUnificator.get(OrePrefixes.ring, ringMats[t], 2), foilPart(foilMats[t], 4),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, smallGearMats[t], 1),
                GTOreDictUnificator.get(OrePrefixes.screw, smallScrewMats[t], 8) };
            NaniteTier[] nanites = { nanite(4 + 2 * t), nanite(1 + 2 * t), nanite(2 + 2 * t), nanite(1 + 2 * t) };
            CondensateType[] becCondensates = { CondensateType.ChromaticGlass,
                CondensateType.DimensionallyShiftedSuperfluid };
            int[] becAmounts = { chromaticAmts[t], dimShiftedAmts[t] };

            if (t == 0) {
                FluidStack[] aalFluids = {
                    MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getFluidStack(chromaticAmts[t] * INGOTS),
                    Materials.DimensionallyShiftedSuperfluid.getFluid(dimShiftedAmts[t] * INGOTS) };
                addBecAndAal(
                    outputs[t].get(1),
                    inputs,
                    nanites,
                    becCondensates,
                    becAmounts,
                    aalFluids,
                    300 * SECONDS,
                    euts[t]);
            } else {
                addBec(outputs[t].get(1), inputs, nanites, becCondensates, becAmounts, 300 * SECONDS, euts[t]);
            }
        }
    }

    private void registerWaveFocus() {
        // 4-tier UEV/UIV/UMV/UXV 120s. Nanites: T4 T2 T3 T2, +1 per tier
        ItemList[] outputs = { ItemList.MetaMaterial_WaveFocus1, ItemList.MetaMaterial_WaveFocus2,
            ItemList.MetaMaterial_WaveFocus3, ItemList.MetaMaterial_WaveFocus4 };
        Object[] ringMats = { GGMaterial.tairitsu, MaterialsElements.STANDALONE.HYPOGEN, GGMaterial.shirabon,
            Materials.SpaceTime };
        Materials[] boltMats = { Materials.Churitsu, Materials.WhiteDwarfMatter, Materials.BlackDwarfMatter,
            Materials.MHDCSM };
        Materials[] screwMats = { Materials.Shijima, Materials.ProtoHalkonite, Materials.Hexanite, Materials.Eternity };
        int[] chromaticAmts = { 8, 12, 16, 24 };
        int[] solderAmts = { 0, 2, 3, 4 }; // tier 0 uses transcendent metal instead
        long[] euts = { TierEU.RECIPE_UEV, TierEU.RECIPE_UIV, TierEU.RECIPE_UMV, TierEU.RECIPE_UXV };

        for (int t = 0; t < 4; t++) {
            ItemStack[] inputs = { ItemList.Gravitational_Lens.get(1), ringPart(ringMats[t], 1),
                GTOreDictUnificator.get(OrePrefixes.bolt, boltMats[t], 2),
                GTOreDictUnificator.get(OrePrefixes.screw, screwMats[t], 2) };
            NaniteTier[] nanites = { nanite(4 + t), nanite(2 + t), nanite(3 + t), nanite(2 + t) };

            if (t == 0) {
                CondensateType[] becCondensates = { CondensateType.ChromaticGlass, CondensateType.TranscendentMetal };
                int[] becAmounts = { chromaticAmts[t], 1 };
                FluidStack[] aalFluids = {
                    MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getFluidStack(chromaticAmts[t] * INGOTS),
                    Materials.TranscendentMetal.getMolten(INGOTS) };
                addBecAndAal(
                    outputs[t].get(1),
                    inputs,
                    nanites,
                    becCondensates,
                    becAmounts,
                    aalFluids,
                    120 * SECONDS,
                    euts[t]);
            } else {
                CondensateType[] becCondensates = { CondensateType.ChromaticGlass,
                    CondensateType.BoundlessCosmicSolder };
                int[] becAmounts = { chromaticAmts[t], solderAmts[t] };
                addBec(outputs[t].get(1), inputs, nanites, becCondensates, becAmounts, 120 * SECONDS, euts[t]);
            }
        }
    }

    private void registerResonanceChamber() {
        // 4-tier UEV/UIV/UMV/UXV 120s. Nanites: T3 T2 T4 T2, +1 per tier
        ItemList[] outputs = { ItemList.MetaMaterial_ResonanceChamber1, ItemList.MetaMaterial_ResonanceChamber2,
            ItemList.MetaMaterial_ResonanceChamber3, ItemList.MetaMaterial_ResonanceChamber4 };
        Object[] plateMats = { GGMaterial.tairitsu, MaterialsElements.STANDALONE.HYPOGEN, GGMaterial.shirabon,
            Materials.SpaceTime };
        Materials[] foilMats = { Materials.Churitsu, Materials.WhiteDwarfMatter, Materials.BlackDwarfMatter,
            Materials.MHDCSM };
        Materials[] platePart3Mats = { Materials.Shijima, Materials.ProtoHalkonite, Materials.Hexanite,
            Materials.Eternity };
        int[] chromaticAmts = { 8, 12, 16, 24 };
        int[] solderAmts = { 0, 2, 3, 4 };
        long[] euts = { TierEU.RECIPE_UEV, TierEU.RECIPE_UIV, TierEU.RECIPE_UMV, TierEU.RECIPE_UXV };

        for (int t = 0; t < 4; t++) {
            ItemStack[] inputs = { ItemList.Relativistic_Heat_Capacitor.get(1), platePart(plateMats[t], 1),
                foilPart(foilMats[t], 1), GTOreDictUnificator.get(OrePrefixes.plate, platePart3Mats[t], 1) };
            NaniteTier[] nanites = { nanite(3 + t), nanite(2 + t), nanite(4 + t), nanite(2 + t) };

            if (t == 0) {
                CondensateType[] becCondensates = { CondensateType.ChromaticGlass, CondensateType.TranscendentMetal };
                int[] becAmounts = { chromaticAmts[t], 1 };
                FluidStack[] aalFluids = {
                    MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getFluidStack(chromaticAmts[t] * INGOTS),
                    Materials.TranscendentMetal.getMolten(INGOTS) };
                addBecAndAal(
                    outputs[t].get(1),
                    inputs,
                    nanites,
                    becCondensates,
                    becAmounts,
                    aalFluids,
                    120 * SECONDS,
                    euts[t]);
            } else {
                CondensateType[] becCondensates = { CondensateType.ChromaticGlass,
                    CondensateType.BoundlessCosmicSolder };
                int[] becAmounts = { chromaticAmts[t], solderAmts[t] };
                addBec(outputs[t].get(1), inputs, nanites, becCondensates, becAmounts, 120 * SECONDS, euts[t]);
            }
        }
    }

    private void registerSensorArray() {
        // 4-tier UEV/UIV/UMV/UXV 90s. Nanites: T2 T2 T3 T4 T3, +1 per tier
        ItemList[] outputs = { ItemList.MetaMaterial_SensorArray1, ItemList.MetaMaterial_SensorArray2,
            ItemList.MetaMaterial_SensorArray3, ItemList.MetaMaterial_SensorArray4 };
        ItemList[] sensors = { ItemList.Sensor_UHV, ItemList.Sensor_UEV, ItemList.Sensor_UIV, ItemList.Sensor_UMV };
        ItemList[] emitters = { ItemList.Emitter_UHV, ItemList.Emitter_UEV, ItemList.Emitter_UIV,
            ItemList.Emitter_UMV };
        Object[] plateMats = { GGMaterial.tairitsu, MaterialsElements.STANDALONE.HYPOGEN, GGMaterial.shirabon,
            Materials.SpaceTime };
        Materials[] screwMats = { Materials.Shijima, Materials.ProtoHalkonite, Materials.Hexanite, Materials.Eternity };
        Materials[] boltMats = { Materials.Churitsu, Materials.WhiteDwarfMatter, Materials.BlackDwarfMatter,
            Materials.MHDCSM };
        int[] chromaticAmts = { 8, 12, 16, 24 };
        int[] solderAmts = { 0, 2, 3, 4 };
        long[] euts = { TierEU.RECIPE_UEV, TierEU.RECIPE_UIV, TierEU.RECIPE_UMV, TierEU.RECIPE_UXV };

        for (int t = 0; t < 4; t++) {
            ItemStack[] inputs = { sensors[t].get(1), emitters[t].get(1), platePart(plateMats[t], 2),
                GTOreDictUnificator.get(OrePrefixes.screw, screwMats[t], 4),
                GTOreDictUnificator.get(OrePrefixes.bolt, boltMats[t], 4) };
            NaniteTier[] nanites = { nanite(2 + t), nanite(2 + t), nanite(3 + t), nanite(4 + t), nanite(3 + t) };

            if (t == 0) {
                CondensateType[] becCondensates = { CondensateType.ChromaticGlass, CondensateType.TranscendentMetal };
                int[] becAmounts = { chromaticAmts[t], 1 };
                FluidStack[] aalFluids = {
                    MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getFluidStack(chromaticAmts[t] * INGOTS),
                    Materials.TranscendentMetal.getMolten(INGOTS) };
                addBecAndAal(
                    outputs[t].get(1),
                    inputs,
                    nanites,
                    becCondensates,
                    becAmounts,
                    aalFluids,
                    90 * SECONDS,
                    euts[t]);
            } else {
                CondensateType[] becCondensates = { CondensateType.ChromaticGlass,
                    CondensateType.BoundlessCosmicSolder };
                int[] becAmounts = { chromaticAmts[t], solderAmts[t] };
                addBec(outputs[t].get(1), inputs, nanites, becCondensates, becAmounts, 90 * SECONDS, euts[t]);
            }
        }
    }

    private void registerFieldManipulator() {
        // 4-tier UEV/UIV/UMV/UXV 120s. Nanites: T4 T3 T2 T2, +1 per tier
        ItemList[] outputs = { ItemList.MetaMaterial_FieldManipulator1, ItemList.MetaMaterial_FieldManipulator2,
            ItemList.MetaMaterial_FieldManipulator3, ItemList.MetaMaterial_FieldManipulator4 };
        ItemList[] fieldGens = { ItemList.Field_Generator_UHV, ItemList.Field_Generator_UEV,
            ItemList.Field_Generator_UIV, ItemList.Field_Generator_UMV };
        Object[] longRodMats = { GGMaterial.tairitsu, MaterialsElements.STANDALONE.HYPOGEN, GGMaterial.shirabon,
            Materials.SpaceTime };
        Materials[] screwMats = { Materials.Churitsu, Materials.WhiteDwarfMatter, Materials.BlackDwarfMatter,
            Materials.MHDCSM };
        Materials[] doublePlateMats = { Materials.Shijima, Materials.ProtoHalkonite, Materials.Hexanite,
            Materials.Eternity };
        int[] chromaticAmts = { 8, 12, 16, 24 };
        int[] solderAmts = { 0, 2, 3, 4 };
        long[] euts = { TierEU.RECIPE_UEV, TierEU.RECIPE_UIV, TierEU.RECIPE_UMV, TierEU.RECIPE_UXV };

        for (int t = 0; t < 4; t++) {
            ItemStack[] inputs = { fieldGens[t].get(1), longRodPart(longRodMats[t], 2),
                GTOreDictUnificator.get(OrePrefixes.screw, screwMats[t], 2),
                GTOreDictUnificator.get(OrePrefixes.plateDouble, doublePlateMats[t], 2) };
            NaniteTier[] nanites = { nanite(4 + t), nanite(3 + t), nanite(2 + t), nanite(2 + t) };

            CondensateType[] becCondensates;
            int[] becAmounts;
            FluidStack[] aalFluids;
            if (t == 0) {
                becCondensates = new CondensateType[] { CondensateType.ChromaticGlass,
                    CondensateType.TranscendentMetal };
                becAmounts = new int[] { chromaticAmts[t], 1 };
                aalFluids = new FluidStack[] {
                    MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getFluidStack(chromaticAmts[t] * INGOTS),
                    Materials.TranscendentMetal.getMolten(INGOTS) };
                addBecAndAal(
                    outputs[t].get(1),
                    inputs,
                    nanites,
                    becCondensates,
                    becAmounts,
                    aalFluids,
                    120 * SECONDS,
                    euts[t]);
            } else {
                becCondensates = new CondensateType[] { CondensateType.ChromaticGlass,
                    CondensateType.BoundlessCosmicSolder };
                becAmounts = new int[] { chromaticAmts[t], solderAmts[t] };
                addBec(outputs[t].get(1), inputs, nanites, becCondensates, becAmounts, 120 * SECONDS, euts[t]);
            }
        }
    }

    /// Builds and registers both the BEC Assembler variant (using entangled condensates + nanite-tier metadata) and
    /// the AAL variant (3× ingredients + regular molten/prepared fluids).
    private void addBecAndAal(ItemStack output, ItemStack[] inputs, NaniteTier[] nanites, CondensateType[] condensates,
        int[] condensateIngots, FluidStack[] aalFluids, int duration, long eut) {
        addBec(output, inputs, nanites, condensates, condensateIngots, duration, eut);

        ItemStack[] aalInputs = new ItemStack[inputs.length];
        for (int i = 0; i < inputs.length; i++) {
            aalInputs[i] = inputs[i].copy();
            aalInputs[i].stackSize *= 3;
        }

        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, inputs[0])
            .metadata(SCANNING, new Scanning(2 * SECONDS, eut))
            .itemInputs(aalInputs)
            .fluidInputs(aalFluids)
            .itemOutputs(output)
            .duration(duration)
            .eut(eut)
            .addTo(AssemblyLine);
    }
    
    /// Builds and registers both the BEC Assembler variant (using entangled condensates + nanite-tier metadata)
    private void addBec(ItemStack output, ItemStack[] inputs, NaniteTier[] nanites, CondensateType[] condensates,
        int[] condensateIngots, int duration, long eut) {
        for (int i = 0; i < inputs.length; i++) {
            if (inputs[i] == null) {
                throw new IllegalStateException(
                    "EM2 Meta Material recipe for " + output.getDisplayName()
                        + " has null input at index "
                        + i
                        + " — an ore prefix is missing for the material in that slot.");
            }
        }
        FluidStack[] entangled = new FluidStack[condensates.length];
        for (int i = 0; i < condensates.length; i++) {
            entangled[i] = condensates[i].getEntangled(condensateIngots[i] * INGOTS);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(inputs)
            .fluidInputs(entangled)
            .itemOutputs(output)
            .metadata(NANITE_TIERS, nanites)
            .duration(duration)
            .eut(eut)
            .addTo(TecTechRecipeMaps.condensateAssemblingRecipes);
    }

    // Material-type adapters: Werkstoff / Materials / gtPlusPlus Material

    private static ItemStack platePart(Object material, int amount) {
        if (material instanceof Werkstoff w) return w.get(OrePrefixes.plate, amount);
        if (material instanceof Materials m) return GTOreDictUnificator.get(OrePrefixes.plate, m, amount);
        if (material instanceof gtPlusPlus.core.material.Material m) return m.getPlate(amount);
        throw new IllegalArgumentException("Unsupported material kind: " + material);
    }

    private static ItemStack foilPart(Object material, int amount) {
        if (material instanceof Werkstoff w) return w.get(OrePrefixes.foil, amount);
        if (material instanceof Materials m) return GTOreDictUnificator.get(OrePrefixes.foil, m, amount);
        if (material instanceof gtPlusPlus.core.material.Material m) return m.getFoil(amount);
        throw new IllegalArgumentException("Unsupported material kind: " + material);
    }

    private static ItemStack fineWirePart(Object material, int amount) {
        if (material instanceof Werkstoff w) return w.get(OrePrefixes.wireFine, amount);
        if (material instanceof Materials m) return GTOreDictUnificator.get(OrePrefixes.wireFine, m, amount);
        if (material instanceof gtPlusPlus.core.material.Material m) return m.getFineWire(amount);
        throw new IllegalArgumentException("Unsupported material kind: " + material);
    }

    private static ItemStack ringPart(Object material, int amount) {
        if (material instanceof Werkstoff w) return w.get(OrePrefixes.ring, amount);
        if (material instanceof Materials m) return GTOreDictUnificator.get(OrePrefixes.ring, m, amount);
        if (material instanceof gtPlusPlus.core.material.Material m) return m.getRing(amount);
        throw new IllegalArgumentException("Unsupported material kind: " + material);
    }

    private static ItemStack longRodPart(Object material, int amount) {
        if (material instanceof Werkstoff w) return w.get(OrePrefixes.stickLong, amount);
        if (material instanceof Materials m) return GTOreDictUnificator.get(OrePrefixes.stickLong, m, amount);
        if (material instanceof gtPlusPlus.core.material.Material m) return m.getLongRod(amount);
        throw new IllegalArgumentException("Unsupported material kind: " + material);
    }
}
