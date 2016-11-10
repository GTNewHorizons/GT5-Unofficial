package pers.gwyog.gtneioreplugin.plugin.gregtech6;

import java.util.ArrayList;
import java.util.List;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler.CachedRecipe;
import gregapi.block.IBlockPlacable;
import gregapi.block.prefixblock.PrefixBlock;
import gregapi.data.CS;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import pers.gwyog.gtneioreplugin.plugin.PluginBase;
import pers.gwyog.gtneioreplugin.util.GT6OreLayerHelper;
import pers.gwyog.gtneioreplugin.util.GT6OreLayerHelper.OreLayerWrapper;

public class PluginGT6VeinStat extends PluginBase {

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
            for (String veinName: GT6OreLayerHelper.mapOreLayerWrapper.keySet()) {
                oreLayerWrapper = GT6OreLayerHelper.mapOreLayerWrapper.get(veinName);
                List<ItemStack> stackListPrimary = new ArrayList<ItemStack>();
                List<ItemStack> stackListSecondary = new ArrayList<ItemStack>();
                List<ItemStack> stackListBetween = new ArrayList<ItemStack>();
                List<ItemStack> stackListSporadic = new ArrayList<ItemStack>();
                for (PrefixBlock prefixBlock: GT6OreLayerHelper.setOreNormalBasicTypes) {
                    stackListPrimary.add(new ItemStack(prefixBlock, 1, oreLayerWrapper.primaryMeta));
                    stackListSecondary.add(new ItemStack(prefixBlock, 1, oreLayerWrapper.secondaryMeta));
                    stackListBetween.add(new ItemStack(prefixBlock, 1, oreLayerWrapper.betweenMeta));
                    stackListSporadic.add(new ItemStack(prefixBlock, 1, oreLayerWrapper.sporadicMeta));
                }  
                this.arecipes.add(new CachedVeinStatRecipe(veinName, stackListPrimary, stackListSecondary, stackListBetween, stackListSporadic));
            }
        }
        else
            super.loadCraftingRecipes(outputId, results);
    }
    
    @Override
    public void loadCraftingRecipes(ItemStack stack) {
        String unlocalizedName = stack.getUnlocalizedName();
        if (unlocalizedName.startsWith("oredict.ore") && !unlocalizedName.startsWith("oredict.oreSmall") && !unlocalizedName.startsWith("oredict.oreBedrock")) {
            short meta = (short)stack.getItemDamage();
            for (OreLayerWrapper worldGen: GT6OreLayerHelper.mapOreLayerWrapper.values()) {
                if (meta==worldGen.primaryMeta || meta==worldGen.secondaryMeta || meta==worldGen.betweenMeta || meta==worldGen.sporadicMeta) {
                    List<ItemStack> stackListPrimary = new ArrayList<ItemStack>();
                    List<ItemStack> stackListSecondary = new ArrayList<ItemStack>();
                    List<ItemStack> stackListBetween = new ArrayList<ItemStack>();
                    List<ItemStack> stackListSporadic = new ArrayList<ItemStack>();
                    for (PrefixBlock prefixBlock: GT6OreLayerHelper.setOreNormalBasicTypes) {
                        stackListPrimary.add(new ItemStack(prefixBlock, 1, worldGen.primaryMeta));
                        stackListSecondary.add(new ItemStack(prefixBlock, 1, worldGen.secondaryMeta));
                        stackListBetween.add(new ItemStack(prefixBlock, 1, worldGen.betweenMeta));
                        stackListSporadic.add(new ItemStack(prefixBlock, 1, worldGen.sporadicMeta));
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
        OreLayerWrapper oreLayer = GT6OreLayerHelper.mapOreLayerWrapper.get(crecipe.veinName);
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.veinName") + ": " + getLocalizedVeinName(oreLayer.veinName), 2, 18, 0x404040, false);
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.primaryOre") + ": " + GT6OreLayerHelper.mapMetaToLocalizedName.get(oreLayer.primaryMeta), 2, 31, 0x404040, false);
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.secondaryOre") + ": " + GT6OreLayerHelper.mapMetaToLocalizedName.get(oreLayer.secondaryMeta), 2, 44, 0x404040, false);
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.betweenOre") + ": " + GT6OreLayerHelper.mapMetaToLocalizedName.get(oreLayer.betweenMeta), 2, 57, 0x404040, false);
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.sporadicOre") + ": " + GT6OreLayerHelper.mapMetaToLocalizedName.get(oreLayer.sporadicMeta), 2, 70, 0x404040, false);
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.genHeight") + ": " + oreLayer.worldGenHeightRange, 2, 83, 0x404040, false);
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.weightedChance") + ": " + oreLayer.weightedChance, 2, 96, 0x404040, false);
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.worldNames") + ": " + getWorldNameTranslated(oreLayer.genOverworld, oreLayer.genNether, oreLayer.genEnd, false, false), 2, 109, 0x404040, false);
        GuiDraw.drawStringR(EnumChatFormatting.BOLD + I18n.format("gtnop.gui.nei.seeAll"), getGuiWidth()-3, 5, 0x404040, false);
    }
    
    public String getLocalizedVeinName(String unlocalizedName) {
        if (unlocalizedName.startsWith("ore.mix.custom."))
            return I18n.format("gtnop.ore.custom.name") + I18n.format("gtnop.ore.vein.name") + unlocalizedName.substring(15);
        else
            return I18n.format("gtnop." + unlocalizedName) + I18n.format("gtnop.ore.vein.name");
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
