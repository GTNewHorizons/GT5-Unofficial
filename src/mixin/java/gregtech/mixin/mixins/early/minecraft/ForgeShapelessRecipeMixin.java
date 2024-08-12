package gregtech.mixin.mixins.early.minecraft;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import gregtech.api.interfaces.IRecipeMutableAccess;

@Mixin(value = ShapelessOreRecipe.class, remap = false)
public class ForgeShapelessRecipeMixin implements IRecipeMutableAccess {

    @Shadow
    private ItemStack output;

    @Shadow
    private ArrayList<Object> input;

    @Override
    public ItemStack gt5u$getRecipeOutputItem() {
        return this.output;
    }

    @Override
    public void gt5u$setRecipeOutputItem(ItemStack newItem) {
        this.output = newItem;
    }

    @Override
    public Object gt5u$getRecipeInputs() {
        return this.input;
    }
}
