package gregtech.mixin.mixins.early.minecraft;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import bartworks.ASM.BWCoreStaticReplacementMethodes;

@Mixin(CraftingManager.class)
public abstract class CraftingManagerMixin {

    /**
     * @author bart
     * @reason Cache the result of findMatchingRecipe
     */
    @Overwrite
    public ItemStack findMatchingRecipe(InventoryCrafting inventoryCrafting, World world) {
        return BWCoreStaticReplacementMethodes.findCachedMatchingRecipe(inventoryCrafting, world);
    }
}
