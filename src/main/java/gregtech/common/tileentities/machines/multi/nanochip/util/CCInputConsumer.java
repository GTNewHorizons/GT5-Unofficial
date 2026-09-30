package gregtech.common.tileentities.machines.multi.nanochip.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.util.GTRecipe;
import gregtech.api.util.ParallelHelper;
import gregtech.common.tileentities.machines.multi.nanochip.hatches.MTEHatchVacuumConveyorInput;

public class CCInputConsumer implements ParallelHelper.InputConsumer {

    private final VacuumConveyorHatchMap<MTEHatchVacuumConveyorInput> inputConveyors;
    private final byte color;

    public CCInputConsumer(VacuumConveyorHatchMap<MTEHatchVacuumConveyorInput> inputConveyors) {
        this.inputConveyors = inputConveyors;
        this.color = -1;
    }

    public CCInputConsumer(VacuumConveyorHatchMap<MTEHatchVacuumConveyorInput> inputConveyors, byte color) {
        this.inputConveyors = inputConveyors;
        this.color = color;
    }

    @Override
    public void consume(GTRecipe recipe, int amountMultiplier, FluidStack[] aFluidInputs, ItemStack[] aInputs) {
        // Note that the aInputs[] parameter can be ignored, since this is what the multiblock contains.
        // We don't care about this, we just want to consume whatever we can from the input conveyor hatches
        for (ItemStack input : recipe.mInputs) {
            // Construct a modifiable stack that tracks how much of this item we need to consume
            ItemStack toConsumeStack = input.copy();
            toConsumeStack.stackSize *= amountMultiplier;

            // no color provided, go through all hatches
            if (color == -1) {
                for (ArrayList<MTEHatchVacuumConveyorInput> hatchList : inputConveyors.allHatches()) {
                    if (consume(hatchList, toConsumeStack)) break;
                }
            } else {
                // otherwise go through only hatches of the specified color
                consume(inputConveyors.findColoredHatches(color), toConsumeStack);
            }
        }

        // Consume fluid inputs in recipe
        recipe.consumeInput(amountMultiplier, aFluidInputs);
    }

    // Returns true if the stack was fully consumed
    private boolean consume(List<MTEHatchVacuumConveyorInput> hatchList, ItemStack toConsumeStack) {
        for (MTEHatchVacuumConveyorInput conveyor : hatchList) {
            int consumed = conveyor.tryConsume(toConsumeStack, false);
            toConsumeStack.stackSize -= consumed;
            if (toConsumeStack.stackSize <= 0) {
                // Break out of both loops... I hate this
                // Labeled loops when!
                return true;
            }
        }
        return toConsumeStack.stackSize <= 0;
    }
}
