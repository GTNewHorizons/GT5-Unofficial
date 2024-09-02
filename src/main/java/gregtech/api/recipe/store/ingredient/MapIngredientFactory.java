package gregtech.api.recipe.store.ingredient;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import gregtech.api.util.item.ItemHolder;

public final class MapIngredientFactory {

    private MapIngredientFactory() {}

    public static @NotNull AbstractMapIngredient from(@NotNull ItemStack stack) {
        if (stack.hasTagCompound()) {
            return new MapItemStackNBTIngredient(stack);
        }
        return new MapItemStackIngredient(stack);
    }

    public static @NotNull AbstractMapIngredient from(@NotNull ItemHolder holder) {
        if (holder.getNBT() != null) {
            return new MapItemStackNBTIngredient(holder);
        }
        return new MapItemStackIngredient(holder);
    }

    public static @NotNull AbstractMapIngredient from(@NotNull String oreDict) {
        return new MapOreDictIngredient(oreDict);
    }

    public static @NotNull AbstractMapIngredient from(@NotNull Object object) {
        if (object instanceof ItemStack stack) {
            return from(stack);
        }
        if (object instanceof ItemHolder holder) {
            return from(holder);
        }
        if (object instanceof String string) {
            return from(string);
        }
        throw new IllegalArgumentException("Ingredient " + object + " is of unknown type");
    }
}
