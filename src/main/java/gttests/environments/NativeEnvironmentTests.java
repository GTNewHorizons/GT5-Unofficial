package gttests.environments;

import static com.gtnewhorizons.horizonqa.api.TestPos.at;
import static gregtech.api.recipe.RecipeEnvironment.LOW_GRAVITY;

import java.util.Collection;
import java.util.UUID;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizons.horizonqa.api.GameTestHelper;
import com.gtnewhorizons.horizonqa.api.TestPos;
import com.gtnewhorizons.horizonqa.api.annotation.GameTest;
import com.gtnewhorizons.horizonqa.api.annotation.GameTestHolder;
import com.gtnewhorizons.horizonqa.api.gt.Multiblock;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Mods;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.recipe.RecipeEnvironment;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.FakeCleanroom;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.tileentities.machines.multi.MTEFusionComputer;
import gregtech.common.tileentities.machines.multi.MTENanoForge;

@GameTestHolder(value = Mods.ModIDs.GREG_TECH, templatePrefix = "multiblock/environments")
public class NativeEnvironmentTests {

    private static final TestEnvironment NANO_ENVIRONMENT = RecipeEnvironment
        .register(new TestEnvironment("nano_admission"));
    private static final TestEnvironment FUSION_ENVIRONMENT = RecipeEnvironment
        .register(new TestEnvironment("fusion_running"));
    private static final TestEnvironment BASIC_ENVIRONMENT = RecipeEnvironment
        .register(new TestEnvironment("basic_running"));
    private static final TestEnvironment FUSION_RELOAD_ENVIRONMENT = RecipeEnvironment
        .register(new TestEnvironment("fusion_reload"));

    @GameTest(template = "fusion_space", timeoutTicks = 700, batch = "gt5.environments")
    public static void lowGravityMetadataPreservesNativeAdmission(GameTestHelper helper) {
        BaseMetaTileEntity base = placeMachine(helper, at(1, 1, 1), ItemList.Machine_LV_Macerator);
        MTEBasicMachine machine = (MTEBasicMachine) base.getMetaTileEntity();
        ItemStack input = new ItemStack(Blocks.command_block, 1, 70);
        ItemStack output = new ItemStack(Items.golden_apple);
        Collection<GTRecipe> recipes = GTValues.RA.stdBuilder()
            .itemInputs(input.copy())
            .itemOutputs(output.copy())
            .duration(20)
            .eut(8)
            .requiresEnvironment(LOW_GRAVITY)
            .addTo(RecipeMaps.maceratorRecipes);
        helper.assertTrue(recipes.size() == 1, "Low-gravity recipe was not registered");
        helper.afterTest(
            () -> RecipeMaps.maceratorRecipes.getBackend()
                .removeRecipes(recipes));
        boolean previousEnabled = GTMod.proxy.mLowGravProcessing;
        boolean previousBypass = FakeCleanroom.LOWGRAV_BYPASS;
        try {
            GTMod.proxy.mLowGravProcessing = true;
            FakeCleanroom.LOWGRAV_BYPASS = false;
            base.increaseStoredEnergyUnits(base.getEUCapacity(), true);
            machine.setInventorySlotContents(machine.getInputSlot(), input.copy());
            base.enableWorking();
            helper.gtnh()
                .fastForwardTicks(60);
            helper.assertTrue(
                ItemStack.areItemStacksEqual(input, machine.getStackInSlot(machine.getInputSlot())),
                "An overworld machine consumed a low-gravity recipe's input");
            helper.assertTrue(machine.maxProgresstime() == 0, "An overworld machine started a low-gravity recipe");
            GTMod.proxy.mLowGravProcessing = false;
            machine.onRecipeEnvironmentChanged();
            helper.gtnh()
                .fastForwardTicks(60);
            helper.assertTrue(
                ItemStack.areItemStacksEqual(output, machine.getStackInSlot(machine.getOutputSlot())),
                "Disabling low-gravity restrictions did not restore native processing");
            helper.succeed();
        } finally {
            GTMod.proxy.mLowGravProcessing = previousEnabled;
            FakeCleanroom.LOWGRAV_BYPASS = previousBypass;
        }
    }

