package com.gtnewhorizons.gtnhintergalactic.loader;

import static gregtech.api.enums.MetaTileEntityIDs.PlanetaryGasSiphonController;
import static gregtech.api.enums.MetaTileEntityIDs.SpaceElevatorController;
import static gregtech.api.enums.MetaTileEntityIDs.SpaceElevatorModuleAssemblerT1;
import static gregtech.api.enums.MetaTileEntityIDs.SpaceElevatorModuleAssemblerT2;
import static gregtech.api.enums.MetaTileEntityIDs.SpaceElevatorModuleAssemblerT3;
import static gregtech.api.enums.MetaTileEntityIDs.SpaceElevatorModuleManager;
import static gregtech.api.enums.MetaTileEntityIDs.SpaceElevatorModuleMinerT1;
import static gregtech.api.enums.MetaTileEntityIDs.SpaceElevatorModuleMinerT2;
import static gregtech.api.enums.MetaTileEntityIDs.SpaceElevatorModuleMinerT3;
import static gregtech.api.enums.MetaTileEntityIDs.SpaceElevatorModulePumpT1;
import static gregtech.api.enums.MetaTileEntityIDs.SpaceElevatorModulePumpT2;
import static gregtech.api.enums.MetaTileEntityIDs.SpaceElevatorModulePumpT3;
import static gregtech.api.enums.MetaTileEntityIDs.SpaceElevatorModuleResearch;

import com.gtnewhorizons.gtnhintergalactic.item.IGItems;
import com.gtnewhorizons.gtnhintergalactic.tile.multi.TileEntityPlanetaryGasSiphon;
import com.gtnewhorizons.gtnhintergalactic.tile.multi.elevator.TileEntitySpaceElevator;
import com.gtnewhorizons.gtnhintergalactic.tile.multi.elevatormodules.TileEntityModuleAssembler;
import com.gtnewhorizons.gtnhintergalactic.tile.multi.elevatormodules.TileEntityModuleManager;
import com.gtnewhorizons.gtnhintergalactic.tile.multi.elevatormodules.TileEntityModuleMiner;
import com.gtnewhorizons.gtnhintergalactic.tile.multi.elevatormodules.TileEntityModulePump;
import com.gtnewhorizons.gtnhintergalactic.tile.multi.elevatormodules.TileEntityModuleResearch;

import gregtech.api.enums.ItemList;
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
                PlanetaryGasSiphonController.ID,
                "PlanetaryGasSiphon",
                GCCoreUtil.translate("gt.blockmachines.multimachine.ig.siphon.name")).getStackForm(1);

        IGItems.SpaceElevatorController = new TileEntitySpaceElevator(
                SpaceElevatorController.ID,
                "SpaceElevator",
                GCCoreUtil.translate("gt.blockmachines.multimachine.ig.elevator.name")).getStackForm(1);
        ItemList.SpaceElevatorController.set(IGItems.SpaceElevatorController);

        IGItems.SpaceElevatorModuleAssemblerT1 = new TileEntityModuleAssembler.TileEntityModuleAssemblerT1(
                SpaceElevatorModuleAssemblerT1.ID,
                "ProjectModuleAssemblerT1",
                GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.assembler.t1.name")).getStackForm(1);
        IGItems.SpaceElevatorModuleAssemblerT2 = new TileEntityModuleAssembler.TileEntityModuleAssemblerT2(
                SpaceElevatorModuleAssemblerT2.ID,
                "ProjectModuleAssemblerT2",
                GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.assembler.t2.name")).getStackForm(1);
        IGItems.SpaceElevatorModuleAssemblerT3 = new TileEntityModuleAssembler.TileEntityModuleAssemblerT3(
                SpaceElevatorModuleAssemblerT3.ID,
                "ProjectModuleAssemblerT3",
                GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.assembler.t3.name")).getStackForm(1);

        IGItems.SpaceElevatorModuleMinerT1 = new TileEntityModuleMiner.TileEntityModuleMinerT1(
                SpaceElevatorModuleMinerT1.ID,
                "ProjectModuleMinerT1",
                GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.miner.t1.name")).getStackForm(1);
        IGItems.SpaceElevatorModuleMinerT2 = new TileEntityModuleMiner.TileEntityModuleMinerT2(
                SpaceElevatorModuleMinerT2.ID,
                "ProjectModuleMinerT2",
                GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.miner.t2.name")).getStackForm(1);
        IGItems.SpaceElevatorModuleMinerT3 = new TileEntityModuleMiner.TileEntityModuleMinerT3(
                SpaceElevatorModuleMinerT3.ID,
                "ProjectModuleMinerT3",
                GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.miner.t3.name")).getStackForm(1);

        IGItems.SpaceElevatorModulePumpT1 = new TileEntityModulePump.TileEntityModulePumpT1(
                SpaceElevatorModulePumpT1.ID,
                "ProjectModulePumpT1",
                GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.pump.t1.name")).getStackForm(1);
        IGItems.SpaceElevatorModulePumpT2 = new TileEntityModulePump.TileEntityModulePumpT2(
                SpaceElevatorModulePumpT2.ID,
                "ProjectModulePumpT2",
                GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.pump.t2.name")).getStackForm(1);
        IGItems.SpaceElevatorModulePumpT3 = new TileEntityModulePump.TileEntityModulePumpT3(
                SpaceElevatorModulePumpT3.ID,
                "ProjectModulePumpT3",
                GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.pump.t3.name")).getStackForm(1);

        IGItems.SpaceElevatorModuleManager = new TileEntityModuleManager(
                SpaceElevatorModuleManager.ID,
                "ProjectModuleManager",
                GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.manager.t1.name")).getStackForm(1);

        IGItems.SpaceElevatorModuleResearch = new TileEntityModuleResearch(
                SpaceElevatorModuleResearch.ID,
                "ProjectModuleResearch",
                GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.research.t1.name")).getStackForm(1);
    }
}
