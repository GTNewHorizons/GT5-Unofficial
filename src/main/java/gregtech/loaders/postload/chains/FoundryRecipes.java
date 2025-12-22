package gregtech.loaders.postload.chains;

import static bartworks.common.loaders.ItemRegistry.bw_realglas;
import static goodgenerator.api.recipe.GoodGeneratorRecipeMaps.preciseAssemblerRecipes;
import static gregtech.api.enums.Mods.EternalSingularity;
import static gregtech.api.enums.Mods.GalacticraftAmunRa;
import static gregtech.api.enums.Mods.UniversalSingularities;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.formingPressRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.PRECISE_ASSEMBLER_CASING_TIER;
import static gtPlusPlus.core.material.MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN;
import static gtPlusPlus.core.material.MaterialsElements.STANDALONE.HYPOGEN;
import static gtPlusPlus.core.material.MaterialsElements.STANDALONE.RHUGNOR;
import static kekztech.common.Blocks.lscLapotronicEnergyUnit;
import static tectech.thing.CustomItemList.Godforge_SingularityShieldingCasing;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import bartworks.system.material.WerkstoffLoader;
import goodgenerator.items.GGMaterial;
import goodgenerator.util.ItemRefer;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import tectech.recipe.TTRecipeAdder;
import tectech.thing.CustomItemList;

public class FoundryRecipes {

    // todo move to coremod
    public static void load() {
        GTValues.RA.stdBuilder() // todo: good recipe, im out of ideas lol
            .itemInputs(
                ItemList.Radiator_Fluid_Solidifier.get(4),
                ItemList.Naquarite_Universal_Insulator_Foil.get(8),
                ItemList.FluidRegulator_UHV.get(4),
                ItemList.Field_Generator_UHV.get(1))
            .fluidInputs(Materials.TungstenCarbide.getMolten(5760), Materials.NaquadahEnriched.getMolten(5760))
            .itemOutputs(ItemList.Central_Casing_ExoFoundry.get(4))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(PRECISE_ASSEMBLER_CASING_TIER, 3)
            .addTo(preciseAssemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SuperconductorUVBase, 1),
                ItemList.Naquarite_Universal_Insulator_Foil.get(8),
                ItemList.Electric_Pump_UV.get(4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 4),
                ItemList.Reactor_Coolant_Sp_6.get(1),
                ItemList.Emitter_UV.get(1))
            .itemOutputs(ItemList.Secondary_Casing_ExoFoundry.get(1))
            .fluidInputs(Materials.CosmicNeutronium.getMolten(INGOTS * 16))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(assemblerRecipes);

