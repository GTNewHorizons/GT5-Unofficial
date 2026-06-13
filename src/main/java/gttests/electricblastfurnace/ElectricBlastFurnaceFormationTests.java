package gttests.electricblastfurnace;

import static com.gtnewhorizons.horizonqa.api.TestPos.at;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizons.horizonqa.api.GameTestHelper;
import com.gtnewhorizons.horizonqa.api.TestPos;
import com.gtnewhorizons.horizonqa.api.annotation.GameTest;
import com.gtnewhorizons.horizonqa.api.annotation.GameTestHolder;
import com.gtnewhorizons.horizonqa.api.gt.Multiblock;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.util.GTRecipeBuilder;

@GameTestHolder(value = "gregtech", templatePrefix = "multiblock/electric_blast_furnace")
public class ElectricBlastFurnaceFormationTests {

    private static final TestPos CONTROLLER = at(1, 0, 0);
    private static final TestPos ONE_COIL = at(0, 1, 0);
    private static final TestPos ENERGY_HATCH = at(0, 0, 0);
    private static final TestPos INPUT_HATCH = at(2, 0, 0);
    private static final TestPos INPUT_BUS = at(1, 0, 1);
    private static final TestPos OUTPUT_BUS = at(1, 0, 2);
    private static final TestPos MAINTENANCE_HATCH = at(0, 0, 1);
    private static final TestPos MUFFLER_HATCH = at(1, 3, 1);

    private static final int HEAT_PROOF_CASING_META = 11;
    private static final int LOW_HEAT = 1_200;
    private static final int EV_CUPRONICKEL_HEAT = 2_001;
    private static final int MV_CUPRONICKEL_HEAT = 1_801;

    @GameTest(template = "valid", batch = "gt5.ebf")
    public static void validStructureForms(GameTestHelper helper) {
        Multiblock ebf = ebf(helper);
        ebf.assertFormed();
        helper.succeed();
    }

    @GameTest(template = "valid", timeoutTicks = 60, batch = "gt5.ebf")
    public static void mixedCoilsNeverForm(GameTestHelper helper) {
        helper.setBlock(ONE_COIL.x(), ONE_COIL.y(), ONE_COIL.z(), GregTechAPI.sBlockCasings5, 1);
        assertNeverForms(helper, "EBF formed with mixed coil tiers");
    }

    @GameTest(template = "valid", timeoutTicks = 60, batch = "gt5.ebf")
    public static void missingEnergyHatchNeverForms(GameTestHelper helper) {
        replaceWithHeatProofCasing(helper, ENERGY_HATCH);
        assertNeverForms(helper, "EBF formed without an energy hatch");
    }

    @GameTest(template = "valid", timeoutTicks = 60, batch = "gt5.ebf")
    public static void missingMaintenanceHatchNeverForms(GameTestHelper helper) {
        replaceWithHeatProofCasing(helper, MAINTENANCE_HATCH);
        assertNeverForms(helper, "EBF formed without a maintenance hatch");
    }

    @GameTest(template = "valid", timeoutTicks = 60, batch = "gt5.ebf")
    public static void missingInputsNeverForms(GameTestHelper helper) {
        replaceWithHeatProofCasing(helper, INPUT_HATCH);
        replaceWithHeatProofCasing(helper, INPUT_BUS);
        assertNeverForms(helper, "EBF formed without an input bus or input hatch");
    }

    @GameTest(template = "valid", timeoutTicks = 60, batch = "gt5.ebf")
    public static void missingOutputsNeverForms(GameTestHelper helper) {
        replaceWithHeatProofCasing(helper, OUTPUT_BUS);
        assertNeverForms(helper, "EBF formed without an output bus or output hatch");
    }

    @GameTest(template = "valid", timeoutTicks = 60, batch = "gt5.ebf")
    public static void missingMufflerHatchNeverForms(GameTestHelper helper) {
        replaceWithHeatProofCasing(helper, MUFFLER_HATCH);
        assertNeverForms(helper, "EBF formed without the top muffler position");
    }

