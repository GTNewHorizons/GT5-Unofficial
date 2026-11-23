package gregtech.nei.transfer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiPredicate;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import gregtech.nei.transfer.RecipeTransferRequest.Ingredient;
import gregtech.nei.transfer.RecipeTransferRequest.Mode;

/**
 * Works out how a recipe transfer changes the machine and the player inventory.
 * <p>
 * It only works on copies and returns the complete new contents of both, so the caller applies the result as a whole
 * or drops it. Rules:
 * <ul>
 * <li>Items in the input slots that belong to the recipe stay and count towards the crafts. Other items go back to
 * the player.</li>
 * <li>How many crafts go in depends on the {@link Mode}. Partial crafts in the machine are completed first.</li>
 * <li>Ingredients come from the machine output slots first, so a machine can go on with what it just made, then from
 * the player inventory. What is left in the output slots goes to the player, as far as there is room.</li>
 * <li>Items that are not consumed are put in once, unless the machine already has one.</li>
 * <li>The recipe fluid comes from the player's fluid containers, the emptiest first, and only as much as needed.
 * Containers that cannot be partly drained are used whole, if the tank has room for the rest. The fluid in the tank
 * counts towards the crafts like the items, and a tank holding another fluid stops the transfer.</li>
 * <li>If not even one craft can be added, nothing changes.</li>
 * </ul>
 */
final class RecipeTransferPlanner {

    /** How much of a stack an input slot holds: 0 if the slot does not accept it. */
    @FunctionalInterface
    interface SlotCapacity {

        int of(int slot, ItemStack stack);
    }

    /** Reads and drains the player's fluid containers, for the recipe fluid only. */
    interface FluidContainers {

        /** Recipe fluid in one container of the stack, 0 if it holds none. */
        int amountIn(ItemStack container);

        /** One container of the stack after draining exactly that amount, or null if it cannot give that amount. */
        Drained drain(ItemStack container, int amount);

        /** @param remainder the drained container, null if it is used up */
        record Drained(ItemStack remainder) {}
    }

    /**
     * @param amount     recipe fluid already in the tank
     * @param otherFluid whether the tank holds another fluid
     */
    record Tank(int amount, boolean otherFluid, int capacity) {}

    /** @param perCraft recipe fluid used per craft */
    record RecipeFluid(int perCraft, Tank tank, FluidContainers containers) {}

    /**
     * @param inputs   current contents of the input slots
     * @param capacity limits of the input slots
     * @param outputs  current contents of the output slots
     */
    record Machine(ItemStack[] inputs, SlotCapacity capacity, ItemStack[] outputs) {}

    enum Outcome {
        /** The machine gets at least one more craft. */
        TRANSFERRED,
        /** Not even one more craft fits or is available. */
        NOTHING_TO_ADD,
        /** The items that are not part of the recipe do not fit back in the inventory. */
        NO_ROOM_FOR_RETURNED_ITEMS,
        /** The tank holds another fluid. */
        OTHER_FLUID_IN_TANK
    }

    /**
     * @param inputs     new contents of the input slots, null unless transferred
     * @param outputs    new contents of the output slots, null unless transferred
     * @param inventory  new contents of the player inventory, null unless transferred
     * @param tankAmount recipe fluid in the tank afterwards
     * @param crafts     complete crafts in the input slots afterwards
     */
    record Result(Outcome outcome, ItemStack[] inputs, ItemStack[] outputs, ItemStack[] inventory, int tankAmount,
        int crafts) {

        static Result failed(Outcome outcome) {
            return new Result(outcome, null, null, null, 0, 0);
        }
    }

    private final List<Ingredient> ingredients;
    private final RecipeFluid fluid;
    private final Machine machine;
    private final ItemStack[] inventory;
    private final BiPredicate<ItemStack, ItemStack> accepts;

    /** For each ingredient, the matching stacks taken out of the input slots. */
    private final List<List<ItemStack>> inMachine = new ArrayList<>();
    /** Input stacks that match no ingredient. */
    private final List<ItemStack> foreign = new ArrayList<>();

    /**
     * @param fluid     the recipe fluid, null if the recipe has none or the machine has no tank
     * @param inventory current contents of the player inventory, in the order items are taken from it
     * @param accepts   whether an ingredient alternative (first) accepts a stack (second)
     */
    RecipeTransferPlanner(List<Ingredient> ingredients, RecipeFluid fluid, Machine machine, ItemStack[] inventory,
        BiPredicate<ItemStack, ItemStack> accepts) {
        this.ingredients = merge(ingredients);
        this.fluid = fluid;
        this.machine = machine;
        this.inventory = inventory;
        this.accepts = accepts;

        for (int i = 0; i < ingredients.size(); i++) inMachine.add(new ArrayList<>());
        for (ItemStack stack : machine.inputs()) {
            if (stack == null) continue;
            int ingredient = findIngredient(stack);
            if (ingredient < 0) foreign.add(stack.copy());
            else inMachine.get(ingredient)
                .add(stack.copy());
        }
    }

