package bartworks.API.recipe;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

import bartworks.util.BWUtil;

public class BWNBTDependantCraftingRecipe implements IRecipe {

    ItemStack result;
    Map<Character, ItemStack> charToStackMap = new HashMap<>(9, 1);
    String[] shape;

    @SuppressWarnings({ "SuspiciousSystemArraycopy" })
    public BWNBTDependantCraftingRecipe(ItemStack result, Object... recipe) {
        this.result = result;
        this.shape = new String[3];
        System.arraycopy(recipe, 0, this.shape, 0, 3);
        this.charToStackMap.put(' ', null);
        for (int i = 3; i < recipe.length; i += 2) {
            this.charToStackMap.put((char) recipe[i], ((ItemStack) recipe[i + 1]).copy());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BWNBTDependantCraftingRecipe that)) return false;

        if (!Objects.equals(this.result, that.result) || !Objects.equals(this.charToStackMap, that.charToStackMap))
            return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(this.shape, that.shape);
    }

    @Override
    public int hashCode() {
        int result1 = this.result != null ? this.result.hashCode() : 0;
        result1 = 31 * result1 + (this.charToStackMap != null ? this.charToStackMap.hashCode() : 0);
        return 31 * result1 + Arrays.hashCode(this.shape);
    }

    @Override
    public boolean matches(InventoryCrafting p_77569_1_, World p_77569_2_) {
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                ItemStack toCheck = p_77569_1_.getStackInRowAndColumn(y, x);
                ItemStack ref = this.charToStackMap.get(this.shape[x].toCharArray()[y]);
                if (!BWUtil.areStacksEqualOrNull(toCheck, ref)) return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting p_77572_1_) {
        return this.result.copy();
    }

    @Override
    public int getRecipeSize() {
        return 10;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.result;
    }
}
