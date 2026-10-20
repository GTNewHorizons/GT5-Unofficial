package gttests.census;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

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
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;

/// Pack-comparable recipe census. Writes, under `-Dgt.recipeCensus`:
/// - one file per machine recipe map plus crafting.txt and furnace.txt, one sorted line per recipe;
/// - accept.txt: for every machine recipe input slot, every item sharing an ore name or association with the
///   slot's stack is swapped in, and the line counts how many recipes a machine looks up (L) and consumes (C);
/// - shown.txt: the alternatives NEI shows for each machine recipe input;
/// - items.tsv: association, ore names, unification behavior and display name of every stack seen.
@GameTestHolder(Mods.ModIDs.GREG_TECH)
public class InterchangeCensus {

    private static final Map<String, ItemStack> SEEN = new HashMap<>();

    @GameTest(timeoutTicks = 20, batch = "gt5.census", required = false)
    public static void dump(GameTestHelper helper) throws IOException {
        String target = System.getProperty("gt.recipeCensus");
        helper.assumeTrue(target != null, "no -Dgt.recipeCensus directory given");
        File dir = new File(target);
        dir.mkdirs();
        long start = System.currentTimeMillis();
        Map<String, int[]> accept = new TreeMap<>();
        Set<String> shown = new TreeSet<>();
        for (Map.Entry<String, RecipeMap<?>> entry : RecipeMap.ALL_RECIPE_MAPS.entrySet()) {
            RecipeMap<?> map = entry.getValue();
            List<String> lines = new ArrayList<>();
            for (GTRecipe recipe : map.getAllRecipes()) {
                lines.add(describe(recipe));
                probeSlots(entry.getKey(), map, recipe, accept, shown);
            }
            write(new File(dir, entry.getKey() + ".txt"), lines);
        }
        List<String> acceptLines = new ArrayList<>();
        for (Map.Entry<String, int[]> e : accept.entrySet()) {
            int[] c = e.getValue();
            acceptLines.add(e.getKey() + "\tn=" + c[0] + "\tL=" + c[1] + "\tC=" + c[2] + "\tO=" + c[3]);
        }
        write(new File(dir, "accept.txt"), acceptLines);
        write(new File(dir, "shown.txt"), new ArrayList<>(shown));
        System.out.println("INTERCHANGE machine maps done in " + (System.currentTimeMillis() - start) + " ms");

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

        List<String> oreLines = new ArrayList<>();
        for (String name : OreDictionary.getOreNames()) {
            for (ItemStack ore : OreDictionary.getOres(name)) oreLines.add(name + "\t" + stack(ore));
        }
        write(new File(dir, "oredict.tsv"), oreLines);

        List<String> items = new ArrayList<>();
        for (Map.Entry<String, ItemStack> e : new TreeMap<>(SEEN).entrySet()) {
            ItemStack s = e.getValue();
            String display;
            try {
                display = s.getDisplayName();
            } catch (Throwable t) {
                display = "?";
            }
            items.add(
                e.getKey() + "\t"
                    + orDash(CensusAdapter.associationName(s))
                    + "\t"
                    + String.join(",", oreNames(s))
                    + "\tbl="
                    + CensusAdapter.isBlacklisted(s)
                    + "\tcopy="
                    + CensusAdapter.isUnifiedCopy(s)
                    + "\t"
                    + display);
        }
        write(new File(dir, "items.tsv"), items);
        write(new File(dir, "sideeffects.txt"), sideEffects());
        System.out.println("INTERCHANGE done in " + (System.currentTimeMillis() - start) + " ms");
        helper.succeed();
    }

    private static void probeSlots(String mapName, RecipeMap<?> map, GTRecipe recipe, Map<String, int[]> accept,
        Set<String> shown) {
        if (recipe.mInputs == null || recipe.mFakeRecipe || !recipe.mEnabled) return;
        FluidStack[] fluids = new FluidStack[recipe.mFluidInputs == null ? 0 : recipe.mFluidInputs.length];
        for (int i = 0; i < fluids.length; i++) {
            if (recipe.mFluidInputs[i] != null) fluids[i] = new FluidStack(recipe.mFluidInputs[i], Integer.MAX_VALUE / 4);
        }
        ItemStack special = recipe.mSpecialItems instanceof ItemStack s ? s : null;
        for (int slot = 0; slot < recipe.mInputs.length; slot++) {
            ItemStack input = recipe.mInputs[slot];
            if (input == null || input.getItem() == null) continue;
            String inputToken = token(input);
            List<String> alternatives = new ArrayList<>();
            for (ItemStack alt : CensusAdapter.shownAlternatives(recipe, input)) {
                if (alt != null && alt.getItem() != null) alternatives.add(token(alt));
            }
            Collections.sort(alternatives);
            shown.add(mapName + "\t" + inputToken + "\t" + alternatives);
            for (ItemStack candidate : candidates(input)) {
                ItemStack[] items = new ItemStack[recipe.mInputs.length];
                for (int i = 0; i < items.length; i++) {
                    ItemStack source = i == slot ? candidate : recipe.mInputs[i];
                    items[i] = source == null ? null : GTUtility.copyAmountUnsafe(Integer.MAX_VALUE / 4, source);
                }
                boolean lookup = false;
                boolean other = false;
                try {
                    for (GTRecipe found : (Iterable<GTRecipe>) map.findRecipeQuery()
                        .items(items)
                        .fluids(fluids)
                        .specialSlot(special)
                        .findAll()::iterator) {
                        if (found == recipe) {
                            lookup = true;
                            break;
                        }
                        other = true;
                    }
                } catch (Throwable t) {
                    other = false;
                }
                boolean consume;
                try {
                    consume = recipe.isRecipeInputEqual(false, true, fluids, items);
                } catch (Throwable t) {
                    consume = false;
                }
                int[] c = accept.computeIfAbsent(
                    mapName + "\t" + inputToken + "\t" + token(candidate),
                    k -> new int[4]);
                c[0]++;
                if (lookup) c[1]++;
                if (consume) c[2]++;
                if (!lookup && other) c[3]++;
            }
        }
    }

