package gregtech.mixin.mixins.early.minecraft.accessors;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import gregtech.mixin.interfaces.accessors.IRecipeMutableAccess;

@Mixin(ShapelessRecipes.class)
public class VanillaShapelessRecipeMixin implements IRecipeMutableAccess {

    @Final
    @Mutable
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
