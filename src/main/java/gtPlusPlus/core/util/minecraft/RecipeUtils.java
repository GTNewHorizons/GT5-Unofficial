package gtPlusPlus.core.util.minecraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.minecraft.ShapedRecipe;
import gtPlusPlus.core.handler.COMPAT_HANDLER;
import gtPlusPlus.core.handler.Recipes.LateRegistrationHandler;
import gtPlusPlus.core.handler.Recipes.RegistrationHandler;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.data.ArrayUtils;

public class RecipeUtils {

    public static int mInvalidID = 1;

    public static void recipeBuilder(final Object slot_1, final Object slot_2, final Object slot_3, final Object slot_4,
            final Object slot_5, final Object slot_6, final Object slot_7, final Object slot_8, final Object slot_9,
            ItemStack resultItem) {

        if (resultItem == null) {
            Logger.RECIPE(
                    "[Fix] Found a recipe with an invalid output, yet had a valid inputs. Using Dummy output so recipe can be found..");
            resultItem = ItemUtils.getItemStackOfAmountFromOreDict("givemeabrokenitem", 1);
            resultItem.setItemDamage(mInvalidID++);
            RegistrationHandler.recipesFailed++;

        } else if ((slot_1 == null) && (slot_2 == null)
                && (slot_3 == null)
                && (slot_4 == null)
                && (slot_5 == null)
                && (slot_6 == null)
                && (slot_7 == null)
                && (slot_8 == null)
                && (slot_9 == null)) {
                    Logger.RECIPE("[Fix] Found a recipe with 0 inputs, yet had a valid output.");
                    Logger.RECIPE(
                            "[Fix] Error found while adding a recipe for: " + resultItem != null
                                    ? resultItem.getDisplayName()
                                    : "Bad Output Item" + " | Please report this issue on Github.");
                    RegistrationHandler.recipesFailed++;
                    return;
                }

        Object[] o = new Object[] { slot_1, slot_2, slot_3, slot_4, slot_5, slot_6, slot_7, slot_8, slot_9 };

        try {
            int size = COMPAT_HANDLER.mRecipesToGenerate.size();
            COMPAT_HANDLER.mRecipesToGenerate.put(new InternalRecipeObject(o, resultItem, false));
            // Utils.LOG_WARNING("Success! Added a recipe for "+resultItem.getDisplayName());
            if (COMPAT_HANDLER.mRecipesToGenerate.size() > size) {
                if (!COMPAT_HANDLER.areInitItemsLoaded) {
                    RegistrationHandler.recipesSuccess++;
                } else {
                    LateRegistrationHandler.recipesSuccess++;
                }
            }
        } catch (RuntimeException k) {
            Logger.RECIPE(
                    "[Fix] Invalid Recipe detected for: " + resultItem != null ? resultItem.getUnlocalizedName()
                            : "INVALID OUTPUT ITEM");
            if (!COMPAT_HANDLER.areInitItemsLoaded) {
                RegistrationHandler.recipesFailed++;
            } else {
                LateRegistrationHandler.recipesFailed++;
            }
        }
    }

    public static void removeCraftingRecipe(Object x) {
        if (null == x) {
            return;
        }
        if (x instanceof String) {
            final Item R = ItemUtils.getItemFromFQRN((String) x);
            if (R != null) {
                x = R;
            } else {
                return;
            }
        }
        if ((x instanceof Item) || (x instanceof ItemStack)) {
            if (x instanceof Item) {
                final ItemStack r = new ItemStack((Item) x);
                Logger.RECIPE("Removing Recipe for " + r.getUnlocalizedName());
            } else {
                Logger.RECIPE("Removing Recipe for " + ((ItemStack) x).getUnlocalizedName());
            }
            if (x instanceof ItemStack) {
                final Item r = ((ItemStack) x).getItem();
                if (null != r) {
                    x = r;
                } else {
                    Logger.RECIPE("Recipe removal failed - Tell Alkalus.");
                    return;
                }
            }
            if (RecipeUtils.attemptRecipeRemoval((Item) x)) {
                Logger.RECIPE("Recipe removal successful");
                return;
            }
            Logger.RECIPE("Recipe removal failed - Tell Alkalus.");
        }
    }

