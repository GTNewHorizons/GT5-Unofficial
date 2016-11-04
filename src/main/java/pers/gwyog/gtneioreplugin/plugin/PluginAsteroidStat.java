package pers.gwyog.gtneioreplugin.plugin;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler.RecipeTransferRect;
import gregtech.api.GregTech_API;
import gregtech.api.util.GT_LanguageManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import pers.gwyog.gtneioreplugin.plugin.PluginVeinStat.CachedVeinStatRecipe;
import pers.gwyog.gtneioreplugin.util.GTOreLayerHelper;
import pers.gwyog.gtneioreplugin.util.GTSmallOreHelper;
import pers.gwyog.gtneioreplugin.util.GTOreLayerHelper.OreLayerWrapper;

public class PluginAsteroidStat extends PluginBase {

	public class CachedAsteroidStatRecipe extends CachedRecipe {
		public String veinName;
		public PositionedStack positionedStackPrimary;
		public PositionedStack positionedStackSecondary;
		public PositionedStack positionedStackBetween;
		public PositionedStack positionedStackSporadic;
			
		public CachedAsteroidStatRecipe(String veinName, ItemStack stackListPrimary, ItemStack stackListSecondary,
				ItemStack stackListBetween, ItemStack stackListSporadic) {
			this.veinName = veinName;
			positionedStackPrimary = new PositionedStack(stackListPrimary, 2, 0);
			positionedStackSecondary = new PositionedStack(stackListSecondary, 22, 0);
			positionedStackBetween = new PositionedStack(stackListBetween, 42, 0);
			positionedStackSporadic = new PositionedStack(stackListSporadic, 62, 0);
		}
		
		@Override
		public List<PositionedStack> getIngredients() {
			List<PositionedStack> ingredientsList = new ArrayList<PositionedStack>();
			ingredientsList.add(positionedStackPrimary);
			ingredientsList.add(positionedStackSecondary);
			ingredientsList.add(positionedStackBetween);
			ingredientsList.add(positionedStackSporadic);
			return ingredientsList;
		}
		
		@Override
		public PositionedStack getResult() {
			return null;
		}
		
	}
	
	@Override
	public void drawExtras(int recipe) {
		CachedAsteroidStatRecipe crecipe = (CachedAsteroidStatRecipe) this.arecipes.get(recipe);
		OreLayerWrapper oreLayer = GTOreLayerHelper.mapOreLayerWrapper.get(crecipe.veinName);
		GuiDraw.drawString(I18n.format("gui.nei.veinName") + ": " + I18n.format(oreLayer.veinName), 2, 18, 0x404040, false);
		GuiDraw.drawString(I18n.format("gui.nei.asteroidPrimary") + ": " + GT_LanguageManager.getTranslation(getGTOreUnlocalizedName(oreLayer.primaryMeta)), 2, 31, 0x404040, false);
		GuiDraw.drawString(I18n.format("gui.nei.asteroidSecondary") + ": " + GT_LanguageManager.getTranslation(getGTOreUnlocalizedName(oreLayer.secondaryMeta)), 2, 44, 0x404040, false);
		GuiDraw.drawString(I18n.format("gui.nei.asteroidBetween") + ": " + GT_LanguageManager.getTranslation(getGTOreUnlocalizedName(oreLayer.betweenMeta)), 2, 57, 0x404040, false);
		GuiDraw.drawString(I18n.format("gui.nei.asteroidSporadic") + ": " + GT_LanguageManager.getTranslation(getGTOreUnlocalizedName(oreLayer.sporadicMeta)), 2, 70, 0x404040, false);
		GuiDraw.drawString(I18n.format("gui.nei.worldNames") + ": " + getWorldNameTranslated(oreLayer.genEndAsteroid, oreLayer.genGCAsteroid), 2, 83, 0x404040, false);		
		GuiDraw.drawStringR(EnumChatFormatting.BOLD + I18n.format("gui.nei.seeAll"), getGuiWidth()-3, 5, 0x404040, false);
	}
		
	public String getWorldNameTranslated(boolean genEndAsteroid, boolean genGCAsteroid) {
    	String worldNameTranslatedAsteroid = "";
		if (genEndAsteroid) {
    		if (!worldNameTranslatedAsteroid.isEmpty())
    			worldNameTranslatedAsteroid += ", ";
    		worldNameTranslatedAsteroid += I18n.format("world.end.name");
    	}
    	if (genGCAsteroid) {
    		if (!worldNameTranslatedAsteroid.isEmpty())
    			worldNameTranslatedAsteroid += ", ";
    		worldNameTranslatedAsteroid += I18n.format("world.asteroid.name");
    	}
    	return worldNameTranslatedAsteroid;
	}
	
	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
		if (outputId.equals(getOutputId())) {
			OreLayerWrapper oreLayerWrapper;
			for (String veinName: GTOreLayerHelper.mapOreLayerWrapper.keySet()) {
				oreLayerWrapper = GTOreLayerHelper.mapOreLayerWrapper.get(veinName);
				if (oreLayerWrapper.genEndAsteroid || oreLayerWrapper.genGCAsteroid) {
					ItemStack stackPrimary = new ItemStack(GregTech_API.sBlockOres1, 1, oreLayerWrapper.primaryMeta+2000);
					ItemStack stackSecondary = new ItemStack(GregTech_API.sBlockOres1, 1, oreLayerWrapper.secondaryMeta+2000);
					ItemStack stackBetween = new ItemStack(GregTech_API.sBlockOres1, 1, oreLayerWrapper.betweenMeta+2000);
					ItemStack stackSporadic = new ItemStack(GregTech_API.sBlockOres1, 1, oreLayerWrapper.sporadicMeta+2000);
					this.arecipes.add(new CachedAsteroidStatRecipe(veinName, stackPrimary, stackSecondary, stackBetween, stackSporadic));
				}
			}
		}
		else
			super.loadCraftingRecipes(outputId, results);
	}
	
	@Override
	public void loadCraftingRecipes(ItemStack stack) {
		if (stack.getUnlocalizedName().startsWith("gt.blockores")) {
			if (stack.getItemDamage()>16000) {
				super.loadCraftingRecipes(stack);
				return;
			}
			short baseMeta = (short)(stack.getItemDamage() % 1000);
			for (OreLayerWrapper worldGen: GTOreLayerHelper.mapOreLayerWrapper.values()) {
				if (worldGen.primaryMeta == baseMeta || worldGen.secondaryMeta == baseMeta || worldGen.betweenMeta == baseMeta || worldGen.sporadicMeta == baseMeta) {
					if (worldGen.genEndAsteroid || worldGen.genGCAsteroid) {
						ItemStack stackPrimary = new ItemStack(GregTech_API.sBlockOres1, 1, worldGen.primaryMeta+2000);
						ItemStack stackSecondary = new ItemStack(GregTech_API.sBlockOres1, 1, worldGen.secondaryMeta+2000);
						ItemStack stackBetween = new ItemStack(GregTech_API.sBlockOres1, 1, worldGen.betweenMeta+2000);
						ItemStack stackSporadic = new ItemStack(GregTech_API.sBlockOres1, 1, worldGen.sporadicMeta+2000);
						this.arecipes.add(new CachedAsteroidStatRecipe(worldGen.veinName, stackPrimary, stackSecondary, stackBetween, stackSporadic));
					}
				}
			}
		}
		else
			super.loadCraftingRecipes(stack);
	}
	
	@Override
	public String getOutputId() {
		return "GTOrePluginAsteroid";
	}
	
	@Override
	public String getRecipeName() {
		return I18n.format("gui.asteroidStat.name");
	}
}