    @GameTest(template = "fusion_space", timeoutTicks = 700, batch = "gt5.environments")
    public static void stalledOneTickRecipeRespectsEnvironmentCheckInterval(GameTestHelper helper) {
        BaseMetaTileEntity base = placeMachine(helper, at(1, 1, 1), ItemList.Machine_LV_Macerator);
        MTEBasicMachine machine = (MTEBasicMachine) base.getMetaTileEntity();
        ItemStack input = new ItemStack(Blocks.command_block, 1, 1);
        ItemStack output = new ItemStack(Items.golden_apple);
        Collection<GTRecipe> recipes = GTValues.RA.stdBuilder()
            .itemInputs(input.copy())
            .itemOutputs(output.copy())
            .duration(1)
            .eut(8)
            .requiresEnvironment(BASIC_ENVIRONMENT)
            .addTo(RecipeMaps.maceratorRecipes);
        helper.assertTrue(recipes.size() == 1, "One-tick environment recipe was not registered");
        helper.afterTest(
            () -> RecipeMaps.maceratorRecipes.getBackend()
                .removeRecipes(recipes));

        BASIC_ENVIRONMENT.available = true;
        BASIC_ENVIRONMENT.checks = 0;
        try {
            base.increaseStoredEnergyUnits(base.getEUCapacity(), true);
            machine.setInventorySlotContents(machine.getInputSlot(), input.copy());
            base.enableWorking();
            for (int tick = 0; tick < 100 && machine.maxProgresstime() == 0; tick++) {
                helper.gtnh()
                    .fastForwardTicks(1);
            }
            helper.assertTrue(machine.maxProgresstime() == 1, "Native machine did not admit a one-tick recipe");
            helper
                .assertTrue(machine.getStackInSlot(machine.getInputSlot()) == null, "Recipe did not consume its input");

            BASIC_ENVIRONMENT.available = false;
            int checksBeforeLoss = BASIC_ENVIRONMENT.checks;
            helper.gtnh()
                .fastForwardTicks(1);
            helper.assertTrue(
                BASIC_ENVIRONMENT.checks == checksBeforeLoss + 1,
                "Completion did not validate the environment");
            helper.assertTrue(
                machine.getStackInSlot(machine.getOutputSlot()) == null,
                "Failed environment released output");
            helper.gtnh()
                .fastForwardTicks(100);
            int checksAfterPause = BASIC_ENVIRONMENT.checks;
            helper.gtnh()
                .fastForwardTicks(200);
            int stalledChecks = BASIC_ENVIRONMENT.checks - checksAfterPause;
            helper.assertTrue(
                stalledChecks > 0 && stalledChecks <= 3,
                "Stalled one-tick recipe ignored the check interval: " + stalledChecks + " checks in 200 ticks");
            helper.assertTrue(machine.maxProgresstime() == 1, "Stalled recipe was discarded");
            helper
                .assertTrue(machine.getStackInSlot(machine.getOutputSlot()) == null, "Stalled recipe released output");

            helper.gtnh()
                .fastForwardTicks(5);
            int checksBeforeRecovery = BASIC_ENVIRONMENT.checks;
            BASIC_ENVIRONMENT.available = true;
            helper.gtnh()
                .fastForwardTicks(1);
            helper.assertTrue(
                BASIC_ENVIRONMENT.checks == checksBeforeRecovery,
                "Cached failure was checked before its deadline");
            helper.assertTrue(machine.getStackInSlot(machine.getOutputSlot()) == null, "Cached failure allowed output");
            machine.onRecipeEnvironmentChanged();
            helper.gtnh()
                .fastForwardTicks(1);
            helper.assertTrue(
                BASIC_ENVIRONMENT.checks == checksBeforeRecovery + 1,
                "Invalidation did not recheck the environment");
            helper.assertTrue(
                ItemStack.areItemStacksEqual(output, machine.getStackInSlot(machine.getOutputSlot())),
                "Environment recovery did not release the recipe output");
            helper.assertTrue(machine.maxProgresstime() == 0, "Recovered one-tick recipe did not finish");
            helper.succeed();
        } finally {
            BASIC_ENVIRONMENT.available = true;
        }
    }

