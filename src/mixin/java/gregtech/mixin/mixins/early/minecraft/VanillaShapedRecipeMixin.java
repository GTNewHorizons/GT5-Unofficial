package gregtech.mixin.mixins.early.minecraft;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import gregtech.api.interfaces.IRecipeMutableAccess;

@Mixin(ShapedRecipes.class)
public class VanillaShapedRecipeMixin implements IRecipeMutableAccess {

    @Shadow
    private ItemStack recipeOutput;

    @Shadow
    @Final
    public ItemStack[] recipeItems;

    @Override
    public ItemStack gt5u$getRecipeOutputItem() {
        return this.recipeOutput;
    }

    @Override
    public void gt5u$setRecipeOutputItem(ItemStack newItem) {
        this.recipeOutput = newItem;
    }

    @Override
    public Object gt5u$getRecipeInputs() {
        return this.recipeItems;
    }
}
