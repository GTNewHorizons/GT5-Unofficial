package gttests.electricblastfurnace;

import static com.gtnewhorizons.horizonqa.api.TestPos.at;
import static gregtech.api.recipe.RecipeEnvironment.CLEANROOM;
import static gregtech.api.recipe.RecipeEnvironment.LOW_GRAVITY;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;

import java.util.Collection;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizons.horizonqa.api.GameTestHelper;
import com.gtnewhorizons.horizonqa.api.TestPos;
import com.gtnewhorizons.horizonqa.api.annotation.GameTest;
import com.gtnewhorizons.horizonqa.api.annotation.GameTestHolder;
import com.gtnewhorizons.horizonqa.api.gt.Multiblock;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Mods;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.recipe.RecipeEnvironment;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SingleRecipeCheck;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;

@GameTestHolder(value = Mods.ModIDs.GREG_TECH, templatePrefix = "multiblock/electric_blast_furnace")
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

    private static final TestEnvironment ADMISSION_ENVIRONMENT = RecipeEnvironment
        .register(new TestEnvironment("environment_admission"));
    private static final TestEnvironment RUNNING_ENVIRONMENT = RecipeEnvironment
        .register(new TestEnvironment("environment_running"));

    @GameTest(template = "valid", timeoutTicks = 4000, batch = "gt5.environments")
    public static void legacyEnvironmentCacheResetsAndRebuilds(GameTestHelper helper) {
        Multiblock ebf = formedEbf(helper);
        MTEMultiBlockBase controller = helper.gtnh()
            .multiBlockController(CONTROLLER);
        BaseMetaTileEntity base = (BaseMetaTileEntity) controller.getBaseMetaTileEntity();
        int[] oldValues = { -100, -200, -300 };
        RecipeEnvironment[][] environments = { { LOW_GRAVITY }, { CLEANROOM }, { LOW_GRAVITY, CLEANROOM } };
        ItemStack oldOutput = stack(Blocks.cobblestone);
        ItemStack newOutput = stack(Blocks.glass);

        for (int index = 0; index < oldValues.length; index++) {
            ItemStack input = new ItemStack(Blocks.command_block, 1, 80 + index);
            FluidStack water = fluid("water", 144);
            Collection<GTRecipe> oldRecipes = GTValues.RA.stdBuilder()
                .itemInputs(input.copy())
                .fluidInputs(water.copy())
                .itemOutputs(oldOutput.copy())
                .duration(2000)
                .eut(8)
                .requiresEnvironment(environments[index])
                .addTo(RecipeMaps.blastFurnaceRecipes);
            helper.assertTrue(oldRecipes.size() == 1, "Initial cache-test recipe was not registered");
            helper.afterTest(
                () -> RecipeMaps.blastFurnaceRecipes.getBackend()
                    .removeRecipes(oldRecipes));
            controller.setRecipeLocking(false);
            controller.setRecipeLocking(true);
            controller.mEfficiency = 10000;
            ebf.inputBus(0)
                .insert(input.copy());
            ebf.inputHatch(0)
                .fill(water.copy());
            ebf.energyHatch(0)
                .supply(TierEU.EV, 1, 3500);
            controller.enableWorking();
            for (int tick = 0; tick < 100 && !ebf.isProcessing(); tick++) helper.gtnh()
                .fastForwardTicks(1);
            helper.assertTrue(ebf.isProcessing(), "Initial cache-test recipe did not start");
            helper.gtnh()
                .fastForwardTicks(3);
            helper
                .assertTrue(ebf.isProcessing() && ebf.progress() > 0, "Initial process did not retain pending output");
            helper.assertTrue(controller.getSingleRecipeCheck() != null, "Native processing did not build its cache");
            ebf.inputBus(0)
                .assertEmpty();
            ebf.inputHatch(0)
                .assertEmpty();

            NBTTagCompound saved = new NBTTagCompound();
            base.writeToNBT(saved);
            int progress = saved.getInteger("mProgresstime");
            int duration = saved.getInteger("mMaxProgresstime");
            NBTTagCompound legacy = saved.getCompoundTag("mSingleRecipeCheck");
            if (index == 2) legacy = new NBTTagCompound();
            legacy.setInteger("specialValue", oldValues[index]);
            if (index == 0) legacy.setInteger("duration", legacy.getInteger("duration") + 1);
            saved.setTag("mSingleRecipeCheck", legacy);
            base.readFromNBT(saved);
            controller = (MTEMultiBlockBase) base.getMetaTileEntity();
            helper.assertTrue(base.isAllowedToWork(), "Legacy cache reset disabled the controller");
            helper.assertTrue(controller.isRecipeLockingEnabled(), "Legacy cache reset disabled recipe caching");
            helper.assertTrue(controller.getSingleRecipeCheck() == null, "Legacy cache was not discarded");
            helper.assertTrue(
                controller.mProgresstime == progress && controller.mMaxProgresstime == duration,
                "Legacy cache reset changed the running process");
            NBTTagCompound uncached = new NBTTagCompound();
            base.writeToNBT(uncached);
            helper.assertTrue(!uncached.hasKey("mSingleRecipeCheck"), "The discarded cache was saved again");
            RecipeMaps.blastFurnaceRecipes.getBackend()
                .removeRecipes(oldRecipes);
            ItemStack replacementInput = input.copy();
            replacementInput.stackSize = 2;
            FluidStack replacementWater = fluid("water", 288);
            Collection<GTRecipe> newRecipes = GTValues.RA.stdBuilder()
                .itemInputs(replacementInput.copy())
                .fluidInputs(replacementWater.copy())
                .itemOutputs(newOutput.copy())
                .duration(40)
                .eut(8)
                .requiresEnvironment(environments[index])
                .addTo(RecipeMaps.blastFurnaceRecipes);
            helper.assertTrue(newRecipes.size() == 1, "Replacement cache-test recipe was not registered");
            helper.afterTest(
                () -> RecipeMaps.blastFurnaceRecipes.getBackend()
                    .removeRecipes(newRecipes));
            GTRecipe replacement = newRecipes.iterator()
                .next();

            ebf = formedEbf(helper);
            helper.gtnh()
                .fastForwardTicks(duration - progress + 10);
            helper.assertTrue(
                countBusItems(helper, OUTPUT_BUS, oldOutput) == index + 1,
                "Cache reset lost or duplicated the pending output");
            helper.assertTrue(
                countBusItems(helper, OUTPUT_BUS, newOutput) == index * 2,
                "Cache reset substituted output for the running process");
            helper.assertTrue(
                controller.getSingleRecipeCheck() == null,
                "The cache was rebuilt without a successful recipe");

            ebf.inputBus(0)
                .insert(replacementInput.copy());
            ebf.inputHatch(0)
                .fill(replacementWater.copy());
            for (int tick = 0; tick < 100 && controller.getSingleRecipeCheck() == null; tick++) {
                helper.gtnh()
                    .fastForwardTicks(1);
            }
            helper.assertTrue(
                controller.getSingleRecipeCheck() != null && controller.getSingleRecipeCheck()
                    .getRecipe() == replacement,
                "Native processing did not rebuild the cache for the current recipe");
            helper.gtnh()
                .fastForwardTicks(100);
            ebf.inputBus(0)
                .assertEmpty();
            ebf.inputHatch(0)
                .assertEmpty();
            helper.assertTrue(
                countBusItems(helper, OUTPUT_BUS, newOutput) == index * 2 + 1,
                "The rebuilt cache used the wrong input cost or output");

            NBTTagCompound current = new NBTTagCompound();
            base.writeToNBT(current);
            helper.assertTrue(
                current.getCompoundTag("mSingleRecipeCheck")
                    .getInteger("specialValue") == 0,
                "The rebuilt cache retained a numeric environment flag");
            base.readFromNBT(current);
            controller = (MTEMultiBlockBase) base.getMetaTileEntity();
            SingleRecipeCheck cached = controller.getSingleRecipeCheck();
            helper.assertTrue(
                base.isAllowedToWork() && cached != null && cached.getRecipe() == replacement,
                "A current-format cache did not survive its next reload");
            ebf = formedEbf(helper);
            ebf.inputBus(0)
                .insert(replacementInput.copy());
            ebf.inputHatch(0)
                .fill(replacementWater.copy());
            helper.gtnh()
                .fastForwardTicks(100);
            ebf.inputBus(0)
                .assertEmpty();
            ebf.inputHatch(0)
                .assertEmpty();
            helper.assertTrue(
                controller.getSingleRecipeCheck() == cached,
                "Native processing unnecessarily replaced the regenerated cache");
            helper.assertTrue(
                countBusItems(helper, OUTPUT_BUS, newOutput) == (index + 1) * 2,
                "The current cache lost or duplicated a completed output");
            if (index == oldValues.length - 1) {
                NBTTagCompound invalid = new NBTTagCompound();
                base.writeToNBT(invalid);
                invalid.getCompoundTag("mSingleRecipeCheck")
                    .setInteger("specialValue", 17);
                base.readFromNBT(invalid);
                helper.assertTrue(!base.isAllowedToWork(), "Migration bypassed an unrelated invalid cache");
            }
            RecipeMaps.blastFurnaceRecipes.getBackend()
                .removeRecipes(newRecipes);
        }

        GTRecipe aliases = GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.command_block, 1, 68))
            .itemOutputs(new ItemStack(Items.golden_apple))
            .duration(20)
            .eut(8)
            .requiresCleanRoom()
            .requiresLowGravity()
            .requiresEnvironment(ADMISSION_ENVIRONMENT)
            .build()
            .orElseThrow(() -> new AssertionError("Builder aliases did not produce a recipe"));
        helper.assertTrue(
            aliases.mSpecialValue == 0 && aliases.getEnvironmentRequirements()
                .contains(CLEANROOM)
                && aliases.getEnvironmentRequirements()
                    .contains(LOW_GRAVITY)
                && aliases.getEnvironmentRequirements()
                    .contains(ADMISSION_ENVIRONMENT),
            "Builder aliases lost combined environment requirements");
        GTRecipe legacyKeys = GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.command_block, 1, 69))
            .itemOutputs(new ItemStack(Items.golden_apple))
            .duration(20)
            .eut(8)
            .metadata(GTRecipeConstants.CLEANROOM, true)
            .metadata(GTRecipeConstants.LOW_GRAVITY, true)
            .build()
            .orElseThrow(() -> new AssertionError("Builder metadata did not produce a recipe"));
        helper.assertTrue(
            legacyKeys.mSpecialValue == 0 && legacyKeys.getEnvironmentRequirements()
                .contains(CLEANROOM)
                && legacyKeys.getEnvironmentRequirements()
                    .contains(LOW_GRAVITY)
                && legacyKeys.getMetadata(GTRecipeConstants.CLEANROOM) == null
                && legacyKeys.getMetadata(GTRecipeConstants.LOW_GRAVITY) == null,
            "Old builder metadata was not converted to environment requirements");
        helper.succeed();
    }

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
    public static void missingOutputBusNeverForms(GameTestHelper helper) {
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

    @GameTest(template = "valid", timeoutTicks = 300, batch = "gt5.environments")
    public static void environmentBlocksInputsAndRetriesWhenSatisfied(GameTestHelper helper) {
        Multiblock ebf = formedEbf(helper);
        ItemStack input = stack(Blocks.dirt);
        ItemStack output = stack(Blocks.cobblestone);
        helper.gtnh()
            .withTestRecipe(
                ebf,
                recipe(LOW_HEAT, 20, TierEU.RECIPE_MV).itemInputs(input.copy())
                    .itemOutputs(output.copy())
                    .requiresEnvironment(ADMISSION_ENVIRONMENT));
        ebf.inputBus(0)
            .insert(input);
        ebf.energyHatch(0)
            .supply(TierEU.EV, 1, 250);

        ADMISSION_ENVIRONMENT.available = false;
        try {
            helper.gtnh()
                .multiBlockController(CONTROLLER)
                .enableWorking();
            helper.gtnh()
                .fastForwardTicks(100);
            ebf.inputBus(0)
                .assertContains(input);
            ebf.outputs()
                .assertNotContains(output);

            ADMISSION_ENVIRONMENT.available = true;
            helper.gtnh()
                .fastForwardTicks(120);
            ebf.inputBus(0)
                .assertEmpty();
            ebf.outputBus(0)
                .assertContains(output);
            ebf.assertNoExplosion();
            helper.succeed();
        } finally {
            ADMISSION_ENVIRONMENT.available = true;
        }
    }

    @GameTest(template = "valid", timeoutTicks = 700, batch = "gt5.environments")
    public static void nonEnvironmentalFailureWaitsForNativeWakeup(GameTestHelper helper) {
        Multiblock ebf = formedEbf(helper);
        MTEMultiBlockBase controller = helper.gtnh()
            .multiBlockController(CONTROLLER);
        ItemStack input = stack(Blocks.bedrock);
        ItemStack output = stack(Blocks.bookshelf);
        ebf.inputBus(0)
            .insert(input.copy());
        ebf.energyHatch(0)
            .supply(TierEU.EV, 1, 650);
        controller.enableWorking();
        helper.gtnh()
            .fastForwardTicks(200);
        helper.assertTrue(
            CheckRecipeResultRegistry.NO_RECIPE.equals(controller.getCheckRecipeResult()),
            "Idle EBF did not settle on NO_RECIPE");

        // Registering the recipe changes the map without sending a machine inventory or work event
        addItemRecipe(helper, ebf, input.copy(), output.copy(), LOW_HEAT, 20, TierEU.RECIPE_MV);
        helper.gtnh()
            .fastForwardTicks(220);
        ebf.inputBus(0)
            .assertContains(input);
        ebf.outputs()
            .assertNotContains(output);

        controller.disableWorking();
        controller.enableWorking();
        ebf.runRecipe(120);
        ebf.inputBus(0)
            .assertEmpty();
        ebf.outputBus(0)
            .assertContains(output);
        ebf.assertNoExplosion();
        helper.succeed();
    }

    @GameTest(template = "valid", timeoutTicks = 700, batch = "gt5.environments")
    public static void environmentLossVoidsRunningRecipeAndAllowsFreshWork(GameTestHelper helper) {
        Multiblock ebf = formedEbf(helper);
        MTEMultiBlockBase controller = helper.gtnh()
            .multiBlockController(CONTROLLER);
        ItemStack input = stack(Blocks.sand);
        ItemStack output = stack(Blocks.glass);
        helper.gtnh()
            .withTestRecipe(
                ebf,
                recipe(LOW_HEAT, 600, TierEU.RECIPE_MV).itemInputs(input.copy())
                    .itemOutputs(output.copy())
                    .requiresEnvironment(RUNNING_ENVIRONMENT));
        ebf.inputBus(0)
            .insert(input.copy());
        ebf.energyHatch(0)
            .supply(TierEU.EV, 1, 650);

        RUNNING_ENVIRONMENT.available = true;
        try {
            controller.enableWorking();
            for (int tick = 0; tick < 100 && !ebf.isProcessing(); tick++) {
                helper.gtnh()
                    .fastForwardTicks(1);
            }
            helper.assertTrue(ebf.isProcessing(), "EBF never started the environment-constrained recipe");
            ebf.inputBus(0)
                .assertEmpty();
            helper.gtnh()
                .fastForwardTicks(5);
            helper.assertTrue(ebf.progress() > 0, "EBF did not advance before losing its environment");

            helper.gtnh()
                .fastForwardTicks(controller.mMaxProgresstime - controller.mProgresstime - 1);
            RUNNING_ENVIRONMENT.available = false;
            helper.gtnh()
                .fastForwardTicks(2);
            helper.assertTrue(!ebf.isProcessing(), "EBF ignored environment loss immediately before completion");
            helper.assertTrue(
                ShutDownReasonRegistry.POWER_LOSS.getKey()
                    .equals(
                        controller.getBaseMetaTileEntity()
                            .getLastShutDownReason()
                            .getKey()),
                "Environment loss did not use the native power-loss path");
            ebf.outputs()
                .assertNotContains(output);

            RUNNING_ENVIRONMENT.available = true;
            helper.gtnh()
                .fastForwardTicks(200);
            helper.assertTrue(
                !controller.getBaseMetaTileEntity()
                    .isAllowedToWork(),
                "Environment recovery re-enabled a machine shut down by native power loss");
            helper.assertTrue(!ebf.isProcessing(), "Environment recovery restarted the discarded recipe");
            ebf.outputs()
                .assertNotContains(output);
            controller.enableWorking();
            ebf.inputBus(0)
                .insert(input.copy());
            ebf.runRecipe(250);
            ebf.inputBus(0)
                .assertEmpty();
            assertBusContainsAmount(helper, OUTPUT_BUS, output, 1, "EBF did not process fresh input after recovery");
            ebf.assertNoExplosion();
            helper.succeed();
        } finally {
            RUNNING_ENVIRONMENT.available = true;
        }
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

    @GameTest(template = "valid", timeoutTicks = 160, batch = "gt5.ebf")
    public static void evEnergyHatchDoesNotGrantHigherVoltageHeatBonus(GameTestHelper helper) {
        Multiblock ebf = formedEbf(helper);
        ItemStack input = stack(Blocks.bookshelf);
        ItemStack output = stack(Blocks.planks);

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
        ebf.assertFormed();
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
        int found = countBusItems(helper, pos, expected);
        helper.assertTrue(found >= amount, message + " (found " + found + ")");
    }

    private static int countBusItems(GameTestHelper helper, TestPos pos, ItemStack expected) {
        IMetaTileEntity bus = helper.gtnh()
            .metaTileEntity(pos);
        int found = 0;
        for (int slot = 0; slot < bus.getSizeInventory(); slot++) {
            ItemStack inSlot = bus.getStackInSlot(slot);
            if (matches(inSlot, expected)) {
                found += inSlot.stackSize;
            }
        }
        return found;
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

    private static final class TestEnvironment extends RecipeEnvironment {

        private boolean available = true;

        private TestEnvironment(String name) {
            super("gregtech:tests/" + name, "gt5-tests." + name);
        }

        @Override
        public boolean isSatisfied(MetaTileEntity machine) {
            return available;
        }
    }
}
