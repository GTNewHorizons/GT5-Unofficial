package gregtech.api.util;

import java.util.Collection;
import java.util.Collections;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants.NBT;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

import cpw.mods.fml.common.FMLCommonHandler;
import gregtech.GTMod;
import gregtech.api.enums.ItemList;
import gregtech.api.util.GTRecipe.RecipeAssemblyLine;
import gregtech.api.util.GTUtility.ItemId;

public class AssemblyLineUtils {

    private static boolean loadedALRecipes = false;

    private static final Multimap<ItemId, RecipeAssemblyLine> AL_RECIPE_LOOKUP = MultimapBuilder.hashKeys()
        .arrayListValues()
        .build();

    private static void loadALRecipes() {
        if (loadedALRecipes) return;

        for (RecipeAssemblyLine recipe : RecipeAssemblyLine.sAssemblylineRecipes) {
            AL_RECIPE_LOOKUP.put(ItemId.create(recipe.mOutput), recipe);
        }

        loadedALRecipes = true;
    }

    /**
     * Finds an Assembly Line recipe from a DataStick.
     *
     * @return The recipes that match the data stick.
     */
    public static Collection<RecipeAssemblyLine> findALRecipeFromDataStick(ItemStack dataStick) {
        return findALRecipeByOutput(getDataStickOutput(dataStick));
    }

    /**
     * Finds a GTRecipe_AssemblyLine based on the expected output ItemStack.
     *
     * @param output - The Output of a GTRecipe_AssemblyLine.
     * @return First found GTRecipe_AssemblyLine with matching output.
     */
    public static Collection<RecipeAssemblyLine> findALRecipeByOutput(ItemStack output) {
        if (output == null) return Collections.emptyList();

        loadALRecipes();

        return AL_RECIPE_LOOKUP.get(ItemId.create(output));
    }

    public static NBTTagCompound saveRecipe(RecipeAssemblyLine recipe) {
        return recipe.mOutput.writeToNBT(new NBTTagCompound());
    }

    public static Collection<RecipeAssemblyLine> loadRecipe(NBTTagCompound tag) {
        ItemStack output = ItemStack.loadItemStackFromNBT(tag);

        return findALRecipeByOutput(output);
    }

    public static @Nullable RecipeAssemblyLine assertSingleRecipe(Collection<RecipeAssemblyLine> recipes) {
        if (recipes.isEmpty()) return null;

        if (recipes.size() > 1) {
            if ((boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment")) {
                throw new RuntimeException(
                    "found too many assembly line recipes for output: '" + recipes.iterator()
                        .next().mOutput.getDisplayName() + "', this method assumes this cannot happen");
            } else {
                GTMod.GT_FML_LOGGER.error(
                    "found too many assembly line recipes for output: '" + recipes.iterator()
                        .next().mOutput.getDisplayName() + "', this method assumes this cannot happen",
                    new Exception());
            }
        }

        return recipes.iterator()
            .next();
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
        if (!isItemDataStick(aDataStick)) return false;
        if (aNewRecipe.mOutput == null) return false;

        String s = aNewRecipe.mOutput.getDisplayName();

        if (FMLCommonHandler.instance()
            .getEffectiveSide()
            .isServer()) {
            s = AssemblyLineServer.lServerNames.get(aNewRecipe.mOutput.getDisplayName());
            if (s == null) {
                s = aNewRecipe.mOutput.getDisplayName();
            }
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
        NBTTagCompound rootTag = aDataStick.getTagCompound();

        if (displayName != null) aDataStick.setStackDisplayName(displayName);

        GTUtility.ItemNBT.setBookTitle(aDataStick, s + " Construction Data");

        rootTag.setTag("output", aNewRecipe.mOutput.writeToNBT(new NBTTagCompound()));
        rootTag.setString("author", author);

        NBTTagList tooltip = new NBTTagList();
        tooltip.appendTag(
            new NBTTagString(
                "Construction plan for " + aNewRecipe.mOutput.stackSize
                    + " "
                    + s
                    + ". Needed EU/t: "
                    + aNewRecipe.mEUt
                    + " Production time: "
                    + (aNewRecipe.mDuration / 20)));

        rootTag.setTag("pages", tooltip);
        if (setUpdateTime) rootTag.setLong("lastUpdate", System.currentTimeMillis());

        return true;
    }
}
