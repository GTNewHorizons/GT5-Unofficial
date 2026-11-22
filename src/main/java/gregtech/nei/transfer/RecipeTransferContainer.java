package gregtech.nei.transfer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.cleanroommc.modularui.integration.nei.INEIRecipeTransfer;
import com.cleanroommc.modularui.screen.ModularContainer;
import com.cleanroommc.modularui.utils.item.SlotItemHandler;

import codechicken.nei.PositionedStack;
import codechicken.nei.api.IRecipeOverlayRenderer;
import codechicken.nei.recipe.GuiOverlayButton.ItemOverlayState;
import codechicken.nei.recipe.IRecipeHandler;
import codechicken.nei.recipe.StackInfo;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import gregtech.api.recipe.RecipeCategory;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTUtility;
import gregtech.api.util.item.GhostCircuitItemStackHandler;
import gregtech.nei.GTNEIDefaultHandler.FixedPositionedStack;
import gregtech.nei.transfer.RecipeTransferRequest.Ingredient;
import gregtech.nei.transfer.RecipeTransferRequest.Mode;

/**
 * Client side container of a basic machine GUI. ModularUI2 hands NEI an overlay handler for the recipe categories it
 * declares, so the NEI "+" button and the overlay key work in the machine GUI.
 */
@SideOnly(Side.CLIENT)
public class RecipeTransferContainer extends ModularContainer implements INEIRecipeTransfer<GuiContainer> {

    private final MTEBasicMachine machine;
    private final RecipeTransferSyncHandler syncHandler;
    private final String[] idents;

    public RecipeTransferContainer(MTEBasicMachine machine, RecipeTransferSyncHandler syncHandler) {
        this.machine = machine;
        this.syncHandler = syncHandler;
        RecipeMap<?> recipeMap = machine.getRecipeMap();
        this.idents = RecipeCategory.ALL_RECIPE_CATEGORIES.values()
            .stream()
            .filter(category -> category.recipeMap == recipeMap)
            .map(category -> category.unlocalizedName)
            .toArray(String[]::new);
    }

    @Override
    public String[] getIdents() {
        return idents;
    }

    @Override
    public int transferRecipe(GuiContainer gui, IRecipeHandler recipe, int recipeIndex, int multiplier) {
        Mode mode = multiplier == 0 ? Mode.FILL : Mode.ADD;
        syncHandler.requestTransfer(createRequest(gui, recipe, recipeIndex, mode, multiplier));
        return multiplier;
    }

    private RecipeTransferRequest createRequest(GuiContainer gui, IRecipeHandler recipe, int recipeIndex, Mode mode,
        int crafts) {
        List<ItemStack> available = availableStacks(gui);
        List<Ingredient> ingredients = new ArrayList<>();
        FluidStack fluid = null;
        int circuitConfig = GhostCircuitItemStackHandler.NO_CONFIG;

        for (PositionedStack stack : recipe.getIngredientStacks(recipeIndex)) {
            switch (kindOf(stack)) {
                case ITEM -> ingredients
                    .add(new Ingredient(alternatives(stack, available), amount((FixedPositionedStack) stack)));
                case CIRCUIT -> circuitConfig = stack.item.getItemDamage();
                case FLUID -> {
                    // Basic machines have one fluid input
                    if (fluid == null) fluid = fluidPerCraft(stack);
                }
                default -> {}
            }
        }

        return new RecipeTransferRequest(ingredients, fluid, circuitConfig, mode, crafts);
    }

    @Override
    public List<ItemOverlayState> presenceOverlay(GuiContainer gui, IRecipeHandler recipe, int recipeIndex) {
        // Used up as ingredients claim it
        List<ItemStack> available = availableStacks(gui);
        List<ItemOverlayState> states = new ArrayList<>();
        for (PositionedStack stack : recipe.getIngredientStacks(recipeIndex)) {
            boolean present = switch (kindOf(stack)) {
                case ITEM -> claim(available, stack, Math.max(1, amount((FixedPositionedStack) stack)));
                case SPECIAL -> claim(available, stack, 1);
                case FLUID -> hasFluid(gui, fluidPerCraft(stack));
                default -> true;
            };
            states.add(new ItemOverlayState(stack, present));
        }
        return states;
    }

    /** Copies of what the player and the machine slots hold. */
    private List<ItemStack> availableStacks(GuiContainer gui) {
        List<ItemStack> available = new ArrayList<>();
        for (ItemStack stack : gui.mc.thePlayer.inventory.mainInventory) {
            if (stack != null) available.add(stack.copy());
        }
        List<Slot> machineSlots = getInputSlots();
        machineSlots.addAll(getMachineSlots(machine.getOutputSlot(), machine.mOutputItems.length));
        for (Slot slot : machineSlots) {
            if (slot.getHasStack()) available.add(
                slot.getStack()
                    .copy());
        }
        return available;
    }

