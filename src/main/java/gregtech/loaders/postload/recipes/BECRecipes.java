package gregtech.loaders.postload.recipes;

import static goodgenerator.util.ItemRefer.Compassline_Casing_UMV;
import static goodgenerator.util.ItemRefer.Compassline_Casing_UXV;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.NANITE_TIERS;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import goodgenerator.items.GGMaterial;
import goodgenerator.util.ItemRefer;
import gregtech.api.casing.Casings;
import gregtech.api.enums.CondensateType;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.NaniteTier;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import tectech.recipe.TecTechRecipeMaps;
import tectech.thing.CustomItemList;

/// Recipes made in the BEC Condensate Assembler.
public class BECRecipes implements Runnable {

    private static final NaniteTier[] TIER_TO_NANITE = { NaniteTier.Carbon, NaniteTier.Silver, NaniteTier.Gold,
        NaniteTier.Transcendent, NaniteTier.SixPhasedCopper, NaniteTier.WhiteDwarf, NaniteTier.BlackDwarf,
        NaniteTier.Universium, NaniteTier.Eternity, NaniteTier.MagMatter };

    @Override
    public void run() {
        addBECCasingRecipes();

        // Transdimensional Alignment Matrix (Convergence)
        addBec(
            ItemList.Transdimensional_Alignment_Matrix.get(1),
            new ItemStack[] { CustomItemList.EOH_Infinite_Energy_Casing.get(1),
                GregtechItemList.SpaceTimeContinuumRipper.get(4), ItemList.Robot_Arm_UMV.get(64),
                ItemList.Sensor_UMV.get(16), ItemList.Field_Generator_UMV.get(4), ItemList.ZPM5.get(1),
                ItemList.EnergisedTesseract.get(32),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.TranscendentMetal, 16),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Flerovium, 64),
                GGMaterial.metastableOganesson.get(OrePrefixes.plateDense, 32),
                ItemList.MetaMaterial_SensorArray1.get(32), ItemList.MetaMaterial_FieldManipulator1.get(32) },
            nanites(1, 2, 4, 4, 4, 3, 2, 4, 1, 1, 2, 3),
            new FluidStack[] { CondensateType.SpaceTime.getEntangled(192 * INGOTS),
                CondensateType.DimensionallyShiftedSuperfluid.getEntangled(10_000),
                CondensateType.Space.getEntangled(32 * INGOTS) },
            600 * SECONDS,
            TierEU.RECIPE_UMV);

        // UMV Component Assembly Line Casing (UMV CoAL)
        addBec(
            Compassline_Casing_UMV.get(1),
            new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SpaceTime, 1),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.SpaceTime, 1),
                ItemList.Robot_Arm_UMV.get(8), ItemList.Electric_Piston_UMV.get(10),
                ItemList.Electric_Motor_UMV.get(16),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.SpaceTime, 4),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.SpaceTime, 16),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Quantium, 8),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UMV, 8),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UIV, 16),
                ItemList.MetaMaterial_Shielding1.get(64), ItemList.MetaMaterial_EnergyConduit1.get(64) },
            nanites(1, 1, 3, 3, 3, 2, 2, 1, 4, 4, 2, 2),
            new FluidStack[] { CondensateType.TranscendentMetal.getEntangled(32 * INGOTS),
                CondensateType.Hypogen.getEntangled(24 * INGOTS), CondensateType.SpaceTime.getEntangled(12 * INGOTS),
                CondensateType.DimensionallyShiftedSuperfluid.getEntangled(10_000) },
            600 * SECONDS,
            TierEU.RECIPE_UMV);

        // UXV Component Assembly Line Casing (UXV CoAL)
        addBec(
            Compassline_Casing_UXV.get(1),
            new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.MHDCSM, 1),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.MHDCSM, 3),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.MagMatter, 3), ItemList.Robot_Arm_UXV.get(8),
                ItemList.Electric_Piston_UXV.get(10), ItemList.Electric_Motor_UXV.get(16),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.MHDCSM, 2),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.MagMatter, 2),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.MHDCSM, 8),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.MagMatter, 8),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SpaceTime, 8),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 8),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UMV, 16),
                ItemList.MetaMaterial_Shielding3.get(64), ItemList.MetaMaterial_EnergyConduit3.get(64),
                ItemList.MetaMaterial_Waveguide3.get(64) },
            nanites(1, 3, 3, 7, 6, 6, 5, 5, 4, 4, 2, 5, 5, 6, 8, 9),
            new FluidStack[] { CondensateType.BoundlessCosmicSolder.getEntangled(32 * INGOTS),
                CondensateType.MHDCSM.getEntangled(8 * INGOTS), CondensateType.Eternity.getEntangled(13 * INGOTS),
                CondensateType.DimensionallyShiftedSuperfluid.getEntangled(11_000) },
            600 * SECONDS,
            TierEU.RECIPE_UMV);
    }

    private void addBECCasingRecipes() {
        // Electromagnetically-isolated Casing
        addBec(
            ItemList.ElectromagneticallyIsolatedCasing.get(8),
            new ItemStack[] { ItemList.BlockQuarkContainmentCasing.get(8),
                GGMaterial.tairitsu.get(OrePrefixes.frameGt, 8),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Churitsu, 8),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Shijima, 8),
                ItemList.MetaMaterial_Shielding1.get(8),
                GTOreDictUnificator.get(OrePrefixes.bolt, Materials.SixPhasedCopper, 32),
                GGMaterial.tairitsu.get(OrePrefixes.ring, 16), MaterialsElements.STANDALONE.HYPOGEN.getScrew(32),
                ItemList.Field_Generator_UEV.get(1) },
            nanites(4, 1, 1, 1, 3, 1, 2, 1, 2),
            new FluidStack[] { CondensateType.ChromaticGlass.getEntangled(512 * INGOTS),
                CondensateType.Infinity.getEntangled(64 * INGOTS),
                CondensateType.CelestialTungsten.getEntangled(64 * INGOTS) },
            1200 * SECONDS,
            TierEU.RECIPE_UIV);

        // Coherence-preserving Plasma Conduit
        addBec(
            ItemList.SuperconductivePlasmaEnergyConduit.get(8),
            new ItemStack[] { ItemList.PeaceEnforcementCasing.get(8), Casings.ParticleBeamGuidancePipeCasing.toStack(8),
                GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.TranscendentMetal, 8),
                ItemList.Electromagnet_Tengam.get(8), ItemList.MetaMaterial_EnergyConduit1.get(8),
                GTOreDictUnificator.get(OrePrefixes.rotor, Materials.SixPhasedCopper, 1),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Churitsu, 4),
                MaterialsElements.STANDALONE.HYPOGEN.getRotor(1), ItemList.Electric_Pump_UIV.get(2) },
            nanites(2, 4, 1, 2, 1, 1, 1, 1, 3),
            new FluidStack[] { CondensateType.ChromaticGlass.getEntangled(256 * INGOTS),
                CondensateType.Infinity.getEntangled(32 * INGOTS),
                CondensateType.CelestialTungsten.getEntangled(32 * INGOTS) },
            1200 * SECONDS,
            TierEU.RECIPE_UIV);

        // Fine-structure Constant Manipulator
        addBec(
            ItemList.FineStructureConstantManipulator.get(4),
            new ItemStack[] { ItemList.ConflictInducementCasing.get(4),
                ItemList.ElectromagneticallyIsolatedCasing.get(1),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.SixPhasedCopper, 2),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Shijima, 1),
                MaterialsElements.STANDALONE.HYPOGEN.getGearSmall(2), ItemList.MetaMaterial_Waveguide1.get(4),
                ItemList.MetaMaterial_ElectrograviticValve1.get(4), ItemList.Sensor_UIV.get(1),
                ItemList.Emitter_UIV.get(1) },
            nanites(3, 4, 1, 1, 1, 1, 1, 3, 2),
            new FluidStack[] { CondensateType.ChromaticGlass.getEntangled(256 * INGOTS),
                CondensateType.Infinity.getEntangled(32 * INGOTS),
                CondensateType.CelestialTungsten.getEntangled(32 * INGOTS) },
            600 * SECONDS,
            TierEU.RECIPE_UIV);

        // Condensate Guidance Coil
        addBec(
            ItemList.CondensateGuidanceCoil.get(1),
            new ItemStack[] { ItemRefer.Field_Restriction_Coil_T3.get(1),
                ItemList.Naquarite_Universal_Insulator_Foil.get(4), ItemList.MetaMaterial_Waveguide1.get(2),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Churitsu, 1),
                GGMaterial.tairitsu.get(OrePrefixes.screw, 16),
                GTOreDictUnificator.get(OrePrefixes.bolt, Materials.Shijima, 16) },
            nanites(3, 1, 4, 1, 1, 1),
            new FluidStack[] { CondensateType.ChromaticGlass.getEntangled(64 * INGOTS),
                CondensateType.Infinity.getEntangled(8 * INGOTS),
                CondensateType.CelestialTungsten.getEntangled(8 * INGOTS) },
            150 * SECONDS,
            TierEU.RECIPE_UIV);

        // Condensate Transformative Coil
        addBec(
            ItemList.CondensateTransformativeCoil.get(1),
            new ItemStack[] { ItemList.CondensateGuidanceCoil.get(1),
                GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUIV, 4),
                ItemList.Circuit_Chip_APIC.get(4), ItemList.MetaMaterial_SensorArray1.get(4),
                ItemList.MetaMaterial_FieldManipulator1.get(2) },
            nanites(3, 1, 4, 1, 1),
            new FluidStack[] { CondensateType.ChromaticGlass.getEntangled(64 * INGOTS),
                CondensateType.Infinity.getEntangled(8 * INGOTS),
                CondensateType.CelestialTungsten.getEntangled(8 * INGOTS) },
            300 * SECONDS,
            TierEU.RECIPE_UIV);

        // Electromagnetic Waveguide
        addBec(
            ItemList.ElectromagneticWaveguide.get(4),
            new ItemStack[] { ItemRefer.AntimatterContainmentCasing.get(4), ItemList.MetaMaterial_Waveguide1.get(4),
                GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.Churitsu, 4),
                ItemList.Field_Generator_UEV.get(1) },
            nanites(3, 4, 1, 1),
            new FluidStack[] { CondensateType.ChromaticGlass.getEntangled(512 * INGOTS),
                CondensateType.Infinity.getEntangled(128 * INGOTS),
                CondensateType.CelestialTungsten.getEntangled(32 * INGOTS),
                CondensateType.SpaceTime.getEntangled(16 * INGOTS) },
            240 * SECONDS,
            TierEU.RECIPE_UIV);
    }

    private void addBec(ItemStack output, ItemStack[] inputs, NaniteTier[] nanites, FluidStack[] condensates,
        int duration, long eut) {
        GTValues.RA.stdBuilder()
            .itemInputs(inputs)
            .fluidInputs(condensates)
            .itemOutputs(output)
            .metadata(NANITE_TIERS, nanites)
            .duration(duration)
            .eut(eut)
            .addTo(TecTechRecipeMaps.condensateAssemblingRecipes);
    }

    private static NaniteTier[] nanites(int... tiers) {
        NaniteTier[] result = new NaniteTier[tiers.length];
        for (int i = 0; i < tiers.length; i++) {
            result[i] = TIER_TO_NANITE[tiers[i] - 1];
        }
        return result;
    }
}
