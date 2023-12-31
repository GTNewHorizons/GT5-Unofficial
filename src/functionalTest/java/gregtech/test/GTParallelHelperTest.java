package gregtech.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_ParallelHelper;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.test.mock.MockIVoidableMachine;

public class GTParallelHelperTest {

    static GT_Recipe rubberRecipe;
    static ItemStack[] inputItems;
    static MockIVoidableMachine machine;

    @BeforeAll
    static void setup() {
        machine = new MockIVoidableMachine();
        ItemStack rubberDust = Materials.RawRubber.getDust(1);
        ItemStack sulfurDust = Materials.Sulfur.getDust(1);
        rubberRecipe = new GT_Recipe(
            new ItemStack[] { rubberDust.copy(), sulfurDust.copy() },
            null,
            null,
            null,
            null,
            new FluidStack[] { Materials.Rubber.getMolten(1000) },
            1,
            1,
            0);

        inputItems = new ItemStack[] { GT_Utility.copyAmountUnsafe(Integer.MAX_VALUE, rubberDust),
            GT_Utility.copyAmountUnsafe(Integer.MAX_VALUE, rubberDust),
            GT_Utility.copyAmountUnsafe(Integer.MAX_VALUE, sulfurDust),
            GT_Utility.copyAmountUnsafe(Integer.MAX_VALUE, sulfurDust) };
    }

    @Test
    void OutputsIntegerOverflow() {
        GT_ParallelHelper helper = new GT_ParallelHelper().setRecipe(rubberRecipe)
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
        GT_ParallelHelper helperWithoutBatchMode = new GT_ParallelHelper().setRecipe(rubberRecipe)
            .setMachine(machine, false, false)
            .setItemInputs(inputItems)
            .setMaxParallel(Integer.MAX_VALUE)
            .setAvailableEUt(TierEU.MAX * 16)
            .setConsumption(false)
            .build();
        assertEquals(Integer.MAX_VALUE, helperWithoutBatchMode.getCurrentParallel());

        // With batch mode
        GT_ParallelHelper helperWithBatchMode = new GT_ParallelHelper().setRecipe(rubberRecipe)
            .setMachine(machine, false, false)
            .setItemInputs(inputItems)
            .setMaxParallel(Integer.MAX_VALUE / 50)
            .setAvailableEUt(TierEU.MAX * 16)
            .enableBatchMode(128)
            .setConsumption(false)
            .build();
        assertEquals(Integer.MAX_VALUE, helperWithBatchMode.getCurrentParallel());
    }
}
