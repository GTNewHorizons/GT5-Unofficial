package gregtech.common.recipes;

import bartworks.API.enums.CircuitImprint;
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
        CircuitImprint circuitImprint = findCircuitImprint(inv);

        return cal != null && circuitImprint != null;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        if (getItemCount(inv) > getRecipeSize()) return null;

        ItemStack cal = findCAL(inv);
        CircuitImprint circuitImprint = findCircuitImprint(inv);

        if (cal == null || circuitImprint == null) return null;

        return installImprint(cal, circuitImprint);
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
            if (getCircuitImprintFromCAL(stack) != null) continue;

            return GTUtility.copyAmount(1, stack);
        }

        return null;
    }

    private static CircuitImprint findCircuitImprint(InventoryCrafting inv) {
        int size = inv.getSizeInventory();

        for (int i = 0; i < size; i++) {
            ItemStack stack = inv.getStackInSlot(i);

            CircuitImprint circuitImprint = CircuitImprint.findCircuitImprintByImprintStack(stack);
            if (circuitImprint == null) continue;
            return circuitImprint;
        }

        return null;
    }

    public static ItemStack installImprint(@NotNull ItemStack cal, @NotNull CircuitImprint circuitImprint) {
        ItemStack imprintedCAL = GTUtility.copyAmount(1, cal);
        NBTTagCompound tag = imprintedCAL.getTagCompound();

        if (tag == null) {
            tag = new NBTTagCompound();
            imprintedCAL.setTagCompound(tag);
        }

        tag.setInteger(MTECircuitAssemblyLine.IMPRINT_ID_KEY, circuitImprint.id);

        return imprintedCAL;
    }

    public static boolean isCAL(@Nullable ItemStack stack) {
        return ItemMachines.getMetaTileEntity(stack) instanceof MTECircuitAssemblyLine;
    }

    public static CircuitImprint getCircuitImprintFromCAL(@NotNull ItemStack cal){
        if (!isCAL(cal)) return null;

        NBTTagCompound tag = cal.getTagCompound();

        if (tag == null) return null;

        // old tag
        if (tag.hasKey(MTECircuitAssemblyLine.IMPRINT_KEY)){
            String name = ItemStack.loadItemStackFromNBT(tag.getCompoundTag(MTECircuitAssemblyLine.IMPRINT_KEY)).getUnlocalizedName();
            if (CircuitImprint.IMPRINT_LOOKUPS_BY_UNLOCALISED_NAMES.containsKey(name)) {
                return CircuitImprint.IMPRINT_LOOKUPS_BY_UNLOCALISED_NAMES.get(name);
            }
        }
        else if (tag.hasKey(MTECircuitAssemblyLine.IMPRINT_ID_KEY)){
            int id = tag.getInteger(MTECircuitAssemblyLine.IMPRINT_ID_KEY);
            if (CircuitImprint.IMPRINT_LOOKUPS_BY_IDS.containsKey(id)){
                return CircuitImprint.IMPRINT_LOOKUPS_BY_IDS.get(id);
            }
        }

        return null;
    }
}
