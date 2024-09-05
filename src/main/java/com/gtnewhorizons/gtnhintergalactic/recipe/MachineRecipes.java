package com.gtnewhorizons.gtnhintergalactic.recipe;

import static gregtech.api.enums.GTValues.RA;
import static gregtech.api.enums.TickTime.MINUTE;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizons.gtnhintergalactic.block.IGBlocks;
import com.gtnewhorizons.gtnhintergalactic.item.IGItems;
import com.gtnewhorizons.gtnhintergalactic.item.ItemMiningDrones;

import bartworks.common.loaders.ItemRegistry;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import goodgenerator.items.GGMaterial;
import goodgenerator.util.ItemRefer;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.MaterialsElements;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.util.RecipeUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import tectech.recipe.TTRecipeAdder;
import tectech.thing.CustomItemList;
import tectech.thing.casing.TTCasingsContainer;

public class MachineRecipes implements Runnable {

    Fluid solderUEV = FluidRegistry.getFluid("molten.mutatedlivingsolder") != null
            ? FluidRegistry.getFluid("molten.mutatedlivingsolder")
            : FluidRegistry.getFluid("molten.solderingalloy");
    Fluid solderLuV = FluidRegistry.getFluid("molten.indalloy140") != null
            ? FluidRegistry.getFluid("molten.indalloy140")
            : FluidRegistry.getFluid("molten.solderingalloy");
    Item pikoCircuit = GameRegistry.findItem("dreamcraft", "item.PikoCircuit");
    Item quantumCircuit = GameRegistry.findItem("dreamcraft", "item.QuantumCircuit");

    @Override
    public void run() {

        // exit early if not in pack
        if (!Loader.isModLoaded("dreamcraft")) return;

        ItemStack hypogenFrameBox_8 = MaterialsElements.STANDALONE.HYPOGEN.getFrameBox(8);
        ItemStack hypogenScrew_32 = MaterialsElements.STANDALONE.HYPOGEN.getScrew(32);
        Fluid hypogenFluid = MaterialsElements.STANDALONE.HYPOGEN.getFluid();
        Fluid celestialTungstenFluid = MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFluid();

        ItemStack preciseAssembler_1 = ItemRefer.Precise_Assembler.get(1);
        ItemStack highComputationStationT3_32 = ItemRefer.HiC_T3.get(32);
        ItemStack highComputationStationT4_32 = ItemRefer.HiC_T4.get(32);
        ItemStack highComputationStationT5_32 = ItemRefer.HiC_T5.get(32);
        ItemStack metaStableOgScrew_64 = GGMaterial.metastableOganesson.get(OrePrefixes.screw, 64);
        ItemStack shirabonGear_8 = GGMaterial.shirabon.get(OrePrefixes.gearGt, 8);
        ItemStack shirabonGearSmall_16 = GGMaterial.shirabon.get(OrePrefixes.gearGtSmall, 16);
        ItemStack titaniumBetaCScrew_64 = GGMaterial.titaniumBetaC.get(OrePrefixes.screw, 64);

        ItemStack voidMiner = ItemRegistry.voidminer[2];

        // Planetary Gas Siphon Controller
        RecipeUtil.addRecipe(
                IGItems.PlanetaryGasSiphon,
                new Object[] { "MPM", "CTC", "HTH", 'M', ItemList.Electric_Motor_IV.get(1), 'P',
                        ItemList.Electric_Pump_IV.get(1), 'C', "circuitElite", 'T',
                        GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.TungstenSteel, 1), 'H',
                        ItemList.Hull_IV.get(1) });

