package gtPlusPlus.xmod.tinkers.util;

import static gregtech.api.enums.Mods.TinkerConstruct;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalDehydratorRecipes;

import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTUtility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.util.reflect.ReflectionUtils;

public class TinkersDryingRecipe {

    public static AutoMap<TinkersDryingRecipe> recipes = new AutoMap<>();

    public final int time;
    public final ItemStack input;
    public final ItemStack result;

    public static void generateAllDryingRecipes() {
        List<?> aRecipes = TinkersUtils.getDryingRecipes();
        if (aRecipes != null && aRecipes.size() > 0) {
            for (Object o : aRecipes) {
                Logger.INFO(
                    "Trying to generate recipe using object of type " + o.getClass()
                        .getSimpleName());
                generateFromTinkersRecipeObject(o);
            }
        } else {
            Logger.INFO(
                "Error generating Drying recipes, map was either null or empty. Null? " + (aRecipes != null)
                    + ", Size: "
                    + aRecipes.size());
        }
        if (!recipes.isEmpty()) {
            Logger.INFO("Adding " + recipes.size() + " drying rack recipes to the dehydrator.");
            for (TinkersDryingRecipe r : recipes) {
                GTValues.RA.stdBuilder()
                    .itemInputs(GTUtility.getIntegratedCircuit(16), r.input)
                    .itemOutputs(r.result)
                    .eut(TierEU.RECIPE_LV)
                    .duration(r.time / 10)
                    .addTo(chemicalDehydratorRecipes);
            }
        }
    }

    public static TinkersDryingRecipe generateFromTinkersRecipeObject(Object o) {
        Field aTime;
        Field aInput;
        Field aOutput;
        Class aTinkerClass = ReflectionUtils.getClass("tconstruct.library.crafting.DryingRackRecipes.DryingRecipe"); // o.getClass();
        if (aTinkerClass == null || TinkerConstruct.isModLoaded()) {
            Logger.INFO(
                "Error generating Drying Recipe, could not find class. Exists? "
                    + ReflectionUtils.doesClassExist("tconstruct.library.crafting.DryingRackRecipes.DryingRecipe"));
            Class clazz = ReflectionUtils.getClass("tconstruct.library.crafting.DryingRackRecipes");
            Class[] y = clazz.getDeclaredClasses();
            if (y == null || y.length <= 0) {
                Logger.INFO("No hidden inner classes.");
                return null;
            } else {
                boolean found = false;
                for (Class h : y) {
                    Logger.INFO("Found hidden inner class: " + h.getCanonicalName());
                    if (h.getSimpleName()
                        .toLowerCase()
                        .equals("dryingrecipe")) {
                        Logger.INFO("Found correct recipe. Caching at correct location.");
                        ReflectionUtils.mCachedClasses
                            .put("tconstruct.library.crafting.DryingRackRecipes.DryingRecipe", h);
                        aTinkerClass = h;
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    return null;
                }
            }
        }
        aTime = ReflectionUtils.getField(aTinkerClass, "time");
        aInput = ReflectionUtils.getField(aTinkerClass, "input");
        aOutput = ReflectionUtils.getField(aTinkerClass, "result");
        try {
            int time_internal = aTime.getInt(o);
            ItemStack input_internal = (ItemStack) aInput.get(o);
            ItemStack result_internal = (ItemStack) aOutput.get(o);
            return new TinkersDryingRecipe(input_internal, time_internal, result_internal);
        } catch (Throwable b) {
            b.printStackTrace();
        }
        return null;
    }

    public TinkersDryingRecipe(final ItemStack input, final int time, final ItemStack result) {
        this.time = time;
        this.input = input;
        this.result = result;
        Logger.INFO(
            "Generating Drying Recipe. Input: " + input.getDisplayName() + ", Output: " + result.getDisplayName());
        recipes.add(this);
    }

    public boolean matches(ItemStack input) {
        if (input.hasTagCompound()) {
            input = input.copy();
            input.getTagCompound()
                .removeTag("frypanKill");
            if (input.getTagCompound()
                .hasNoTags()) {
                input.setTagCompound((NBTTagCompound) null);
            }
        }
        return ItemStack.areItemStacksEqual(this.input, input);
    }

    public ItemStack getResult() {
        return this.result.copy();
    }
}
