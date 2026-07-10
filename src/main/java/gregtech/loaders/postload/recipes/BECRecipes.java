package gregtech.loaders.postload.recipes;

import static goodgenerator.loader.Loaders.compactFusionCoil;
import static goodgenerator.util.ItemRefer.Compassline_Casing_UMV;
import static goodgenerator.util.ItemRefer.Compassline_Casing_UXV;
import static gregtech.api.enums.Mods.AE2FluidCraft;
import static gregtech.api.enums.Mods.AppliedEnergistics2;
import static gregtech.api.enums.Mods.Avaritia;
import static gregtech.api.enums.Mods.EtFuturumRequiem;
import static gregtech.api.enums.Mods.EternalSingularity;
import static gregtech.api.enums.Mods.ExtraUtilities;
import static gregtech.api.enums.Mods.GalacticraftAmunRa;
import static gregtech.api.enums.Mods.GraviSuite;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.Mods.SuperSolarPanels;
import static gregtech.api.enums.Mods.UniversalSingularities;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.STACKS;
import static gregtech.api.util.GTRecipeConstants.NANITE_TIERS;
import static kekztech.common.Blocks.lscLapotronicEnergyUnit;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.kuba6000.mobsinfo.loader.extras.ExtraUtilities;

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
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import kekztech.common.Blocks;
import kekztech.common.TileEntities;
import li.cil.oc.common.tileentity.traits.power.AppliedEnergistics2;
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
        addCompressorRecipes();
        addehatchRecipes();
        addOtherStuffRecipes();
        if (Avaritia.isModLoaded() && ExtraUtilities.isModLoaded() && AE2FluidCraft.isModLoaded()) {
            addEyeOfHarmonyRecipes();
        }

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
            new FluidStack[] { CondensateType.BoundlessCosmicSolder.getEntangled(5_000),
                CondensateType.MHDCSM.getEntangled(8 * INGOTS), CondensateType.Eternity.getEntangled(13 * INGOTS),
                CondensateType.DimensionallyShiftedSuperfluid.getEntangled(11_000) },
            600 * SECONDS,
            TierEU.RECIPE_UMV);
    }

    private void addCompressorRecipes() {
        // black hole
        if (UniversalSingularities.isModLoaded()) {
            addBec(
                ItemList.Machine_Multi_BlackHoleCompressor.get(1),
                new ItemStack[] { ItemList.Machine_Multi_HIPCompressor.get(1),
                    ItemList.Machine_Multi_NeutroniumCompressor.get(1), ItemList.AdvancedImplosionCompressor.get(16),
                    ItemList.CompressorUIV.get(8L), ItemList.ElectricImplosionCompressor.get(4),
                    ItemList.Field_Generator_UEV.get(4), ItemList.ZPM3.get(2),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UIV, 4),
                    GregtechItemList.Laser_Lens_Special.get(64),
                    GTOreDictUnificator.get(OrePrefixes.ring, Materials.ProtoHalkonite, 32),
                    GTOreDictUnificator.get(OrePrefixes.rotor, Materials.ProtoHalkonite, 16),
                    GTOreDictUnificator.get(OrePrefixes.gear, Materials.ProtoHalkonite, 8),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Naquadria, 16),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Kevlar, 4),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.SuperconductorUIVBase, 4),
                    GGMaterial.metastableOganesson.get(OrePrefixes.gearGtSmall, 4) },
                nanites(1, 1, 2, 2, 3, 1, 1, 3, 4, 3, 3, 3, 1, 2, 4, 6),
                new FluidStack[] { CondensateType.SpaceTime.getEntangled(10 * INGOTS),
                    CondensateType.Infinity.getEntangled(100 * INGOTS),
                    CondensateType.Neutronium.getEntangled(1024 * INGOTS),
                    CondensateType.CosmicNeutronium.getEntangled(512 * INGOTS) },
                600 * SECONDS,
                TierEU.RECIPE_UMV);
        }

        // HIP
        if (EtFuturumRequiem.isModLoaded()) {
            addBec(
                ItemList.Machine_Multi_HIPCompressor.get(1),
                new ItemStack[] { ItemList.Machine_Multi_IndustrialCompressor.get(4),
                    ItemList.Heating_Duct_Casing.get(4L), ItemList.Coolant_Duct_Casing.get(4L),
                    getModItem(EtFuturumRequiem.ID, "netherite_block", 4), ItemList.Electric_Piston_ZPM.get(16),
                    ItemList.Robot_Arm_ZPM.get(4), ItemList.Electric_Pump_ZPM.get(4),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 4),
                    GGMaterial.incoloy903.get(OrePrefixes.gearGt, 64),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Naquadria, 1) },
                nanites(1, 1, 2, 2, 3, 1, 1, 2, 4, 5),
                new FluidStack[] { CondensateType.DimensionallyShiftedSuperfluid.getEntangled(1_000) },
                10 * SECONDS,
                TierEU.RECIPE_UMV);
        }

        // EIC
        addBec(
            ItemList.ElectricImplosionCompressor.get(1),
            new ItemStack[] { ItemList.Machine_Multi_ImplosionCompressor.get(1),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Neutronium, 1),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Osmium, 64),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 26),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 64),
                ItemList.Electric_Piston_UV.get(64) },
            nanites(1, 1, 2, 3, 4, 5),
            new FluidStack[] { CondensateType.ChromaticGlass.getEntangled(1 * INGOTS),
                CondensateType.Neutronium.getEntangled(10 * INGOTS) },
            10 * SECONDS,
            TierEU.RECIPE_UMV);
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
                ItemList.StableBosonContainmentUnit.get(6), getModItem(NewHorizonsCoreMod.ID, "RadoxPolymerLens", 9),
                getModItem(NewHorizonsCoreMod.ID, "ChromaticLens", 9),
                getModItem(NewHorizonsCoreMod.ID, "MysteriousCrystalLens", 9),
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

        // Graviton Anomaly since it's important ingrediant to gorge
        addBec(
            GregtechItemList.Battery_Casing_Gem_4.get(3),
            new ItemStack[] { MaterialsElements.STANDALONE.ADVANCED_NITINOL.getPlate(32),
                GTOreDictUnificator.get(OrePrefixes.wireGt12, Materials.SuperconductorUHV, 64),
                MaterialsAlloy.TITANSTEEL.getGear(8), GregtechItemList.DehydratorCoilWireZPM.get(64),
                GregtechItemList.DehydratorCoilWireZPM.get(64), MaterialsAlloy.ABYSSAL.getPlate(16),
                MaterialsAlloy.TRINIUM_REINFORCED_STEEL.getPlate(32), MaterialsAlloy.OCTIRON.getPlate(32) },
            nanites(3, 3, 4, 1, 1, 1, 1, 2),
            new FluidStack[] { CondensateType.Neutronium.getEntangled(64 * INGOTS) },
            300 * SECONDS,
            TierEU.RECIPE_UIV);

        addBec(
            GregtechItemList.Battery_Gem_4.get(3),
            new ItemStack[] { GregtechItemList.Battery_Casing_Gem_4.get(2), ItemList.StableBosonContainmentUnit.get(4),
                MaterialsAlloy.ABYSSAL.getPlate(64), GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UHV, 16),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUHV, 64),
                MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getBolt(16), MaterialsAlloy.TITANSTEEL.getScrew(16),
                MaterialsAlloy.TRINIUM_REINFORCED_STEEL.getPlate(64), MaterialsAlloy.OCTIRON.getPlate(32),
                MaterialsAlloy.TITANSTEEL.getPlate(32) },
            nanites(3, 3, 2, 4, 1, 1, 1, 1, 1, 1, 2),
            new FluidStack[] { CondensateType.Neutronium.getEntangled(64 * INGOTS) },
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
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.MAX, 4), ItemList.Emitter_UXV.get(4),
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

    private void addehatchRecipes() {
        // common ehatch
        addBec(
            ItemList.Hatch_Energy_UMV.get(1),
            new ItemStack[] { ItemList.Hull_UMV.get(1),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUMV, 2),
                ItemList.Circuit_Chip_ZPIC.get(2), GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UMV, 2),
                ItemList.UMV_Coil.get(2), ItemList.Reactor_Coolant_Sp_6.get(6), ItemList.Electric_Pump_UMV.get(1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.CallistoIce, 16),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Ledox, 16),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Glowstone, 32) },
            nanites(5, 4, 3, 3, 4, 2, 3, 2, 4, 5, 4),
            new FluidStack[] { CondensateType.CelestialTungsten.getEntangled(4 * INGOTS) },
            500 * SECONDS,
            TierEU.RECIPE_UIV);

        addBec(
            ItemList.Hatch_Energy_UXV.get(1),
            new ItemStack[] { ItemList.Hull_UXV.get(1),
                GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.SuperconductorUMV, 4),
                ItemList.Circuit_Chip_YPIC.get(2), GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 2),
                ItemList.UXV_Coil.get(2), ItemList.Reactor_Coolant_Sp_6.get(8), ItemList.Electric_Pump_UXV.get(1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.CallistoIce, 32),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Ledox, 32),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Glowstone, 64) },
            nanites(9, 8, 7, 7, 8, 6, 7, 6, 8, 9, 8),
            new FluidStack[] { CondensateType.BoundlessCosmicSolder.getEntangled(1_000) },
            500 * SECONDS,
            TierEU.RECIPE_UMV);

        // wireless ehatch
        addBec(
            ItemList.Wireless_Hatch_Energy_UMV.get(1),
            new ItemStack[] { ItemList.Hatch_Energy_UMV.get(1), new ItemStack(compactFusionCoil, 1),
                ItemList.Casing_Coil_Superconductor.get(1), CustomItemList.Machine_Multi_Transformer.get(1),
                CustomItemList.eM_Power.get(2), GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SpaceTime, 2),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Infinity, 1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 4), ItemList.EnergisedTesseract.get(1) },
            nanites(5, 4, 3, 3, 4, 2, 3, 5, 4),
            new FluidStack[] { CondensateType.CelestialTungsten.getEntangled(4 * INGOTS),
                CondensateType.Infinity.getEntangled(1 * INGOTS) },
            200 * SECONDS,
            TierEU.RECIPE_UMV);

        addBec(
            ItemList.Wireless_Hatch_Energy_UXV.get(1),
            new ItemStack[] { ItemList.Hatch_Energy_UXV.get(1), new ItemStack(compactFusionCoil, 1),
                ItemList.Casing_Coil_Superconductor.get(1), CustomItemList.Machine_Multi_Transformer.get(1),
                CustomItemList.eM_Power.get(2), GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SpaceTime, 2),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Infinity, 1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.MAX, 4), ItemList.EnergisedTesseract.get(1) },
            nanites(9, 8, 7, 7, 8, 6, 7, 9, 8),
            new FluidStack[] { CondensateType.CelestialTungsten.getEntangled(4 * INGOTS),
                CondensateType.Infinity.getEntangled(1 * INGOTS) },
            200 * SECONDS,
            TierEU.RECIPE_UMV);
    }

    private void addOtherStuffRecipes() {
        // UHV+ solenoids
        addBec(
            ItemList.Superconducting_Magnet_Solenoid_UHV.get(1),
            new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUHV, 16),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 2),
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Neutronium, 1),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Naquadria, 8),
                GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.CosmicNeutronium, 2),
                ItemList.Large_Fluid_Cell_Iridium.get(1), ItemList.Electric_Pump_UHV.get(1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.CallistoIce, 32),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Ledox, 32) },
            nanites(1, 1, 2, 3, 4, 3, 2, 1, 1),
            new FluidStack[] { CondensateType.ChromaticGlass.getEntangled(8 * INGOTS) },
            100 * SECONDS,
            TierEU.RECIPE_UEV);

        addBec(
            ItemList.Superconducting_Magnet_Solenoid_UEV.get(1),
            new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUEV, 16),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Draconium, 2),
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.NetherStar, 1),
                GGMaterial.metastableOganesson.get(OrePrefixes.stickLong, 8),
                GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.Infinity, 2),
                ItemList.Large_Fluid_Cell_Iridium.get(1), ItemList.Electric_Pump_UEV.get(1),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.CallistoIce, 2),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Ledox, 2) },
            nanites(1, 1, 2, 3, 4, 3, 2, 1, 1),
            new FluidStack[] { CondensateType.CelestialTungsten.getEntangled(8 * INGOTS) },
            100 * SECONDS,
            TierEU.RECIPE_UIV);

        addBec(
            ItemList.Superconducting_Magnet_Solenoid_UIV.get(1),
            new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUIV, 16),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.NetherStar, 2),
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.DraconiumAwakened, 1),
                GGMaterial.metastableOganesson.get(OrePrefixes.stickLong, 8),
                GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.TranscendentMetal, 2),
                ItemList.Large_Fluid_Cell_Neutronium.get(1), ItemList.Electric_Pump_UIV.get(1) },
            nanites(2, 2, 3, 4, 5, 4, 3),
            new FluidStack[] { CondensateType.TranscendentMetal.getEntangled(32 * INGOTS),
                CondensateType.CelestialTungsten.getEntangled(32 * INGOTS) },
            100 * SECONDS,
            TierEU.RECIPE_UMV);

        addBec(
            ItemList.Superconducting_Magnet_Solenoid_UMV.get(1),
            new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUMV, 16),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Quantium, 2),
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Infinity, 1),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Infinity, 8),
                GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.SpaceTime, 2),
                ItemList.Large_Fluid_Cell_Neutronium.get(1), ItemList.Electric_Pump_UMV.get(1) },
            nanites(3, 3, 4, 5, 6, 5, 4),
            new FluidStack[] { CondensateType.SpaceTime.getEntangled(16 * INGOTS),
                CondensateType.BoundlessCosmicSolder.getEntangled(1_000) },
            200 * SECONDS,
            TierEU.RECIPE_UMV);

        // T4 QFT Manipulator, 1.5x cheaper recipe
        addBec(
            GregtechItemList.SpaceTimeContinuumRipper.get(3),
            new ItemStack[] { GregtechItemList.ForceFieldGlass.get(16),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Carbon, 64), ItemList.Emitter_UMV.get(8),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUMV, 16),
                GregtechItemList.Laser_Lens_Special.get(2), // Quantum Anormaly
                ItemRefer.Advanced_Radiation_Protection_Plate.get(32), ItemList.NaquadriaSupersolid.get(8),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Thulium, 40),
                MaterialsElements.getInstance().NEPTUNIUM.getBlock(32),
                MaterialsElements.getInstance().FERMIUM.getBlock(32) },
            nanites(5, 4, 3, 3, 4, 3, 2, 4, 5, 4),
            new FluidStack[] { CondensateType.Infinity.getEntangled(16 * INGOTS) },
            1000 * SECONDS,
            TierEU.RECIPE_UMV);

        addBec(
            ItemRefer.Field_Restriction_Coil_T4.get(1),
            new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SpaceTime, 1),
                ItemList.Field_Generator_UIV.get(2), ItemList.Electric_Pump_UIV.get(8),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUEV, 64),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUEV, 64),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.SpaceTime, 8),
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.SpaceTime, 16),
                ItemList.Circuit_Wafer_PPIC.get(64), GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 1),
                GGMaterial.metastableOganesson.get(OrePrefixes.gearGtSmall, 8) },
            nanites(5, 4, 3, 3, 4, 3, 2, 4, 5, 4),
            new FluidStack[] { CondensateType.TranscendentMetal.getEntangled(64 * INGOTS),
                CondensateType.DimensionallyShiftedSuperfluid.getEntangled(64_000) },
            200 * SECONDS,
            TierEU.RECIPE_UMV);
    }

    public void runLateRecipes() {
        // Shielding Casing
        addBec(
            CustomItemList.Godforge_SingularityShieldingCasing.get(6),
            new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SixPhasedCopper, 4),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Infinity, 2),
                MaterialsAlloy.QUANTUM.getPlate(16),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.InfinityCatalyst, 4),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Netherite, 2),
                getModItem(EternalSingularity.ID, "combined_singularity", 1L, 2),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.SuperconductorUIVBase, 8),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Creon, 16),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Mellion, 16),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.SuperconductorUEVBase, 8),
                getModItem(EternalSingularity.ID, "combined_singularity", 1L, 4),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.TranscendentMetal, 2),
                MaterialsAlloy.TITANSTEEL.getFrameBox(4),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.ProtoHalkonite, 16),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.CosmicNeutronium, 2),
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
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Eternity, 16),
                ItemList.Transdimensional_Alignment_Matrix.get(1), ItemList.MetaMaterial_Shielding3.get(16),
                ItemList.MetaMaterial_ElectrograviticValve3.get(16), ItemList.MetaMaterial_FieldManipulator4.get(32) },
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
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.ProtoHalkonite, 32),
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Hexanite, 8),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Neutronium, 48),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Bedrockium, 1),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.CosmicNeutronium, 1),
                GGMaterial.shirabon.get(OrePrefixes.plateSuperdense, 1),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Infinity, 1),
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
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.ProtoHalkonite, 32),
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Hexanite, 8),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Neutronium, 48),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Bedrockium, 1),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.CosmicNeutronium, 1),
                GGMaterial.shirabon.get(OrePrefixes.plateSuperdense, 1),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Infinity, 1),
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
                ItemList.AcceleratorUV.get(4L * tp1), GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, tp1),
                getModItem(SuperSolarPanels.ID, "redcomponent", 64),
                getModItem(SuperSolarPanels.ID, "greencomponent", 64),
                getModItem(SuperSolarPanels.ID, "bluecomponent", 64), ItemList.MetaMaterial_EnergyConduit1.get(tp1),
                waveFocus[BOLT_TIER[t] - 1].get(focusQty[BOLT_POS[t]]),
                ItemList.MetaMaterial_ElectrograviticValve1.get(2L * tp1), ItemList.MetaMaterial_Waveguide1.get(tp1),
                ItemList.Energy_Cluster.get(tp1),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.Hexanite, 4L * tp1),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Hexanite, 4 * tp1),
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
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, tp1),
                CustomItemList.Godforge_SingularityShieldingCasing.get(4 * tp1),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Hexanite, tp1),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Hexanite, 2L * tp1),
                GTOreDictUnificator.get(OrePrefixes.plateDense, TIER_MATS[t], tp1),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Hexanite, tp1),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.CosmicNeutronium, tp1),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Infinity, tp1),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.TranscendentMetal, tp1) };
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
                CustomItemList.EOH_Infinite_Energy_Casing.get(1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, tp1),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SuperconductorUHVBase, 4L * tp1),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SuperconductorUEVBase, 4L * tp1),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SuperconductorUIVBase, 4L * tp1),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SuperconductorUMVBase, 4L * tp1),
                ItemList.Superconducting_Magnet_Solenoid_UHV.get(2L * tp1),
                ItemList.Superconducting_Magnet_Solenoid_UEV.get(2L * tp1),
                ItemList.Superconducting_Magnet_Solenoid_UIV.get(2L * tp1),
                ItemList.Superconducting_Magnet_Solenoid_UMV.get(2L * tp1),
                sensorArray[sensorTier[t] - 1].get(sensorQty[t]),
                fieldManipulator[BOLT_TIER[t] - 1].get(manipulatorQty[BOLT_POS[t]]),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.Hexanite, 4L * tp1),
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
