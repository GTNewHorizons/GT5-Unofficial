package gtneioreplugin.plugin.gregtech5;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

import codechicken.nei.PositionedStack;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.common.blocks.BlockOres2;
import gregtech.common.blocks.ItemOres2;
import gregtech.common.blocks.BlockOres2.StoneType;
import gtneioreplugin.plugin.item.ItemDimensionDisplay;
import gtneioreplugin.util.GT5OreSmallHelper;
import gtneioreplugin.util.GT5OreSmallHelper.OreSmallWrapper;

public class PluginGT5SmallOreStat extends PluginGT5Base {

    @Override
    public void drawExtras(int recipe) {
        OreSmallWrapper oreSmall = getSmallOre(recipe);

        drawSmallOreName(oreSmall);
        drawSmallOreInfo(oreSmall);

        drawDimNames();

        drawSeeAllRecipesLabel();
    }

    private void drawSmallOreName(OreSmallWrapper oreSmall) {
        String oreName = getGTOreLocalizedName(oreSmall.material, true);
        drawLine("gtnop.gui.nei.oreName", oreName, 2, 18);
    }

    private void drawSmallOreInfo(OreSmallWrapper oreSmall) {
        drawLine("gtnop.gui.nei.genHeight", oreSmall.worldGenHeightRange, 2, 31);
        drawLine("gtnop.gui.nei.amount", String.valueOf(oreSmall.amountPerChunk), 2, 44);
        drawLine("gtnop.gui.nei.chanceDrops", "", 2, 83 + getRestrictBiomeOffset());
        drawLine("gtnop.gui.nei.worldNames", "", 2, 100);
    }

    private OreSmallWrapper getSmallOre(int recipe) {
        CachedOreSmallRecipe crecipe = (CachedOreSmallRecipe) this.arecipes.get(recipe);
        return GT5OreSmallHelper.SMALL_ORES_BY_NAME.get(crecipe.oreGenName);
    }

    public int getRestrictBiomeOffset() {
        return GT5OreSmallHelper.restrictBiomeSupport ? 0 : -13;
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(getOutputId()))
            for (ItemStack stack : GT5OreSmallHelper.SMALL_ORE_LIST) loadCraftingRecipes(stack);
        else super.loadCraftingRecipes(outputId, results);
    }

    @Override
    public void loadCraftingRecipes(ItemStack stack) {
        if (stack.getItem() instanceof ItemOres2) {
            loadSmallOre(BlockOres2.getMaterial(stack.getItemDamage()));
        } else if (GT5OreSmallHelper.ORE_DROP_TO_MAT.containsKey(stack.getUnlocalizedName())) {
            loadSmallOre(GT5OreSmallHelper.ORE_DROP_TO_MAT.get(stack.getUnlocalizedName()));
        } else {
            super.loadCraftingRecipes(stack);
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack stack) {
        String abbr = ItemDimensionDisplay.getDimension(stack);
        if (abbr == null) return;

        for (OreSmallWrapper oreVein : GT5OreSmallHelper.SMALL_ORES_BY_DIM.get(abbr).smallOres) {
            addSmallOre(oreVein);
        }
    }

    private void loadSmallOre(Materials material) {
        OreSmallWrapper smallOre = getSmallOre(material);
        if (smallOre != null) {
            addSmallOre(smallOre);
        }
    }

    private OreSmallWrapper getSmallOre(Materials material) {
        for (OreSmallWrapper oreSmallWorldGen : GT5OreSmallHelper.SMALL_ORES_BY_NAME.values()) {
            if (oreSmallWorldGen.material == material) {
                return oreSmallWorldGen;
            }
        }
        return null;
    }

    private void addSmallOre(OreSmallWrapper smallOre) {
        this.arecipes.add(
            new CachedOreSmallRecipe(
                smallOre.oreGenName,
                smallOre.getMaterialDrops(),
                getStoneDusts(),
                GT5OreSmallHelper.ORE_MAT_TO_DROPS.get(smallOre.material)));
    }

    private List<ItemStack> getStoneDusts() {
        List<ItemStack> stoneDusts = new ArrayList<>();

        for (StoneType stoneType : StoneType.STONE_TYPES) {
            stoneDusts.add(GTOreDictUnificator.get(OrePrefixes.dust, stoneType.material, 1));
        }

        return stoneDusts;
    }

    @Override
    public String getOutputId() {
        return "GTOrePluginOreSmall";
    }

    @Override
    public String getRecipeName() {
        return I18n.format("gtnop.gui.smallOreStat.name");
    }

    public class CachedOreSmallRecipe extends CachedRecipe {

        public final String oreGenName;
        public final PositionedStack positionedStackOreSmall;
        public final PositionedStack positionedStackMaterialDust;
        public final List<PositionedStack> positionedDropStackList;
        private final List<PositionedStack> dimensionDisplayItems = new ArrayList<>();

        public CachedOreSmallRecipe(String oreGenName, List<ItemStack> stackList, List<ItemStack> materialDustStackList,
            List<ItemStack> dropStackList) {
            this.oreGenName = oreGenName;
            this.positionedStackOreSmall = new PositionedStack(stackList, 2, 0);
            this.positionedStackMaterialDust = new PositionedStack(
                materialDustStackList,
                43,
                79 + getRestrictBiomeOffset());
            List<PositionedStack> positionedDropStackList = new ArrayList<>();
            int i = 1;
            for (ItemStack stackDrop : dropStackList) positionedDropStackList.add(
                new PositionedStack(stackDrop, 43 + 20 * (i % 4), 79 + 16 * ((i++) / 4) + getRestrictBiomeOffset()));
            this.positionedDropStackList = positionedDropStackList;
            setDimensionDisplayItems();
        }

        private void setDimensionDisplayItems() {
            int x = 2;
            int y = 110;
            int count = 0;
            int itemsPerLine = 9;
            int itemSize = 18;
            
            OreSmallWrapper wrapper = GT5OreSmallHelper.SMALL_ORES_BY_NAME.get(this.oreGenName);

            for (String abbrDimName : wrapper.enabledDims) {
                ItemStack item = ItemDimensionDisplay.getItem(abbrDimName);
                if (item != null) {
                    int xPos = x + itemSize * (count % itemsPerLine);
                    int yPos = y + itemSize * (count / itemsPerLine);
                    dimensionDisplayItems.add(new PositionedStack(item, xPos, yPos, false));
                    count++;
                }
            }
        }

        @Override
        public List<PositionedStack> getIngredients() {
            return dimensionDisplayItems;
        }

        @Override
        public PositionedStack getResult() {
            return null;
        }

        @Override
        public List<PositionedStack> getOtherStacks() {
            List<PositionedStack> outputs = new ArrayList<>();
            positionedStackOreSmall.setPermutationToRender((cycleticks / 20) % positionedStackOreSmall.items.length);
            positionedStackMaterialDust
                .setPermutationToRender((cycleticks / 20) % positionedStackMaterialDust.items.length);
            outputs.add(positionedStackOreSmall);
            outputs.add(positionedStackMaterialDust);
            outputs.addAll(positionedDropStackList);
            return outputs;
        }
    }
}
