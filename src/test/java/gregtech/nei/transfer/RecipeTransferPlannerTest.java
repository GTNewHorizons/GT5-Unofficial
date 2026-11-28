package gregtech.nei.transfer;

import static gregtech.nei.transfer.RecipeTransferPlanner.Outcome.NOTHING_TO_ADD;
import static gregtech.nei.transfer.RecipeTransferPlanner.Outcome.NO_ROOM_FOR_RETURNED_ITEMS;
import static gregtech.nei.transfer.RecipeTransferPlanner.Outcome.OTHER_FLUID_IN_TANK;
import static gregtech.nei.transfer.RecipeTransferPlanner.Outcome.TRANSFERRED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.junit.jupiter.api.Test;

import gregtech.nei.transfer.RecipeTransferPlanner.FluidContainers;
import gregtech.nei.transfer.RecipeTransferPlanner.Machine;
import gregtech.nei.transfer.RecipeTransferPlanner.RecipeFluid;
import gregtech.nei.transfer.RecipeTransferPlanner.Result;
import gregtech.nei.transfer.RecipeTransferPlanner.SlotCapacity;
import gregtech.nei.transfer.RecipeTransferPlanner.Tank;
import gregtech.nei.transfer.RecipeTransferRequest.Ingredient;
import gregtech.nei.transfer.RecipeTransferRequest.Mode;

class RecipeTransferPlannerTest {

    private static final Item CHEST = item("chest");
    private static final Item DIRT = item("dirt");
    private static final Item PLANK = item("plank");
    private static final Item SHAPE = item("shape");
    private static final Item STICK = item("stick");
    private static final Item TOOL = item("tool").setMaxStackSize(1);
    /** Holds any amount of fluid, in its NBT. */
    private static final Item CELL = item("cell");
    /** Holds 1000 or nothing. */
    private static final Item CAN = item("can");
    private static final Item EMPTY_CAN = item("empty_can");

    private static final FluidContainers CONTAINERS = new FluidContainers() {

        @Override
        public int amountIn(ItemStack container) {
            if (container.getItem() == CAN) return 1000;
            if (container.getItem() == CELL && container.hasTagCompound()) return container.getTagCompound()
                .getInteger("amount");
            return 0;
        }

        @Override
        public Drained drain(ItemStack container, int amount) {
            int content = amountIn(container);
            if (amount <= 0 || amount > content) return null;
            if (container.getItem() == CAN) return amount == content ? new Drained(new ItemStack(EMPTY_CAN)) : null;
            return new Drained(cell(1, content - amount));
        }
    };

    private static final BiPredicate<ItemStack, ItemStack> SAME_ITEM = (alternative,
        stack) -> alternative.getItem() == stack.getItem() && alternative.getItemDamage() == stack.getItemDamage();
    private static final SlotCapacity STACK_LIMIT = (slot, stack) -> stack.getMaxStackSize();

    private static final List<Ingredient> CHEST_RECIPE = List
        .of(ingredient(1, new ItemStack(CHEST)), ingredient(8, new ItemStack(DIRT)));
    /** Recipes list more than a stack of an item over several slots, like 104 dirt as 64 + 40. */
    private static final List<Ingredient> SPLIT_RECIPE = List.of(
        ingredient(64, new ItemStack(DIRT)),
        ingredient(40, new ItemStack(DIRT)),
        ingredient(16, new ItemStack(PLANK)));

    @Test
    void fillsOneCraftWithOneSet() {
        Result result = plan(CHEST_RECIPE, 0, inputs(), inventory(stack(CHEST, 1), stack(DIRT, 8)));

        assertEquals(TRANSFERRED, result.outcome());
        assertEquals(1, result.crafts());
        assertStacks(result.inputs(), stack(CHEST, 1), stack(DIRT, 8));
        assertStacks(result.inventory());
    }

    @Test
    void fillsEveryCraftThatFits() {
        Result result = plan(CHEST_RECIPE, 0, inputs(), inventory(stack(CHEST, 2), stack(DIRT, 20)));

        assertEquals(2, result.crafts());
        assertStacks(result.inputs(), stack(CHEST, 2), stack(DIRT, 16));
        assertStacks(result.inventory(), stack(DIRT, 4));
    }

