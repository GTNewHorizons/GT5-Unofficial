package gregtech.common.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import bartworks.common.tileentities.multis.MTECircuitAssemblyLine;
import bartworks.system.material.CircuitGeneration.BWMetaItems;
import gregtech.api.util.GTUtility;
import gregtech.common.blocks.ItemMachines;

public class CALImprintRecipe implements IRecipe {

    public static final CALImprintRecipe INSTANCE = new CALImprintRecipe();

    public static void register() {
        CraftingManager.getInstance()
            .getRecipeList()
            .add(INSTANCE);
    }

    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        if (getItemCount(inv) > getRecipeSize()) return false;

        ItemStack cal = findCAL(inv);
        ItemStack imprint = findImprint(inv);

        return cal != null && imprint != null;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        if (getItemCount(inv) > getRecipeSize()) return null;

        ItemStack cal = findCAL(inv);
        ItemStack imprint = findImprint(inv);

        if (cal == null || imprint == null) return null;

        cal = GTUtility.copyAmount(1, cal);
        imprint = GTUtility.copyAmount(1, imprint);

        return installImprint(cal, imprint);
    }

    @Override
    public int getRecipeSize() {
        return 2;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }

    private static int getItemCount(InventoryCrafting inv) {
        int size = inv.getSizeInventory();
        int count = 0;

        for (int i = 0; i < size; i++) {
            ItemStack stack = inv.getStackInSlot(i);

            if (!GTUtility.isStackValid(stack)) continue;

            count++;
        }

        return count;
    }

    private static ItemStack findCAL(InventoryCrafting inv) {
        int size = inv.getSizeInventory();

        for (int i = 0; i < size; i++) {
            ItemStack stack = inv.getStackInSlot(i);

            if (!isCAL(stack)) continue;
            if (getCircuitFromCAL(stack) != null) continue;

            return stack;
        }

        return null;
    }

    private static ItemStack findImprint(InventoryCrafting inv) {
        int size = inv.getSizeInventory();

        for (int i = 0; i < size; i++) {
            ItemStack stack = inv.getStackInSlot(i);

            if (stack == null || stack.getItem() == null) continue;

            if (getCircuitFromImprint(stack) == null) continue;

            return stack;
        }

        return null;
    }

    public static ItemStack installImprint(@NotNull ItemStack cal, @NotNull ItemStack imprint) {
        NBTTagCompound tag = cal.getTagCompound();

        if (tag == null) {
            tag = new NBTTagCompound();
            cal.setTagCompound(tag);
        }

        tag.setTag(MTECircuitAssemblyLine.IMPRINT_KEY, imprint.getTagCompound());

        return cal;
    }

    public static boolean isCAL(@Nullable ItemStack stack) {
        return ItemMachines.getMetaTileEntity(stack) instanceof MTECircuitAssemblyLine;
    }

    public static ItemStack getImprintForCircuit(@NotNull ItemStack circuit) {
        ItemStack imprint = new ItemStack(BWMetaItems.getCircuitParts(), 1, 0);

        imprint.setTagCompound(circuit.writeToNBT(new NBTTagCompound()));

        return imprint;
    }

    public static ItemStack getCircuitFromCAL(@NotNull ItemStack cal) {
        if (!isCAL(cal)) return null;

        NBTTagCompound tag = cal.getTagCompound();

        if (tag == null || !tag.hasKey(MTECircuitAssemblyLine.IMPRINT_KEY)) return null;

        return ItemStack.loadItemStackFromNBT(tag.getCompoundTag(MTECircuitAssemblyLine.IMPRINT_KEY));
    }

    public static ItemStack getCircuitFromImprint(@Nullable ItemStack imprint) {
        if (imprint == null) return null;
        if (imprint.getItem() == null) return null;
        if (!(imprint.getItem() instanceof BWMetaItems.BW_GT_MetaGenCircuits)) return null;
        if (imprint.getItemDamage() != 0) return null;
        if (imprint.getTagCompound() == null) return null;

        return ItemStack.loadItemStackFromNBT(imprint.getTagCompound());
    }
}