    @GameTest(template = "missing_coils", timeoutTicks = 60, batch = "gt5.ebf")
    public static void missingCoilsNeverForms(GameTestHelper helper) {
        assertNeverForms(helper, "EBF formed without coils");
    }

    @GameTest(template = "valid", batch = "gt5.ebf")
    public static void formedMachineDeformsWhenCoilBreaks(GameTestHelper helper) {
        Multiblock ebf = formedEbf(helper);

        helper.destroyBlock(ONE_COIL.x(), ONE_COIL.y(), ONE_COIL.z());

        ebf.assertNotFormed("EBF stayed formed after a coil was destroyed");
        helper.succeed();
    }

    @GameTest(template = "valid", timeoutTicks = 200, batch = "gt5.ebf")
    public static void syntheticRecipeRunsAtValidHeat(GameTestHelper helper) {
        Multiblock ebf = formedEbf(helper);
        ItemStack input = stack(Blocks.dirt);
        ItemStack output = stack(Blocks.cobblestone);

        addItemRecipe(helper, ebf, input, output, LOW_HEAT, 20, TierEU.RECIPE_MV);
        ebf.inputBus(0)
            .insert(input);
        ebf.energyHatch(0)
            .supply(TierEU.EV, 1, 80);

        ebf.runRecipe(120);

        ebf.inputBus(0)
            .assertEmpty();
        ebf.outputBus(0)
            .assertContains(output);
        ebf.assertNoExplosion();
        helper.succeed();
    }

    @GameTest(template = "valid", timeoutTicks = 200, batch = "gt5.ebf")
    public static void recipeAtExactHeatLimitRuns(GameTestHelper helper) {
        Multiblock ebf = formedEbf(helper);
        ItemStack input = stack(Blocks.brick_block);
        ItemStack output = stack(Blocks.hardened_clay);

        addItemRecipe(helper, ebf, input, output, EV_CUPRONICKEL_HEAT, 20, TierEU.RECIPE_MV);
        ebf.inputBus(0)
            .insert(input);
        ebf.energyHatch(0)
            .supply(TierEU.EV, 1, 100);

        ebf.runRecipe(120);

        ebf.inputBus(0)
            .assertEmpty();
        ebf.outputBus(0)
            .assertContains(output);
        ebf.assertNoExplosion();
        helper.succeed();
    }

    @GameTest(template = "valid", timeoutTicks = 160, batch = "gt5.ebf")
    public static void recipeAboveHeatNeverStarts(GameTestHelper helper) {
        Multiblock ebf = formedEbf(helper);
        ItemStack input = stack(Blocks.sand);
        ItemStack output = stack(Blocks.glass);

        addItemRecipe(helper, ebf, input, output, EV_CUPRONICKEL_HEAT + 100, 20, TierEU.RECIPE_MV);
        ebf.inputBus(0)
            .insert(input);
        ebf.energyHatch(0)
            .supply(TierEU.EV, 1, 100);

        helper.gtnh()
            .fastForwardTicks(100);

        ebf.inputBus(0)
            .assertContains(input);
        ebf.outputs()
            .assertNotContains(output);
        ebf.assertNoExplosion();
        helper.succeed();
    }

    @GameTest(template = "valid", timeoutTicks = 160, batch = "gt5.ebf")
    public static void recipeOneHeatAboveLimitDoesNotConsumeInput(GameTestHelper helper) {
        Multiblock ebf = formedEbf(helper);
        ItemStack input = stack(Blocks.sandstone);
        ItemStack output = stack(Blocks.stonebrick);

        addItemRecipe(helper, ebf, input, output, EV_CUPRONICKEL_HEAT + 1, 20, TierEU.RECIPE_MV);
        ebf.inputBus(0)
            .insert(input);
        ebf.energyHatch(0)
            .supply(TierEU.EV, 1, 100);

        helper.gtnh()
            .fastForwardTicks(100);

        ebf.inputBus(0)
            .assertContains(input);
        ebf.outputs()
            .assertNotContains(output);
        ebf.assertNoExplosion();
        helper.succeed();
    }

