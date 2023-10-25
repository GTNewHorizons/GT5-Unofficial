package gregtech.api.recipe.maps;

import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.recipe.check.FindRecipeResult.NOT_FOUND;

import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBackendPropertiesBuilder;
import gregtech.api.recipe.check.FindRecipeResult;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import mods.railcraft.api.crafting.RailcraftCraftingManager;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MaceratorBackend extends RecipeMapBackend {

    public MaceratorBackend(RecipeMapBackendPropertiesBuilder propertiesBuilder) {
        super(propertiesBuilder);
    }

    @Override
    protected FindRecipeResult findFallback(ItemStack[] items, FluidStack[] fluids, @Nullable ItemStack specialSlot,
        Predicate<GT_Recipe> recipeValidator) {
        if (items.length == 0 || items[0] == null) {
            return NOT_FOUND;
        }

        if (Railcraft.isModLoaded()) {
            List<ItemStack> recipeOutputs = RailcraftCraftingManager.rockCrusher
                .getRecipe(GT_Utility.copyAmount(1, items[0]))
                .getRandomizedOuputs();
            if (recipeOutputs != null) {
                return GT_Values.RA.stdBuilder()
                    .itemInputs(GT_Utility.copyAmount(1, items[0]))
                    .itemOutputs(recipeOutputs.toArray(new ItemStack[0]))
                    .duration(800)
                    .eut(2)
                    .noBuffer()
                    .needsEmptyOutput()
                    .buildAndGetResult(recipeValidator);
            }
        }

        ItemStack comparedInput = GT_Utility.copyOrNull(items[0]);
        ItemStack[] outputItems = GT_ModHandler.getMachineOutput(
            comparedInput,
            ic2.api.recipe.Recipes.macerator.getRecipes(),
            true,
            new NBTTagCompound(),
            null,
            null,
            null);
        if (comparedInput != null && GT_Utility.arrayContainsNonNull(outputItems)) {
            return GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.copyAmount(items[0].stackSize - comparedInput.stackSize, items[0]))
                .itemOutputs(outputItems)
                .duration(400)
                .eut(2)
                .noOptimize()
                .buildAndGetResult(recipeValidator);
        }
        return NOT_FOUND;
    }

    @Override
    public boolean containsInput(ItemStack item) {
        return super.containsInput(item) || GT_Utility.arrayContainsNonNull(
            GT_ModHandler.getMachineOutput(
                GT_Utility.copyAmount(64, item),
                ic2.api.recipe.Recipes.macerator.getRecipes(),
                false,
                new NBTTagCompound(),
                null,
                null,
                null));
    }
}
