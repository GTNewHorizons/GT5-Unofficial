package gtPlusPlus.xmod.thaumcraft.objects.wrapper.research;

import java.util.List;

import gtPlusPlus.xmod.thaumcraft.objects.wrapper.aspect.TC_AspectList_Wrapper;
import gtPlusPlus.xmod.thaumcraft.objects.wrapper.recipe.TC_CrucibleRecipe_Wrapper;
import gtPlusPlus.xmod.thaumcraft.objects.wrapper.recipe.TC_IArcaneRecipe_Wrapper;
import gtPlusPlus.xmod.thaumcraft.objects.wrapper.recipe.TC_InfusionEnchantmentRecipe_Wrapper;
import gtPlusPlus.xmod.thaumcraft.objects.wrapper.recipe.TC_InfusionRecipe_Wrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class TC_ResearchPage_Wrapper {
	public TC_PageType_Wrapper type;
	public String text;
	public String research;
	public ResourceLocation image;
	public TC_AspectList_Wrapper aspects;
	public Object recipe;
	public ItemStack recipeOutput;

	public TC_ResearchPage_Wrapper(String text) {
		this.type = TC_PageType_Wrapper.TEXT;
		this.text = null;
		this.research = null;
		this.image = null;
		this.aspects = null;
		this.recipe = null;
		this.recipeOutput = null;
		this.type = TC_PageType_Wrapper.TEXT;
		this.text = text;
	}

	public TC_ResearchPage_Wrapper(String research, String text) {
		this.type = TC_PageType_Wrapper.TEXT;
		this.text = null;
		this.research = null;
		this.image = null;
		this.aspects = null;
		this.recipe = null;
		this.recipeOutput = null;
		this.type = TC_PageType_Wrapper.TEXT_CONCEALED;
		this.research = research;
		this.text = text;
	}

	public TC_ResearchPage_Wrapper(IRecipe recipe) {
		this.type = TC_PageType_Wrapper.TEXT;
		this.text = null;
		this.research = null;
		this.image = null;
		this.aspects = null;
		this.recipe = null;
		this.recipeOutput = null;
		this.type = TC_PageType_Wrapper.NORMAL_CRAFTING;
		this.recipe = recipe;
		this.recipeOutput = recipe.getRecipeOutput();
	}

	public TC_ResearchPage_Wrapper(IRecipe[] recipe) {
		this.type = TC_PageType_Wrapper.TEXT;
		this.text = null;
		this.research = null;
		this.image = null;
		this.aspects = null;
		this.recipe = null;
		this.recipeOutput = null;
		this.type = TC_PageType_Wrapper.NORMAL_CRAFTING;
		this.recipe = recipe;
	}

	public TC_ResearchPage_Wrapper(TC_IArcaneRecipe_Wrapper[] recipe) {
		this.type = TC_PageType_Wrapper.TEXT;
		this.text = null;
		this.research = null;
		this.image = null;
		this.aspects = null;
		this.recipe = null;
		this.recipeOutput = null;
		this.type = TC_PageType_Wrapper.ARCANE_CRAFTING;
		this.recipe = recipe;
	}

	public TC_ResearchPage_Wrapper(TC_CrucibleRecipe_Wrapper[] recipe) {
		this.type = TC_PageType_Wrapper.TEXT;
		this.text = null;
		this.research = null;
		this.image = null;
		this.aspects = null;
		this.recipe = null;
		this.recipeOutput = null;
		this.type = TC_PageType_Wrapper.CRUCIBLE_CRAFTING;
		this.recipe = recipe;
	}

	public TC_ResearchPage_Wrapper(TC_InfusionRecipe_Wrapper[] recipe) {
		this.type = TC_PageType_Wrapper.TEXT;
		this.text = null;
		this.research = null;
		this.image = null;
		this.aspects = null;
		this.recipe = null;
		this.recipeOutput = null;
		this.type = TC_PageType_Wrapper.INFUSION_CRAFTING;
		this.recipe = recipe;
	}

	public TC_ResearchPage_Wrapper(List recipe) {
		this.type = TC_PageType_Wrapper.TEXT;
		this.text = null;
		this.research = null;
		this.image = null;
		this.aspects = null;
		this.recipe = null;
		this.recipeOutput = null;
		this.type = TC_PageType_Wrapper.COMPOUND_CRAFTING;
		this.recipe = recipe;
	}

	public TC_ResearchPage_Wrapper(TC_IArcaneRecipe_Wrapper recipe) {
		this.type = TC_PageType_Wrapper.TEXT;
		this.text = null;
		this.research = null;
		this.image = null;
		this.aspects = null;
		this.recipe = null;
		this.recipeOutput = null;
		this.type = TC_PageType_Wrapper.ARCANE_CRAFTING;
		this.recipe = recipe;
		this.recipeOutput = recipe.getRecipeOutput();
	}

	public TC_ResearchPage_Wrapper(TC_CrucibleRecipe_Wrapper recipe) {
		this.type = TC_PageType_Wrapper.TEXT;
		this.text = null;
		this.research = null;
		this.image = null;
		this.aspects = null;
		this.recipe = null;
		this.recipeOutput = null;
		this.type = TC_PageType_Wrapper.CRUCIBLE_CRAFTING;
		this.recipe = recipe;
		this.recipeOutput = recipe.getRecipeOutput();
	}

	public TC_ResearchPage_Wrapper(ItemStack input) {
		this.type = TC_PageType_Wrapper.TEXT;
		this.text = null;
		this.research = null;
		this.image = null;
		this.aspects = null;
		this.recipe = null;
		this.recipeOutput = null;
		this.type = TC_PageType_Wrapper.SMELTING;
		this.recipe = input;
		this.recipeOutput = FurnaceRecipes.smelting().getSmeltingResult(input);
	}

	public TC_ResearchPage_Wrapper(TC_InfusionRecipe_Wrapper recipe) {
		this.type = TC_PageType_Wrapper.TEXT;
		this.text = null;
		this.research = null;
		this.image = null;
		this.aspects = null;
		this.recipe = null;
		this.recipeOutput = null;
		this.type = TC_PageType_Wrapper.INFUSION_CRAFTING;
		this.recipe = recipe;
		if (recipe.getRecipeOutput() instanceof ItemStack) {
			this.recipeOutput = (ItemStack) recipe.getRecipeOutput();
		} else {
			this.recipeOutput = recipe.getRecipeInput();
		}

	}

	public TC_ResearchPage_Wrapper(TC_InfusionEnchantmentRecipe_Wrapper recipe) {
		this.type = TC_PageType_Wrapper.TEXT;
		this.text = null;
		this.research = null;
		this.image = null;
		this.aspects = null;
		this.recipe = null;
		this.recipeOutput = null;
		this.type = TC_PageType_Wrapper.INFUSION_ENCHANTMENT;
		this.recipe = recipe;
	}

	public TC_ResearchPage_Wrapper(ResourceLocation image, String caption) {
		this.type = TC_PageType_Wrapper.TEXT;
		this.text = null;
		this.research = null;
		this.image = null;
		this.aspects = null;
		this.recipe = null;
		this.recipeOutput = null;
		this.type = TC_PageType_Wrapper.IMAGE;
		this.image = image;
		this.text = caption;
	}

	public TC_ResearchPage_Wrapper(TC_AspectList_Wrapper as) {
		this.type = TC_PageType_Wrapper.TEXT;
		this.text = null;
		this.research = null;
		this.image = null;
		this.aspects = null;
		this.recipe = null;
		this.recipeOutput = null;
		this.type = TC_PageType_Wrapper.ASPECTS;
		this.aspects = as;
	}

	public String getTranslatedText() {
		String ret = "";
		if (this.text != null) {
			ret = StatCollector.translateToLocal(this.text);
			if (ret.isEmpty()) {
				ret = this.text;
			}
		}

		return ret;
	}
}