    private static boolean attemptRecipeRemoval(final Item I) {
        Logger.RECIPE("Create list of recipes.");
        final List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
        final Iterator<IRecipe> items = recipes.iterator();
        Logger.RECIPE("Begin list iteration.");
        while (items.hasNext()) {
            final ItemStack is = items.next().getRecipeOutput();
            if ((is != null) && (is.getItem() == I)) {
                items.remove();
                Logger.RECIPE("Remove a recipe with " + I.getUnlocalizedName() + " as output.");
                continue;
            }
        }
        Logger.RECIPE("All recipes should be gone?");
        if (!items.hasNext()) {
            Logger.RECIPE("We iterated once, let's try again to double check.");
            for (IRecipe recipe : recipes) {
                final ItemStack is = recipe.getRecipeOutput();
                if ((is != null) && (is.getItem() == I)) {
                    items.remove();
                    Logger.RECIPE("REMOVING MISSED RECIPE - RECHECK CONSTRUCTORS");
                    return true;
                }
            }
            Logger.RECIPE("Should be all gone now after double checking, so return true.");
            return true;
        }
        Logger.RECIPE("Return false, because something went wrong.");
        return false;
    }

    public static boolean addShapedGregtechRecipe(final Object InputItem1, final Object InputItem2,
            final Object InputItem3, final Object InputItem4, final Object InputItem5, final Object InputItem6,
            final Object InputItem7, final Object InputItem8, final Object InputItem9, final ItemStack OutputItem) {

        Object[] o = { InputItem1, InputItem2, InputItem3, InputItem4, InputItem5, InputItem6, InputItem7, InputItem8,
                InputItem9 };

        if (gtPlusPlus.GTplusplus.CURRENT_LOAD_PHASE != GTplusplus.INIT_PHASE.POST_INIT) {
            CORE.crash(
                    "Load Phase " + gtPlusPlus.GTplusplus.CURRENT_LOAD_PHASE
                            + " should be "
                            + GTplusplus.INIT_PHASE.POST_INIT
                            + ". Unable to register recipe.");
        }

        int size = COMPAT_HANDLER.mGtRecipesToGenerate.size();
        COMPAT_HANDLER.mGtRecipesToGenerate.put(new InternalRecipeObject(o, OutputItem, true));

        if (COMPAT_HANDLER.mGtRecipesToGenerate.size() > size) {
            if (!COMPAT_HANDLER.areInitItemsLoaded) {
                RegistrationHandler.recipesSuccess++;
            } else {
                LateRegistrationHandler.recipesSuccess++;
            }
            return true;
        }
        return false;
    }

    public static boolean addShapelessGregtechRecipe(final Object[] inputItems, final ItemStack OutputItem) {
        // Catch Invalid Recipes
        if (inputItems.length > 9 || inputItems.length < 1) {
            if (OutputItem != null) {
                Logger.RECIPE(
                        "[Fix] Invalid input array for shapeless recipe, which should output "
                                + OutputItem.getDisplayName());
            }
            return false;
        }
        // let gregtech handle shapeless recipes.
        if (GT_ModHandler.addShapelessCraftingRecipe(OutputItem, inputItems)) {
            return true;
        }
        return false;
    }

    public static boolean generateMortarRecipe(ItemStack aStack, ItemStack aOutput) {
        return RecipeUtils.addShapedGregtechRecipe(
                aStack,
                null,
                null,
                CI.craftingToolMortar,
                null,
                null,
                null,
                null,
                null,
                aOutput);
    }

    public static String[] getRecipeInfo(GT_Recipe m) {
        if (m == null) {
            return new String[] {};
        }
        AutoMap<String> result = new AutoMap<>();
        result.put(m.toString());
        result.put("Input " + ItemUtils.getArrayStackNames(m.mInputs));
        result.put("Output " + ItemUtils.getArrayStackNames(m.mOutputs));
        result.put("Input " + ItemUtils.getArrayStackNames(m.mFluidInputs));
        result.put("Output " + ItemUtils.getArrayStackNames(m.mFluidOutputs));
        result.put("Can be buffered? " + m.mCanBeBuffered);
        result.put("Duration: " + m.mDuration);
        result.put("EU/t: " + m.mEUt);
        result.put("Is Hidden? " + m.mHidden);
        result.put("Is Enabled? " + m.mEnabled);
        result.put("Special Value: " + m.mSpecialValue);
        result.put("=====================================");
        String s[] = result.toArray();
        return s;
    }

    public static class InternalRecipeObject implements RunnableWithInfo<String> {

        final ItemStack mOutput;
        final ShapedOreRecipe mRecipe;
        public final boolean isValid;

