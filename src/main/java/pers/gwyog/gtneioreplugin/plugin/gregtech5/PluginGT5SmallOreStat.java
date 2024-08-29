package pers.gwyog.gtneioreplugin.plugin.gregtech5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

import codechicken.nei.PositionedStack;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import pers.gwyog.gtneioreplugin.plugin.item.ItemDimensionDisplay;
import pers.gwyog.gtneioreplugin.util.DimensionHelper;
import pers.gwyog.gtneioreplugin.util.GT5OreSmallHelper;
import pers.gwyog.gtneioreplugin.util.GT5OreSmallHelper.OreSmallWrapper;

public class PluginGT5SmallOreStat extends PluginGT5Base {

    private static final int SMALL_ORE_BASE_META = 16000;

    @Override
    public void drawExtras(int recipe) {
        OreSmallWrapper oreSmall = getSmallOre(recipe);

        drawSmallOreName(oreSmall);
        drawSmallOreInfo(oreSmall);

        drawDimNames();

        drawSeeAllRecipesLabel();
    }

    private void drawSmallOreName(OreSmallWrapper oreSmall) {
        String oreName = getGTOreLocalizedName((short) (oreSmall.oreMeta + SMALL_ORE_BASE_META));
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
        return GT5OreSmallHelper.mapOreSmallWrapper.get(crecipe.oreGenName);
    }

    public int getRestrictBiomeOffset() {
        return GT5OreSmallHelper.restrictBiomeSupport ? 0 : -13;
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(getOutputId()))
            for (ItemStack stack : GT5OreSmallHelper.oreSmallList) loadCraftingRecipes(stack);
        else super.loadCraftingRecipes(outputId, results);
    }

    @Override
    public void loadCraftingRecipes(ItemStack stack) {
        if (stack.getUnlocalizedName()
            .startsWith("gt.blockores")) {
            short oreMeta = (short) (stack.getItemDamage() % 1000);
            loadSmallOre(oreMeta, getMaximumMaterialIndex(oreMeta, true));
        } else if (GT5OreSmallHelper.mapOreDropUnlocalizedNameToOreMeta.containsKey(stack.getUnlocalizedName())) {
            short oreMeta = GT5OreSmallHelper.mapOreDropUnlocalizedNameToOreMeta.get(stack.getUnlocalizedName());
            loadSmallOre(oreMeta, 7);
        } else super.loadCraftingRecipes(stack);
    }

    @Override
    public void loadUsageRecipes(ItemStack stack) {
        String dimension = ItemDimensionDisplay.getDimension(stack);
        if (dimension == null) {
            return;
        }

        for (OreSmallWrapper oreVein : GT5OreSmallHelper.mapOreSmallWrapper.values()) {
            if (Arrays.asList(getDimNameArrayFromVeinName(oreVein.oreGenName))
                .contains(dimension)) {
                addSmallOre(oreVein, 7);
            }
        }
    }

    private void loadSmallOre(short oreMeta, int maximumIndex) {
        OreSmallWrapper smallOre = getSmallOre(oreMeta);
        if (smallOre != null) {
            addSmallOre(smallOre, maximumIndex);
        }
    }

    private OreSmallWrapper getSmallOre(short oreMeta) {
        for (OreSmallWrapper oreSmallWorldGen : GT5OreSmallHelper.mapOreSmallWrapper.values()) {
            if (oreSmallWorldGen.oreMeta == oreMeta) {
                return oreSmallWorldGen;
            }
        }
        return null;
    }

    private void addSmallOre(OreSmallWrapper smallOre, int maximumIndex) {
        this.arecipes.add(
            new CachedOreSmallRecipe(
                smallOre.oreGenName,
                smallOre.getMaterialDrops(maximumIndex),
                getStoneDusts(maximumIndex),
                GT5OreSmallHelper.mapOreMetaToOreDrops.get(smallOre.oreMeta)));
    }

    private List<ItemStack> getStoneDusts(int maximumIndex) {
        List<ItemStack> materialDustStackList = new ArrayList<>();
        for (int i = 0; i < maximumIndex; i++) materialDustStackList
            .add(GT_OreDictUnificator.get(OrePrefixes.dust, GT5OreSmallHelper.getDroppedDusts()[i], 1L));
        return materialDustStackList;
    }

    @Override
    public String getOutputId() {
        return "GTOrePluginOreSmall";
    }

    @Override
    public String getRecipeName() {
        return I18n.format("gtnop.gui.smallOreStat.name");
    }

    private String[] getDimNameArrayFromVeinName(String veinName) {
        OreSmallWrapper oreSmall = GT5OreSmallHelper.mapOreSmallWrapper.get(veinName);
        String[] dims = GT5OreSmallHelper.bufferedDims.get(oreSmall)
            .keySet()
            .toArray(new String[0]);
        Arrays.sort(
            dims,
            Comparator.comparingInt(
                s -> Arrays.asList(DimensionHelper.DimNameDisplayed)
                    .indexOf(s)));
        return dims;
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
            for (String dim : getDimNameArrayFromVeinName(this.oreGenName)) {
                ItemStack item = ItemDimensionDisplay.getItem(dim);
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
