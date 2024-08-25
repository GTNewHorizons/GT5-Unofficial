package gregtech.api.util;

import static gregtech.GT_Mod.GT_FML_LOGGER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fluids.FluidStack;

import cpw.mods.fml.common.FMLCommonHandler;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.util.GT_Recipe.GT_Recipe_AssemblyLine;

public class GT_AssemblyLineUtils {

    /**
     * A cache of Recipes using the Output as Key.
     */
    private static final HashMap<GT_ItemStack, GT_Recipe_AssemblyLine> sRecipeCacheByOutput = new HashMap<>();
    /**
     * A cache of Recipes using the Recipe Hash String as Key.
     */
    private static final HashMap<String, GT_Recipe_AssemblyLine> sRecipeCacheByRecipeHash = new HashMap<>();

    /**
     * Checks the DataStick for deprecated/invalid recipes, updating them as required.
     *
     * @param aDataStick - The DataStick to process
     * @return Is this DataStick now valid with a current recipe?
     */
    public static GT_Recipe_AssemblyLine processDataStick(ItemStack aDataStick) {
        if (!isItemDataStick(aDataStick)) {
            return null;
        }
        if (doesDataStickNeedUpdate(aDataStick)) {
            ItemStack aStickOutput = getDataStickOutput(aDataStick);
            if (aStickOutput != null) {
                GT_Recipe_AssemblyLine aIntendedRecipe = findAssemblyLineRecipeByOutput(aStickOutput);
                if (aIntendedRecipe != null && setAssemblyLineRecipeOnDataStick(aDataStick, aIntendedRecipe))
                    return aIntendedRecipe;
            }
        }
        return null;
    }

    /**
     * Finds an Assembly Line recipe from a DataStick.
     *
     * @param aDataStick - The DataStick to check.
     * @return The GT_Recipe_AssemblyLine recipe contained on the DataStick, if any.
     */
    public static GT_Recipe_AssemblyLine findAssemblyLineRecipeFromDataStick(ItemStack aDataStick) {
        return findAssemblyLineRecipeFromDataStick(aDataStick, false).getRecipe();
    }

