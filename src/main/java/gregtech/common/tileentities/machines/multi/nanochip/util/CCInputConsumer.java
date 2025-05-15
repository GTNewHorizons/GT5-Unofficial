package gregtech.common.tileentities.machines.multi.nanochip.util;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.util.GTRecipe;
import gregtech.api.util.ParallelHelper;
import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyModuleBase;
import gregtech.common.tileentities.machines.multi.nanochip.hatches.MTEHatchVacuumConveyorInput;

public class CCInputConsumer implements ParallelHelper.InputConsumer {

    private final VacuumConveyorHatchMap<MTEHatchVacuumConveyorInput> inputConveyors;
    private final MTENanochipAssemblyModuleBase<?> module;

    public CCInputConsumer(VacuumConveyorHatchMap<MTEHatchVacuumConveyorInput> inputConveyors,
        MTENanochipAssemblyModuleBase<?> module) {
        this.inputConveyors = inputConveyors;
        this.module = module;
    }

    @Override
    public void consume(GTRecipe recipe, int amountMultiplier, FluidStack[] aFluidInputs, ItemStack[] aInputs) {
        // Note that the aInputs[] parameter can be ignored, since this is what the multiblock contains.
        // We don't care about this, we just want to consume whatever we can from the input conveyor hatches
        for (ItemStack input : recipe.mInputs) {
            // Construct a modifiable stack that tracks how much of this item we need to consume
            ItemStack toConsumeStack = input.copy();
            toConsumeStack.stackSize *= amountMultiplier;

            for (ArrayList<MTEHatchVacuumConveyorInput> hatchList : inputConveyors.allHatches()) {
                boolean done = false;
                for (MTEHatchVacuumConveyorInput conveyor : hatchList) {
                    int consumed = conveyor.tryConsume(toConsumeStack);
                    toConsumeStack.stackSize -= consumed;
                    if (toConsumeStack.stackSize <= 0) {
                        // Break out of both loops... I hate this
                        // Labeled loops when!
                        done = true;
                        break;
                    }
                }
                if (done) break;
            }
        }

        // Consume fluid inputs in recipe
        for (FluidStack fluid : recipe.mFluidInputs) {
            FluidStack toConsume = fluid.copy();
            toConsume.amount *= amountMultiplier;
            module.depleteInput(toConsume);
        }
    }
}
