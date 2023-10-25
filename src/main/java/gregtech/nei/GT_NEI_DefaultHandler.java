package gregtech.nei;

import static gregtech.api.enums.GT_Values.V;

import java.awt.Rectangle;
import java.lang.ref.SoftReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.Range;
import org.lwjgl.opengl.GL11;

import com.gtnewhorizons.modularui.api.GlStateManager;
import com.gtnewhorizons.modularui.api.UIInfos;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import codechicken.nei.NEIClientUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.ICraftingHandler;
import codechicken.nei.recipe.IUsageHandler;
import codechicken.nei.recipe.RecipeCatalysts;
import codechicken.nei.recipe.TemplateRecipeHandler;
import gregtech.GT_Mod;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SteamVariant;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IOverclockDescriptionProvider;
import gregtech.api.objects.ItemData;
import gregtech.api.objects.overclockdescriber.EUNoOverclockDescriber;
import gregtech.api.objects.overclockdescriber.OverclockDescriber;
import gregtech.api.recipe.BasicUIProperties;
import gregtech.api.recipe.NEIRecipeProperties;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Item_Machines;
import gregtech.common.gui.modularui.UIHelper;

public class GT_NEI_DefaultHandler extends TemplateRecipeHandler {

    private static final int offsetX = 5;
    private static final int offsetY = 11;
    protected static final Pos2d WINDOW_OFFSET = new Pos2d(-offsetX, -offsetY);

    private static final ConcurrentMap<RecipeMap<?>, SortedRecipeListCache> CACHE = new ConcurrentHashMap<>();

    private static final int RECIPE_NAME_WIDTH = 140;

    /**
     * Static version of {@link TemplateRecipeHandler#cycleticks}. Can be referenced from cached recipes.
     */
    private static int cycleTicksStatic = Math.abs((int) System.currentTimeMillis());
    /**
     * Basically {@link #cycleTicksStatic} but always updated even while holding shift
     */
    private static int drawTicks;
    private static final int PROGRESSBAR_CYCLE_TICKS = 200;

    protected final RecipeMap<?> recipeMap;
    protected final RecipeMapFrontend frontend;
    protected final BasicUIProperties uiProperties;
    protected final NEIRecipeProperties neiProperties;

    protected final ModularWindow modularWindow;
    protected final ItemStackHandler itemInputsInventory;
    protected final ItemStackHandler itemOutputsInventory;
    protected final ItemStackHandler specialSlotInventory;
    protected final ItemStackHandler fluidInputsInventory;
    protected final ItemStackHandler fluidOutputsInventory;

    protected OverclockDescriber overclockDescriber;
    /**
     * Localized name of this handler displayed on the top.
     */
    private String recipeNameDisplay;
    /**
     * Tooltip shown while hovering over header of this handler. Can be null if the full name fits in the screen.
     */
    private NEIHandlerAbsoluteTooltip recipeNameTooltip;

    public GT_NEI_DefaultHandler(RecipeMap<?> recipeMap) {
        this.recipeMap = recipeMap;
        this.frontend = recipeMap.getFrontend();
        this.uiProperties = frontend.getUIProperties();
        this.neiProperties = frontend.getNEIProperties();
        Rectangle transferRect = new Rectangle(uiProperties.neiTransferRect);
        transferRect.translate(WINDOW_OFFSET.x, WINDOW_OFFSET.y);
        this.transferRects.add(new RecipeTransferRect(transferRect, getOverlayIdentifier()));

        ModularWindow.Builder builder = frontend.createNEITemplate(
            itemInputsInventory = new ItemStackHandler(uiProperties.maxItemInputs),
            itemOutputsInventory = new ItemStackHandler(uiProperties.maxItemOutputs),
            specialSlotInventory = new ItemStackHandler(1),
            fluidInputsInventory = new ItemStackHandler(uiProperties.maxFluidInputs),
            fluidOutputsInventory = new ItemStackHandler(uiProperties.maxFluidOutputs),
            () -> ((float) getDrawTicks() % PROGRESSBAR_CYCLE_TICKS) / PROGRESSBAR_CYCLE_TICKS,
            WINDOW_OFFSET);
        modularWindow = builder.build();
        UIInfos.initializeWindow(Minecraft.getMinecraft().thePlayer, modularWindow);
    }