    @GameTest(template = "nano_space", timeoutTicks = 700, batch = "gt5.environments")
    public static void nanoForgePreservesBoostResourcesWhenEnvironmentIsMissing(GameTestHelper helper) {
        TestPos controller = at(20, 1, 0);
        MTENanoForge forge = (MTENanoForge) placeMachine(helper, controller, ItemList.NanoForge).getMetaTileEntity();
        forge.construct(new ItemStack(Items.stick, 4), false);
        placeMachine(helper, at(16, 1, 0), ItemList.Hatch_Input_Bus_LV);
        placeMachine(helper, at(17, 1, 0), ItemList.Hatch_Input_LV);
        placeMachine(helper, at(18, 1, 0), ItemList.Hatch_Output_Bus_LV);
        placeMachine(helper, at(19, 1, 0), ItemList.Hatch_Energy_EV);
        forge.setInventorySlotContents(
            forge.getControllerSlotIndex(),
            MaterialLibAPI.getStack(Materials.Eternity, Shapes.nanite, 1));
        Multiblock machine = helper.gtnh()
            .withWarpRange(48)
            .multiblock(controller);
        machine.assertFormed();

        ItemStack input = new ItemStack(Items.apple);
        ItemStack nanite = MaterialLibAPI.getStack(Materials.Carbon, Shapes.nanite, 1);
        FluidStack magmatter = MaterialLibAPI.getFluidStack(Materials.MagMatter, FluidShapes.fluidMolten, 288);
        helper.gtnh()
            .withTestRecipe(
                machine,
                GTValues.RA.stdBuilder()
                    .itemInputs(input.copy())
                    .itemOutputs(nanite.copy())
                    .duration(100)
                    .eut(32)
                    .specialValue(4)
                    .requiresEnvironment(NANO_ENVIRONMENT));
        machine.inputBus(0)
            .insert(input.copy(), nanite.copy());
        machine.inputHatch(0)
            .fill(magmatter.copy());
        machine.energyHatch(0)
            .supply(TierEU.EV, 1, 500);

        NANO_ENVIRONMENT.available = false;
        try {
            forge.enableWorking();
            helper.gtnh()
                .fastForwardTicks(120);
            machine.inputHatch(0)
                .assertContains(magmatter);
            machine.inputBus(0)
                .assertContains(nanite);
            machine.inputBus(0)
                .assertContains(input);
            machine.outputs()
                .assertNotContains(nanite);

            NANO_ENVIRONMENT.available = true;
            helper.gtnh()
                .fastForwardTicks(220);
            machine.inputHatch(0)
                .assertEmpty();
            machine.inputBus(0)
                .assertEmpty();
            machine.outputBus(0)
                .assertContains(nanite);
            machine.assertNoExplosion();
            helper.succeed();
        } finally {
            NANO_ENVIRONMENT.available = true;
        }
    }

    @GameTest(template = "fusion_space", timeoutTicks = 700, batch = "gt5.environments")
    public static void fusionLosesPendingOutputWhenItsEnvironmentDisappears(GameTestHelper helper) {
        TestPos controller = at(7, 1, 12);
        BaseMetaTileEntity base = placeMachine(helper, controller, ItemList.FusionComputer_LuV);
        MTEFusionComputer fusion = (MTEFusionComputer) base.getMetaTileEntity();
        fusion.construct(new ItemStack(Items.stick), false);
        placeMachine(helper, at(3, 1, 2), ItemList.Hatch_Energy_LuV);
        placeMachine(helper, at(6, 0, 1), ItemList.Hatch_Input_LV);
        placeMachine(helper, at(8, 0, 1), ItemList.Hatch_Input_LV);
        placeMachine(helper, at(6, 1, 0), ItemList.Hatch_Output_LV);
        Multiblock machine = helper.gtnh()
            .multiblock(controller);
        machine.assertFormed();
        FluidStack input = new FluidStack(FluidRegistry.WATER, 1000);
        FluidStack secondInput = new FluidStack(FluidRegistry.LAVA, 1000);
        FluidStack output = MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidGas, 1000);
        helper.gtnh()
            .withTestRecipe(
                machine,
                GTValues.RA.stdBuilder()
                    .fluidInputs(input.copy(), secondInput.copy())
                    .fluidOutputs(output.copy())
                    .duration(2000)
                    .eut(8)
                    .requiresEnvironment(FUSION_ENVIRONMENT));
        machine.inputHatch(0)
            .fill(input.copy());
        machine.inputHatch(1)
            .fill(secondInput.copy());
        machine.energyHatch(0)
            .supply(TierEU.LuV, 1, 500);

