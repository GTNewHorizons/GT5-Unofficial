package gregtech.api.util;

import static gregtech.api.enums.Mods.GregTech;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import cpw.mods.fml.common.Loader;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.recipe.RecipeMetadataKey;

/**
 * Define helpers useful in the creation of recipe maps.
 */
public class GTRecipeMapUtil {

    public static final Function<GTRecipe, GTRecipe> ALL_FAKE_RECIPE = r -> {
        r.mFakeRecipe = true;
        return r;
    };

    private static final Map<String, IRecipeMap> addonRecipeMaps = new HashMap<>();
    private static final Multimap<String, Consumer<IRecipeMap>> delayedActions = ArrayListMultimap.create();

    /**
     * Set of metadata that work as alias for special values.
     */
    public static final Set<RecipeMetadataKey<Integer>> SPECIAL_VALUE_ALIASES = new HashSet<>();

    public static <T> T[] appendArray(T[] arr, T val) {
        T[] newArr = Arrays.copyOf(arr, arr.length + 1);
        newArr[arr.length] = val;
        return newArr;
    }

    public static GTRecipeTemplate asTemplate(GTRecipe r) {
        return asTemplate(r, false);
    }

    public static GTRecipeTemplate asTemplate(GTRecipe r, boolean includeTemplate) {
        return new GTRecipeTemplate(r, includeTemplate);
    }

    public static List<GTRecipe> buildRecipeForMultiblock(GTRecipeBuilder b) {
        return buildOrEmpty(convertCellToFluid(b, true));

    }

    public static List<GTRecipe> buildRecipeForMultiblockNoCircuit(GTRecipeBuilder b) {
        return buildOrEmpty(convertCellToFluid(b, false));
    }

    public static GTRecipeBuilder convertCellToFluid(GTRecipeBuilder b, boolean removeIntegratedCircuit) {
        List<ItemStack> itemInputs = new ArrayList<>(Arrays.asList(b.getItemInputsBasic()));
        List<ItemStack> itemOutputs = new ArrayList<>(Arrays.asList(b.getItemOutputs()));
        List<FluidStack> fluidInputs = new ArrayList<>(Arrays.asList(b.getFluidInputs()));
        List<FluidStack> fluidOutputs = new ArrayList<>(Arrays.asList(b.getFluidOutputs()));
        TIntList chances = b.getChances() != null ? new TIntArrayList(b.getChances()) : null;
        if (!hasCells(itemInputs, itemOutputs) && !removeIntegratedCircuit) {
            return b; // Skip conversion if no cells/filled containers exist
        }
        cellToFluid(itemInputs, fluidInputs, removeIntegratedCircuit, null);
        cellToFluid(itemOutputs, fluidOutputs, removeIntegratedCircuit, chances);
        itemInputs.removeIf(Objects::isNull);
        if (chances == null) {
            itemOutputs.removeIf(Objects::isNull);
        }
        fluidInputs.removeIf(Objects::isNull);
        fluidOutputs.removeIf(Objects::isNull);
        b.itemInputs(itemInputs.toArray(new ItemStack[0]));
        b.itemOutputs(itemOutputs.toArray(new ItemStack[0]), chances != null ? chances.toArray() : null);
        b.fluidInputs(fluidInputs.toArray(new FluidStack[0]));
        b.fluidOutputs(fluidOutputs.toArray(new FluidStack[0]));
        return b;
    }

    private static void cellToFluid(List<ItemStack> items, List<FluidStack> fluids, boolean removeIntegratedCircuit,
        TIntList chances) {
        for (int i = items.size() - 1; i >= 0; i--) {
            ItemStack item = items.get(i);
            if (GTUtility.getFluidForFilledItem(item, true) != null || GTUtility.isCellEmpty(item)
                || (removeIntegratedCircuit && GTUtility.isAnyIntegratedCircuit(item))) {
                fluids.add(GTUtility.convertCellToFluid(item));
                items.remove(i);
                if (chances != null) chances.removeAt(i);
            }
        }
    }

