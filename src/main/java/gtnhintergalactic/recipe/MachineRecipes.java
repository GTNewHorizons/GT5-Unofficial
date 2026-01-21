package gtnhintergalactic.recipe;

import static gregtech.api.enums.GTValues.RA;
import static gregtech.api.enums.Mods.GalacticraftAmunRa;
import static gregtech.api.enums.Mods.GalacticraftCore;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.Mods.OpenBlocks;
import static gregtech.api.enums.TickTime.MINUTE;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.STACKS;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import bartworks.common.loaders.ItemRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import goodgenerator.items.GGMaterial;
import goodgenerator.util.ItemRefer;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.util.RecipeUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import tectech.recipe.TTRecipeAdder;
import tectech.thing.CustomItemList;
import tectech.thing.casing.TTCasingsContainer;

public class MachineRecipes implements Runnable {

    @Override
    public void run() {

        // exit early if not in pack
        if (!NewHorizonsCoreMod.isModLoaded() || !GalacticraftCore.isModLoaded()) return;

        ItemStack hypogenFrameBox_8 = MaterialsElements.STANDALONE.HYPOGEN.getFrameBox(8);
        ItemStack hypogenScrew_32 = MaterialsElements.STANDALONE.HYPOGEN.getScrew(32);
        Fluid hypogenFluid = MaterialsElements.STANDALONE.HYPOGEN.getFluid();
        Fluid celestialTungstenFluid = MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFluid();

        ItemStack preciseAssembler_1 = ItemRefer.Precise_Assembler.get(1);
        ItemStack highComputationStationT3_32 = ItemRefer.HiC_T3.get(32);
        ItemStack highComputationStationT4_32 = ItemRefer.HiC_T4.get(32);
        ItemStack highComputationStationT5_32 = ItemRefer.HiC_T5.get(32);
        ItemStack metaStableOgScrew_64 = GGMaterial.metastableOganesson.get(OrePrefixes.screw, 64);
        ItemStack titaniumBetaCScrew_64 = GGMaterial.titaniumBetaC.get(OrePrefixes.screw, 64);

        ItemStack voidMiner = ItemRegistry.voidminer[2];

        // Planetary Gas Siphon Controller
        RecipeUtil.addRecipe(
            ItemList.PlanetaryGasSiphonController.get(1),
            new Object[] { "MPM", "CTC", "HTH", 'M', ItemList.Electric_Motor_IV.get(1), 'P',
                ItemList.Electric_Pump_IV.get(1), 'C', "circuitElite", 'T',
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.TungstenSteel, 1), 'H',
                ItemList.Hull_IV.get(1) });