    /**
     * Finds an Assembly Line recipe from a DataStick.
     *
     * @param aDataStick         - The DataStick to check.
     * @param aReturnBuiltRecipe - Do we return a GT_Recipe_AssemblyLine built from the data on the Data Stick instead
     *                           of searching the Recipe Map?
     * @return The GT_Recipe_AssemblyLine recipe contained on the DataStick, if any.
     */
    @Nonnull
    public static LookupResult findAssemblyLineRecipeFromDataStick(ItemStack aDataStick, boolean aReturnBuiltRecipe) {
        if (!isItemDataStick(aDataStick) || !doesDataStickHaveOutput(aDataStick)) {
            return LookupResultType.INVALID_STICK.getResult();
        }
        List<ItemStack> aInputs = new ArrayList<>(16);
        ItemStack aOutput = getDataStickOutput(aDataStick);
        List<List<ItemStack>> mOreDictAlt = new ArrayList<>(16);
        List<FluidStack> aFluidInputs = new ArrayList<>(4);

        NBTTagCompound aTag = aDataStick.getTagCompound();
        if (aTag == null) {
            return LookupResultType.INVALID_STICK.getResult();
        }

        // Get From Cache
        if (doesDataStickHaveRecipeHash(aDataStick)) {
            GT_Recipe_AssemblyLine aRecipeFromCache = sRecipeCacheByRecipeHash.get(getHashFromDataStack(aDataStick));
            if (aRecipeFromCache != null && GT_Utility.areStacksEqual(aOutput, aRecipeFromCache.mOutput)) {
                return LookupResultType.VALID_STACK_AND_VALID_HASH.getResult(aRecipeFromCache);
            } // else: no cache, or the old recipe run into a hash collision with a different new recipe
        }

        for (int i = 0; i < 16; i++) {
            int count = aTag.getInteger("a" + i);
            if (!aTag.hasKey("" + i) && count <= 0) {
                continue;
            }

            List<ItemStack> tAltCurrent = new ArrayList<>();
            for (int j = 0; j < count; j++) {
                ItemStack tLoaded = GT_Utility.loadItem(aTag, "a" + i + ":" + j);
                if (tLoaded == null) {
                    continue;
                }
                tAltCurrent.add(tLoaded);
                if (GT_Values.D1) {
                    GT_FML_LOGGER.info("Item Alt " + i + " : " + tLoaded.getUnlocalizedName());
                }
            }
            mOreDictAlt.add(tAltCurrent);
            ItemStack tLoaded = GT_Utility.loadItem(aTag, "" + i);
            if (tLoaded == null) {
                continue;
            }
            aInputs.add(tLoaded);
            if (GT_Values.D1) {
                GT_FML_LOGGER.info("Item " + i + " : " + tLoaded.getUnlocalizedName());
            }
        }

        if (GT_Values.D1) {
            GT_FML_LOGGER.info("All Items done, start fluid check");
        }
        for (int i = 0; i < 4; i++) {
            if (!aTag.hasKey("f" + i)) continue;
            FluidStack tLoaded = GT_Utility.loadFluid(aTag, "f" + i);
            if (tLoaded == null) continue;
            aFluidInputs.add(tLoaded);
            if (GT_Values.D1) {
                GT_FML_LOGGER.info("Fluid " + i + " " + tLoaded.getUnlocalizedName());
            }
        }
        if (!aTag.hasKey("output") || !aTag.hasKey("time")
            || aTag.getInteger("time") <= 0
            || !aTag.hasKey("eu")
            || !GT_Utility.isStackValid(aOutput)) {
            return LookupResultType.INVALID_STICK.getResult();
        }
        if (GT_Values.D1) {
            GT_FML_LOGGER.info("Found Data Stick recipe");
        }

        int aTime = aTag.getInteger("time");
        int aEU = aTag.getInteger("eu");

        // Try build a recipe instance
        if (aReturnBuiltRecipe) {
            return LookupResultType.VALID_STACK_AND_VALID_HASH.getResult(
                new GT_Recipe_AssemblyLine(
                    null,
                    0,
                    aInputs.toArray(new ItemStack[0]),
                    aFluidInputs.toArray(new FluidStack[0]),
                    aOutput,
                    aTime,
                    aEU));
        }

        for (GT_Recipe_AssemblyLine aRecipe : GT_Recipe.GT_Recipe_AssemblyLine.sAssemblylineRecipes) {
            if (aRecipe.mEUt != aEU || aRecipe.mDuration != aTime) continue;
            if (!GT_Utility.areStacksEqual(aOutput, aRecipe.mOutput, true)) continue;
            if (!GT_Utility.areStackListsEqual(Arrays.asList(aRecipe.mInputs), aInputs, false, true)) continue;
            if (!Objects.equals(Arrays.asList(aRecipe.mFluidInputs), aFluidInputs)) continue;
            if (!areStacksEqual(aRecipe.mOreDictAlt, mOreDictAlt)) continue;

            // Cache it
            String aRecipeHash = generateRecipeHash(aRecipe);
            sRecipeCacheByRecipeHash.put(aRecipeHash, aRecipe);
            sRecipeCacheByOutput.put(new GT_ItemStack(aRecipe.mOutput), aRecipe);
            if (doesDataStickHaveRecipeHash(aDataStick)) {
                String aStickHash = getHashFromDataStack(aDataStick);
                if (aRecipeHash.equals(aStickHash))
                    return LookupResultType.VALID_STACK_AND_VALID_HASH.getResult(aRecipe);
            }
            return LookupResultType.VALID_STACK_AND_VALID_RECIPE.getResult(aRecipe);
        }
        return LookupResultType.VALID_STACK_BUT_INVALID_RECIPE.getResult();
    }

