package gtneioreplugin.plugin.gregtech5;

import java.awt.Point;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import cpw.mods.fml.relauncher.ReflectionHelper;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.OrePrefixes.ParsedOreDictName;
import gregtech.api.enums.StoneType;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.common.ores.IOreAdapter;
import gregtech.common.ores.OreInfo;
import gregtech.common.ores.OreManager;
import gtneioreplugin.plugin.item.ItemDimensionDisplay;
import gtneioreplugin.util.DimensionHelper;
import gtneioreplugin.util.GT5OreSmallHelper;
import gtneioreplugin.util.GT5OreSmallHelper.OreSmallWrapper;
import gtneioreplugin.util.GT5OreSmallHelper.SmallOreDimensionWrapper;

public class PluginGT5SmallOreStat extends PluginGT5Base {

    private static Field guiLeftField;
    private static Field guiTopField;

    private static final int NAME_Y = 4;
    private static final int NAME_MAX_WIDTH = 132;
    private static final int CONTENT_ROW_START_Y = NAME_Y + 8 + 3; // font height + 3px gap
    private static final int INFO_X = 6;
    private static final int ROW_STEP = 12;
    private static final int ORE_ICON_X = 136;
    private static final int HEIGHT_ROW_Y = NAME_Y + 8 + 6; // main name + 6px visible gap
    private static final int AMOUNT_ROW_Y = HEIGHT_ROW_Y + ROW_STEP;
    private static final int ORE_ICON_Y = (HEIGHT_ROW_Y + AMOUNT_ROW_Y) / 2 - 8; // center icon Height/Amount
    private static final int DROPS_LABEL_Y = AMOUNT_ROW_Y + ROW_STEP;
    private static final int DROPS_GRID_X = 4;
    private static final int DROPS_GRID_Y = DROPS_LABEL_Y + 10;
    private static final int DROPS_ITEMS_PER_LINE = 9;
    private static final int DROPS_ITEM_STEP = 18;
    private static final int DIM_NAMES_ROW_Y = 92;
    private static final int DIM_ITEMS_START_Y = 102;

    @Override
    public void drawExtras(int recipe) {
        OreSmallWrapper oreSmall = getSmallOre(recipe);

        drawSmallOreName(oreSmall);
        drawSmallOreInfo(oreSmall);

        drawDimNames();
    }

    private void drawSmallOreName(OreSmallWrapper oreSmall) {
        String oreText = getSmallOreNameText(oreSmall);
        String oreTextToDraw = getTrimmedWithEllipsis(oreText, NAME_MAX_WIDTH);
        GuiDraw.drawStringC(
            EnumChatFormatting.BOLD + oreTextToDraw + EnumChatFormatting.RESET,
            getGuiWidth() / 2,
            NAME_Y,
            0x404040,
            false);
    }

    @Override
    public List<String> handleTooltip(GuiRecipe<?> gui, List<String> currentTip, int recipe) {
        OreSmallWrapper oreSmall = getSmallOre(recipe);
        String fullTitle = getSmallOreNameText(oreSmall);
        if (!isTruncated(fullTitle, NAME_MAX_WIDTH)) return currentTip;

        Point recipePos = gui.getRecipePosition(recipe);
        Point mousePosAbs = GuiDraw.getMousePosition();
        Point mousePos = new Point(
            mousePosAbs.x - getGuiLeft(gui) - recipePos.x,
            mousePosAbs.y - getGuiTop(gui) - recipePos.y);

        int textWidth = GuiDraw.fontRenderer.getStringWidth(getTrimmedWithEllipsis(fullTitle, NAME_MAX_WIDTH));
        int left = getGuiWidth() / 2 - textWidth / 2;
        int right = left + textWidth;
        int top = NAME_Y;
        int bottom = NAME_Y + GuiDraw.fontRenderer.FONT_HEIGHT;
        if (mousePos.x >= left && mousePos.x <= right && mousePos.y >= top && mousePos.y <= bottom) {
            currentTip.add(fullTitle);
        }

        return currentTip;
    }

