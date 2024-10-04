package gtPlusPlus.core.interfaces;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public interface IItemBlueprint {

    /**
     * The inventory size for the blueprint~
     */
    int INV_SIZE = 9;

    /**
     * Meta Compatible function to allow meta items to be blueprints
     *
     * @param stack yourMetaItem
     * @return true if it is a Blueprint
     */
    boolean isBlueprint(ItemStack stack);

    /**
     * Sets the blueprint for this itemstack.
     *
     * @param stack yourMetaItem
     * @return true if blueprint is set successfully
     */
    boolean setBlueprint(ItemStack stack, IInventory craftingTable, ItemStack output);

    /**
     * Sets the name of the recipe/blueprint
     *
     * @param String Blueprint Name
     * @return N/A
     */
    void setBlueprintName(ItemStack stack, String name);

    /**
     * Does this itemstack hold a blueprint?
     *
     * @param stack yourMetaItem
     * @return true if is holding a Blueprint
     */
    boolean hasBlueprint(ItemStack stack);

    /**
     * Gets the recipe held by the item
     *
     * @param stack yourMetaItem
     * @return the blueprints contents
     */
    ItemStack[] getBlueprint(ItemStack stack);
}
