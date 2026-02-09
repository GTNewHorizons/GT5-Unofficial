package gregtech.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ParallelHelper;
import gregtech.test.mock.MockIVoidableMachine;

public class GTParallelHelperTest {

    static GTRecipe rubberRecipe;
    static ItemStack[] inputItems;
    static MockIVoidableMachine machine;

    @BeforeAll
    static void setup() {
        machine = new MockIVoidableMachine();
        ItemStack rubberDust = Materials.RubberRaw.getDust(1);
        ItemStack sulfurDust = Materials.Sulfur.getDust(1);
        rubberRecipe = new GTRecipe(
            new ItemStack[] { rubberDust.copy(), sulfurDust.copy() },
            new ItemStack[] { Materials.Rubber.getDust(1), Materials.Rubber.getDustTiny(1) },
            null,
            null,
            new int[] { 10000, 6667 },
            null,
            null,
            null,
            new FluidStack[] { Materials.Rubber.getMolten(1_000) },
            1,
            1,
            0);

        inputItems = new ItemStack[] { GTUtility.copyAmountUnsafe(Integer.MAX_VALUE, rubberDust),
            GTUtility.copyAmountUnsafe(Integer.MAX_VALUE, rubberDust),
            GTUtility.copyAmountUnsafe(Integer.MAX_VALUE, sulfurDust),
            GTUtility.copyAmountUnsafe(Integer.MAX_VALUE, sulfurDust) };
    }

    @Test
    void OutputsIntegerOverflow() {
        ParallelHelper helper = new ParallelHelper().setRecipe(rubberRecipe)
            .setMachine(machine, false, false)
            .setItemInputs(inputItems)
            .setMaxParallel(4_000_000)
            .setAvailableEUt(4_000_000)
            .setOutputCalculation(true)
            .setConsumption(false);
        helper.build();
        FluidStack[] fluidStacks = helper.getFluidOutputs();

        assertEquals(2, fluidStacks.length);
        assertEquals(Integer.MAX_VALUE, fluidStacks[0].amount);
        assertEquals(4_000_000L * 1000 - Integer.MAX_VALUE, fluidStacks[1].amount);
    }

    @Test
    void parallelIntegerOverflow() {
        // Without batch mode
        ParallelHelper helperWithoutBatchMode = new ParallelHelper().setRecipe(rubberRecipe)
            .setMachine(machine, false, false)
            .setItemInputs(inputItems)
            .setMaxParallel(Integer.MAX_VALUE)
            .setAvailableEUt(TierEU.MAX * 16)
            .setConsumption(false)
            .build();
        assertEquals(Integer.MAX_VALUE, helperWithoutBatchMode.getCurrentParallel());

        // With batch mode
        ParallelHelper helperWithBatchMode = new ParallelHelper().setRecipe(rubberRecipe)
            .setMachine(machine, false, false)
            .setItemInputs(inputItems)
            .setMaxParallel(Integer.MAX_VALUE / 50)
            .setAvailableEUt(TierEU.MAX * 16)
            .enableBatchMode(128)
            .setConsumption(false)
            .build();
        assertEquals(Integer.MAX_VALUE, helperWithBatchMode.getCurrentParallel());
    }

    @Test
    void chanceMultiplier() {
        ParallelHelper helper = new ParallelHelper().setRecipe(rubberRecipe)
            .setMachine(machine, false, false)
            .setItemInputs(inputItems)
            .setMaxParallel(10)
            .setAvailableEUt(10)
            .setConsumption(false)
            .setOutputCalculation(true)
            .setChanceMultiplier(10)
            .build();

        int rubberDustAmount = helper.getItemOutputs()[0].stackSize;
        int rubberDustTinyAmount = helper.getItemOutputs()[1].stackSize;

        assertEquals(100, rubberDustAmount);
        assertTrue(rubberDustTinyAmount >= 60 && rubberDustTinyAmount <= 70);
    }

}