        // Space Elevator Controller
        TTRecipeAdder.addResearchableAssemblylineRecipe(
                GTModHandler.getModItem("OpenBlocks", "elevator", 1, 0),
                256000,
                256,
                1000000,
                4,
                new Object[] { GTModHandler.getModItem("OpenBlocks", "elevator", 1),
                        GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 64),
                        ItemList.Field_Generator_UV.get(16),
                        new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 4 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 4 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 4 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 4 },
                        GTModHandler.getModItem("dreamcraft", "item.HeavyDutyPlateTier7", 32),
                        ItemList.Circuit_Chip_PPIC.get(64),
                        GTOreDictUnificator.get(OrePrefixes.screw, Materials.CosmicNeutronium, 64),
                        ItemList.Electric_Motor_UV.get(32), new ItemStack(IGBlocks.SpaceElevatorCasing, 8) },
                new FluidStack[] { new FluidStack(solderLuV, 5760), Materials.UUMatter.getFluid(16000),
                        Materials.Lubricant.getFluid(32000), Materials.Neutronium.getMolten(1440) },
                IGItems.SpaceElevatorController,
                5 * MINUTE,
                (int) TierEU.RECIPE_UHV);

        // Nanotube spool
        RA.stdBuilder()
                .itemInputs(
                        GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.Graphene, 64),
                        GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.Graphene, 64),
                        GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.Graphene, 64),
                        GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.Graphene, 64))
                .itemOutputs(new ItemStack(IGItems.SpaceElevatorItems, 1, 0))
                .fluidInputs(Materials.AdvancedGlue.getFluid(720)).duration(1 * MINUTE).eut(TierEU.RECIPE_ZPM)
                .requiresCleanRoom().noOptimize().addTo(assemblerRecipes);

        // Space Elevator Cable
        TTRecipeAdder.addResearchableAssemblylineRecipe(
                new ItemStack(IGItems.SpaceElevatorItems, 1, 0),
                128000,
                256,
                500000,
                2,
                new Object[] { new ItemStack(IGItems.SpaceElevatorItems, 16, 0),
                        new ItemStack(IGItems.SpaceElevatorItems, 16, 0),
                        new ItemStack(IGItems.SpaceElevatorItems, 16, 0),
                        new ItemStack(IGItems.SpaceElevatorItems, 16, 0),
                        new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 4 }, },
                new FluidStack[] { Materials.AdvancedGlue.getFluid(720) },
                new ItemStack(IGBlocks.SpaceElevatorCable),
                1 * MINUTE,
                (int) TierEU.RECIPE_UHV);

        // Space Elevator Base Casing
        TTRecipeAdder.addResearchableAssemblylineRecipe(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Neutronium, 1),
                64000,
                128,
                500000,
                2,
                new Object[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 8),
                        GTOreDictUnificator.get(OrePrefixes.screw, Materials.Palladium, 32),
                        GTOreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 64),
                        new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 4 },
                        ItemList.Electric_Piston_UV.get(2),
                        GTOreDictUnificator.get(OrePrefixes.ring, Materials.CosmicNeutronium, 8), },
                new FluidStack[] { new FluidStack(solderLuV, 5760), Materials.UUMatter.getFluid(2000),
                        Materials.Iridium.getMolten(1152) },
                new ItemStack(IGBlocks.SpaceElevatorCasing, 8, 0),
                1 * MINUTE,
                (int) TierEU.RECIPE_UV);

        // Space Elevator Support Structure
        TTRecipeAdder.addResearchableAssemblylineRecipe(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 1),
                64000,
                128,
                500000,
                2,
                new Object[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 8),
                        GTOreDictUnificator.get(OrePrefixes.bolt, Materials.Naquadria, 16),
                        GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Neutronium, 8),
                        GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.Osmiridium, 8), },
                new FluidStack[] { new FluidStack(solderLuV, 5760), Materials.UUMatter.getFluid(1000),
                        Materials.Iridium.getMolten(1440) },
                new ItemStack(IGBlocks.SpaceElevatorCasing, 8, 1),
                1 * MINUTE,
                (int) TierEU.RECIPE_UV);

        // Space Elevator Internal Structure
        TTRecipeAdder.addResearchableAssemblylineRecipe(
                new ItemStack(TTCasingsContainer.sBlockCasingsTT, 1, 0),
                64000,
                128,
                500000,
                2,
                new Object[] { new ItemStack(TTCasingsContainer.sBlockCasingsTT, 8, 0),
                        GTOreDictUnificator.get(OrePrefixes.bolt, Materials.Palladium, 16),
                        GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.Neutronium, 8), },
                new FluidStack[] { new FluidStack(solderLuV, 5760), Materials.UUMatter.getFluid(8000),
                        Materials.Concrete.getMolten(1440) },
                new ItemStack(IGBlocks.SpaceElevatorCasing, 8, 2),
                1 * MINUTE,
                (int) TierEU.RECIPE_UV);

        // Space Elevator Motor MK-I
        TTRecipeAdder.addResearchableAssemblylineRecipe(
                new ItemStack(IGBlocks.SpaceElevatorCasing, 1, 0),
                64000,
                128,
                500000,
                2,
                new Object[] { new ItemStack(IGBlocks.SpaceElevatorCasing, 1, 0), ItemList.Electric_Motor_UV.get(4),
                        GTOreDictUnificator.get(OrePrefixes.ring, Materials.Neutronium, 8),
                        GTOreDictUnificator.get(OrePrefixes.stick, Materials.Neutronium, 4),
                        new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 1 },
                        GTOreDictUnificator.get(OrePrefixes.screw, Materials.CosmicNeutronium, 16),
                        GTOreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 16), },
                new FluidStack[] { new FluidStack(solderLuV, 5760), Materials.UUMatter.getFluid(8000),
                        Materials.Naquadria.getMolten(1440), Materials.Lubricant.getFluid(16000) },
                new ItemStack(IGBlocks.SpaceElevatorMotor, 1, 0),
                1 * MINUTE,
                (int) TierEU.RECIPE_UV);

        // Space Elevator Motor MK-II
        TTRecipeAdder.addResearchableAssemblylineRecipe(
                new ItemStack(IGBlocks.SpaceElevatorMotor, 1, 0),
                128000,
                256,
                2000000,
                2,
                new Object[] { new ItemStack(IGBlocks.SpaceElevatorCasing, 1, 0), ItemList.Electric_Motor_UHV.get(4),
                        GTOreDictUnificator.get(OrePrefixes.ring, Materials.CosmicNeutronium, 8),
                        GTOreDictUnificator.get(OrePrefixes.stick, Materials.CosmicNeutronium, 4),
                        new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 1 },
                        GTOreDictUnificator.get(OrePrefixes.screw, Materials.Infinity, 16),
                        GTOreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 16), },
                new FluidStack[] { new FluidStack(solderLuV, 5760), Materials.UUMatter.getFluid(8000),
                        Materials.Naquadria.getMolten(1440), Materials.Lubricant.getFluid(16000) },
                new ItemStack(IGBlocks.SpaceElevatorMotor, 1, 1),
                1 * MINUTE,
                (int) TierEU.RECIPE_UHV);

        // Space Elevator Motor MK-III
        TTRecipeAdder.addResearchableAssemblylineRecipe(
                new ItemStack(IGBlocks.SpaceElevatorMotor, 1, 1),
                128000,
                256,
                2000000,
                2,
                new Object[] { new ItemStack(IGBlocks.SpaceElevatorCasing, 1, 0), ItemList.Electric_Motor_UEV.get(4),
                        GTOreDictUnificator.get(OrePrefixes.ring, Materials.Infinity, 8),
                        GTOreDictUnificator.get(OrePrefixes.stick, Materials.Infinity, 4),
                        new Object[] { OrePrefixes.circuit.get(Materials.Bio), 1 },
                        GTOreDictUnificator.get(OrePrefixes.screw, MaterialsUEVplus.TranscendentMetal, 16),
                        GTOreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 16), },
                new FluidStack[] { new FluidStack(solderUEV, 2880), Materials.UUMatter.getFluid(8000),
                        Materials.Naquadria.getMolten(1440),
                        MaterialsUEVplus.DimensionallyShiftedSuperfluid.getFluid(24000) },
                new ItemStack(IGBlocks.SpaceElevatorMotor, 1, 2),
                1 * MINUTE,
                (int) TierEU.RECIPE_UEV);

        // Space Elevator Motor MK-IV
        TTRecipeAdder.addResearchableAssemblylineRecipe(
                new ItemStack(IGBlocks.SpaceElevatorMotor, 1, 2),
                128000,
                256,
                2000000,
                2,
                new Object[] { new ItemStack(IGBlocks.SpaceElevatorCasing, 1, 0), ItemList.Electric_Motor_UIV.get(4),
                        GTOreDictUnificator.get(OrePrefixes.ring, MaterialsUEVplus.TranscendentMetal, 8),
                        GTOreDictUnificator.get(OrePrefixes.stick, MaterialsUEVplus.TranscendentMetal, 4),
                        new Object[] { OrePrefixes.circuit.get(Materials.Optical), 1 },
                        GTOreDictUnificator.get(OrePrefixes.screw, MaterialsUEVplus.SpaceTime, 16),
                        GTOreDictUnificator.get(OrePrefixes.plate, MaterialsUEVplus.ProtoHalkonite, 16), },
                new FluidStack[] { new FluidStack(solderUEV, 2880), Materials.UUMatter.getFluid(8000),
                        Materials.Naquadria.getMolten(1440),
                        MaterialsUEVplus.DimensionallyShiftedSuperfluid.getFluid(24000) },
                new ItemStack(IGBlocks.SpaceElevatorMotor, 1, 3),
                1 * MINUTE,
                (int) TierEU.RECIPE_UIV);

        // Space Elevator Motor MK-V
        TTRecipeAdder.addResearchableAssemblylineRecipe(
                new ItemStack(IGBlocks.SpaceElevatorMotor, 1, 3),
                128000,
                256,
                2000000,
                2,
                new Object[] { new ItemStack(IGBlocks.SpaceElevatorCasing, 1, 0), ItemList.Electric_Motor_UMV.get(4),
                        GTOreDictUnificator.get(OrePrefixes.ring, MaterialsUEVplus.SpaceTime, 8),
                        GTOreDictUnificator.get(OrePrefixes.stick, MaterialsUEVplus.SpaceTime, 4),
                        new ItemStack(pikoCircuit, 1),
                        GTOreDictUnificator.get(OrePrefixes.screw, MaterialsUEVplus.Universium, 16),
                        GTOreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 16), },
                new FluidStack[] { new FluidStack(solderUEV, 2880), Materials.UUMatter.getFluid(8000),
                        Materials.Naquadria.getMolten(1440),
                        MaterialsUEVplus.DimensionallyShiftedSuperfluid.getFluid(24000) },
                new ItemStack(IGBlocks.SpaceElevatorMotor, 1, 4),
                1 * MINUTE,
                (int) TierEU.RECIPE_UIV);

        // Space Elevator Modules

        // Pump Module MK-I
        TTRecipeAdder.addResearchableAssemblylineRecipe(
                IGItems.PlanetaryGasSiphon,
                16777216,
                2048,
                2000000,
                4,
                new Object[] { ItemList.OilDrillInfinite.get(1), IGItems.PlanetaryGasSiphon,
                        CustomItemList.enderLinkFluidCover.get(2),
                        GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 4),
                        new Object[] { OrePrefixes.circuit.get(Materials.Bio), 4 }, ItemList.Electric_Pump_UEV.get(2),
                        GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Infinity, 4),
                        GTOreDictUnificator.get(OrePrefixes.screw, Materials.CosmicNeutronium, 32), },
                new FluidStack[] { new FluidStack(solderUEV, 1296), Materials.Infinity.getMolten(576) },
                IGItems.SpaceElevatorModulePumpT1,
                2 * MINUTE,
                (int) TierEU.RECIPE_UEV);

        // Pump Module MK-II
        TTRecipeAdder.addResearchableAssemblylineRecipe(
                IGItems.SpaceElevatorModulePumpT1,
                33554432,
                8192,
                64000000,
                4,
                new Object[] { ItemList.OilDrillInfinite.get(4), GTUtility.copyAmount(4, IGItems.PlanetaryGasSiphon),
                        CustomItemList.enderLinkFluidCover.get(8),
                        GTOreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.SpaceTime, 4),
                        new Object[] { OrePrefixes.circuit.get(Materials.Optical), 16 },
                        ItemList.Electric_Pump_UIV.get(8),
                        GTOreDictUnificator.get(OrePrefixes.gearGt, MaterialsUEVplus.SpaceTime, 4),
                        metaStableOgScrew_64, },
                new FluidStack[] { new FluidStack(solderUEV, 4608),
                        MaterialsUEVplus.MoltenProtoHalkoniteBase.getFluid(2304) },
                IGItems.SpaceElevatorModulePumpT2,
                2 * MINUTE,
                (int) TierEU.RECIPE_UIV);

        // Pump Module MK-II - Assembler alt
        RA.stdBuilder()
                .itemInputs(
                        GTUtility.copyAmount(4, IGItems.SpaceElevatorModulePumpT1),
                        GTOreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.SpaceTime, 8),
                        GTOreDictUnificator.get(OrePrefixes.circuit, Materials.Optical, 16),
                        ItemList.Electric_Pump_UIV.get(8),
                        GTOreDictUnificator.get(OrePrefixes.gearGt, MaterialsUEVplus.SpaceTime, 8),
                        metaStableOgScrew_64,
                        GTOreDictUnificator.get(OrePrefixes.plateDouble, MaterialsUEVplus.ProtoHalkonite, 16))
                .itemOutputs(IGItems.SpaceElevatorModulePumpT2).fluidInputs(new FluidStack(solderUEV, 4608))
                .duration(2 * MINUTE).eut(TierEU.RECIPE_UIV).noOptimize().addTo(assemblerRecipes);

        // Pump Module MK-III
        TTRecipeAdder.addResearchableAssemblylineRecipe(
                IGItems.SpaceElevatorModulePumpT2,
                67108864,
                32767,
                256000000,
                4,
                new Object[] { ItemList.OilDrillInfinite.get(16), GTUtility.copyAmount(16, IGItems.PlanetaryGasSiphon),
                        CustomItemList.enderLinkFluidCover.get(32),
                        GTOreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.BlackDwarfMatter, 4),
                        new Object[] { OrePrefixes.circuit.get(Materials.Exotic), 16 },
                        ItemList.Electric_Pump_UMV.get(8),
                        GTOreDictUnificator.get(OrePrefixes.gearGt, MaterialsUEVplus.BlackDwarfMatter, 4),
                        GTOreDictUnificator.get(OrePrefixes.screw, MaterialsUEVplus.WhiteDwarfMatter, 64) },
                new FluidStack[] { new FluidStack(solderUEV, 9216), MaterialsUEVplus.Eternity.getMolten(2304) },
                IGItems.SpaceElevatorModulePumpT3,
                2 * MINUTE,
                (int) TierEU.RECIPE_UMV);

        // Pump Module MK-III - Assembler alt
        RA.stdBuilder()
                .itemInputs(
                        GTUtility.copyAmount(4, IGItems.SpaceElevatorModulePumpT2),
                        GTOreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.BlackDwarfMatter, 8),
                        GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UMV, 16),
                        ItemList.Electric_Pump_UMV.get(8),
                        GTOreDictUnificator.get(OrePrefixes.gearGt, MaterialsUEVplus.BlackDwarfMatter, 8),
                        GTOreDictUnificator.get(OrePrefixes.screw, MaterialsUEVplus.WhiteDwarfMatter, 64),
                        GTOreDictUnificator.get(OrePrefixes.plateDouble, MaterialsUEVplus.Eternity, 16))
                .itemOutputs(IGItems.SpaceElevatorModulePumpT3).fluidInputs(new FluidStack(solderUEV, 9216))
                .duration(2 * MINUTE).eut(TierEU.RECIPE_UIV).noOptimize().addTo(assemblerRecipes);

        // Assembler Module MK-I
        TTRecipeAdder.addResearchableAssemblylineRecipe(
                preciseAssembler_1,
                256000,
                256,
                4000000,
                4,
                new Object[] { new ItemStack(IGBlocks.SpaceElevatorCasing, 1, 0),
                        new ItemStack(GameRegistry.findItem("gregtech", "gt.blockmachines"), 4, 10782),
                        new ItemStack(GameRegistry.findItem("gregtech", "gt.blockmachines"), 4, 1187),
                        GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.CosmicNeutronium, 8),
                        GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.CosmicNeutronium, 16),
                        ItemList.Robot_Arm_UHV.get(8), ItemList.Conveyor_Module_UHV.get(16),
                        highComputationStationT3_32,
                        new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 16 },
                        GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 8),
                        GTOreDictUnificator.get(OrePrefixes.screw, Materials.Neutronium, 32) },
                new FluidStack[] { new FluidStack(solderLuV, 1296), Materials.Naquadria.getMolten(1296),
                        Materials.Lubricant.getFluid(16000) },
                IGItems.SpaceElevatorModuleAssemblerT1,
                2 * MINUTE,
                (int) TierEU.RECIPE_UEV);

        // Assembler Module MK-II
        TTRecipeAdder.addResearchableAssemblylineRecipe(
                IGItems.SpaceElevatorModuleAssemblerT1,
                2048000,
                2048,
                64000000,
                4,
                new Object[] { new ItemStack(IGBlocks.SpaceElevatorCasing, 1, 0),
                        new ItemStack(GameRegistry.findItem("gregtech", "gt.blockmachines"), 4, 10784),
                        new ItemStack(GameRegistry.findItem("gregtech", "gt.blockmachines"), 4, 12091),
                        GTOreDictUnificator.get(OrePrefixes.gearGt, MaterialsUEVplus.TranscendentMetal, 8),
                        GTOreDictUnificator.get(OrePrefixes.gearGtSmall, MaterialsUEVplus.ProtoHalkonite, 16),
                        ItemList.Robot_Arm_UIV.get(8), ItemList.Conveyor_Module_UIV.get(16),
                        highComputationStationT4_32, new Object[] { OrePrefixes.circuit.get(Materials.Optical), 16 },
                        hypogenFrameBox_8, hypogenScrew_32 },
                new FluidStack[] { new FluidStack(solderUEV, 1296), Materials.Infinity.getMolten(1296),
                        Materials.UUMatter.getFluid(16000) },
                IGItems.SpaceElevatorModuleAssemblerT2,
                2 * MINUTE,
                (int) TierEU.RECIPE_UIV);

        // Assembler Module MK-III
        TTRecipeAdder.addResearchableAssemblylineRecipe(
                IGItems.SpaceElevatorModuleAssemblerT2,
                32768000,
                4096,
                256000000,
                4,
                new Object[] { new ItemStack(IGBlocks.SpaceElevatorCasing, 1, 0),
                        new ItemStack(GameRegistry.findItem("gregtech", "gt.blockmachines"), 4, 10786),
                        new ItemStack(GameRegistry.findItem("gregtech", "gt.blockmachines"), 4, 12093),
                        GTOreDictUnificator.get(
                                OrePrefixes.gearGt,
                                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter,
                                8),
                        shirabonGear_8,
                        GTOreDictUnificator.get(
                                OrePrefixes.gearGtSmall,
                                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter,
                                16),
                        shirabonGearSmall_16, ItemList.Robot_Arm_UXV.get(8), ItemList.Conveyor_Module_UXV.get(16),
                        highComputationStationT5_32, new ItemStack(quantumCircuit, 16),
                        GTOreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.Universium, 8),
                        GTOreDictUnificator.get(OrePrefixes.screw, MaterialsUEVplus.Universium, 32) },
                new FluidStack[] { new FluidStack(solderUEV, 5184), MaterialsUEVplus.BlackDwarfMatter.getMolten(1296),
                        MaterialsUEVplus.WhiteDwarfMatter.getMolten(1296), MaterialsUEVplus.SpaceTime.getMolten(1296) },
                IGItems.SpaceElevatorModuleAssemblerT3,
                2 * MINUTE,
                (int) TierEU.RECIPE_UXV);

        // Research Module
        TTRecipeAdder.addResearchableAssemblylineRecipe(
                new ItemStack(GameRegistry.findItem("gregtech", "gt.blockmachines"), 1, 11012),
                512000,
                512,
                16000000,
                4,
                new Object[] { CustomItemList.Machine_Multi_Research.get(4), ItemList.Sensor_UHV.get(4),
                        new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 16 },
                        GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Infinity, 4),
                        GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 4),
                        GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.CosmicNeutronium, 64),
                        GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.CosmicNeutronium, 64),
                        metaStableOgScrew_64,
                        GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 32) },
                new FluidStack[] { new FluidStack(solderLuV, 4608), Materials.Infinity.getMolten(2304),
                        Materials.UUMatter.getFluid(8000), Materials.SuperCoolant.getFluid(4000) },
                IGItems.SpaceElevatorModuleResearch,
                2 * MINUTE,
                (int) TierEU.RECIPE_UEV);

        // Project Manager Module
        TTRecipeAdder.addResearchableAssemblylineRecipe(
                new ItemStack(GameRegistry.findItem("miscutils", "blockProjectBench"), 1),
                256000,
                512,
                1000000,
                4,
                new Object[] { new ItemStack(GameRegistry.findItem("miscutils", "blockProjectBench"), 4),
                        ItemList.Emitter_UV.get(2), ItemList.Sensor_UV.get(2),
                        new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 16 },
                        GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Neutronium, 4),
                        GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 4),
                        new ItemStack(
                                GameRegistry.findItem("structurelib", "item.structurelib.constructableTrigger"),
                                64),
                        titaniumBetaCScrew_64, },
                new FluidStack[] { new FluidStack(solderLuV, 4608), Materials.Iridium.getMolten(2304),
                        Materials.UUMatter.getFluid(2000) },
                IGItems.SpaceElevatorModuleManager,
                2 * MINUTE,
                (int) TierEU.RECIPE_UHV);

        // Miner Module MK-I
        TTRecipeAdder.addResearchableAssemblylineRecipe(
                voidMiner,
                2000000,
                512,
                2000000,
                8,
                new Object[] { ItemList.OreDrill4.get(1), ItemList.Robot_Arm_UV.get(8),
                        ItemList.Field_Generator_UV.get(4),
                        new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 16 },
                        ItemList.Sensor_UV.get(16),
                        GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUV, 32),
                        GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 16) },
                new FluidStack[] { new FluidStack(solderLuV, 2880), Materials.Naquadria.getMolten(1440),
                        Materials.Lubricant.getFluid(8000) },
                IGItems.SpaceElevatorModuleMinerT1,
                2 * MINUTE,
                (int) TierEU.RECIPE_UHV);
        // Miner Module MK-II
        TTRecipeAdder.addResearchableAssemblylineRecipe(
                IGItems.SpaceElevatorModuleMinerT1,
                3000000,
                1024,
                3000000,
                12,
                new Object[] { IGItems.SpaceElevatorModuleMinerT1, ItemList.Robot_Arm_UHV.get(8),
                        ItemList.Field_Generator_UHV.get(4),
                        new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 16 }, ItemList.Sensor_UHV.get(16),
                        GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 32),
                        GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 16) },
                new FluidStack[] { new FluidStack(solderLuV, 2880), Materials.Naquadria.getMolten(2880),
                        Materials.Lubricant.getFluid(16000) },
                IGItems.SpaceElevatorModuleMinerT2,
                2 * MINUTE,
                (int) TierEU.RECIPE_UEV);
        // Miner Module MK-III
        TTRecipeAdder.addResearchableAssemblylineRecipe(
                IGItems.SpaceElevatorModuleMinerT2,
                4000000,
                2048,
                4000000,
                16,
                new Object[] { IGItems.SpaceElevatorModuleMinerT2, ItemList.Robot_Arm_UEV.get(8),
                        ItemList.Field_Generator_UEV.get(4),
                        new Object[] { OrePrefixes.circuit.get(Materials.Bio), 16 }, ItemList.Sensor_UEV.get(16),
                        GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUEV, 32),
                        GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 16) },
                new FluidStack[] { new FluidStack(solderUEV, 2880), MaterialsUEVplus.TranscendentMetal.getMolten(1440),
                        Materials.UUMatter.getFluid(2000) },
                IGItems.SpaceElevatorModuleMinerT3,
                2 * MINUTE,
                (int) TierEU.RECIPE_UIV);

        // Mining drones

        // LV
        RA.stdBuilder()
                .itemInputs(
                        GTOreDictUnificator.get(OrePrefixes.toolHeadDrill, Materials.Titanium, 8),
                        ItemList.Robot_Arm_LV.get(8),
                        ItemList.Field_Generator_LV.get(2),
                        GTOreDictUnificator.get("circuitAdvanced", 4),
                        new ItemStack(GCItems.heavyPlatingTier1, 16),
                        new ItemStack(GCItems.rocketEngine, 2),
                        ItemList.Sensor_LV.get(8))
                .itemOutputs(new ItemStack(IGItems.MiningDrones, 1, ItemMiningDrones.DroneTiers.LV.ordinal()))
                .fluidInputs(Materials.SolderingAlloy.getMolten(720)).duration(1 * MINUTE).eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);

        // MV
        RA.stdBuilder()
                .itemInputs(
                        GTOreDictUnificator.get(OrePrefixes.toolHeadDrill, Materials.TungstenSteel, 8),
                        ItemList.Robot_Arm_MV.get(8),
                        ItemList.Field_Generator_MV.get(2),
                        GTOreDictUnificator.get("circuitData", 4),
                        new ItemStack(GCItems.heavyPlatingTier1, 32),
                        new ItemStack(GCItems.rocketEngine, 4),
                        ItemList.Sensor_MV.get(8))
                .itemOutputs(new ItemStack(IGItems.MiningDrones, 1, ItemMiningDrones.DroneTiers.MV.ordinal()))
                .fluidInputs(Materials.SolderingAlloy.getMolten(1440)).duration(1 * MINUTE).eut(TierEU.RECIPE_MV)
                .addTo(assemblerRecipes);

        // HV
        RA.stdBuilder()
                .itemInputs(
                        GTOreDictUnificator.get(OrePrefixes.toolHeadDrill, Materials.Iridium, 8),
                        ItemList.Robot_Arm_HV.get(8),
                        ItemList.Field_Generator_HV.get(2),
                        GTOreDictUnificator.get("circuitElite", 4),
                        new ItemStack(MarsItems.marsItemBasic, 32, 3),
                        new ItemStack(GCItems.rocketEngine, 4),
                        ItemList.Sensor_HV.get(8))
                .itemOutputs(new ItemStack(IGItems.MiningDrones, 1, ItemMiningDrones.DroneTiers.HV.ordinal()))
                .fluidInputs(Materials.SolderingAlloy.getMolten(1440)).duration(1 * MINUTE).eut(TierEU.RECIPE_HV)
                .addTo(assemblerRecipes);

        // EV
        TTRecipeAdder.addResearchableAssemblylineRecipe(
                new ItemStack(IGItems.MiningDrones, 1, ItemMiningDrones.DroneTiers.HV.ordinal()),
                50000,
                128,
                1000000,
                4,
                new Object[] { GTOreDictUnificator.get(OrePrefixes.toolHeadDrill, Materials.Trinium, 8),
                        ItemList.Robot_Arm_EV.get(8), ItemList.Field_Generator_EV.get(2),
                        new Object[] { OrePrefixes.circuit.get(Materials.Master), 4 },
                        new ItemStack(AsteroidsItems.basicItem, 32, 0), new ItemStack(AsteroidsItems.basicItem, 4, 1),
                        ItemList.Sensor_EV.get(8) },
                new FluidStack[] { new FluidStack(solderLuV, 720), Materials.Iridium.getMolten(720),
                        new FluidStack(FluidRegistry.getFluid("liquid_drillingfluid"), 16000) },
                new ItemStack(IGItems.MiningDrones, 1, ItemMiningDrones.DroneTiers.EV.ordinal()),
                1 * MINUTE,
                (int) TierEU.RECIPE_EV);

        // IV
        TTRecipeAdder.addResearchableAssemblylineRecipe(
                new ItemStack(IGItems.MiningDrones, 1, ItemMiningDrones.DroneTiers.EV.ordinal()),
                75000,
                128,
                1000000,
                8,
                new Object[] { GTOreDictUnificator.get(OrePrefixes.toolHeadDrill, Materials.NaquadahAlloy, 8),
                        ItemList.Robot_Arm_IV.get(8), ItemList.Field_Generator_IV.get(2),
                        new Object[] { OrePrefixes.circuit.get(Materials.Ultimate), 4 },
                        GTModHandler.getModItem("dreamcraft", "item.HeavyDutyPlateTier4", 32),
                        new ItemStack(AsteroidsItems.basicItem, 4, 1), ItemList.Sensor_IV.get(8) },
                new FluidStack[] { new FluidStack(solderLuV, 1440), Materials.Iridium.getMolten(1440),
                        new FluidStack(FluidRegistry.getFluid("liquid_drillingfluid"), 32000) },
                new ItemStack(IGItems.MiningDrones, 1, ItemMiningDrones.DroneTiers.IV.ordinal()),
                1 * MINUTE,
                (int) TierEU.RECIPE_IV);

        // LuV
        TTRecipeAdder.addResearchableAssemblylineRecipe(
                new ItemStack(IGItems.MiningDrones, 1, ItemMiningDrones.DroneTiers.IV.ordinal()),
                100000,
                256,
                2000000,
                4,
                new Object[] { GTOreDictUnificator.get(OrePrefixes.toolHeadDrill, Materials.Naquadria, 8),
                        ItemList.Robot_Arm_LuV.get(8), ItemList.Field_Generator_LuV.get(2),
                        new Object[] { OrePrefixes.circuit.get(Materials.Ultimate), 4 },
                        GTModHandler.getModItem("dreamcraft", "item.HeavyDutyPlateTier5", 32),
                        GTModHandler.getModItem("dreamcraft", "item.HeavyDutyRocketEngineTier3", 4),
                        ItemList.Sensor_LuV.get(8) },
                new FluidStack[] { new FluidStack(solderLuV, 2880), Materials.Osmiridium.getMolten(1440),
                        new FluidStack(FluidRegistry.getFluid("liquid_drillingfluid"), 64000) },
                new ItemStack(IGItems.MiningDrones, 1, ItemMiningDrones.DroneTiers.LuV.ordinal()),
                1 * MINUTE,
                (int) TierEU.RECIPE_LuV);

        // ZPM
        TTRecipeAdder.addResearchableAssemblylineRecipe(
                new ItemStack(IGItems.MiningDrones, 1, ItemMiningDrones.DroneTiers.LuV.ordinal()),
                125000,
                256,
                2000000,
                8,
                new Object[] { GTOreDictUnificator.get(OrePrefixes.toolHeadDrill, Materials.Neutronium, 8),
                        ItemList.Robot_Arm_ZPM.get(8), ItemList.Field_Generator_ZPM.get(2),
                        new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 2 },
                        GTModHandler.getModItem("dreamcraft", "item.HeavyDutyPlateTier6", 32),
                        GTModHandler.getModItem("dreamcraft", "item.HeavyDutyRocketEngineTier3", 4),
                        ItemList.Sensor_ZPM.get(8) },
                new FluidStack[] { new FluidStack(solderLuV, 2880), Materials.Osmiridium.getMolten(1440),
                        new FluidStack(FluidRegistry.getFluid("liquid_drillingfluid"), 128000) },
                new ItemStack(IGItems.MiningDrones, 1, ItemMiningDrones.DroneTiers.ZPM.ordinal()),
                1 * MINUTE,
                (int) TierEU.RECIPE_ZPM);

        // UV
        TTRecipeAdder.addResearchableAssemblylineRecipe(
                new ItemStack(IGItems.MiningDrones, 1, ItemMiningDrones.DroneTiers.ZPM.ordinal()),
                150000,
                512,
                4000000,
                4,
                new Object[] { GTOreDictUnificator.get(OrePrefixes.toolHeadDrill, Materials.CosmicNeutronium, 8),
                        ItemList.Robot_Arm_UV.get(8), ItemList.Field_Generator_UV.get(2),
                        new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 4 },
                        GTModHandler.getModItem("dreamcraft", "item.HeavyDutyPlateTier7", 32),
                        GTModHandler.getModItem("dreamcraft", "item.HeavyDutyRocketEngineTier4", 4),
                        ItemList.Sensor_UV.get(8) },
                new FluidStack[] { new FluidStack(solderLuV, 2880), Materials.Naquadria.getMolten(1440),
                        new FluidStack(FluidRegistry.getFluid("liquid_drillingfluid"), 256000) },
                new ItemStack(IGItems.MiningDrones, 1, ItemMiningDrones.DroneTiers.UV.ordinal()),
                1 * MINUTE,
                (int) TierEU.RECIPE_UV);

        // UHV
        TTRecipeAdder.addResearchableAssemblylineRecipe(
                new ItemStack(IGItems.MiningDrones, 1, ItemMiningDrones.DroneTiers.UV.ordinal()),
                175000,
                512,
                4000000,
                8,
                new Object[] { GTOreDictUnificator.get(OrePrefixes.toolHeadDrill, Materials.Infinity, 8),
                        ItemList.Robot_Arm_UHV.get(8), ItemList.Field_Generator_UHV.get(2),
                        new Object[] { OrePrefixes.circuit.get(Materials.Bio), 4 },
                        GTModHandler.getModItem("dreamcraft", "item.HeavyDutyPlateTier8", 32),
                        GTModHandler.getModItem("dreamcraft", "item.HeavyDutyRocketEngineTier4", 4),
                        ItemList.Sensor_UHV.get(8) },
                new FluidStack[] { new FluidStack(solderUEV, 2880), Materials.Neutronium.getMolten(1440),
                        new FluidStack(FluidRegistry.getFluid("liquid_drillingfluid"), 512000) },
                new ItemStack(IGItems.MiningDrones, 1, ItemMiningDrones.DroneTiers.UHV.ordinal()),
                1 * MINUTE,
                (int) TierEU.RECIPE_UHV);

        // UEV
        TTRecipeAdder.addResearchableAssemblylineRecipe(
                new ItemStack(IGItems.MiningDrones, 1, ItemMiningDrones.DroneTiers.UHV.ordinal()),
                200000,
                512,
                4000000,
                8,
                new Object[] { GTOreDictUnificator.get(OrePrefixes.toolHeadDrill, Materials.CosmicNeutronium, 8),
                        ItemList.Robot_Arm_UEV.get(8), ItemList.Field_Generator_UEV.get(2),
                        new Object[] { OrePrefixes.circuit.get(Materials.Optical), 4 },
                        GTModHandler.getModItem("dreamcraft", "item.HeavyDutyPlateTier8", 64),
                        GTModHandler.getModItem("dreamcraft", "item.HeavyDutyRocketEngineTier4", 8),
                        ItemList.Sensor_UEV.get(8) },
                new FluidStack[] { new FluidStack(solderUEV, 2880), Materials.Quantium.getMolten(1440),
                        new FluidStack(FluidRegistry.getFluid("liquid_drillingfluid"), 512000) },
                new ItemStack(IGItems.MiningDrones, 1, ItemMiningDrones.DroneTiers.UEV.ordinal()),
                1 * MINUTE,
                (int) TierEU.RECIPE_UEV);

        // UIV
        TTRecipeAdder.addResearchableAssemblylineRecipe(
                new ItemStack(IGItems.MiningDrones, 1, ItemMiningDrones.DroneTiers.UEV.ordinal()),
                225000,
                512,
                8000000,
                4,
                new Object[] { GTOreDictUnificator.get(OrePrefixes.toolHeadDrill, Materials.CosmicNeutronium, 8),
                        ItemList.Robot_Arm_UIV.get(8), ItemList.Field_Generator_UIV.get(2),
                        new ItemStack(pikoCircuit, 4),
                        GTModHandler.getModItem("dreamcraft", "item.HeavyDutyPlateTier8", 64),
                        GTModHandler.getModItem("dreamcraft", "item.HeavyDutyPlateTier8", 64),
                        GTModHandler.getModItem("dreamcraft", "item.HeavyDutyRocketEngineTier4", 16),
                        ItemList.Sensor_UIV.get(8) },
                new FluidStack[] { new FluidStack(solderUEV, 5760), Materials.Quantium.getMolten(2880),
                        new FluidStack(FluidRegistry.getFluid("liquid_drillingfluid"), 512000) },
                new ItemStack(IGItems.MiningDrones, 1, ItemMiningDrones.DroneTiers.UIV.ordinal()),
                1 * MINUTE,
                (int) TierEU.RECIPE_UIV);

        // UMV
        TTRecipeAdder.addResearchableAssemblylineRecipe(
                new ItemStack(IGItems.MiningDrones, 1, ItemMiningDrones.DroneTiers.UIV.ordinal()),
                250000,
                512,
                8000000,
                8,
                new Object[] { GTOreDictUnificator.get(OrePrefixes.toolHeadDrill, MaterialsUEVplus.SpaceTime, 8),
                        ItemList.Robot_Arm_UMV.get(8), ItemList.Field_Generator_UMV.get(2),
                        new ItemStack(quantumCircuit, 4),
                        GTModHandler.getModItem("dreamcraft", "item.HeavyDutyPlateTier8", 64),
                        GTModHandler.getModItem("dreamcraft", "item.HeavyDutyPlateTier8", 64),
                        GTModHandler.getModItem("dreamcraft", "item.HeavyDutyPlateTier8", 64),
                        GTModHandler.getModItem("dreamcraft", "item.HeavyDutyRocketEngineTier4", 32),
                        ItemList.Sensor_UMV.get(8) },
                new FluidStack[] { new FluidStack(hypogenFluid, 576), new FluidStack(celestialTungstenFluid, 576),
                        new FluidStack(FluidRegistry.getFluid("liquid_drillingfluid"), 512000) },
                new ItemStack(IGItems.MiningDrones, 1, ItemMiningDrones.DroneTiers.UMV.ordinal()),
                1 * MINUTE,
                (int) TierEU.RECIPE_UMV);

        // UXV
        TTRecipeAdder.addResearchableAssemblylineRecipe(
                new ItemStack(IGItems.MiningDrones, 1, ItemMiningDrones.DroneTiers.UMV.ordinal()),
                275000,
                512,
                16000000,
                4,
                new Object[] { GTOreDictUnificator.get(OrePrefixes.toolHeadDrill, MaterialsUEVplus.Eternity, 8),
                        ItemList.Robot_Arm_UXV.get(8), ItemList.Field_Generator_UXV.get(2),
                        new ItemStack(quantumCircuit, 8),
                        GTModHandler.getModItem("dreamcraft", "item.HeavyDutyPlateTier8", 64),
                        GTModHandler.getModItem("dreamcraft", "item.HeavyDutyPlateTier8", 64),
                        GTModHandler.getModItem("dreamcraft", "item.HeavyDutyPlateTier8", 64),
                        GTModHandler.getModItem("dreamcraft", "item.HeavyDutyPlateTier8", 64),
                        GTModHandler.getModItem("dreamcraft", "item.HeavyDutyRocketEngineTier4", 64),
                        ItemList.Sensor_UXV.get(8) },
                new FluidStack[] { MaterialsUEVplus.Space.getFluid(576), MaterialsUEVplus.Universium.getFluid(576),
                        new FluidStack(FluidRegistry.getFluid("liquid_drillingfluid"), 512000) },
                new ItemStack(IGItems.MiningDrones, 1, ItemMiningDrones.DroneTiers.UXV.ordinal()),
                1 * MINUTE,
                (int) TierEU.RECIPE_UXV);
    }
}