    public RecipeMap<?> getRecipeMap() {
        return recipeMap;
    }

    private SortedRecipeListCache getCacheHolder() {
        return CACHE.computeIfAbsent(recipeMap, m -> new SortedRecipeListCache());
    }

    public List<CachedDefaultRecipe> getCache() {
        SortedRecipeListCache cacheHolder = getCacheHolder();
        List<CachedDefaultRecipe> cache;
        if (cacheHolder.getCachedRecipesVersion() != GT_Mod.gregtechproxy.getReloadCount()
            || (cache = cacheHolder.getCachedRecipes()) == null) {
            cache = recipeMap.getAllRecipes()
                .stream() // do not use parallel stream. This is already parallelized by NEI
                .filter(r -> !r.mHidden)
                .sorted()
                .map(CachedDefaultRecipe::new)
                .collect(Collectors.toList());
            // while the NEI parallelize handlers, for each individual handler it still uses sequential execution model,
            // so we do not need any synchronization here
            // even if it does break, at worst case it's just recreating the cache multiple times, which should be fine
            cacheHolder.setCachedRecipes(cache);
            cacheHolder.setCachedRecipesVersion(GT_Mod.gregtechproxy.getReloadCount());
        }
        return cache;
    }

    @Override
    public TemplateRecipeHandler newInstance() {
        return new GT_NEI_DefaultHandler(this.recipeMap);
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(getOverlayIdentifier())) {
            if (results.length > 0 && results[0] instanceof OverclockDescriber) {
                overclockDescriber = (OverclockDescriber) results[0];
                if (neiProperties.useCustomFilter) {
                    loadTieredRecipesWithCustomFilter(overclockDescriber);
                } else {
                    loadTieredRecipesUpTo(overclockDescriber.getTier());
                }
            } else {
                arecipes.addAll(getCache());
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack aResult) {
        ItemData tPrefixMaterial = GT_OreDictUnificator.getAssociation(aResult);

        ArrayList<ItemStack> tResults = new ArrayList<>();
        tResults.add(aResult);
        tResults.add(GT_OreDictUnificator.get(true, aResult));
        if ((tPrefixMaterial != null) && (!tPrefixMaterial.mBlackListed)
            && (!tPrefixMaterial.mPrefix.mFamiliarPrefixes.isEmpty())) {
            for (OrePrefixes tPrefix : tPrefixMaterial.mPrefix.mFamiliarPrefixes) {
                tResults.add(GT_OreDictUnificator.get(tPrefix, tPrefixMaterial.mMaterial.mMaterial, 1L));
            }
        }
        if (aResult.getUnlocalizedName()
            .startsWith("gt.blockores")) {
            for (int i = 0; i < 8; i++) {
                tResults.add(new ItemStack(aResult.getItem(), 1, aResult.getItemDamage() % 1000 + i * 1000));
            }
        }
        addFluidStacks(aResult, tResults);
        for (CachedDefaultRecipe recipe : getCache()) {
            if (tResults.stream()
                .anyMatch(stack -> recipe.contains(recipe.mOutputs, stack))) arecipes.add(recipe);
        }
    }

    private void addFluidStacks(ItemStack aStack, ArrayList<ItemStack> tResults) {
        FluidStack tFluid = GT_Utility.getFluidForFilledItem(aStack, true);
        FluidStack tFluidStack;
        if (tFluid != null) {
            tFluidStack = tFluid;
            tResults.add(GT_Utility.getFluidDisplayStack(tFluid, false));
        } else tFluidStack = GT_Utility.getFluidFromDisplayStack(aStack);
        if (tFluidStack != null) {
            tResults.addAll(GT_Utility.getContainersFromFluid(tFluidStack));
        }
    }

    private void loadTieredRecipesWithCustomFilter(OverclockDescriber overclockDescriber) {
        arecipes.addAll(getTieredRecipesWithCustomFilter(overclockDescriber));
    }

    private List<CachedDefaultRecipe> getTieredRecipesWithCustomFilter(OverclockDescriber overclockDescriber) {
        List<CachedDefaultRecipe> recipes = getCache();
        if (!recipes.isEmpty()) {
            recipes = recipes.stream()
                .filter(recipe -> overclockDescriber.canHandle(recipe.mRecipe))
                .collect(Collectors.toList());
        }
        return recipes;
    }

    private void loadTieredRecipesUpTo(byte upperTier) {
        arecipes.addAll(getTieredRecipes(upperTier));
    }

    private List<CachedDefaultRecipe> getTieredRecipes(byte upperTier) {
        List<CachedDefaultRecipe> recipes = getCache();
        if (!recipes.isEmpty()) {
            Range<Integer> indexRange = getCacheHolder().getIndexRangeForTiers((byte) 0, upperTier);
            recipes = recipes.subList(indexRange.getMinimum(), indexRange.getMaximum() + 1);
        }
        return recipes;
    }

    @Override
    public void loadUsageRecipes(ItemStack aInput) {
        ItemData tPrefixMaterial = GT_OreDictUnificator.getAssociation(aInput);
        ArrayList<ItemStack> tInputs = new ArrayList<>();
        tInputs.add(aInput);
        tInputs.add(GT_OreDictUnificator.get(false, aInput));
        if ((tPrefixMaterial != null) && (!tPrefixMaterial.mPrefix.mFamiliarPrefixes.isEmpty())) {
            for (OrePrefixes tPrefix : tPrefixMaterial.mPrefix.mFamiliarPrefixes) {
                tInputs.add(GT_OreDictUnificator.get(tPrefix, tPrefixMaterial.mMaterial.mMaterial, 1L));
            }
        }
        addFluidStacks(aInput, tInputs);
        for (CachedDefaultRecipe recipe : getCache()) {
            if (tInputs.stream()
                .anyMatch(stack -> recipe.contains(recipe.mInputs, stack))) arecipes.add(recipe);
        }
    }

    @Override
    public IUsageHandler getUsageAndCatalystHandler(String inputId, Object... ingredients) {
        if (inputId.equals("item")) {
            ItemStack candidate = (ItemStack) ingredients[0];
            GT_NEI_DefaultHandler handler = (GT_NEI_DefaultHandler) newInstance();
            if (RecipeCatalysts.containsCatalyst(handler, candidate)) {
                IMetaTileEntity metaTile = GT_Item_Machines.getMetaTileEntity(candidate);
                OverclockDescriber overclockDescriber;
                if (metaTile instanceof IOverclockDescriptionProvider provider) {
                    overclockDescriber = provider.getOverclockDescriber();
                } else {
                    overclockDescriber = null;
                }
                handler.loadCraftingRecipes(getOverlayIdentifier(), overclockDescriber);
                return handler;
            }
        }
        return this.getUsageHandler(inputId, ingredients);
    }

    @Override
    public ICraftingHandler getRecipeHandler(String outputId, Object... results) {
        GT_NEI_DefaultHandler handler = (GT_NEI_DefaultHandler) super.getRecipeHandler(outputId, results);
        if (results.length > 0 && results[0] instanceof OverclockDescriber) {
            handler.overclockDescriber = (OverclockDescriber) results[0];
        }
        return handler;
    }

    @Override
    public String getOverlayIdentifier() {
        return this.recipeMap.unlocalizedName;
    }

    @Override
    public void drawBackground(int recipe) {
        drawUI(modularWindow);
    }

    @Override
    public void drawForeground(int recipe) {
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glDisable(GL11.GL_LIGHTING);
        drawExtras(recipe);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!NEIClientUtils.shiftKey()) cycleTicksStatic++;
        drawTicks++;
    }

