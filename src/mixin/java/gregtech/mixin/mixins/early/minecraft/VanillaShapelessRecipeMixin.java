package gregtech.mixin.mixins.early.minecraft;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import gregtech.api.interfaces.IRecipeMutableAccess;

@Mixin(ShapelessRecipes.class)
public class VanillaShapelessRecipeMixin implements IRecipeMutableAccess {

    @Shadow
    private ItemStack recipeOutput;

    @Shadow
    @Final
    public List<ItemStack> recipeItems;

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