    private static boolean hasCells(List<ItemStack> itemInputs, List<ItemStack> itemOutputs) {
        // Check input items
        for (ItemStack stack : itemInputs) {
            if (stack == null) continue;
            if (GTUtility.getFluidForFilledItem(stack, true) != null || GTUtility.isCellEmpty(stack)) {
                return true;
            }
        }
        // Check output items
        for (ItemStack stack : itemOutputs) {
            if (stack == null) continue;
            if (GTUtility.getFluidForFilledItem(stack, true) != null || GTUtility.isCellEmpty(stack)) {
                return true;
            }
        }
        return false;
    }

    public static List<GTRecipe> buildOrEmpty(GTRecipeBuilder builder) {
        return builder.build()
            .map(Collections::singletonList)
            .orElse(Collections.emptyList());
    }

    /**
     * Register a recipe map as part of your mod's public API under your modID and your given identifier.
     *
     * @param identifier   map name
     * @param recipeMap    the map to register
     * @param dependencies fully qualified identifier of dependent recipe maps. scheduler will only add recipes to one
     *                     of the dependent recipe maps and this recipe map concurrently, guaranteeing thread safety.
     *                     Currently unused, but you are advised to fill them, so that when The Day (tm) comes we don't
     *                     end up with a bunch of weird concurrency bugs.
     */
    public static void registerRecipeMap(String identifier, IRecipeMap recipeMap, RecipeMapDependency... dependencies) {
        String modId = Loader.instance()
            .activeModContainer()
            .getModId();
        if (GregTech.ID.equals(modId)) throw new IllegalStateException(
            "do not register recipe map under the name of gregtech! do it in your own preinit!");
        String id = modId + "@" + identifier;
        addonRecipeMaps.put(id, recipeMap);
        for (Consumer<IRecipeMap> action : delayedActions.get(id)) {
            action.accept(recipeMap);
        }
    }

    /**
     * Use this to register recipes for a recipe map in addon not present at compile time.
     * <p>
     * Do not use this for recipes maps already in {@link GTRecipeConstants}. None of them will be available via this
     * interface!
     *
     * @param identifier     recipe map id
     * @param registerAction DO NOT ADD RECIPES TO MAPS OTHER THAN THE ONE PASSED TO YOU. DO NOT DO ANYTHING OTHER THAN
     *                       ADDING RECIPES TO THIS R
     */
    public static void registerRecipesFor(String modid, String identifier, Consumer<IRecipeMap> registerAction) {
        String id = modid + "@" + identifier;
        IRecipeMap map = addonRecipeMaps.get(id);
        if (map == null) delayedActions.put(id, registerAction);
        else registerAction.accept(map);
    }

    public static final class GTRecipeTemplate {

        private final GTRecipe template;
        private final List<GTRecipe> derivatives = new ArrayList<>();

        private GTRecipeTemplate(GTRecipe template, boolean includeTemplate) {
            this.template = template;
            if (includeTemplate) derivatives.add(template);
        }

        public GTRecipe derive() {
            GTRecipe derived = template.copyShallow();
            derivatives.add(derived);
            return derived;
        }

        public List<GTRecipe> getAll() {
            // fix shallow references
            Set<Object> references = Collections.newSetFromMap(new IdentityHashMap<>());
            for (GTRecipe r : derivatives) {
                if (r.mInputs.length != 0 && !references.add(r.mInputs)) {
                    r.mInputs = r.mInputs.clone();
                }
                if (r.mOutputs.length != 0 && !references.add(r.mOutputs)) {
                    r.mOutputs = r.mOutputs.clone();
                }
                if (r.mFluidInputs.length != 0 && !references.add(r.mFluidInputs)) {
                    r.mFluidInputs = r.mFluidInputs.clone();
                }
                if (r.mFluidOutputs.length != 0 && !references.add(r.mFluidOutputs)) {
                    r.mFluidOutputs = r.mFluidOutputs.clone();
                }
            }
            return derivatives;
        }
    }

    public static final class RecipeMapDependency {

        private final IRecipeMap obj;
        private final String id;

        public RecipeMapDependency(IRecipeMap obj, String id) {
            this.obj = obj;
            this.id = id;
        }

        public static RecipeMapDependency create(String id) {
            return new RecipeMapDependency(null, id);
        }

        public static RecipeMapDependency create(IRecipeMap obj) {
            return new RecipeMapDependency(obj, null);
        }
    }
}