    @Override
    public int recipiesPerPage() {
        return 1;
    }

    @Override
    public String getRecipeName() {
        if (recipeNameDisplay == null) {
            recipeNameDisplay = computeRecipeName();
            frontend.updateNEITextColorOverride();
        }
        return recipeNameDisplay;
    }

    private String computeRecipeName() {
        String recipeName = GT_LanguageManager.getTranslation(this.recipeMap.unlocalizedName);
        if (overclockDescriber != null) {
            recipeName = addSuffixToRecipeName(recipeName, overclockDescriber.getTierString() + ")");
        }
        return recipeName;
    }

    private String addSuffixToRecipeName(final String aRecipeName, final String suffix) {
        final String recipeName;
        final String separator;
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        int recipeNameWidth = fontRenderer.getStringWidth(aRecipeName);
        int targetWidth = RECIPE_NAME_WIDTH - fontRenderer.getStringWidth(suffix);
        if (recipeNameWidth + fontRenderer.getStringWidth(" (") <= targetWidth) {
            recipeName = aRecipeName;
            separator = " (";
        } else {
            setupRecipeNameTooltip(aRecipeName + " (" + suffix);
            separator = "...(";
            recipeName = shrinkRecipeName(aRecipeName, targetWidth - fontRenderer.getStringWidth(separator));
        }
        return recipeName + separator + suffix;
    }

