package gtneioreplugin.plugin.gregtech5;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

public class PluginGT5SmallOreStat extends PluginGT5OreBase {

    @Override
    public void drawExtras(int recipe) {
        CachedOreSmallRecipe crecipe = (CachedOreSmallRecipe) this.arecipes.get(recipe);
        crecipe.drawExtra();
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
        List<ItemStack> ores = new ArrayList<>();
        List<ItemStack> dusts = new ArrayList<>();

        Set<StoneType> stoneTypes = getStoneTypesForDimensions(smallOre.enabledDims.toArray(new String[0]));

        getOresAndDusts(smallOre, stoneTypes, ores, dusts);

        if (ores.isEmpty()) {
            // Fallback
            getOresAndDusts(smallOre, StoneType.STONE_TYPES, ores, dusts);
        }

        this.arecipes.add(
            new CachedOreSmallRecipe(smallOre, ores, dusts, GT5OreSmallHelper.ORE_MAT_TO_DROPS.get(smallOre.material)));
    }

    private void getOresAndDusts(OreSmallWrapper smallOre, Iterable<StoneType> stoneTypes, List<ItemStack> ores,
        List<ItemStack> dusts) {
        try (OreInfo<IOreMaterial> info = OreInfo.getNewInfo()) {
            info.material = smallOre.material;
            info.isSmall = true;

            IOreAdapter<?> adapter = OreManager.getAdapter(info);
            assert adapter != null;
            for (StoneType stoneType : stoneTypes) {
                info.stoneType = stoneType;

                if (adapter.supports(info)) {
                    ores.add(adapter.getStack(info, 1));
                    dusts.add(stoneType.getDust(true, 1));
                }
            }
        }
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

        public final OreSmallWrapper oreSmall;
        public final String oreGenName;
        public final PositionedStack positionedStackOreSmall;
        public final PositionedStack positionedStackMaterialDust;
        public final List<PositionedStack> positionedDropStackList;
        private final List<PositionedStack> dimensionDisplayItems = new ArrayList<>();

        private final List<String> title;
        private final int titleBottom;
        private final int chanceDropsPos;
        private final int dimHeaderYPos;

        public CachedOreSmallRecipe(OreSmallWrapper oreSmall, List<ItemStack> stackList,
            List<ItemStack> materialDustStackList, List<ItemStack> dropStackList) {
            this.oreSmall = oreSmall;
            this.oreGenName = oreSmall.oreGenName;

            title = getTitleLines(getGTOreLocalizedName(oreSmall.material, true));
            titleBottom = title.size() * 10 + 1 + TITLE_Y_POS;
            chanceDropsPos = titleBottom + 29;

            this.positionedStackOreSmall = new PositionedStack(stackList, getGuiWidth() - 32, titleBottom + 8);
            this.positionedStackMaterialDust = new PositionedStack(materialDustStackList, 0, 0);

            List<PositionedStack> positionedDropStackList = new ArrayList<>();
            int i = 0;
            for (ItemStack stackDrop : dropStackList) {
                int x = LEFT_PADDING + 18 * (i % 9);
                int y = chanceDropsPos + 10 + 18 * (i / 9);
                if (i == 0) {
                    positionedStackMaterialDust.relx = x;
                    positionedStackMaterialDust.rely = y;
                } else {
                    positionedDropStackList.add(new PositionedStack(stackDrop, x, y));
                }
                i++;
            }
            this.positionedDropStackList = positionedDropStackList;

            OreSmallWrapper wrapper = GT5OreSmallHelper.SMALL_ORES_BY_NAME.get(this.oreGenName);
            String[] abbrDims = wrapper.enabledDims.toArray(new String[0]);
            sortDimNamesByTier(abbrDims);
            dimHeaderYPos = chanceDropsPos + ((i / 9) + 1) * 16 + 14;
            createDimensionDisplayItems(abbrDims, dimHeaderYPos, dimensionDisplayItems);
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
            updateRenderPermutation(positionedStackOreSmall);
            updateRenderPermutation(positionedStackMaterialDust);
            outputs.add(positionedStackOreSmall);
            outputs.add(positionedStackMaterialDust);
            outputs.addAll(positionedDropStackList);
            return outputs;
        }

        public void drawExtra() {
            drawTitle(title);
            drawSmallOreInfo();

            drawDimHeader(dimHeaderYPos);

            drawSeeAllRecipesLabel();
        }

        private void drawSmallOreInfo() {
            drawLine("gtnop.gui.nei.genHeight", oreSmall.worldGenHeightRange, LEFT_PADDING, titleBottom + 3);
            drawLine("gtnop.gui.nei.amount", String.valueOf(oreSmall.amountPerChunk), LEFT_PADDING, titleBottom + 16);
            drawLine("gtnop.gui.nei.chanceDrops", "", LEFT_PADDING, chanceDropsPos);
        }
    }
}
