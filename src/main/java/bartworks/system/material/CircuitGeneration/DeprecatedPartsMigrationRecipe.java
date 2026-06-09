package bartworks.system.material.CircuitGeneration;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

import bartworks.API.enums.CircuitImprint;

public class DeprecatedPartsMigrationRecipe implements IRecipe {

    @Override
    public boolean matches(InventoryCrafting inv, World p_77569_2_) {
        if (getCraftingResult(inv) != null) return true;
        return false;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack foundPart = null;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack != null) {
                if (stack.getItem() == CircuitPartsItem.getCircuitParts() && stack.hasTagCompound()) {
                    int id = stack.getItemDamage();
                    if (id == 0 || id == 1) {
                        ItemStack imprintStack = ItemStack.loadItemStackFromNBT(stack.getTagCompound());
                        if (imprintStack == null) continue;
                        String circuitName = imprintStack.getUnlocalizedName();
                        CircuitImprint ci = CircuitImprint.IMPRINT_LOOKUPS_BY_UNLOCALISED_NAMES.get(circuitName);
                        if (ci == null) continue;
                        if (foundPart != null) return null;
                        foundPart = id == 0 ? ci.imprint.get(1) : ci.slicedCircuit.get(1);
                    }
                } else return null;
            }
        }
        return foundPart;
    }

    @Override
    public int getRecipeSize() {
        return 1;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }
}
