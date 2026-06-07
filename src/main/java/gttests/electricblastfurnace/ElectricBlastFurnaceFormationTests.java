package gttests.electricblastfurnace;

import static com.gtnewhorizons.horizonqa.api.TestPos.at;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import com.gtnewhorizons.horizonqa.api.GameTestAssertException;
import com.gtnewhorizons.horizonqa.api.GameTestHelper;
import com.gtnewhorizons.horizonqa.api.TestPos;
import com.gtnewhorizons.horizonqa.api.annotation.GameTest;
import com.gtnewhorizons.horizonqa.api.annotation.GameTestHolder;
import com.gtnewhorizons.horizonqa.api.gt.Multiblock;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;

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
    private static final int MV_ENERGY_HATCH_ID = 42;
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
        forceStructureRescan(helper);

        helper.assertFalse(ebf.isFormed(), "EBF stayed formed after a coil was destroyed");
        helper.succeed();
    }

    @GameTest(template = "valid", timeoutTicks = 200, batch = "gt5.ebf")
    public static void syntheticRecipeRunsAtValidHeat(GameTestHelper helper) {
        Multiblock ebf = formedEbf(helper);
        ItemStack input = stack(Blocks.dirt);
        ItemStack output = stack(Blocks.cobblestone);

        addTestRecipe(helper, ebf, input, output, LOW_HEAT, 20, TierEU.RECIPE_MV);
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

    @GameTest(template = "valid", timeoutTicks = 160, batch = "gt5.ebf")
    public static void recipeAboveHeatNeverStarts(GameTestHelper helper) {
        Multiblock ebf = formedEbf(helper);
        ItemStack input = stack(Blocks.sand);
        ItemStack output = stack(Blocks.glass);

        addTestRecipe(helper, ebf, input, output, EV_CUPRONICKEL_HEAT + 100, 20, TierEU.RECIPE_MV);
        ebf.inputBus(0)
            .insert(input);
        ebf.energyHatch(0)
            .supply(TierEU.EV, 1, 100);

        helper.gtnh()
            .fastForwardTicks(100);

        ebf.inputBus(0)
            .assertContains(input);
        assertOutputMissing(helper, ebf, output, "EBF produced output for a recipe above its heat capacity");
        ebf.assertNoExplosion();
        helper.succeed();
    }

    @GameTest(template = "valid", timeoutTicks = 200, batch = "gt5.ebf")
    public static void evEnergyHatchAllowsVoltageHeatBonus(GameTestHelper helper) {
        Multiblock ebf = formedEbf(helper);
        ItemStack input = stack(Blocks.gravel);
        ItemStack output = stack(Blocks.stone);

        addTestRecipe(helper, ebf, input, output, MV_CUPRONICKEL_HEAT + 1, 20, TierEU.RECIPE_MV);
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
    public static void mvEnergyHatchDoesNotProvideEvHeatBonus(GameTestHelper helper) {
        setMetaTileEntityId(helper, ENERGY_HATCH, MV_ENERGY_HATCH_ID);

        Multiblock ebf = formedEbf(helper);
        ItemStack input = stack(Blocks.sponge);
        ItemStack output = stack(Blocks.mossy_cobblestone);

        addTestRecipe(helper, ebf, input, output, MV_CUPRONICKEL_HEAT + 1, 20, TierEU.RECIPE_MV);
        ebf.inputBus(0)
            .insert(input);
        ebf.energyHatch(0)
            .supply(TierEU.MV, 1, 100);

        helper.gtnh()
            .fastForwardTicks(100);

        ebf.inputBus(0)
            .assertContains(input);
        assertOutputMissing(helper, ebf, output, "MV energy hatch unexpectedly allowed EV-boosted EBF heat");
        ebf.assertNoExplosion();
        helper.succeed();
    }

    @GameTest(template = "valid", timeoutTicks = 160, batch = "gt5.ebf")
    public static void insufficientEuNeverProducesOutput(GameTestHelper helper) {
        Multiblock ebf = formedEbf(helper);
        ItemStack input = stack(Blocks.netherrack);
        ItemStack output = stack(Blocks.nether_brick);

        addTestRecipe(helper, ebf, input, output, LOW_HEAT, 20, TierEU.RECIPE_MV);
        ebf.inputBus(0)
            .insert(input);

        helper.gtnh()
            .fastForwardTicks(100);

        assertOutputMissing(helper, ebf, output, "EBF produced output without EU");
        ebf.assertNoExplosion();
        helper.succeed();
    }

    @GameTest(template = "valid", timeoutTicks = 160, batch = "gt5.ebf")
    public static void fullOutputBusDoesNotConsumeInputs(GameTestHelper helper) {
        Multiblock ebf = formedEbf(helper);
        ItemStack input = stack(Blocks.soul_sand);
        ItemStack output = stack(Blocks.obsidian);

        addTestRecipe(helper, ebf, input, output, LOW_HEAT, 20, TierEU.RECIPE_MV);
        ebf.inputBus(0)
            .insert(input);
        fillOutputBus(helper);
        ebf.energyHatch(0)
            .supply(TierEU.EV, 1, 100);

        helper.gtnh()
            .fastForwardTicks(100);

        ebf.inputBus(0)
            .assertContains(input);
        assertOutputMissing(helper, ebf, output, "EBF produced output into a full output bus");
        ebf.assertNoExplosion();
        helper.succeed();
    }

    @GameTest(template = "valid", timeoutTicks = 900, batch = "gt5.ebf", required = false)
    public static void runningRecipeEmitsPollution(GameTestHelper helper) {
        Multiblock ebf = formedEbf(helper);
        ItemStack input = stack(Blocks.end_stone);
        ItemStack output = stack(Blocks.quartz_block);

        addTestRecipe(helper, ebf, input, output, LOW_HEAT, 600, TierEU.RECIPE_EV);
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

    private static Multiblock ebf(GameTestHelper helper) {
        Multiblock ebf = helper.gtnh()
            .multiblock(CONTROLLER);
        ebf.fixMaintenance();
        return ebf;
    }

    private static void assertNeverForms(GameTestHelper helper, String message) {
        Multiblock ebf = ebf(helper);
        forceStructureRescan(helper);
        helper.assertFalse(ebf.isFormed(), message);
        helper.onEachTick(() -> helper.assertFalse(ebf.isFormed(), message));
        helper.succeedAtTimeout();
    }

    private static void replaceWithHeatProofCasing(GameTestHelper helper, TestPos pos) {
        helper.setBlock(pos.x(), pos.y(), pos.z(), GregTechAPI.sBlockCasings1, HEAT_PROOF_CASING_META);
    }

    private static void addTestRecipe(GameTestHelper helper, Multiblock ebf, ItemStack input, ItemStack output,
        int heat, int duration, long eut) {
        helper.gtnh()
            .withTestRecipe(
                ebf,
                GTValues.RA.stdBuilder()
                    .itemInputs(input.copy())
                    .itemOutputs(output.copy())
                    .duration(duration)
                    .eut(eut)
                    .metadata(COIL_HEAT, heat));
    }

    private static void assertOutputMissing(GameTestHelper helper, Multiblock ebf, ItemStack output, String message) {
        try {
            ebf.outputBus(0)
                .assertContains(output);
        } catch (GameTestAssertException expected) {
            return;
        }
        helper.fail(message);
    }

    private static void fillOutputBus(GameTestHelper helper) {
        IMetaTileEntity outputBus = metaTileEntity(helper, OUTPUT_BUS, "output bus");
        for (int slot = 0; slot < outputBus.getSizeInventory(); slot++) {
            outputBus.setInventorySlotContents(slot, fullStack(Blocks.stone));
        }
    }

    private static void forceStructureRescan(GameTestHelper helper) {
        IMetaTileEntity metaTileEntity = metaTileEntity(helper, CONTROLLER, "EBF controller");
        if (!(metaTileEntity instanceof MTEMultiBlockBase multiBlock)) {
            helper.fail("EBF controller is not a multiblock controller");
            return;
        }

        // assertFormed() intentionally skips rescans once mMachine is true.
        multiBlock.checkStructure(true, multiBlock.getBaseMetaTileEntity());
    }

    private static IMetaTileEntity metaTileEntity(GameTestHelper helper, TestPos pos, String label) {
        TestPos absolutePos = helper.absolute(pos.x(), pos.y(), pos.z());
        TileEntity tileEntity = helper.getWorld()
            .getTileEntity(absolutePos.x(), absolutePos.y(), absolutePos.z());

        if (!(tileEntity instanceof IGregTechTileEntity gregTechTileEntity)) {
            helper.fail("Expected a GregTech tile entity for " + label);
            return null;
        }

        IMetaTileEntity metaTileEntity = gregTechTileEntity.getMetaTileEntity();
        if (metaTileEntity == null) {
            helper.fail("Missing meta tile entity for " + label);
        }
        return metaTileEntity;
    }

    private static void setMetaTileEntityId(GameTestHelper helper, TestPos pos, int id) {
        NBTTagCompound nbt = helper.getTileNBT(pos.x(), pos.y(), pos.z());
        nbt.setInteger("mID", id);
        helper.setTile(pos.x(), pos.y(), pos.z(), nbt);
    }

    private static ItemStack stack(Block block) {
        return new ItemStack(block, 1);
    }

    private static ItemStack fullStack(Block block) {
        return new ItemStack(block, 64);
    }
}