    @Test
    void addsOneCraftAtATime() {
        ItemStack[] inventory = inventory(stack(CHEST, 2), stack(DIRT, 16));

        Result first = plan(CHEST_RECIPE, 1, inputs(), inventory);
        assertEquals(1, first.crafts());
        assertStacks(first.inputs(), stack(CHEST, 1), stack(DIRT, 8));

        Result second = plan(CHEST_RECIPE, 1, first.inputs(), first.inventory());
        assertEquals(2, second.crafts());
        assertStacks(second.inputs(), stack(CHEST, 2), stack(DIRT, 16));
        assertStacks(second.inventory());
    }

    @Test
    void leftoversCountTowardsTheNextCraft() {
        Result result = plan(CHEST_RECIPE, 1, inputs(stack(DIRT, 5)), inventory(stack(CHEST, 1), stack(DIRT, 10)));

        assertEquals(1, result.crafts());
        assertStacks(result.inputs(), stack(CHEST, 1), stack(DIRT, 8));
        assertStacks(result.inventory(), stack(DIRT, 7));
    }

    @Test
    void addsAsManyAsPossibleUpToTheMultiplier() {
        Result result = plan(CHEST_RECIPE, 5, inputs(), inventory(stack(CHEST, 3), stack(DIRT, 64)));

        assertEquals(3, result.crafts());
    }

    @Test
    void changesNothingWhenACraftIsMissing() {
        Result result = plan(CHEST_RECIPE, 0, inputs(), inventory(stack(CHEST, 1), stack(DIRT, 7)));

        assertEquals(NOTHING_TO_ADD, result.outcome());
        assertNull(result.inputs());
    }

    @Test
    void fillsUpAPartlyFilledMachine() {
        Result result = plan(
            CHEST_RECIPE,
            0,
            inputs(stack(CHEST, 1), stack(DIRT, 5)),
            inventory(stack(CHEST, 2), stack(DIRT, 20)));

        assertEquals(3, result.crafts());
        assertStacks(result.inputs(), stack(CHEST, 3), stack(DIRT, 24));
        assertStacks(result.inventory(), stack(DIRT, 1));
    }

    @Test
    void addsAWholeCraftOfAnItemSplitOverSlots() {
        Result result = plan(
            SPLIT_RECIPE,
            1,
            inputs(stack(DIRT, 64), stack(DIRT, 40), stack(PLANK, 16)),
            inventory(stack(DIRT, 128), stack(PLANK, 32)));

        assertEquals(2, result.crafts());
        assertStacks(
            result.inputs(),
            stack(DIRT, 64),
            stack(DIRT, 64),
            stack(DIRT, 64),
            stack(DIRT, 16),
            stack(PLANK, 32));
        assertStacks(result.inventory(), stack(DIRT, 24), stack(PLANK, 16));
    }

    @Test
    void addsNothingWhenAnItemSplitOverSlotsIsShortOfACraft() {
        Result result = plan(
            SPLIT_RECIPE,
            0,
            inputs(stack(DIRT, 64), stack(DIRT, 40), stack(PLANK, 16)),
            inventory(stack(DIRT, 50)));

        assertEquals(NOTHING_TO_ADD, result.outcome());
    }

    @Test
    void changesNothingWhenTheMachineIsAlreadyFull() {
        Result result = plan(CHEST_RECIPE, 0, inputs(stack(CHEST, 1), stack(DIRT, 8)), inventory());

        assertEquals(NOTHING_TO_ADD, result.outcome());
    }

    @Test
    void returnsItemsThatAreNotInTheRecipe() {
        Result result = plan(CHEST_RECIPE, 0, inputs(stack(STICK, 3)), inventory(stack(CHEST, 1), stack(DIRT, 8)));

        assertStacks(result.inputs(), stack(CHEST, 1), stack(DIRT, 8));
        assertStacks(result.inventory(), stack(STICK, 3));
    }

    @Test
    void changesNothingWhenReturnedItemsDoNotFit() {
        ItemStack[] inventory = new ItemStack[] { stack(CHEST, 1), stack(DIRT, 8), stack(TOOL, 1) };
        ItemStack[] inputs = inputs(stack(STICK, 3), stack(PLANK, 5), stack(TOOL, 1), stack(TOOL, 1));

        Result result = plan(CHEST_RECIPE, 0, inputs, inventory);

        assertEquals(NO_ROOM_FOR_RETURNED_ITEMS, result.outcome());
        assertNull(result.inventory());
    }

