package gregtech.loaders.postload;

import static gregtech.api.util.GTRecipeConstants.FOUNDRY_MODULE;

import net.minecraft.item.ItemStack;

import goodgenerator.items.GGMaterial;
import goodgenerator.util.ItemRefer;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.tileentities.machines.multi.foundry.FoundryModule;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import tectech.thing.CustomItemList;
import tectech.thing.block.BlockGodforgeGlass;

public class FoundryFakeModuleCostLoader {

    public static void load() {
        GTOreDictUnificator.registerOre("exoFoundryCasingChassis", ItemList.Magnetic_Chassis_T1_ExoFoundry.get(1));
        GTOreDictUnificator.registerOre("exoFoundryCasingChassis", ItemList.Magnetic_Chassis_T2_ExoFoundry.get(1));
        GTOreDictUnificator.registerOre("exoFoundryCasingChassis", ItemList.Magnetic_Chassis_T3_ExoFoundry.get(1));

        // ECB
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Extra_Casting_Basins_ExoFoundry.get(64),
                ItemList.Casing_Fluid_Solidifier.get(64),
                ItemList.Primary_Casing_ExoFoundry.get(36),
                ItemList.Radiator_Fluid_Solidifier.get(32),
                GGMaterial.preciousMetalAlloy.get(OrePrefixes.blockCasing, 8),
                GGMaterial.preciousMetalAlloy.get(OrePrefixes.blockCasingAdvanced, 4),
                GGMaterial.preciousMetalAlloy.get(OrePrefixes.sheetmetal, 8),
                GTOreDictUnificator.get(OrePrefixes.sheetmetal, Materials.Erbium, 6),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Erbium, 30))
            .itemOutputs(ItemList.Extra_Casting_Basins_ExoFoundry.get(1))
            .duration(1)
            .eut(1)
            .metadata(FOUNDRY_MODULE, FoundryModule.EXTRA_CASTING_BASINS)
            .fake()
            .addTo(RecipeMaps.foundryFakeModuleCostRecipes);

        // UC
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Universal_Collapser_ExoFoundry.get(4),
                GTUtility.copyAmountUnsafe(72, CustomItemList.EOH_Reinforced_Spatial_Casing.get(1)),
                CustomItemList.EOH_Reinforced_Temporal_Casing.get(48),
                Materials.WhiteDwarfMatter.getBlocks(28),
                Materials.BlackDwarfMatter.getBlocks(28),
                Materials.MHDCSM.getBlocks(4),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.MHDCSM, 40),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Universium, 16),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.WhiteDwarfMatter, 8),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.BlackDwarfMatter, 8))
            .itemOutputs(ItemList.Universal_Collapser_ExoFoundry.get(1))
            .duration(1)
            .eut(1)
            .metadata(FOUNDRY_MODULE, FoundryModule.UNIVERSAL_COLLAPSER)
            .fake()
            .addTo(RecipeMaps.foundryFakeModuleCostRecipes);

        // PES
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Power_Efficient_Subsystems_ExoFoundry.get(64),
                GregtechItemList.Casing_Machine_Custom_4.get(28), // Rugged Botmium Machine Casing
                ItemList.Casing_Item_Pipe_Quantium.get(4),
                GTOreDictUnificator.get(OrePrefixes.sheetmetal, Materials.TengamAttuned, 16),
                GTOreDictUnificator.get(OrePrefixes.sheetmetal, Materials.Samarium, 16),
                GTOreDictUnificator.get(OrePrefixes.sheetmetal, Materials.Quantium, 16),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Dysprosium, 32),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.TengamAttuned, 4))
            .itemOutputs(ItemList.Power_Efficient_Subsystems_ExoFoundry.get(1))
            .duration(1)
            .eut(1)
            .metadata(FOUNDRY_MODULE, FoundryModule.POWER_EFFICIENT_SUBSYSTEMS)
            .fake()
            .addTo(RecipeMaps.foundryFakeModuleCostRecipes);

        // EOC
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Efficient_Overclocking_ExoFoundry.get(40),
                ItemRefer.MagneticFluxCasing.get(72),
                ItemRefer.GravityStabilizationCasing.get(48),
                ItemRefer.AntimatterContainmentCasing.get(36),
                new Object[] { "exoFoundryCasingChassis", 24 },
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Naquadria, 16))
            .itemOutputs(ItemList.Efficient_Overclocking_ExoFoundry.get(1))
            .duration(1)
            .eut(1)
            .metadata(FOUNDRY_MODULE, FoundryModule.EFFICIENT_OC)
            .fake()
            .addTo(RecipeMaps.foundryFakeModuleCostRecipes);

        // SLC
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Streamlined_Casters_ExoFoundry.get(64),
                GregtechItemList.Casing_ElementalDuplicator.get(12),
                GregtechItemList.Casing_Molecular_Transformer_3.get(12),
                GTOreDictUnificator.get(OrePrefixes.sheetmetal, Materials.SuperconductorUEVBase, 16),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SuperconductorUEVBase, 28),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Tritanium, 24))
            .itemOutputs(ItemList.Streamlined_Casters_ExoFoundry.get(1))
            .duration(1)
            .eut(1)
            .metadata(FOUNDRY_MODULE, FoundryModule.STREAMLINED_CASTERS)
            .fake()
            .addTo(RecipeMaps.foundryFakeModuleCostRecipes);

        // HR
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Heliocast_Reinforcement_ExoFoundry.get(12),
                CustomItemList.Godforge_MagneticConfinementCasing.get(32),
                new ItemStack(BlockGodforgeGlass.INSTANCE, 16),
                GTOreDictUnificator.get(OrePrefixes.sheetmetal, Materials.SpaceTime, 16),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SixPhasedCopper, 24),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.TranscendentMetal, 12),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Creon, 12),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Mellion, 12),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SpaceTime, 12))
            .itemOutputs(ItemList.Heliocast_Reinforcement_ExoFoundry.get(1))
            .duration(1)
            .eut(1)
            .metadata(FOUNDRY_MODULE, FoundryModule.HELIOCAST_REINFORCEMENT)
            .fake()
            .addTo(RecipeMaps.foundryFakeModuleCostRecipes);

        // HC
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hypercooler_ExoFoundry.get(36),
                ItemList.GlassQuarkContainment.get(32),
                ItemList.InfinityCooledCasing.get(20),
                GregtechItemList.Casing_AdvancedVacuum.get(19),
                GTOreDictUnificator.get(OrePrefixes.sheetmetal, Materials.CallistoIce, 64),
                GTOreDictUnificator.get(OrePrefixes.sheetmetal, Materials.SuperconductorUHVBase, 16),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.InfinityCatalyst, 48),
                ItemList.Hatch_Input_LV.get(1))
            .itemOutputs(ItemList.Hypercooler_ExoFoundry.get(1))
            .duration(1)
            .eut(1)
            .metadata(FOUNDRY_MODULE, FoundryModule.HYPERCOOLER)
            .fake()
            .addTo(RecipeMaps.foundryFakeModuleCostRecipes);
    }
}