    /** Whether the tank and the player's containers hold enough fluid for one craft. */
    private boolean hasFluid(GuiContainer gui, FluidStack fluid) {
        if (fluid == null || machine.getCapacity() <= 0) return true;

        long available = 0;
        FluidStack inTank = machine.getFillableStack();
        if (inTank != null && inTank.isFluidEqual(fluid)) available += inTank.amount;
        RecipeFluidContainers containers = new RecipeFluidContainers(fluid);
        for (ItemStack stack : gui.mc.thePlayer.inventory.mainInventory) {
            if (stack != null) available += (long) containers.amountIn(stack) * stack.stackSize;
        }
        return available >= fluid.amount;
    }

    /** Fluid used per craft, null if none. */
    private static FluidStack fluidPerCraft(PositionedStack stack) {
        FluidStack fluid = StackInfo.getFluid(stack.item);
        return fluid != null && fluid.amount > 0 ? fluid : null;
    }

    /** Machine input slots in GUI order. */
    private List<Slot> getInputSlots() {
        return getMachineSlots(machine.getInputSlot(), machine.mInputSlotCount);
    }

    private List<Slot> getMachineSlots(int first, int count) {
        List<Slot> slots = new ArrayList<>();
        int last = first + count;
        for (Slot slot : inventorySlots) {
            if (slot instanceof SlotItemHandler handlerSlot && handlerSlot.getItemHandler() == machine.inventoryHandler
                && slot.getSlotIndex() >= first
                && slot.getSlotIndex() < last) {
                slots.add(slot);
            }
        }
        return slots;
    }

    private Slot getCircuitSlot() {
        for (Slot slot : inventorySlots) {
            if (slot instanceof SlotItemHandler handlerSlot
                && handlerSlot.getItemHandler() instanceof GhostCircuitItemStackHandler) {
                return slot;
            }
        }
        return null;
    }

    /**
     * Ghost stacks for the overlay key and a plain click on "+". NEI's default overlay renderer matches ghost stacks to
     * slots by position, but ModularUI2 draws slot overlays with the slot moved to 1,1, so this one matches by slot.
     */
    public IRecipeOverlayRenderer createOverlayRenderer(List<PositionedStack> ingredients) {
        Map<Slot, ItemStack> ghosts = new HashMap<>();
        List<Slot> inputSlots = getInputSlots();
        int next = 0;
        for (PositionedStack stack : ingredients) {
            switch (kindOf(stack)) {
                case ITEM -> {
                    if (next < inputSlots.size()) ghosts.put(inputSlots.get(next++), stack.item);
                }
                case CIRCUIT -> {
                    Slot circuitSlot = machine.allowSelectCircuit() ? getCircuitSlot() : null;
                    if (circuitSlot != null) ghosts.put(circuitSlot, stack.item);
                }
                default -> {}
            }
        }
        return new RecipeTransferOverlayRenderer(ghosts);
    }

    private enum Kind {
        ITEM,
        CIRCUIT,
        FLUID,
        /** The special slot item, which the transfer leaves to the player. */
        SPECIAL,
        OTHER
    }

    private static Kind kindOf(PositionedStack stack) {
        if (!(stack instanceof FixedPositionedStack fixed)) return Kind.OTHER;
        if (fixed.isFluid()) return Kind.FLUID;
        if (GTUtility.isAnyIntegratedCircuit(stack.item)) return Kind.CIRCUIT;
        return fixed.isInput() ? Kind.ITEM : Kind.SPECIAL;
    }

    /** Items used per craft, 0 if not consumed. NEI may show 1 instead of the real stack size. */
    private static int amount(FixedPositionedStack stack) {
        return stack.getChance() == 0 ? 0 : stack.realStackSize;
    }

    /**
     * The alternatives NEI shows that match something available, so the request stays small even for ingredients
     * with many alternatives. If none matches, the first one stands for the missing ingredient.
     */
    private static List<ItemStack> alternatives(PositionedStack stack, List<ItemStack> available) {
        List<ItemStack> alternatives = new ArrayList<>();
        for (ItemStack item : stack.items) {
            for (ItemStack stackAvailable : available) {
                if (GTUtility.areStacksEqual(item, stackAvailable, true)) {
                    alternatives.add(GTUtility.copyAmount(1, item));
                    break;
                }
            }
        }
        if (alternatives.isEmpty()) alternatives.add(GTUtility.copyAmount(1, stack.item));
        return alternatives;
    }

    /** Whether enough of the stack is available, using it up if so. */
    private static boolean claim(List<ItemStack> available, PositionedStack ingredient, int amount) {
        int found = 0;
        for (ItemStack stack : available) {
            if (stack.stackSize > 0 && accepts(ingredient, stack)) found += stack.stackSize;
        }
        if (found < amount) return false;
        for (ItemStack stack : available) {
            if (amount <= 0) break;
            if (stack.stackSize <= 0 || !accepts(ingredient, stack)) continue;
            int used = Math.min(amount, stack.stackSize);
            stack.stackSize -= used;
            amount -= used;
        }
        return true;
    }

    private static boolean accepts(PositionedStack ingredient, ItemStack stack) {
        for (ItemStack alternative : ingredient.items) {
            if (GTUtility.areStacksEqual(alternative, stack, true)) return true;
        }
        return false;
    }
}
