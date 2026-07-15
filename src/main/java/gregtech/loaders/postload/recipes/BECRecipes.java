package gregtech.loaders.postload.recipes;

import static goodgenerator.util.ItemRefer.Compassline_Casing_UMV;
import static goodgenerator.util.ItemRefer.Compassline_Casing_UXV;
import static gregtech.api.enums.Mods.AE2FluidCraft;
import static gregtech.api.enums.Mods.AppliedEnergistics2;
import static gregtech.api.enums.Mods.Avaritia;
import static gregtech.api.enums.Mods.EternalSingularity;
import static gregtech.api.enums.Mods.ExtraUtilities;
import static gregtech.api.enums.Mods.GalacticraftAmunRa;
import static gregtech.api.enums.Mods.GraviSuite;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.Mods.SuperSolarPanels;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.STACKS;
import static gregtech.api.util.GTRecipeConstants.NANITE_TIERS;
import static kekztech.common.Blocks.lscLapotronicEnergyUnit;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.common.loaders.ItemRegistry;
import bartworks.system.material.WerkstoffLoader;
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
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import kekztech.common.Blocks;
import kekztech.common.TileEntities;
import tectech.recipe.TecTechRecipeMaps;
import tectech.thing.CustomItemList;
import tectech.thing.block.BlockGodforgeGlass;
import tectech.thing.block.BlockQuantumGlass;

/// Recipes made in the BEC Condensate Assembler.
public class BECRecipes implements Runnable {

    private static final NaniteTier[] TIER_TO_NANITE = { NaniteTier.Carbon, NaniteTier.Silver, NaniteTier.Gold,
        NaniteTier.Transcendent, NaniteTier.SixPhasedCopper, NaniteTier.WhiteDwarf, NaniteTier.BlackDwarf,
        NaniteTier.Universium, NaniteTier.Eternity, NaniteTier.MagMatter };

    // The old EoH recipes were gated by the material of their bolt, following the tier of BOLT_TIER
    // This same tiering pattern is used through the 4-tier BEC Metamaterials.
    // "Bolt" is used for brevity.
    private static final int[] BOLT_TIER = { 1, 2, 2, 2, 3, 3, 3, 4, 4 };
    private static final int[] BOLT_POS = { 0, 0, 1, 2, 0, 1, 2, 0, 1 };
    // Materials used to differentiate each tier of EoH part
    private static final Materials[] TIER_MATS = { Materials.Netherite, Materials.ProtoHalkonite,
        Materials.SixPhasedCopper, Materials.TranscendentMetal, Materials.Mellion, Materials.Creon, Materials.SpaceTime,
        Materials.Hexanite, Materials.Eternity };

