package pers.gwyog.gtneioreplugin.plugin;

import java.awt.Rectangle;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.recipe.TemplateRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler.RecipeTransferRect;
import gregtech.api.util.GT_LanguageManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;

public class PluginBase extends TemplateRecipeHandler {

	@Override
	public int recipiesPerPage() {
		return 1;
	}
	
	@Override
	public String getRecipeName() {
		return null;
	}

	@Override
	public String getGuiTexture() {
		return "gtneioreplugin:textures/gui/nei/guiBase.png";
	}
	
    @Override
    public void loadTransferRects() {
    	int stringLength = GuiDraw.getStringWidth(EnumChatFormatting.BOLD + I18n.format("gui.nei.seeAll"));
    	transferRects.add(new RecipeTransferRect(new Rectangle(getGuiWidth()-stringLength-3, 5, stringLength, 9), getOutputId()));    
    }
	
    public String getOutputId() {
    	return null;
    }
    
    public int getMaximumMaterialIndex(short meta, boolean smallOre) {
    	int offset = smallOre? 16000: 0;
    	if (!getGTOreLocalizedName((short)(meta+offset+5000)).equals(getGTOreUnlocalizedName((short)(meta+offset+5000))))
    		return 7;
    	else if (!getGTOreLocalizedName((short)(meta+offset+5000)).equals(getGTOreUnlocalizedName((short)(meta+offset+5000))))
    		return 6;
    	else 
    		return 5;
    }
    
    public String getGTOreLocalizedName(short index) {
    	return GT_LanguageManager.getTranslation(getGTOreUnlocalizedName(index));
    }
    
	public String getGTOreUnlocalizedName(short index) {
		return "gt.blockores." + index + ".name";
	}
	
	public String getBiomeTranslated(String unlocalizedBiome) {
		return unlocalizedBiome.equals("None")? I18n.format("gtnop.biome.none.name"): unlocalizedBiome;
	}
	
	public String getWorldNameTranslated(boolean genOverworld, boolean genNether, boolean genEnd, boolean genMoon, boolean genMars) {
		String worldNameTranslated = "";
    	if (genOverworld) {
    		if (!worldNameTranslated.isEmpty())
    			worldNameTranslated += ", ";
    		worldNameTranslated += I18n.format("gtnop.world.overworld.name");
    	}
    	if (genNether) {
    		if (!worldNameTranslated.isEmpty())
    			worldNameTranslated += ", ";
    		worldNameTranslated += I18n.format("gtnop.world.nether.name");
    	}
    	if (genEnd) {
    		if (!worldNameTranslated.isEmpty())
    			worldNameTranslated += ", ";
    		worldNameTranslated += I18n.format("gtnop.world.end.name");
    	}
    	if (genMoon) {
    		if (!worldNameTranslated.isEmpty())
    			worldNameTranslated += ", ";
    		worldNameTranslated += I18n.format("gtnop.world.moon.name");
    	}
    	if (genMars) {
    		if (!worldNameTranslated.isEmpty())
    			worldNameTranslated += ", ";
    		worldNameTranslated += I18n.format("gtnop.world.mars.name");
    	}
		return worldNameTranslated;
	}
	
	public int getGuiWidth() {
	    return 166;
	}

}
