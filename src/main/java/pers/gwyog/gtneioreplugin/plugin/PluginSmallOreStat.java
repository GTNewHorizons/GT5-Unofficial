package pers.gwyog.gtneioreplugin.plugin;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.common.blocks.GT_Block_Ores_Abstract;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import pers.gwyog.gtneioreplugin.plugin.PluginAsteroidStat.CachedAsteroidStatRecipe;
import pers.gwyog.gtneioreplugin.plugin.PluginVeinStat.CachedVeinStatRecipe;
import pers.gwyog.gtneioreplugin.util.GTOreLayerHelper;
import pers.gwyog.gtneioreplugin.util.GTSmallOreHelper;
import pers.gwyog.gtneioreplugin.util.GTOreLayerHelper.OreLayerWrapper;
import pers.gwyog.gtneioreplugin.util.GTSmallOreHelper.SmallOreWrapper;

public class PluginSmallOreStat extends PluginBase {
    
    public class CachedSmallOreRecipe extends CachedRecipe {
        public String oreGenName;
        public PositionedStack positionedStackSmallOre;
        public PositionedStack positionedStackMaterialDust;
        public List<PositionedStack> positionedDropStackList;

        public CachedSmallOreRecipe(String oreGenName, List<ItemStack> stackList, List<ItemStack> materialDustStackList, List<ItemStack> dropStackList) {
            this.oreGenName = oreGenName;
            this.positionedStackSmallOre = new PositionedStack(stackList, 2, 0);
            this.positionedStackMaterialDust = new PositionedStack(materialDustStackList, 43, 79+getRestrictBiomeOffset());
            List<PositionedStack> positionedDropStackList = new ArrayList<PositionedStack>();
            int i = 1;
            for (ItemStack stackDrop: dropStackList)
                positionedDropStackList.add(new PositionedStack(stackDrop, 43+20*(i%4), 79+16*((i++)/4)+getRestrictBiomeOffset()));
            this.positionedDropStackList = positionedDropStackList;
        }

        @Override
        public List<PositionedStack> getIngredients() {
            positionedStackSmallOre.setPermutationToRender((cycleticks / 20) % positionedStackSmallOre.items.length);
            positionedStackMaterialDust.setPermutationToRender((cycleticks / 20) % positionedStackMaterialDust.items.length);
            positionedDropStackList.add(positionedStackSmallOre);
            positionedDropStackList.add(positionedStackMaterialDust);
            return positionedDropStackList;
            
        }
        
        @Override
        public PositionedStack getResult() {
            return null;
        }
        
    }
    
    @Override
    public void drawExtras(int recipe) {
        CachedSmallOreRecipe crecipe = (CachedSmallOreRecipe) this.arecipes.get(recipe);
        SmallOreWrapper smallOre = GTSmallOreHelper.mapSmallOreWrapper.get(crecipe.oreGenName);
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.oreName") + ": " + getGTOreLocalizedName((short)(smallOre.oreMeta+16000)), 2, 18, 0x404040, false);
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.genHeight") + ": " + smallOre.worldGenHeightRange, 2, 31, 0x404040, false);
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.amount") + ": " + smallOre.amountPerChunk, 2, 44, 0x404040, false);
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.worldNames") + ": " + getWorldNameTranslated(smallOre.genOverworld, smallOre.genNether, smallOre.genEnd, smallOre.genMoon, smallOre.genMars), 2, 57, 0x404040, false);
        if (GTSmallOreHelper.restrictBiomeSupport) GuiDraw.drawString(I18n.format("gtnop.gui.nei.restrictBiome") + ": " + getBiomeTranslated(smallOre.restrictBiome), 2, 70, 0x404040, false);
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.chanceDrops") + ": ", 2, 83+getRestrictBiomeOffset(), 0x404040, false);
        GuiDraw.drawStringR(EnumChatFormatting.BOLD + I18n.format("gtnop.gui.nei.seeAll"), getGuiWidth()-3, 5, 0x404040, false);
    }
    
    public int getRestrictBiomeOffset() {
        return GTSmallOreHelper.restrictBiomeSupport? 0: -13;
    }
    
    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(getOutputId()))
            for (ItemStack stack: GTSmallOreHelper.smallOreList)
                loadCraftingRecipes(stack);
        else
            super.loadCraftingRecipes(outputId, results);
    }
    
    @Override
    public void loadCraftingRecipes(ItemStack stack) {
        if (stack.getUnlocalizedName().startsWith("gt.blockores")) {
            if (stack.getItemDamage()<16000) {
                super.loadCraftingRecipes(stack);
                return;
            }
            short baseMeta = (short)(stack.getItemDamage() % 1000);
            for (SmallOreWrapper smallOreWorldGen: GTSmallOreHelper.mapSmallOreWrapper.values()) {
                if (smallOreWorldGen.oreMeta == baseMeta) {
                    List<ItemStack> stackList = new ArrayList<ItemStack>();
                    int maximumIndex = getMaximumMaterialIndex(baseMeta, true);
                    for (int i=0;i<maximumIndex;i++) 
                        stackList.add(new ItemStack(GregTech_API.sBlockOres1, 1, smallOreWorldGen.oreMeta+16000+i*1000));
                    List<ItemStack> materialDustStackList = new ArrayList<ItemStack>();
                    for (int i=0;i<maximumIndex;i++)
                        materialDustStackList.add(GT_OreDictUnificator.get(OrePrefixes.dust, GTSmallOreHelper.getDroppedDusts()[i], 1L));
                    this.arecipes.add(new CachedSmallOreRecipe(smallOreWorldGen.oreGenName, stackList, materialDustStackList, GTSmallOreHelper.mapOreMetaToOreDrops.get(baseMeta)));
                }
            }
        }
        else if (GTSmallOreHelper.mapOreDropUnlocalizedNameToOreMeta.keySet().contains(stack.getUnlocalizedName())) {
            short baseMeta = GTSmallOreHelper.mapOreDropUnlocalizedNameToOreMeta.get(stack.getUnlocalizedName());
            for (String oreGenName: GTSmallOreHelper.mapSmallOreWrapper.keySet()) {
                SmallOreWrapper smallOreWrapper = GTSmallOreHelper.mapSmallOreWrapper.get(oreGenName);
                if (smallOreWrapper.oreMeta == baseMeta) {
                    List<ItemStack> stackList = new ArrayList<ItemStack>();
                    for (int i=0;i<7;i++) 
                        stackList.add(new ItemStack(GregTech_API.sBlockOres1, 1, baseMeta+16000+i*1000));
                    List<ItemStack> materialDustStackList = new ArrayList<ItemStack>();
                    for (int i=0;i<7;i++)
                        materialDustStackList.add(GT_OreDictUnificator.get(OrePrefixes.dust, GTSmallOreHelper.getDroppedDusts()[i], 1L));
                    this.arecipes.add(new CachedSmallOreRecipe(GTSmallOreHelper.mapSmallOreWrapper.get(oreGenName).oreGenName, stackList, materialDustStackList, GTSmallOreHelper.mapOreMetaToOreDrops.get(baseMeta)));
                }
            }
        }    
        else
            super.loadCraftingRecipes(stack);
    }
    
    @Override
    public String getOutputId() {
        return "GTOrePluginSmallOre";
    }
        
    @Override
    public String getRecipeName() {
        return I18n.format("gtnop.gui.smallOreStat.name");
    }
}