    @GameTest(template = "valid", timeoutTicks = 200, batch = "gt5.ebf")
    public static void evEnergyHatchAllowsVoltageHeatBonus(GameTestHelper helper) {
        Multiblock ebf = formedEbf(helper);
        ItemStack input = stack(Blocks.gravel);
        ItemStack output = stack(Blocks.stone);

        addItemRecipe(helper, ebf, input, output, MV_CUPRONICKEL_HEAT + 1, 20, TierEU.RECIPE_MV);
        ebf.inputBus(0)
            .insert(input);
        ebf.energyHatch(0)
            .supply(TierEU.EV, 1, 100);

        ebf.runRecipe(120);

        ebf.inputBus(0)
            .assertEmpty();
        ebf.outputBus(0)
            .assertContains(output);
        ebf.assertNoExplosion();
        helper.succeed();
    }

    @GameTest(template = "valid", timeoutTicks = 200, batch = "gt5.ebf")
    public static void formedWithOnlyInputBusRunsItemRecipe(GameTestHelper helper) {
        replaceWithHeatProofCasing(helper, INPUT_HATCH);

        Multiblock ebf = formedEbfAfterMutation(helper);
        ItemStack input = stack(Blocks.coal_block);
        ItemStack output = stack(Blocks.iron_block);

        addItemRecipe(helper, ebf, input, output, LOW_HEAT, 20, TierEU.RECIPE_MV);
        ebf.inputBus(0)
            .insert(input);
        ebf.energyHatch(0)
            .supply(TierEU.EV, 1, 100);

        ebf.runRecipe(120);

        ebf.inputBus(0)
            .assertEmpty();
        ebf.outputBus(0)
            .assertContains(output);
        ebf.assertNoExplosion();
        helper.succeed();
    }

    @GameTest(template = "valid", batch = "gt5.ebf")
    public static void formedWithOnlyInputHatchStillForms(GameTestHelper helper) {
        replaceWithHeatProofCasing(helper, INPUT_BUS);

        Multiblock ebf = formedEbfAfterMutation(helper);
        ebf.assertNoExplosion();
        helper.succeed();
    }

    @GameTest(template = "valid", timeoutTicks = 200, batch = "gt5.ebf")
    public static void formedWithOnlyOutputBusRunsItemRecipe(GameTestHelper helper) {
        Multiblock ebf = formedEbf(helper);
        ItemStack input = stack(Blocks.redstone_block);
        ItemStack output = stack(Blocks.quartz_block);

        addItemRecipe(helper, ebf, input, output, LOW_HEAT, 20, TierEU.RECIPE_MV);
        ebf.inputBus(0)
            .insert(input);
        ebf.energyHatch(0)
            .supply(TierEU.EV, 1, 100);

        ebf.runRecipe(120);

        ebf.inputBus(0)
            .assertEmpty();
        ebf.outputBus(0)
            .assertContains(output);
        ebf.assertNoExplosion();
        helper.succeed();
    }

    @GameTest(template = "valid", timeoutTicks = 200, batch = "gt5.ebf")
    public static void itemAndFluidRecipeConsumesExactFluid(GameTestHelper helper) {
        Multiblock ebf = formedEbf(helper);
        ItemStack itemInput = stack(Blocks.lapis_block);
        FluidStack fluidInput = fluid("water", 250);
        ItemStack output = stack(Blocks.gold_block);

        addItemAndFluidToItemRecipe(helper, ebf, itemInput, fluidInput, output, LOW_HEAT, 20, TierEU.RECIPE_MV);
        ebf.inputBus(0)
            .insert(itemInput);
        ebf.inputHatch(0)
            .fill(fluidInput.copy());
        ebf.energyHatch(0)
            .supply(TierEU.EV, 1, 100);

        ebf.runRecipe(120);

        ebf.inputBus(0)
            .assertEmpty();
        ebf.inputHatch(0)
            .assertEmpty();
        ebf.outputBus(0)
            .assertContains(output);
        ebf.assertNoExplosion();
        helper.succeed();
    }

