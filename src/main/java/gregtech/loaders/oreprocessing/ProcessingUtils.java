package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.simpleWasherRecipes;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.MaterialUtils;
import gregtech.api.material.ProcessingSubstitutes;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gregtech.api.util.NBTPersist;

public class ProcessingUtils {

    public static String itemStackKey(ItemStack stack) {
        if (stack == null || stack.getItem() == null) return null;

        String id = stack.getItem().delegate.name();

        String key = id + "@" + Items.feather.getDamage(stack);
        if (stack.hasTagCompound()) key += "#nbt=" + NBTPersist.toJsonObjectExact(stack.getTagCompound());
        return key;
    }

    /// Adds a simple washer recipe washing `input` into `output`; does nothing when either is null.
    static void addSimpleWasherRecipe(ItemStack input, ItemStack output) {
        if (input == null || output == null) return;
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, input))
            .itemOutputs(output)
            .fluidInputs(GTUtility.getWater(100))
            .duration(5 * TICKS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(simpleWasherRecipes);
    }

    /// Adds the crafting-table hammer recipe crushing a `prefix` part of `material` into `into` of the material it
    /// macerates into, through [ProcessingSubstitutes#crafting] when `substituted`. Skipped for a material that is not
    /// unifiable.
    static void addHammerRecipe(OrePrefixes prefix, Material material, Shape into, boolean substituted) {
        if (!MaterialUtils.unifiable(material)) return;
        Material crushed = MaterialUtils.macerateInto(material);
        ItemStack output = MaterialLibAPI.findStack(crushed, into, 1);
        GTModHandler.addCraftingRecipe(
            substituted ? ProcessingSubstitutes.crafting(crushed, output) : output,
            GTModHandler.RecipeBits.BITS_STD,
            new Object[] { "h", "X", 'X', prefix.ingredient(material) });
    }

    /// Adds the crafting recipes splitting a dust of `material` into four small dusts or nine tiny dusts, and back.
    static void addDustSplitRecipes(Material material, Shape smaller) {
        if (!MaterialUtils.unifiable(material)) return;
        boolean small = smaller == Shapes.dustSmall;
        OrePrefixes part = small ? OrePrefixes.dustSmall : OrePrefixes.dustTiny;
        GTModHandler.addCraftingRecipe(
            MaterialLibAPI.findStack(material, smaller, small ? 4 : 9),
            GTModHandler.RecipeBits.BITS_STD,
            new Object[] { small ? " X " : "X  ", "   ", "   ", 'X', OrePrefixes.dust.ingredient(material) });
        GTModHandler.addCraftingRecipe(
            MaterialLibAPI.findStack(material, Shapes.dust, 1),
            GTModHandler.RecipeBits.BITS_STD,
            small ? new Object[] { "XX", "XX", 'X', part.ingredient(material) }
                : new Object[] { "XXX", "XXX", "XXX", 'X', part.ingredient(material) });
    }
}