    /// Every other item registered under one of the stack's ore names or its association name. Wildcard entries
    /// stand in with damage 0.
    private static List<ItemStack> candidates(ItemStack input) {
        Set<String> names = new LinkedHashSet<>(oreNames(input));
        String association = CensusAdapter.associationName(input);
        if (association != null && !association.isEmpty()) names.add(association);
        Map<String, ItemStack> result = new TreeMap<>();
        for (String name : names) {
            for (ItemStack ore : OreDictionary.getOres(name, false)) {
                if (ore == null || ore.getItem() == null) continue;
                ItemStack c = ore.getItemDamage() == OreDictionary.WILDCARD_VALUE
                    ? new ItemStack(ore.getItem(), 1, 0)
                    : ore;
                if (c.getItem() == input.getItem() && c.getItemDamage() == input.getItemDamage()) continue;
                result.putIfAbsent(token(c), c);
            }
        }
        return new ArrayList<>(result.values());
    }

    private static List<String> oreNames(ItemStack stack) {
        List<String> names = new ArrayList<>();
        if (stack == null || stack.getItem() == null) return names;
        for (int id : OreDictionary.getOreIDs(stack)) names.add(OreDictionary.getOreName(id));
        Collections.sort(names);
        return names;
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
            + recipe.mSpecialValue
            + chances(" ic=", recipe.mInputChances)
            + chances(" oc=", recipe.mOutputChances)
            + chances(" foc=", recipe.mFluidOutputChances)
            + flags(recipe)
            + (recipe.mSpecialItems instanceof ItemStack s ? " special=" + stack(s) : "")
            + metadata(recipe);
    }

    private static String chances(String label, int[] chances) {
        if (chances == null) return "";
        boolean partial = false;
        for (int c : chances) if (c != 10000) partial = true;
        return partial ? label + Arrays.toString(chances)
            .replace(" ", "") : "";
    }

    private static String flags(GTRecipe recipe) {
        List<String> flags = new ArrayList<>();
        if (recipe.mHidden) flags.add("hidden");
        if (recipe.mFakeRecipe) flags.add("fake");
        if (!recipe.mEnabled) flags.add("disabled");
        if (recipe.isNBTSensitive) flags.add("nbt");
        if (recipe.mNeedsEmptyOutput) flags.add("emptyout");
        return flags.isEmpty() ? "" : " flags=" + String.join(",", flags);
    }

    /// Metadata entries with primitive or string values; other value types differ in representation between the
    /// packs and are recorded by key only.
    private static String metadata(GTRecipe recipe) {
        List<String> entries = new ArrayList<>();
        for (Map.Entry<?, Object> e : recipe.getMetadataStorage()
            .getEntries()) {
            Object v = e.getValue();
            boolean plain = v == null || v instanceof Number || v instanceof Boolean || v instanceof String
                || v instanceof Enum<?>;
            entries.add(e.getKey() + "=" + (plain ? String.valueOf(v) : "<" + v.getClass().getSimpleName() + ">"));
        }
        if (entries.isEmpty()) return "";
        Collections.sort(entries);
        return " meta={" + String.join(";", entries).replace(" ", "_") + "}";
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

    /// Item stack limits of every ore-dictionary stack and every registered cover item: the non-recipe effects of
    /// part recipe generation.
    private static List<String> sideEffects() {
        Set<String> lines = new TreeSet<>();
        for (String name : net.minecraftforge.oredict.OreDictionary.getOreNames()) {
            for (ItemStack s : net.minecraftforge.oredict.OreDictionary.getOres(name)) {
                if (s == null || s.getItem() == null) continue;
                lines.add("limit	" + rawToken(s) + "	" + s.getItem().getItemStackLimit(s));
            }
        }
        try {
            java.lang.reflect.Field f = gregtech.api.covers.CoverRegistry.class.getDeclaredField("covers");
            f.setAccessible(true);
            for (Object key : ((Map<?, ?>) f.get(null)).keySet()) {
                lines.add("cover	" + rawToken(((gregtech.api.objects.GTItemStack) key).toStack()));
            }
        } catch (ReflectiveOperationException e) {
            lines.add("cover	ERROR " + e);
        }
        return new ArrayList<>(lines);
    }

    private static String rawToken(ItemStack stack) {
        String name = Item.itemRegistry.getNameForObject(stack.getItem());
        return (name == null ? stack.getItem().getClass().getName() : name) + "@" + stack.getItemDamage();
    }

    private static String token(ItemStack stack) {
        String name = Item.itemRegistry.getNameForObject(stack.getItem());
        String t = (name == null ? stack.getItem()
            .getClass()
            .getName() : name) + "@" + stack.getItemDamage();
        SEEN.putIfAbsent(t, stack);
        return t;
    }

    private static String stack(ItemStack stack) {
        if (stack == null || stack.getItem() == null) return "-";
        return token(stack) + "x"
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
                stack == null || stack.getFluid() == null ? "-"
                    : stack.getFluid()
                        .getName() + "x"
                        + stack.amount);
        }
        return parts.toString();
    }

    private static String orDash(String s) {
        return s == null || s.isEmpty() ? "-" : s;
    }
}
