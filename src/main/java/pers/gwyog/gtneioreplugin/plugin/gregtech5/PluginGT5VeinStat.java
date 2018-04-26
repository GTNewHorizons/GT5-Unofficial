package pers.gwyog.gtneioreplugin.plugin.gregtech5;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import gregtech.api.GregTech_API;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import pers.gwyog.gtneioreplugin.GTNEIOrePlugin;
import pers.gwyog.gtneioreplugin.util.GT5CFGHelper;
import pers.gwyog.gtneioreplugin.util.GT5OreLayerHelper;
import pers.gwyog.gtneioreplugin.util.GT5OreLayerHelper.OreLayerWrapper;
import pers.gwyog.gtneioreplugin.util.Oremix;

public class PluginGT5VeinStat extends PluginGT5Base {

    public class CachedVeinStatRecipe extends CachedRecipe {
        public String veinName;
        public PositionedStack positionedStackPrimary;
        public PositionedStack positionedStackSecondary;
        public PositionedStack positionedStackBetween;
        public PositionedStack positionedStackSporadic;
            
        public CachedVeinStatRecipe(String veinName, List<ItemStack> stackListPrimary, List<ItemStack> stackListSecondary,
                List<ItemStack> stackListBetween, List<ItemStack> stackListSporadic) {
            this.veinName = veinName;
            positionedStackPrimary = new PositionedStack(stackListPrimary, 2, 0);
            positionedStackSecondary = new PositionedStack(stackListSecondary, 22, 0);
            positionedStackBetween = new PositionedStack(stackListBetween, 42, 0);
            positionedStackSporadic = new PositionedStack(stackListSporadic, 62, 0);
        }
        
        @Override
        public List<PositionedStack> getIngredients() {
            List<PositionedStack> ingredientsList = new ArrayList<PositionedStack>();
            positionedStackPrimary.setPermutationToRender((cycleticks / 20) % positionedStackPrimary.items.length);;
            positionedStackSecondary.setPermutationToRender((cycleticks / 20) % positionedStackPrimary.items.length);;
            positionedStackBetween.setPermutationToRender((cycleticks / 20) % positionedStackPrimary.items.length);;
            positionedStackSporadic.setPermutationToRender((cycleticks / 20) % positionedStackPrimary.items.length);;
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
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(getOutputId())) {
            OreLayerWrapper oreLayerWrapper;
            for (String veinName: GT5OreLayerHelper.mapOreLayerWrapper.keySet()) {
                oreLayerWrapper = GT5OreLayerHelper.mapOreLayerWrapper.get(veinName);
                List<ItemStack> stackListPrimary = new ArrayList<ItemStack>();
                List<ItemStack> stackListSecondary = new ArrayList<ItemStack>();
                List<ItemStack> stackListBetween = new ArrayList<ItemStack>();
                List<ItemStack> stackListSporadic = new ArrayList<ItemStack>();
                for (int i=0;i<7;i++) {
                    stackListPrimary.add(new ItemStack(GregTech_API.sBlockOres1, 1, oreLayerWrapper.Meta[0]+i*1000));
                    stackListSecondary.add(new ItemStack(GregTech_API.sBlockOres1, 1, oreLayerWrapper.Meta[1]+i*1000));
                    stackListBetween.add(new ItemStack(GregTech_API.sBlockOres1, 1, oreLayerWrapper.Meta[2]+i*1000));
                    stackListSporadic.add(new ItemStack(GregTech_API.sBlockOres1, 1, oreLayerWrapper.Meta[3]+i*1000));
                }    
                this.arecipes.add(new CachedVeinStatRecipe(veinName, stackListPrimary, stackListSecondary, stackListBetween, stackListSporadic));
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
            for (OreLayerWrapper worldGen: GT5OreLayerHelper.mapOreLayerWrapper.values()) {
                if (worldGen.Meta[0] == baseMeta || worldGen.Meta[1] == baseMeta || worldGen.Meta[2] == baseMeta || worldGen.Meta[3] == baseMeta) {
                    List<ItemStack> stackListPrimary = new ArrayList<ItemStack>();
                    List<ItemStack> stackListSecondary = new ArrayList<ItemStack>();
                    List<ItemStack> stackListBetween = new ArrayList<ItemStack>();
                    List<ItemStack> stackListSporadic = new ArrayList<ItemStack>();
                    for (int i=0;i<getMaximumMaterialIndex(baseMeta, false);i++) {
                        stackListPrimary.add(new ItemStack(GregTech_API.sBlockOres1, 1, worldGen.Meta[0]+i*1000));
                        stackListSecondary.add(new ItemStack(GregTech_API.sBlockOres1, 1, worldGen.Meta[1]+i*1000));
                        stackListBetween.add(new ItemStack(GregTech_API.sBlockOres1, 1, worldGen.Meta[2]+i*1000));
                        stackListSporadic.add(new ItemStack(GregTech_API.sBlockOres1, 1, worldGen.Meta[3]+i*1000));
                    }
                    this.arecipes.add(new CachedVeinStatRecipe(worldGen.veinName, stackListPrimary, stackListSecondary, stackListBetween, stackListSporadic));
                }
            }
        }
        else
            super.loadCraftingRecipes(stack);
    }
       
