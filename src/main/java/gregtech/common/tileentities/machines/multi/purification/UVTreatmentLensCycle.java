package gregtech.common.tileentities.machines.multi.purification;

import java.util.List;

import net.minecraft.item.ItemStack;

public class UVTreatmentLensCycle {

    private final List<ItemStack> lenses;

    private int currentLens = 0;

    public UVTreatmentLensCycle(List<ItemStack> lenses) {
        this.lenses = lenses;
        if (lenses.isEmpty()) {
            throw new IllegalArgumentException("Supplied lens array may not be empty");
        }
    }

    public ItemStack current() {
        return lenses.get(currentLens);
    }

    public boolean advance() {
        if (currentLens < lenses.size() - 1) {
            currentLens = currentLens + 1;
            return true;
        }
        return false;
    }

    public void reset() {
        currentLens = 0;
    }

    public ItemStack first() {
        return lenses.get(0);
    }
}
