package gtnhintergalactic.loader;

import static gregtech.api.enums.MetaTileEntityIDs.DysonSwarmController;
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

import net.minecraft.item.ItemStack;

import gregtech.api.enums.ItemList;
import gtnhintergalactic.tile.multi.TileEntityDysonSwarm;
import gtnhintergalactic.tile.multi.TileEntityPlanetaryGasSiphon;
import gtnhintergalactic.tile.multi.elevator.TileEntitySpaceElevator;
import gtnhintergalactic.tile.multi.elevatormodules.TileEntityModuleAssembler;
import gtnhintergalactic.tile.multi.elevatormodules.TileEntityModuleManager;
import gtnhintergalactic.tile.multi.elevatormodules.TileEntityModuleMiner;
import gtnhintergalactic.tile.multi.elevatormodules.TileEntityModulePump;
import gtnhintergalactic.tile.multi.elevatormodules.TileEntityModuleResearch;
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

        ItemStack stack;

        stack = new TileEntityPlanetaryGasSiphon(
            PlanetaryGasSiphonController.ID,
            "PlanetaryGasSiphon",
            GCCoreUtil.translate("gt.blockmachines.multimachine.ig.siphon.name")).getStackForm(1);
        ItemList.PlanetaryGasSiphonController.set(stack);

        stack = new TileEntityDysonSwarm(
            DysonSwarmController.ID,
            "DysonSwarm",
            GCCoreUtil.translate("gt.blockmachines.multimachine.ig.dyson.name")).getStackForm(1);
        ItemList.DysonSwarmController.set(stack);

        stack = new TileEntitySpaceElevator(
            SpaceElevatorController.ID,
            "SpaceElevator",
            GCCoreUtil.translate("gt.blockmachines.multimachine.ig.elevator.name")).getStackForm(1);
        ItemList.SpaceElevatorController.set(stack);

        stack = new TileEntityModuleAssembler.TileEntityModuleAssemblerT1(
            SpaceElevatorModuleAssemblerT1.ID,
            "ProjectModuleAssemblerT1",
            GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.assembler.t1.name")).getStackForm(1);
        ItemList.SpaceElevatorModuleAssemblerT1.set(stack);

        stack = new TileEntityModuleAssembler.TileEntityModuleAssemblerT2(
            SpaceElevatorModuleAssemblerT2.ID,
            "ProjectModuleAssemblerT2",
            GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.assembler.t2.name")).getStackForm(1);
        ItemList.SpaceElevatorModuleAssemblerT2.set(stack);

        stack = new TileEntityModuleAssembler.TileEntityModuleAssemblerT3(
            SpaceElevatorModuleAssemblerT3.ID,
            "ProjectModuleAssemblerT3",
            GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.assembler.t3.name")).getStackForm(1);
        ItemList.SpaceElevatorModuleAssemblerT3.set(stack);

        stack = new TileEntityModuleMiner.TileEntityModuleMinerT1(
            SpaceElevatorModuleMinerT1.ID,
            "ProjectModuleMinerT1",
            GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.miner.t1.name")).getStackForm(1);
        ItemList.SpaceElevatorModuleMinerT1.set(stack);

        stack = new TileEntityModuleMiner.TileEntityModuleMinerT2(
            SpaceElevatorModuleMinerT2.ID,
            "ProjectModuleMinerT2",
            GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.miner.t2.name")).getStackForm(1);
        ItemList.SpaceElevatorModuleMinerT2.set(stack);

        stack = new TileEntityModuleMiner.TileEntityModuleMinerT3(
            SpaceElevatorModuleMinerT3.ID,
            "ProjectModuleMinerT3",
            GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.miner.t3.name")).getStackForm(1);
        ItemList.SpaceElevatorModuleMinerT3.set(stack);

        stack = new TileEntityModulePump.TileEntityModulePumpT1(
            SpaceElevatorModulePumpT1.ID,
            "ProjectModulePumpT1",
            GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.pump.t1.name")).getStackForm(1);
        ItemList.SpaceElevatorModulePumpT1.set(stack);

        stack = new TileEntityModulePump.TileEntityModulePumpT2(
            SpaceElevatorModulePumpT2.ID,
            "ProjectModulePumpT2",
            GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.pump.t2.name")).getStackForm(1);
        ItemList.SpaceElevatorModulePumpT2.set(stack);

        stack = new TileEntityModulePump.TileEntityModulePumpT3(
            SpaceElevatorModulePumpT3.ID,
            "ProjectModulePumpT3",
            GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.pump.t3.name")).getStackForm(1);
        ItemList.SpaceElevatorModulePumpT3.set(stack);

        stack = new TileEntityModuleManager(
            SpaceElevatorModuleManager.ID,
            "ProjectModuleManager",
            GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.manager.t1.name")).getStackForm(1);
        ItemList.SpaceElevatorModuleManager.set(stack);

        stack = new TileEntityModuleResearch(
            SpaceElevatorModuleResearch.ID,
            "ProjectModuleResearch",
            GCCoreUtil.translate("gt.blockmachines.multimachine.project.ig.research.t1.name")).getStackForm(1);
        ItemList.SpaceElevatorModuleResearch.set(stack);
    }
}