    @GameTest(template = "valid", timeoutTicks = 160, batch = "gt5.ebf")
    public static void insufficientFluidDoesNotConsumeItems(GameTestHelper helper) {
        Multiblock ebf = formedEbf(helper);
        ItemStack itemInput = stack(Blocks.diamond_block);
        FluidStack fluidInput = fluid("water", 250);
        FluidStack partialFluidInput = fluid("water", 100);
        ItemStack output = stack(Blocks.emerald_block);

        addItemAndFluidToItemRecipe(helper, ebf, itemInput, fluidInput, output, LOW_HEAT, 20, TierEU.RECIPE_MV);
        ebf.inputBus(0)
            .insert(itemInput);
        ebf.inputHatch(0)
            .fill(partialFluidInput.copy());
        ebf.energyHatch(0)
            .supply(TierEU.EV, 1, 100);

        helper.gtnh()
            .fastForwardTicks(100);

        ebf.inputBus(0)
            .assertContains(itemInput);
        ebf.inputHatch(0)
            .assertContains(partialFluidInput);
        ebf.outputs()
            .assertNotContains(output);
        ebf.assertNoExplosion();
        helper.succeed();
    }

    @GameTest(template = "valid", timeoutTicks = 160, batch = "gt5.ebf")
    public static void insufficientEuNeverProducesOutput(GameTestHelper helper) {
        Multiblock ebf = formedEbf(helper);
        ItemStack input = stack(Blocks.netherrack);
        ItemStack output = stack(Blocks.nether_brick);

        addItemRecipe(helper, ebf, input, output, LOW_HEAT, 20, TierEU.RECIPE_MV);
        ebf.inputBus(0)
            .insert(input);

        helper.gtnh()
            .fastForwardTicks(100);

        ebf.outputs()
            .assertNotContains(output);
        ebf.assertNoExplosion();
        helper.succeed();
    }

    @GameTest(template = "valid", timeoutTicks = 160, batch = "gt5.ebf")
    public static void fullOutputBusDoesNotConsumeInputs(GameTestHelper helper) {
        Multiblock ebf = formedEbf(helper);
        ItemStack input = stack(Blocks.soul_sand);
        ItemStack output = stack(Blocks.obsidian);

        addItemRecipe(helper, ebf, input, output, LOW_HEAT, 20, TierEU.RECIPE_MV);
        ebf.inputBus(0)
            .insert(input);
        fillOutputBus(ebf);
        ebf.energyHatch(0)
            .supply(TierEU.EV, 1, 100);

        helper.gtnh()
            .fastForwardTicks(100);

        ebf.inputBus(0)
            .assertContains(input);
        ebf.outputs()
            .assertNotContains(output);
        ebf.assertNoExplosion();
        helper.succeed();
    }

    @GameTest(template = "valid", timeoutTicks = 200, batch = "gt5.ebf")
    public static void outputBusWithOneFreeSlotAcceptsRecipe(GameTestHelper helper) {
        Multiblock ebf = formedEbf(helper);
        ItemStack input = stack(Blocks.mycelium);
        ItemStack output = stack(Blocks.packed_ice);

        addItemRecipe(helper, ebf, input, output, LOW_HEAT, 20, TierEU.RECIPE_MV);
        ebf.inputBus(0)
            .insert(input);
        fillOutputBusExceptOneSlot(ebf);
        ebf.energyHatch(0)
            .supply(TierEU.EV, 1, 100);

        ebf.runRecipe(120);

        ebf.inputBus(0)
            .assertEmpty();
        ebf.outputBus(0)
            .assertContains(output);
        ebf.assertNoExplosion();
        helper.succeed();
    }

    @GameTest(template = "valid", timeoutTicks = 240, batch = "gt5.ebf")
    public static void syntheticRecipeRunsTwice(GameTestHelper helper) {
        Multiblock ebf = formedEbf(helper);
        ItemStack input = stack(Blocks.stained_hardened_clay);
        ItemStack output = stack(Blocks.stained_glass);

        addItemRecipe(helper, ebf, input, output, LOW_HEAT, 20, TierEU.RECIPE_MV);
        ebf.inputBus(0)
            .insert(input.copy(), input.copy());
        ebf.energyHatch(0)
            .supply(TierEU.EV, 1, 100);

        ebf.runRecipe(120);

        ebf.inputBus(0)
            .assertEmpty();
        assertBusContainsAmount(helper, OUTPUT_BUS, output, 2, "EBF did not process both queued synthetic inputs");
        ebf.assertNoExplosion();
        helper.succeed();
    }

