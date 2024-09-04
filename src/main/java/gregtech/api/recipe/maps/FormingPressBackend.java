package gregtech.api.recipe.maps;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBackendPropertiesBuilder;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MethodsReturnNonnullByDefault;

/**
 * Special Class for Forming Press handling.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class FormingPressBackend extends RecipeMapBackend {

    public FormingPressBackend(RecipeMapBackendPropertiesBuilder propertiesBuilder) {
        super(propertiesBuilder);
    }

    @Override
    protected GTRecipe modifyFoundRecipe(GTRecipe recipe, ItemStack[] items, FluidStack[] fluids,
        @Nullable ItemStack specialSlot) {
        for (ItemStack mold : items) {
            if (ItemList.Shape_Mold_Credit.isStackEqual(mold, false, true)) {
                NBTTagCompound nbt = mold.getTagCompound();
                if (nbt == null) nbt = new NBTTagCompound();
                if (!nbt.hasKey("credit_security_id")) nbt.setLong("credit_security_id", System.nanoTime());
                mold.setTagCompound(nbt);

                recipe = recipe.copy();
                recipe.mCanBeBuffered = false;
                recipe.mOutputs[0].setTagCompound(nbt);
                return recipe;
            }
        }
        return recipe;
    }

    @Override
    protected GTRecipe findFallback(ItemStack[] items, FluidStack[] fluids, @Nullable ItemStack specialSlot) {
        if (items.length < 2) {
            return null;
        }
        return findRenamingRecipe(items);
    }

    @Nullable
    private GTRecipe findRenamingRecipe(ItemStack[] inputs) {
        ItemStack mold = findNameMoldIndex(inputs);
        if (mold == null) return null;
        ItemStack input = findStackToRename(inputs, mold);
        if (input == null) return null;
        ItemStack output = GTUtility.copyAmount(1, input);
        if (output == null) return null;
        output.setStackDisplayName(mold.getDisplayName());
        return GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(0, mold), GTUtility.copyAmount(1, input))
            .itemOutputs(output)
            .duration(128)
            .eut(8)
            .noBuffer()
            .nbtSensitive()
            .build()
            .orElse(null);
    }

    @Nullable
    private ItemStack findNameMoldIndex(ItemStack[] inputs) {
        for (ItemStack stack : inputs) {
            if (ItemList.Shape_Mold_Name.isStackEqual(stack, false, true)) return stack;
        }
        return null;
    }

    @Nullable
    private ItemStack findStackToRename(ItemStack[] inputs, ItemStack mold) {
        for (ItemStack stack : inputs) {
            if (stack == mold || stack == null) continue;
            return stack;
        }
        return null;
    }
}