    @Test
    void putsInOneShapeAndKeepsItAcrossCrafts() {
        List<Ingredient> recipe = List.of(ingredient(0, new ItemStack(SHAPE)), ingredient(2, new ItemStack(PLANK)));

        Result result = plan(recipe, 0, inputs(), inventory(stack(SHAPE, 2), stack(PLANK, 10)));

        assertEquals(5, result.crafts());
        assertStacks(result.inputs(), stack(SHAPE, 1), stack(PLANK, 10));
        assertStacks(result.inventory(), stack(SHAPE, 1));
    }

    @Test
    void doesNotAddAShapeWhenTheMachineHasOne() {
        List<Ingredient> recipe = List.of(ingredient(0, new ItemStack(SHAPE)), ingredient(2, new ItemStack(PLANK)));

        Result result = plan(recipe, 1, inputs(stack(SHAPE, 1)), inventory(stack(SHAPE, 1), stack(PLANK, 2)));

        assertStacks(result.inputs(), stack(SHAPE, 1), stack(PLANK, 2));
        assertStacks(result.inventory(), stack(SHAPE, 1));
    }

    @Test
    void changesNothingWithoutTheShape() {
        List<Ingredient> recipe = List.of(ingredient(0, new ItemStack(SHAPE)), ingredient(2, new ItemStack(PLANK)));

        Result result = plan(recipe, 0, inputs(), inventory(stack(PLANK, 10)));

        assertEquals(NOTHING_TO_ADD, result.outcome());
    }

    @Test
    void usesSeveralSlotsForLargeAmounts() {
        List<Ingredient> recipe = List.of(ingredient(16, new ItemStack(DIRT)));

        Result result = plan(recipe, 0, inputs(), inventory(stack(DIRT, 64), stack(DIRT, 64), stack(DIRT, 10)));

        assertEquals(8, result.crafts());
        assertStacks(result.inputs(), stack(DIRT, 64), stack(DIRT, 64));
        assertStacks(result.inventory(), stack(DIRT, 10));
    }

    @Test
    void stopsWhenTheSlotsAreFull() {
        List<Ingredient> recipe = List.of(ingredient(1, new ItemStack(TOOL)), ingredient(1, new ItemStack(DIRT)));
        ItemStack[] inventory = inventory(stack(TOOL, 1), stack(TOOL, 1), stack(TOOL, 1), stack(DIRT, 9));

        Result result = plan(recipe, 0, new ItemStack[2], inventory);

        assertEquals(1, result.crafts());
        assertStacks(result.inputs(), stack(TOOL, 1), stack(DIRT, 1));
    }

    @Test
    void respectsTheSlotLimits() {
        List<Ingredient> recipe = List.of(ingredient(1, new ItemStack(DIRT)));
        SlotCapacity sixteen = (slot, stack) -> 16;

        Result result = new RecipeTransferPlanner(
            recipe,
            null,
            new Machine(new ItemStack[2], sixteen, new ItemStack[0]),
            inventory(stack(DIRT, 64)),
            SAME_ITEM).plan(Mode.FILL, 0);

        assertEquals(32, result.crafts());
        assertStacks(result.inputs(), stack(DIRT, 16), stack(DIRT, 16));
    }

    @Test
    void mixesAlternativesWhenOneIsNotEnough() {
        List<Ingredient> recipe = List.of(ingredient(40, new ItemStack(PLANK, 1, 0), new ItemStack(PLANK, 1, 1)));

        Result result = plan(recipe, 1, inputs(), inventory(new ItemStack(PLANK, 30, 0), new ItemStack(PLANK, 20, 1)));

        assertEquals(1, result.crafts());
        assertStacks(result.inputs(), new ItemStack(PLANK, 30, 0), new ItemStack(PLANK, 10, 1));
    }

    @Test
    void prefersTheAlternativeAlreadyInTheMachine() {
        List<Ingredient> recipe = List.of(ingredient(4, new ItemStack(PLANK, 1, 0), new ItemStack(PLANK, 1, 1)));

        Result result = plan(
            recipe,
            1,
            inputs(new ItemStack(PLANK, 4, 1)),
            inventory(new ItemStack(PLANK, 60, 0), new ItemStack(PLANK, 8, 1)));

        assertEquals(2, result.crafts());
        assertStacks(result.inputs(), new ItemStack(PLANK, 8, 1));
    }