    @GameTest(template = "valid", timeoutTicks = 240, batch = "gt5.ebf")
    public static void unmaintainedMachineDoesNotRunUntilFixed(GameTestHelper helper) {
        Multiblock ebf = formedEbfWithoutMaintenance(helper);
        ItemStack input = stack(Blocks.ice);
        ItemStack output = stack(Blocks.clay);

        addItemRecipe(helper, ebf, input, output, LOW_HEAT, 20, TierEU.RECIPE_MV);
        ebf.inputBus(0)
            .insert(input);
        ebf.energyHatch(0)
            .supply(TierEU.EV, 1, 100);

        helper.gtnh()
            .fastForwardTicks(100);

        ebf.inputBus(0)
            .assertContains(input);
        ebf.outputs()
            .assertNotContains(output);

        ebf.fixMaintenance();
        ebf.energyHatch(0)
            .supply(TierEU.EV, 1, 100);
        ebf.runRecipe(120);

        ebf.inputBus(0)
            .assertEmpty();
        ebf.outputBus(0)
            .assertContains(output);
        ebf.assertNoExplosion();
        helper.succeed();
    }

    @GameTest(template = "valid", timeoutTicks = 160, batch = "gt5.ebf")
    public static void brokenStructureDoesNotRunRecipeAfterRescan(GameTestHelper helper) {
        Multiblock ebf = formedEbf(helper);
        ItemStack input = stack(Blocks.cactus);
        ItemStack output = stack(Blocks.log);

        addItemRecipe(helper, ebf, input, output, LOW_HEAT, 20, TierEU.RECIPE_MV);
        ebf.inputBus(0)
            .insert(input);
        helper.destroyBlock(ONE_COIL.x(), ONE_COIL.y(), ONE_COIL.z());
        ebf.assertNotFormed("EBF stayed formed after a coil was destroyed");
        helper.gtnh()
            .supplyEU(ENERGY_HATCH, TierEU.EV, 1, 100);

        helper.gtnh()
            .fastForwardTicks(100);

        assertBusContainsAmount(helper, INPUT_BUS, input, 1, "EBF consumed input after the structure was broken");
        ebf.outputs()
            .assertNotContains(output);
        ebf.assertNoExplosion();
        helper.succeed();
    }

    @GameTest(template = "valid", timeoutTicks = 60, batch = "gt5.ebf")
    public static void wrongCoilBlockNeverForms(GameTestHelper helper) {
        replaceWithHeatProofCasing(helper, ONE_COIL);
        assertNeverForms(helper, "EBF formed with a heat-proof casing in a coil position");
    }

    @GameTest(template = "valid", timeoutTicks = 900, batch = "gt5.ebf", required = false)
    public static void runningRecipeEmitsPollution(GameTestHelper helper) {
        Multiblock ebf = formedEbf(helper);
        ItemStack input = stack(Blocks.end_stone);
        ItemStack output = stack(Blocks.quartz_block);

        addItemRecipe(helper, ebf, input, output, LOW_HEAT, 600, TierEU.RECIPE_EV);
        ebf.inputBus(0)
            .insert(input);
        ebf.energyHatch(0)
            .supply(TierEU.EV, 1, 800);

        ebf.runRecipe(800);

        helper.gtnh()
            .assertPollutionEmitted(1);
        ebf.assertNoExplosion();
        helper.succeed();
    }

    private static Multiblock formedEbf(GameTestHelper helper) {
        Multiblock ebf = ebf(helper);
        ebf.assertFormed();
        return ebf;
    }

    private static Multiblock formedEbfAfterMutation(GameTestHelper helper) {
        Multiblock ebf = ebf(helper);
        helper.assertTrue(ebf.forceStructureCheck(), "EBF did not form after structure mutation");
        return ebf;
    }

