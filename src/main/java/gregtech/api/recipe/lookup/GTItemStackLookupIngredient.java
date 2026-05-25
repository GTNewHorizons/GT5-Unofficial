package gregtech.api.recipe.lookup;

import java.util.Objects;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public final class GTItemStackLookupIngredient extends GTRecipeLookupIngredient {

    private final Item item;
    private final int damage;
    private final boolean specialIngredient;

    private GTItemStackLookupIngredient(ItemStack stack, boolean wildcardDamage, boolean specialIngredient) {
        this(
            Objects.requireNonNull(stack, "stack")
                .getItem(),
            normalizeDamage(stack, wildcardDamage),
            specialIngredient);
    }

    private GTItemStackLookupIngredient(Item item, int damage, boolean specialIngredient) {
        super(hash(item, damage, specialIngredient));
        this.item = Objects.requireNonNull(item, "item");
        this.damage = damage;
        this.specialIngredient = specialIngredient;
    }

    public static GTItemStackLookupIngredient fromRecipe(ItemStack stack) {
        return new GTItemStackLookupIngredient(stack, isWildcard(stack), false);
    }

    public static GTItemStackLookupIngredient fromRuntime(ItemStack stack) {
        return new GTItemStackLookupIngredient(stack, false, false);
    }

    public static GTItemStackLookupIngredient fromRuntimeWildcard(ItemStack stack) {
        return new GTItemStackLookupIngredient(stack, true, false);
    }

    public static GTItemStackLookupIngredient fromSpecialRecipe(ItemStack stack) {
        return new GTItemStackLookupIngredient(stack, isWildcard(stack), true);
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

    @Override
    public boolean isSpecialIngredient() {
        return specialIngredient;
    }

    @Override
    protected boolean equalsSameClass(GTRecipeLookupIngredient other) {
        GTItemStackLookupIngredient otherItem = (GTItemStackLookupIngredient) other;
        return item == otherItem.item && damage == otherItem.damage && specialIngredient == otherItem.specialIngredient;
    }

    private static boolean isWildcard(ItemStack stack) {
        return Objects.requireNonNull(stack, "stack")
            .getItemDamage() == OreDictionary.WILDCARD_VALUE;
    }

    private static int normalizeDamage(ItemStack stack, boolean wildcardDamage) {
        if (wildcardDamage) return OreDictionary.WILDCARD_VALUE;
        return stack.getItemDamage();
    }

    private static int hash(Item item, int damage, boolean specialIngredient) {
        int result = System.identityHashCode(item);
        result = 31 * result + damage;
        result = 31 * result + (specialIngredient ? 1 : 0);
        return result;
    }
}
