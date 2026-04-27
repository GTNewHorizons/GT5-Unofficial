package gtneioreplugin.plugin.gregtech5;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;

import bartworks.system.material.Werkstoff;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.StackInfo;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.OrePrefixes.ParsedOreDictName;
import gregtech.api.enums.StoneType;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.common.ores.OreManager;
import gtneioreplugin.plugin.item.ItemDimensionDisplay;
import gtneioreplugin.util.GT5OreLayerHelper;
import gtneioreplugin.util.GT5OreLayerHelper.NormalOreDimensionWrapper;
import gtneioreplugin.util.GT5OreLayerHelper.OreLayerWrapper;
import gtneioreplugin.util.OreVeinLayer;

public class PluginGT5VeinStat extends PluginGT5OreBase {

    private static final int VEIN_LAYER_START_Y = 23;
    private static final int VEIN_LAYER_HEIGHT = 22;
    private static final int VEIN_INFO_Y_POS = 110;
    private static final int DIM_HEADER_Y_POS = 120;
    private static final DecimalFormat format = new DecimalFormat("0.##");

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(getOutputId())) {
            for (OreLayerWrapper oreVein : getAllVeins()) {
                addVeinWithLayers(oreVein);
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack stack) {
        IOreMaterial mat = OreManager.getMaterial(stack);

        if (mat != null) {
            loadMatchingVeins(mat);
            return;
        }

        boolean isMatItem = false;

        for (ParsedOreDictName oredict : OrePrefixes.detectPrefix(stack)) {
            if (!PREFIX_WHITELIST.contains(oredict.prefix)) continue;

            mat = IOreMaterial.findMaterial(oredict.material);

            if (mat != null) {
                isMatItem |= loadMatchingVeins(mat);
                if (mat instanceof Werkstoff werkstoff) {
                    isMatItem |= loadMatchingVeins(werkstoff.getBridgeMaterial());
                }
            }
        }

        if (isMatItem) return;

        super.loadCraftingRecipes(stack);
    }

    private boolean loadMatchingVeins(IOreMaterial ore) {
        boolean foundAny = false;

        for (OreLayerWrapper oreVein : getAllVeins()) {
            if (oreVein.containsOre(ore)) {
                addVeinWithLayers(oreVein);
                foundAny = true;
            }
        }

        return foundAny;
    }

    @Override
    public void loadUsageRecipes(ItemStack stack) {
        String dimension = ItemDimensionDisplay.getDimension(stack);
        if (dimension == null) {
            return;
        }

        for (OreLayerWrapper oreVein : getAllVeins()) {
            if (Arrays.asList(getDimNameArrayFromVeinName(oreVein.veinName))
                .contains(dimension)) {
                addVeinWithLayers(oreVein);
            }
        }
    }

    private void addVeinWithLayers(OreLayerWrapper oreVein) {
        String[] dimAbbr = getDimNameArrayFromVeinName(oreVein.veinName);

        Set<StoneType> stoneTypes = getStoneTypesForDimensions(dimAbbr);

        this.arecipes.add(
            new CachedVeinStatRecipe(
                oreVein,
                dimAbbr,
                oreVein.getVeinLayerOre(OreVeinLayer.VEIN_PRIMARY, stoneTypes),
                oreVein.getVeinLayerOre(OreVeinLayer.VEIN_SECONDARY, stoneTypes),
                oreVein.getVeinLayerOre(OreVeinLayer.VEIN_BETWEEN, stoneTypes),
                oreVein.getVeinLayerOre(OreVeinLayer.VEIN_SPORADIC, stoneTypes)));
    }

    private Collection<OreLayerWrapper> getAllVeins() {
        return GT5OreLayerHelper.getOreVeinsByName()
            .values();
    }

    @Override
    public void drawExtras(int recipe) {
        CachedVeinStatRecipe crecipe = (CachedVeinStatRecipe) this.arecipes.get(recipe);
        crecipe.drawExtra();
    }

    @Override
    public String getOutputId() {
        return "GTOrePluginVein";
    }

    @Override
    public String getRecipeName() {
        return I18n.format("gtnop.gui.veinStat.name");
    }

    private String[] getDimNameArrayFromVeinName(String veinName) {
        OreLayerWrapper oreLayer = GT5OreLayerHelper.getVeinByName(veinName);
        String[] dims = oreLayer.abbrDimNames.toArray(new String[0]);
        sortDimNamesByTier(dims);
        return dims;
    }

    @Override
    public List<String> handleItemTooltip(GuiRecipe<?> gui, ItemStack stack, List<String> currenttip, int recipeIndex) {
        if (arecipes.get(recipeIndex) instanceof CachedVeinStatRecipe recipe) {
            recipe.addItemTooltip(stack, currenttip);
            return currenttip;
        }
        return super.handleItemTooltip(gui, stack, currenttip, recipeIndex);
    }

    @Override
    public int getRecipeHeight(int recipe) {
        CachedVeinStatRecipe crecipe = (CachedVeinStatRecipe) this.arecipes.get(recipe);
        return crecipe.totalHeight;
    }

    public class CachedVeinStatRecipe extends CachedRecipe {

        private final OreLayerWrapper oreVein;
        public final PositionedStack positionedStackPrimary;
        public final PositionedStack positionedStackSecondary;
        public final PositionedStack positionedStackBetween;
        public final PositionedStack positionedStackSporadic;
        private final List<PositionedStack> dimensionDisplayItems = new ArrayList<>();

        private final List<String> title;

        public final int totalHeight;

        public CachedVeinStatRecipe(OreLayerWrapper oreVein, String[] dimAbbr, List<ItemStack> stackListPrimary,
            List<ItemStack> stackListSecondary, List<ItemStack> stackListBetween, List<ItemStack> stackListSporadic) {
            this.oreVein = oreVein;

            String titleText = oreVein.getLocalizedName() + " " + I18n.format("gtnop.gui.nei.vein");
            title = getTitleLines(titleText);

            int stackPosY = VEIN_LAYER_START_Y;
            if (title.size() == 1) {
                stackPosY -= 5;
            }
            positionedStackPrimary = new PositionedStack(stackListPrimary, LEFT_PADDING, stackPosY);
            positionedStackSecondary = new PositionedStack(
                stackListSecondary,
                LEFT_PADDING,
                stackPosY + VEIN_LAYER_HEIGHT);
            positionedStackBetween = new PositionedStack(
                stackListBetween,
                LEFT_PADDING,
                stackPosY + VEIN_LAYER_HEIGHT * 2);
            positionedStackSporadic = new PositionedStack(
                stackListSporadic,
                LEFT_PADDING,
                stackPosY + VEIN_LAYER_HEIGHT * 3);

            createDimensionDisplayItems(dimAbbr, DIM_HEADER_Y_POS, dimensionDisplayItems);

            totalHeight = DIM_HEADER_Y_POS + 10 + MathHelper.ceiling_float_int(dimAbbr.length / 9f) * 18 + 3;
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
            updateRenderPermutation(positionedStackPrimary);
            updateRenderPermutation(positionedStackSecondary);
            updateRenderPermutation(positionedStackBetween);
            updateRenderPermutation(positionedStackSporadic);
            outputs.add(positionedStackPrimary);
            outputs.add(positionedStackSecondary);
            outputs.add(positionedStackBetween);
            outputs.add(positionedStackSporadic);
            return outputs;
        }

        public void addItemTooltip(ItemStack stack, List<String> currentTip) {
            for (PositionedStack item : dimensionDisplayItems) {
                if (StackInfo.equalItemAndNBT(stack, item.item, false)) {
                    String dimAbbr = ItemDimensionDisplay.getDimension(stack);
                    if (dimAbbr != null) {
                        NormalOreDimensionWrapper wrapper = GT5OreLayerHelper.getVeinByDim(dimAbbr);
                        if (wrapper != null && wrapper.oreVeinToProbabilityInDimension.containsKey(oreVein)) {
                            String percent = format.format(wrapper.oreVeinToProbabilityInDimension.get(oreVein) * 100);
                            currentTip.add(EnumChatFormatting.AQUA + I18n.format("gtnop.gui.nei.genChance", percent));
                        }
                    }
                }
            }
        }

        public void drawExtra() {
            drawTitle(title);
            drawVeinLayerNames();
            drawVeinInfo();
            drawDimHeader(DIM_HEADER_Y_POS);
        }

        private void drawVeinLayerNames() {
            drawVeinLayerNameLine(OreVeinLayer.VEIN_PRIMARY, 0);
            drawVeinLayerNameLine(OreVeinLayer.VEIN_SECONDARY, 1);
            drawVeinLayerNameLine(OreVeinLayer.VEIN_BETWEEN, 2);
            drawVeinLayerNameLine(OreVeinLayer.VEIN_SPORADIC, 3);
        }

        private void drawVeinLayerNameLine(int veinLayer, int index) {
            int height = VEIN_LAYER_START_Y + VEIN_LAYER_HEIGHT * index;
            if (title.size() == 1) {
                height -= 5;
            }
            drawHeader(OreVeinLayer.getOreVeinLayerName(veinLayer), LEFT_PADDING + 18, height);
            String oreName = I18n
                .format("gtnop.gui.nei.lineValue", getGTOreLocalizedName(oreVein.ores[veinLayer], false));
            drawLine(oreName, LEFT_PADDING + 18, height + 10, LINE_VALUE_COLOR);
        }

        private void drawVeinInfo() {
            drawKeyValueLine("gtnop.gui.nei.genHeight", oreVein.worldGenHeightRange, LEFT_PADDING, VEIN_INFO_Y_POS);
            drawKeyValueLine(
                "gtnop.gui.nei.weightedChance",
                Short.toString(oreVein.randomWeight),
                100,
                VEIN_INFO_Y_POS);
        }
    }
}
