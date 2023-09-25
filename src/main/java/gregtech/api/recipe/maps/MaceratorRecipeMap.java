package gregtech.api.recipe.maps;

import static gregtech.api.enums.GT_Values.D1;
import static gregtech.api.recipe.check.FindRecipeResult.NOT_FOUND;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.GregTech_API;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.FindRecipeResult;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

/**
 * Special Class for Macerator/RockCrusher Recipe handling.
 */
public class MaceratorRecipeMap extends RecipeMap {

    public MaceratorRecipeMap(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName,
        String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount, int aMinimalInputItems,
        int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre, int aNEISpecialValueMultiplier,
        String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI, boolean aNEIAllowed) {
        super(
            aRecipeList,
            aUnlocalizedName,
            aLocalName,
            aNEIName,
            aNEIGUIPath,
            aUsualInputCount,
            aUsualOutputCount,
            aMinimalInputItems,
            aMinimalInputFluids,
            aAmperage,
            aNEISpecialValuePre,
            aNEISpecialValueMultiplier,
            aNEISpecialValuePost,
            aShowVoltageAmperageInNEI,
            aNEIAllowed);
    }

    @Nonnull
    @Override
    public FindRecipeResult findRecipeWithResult(GT_Recipe aRecipe, Predicate<GT_Recipe> aIsValidRecipe,
        boolean aNotUnificated, boolean aDontCheckStackSizes, long aVoltage, FluidStack[] aFluids,
        ItemStack aSpecialSlot, ItemStack... aInputs) {
        if (aInputs == null || aInputs.length == 0 || aInputs[0] == null || !GregTech_API.sPostloadFinished)
            return super.findRecipeWithResult(
                aRecipe,
                aIsValidRecipe,
                aNotUnificated,
                aDontCheckStackSizes,
                aVoltage,
                aFluids,
                aSpecialSlot,
                aInputs);
        FindRecipeResult result = super.findRecipeWithResult(
            aRecipe,
            aIsValidRecipe,
            aNotUnificated,
            aDontCheckStackSizes,
            aVoltage,
            aFluids,
            aSpecialSlot,
            aInputs);
        if (result.isSuccessful()) return result;

        try {
            List<ItemStack> tRecipeOutputs = mods.railcraft.api.crafting.RailcraftCraftingManager.rockCrusher
                .getRecipe(GT_Utility.copyAmount(1, aInputs[0]))
                .getRandomizedOuputs();
            if (tRecipeOutputs != null) {
                GT_Recipe recipe = new GT_Recipe(
                    false,
                    new ItemStack[] { GT_Utility.copyAmount(1, aInputs[0]) },
                    tRecipeOutputs.toArray(new ItemStack[0]),
                    null,
                    null,
                    null,
                    null,
                    800,
                    2,
                    0);
                recipe.mCanBeBuffered = false;
                recipe.mNeedsEmptyOutput = true;
                return FindRecipeResult.ofSuccess(recipe);
            }
        } catch (NoClassDefFoundError e) {
            if (D1) GT_Log.err.println("Railcraft Not loaded");
        } catch (NullPointerException e) {
            /**/
        }

        ItemStack tComparedInput = GT_Utility.copyOrNull(aInputs[0]);
        ItemStack[] tOutputItems = GT_ModHandler.getMachineOutput(
            tComparedInput,
            ic2.api.recipe.Recipes.macerator.getRecipes(),
            true,
            new NBTTagCompound(),
            null,
            null,
            null);
        if (tComparedInput != null && GT_Utility.arrayContainsNonNull(tOutputItems)) {
            return FindRecipeResult.ofSuccess(
                new GT_Recipe(
                    false,
                    new ItemStack[] {
                        GT_Utility.copyAmount(aInputs[0].stackSize - tComparedInput.stackSize, aInputs[0]) },
                    tOutputItems,
                    null,
                    null,
                    null,
                    null,
                    400,
                    2,
                    0));
        }
        return NOT_FOUND;
    }

    @Override
    public boolean containsInput(ItemStack aStack) {
        return super.containsInput(aStack) || GT_Utility.arrayContainsNonNull(
            GT_ModHandler.getMachineOutput(
                GT_Utility.copyAmount(64, aStack),
                ic2.api.recipe.Recipes.macerator.getRecipes(),
                false,
                new NBTTagCompound(),
                null,
                null,
                null));
    }
}
