package com.gtnewhorizons.gtnhintergalactic.loader;

import com.gtnewhorizons.gtnhintergalactic.item.IGItems;
import com.gtnewhorizons.gtnhintergalactic.tile.multi.TileEntityPlanetaryGasSiphon;
import com.gtnewhorizons.gtnhintergalactic.tile.multi.elevator.TileEntitySpaceElevator;
import com.gtnewhorizons.gtnhintergalactic.tile.multi.elevatormodules.*;

import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;

/**
 * Loader for all machines
 *
 * @author minecraft7771
 */
public class MachineLoader implements Runnable {

    /**
     * Execute the machine loader
     */
    @Override
    public void run() {

        IGItems.PlanetaryGasSiphon = new TileEntityPlanetaryGasSiphon(
                14002,
                "PlanetaryGasSiphon",
                GCCoreUtil.translate("gt.blockmachines.multimachine.ig.siphon.name")).getStackForm(1);

        IGItems.SpaceElevatorController = new TileEntitySpaceElevator(
                14003,
                "SpaceElevator",
                GCCoreUtil.translate("gt.blockmachines.multimachine.ig.elevator.name")).getStackForm(1);

        IGItems.SpaceElevatorModuleAssemblerT1 = new TileEntityModuleAssembler.TileEntityModuleAssemblerT1(
                14004,
                "ProjectModuleAssemblerT1",
                GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.assembler.t1.name")).getStackForm(1);
        IGItems.SpaceElevatorModuleAssemblerT2 = new TileEntityModuleAssembler.TileEntityModuleAssemblerT2(
                14005,
                "ProjectModuleAssemblerT2",
                GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.assembler.t2.name")).getStackForm(1);
        IGItems.SpaceElevatorModuleAssemblerT3 = new TileEntityModuleAssembler.TileEntityModuleAssemblerT3(
                14006,
                "ProjectModuleAssemblerT3",
                GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.assembler.t3.name")).getStackForm(1);

        IGItems.SpaceElevatorModuleMinerT1 = new TileEntityModuleMiner.TileEntityModuleMinerT1(
                14007,
                "ProjectModuleMinerT1",
                GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.miner.t1.name")).getStackForm(1);
        IGItems.SpaceElevatorModuleMinerT2 = new TileEntityModuleMiner.TileEntityModuleMinerT2(
                14008,
                "ProjectModuleMinerT2",
                GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.miner.t2.name")).getStackForm(1);
        IGItems.SpaceElevatorModuleMinerT3 = new TileEntityModuleMiner.TileEntityModuleMinerT3(
                14009,
                "ProjectModuleMinerT3",
                GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.miner.t3.name")).getStackForm(1);

        IGItems.SpaceElevatorModulePumpT1 = new TileEntityModulePump.TileEntityModulePumpT1(
                14010,
                "ProjectModulePumpT1",
                GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.pump.t1.name")).getStackForm(1);
        IGItems.SpaceElevatorModulePumpT2 = new TileEntityModulePump.TileEntityModulePumpT2(
                14011,
                "ProjectModulePumpT2",
                GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.pump.t2.name")).getStackForm(1);

        IGItems.SpaceElevatorModuleManager = new TileEntityModuleManager(
                14012,
                "ProjectModuleManager",
                GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.manager.t1.name")).getStackForm(1);

        IGItems.SpaceElevatorModuleResearch = new TileEntityModuleResearch(
                14013,
                "ProjectModuleResearch",
                GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.research.t1.name")).getStackForm(1);
    }
}
