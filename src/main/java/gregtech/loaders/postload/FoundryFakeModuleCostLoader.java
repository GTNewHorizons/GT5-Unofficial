package gregtech.loaders.postload;

import static gregtech.api.util.GTRecipeConstants.FOUNDRY_MODULE;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import goodgenerator.util.ItemRefer;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.materials.BlockShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.TEBlockShapes;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTUtility;
import gregtech.api.util.OreDictUtils;
import gregtech.common.tileentities.machines.multi.foundry.FoundryModule;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import tectech.thing.CustomItemList;
import tectech.thing.block.BlockGodforgeGlass;

public class FoundryFakeModuleCostLoader {

    public static void load() {
        OreDictUtils.registerOre("exoFoundryCasingChassis", ItemList.Magnetic_Chassis_T1_ExoFoundry.get(1));
        OreDictUtils.registerOre("exoFoundryCasingChassis", ItemList.Magnetic_Chassis_T2_ExoFoundry.get(1));
        OreDictUtils.registerOre("exoFoundryCasingChassis", ItemList.Magnetic_Chassis_T3_ExoFoundry.get(1));

        // ECB
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Extra_Casting_Basins_ExoFoundry.get(64),
                ItemList.Casing_Fluid_Solidifier.get(64),
                ItemList.Primary_Casing_ExoFoundry.get(36),
                ItemList.Radiator_Fluid_Solidifier.get(32),
                MaterialLibAPI.getStack(Materials.PreciousMetalsAlloy, BlockShapes.blockCasing, 8),
                MaterialLibAPI.getStack(Materials.PreciousMetalsAlloy, BlockShapes.blockCasingAdvanced, 4),
                MaterialLibAPI.getStack(Materials.PreciousMetalsAlloy, BlockShapes.sheetmetal, 8),
                MaterialLibAPI.getStack(Materials.Erbium, BlockShapes.sheetmetal, 8),
                MaterialLibAPI.getStack(Materials.Erbium, TEBlockShapes.frameGt, 28))
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
                MaterialLibAPI.getStack(Materials.WhiteDwarfMatter, BlockShapes.block, 28),
                MaterialLibAPI.getStack(Materials.BlackDwarfMatter, BlockShapes.block, 28),
                MaterialLibAPI.getStack(Materials.MHDCSM, BlockShapes.block, 4),
                MaterialLibAPI.getStack(Materials.MHDCSM, TEBlockShapes.frameGt, 40),
                MaterialLibAPI.getStack(Materials.Universium, TEBlockShapes.frameGt, 16),
                MaterialLibAPI.getStack(Materials.WhiteDwarfMatter, TEBlockShapes.frameGt, 8),
                MaterialLibAPI.getStack(Materials.BlackDwarfMatter, TEBlockShapes.frameGt, 8))
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
                MaterialLibAPI.getStack(Materials.TengamAttuned, BlockShapes.sheetmetal, 16),
                MaterialLibAPI.getStack(Materials.Samarium, BlockShapes.sheetmetal, 16),
                MaterialLibAPI.getStack(Materials.Quantium, BlockShapes.sheetmetal, 16),
                MaterialLibAPI.getStack(Materials.Dysprosium, TEBlockShapes.frameGt, 32),
                MaterialLibAPI.getStack(Materials.TengamAttuned, TEBlockShapes.frameGt, 4))
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
                MaterialLibAPI.getStack(Materials.Naquadria, TEBlockShapes.frameGt, 16))
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
                MaterialLibAPI.getStack(Materials.SuperconductorUEVBase, BlockShapes.sheetmetal, 16),
                MaterialLibAPI.getStack(Materials.SuperconductorUEVBase, TEBlockShapes.frameGt, 28),
                MaterialLibAPI.getStack(Materials.Tritanium, TEBlockShapes.frameGt, 24))
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
                MaterialLibAPI.getStack(Materials.SpaceTime, BlockShapes.sheetmetal, 16),
                MaterialLibAPI.getStack(Materials.SixPhasedCopper, TEBlockShapes.frameGt, 24),
                MaterialLibAPI.getStack(Materials.TranscendentMetal, TEBlockShapes.frameGt, 12),
                MaterialLibAPI.getStack(Materials.Creon, TEBlockShapes.frameGt, 12),
                MaterialLibAPI.getStack(Materials.Mellion, TEBlockShapes.frameGt, 12),
                MaterialLibAPI.getStack(Materials.SpaceTime, TEBlockShapes.frameGt, 12))
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
                MaterialLibAPI.getStack(Materials.CallistoIce, BlockShapes.sheetmetal, 64),
                MaterialLibAPI.getStack(Materials.SuperconductorUHVBase, BlockShapes.sheetmetal, 16),
                MaterialLibAPI.getStack(Materials.InfinityCatalyst, TEBlockShapes.frameGt, 48),
                ItemList.Hatch_Input_LV.get(1))
            .itemOutputs(ItemList.Hypercooler_ExoFoundry.get(1))
            .duration(1)
            .eut(1)
            .metadata(FOUNDRY_MODULE, FoundryModule.HYPERCOOLER)
            .fake()
            .addTo(RecipeMaps.foundryFakeModuleCostRecipes);
    }
}
