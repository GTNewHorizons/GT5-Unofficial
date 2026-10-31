package gregtech.api.util;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import gregtech.mixin.interfaces.accessors.ShapedOreRecipeAccessor;

/**
 * Cheap checks run before {@link IRecipe#matches} when GT searches the whole crafting recipe list. A recipe is skipped
 * if its shape cannot fit the occupied slots of the grid, or if the grid holds an item none of its inputs accept. Only
 * the exact vanilla, Forge, GT, AE2 and Blood Magic recipe classes are checked, subclasses may match by other rules.
 */
public final class CraftingRecipeFilter {

    private static final int OTHER = 0, SHAPED = 1, SHAPED_ORE = 2, SHAPELESS = 3, SHAPELESS_ORE = 4, SHAPED_FIELDS = 5;

    // shaped recipes of other mods that keep a Forge style input, width and height in private fields of their own
    private static final Set<String> SHAPED_FIELD_CLASSES = Set
        .of("appeng.recipes.game.ShapedRecipe", "WayofTime.alchemicalWizardry.api.items.ShapedBloodOrbRecipe");

    private static final ClassValue<Integer> KIND = new ClassValue<>() {

        @Override
        protected Integer computeValue(Class<?> type) {
            if (type == ShapedRecipes.class) return SHAPED;
            if (type == ShapedOreRecipe.class || type == GTShapedRecipe.class) return SHAPED_ORE;
            if (type == ShapelessRecipes.class) return SHAPELESS;
            if (type == ShapelessOreRecipe.class || type == GTShapelessRecipe.class) return SHAPELESS_ORE;
            if (SHAPED_FIELD_CLASSES.contains(type.getName()) && FIELDS.get(type) != null) return SHAPED_FIELDS;
            return OTHER;
        }
    };

    private static final ClassValue<FieldLayout> FIELDS = new ClassValue<>() {

        @Override
        protected FieldLayout computeValue(Class<?> type) {
            try {
                return new FieldLayout(type);
            } catch (ReflectiveOperationException | RuntimeException e) {
                return null;
            }
        }
    };

    private static final long[] ANY_MASK = new long[0];
    private static final Map<IRecipe, ShapeInfo> SHAPES = new IdentityHashMap<>();

    // bumped on every ore dictionary registration, item summaries only need checking again after one
    private static int oreVersion;

    private static InventoryCrafting lastGrid;
    private static int lastOccupied;
    private static Item[] lastItems;

    private CraftingRecipeFilter() {}

    public static void onOreRegistered() {
        oreVersion++;
    }

    static int getOreVersion() {
        return oreVersion;
    }

    /** Same result as {@code recipe.matches(grid, world)}, but skips the call when the recipe cannot match. */
    static boolean matches(IRecipe recipe, InventoryCrafting grid, World world) {
        // the callers fill a new grid for every search
        if (grid != lastGrid) summarize(grid);
        final int kind = KIND.get(recipe.getClass());
        switch (kind) {
            case OTHER:
                break;
            case SHAPELESS:
                if (((ShapelessRecipes) recipe).recipeItems.size() != Integer.bitCount(lastOccupied)) return false;
                break;
            case SHAPELESS_ORE:
                if (((ShapelessOreRecipe) recipe).getInput()
                    .size() != Integer.bitCount(lastOccupied)) return false;
                break;
            default:
                final ShapeInfo info = shapeInfo(recipe, kind);
                if (!info.fits(lastOccupied)) return false;
                if (kind != SHAPED_FIELDS && !acceptsAll(summary(recipe, kind, info), lastItems)) return false;
        }
        return recipe.matches(grid, world);
    }

    /** False if no grid with these occupied slots (bit i for slot i) can match the recipe. */
    static boolean mayFit(IRecipe recipe, int occupied) {
        final int kind = KIND.get(recipe.getClass());
        return switch (kind) {
            case OTHER -> true;
            case SHAPELESS -> ((ShapelessRecipes) recipe).recipeItems.size() == Integer.bitCount(occupied);
            case SHAPELESS_ORE -> ((ShapelessOreRecipe) recipe).getInput()
                .size() == Integer.bitCount(occupied);
            default -> shapeInfo(recipe, kind).fits(occupied);
        };
    }

    /**
     * Every item the inputs of a shaped recipe accept, or null if it may take anything as far as the filter knows. Only
     * valid until the next ore dictionary registration.
     */
    static Set<Item> acceptedItems(IRecipe recipe) {
        final int kind = KIND.get(recipe.getClass());
        if (kind != SHAPED && kind != SHAPED_ORE) return null;
        final ItemSummary summary = summary(recipe, kind, shapeInfo(recipe, kind));
        return summary.anyItem ? null : summary.items;
    }

    private static void summarize(InventoryCrafting grid) {
        int occupied = 0, count = 0;
        final Item[] items = new Item[9];
        for (int i = 0; i < 9 && i < grid.getSizeInventory(); i++) {
            final ItemStack stack = grid.getStackInSlot(i);
            if (stack == null) continue;
            occupied |= 1 << i;
            items[count++] = stack.getItem();
        }
        lastGrid = grid;
        lastOccupied = occupied;
        lastItems = Arrays.copyOf(items, count);
    }

    private static boolean acceptsAll(ItemSummary summary, Item[] items) {
        if (summary.anyItem) return true;
        for (Item item : items) {
            if (item == null) return true;
            if (!summary.accepts(item)) return false;
        }
        return true;
    }