    @Override
    public void run() {
        addBECCasingRecipes();
        if (NewHorizonsCoreMod.isModLoaded()) addGodforgeRecipes();
        if (Avaritia.isModLoaded() && ExtraUtilities.isModLoaded() && AE2FluidCraft.isModLoaded()) {
            addEyeOfHarmonyRecipes();
        }

        // Transdimensional Alignment Matrix (Convergence)
        addBec(
            ItemList.Transdimensional_Alignment_Matrix.get(1),
            new ItemStack[] { CustomItemList.EOH_Infinite_Energy_Casing.get(1),
                GregtechItemList.SpaceTimeContinuumRipper.get(4), ItemList.Robot_Arm_UMV.get(64),
                ItemList.Sensor_UMV.get(16), ItemList.Field_Generator_UMV.get(4), ItemList.ZPM5.get(1),
                ItemList.EnergisedTesseract.get(32), GTOreDictUnificator.get("naniteTranscendentMetal", 16),
                GTOreDictUnificator.get("plateDenseFlerovium_GT5U", 64),
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
            new ItemStack[] { GTOreDictUnificator.get("frameGtSpaceTime", 1),
                MaterialLibAPI.getStack(Materials2Materials.SpaceTime, Materials2Shapes.plateSuperdense, (int) (1)),
                ItemList.Robot_Arm_UMV.get(8), ItemList.Electric_Piston_UMV.get(10),
                ItemList.Electric_Motor_UMV.get(16),
                MaterialLibAPI.getStack(Materials2Materials.SpaceTime, Materials2Shapes.gearGt, (int) (4)),
                MaterialLibAPI.getStack(Materials2Materials.SpaceTime, Materials2Shapes.gearGtSmall, (int) (16)),
                GTOreDictUnificator.get("cableGt04Quantium", 8), GTOreDictUnificator.get("circuitExotic", 8),
                GTOreDictUnificator.get("circuitOptical", 16), ItemList.MetaMaterial_Shielding1.get(64),
                ItemList.MetaMaterial_EnergyConduit1.get(64) },
            nanites(1, 1, 3, 3, 3, 2, 2, 1, 4, 4, 2, 2),
            new FluidStack[] { CondensateType.TranscendentMetal.getEntangled(32 * INGOTS),
                CondensateType.Hypogen.getEntangled(24 * INGOTS), CondensateType.SpaceTime.getEntangled(12 * INGOTS),
                CondensateType.DimensionallyShiftedSuperfluid.getEntangled(10_000) },
            600 * SECONDS,
            TierEU.RECIPE_UMV);

        // UXV Component Assembly Line Casing (UXV CoAL)
        addBec(
            Compassline_Casing_UXV.get(1),
            new ItemStack[] { GTOreDictUnificator.get("frameGtMagnetohydrodynamicallyConstrainedStarMatter", 1),
                GTOreDictUnificator.get("plateDenseMagnetohydrodynamicallyConstrainedStarMatter", 3),
                GTOreDictUnificator.get("plateDenseMagmatter", 3), ItemList.Robot_Arm_UXV.get(8),
                ItemList.Electric_Piston_UXV.get(10), ItemList.Electric_Motor_UXV.get(16),
                GTOreDictUnificator.get("gearGtMagnetohydrodynamicallyConstrainedStarMatter", 2),
                GTOreDictUnificator.get("gearGtMagmatter", 2),
                GTOreDictUnificator.get("gearGtSmallMagnetohydrodynamicallyConstrainedStarMatter", 8),
                GTOreDictUnificator.get("gearGtSmallMagmatter", 8), GTOreDictUnificator.get("wireGt04SpaceTime", 8),
                GTOreDictUnificator.get("circuitCosmic", 8), GTOreDictUnificator.get("circuitExotic", 16),
                ItemList.MetaMaterial_Shielding3.get(64), ItemList.MetaMaterial_EnergyConduit3.get(64),
                ItemList.MetaMaterial_Waveguide3.get(64) },
            nanites(1, 3, 3, 7, 6, 6, 5, 5, 4, 4, 2, 5, 5, 6, 8, 9),
            new FluidStack[] { CondensateType.BoundlessCosmicSolder.getEntangled(5_000),
                CondensateType.MHDCSM.getEntangled(8 * INGOTS), CondensateType.Eternity.getEntangled(13 * INGOTS),
                CondensateType.DimensionallyShiftedSuperfluid.getEntangled(11_000) },
            600 * SECONDS,
            TierEU.RECIPE_UMV);
    }

    private void addGodforgeRecipes() {
        // Magnetic Confinement Casing
        addBec(
            CustomItemList.Godforge_MagneticConfinementCasing.get(12),
            new ItemStack[] { GTOreDictUnificator.get("frameGtTranscendentMetal", 12),
                WerkstoffLoader.MagnetoResonaticDust.get(OrePrefixes.block, 16),
                MaterialLibAPI.getStack(Materials2Materials.TengamAttuned, Materials2Shapes.plateDense, (int) (32)),
                MaterialLibAPI.getStack(Materials2Materials.Creon, Materials2Shapes.plate, (int) (16)),
                MaterialsElements.STANDALONE.HYPOGEN.getScrew(8),
                MaterialLibAPI.getStack(Materials2Materials.SixPhasedCopper, Materials2Shapes.screw, (int) (8)),
                ItemList.SuperconductorComposite.get(1), ItemList.Emitter_UIV.get(2),
                ItemList.Electromagnet_Tengam.get(1) },
            nanites(1, 2, 1, 1, 1, 1, 2, 3, 4),
            new FluidStack[] { CondensateType.Hypogen.getEntangled(4 * INGOTS),
                CondensateType.CelestialTungsten.getEntangled(32 * INGOTS) },
            1800 * SECONDS,
            TierEU.RECIPE_UIV);

        if (GalacticraftAmunRa.isModLoaded()) {
            // Structure Casing
            addBec(
                CustomItemList.Godforge_BoundlessStructureCasing.get(2),
                new ItemStack[] { GTOreDictUnificator.get("frameGtMellion", 24),
                    GTOreDictUnificator.get("frameGtSixPhasedCopper", 24),
                    GTOreDictUnificator.get("frameGtTranscendentMetal", 12),
                    MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getFrameBox(12),
                    MaterialLibAPI.getStack(Materials2Materials.Creon, Materials2Shapes.plate, (int) (9)),
                    ItemList.StableBosonContainmentUnit.get(2), ItemList.Field_Generator_UEV.get(3),
                    // Artificial Gravity Generator
                    getModItem(GalacticraftAmunRa.ID, "tile.machines3", 6L, 1) },
                nanites(1, 1, 1, 1, 2, 2, 4, 3),
                new FluidStack[] { CondensateType.Hypogen.getEntangled(4 * INGOTS),
                    CondensateType.Neutronium.getEntangled(32 * INGOTS) },
                300 * SECONDS,
                TierEU.RECIPE_UIV);
        }

        // Guidance Casing
        addBec(
            CustomItemList.Godforge_GuidanceCasing.get(2),
            new ItemStack[] { CustomItemList.Godforge_BoundlessStructureCasing.get(2), ItemList.ZPM2.get(2),
                GregtechItemList.CosmicFabricManipulator.get(2), ItemList.Field_Generator_UEV.get(3),
                ItemList.Emitter_UIV.get(4),
                MaterialLibAPI.getStack(Materials2Materials.Creon, Materials2Shapes.plate, (int) (9)),
                MaterialLibAPI.getStack(Materials2Materials.Creon, Materials2Shapes.gearGt, (int) (12)),
                MaterialLibAPI.getStack(Materials2Materials.Mellion, Materials2Shapes.gearGtSmall, (int) (12)) },
            nanites(1, 2, 2, 4, 3, 1, 1, 1),
            new FluidStack[] { CondensateType.Hypogen.getEntangled(4 * INGOTS),
                CondensateType.CosmicNeutronium.getEntangled(32 * INGOTS) },
            300 * SECONDS,
            TierEU.RECIPE_UIV);

        // Gravitational Lens
        addBec(
            new ItemStack(BlockGodforgeGlass.INSTANCE, 2),
            new ItemStack[] { new ItemStack(BlockQuantumGlass.INSTANCE, 12),
                new ItemStack(ItemRegistry.bw_glasses[0], 12, 8), GregtechItemList.ForceFieldGlass.get(12),
                ItemList.StableBosonContainmentUnit.get(6), getModItem(NewHorizonsCoreMod.ID, "RadoxPolymerLens", 9),
                getModItem(NewHorizonsCoreMod.ID, "ChromaticLens", 9),
                getModItem(NewHorizonsCoreMod.ID, "MysteriousCrystalLens", 9),
                WerkstoffLoader.MagnetoResonaticDust.get(OrePrefixes.lens, 9),
                MaterialsElements.STANDALONE.RHUGNOR.getPlate(24),
                MaterialLibAPI.getStack(Materials2Materials.Creon, Materials2Shapes.stickLong, (int) (33)),
                MaterialLibAPI.getStack(Materials2Materials.Mellion, Materials2Shapes.stickLong, (int) (9)),
                MaterialLibAPI.getStack(Materials2Materials.SixPhasedCopper, Materials2Shapes.stickLong, (int) (9)),
                MaterialsElements.STANDALONE.ADVANCED_NITINOL.getPlateSuperdense(24) },
            nanites(3, 3, 2, 4, 1, 1, 1, 1, 1, 1, 1, 1, 2),
            new FluidStack[] { CondensateType.ChromaticGlass.getEntangled(243 * INGOTS) },
            300 * SECONDS,
            TierEU.RECIPE_UIV);

        // Graviton Modulator 1
        addBec(
            CustomItemList.Godforge_GravitonFlowModulatorTier1.get(3),
            new ItemStack[] { CustomItemList.Godforge_MagneticConfinementCasing.get(3),
                ItemRefer.Field_Restriction_Coil_T3.get(1),
                MaterialLibAPI.getStack(Materials2Materials.Creon, Materials2Shapes.plate, (int) (16)),
                MaterialLibAPI.getStack(Materials2Materials.Mellion, Materials2Shapes.gearGtSmall, (int) (8)),
                GregtechItemList.Battery_Gem_4.get(2), GregtechItemList.Laser_Lens_Special.get(4),
                ItemList.Emitter_UIV.get(4), GTOreDictUnificator.get("circuitBio", 16),
                GTOreDictUnificator.get("naniteSilver", 2),
                MaterialLibAPI
                    .getStack(Materials2Materials.SuperconductorUIVBase, Materials2Shapes.plate, (int) (32)) },
            nanites(1, 2, 1, 1, 1, 1, 3, 4, 2, 3),
            new FluidStack[] { CondensateType.ChromaticGlass.getEntangled(32 * INGOTS),
                CondensateType.Infinity.getEntangled(32 * INGOTS) },
            450 * SECONDS,
            TierEU.RECIPE_UIV);

        // Graviton Modulator 2
        addBec(
            CustomItemList.Godforge_GravitonFlowModulatorTier2.get(1),
            new ItemStack[] { CustomItemList.Godforge_MagneticConfinementCasing.get(1),
                ItemRefer.Field_Restriction_Coil_T4.get(1),
                MaterialLibAPI.getStack(Materials2Materials.Creon, Materials2Shapes.plateDense, (int) (8)),
                MaterialLibAPI.getStack(Materials2Materials.Mellion, Materials2Shapes.gearGt, (int) (4)),
                GTOreDictUnificator.get("frameGtSuperconductorUIVBase", 32), GregtechItemList.Battery_Gem_4.get(4),
                GregtechItemList.Laser_Lens_Special.get(8), ItemList.Emitter_UMV.get(4),
                GTOreDictUnificator.get("circuitExotic", 8), GTOreDictUnificator.get("naniteSilver", 2),
                GTOreDictUnificator.get("naniteGold", 2), ItemList.MetaMaterial_Waveguide1.get(4) },
            nanites(1, 1, 1, 1, 1, 2, 3, 3, 3, 2, 2, 4),
            new FluidStack[] { CondensateType.Bedrockium.getEntangled(64 * INGOTS),
                CondensateType.Infinity.getEntangled(32 * INGOTS),
                CondensateType.SpaceTime.getEntangled(32 * INGOTS), },
            300 * SECONDS,
            TierEU.RECIPE_UMV);

        // Graviton Modulator 3
        addBec(
            CustomItemList.Godforge_GravitonFlowModulatorTier3.get(1),
            new ItemStack[] { CustomItemList.Godforge_MagneticConfinementCasing.get(1),
                ItemRefer.Field_Restriction_Coil_T4.get(4),
                MaterialLibAPI.getStack(Materials2Materials.Creon, Materials2Shapes.plateSuperdense, (int) (8)),
                MaterialLibAPI.getStack(Materials2Materials.Mellion, Materials2Shapes.gearGt, (int) (64)),
                GregtechItemList.SpaceTimeContinuumRipper.get(8), GregtechItemList.Battery_Gem_4.get(8),
                GregtechItemList.Laser_Lens_Special.get(8), GTOreDictUnificator.get("circuitTranscendent", 4),
                ItemList.Emitter_UXV.get(4), ItemList.MetaMaterial_Waveguide2.get(64),
                ItemList.MetaMaterial_EnergyConduit2.get(64), ItemList.MetaMaterial_EnergyConduit3.get(8),
                GTOreDictUnificator.get("naniteSilver", 8), GTOreDictUnificator.get("naniteGold", 8),
                GTOreDictUnificator.get("naniteSixPhasedCopper", 8), GTOreDictUnificator.get("naniteUniversium", 8) },
            nanites(1, 3, 2, 2, 4, 2, 1, 7, 8, 6, 6, 9, 2, 3, 5, 8),
            new FluidStack[] { CondensateType.SpaceTime.getEntangled(256 * INGOTS),
                CondensateType.BoundlessCosmicSolder.getEntangled(20_000),
                CondensateType.Eternity.getEntangled(128 * INGOTS),
                CondensateType.MagMatter.getEntangled(32 * INGOTS) },
            300 * SECONDS,
            TierEU.RECIPE_UXV);

        // Phonon Transmission Conduit
        addBec(
            CustomItemList.Godforge_HarmonicPhononTransmissionConduit.get(2),
            new ItemStack[] { GTOreDictUnificator.get("frameGtTranscendentMetal", 2),
                MaterialLibAPI.getStack(Materials2Materials.Creon, Materials2Shapes.stickLong, (int) (18)),
                new ItemStack(Blocks.tfftStorageField, 2, 9), ItemList.Tesseract.get(12),
                ItemList.MetaMaterial_ResonanceChamber1.get(4), ItemList.Thermal_Superconductor.get(9),
                ItemList.Field_Generator_UEV.get(6),
                MaterialLibAPI.getStack(Materials2Materials.SixPhasedCopper, Materials2Shapes.bolt, (int) (36)) },
            nanites(1, 1, 2, 3, 4, 3, 2, 1),
            new FluidStack[] { CondensateType.TranscendentMetal.getEntangled(32 * INGOTS),
                CondensateType.PhononMedium.getEntangled(2_000),
                CondensateType.CelestialTungsten.getEntangled(16 * INGOTS), },
            300 * SECONDS,
            TierEU.RECIPE_UXV);
    }

    public void runLateRecipes() {
        // Shielding Casing
        addBec(
            CustomItemList.Godforge_SingularityShieldingCasing.get(6),
            new ItemStack[] { GTOreDictUnificator.get("frameGtSixPhasedCopper", 4),
                MaterialLibAPI.getStack(Materials2Materials.Infinity, Materials2Shapes.plateSuperdense, (int) (2)),
                MaterialsAlloy.QUANTUM.getPlate(16), GTOreDictUnificator.get("frameGtInfinityCatalyst", 4),
                MaterialLibAPI.getStack(Materials2Materials.Netherite, Materials2Shapes.plateSuperdense, (int) (2)),
                getModItem(EternalSingularity.ID, "combined_singularity", 1L, 2),
                MaterialLibAPI
                    .getStack(Materials2Materials.SuperconductorUIVBase, Materials2Shapes.stickLong, (int) (8)),
                MaterialLibAPI.getStack(Materials2Materials.Creon, Materials2Shapes.plate, (int) (16)),
                MaterialLibAPI.getStack(Materials2Materials.Mellion, Materials2Shapes.plate, (int) (16)),
                MaterialLibAPI
                    .getStack(Materials2Materials.SuperconductorUEVBase, Materials2Shapes.stickLong, (int) (8)),
                getModItem(EternalSingularity.ID, "combined_singularity", 1L, 4),
                MaterialLibAPI
                    .getStack(Materials2Materials.TranscendentMetal, Materials2Shapes.plateSuperdense, (int) (2)),
                MaterialsAlloy.TITANSTEEL.getFrameBox(4), GTOreDictUnificator.get("plateprotohalkonite", 16),
                MaterialLibAPI
                    .getStack(Materials2Materials.CosmicNeutronium, Materials2Shapes.plateSuperdense, (int) (2)),
                MaterialsAlloy.ABYSSAL.getFrameBox(4) },
            nanites(1, 2, 1, 1, 2, 4, 3, 1, 1, 3, 4, 2, 1, 1, 2, 1),
            new FluidStack[] { CondensateType.Hypogen.getEntangled(4 * INGOTS),
                CondensateType.Bedrockium.getEntangled(256 * STACKS),
                CondensateType.CelestialTungsten.getEntangled(32 * STACKS),
                CondensateType.Neutronium.getEntangled(32 * STACKS) },
            300 * SECONDS,
            TierEU.RECIPE_UMV);
    }

    private void addBECCasingRecipes() {
        // Electromagnetically-isolated Casing
        addBec(
            ItemList.ElectromagneticallyIsolatedCasing.get(8),
            new ItemStack[] { ItemList.BlockQuarkContainmentCasing.get(8),
                GGMaterial.tairitsu.get(OrePrefixes.frameGt, 8), GTOreDictUnificator.get("frameGtChuritsu", 8),
                GTOreDictUnificator.get("frameGtShijima", 8), ItemList.MetaMaterial_Shielding1.get(8),
                MaterialLibAPI.getStack(Materials2Materials.SixPhasedCopper, Materials2Shapes.bolt, (int) (32)),
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
                GTOreDictUnificator.get("pipeHugeTranscendentMetal", 8), ItemList.Electromagnet_Tengam.get(8),
                ItemList.MetaMaterial_EnergyConduit1.get(8),
                MaterialLibAPI.getStack(Materials2Materials.SixPhasedCopper, Materials2Shapes.rotor, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Churitsu, Materials2Shapes.stickLong, (int) (4)),
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
                MaterialLibAPI.getStack(Materials2Materials.SixPhasedCopper, Materials2Shapes.gearGtSmall, (int) (2)),
                MaterialLibAPI.getStack(Materials2Materials.Shijima, Materials2Shapes.gearGt, (int) (1)),
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
                GTOreDictUnificator.get("frameGtChuritsu", 1), GGMaterial.tairitsu.get(OrePrefixes.screw, 16),
                MaterialLibAPI.getStack(Materials2Materials.Shijima, Materials2Shapes.bolt, (int) (16)) },
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
                GTOreDictUnificator.get("wireGt02SuperconductorUIV", 4), ItemList.Circuit_Chip_APIC.get(4),
                ItemList.MetaMaterial_SensorArray1.get(4), ItemList.MetaMaterial_FieldManipulator1.get(2) },
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
                MaterialLibAPI.getStack(Materials2Materials.Churitsu, Materials2Shapes.itemCasing, (int) (4)),
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

        // Astral Array Fabricator
        addBec(
            CustomItemList.astralArrayFabricator.get(1),
            new ItemStack[] { CustomItemList.SpacetimeCompressionFieldGeneratorTier8.get(64),
                CustomItemList.SpacetimeCompressionFieldGeneratorTier8.get(64),
                CustomItemList.SpacetimeCompressionFieldGeneratorTier8.get(10),
                CustomItemList.StabilisationFieldGeneratorTier8.get(48),
                CustomItemList.TimeAccelerationFieldGeneratorTier8.get(64),
                CustomItemList.TimeAccelerationFieldGeneratorTier8.get(64),
                CustomItemList.TimeAccelerationFieldGeneratorTier8.get(40), ItemList.Field_Generator_UMV.get(8),
                CustomItemList.EOH_Reinforced_Temporal_Casing.get(64),
                CustomItemList.EOH_Reinforced_Spatial_Casing.get(64), CustomItemList.EOH_Infinite_Energy_Casing.get(32),
                GTOreDictUnificator.get("naniteEternity", 16), ItemList.Transdimensional_Alignment_Matrix.get(1),
                ItemList.MetaMaterial_Shielding3.get(16), ItemList.MetaMaterial_ElectrograviticValve3.get(16),
                ItemList.MetaMaterial_FieldManipulator4.get(32) },
            nanites(6, 6, 6, 8, 6, 6, 6, 3, 1, 1, 2, 9, 10, 4, 5, 7),
            new FluidStack[] { CondensateType.ChromaticGlass.getEntangled(65_536 * INGOTS),
                CondensateType.Space.getEntangled(3_640 * INGOTS), CondensateType.Time.getEntangled(3_640 * INGOTS),
                CondensateType.Eternity.getEntangled(1_820 * INGOTS) },
            7200 * SECONDS,
            TierEU.RECIPE_UXV);
    }

    private void addEyeOfHarmonyCasings() {

        // Reinforced Temporal Structure Casing
        addBec(
            CustomItemList.EOH_Reinforced_Temporal_Casing.get(4),
            new ItemStack[] { CustomItemList.Godforge_SingularityShieldingCasing.get(32),
                GTOreDictUnificator.get("plateDenseprotohalkonite", 32), GTOreDictUnificator.get("blockHexanite", 8),
                GTOreDictUnificator.get("naniteNeutronium", 48),
                MaterialLibAPI.getStack(Materials2Materials.Bedrockium, Materials2Shapes.plateSuperdense, (int) (1)),
                MaterialLibAPI
                    .getStack(Materials2Materials.CosmicNeutronium, Materials2Shapes.plateSuperdense, (int) (1)),
                GGMaterial.shirabon.get(OrePrefixes.plateSuperdense, 1),
                MaterialLibAPI.getStack(Materials2Materials.Infinity, Materials2Shapes.plateSuperdense, (int) (1)),
                ItemList.Machine_UV_SolarPanel.get(1), ItemList.AcceleratorUV.get(4),
                getModItem(GraviSuite.ID, "itemSimpleItem", 64, 3), ItemList.EnergisedTesseract.get(1) },
            nanites(4, 1, 1, 2, 1, 1, 4, 1, 1, 1, 1, 2),
            new FluidStack[] { CondensateType.Neutronium.getEntangled(512 * INGOTS),
                CondensateType.CosmicNeutronium.getEntangled(512 * INGOTS),
                CondensateType.Bedrockium.getEntangled(256 * INGOTS), CondensateType.Time.getEntangled(10 * INGOTS) },
            3600 * SECONDS,
            TierEU.RECIPE_UMV);

        // Reinforced Spatial Structure Casing
        addBec(
            CustomItemList.EOH_Reinforced_Spatial_Casing.get(4),
            new ItemStack[] { CustomItemList.Godforge_SingularityShieldingCasing.get(32),
                GTOreDictUnificator.get("plateDenseprotohalkonite", 32), GTOreDictUnificator.get("blockHexanite", 8),
                GTOreDictUnificator.get("naniteNeutronium", 48),
                MaterialLibAPI.getStack(Materials2Materials.Bedrockium, Materials2Shapes.plateSuperdense, (int) (1)),
                MaterialLibAPI
                    .getStack(Materials2Materials.CosmicNeutronium, Materials2Shapes.plateSuperdense, (int) (1)),
                GGMaterial.shirabon.get(OrePrefixes.plateSuperdense, 1),
                MaterialLibAPI.getStack(Materials2Materials.Infinity, Materials2Shapes.plateSuperdense, (int) (1)),
                ItemList.Machine_UV_SolarPanel.get(1), ItemList.Quantum_Chest_IV.get(1),
                getModItem(GraviSuite.ID, "itemSimpleItem", 64, 3), ItemList.EnergisedTesseract.get(1) },
            nanites(4, 1, 1, 2, 1, 1, 4, 1, 1, 1, 1, 2),
            new FluidStack[] { CondensateType.Neutronium.getEntangled(512 * INGOTS),
                CondensateType.CosmicNeutronium.getEntangled(512 * INGOTS),
                CondensateType.Bedrockium.getEntangled(256 * INGOTS), CondensateType.Space.getEntangled(10 * INGOTS) },
            3600 * SECONDS,
            TierEU.RECIPE_UMV);

        // Infinite Spacetime Boundary Casing
        addBec(
            CustomItemList.EOH_Infinite_Energy_Casing.get(1),
            new ItemStack[] { TileEntities.lsc.getStackForm(1), ItemList.Machine_UV_SolarPanel.get(1),
                new ItemStack(lscLapotronicEnergyUnit, 1, 5), GTOreDictUnificator.get("wireGt16SuperconductorUMV", 4),
                CustomItemList.Machine_Multi_Transformer.get(16), ItemList.Wireless_Hatch_Energy_UMV.get(4),
                CustomItemList.eM_energyTunnel5_UMV.get(1),
                getModItem(NewHorizonsCoreMod.ID, "HighEnergyFlowCircuit", 64, 0),
                GTOreDictUnificator.get("plateMetastableOganesson", 6),
                MaterialLibAPI.getStack(Materials2Materials.BlueTopaz, Materials2Shapes.plate, (int) (6)),
                MaterialLibAPI.getStack(Materials2Materials.CallistoIce, Materials2Shapes.plate, (int) (6)),
                MaterialLibAPI.getStack(Materials2Materials.Ledox, Materials2Shapes.plate, (int) (6)),
                GTOreDictUnificator.get("screwMetastableOganesson", 6),
                MaterialLibAPI.getStack(Materials2Materials.BlueTopaz, Materials2Shapes.screw, (int) (6)),
                MaterialLibAPI.getStack(Materials2Materials.CallistoIce, Materials2Shapes.screw, (int) (6)),
                MaterialLibAPI.getStack(Materials2Materials.Ledox, Materials2Shapes.screw, (int) (6)) },
            nanites(4, 1, 2, 1, 1, 3, 4, 3, 1, 1, 1, 1, 1, 1, 1, 1),
            new FluidStack[] { CondensateType.Neutronium.getEntangled(1024 * INGOTS),
                CondensateType.CosmicNeutronium.getEntangled(1024 * INGOTS),
                CondensateType.Bedrockium.getEntangled(256 * INGOTS),
                CondensateType.SpaceTime.getEntangled(128 * INGOTS) },
            3600 * SECONDS,
            TierEU.RECIPE_UMV);
    }

    private void addTimeDilation() {
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
                CustomItemList.Godforge_GravitonFlowModulatorTier2.get(1), ItemList.Machine_UV_SolarPanel.get(tp1),
                ItemList.AcceleratorUV.get(4L * tp1), GTOreDictUnificator.get("circuitCosmic", tp1),
                getModItem(SuperSolarPanels.ID, "redcomponent", 64),
                getModItem(SuperSolarPanels.ID, "greencomponent", 64),
                getModItem(SuperSolarPanels.ID, "bluecomponent", 64), ItemList.MetaMaterial_EnergyConduit1.get(tp1),
                waveFocus[BOLT_TIER[t] - 1].get(focusQty[BOLT_POS[t]]),
                ItemList.MetaMaterial_ElectrograviticValve1.get(2L * tp1), ItemList.MetaMaterial_Waveguide1.get(tp1),
                ItemList.Energy_Cluster.get(tp1),
                MaterialLibAPI.getStack(Materials2Materials.Hexanite, Materials2Shapes.ring, (int) (4 * tp1)),
                MaterialLibAPI.getStack(Materials2Materials.Hexanite, Materials2Shapes.gearGtSmall, (int) (4 * tp1)),
                GTOreDictUnificator.get(OrePrefixes.gearGt, TIER_MATS[t], tp1) };
            NaniteTier[] nanites = nanitesShifted(BOLT_TIER[t] - 1, 2, 2, 2, 2, 2, 1, 1, 1, 3, 4, 3, 3, 4, 1, 1, 1);
            FluidStack[] condensates = { cosmicSolder(t), CondensateType.Time.getEntangled(10 * tp1 * INGOTS),
                CondensateType.SpaceTime.getEntangled(10 * INGOTS),
                CondensateType.DimensionallyShiftedSuperfluid.getEntangled(3_000 * tp1) };
            addBec(outputs[t].get(1), inputs, nanites, condensates, 3600 * SECONDS, TierEU.RECIPE_UMV);
        }
    }

    private void addSpacetimeCompression() {
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
                ItemList.MetaMaterial_Shielding1.get(4L * tp1),
                resonanceChamber[BOLT_TIER[t] - 1].get(resonanceQty[BOLT_POS[t]]),
                GTOreDictUnificator.get("circuitCosmic", tp1),
                CustomItemList.Godforge_SingularityShieldingCasing.get(4 * tp1),
                GTOreDictUnificator.get("frameGtHexanite", tp1),
                MaterialLibAPI.getStack(Materials2Materials.Hexanite, Materials2Shapes.stickLong, (int) (2 * tp1)),
                GTOreDictUnificator.get(OrePrefixes.plateDense, TIER_MATS[t], tp1),
                MaterialLibAPI.getStack(Materials2Materials.Hexanite, Materials2Shapes.plateSuperdense, (int) (tp1)),
                MaterialLibAPI
                    .getStack(Materials2Materials.CosmicNeutronium, Materials2Shapes.plateSuperdense, (int) (tp1)),
                MaterialLibAPI.getStack(Materials2Materials.Infinity, Materials2Shapes.plateSuperdense, (int) (tp1)),
                MaterialLibAPI
                    .getStack(Materials2Materials.TranscendentMetal, Materials2Shapes.plateSuperdense, (int) (tp1)) };
            NaniteTier[] nanites = nanitesShifted(BOLT_TIER[t] - 1, 3, 3, 2, 4, 2, 2, 4, 3, 2, 2, 1, 1, 1, 1, 1, 1);
            FluidStack[] condensates = { cosmicSolder(t), CondensateType.Space.getEntangled(10 * tp1 * INGOTS),
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
                CustomItemList.EOH_Infinite_Energy_Casing.get(1), GTOreDictUnificator.get("circuitCosmic", tp1),
                GTOreDictUnificator.get("frameGtLongasssuperconductornameforuhvwire", 4L * tp1),
                GTOreDictUnificator.get("frameGtSuperconductorUEVBase", 4L * tp1),
                GTOreDictUnificator.get("frameGtSuperconductorUIVBase", 4L * tp1),
                GTOreDictUnificator.get("frameGtSuperconductorUMVBase", 4L * tp1),
                ItemList.Superconducting_Magnet_Solenoid_UHV.get(2L * tp1),
                ItemList.Superconducting_Magnet_Solenoid_UEV.get(2L * tp1),
                ItemList.Superconducting_Magnet_Solenoid_UIV.get(2L * tp1),
                ItemList.Superconducting_Magnet_Solenoid_UMV.get(2L * tp1),
                sensorArray[sensorTier[t] - 1].get(sensorQty[t]),
                fieldManipulator[BOLT_TIER[t] - 1].get(manipulatorQty[BOLT_POS[t]]),
                MaterialLibAPI.getStack(Materials2Materials.Hexanite, Materials2Shapes.screw, (int) (4 * tp1)),
                GTOreDictUnificator.get(OrePrefixes.stickLong, TIER_MATS[t], 4L * tp1) };
            NaniteTier[] nanites = nanitesShifted(BOLT_TIER[t] - 1, 3, 3, 3, 2, 1, 1, 1, 1, 2, 2, 2, 2, 4, 4, 1, 1);
            FluidStack[] condensates = { cosmicSolder(t), CondensateType.Time.getEntangled(10 * tp1 * INGOTS),
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
    private static FluidStack cosmicSolder(int tier) {
        return tier == 0 ? CondensateType.TranscendentMetal.getEntangled(69 * INGOTS)
            : CondensateType.BoundlessCosmicSolder.getEntangled(10_000 * tier);
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