    private static Multiblock formedEbfWithoutMaintenance(GameTestHelper helper) {
        Multiblock ebf = helper.gtnh()
            .multiblock(CONTROLLER);
        ebf.assertFormed();
        breakMaintenanceIssues(helper);
        return ebf;
    }

    private static Multiblock ebf(GameTestHelper helper) {
        Multiblock ebf = helper.gtnh()
            .multiblock(CONTROLLER);
        ebf.fixMaintenance();
        return ebf;
    }

    private static void assertNeverForms(GameTestHelper helper, String message) {
        Multiblock ebf = ebf(helper);
        ebf.assertNeverForms(message);
    }

    private static void replaceWithHeatProofCasing(GameTestHelper helper, TestPos pos) {
        helper.setBlock(pos.x(), pos.y(), pos.z(), GregTechAPI.sBlockCasings1, HEAT_PROOF_CASING_META);
    }

    private static void addItemRecipe(GameTestHelper helper, Multiblock ebf, ItemStack input, ItemStack output,
        int heat, int duration, long eut) {
        helper.gtnh()
            .withTestRecipe(
                ebf,
                recipe(heat, duration, eut).itemInputs(input.copy())
                    .itemOutputs(output.copy()));
    }

    private static void addItemAndFluidToItemRecipe(GameTestHelper helper, Multiblock ebf, ItemStack itemInput,
        FluidStack fluidInput, ItemStack output, int heat, int duration, long eut) {
        helper.gtnh()
            .withTestRecipe(
                ebf,
                recipe(heat, duration, eut).itemInputs(itemInput.copy())
                    .fluidInputs(fluidInput.copy())
                    .itemOutputs(output.copy()));
    }

    private static GTRecipeBuilder recipe(int heat, int duration, long eut) {
        return GTValues.RA.stdBuilder()
            .duration(duration)
            .eut(eut)
            .metadata(COIL_HEAT, heat);
    }

    private static void assertBusContainsAmount(GameTestHelper helper, TestPos pos, ItemStack expected, int amount,
        String message) {
        IMetaTileEntity bus = helper.gtnh()
            .metaTileEntity(pos);
        int found = 0;
        for (int slot = 0; slot < bus.getSizeInventory(); slot++) {
            ItemStack inSlot = bus.getStackInSlot(slot);
            if (matches(inSlot, expected)) {
                found += inSlot.stackSize;
            }
        }
        helper.assertTrue(found >= amount, message + " (found " + found + ")");
    }

    private static void fillOutputBus(Multiblock ebf) {
        ebf.outputBus(0)
            .fillAllSlots(fullStack(Blocks.stone));
    }

    private static void fillOutputBusExceptOneSlot(Multiblock ebf) {
        ebf.outputBus(0)
            .fillAllSlots(fullStack(Blocks.stone))
            .setSlot(0, null);
    }

    private static void breakMaintenanceIssues(GameTestHelper helper) {
        MTEMultiBlockBase multiBlock = helper.gtnh()
            .multiBlockController(CONTROLLER);

        multiBlock.mWrench = false;
        multiBlock.mScrewdriver = false;
        multiBlock.mSoftMallet = false;
        multiBlock.mHardHammer = false;
        multiBlock.mSolderingTool = false;
        multiBlock.mCrowbar = false;
    }

    private static FluidStack fluid(String fluidName, int amount) {
        FluidStack fluid = FluidRegistry.getFluidStack(fluidName, amount);
        if (fluid == null) {
            throw new IllegalArgumentException("Unknown fluid: " + fluidName);
        }
        return fluid;
    }

    private static ItemStack stack(Block block) {
        return new ItemStack(block, 1);
    }

    private static ItemStack fullStack(Block block) {
        return new ItemStack(block, 64);
    }

    private static boolean matches(ItemStack actual, ItemStack expected) {
        return actual != null && actual.isItemEqual(expected) && ItemStack.areItemStackTagsEqual(actual, expected);
    }
}
