package gregtech.api.recipe.lookup;

import java.util.Objects;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public final class GTItemStackLookupIngredient extends GTRecipeLookupIngredient {

    private final Item item;
    private final int damage;
    private final boolean nbtSensitive;

    private GTItemStackLookupIngredient(ItemStack stack, boolean wildcardDamage, boolean nbtSensitive) {
        this(
            Objects.requireNonNull(stack, "stack")
                .getItem(),
            normalizeDamage(stack, wildcardDamage),
            nbtSensitive);
    }

    private GTItemStackLookupIngredient(Item item, int damage, boolean nbtSensitive) {
        super(hash(item, damage, nbtSensitive));
        this.item = Objects.requireNonNull(item, "item");
        this.damage = damage;
        this.nbtSensitive = nbtSensitive;
    }

    public static GTItemStackLookupIngredient fromRecipe(ItemStack stack) {
        return fromRecipe(stack, false);
    }

    public static GTItemStackLookupIngredient fromRecipe(ItemStack stack, boolean recipeNbtSensitive) {
        return new GTItemStackLookupIngredient(stack, isWildcard(stack), recipeNbtSensitive && stack.hasTagCompound());
    }

    public static GTItemStackLookupIngredient fromRuntime(ItemStack stack) {
        return new GTItemStackLookupIngredient(stack, false, false);
    }

    public static GTItemStackLookupIngredient fromRuntimeWildcard(ItemStack stack) {
        return new GTItemStackLookupIngredient(stack, true, false);
    }

    public static GTItemStackLookupIngredient fromNbtSensitiveRecipe(ItemStack stack) {
        return fromRecipe(stack, true);
    }

    public Item getItem() {
        return item;
    }

    public int getDamage() {
        return damage;
    }

    public boolean isWildcard() {
        return damage == OreDictionary.WILDCARD_VALUE;
    }

    public boolean isNbtSensitive() {
        return nbtSensitive;
    }

    @Override
    protected boolean equalsSameClass(GTRecipeLookupIngredient other) {
        GTItemStackLookupIngredient otherItem = (GTItemStackLookupIngredient) other;
        return item == otherItem.item && damage == otherItem.damage && nbtSensitive == otherItem.nbtSensitive;
    }

    private static boolean isWildcard(ItemStack stack) {
        return Objects.requireNonNull(stack, "stack")
            .getItemDamage() == OreDictionary.WILDCARD_VALUE;
    }

    private static int normalizeDamage(ItemStack stack, boolean wildcardDamage) {
        if (wildcardDamage) return OreDictionary.WILDCARD_VALUE;
        return stack.getItemDamage();
    }

    private static int hash(Item item, int damage, boolean nbtSensitive) {
        int result = System.identityHashCode(item);
        result = 31 * result + damage;
        result = 31 * result + (nbtSensitive ? 1 : 0);
        return result;
    }
}
