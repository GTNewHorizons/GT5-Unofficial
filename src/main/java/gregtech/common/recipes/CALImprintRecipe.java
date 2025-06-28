package gregtech.common.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

import gregtech.api.util.GTUtility;
import gregtech.nei.GTNEIImprintHandler;

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

        return GTNEIImprintHandler.installImprint(cal, imprint);
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

            if (!GTNEIImprintHandler.isCAL(stack)) continue;
            if (GTNEIImprintHandler.getCircuitFromCAL(stack) != null) continue;

            return stack;
        }

        return null;
    }

    private static ItemStack findImprint(InventoryCrafting inv) {
        int size = inv.getSizeInventory();

        for (int i = 0; i < size; i++) {
            ItemStack stack = inv.getStackInSlot(i);

            if (stack == null || stack.getItem() == null) continue;

            if (GTNEIImprintHandler.getCircuitFromImprint(stack) == null) continue;

            return stack;
        }

        return null;
    }
}
