package gttests.oredict;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import com.gtnewhorizons.horizonqa.api.GameTestHelper;
import com.gtnewhorizons.horizonqa.api.annotation.GameTest;
import com.gtnewhorizons.horizonqa.api.annotation.GameTestHolder;

import gregtech.api.enums.Circuits;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;

@GameTestHolder(Mods.ModIDs.GREG_TECH)
public class InterchangeableInputTests {

    /// A recipe input holding a tier's circuit placeholder accepts every circuit registered under the tier's name,
    /// both when the machine looks the recipe up and when it consumes the inputs.
    @GameTest(timeoutTicks = 20, batch = "gt5.oredict")
    public static void everyCircuitOfATierFillsItsPlaceholder(GameTestHelper helper) {
        List<String> rejected = new ArrayList<>();
        for (Circuits tier : Circuits.values()) {
            ItemStack placeholder = tier.get(1);
            GTRecipe recipe = firstAssemblerRecipeUsing(placeholder);
            if (recipe == null) continue;
            String name = OrePrefixes.circuit.oreDictName(tier.materialName());
            for (ItemStack circuit : OreDictionary.getOres(name)) {
                if (circuit.getItemDamage() == OreDictionary.WILDCARD_VALUE) continue;
                ItemStack[] items = swap(recipe.mInputs, placeholder, circuit);
                FluidStack[] fluids = plenty(recipe.mFluidInputs);
                boolean found = RecipeMaps.assemblerRecipes.findRecipeQuery()
                    .items(items)
                    .fluids(fluids)
                    .find() != null;
                boolean consumed = recipe.isRecipeInputEqual(false, true, fluids, items);
                if (!found || !consumed) {
                    rejected.add(name + ": " + describe(circuit) + " lookup=" + found + " consume=" + consumed);
                }
            }
        }
        helper.assertTrue(rejected.isEmpty(), "Circuits rejected by their tier's recipes: " + rejected);
        helper.succeed();
    }

    private static GTRecipe firstAssemblerRecipeUsing(ItemStack placeholder) {
        for (GTRecipe recipe : RecipeMaps.assemblerRecipes.getAllRecipes()) {
            if (recipe.mFakeRecipe || !recipe.mEnabled || recipe.mSpecialItems != null) continue;
            for (ItemStack input : recipe.mInputs) {
                if (GTUtility.areStacksEqual(input, placeholder, true)) return recipe;
            }
        }
        return null;
    }

    private static ItemStack[] swap(ItemStack[] inputs, ItemStack placeholder, ItemStack replacement) {
        ItemStack[] items = new ItemStack[inputs.length];
        for (int i = 0; i < inputs.length; i++) {
            if (inputs[i] == null) continue;
            ItemStack source = GTUtility.areStacksEqual(inputs[i], placeholder, true) ? replacement : inputs[i];
            items[i] = GTUtility.copyAmountUnsafe(Integer.MAX_VALUE / 4, source);
        }
        return items;
    }

    private static FluidStack[] plenty(FluidStack[] inputs) {
        FluidStack[] fluids = new FluidStack[inputs.length];
        for (int i = 0; i < inputs.length; i++) {
            if (inputs[i] != null) fluids[i] = new FluidStack(inputs[i], Integer.MAX_VALUE / 4);
        }
        return fluids;
    }

    private static String describe(ItemStack stack) {
        return Item.itemRegistry.getNameForObject(stack.getItem()) + "@" + stack.getItemDamage();
    }
}