    private static boolean areStacksEqual(ItemStack[][] lhs, List<List<ItemStack>> rhs) {
        for (int i = 0; i < lhs.length; i++) {
            if (!areStacksEqual(lhs[i], rhs.get(i))) return false;
        }
        return true;
    }

    private static boolean areStacksEqual(ItemStack[] lhs, List<ItemStack> rhs) {
        return lhs == null ? rhs.isEmpty()
            : !rhs.isEmpty() && GT_Utility.areStackListsEqual(Arrays.asList(lhs), rhs, false, true);
    }

    /**
     * Finds a GT_Recipe_AssemblyLine based on the expected output ItemStack.
     *
     * @param aOutput - The Output of a GT_Recipe_AssemblyLine.
     * @return First found GT_Recipe_AssemblyLine with matching output.
     */
    public static GT_Recipe_AssemblyLine findAssemblyLineRecipeByOutput(ItemStack aOutput) {
        if (aOutput == null) {
            return null;
        }

        // Check the cache
        GT_ItemStack aCacheStack = new GT_ItemStack(aOutput);
        GT_Recipe_AssemblyLine aRecipeFromCache = sRecipeCacheByOutput.get(aCacheStack);
        if (aRecipeFromCache != null) {
            return aRecipeFromCache;
        }

        // Iterate all recipes and return the first matching based on Output.
        for (GT_Recipe_AssemblyLine aRecipe : GT_Recipe.GT_Recipe_AssemblyLine.sAssemblylineRecipes) {
            ItemStack aRecipeOutput = aRecipe.mOutput;
            if (GT_Utility.areStacksEqual(aRecipeOutput, aOutput)) {
                // Cache it to prevent future iterations of all recipes
                sRecipeCacheByOutput.put(aCacheStack, aRecipe);
                sRecipeCacheByRecipeHash.put(generateRecipeHash(aRecipe), aRecipe);
                return aRecipe;
            }
        }
        return null;
    }

    /**
     * @param aRecipe - The recipe to generate a Recipe Hash String from.
     * @return The Recipe Hash String.
     */
    public static String generateRecipeHash(GT_Recipe_AssemblyLine aRecipe) {
        String aHash = "Invalid.Recipe.Hash";
        if (aRecipe != null) {
            aHash = "Hash." + aRecipe.getPersistentHash();
        }
        return aHash;
    }

    /**
     * @param aRecipe - The recipe to add to internal caches
     * @throws IllegalArgumentException if given recipe collide with any existing recipe in the cache
     */
    public static void addRecipeToCache(GT_Recipe_AssemblyLine aRecipe) {
        if (aRecipe != null) {
            String aHash = "Hash." + aRecipe.getPersistentHash();
            GT_Recipe_AssemblyLine existing = sRecipeCacheByOutput.put(new GT_ItemStack(aRecipe.mOutput), aRecipe);
            if (existing != null) throw new IllegalArgumentException("Duplicate assline recipe for " + aRecipe.mOutput);
            existing = sRecipeCacheByRecipeHash.put(aHash, aRecipe);
            if (existing != null && !existing.equals(aRecipe))
                throw new IllegalArgumentException("Recipe hash collision for " + aRecipe + " and " + existing);
        }
    }

    /**
     * @param aHash - Recipe hash String, may be null but will just be treated as invalid.
     * @return Is this Recipe Hash String valid?
     */
    public static boolean isValidHash(String aHash) {
        if (aHash != null && aHash.length() > 0) {
            // persistent hash can never be 0
            return !aHash.equals("Invalid.Recipe.Hash") && !aHash.equals("Hash.0");
        }
        return false;
    }

    /**
     * @param aStack - The ItemStack to check.
     * @return Is this ItemStack a Data Stick?
     */
    public static boolean isItemDataStick(ItemStack aStack) {
        return GT_Utility.isStackValid(aStack) && ItemList.Tool_DataStick.isStackEqual(aStack, false, true);
    }

