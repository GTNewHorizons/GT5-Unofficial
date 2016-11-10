package pers.gwyog.gtneioreplugin.plugin.gregtech6;

import java.util.ArrayList;
import java.util.List;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler.CachedRecipe;
import gregapi.block.prefixblock.PrefixBlock;
import gregapi.data.CS;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import pers.gwyog.gtneioreplugin.util.GT6OreBedrockHelper;
import pers.gwyog.gtneioreplugin.util.GT6OreLayerHelper;
import pers.gwyog.gtneioreplugin.util.GT6OreSmallHelper;
import pers.gwyog.gtneioreplugin.util.GT6OreBedrockHelper.OreBedrockWrapper;

public class PluginGT6BedrockOreStat extends PluginGT6Base {
    
    public class CachedBedrockOreStatRecipe extends CachedRecipe {
        public String oreName;
        public PositionedStack positionedStackOreBedrock;
        public PositionedStack positionedStackOreSmallBedrock;
        public PositionedStack positionedStackOre;
        public PositionedStack positionedStackOreSmall;
            
        public CachedBedrockOreStatRecipe(String oreName, ItemStack stackOreBedrock, ItemStack stackOreSmallBedrock,
                List<ItemStack> stackListOre, List<ItemStack> stackListOreSmall) {
            this.oreName = oreName;
            positionedStackOreBedrock = new PositionedStack(stackOreBedrock, 2, 0);
            positionedStackOreSmallBedrock = new PositionedStack(stackOreSmallBedrock, 22, 0);
            positionedStackOre = new PositionedStack(stackListOre, 42, 0);
            positionedStackOreSmall = new PositionedStack(stackListOreSmall, 62, 0);
        }
        
        @Override
        public List<PositionedStack> getIngredients() {
            List<PositionedStack> ingredientsList = new ArrayList<PositionedStack>();
            positionedStackOre.setPermutationToRender((cycleticks / 20) % positionedStackOre.items.length);;
            positionedStackOreSmall.setPermutationToRender((cycleticks / 20) % positionedStackOreSmall.items.length);;
            ingredientsList.add(positionedStackOreBedrock);
            ingredientsList.add(positionedStackOreSmallBedrock);
            ingredientsList.add(positionedStackOre);
            ingredientsList.add(positionedStackOreSmall);
            return ingredientsList;
        }
        
        @Override
        public PositionedStack getResult() {
            return null;
        }
        
    }
    
    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(getOutputId()))
            for (OreBedrockWrapper worldGen: GT6OreBedrockHelper.mapOreBedrockWrapper.values())
                loadCraftingRecipes(new ItemStack(CS.BlocksGT.oreBedrock, 1, worldGen.meta));
        else
            super.loadCraftingRecipes(outputId, results);
    }
    
    @Override
    public void loadCraftingRecipes(ItemStack stack) {
        String unlocalizedName = stack.getUnlocalizedName();
        if (unlocalizedName.startsWith("oredict.oreBedrock") || unlocalizedName.startsWith("oredict.ore") || unlocalizedName.startsWith("oredict.oreSmall")) {
            short meta = (short)stack.getItemDamage();
            for (OreBedrockWrapper worldGen: GT6OreBedrockHelper.mapOreBedrockWrapper.values()) {
                if (meta==worldGen.meta) {
                    ItemStack stackOreBedrock = new ItemStack(CS.BlocksGT.oreBedrock, 1, meta);
                    ItemStack stackOreSmallBedrock = new ItemStack(CS.BlocksGT.oreSmallBedrock, 1, meta);
                    List<ItemStack> stackListOre = new ArrayList<ItemStack>();
                    List<ItemStack> stackListOreSmall = new ArrayList<ItemStack>();
                    for (PrefixBlock prefixBlock: GT6OreLayerHelper.setOreNormalBasicTypes) 
                        stackListOre.add(new ItemStack(prefixBlock, 1, meta));
                    for (PrefixBlock prefixBlock: GT6OreSmallHelper.setOreSmallBasicTypes) 
                        stackListOreSmall.add(new ItemStack(prefixBlock, 1, meta));
                    this.arecipes.add(new CachedBedrockOreStatRecipe(worldGen.veinName, stackOreBedrock, stackOreSmallBedrock, stackListOre, stackListOreSmall));
                }
            }
        }
        else
            super.loadCraftingRecipes(stack);
    }
    
    @Override
    public void drawExtras(int recipe) {
        CachedBedrockOreStatRecipe crecipe = (CachedBedrockOreStatRecipe) this.arecipes.get(recipe);
        OreBedrockWrapper oreLayer = GT6OreBedrockHelper.mapOreBedrockWrapper.get(crecipe.oreName);
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.oreName") + ": " + getLocalizedOreBedrockName(oreLayer.meta), 2, 18, 0x404040, false);
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.oreOthers") + ": " + getLocalizedSmallOreName(oreLayer.meta), 2, 31, 0x404040, false);
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.oreOthers") + ": " + getLocalizedOreName(oreLayer.meta), 2, 44, 0x404040, false);
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.oreOthers") + ": " + getLocalizedSmallOreName(oreLayer.meta), 2, 57, 0x404040, false);
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.genHeight") + ": " + "0-6", 2, 70, 0x404040, false);
        GuiDraw.drawString(I18n.format("gtnop.nei.genPosition") + ": " + I18n.format("gtnop.nei.genPositionInfo"), 2,83, 0x404040, false);
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.weightedChance") + ": " + "1/" + oreLayer.probability, 2, 96, 0x404040, false);
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.worldNames") + ": " + getWorldNameTranslated(oreLayer.genOverworld, oreLayer.genNether, oreLayer.genEnd, false, false), 2, 109, 0x404040, false);
        GuiDraw.drawStringR(EnumChatFormatting.BOLD + I18n.format("gtnop.gui.nei.seeAll"), getGuiWidth()-3, 5, 0x404040, false);
    }
    
    @Override
    public String getOutputId() {
        return "GTOrePluginOreBedrock";
    }
    
    @Override
    public String getRecipeName() {
        return I18n.format("gtnop.gui.bedrockOreStat.name");
    }
    
}