        public InternalRecipeObject(Object[] aInputs, ItemStack aOutput, boolean gtRecipe) {
            Logger.RECIPE("===================================");
            mOutput = aOutput != null ? aOutput.copy() : null;
            Object[] aFiltered = new Object[9];
            int aValid = 0;
            for (Object o : aInputs) {
                if (o instanceof ItemStack) {
                    aFiltered[aValid++] = o;
                } else if (o instanceof Item) {
                    aFiltered[aValid++] = ItemUtils.getSimpleStack((Item) o);
                } else if (o instanceof Block) {
                    aFiltered[aValid++] = ItemUtils.getSimpleStack((Block) o);
                } else if (o instanceof String) {
                    aFiltered[aValid++] = o;
                } else if (o == null) {
                    aFiltered[aValid++] = null;
                } else {
                    Logger.RECIPE("Cleaned a " + o.getClass().getSimpleName() + " from recipe input.");
                }
            }

            int validCounter = 0, invalidCounter = 0;
            for (Object p : aFiltered) {
                if (p instanceof ItemStack) {
                    validCounter++;
                } else if (p instanceof Item) {
                    validCounter++;
                } else if (p instanceof Block) {
                    validCounter++;
                } else if (p instanceof String) {
                    validCounter++;
                } else if (p == null) {
                    validCounter++;
                } else {
                    invalidCounter++;
                }
            }

            Logger.RECIPE("Using " + validCounter + " valid inputs and " + invalidCounter + " invalid inputs.");
            ShapedRecipe r = new ShapedRecipe(aFiltered, mOutput);
            if (r != null && r.mRecipe != null) {
                isValid = true;
            } else {
                isValid = false;
            }
            mRecipe = r != null ? r.mRecipe : null;
        }

        @Override
        public void run() {
            if (this.isValid) {
                GameRegistry.addRecipe(mRecipe);
            } else {
                Logger.RECIPE(
                        "[Fix] Invalid shapped recipe outputting "
                                + (mOutput != null ? mOutput.getDisplayName() : "Bad Output Item"));
            }
        }

        @Override
        public String getInfoData() {
            if (mOutput != null && mOutput instanceof ItemStack) {
                return ((ItemStack) mOutput).getDisplayName();
            }
            return "";
        }
    }

    public static boolean removeRecipeByOutput(ItemStack aOutput) {
        return removeRecipeByOutput(aOutput, true, false, false);
    }

    public static boolean removeRecipeByOutput(ItemStack aOutput, boolean aIgnoreNBT,
            boolean aNotRemoveShapelessRecipes, boolean aOnlyRemoveNativeHandlers) {
        if (aOutput == null) {
            return false;
        } else {
            boolean rReturn = false;
            ArrayList<IRecipe> tList = (ArrayList) CraftingManager.getInstance().getRecipeList();
            aOutput = GT_OreDictUnificator.get(aOutput);
            int tList_sS = tList.size();

            for (int i = 0; i < tList_sS; ++i) {
                IRecipe tRecipe = (IRecipe) tList.get(i);
                if (!aNotRemoveShapelessRecipes
                        || !(tRecipe instanceof ShapelessRecipes) && !(tRecipe instanceof ShapelessOreRecipe)) {
                    if (aOnlyRemoveNativeHandlers) {
                        if (!gregtech.api.util.GT_ModHandler.sNativeRecipeClasses
                                .contains(tRecipe.getClass().getName())) {
                            continue;
                        }
                    } else if (gregtech.api.util.GT_ModHandler.sSpecialRecipeClasses
                            .contains(tRecipe.getClass().getName())) {
                                continue;
                            }

                    ItemStack tStack = tRecipe.getRecipeOutput();
                    if (GT_Utility.areStacksEqual(GT_OreDictUnificator.get(tStack), aOutput, aIgnoreNBT)) {
                        tList.remove(i--);
                        tList_sS = tList.size();
                        rReturn = true;
                    }
                }
            }

            return rReturn;
        }
    }

    public static void addSmeltingRecipe(ItemStack aStackInput, ItemStack aStackOutput, float aXpGained) {

        GameRegistry.addSmelting(aStackInput, aStackOutput, aXpGained);
    }

    public static boolean addShapedRecipe(Object Input_1, Object Input_2, Object Input_3, Object Input_4,
            Object Input_5, Object Input_6, Object Input_7, Object Input_8, Object Input_9, ItemStack aOutputStack) {
        return addShapedRecipe(
                new Object[] { Input_1, Input_2, Input_3, Input_4, Input_5, Input_6, Input_7, Input_8, Input_9 },
                aOutputStack);
    }

