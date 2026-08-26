package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.NANITE_TIERS;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import bartworks.system.material.Werkstoff;
import goodgenerator.items.GGMaterial;
import gregtech.api.enums.CondensateType;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.NaniteTier;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.material.MaterialsElements;
import tectech.recipe.TecTechRecipeMaps;

/// BEC Meta Material recipes
/// </p>
/// There are two groups, four 3-tier items and four 4-tier items.
/// The 3-tier items have 5 ingredients: a base and 4 tiered materials
/// The 4-tier items have 4 ingredients: a base and 3 tiered materials
/// To ensure consistency among the items, they are autogenned. Thus, there are arrays for the tiered materials.
public class BECMetaMaterialRecipes implements Runnable {

    private static final NaniteTier[] TIER_TO_NANITE = { NaniteTier.Carbon, NaniteTier.Silver, NaniteTier.Gold,
        NaniteTier.Transcendent, NaniteTier.SixPhasedCopper, NaniteTier.WhiteDwarf, NaniteTier.BlackDwarf,
        NaniteTier.Universium, NaniteTier.Eternity, NaniteTier.MagMatter };

    private static final int[] baseAmounts = { 1, 2, 4, 8 };
    private static final int baseDuration = 300 * SECONDS;

    private static final Materials[] other3Tier = { Materials.ProtoHalkonite, Materials.Hexanite, Materials.MHDCSM };
    private static final Object[] black3Tier = { GGMaterial.tairitsu, Materials.TranscendentMetal,
        Materials.BlackDwarfMatter };
    private static final Materials[] gray3Tier = { Materials.Churitsu, Materials.SpaceTime, Materials.Eternity };
    private static final Materials[] white3Tier = { Materials.Shijima, Materials.WhiteDwarfMatter,
        Materials.MagMatter };

    private static final Object[] black4Tier = { GGMaterial.tairitsu, MaterialsElements.STANDALONE.HYPOGEN,
        GGMaterial.shirabon, Materials.SpaceTime };
    private static final Materials[] gray4Tier = { Materials.Churitsu, Materials.WhiteDwarfMatter,
        Materials.BlackDwarfMatter, Materials.MHDCSM };
    private static final Materials[] white4Tier = { Materials.Shijima, Materials.ProtoHalkonite, Materials.Hexanite,
        Materials.Eternity };

    private static final int[] chromatic4TierAmts = { 8, 12, 16, 24 };
    private static final int[] infinity4TierAmts = { 4, 6, 8, 12 };
    private static final int[] solder4TierAmts = { 1, 2, 3, 4 };

    private static NaniteTier nanite(int tier) {
        return TIER_TO_NANITE[tier - 1];
    }

    @Override
    public void run() {
        if (NewHorizonsCoreMod.isModLoaded()) registerShielding();
        registerWaveguide();
        registerEnergyConduit();
        if (Mods.Railcraft.isModLoaded()) registerElectrograviticValve();
        registerWaveFocus();
        registerResonanceChamber();
        registerSensorArray();
        registerFieldManipulator();
    }

    private void registerShielding() {
        // 3-tier UIV/UMV/UXV 300s. Nanites: T4 T1 T1 T3 T1, +2 per tier
        ItemList[] outputs = { ItemList.MetaMaterial_Shielding1, ItemList.MetaMaterial_Shielding2,
            ItemList.MetaMaterial_Shielding3 };
        int[] condensateAmounts = { 2, 4, 8 };
        long[] euts = { TierEU.RECIPE_UIV, TierEU.RECIPE_UMV, TierEU.RECIPE_UXV };

        for (int t = 0; t < 3; t++) {
            ItemStack[] inputs = {
                GTModHandler
                    .getModItem(NewHorizonsCoreMod.ID, "IrradiantReinforcedBedrockiumPlate", baseAmounts[t + 1]),
                GTOreDictUnificator.get(OrePrefixes.foil, other3Tier[t], 4), platePart(black3Tier[t], 1),
                GTOreDictUnificator.get(OrePrefixes.screw, gray3Tier[t], 8),
                GTOreDictUnificator.get(OrePrefixes.bolt, white3Tier[t], 8) };
            NaniteTier[] nanites = { nanite(4 + 2 * t), nanite(1 + 2 * t), nanite(1 + 2 * t), nanite(3 + 2 * t),
                nanite(1 + 2 * t) };

            CondensateType[] becCondensates = { CondensateType.ChromaticGlass, CondensateType.TranscendentMetal };
            int[] becAmounts = { condensateAmounts[t], condensateAmounts[t] };
            addBec(outputs[t].get(1), inputs, nanites, becCondensates, becAmounts, baseDuration, euts[t]);
        }
    }

