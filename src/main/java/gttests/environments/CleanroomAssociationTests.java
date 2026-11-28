package gttests.environments;

import static com.gtnewhorizons.horizonqa.api.TestPos.at;
import static gregtech.api.recipe.RecipeEnvironment.CLEANROOM;

import java.util.Collection;
import java.util.UUID;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.gtnhlib.capability.Capabilities;
import com.gtnewhorizons.horizonqa.api.GameTestHelper;
import com.gtnewhorizons.horizonqa.api.TestPos;
import com.gtnewhorizons.horizonqa.api.annotation.GameTest;
import com.gtnewhorizons.horizonqa.api.annotation.GameTestHolder;
import com.gtnewhorizons.horizonqa.api.gt.Multiblock;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Mods;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.ICleanroomReceiver;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.tileentities.machines.multi.MTECleanroom;

@GameTestHolder(value = Mods.ModIDs.GREG_TECH, templatePrefix = "multiblock/cleanroom")
public class CleanroomAssociationTests {

    @GameTest(template = "nested_space", timeoutTicks = 200, batch = "gt5.cleanroom")
    public static void nestedRoomsKeepTheCleanestAvailableAssociation(GameTestHelper helper) {
        BaseMetaTileEntity outerBase = placeMachine(helper, at(6, 10, 6), ItemList.Machine_Multi_Cleanroom);
        MTECleanroom outer = (MTECleanroom) outerBase.getMetaTileEntity();
        outer.construct(new ItemStack(Items.stick, 4), false);
        placeMachine(helper, at(2, 3, 6), ItemList.Hatch_Energy_LV);
        placeMachine(helper, at(2, 4, 6), ItemList.Hatch_Maintenance);

        BaseMetaTileEntity innerBase = placeMachine(helper, at(6, 8, 6), ItemList.Machine_Multi_Cleanroom);
        MTECleanroom inner = (MTECleanroom) innerBase.getMetaTileEntity();
        inner.construct(new ItemStack(Items.stick, 2), false);
        placeMachine(helper, at(4, 5, 6), ItemList.Hatch_Energy_LV);
        placeMachine(helper, at(4, 6, 6), ItemList.Hatch_Maintenance);
        BaseMetaTileEntity machine = placeMachine(helper, at(6, 5, 6), ItemList.Machine_LV_Macerator);
        MTEBasicMachine machineLogic = (MTEBasicMachine) machine.getMetaTileEntity();

        helper.gtnh()
            .multiblock(at(6, 10, 6))
            .assertFormed();
        helper.gtnh()
            .multiblock(at(6, 8, 6))
            .assertFormed();
        ICleanroomReceiver receiver = Capabilities.getCapability(machine, ICleanroomReceiver.class);
        helper.assertTrue(receiver != null, "Native machine did not expose its cleanroom association");

        outer.mEfficiency = 10000;
        inner.mEfficiency = 9000;
        helper.assertTrue(receiver.getCleanroom() == outer, "The last structure check replaced the cleaner outer room");
        inner.mEfficiency = 10000;
        outer.mEfficiency = 9000;
        helper.assertTrue(receiver.getCleanroom() == inner, "Changing cleanness did not select the cleaner inner room");

        inner.stopMachine(ShutDownReasonRegistry.POWER_LOSS);
        helper.assertTrue(receiver.getCleanroom() == outer, "Inner power loss discarded the healthy outer room");
        helper.assertTrue(CLEANROOM.isSatisfied(machineLogic), "Outer room did not satisfy admission");

        inner.mEfficiency = 10000;
        helper.setBlock(at(2, 5, 6), Blocks.air);
        helper.assertTrue(!outer.checkStructure(true, outerBase), "Broken outer room remained formed");
        helper.assertTrue(receiver.getCleanroom() == inner, "Outer structure failure cleared the inner association");

        helper.setBlock(at(2, 5, 6), GregTechAPI.sBlockReinforced, 2);
        helper.assertTrue(outer.checkStructure(true, outerBase), "Repaired outer room did not form");
        outer.mEfficiency = 10000;
        inner.mEfficiency = 9000;
        helper.assertTrue(receiver.getCleanroom() == outer, "Repaired cleaner room was not reattached");

        helper.setBlock(at(6, 10, 6), Blocks.air);
        helper.assertTrue(receiver.getCleanroom() == inner, "Removing the outer controller discarded the inner room");
        helper.setBlock(at(6, 8, 6), Blocks.air);
        helper.assertTrue(!CLEANROOM.isSatisfied(machineLogic), "Removed controllers still satisfied admission");
        helper.succeed();
    }

