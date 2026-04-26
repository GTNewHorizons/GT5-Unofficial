package gtneioreplugin.plugin.gregtech5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.google.common.collect.ImmutableList;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.StackInfo;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.OrePrefixes.ParsedOreDictName;
import gregtech.api.enums.StoneType;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.common.ores.OreManager;
import gtneioreplugin.plugin.item.ItemDimensionDisplay;
import gtneioreplugin.util.DimensionHelper;
import gtneioreplugin.util.DimensionHelper.Dimension;
import gtneioreplugin.util.GT5OreLayerHelper;
import gtneioreplugin.util.GT5OreLayerHelper.NormalOreDimensionWrapper;
import gtneioreplugin.util.GT5OreLayerHelper.OreLayerWrapper;
import gtneioreplugin.util.OreVeinLayer;

public class PluginGT5VeinStat extends PluginGT5Base {

    private static final int VEIN_LAYER_START_Y = 23;
    private static final int VEIN_LAYER_HEIGHT = 22;
    private static final int VEIN_INFO_Y_POS = 110;
    private static final int DIM_HEADER_Y_POS = 120;

    // spotless:off
    public static final List<OrePrefixes> PREFIX_WHITELIST = ImmutableList.of(
        OrePrefixes.dust,
        OrePrefixes.dustPure,
        OrePrefixes.dustImpure,
        OrePrefixes.oreBlackgranite,
        OrePrefixes.oreRedgranite,
        OrePrefixes.oreMarble,
        OrePrefixes.oreBasalt,
        OrePrefixes.oreNetherrack,
        OrePrefixes.oreNether,
        OrePrefixes.oreEndstone,
        OrePrefixes.oreEnd,
        OrePrefixes.ore,
        OrePrefixes.crushedCentrifuged,
        OrePrefixes.crushedPurified,
        OrePrefixes.crushed,
        OrePrefixes.rawOre,
        OrePrefixes.gemChipped,
        OrePrefixes.gemFlawed,
        OrePrefixes.gemFlawless,
        OrePrefixes.gemExquisite,
        OrePrefixes.gem
    );
    // spotless:on

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

        Set<StoneType> stoneTypes = new HashSet<>();

        for (String abbr : dimAbbr) {
            Dimension dimension = DimensionHelper.getByIndex(DimensionHelper.getIndexByAbbr(abbr));
            if (dimension != null) {
                stoneTypes.addAll(dimension.stoneTypes());
            }
        }

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

        Arrays.sort(dims, (a, b) -> {
            int indexA = DimensionHelper.getIndexByAbbr(a);
            int indexB = DimensionHelper.getIndexByAbbr(b);
            return Integer.compare(indexA, indexB);
        });
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

    public class CachedVeinStatRecipe extends CachedRecipe {

        private final OreLayerWrapper oreVein;
        public final PositionedStack positionedStackPrimary;
        public final PositionedStack positionedStackSecondary;
        public final PositionedStack positionedStackBetween;
        public final PositionedStack positionedStackSporadic;
        private final List<PositionedStack> dimensionDisplayItems = new ArrayList<>();

        public CachedVeinStatRecipe(OreLayerWrapper oreVein, String[] dimAbbr, List<ItemStack> stackListPrimary,
            List<ItemStack> stackListSecondary, List<ItemStack> stackListBetween, List<ItemStack> stackListSporadic) {
            this.oreVein = oreVein;
            positionedStackPrimary = new PositionedStack(stackListPrimary, 0, VEIN_LAYER_START_Y);
            positionedStackSecondary = new PositionedStack(stackListSecondary, 0, 0);
            positionedStackBetween = new PositionedStack(stackListBetween, 0, 0);
            positionedStackSporadic = new PositionedStack(stackListSporadic, 0, 0);
            updateOreYPos(0);
            setDimensionDisplayItems(dimAbbr);
        }

        private void setDimensionDisplayItems(String[] dimAbbr) {
            int count = 0;
            int itemsPerLine = 9;
            int itemSize = 18;
            for (String dim : dimAbbr) {
                ItemStack item = ItemDimensionDisplay.getItem(dim);
                if (item != null) {
                    int xPos = LEFT_PADDING + itemSize * (count % itemsPerLine);
                    int yPos = DIM_HEADER_Y_POS + 10 + itemSize * (count / itemsPerLine);
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
            positionedStackPrimary.setPermutationToRender((cycleticks / 20) % positionedStackPrimary.items.length);
            positionedStackSecondary.setPermutationToRender((cycleticks / 20) % positionedStackSecondary.items.length);
            positionedStackBetween.setPermutationToRender((cycleticks / 20) % positionedStackBetween.items.length);
            positionedStackSporadic.setPermutationToRender((cycleticks / 20) % positionedStackSporadic.items.length);
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
                            String percent = String
                                .format("%.2f", wrapper.oreVeinToProbabilityInDimension.get(oreVein) * 100);
                            currentTip
                                .add(String.format(EnumChatFormatting.AQUA + "Chance to generate: %s%%", percent));
                        }
                    }
                }
            }
        }

        private void updateOreYPos(int yOffset) {
            positionedStackPrimary.rely = VEIN_LAYER_START_Y + yOffset;
            positionedStackSecondary.rely = VEIN_LAYER_START_Y + VEIN_LAYER_HEIGHT + yOffset;
            positionedStackBetween.rely = VEIN_LAYER_START_Y + VEIN_LAYER_HEIGHT * 2 + yOffset;
            positionedStackSporadic.rely = VEIN_LAYER_START_Y + VEIN_LAYER_HEIGHT * 3 + yOffset;
        }

        public void drawExtra() {
            int numLines = drawVeinName(oreVein);

            drawVeinLayerNames(oreVein, numLines);

            drawVeinInfo(oreVein);
            drawDimHeader(DIM_HEADER_Y_POS);

            drawSeeAllRecipesLabel();
        }

        private int drawVeinName(OreLayerWrapper oreLayer) {
            String text = oreLayer.getLocalizedName() + " " + I18n.format("gtnop.gui.nei.vein");
            return drawTitle(text);
        }

        private void drawVeinLayerNames(OreLayerWrapper oreLayer, int numLines) {
            int yOffset = numLines == 1 ? -5 : 0;
            drawVeinLayerNameLine(oreLayer, OreVeinLayer.VEIN_PRIMARY, 0, yOffset);
            drawVeinLayerNameLine(oreLayer, OreVeinLayer.VEIN_SECONDARY, 1, yOffset);
            drawVeinLayerNameLine(oreLayer, OreVeinLayer.VEIN_BETWEEN, 2, yOffset);
            drawVeinLayerNameLine(oreLayer, OreVeinLayer.VEIN_SPORADIC, 3, yOffset);
            updateOreYPos(yOffset);
        }

        private void drawVeinLayerNameLine(OreLayerWrapper oreLayer, int veinLayer, int index, int yOffset) {
            int height = yOffset + VEIN_LAYER_START_Y + VEIN_LAYER_HEIGHT * index;
            drawHeader(OreVeinLayer.getOreVeinLayerName(veinLayer), LEFT_PADDING + 16, height);
            drawLine(getGTOreLocalizedName(oreLayer.ores[veinLayer], false), LEFT_PADDING + 16, height + 10);
        }

        private void drawVeinInfo(OreLayerWrapper oreLayer) {
            drawLine("gtnop.gui.nei.genHeight", oreLayer.worldGenHeightRange, LEFT_PADDING, VEIN_INFO_Y_POS);
            drawLine("gtnop.gui.nei.weightedChance", Integer.toString(oreLayer.randomWeight), 100, VEIN_INFO_Y_POS);
        }
    }
}