    @Test
    void takesFluidFromTheEmptiestCellFirst() {
        List<Ingredient> recipe = List.of(ingredient(1, new ItemStack(DIRT)));
        ItemStack[] inventory = inventory(stack(DIRT, 1), cell(1, 8000), cell(1, 5000));

        Result result = plan(recipe, fluid(12000, 0, 16000), 0, inputs(), inventory);

        assertEquals(1, result.crafts());
        assertEquals(12000, result.tankAmount());
        assertStacks(result.inventory(), cell(1, 1000), cell(1, 0));
    }

    @Test
    void takesOneCellOffAStack() {
        List<Ingredient> recipe = List.of(ingredient(1, new ItemStack(DIRT)));
        ItemStack[] inventory = inventory(stack(DIRT, 1), cell(3, 1000));

        Result result = plan(recipe, fluid(1500, 0, 16000), 0, inputs(), inventory);

        assertEquals(1500, result.tankAmount());
        assertStacks(result.inventory(), cell(1, 0), cell(1, 1000), cell(1, 500));
    }

    @Test
    void usesAWholeCanWhenTheTankHasRoom() {
        List<Ingredient> recipe = List.of(ingredient(1, new ItemStack(DIRT)));

        Result result = plan(recipe, fluid(500, 0, 16000), 0, inputs(), inventory(stack(DIRT, 1), stack(CAN, 2)));

        assertEquals(1000, result.tankAmount());
        assertStacks(result.inventory(), stack(EMPTY_CAN, 1), stack(CAN, 1));
    }

    @Test
    void changesNothingWhenAWholeCanDoesNotFit() {
        List<Ingredient> recipe = List.of(ingredient(1, new ItemStack(DIRT)));

        Result result = plan(recipe, fluid(500, 0, 800), 0, inputs(), inventory(stack(DIRT, 1), stack(CAN, 2)));

        assertEquals(NOTHING_TO_ADD, result.outcome());
    }

    @Test
    void changesNothingWhenTheTankHoldsAnotherFluid() {
        List<Ingredient> recipe = List.of(ingredient(1, new ItemStack(DIRT)));
        RecipeFluid otherFluid = new RecipeFluid(1000, new Tank(0, true, 16000), CONTAINERS);

        Result result = plan(recipe, otherFluid, 0, inputs(), inventory(stack(DIRT, 1), cell(1, 8000)));

        assertEquals(OTHER_FLUID_IN_TANK, result.outcome());
    }

    @Test
    void theFluidLimitsTheItems() {
        List<Ingredient> recipe = List.of(ingredient(1, new ItemStack(DIRT)));

        Result result = plan(recipe, fluid(1000, 0, 16000), 0, inputs(), inventory(stack(DIRT, 5), cell(1, 2500)));

        assertEquals(2, result.crafts());
        assertEquals(2000, result.tankAmount());
        assertStacks(result.inputs(), stack(DIRT, 2));
        assertStacks(result.inventory(), stack(DIRT, 3), cell(1, 500));
    }

    @Test
    void theTankLimitsTheCrafts() {
        List<Ingredient> recipe = List.of(ingredient(1, new ItemStack(DIRT)));

        Result result = plan(recipe, fluid(1000, 0, 3000), 0, inputs(), inventory(stack(DIRT, 10), cell(1, 8000)));

        assertEquals(3, result.crafts());
        assertEquals(3000, result.tankAmount());
        assertStacks(result.inventory(), stack(DIRT, 7), cell(1, 5000));
    }

    @Test
    void fluidInTheTankCountsTowardsTheCrafts() {
        List<Ingredient> recipe = List.of(ingredient(1, new ItemStack(DIRT)));

        Result result = plan(
            recipe,
            fluid(1000, 1000, 16000),
            1,
            inputs(stack(DIRT, 1)),
            inventory(stack(DIRT, 1), cell(1, 1000)));

        assertEquals(2, result.crafts());
        assertEquals(2000, result.tankAmount());
        assertStacks(result.inventory(), cell(1, 0));
    }

    @Test
    void neverPutsMoreFluidThanTheTankHolds() {
        List<Ingredient> recipe = List.of(ingredient(1, new ItemStack(DIRT)));

        Result result = plan(
            recipe,
            fluid(1000, 16000, 16000),
            1,
            inputs(stack(DIRT, 16)),
            inventory(stack(DIRT, 5), cell(1, 8000)));

        assertEquals(NOTHING_TO_ADD, result.outcome());
    }

