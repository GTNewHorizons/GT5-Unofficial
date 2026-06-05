package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.AE2FluidCraft;
import static gregtech.api.enums.Mods.AppliedEnergistics2;
import static gregtech.api.enums.Mods.Avaritia;
import static gregtech.api.enums.Mods.ExtraUtilities;
import static gregtech.api.enums.Mods.GraviSuite;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.Mods.SuperSolarPanels;
import static gregtech.api.enums.Mods.TinkersGregworks;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.NANITE_TIERS;
import static kekztech.common.Blocks.lscLapotronicEnergyUnit;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.rwtema.extrautils.ExtraUtils;

import fox.spiteful.avaritia.compat.ticon.Tonkers;
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
import kekztech.common.TileEntities;
import tconstruct.tools.TinkerTools;
import tectech.recipe.TecTechRecipeMaps;
import tectech.thing.CustomItemList;
import vexatos.tgregworks.reference.PartTypes;
import vexatos.tgregworks.util.TGregUtils;

/// Recipes made in the BEC Condensate Assembler.
public class BECRecipes implements Runnable {

    private static final NaniteTier[] TIER_TO_NANITE = { NaniteTier.Carbon, NaniteTier.Silver, NaniteTier.Gold,
        NaniteTier.Transcendent, NaniteTier.SixPhasedCopper, NaniteTier.WhiteDwarf, NaniteTier.BlackDwarf,
        NaniteTier.Universium, NaniteTier.Eternity, NaniteTier.MagMatter };

    // The old EoH recipes were gated by the material of their bolt, following the tier of BOLT_TIER
    // This same tiering pattern is used through the 4-tier BEC Metamaterials.
    // "Bolt" is used for brevity. Non-bolt means not forming the primary gating.
    private static final int[] NON_BOLT_TIER = { 1, 1, 1, 1, 1, 1, 2, 2, 3 };
    private static final int[] BOLT_TIER = { 1, 2, 2, 2, 3, 3, 3, 4, 4 };
    private static final int[] BOLT_POS = { 0, 0, 1, 2, 0, 1, 2, 0, 1 };

