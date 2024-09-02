package gregtech.api.util;

import static gregtech.GTMod.GT_FML_LOGGER;

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
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.objects.GTItemStack;
import gregtech.api.util.GTRecipe.RecipeAssemblyLine;

public class AssemblyLineUtils {

    /**
     * A cache of Recipes using the Output as Key.
     */
    private static final HashMap<GTItemStack, GTRecipe.RecipeAssemblyLine> sRecipeCacheByOutput = new HashMap<>();
    /**
     * A cache of Recipes using the Recipe Hash String as Key.
     */
    private static final HashMap<String, GTRecipe.RecipeAssemblyLine> sRecipeCacheByRecipeHash = new HashMap<>();

    /**
     * Checks the DataStick for deprecated/invalid recipes, updating them as required.
     *
     * @param aDataStick - The DataStick to process
     * @return Is this DataStick now valid with a current recipe?
     */
    public static GTRecipe.RecipeAssemblyLine processDataStick(ItemStack aDataStick) {
        if (!isItemDataStick(aDataStick)) {
            return null;
        }
        if (doesDataStickNeedUpdate(aDataStick)) {
            ItemStack aStickOutput = getDataStickOutput(aDataStick);
            if (aStickOutput != null) {
                GTRecipe.RecipeAssemblyLine aIntendedRecipe = findAssemblyLineRecipeByOutput(aStickOutput);
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
     * @return The GTRecipe_AssemblyLine recipe contained on the DataStick, if any.
     */
    public static RecipeAssemblyLine findAssemblyLineRecipeFromDataStick(ItemStack aDataStick) {
        return findAssemblyLineRecipeFromDataStick(aDataStick, false).getRecipe();
    }

    /**
     * Finds an Assembly Line recipe from a DataStick.
     *
     * @param aDataStick         - The DataStick to check.
     * @param aReturnBuiltRecipe - Do we return a GTRecipe_AssemblyLine built from the data on the Data Stick instead
     *                           of searching the Recipe Map?
     * @return The GTRecipe_AssemblyLine recipe contained on the DataStick, if any.
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
            GTRecipe.RecipeAssemblyLine aRecipeFromCache = sRecipeCacheByRecipeHash
                .get(getHashFromDataStack(aDataStick));
            if (aRecipeFromCache != null && GTUtility.areStacksEqual(aOutput, aRecipeFromCache.mOutput)) {
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
                ItemStack tLoaded = GTUtility.loadItem(aTag, "a" + i + ":" + j);
                if (tLoaded == null) {
                    continue;
                }
                tAltCurrent.add(tLoaded);
                if (GTValues.D1) {
                    GT_FML_LOGGER.info("Item Alt " + i + " : " + tLoaded.getUnlocalizedName());
                }
            }
            mOreDictAlt.add(tAltCurrent);
            ItemStack tLoaded = GTUtility.loadItem(aTag, "" + i);
            if (tLoaded == null) {
                continue;
            }
            aInputs.add(tLoaded);
            if (GTValues.D1) {
                GT_FML_LOGGER.info("Item " + i + " : " + tLoaded.getUnlocalizedName());
            }
        }

        if (GTValues.D1) {
            GT_FML_LOGGER.info("All Items done, start fluid check");
        }
        for (int i = 0; i < 4; i++) {
            if (!aTag.hasKey("f" + i)) continue;
            FluidStack tLoaded = GTUtility.loadFluid(aTag, "f" + i);
            if (tLoaded == null) continue;
            aFluidInputs.add(tLoaded);
            if (GTValues.D1) {
                GT_FML_LOGGER.info("Fluid " + i + " " + tLoaded.getUnlocalizedName());
            }
        }
        if (!aTag.hasKey("output") || !aTag.hasKey("time")
            || aTag.getInteger("time") <= 0
            || !aTag.hasKey("eu")
            || !GTUtility.isStackValid(aOutput)) {
            return LookupResultType.INVALID_STICK.getResult();
        }
        if (GTValues.D1) {
            GT_FML_LOGGER.info("Found Data Stick recipe");
        }

        int aTime = aTag.getInteger("time");
        int aEU = aTag.getInteger("eu");

        // Try build a recipe instance
        if (aReturnBuiltRecipe) {
            return LookupResultType.VALID_STACK_AND_VALID_HASH.getResult(
                new GTRecipe.RecipeAssemblyLine(
                    null,
                    0,
                    aInputs.toArray(new ItemStack[0]),
                    aFluidInputs.toArray(new FluidStack[0]),
                    aOutput,
                    aTime,
                    aEU));
        }

        for (GTRecipe.RecipeAssemblyLine aRecipe : RecipeAssemblyLine.sAssemblylineRecipes) {
            if (aRecipe.mEUt != aEU || aRecipe.mDuration != aTime) continue;
            if (!GTUtility.areStacksEqual(aOutput, aRecipe.mOutput, true)) continue;
            if (!GTUtility.areStackListsEqual(Arrays.asList(aRecipe.mInputs), aInputs, false, true)) continue;
            if (!Objects.equals(Arrays.asList(aRecipe.mFluidInputs), aFluidInputs)) continue;
            if (!areStacksEqual(aRecipe.mOreDictAlt, mOreDictAlt)) continue;

            // Cache it
            String aRecipeHash = generateRecipeHash(aRecipe);
            sRecipeCacheByRecipeHash.put(aRecipeHash, aRecipe);
            sRecipeCacheByOutput.put(new GTItemStack(aRecipe.mOutput), aRecipe);
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
            : !rhs.isEmpty() && GTUtility.areStackListsEqual(Arrays.asList(lhs), rhs, false, true);
    }

    /**
     * Finds a GTRecipe_AssemblyLine based on the expected output ItemStack.
     *
     * @param aOutput - The Output of a GTRecipe_AssemblyLine.
     * @return First found GTRecipe_AssemblyLine with matching output.
     */
    public static GTRecipe.RecipeAssemblyLine findAssemblyLineRecipeByOutput(ItemStack aOutput) {
        if (aOutput == null) {
            return null;
        }

        // Check the cache
        GTItemStack aCacheStack = new GTItemStack(aOutput);
        RecipeAssemblyLine aRecipeFromCache = sRecipeCacheByOutput.get(aCacheStack);
        if (aRecipeFromCache != null) {
            return aRecipeFromCache;
        }

        // Iterate all recipes and return the first matching based on Output.
        for (RecipeAssemblyLine aRecipe : GTRecipe.RecipeAssemblyLine.sAssemblylineRecipes) {
            ItemStack aRecipeOutput = aRecipe.mOutput;
            if (GTUtility.areStacksEqual(aRecipeOutput, aOutput)) {
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
    public static String generateRecipeHash(GTRecipe.RecipeAssemblyLine aRecipe) {
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
    public static void addRecipeToCache(RecipeAssemblyLine aRecipe) {
        if (aRecipe != null) {
            String aHash = "Hash." + aRecipe.getPersistentHash();
            GTRecipe.RecipeAssemblyLine existing = sRecipeCacheByOutput.put(new GTItemStack(aRecipe.mOutput), aRecipe);
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
        return GTUtility.isStackValid(aStack) && ItemList.Tool_DataStick.isStackEqual(aStack, false, true);
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
                GTRecipe.RecipeAssemblyLine aIntendedRecipe = findAssemblyLineRecipeByOutput(aStickOutput);
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
            return GTUtility.loadItem(aDataStick.getTagCompound(), "output");
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
     * @param aNewRecipe - The New GTRecipe_AssemblyLine recipe to update it with.
     * @return Did we set the new recipe data & Recipe Hash String on the Data Stick?
     */
    public static boolean setAssemblyLineRecipeOnDataStick(ItemStack aDataStick,
        GTRecipe.RecipeAssemblyLine aNewRecipe) {
        return setAssemblyLineRecipeOnDataStick(aDataStick, aNewRecipe, true);
    }

    public static boolean setAssemblyLineRecipeOnDataStick(ItemStack aDataStick, GTRecipe.RecipeAssemblyLine aNewRecipe,
        boolean setUpdateTime) {
        if (isItemDataStick(aDataStick) && aNewRecipe.mOutput != null) {
            String s = aNewRecipe.mOutput.getDisplayName();
            if (FMLCommonHandler.instance()
                .getEffectiveSide()
                .isServer()) {
                s = AssemblyLineServer.lServerNames.get(aNewRecipe.mOutput.getDisplayName());
                if (s == null) {
                    s = aNewRecipe.mOutput.getDisplayName();
                }
            }

            String aHash = generateRecipeHash(aNewRecipe);
            if (GTValues.D1) {
                GTRecipe.RecipeAssemblyLine aOldRecipe = findAssemblyLineRecipeFromDataStick(aDataStick, true).recipe;
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
            if (GTValues.D1) {
                GTUtility.ItemNBT.setBookTitle(aDataStick, s + " Construction Data (" + aHash + ")");
            } else {
                GTUtility.ItemNBT.setBookTitle(aDataStick, s + " Construction Data");
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
                                s = AssemblyLineServer.lServerNames.get(tStack.getDisplayName());
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
                            s = AssemblyLineServer.lServerNames.get(aNewRecipe.mInputs[i].getDisplayName());
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
                        s = AssemblyLineServer.lServerNames.get(aNewRecipe.mFluidInputs[i].getLocalizedName());
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

        public LookupResult getResult(GTRecipe.RecipeAssemblyLine recipe) {
            if ((recipe == null) != recipeNull)
                throw new IllegalArgumentException("This result type does not allow given input");
            return new LookupResult(recipe, this);
        }
    }

    public static class LookupResult {

        private final GTRecipe.RecipeAssemblyLine recipe;
        private final LookupResultType type;

        LookupResult(GTRecipe.RecipeAssemblyLine recipe, LookupResultType type) {
            this.recipe = recipe;
            this.type = type;
        }

        public GTRecipe.RecipeAssemblyLine getRecipe() {
            return recipe;
        }

        public LookupResultType getType() {
            return type;
        }
    }
}
