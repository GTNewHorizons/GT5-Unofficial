package gttests.census;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.gtnewhorizons.horizonqa.api.GameTestHelper;
import com.gtnewhorizons.horizonqa.api.annotation.GameTest;
import com.gtnewhorizons.horizonqa.api.annotation.GameTestHolder;

import gregtech.api.enums.Mods;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.material.MaterialData;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.OreDictUtils;
import gregtech.api.util.GTRecipe;

/// Writes every machine recipe and every crafting-table recipe as one sorted text line each under the directory
/// named by `-Dgt.recipeCensus`, one file per recipe map, so two boots diff line by line. Skipped without the
/// property.
@GameTestHolder(Mods.ModIDs.GREG_TECH)
public class RecipeCensusDump {

    @GameTest(timeoutTicks = 20, batch = "gt5.census", required = false)
    public static void dump(GameTestHelper helper) throws IOException {
        String target = System.getProperty("gt.recipeCensus");
        helper.assumeTrue(target != null, "no -Dgt.recipeCensus directory given");
        File dir = new File(target);
        dir.mkdirs();
        for (Map.Entry<String, RecipeMap<?>> entry : RecipeMap.ALL_RECIPE_MAPS.entrySet()) {
            List<String> lines = new ArrayList<>();
            for (GTRecipe recipe : entry.getValue()
                .getAllRecipes()) {
                lines.add(describe(recipe));
            }
            write(new File(dir, entry.getKey() + ".txt"), lines);
        }
        List<String> crafting = new ArrayList<>();
        for (Object recipe : CraftingManager.getInstance()
            .getRecipeList()) {
            crafting.add(describe((IRecipe) recipe));
        }
        write(new File(dir, "crafting.txt"), crafting);
        List<String> smelting = new ArrayList<>();
        for (Map.Entry<?, ?> entry : ((Map<?, ?>) FurnaceRecipes.smelting()
            .getSmeltingList()).entrySet()) {
            smelting.add("in=" + stack((ItemStack) entry.getKey()) + " out=" + stack((ItemStack) entry.getValue()));
        }
        write(new File(dir, "furnace.txt"), smelting);
        List<String> unification = new ArrayList<>();
        for (String name : OreDictionary.getOreNames()) {
            unification.add(name + " first=" + stack(GTModHandler.collisionProbe(name)));
            for (ItemStack ore : OreDictionary.getOres(name)) {
                ItemData data = MaterialData.getAssociation(ore);
                unification.add(
                    name + " ore=" + stack(ore) + " assoc=" + (data == null ? "-" : data.toString()) + " copy="
                        + OreDictUtils.isUnifiedCopy(ore));
            }
        }
        write(new File(dir, "unification.txt"), unification);
        helper.succeed();
    }

    private static void write(File file, List<String> lines) throws IOException {
        Collections.sort(lines);
        Files.write(file.toPath(), lines, StandardCharsets.UTF_8);
    }

    private static String describe(GTRecipe recipe) {
        return "in=" + stacks(recipe.mInputs)
            + " fin="
            + fluids(recipe.mFluidInputs)
            + " out="
            + stacks(recipe.mOutputs)
            + " fout="
            + fluids(recipe.mFluidOutputs)
            + " t="
            + recipe.mDuration
            + " eut="
            + recipe.mEUt
            + " sv="
            + recipe.mSpecialValue;
    }

    private static String describe(IRecipe recipe) {
        Object[] inputs;
        if (recipe instanceof ShapedOreRecipe shaped) inputs = shaped.getInput();
        else if (recipe instanceof ShapelessOreRecipe shapeless) inputs = shapeless.getInput()
            .toArray();
        else if (recipe instanceof ShapedRecipes shaped) inputs = shaped.recipeItems;
        else if (recipe instanceof ShapelessRecipes shapeless) inputs = shapeless.recipeItems.toArray();
        else inputs = new Object[] { recipe.getClass()
            .getName() };
        List<String> ingredients = new ArrayList<>();
        for (Object input : inputs) ingredients.add(ingredient(input));
        return "out=" + stack(recipe.getRecipeOutput())
            + " "
            + recipe.getClass()
                .getSimpleName()
            + " in="
            + ingredients;
    }

    private static String ingredient(Object input) {
        if (input == null) return "-";
        if (input instanceof ItemStack stack) return stack(stack);
        if (input instanceof List<?>list) {
            List<String> alternatives = new ArrayList<>();
            for (Object alternative : list) alternatives.add(ingredient(alternative));
            Collections.sort(alternatives);
            return "any" + alternatives;
        }
        return String.valueOf(input);
    }

    private static String stacks(ItemStack[] stacks) {
        List<String> parts = new ArrayList<>();
        if (stacks != null) for (ItemStack stack : stacks) parts.add(stack(stack));
        return parts.toString();
    }

    private static String stack(ItemStack stack) {
        if (stack == null || stack.getItem() == null) return "-";
        String name = Item.itemRegistry.getNameForObject(stack.getItem());
        return (name == null ? stack.getItem()
            .getClass()
            .getName() : name) + "@"
            + stack.getItemDamage()
            + "x"
            + stack.stackSize
            + (stack.hasTagCompound() ? "#" + Integer.toHexString(
                stack.getTagCompound()
                    .hashCode())
                : "");
    }

    private static String fluids(FluidStack[] stacks) {
        List<String> parts = new ArrayList<>();
        if (stacks != null) for (FluidStack stack : stacks) {
            parts.add(
                stack == null ? "-"
                    : stack.getFluid()
                        .getName() + "x"
                        + stack.amount);
        }
        return parts.toString();
    }
}