    @Override
    public void drawExtras(int recipe) {
        CachedVeinStatRecipe crecipe = (CachedVeinStatRecipe) this.arecipes.get(recipe);
        OreLayerWrapper oreLayer = GT5OreLayerHelper.mapOreLayerWrapper.get(crecipe.veinName);
        
        String Dims = getDims(oreLayer); 
        
        /*if (getLocalizedVeinName(oreLayer).length>1) {
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.veinName") + ": " + getLocalizedVeinName(oreLayer)[0], 2, 20, 0x404040, false);
        if (getLocalizedVeinName(oreLayer).length>2) {
        	GuiDraw.drawString(I18n.format(getLocalizedVeinName(oreLayer)[1]), 2, 30, 0x404040, false);
        	GuiDraw.drawString(I18n.format(getLocalizedVeinName(oreLayer)[2]), 2, 40, 0x404040, false);
        }
        else
        GuiDraw.drawString(I18n.format(getLocalizedVeinName(oreLayer)[1]), 2, 30, 0x404040, false);
        }
        else*/
        if (getGTOreLocalizedName(oreLayer.Meta[0]).contains("Ore"))
        	GuiDraw.drawString(I18n.format("gtnop.gui.nei.veinName") + ": " + getGTOreLocalizedName(oreLayer.Meta[0]).split("Ore")[0] + " " +I18n.format("gtnop.gui.nei.vein"), 2, 20, 0x404040, false);
        else if (getGTOreLocalizedName(oreLayer.Meta[0]).contains("Sand"))
            GuiDraw.drawString(I18n.format("gtnop.gui.nei.veinName") + ": " + getGTOreLocalizedName(oreLayer.Meta[0]).split("Sand")[0] + " " +I18n.format("gtnop.gui.nei.vein"), 2, 20, 0x404040, false);   
        else
        	GuiDraw.drawString(I18n.format("gtnop.gui.nei.veinName") + ": " + getGTOreLocalizedName(oreLayer.Meta[0]) + " " +I18n.format("gtnop.gui.nei.vein"), 2, 20, 0x404040, false);
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.primaryOre") + ": " + getGTOreLocalizedName(oreLayer.Meta[0]), 2, 50, 0x404040, false);
        
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.secondaryOre") + ": " + getGTOreLocalizedName(oreLayer.Meta[1]), 2, 60, 0x404040, false);
        
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.betweenOre") + ": " + getGTOreLocalizedName(oreLayer.Meta[2]), 2, 70, 0x404040, false);
        
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.sporadicOre") + ": " + getGTOreLocalizedName(oreLayer.Meta[3]), 2, 80, 0x404040, false);
        
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.genHeight") + ": " + oreLayer.worldGenHeightRange, 2, 90, 0x404040, false);
       
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.weightedChance") + ": " + Integer.toString(oreLayer.randomWeight), 100, 90, 0x404040, false);
        
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.worldNames") + ": ", 2, 100, 0x404040, false);
        if (Dims.length()>36) {
        GuiDraw.drawString(I18n.format("") + Dims.substring(0, 36), 2, 110, 0x404040, false);
        	if (Dims.length()>70) {
        	GuiDraw.drawString(I18n.format("") + Dims.substring(36, 70), 2, 120, 0x404040, false);
        	GuiDraw.drawString(I18n.format("") + Dims.substring(70, Dims.length()-1), 2, 130, 0x404040, false);
        }
        else
        GuiDraw.drawString(I18n.format("") + Dims.substring(36, Dims.length()-1), 2, 120, 0x404040, false);
        }
        else
        GuiDraw.drawString(I18n.format("") + Dims.substring(0, Dims.length()-1), 2, 110, 0x404040, false);
            
        
        //if (GT5OreLayerHelper.restrictBiomeSupport) GuiDraw.drawString(I18n.format("gtnop.gui.nei.restrictBiome") + ": " + getBiomeTranslated(oreLayer.restrictBiome), 2, 122, 0x404040, false);
        GuiDraw.drawStringR(EnumChatFormatting.BOLD + I18n.format("gtnop.gui.nei.seeAll"), getGuiWidth()-3, 5, 0x404040, false);
        
    }
    