    private static ShapeInfo shapeInfo(IRecipe recipe, int kind) {
        ShapeInfo info = SHAPES.get(recipe);
        if (info == null) {
            info = new ShapeInfo(fittingMasks(recipe, kind));
            SHAPES.put(recipe, info);
        }
        return info;
    }

    private static ItemSummary summary(IRecipe recipe, int kind, ShapeInfo info) {
        ItemSummary summary = info.items;
        // only new ore entries can make a summary too narrow
        if (summary == null || summary.oreVersion != oreVersion && summary.isStale()) {
            summary = new ItemSummary(
                kind == SHAPED ? ((ShapedRecipes) recipe).recipeItems : ((ShapedOreRecipe) recipe).getInput());
            info.items = summary;
        }
        summary.oreVersion = oreVersion;
        return summary;
    }

    private static long[] fittingMasks(IRecipe recipe, int kind) {
        return switch (kind) {
            case SHAPED -> {
                final ShapedRecipes shaped = (ShapedRecipes) recipe;
                yield fittingMasks(shaped.recipeItems, shaped.recipeWidth, shaped.recipeHeight);
            }
            case SHAPED_FIELDS -> FIELDS.get(recipe.getClass())
                .masks(recipe);
            default -> {
                final ShapedOreRecipeAccessor size = (ShapedOreRecipeAccessor) recipe;
                yield fittingMasks(((ShapedOreRecipe) recipe).getInput(), size.gt5u$getWidth(), size.gt5u$getHeight());
            }
        };
    }

    /**
     * One bit for each of the 512 occupied slot masks the recipe can match: its non empty cells at some offset in the
     * grid, plain or mirrored.
     */
    private static long[] fittingMasks(Object[] input, int width, int height) {
        if (input == null || width <= 0 || height <= 0 || input.length < width * height) return ANY_MASK;
        final long[] masks = new long[8];
        if (width > 3 || height > 3) return masks;
        for (int offsetY = 0; offsetY <= 3 - height; offsetY++) {
            for (int offsetX = 0; offsetX <= 3 - width; offsetX++) {
                int normal = 0, mirrored = 0;
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        if (input[x + y * width] == null) continue;
                        normal |= 1 << (offsetX + x + (offsetY + y) * 3);
                        mirrored |= 1 << (offsetX + width - x - 1 + (offsetY + y) * 3);
                    }
                }
                masks[normal >>> 6] |= 1L << normal;
                masks[mirrored >>> 6] |= 1L << mirrored;
            }
        }
        return masks;
    }

    private static final class ShapeInfo {

        private final long[] masks;
        private ItemSummary items;

        private ShapeInfo(long[] masks) {
            this.masks = masks;
        }

        private boolean fits(int occupied) {
            return masks == ANY_MASK || (masks[occupied >>> 6] & 1L << occupied) != 0;
        }
    }

    /** The items the inputs of a recipe accept, along with the sizes of the ore lists they came from. */
    private static final class ItemSummary {

        private static final int SMALL = 12;

        private final Set<Item> items = Collections.newSetFromMap(new IdentityHashMap<>());
        // most recipes take a handful of items, a short scan is faster than hashing
        private Item[] few;
        private boolean anyItem;
        private int oreVersion;
        private List<?>[] lists = new List<?>[0];
        private int[] sizes = new int[0];

        private ItemSummary(Object[] inputs) {
            if (inputs == null) {
                anyItem = true;
                return;
            }
            final List<List<?>> seen = new ArrayList<>();
            for (Object input : inputs) {
                if (input instanceof ItemStack stack) {
                    items.add(stack.getItem());
                } else if (input instanceof List<?>list) {
                    seen.add(list);
                    for (Object option : list) {
                        if (option instanceof ItemStack stack) items.add(stack.getItem());
                        else anyItem = true;
                    }
                } else if (input != null) {
                    anyItem = true;
                }
            }
            if (items.size() <= SMALL) few = items.toArray(new Item[0]);
            lists = seen.toArray(new List<?>[0]);
            sizes = new int[lists.length];
            for (int i = 0; i < lists.length; i++) sizes[i] = lists[i].size();
        }

        private boolean accepts(Item item) {
            if (few == null) return items.contains(item);
            for (Item candidate : few) {
                if (candidate == item) return true;
            }
            return false;
        }

        // ore lists only grow while mods load, so a changed size means new entries
        private boolean isStale() {
            for (int i = 0; i < lists.length; i++) {
                if (lists[i].size() != sizes[i]) return true;
            }
            return false;
        }
    }

    private static final class FieldLayout {

        private final MethodHandle input, width, height;

        private FieldLayout(Class<?> type) throws ReflectiveOperationException {
            input = getter(type, "input");
            width = getter(type, "width");
            height = getter(type, "height");
        }

        private static MethodHandle getter(Class<?> type, String name) throws ReflectiveOperationException {
            final Field field = type.getDeclaredField(name);
            field.setAccessible(true);
            return MethodHandles.lookup()
                .unreflectGetter(field);
        }

        private long[] masks(IRecipe recipe) {
            try {
                return fittingMasks(
                    (Object[]) input.invoke(recipe),
                    (int) width.invoke(recipe),
                    (int) height.invoke(recipe));
            } catch (Throwable t) {
                return ANY_MASK;
            }
        }
    }
}