    private String shrinkRecipeName(String recipeName, int targetWidth) {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        do {
            recipeName = recipeName.substring(0, recipeName.length() - 2);
        } while (fontRenderer.getStringWidth(recipeName) > targetWidth);
        return recipeName;
    }

    private void setupRecipeNameTooltip(String tooltip) {
        recipeNameTooltip = new NEIHandlerAbsoluteTooltip(tooltip, new Rectangle(13, -34, RECIPE_NAME_WIDTH - 1, 11));
    }

    @Override
    public String getRecipeTabName() {
        return GT_LanguageManager.getTranslation(this.recipeMap.unlocalizedName);
    }

    @Override
    public String getGuiTexture() {
        // not called
        return "";
    }

    @Override
    public List<String> handleItemTooltip(GuiRecipe<?> gui, ItemStack aStack, List<String> currentTip,
        int aRecipeIndex) {
        CachedRecipe tObject = this.arecipes.get(aRecipeIndex);
        if (tObject instanceof CachedDefaultRecipe) {
            currentTip = frontend.handleNEIItemTooltip(aStack, currentTip, (CachedDefaultRecipe) tObject);
        }

        if (recipeNameTooltip != null) {
            recipeNameTooltip.handleTooltip(currentTip, aRecipeIndex);
        }
        return currentTip;
    }

    @Override
    public void drawExtras(int aRecipeIndex) {
        CachedDefaultRecipe cachedRecipe = ((CachedDefaultRecipe) this.arecipes.get(aRecipeIndex));

        drawDescription(cachedRecipe);
        frontend.drawNEIOverlays(cachedRecipe);
    }

    private void drawDescription(CachedDefaultRecipe cachedRecipe) {
        GT_Recipe recipe = cachedRecipe.mRecipe;
        if (overclockDescriber == null) {
            // By default, assume generic LV EU with no overclocks
            overclockDescriber = new EUNoOverclockDescriber((byte) 1, uiProperties.amperage);
        }

        GT_OverclockCalculator calculator = overclockDescriber.createCalculator(
            new GT_OverclockCalculator().setRecipeEUt(recipe.mEUt)
                .setDuration(recipe.mDuration),
            recipe);
        calculator.calculate();

        frontend.drawNEIDescription(
            new NEIRecipeInfo(
                recipe,
                recipeMap,
                cachedRecipe,
                overclockDescriber,
                calculator,
                getDescriptionYOffset()));
    }

    protected int getDescriptionYOffset() {
        return neiProperties.recipeBackgroundSize.height + neiProperties.recipeBackgroundOffset.y + WINDOW_OFFSET.y + 3;
    }

    protected void drawUI(ModularWindow window) {
        for (IDrawable background : window.getBackground()) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(
                WINDOW_OFFSET.x + neiProperties.recipeBackgroundOffset.x,
                WINDOW_OFFSET.y + neiProperties.recipeBackgroundOffset.y,
                0);
            GlStateManager.color(1f, 1f, 1f, 1f);
            background.draw(Pos2d.ZERO, window.getSize(), 0);
            GlStateManager.popMatrix();
        }