    private void registerWaveguide() {
        // 3-tier UIV/UMV/UXV 300s. Nanites: T4 T1 T1 T3 T1, +2 per tier
        ItemList[] outputs = { ItemList.MetaMaterial_Waveguide1, ItemList.MetaMaterial_Waveguide2,
            ItemList.MetaMaterial_Waveguide3 };
        ItemList[] coils = { ItemList.UIV_Coil, ItemList.UMV_Coil, ItemList.UXV_Coil };
        int[] chromaticAmts = { 4, 6, 8 };
        int[] transcendentMetalAmts = { 0, 2, 4 };
        long[] euts = { TierEU.RECIPE_UIV, TierEU.RECIPE_UMV, TierEU.RECIPE_UXV };

        for (int t = 0; t < 3; t++) {
            ItemStack[] inputs = { coils[t].get(1L), longRodPart(other3Tier[t], 1), fineWirePart(black3Tier[t], 8),
                GTOreDictUnificator.get(OrePrefixes.foil, gray3Tier[t], 4),
                GTOreDictUnificator.get(OrePrefixes.screw, white3Tier[t], 8) };
            NaniteTier[] nanites = { nanite(4 + 2 * t), nanite(1 + 2 * t), nanite(1 + 2 * t), nanite(3 + 2 * t),
                nanite(1 + 2 * t) };
            final CondensateType[] becCondensates;
            final int[] becAmounts;
            if (t == 0) {
                becCondensates = new CondensateType[] { CondensateType.ChromaticGlass };
                becAmounts = new int[] { chromaticAmts[t] };
            } else {
                becCondensates = new CondensateType[] { CondensateType.ChromaticGlass,
                    CondensateType.TranscendentMetal };
                becAmounts = new int[] { chromaticAmts[t], transcendentMetalAmts[t] };
            }
            addBec(outputs[t].get(1), inputs, nanites, becCondensates, becAmounts, baseDuration, euts[t]);
        }
    }

