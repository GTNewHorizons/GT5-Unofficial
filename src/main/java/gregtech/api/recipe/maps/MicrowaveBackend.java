package gregtech.api.recipe.maps;

import static gregtech.api.enums.GT_Values.W;
import static gregtech.api.recipe.check.FindRecipeResult.*;

import java.util.function.Predicate;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.SubTag;
import gregtech.api.objects.ItemData;
import gregtech.api.objects.MaterialStack;
import gregtech.api.recipe.RecipeMapBackendPropertiesBuilder;
import gregtech.api.recipe.check.FindRecipeResult;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.MethodsReturnNonnullByDefault;

/**
 * Special Class for Microwave Recipe handling.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MicrowaveBackend extends NonGTBackend {

    public MicrowaveBackend(RecipeMapBackendPropertiesBuilder propertiesBuilder) {
        super(propertiesBuilder);
    }

    @Override
    protected FindRecipeResult overwriteFindRecipe(ItemStack[] items, FluidStack[] fluids,
        @Nullable ItemStack specialSlot, Predicate<GT_Recipe> recipeValidator, @Nullable GT_Recipe cachedRecipe) {
        if (items.length == 0 || items[0] == null) {
            return NOT_FOUND;
        }
        if (cachedRecipe != null && cachedRecipe.isRecipeInputEqual(false, true, fluids, items)
            && recipeValidator.test(cachedRecipe)) {
            return FindRecipeResult.ofSuccess(cachedRecipe);
        }

        ItemStack output = GT_ModHandler.getSmeltingOutput(items[0], false, null);

        if (GT_Utility.areStacksEqual(items[0], new ItemStack(Items.book, 1, W))) {
            return GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.copyAmount(1, items[0]))
                .itemOutputs(GT_Utility.getWrittenBook("Manual_Microwave", ItemList.Book_Written_03.get(1)))
                .duration(32)
                .eut(4)
                .noOptimize()
                .buildAndGetResult(recipeValidator);
        }

        // Check Container Item of Input since it is around the Input, then the Input itself, then Container Item of
        // Output and last check the Output itself
        for (ItemStack item : new ItemStack[] { GT_Utility.getContainerItem(items[0], true), items[0],
            GT_Utility.getContainerItem(output, true), output }) {
            if (item == null) continue;
            if (GT_Utility.areStacksEqual(item, new ItemStack(Blocks.netherrack, 1, W), true)
                || GT_Utility.areStacksEqual(item, new ItemStack(Blocks.tnt, 1, W), true)
                || GT_Utility.areStacksEqual(item, new ItemStack(Items.egg, 1, W), true)
                || GT_Utility.areStacksEqual(item, new ItemStack(Items.firework_charge, 1, W), true)
                || GT_Utility.areStacksEqual(item, new ItemStack(Items.fireworks, 1, W), true)
                || GT_Utility.areStacksEqual(item, new ItemStack(Items.fire_charge, 1, W), true)) {
                GT_Log.exp
                    .println("Microwave Explosion due to TNT || EGG || FIREWORKCHARGE || FIREWORK || FIRE CHARGE");
                return EXPLODE;
            }

            ItemData itemData = GT_OreDictUnificator.getItemData(item);
            if (itemData != null) {
                if (itemData.mMaterial != null && itemData.mMaterial.mMaterial != null) {
                    if (itemData.mMaterial.mMaterial.contains(SubTag.METAL)
                        || itemData.mMaterial.mMaterial.contains(SubTag.EXPLOSIVE)) {
                        GT_Log.exp.println("Microwave Explosion due to METAL insertion");
                        return EXPLODE;
                    }
                    if (itemData.mMaterial.mMaterial.contains(SubTag.FLAMMABLE)) {
                        GT_Log.exp.println("Microwave INFLAMMATION due to FLAMMABLE insertion");
                        return ON_FIRE;
                    }
                }
                for (MaterialStack materialStack : itemData.mByProducts) {
                    if (materialStack == null) continue;
                    if (materialStack.mMaterial.contains(SubTag.METAL)
                        || materialStack.mMaterial.contains(SubTag.EXPLOSIVE)) {
                        GT_Log.exp.println("Microwave Explosion due to METAL insertion");
                        return EXPLODE;
                    }
                    if (materialStack.mMaterial.contains(SubTag.FLAMMABLE)) {
                        GT_Log.exp.println("Microwave INFLAMMATION due to FLAMMABLE insertion");
                        return ON_FIRE;
                    }
                }
            }
            if (TileEntityFurnace.getItemBurnTime(item) > 0) {
                GT_Log.exp.println("Microwave INFLAMMATION due to BURNABLE insertion");
                return ON_FIRE;
            }
        }

        return output == null ? NOT_FOUND
            : GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.copyAmount(1, items[0]))
                .itemOutputs(output)
                .duration(32)
                .eut(4)
                .noOptimize()
                .buildAndGetResult(recipeValidator);
    }

    @Override
    public boolean containsInput(ItemStack item) {
        return GT_ModHandler.getSmeltingOutput(item, false, null) != null;
    }
}