    /**
     * Joins the ingredients that accept the same items, adding up their amounts. Recipes list more than a stack of an
     * item over several slots (104 as 64 + 40), and the items of that kind in the machine must count for all of them.
     */
    private static List<Ingredient> merge(List<Ingredient> ingredients) {
        List<Ingredient> merged = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            int same = -1;
            for (int i = 0; i < merged.size() && same < 0; i++) {
                if (sameAlternatives(merged.get(i), ingredient)) same = i;
            }
            if (same < 0) {
                merged.add(ingredient);
            } else {
                Ingredient first = merged.get(same);
                int amount = saturatedAdd(first.amount(), ingredient.amount());
                merged.set(same, new Ingredient(first.alternatives(), amount));
            }
        }
        return merged;
    }

    private static boolean sameAlternatives(Ingredient a, Ingredient b) {
        List<ItemStack> first = a.alternatives();
        List<ItemStack> second = b.alternatives();
        if (first.size() != second.size()) return false;
        for (ItemStack alternative : first) {
            if (findGroup(second, alternative) < 0) return false;
        }
        return true;
    }

    /** @param crafts crafts for {@link Mode#ADD} */
    Result plan(Mode mode, int crafts) {
        if (fluid != null && fluid.tank()
            .otherFluid()) return Result.failed(Outcome.OTHER_FLUID_IN_TANK);

        int current = currentCrafts();
        int wanted = switch (mode) {
            case FILL -> upperBound();
            case ADD -> saturatedAdd(current, crafts);
        };
        if (wanted <= current) return Result.failed(Outcome.NOTHING_TO_ADD);

        // The largest number of crafts in (current, wanted] that fits. All of them is tried first: with a full
        // inventory, an ingredient stack used up completely can free the slot a drained container needs, so fewer
        // crafts can fail where more fit. Below that, it assumes that if n crafts fit, fewer do too.
        Layout best = layout(wanted);
        int low = current + 1;
        int high = best == null ? wanted - 1 : low - 1;
        while (low <= high) {
            int middle = low + (high - low) / 2;
            Layout layout = layout(middle);
            if (layout != null) {
                best = layout;
                low = middle + 1;
            } else {
                high = middle - 1;
            }
        }
        if (best == null) return Result.failed(Outcome.NOTHING_TO_ADD);

        for (ItemStack stack : foreign) {
            if (!addToInventory(best.inventory, stack.copy())) return Result.failed(Outcome.NO_ROOM_FOR_RETURNED_ITEMS);
        }
        for (int i = 0; i < best.outputs.length; i++) {
            ItemStack stack = best.outputs[i];
            if (stack != null && addToInventory(best.inventory, stack)) best.outputs[i] = null;
        }
        return new Result(Outcome.TRANSFERRED, best.inputs, best.outputs, best.inventory, best.tankAmount, best.crafts);
    }

    /** Complete crafts in the input slots. Recipes that consume nothing count as one when complete. */
    private int currentCrafts() {
        int crafts = Integer.MAX_VALUE;
        for (int i = 0; i < ingredients.size(); i++) {
            int amount = ingredients.get(i)
                .amount();
            int present = count(inMachine.get(i));
            if (amount == 0) {
                if (present == 0) return 0;
            } else {
                crafts = Math.min(crafts, present / amount);
            }
        }
        if (fluid != null) crafts = Math.min(
            crafts,
            fluid.tank()
                .amount() / fluid.perCraft());
        return crafts == Integer.MAX_VALUE ? 1 : crafts;
    }

    /** No more crafts than the machine and the inventory hold together. */
    private int upperBound() {
        long bound = Integer.MAX_VALUE;
        boolean consumes = false;
        for (int i = 0; i < ingredients.size(); i++) {
            Ingredient ingredient = ingredients.get(i);
            if (!ingredient.isConsumed()) continue;
            consumes = true;
            long available = count(inMachine.get(i)) + count(machine.outputs(), ingredient)
                + count(inventory, ingredient);
            bound = Math.min(bound, available / ingredient.amount());
        }
        if (fluid != null) {
            consumes = true;
            Tank tank = fluid.tank();
            long available = tank.amount();
            for (ItemStack stack : inventory) {
                if (stack != null) available += (long) fluid.containers()
                    .amountIn(stack) * stack.stackSize;
            }
            bound = Math.min(bound, Math.min(available, tank.capacity()) / fluid.perCraft());
        }
        return consumes ? (int) bound : 1;
    }

    private record Layout(ItemStack[] inputs, ItemStack[] outputs, ItemStack[] inventory, int tankAmount, int crafts) {}

    /** The machine and inventory contents with the given number of crafts in the machine, or null if impossible. */
    private Layout layout(int crafts) {
        if (fluid != null && (long) fluid.perCraft() * crafts > fluid.tank()
            .capacity()) return null;

        ItemStack[] newOutputs = copy(machine.outputs());
        ItemStack[] newInventory = copy(inventory);
        List<ItemStack> groups = new ArrayList<>();

        for (int i = 0; i < ingredients.size(); i++) {
            Ingredient ingredient = ingredients.get(i);
            List<ItemStack> present = inMachine.get(i);
            List<ItemStack> ingredientGroups = new ArrayList<>();
            for (ItemStack stack : present) addToGroups(ingredientGroups, stack.copy());

            long missing = ingredient.isConsumed() ? (long) ingredient.amount() * crafts - count(present)
                : present.isEmpty() ? 1 : 0;
            if (missing > 0 && !take(List.of(newOutputs, newInventory), ingredient, missing, ingredientGroups)) {
                return null;
            }
            groups.addAll(ingredientGroups);
        }

        int tankAmount = 0;
        if (fluid != null) {
            Tank tank = fluid.tank();
            tankAmount = tank.amount();
            long missing = (long) fluid.perCraft() * crafts - tank.amount();
            if (missing > 0) {
                long poured = pour(newInventory, missing, tank.capacity() - tank.amount());
                if (poured < 0) return null;
                tankAmount += (int) poured;
            }
        }

        ItemStack[] newInputs = new ItemStack[machine.inputs().length];
        SlotCapacity capacity = machine.capacity();
        int slot = 0;
        for (ItemStack group : groups) {
            int left = group.stackSize;
            while (left > 0) {
                while (slot < newInputs.length && capacity.of(slot, group) <= 0) slot++;
                if (slot >= newInputs.length) return null;
                int amount = Math.min(left, capacity.of(slot, group));
                newInputs[slot++] = copyWithSize(group, amount);
                left -= amount;
            }
        }
        return new Layout(newInputs, newOutputs, newInventory, tankAmount, crafts);
    }

    /**
     * Takes items for an ingredient out of the sources, in order. Prefers the kinds already in the machine, then the
     * ones that match an alternative exactly (so a plain item goes before the same item with NBT, like a filled cell or
     * a renamed item), then the kinds there are most of, so the recipe uses as few slots as possible.
     */
    private boolean take(List<ItemStack[]> sources, Ingredient ingredient, long amount, List<ItemStack> groups) {
        List<ItemStack> kinds = new ArrayList<>();
        for (ItemStack[] source : sources) {
            for (ItemStack stack : source) {
                if (stack != null && matches(ingredient, stack)) addToGroups(kinds, stack.copy());
            }
        }
        kinds.sort(
            Comparator.comparing((ItemStack kind) -> findGroup(groups, kind) < 0)
                .thenComparing(kind -> !matchesExactly(ingredient, kind))
                .thenComparing(kind -> -kind.stackSize));

        for (ItemStack kind : kinds) {
            for (ItemStack[] source : sources) {
                for (int i = 0; i < source.length && amount > 0; i++) {
                    ItemStack stack = source[i];
                    if (stack == null || !sameKind(stack, kind)) continue;
                    int taken = (int) Math.min(amount, stack.stackSize);
                    addToGroups(groups, copyWithSize(stack, taken));
                    stack.stackSize -= taken;
                    if (stack.stackSize <= 0) source[i] = null;
                    amount -= taken;
                }
            }
        }
        return amount <= 0;
    }

    /**
     * Drains the recipe fluid out of the inventory containers, the emptiest first, and puts the drained containers
     * back. Returns how much was poured, which can be more than needed but never more than the room in the tank, or
     * -1 if there is not enough fluid or no room in the inventory for a drained container.
     */
    private long pour(ItemStack[] from, long missing, long room) {
        FluidContainers containers = fluid.containers();
        List<Integer> slots = new ArrayList<>();
        for (int i = 0; i < from.length; i++) {
            if (from[i] != null && containers.amountIn(from[i]) > 0) slots.add(i);
        }
        slots.sort(Comparator.comparingInt(i -> containers.amountIn(from[i])));

        long poured = 0;
        for (int slot : slots) {
            while (missing > 0 && from[slot] != null) {
                ItemStack container = copyWithSize(from[slot], 1);
                int content = containers.amountIn(container);
                if (content <= 0) break;

                int amount = (int) Math.min(missing, content);
                FluidContainers.Drained drained = containers.drain(container, amount);
                if (drained == null) {
                    // Cannot be partly drained: use it whole if the tank has room for the rest
                    if (poured + content > room) break;
                    amount = content;
                    drained = containers.drain(container, amount);
                    if (drained == null) break;
                }

                if (from[slot].stackSize == 1) {
                    from[slot] = drained.remainder();
                } else {
                    from[slot].stackSize--;
                    if (drained.remainder() != null && !addToInventory(
                        from,
                        drained.remainder()
                            .copy()))
                        return -1;
                }
                poured += amount;
                missing -= amount;
            }
            if (missing <= 0) break;
        }
        return missing > 0 ? -1 : poured;
    }

    /**
     * Adds the stack to the inventory, merging it first. Returns false, with the rest left in the stack, if it does not
     * all fit.
     */
    private static boolean addToInventory(ItemStack[] inventory, ItemStack stack) {
        for (ItemStack slot : inventory) {
            if (slot == null || !sameKind(slot, stack)) continue;
            int moved = Math.min(stack.stackSize, slot.getMaxStackSize() - slot.stackSize);
            slot.stackSize += moved;
            stack.stackSize -= moved;
            if (stack.stackSize <= 0) return true;
        }
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] != null) continue;
            int moved = Math.min(stack.stackSize, stack.getMaxStackSize());
            inventory[i] = copyWithSize(stack, moved);
            stack.stackSize -= moved;
            if (stack.stackSize <= 0) return true;
        }
        return false;
    }

    private int findIngredient(ItemStack stack) {
        for (int i = 0; i < ingredients.size(); i++) {
            if (matches(ingredients.get(i), stack)) return i;
        }
        return -1;
    }

    private static boolean matchesExactly(Ingredient ingredient, ItemStack stack) {
        for (ItemStack alternative : ingredient.alternatives()) {
            if (alternative.getItem() == stack.getItem()
                && (alternative.getItemDamage() == stack.getItemDamage()
                    || alternative.getItemDamage() == OreDictionary.WILDCARD_VALUE)
                && ItemStack.areItemStackTagsEqual(alternative, stack)) return true;
        }
        return false;
    }

    private boolean matches(Ingredient ingredient, ItemStack stack) {
        for (ItemStack alternative : ingredient.alternatives()) {
            if (accepts.test(alternative, stack)) return true;
        }
        return false;
    }

    /** Groups stacks of the same kind into one stack whose size can go beyond the max stack size. */
    private static void addToGroups(List<ItemStack> groups, ItemStack stack) {
        int group = findGroup(groups, stack);
        if (group < 0) groups.add(stack);
        else groups.get(group).stackSize += stack.stackSize;
    }

    private static int findGroup(List<ItemStack> groups, ItemStack stack) {
        for (int i = 0; i < groups.size(); i++) {
            if (sameKind(groups.get(i), stack)) return i;
        }
        return -1;
    }

    /** Whether two stacks can be in the same slot. */
    private static boolean sameKind(ItemStack a, ItemStack b) {
        return a.getItem() == b.getItem() && a.getItemDamage() == b.getItemDamage()
            && ItemStack.areItemStackTagsEqual(a, b);
    }

    private static int count(List<ItemStack> stacks) {
        int count = 0;
        for (ItemStack stack : stacks) count += stack.stackSize;
        return count;
    }

    private long count(ItemStack[] stacks, Ingredient ingredient) {
        long count = 0;
        for (ItemStack stack : stacks) {
            if (stack != null && matches(ingredient, stack)) count += stack.stackSize;
        }
        return count;
    }

    private static int saturatedAdd(int a, int b) {
        long sum = (long) a + b;
        return sum > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) sum;
    }

    private static ItemStack copyWithSize(ItemStack stack, int size) {
        ItemStack copy = stack.copy();
        copy.stackSize = size;
        return copy;
    }

    private static ItemStack[] copy(ItemStack[] stacks) {
        ItemStack[] copy = new ItemStack[stacks.length];
        for (int i = 0; i < stacks.length; i++) {
            if (stacks[i] != null) copy[i] = stacks[i].copy();
        }
        return copy;
    }
}