    @Override
    public void run() {
        addBECCasingRecipes();
        if (TinkersGregworks.isModLoaded() && Avaritia.isModLoaded()
            && ExtraUtilities.isModLoaded()
            && AE2FluidCraft.isModLoaded()) {
            addEyeOfHarmonyRecipes();
        }
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

    private void addEyeOfHarmonyRecipes() {
        addEyeOfHarmonyCasings();
        addTimeDilation();
        addSpacetimeCompression();
        addStabilisation();
        addBec(
            CustomItemList.astralArrayFabricator.get(1),
            new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.WhiteDwarfMatter, 8),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.BlackDwarfMatter, 8),
                ItemList.EnergisedTesseract.get(32),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Eternity, 16),
                CustomItemList.SpacetimeCompressionFieldGeneratorTier8.get(64),
                CustomItemList.SpacetimeCompressionFieldGeneratorTier8.get(64),
                CustomItemList.SpacetimeCompressionFieldGeneratorTier8.get(10),
                CustomItemList.TimeAccelerationFieldGeneratorTier8.get(64),
                CustomItemList.TimeAccelerationFieldGeneratorTier8.get(64),
                CustomItemList.TimeAccelerationFieldGeneratorTier8.get(40),
                CustomItemList.StabilisationFieldGeneratorTier8.get(48),
                CustomItemList.EOH_Infinite_Energy_Casing.get(32),
                CustomItemList.EOH_Reinforced_Temporal_Casing.get(64),
                CustomItemList.EOH_Reinforced_Spatial_Casing.get(64), ItemList.Field_Generator_UMV.get(16) },
            nanites(1, 1, 3, 9, 6, 6, 6, 6, 6, 6, 7, 4, 4, 4, 10),
            new FluidStack[] { CondensateType.ChromaticGlass.getEntangled(65_536 * INGOTS),
                CondensateType.Space.getEntangled(16_384 * 64), CondensateType.Time.getEntangled(16_384 * 64),
                CondensateType.Eternity.getEntangled(8_192 * 64) },
            7200 * SECONDS,
            TierEU.RECIPE_UXV);
    }

    private void addEyeOfHarmonyCasings() {
        ItemStack largeShirabonPlate = TGregUtils.newItemStack(Materials.get("Shirabon"), PartTypes.LargePlate, 1);
        ItemStack largeInfinityPlate = new ItemStack(TinkerTools.largePlate, 1, Tonkers.infinityMetalId);
        ItemStack largeBedrockiumPlate = new ItemStack(TinkerTools.largePlate, 1, ExtraUtils.tcon_bedrock_material_id);
        ItemStack largeCosmicNeutroniumPlate = new ItemStack(TinkerTools.largePlate, 1, Tonkers.neutroniumId);

        // Reinforced Temporal Structure Casing
        addBec(
            CustomItemList.EOH_Reinforced_Temporal_Casing.get(1),
            new ItemStack[] { CustomItemList.Godforge_SingularityShieldingCasing.get(32),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.ProtoHalkonite, 32),
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Hexanite, 8),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Neutronium, 48), largeBedrockiumPlate,
                largeCosmicNeutroniumPlate, largeShirabonPlate, largeInfinityPlate,
                ItemList.Machine_UV_SolarPanel.get(1), ItemList.AcceleratorUV.get(4),
                getModItem(GraviSuite.ID, "itemSimpleItem", 64, 3), ItemList.EnergisedTesseract.get(1) },
            nanites(4, 1, 1, 2, 1, 1, 4, 1, 1, 1, 1, 2),
            new FluidStack[] { CondensateType.NEUTRONIUM.getEntangled(512 * INGOTS),
                CondensateType.COSMICNEUTRONIUM.getEntangled(512 * INGOTS),
                CondensateType.BEDROCKIUM.getEntangled(256 * INGOTS), CondensateType.Time.getEntangled(10 * INGOTS) },
            3600 * SECONDS,
            TierEU.RECIPE_UMV);

        // Reinforced Spatial Structure Casing
        addBec(
            CustomItemList.EOH_Reinforced_Spatial_Casing.get(1),
            new ItemStack[] { CustomItemList.Godforge_SingularityShieldingCasing.get(32),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.ProtoHalkonite, 32),
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Hexanite, 8),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Neutronium, 48), largeBedrockiumPlate,
                largeCosmicNeutroniumPlate, largeShirabonPlate, largeInfinityPlate,
                ItemList.Machine_UV_SolarPanel.get(1), ItemList.Quantum_Chest_IV.get(1),
                getModItem(GraviSuite.ID, "itemSimpleItem", 64, 3), ItemList.EnergisedTesseract.get(1) },
            nanites(4, 1, 1, 2, 1, 1, 4, 1, 1, 1, 1, 2),
            new FluidStack[] { CondensateType.NEUTRONIUM.getEntangled(512 * INGOTS),
                CondensateType.COSMICNEUTRONIUM.getEntangled(512 * INGOTS),
                CondensateType.BEDROCKIUM.getEntangled(256 * INGOTS), CondensateType.Space.getEntangled(10 * INGOTS) },
            3600 * SECONDS,
            TierEU.RECIPE_UMV);

        // Infinite Spacetime Boundary Casing
        addBec(
            CustomItemList.EOH_Infinite_Energy_Casing.get(1),
            new ItemStack[] { TileEntities.lsc.getStackForm(1), ItemList.Machine_UV_SolarPanel.get(1),
                new ItemStack(lscLapotronicEnergyUnit, 1, 5),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUMV, 4),
                CustomItemList.Machine_Multi_Transformer.get(16), ItemList.Wireless_Hatch_Energy_UMV.get(4),
                CustomItemList.eM_energyTunnel5_UMV.get(1),
                getModItem(NewHorizonsCoreMod.ID, "HighEnergyFlowCircuit", 64, 0),
                GTOreDictUnificator.get("plateMetastableOganesson", 6),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.BlueTopaz, 6),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.CallistoIce, 6),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Ledox, 6),
                GTOreDictUnificator.get("screwMetastableOganesson", 6),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.BlueTopaz, 6),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.CallistoIce, 6),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.Ledox, 6) },
            nanites(4, 1, 2, 1, 1, 3, 4, 3, 1, 1, 1, 1, 1, 1, 1, 1),
            new FluidStack[] { CondensateType.NEUTRONIUM.getEntangled(1024 * INGOTS),
                CondensateType.COSMICNEUTRONIUM.getEntangled(1024 * INGOTS),
                CondensateType.BEDROCKIUM.getEntangled(256 * INGOTS),
                CondensateType.SpaceTime.getEntangled(128 * INGOTS) },
            3600 * SECONDS,
            TierEU.RECIPE_UMV);
    }

    private void addTimeDilation() {
        ItemList[] energyConduit = { ItemList.MetaMaterial_EnergyConduit1, ItemList.MetaMaterial_EnergyConduit2,
            ItemList.MetaMaterial_EnergyConduit3 };
        ItemList[] electrograviticValve = { ItemList.MetaMaterial_ElectrograviticValve1,
            ItemList.MetaMaterial_ElectrograviticValve2, ItemList.MetaMaterial_ElectrograviticValve3 };
        ItemList[] waveguide = { ItemList.MetaMaterial_Waveguide1, ItemList.MetaMaterial_Waveguide2,
            ItemList.MetaMaterial_Waveguide3 };
        ItemList[] waveFocus = { ItemList.MetaMaterial_WaveFocus1, ItemList.MetaMaterial_WaveFocus2,
            ItemList.MetaMaterial_WaveFocus3, ItemList.MetaMaterial_WaveFocus4 };
        CustomItemList[] outputs = { CustomItemList.TimeAccelerationFieldGeneratorTier0,
            CustomItemList.TimeAccelerationFieldGeneratorTier1, CustomItemList.TimeAccelerationFieldGeneratorTier2,
            CustomItemList.TimeAccelerationFieldGeneratorTier3, CustomItemList.TimeAccelerationFieldGeneratorTier4,
            CustomItemList.TimeAccelerationFieldGeneratorTier5, CustomItemList.TimeAccelerationFieldGeneratorTier6,
            CustomItemList.TimeAccelerationFieldGeneratorTier7, CustomItemList.TimeAccelerationFieldGeneratorTier8 };
        int[] focusQty = { 1, 4, 16 };

        for (int t = 0; t < 9; t++) {
            int tp1 = t + 1;
            ItemStack[] inputs = { CustomItemList.EOH_Reinforced_Temporal_Casing.get(1),
                ItemList.Machine_UV_SolarPanel.get(tp1), ItemList.AcceleratorUV.get(4L * tp1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, tp1), ItemList.Energy_Cluster.get(tp1),
                getModItem(SuperSolarPanels.ID, "redcomponent", 64),
                getModItem(SuperSolarPanels.ID, "greencomponent", 64),
                getModItem(SuperSolarPanels.ID, "bluecomponent", 64), energyConduit[NON_BOLT_TIER[t] - 1].get(4L * tp1),
                waveFocus[BOLT_TIER[t] - 1].get(focusQty[BOLT_POS[t]]),
                electrograviticValve[NON_BOLT_TIER[t] - 1].get(2L * tp1), waveguide[NON_BOLT_TIER[t] - 1].get(2L * tp1),
                CustomItemList.Godforge_GravitonFlowModulatorTier2.get(2L * tp1),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.Hexanite, 4L * tp1),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Hexanite, tp1),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Hexanite, tp1) };
            NaniteTier[] nanites = nanitesShifted(BOLT_TIER[t] - 1, 2, 2, 2, 2, 2, 1, 1, 1, 3, 4, 3, 3, 4, 1, 1, 1);
            FluidStack[] condensates = { cosmicSolder(t).getEntangled(10 * tp1 * 1000),
                CondensateType.Time.getEntangled(10 * tp1 * INGOTS), CondensateType.SpaceTime.getEntangled(10 * INGOTS),
                CondensateType.DimensionallyShiftedSuperfluid.getEntangled(20 * tp1 * INGOTS) };
            addBec(outputs[t].get(1), inputs, nanites, condensates, 3600 * SECONDS, TierEU.RECIPE_UMV);
        }
    }

    private void addSpacetimeCompression() {
        ItemList[] shielding = { ItemList.MetaMaterial_Shielding1, ItemList.MetaMaterial_Shielding2,
            ItemList.MetaMaterial_Shielding3 };
        ItemList[] resonanceChamber = { ItemList.MetaMaterial_ResonanceChamber1,
            ItemList.MetaMaterial_ResonanceChamber2, ItemList.MetaMaterial_ResonanceChamber3,
            ItemList.MetaMaterial_ResonanceChamber4 };
        GregtechItemList[] manipulators = { GregtechItemList.CosmicFabricManipulator,
            GregtechItemList.InfinityInfusedManipulator, GregtechItemList.SpaceTimeContinuumRipper };
        CustomItemList[] outputs = { CustomItemList.SpacetimeCompressionFieldGeneratorTier0,
            CustomItemList.SpacetimeCompressionFieldGeneratorTier1,
            CustomItemList.SpacetimeCompressionFieldGeneratorTier2,
            CustomItemList.SpacetimeCompressionFieldGeneratorTier3,
            CustomItemList.SpacetimeCompressionFieldGeneratorTier4,
            CustomItemList.SpacetimeCompressionFieldGeneratorTier5,
            CustomItemList.SpacetimeCompressionFieldGeneratorTier6,
            CustomItemList.SpacetimeCompressionFieldGeneratorTier7,
            CustomItemList.SpacetimeCompressionFieldGeneratorTier8 };
        int[] resonanceQty = { 1, 4, 16 };

        for (int t = 0; t < 9; t++) {
            int tp1 = t + 1;
            ItemStack[] inputs = { CustomItemList.EOH_Reinforced_Spatial_Casing.get(1),
                ItemList.Machine_Multi_BlackHoleCompressor.get(tp1),
                getModItem(AppliedEnergistics2.ID, "item.ItemExtremeStorageCell.Singularity", 1),
                getModItem(AE2FluidCraft.ID, "fluid_storage.singularity", 1), manipulators[t / 3].get(t % 3 + 1),
                shielding[NON_BOLT_TIER[t] - 1].get(4L * tp1),
                resonanceChamber[BOLT_TIER[t] - 1].get(resonanceQty[BOLT_POS[t]]),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, tp1),
                Casings.SingularityReinforcedStellarShieldingCasing.toStack(4 * tp1),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Hexanite, tp1),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Hexanite, 2L * tp1),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Hexanite, tp1),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.ProtoHalkonite, tp1),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.CosmicNeutronium, tp1),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Infinity, tp1),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.TranscendentMetal, tp1) };
            NaniteTier[] nanites = nanitesShifted(BOLT_TIER[t] - 1, 3, 3, 2, 4, 2, 2, 4, 3, 2, 2, 1, 1, 1, 1, 1, 1);
            FluidStack[] condensates = { cosmicSolder(t).getEntangled(10 * tp1 * 1000),
                CondensateType.Space.getEntangled(10 * tp1 * INGOTS),
                CondensateType.SpaceTime.getEntangled(10 * INGOTS),
                CondensateType.Hypogen.getEntangled(20 * tp1 * INGOTS) };
            addBec(outputs[t].get(1), inputs, nanites, condensates, 3600 * SECONDS, TierEU.RECIPE_UMV);
        }
    }

    private void addStabilisation() {
        ItemList[] sensorArray = { ItemList.MetaMaterial_SensorArray1, ItemList.MetaMaterial_SensorArray2,
            ItemList.MetaMaterial_SensorArray3, ItemList.MetaMaterial_SensorArray4 };
        ItemList[] fieldManipulator = { ItemList.MetaMaterial_FieldManipulator1,
            ItemList.MetaMaterial_FieldManipulator2, ItemList.MetaMaterial_FieldManipulator3,
            ItemList.MetaMaterial_FieldManipulator4 };
        CustomItemList[] timeGens = { CustomItemList.TimeAccelerationFieldGeneratorTier0,
            CustomItemList.TimeAccelerationFieldGeneratorTier1, CustomItemList.TimeAccelerationFieldGeneratorTier2,
            CustomItemList.TimeAccelerationFieldGeneratorTier3, CustomItemList.TimeAccelerationFieldGeneratorTier4,
            CustomItemList.TimeAccelerationFieldGeneratorTier5, CustomItemList.TimeAccelerationFieldGeneratorTier6,
            CustomItemList.TimeAccelerationFieldGeneratorTier7, CustomItemList.TimeAccelerationFieldGeneratorTier8 };
        CustomItemList[] spaceGens = { CustomItemList.SpacetimeCompressionFieldGeneratorTier0,
            CustomItemList.SpacetimeCompressionFieldGeneratorTier1,
            CustomItemList.SpacetimeCompressionFieldGeneratorTier2,
            CustomItemList.SpacetimeCompressionFieldGeneratorTier3,
            CustomItemList.SpacetimeCompressionFieldGeneratorTier4,
            CustomItemList.SpacetimeCompressionFieldGeneratorTier5,
            CustomItemList.SpacetimeCompressionFieldGeneratorTier6,
            CustomItemList.SpacetimeCompressionFieldGeneratorTier7,
            CustomItemList.SpacetimeCompressionFieldGeneratorTier8 };
        CustomItemList[] outputs = { CustomItemList.StabilisationFieldGeneratorTier0,
            CustomItemList.StabilisationFieldGeneratorTier1, CustomItemList.StabilisationFieldGeneratorTier2,
            CustomItemList.StabilisationFieldGeneratorTier3, CustomItemList.StabilisationFieldGeneratorTier4,
            CustomItemList.StabilisationFieldGeneratorTier5, CustomItemList.StabilisationFieldGeneratorTier6,
            CustomItemList.StabilisationFieldGeneratorTier7, CustomItemList.StabilisationFieldGeneratorTier8 };
        // Sensor arrays follow the field-manipulator tiering, but one tier down, with their own quantity banding.
        // This is mainly for achieving cost parity with the pre-BEC recipes.
        int[] sensorTier = { 1, 1, 2, 2, 2, 3, 3, 3, 4 };
        int[] sensorQty = { 1, 1, 1, 1, 4, 1, 1, 4, 1 };
        int[] manipulatorQty = { 1, 2, 8 };

        for (int t = 0; t < 9; t++) {
            int tp1 = t + 1;
            ItemStack[] inputs = { timeGens[t].get(1), spaceGens[t].get(1),
                CustomItemList.EOH_Infinite_Energy_Casing.get(1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, tp1),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SuperconductorUHVBase, 4L * tp1),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SuperconductorUEVBase, 4L * tp1),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SuperconductorUIVBase, 4L * tp1),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SuperconductorUMVBase, 4L * tp1),
                ItemList.Field_Generator_UV.get(2L * tp1), ItemList.Field_Generator_UHV.get(2L * tp1),
                ItemList.Field_Generator_UEV.get(2L * tp1), ItemList.Field_Generator_UIV.get(2L * tp1),
                sensorArray[sensorTier[t] - 1].get(sensorQty[t]),
                fieldManipulator[BOLT_TIER[t] - 1].get(manipulatorQty[BOLT_POS[t]]),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.Hexanite, 4L * tp1),
                GTOreDictUnificator.get(OrePrefixes.bolt, Materials.Hexanite, 4L * tp1) };
            NaniteTier[] nanites = nanitesShifted(BOLT_TIER[t] - 1, 3, 3, 3, 2, 1, 1, 1, 1, 2, 2, 2, 2, 4, 4, 1, 1);
            FluidStack[] condensates = { cosmicSolder(t).getEntangled(10 * tp1 * 1000),
                CondensateType.Time.getEntangled(10 * tp1 * INGOTS),
                CondensateType.Space.getEntangled(10 * tp1 * INGOTS),
                CondensateType.SpaceTime.getEntangled(10 * INGOTS) };
            addBec(outputs[t].get(1), inputs, nanites, condensates, 3600 * SECONDS, TierEU.RECIPE_UMV);
        }
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

    /// Cosmic solder condensate: transcendent metal at tier 0, boundless cosmic solder thereafter.
    private static CondensateType cosmicSolder(int tier) {
        return tier == 0 ? CondensateType.TranscendentMetal : CondensateType.BoundlessCosmicSolder;
    }

    private static NaniteTier[] nanites(int... tiers) {
        return nanitesShifted(0, tiers);
    }

    private static NaniteTier[] nanitesShifted(int shift, int... tiers) {
        NaniteTier[] result = new NaniteTier[tiers.length];
        for (int i = 0; i < tiers.length; i++) {
            result[i] = TIER_TO_NANITE[tiers[i] - 1 + shift];
        }
        return result;
    }
}