    /**
     * @param aDataStick - The Data Stick to check.
     * @return Does this Data Stick have a valid output ItemStack?
     */
    public static boolean doesDataStickHaveOutput(ItemStack aDataStick) {
        return isItemDataStick(aDataStick) && aDataStick.hasTagCompound()
            && aDataStick.getTagCompound()
                .hasKey("output");
    }

    /**
     * @param aDataStick - The Data Stick to check.
     * @return Does this Data Stick need recipe data updated.
     */
    public static boolean doesDataStickNeedUpdate(ItemStack aDataStick) {
        if (isItemDataStick(aDataStick) && doesDataStickHaveRecipeHash(aDataStick)) {
            String aStickHash = getHashFromDataStack(aDataStick);
            if (isValidHash(aStickHash) && doesDataStickHaveOutput(aDataStick)) {
                ItemStack aStickOutput = getDataStickOutput(aDataStick);
                GT_Recipe_AssemblyLine aIntendedRecipe = findAssemblyLineRecipeByOutput(aStickOutput);
                return !aStickHash.equals(generateRecipeHash(aIntendedRecipe));
            }
        }
        return true;
    }

    /**
     * @param aDataStick - The Data Stick to check.
     * @return Does this have a Recipe Hash String at all?
     */
    public static boolean doesDataStickHaveRecipeHash(ItemStack aDataStick) {
        if (isItemDataStick(aDataStick) && aDataStick.hasTagCompound()) {
            NBTTagCompound aNBT = aDataStick.getTagCompound();
            return aNBT.hasKey("Data.Recipe.Hash") && !aNBT.getString("Data.Recipe.Hash")
                .equals("Hash.0");
        }
        return false;
    }

    /**
     * Get the Output ItemStack from a Data Stick.
     *
     * @param aDataStick - The Data Stick to check.
     * @return Output ItemStack contained on the Data Stick.
     */
    public static ItemStack getDataStickOutput(ItemStack aDataStick) {
        if (doesDataStickHaveOutput(aDataStick)) {
            return GT_Utility.loadItem(aDataStick.getTagCompound(), "output");
        }
        return null;
    }

    /**
     * @param aDataStick - The Data Stick to process.
     * @return The stored Recipe Hash String on the Data Stick, will return an invalid Hash if one is not found.
     *         <p>
     *         The hash will be guaranteed to pass isValidHash().
     *         <p>
     *         Will not return Null.
     */
    public static String getHashFromDataStack(ItemStack aDataStick) {
        if (isItemDataStick(aDataStick) && aDataStick.hasTagCompound()) {
            NBTTagCompound aNBT = aDataStick.getTagCompound();
            if (aNBT.hasKey("Data.Recipe.Hash", NBT.TAG_STRING)) {
                String hash = aNBT.getString("Data.Recipe.Hash");
                if (isValidHash(hash)) return hash;
            }
        }
        return "Invalid.Recipe.Hash";
    }

    /**
     *
     * @param aDataStick  - The Data Stick to update.
     * @param aRecipeHash - The Recipe Hash String to update with.
     * @return Did we update the Recipe Hash String on the Data Stick?
     */
    public static boolean setRecipeHashOnDataStick(ItemStack aDataStick, String aRecipeHash) {
        if (isItemDataStick(aDataStick) && aDataStick.hasTagCompound()) {
            NBTTagCompound aNBT = aDataStick.getTagCompound();
            aNBT.setString("Data.Recipe.Hash", aRecipeHash);
            aDataStick.setTagCompound(aNBT);
            return true;
        }
        return false;
    }

    /**
     *
     * @param aDataStick - The Data Stick to update.
     * @param aNewRecipe - The New GT_Recipe_AssemblyLine recipe to update it with.
     * @return Did we set the new recipe data & Recipe Hash String on the Data Stick?
     */
    public static boolean setAssemblyLineRecipeOnDataStick(ItemStack aDataStick, GT_Recipe_AssemblyLine aNewRecipe) {
        return setAssemblyLineRecipeOnDataStick(aDataStick, aNewRecipe, true);
    }

