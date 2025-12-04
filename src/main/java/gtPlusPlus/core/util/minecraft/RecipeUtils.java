package gtPlusPlus.core.util.minecraft;

import java.util.Arrays;
import java.util.Collections;
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
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.minecraft.ShapedRecipe;
import gtPlusPlus.core.handler.CompatHandler;
import gtPlusPlus.core.handler.Recipes.LateRegistrationHandler;
import gtPlusPlus.core.handler.Recipes.RegistrationHandler;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

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
                    "[Fix] Error found while adding a recipe for: " + resultItem != null ? resultItem.getDisplayName()
                        : "Bad Output Item" + " | Please report this issue on Github.");
                RegistrationHandler.recipesFailed++;
                return;
            }

        Object[] o = new Object[] { slot_1, slot_2, slot_3, slot_4, slot_5, slot_6, slot_7, slot_8, slot_9 };

        try {
            int size = CompatHandler.mRecipesToGenerate.size();
            CompatHandler.mRecipesToGenerate.add(new InternalRecipeObject(o, resultItem, false));
            // Utils.LOG_WARNING("Success! Added a recipe for "+resultItem.getDisplayName());
            if (CompatHandler.mRecipesToGenerate.size() > size) {
                if (!CompatHandler.areInitItemsLoaded) {
                    RegistrationHandler.recipesSuccess++;
                } else {
                    LateRegistrationHandler.recipesSuccess++;
                }
            }
        } catch (RuntimeException k) {
            Logger.RECIPE(
                "[Fix] Invalid Recipe detected for: " + resultItem != null ? resultItem.getUnlocalizedName()
                    : "INVALID OUTPUT ITEM");
            if (!CompatHandler.areInitItemsLoaded) {
                RegistrationHandler.recipesFailed++;
            } else {
                LateRegistrationHandler.recipesFailed++;
            }
        }
    }

    public static boolean addShapedGregtechRecipe(final Object InputItem1, final Object InputItem2,
        final Object InputItem3, final Object InputItem4, final Object InputItem5, final Object InputItem6,
        final Object InputItem7, final Object InputItem8, final Object InputItem9, final ItemStack OutputItem) {

        Object[] o = { InputItem1, InputItem2, InputItem3, InputItem4, InputItem5, InputItem6, InputItem7, InputItem8,
            InputItem9 };

        if (gtPlusPlus.GTplusplus.CURRENT_LOAD_PHASE != GTplusplus.INIT_PHASE.POST_INIT) {
            Logger.ERROR(
                "Load Phase " + gtPlusPlus.GTplusplus.CURRENT_LOAD_PHASE
                    + " should be "
                    + GTplusplus.INIT_PHASE.POST_INIT
                    + ". Unable to register recipe.");
            throw new IllegalStateException();
        }

        int size = CompatHandler.mGtRecipesToGenerate.size();
        CompatHandler.mGtRecipesToGenerate.add(new InternalRecipeObject(o, OutputItem, true));

        if (CompatHandler.mGtRecipesToGenerate.size() > size) {
            if (!CompatHandler.areInitItemsLoaded) {
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
        return GTModHandler.addShapelessCraftingRecipe(OutputItem, inputItems);
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
                    aFiltered[aValid++] = new ItemStack((Item) o);
                } else if (o instanceof Block) {
                    aFiltered[aValid++] = new ItemStack((Block) o);
                } else if (o instanceof String) {
                    aFiltered[aValid++] = o;
                } else if (o == null) {
                    aFiltered[aValid++] = null;
                } else {
                    Logger.RECIPE(
                        "Cleaned a " + o.getClass()
                            .getSimpleName() + " from recipe input.");
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
            isValid = r.mRecipe != null;
            mRecipe = r.mRecipe;
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
                return mOutput.getDisplayName();
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
            final List<IRecipe> allRecipes = CraftingManager.getInstance()
                .getRecipeList();
            aOutput = GTOreDictUnificator.get(aOutput);
            int size = allRecipes.size();
            for (int i = 0; i < size; ++i) {
                IRecipe recipe = allRecipes.get(i);
                if (!aNotRemoveShapelessRecipes
                    || !(recipe instanceof ShapelessRecipes) && !(recipe instanceof ShapelessOreRecipe)) {
                    if (aOnlyRemoveNativeHandlers) {
                        if (!GTModHandler.sNativeRecipeClasses.contains(
                            recipe.getClass()
                                .getName())) {
                            continue;
                        }
                    } else if (GTModHandler.sSpecialRecipeClasses.contains(
                        recipe.getClass()
                            .getName())) {
                                continue;
                            }

                    final ItemStack output = recipe.getRecipeOutput();
                    if (GTUtility.areStacksEqual(GTOreDictUnificator.get_nocopy(output), aOutput, aIgnoreNBT)) {
                        allRecipes.remove(i--);
                        size = allRecipes.size();
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

        StringBuilder aFullString = new StringBuilder();
        String aFullStringExpanded = "abcdefghi";

        for (int i = 0; i < 9; i++) {
            Object o = Inputs[i];

            if (o instanceof ItemStack) {
                Slots[i] = GTUtility.copyAmount(1, (ItemStack) o);
                aFullString.append(aFullStringExpanded.charAt(i));
            } else if (o instanceof Item) {
                Slots[i] = new ItemStack((Item) o, 1);
                aFullString.append(aFullStringExpanded.charAt(i));
            } else if (o instanceof Block) {
                Slots[i] = new ItemStack((Block) o, 1);
                aFullString.append(aFullStringExpanded.charAt(i));
            } else if (o instanceof String) {
                Slots[i] = o;
                aFullString.append(aFullStringExpanded.charAt(i));
            } else if (o instanceof ItemData aData) {
                ItemStack aStackFromGT = ItemUtils.getOrePrefixStack(aData.mPrefix, aData.mMaterial.mMaterial, 1);
                Slots[i] = aStackFromGT;
                aFullString.append(aFullStringExpanded.charAt(i));
            } else if (o == null) {
                Slots[i] = null;
                aFullString.append(" ");
            } else {
                Slots[i] = null;
                Logger.INFO(
                    "Cleaned a " + o.getClass()
                        .getSimpleName() + " from recipe input.");
                Logger.INFO("ERROR");
            }
        }
        Logger.RECIPE("Using String: " + aFullString);

        String aRow1 = aFullString.substring(0, 3);
        String aRow2 = aFullString.substring(3, 6);
        String aRow3 = aFullString.substring(6, 9);
        Logger.RECIPE(aRow1);
        Logger.RECIPE(aRow2);
        Logger.RECIPE(aRow3);

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
        List<Object> list = new ObjectArrayList<>(aDataObject);
        list.removeAll(Collections.singleton(null));
        aDataObject = list.toArray(new Object[0]);
        Logger.RECIPE("Clean Size: " + aDataObject.length);
        Logger.RECIPE("ArrayData: " + Arrays.toString(aDataObject));

        ShapedOreRecipe aRecipe = new ShapedOreRecipe(aOutputStack, aDataObject);

        int size = CompatHandler.mRecipesToGenerate.size();
        CompatHandler.mRecipesToGenerate.add(new InternalRecipeObject2(aRecipe));
        if (CompatHandler.mRecipesToGenerate.size() > size) {
            if (!CompatHandler.areInitItemsLoaded) {
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
            this.isValid = mOutput != null;
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