        FUSION_ENVIRONMENT.available = true;
        try {
            fusion.enableWorking();
            for (int tick = 0; tick < 200 && !machine.isProcessing(); tick++) {
                helper.gtnh()
                    .fastForwardTicks(1);
            }
            helper.assertTrue(
                machine.isProcessing(),
                "Native fusion did not start: " + fusion.getCheckRecipeResult()
                    .getID() + ", stored EU: " + base.getStoredEU());
            machine.inputHatch(0)
                .assertEmpty();
            helper.gtnh()
                .fastForwardTicks(3);
            helper.assertTrue(machine.progress() > 0, "Native fusion did not advance before environment loss");

            FUSION_ENVIRONMENT.available = false;
            helper.gtnh()
                .fastForwardTicks(2);
            helper.assertTrue(machine.isProcessing(), "Continuous conditions were still checked every tick");
            helper.gtnh()
                .fastForwardTicks(100);
            helper.assertTrue(!machine.isProcessing(), "Custom fusion tick ignored the environment check interval");
            helper.assertTrue(
                ShutDownReasonRegistry.POWER_LOSS.getKey()
                    .equals(
                        base.getLastShutDownReason()
                            .getKey()),
                "Native fusion did not stop through POWER_LOSS");
            FUSION_ENVIRONMENT.available = true;
            helper.gtnh()
                .fastForwardTicks(100);
            helper.assertTrue(!base.isAllowedToWork(), "Environment recovery re-enabled failed fusion");
            machine.outputHatch(0)
                .assertEmpty();
            machine.assertNoExplosion();
            helper.succeed();
        } finally {
            FUSION_ENVIRONMENT.available = true;
        }
    }

    @GameTest(template = "fusion_space", timeoutTicks = 3000, batch = "gt5.environments")
    public static void fusionReloadRejectsItsSavedUnsatisfiedEnvironment(GameTestHelper helper) {
        fusionReloadRetainsAcceptedEnvironment(helper, FUSION_RELOAD_ENVIRONMENT.getId(), false, false);
    }

    @GameTest(template = "fusion_space", timeoutTicks = 3000, batch = "gt5.environments")
    public static void fusionReloadRejectsAnUnknownSavedEnvironment(GameTestHelper helper) {
        fusionReloadRetainsAcceptedEnvironment(helper, "gregtech:tests/absent_fusion_provider", false, false);
    }

    @GameTest(template = "fusion_space", timeoutTicks = 3000, batch = "gt5.environments")
    public static void fusionReloadCompletesWithItsSavedSatisfiedEnvironment(GameTestHelper helper) {
        fusionReloadRetainsAcceptedEnvironment(helper, FUSION_RELOAD_ENVIRONMENT.getId(), true, false);
    }

    @GameTest(template = "fusion_space", timeoutTicks = 3000, batch = "gt5.environments")
    public static void fusionReloadKeepsItsSavedConditionAfterReconstruction(GameTestHelper helper) {
        fusionReloadRetainsAcceptedEnvironment(helper, FUSION_RELOAD_ENVIRONMENT.getId(), true, true);
    }

    private static void fusionReloadRetainsAcceptedEnvironment(GameTestHelper helper, String savedEnvironmentId,
        boolean environmentAvailable, boolean loseEnvironmentAfterReload) {
        TestPos controller = at(7, 1, 12);
        BaseMetaTileEntity base = placeMachine(helper, controller, ItemList.FusionComputer_LuV);
        MTEFusionComputer fusion = (MTEFusionComputer) base.getMetaTileEntity();
        fusion.construct(new ItemStack(Items.stick), false);
        placeMachine(helper, at(3, 1, 2), ItemList.Hatch_Energy_LuV);
        MTEHatchInput firstHatch = (MTEHatchInput) placeMachine(helper, at(6, 0, 1), ItemList.Hatch_Input_LV)
            .getMetaTileEntity();
        MTEHatchInput secondHatch = (MTEHatchInput) placeMachine(helper, at(8, 0, 1), ItemList.Hatch_Input_LV)
            .getMetaTileEntity();
        MTEHatchOutput outputHatch = (MTEHatchOutput) placeMachine(helper, at(6, 1, 0), ItemList.Hatch_Output_LV)
            .getMetaTileEntity();
        Multiblock machine = helper.gtnh()
            .multiblock(controller);
        machine.assertFormed();
        fusion.setRecipeLocking(false);
        FluidStack input = new FluidStack(FluidRegistry.WATER, 1000);
        FluidStack secondInput = new FluidStack(FluidRegistry.LAVA, 1000);
        FluidStack output = MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidGas, 1000);
        Collection<GTRecipe> originalRecipes = GTValues.RA.stdBuilder()
            .fluidInputs(input.copy(), secondInput.copy())
            .fluidOutputs(output.copy())
            .duration(2000)
            .eut(8)
            .requiresEnvironment(FUSION_RELOAD_ENVIRONMENT)
            .addTo(RecipeMaps.fusionRecipes);
        helper.assertTrue(originalRecipes.size() == 1, "Original fusion reload recipe was not registered");
        helper.afterTest(
            () -> RecipeMaps.fusionRecipes.getBackend()
                .removeRecipes(originalRecipes));
        machine.inputHatch(0)
            .fill(input.copy());
        machine.inputHatch(1)
            .fill(secondInput.copy());
        machine.energyHatch(0)
            .supply(TierEU.LuV, 1, 2500);

        FUSION_RELOAD_ENVIRONMENT.available = true;
        try {
            fusion.enableWorking();
            for (int tick = 0; tick < 200 && !machine.isProcessing(); tick++) {
                helper.gtnh()
                    .fastForwardTicks(1);
            }
            helper.assertTrue(machine.isProcessing(), "Native fusion did not start its reload fixture");
            machine.inputHatch(0)
                .assertEmpty();
            machine.inputHatch(1)
                .assertEmpty();
            helper.gtnh()
                .fastForwardTicks(3);
            helper.assertTrue(
                fusion.mProgresstime > 0 && fusion.mProgresstime < fusion.mMaxProgresstime,
                "Fusion reload fixture has no unfinished progress");
            helper.assertTrue(
                fusion.mOutputFluids != null && fusion.mOutputFluids.length == 1
                    && fusion.mOutputFluids[0] != null
                    && fusion.mOutputFluids[0].isFluidEqual(output)
                    && fusion.mOutputFluids[0].amount == output.amount,
                "Fusion reload fixture has no pending output");
            machine.outputHatch(0)
                .assertEmpty();
            machine.inputHatch(0)
                .fill(input.copy());
            machine.inputHatch(1)
                .fill(secondInput.copy());
            base.increaseStoredEnergyUnits(base.getEUCapacity(), true);

            NBTTagCompound saved = new NBTTagCompound();
            base.writeToNBT(saved);
            NBTTagList savedEnvironments = saved.getTagList("recipeEnvironments", 8);
            helper.assertTrue(
                savedEnvironments.tagCount() == 1 && FUSION_RELOAD_ENVIRONMENT.getId()
                    .equals(savedEnvironments.getStringTagAt(0)),
                "Full Base save did not retain the admitted fusion environment");
            if (!FUSION_RELOAD_ENVIRONMENT.getId()
                .equals(savedEnvironmentId)) {
                helper
                    .assertTrue(RecipeEnvironment.get(savedEnvironmentId) == null, "Missing provider ID is registered");
                NBTTagList unknownEnvironment = new NBTTagList();
                unknownEnvironment.appendTag(new NBTTagString(savedEnvironmentId));
                saved.setTag("recipeEnvironments", unknownEnvironment);
            }
            int progress = saved.getInteger("mProgresstime");
            int duration = saved.getInteger("mMaxProgresstime");
            RecipeMaps.fusionRecipes.getBackend()
                .removeRecipes(originalRecipes);
            Collection<GTRecipe> currentRecipes = GTValues.RA.stdBuilder()
                .fluidInputs(input.copy(), secondInput.copy())
                .fluidOutputs(output.copy())
                .duration(2000)
                .eut(8)
                .addTo(RecipeMaps.fusionRecipes);
            helper.assertTrue(currentRecipes.size() == 1, "Current fusion reload recipe was not registered");
            helper.afterTest(
                () -> RecipeMaps.fusionRecipes.getBackend()
                    .removeRecipes(currentRecipes));
            helper.assertTrue(
                currentRecipes.iterator()
                    .next()
                    .getEnvironmentRequirements()
                    .isEmpty(),
                "Current fusion reload recipe still has an admission requirement");
            FUSION_RELOAD_ENVIRONMENT.available = environmentAvailable;
            base.readFromNBT(saved);
            fusion = (MTEFusionComputer) base.getMetaTileEntity();
            helper.assertTrue(
                fusion.mProgresstime == progress && fusion.mMaxProgresstime == duration && base.isAllowedToWork(),
                "Full Base reload changed the fixture before ticking");
            machine = helper.gtnh()
                .multiblock(controller);
            machine.assertFormed();
            helper.gtnh()
                .fastForwardTicks(1);
            helper.assertTrue(
                firstHatch.getFluidAmount() == input.amount && secondHatch.getFluidAmount() == secondInput.amount,
                "Fusion reload consumed the stocked next batch");

            if (loseEnvironmentAfterReload) {
                helper.assertTrue(
                    machine.isProcessing() && base.isAllowedToWork(),
                    "Fusion did not resume before losing its saved condition");
                FUSION_RELOAD_ENVIRONMENT.available = false;
                fusion.onRecipeEnvironmentChanged();
            }

            if (environmentAvailable && !loseEnvironmentAfterReload) {
                base.disableWorking();
                helper.gtnh()
                    .fastForwardTicks(duration - progress + 100);
                helper.assertTrue(!machine.isProcessing(), "Valid saved fusion environment prevented completion");
                machine.outputHatch(0)
                    .assertContains(output);
                helper.assertTrue(outputHatch.getFluidAmount() == output.amount, "Fusion reload duplicated its output");
            } else {
                helper.gtnh()
                    .fastForwardTicks(120);
                helper.assertTrue(!machine.isProcessing(), "Fusion reload discarded its saved invalid environment");
                helper.assertTrue(
                    ShutDownReasonRegistry.POWER_LOSS.getKey()
                        .equals(
                            base.getLastShutDownReason()
                                .getKey()),
                    "Fusion reload did not reject its saved environment through POWER_LOSS");
                helper.assertTrue(!base.isAllowedToWork(), "Failed fusion reload remained enabled");
                helper.assertTrue(fusion.mOutputFluids == null, "Failed fusion reload retained its pending output");
                FUSION_RELOAD_ENVIRONMENT.available = true;
                helper.gtnh()
                    .fastForwardTicks(duration - progress + 100);
                machine.outputHatch(0)
                    .assertEmpty();
            }
            helper.assertTrue(
                firstHatch.getFluidAmount() == input.amount && secondHatch.getFluidAmount() == secondInput.amount,
                "Fusion reload spent next-batch inputs while completing or rejecting the saved process");
            machine.assertNoExplosion();
            helper.succeed();
        } finally {
            FUSION_RELOAD_ENVIRONMENT.available = true;
        }
    }

    private static BaseMetaTileEntity placeMachine(GameTestHelper helper, TestPos pos, ItemList machine) {
        helper.setBlock(pos, GregTechAPI.sBlockMachines, 0);
        BaseMetaTileEntity base = helper.assertTileEntityPresent(BaseMetaTileEntity.class, pos);
        base.setInitialValuesAsNBT(
            null,
            (short) machine.get(1)
                .getItemDamage());
        base.setFrontFacing(ForgeDirection.NORTH);
        base.setOwnerName("EnvironmentTest");
        base.setOwnerUuid(UUID.fromString("561c5654-f935-4ebd-88a6-003cbbbe39de"));
        return base;
    }

    private static final class TestEnvironment extends RecipeEnvironment {

        private boolean available = true;
        private int checks;

        private TestEnvironment(String name) {
            super("gregtech:tests/" + name, "gt5-tests." + name);
        }

        @Override
        public boolean isSatisfied(MetaTileEntity machine) {
            checks++;
            return available;
        }
    }
}
