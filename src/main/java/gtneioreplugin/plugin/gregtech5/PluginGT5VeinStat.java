package gtneioreplugin.plugin.gregtech5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

import com.google.common.collect.ImmutableList;

import codechicken.nei.PositionedStack;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.OrePrefixes.ParsedOreDictName;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.common.ores.OreManager;
import gtneioreplugin.plugin.item.ItemDimensionDisplay;
import gtneioreplugin.util.DimensionHelper;
import gtneioreplugin.util.GT5OreLayerHelper;
import gtneioreplugin.util.GT5OreLayerHelper.OreLayerWrapper;
import gtneioreplugin.util.OreVeinLayer;

public class PluginGT5VeinStat extends PluginGT5Base {

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
        this.arecipes.add(
            new CachedVeinStatRecipe(
                oreVein.veinName,
                oreVein.getVeinLayerOre(OreVeinLayer.VEIN_PRIMARY),
                oreVein.getVeinLayerOre(OreVeinLayer.VEIN_SECONDARY),
                oreVein.getVeinLayerOre(OreVeinLayer.VEIN_BETWEEN),
                oreVein.getVeinLayerOre(OreVeinLayer.VEIN_SPORADIC)));
    }

    private Collection<OreLayerWrapper> getAllVeins() {
        return GT5OreLayerHelper.getOreVeinsByName()
            .values();
    }

    @Override
    public void drawExtras(int recipe) {
        OreLayerWrapper oreLayer = getOreLayer(recipe);

        drawVeinName(oreLayer);
        drawVeinLayerNames(oreLayer);
        drawVeinInfo(oreLayer);

        drawDimNames();

        drawSeeAllRecipesLabel();
    }

    private OreLayerWrapper getOreLayer(int recipe) {
        CachedVeinStatRecipe crecipe = (CachedVeinStatRecipe) this.arecipes.get(recipe);
        return GT5OreLayerHelper.getVeinByName(crecipe.veinName);
    }

    private void drawVeinName(OreLayerWrapper oreLayer) {
        drawVeinNameLine(oreLayer.getLocalizedName() + " ");
    }

    private void drawVeinNameLine(String veinName) {
        drawLine("gtnop.gui.nei.veinName", veinName + I18n.format("gtnop.gui" + ".nei.vein"), 2, 20);
    }

    private void drawVeinLayerNames(OreLayerWrapper oreLayer) {
        drawVeinLayerNameLine(oreLayer, OreVeinLayer.VEIN_PRIMARY, 50);
        drawVeinLayerNameLine(oreLayer, OreVeinLayer.VEIN_SECONDARY, 60);
        drawVeinLayerNameLine(oreLayer, OreVeinLayer.VEIN_BETWEEN, 70);
        drawVeinLayerNameLine(oreLayer, OreVeinLayer.VEIN_SPORADIC, 80);
    }

    private void drawVeinLayerNameLine(OreLayerWrapper oreLayer, int veinLayer, int height) {
        drawLine(
            OreVeinLayer.getOreVeinLayerName(veinLayer),
            getGTOreLocalizedName(oreLayer.ores[veinLayer], false),
            2,
            height);
    }

    private void drawVeinInfo(OreLayerWrapper oreLayer) {
        drawLine("gtnop.gui.nei.genHeight", oreLayer.worldGenHeightRange, 2, 90);
        drawLine("gtnop.gui.nei.weightedChance", Integer.toString(oreLayer.randomWeight), 100, 90);
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

    public class CachedVeinStatRecipe extends CachedRecipe {

        public final String veinName;
        public final PositionedStack positionedStackPrimary;
        public final PositionedStack positionedStackSecondary;
        public final PositionedStack positionedStackBetween;
        public final PositionedStack positionedStackSporadic;
        private final List<PositionedStack> dimensionDisplayItems = new ArrayList<>();

        public CachedVeinStatRecipe(String veinName, List<ItemStack> stackListPrimary,
            List<ItemStack> stackListSecondary, List<ItemStack> stackListBetween, List<ItemStack> stackListSporadic) {
            this.veinName = veinName;
            positionedStackPrimary = new PositionedStack(stackListPrimary, 2, 0);
            positionedStackSecondary = new PositionedStack(stackListSecondary, 22, 0);
            positionedStackBetween = new PositionedStack(stackListBetween, 42, 0);
            positionedStackSporadic = new PositionedStack(stackListSporadic, 62, 0);
            setDimensionDisplayItems();
        }

        private void setDimensionDisplayItems() {
            int x = 2;
            int y = 110;
            int count = 0;
            int itemsPerLine = 9;
            int itemSize = 18;
            for (String dim : getDimNameArrayFromVeinName(this.veinName)) {
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
    }
}