    private void drawSmallOreInfo(OreSmallWrapper oreSmall) {
        drawLine("gtnop.gui.nei.genHeight", oreSmall.worldGenHeightRange, INFO_X, HEIGHT_ROW_Y);
        drawLine("gtnop.gui.nei.amount", String.valueOf(oreSmall.amountPerChunk), INFO_X, AMOUNT_ROW_Y);
        drawLine("gtnop.gui.nei.chanceDrops", "", INFO_X, DROPS_LABEL_Y);
    }

    @Override
    protected void drawDimNames() {
        drawLine("gtnop.gui.nei.worldNames", "", INFO_X, DIM_NAMES_ROW_Y);
    }

    @Override
    public int getGuiHeight() {
        return 166;
    }

    private String getSmallOreNameText(OreSmallWrapper oreSmall) {
        String oreName = getGTOreLocalizedName(oreSmall.material, true);
        return I18n.format("gtnop.gui.nei.veinName", oreName);
    }

    private boolean isTruncated(String text, int maxWidth) {
        return GuiDraw.fontRenderer.getStringWidth(text) > maxWidth;
    }

    private String getTrimmedWithEllipsis(String text, int maxWidth) {
        int clampedMaxWidth = Math.max(0, maxWidth);
        if (GuiDraw.fontRenderer.getStringWidth(text) <= clampedMaxWidth) return text;

        int ellipsisWidth = GuiDraw.fontRenderer.getStringWidth("...");
        if (clampedMaxWidth <= ellipsisWidth) {
            return GuiDraw.fontRenderer.trimStringToWidth(text, clampedMaxWidth);
        }
        String trimmed = GuiDraw.fontRenderer.trimStringToWidth(text, clampedMaxWidth - ellipsisWidth);
        return trimmed + "...";
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
        public final List<PositionedStack> positionedDropStackList;
        private final List<PositionedStack> dimensionDisplayItems = new ArrayList<>();

        public CachedOreSmallRecipe(String oreGenName, List<ItemStack> stackList, List<ItemStack> materialDustStackList,
            List<ItemStack> dropStackList) {
            this.oreGenName = oreGenName;
            this.positionedStackOreSmall = new PositionedStack(stackList, ORE_ICON_X, ORE_ICON_Y);
            List<PositionedStack> positionedDropStackList = new ArrayList<>();
            int i = 0;
            if (materialDustStackList != null) {
                for (ItemStack stackDrop : materialDustStackList) {
                    positionedDropStackList.add(
                        new PositionedStack(
                            stackDrop,
                            DROPS_GRID_X + DROPS_ITEM_STEP * (i % DROPS_ITEMS_PER_LINE),
                            DROPS_GRID_Y + DROPS_ITEM_STEP * (i / DROPS_ITEMS_PER_LINE)));
                    i++;
                }
            }
            if (dropStackList != null) {
                for (ItemStack stackDrop : dropStackList) {
                    positionedDropStackList.add(
                        new PositionedStack(
                            stackDrop,
                            DROPS_GRID_X + DROPS_ITEM_STEP * (i % DROPS_ITEMS_PER_LINE),
                            DROPS_GRID_Y + DROPS_ITEM_STEP * (i / DROPS_ITEMS_PER_LINE)));
                    i++;
                }
            }
            this.positionedDropStackList = positionedDropStackList;
            setDimensionDisplayItems();
        }

        private void setDimensionDisplayItems() {
            int x = 4;
            int y = DIM_ITEMS_START_Y;
            int count = 0;
            int itemsPerLine = 9;
            int itemSize = 18;

            OreSmallWrapper wrapper = GT5OreSmallHelper.SMALL_ORES_BY_NAME.get(this.oreGenName);
            List<String> sortedDims = new ArrayList<>(wrapper.enabledDims);
            sortedDims
                .sort((a, b) -> Integer.compare(DimensionHelper.getIndexByAbbr(a), DimensionHelper.getIndexByAbbr(b)));

            for (String abbrDimName : sortedDims) {
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
            outputs.add(positionedStackOreSmall);
            outputs.addAll(positionedDropStackList);
            return outputs;
        }
    }
}
