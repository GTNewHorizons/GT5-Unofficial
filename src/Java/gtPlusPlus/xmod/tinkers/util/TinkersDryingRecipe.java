package gtPlusPlus.xmod.tinkers.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import gregtech.api.enums.GT_Values;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TinkersDryingRecipe {
	
    public static AutoMap<TinkersDryingRecipe> recipes = new AutoMap<TinkersDryingRecipe>();

	public final int time;
	public final ItemStack input;
	public final ItemStack result;
	
	public static void generateAllDryingRecipes() {
		List<?> aRecipes = TinkersUtils.getDryingRecipes();
		if (aRecipes != null && aRecipes.size() > 0) {
			for (Object o : aRecipes) {
				generateFromTinkersRecipeObject(o);
			}
		}
		if (!recipes.isEmpty()) {
			Logger.INFO("Adding "+recipes.size()+" drying rack recipes to the dehydrator.");
			for (TinkersDryingRecipe r : recipes) {
				CORE.RA.addDehydratorRecipe(
						new ItemStack[] {
								CI.getNumberedCircuit(16),
								r.input
						},
						GT_Values.NF,
						GT_Values.NF,
						new ItemStack[] {r.result},
						new int[] {},
						r.time,
						120);
			}
		}
	}

	public static TinkersDryingRecipe generateFromTinkersRecipeObject(Object o) {
		Field aTime;
		Field aInput;
		Field aOutput;
		Class aTinkerClass = ReflectionUtils.getClass("tconstruct.library.crafting.DryingRackRecipes.DryingRecipe");
		if (aTinkerClass == null || !LoadedMods.TiCon) {
			return null;
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

		}
		return null;
	}

	TinkersDryingRecipe(final ItemStack input, final int time, final ItemStack result) {
		this.time = time;
		this.input = input;
		this.result = result;
		recipes.add(this);
	}

	public boolean matches(ItemStack input) {
		if (input.hasTagCompound()) {
			input = input.copy();
			input.getTagCompound().removeTag("frypanKill");
			if (input.getTagCompound().hasNoTags()) {
				input.setTagCompound((NBTTagCompound) null);
			}
		}
		return ItemStack.areItemStacksEqual(this.input, input);
	}

	public ItemStack getResult() {
		return this.result.copy();
	}
}