        for (Widget widget : window.getChildren()) {
            // NEI already did translation, so we can't use Widget#drawInternal here
            GlStateManager.pushMatrix();
            GlStateManager.translate(widget.getPos().x, widget.getPos().y, 0);
            GlStateManager.color(1, 1, 1, window.getAlpha());
            GlStateManager.enableBlend();

            // maybe we can use Minecraft#timer but none of the IDrawables use partialTicks
            widget.drawBackground(0);

            // noinspection OverrideOnly // It's either suppressing this warning or changing ModularUI
            widget.draw(0);
            GlStateManager.popMatrix();
        }
    }

    public static int getDrawTicks() {
        return drawTicks;
    }

    public static class FixedPositionedStack extends PositionedStack {

        public static final DecimalFormat chanceFormat = new DecimalFormat("##0.##%");
        public final int mChance;
        public final int realStackSize;
        public final boolean renderRealStackSize;

        public FixedPositionedStack(Object object, boolean renderRealStackSizes, int x, int y) {
            this(object, renderRealStackSizes, x, y, 0, true);
        }

        public FixedPositionedStack(Object object, boolean renderRealStackSizes, int x, int y, boolean aUnificate) {
            this(object, renderRealStackSizes, x, y, 0, aUnificate);
        }

        public FixedPositionedStack(Object object, boolean renderRealStackSize, int x, int y, int aChance,
            boolean aUnificate) {
            super(aUnificate ? GT_OreDictUnificator.getNonUnifiedStacks(object) : object, x, y, true);
            this.mChance = aChance;
            realStackSize = item != null ? item.stackSize : 0;
            this.renderRealStackSize = renderRealStackSize;
            if (!renderRealStackSize) {
                for (ItemStack stack : items) {
                    stack.stackSize = 1;
                }
            }
        }

        public boolean isChanceBased() {
            return mChance > 0 && mChance < 10000;
        }

        public String getChanceText() {
            return chanceFormat.format((float) mChance / 10000);
        }

        public boolean isNotConsumed() {
            return !isFluid() && item.stackSize == 0;
        }

        public boolean isFluid() {
            return ItemList.Display_Fluid.isStackEqual(item, true, true);
        }
    }

    public class CachedDefaultRecipe extends TemplateRecipeHandler.CachedRecipe {

        public final GT_Recipe mRecipe;
        public final List<PositionedStack> mOutputs;
        public final List<PositionedStack> mInputs;

        public CachedDefaultRecipe(GT_Recipe aRecipe) {
            super();
            this.mRecipe = aRecipe;
            mOutputs = new ArrayList<>();
            mInputs = new ArrayList<>();

            for (Widget child : modularWindow.getChildren()) {
                if (child instanceof SlotWidget widget) {
                    if (widget.getMcSlot()
                        .getItemHandler() == itemInputsInventory) {
                        int i = widget.getMcSlot()
                            .getSlotIndex();
                        Object input = aRecipe instanceof GT_Recipe.GT_Recipe_WithAlt
                            ? ((GT_Recipe.GT_Recipe_WithAlt) aRecipe).getAltRepresentativeInput(i)
                            : aRecipe.getRepresentativeInput(i);
                        if (input != null) {
                            mInputs.add(
                                new FixedPositionedStack(
                                    input,
                                    GT_NEI_DefaultHandler.this.neiProperties.renderRealStackSizes,
                                    widget.getPos().x + 1,
                                    widget.getPos().y + 1,
                                    true));
                        }
                    } else if (widget.getMcSlot()
                        .getItemHandler() == itemOutputsInventory) {
                            int i = widget.getMcSlot()
                                .getSlotIndex();
                            if (aRecipe.mOutputs.length > i && aRecipe.mOutputs[i] != null) {
                                mOutputs.add(
                                    new FixedPositionedStack(
                                        aRecipe.mOutputs[i],
                                        GT_NEI_DefaultHandler.this.neiProperties.renderRealStackSizes,
                                        widget.getPos().x + 1,
                                        widget.getPos().y + 1,
                                        aRecipe.getOutputChance(i),
                                        GT_NEI_DefaultHandler.this.neiProperties.unificateOutput));
                            }
                        } else if (widget.getMcSlot()
                            .getItemHandler() == specialSlotInventory) {
                                if (aRecipe.mSpecialItems != null) {
                                    mInputs.add(
                                        new FixedPositionedStack(
                                            aRecipe.mSpecialItems,
                                            GT_NEI_DefaultHandler.this.neiProperties.renderRealStackSizes,
                                            widget.getPos().x + 1,
                                            widget.getPos().y + 1));
                                }
                            } else if (widget.getMcSlot()
                                .getItemHandler() == fluidInputsInventory) {
                                    int i = widget.getMcSlot()
                                        .getSlotIndex();
                                    if (aRecipe.mFluidInputs.length > i && aRecipe.mFluidInputs[i] != null
                                        && aRecipe.mFluidInputs[i].getFluid() != null) {
                                        mInputs.add(
                                            new FixedPositionedStack(
                                                GT_Utility.getFluidDisplayStack(aRecipe.mFluidInputs[i], true),
                                                GT_NEI_DefaultHandler.this.neiProperties.renderRealStackSizes,
                                                widget.getPos().x + 1,
                                                widget.getPos().y + 1));
                                    }
                                } else if (widget.getMcSlot()
                                    .getItemHandler() == fluidOutputsInventory) {
                                        int i = widget.getMcSlot()
                                            .getSlotIndex();
                                        if (aRecipe.mFluidOutputs.length > i && aRecipe.mFluidOutputs[i] != null
                                            && aRecipe.mFluidOutputs[i].getFluid() != null) {
                                            mOutputs.add(
                                                new FixedPositionedStack(
                                                    GT_Utility.getFluidDisplayStack(aRecipe.mFluidOutputs[i], true),
                                                    GT_NEI_DefaultHandler.this.neiProperties.renderRealStackSizes,
                                                    widget.getPos().x + 1,
                                                    widget.getPos().y + 1));
                                        }
                                    }
                }
            }

            // items and fluids that exceed usual count
            UIHelper.forEachSlots((i, backgrounds, pos) -> {
                if (i >= GT_NEI_DefaultHandler.this.uiProperties.maxItemInputs && aRecipe.mInputs[i] != null) {
                    mInputs.add(
                        new FixedPositionedStack(
                            aRecipe.mInputs[i],
                            GT_NEI_DefaultHandler.this.neiProperties.renderRealStackSizes,
                            pos.x + 1,
                            pos.y + 1,
                            true));
                }
            }, (i, backgrounds, pos) -> {
                if (i >= GT_NEI_DefaultHandler.this.uiProperties.maxItemOutputs && aRecipe.mOutputs[i] != null) {
                    mOutputs.add(
                        new FixedPositionedStack(
                            aRecipe.mOutputs[i],
                            GT_NEI_DefaultHandler.this.neiProperties.renderRealStackSizes,
                            pos.x + 1,
                            pos.y + 1,
                            aRecipe.getOutputChance(i),
                            GT_NEI_DefaultHandler.this.neiProperties.unificateOutput));
                }
            }, (i, backgrounds, pos) -> {}, (i, backgrounds, pos) -> {
                if (i >= GT_NEI_DefaultHandler.this.uiProperties.maxFluidInputs && aRecipe.mFluidInputs[i] != null
                    && aRecipe.mFluidInputs[i].getFluid() != null) {
                    mInputs.add(
                        new FixedPositionedStack(
                            GT_Utility.getFluidDisplayStack(aRecipe.mFluidInputs[i], true),
                            GT_NEI_DefaultHandler.this.neiProperties.renderRealStackSizes,
                            pos.x + 1,
                            pos.y + 1));
                }
            }, (i, backgrounds, pos) -> {
                if (i >= GT_NEI_DefaultHandler.this.uiProperties.maxFluidOutputs && aRecipe.mFluidOutputs[i] != null
                    && aRecipe.mFluidOutputs[i].getFluid() != null) {
                    mOutputs.add(
                        new FixedPositionedStack(
                            GT_Utility.getFluidDisplayStack(aRecipe.mFluidOutputs[i], true),
                            GT_NEI_DefaultHandler.this.neiProperties.renderRealStackSizes,
                            pos.x + 1,
                            pos.y + 1));
                }
            },
                IDrawable.EMPTY,
                IDrawable.EMPTY,
                GT_NEI_DefaultHandler.this.frontend,
                aRecipe.mInputs.length,
                aRecipe.mOutputs.length,
                aRecipe.mFluidInputs.length,
                aRecipe.mFluidOutputs.length,
                SteamVariant.NONE,
                WINDOW_OFFSET);
        }

        @Override
        public List<PositionedStack> getIngredients() {
            return getCycledIngredients(cycleTicksStatic / 10, this.mInputs);
        }

        @Override
        public PositionedStack getResult() {
            return null;
        }

        @Override
        public List<PositionedStack> getOtherStacks() {
            return this.mOutputs;
        }
    }

    private class SortedRecipeListCache {

        private int mCachedRecipesVersion = -1;

        @Nullable
        private SoftReference<List<CachedDefaultRecipe>> mCachedRecipes;

        private Map<Byte, Range<Integer>> mTierIndexes;
        private Range<Byte> mTierRange;

        public int getCachedRecipesVersion() {
            return mCachedRecipesVersion;
        }

        public void setCachedRecipesVersion(int aCachedRecipesVersion) {
            this.mCachedRecipesVersion = aCachedRecipesVersion;
        }

        @Nullable
        public List<CachedDefaultRecipe> getCachedRecipes() {
            return mCachedRecipes == null ? null : mCachedRecipes.get();
        }

        public void setCachedRecipes(@Nonnull List<CachedDefaultRecipe> aCachedRecipes) {
            this.mCachedRecipes = new SoftReference<>(aCachedRecipes);
        }

        public Range<Integer> getIndexRangeForTiers(byte lowerTier, byte upperTier) {
            if (mTierIndexes == null) {
                computeTierIndexes();
            }
            return Range.between(getLowIndexForTier(lowerTier), getHighIndexForTier(upperTier));
        }

        private void computeTierIndexes() {
            // Holds 16 elements without rehashing
            mTierIndexes = new HashMap<>(V.length + 1, 1f);
            assert mCachedRecipes != null;
            Iterator<CachedDefaultRecipe> iterator = Objects.requireNonNull(mCachedRecipes.get())
                .iterator();

            int index = 0;
            int minIndex = 0;
            int maxIndex = -1;
            byte previousTier = -1;
            byte lowestTier = 0;
            while (iterator.hasNext()) {
                CachedDefaultRecipe recipe = iterator.next();
                byte recipeTier = GT_Utility
                    .getTier(recipe.mRecipe.mEUt / GT_NEI_DefaultHandler.this.recipeMap.getAmperage());
                if (recipeTier != previousTier) {
                    if (maxIndex != -1) {
                        mTierIndexes.put(previousTier, Range.between(minIndex, maxIndex));
                    } else {
                        lowestTier = recipeTier;
                    }
                    minIndex = index;
                    previousTier = recipeTier;
                }
                maxIndex = index;
                index++;
                if (!iterator.hasNext()) {
                    mTierIndexes.put(recipeTier, Range.between(minIndex, maxIndex));
                    mTierRange = Range.between(lowestTier, recipeTier);
                }
            }
        }

        private int getLowIndexForTier(byte lowerTier) {
            byte lowTier = (byte) Math.max(mTierRange.getMinimum(), lowerTier);
            while (mTierIndexes.get(lowTier) == null) {
                lowTier++;
            }
            return mTierIndexes.get(lowTier)
                .getMinimum();
        }

        private int getHighIndexForTier(byte upperTier) {
            byte highTier = (byte) Math.min(mTierRange.getMaximum(), upperTier);
            while (mTierIndexes.get(highTier) == null) {
                highTier--;
            }
            return mTierIndexes.get(highTier)
                .getMaximum();
        }
    }
}
