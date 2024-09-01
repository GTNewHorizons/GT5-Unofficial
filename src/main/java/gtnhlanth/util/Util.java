package gtnhlanth.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizons.modularui.api.math.Pos2d;

public class Util {

    public static void depleteDurabilityOfStack(ItemStack stack, int damage) {

        if (stack == null) return;

        if (stack.stackSize == 0) // Might happen, who knows
            return;

        if (damage + stack.getItemDamage() > stack.getMaxDamage()) {
            stack.stackSize--;
        } else {

            stack.setItemDamage(stack.getItemDamage() + damage);

        }

    }

    public static List<Pos2d> getGridPositions(int totalCount, int xOrigin, int yOrigin, int xDirMaxCount,
        int yDirMaxCount, int distanceBetweenSlots) {
        // 18 pixels to get to a new grid for placing an item tile since they are 16x16 and have 1 pixel buffers
        // around them.

        int distance = 18 + distanceBetweenSlots;

        List<Pos2d> results = new ArrayList<>();
        int count = 0;
        loop: for (int j = 0; j < yDirMaxCount; j++) {
            for (int i = 0; i < xDirMaxCount; i++) {
                if (count >= totalCount) break loop;
                results.add(new Pos2d(xOrigin + i * distance, yOrigin + j * distance));
                count++;
            }
        }
        return results;
    }

    public static boolean coolantFluidCheck(FluidStack inStack, int fluidToConsume) {
        return (inStack.amount < fluidToConsume
            || (!inStack.isFluidEqual(FluidRegistry.getFluidStack("ic2coolant", 1)) && inStack.getFluid()
                .getTemperature() > 200));
    }
}
