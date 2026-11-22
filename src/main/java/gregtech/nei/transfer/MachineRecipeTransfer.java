package gregtech.nei.transfer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.cleanroommc.modularui.utils.item.ItemStackHandler;

import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import gregtech.api.util.GTUtility;
import gregtech.api.util.item.GhostCircuitItemStackHandler;
import gregtech.common.items.ItemIntegratedCircuit;
import gregtech.nei.transfer.RecipeTransferPlanner.Machine;
import gregtech.nei.transfer.RecipeTransferPlanner.RecipeFluid;
import gregtech.nei.transfer.RecipeTransferPlanner.Result;
import gregtech.nei.transfer.RecipeTransferPlanner.Tank;

/**
 * A recipe transfer between a player and a basic machine, planned and applied by the server.
 */
final class MachineRecipeTransfer {

    private static final int HOTBAR_SIZE = InventoryPlayer.getHotbarSize();

    private final MTEBasicMachine machine;
    private final EntityPlayer player;
    private final RecipeTransferRequest request;
    /** Player inventory slots in the order items are taken from them: main inventory first, then the hotbar. */
    private final int[] order;
    private final RecipeFluid fluid;
    private final Result result;

    MachineRecipeTransfer(MTEBasicMachine machine, EntityPlayer player, RecipeTransferRequest request) {
        this.machine = machine;
        this.player = player;
        this.request = request;

        ItemStackHandler machineInventory = machine.inventoryHandler;
        int firstInput = machine.getInputSlot();
        ItemStack[] inputs = slots(machineInventory, firstInput, machine.mInputSlotCount);
        ItemStack[] outputs = slots(machineInventory, machine.getOutputSlot(), machine.mOutputItems.length);

        order = inventoryOrder(player);
        ItemStack[] inventory = orderedInventory(player, order);

        // Machines without a tank (steam machines) leave the fluid to the player
        if (request.fluid() != null && machine.getCapacity() > 0) {
            FluidStack inTank = machine.getFillableStack();
            boolean empty = inTank == null || inTank.amount <= 0;
            boolean otherFluid = !empty && !inTank.isFluidEqual(request.fluid());
            fluid = new RecipeFluid(
                request.fluid().amount,
                new Tank(empty || otherFluid ? 0 : inTank.amount, otherFluid, machine.getCapacity()),
                new RecipeFluidContainers(request.fluid()));
        } else {
            fluid = null;
        }

        Machine machineState = new Machine(
            inputs,
            (slot, stack) -> machineInventory.isItemValid(firstInput + slot, stack)
                ? machine.getStackSizeLimit(firstInput + slot, stack)
                : 0,
            outputs);
        result = new RecipeTransferPlanner(
            request.ingredients(),
            fluid,
            machineState,
            inventory,
            (alternative, stack) -> GTUtility.areStacksEqual(alternative, stack, true))
                .plan(request.mode(), request.crafts());
    }

    Result result() {
        return result;
    }

    /** Writes a transferred result into the machine and the player inventory. */
    void apply() {
        if (result.outcome() != RecipeTransferPlanner.Outcome.TRANSFERRED) return;

        ItemStackHandler machineInventory = machine.inventoryHandler;
        setSlots(machineInventory, machine.getInputSlot(), result.inputs());
        setSlots(machineInventory, machine.getOutputSlot(), result.outputs());

        ItemStack[] playerInventory = player.inventory.mainInventory;
        for (int i = 0; i < order.length; i++) playerInventory[order[i]] = result.inventory()[i];
        player.inventory.markDirty();

        if (fluid != null) {
            machine.setFillableStack(
                result.tankAmount() > 0 ? new FluidStack(request.fluid(), result.tankAmount()) : null);
        }
        if (machine.allowSelectCircuit()) {
            new GhostCircuitItemStackHandler(machine).setCircuitConfig(validCircuit(request.circuitConfig()));
        }
        machine.getBaseMetaTileEntity()
            .markInventoryBeenModified();
    }

    /** Player inventory slots in the order items are taken from them and given to them: main inventory, then hotbar. */
    private static int[] inventoryOrder(EntityPlayer player) {
        int[] order = new int[player.inventory.mainInventory.length];
        for (int i = 0; i < order.length; i++) order[i] = (i + HOTBAR_SIZE) % order.length;
        return order;
    }

    private static ItemStack[] orderedInventory(EntityPlayer player, int[] order) {
        ItemStack[] inventory = new ItemStack[order.length];
        for (int i = 0; i < order.length; i++) inventory[i] = player.inventory.mainInventory[order[i]];
        return inventory;
    }

    private static ItemStack[] slots(ItemStackHandler inventory, int first, int count) {
        ItemStack[] stacks = new ItemStack[count];
        for (int i = 0; i < count; i++) stacks[i] = inventory.getStackInSlot(first + i);
        return stacks;
    }

    private static void setSlots(ItemStackHandler inventory, int first, ItemStack[] stacks) {
        for (int i = 0; i < stacks.length; i++) inventory.setStackInSlot(first + i, stacks[i]);
    }

    private static int validCircuit(int config) {
        return config >= 1 && config <= ItemIntegratedCircuit.MAX_CIRCUIT_NUMBER ? config
            : GhostCircuitItemStackHandler.NO_CONFIG;
    }
}