    public static boolean setAssemblyLineRecipeOnDataStick(ItemStack aDataStick, GT_Recipe_AssemblyLine aNewRecipe,
        boolean setUpdateTime) {
        if (isItemDataStick(aDataStick) && aNewRecipe.mOutput != null) {
            String s = aNewRecipe.mOutput.getDisplayName();
            if (FMLCommonHandler.instance()
                .getEffectiveSide()
                .isServer()) {
                s = GT_Assemblyline_Server.lServerNames.get(aNewRecipe.mOutput.getDisplayName());
                if (s == null) {
                    s = aNewRecipe.mOutput.getDisplayName();
                }
            }

            String aHash = generateRecipeHash(aNewRecipe);
            if (GT_Values.D1) {
                GT_Recipe_AssemblyLine aOldRecipe = findAssemblyLineRecipeFromDataStick(aDataStick, true).recipe;
                GT_FML_LOGGER.info(
                    "Updating data stick: " + aDataStick.getDisplayName()
                        + " | Old Recipe Hash: "
                        + generateRecipeHash(aOldRecipe)
                        + ", New Recipe Hash: "
                        + aHash);
            }

            String author = "Assembling Line Recipe Generator";
            String displayName = null;
            if (aDataStick.hasTagCompound()) {
                NBTTagCompound tag = aDataStick.getTagCompound();
                if (tag.hasKey("author", NBT.TAG_STRING)) {
                    author = tag.getString("author");
                }
                if (tag.hasKey("display", NBT.TAG_COMPOUND)) {
                    NBTTagCompound displayTag = tag.getCompoundTag("display");
                    if (displayTag.hasKey("Name", NBT.TAG_STRING)) displayName = displayTag.getString("Name");
                }
            }

            // remove possible old NBTTagCompound
            aDataStick.setTagCompound(new NBTTagCompound());
            if (displayName != null) aDataStick.setStackDisplayName(displayName);
            if (GT_Values.D1) {
                GT_Utility.ItemNBT.setBookTitle(aDataStick, s + " Construction Data (" + aHash + ")");
            } else {
                GT_Utility.ItemNBT.setBookTitle(aDataStick, s + " Construction Data");
            }

            NBTTagCompound tNBT = aDataStick.getTagCompound();
            if (tNBT == null) {
                tNBT = new NBTTagCompound();
            }

            tNBT.setTag("output", aNewRecipe.mOutput.writeToNBT(new NBTTagCompound()));
            tNBT.setInteger("time", aNewRecipe.mDuration);
            tNBT.setInteger("eu", aNewRecipe.mEUt);
            tNBT.setString("author", author);
            NBTTagList tNBTList = new NBTTagList();
            tNBTList.appendTag(
                new NBTTagString(
                    "Construction plan for " + aNewRecipe.mOutput.stackSize
                        + " "
                        + s
                        + ". Needed EU/t: "
                        + aNewRecipe.mEUt
                        + " Production time: "
                        + (aNewRecipe.mDuration / 20)));
            for (int i = 0; i < aNewRecipe.mInputs.length; i++) {
                boolean hasSetOreDictAlt = false;

                if (aNewRecipe.mOreDictAlt[i] != null && aNewRecipe.mOreDictAlt[i].length > 0) {
                    tNBT.setInteger("a" + i, aNewRecipe.mOreDictAlt[i].length);
                    int count = 0;
                    StringBuilder tBuilder = new StringBuilder("Input Bus " + (i + 1) + ": ");
                    for (int j = 0; j < aNewRecipe.mOreDictAlt[i].length; j++) {
                        ItemStack tStack = aNewRecipe.mOreDictAlt[i][j];
                        if (tStack != null) {
                            tNBT.setTag("a" + i + ":" + j, tStack.writeToNBT(new NBTTagCompound()));

                            s = tStack.getDisplayName();
                            if (FMLCommonHandler.instance()
                                .getEffectiveSide()
                                .isServer()) {
                                s = GT_Assemblyline_Server.lServerNames.get(tStack.getDisplayName());
                                if (s == null) s = tStack.getDisplayName();
                            }

                            tBuilder.append(count == 0 ? "" : "\nOr ")
                                .append(tStack.stackSize)
                                .append(" ")
                                .append(s);
                            count++;
                        }
                    }
                    if (count > 0) {
                        tNBTList.appendTag(new NBTTagString(tBuilder.toString()));
                        hasSetOreDictAlt = true;
                    }
                }

                if (aNewRecipe.mInputs[i] != null) {
                    tNBT.setTag("" + i, aNewRecipe.mInputs[i].writeToNBT(new NBTTagCompound()));

                    if (!hasSetOreDictAlt) {
                        s = aNewRecipe.mInputs[i].getDisplayName();
                        if (FMLCommonHandler.instance()
                            .getEffectiveSide()
                            .isServer()) {
                            s = GT_Assemblyline_Server.lServerNames.get(aNewRecipe.mInputs[i].getDisplayName());
                            if (s == null) s = aNewRecipe.mInputs[i].getDisplayName();
                        }
                        tNBTList.appendTag(
                            new NBTTagString(
                                "Input Bus " + (i + 1) + ": " + aNewRecipe.mInputs[i].stackSize + " " + s));
                    }
                }
            }
            for (int i = 0; i < aNewRecipe.mFluidInputs.length; i++) {
                if (aNewRecipe.mFluidInputs[i] != null) {
                    tNBT.setTag("f" + i, aNewRecipe.mFluidInputs[i].writeToNBT(new NBTTagCompound()));

                    s = aNewRecipe.mFluidInputs[i].getLocalizedName();
                    if (FMLCommonHandler.instance()
                        .getEffectiveSide()
                        .isServer()) {
                        s = GT_Assemblyline_Server.lServerNames.get(aNewRecipe.mFluidInputs[i].getLocalizedName());
                        if (s == null) s = aNewRecipe.mFluidInputs[i].getLocalizedName();
                    }
                    tNBTList.appendTag(
                        new NBTTagString(
                            "Input Hatch " + (i + 1) + ": " + aNewRecipe.mFluidInputs[i].amount + "L " + s));
                }
            }
            tNBT.setTag("pages", tNBTList);
            if (setUpdateTime) tNBT.setLong("lastUpdate", System.currentTimeMillis());
            aDataStick.setTagCompound(tNBT);
            // Set recipe hash
            setRecipeHashOnDataStick(aDataStick, aHash);
            return true;
        }
        return false;
    }

