package pers.gwyog.gtneioreplugin.plugin.gregtech6;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler.CachedRecipe;
import gregapi.block.IBlockPlacable;
import gregapi.block.behaviors.Drops_SmallOre;
import gregapi.block.prefixblock.PrefixBlock;
import gregapi.data.CS;
import gregapi.data.OP;
import gregapi.oredict.OreDictMaterial;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import pers.gwyog.gtneioreplugin.util.GT6OreSmallHelper;
import pers.gwyog.gtneioreplugin.util.GT6OreSmallHelper.OreSmallWrapper;

public class PluginGT6SmallOreStat extends PluginGT6Base {

    public class CachedOreSmallRecipe extends CachedRecipe {
        public String oreGenName;
        public PositionedStack positionedStackOreSmall;
        public PositionedStack positionedStackMaterialDust;
        public List<PositionedStack> positionedDropStackList;

        public CachedOreSmallRecipe(String oreGenName, List<ItemStack> stackList, List<ItemStack> materialDustStackList, List<ItemStack> dropStackList) {
            this.oreGenName = oreGenName;
            this.positionedStackOreSmall = new PositionedStack(stackList, 2, 0);
            this.positionedStackMaterialDust = new PositionedStack(materialDustStackList, 43, 67);
            List<PositionedStack> positionedDropStackList = new ArrayList<PositionedStack>();
            int i = 1;
            for (ItemStack stackDrop: dropStackList)
                positionedDropStackList.add(new PositionedStack(stackDrop, 43+20*(i%5), 67+17*((i++)/5)));
            this.positionedDropStackList = positionedDropStackList;
        }

        @Override
        public List<PositionedStack> getIngredients() {
            positionedStackOreSmall.setPermutationToRender((cycleticks / 20) % positionedStackOreSmall.items.length);
            positionedStackMaterialDust.setPermutationToRender((cycleticks / 20) % positionedStackMaterialDust.items.length);
            positionedDropStackList.add(positionedStackOreSmall);
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
        CachedOreSmallRecipe crecipe = (CachedOreSmallRecipe) this.arecipes.get(recipe);
        OreSmallWrapper oreSmall = GT6OreSmallHelper.mapOreSmallWrapper.get(crecipe.oreGenName);
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.oreName") + ": " + getLocalizedSmallOreName(oreSmall.oreMeta), 2, 18, 0x404040, false);
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.genHeight") + ": " + oreSmall.worldGenHeightRange, 2, 31, 0x404040, false);
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.amount") + ": " + oreSmall.amountPerChunk, 2, 44, 0x404040, false);
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.worldNames") + ": " + getWorldNameTranslated(oreSmall.genOverworld, oreSmall.genNether, oreSmall.genEnd, false, false), 2, 57, 0x404040, false);
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.chanceDrops") + ": ", 2, 70, 0x404040, false);
        GuiDraw.drawStringR(EnumChatFormatting.BOLD + I18n.format("gtnop.gui.nei.seeAll"), getGuiWidth()-3, 5, 0x404040, false);
    }
    
    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(getOutputId()))
            for (ItemStack stack: GT6OreSmallHelper.oreSmallList)
                loadCraftingRecipes(stack);
        else
            super.loadCraftingRecipes(outputId, results);
    }
    
    @Override
    public void loadCraftingRecipes(ItemStack stack) {
        if (stack.getUnlocalizedName().startsWith("oredict.oreSmall") && !stack.isItemEqual(new ItemStack(CS.BlocksGT.oreSmallBedrock, 1, stack.getItemDamage()))) {
            short meta = (short)stack.getItemDamage();
            for (OreSmallWrapper oreSmallWorldGen: GT6OreSmallHelper.mapOreSmallWrapper.values()) {
                if (oreSmallWorldGen.oreMeta == meta) {
                    List<ItemStack> stackList = new ArrayList<ItemStack>();
                    List<ItemStack> materialDustStackList = new ArrayList<ItemStack>();
                    for (PrefixBlock prefixBlock: GT6OreSmallHelper.setOreSmallBasicTypes) {
                        stackList.add(new ItemStack(prefixBlock, 1, meta));
                        Drops_SmallOre drops_SmallOre = ((Drops_SmallOre)prefixBlock.mDrops);
                        Class clazzDropsOreSmall = drops_SmallOre.getClass();
                        try {
                            Field fieldSecondaryDrop = clazzDropsOreSmall.getDeclaredField("mSecondaryDrop");
                            fieldSecondaryDrop.setAccessible(true);
                            OreDictMaterial oreDictMaterial = (OreDictMaterial)fieldSecondaryDrop.get(drops_SmallOre);
                            materialDustStackList.add(OP.dust.mat(oreDictMaterial.mTargetCrushing.mMaterial, 1L));
                        } catch (Exception e) {}
                    }
                    this.arecipes.add(new CachedOreSmallRecipe(oreSmallWorldGen.oreGenName, stackList, materialDustStackList, GT6OreSmallHelper.mapOreMetaToOreDrops.get(meta)));
                }
            }
        }
        else if (GT6OreSmallHelper.mapOreDropUnlocalizedNameToOreMeta.keySet().contains(stack.getUnlocalizedName())) {
            short meta = GT6OreSmallHelper.mapOreDropUnlocalizedNameToOreMeta.get(stack.getUnlocalizedName());
            for (String oreGenName: GT6OreSmallHelper.mapOreSmallWrapper.keySet()) {
                OreSmallWrapper oreSmallWrapper = GT6OreSmallHelper.mapOreSmallWrapper.get(oreGenName);
                if (oreSmallWrapper.oreMeta == meta) {
                    List<ItemStack> stackList = new ArrayList<ItemStack>();
                    List<ItemStack> materialDustStackList = new ArrayList<ItemStack>();
                    for (PrefixBlock prefixBlock: GT6OreSmallHelper.setOreSmallBasicTypes) {
                        stackList.add(new ItemStack(prefixBlock, 1, meta));
                        Drops_SmallOre drops_OreSmall = ((Drops_SmallOre)prefixBlock.mDrops);
                        Class clazzDropsOreSmall = drops_OreSmall.getClass();
                        try {
                            Field fieldSecondaryDrop = clazzDropsOreSmall.getDeclaredField("mSecondaryDrop");
                            fieldSecondaryDrop.setAccessible(true);
                            OreDictMaterial oreDictMaterial = (OreDictMaterial)fieldSecondaryDrop.get(drops_OreSmall);
                            materialDustStackList.add(OP.dust.mat(oreDictMaterial.mTargetCrushing.mMaterial, 1L));
                        } catch (Exception e) {}
                    }
                    this.arecipes.add(new CachedOreSmallRecipe(GT6OreSmallHelper.mapOreSmallWrapper.get(oreGenName).oreGenName, stackList, materialDustStackList, GT6OreSmallHelper.mapOreMetaToOreDrops.get(meta)));
                }
            }
        }    
        else
            super.loadCraftingRecipes(stack);
    }
    
    @Override
    public String getOutputId() {
        return "GTOrePluginOreSmall";
    }
        
    @Override
    public String getRecipeName() {
        return I18n.format("gtnop.gui.smallOreStat.name");
    }
    
}