    private void registerEnergyConduit() {
        // 3-tier UIV/UMV/UXV 300s. Nanites: T4 T1 T1 T3 T1, +2 per tier
        ItemList[] outputs = { ItemList.MetaMaterial_EnergyConduit1, ItemList.MetaMaterial_EnergyConduit2,
            ItemList.MetaMaterial_EnergyConduit3 };
        ItemStack[] superconductors = { GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUIV, 1),
            GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUMV, 1),
            ItemList.Thermal_Superconductor.get(1) };
        int[] chromaticAmts = { 4, 8, 16 };
        int[] transcendentMetalAmts = { 0, 2, 4 };
        long[] euts = { TierEU.RECIPE_UIV, TierEU.RECIPE_UMV, TierEU.RECIPE_UXV };

        for (int t = 0; t < 3; t++) {
            ItemStack[] inputs = { superconductors[t], rodPart(other3Tier[t], 2), foilPart(black3Tier[t], 4),
                GTOreDictUnificator.get(OrePrefixes.itemCasing, gray3Tier[t], 2),
                GTOreDictUnificator.get(OrePrefixes.screw, white3Tier[t], 8) };
            NaniteTier[] nanites = { nanite(4 + 2 * t), nanite(1 + 2 * t), nanite(1 + 2 * t), nanite(3 + 2 * t),
                nanite(1 + 2 * t) };
            final CondensateType[] becCondensates;
            final int[] becAmounts;
            if (t == 0) {
                becCondensates = new CondensateType[] { CondensateType.ChromaticGlass };
                becAmounts = new int[] { chromaticAmts[t] };
            } else {
                becCondensates = new CondensateType[] { CondensateType.ChromaticGlass,
                    CondensateType.TranscendentMetal };
                becAmounts = new int[] { chromaticAmts[t], transcendentMetalAmts[t] };
            }
            addBec(outputs[t].get(1), inputs, nanites, becCondensates, becAmounts, baseDuration, euts[t]);
        }
    }

    private void registerElectrograviticValve() {
        // 3-tier UIV/UMV/UXV 300s. Nanites: T4 T1 T1 T3 T1, +2 per tier
        ItemList[] outputs = { ItemList.MetaMaterial_ElectrograviticValve1, ItemList.MetaMaterial_ElectrograviticValve2,
            ItemList.MetaMaterial_ElectrograviticValve3 };
        int[] chromaticAmts = { 4, 8, 16 };
        int[] dimShiftedAmts = { 1, 2, 4 };
        long[] euts = { TierEU.RECIPE_UIV, TierEU.RECIPE_UMV, TierEU.RECIPE_UXV };

        for (int t = 0; t < 3; t++) {
            ItemStack[] inputs = { GTModHandler.getModItem(Railcraft.ID, "machine.eta", baseAmounts[t + 1], 8),
                GTOreDictUnificator.get(OrePrefixes.ring, other3Tier[t], 2), foilPart(black3Tier[t], 4),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, gray3Tier[t], 1),
                GTOreDictUnificator.get(OrePrefixes.screw, white3Tier[t], 8) };
            NaniteTier[] nanites = { nanite(4 + 2 * t), nanite(1 + 2 * t), nanite(1 + 2 * t), nanite(3 + 2 * t),
                nanite(1 + 2 * t) };

            CondensateType[] becCondensates = { CondensateType.ChromaticGlass,
                CondensateType.DimensionallyShiftedSuperfluid };
            int[] becAmounts = { chromaticAmts[t], dimShiftedAmts[t] };
            addBec(outputs[t].get(1), inputs, nanites, becCondensates, becAmounts, baseDuration, euts[t]);
        }
    }

    private void reg4Tier(ItemList[] outputs, ItemStack[] inputs, long[] euts, int t) {
        NaniteTier[] nanites = { nanite(4 + t), nanite(1 + t), nanite(2 + t), nanite(1 + t) };
        final CondensateType[] becCondensates;
        final int[] becAmounts;
        if (t == 0) {
            becCondensates = new CondensateType[] { CondensateType.ChromaticGlass, CondensateType.Infinity,
                CondensateType.TranscendentMetal };
            final CondensateType[] becCondensates2 = new CondensateType[] { CondensateType.ChromaticGlass,
                CondensateType.Infinity, CondensateType.BoundlessCosmicSolder };
            becAmounts = new int[] { chromatic4TierAmts[t], infinity4TierAmts[t], 2 };
            final int[] becAmounts2 = new int[] { chromatic4TierAmts[t], infinity4TierAmts[t], 1 };
            addBec(outputs[t].get(1), inputs, nanites, becCondensates, becAmounts, baseDuration, euts[t]);
            addBec(outputs[t].get(1), inputs, nanites, becCondensates2, becAmounts2, baseDuration, euts[t]);
        } else {
            becCondensates = new CondensateType[] { CondensateType.ChromaticGlass, CondensateType.Infinity,
                CondensateType.BoundlessCosmicSolder };
            becAmounts = new int[] { chromatic4TierAmts[t], infinity4TierAmts[t], solder4TierAmts[t] };
            addBec(outputs[t].get(1), inputs, nanites, becCondensates, becAmounts, baseDuration, euts[t]);
        }
    }

    private void registerWaveFocus() {
        // 4-tier UEV/UIV/UMV/UXV 300s. Nanites: T4 T1 T2 T1, +1 per tier
        ItemList[] outputs = { ItemList.MetaMaterial_WaveFocus1, ItemList.MetaMaterial_WaveFocus2,
            ItemList.MetaMaterial_WaveFocus3, ItemList.MetaMaterial_WaveFocus4 };
        long[] euts = { TierEU.RECIPE_UEV, TierEU.RECIPE_UIV, TierEU.RECIPE_UMV, TierEU.RECIPE_UXV };

        for (int t = 0; t < 4; t++) {
            ItemStack[] inputs = { ItemList.Gravitational_Lens.get(1), ringPart(black4Tier[t], 2),
                GTOreDictUnificator.get(OrePrefixes.ring, gray4Tier[t], 1),
                GTOreDictUnificator.get(OrePrefixes.screw, white4Tier[t], 2) };
            reg4Tier(outputs, inputs, euts, t);
        }
    }

    private void registerResonanceChamber() {
        // 4-tier UEV/UIV/UMV/UXV 300s. Nanites: T3 T2 T4 T2, +1 per tier
        ItemList[] outputs = { ItemList.MetaMaterial_ResonanceChamber1, ItemList.MetaMaterial_ResonanceChamber2,
            ItemList.MetaMaterial_ResonanceChamber3, ItemList.MetaMaterial_ResonanceChamber4 };
        ItemList[] emitters = { ItemList.Emitter_UHV, ItemList.Emitter_UEV, ItemList.Emitter_UIV,
            ItemList.Emitter_UMV };
        long[] euts = { TierEU.RECIPE_UEV, TierEU.RECIPE_UIV, TierEU.RECIPE_UMV, TierEU.RECIPE_UXV };

        for (int t = 0; t < 4; t++) {
            ItemStack[] inputs = { emitters[t].get(2), rodPart(black4Tier[t], 2), foilPart(gray4Tier[t], 1),
                GTOreDictUnificator.get(OrePrefixes.plate, white4Tier[t], 1) };
            reg4Tier(outputs, inputs, euts, t);
        }
    }

    private void registerSensorArray() {
        // 4-tier UEV/UIV/UMV/UXV 300s. Nanites: T4 T1 T2 T1, +1 per tier
        ItemList[] outputs = { ItemList.MetaMaterial_SensorArray1, ItemList.MetaMaterial_SensorArray2,
            ItemList.MetaMaterial_SensorArray3, ItemList.MetaMaterial_SensorArray4 };
        ItemList[] sensors = { ItemList.Sensor_UHV, ItemList.Sensor_UEV, ItemList.Sensor_UIV, ItemList.Sensor_UMV };
        long[] euts = { TierEU.RECIPE_UEV, TierEU.RECIPE_UIV, TierEU.RECIPE_UMV, TierEU.RECIPE_UXV };

        for (int t = 0; t < 4; t++) {
            ItemStack[] inputs = { sensors[t].get(2), platePart(black4Tier[t], 2),
                GTOreDictUnificator.get(OrePrefixes.itemCasing, gray4Tier[t], 1),
                GTOreDictUnificator.get(OrePrefixes.bolt, white4Tier[t], 16) };
            reg4Tier(outputs, inputs, euts, t);
        }
    }

    private void registerFieldManipulator() {
        // 4-tier UEV/UIV/UMV/UXV 300s. Nanites: T4 T3 T2 T2, +1 per tier
        ItemList[] outputs = { ItemList.MetaMaterial_FieldManipulator1, ItemList.MetaMaterial_FieldManipulator2,
            ItemList.MetaMaterial_FieldManipulator3, ItemList.MetaMaterial_FieldManipulator4 };
        ItemList[] fieldGens = { ItemList.Field_Generator_UHV, ItemList.Field_Generator_UEV,
            ItemList.Field_Generator_UIV, ItemList.Field_Generator_UMV };
        long[] euts = { TierEU.RECIPE_UEV, TierEU.RECIPE_UIV, TierEU.RECIPE_UMV, TierEU.RECIPE_UXV };

        for (int t = 0; t < 4; t++) {
            ItemStack[] inputs = { fieldGens[t].get(1), longRodPart(black4Tier[t], 2),
                GTOreDictUnificator.get(OrePrefixes.ring, gray4Tier[t], 1),
                GTOreDictUnificator.get(OrePrefixes.plateDouble, white4Tier[t], 2) };
            reg4Tier(outputs, inputs, euts, t);
        }
    }

    /// Builds and registers both the BEC Assembler variant (using entangled condensates + nanite-tier metadata)
    private void addBec(ItemStack output, ItemStack[] inputs, NaniteTier[] nanites, CondensateType[] condensates,
        int[] condensateIngots, int duration, long eut) {
        for (int i = 0; i < inputs.length; i++) {
            if (inputs[i] == null) {
                throw new IllegalStateException(
                    "BEC Meta Material recipe for " + output.getDisplayName()
                        + " has null input at index "
                        + i
                        + " — an ore prefix is missing for the material in that slot.");
            }
        }
        FluidStack[] entangled = new FluidStack[condensates.length];
        for (int i = 0; i < condensates.length; i++) {
            CondensateType condensateType = condensates[i];
            entangled[i] = condensateType.getEntangled(condensateIngots[i] * condensateType.getUnit());
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

    private static ItemStack rodPart(Object material, int amount) {
        if (material instanceof Werkstoff w) return w.get(OrePrefixes.stick, amount);
        if (material instanceof Materials m) return GTOreDictUnificator.get(OrePrefixes.stick, m, amount);
        if (material instanceof gtPlusPlus.core.material.Material m) return m.getRod(amount);
        throw new IllegalArgumentException("Unsupported material kind: " + material);
    }

    private static ItemStack longRodPart(Object material, int amount) {
        if (material instanceof Werkstoff w) return w.get(OrePrefixes.stickLong, amount);
        if (material instanceof Materials m) return GTOreDictUnificator.get(OrePrefixes.stickLong, m, amount);
        if (material instanceof gtPlusPlus.core.material.Material m) return m.getLongRod(amount);
        throw new IllegalArgumentException("Unsupported material kind: " + material);
    }
}