        // Space Elevator Controller
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GTModHandler.getModItem(OpenBlocks.ID, "elevator", 1, 0),
            256000,
            256,
            1000000,
            4,
            new Object[] { GTModHandler.getModItem(OpenBlocks.ID, "elevator", 1),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 64),
                ItemList.Field_Generator_UV.get(16), new Object[] { OrePrefixes.circuit.get(Materials.UHV), 16 },
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "HeavyDutyPlateTier7", 32),
                ItemList.Circuit_Chip_PPIC.get(64),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.CosmicNeutronium, 64),
                ItemList.Electric_Motor_UV.get(32), ItemList.SpaceElevatorBaseCasing.get(8) },
            new FluidStack[] { MaterialsAlloy.INDALLOY_140.getFluidStack(40 * INGOTS),
                Materials.UUMatter.getFluid(16_000), Materials.Lubricant.getFluid(32_000),
                Materials.Neutronium.getMolten(10 * INGOTS) },
            ItemList.SpaceElevatorController.get(1),
            5 * MINUTE,
            (int) TierEU.RECIPE_UHV);

        // Nanotube spool
        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.Graphene, 64),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.Graphene, 64),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.Graphene, 64),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.Graphene, 64))
            .itemOutputs(ItemList.NanotubeSpool.get(1))
            .fluidInputs(Materials.GlueAdvanced.getFluid(720))
            .duration(1 * MINUTE)
            .eut(TierEU.RECIPE_ZPM)
            .requiresCleanRoom()
            .addTo(assemblerRecipes);

        // Space Elevator Cable
        RA.stdBuilder()
            .itemInputs(ItemList.NanotubeSpool.get(64), GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UHV, 4))
            .fluidInputs(MaterialMisc.ETHYL_CYANOACRYLATE.getFluidStack(8_000))
            .itemOutputs(ItemList.SpaceElevatorCable.get(1))
            .duration(2 * MINUTE)
            .eut(TierEU.RECIPE_UHV)
            .addTo(assemblerRecipes);

        // Space Elevator Base Casing
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GTOreDictUnificator.get(OrePrefixes.block, Materials.Neutronium, 1),
            64000,
            128,
            (int) TierEU.RECIPE_UV,
            2,
            new Object[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 8),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.Palladium, 32),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 64),
                new Object[] { OrePrefixes.circuit.get(Materials.UHV), 4 }, ItemList.Electric_Piston_UV.get(2),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.CosmicNeutronium, 8), },
            new FluidStack[] { MaterialsAlloy.INDALLOY_140.getFluidStack(40 * INGOTS),
                Materials.UUMatter.getFluid(2_000), Materials.Iridium.getMolten(8 * INGOTS) },
            ItemList.SpaceElevatorBaseCasing.get(8),
            1 * MINUTE,
            (int) TierEU.RECIPE_UV);

        // Space Elevator Support Structure
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 1),
            64000,
            128,
            (int) TierEU.RECIPE_UV,
            2,
            new Object[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 8),
                GTOreDictUnificator.get(OrePrefixes.bolt, Materials.Naquadria, 16),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Neutronium, 8),
                GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.Osmiridium, 8), },
            new FluidStack[] { MaterialsAlloy.INDALLOY_140.getFluidStack(40 * INGOTS),
                Materials.UUMatter.getFluid(1_000), Materials.Iridium.getMolten(10 * INGOTS) },
            ItemList.SpaceElevatorSupportStructure.get(8),
            1 * MINUTE,
            (int) TierEU.RECIPE_UV);

        // Space Elevator Internal Structure
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            new ItemStack(TTCasingsContainer.sBlockCasingsTT, 1, 0),
            64000,
            128,
            (int) TierEU.RECIPE_UV,
            2,
            new Object[] { new ItemStack(TTCasingsContainer.sBlockCasingsTT, 8, 0),
                GTOreDictUnificator.get(OrePrefixes.bolt, Materials.Palladium, 16),
                GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.Neutronium, 8), },
            new FluidStack[] { MaterialsAlloy.INDALLOY_140.getFluidStack(40 * INGOTS),
                Materials.UUMatter.getFluid(8_000), Materials.Concrete.getMolten(10 * INGOTS) },
            ItemList.SpaceElevatorInternalStructure.get(8),
            1 * MINUTE,
            (int) TierEU.RECIPE_UV);

        // Space Elevator Motor MK-I
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.SpaceElevatorBaseCasing.get(1),
            64000,
            128,
            (int) TierEU.RECIPE_UV,
            2,
            new Object[] { ItemList.SpaceElevatorBaseCasing.get(1), ItemList.Electric_Motor_UV.get(4),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.Neutronium, 8),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Neutronium, 4),
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 1 },
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.CosmicNeutronium, 16),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 16), },
            new FluidStack[] { MaterialsAlloy.INDALLOY_140.getFluidStack(40 * INGOTS),
                Materials.UUMatter.getFluid(8_000), Materials.Naquadria.getMolten(10 * INGOTS),
                Materials.Lubricant.getFluid(16_000) },
            ItemList.SpaceElevatorMotorT1.get(1),
            1 * MINUTE,
            (int) TierEU.RECIPE_UV);

        // Space Elevator Motor MK-II
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.SpaceElevatorMotorT1.get(1),
            128000,
            256,
            (int) TierEU.RECIPE_UHV,
            2,
            new Object[] { ItemList.SpaceElevatorBaseCasing.get(1), ItemList.Electric_Motor_UHV.get(4),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.CosmicNeutronium, 8),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.CosmicNeutronium, 4),
                new Object[] { OrePrefixes.circuit.get(Materials.UHV), 1 },
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.Infinity, 16),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 16), },
            new FluidStack[] { MaterialsAlloy.INDALLOY_140.getFluidStack(40 * INGOTS),
                Materials.UUMatter.getFluid(8_000), Materials.Naquadria.getMolten(10 * INGOTS),
                Materials.Lubricant.getFluid(16_000) },
            ItemList.SpaceElevatorMotorT2.get(1),
            1 * MINUTE,
            (int) TierEU.RECIPE_UHV);

        // Space Elevator Motor MK-III
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.SpaceElevatorMotorT2.get(1),
            128000,
            256,
            (int) TierEU.RECIPE_UHV,
            2,
            new Object[] { ItemList.SpaceElevatorBaseCasing.get(1), ItemList.Electric_Motor_UEV.get(4),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.Infinity, 8),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Infinity, 4),
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 1 },
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.TranscendentMetal, 16),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 16), },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(20 * INGOTS),
                Materials.UUMatter.getFluid(8_000), Materials.Naquadria.getMolten(10 * INGOTS),
                Materials.DimensionallyShiftedSuperfluid.getFluid(24_000) },
            ItemList.SpaceElevatorMotorT3.get(1),
            1 * MINUTE,
            (int) TierEU.RECIPE_UEV);

        // Space Elevator Motor MK-IV
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.SpaceElevatorMotorT3.get(1),
            128000,
            256,
            (int) TierEU.RECIPE_UHV,
            2,
            new Object[] { ItemList.SpaceElevatorBaseCasing.get(1), ItemList.Electric_Motor_UIV.get(4),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.TranscendentMetal, 8),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.TranscendentMetal, 4),
                new Object[] { OrePrefixes.circuit.get(Materials.UIV), 1 },
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.SpaceTime, 16),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.ProtoHalkonite, 16), },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(20 * INGOTS),
                Materials.UUMatter.getFluid(8_000), Materials.Naquadria.getMolten(10 * INGOTS),
                Materials.DimensionallyShiftedSuperfluid.getFluid(24_000) },
            ItemList.SpaceElevatorMotorT4.get(1),
            1 * MINUTE,
            (int) TierEU.RECIPE_UIV);

        // Space Elevator Motor MK-V
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.SpaceElevatorMotorT4.get(1),
            128000,
            256,
            (int) TierEU.RECIPE_UHV,
            2,
            new Object[] { ItemList.SpaceElevatorBaseCasing.get(1), ItemList.Electric_Motor_UMV.get(4),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.SpaceTime, 8),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.SpaceTime, 4),
                new Object[] { OrePrefixes.circuit.get(Materials.UMV), 1L },
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.Universium, 16),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 16), },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(20 * INGOTS),
                Materials.UUMatter.getFluid(8_000), Materials.Naquadria.getMolten(10 * INGOTS),
                Materials.DimensionallyShiftedSuperfluid.getFluid(24_000) },
            ItemList.SpaceElevatorMotorT5.get(1),
            1 * MINUTE,
            (int) TierEU.RECIPE_UIV);

        // Space Elevator Modules

        // Pump Module MK-I
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.PlanetaryGasSiphonController.get(1),
            16777216,
            2048,
            (int) TierEU.RECIPE_UHV,
            4,
            new Object[] { ItemList.OilDrillInfinite.get(1), ItemList.PlanetaryGasSiphonController.get(1),
                CustomItemList.enderLinkFluidCover.get(2),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 4),
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 4 }, ItemList.Electric_Pump_UEV.get(2),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Infinity, 4),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.CosmicNeutronium, 32), },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(9 * INGOTS),
                Materials.Infinity.getMolten(4 * INGOTS) },
            ItemList.SpaceElevatorModulePumpT1.get(1),
            2 * MINUTE,
            (int) TierEU.RECIPE_UEV);

        // Pump Module MK-II
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.SpaceElevatorModulePumpT1.get(1),
            33554432,
            8192,
            64000000,
            4,
            new Object[] { ItemList.OilDrillInfinite.get(4), ItemList.PlanetaryGasSiphonController.get(4),
                CustomItemList.enderLinkFluidCover.get(8),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SpaceTime, 4),
                new Object[] { OrePrefixes.circuit.get(Materials.UIV), 16 }, ItemList.Electric_Pump_UIV.get(8),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.SpaceTime, 4), metaStableOgScrew_64, },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(32 * INGOTS),
                Materials.MoltenProtoHalkoniteBase.getFluid(16 * INGOTS) },
            ItemList.SpaceElevatorModulePumpT2.get(1),
            2 * MINUTE,
            (int) TierEU.RECIPE_UIV);

        // Pump Module MK-II - Assembler alt
        RA.stdBuilder()
            .itemInputs(
                ItemList.SpaceElevatorModulePumpT1.get(4),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SpaceTime, 8),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UIV, 16),
                ItemList.Electric_Pump_UIV.get(8),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.SpaceTime, 8),
                metaStableOgScrew_64,
                GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.ProtoHalkonite, 16))
            .itemOutputs(ItemList.SpaceElevatorModulePumpT2.get(1))
            .fluidInputs(MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(32 * INGOTS))
            .duration(2 * MINUTE)
            .eut(TierEU.RECIPE_UIV)
            .addTo(assemblerRecipes);

        // Pump Module MK-III
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.SpaceElevatorModulePumpT2.get(1),
            67108864,
            32767,
            256000000,
            4,
            new Object[] { ItemList.OilDrillInfinite.get(16), ItemList.PlanetaryGasSiphonController.get(16),
                CustomItemList.enderLinkFluidCover.get(32),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.BlackDwarfMatter, 4),
                new Object[] { OrePrefixes.circuit.get(Materials.UMV), 16 }, ItemList.Electric_Pump_UMV.get(8),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.BlackDwarfMatter, 4),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.WhiteDwarfMatter, 64) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(1 * STACKS),
                Materials.Eternity.getMolten(16 * INGOTS) },
            ItemList.SpaceElevatorModulePumpT3.get(1),
            2 * MINUTE,
            (int) TierEU.RECIPE_UMV);

        // Pump Module MK-III - Assembler alt
        RA.stdBuilder()
            .itemInputs(
                ItemList.SpaceElevatorModulePumpT2.get(4),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.BlackDwarfMatter, 8),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UMV, 16),
                ItemList.Electric_Pump_UMV.get(8),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.BlackDwarfMatter, 8),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.WhiteDwarfMatter, 64),
                GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.Eternity, 16))
            .itemOutputs(ItemList.SpaceElevatorModulePumpT3.get(1))
            .fluidInputs(MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(1 * STACKS))
            .duration(2 * MINUTE)
            .eut(TierEU.RECIPE_UIV)
            .addTo(assemblerRecipes);

        // Assembler Module MK-I
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            preciseAssembler_1,
            256000,
            256,
            4000000,
            4,
            new Object[] { ItemList.SpaceElevatorBaseCasing.get(1),
                new ItemStack(GameRegistry.findItem("gregtech", "gt.blockmachines"), 4, 10782),
                new ItemStack(GameRegistry.findItem("gregtech", "gt.blockmachines"), 4, 1187),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.CosmicNeutronium, 8),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.CosmicNeutronium, 16),
                ItemList.Robot_Arm_UHV.get(8), ItemList.Conveyor_Module_UHV.get(16), highComputationStationT3_32,
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 16 },
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 8),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.Neutronium, 32) },
            new FluidStack[] { MaterialsAlloy.INDALLOY_140.getFluidStack(9 * INGOTS),
                Materials.Naquadria.getMolten(9 * INGOTS), Materials.Lubricant.getFluid(16_000) },
            ItemList.SpaceElevatorModuleAssemblerT1.get(1),
            2 * MINUTE,
            (int) TierEU.RECIPE_UEV);

        // Assembler Module MK-II
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.SpaceElevatorModuleAssemblerT1.get(1),
            2048000,
            2048,
            64000000,
            4,
            new Object[] { ItemList.SpaceElevatorBaseCasing.get(1),
                new ItemStack(GameRegistry.findItem("gregtech", "gt.blockmachines"), 4, 10784),
                new ItemStack(GameRegistry.findItem("gregtech", "gt.blockmachines"), 4, 12091),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.TranscendentMetal, 8),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.ProtoHalkonite, 16),
                ItemList.Robot_Arm_UIV.get(8), ItemList.Conveyor_Module_UIV.get(16), highComputationStationT4_32,
                new Object[] { OrePrefixes.circuit.get(Materials.UIV), 16 }, hypogenFrameBox_8, hypogenScrew_32 },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(9 * INGOTS),
                Materials.Infinity.getMolten(9 * INGOTS), Materials.UUMatter.getFluid(16_000) },
            ItemList.SpaceElevatorModuleAssemblerT2.get(1),
            2 * MINUTE,
            (int) TierEU.RECIPE_UIV);

        // Assembler Module MK-III
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.SpaceElevatorModuleAssemblerT2.get(1),
            32768000,
            4096,
            256000000,
            4,
            new Object[] { ItemList.SpaceElevatorBaseCasing.get(1),
                new ItemStack(GameRegistry.findItem("gregtech", "gt.blockmachines"), 4, 10786),
                new ItemStack(GameRegistry.findItem("gregtech", "gt.blockmachines"), 4, 12093),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.MHDCSM, 8),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.MagMatter, 8),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.MHDCSM, 16),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.MagMatter, 16),
                ItemList.Robot_Arm_UXV.get(8), ItemList.Conveyor_Module_UXV.get(16), highComputationStationT5_32,
                new Object[] { OrePrefixes.circuit.get(Materials.UXV), 16 },
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Universium, 8),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.Universium, 32) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(36 * INGOTS),
                Materials.BlackDwarfMatter.getMolten(9 * INGOTS), Materials.WhiteDwarfMatter.getMolten(9 * INGOTS),
                Materials.SpaceTime.getMolten(9 * INGOTS) },
            ItemList.SpaceElevatorModuleAssemblerT3.get(1),
            2 * MINUTE,
            (int) TierEU.RECIPE_UXV);

        // TEMPORARY COMMENT-OUT UNTIL FEATURES ARE FULLY INTEGRATED TO PREVENT CRAFTING
        // // Research Module
        // TTRecipeAdder.addResearchableAssemblylineRecipe(
        // new ItemStack(GameRegistry.findItem("gregtech", "gt.blockmachines"), 1, 11012),
        // 512000,
        // 512,
        // 16000000,
        // 4,
        // new Object[] { CustomItemList.Machine_Multi_Research.get(4), ItemList.Sensor_UHV.get(4),
        // new Object[] { OrePrefixes.circuit.get(Materials.UHV), 16 },
        // GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Infinity, 4),
        // GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 4),
        // GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.CosmicNeutronium, 64),
        // GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.CosmicNeutronium, 64), metaStableOgScrew_64,
        // GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 32) },
        // new FluidStack[] { MaterialsAlloy.INDALLOY_140.getFluidStack(32 * INGOTS),
        // Materials.Infinity.getMolten(16 * INGOTS), Materials.UUMatter.getFluid(8_000),
        // Materials.SuperCoolant.getFluid(4_000) },
        // ItemList.SpaceElevatorModuleResearch.get(1),
        // 2 * MINUTE,
        // (int) TierEU.RECIPE_UEV);
        //
        // // Project Manager Module
        // TTRecipeAdder.addResearchableAssemblylineRecipe(
        // new ItemStack(GameRegistry.findItem("miscutils", "blockProjectBench"), 1),
        // 256000,
        // 512,
        // 1000000,
        // 4,
        // new Object[] { new ItemStack(GameRegistry.findItem("miscutils", "blockProjectBench"), 4),
        // ItemList.Emitter_UV.get(2), ItemList.Sensor_UV.get(2),
        // new Object[] { OrePrefixes.circuit.get(Materials.UV), 16 },
        // GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Neutronium, 4),
        // GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 4),
        // new ItemStack(GameRegistry.findItem("structurelib", "item.structurelib.constructableTrigger"), 64),
        // titaniumBetaCScrew_64, },
        // new FluidStack[] { MaterialsAlloy.INDALLOY_140.getFluidStack(32 * INGOTS),
        // Materials.Iridium.getMolten(16 * INGOTS), Materials.UUMatter.getFluid(2_000) },
        // ItemList.SpaceElevatorModuleManager.get(1),
        // 2 * MINUTE,
        // (int) TierEU.RECIPE_UHV);

        // Miner Module MK-I
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            voidMiner,
            2000000,
            512,
            (int) TierEU.RECIPE_UHV,
            8,
            new Object[] { ItemList.OreDrill4.get(1), ItemList.Robot_Arm_UV.get(8), ItemList.Field_Generator_UV.get(4),
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 16 }, ItemList.Sensor_UV.get(16),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUV, 32),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 16) },
            new FluidStack[] { MaterialsAlloy.INDALLOY_140.getFluidStack(20 * INGOTS),
                Materials.Naquadria.getMolten(10 * INGOTS), Materials.Lubricant.getFluid(8_000) },
            ItemList.SpaceElevatorModuleMinerT1.get(1),
            2 * MINUTE,
            (int) TierEU.RECIPE_UHV);
        // Miner Module MK-II
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.SpaceElevatorModuleMinerT1.get(1),
            3000000,
            1024,
            3000000,
            12,
            new Object[] { ItemList.SpaceElevatorModuleMinerT1.get(1), ItemList.Robot_Arm_UHV.get(8),
                ItemList.Field_Generator_UHV.get(4), new Object[] { OrePrefixes.circuit.get(Materials.UHV), 16 },
                ItemList.Sensor_UHV.get(16),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 32),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 16) },
            new FluidStack[] { MaterialsAlloy.INDALLOY_140.getFluidStack(20 * INGOTS),
                Materials.Naquadria.getMolten(20 * INGOTS), Materials.Lubricant.getFluid(16_000) },
            ItemList.SpaceElevatorModuleMinerT2.get(1),
            2 * MINUTE,
            (int) TierEU.RECIPE_UEV);
        // Miner Module MK-III
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.SpaceElevatorModuleMinerT2.get(1),
            4000000,
            2048,
            4000000,
            16,
            new Object[] { ItemList.SpaceElevatorModuleMinerT2.get(1), ItemList.Robot_Arm_UEV.get(8),
                ItemList.Field_Generator_UEV.get(4), new Object[] { OrePrefixes.circuit.get(Materials.UEV), 16 },
                ItemList.Sensor_UEV.get(16),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUEV, 32),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 16) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(20 * INGOTS),
                Materials.TranscendentMetal.getMolten(10 * INGOTS), Materials.UUMatter.getFluid(2_000) },
            ItemList.SpaceElevatorModuleMinerT3.get(1),
            2 * MINUTE,
            (int) TierEU.RECIPE_UIV);

        // Mining drones

        // LV
        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.toolHeadDrill, Materials.Titanium, 8),
                ItemList.Robot_Arm_LV.get(8),
                ItemList.Field_Generator_LV.get(2),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.HV, 4),
                new ItemStack(GCItems.heavyPlatingTier1, 16),
                new ItemStack(GCItems.rocketEngine, 2),
                ItemList.Sensor_LV.get(8))
            .itemOutputs(ItemList.MiningDroneLV.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(5 * INGOTS))
            .duration(1 * MINUTE)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        // MV
        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.toolHeadDrill, Materials.TungstenSteel, 8),
                ItemList.Robot_Arm_MV.get(8),
                ItemList.Field_Generator_MV.get(2),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.EV, 4),
                new ItemStack(GCItems.heavyPlatingTier1, 32),
                new ItemStack(GCItems.rocketEngine, 4),
                ItemList.Sensor_MV.get(8))
            .itemOutputs(ItemList.MiningDroneMV.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(10 * INGOTS))
            .duration(1 * MINUTE)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        // HV
        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.toolHeadDrill, Materials.Iridium, 8),
                ItemList.Robot_Arm_HV.get(8),
                ItemList.Field_Generator_HV.get(2),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 4),
                new ItemStack(MarsItems.marsItemBasic, 32, 3),
                new ItemStack(GCItems.rocketEngine, 4),
                ItemList.Sensor_HV.get(8))
            .itemOutputs(ItemList.MiningDroneHV.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(10 * INGOTS))
            .duration(1 * MINUTE)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        // EV
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.MiningDroneHV.get(1),
            50000,
            128,
            1000000,
            4,
            new Object[] { GTOreDictUnificator.get(OrePrefixes.toolHeadDrill, Materials.Trinium, 8),
                ItemList.Robot_Arm_EV.get(8), ItemList.Field_Generator_EV.get(2),
                new Object[] { OrePrefixes.circuit.get(Materials.LuV), 4 },
                new ItemStack(AsteroidsItems.basicItem, 32, 0), new ItemStack(AsteroidsItems.basicItem, 4, 1),
                ItemList.Sensor_EV.get(8) },
            new FluidStack[] { MaterialsAlloy.INDALLOY_140.getFluidStack(5 * INGOTS),
                Materials.Iridium.getMolten(5 * INGOTS),
                new FluidStack(FluidRegistry.getFluid("liquid_drillingfluid"), 16_000) },
            ItemList.MiningDroneEV.get(1),
            1 * MINUTE,
            (int) TierEU.RECIPE_EV);

        // IV
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.MiningDroneEV.get(1),
            75000,
            128,
            1000000,
            8,
            new Object[] { GTOreDictUnificator.get(OrePrefixes.toolHeadDrill, Materials.NaquadahAlloy, 8),
                ItemList.Robot_Arm_IV.get(8), ItemList.Field_Generator_IV.get(2),
                new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 4 },
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "HeavyDutyPlateTier4", 32),
                new ItemStack(AsteroidsItems.basicItem, 4, 1), ItemList.Sensor_IV.get(8) },
            new FluidStack[] { MaterialsAlloy.INDALLOY_140.getFluidStack(10 * INGOTS),
                Materials.Iridium.getMolten(10 * INGOTS),
                new FluidStack(FluidRegistry.getFluid("liquid_drillingfluid"), 32_000) },
            ItemList.MiningDroneIV.get(1),
            1 * MINUTE,
            (int) TierEU.RECIPE_IV);

        // LuV
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.MiningDroneIV.get(1),
            100000,
            256,
            (int) TierEU.RECIPE_UHV,
            4,
            new Object[] { GTOreDictUnificator.get(OrePrefixes.toolHeadDrill, Materials.Naquadria, 8),
                ItemList.Robot_Arm_LuV.get(8), ItemList.Field_Generator_LuV.get(2),
                new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 4 },
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "HeavyDutyPlateTier5", 32),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "HeavyDutyRocketEngineTier3", 4),
                ItemList.Sensor_LuV.get(8) },
            new FluidStack[] { MaterialsAlloy.INDALLOY_140.getFluidStack(20 * INGOTS),
                Materials.Osmiridium.getMolten(10 * INGOTS),
                new FluidStack(FluidRegistry.getFluid("liquid_drillingfluid"), 64_000) },
            ItemList.MiningDroneLuV.get(1),
            1 * MINUTE,
            (int) TierEU.RECIPE_LuV);

        // ZPM
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.MiningDroneLuV.get(1),
            125000,
            256,
            (int) TierEU.RECIPE_UHV,
            8,
            new Object[] { GTOreDictUnificator.get(OrePrefixes.toolHeadDrill, Materials.Neutronium, 8),
                ItemList.Robot_Arm_ZPM.get(8), ItemList.Field_Generator_ZPM.get(2),
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 2 },
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "HeavyDutyPlateTier6", 32),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "HeavyDutyRocketEngineTier3", 4),
                ItemList.Sensor_ZPM.get(8) },
            new FluidStack[] { MaterialsAlloy.INDALLOY_140.getFluidStack(20 * INGOTS),
                Materials.Osmiridium.getMolten(10 * INGOTS),
                new FluidStack(FluidRegistry.getFluid("liquid_drillingfluid"), 128_000) },
            ItemList.MiningDroneZPM.get(1),
            1 * MINUTE,
            (int) TierEU.RECIPE_ZPM);

        // UV
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.MiningDroneZPM.get(1),
            150000,
            512,
            4000000,
            4,
            new Object[] { GTOreDictUnificator.get(OrePrefixes.toolHeadDrill, Materials.CosmicNeutronium, 8),
                ItemList.Robot_Arm_UV.get(8), ItemList.Field_Generator_UV.get(2),
                new Object[] { OrePrefixes.circuit.get(Materials.UHV), 4 },
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "HeavyDutyPlateTier7", 32),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "HeavyDutyRocketEngineTier4", 4),
                ItemList.Sensor_UV.get(8) },
            new FluidStack[] { MaterialsAlloy.INDALLOY_140.getFluidStack(20 * INGOTS),
                Materials.Naquadria.getMolten(10 * INGOTS),
                new FluidStack(FluidRegistry.getFluid("liquid_drillingfluid"), 256_000) },
            ItemList.MiningDroneUV.get(1),
            1 * MINUTE,
            (int) TierEU.RECIPE_UV);

        // UHV
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.MiningDroneUV.get(1),
            175000,
            512,
            4000000,
            8,
            new Object[] { GTOreDictUnificator.get(OrePrefixes.toolHeadDrill, Materials.Infinity, 8),
                ItemList.Robot_Arm_UHV.get(8), ItemList.Field_Generator_UHV.get(2),
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 4 },
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "HeavyDutyPlateTier8", 32),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "HeavyDutyRocketEngineTier4", 4),
                ItemList.Sensor_UHV.get(8) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(20 * INGOTS),
                Materials.Neutronium.getMolten(10 * INGOTS),
                new FluidStack(FluidRegistry.getFluid("liquid_drillingfluid"), 512_000) },
            ItemList.MiningDroneUHV.get(1),
            1 * MINUTE,
            (int) TierEU.RECIPE_UHV);

        ItemStack t9Plate = GTModHandler.getModItem(GalacticraftAmunRa.ID, "item.baseItem", 1, 15);

        // UEV
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.MiningDroneUHV.get(1),
            200000,
            512,
            4000000,
            8,
            new Object[] { GTOreDictUnificator.get(OrePrefixes.toolHeadDrill, Materials.CosmicNeutronium, 8),
                ItemList.Robot_Arm_UEV.get(8), ItemList.Field_Generator_UEV.get(2),
                new Object[] { OrePrefixes.circuit.get(Materials.UIV), 4 }, GTUtility.copyAmount(32, t9Plate),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "HeavyDutyRocketEngineTier4", 8),
                ItemList.Sensor_UEV.get(8) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(20 * INGOTS),
                Materials.Quantium.getMolten(10 * INGOTS),
                new FluidStack(FluidRegistry.getFluid("liquid_drillingfluid"), 512_000) },
            ItemList.MiningDroneUEV.get(1),
            1 * MINUTE,
            (int) TierEU.RECIPE_UEV);

        // UIV
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.MiningDroneUEV.get(1),
            225000,
            512,
            (int) TierEU.RECIPE_UEV,
            4,
            new Object[] { GTOreDictUnificator.get(OrePrefixes.toolHeadDrill, Materials.CosmicNeutronium, 8),
                ItemList.Robot_Arm_UIV.get(8), ItemList.Field_Generator_UIV.get(2),
                new Object[] { OrePrefixes.circuit.get(Materials.UMV), 4 }, GTUtility.copyAmount(64, t9Plate),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "HeavyDutyRocketEngineTier4", 16),
                ItemList.Sensor_UIV.get(8) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(40 * INGOTS),
                Materials.Quantium.getMolten(20 * INGOTS),
                new FluidStack(FluidRegistry.getFluid("liquid_drillingfluid"), 512_000) },
            ItemList.MiningDroneUIV.get(1),
            1 * MINUTE,
            (int) TierEU.RECIPE_UIV);

        // UMV
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.MiningDroneUIV.get(1),
            250000,
            512,
            (int) TierEU.RECIPE_UEV,
            8,
            new Object[] { GTOreDictUnificator.get(OrePrefixes.toolHeadDrill, Materials.SpaceTime, 8),
                ItemList.Robot_Arm_UMV.get(8), ItemList.Field_Generator_UMV.get(2),
                new Object[] { OrePrefixes.circuit.get(Materials.UXV), 4 }, GTUtility.copyAmount(64, t9Plate),
                GTUtility.copyAmount(64, t9Plate),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "HeavyDutyRocketEngineTier4", 32),
                ItemList.Sensor_UMV.get(8) },
            new FluidStack[] { new FluidStack(hypogenFluid, 4 * INGOTS),
                new FluidStack(celestialTungstenFluid, 4 * INGOTS),
                new FluidStack(FluidRegistry.getFluid("liquid_drillingfluid"), 512_000) },
            ItemList.MiningDroneUMV.get(1),
            1 * MINUTE,
            (int) TierEU.RECIPE_UMV);

        // UXV
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.MiningDroneUMV.get(1),
            275000,
            512,
            16000000,
            4,
            new Object[] { GTOreDictUnificator.get(OrePrefixes.toolHeadDrill, Materials.Eternity, 8),
                ItemList.Robot_Arm_UXV.get(8), ItemList.Field_Generator_UXV.get(2),
                new Object[] { OrePrefixes.circuit.get(Materials.UXV), 8 }, GTUtility.copyAmount(64, t9Plate),
                GTUtility.copyAmount(64, t9Plate), GTUtility.copyAmount(64, t9Plate), GTUtility.copyAmount(64, t9Plate),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "HeavyDutyRocketEngineTier4", 64),
                ItemList.Sensor_UXV.get(8) },
            new FluidStack[] { Materials.Space.getMolten(4 * INGOTS), Materials.Universium.getMolten(4 * INGOTS),
                new FluidStack(FluidRegistry.getFluid("liquid_drillingfluid"), 512_000) },
            ItemList.MiningDroneUXV.get(1),
            1 * MINUTE,
            (int) TierEU.RECIPE_UXV);
    }
}