    public static String[] getLocalizedVeinName(OreLayerWrapper oreLayer) {
    	
    	String unlocalizedName = oreLayer.veinName;
        if (unlocalizedName.startsWith("ore.mix.custom."))
            return get_Cnames(oreLayer);//I18n.format("gtnop.ore.custom.name") + I18n.format("gtnop.ore.vein.name") + unlocalizedName.substring(15);
        else
            return new String[] {I18n.format("gtnop." + unlocalizedName) + I18n.format("gtnop.ore.vein.name")};
    }
    
    public static String coustomVeinRenamer(OreLayerWrapper oreLayer) {
    	Set<String> s = new HashSet<String>();
    	for (int i=0; i < 4; i++)
    		s.add(getGTOreLocalizedName(oreLayer.Meta[i]).replaceAll(" ", ""));
    	return s.toString()
    			.replace("[".charAt(0), ",".charAt(0))
    			.replace("]".charAt(0), ",".charAt(0))
    			.replaceAll(" Ore", ",")
    			.replaceAll("Ore", ",")
    			.replaceAll(" Sand", ",")
    			.replaceAll("Sand", ",")
    			.replaceAll("Stone", ",")
    			.replaceAll(" Stone", ",")
    			.replaceAll("Earth", ",")
    			.replaceAll(" Earth", ",")
    			.replaceAll("Infused", ",")
    			.replaceAll(" Infused", ",")
    			.replaceAll(",","")
    			.trim();
    }
    
    /*public String getWeightedChance(OreLayerWrapper oreLayer) {
        String weightedChance = "";
        for (int i=0; i < oreLayer.alloweddims.size(); i++) {
        if (oreLayer.alloweddims.get(i) && (oreLayer.Weight.get(i) != 0)) {
            if (!weightedChance.isEmpty())
                weightedChance += ", ";
            weightedChance += String.format("%.2f%%", (100.0f*oreLayer.Weight.get(i))/GT5OreLayerHelper.weightPerWorld[i]);
        }
        }
        return weightedChance;
    }*/
    
    public static String getDims(OreLayerWrapper oreLayer)  {
    	return GT5CFGHelper.GT5CFG(GregTech_API.sWorldgenFile.mConfig.getConfigFile(), oreLayer.veinName.replace("ore.mix.custom.", "").replace("ore.mix.", ""));
    }
    
    public static String[] get_Cnames(OreLayerWrapper oreLayer) {
    	
    	String[] splt = coustomVeinRenamer(oreLayer).split("\\s");
    	/*HashSet<String> h = new HashSet<String>();
    	for (int i=0; i < splt.length;i++) {
    		h.add(splt[i]);
    	}
    	h.toArray(splt);*/
    	
    	String[] ret = {oreLayer.veinName.replace("ore.mix.custom.", "")+" "," "," "};
    	for (int i=0; i < splt.length;i++) {
    		//FMLLog.info("Split:"+splt[i]);
    		//FMLLog.info("I:"+Integer.toString(i));
    	if(ret[0].length()+splt[i].length()<=20)
        	ret[0]=ret[0]+splt[i]+" ";
    	if((ret[0].length()+splt[i].length()>20)&&ret[1].length()+splt[i].length()<=70&&!ret[0].contains(splt[i]))
    		ret[1]=ret[1]+splt[i]+" ";
    	if((ret[0].length()+splt[i].length()>20)&&(ret[1].length()+splt[i].length()>70)&&ret[2].length()+splt[i].length()<=70&&!ret[1].contains(splt[i]))
    		ret[2]=ret[2]+splt[i]+" ";
    	}		
    	for (int i=0; i < ret.length;i++) {
    		ret[i]=ret[i].trim();
    	}
    	
    	if(ret[2].isEmpty()&&!ret[1].isEmpty())
    		if(ret[1].length()<=65)
    			ret[1]=ret[1]+" Vein";
    		else
    			ret[2]=ret[2]+"Vein";
    	else if(ret[1].isEmpty()&&ret[2].isEmpty()&&!ret[0].isEmpty())
    		if(ret[0].length()<=15)
    			ret[0]=ret[0]+" Vein";
    		else
    			ret[1]=ret[1]+"Vein";
    	else if (!(ret[1].isEmpty()&&ret[2].isEmpty()))
    			ret[2]=ret[2]+"Vein";
    	String[] ret2 = new String[2];
    	if (ret[2].isEmpty()&&!ret[1].isEmpty()) {
    		ret2[0] = ret[0];
    		ret2[1] = ret[1];
    		return ret2;
    	}
    	String[] ret1 = new String[1];
    	if (ret[1].isEmpty()&&ret[1].isEmpty()&&!ret[0].isEmpty()) {
    		ret1[0] = ret[0];
    	return ret1;
    	}
    	else
    	return ret;
    }
    
    @Override
    public String getOutputId() {
        return "GTOrePluginVein";
    }
    
    @Override
    public String getRecipeName() {
        return I18n.format("gtnop.gui.veinStat.name");
    }
    
}