        if (UniversalSingularities.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(bw_realglas, 8, 14),
                    ItemList.Naquarite_Universal_Insulator_Foil.get(8),
                    getModItem(UniversalSingularities.ID, "universal.general.singularity", 1, 13))
                .fluidInputs(Materials.SuperCoolant.getFluid(64000))
                .itemOutputs(ItemList.Glass_ExoFoundry.get(8))
                .duration(16 * SECONDS)
                .eut(TierEU.RECIPE_UHV)
                .addTo(formingPressRecipes);
        }

        if (EternalSingularity.isModLoaded() && GalacticraftAmunRa.isModLoaded()) {

            // controller
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Machine_Multi_Solidifier.get(1),
                4_000_000,
                2_048,
                (int) TierEU.RECIPE_UEV,
                64,
                new Object[] { ItemList.Machine_Multi_Solidifier.get(64),
                    GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Infinity, 8),
                    ItemList.Electric_Pump_UEV.get(8),
                    GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUEV, 8), HYPOGEN.getRotor(4),
                    ItemList.Field_Generator_UEV.get(4), getModItem(EternalSingularity.ID, "eternal_singularity", 1L),
                    GregtechItemList.Laser_Lens_Special.get(1) },
                new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(9216),
                    GGMaterial.preciousMetalAlloy.getMolten(4608), GGMaterial.metastableOganesson.getMolten(2880),
                    MaterialsElements.STANDALONE.DRAGON_METAL.getFluidStack(1440) },
                ItemList.Machine_Multi_ExoFoundry.get(1),
                60 * SECONDS,
                (int) TierEU.RECIPE_UIV);
            // base casing
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Casing_Fluid_Solidifier.get(1),
                500_000,
                2_048,
                (int) TierEU.RECIPE_UEV,
                8,
                new Object[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Netherite, 1),
                    ItemList.Optically_Perfected_CPU.get(32), ItemRefer.Advanced_Radiation_Protection_Plate.get(32),
                    GTOreDictUnificator.get(OrePrefixes.screw, Materials.Infinity, 6),
                    GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUEV, 4) },
                new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(576),
                    new FluidStack(FluidRegistry.getFluid("oganesson"), 500),
                    GGMaterial.enrichedNaquadahAlloy.getMolten(288) },
                ItemList.Primary_Casing_ExoFoundry.get(1),
                15 * SECONDS,
                (int) TierEU.RECIPE_UEV);
            // ------------------------ Central Chassis ------------------------

            // t1 chassis
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Radiator_Fluid_Solidifier.get(1),
                1_000_000,
                2_048,
                (int) TierEU.RECIPE_UEV,
                16,
                new Object[] { HYPOGEN.getFrameBox(1), WerkstoffLoader.HDCS.get(OrePrefixes.plate, 32),
                    ItemList.Optically_Compatible_Memory.get(8), ItemRefer.HiC_T4.get(4),
                    new Object[] { OrePrefixes.circuit.get(Materials.UEV), 4L }, ItemList.Field_Generator_UHV.get(1),
                    GTOreDictUnificator.get(OrePrefixes.screw, Materials.TengamAttuned, 6),
                    ItemList.NuclearStar.get(2) },
                new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(576),
                    Materials.SuperconductorUEVBase.getMolten(288),
                    MaterialsElements.STANDALONE.RHUGNOR.getFluidStack(144) },
                ItemList.Magnetic_Chassis_T1_ExoFoundry.get(1),
                30 * SECONDS,
                (int) TierEU.RECIPE_UEV);

            // t2 chassis
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Magnetic_Chassis_T1_ExoFoundry.get(1),
                2_000_000,
                4_096,
                (int) TierEU.RECIPE_UIV,
                64,
                new Object[] { ItemRefer.Compact_Fusion_Coil_T4.get(1),
                    GTOreDictUnificator.get(OrePrefixes.screw, Materials.Ichorium, 64),
                    getModItem(EternalSingularity.ID, "eternal_singularity", 16L),
                    getModItem(GalacticraftAmunRa.ID, "tile.machines3", 1L, 1),
                    new Object[] { OrePrefixes.circuit.get(Materials.UIV), 4L },
                    GTOreDictUnificator.get(OrePrefixes.nanite, Materials.TranscendentMetal, 2),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.SixPhasedCopper, 1),
                    ItemList.Field_Generator_UIV.get(1) },
                new FluidStack[] { Materials.MoltenProtoHalkoniteBase.getFluid(2880), Materials.Mellion.getMolten(576),
                    Materials.Creon.getMolten(576), Materials.DimensionallyShiftedSuperfluid.getFluid(20000) },
                ItemList.Magnetic_Chassis_T2_ExoFoundry.get(1),
                30 * SECONDS,
                (int) TierEU.RECIPE_UIV);

            // t3 chassis

            TTRecipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Magnetic_Chassis_T2_ExoFoundry.get(1),
                4_000_000,
                8_192,
                (int) TierEU.RECIPE_UXV,
                256,
                new Object[] { kubatech.api.enums.ItemList.DEFCCasingT5.get(1),
                    ItemRefer.Field_Restriction_Coil_T4.get(4), Godforge_SingularityShieldingCasing.get(4),
                    GTOreDictUnificator.get(OrePrefixes.screw, Materials.Universium, 4),
                    getModItem(EternalSingularity.ID, "combined_singularity", 64, 15),
                    new Object[] { OrePrefixes.circuit.get(Materials.UXV), 16L }, ItemList.Field_Generator_UMV.get(8),
                    new ItemStack(lscLapotronicEnergyUnit, 1, 5),
                    GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.SpaceTime, 64),
                    GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.SuperconductorUMVBase, 64),
                    GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Eternity, 64),
                    GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.MagMatter, 64),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.SpaceTime, 16),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.SuperconductorUMVBase, 16),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Eternity, 16),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.MagMatter, 16) },
                new FluidStack[] { Materials.MoltenProtoHalkoniteBase.getFluid(589_824), // <-- TODO: replace with
                                                                                         // exo-halk
                    Materials.QuarkGluonPlasma.getFluid(100_000), Materials.MagMatter.getMolten(5760),
                    Materials.MHDCSM.getMolten(64) },
                ItemList.Magnetic_Chassis_T3_ExoFoundry.get(1),
                30 * SECONDS,
                (int) TierEU.RECIPE_UXV);

            // ------------------------ Modules ------------------------

            // Extra Casting Basins
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Tungsten, 1),
                1_000_000,
                2_048,
                (int) TierEU.RECIPE_UEV,
                64,
                new Object[] { ItemList.Magnetic_Chassis_T1_ExoFoundry.get(1), ItemList.UHTResistantMesh.get(16),
                    GGMaterial.preciousMetalAlloy.get(OrePrefixes.plate, 6),
                    GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.SuperconductorUEVBase, 4),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.InfinityCatalyst, 2),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.TengamAttuned, 2),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.CosmicNeutronium, 2),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Neutronium, 2), },
                new FluidStack[] { Materials.Naquadria.getMolten(64000), Materials.Manyullyn.getMolten(16000),
                    MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(2880) },
                ItemList.Extra_Casting_Basins_ExoFoundry.get(1),
                30 * SECONDS,
                (int) TierEU.RECIPE_UEV);
            // Streamlined Casters
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                ItemList.FluidRegulator_UEV.get(1),
                1_000_000,
                2_048,
                (int) TierEU.RECIPE_UEV,
                64,
                new Object[] { ItemList.Magnetic_Chassis_T1_ExoFoundry.get(1),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Tritanium, 16),
                    ItemList.AcceleratorUV.get(8), CELESTIAL_TUNGSTEN.getRotor(4),
                    new Object[] { OrePrefixes.circuit.get(Materials.UEV), 2 }, ItemList.Electric_Pump_UEV.get(1) },
                new FluidStack[] { Materials.Holmium.getMolten(5760),
                    MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(2880),
                    GGMaterial.enrichedNaquadahAlloy.getMolten(1440) },
                ItemList.Streamlined_Casters_ExoFoundry.get(1),
                30 * SECONDS,
                (int) TierEU.RECIPE_UEV);
            // Power Efficient Subsystems
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Glowstone, 1),
                1_000_000,
                2_048,
                (int) TierEU.RECIPE_UEV,
                64,
                new Object[] { ItemList.Magnetic_Chassis_T1_ExoFoundry.get(1),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.TengamAttuned, 6),
                    ItemList.Circuit_Wafer_QPIC.get(64), ItemList.Cover_SolarPanel_UV.get(2),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEV, 2),
                    ItemList.Energy_Cluster.get(1), },
                new FluidStack[] { Materials.Neodymium.getMolten(36864), // 4 : 2 : 1 magnet ratio
                    Materials.Samarium.getMolten(18432), Materials.TengamPurified.getMolten(9216),
                    MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(2880)

                },
                ItemList.Power_Efficient_Subsystems_ExoFoundry.get(1),
                30 * SECONDS,
                (int) TierEU.RECIPE_UEV);
            // Hypercooler
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                HYPOGEN.getRotor(1),
                2_000_000,
                2_048,
                (int) TierEU.RECIPE_UIV,
                64,
                new Object[] { ItemList.Magnetic_Chassis_T2_ExoFoundry.get(1),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Netherite, 64),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.SixPhasedCopper, 32),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.TranscendentMetal, 16),
                    ItemList.AcceleratorUV.get(8), ItemList.Electric_Pump_UIV.get(4),
                    ItemList.FluidRegulator_UIV.get(4), new Object[] { OrePrefixes.circuit.get(Materials.UIV), 2L }, },
                new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(147_456),
                    Materials.DimensionallyShiftedSuperfluid.getFluid(90000),
                    Materials.MoltenProtoHalkoniteBase.getFluid(1152), Materials.SpaceTime.getMolten(36) },
                ItemList.Hypercooler_ExoFoundry.get(1),
                60 * SECONDS,
                (int) TierEU.RECIPE_UIV);
            // Efficient Overclocker
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                ItemRefer.AntimatterContainmentCasing.get(1),
                4_000_000,
                4_096,
                (int) TierEU.RECIPE_UMV,
                256,
                new Object[] { ItemList.Magnetic_Chassis_T2_ExoFoundry.get(1),
                    ItemRefer.Field_Restriction_Coil_T4.get(1),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.SuperconductorUMVBase, 16),
                    GTOreDictUnificator.get(OrePrefixes.stick, Materials.ProtoHalkonite, 8),
                    new Object[] { OrePrefixes.circuit.get(Materials.UXV), 8 }, ItemList.Electric_Pump_UMV.get(4),
                    ItemList.Field_Generator_UMV.get(1) },
                new FluidStack[] { Materials.DimensionallyShiftedSuperfluid.getFluid(300000),
                    MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(147_456), GGMaterial.shirabon.getMolten(5760) },
                ItemList.Efficient_Overclocking_ExoFoundry.get(1),
                60 * SECONDS,
                (int) TierEU.RECIPE_UMV);

            // Harmonic Reinforcement
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.TranscendentMetal, 1),
                4_000_000,
                2_048,
                (int) TierEU.RECIPE_UIV,
                64,
                new Object[] { ItemList.Magnetic_Chassis_T2_ExoFoundry.get(1),
                    GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.SixPhasedCopper, 6),
                    GTOreDictUnificator.get(OrePrefixes.bolt, Materials.TranscendentMetal, 32),
                    new Object[] { OrePrefixes.circuit.get(Materials.UIV), 4 }, RHUGNOR.getGear(4),
                    ItemList.Sensor_UIV.get(1), ItemList.Emitter_UIV.get(1) },
                new FluidStack[] { GGMaterial.metastableOganesson.getMolten(5760), Materials.Mellion.getMolten(2880),
                    Materials.Creon.getMolten(2880), Materials.SpaceTime.getMolten(576) },
                ItemList.Heliocast_Reinforcement_ExoFoundry.get(1),
                60 * SECONDS,
                (int) TierEU.RECIPE_UIV);

            // Time Dilation System
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Magnetic_Chassis_T3_ExoFoundry.get(1),
                128_000_000,
                16_000,
                (int) TierEU.RECIPE_MAX,
                256,
                new Object[] { ItemList.Magnetic_Chassis_T3_ExoFoundry.get(4),
                    CustomItemList.StabilisationFieldGeneratorTier8.get(32), ItemList.Timepiece.get(64),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Eternity, 64),
                    ItemList.Field_Generator_UXV.get(16),
                    GTOreDictUnificator.get(OrePrefixes.nanite, Materials.MagMatter, 4),
                    CustomItemList.eM_dynamoTunnel7_UXV.get(1), new ItemStack(lscLapotronicEnergyUnit, 1, 10) },
                new FluidStack[] { Materials.Time.getMolten(4_000_000), Materials.Space.getMolten(4_000_000),
                    Materials.PhononMedium.getFluid(1_000_000), Materials.Universium.getMolten(1_000_000) },
                ItemList.Universal_Collapser_ExoFoundry.get(1),
                60 * SECONDS,
                (int) TierEU.RECIPE_UXV); // <-- maybe make this MAX voltage
        }
    }
}