    @Test
    void addsOnlyTheCraftsTheTankHasRoomFor() {
        List<Ingredient> recipe = List.of(ingredient(1, new ItemStack(DIRT)));

        Result result = plan(
            recipe,
            fluid(1000, 14000, 16000),
            5,
            inputs(stack(DIRT, 14)),
            inventory(stack(DIRT, 10), cell(1, 8000)));

        assertEquals(16, result.crafts());
        assertEquals(16000, result.tankAmount());
    }

    @Test
    void usesEverythingWhenThatFreesTheSlotForTheEmptyCans() {
        List<Ingredient> recipe = List.of(ingredient(1, new ItemStack(DIRT)));
        ItemStack[] inventory = new ItemStack[36];
        inventory[0] = stack(DIRT, 16);
        inventory[1] = stack(CAN, 64);
        for (int i = 2; i < inventory.length; i++) inventory[i] = stack(STICK, 64);

        Result result = plan(recipe, fluid(1000, 0, 64000), 0, inputs(), inventory);

        assertEquals(16, result.crafts());
        assertEquals(16000, result.tankAmount());
    }

    @Test
    void prefersThePlainItemToOneWithNbt() {
        List<Ingredient> recipe = List.of(ingredient(1, new ItemStack(CELL)));

        Result result = plan(recipe, 1, inputs(), inventory(cell(1, 1000), cell(1, 0)));

        assertStacks(result.inputs(), cell(1, 0));
        assertStacks(result.inventory(), cell(1, 1000));
    }

    @Test
    void fillsAMoldRecipeWithFluidOnly() {
        List<Ingredient> recipe = List.of(ingredient(0, new ItemStack(SHAPE)));

        Result result = plan(recipe, fluid(144, 0, 16000), 0, inputs(), inventory(stack(SHAPE, 1), cell(1, 1000)));

        assertEquals(6, result.crafts());
        assertEquals(864, result.tankAmount());
        assertStacks(result.inputs(), stack(SHAPE, 1));
        assertStacks(result.inventory(), cell(1, 136));
    }

    @Test
    void takesWhatTheMachineMadeBeforeTheInventory() {
        Result result = withOutputs(
            2,
            inputs(),
            new ItemStack[] { stack(CHEST, 2), null },
            inventory(stack(CHEST, 5), stack(DIRT, 16)));

        assertStacks(result.inputs(), stack(CHEST, 2), stack(DIRT, 16));
        assertStacks(result.outputs());
        assertStacks(result.inventory(), stack(CHEST, 5));
    }

    @Test
    void movesTheOtherOutputsToTheInventory() {
        Result result = withOutputs(
            1,
            inputs(),
            new ItemStack[] { stack(STICK, 4), stack(CHEST, 1) },
            inventory(stack(DIRT, 8)));

        assertStacks(result.inputs(), stack(CHEST, 1), stack(DIRT, 8));
        assertStacks(result.outputs());
        assertStacks(result.inventory(), stack(STICK, 4));
    }

    @Test
    void leavesTheOutputsThatDoNotFit() {
        ItemStack[] inventory = new ItemStack[] { stack(DIRT, 8), stack(TOOL, 1) };

        Result result = withOutputs(
            1,
            inputs(),
            new ItemStack[] { stack(STICK, 4), stack(CHEST, 1), stack(TOOL, 1) },
            inventory);

        assertEquals(TRANSFERRED, result.outcome());
        assertStacks(result.outputs(), stack(TOOL, 1));
        assertStacks(result.inventory(), stack(STICK, 4), stack(TOOL, 1));
    }

    @Test
    void countsWhatTheMachineMadeAsAvailable() {
        Result result = new RecipeTransferPlanner(
            CHEST_RECIPE,
            null,
            new Machine(inputs(), STACK_LIMIT, new ItemStack[] { stack(CHEST, 1) }),
            inventory(stack(DIRT, 16)),
            SAME_ITEM).plan(Mode.FILL, 0);

        assertEquals(1, result.crafts());
        assertStacks(result.outputs());
        assertStacks(result.inventory(), stack(DIRT, 8));
    }

