package com.gtnewhorizons.gtnhintergalactic.recipe;

import static gregtech.api.enums.GT_Values.RA;
import static gregtech.api.enums.GT_Values.VP;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry;
import com.github.technus.tectech.recipe.TT_recipeAdder;
import com.github.technus.tectech.thing.CustomItemList;
import com.github.technus.tectech.thing.casing.TT_Container_Casings;
import com.gtnewhorizons.gtnhintergalactic.block.IGBlocks;
import com.gtnewhorizons.gtnhintergalactic.item.IGItems;
import com.gtnewhorizons.gtnhintergalactic.item.ItemMiningDrones;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import goodgenerator.items.MyMaterial;
import goodgenerator.util.ItemRefer;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.material.ELEMENT;
import micdoodle8.mods.galacticraft.core.items.GCItems;
import micdoodle8.mods.galacticraft.core.util.RecipeUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;

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

        ItemStack hypogenFrameBox_8, hypogenScrew_32, preciseAssembler_1, highComputationStationT3_32,
                highComputationStationT4_32, highComputationStationT5_32, metaStableOgScrew_64, shirabonGear_8,
                shirabonGearSmall_16, titaniumBetaCScrew_64, voidMiner;

        if (Loader.isModLoaded("miscutils")) {
            hypogenFrameBox_8 = ELEMENT.STANDALONE.HYPOGEN.getFrameBox(8);
            hypogenScrew_32 = ELEMENT.STANDALONE.HYPOGEN.getScrew(32);
        } else {
            hypogenFrameBox_8 = GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 8);
            hypogenScrew_32 = GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Infinity, 32);
        }

        if (Loader.isModLoaded("GoodGenerator")) {
            preciseAssembler_1 = ItemRefer.Precise_Assembler.get(1);
            highComputationStationT3_32 = ItemRefer.HiC_T3.get(32);
            highComputationStationT4_32 = ItemRefer.HiC_T4.get(32);
            highComputationStationT5_32 = ItemRefer.HiC_T5.get(32);
            metaStableOgScrew_64 = MyMaterial.metastableOganesson.get(OrePrefixes.screw, 64);
            shirabonGear_8 = MyMaterial.shirabon.get(OrePrefixes.gearGt, 8);
            shirabonGearSmall_16 = MyMaterial.shirabon.get(OrePrefixes.gearGtSmall, 16);
            titaniumBetaCScrew_64 = MyMaterial.titaniumBetaC.get(OrePrefixes.screw, 64);
        } else {
            preciseAssembler_1 = ItemList.Machine_IV_Assembler.get(1);
            highComputationStationT3_32 = GT_OreDictUnificator
                    .get(OrePrefixes.circuit, Materials.SuperconductorUHV, 32);
            highComputationStationT4_32 = GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Infinite, 32);
            highComputationStationT5_32 = GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Bio, 32);
            metaStableOgScrew_64 = GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Neutronium, 64);
            shirabonGear_8 = GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Infinity, 8);
            shirabonGearSmall_16 = GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Infinity, 16);
            titaniumBetaCScrew_64 = GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Osmiridium, 64);
        }

        if (Loader.isModLoaded("bartworks")) {
            voidMiner = ItemRegistry.voidminer[2];
        } else {
            voidMiner = ItemList.OreDrill4.get(1);
        }

        // Planetary Gas Siphon Controller
        RecipeUtil.addRecipe(
                IGItems.PlanetaryGasSiphon,
                new Object[] { "MPM", "CTC", "HTH", 'M', ItemList.Electric_Motor_IV.get(1), 'P',
                        ItemList.Electric_Pump_IV.get(1), 'C', "circuitElite", 'T',
                        GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.TungstenSteel, 1), 'H',
                        ItemList.Hull_IV.get(1) });

        // Space Elevator Controller
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                GT_ModHandler.getModItem("OpenBlocks", "elevator", 1, 0),
                256000,
                256,
                1000000,
                4,
                new Object[] { GT_ModHandler.getModItem("OpenBlocks", "elevator", 1),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 64),
                        ItemList.Field_Generator_UV.get(16),
                        new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 4 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 4 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 4 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 4 },
                        GT_ModHandler.getModItem("dreamcraft", "item.HeavyDutyPlateTier7", 32),
                        ItemList.Circuit_Chip_PPIC.get(64),
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.CosmicNeutronium, 64),
                        ItemList.Electric_Motor_UV.get(32), new ItemStack(IGBlocks.SpaceElevatorCasing, 8) },
                new FluidStack[] { new FluidStack(solderLuV, 5760), Materials.UUMatter.getFluid(16000),
                        Materials.Lubricant.getFluid(32000), Materials.Neutronium.getMolten(1440) },
                IGItems.SpaceElevatorController,
                36000,
                (int) VP[9]);

        // Nanotube spool
        RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Graphene, 64),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Graphene, 64),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Graphene, 64),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Graphene, 64), },
                Materials.AdvancedGlue.getFluid(720),
                new ItemStack(IGItems.SpaceElevatorItems, 1, 0),
                1200,
                (int) VP[7],
                true);

        // Space Elevator Cable
        TT_recipeAdder.addResearchableAssemblylineRecipe(
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
                1200,
                (int) VP[9]);

        // Space Elevator Base Casing
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Neutronium, 1),
                64000,
                128,
                500000,
                2,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 8),
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Palladium, 32),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 64),
                        new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 4 },
                        ItemList.Electric_Piston_UV.get(2),
                        GT_OreDictUnificator.get(OrePrefixes.ring, Materials.CosmicNeutronium, 8), },
                new FluidStack[] { new FluidStack(solderLuV, 5760), Materials.UUMatter.getFluid(2000),
                        Materials.Iridium.getMolten(1152) },
                new ItemStack(IGBlocks.SpaceElevatorCasing, 8, 0),
                1200,
                (int) VP[8]);

        // Space Elevator Support Structure
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 1),
                64000,
                128,
                500000,
                2,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 8),
                        GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Naquadria, 16),
                        GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Neutronium, 8),
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Osmiridium, 8), },
                new FluidStack[] { new FluidStack(solderLuV, 5760), Materials.UUMatter.getFluid(1000),
                        Materials.Iridium.getMolten(1440) },
                new ItemStack(IGBlocks.SpaceElevatorCasing, 8, 1),
                1200,
                (int) VP[8]);

        // Space Elevator Internal Structure
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                new ItemStack(TT_Container_Casings.sBlockCasingsTT, 1, 0),
                64000,
                128,
                500000,
                2,
                new Object[] { new ItemStack(TT_Container_Casings.sBlockCasingsTT, 8, 0),
                        GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Palladium, 16),
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Neutronium, 8), },
                new FluidStack[] { new FluidStack(solderLuV, 5760), Materials.UUMatter.getFluid(8000),
                        Materials.Concrete.getMolten(1440) },
                new ItemStack(IGBlocks.SpaceElevatorCasing, 8, 2),
                1200,
                (int) VP[8]);

        // Space Elevator Motor MK-I
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                new ItemStack(IGBlocks.SpaceElevatorCasing, 1, 0),
                64000,
                128,
                500000,
                2,
                new Object[] { new ItemStack(IGBlocks.SpaceElevatorCasing, 1, 0), ItemList.Electric_Motor_UV.get(4),
                        GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Neutronium, 8),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Neutronium, 4),
                        new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 1 },
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.CosmicNeutronium, 16),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 16), },
                new FluidStack[] { new FluidStack(solderLuV, 5760), Materials.UUMatter.getFluid(8000),
                        Materials.Naquadria.getMolten(1440), Materials.Lubricant.getFluid(16000) },
                new ItemStack(IGBlocks.SpaceElevatorMotor, 1, 0),
                2400,
                (int) VP[8]);

        // Space Elevator Motor MK-II
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                new ItemStack(IGBlocks.SpaceElevatorMotor, 1, 0),
                128000,
                256,
                2000000,
                2,
                new Object[] { new ItemStack(IGBlocks.SpaceElevatorCasing, 1, 0), ItemList.Electric_Motor_UHV.get(4),
                        GT_OreDictUnificator.get(OrePrefixes.ring, Materials.CosmicNeutronium, 8),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.CosmicNeutronium, 4),
                        new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 1 },
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Infinity, 16),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 16), },
                new FluidStack[] { new FluidStack(solderLuV, 5760), Materials.UUMatter.getFluid(8000),
                        Materials.Naquadria.getMolten(1440), Materials.Lubricant.getFluid(16000) },
                new ItemStack(IGBlocks.SpaceElevatorMotor, 1, 1),
                2400,
                (int) VP[9]);

        // Space Elevator Motor MK-III
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                new ItemStack(IGBlocks.SpaceElevatorMotor, 1, 1),
                128000,
                256,
                2000000,
                2,
                new Object[] { new ItemStack(IGBlocks.SpaceElevatorCasing, 1, 0), ItemList.Electric_Motor_UEV.get(4),
                        GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Infinity, 8),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Infinity, 4),
                        new Object[] { OrePrefixes.circuit.get(Materials.Bio), 1 },
                        GT_OreDictUnificator.get(OrePrefixes.screw, MaterialsUEVplus.TranscendentMetal, 16),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 16), },
                new FluidStack[] { new FluidStack(solderUEV, 2880), Materials.UUMatter.getFluid(8000),
                        Materials.Naquadria.getMolten(1440), Materials.Lubricant.getFluid(24000) },
                new ItemStack(IGBlocks.SpaceElevatorMotor, 1, 2),
                2400,
                (int) VP[10]);

        // Space Elevator Motor MK-IV
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                new ItemStack(IGBlocks.SpaceElevatorMotor, 1, 2),
                128000,
                256,
                2000000,
                2,
                new Object[] { new ItemStack(IGBlocks.SpaceElevatorCasing, 1, 0), ItemList.Electric_Motor_UIV.get(4),
                        GT_OreDictUnificator.get(OrePrefixes.ring, MaterialsUEVplus.TranscendentMetal, 8),
                        GT_OreDictUnificator.get(OrePrefixes.stick, MaterialsUEVplus.TranscendentMetal, 4),
                        new Object[] { OrePrefixes.circuit.get(Materials.Optical), 1 },
                        GT_OreDictUnificator.get(OrePrefixes.screw, MaterialsUEVplus.SpaceTime, 16),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 16), },
                new FluidStack[] { new FluidStack(solderUEV, 2880), Materials.UUMatter.getFluid(8000),
                        Materials.Naquadria.getMolten(1440), Materials.Lubricant.getFluid(24000) },
                new ItemStack(IGBlocks.SpaceElevatorMotor, 1, 3),
                2400,
                (int) VP[11]);

        // Space Elevator Motor MK-V
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                new ItemStack(IGBlocks.SpaceElevatorMotor, 1, 3),
                128000,
                256,
                2000000,
                2,
                new Object[] { new ItemStack(IGBlocks.SpaceElevatorCasing, 1, 0), ItemList.Electric_Motor_UMV.get(4),
                        GT_OreDictUnificator.get(OrePrefixes.ring, MaterialsUEVplus.SpaceTime, 8),
                        GT_OreDictUnificator.get(OrePrefixes.stick, MaterialsUEVplus.SpaceTime, 4),
                        new ItemStack(pikoCircuit, 1),
                        GT_OreDictUnificator.get(OrePrefixes.screw, MaterialsUEVplus.Universium, 16),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 16), },
                new FluidStack[] { new FluidStack(solderUEV, 2880), Materials.UUMatter.getFluid(8000),
                        Materials.Naquadria.getMolten(1440), Materials.Lubricant.getFluid(24000) },
                new ItemStack(IGBlocks.SpaceElevatorMotor, 1, 4),
                2400,
                (int) VP[11]);

        // Space Elevator Modules

        // Pump Module MK-I
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                IGItems.PlanetaryGasSiphon,
                16777216,
                2048,
                2000000,
                4,
                new Object[] { ItemList.OilDrillInfinite.get(1), IGItems.PlanetaryGasSiphon,
                        CustomItemList.enderLinkFluidCover.get(2),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 4),
                        new Object[] { OrePrefixes.circuit.get(Materials.Bio), 4 }, ItemList.Electric_Pump_UEV.get(2),
                        GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Infinity, 4),
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.CosmicNeutronium, 32), },
                new FluidStack[] { new FluidStack(solderUEV, 1296), Materials.Infinity.getMolten(576) },
                IGItems.SpaceElevatorModulePumpT1,
                6000,
                (int) VP[10]);

        // Pump Module MK-II
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                IGItems.SpaceElevatorModulePumpT1,
                33554432,
                8192,
                64000000,
                4,
                new Object[] { ItemList.OilDrillInfinite.get(4), GT_Utility.copyAmount(4, IGItems.PlanetaryGasSiphon),
                        CustomItemList.enderLinkFluidCover.get(8),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.SpaceTime, 4),
                        new Object[] { OrePrefixes.circuit.get(Materials.Optical), 16 },
                        ItemList.Electric_Pump_UIV.get(8),
                        GT_OreDictUnificator.get(OrePrefixes.gearGt, MaterialsUEVplus.SpaceTime, 4),
                        metaStableOgScrew_64, },
                new FluidStack[] { new FluidStack(solderUEV, 4608),
                        MaterialsUEVplus.TranscendentMetal.getMolten(2304) },
                IGItems.SpaceElevatorModulePumpT2,
                600 * 20,
                (int) VP[11]);

        // Pump Module MK-II - Assembler alt
        RA.addAssemblerRecipe(
                new ItemStack[] { GT_Utility.copyAmount(4, IGItems.SpaceElevatorModulePumpT1),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.SpaceTime, 8),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Optical, 16),
                        ItemList.Electric_Pump_UIV.get(8),
                        GT_OreDictUnificator.get(OrePrefixes.gearGt, MaterialsUEVplus.SpaceTime, 8),
                        metaStableOgScrew_64,
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, MaterialsUEVplus.TranscendentMetal, 16), },
                new FluidStack(solderUEV, 4608),
                IGItems.SpaceElevatorModulePumpT2,
                1200 * 20,
                (int) VP[11],
                false);

        // Assembler Module MK-I
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                preciseAssembler_1,
                256000,
                256,
                4000000,
                4,
                new Object[] { new ItemStack(IGBlocks.SpaceElevatorCasing, 1, 0),
                        new ItemStack(GameRegistry.findItem("gregtech", "gt.blockmachines"), 4, 10782),
                        new ItemStack(GameRegistry.findItem("gregtech", "gt.blockmachines"), 4, 1187),
                        GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.CosmicNeutronium, 8),
                        GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.CosmicNeutronium, 16),
                        ItemList.Robot_Arm_UHV.get(8), ItemList.Conveyor_Module_UHV.get(16),
                        highComputationStationT3_32,
                        new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 16 },
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 8),
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Neutronium, 32) },
                new FluidStack[] { new FluidStack(solderLuV, 1296), Materials.Naquadria.getMolten(1296),
                        Materials.Lubricant.getFluid(16000) },
                IGItems.SpaceElevatorModuleAssemblerT1,
                600 * 20,
                (int) VP[10]);

        // Assembler Module MK-II
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                IGItems.SpaceElevatorModuleAssemblerT1,
                2048000,
                2048,
                64000000,
                4,
                new Object[] { new ItemStack(IGBlocks.SpaceElevatorCasing, 1, 0),
                        new ItemStack(GameRegistry.findItem("gregtech", "gt.blockmachines"), 4, 10784),
                        new ItemStack(GameRegistry.findItem("gregtech", "gt.blockmachines"), 4, 12091),
                        GT_OreDictUnificator.get(OrePrefixes.gearGt, MaterialsUEVplus.TranscendentMetal, 8),
                        GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, MaterialsUEVplus.TranscendentMetal, 16),
                        ItemList.Robot_Arm_UIV.get(8), ItemList.Conveyor_Module_UIV.get(16),
                        highComputationStationT4_32, new Object[] { OrePrefixes.circuit.get(Materials.Optical), 16 },
                        hypogenFrameBox_8, hypogenScrew_32 },
                new FluidStack[] { new FluidStack(solderUEV, 1296), Materials.Infinity.getMolten(1296),
                        Materials.UUMatter.getFluid(16000) },
                IGItems.SpaceElevatorModuleAssemblerT2,
                600 * 20,
                (int) VP[11]);

        // Assembler Module MK-III
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                IGItems.SpaceElevatorModuleAssemblerT2,
                32768000,
                4096,
                256000000,
                4,
                new Object[] { new ItemStack(IGBlocks.SpaceElevatorCasing, 1, 0),
                        new ItemStack(GameRegistry.findItem("gregtech", "gt.blockmachines"), 4, 10786),
                        new ItemStack(GameRegistry.findItem("gregtech", "gt.blockmachines"), 4, 12093),
                        GT_OreDictUnificator.get(
                                OrePrefixes.gearGt,
                                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter,
                                8),
                        shirabonGear_8,
                        GT_OreDictUnificator.get(
                                OrePrefixes.gearGtSmall,
                                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter,
                                16),
                        shirabonGearSmall_16, ItemList.Robot_Arm_UXV.get(8), ItemList.Conveyor_Module_UXV.get(16),
                        highComputationStationT5_32, new ItemStack(quantumCircuit, 16),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.Universium, 8),
                        GT_OreDictUnificator.get(OrePrefixes.screw, MaterialsUEVplus.Universium, 32) },
                new FluidStack[] { new FluidStack(solderUEV, 5184), MaterialsUEVplus.BlackDwarfMatter.getMolten(1296),
                        MaterialsUEVplus.WhiteDwarfMatter.getMolten(1296), MaterialsUEVplus.SpaceTime.getMolten(1296) },
                IGItems.SpaceElevatorModuleAssemblerT3,
                600 * 20,
                (int) VP[13]);

        // Research Module
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                new ItemStack(GameRegistry.findItem("gregtech", "gt.blockmachines"), 1, 11012),
                512000,
                512,
                16000000,
                4,
                new Object[] { CustomItemList.Machine_Multi_Research.get(4), ItemList.Sensor_UHV.get(4),
                        new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 16 },
                        GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Infinity, 4),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 4),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.CosmicNeutronium, 64),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.CosmicNeutronium, 64),
                        metaStableOgScrew_64,
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 32) },
                new FluidStack[] { new FluidStack(solderLuV, 4608), Materials.Infinity.getMolten(2304),
                        Materials.UUMatter.getFluid(8000), Materials.SuperCoolant.getFluid(4000) },
                IGItems.SpaceElevatorModuleResearch,
                600 * 20,
                (int) VP[10]);

        // Project Manager Module
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                new ItemStack(GameRegistry.findItem("miscutils", "blockProjectBench"), 1),
                256000,
                512,
                1000000,
                4,
                new Object[] { new ItemStack(GameRegistry.findItem("miscutils", "blockProjectBench"), 4),
                        ItemList.Emitter_UV.get(2), ItemList.Sensor_UV.get(2),
                        new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 16 },
                        GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Neutronium, 4),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 4),
                        new ItemStack(
                                GameRegistry.findItem("structurelib", "item.structurelib.constructableTrigger"),
                                64),
                        titaniumBetaCScrew_64, },
                new FluidStack[] { new FluidStack(solderLuV, 4608), Materials.Iridium.getMolten(2304),
                        Materials.UUMatter.getFluid(2000) },
                IGItems.SpaceElevatorModuleManager,
                300 * 20,
                (int) VP[9]);

        // Miner Module MK-I
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                voidMiner,
                2000000,
                512,
                2000000,
                8,
                new Object[] { ItemList.OreDrill4.get(1), ItemList.Robot_Arm_UV.get(8),
                        ItemList.Field_Generator_UV.get(4),
                        new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 16 },
                        ItemList.Sensor_UV.get(16),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUV, 32),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 16) },
                new FluidStack[] { new FluidStack(solderLuV, 2880), Materials.Naquadria.getMolten(1440),
                        Materials.Lubricant.getFluid(8000) },
                IGItems.SpaceElevatorModuleMinerT1,
                4500,
                (int) VP[9]);
        // Miner Module MK-II
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                IGItems.SpaceElevatorModuleMinerT1,
                3000000,
                1024,
                3000000,
                12,
                new Object[] { IGItems.SpaceElevatorModuleMinerT1, ItemList.Robot_Arm_UHV.get(8),
                        ItemList.Field_Generator_UHV.get(4),
                        new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 16 }, ItemList.Sensor_UHV.get(16),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 32),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 16) },
                new FluidStack[] { new FluidStack(solderLuV, 2880), Materials.Naquadria.getMolten(2880),
                        Materials.Lubricant.getFluid(16000) },
                IGItems.SpaceElevatorModuleMinerT2,
                9000,
                (int) VP[10]);
        // Miner Module MK-III
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                IGItems.SpaceElevatorModuleMinerT2,
                4000000,
                2048,
                4000000,
                16,
                new Object[] { IGItems.SpaceElevatorModuleMinerT2, ItemList.Robot_Arm_UEV.get(8),
                        ItemList.Field_Generator_UEV.get(4),
                        new Object[] { OrePrefixes.circuit.get(Materials.Bio), 16 }, ItemList.Sensor_UEV.get(16),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUEV, 32),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 16) },
                new FluidStack[] { new FluidStack(solderUEV, 2880), MaterialsUEVplus.TranscendentMetal.getMolten(1440),
                        Materials.UUMatter.getFluid(2000) },
                IGItems.SpaceElevatorModuleMinerT3,
                18000,
                (int) VP[11]);

        // Mining drones

        // LV
        RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.toolHeadDrill, Materials.Titanium, 8),
                        ItemList.Robot_Arm_LV.get(8), ItemList.Field_Generator_LV.get(2),
                        GT_OreDictUnificator.get("circuitAdvanced", 4), new ItemStack(GCItems.heavyPlatingTier1, 16),
                        new ItemStack(GCItems.rocketEngine, 2), ItemList.Sensor_LV.get(8) },
                Materials.SolderingAlloy.getMolten(720),
                new ItemStack(IGItems.MiningDrones, 1, ItemMiningDrones.DroneTiers.LV.ordinal()),
                1200,
                (int) VP[1],
                false);

        // MV
        RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.toolHeadDrill, Materials.TungstenSteel, 8),
                        ItemList.Robot_Arm_MV.get(8), ItemList.Field_Generator_MV.get(2),
                        GT_OreDictUnificator.get("circuitData", 4), new ItemStack(GCItems.heavyPlatingTier1, 32),
                        new ItemStack(GCItems.rocketEngine, 4), ItemList.Sensor_MV.get(8) },
                Materials.SolderingAlloy.getMolten(1440),
                new ItemStack(IGItems.MiningDrones, 1, ItemMiningDrones.DroneTiers.MV.ordinal()),
                1200,
                (int) VP[2],
                false);

        // HV
        RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.toolHeadDrill, Materials.Iridium, 8),
                        ItemList.Robot_Arm_HV.get(8), ItemList.Field_Generator_HV.get(2),
                        GT_OreDictUnificator.get("circuitElite", 4), new ItemStack(MarsItems.marsItemBasic, 32, 3),
                        new ItemStack(GCItems.rocketEngine, 4), ItemList.Sensor_HV.get(8) },
                Materials.SolderingAlloy.getMolten(1440),
                new ItemStack(IGItems.MiningDrones, 1, ItemMiningDrones.DroneTiers.HV.ordinal()),
                1200,
                (int) VP[3],
                false);

        // EV
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                new ItemStack(IGItems.MiningDrones, 1, ItemMiningDrones.DroneTiers.HV.ordinal()),
                50000,
                128,
                1000000,
                4,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.toolHeadDrill, Materials.Trinium, 8),
                        ItemList.Robot_Arm_EV.get(8), ItemList.Field_Generator_EV.get(2),
                        new Object[] { OrePrefixes.circuit.get(Materials.Master), 4 },
                        new ItemStack(AsteroidsItems.basicItem, 32, 0), new ItemStack(AsteroidsItems.basicItem, 4, 1),
                        ItemList.Sensor_EV.get(8) },
                new FluidStack[] { new FluidStack(solderLuV, 720), Materials.Iridium.getMolten(720),
                        new FluidStack(FluidRegistry.getFluid("liquid_drillingfluid"), 16000) },
                new ItemStack(IGItems.MiningDrones, 1, ItemMiningDrones.DroneTiers.EV.ordinal()),
                1200,
                (int) VP[4]);

        // IV
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                new ItemStack(IGItems.MiningDrones, 1, ItemMiningDrones.DroneTiers.EV.ordinal()),
                75000,
                128,
                1000000,
                8,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.toolHeadDrill, Materials.NaquadahAlloy, 8),
                        ItemList.Robot_Arm_IV.get(8), ItemList.Field_Generator_IV.get(2),
                        new Object[] { OrePrefixes.circuit.get(Materials.Ultimate), 4 },
                        GT_ModHandler.getModItem("dreamcraft", "item.HeavyDutyPlateTier4", 32),
                        new ItemStack(AsteroidsItems.basicItem, 4, 1), ItemList.Sensor_IV.get(8) },
                new FluidStack[] { new FluidStack(solderLuV, 1440), Materials.Iridium.getMolten(1440),
                        new FluidStack(FluidRegistry.getFluid("liquid_drillingfluid"), 32000) },
                new ItemStack(IGItems.MiningDrones, 1, ItemMiningDrones.DroneTiers.IV.ordinal()),
                1200,
                (int) VP[5]);

        // LuV
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                new ItemStack(IGItems.MiningDrones, 1, ItemMiningDrones.DroneTiers.IV.ordinal()),
                100000,
                256,
                2000000,
                4,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.toolHeadDrill, Materials.Naquadria, 8),
                        ItemList.Robot_Arm_LuV.get(8), ItemList.Field_Generator_LuV.get(2),
                        new Object[] { OrePrefixes.circuit.get(Materials.Ultimate), 4 },
                        GT_ModHandler.getModItem("dreamcraft", "item.HeavyDutyPlateTier5", 32),
                        GT_ModHandler.getModItem("dreamcraft", "item.HeavyDutyRocketEngineTier3", 4),
                        ItemList.Sensor_LuV.get(8) },
                new FluidStack[] { new FluidStack(solderLuV, 2880), Materials.Osmiridium.getMolten(1440),
                        new FluidStack(FluidRegistry.getFluid("liquid_drillingfluid"), 64000) },
                new ItemStack(IGItems.MiningDrones, 1, ItemMiningDrones.DroneTiers.LuV.ordinal()),
                1200,
                (int) VP[6]);

        // ZPM
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                new ItemStack(IGItems.MiningDrones, 1, ItemMiningDrones.DroneTiers.LuV.ordinal()),
                125000,
                256,
                2000000,
                8,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.toolHeadDrill, Materials.Neutronium, 8),
                        ItemList.Robot_Arm_ZPM.get(8), ItemList.Field_Generator_ZPM.get(2),
                        new Object[] { OrePrefixes.circuit.get(Materials.SuperconductorUHV), 2 },
                        GT_ModHandler.getModItem("dreamcraft", "item.HeavyDutyPlateTier6", 32),
                        GT_ModHandler.getModItem("dreamcraft", "item.HeavyDutyRocketEngineTier3", 4),
                        ItemList.Sensor_ZPM.get(8) },
                new FluidStack[] { new FluidStack(solderLuV, 2880), Materials.Osmiridium.getMolten(1440),
                        new FluidStack(FluidRegistry.getFluid("liquid_drillingfluid"), 128000) },
                new ItemStack(IGItems.MiningDrones, 1, ItemMiningDrones.DroneTiers.ZPM.ordinal()),
                1200,
                (int) VP[7]);

        // UV
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                new ItemStack(IGItems.MiningDrones, 1, ItemMiningDrones.DroneTiers.ZPM.ordinal()),
                150000,
                512,
                4000000,
                4,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.toolHeadDrill, Materials.CosmicNeutronium, 8),
                        ItemList.Robot_Arm_UV.get(8), ItemList.Field_Generator_UV.get(2),
                        new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 4 },
                        GT_ModHandler.getModItem("dreamcraft", "item.HeavyDutyPlateTier7", 32),
                        GT_ModHandler.getModItem("dreamcraft", "item.HeavyDutyRocketEngineTier4", 4),
                        ItemList.Sensor_UV.get(8) },
                new FluidStack[] { new FluidStack(solderLuV, 2880), Materials.Naquadria.getMolten(1440),
                        new FluidStack(FluidRegistry.getFluid("liquid_drillingfluid"), 256000) },
                new ItemStack(IGItems.MiningDrones, 1, ItemMiningDrones.DroneTiers.UV.ordinal()),
                1200,
                (int) VP[8]);

        // UHV
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                new ItemStack(IGItems.MiningDrones, 1, ItemMiningDrones.DroneTiers.UV.ordinal()),
                175000,
                512,
                4000000,
                8,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.toolHeadDrill, Materials.Infinity, 8),
                        ItemList.Robot_Arm_UHV.get(8), ItemList.Field_Generator_UHV.get(2),
                        new Object[] { OrePrefixes.circuit.get(Materials.Bio), 4 },
                        GT_ModHandler.getModItem("dreamcraft", "item.HeavyDutyPlateTier8", 32),
                        GT_ModHandler.getModItem("dreamcraft", "item.HeavyDutyRocketEngineTier4", 4),
                        ItemList.Sensor_UHV.get(8) },
                new FluidStack[] { new FluidStack(solderUEV, 2880), Materials.Neutronium.getMolten(1440),
                        new FluidStack(FluidRegistry.getFluid("liquid_drillingfluid"), 512000) },
                new ItemStack(IGItems.MiningDrones, 1, ItemMiningDrones.DroneTiers.UHV.ordinal()),
                1200,
                (int) VP[9]);

        // UEV
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                new ItemStack(IGItems.MiningDrones, 1, ItemMiningDrones.DroneTiers.UHV.ordinal()),
                200000,
                512,
                4000000,
                8,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.toolHeadDrill, Materials.CosmicNeutronium, 8),
                        ItemList.Robot_Arm_UEV.get(8), ItemList.Field_Generator_UEV.get(2),
                        new Object[] { OrePrefixes.circuit.get(Materials.Optical), 4 },
                        GT_ModHandler.getModItem("dreamcraft", "item.HeavyDutyPlateTier8", 64),
                        GT_ModHandler.getModItem("dreamcraft", "item.HeavyDutyRocketEngineTier4", 8),
                        ItemList.Sensor_UEV.get(8) },
                new FluidStack[] { new FluidStack(solderUEV, 2880), Materials.Quantium.getMolten(1440),
                        new FluidStack(FluidRegistry.getFluid("liquid_drillingfluid"), 512000) },
                new ItemStack(IGItems.MiningDrones, 1, ItemMiningDrones.DroneTiers.UEV.ordinal()),
                1200,
                (int) VP[10]);
    }
}
