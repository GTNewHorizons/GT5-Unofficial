package gregtech.api.util.recipe;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.util.GT_Recipe;
import gregtech.api.util.item.ItemHolder;

public class RecipeInputRequirements {

    protected Map<ItemHolder, Long> itemInputs = new HashMap<>();
    protected Set<ItemHolder> itemInputsMet = new HashSet<>();
    protected boolean metAllItem = false;
    protected Map<Fluid, Long> fluidInputs = new HashMap<>();
    protected Set<Fluid> fluidInputsMet = new HashSet<>();
    protected boolean metAllFluid = false;

    public RecipeInputRequirements(@Nonnull GT_Recipe recipe) {
        this(recipe.mInputs, recipe.mFluidInputs);
    }

    public RecipeInputRequirements(@Nonnull ItemStack[] itemInputs, @Nonnull FluidStack[] fluidInputs) {
        for (ItemStack item : itemInputs) {
            if (item == null) continue;
            ItemHolder itemIH = new ItemHolder(item);
            this.itemInputs.put(itemIH, this.itemInputs.getOrDefault(itemIH, 0L) + item.stackSize);
        }

        for (FluidStack fluid : fluidInputs) {
            if (fluid == null) continue;
            this.fluidInputs.put(fluid.getFluid(), this.fluidInputs.getOrDefault(fluid.getFluid(), 0L) + fluid.amount);
        }
    }

    /**
     * 
     * @param itemInputs we have and want to fill this request
     * @return {@code true} when all item inputs are met
     */
    public boolean tryToFillItemRequirements(Map<ItemHolder, Long> itemInputs) {
        if (metAllItem) return metAllItem;
        for (Entry<ItemHolder, Long> entry : itemInputs.entrySet()) {
            if (itemInputsMet.contains(entry.getKey())) continue;
            if (!this.itemInputs.containsKey(entry.getKey())) continue;
            if (this.itemInputs.get(entry.getKey()) > entry.getValue()) continue;
            itemInputsMet.add(entry.getKey());
        }
        metAllItem = itemInputsMet.containsAll(this.itemInputs.keySet());
        return metAllItem;
    }

    /**
     * 
     * @param fluidInputs we have and want to fill this request
     * @return {@code true} when all fluid inputs are met
     */
    public boolean tryToFillFluidRequirements(Map<Fluid, Long> fluidInputs) {
        if (metAllFluid) return metAllFluid;
        for (Entry<Fluid, Long> entry : fluidInputs.entrySet()) {
            if (fluidInputsMet.contains(entry.getKey())) continue;
            if (!this.fluidInputs.containsKey(entry.getKey())) continue;
            if (this.fluidInputs.get(entry.getKey()) > entry.getValue()) continue;
            fluidInputsMet.add(entry.getKey());
        }
        metAllFluid = fluidInputsMet.containsAll(this.fluidInputs.keySet());
        return metAllFluid;
    }
}