    public enum LookupResultType {

        INVALID_STICK(true),
        VALID_STACK_BUT_INVALID_RECIPE(true),
        VALID_STACK_AND_VALID_RECIPE(false),
        VALID_STACK_AND_VALID_HASH(false);

        private final boolean recipeNull;
        private LookupResult singletonResult;

        LookupResultType(boolean recipeNull) {
            this.recipeNull = recipeNull;
        }

        public LookupResult getResult() {
            if (!recipeNull) throw new IllegalArgumentException("This result type require a nonnull recipe");
            if (singletonResult == null) singletonResult = new LookupResult(null, this);
            return singletonResult;
        }

        public LookupResult getResult(GT_Recipe_AssemblyLine recipe) {
            if ((recipe == null) != recipeNull)
                throw new IllegalArgumentException("This result type does not allow given input");
            return new LookupResult(recipe, this);
        }
    }

    public static class LookupResult {

        private final GT_Recipe_AssemblyLine recipe;
        private final LookupResultType type;

        LookupResult(GT_Recipe_AssemblyLine recipe, LookupResultType type) {
            this.recipe = recipe;
            this.type = type;
        }

        public GT_Recipe_AssemblyLine getRecipe() {
            return recipe;
        }

        public LookupResultType getType() {
            return type;
        }
    }
}