    @Test
    void planningChangesNoneOfTheGivenStacks() {
        // The server passes the stacks of the machine and the player themselves, so a plan must work on copies
        List<Ingredient> recipe = List.of(ingredient(2, new ItemStack(DIRT)));
        ItemStack[] inputs = inputs(stack(STICK, 3), stack(DIRT, 1));
        ItemStack[] outputs = new ItemStack[] { stack(DIRT, 2), stack(PLANK, 4) };
        ItemStack[] inventory = inventory(stack(DIRT, 5), cell(2, 1500), stack(CAN, 1));
        List<String> before = snapshot(inputs, outputs, inventory);

        Result result = new RecipeTransferPlanner(
            recipe,
            fluid(1000, 0, 16000),
            new Machine(inputs, STACK_LIMIT, outputs),
            inventory,
            SAME_ITEM).plan(Mode.FILL, 0);

        assertEquals(4, result.crafts());
        assertEquals(before, snapshot(inputs, outputs, inventory));
    }

    private static Result withOutputs(int crafts, ItemStack[] inputs, ItemStack[] outputs, ItemStack[] inventory) {
        return new RecipeTransferPlanner(
            CHEST_RECIPE,
            null,
            new Machine(inputs, STACK_LIMIT, outputs),
            inventory,
            SAME_ITEM).plan(Mode.ADD, crafts);
    }

    private static Result plan(List<Ingredient> recipe, RecipeFluid fluid, int multiplier, ItemStack[] inputs,
        ItemStack[] inventory) {
        return new RecipeTransferPlanner(
            recipe,
            fluid,
            new Machine(inputs, STACK_LIMIT, new ItemStack[0]),
            inventory,
            SAME_ITEM).plan(multiplier == 0 ? Mode.FILL : Mode.ADD, multiplier);
    }

    private static RecipeFluid fluid(int perCraft, int inTank, int capacity) {
        return new RecipeFluid(perCraft, new Tank(inTank, false, capacity), CONTAINERS);
    }

    private static ItemStack cell(int size, int amount) {
        ItemStack cell = new ItemStack(CELL, size);
        if (amount > 0) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("amount", amount);
            cell.setTagCompound(tag);
        }
        return cell;
    }

    private static Result plan(List<Ingredient> recipe, int multiplier, ItemStack[] inputs, ItemStack[] inventory) {
        return new RecipeTransferPlanner(
            recipe,
            null,
            new Machine(inputs, STACK_LIMIT, new ItemStack[0]),
            inventory,
            SAME_ITEM).plan(multiplier == 0 ? Mode.FILL : Mode.ADD, multiplier);
    }

    private static Ingredient ingredient(int amount, ItemStack... alternatives) {
        return new Ingredient(List.of(alternatives), amount);
    }

    private static Item item(String name) {
        return new Item().setUnlocalizedName("transfer.test." + name);
    }

    private static ItemStack stack(Item item, int size) {
        return new ItemStack(item, size);
    }

    /** Nine input slots, like the basic machines. */
    private static ItemStack[] inputs(ItemStack... stacks) {
        return Arrays.copyOf(stacks, 9);
    }

    /** A player inventory with free slots after the given stacks. */
    private static ItemStack[] inventory(ItemStack... stacks) {
        return Arrays.copyOf(stacks, 36);
    }

    /** The non-empty slots hold these stacks, in this order. */
    private static void assertStacks(ItemStack[] actual, ItemStack... expected) {
        List<String> actualStacks = Arrays.stream(actual)
            .filter(s -> s != null)
            .map(RecipeTransferPlannerTest::describe)
            .toList();
        List<String> expectedStacks = Arrays.stream(expected)
            .map(RecipeTransferPlannerTest::describe)
            .toList();
        assertEquals(expectedStacks, actualStacks);
    }

    /** Every slot of the arrays, empty ones included. */
    private static List<String> snapshot(ItemStack[]... arrays) {
        return Arrays.stream(arrays)
            .flatMap(Arrays::stream)
            .map(stack -> stack == null ? "-" : describe(stack))
            .toList();
    }

    private static String describe(ItemStack stack) {
        String fluid = stack.hasTagCompound() ? "{" + stack.getTagCompound()
            .getInteger("amount") + "}" : "";
        return stack.stackSize + "x"
            + stack.getItem()
                .getUnlocalizedName()
            + "@"
            + stack.getItemDamage()
            + fluid;
    }
}