    private static boolean addShapedRecipe(Object[] Inputs, ItemStack aOutputStack) {
        Object[] Slots = new Object[9];

        String aFullString = "";
        String aFullStringExpanded = "abcdefghi";

        for (int i = 0; i < 9; i++) {
            Object o = Inputs[i];

            if (o instanceof ItemStack) {
                Slots[i] = ItemUtils.getSimpleStack((ItemStack) o, 1);
                aFullString += aFullStringExpanded.charAt(i);
            } else if (o instanceof Item) {
                Slots[i] = ItemUtils.getSimpleStack((Item) o, 1);
                aFullString += aFullStringExpanded.charAt(i);
            } else if (o instanceof Block) {
                Slots[i] = ItemUtils.getSimpleStack((Block) o, 1);
                aFullString += aFullStringExpanded.charAt(i);
            } else if (o instanceof String) {
                Slots[i] = o;
                aFullString += aFullStringExpanded.charAt(i);
            } else if (o instanceof ItemData aData) {
                ItemStack aStackFromGT = ItemUtils.getOrePrefixStack(aData.mPrefix, aData.mMaterial.mMaterial, 1);
                Slots[i] = aStackFromGT;
                aFullString += aFullStringExpanded.charAt(i);
            } else if (o == null) {
                Slots[i] = null;
                aFullString += " ";
            } else {
                Slots[i] = null;
                Logger.INFO("Cleaned a " + o.getClass().getSimpleName() + " from recipe input.");
                Logger.INFO("ERROR");
                CORE.crash("Bad Shaped Recipe.");
            }
        }
        Logger.RECIPE("Using String: " + aFullString);

        String aRow1 = aFullString.substring(0, 3);
        String aRow2 = aFullString.substring(3, 6);
        String aRow3 = aFullString.substring(6, 9);
        Logger.RECIPE("" + aRow1);
        Logger.RECIPE("" + aRow2);
        Logger.RECIPE("" + aRow3);

        String[] aStringData = new String[] { aRow1, aRow2, aRow3 };
        Object[] aDataObject = new Object[19];
        aDataObject[0] = aStringData;
        int aIndex = 0;

        for (int u = 1; u < 20; u += 2) {
            if (aIndex == 9) {
                break;
            }
            if (aFullString.charAt(aIndex) != (' ')) {
                aDataObject[u] = aFullString.charAt(aIndex);
                aDataObject[u + 1] = Slots[aIndex];
                Logger.INFO(
                        "(" + aIndex
                                + ") "
                                + aFullString.charAt(aIndex)
                                + " | "
                                + (Slots[aIndex] instanceof ItemStack ? ItemUtils.getItemName((ItemStack) Slots[aIndex])
                                        : Slots[aIndex] instanceof String ? (String) Slots[aIndex] : "Unknown"));
            }
            aIndex++;
        }

        Logger.RECIPE("Data Size: " + aDataObject.length);
        aDataObject = ArrayUtils.removeNulls(aDataObject);
        Logger.RECIPE("Clean Size: " + aDataObject.length);
        Logger.RECIPE("ArrayData: " + Arrays.toString(aDataObject));

        ShapedOreRecipe aRecipe = new ShapedOreRecipe(aOutputStack, aDataObject);

        int size = COMPAT_HANDLER.mRecipesToGenerate.size();
        COMPAT_HANDLER.mRecipesToGenerate.put(new InternalRecipeObject2(aRecipe));
        if (COMPAT_HANDLER.mRecipesToGenerate.size() > size) {
            if (!COMPAT_HANDLER.areInitItemsLoaded) {
                RegistrationHandler.recipesSuccess++;
            } else {
                LateRegistrationHandler.recipesSuccess++;
            }
            return true;
        }
        return false;
    }

    public static class InternalRecipeObject2 implements RunnableWithInfo<String> {

        final ItemStack mOutput;
        final ShapedOreRecipe mRecipe;
        final boolean isValid;

        public InternalRecipeObject2(ShapedOreRecipe aRecipe) {
            mRecipe = aRecipe;
            mOutput = aRecipe.getRecipeOutput();
            if (mOutput != null) {
                this.isValid = true;
            } else {
                this.isValid = false;
            }
        }

        @Override
        public void run() {
            if (this.isValid) {
                GameRegistry.addRecipe(mRecipe);
            } else {
                Logger.INFO(
                        "[Fix] Invalid shapped recipe outputting " + mOutput != null ? mOutput.getDisplayName()
                                : "Bad Output Item");
            }
        }

        @Override
        public String getInfoData() {
            if (mOutput != null) {
                return mOutput.getDisplayName();
            }
            return "";
        }
    }
}
