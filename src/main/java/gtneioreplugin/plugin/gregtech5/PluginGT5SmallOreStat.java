package gtneioreplugin.plugin.gregtech5;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

import codechicken.nei.PositionedStack;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.OrePrefixes.ParsedOreDictName;
import gregtech.api.enums.StoneType;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.common.ores.IOreAdapter;
import gregtech.common.ores.OreInfo;
import gregtech.common.ores.OreManager;
import gtneioreplugin.plugin.item.ItemDimensionDisplay;
import gtneioreplugin.util.GT5OreSmallHelper;
import gtneioreplugin.util.GT5OreSmallHelper.OreSmallWrapper;
import gtneioreplugin.util.GT5OreSmallHelper.SmallOreDimensionWrapper;

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
        drawLine("gtnop.gui.nei.chanceDrops", "", 2, 70);
        drawLine("gtnop.gui.nei.worldNames", "", 2, 100);
    }

    private OreSmallWrapper getSmallOre(int recipe) {
        CachedOreSmallRecipe crecipe = (CachedOreSmallRecipe) this.arecipes.get(recipe);
        return GT5OreSmallHelper.SMALL_ORES_BY_NAME.get(crecipe.oreGenName);
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(getOutputId())) {
            for (ItemStack stack : GT5OreSmallHelper.SMALL_ORE_LIST) {
                loadCraftingRecipes(stack);
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack stack) {
        IOreMaterial mat = OreManager.getMaterial(stack);

        if (mat == null) {
            mat = GT5OreSmallHelper.ORE_DROP_TO_MAT.get(stack.getUnlocalizedName());
        }

        if (mat != null) {
            loadSmallOre(mat);
            return;
        }

        boolean isMatItem = false;

        for (ParsedOreDictName oredict : OrePrefixes.detectPrefix(stack)) {
            if (!PluginGT5VeinStat.PREFIX_WHITELIST.contains(oredict.prefix)) continue;

            mat = IOreMaterial.findMaterial(oredict.material);

            if (mat != null) {
                isMatItem |= loadSmallOre(mat);
            }
        }

        if (isMatItem) return;

        super.loadCraftingRecipes(stack);
    }

    @Override
    public void loadUsageRecipes(ItemStack stack) {
        String abbr = ItemDimensionDisplay.getDimension(stack);
        if (abbr == null) return;

        SmallOreDimensionWrapper wrapper = GT5OreSmallHelper.getSmallOrebyDim(abbr);
        if (wrapper == null) return;

        for (OreSmallWrapper oreVein : wrapper.smallOres) {
            addSmallOre(oreVein);
        }
    }

    private boolean loadSmallOre(IOreMaterial material) {
        OreSmallWrapper smallOre = GT5OreSmallHelper.SMALL_ORES_BY_MAT.get(material);

        if (smallOre != null) {
            addSmallOre(smallOre);

            return true;
        } else {
            return false;
        }
    }

    private void addSmallOre(OreSmallWrapper smallOre) {

        List<ItemStack> ores = smallOre.getOreVariants();
        List<ItemStack> dusts = new ArrayList<>();

        try (OreInfo<IOreMaterial> info = OreInfo.getNewInfo()) {
            info.material = smallOre.material;
            info.isSmall = true;

            IOreAdapter<?> adapter = OreManager.getAdapter(info);

            for (StoneType stoneType : StoneType.STONE_TYPES) {
                info.stoneType = stoneType;

                if (adapter.supports(info)) {
                    ores.add(adapter.getStack(info, 1));
                    dusts.add(stoneType.getDust(true, 1));
                }
            }
        }

        this.arecipes.add(
            new CachedOreSmallRecipe(
                smallOre.oreGenName,
                ores,
                dusts,
                GT5OreSmallHelper.ORE_MAT_TO_DROPS.get(smallOre.material)));
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
            this.positionedStackMaterialDust = new PositionedStack(materialDustStackList, 43, 66);
            List<PositionedStack> positionedDropStackList = new ArrayList<>();
            int i = 1;
            for (ItemStack stackDrop : dropStackList) {
                positionedDropStackList.add(new PositionedStack(stackDrop, 43 + 20 * (i % 4), 66 + 16 * ((i++) / 4)));
            }
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
