package gregtech.api.recipe.lookup;

import java.util.Objects;
import java.util.function.Consumer;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public final class GTItemStackLookupIngredient extends GTRecipeLookupIngredient {

    private final Item item;
    private final int damage;

    private GTItemStackLookupIngredient(ItemStack stack, boolean wildcardDamage) {
        this(
            Objects.requireNonNull(stack, "stack")
                .getItem(),
            normalizeDamage(stack, wildcardDamage));
    }

    private GTItemStackLookupIngredient(Item item, int damage) {
        super(hash(item, damage));
        this.item = Objects.requireNonNull(item, "item");
        this.damage = damage;
    }

    public static GTItemStackLookupIngredient fromRecipe(ItemStack stack) {
        return fromRecipe(stack, false);
    }

    public static GTItemStackLookupIngredient fromRecipe(ItemStack stack, boolean recipeNbtSensitive) {
        return new GTItemStackLookupIngredient(stack, isWildcard(stack));
    }

    public static GTItemStackLookupIngredient fromNbtSensitiveRecipe(ItemStack stack) {
        return fromRecipe(stack, true);
    }

    public static void fromRuntime(Consumer<? super GTItemStackLookupIngredient> ingredients, ItemStack stack) {
        ingredients.accept(new GTItemStackLookupIngredient(stack, false));
        if (stack.getItemDamage() != OreDictionary.WILDCARD_VALUE) {
            ingredients.accept(new GTItemStackLookupIngredient(stack, true));
        }
    }

    private static GTItemStackLookupIngredient fromRuntime(ItemStack stack, boolean recipeNbtSensitive) {
        return new GTItemStackLookupIngredient(stack, recipeNbtSensitive);
    }

    public static GTItemStackLookupIngredient fromRuntime(ItemStack stack) {
        return fromRuntime(stack, false);
    }

    public static GTItemStackLookupIngredient fromRuntimeWildcard(ItemStack stack) {
        return fromRuntime(stack, true);
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
        return false;
    }

    @Override
    protected boolean equalsSameClass(GTRecipeLookupIngredient other) {
        GTItemStackLookupIngredient otherItem = (GTItemStackLookupIngredient) other;
        return item == otherItem.item && damage == otherItem.damage;
    }

    private static boolean isWildcard(ItemStack stack) {
        return Objects.requireNonNull(stack, "stack")
            .getItemDamage() == OreDictionary.WILDCARD_VALUE;
    }

    private static int normalizeDamage(ItemStack stack, boolean wildcardDamage) {
        if (wildcardDamage) return OreDictionary.WILDCARD_VALUE;
        return stack.getItemDamage();
    }

    private static int hash(Item item, int damage) {
        int result = System.identityHashCode(item);
        result = 31 * result + damage;
        return result;
    }
}
