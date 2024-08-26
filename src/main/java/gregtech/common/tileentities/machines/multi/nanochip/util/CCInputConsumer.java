package gregtech.common.tileentities.machines.multi.nanochip.util;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.util.GT_ParallelHelper;
import gregtech.api.util.GT_Recipe;
import gregtech.common.tileentities.machines.multi.nanochip.GT_MetaTileEntity_NanochipAssemblyModuleBase;
import gregtech.common.tileentities.machines.multi.nanochip.hatches.GT_MetaTileEntity_Hatch_VacuumConveyor_Input;

public class CCInputConsumer implements GT_ParallelHelper.InputConsumer {

    private final VacuumConveyorHatchMap<GT_MetaTileEntity_Hatch_VacuumConveyor_Input> inputConveyors;
    private final GT_MetaTileEntity_NanochipAssemblyModuleBase<?> module;

    public CCInputConsumer(VacuumConveyorHatchMap<GT_MetaTileEntity_Hatch_VacuumConveyor_Input> inputConveyors,
        GT_MetaTileEntity_NanochipAssemblyModuleBase<?> module) {
        this.inputConveyors = inputConveyors;
        this.module = module;
    }

    @Override
    public void consume(GT_Recipe recipe, int amountMultiplier, FluidStack[] aFluidInputs, ItemStack[] aInputs) {
        // Note that the aInputs[] parameter can be ignored, since this is what the multiblock contains.
        // We don't care about this, we just want to consume whatever we can from the input conveyor hatches
        for (ItemStack input : recipe.mInputs) {
            // Construct a modifiable stack that tracks how much of this item we need to consume
            ItemStack toConsumeStack = input.copy();
            toConsumeStack.stackSize *= amountMultiplier;

            for (ArrayList<GT_MetaTileEntity_Hatch_VacuumConveyor_Input> hatchList : inputConveyors.allHatches()) {
                boolean done = false;
                for (GT_MetaTileEntity_Hatch_VacuumConveyor_Input conveyor : hatchList) {
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
        for (FluidStack fluid : aFluidInputs) {
            module.depleteInput(fluid);
        }
    }
}