    @GameTest(template = "nested_space", timeoutTicks = 1000, batch = "gt5.cleanroom")
    public static void cleanroomRecoveryWakesWaitingMachineBeforeItsPeriodicRetry(GameTestHelper helper) {
        BaseMetaTileEntity roomBase = placeMachine(helper, at(6, 10, 6), ItemList.Machine_Multi_Cleanroom);
        MTECleanroom room = (MTECleanroom) roomBase.getMetaTileEntity();
        room.construct(new ItemStack(Items.stick, 4), false);
        placeMachine(helper, at(2, 3, 6), ItemList.Hatch_Energy_LV);
        placeMachine(helper, at(2, 4, 6), ItemList.Hatch_Maintenance);
        BaseMetaTileEntity machineBase = placeMachine(helper, at(6, 5, 6), ItemList.Machine_LV_Macerator);
        MTEBasicMachine machine = (MTEBasicMachine) machineBase.getMetaTileEntity();
        Multiblock cleanroom = helper.gtnh()
            .multiblock(at(6, 10, 6));
        cleanroom.assertFormed();
        cleanroom.fixMaintenance();
        cleanroom.energyHatch(0)
            .supply(TierEU.LV, 2, 650);
        machineBase.increaseStoredEnergyUnits(machineBase.getEUCapacity(), true);

        Collection<GTRecipe> recipes = GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.command_block))
            .itemOutputs(new ItemStack(Items.golden_apple))
            .duration(2000)
            .eut(TierEU.RECIPE_LV)
            .requiresEnvironment(CLEANROOM)
            .addTo(RecipeMaps.maceratorRecipes);
        helper.assertTrue(recipes.size() == 1, "Cleanroom recovery recipe was not registered");
        helper.afterTest(
            () -> RecipeMaps.maceratorRecipes.getBackend()
                .removeRecipes(recipes));
        room.stopMachine(ShutDownReasonRegistry.POWER_LOSS);
        machine.setInventorySlotContents(machine.getInputSlot(), new ItemStack(Blocks.command_block));
        machineBase.enableWorking();
        helper.gtnh()
            .fastForwardTicks(120);
        helper.assertTrue(machine.maxProgresstime() == 0, "Recipe started in an unclean room");
        helper.assertTrue(machine.getStackInSlot(machine.getInputSlot()) != null, "Blocked recipe consumed its input");

        room.enableWorking();
        helper.gtnh()
            .fastForwardTicks(1);
        int recoveryTicks = room.maxProgresstime() + 2;
        helper.assertTrue(room.maxProgresstime() > 0, "Cleanroom did not restart");
        helper.assertTrue(
            machineBase.getTimer() % 600 + recoveryTicks < 600,
            "Recovery window overlaps the basic machine's periodic retry");
        helper.gtnh()
            .fastForwardTicks(recoveryTicks);
        helper.assertTrue(room.getCleanness() > 0, "Native controller cycle did not restore cleanness");
        helper.assertTrue(machine.maxProgresstime() > 0, "Cleanroom recovery did not wake the waiting machine");
        helper.assertTrue(machine.getStackInSlot(machine.getInputSlot()) == null, "Waiting recipe did not start");
        helper.succeed();
    }

    private static BaseMetaTileEntity placeMachine(GameTestHelper helper, TestPos pos, ItemList machine) {
        helper.setBlock(pos, GregTechAPI.sBlockMachines, 0);
        BaseMetaTileEntity base = helper.assertTileEntityPresent(BaseMetaTileEntity.class, pos);
        base.setInitialValuesAsNBT(
            null,
            (short) machine.get(1)
                .getItemDamage());
        base.setFrontFacing(ForgeDirection.NORTH);
        base.setOwnerName("CleanroomAssociationTest");
        base.setOwnerUuid(UUID.fromString("561c5654-f935-4ebd-88a6-003cbbbe39de"));
        return base;
    }
}
