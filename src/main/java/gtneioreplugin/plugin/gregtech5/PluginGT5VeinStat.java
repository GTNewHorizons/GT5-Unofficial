package gtneioreplugin.plugin.gregtech5;

import java.awt.Point;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.google.common.collect.ImmutableList;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import cpw.mods.fml.relauncher.ReflectionHelper;
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

    private static Field guiLeftField;
    private static Field guiTopField;

    private static final int LAYER_ICON_X = 4;
    private static final int LAYER_TEXT_X = 24;
    private static final int LAYER_TEXT_MAX_WIDTH = 138;
    private static final int VEIN_NAME_Y = 4;
    private static final int LAYER_ROW_START_Y = VEIN_NAME_Y + 8 + 3; // font height + 3px gap
    private static final int LAYER_ROW_STEP = 18; // 16px icon + 2px gap
    private static final int LAYER_TEXT_CENTER_OFFSET_Y = 4;
    private static final int VEIN_INFO_X = 6;
    private static final int VEIN_INFO_ROW_Y = LAYER_ROW_START_Y + LAYER_ROW_STEP * 3
        + LAYER_TEXT_CENTER_OFFSET_Y
        + 8
        + 6; // last layer text + font height + 6px gap
    private static final int VEIN_INFO_SECOND_ROW_Y = VEIN_INFO_ROW_Y + 12;
    private static final int DIM_NAMES_ROW_Y = VEIN_INFO_SECOND_ROW_Y + 12;
    private static final int DIM_ITEMS_START_Y = DIM_NAMES_ROW_Y + 10;

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
    }

    @Override
    public List<String> handleTooltip(GuiRecipe<?> gui, List<String> currentTip, int recipe) {
        OreLayerWrapper oreLayer = getOreLayer(recipe);
        Point recipePos = gui.getRecipePosition(recipe);
        Point mousePosAbs = GuiDraw.getMousePosition();
        Point mousePos = new Point(
            mousePosAbs.x - getGuiLeft(gui) - recipePos.x,
            mousePosAbs.y - getGuiTop(gui) - recipePos.y);
        int fontHeight = GuiDraw.fontRenderer.FONT_HEIGHT;

        int[] layers = { OreVeinLayer.VEIN_PRIMARY, OreVeinLayer.VEIN_SECONDARY, OreVeinLayer.VEIN_BETWEEN,
            OreVeinLayer.VEIN_SPORADIC };
        for (int i = 0; i < layers.length; i++) {
            String fullText = getVeinLayerFullText(oreLayer, layers[i]);
            if (!isLineTruncated(fullText, LAYER_TEXT_MAX_WIDTH)) continue;

            int y = LAYER_ROW_START_Y + LAYER_ROW_STEP * i + LAYER_TEXT_CENTER_OFFSET_Y;
            if (mousePos.x >= LAYER_TEXT_X && mousePos.x <= LAYER_TEXT_X + LAYER_TEXT_MAX_WIDTH
                && mousePos.y >= y
                && mousePos.y <= y + fontHeight) {
                currentTip.add(fullText);
                break;
            }
        }

        return currentTip;
    }

    private OreLayerWrapper getOreLayer(int recipe) {
        CachedVeinStatRecipe crecipe = (CachedVeinStatRecipe) this.arecipes.get(recipe);
        return GT5OreLayerHelper.getVeinByName(crecipe.veinName);
    }

    private void drawVeinName(OreLayerWrapper oreLayer) {
        drawVeinNameLine(oreLayer.localizedName + " ");
    }

    private void drawVeinNameLine(String veinName) {
        String veinText = I18n.format("gtnop.gui.nei.veinName", veinName + I18n.format("gtnop.gui.nei.vein"));
        veinText = EnumChatFormatting.BOLD + veinText + EnumChatFormatting.RESET;
        GuiDraw.drawStringC(veinText, getGuiWidth() / 2, VEIN_NAME_Y, 0x404040, false);
    }

    private void drawVeinLayerNames(OreLayerWrapper oreLayer) {
        drawVeinLayerNameLine(oreLayer, OreVeinLayer.VEIN_PRIMARY, LAYER_ROW_START_Y + LAYER_TEXT_CENTER_OFFSET_Y);
        drawVeinLayerNameLine(
            oreLayer,
            OreVeinLayer.VEIN_SECONDARY,
            LAYER_ROW_START_Y + LAYER_ROW_STEP + LAYER_TEXT_CENTER_OFFSET_Y);
        drawVeinLayerNameLine(
            oreLayer,
            OreVeinLayer.VEIN_BETWEEN,
            LAYER_ROW_START_Y + LAYER_ROW_STEP * 2 + LAYER_TEXT_CENTER_OFFSET_Y);
        drawVeinLayerNameLine(
            oreLayer,
            OreVeinLayer.VEIN_SPORADIC,
            LAYER_ROW_START_Y + LAYER_ROW_STEP * 3 + LAYER_TEXT_CENTER_OFFSET_Y);
    }

    private void drawVeinLayerNameLine(OreLayerWrapper oreLayer, int veinLayer, int height) {
        drawLine(
            OreVeinLayer.getOreVeinLayerName(veinLayer),
            getGTOreLocalizedName(oreLayer.ores[veinLayer], false),
            LAYER_TEXT_X,
            height,
            LAYER_TEXT_MAX_WIDTH);
    }

    private String getVeinLayerFullText(OreLayerWrapper oreLayer, int veinLayer) {
        return I18n.format(OreVeinLayer.getOreVeinLayerName(veinLayer)) + ": "
            + getGTOreLocalizedName(oreLayer.ores[veinLayer], false);
    }

    private boolean isLineTruncated(String text, int maxWidth) {
        return GuiDraw.fontRenderer.getStringWidth(text) > maxWidth;
    }

    private int getGuiLeft(GuiRecipe<?> gui) {
        Field field = getGuiLeftField();
        if (field == null) return 0;
        try {
            return field.getInt(gui);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to read guiLeft", e);
        }
    }

    private int getGuiTop(GuiRecipe<?> gui) {
        Field field = getGuiTopField();
        if (field == null) return 0;
        try {
            return field.getInt(gui);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to read guiTop", e);
        }
    }

    private static Field getGuiLeftField() {
        if (guiLeftField == null) {
            try {
                guiLeftField = ReflectionHelper.findField(GuiContainer.class, "guiLeft", "field_147003_i");
            } catch (Throwable ignored) {
                return null;
            }
        }
        return guiLeftField;
    }

    private static Field getGuiTopField() {
        if (guiTopField == null) {
            try {
                guiTopField = ReflectionHelper.findField(GuiContainer.class, "guiTop", "field_147009_r");
            } catch (Throwable ignored) {
                return null;
            }
        }
        return guiTopField;
    }

    private void drawVeinInfo(OreLayerWrapper oreLayer) {
        drawLine("gtnop.gui.nei.genHeight", oreLayer.worldGenHeightRange, VEIN_INFO_X, VEIN_INFO_ROW_Y);
        drawLine(
            "gtnop.gui.nei.weightedChance",
            Integer.toString(oreLayer.randomWeight),
            VEIN_INFO_X,
            VEIN_INFO_SECOND_ROW_Y);
    }

    @Override
    protected void drawDimNames() {
        drawLine("gtnop.gui.nei.worldNames", "", 6, DIM_NAMES_ROW_Y);
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
            positionedStackPrimary = new PositionedStack(stackListPrimary, LAYER_ICON_X, LAYER_ROW_START_Y);
            positionedStackSecondary = new PositionedStack(
                stackListSecondary,
                LAYER_ICON_X,
                LAYER_ROW_START_Y + LAYER_ROW_STEP);
            positionedStackBetween = new PositionedStack(
                stackListBetween,
                LAYER_ICON_X,
                LAYER_ROW_START_Y + LAYER_ROW_STEP * 2);
            positionedStackSporadic = new PositionedStack(
                stackListSporadic,
                LAYER_ICON_X,
                LAYER_ROW_START_Y + LAYER_ROW_STEP * 3);
            setDimensionDisplayItems();
        }

        private void setDimensionDisplayItems() {
            int x = 4;
            int y = DIM_ITEMS_START_Y;
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
