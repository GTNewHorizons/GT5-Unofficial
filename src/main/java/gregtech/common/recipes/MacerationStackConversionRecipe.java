package gregtech.common.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.gtnewhorizon.gtnhlib.item.ItemStackNBT;

import gregtech.api.enums.ItemList;
import gregtech.api.util.GTUtility;
import gregtech.common.blocks.ItemMachines;
import gregtech.common.tileentities.machines.multi.MTEIndustrialMacerator;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIndustrialMaceratorLegacy;

// TODO: Remove in 2.10 when old macerator controller is removed
public class MacerationStackConversionRecipe implements IRecipe {

    public static final MacerationStackConversionRecipe INSTANCE = new MacerationStackConversionRecipe();

    public static void register() {
        CraftingManager.getInstance()
            .getRecipeList()
            .add(INSTANCE);
    }

    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        return getItemCount(inv) == getRecipeSize() && findController(inv) != null;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        if (getItemCount(inv) != getRecipeSize()) return null;

        ItemStack controller = findController(inv);

        if (controller == null) return null;

        return copyUpgradeChip(controller);
    }

    @Override
    public int getRecipeSize() {
        return 1;
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

    private static ItemStack findController(InventoryCrafting inv) {
        int size = inv.getSizeInventory();

        for (int i = 0; i < size; i++) {
            ItemStack stack = inv.getStackInSlot(i);

            if (!isOldMacerationStack(stack)) continue;

            return GTUtility.copyAmount(1, stack);
        }

        return null;
    }

    public static ItemStack copyUpgradeChip(@NotNull ItemStack original) {
        ItemStack stack = ItemList.MacerationStack.get(1);
        if (!original.hasTagCompound() || !original.getTagCompound()
            .hasKey(MTEIndustrialMacerator.TIER)) return stack;
        ItemStackNBT.setByte(
            stack,
            MTEIndustrialMacerator.TIER,
            original.getTagCompound()
                .getByte(MTEIndustrialMacerator.TIER));
        return stack;
    }

    public static boolean isOldMacerationStack(@Nullable ItemStack stack) {
        return ItemMachines.getMetaTileEntity(stack) instanceof MTEIndustrialMaceratorLegacy;
    }
}
