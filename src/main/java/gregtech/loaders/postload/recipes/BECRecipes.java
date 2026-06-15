package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.GalacticraftAmunRa;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.NANITE_TIERS;
import static tectech.loader.recipe.BaseRecipeLoader.getNHCoreModItem;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

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
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import kekztech.common.Blocks;
import tectech.recipe.TecTechRecipeMaps;
import tectech.thing.CustomItemList;
import tectech.thing.block.BlockGodforgeGlass;
import tectech.thing.block.BlockQuantumGlass;

/// Recipes made in the BEC Condensate Assembler.
public class BECRecipes implements Runnable {

    private static final NaniteTier[] TIER_TO_NANITE = { NaniteTier.Carbon, NaniteTier.Silver, NaniteTier.Gold,
        NaniteTier.Transcendent, NaniteTier.SixPhasedCopper, NaniteTier.WhiteDwarf, NaniteTier.BlackDwarf,
        NaniteTier.Universium, NaniteTier.Eternity, NaniteTier.MagMatter };

    @Override
    public void run() {
        addBECCasingRecipes();
        addGodforgeRecipes();
    }

    private void addGodforgeRecipes() {
        // Magnetic Confinement Casing
        addBec(
            CustomItemList.Godforge_MagneticConfinementCasing.get(12),
            new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.TranscendentMetal, 12),
                WerkstoffLoader.MagnetoResonaticDust.get(OrePrefixes.block, 16),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.TengamAttuned, 32),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Creon, 16),
                MaterialsElements.STANDALONE.HYPOGEN.getScrew(8),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.SixPhasedCopper, 8),
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
                new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Mellion, 24),
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SixPhasedCopper, 24),
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.TranscendentMetal, 12),
                    MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getFrameBox(12),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.Creon, 9),
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
                ItemList.Emitter_UIV.get(4), GTOreDictUnificator.get(OrePrefixes.plate, Materials.Creon, 9),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Creon, 12),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Mellion, 12) },
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
                ItemList.StableBosonContainmentUnit.get(6), getNHCoreModItem("RadoxPolymerLens", 9),
                getNHCoreModItem("ChromaticLens", 9), getNHCoreModItem("MysteriousCrystalLens", 9),
                WerkstoffLoader.MagnetoResonaticDust.get(OrePrefixes.lens, 9),
                MaterialsElements.STANDALONE.RHUGNOR.getPlate(24),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Creon, 33),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Mellion, 9),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.SixPhasedCopper, 9),
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
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Creon, 16),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Mellion, 8),
                GregtechItemList.Battery_Gem_4.get(2), GregtechItemList.Laser_Lens_Special.get(4),
                ItemList.Emitter_UIV.get(4), GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 16),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Silver, 2),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.SuperconductorUIVBase, 32) },
            nanites(1, 2, 1, 1, 1, 1, 2, 4, 3),
            new FluidStack[] { CondensateType.ChromaticGlass.getEntangled(32 * INGOTS),
                CondensateType.Infinity.getEntangled(32 * INGOTS) },
            450 * SECONDS,
            TierEU.RECIPE_UIV);

        // Graviton Modulator 2
        addBec(
            CustomItemList.Godforge_GravitonFlowModulatorTier2.get(1),
            new ItemStack[] { CustomItemList.Godforge_MagneticConfinementCasing.get(1),
                ItemRefer.Field_Restriction_Coil_T4.get(1),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Creon, 8),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Mellion, 4),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SuperconductorUIVBase, 32),
                GregtechItemList.Battery_Gem_4.get(4), GregtechItemList.Laser_Lens_Special.get(8),
                ItemList.Emitter_UMV.get(4), GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UMV, 8),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Silver, 2),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Gold, 2),
                ItemList.MetaMaterial_Waveguide1.get(4) },
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
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Creon, 8),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Mellion, 64),
                GregtechItemList.SpaceTimeContinuumRipper.get(8), GregtechItemList.Battery_Gem_4.get(8),
                GregtechItemList.Laser_Lens_Special.get(8),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.MAX, 4),
                GregtechItemList.Laser_Lens_Special.get(8), ItemList.Emitter_UXV.get(4),
                ItemList.MetaMaterial_Waveguide2.get(64), ItemList.MetaMaterial_EnergyConduit2.get(64),
                ItemList.MetaMaterial_EnergyConduit3.get(8),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Silver, 8),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Gold, 8),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.SixPhasedCopper, 8),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Universium, 8) },
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
            new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.TranscendentMetal, 2),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Creon, 18),
                new ItemStack(Blocks.tfftStorageField, 2, 9), ItemList.Tesseract.get(12),
                ItemList.MetaMaterial_ResonanceChamber1.get(4), ItemList.Thermal_Superconductor.get(9),
                ItemList.Field_Generator_UEV.get(6),
                GTOreDictUnificator.get(OrePrefixes.bolt, Materials.SixPhasedCopper, 36) },
            nanites(1, 1, 2, 3, 4, 3, 2, 1),
            new FluidStack[] { CondensateType.TranscendentMetal.getEntangled(32 * INGOTS),
                CondensateType.PhononMedium.getEntangled(2_000),
                CondensateType.CelestialTungsten.getEntangled(16 * INGOTS), },
            300 * SECONDS,
            TierEU.RECIPE_UXV);
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
