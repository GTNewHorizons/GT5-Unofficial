package gregtech.nei;

import static gregtech.api.enums.GTValues.V;

import java.awt.Rectangle;
import java.lang.ref.SoftReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
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
import gregtech.GTMod;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SteamVariant;
import gregtech.api.gui.GUIColorOverride;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IOverclockDescriptionProvider;
import gregtech.api.objects.ItemData;
import gregtech.api.objects.overclockdescriber.EUNoOverclockDescriber;
import gregtech.api.objects.overclockdescriber.OverclockDescriber;
import gregtech.api.recipe.BasicUIProperties;
import gregtech.api.recipe.NEIRecipeProperties;
import gregtech.api.recipe.RecipeCategory;
import gregtech.api.recipe.RecipeCategorySetting;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.OverclockCalculator;
import gregtech.common.blocks.ItemMachines;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.common.tileentities.machines.multi.nanochip.util.CCNEIRepresentation;

public class GTNEIDefaultHandler extends TemplateRecipeHandler {

    private static final int offsetX = 5;
    private static final int offsetY = 11;
    protected static final Pos2d WINDOW_OFFSET = new Pos2d(-offsetX, -offsetY);

    private static final ConcurrentMap<RecipeCategory, SortedRecipeListCache> CACHE = new ConcurrentHashMap<>();

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

    protected final RecipeCategory recipeCategory;
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

    protected final GUIColorOverride colorOverride = GUIColorOverride
        .get(GTUITextures.BACKGROUND_NEI_SINGLE_RECIPE.location);
    private int neiTextColorOverride = -1;

    public GTNEIDefaultHandler(RecipeCategory recipeCategory) {
        this.recipeCategory = recipeCategory;
        this.recipeMap = recipeCategory.recipeMap;
        this.frontend = recipeMap.getFrontend();
        this.uiProperties = frontend.getUIProperties();
        this.neiProperties = frontend.getNEIProperties();
        uiProperties.neiTransferRect.forEach(transferRect -> {
            transferRect = new Rectangle(transferRect);
            transferRect.translate(WINDOW_OFFSET.x, WINDOW_OFFSET.y);
            this.transferRects.add(new RecipeTransferRect(transferRect, recipeMap.unlocalizedName));
        });

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
        return CACHE.computeIfAbsent(recipeCategory, m -> new SortedRecipeListCache());
    }

    public List<CachedDefaultRecipe> getCache() {
        SortedRecipeListCache cacheHolder = getCacheHolder();
        List<CachedDefaultRecipe> cache;
        if (cacheHolder.getCachedRecipesVersion() != GTMod.proxy.getNEIReloadCount()
            || (cache = cacheHolder.getCachedRecipes()) == null) {
            try {
                RecipeCategory defaultCategory = recipeMap.getDefaultRecipeCategory();
                Collection<GTRecipe> recipes;
                if (this.recipeCategory == defaultCategory) {
                    // This is main category, so merge categories that are configured as such
                    Stream<GTRecipe> recipesToMerge = recipeMap.getBackend()
                        .getRecipeCategoryMap()
                        .entrySet()
                        .stream()
                        .flatMap(entry -> {
                            RecipeCategory recipeCategory = entry.getKey();
                            Collection<GTRecipe> correspondingRecipes = entry.getValue();
                            boolean merge = recipeCategory.settingSupplier.get() == RecipeCategorySetting.MERGE;
                            return merge ? correspondingRecipes.stream() : Stream.empty();
                        });
                    recipes = Stream.concat(
                        recipesToMerge,
                        recipeMap.getBackend()
                            .getRecipesByCategory(defaultCategory)
                            .stream())
                        .collect(Collectors.toList());
                } else {
                    // This is "sub" category
                    if (recipeCategory.settingSupplier.get() == RecipeCategorySetting.ENABLE) {
                        recipes = recipeMap.getBackend()
                            .getRecipesByCategory(recipeCategory);
                    } else {
                        recipes = Collections.emptyList();
                    }
                }
                cache = recipes.stream() // do not use parallel stream. This is already parallelized by NEI
                    .filter(r -> !r.mHidden)
                    .sorted(neiProperties.comparator)
                    .map(CachedDefaultRecipe::new)
                    .collect(Collectors.toList());
                // while the NEI parallelize handlers, for each individual handler it still uses sequential execution
                // model,
                // so we do not need any synchronization here
                // even if it does break, at worst case it's just recreating the cache multiple times, which should be
                // fine
                cacheHolder.setCachedRecipes(cache);
                cacheHolder.setCachedRecipesVersion(GTMod.proxy.getNEIReloadCount());
            } catch (Exception e) {
                throw new RuntimeException(
                    "Could not construct GT NEI Handler cache for category " + recipeCategory
                        + ", display name "
                        + recipeNameDisplay,
                    e);
            }
        }
        return cache;
    }

    @Override
    public TemplateRecipeHandler newInstance() {
        return new GTNEIDefaultHandler(recipeCategory);
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(recipeMap.unlocalizedName)) {
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
        ItemData tPrefixMaterial = GTOreDictUnificator.getAssociation(aResult);

        ArrayList<ItemStack> tResults = new ArrayList<>();
        tResults.add(aResult);
        tResults.add(GTOreDictUnificator.get(true, aResult));
        if ((tPrefixMaterial != null) && (!tPrefixMaterial.mBlackListed)
            && (!tPrefixMaterial.mPrefix.mFamiliarPrefixes.isEmpty())) {
            for (OrePrefixes tPrefix : tPrefixMaterial.mPrefix.mFamiliarPrefixes) {
                tResults.add(GTOreDictUnificator.get(tPrefix, tPrefixMaterial.mMaterial.mMaterial, 1L));
            }
        }
        if (aResult != null) {
            List<ItemStack> ccRepresentations = CCNEIRepresentation.NEI_RECIPE_ASSOCIATIONS.get(aResult);
            if (ccRepresentations != null) {
                tResults.addAll(ccRepresentations);
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
        FluidStack tFluid = GTUtility.getFluidForFilledItem(aStack, true);
        FluidStack tFluidStack;
        if (tFluid != null) {
            tFluidStack = tFluid;
            tResults.add(GTUtility.getFluidDisplayStack(tFluid, false));
        } else tFluidStack = GTUtility.getFluidFromDisplayStack(aStack);
        if (tFluidStack != null) {
            tResults.addAll(GTUtility.getContainersFromFluid(tFluidStack));
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
        ItemData tPrefixMaterial = GTOreDictUnificator.getAssociation(aInput);
        ArrayList<ItemStack> tInputs = new ArrayList<>();
        tInputs.add(aInput);
        tInputs.add(GTOreDictUnificator.get(false, aInput));
        if ((tPrefixMaterial != null) && (!tPrefixMaterial.mPrefix.mFamiliarPrefixes.isEmpty())) {
            for (OrePrefixes tPrefix : tPrefixMaterial.mPrefix.mFamiliarPrefixes) {
                tInputs.add(GTOreDictUnificator.get(tPrefix, tPrefixMaterial.mMaterial.mMaterial, 1L));
            }
        }
        if (aInput != null) {
            List<ItemStack> ccRepresentations = CCNEIRepresentation.NEI_USAGE_ASSOCIATIONS.get(aInput);
            if (ccRepresentations != null) {
                tInputs.addAll(ccRepresentations);
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
            GTNEIDefaultHandler handler = (GTNEIDefaultHandler) newInstance();
            if (RecipeCatalysts.containsCatalyst(handler, candidate)) {
                IMetaTileEntity metaTile = ItemMachines.getMetaTileEntity(candidate);
                OverclockDescriber overclockDescriber;
                if (metaTile instanceof IOverclockDescriptionProvider provider) {
                    overclockDescriber = provider.getOverclockDescriber();
                } else {
                    overclockDescriber = null;
                }
                handler.loadCraftingRecipes(recipeMap.unlocalizedName, overclockDescriber);
                return handler;
            }
        }
        return this.getUsageHandler(inputId, ingredients);
    }

    @Override
    public ICraftingHandler getRecipeHandler(String outputId, Object... results) {
        GTNEIDefaultHandler handler = (GTNEIDefaultHandler) super.getRecipeHandler(outputId, results);
        if (results.length > 0 && results[0] instanceof OverclockDescriber) {
            handler.overclockDescriber = (OverclockDescriber) results[0];
        }
        return handler;
    }

    @Override
    public String getOverlayIdentifier() {
        return recipeCategory.unlocalizedName;
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
            neiTextColorOverride = colorOverride.getTextColorOrDefault("nei", -1);
        }
        return recipeNameDisplay;
    }

    private String computeRecipeName() {
        String recipeName = StatCollector.translateToLocal(recipeCategory.unlocalizedName);
        if (overclockDescriber != null) {
            String suffix = "(" + overclockDescriber.getTierString() + ")";
            // Space will be cropped if title exceeds
            return shrinkRecipeName(recipeName + " ", suffix);
        } else {
            return shrinkRecipeName(recipeName, "");
        }
    }

    private String shrinkRecipeName(final String originalRecipeName, final String suffix) {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        int suffixWidth = fontRenderer.getStringWidth(suffix);
        if (fontRenderer.getStringWidth(originalRecipeName) + suffixWidth <= RECIPE_NAME_WIDTH) {
            return originalRecipeName + suffix;
        }

        final String ellipsis = "...";
        final int ellipsisWidth = fontRenderer.getStringWidth(ellipsis);
        String recipeName = originalRecipeName;
        do {
            recipeName = recipeName.substring(0, recipeName.length() - 1);
        } while (fontRenderer.getStringWidth(recipeName) + ellipsisWidth + suffixWidth > RECIPE_NAME_WIDTH);
        setupRecipeNameTooltip(originalRecipeName + suffix);
        return recipeName + ellipsis + suffix;
    }

    private void setupRecipeNameTooltip(String tooltip) {
        recipeNameTooltip = new NEIHandlerAbsoluteTooltip(tooltip, new Rectangle(13, -34, RECIPE_NAME_WIDTH - 1, 11));
    }

    @Override
    public String getRecipeTabName() {
        return StatCollector.translateToLocal(recipeCategory.unlocalizedName);
    }

    @Override
    public String getGuiTexture() {
        // not called
        return "";
    }

    @Override
    public List<String> handleItemTooltip(GuiRecipe<?> gui, ItemStack aStack, List<String> currentTip,
        int aRecipeIndex) {
        if (recipeNameTooltip != null) {
            recipeNameTooltip.handleTooltip(currentTip, aRecipeIndex);
        }
        if (aStack == null) {
            return currentTip;
        }

        CachedRecipe tObject = this.arecipes.get(aRecipeIndex);
        if (tObject instanceof CachedDefaultRecipe) {
            currentTip = frontend.handleNEIItemTooltip(aStack, currentTip, (CachedDefaultRecipe) tObject);
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
        GTRecipe recipe = cachedRecipe.mRecipe;
        if (overclockDescriber == null) {
            // By default, assume generic LV EU with no overclocks
            overclockDescriber = new EUNoOverclockDescriber((byte) 1, uiProperties.amperage);
        }

        OverclockCalculator calculator = overclockDescriber.createCalculator(
            new OverclockCalculator().setRecipeEUt(recipe.mEUt)
                .setDuration(recipe.mDuration),
            recipe);
        calculator.calculate();

        frontend.drawDescription(
            new RecipeDisplayInfo(
                recipe,
                recipeMap,
                overclockDescriber,
                calculator,
                getDescriptionYOffset(),
                neiTextColorOverride));
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
            super(aUnificate ? GTOreDictUnificator.getNonUnifiedStacks(object) : object, x, y, true);
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

        public final GTRecipe mRecipe;
        public final List<PositionedStack> mOutputs;
        public final List<PositionedStack> mInputs;

        public CachedDefaultRecipe(GTRecipe aRecipe) {
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
                        final Object input;
                        if (aRecipe instanceof GTRecipe.GTRecipe_WithAlt withAltRecipe) {
                            input = withAltRecipe.getAltRepresentativeInput(i);
                        } else {
                            ItemStack[] inputs = GTNEIDefaultHandler.this.neiProperties.itemInputsGetter.apply(aRecipe);
                            if (i < inputs.length && inputs[i] != null) {
                                input = inputs[i];
                            } else {
                                input = null;
                            }
                        }
                        if (input != null) {
                            mInputs.add(
                                new FixedPositionedStack(
                                    input,
                                    GTNEIDefaultHandler.this.neiProperties.renderRealStackSizes,
                                    widget.getPos().x + 1,
                                    widget.getPos().y + 1,
                                    true));
                        }
                    } else if (widget.getMcSlot()
                        .getItemHandler() == itemOutputsInventory) {
                            int i = widget.getMcSlot()
                                .getSlotIndex();
                            ItemStack[] outputs = GTNEIDefaultHandler.this.neiProperties.itemOutputsGetter
                                .apply(aRecipe);
                            if (i < outputs.length && outputs[i] != null) {
                                mOutputs.add(
                                    new FixedPositionedStack(
                                        outputs[i],
                                        GTNEIDefaultHandler.this.neiProperties.renderRealStackSizes,
                                        widget.getPos().x + 1,
                                        widget.getPos().y + 1,
                                        aRecipe.getOutputChance(i),
                                        GTNEIDefaultHandler.this.neiProperties.unificateOutput));
                            }
                        } else if (widget.getMcSlot()
                            .getItemHandler() == specialSlotInventory) {
                                if (aRecipe.mSpecialItems != null) {
                                    mInputs.add(
                                        new FixedPositionedStack(
                                            aRecipe.mSpecialItems,
                                            GTNEIDefaultHandler.this.neiProperties.renderRealStackSizes,
                                            widget.getPos().x + 1,
                                            widget.getPos().y + 1));
                                }

                            } else if (widget.getMcSlot()
                                .getItemHandler() == fluidInputsInventory) {
                                    int i = widget.getMcSlot()
                                        .getSlotIndex();
                                    FluidStack[] inputs = GTNEIDefaultHandler.this.neiProperties.fluidInputsGetter
                                        .apply(aRecipe);
                                    if (inputs.length > i && inputs[i] != null && inputs[i].getFluid() != null) {
                                        mInputs.add(
                                            new FixedPositionedStack(
                                                GTUtility.getFluidDisplayStack(inputs[i], true),
                                                GTNEIDefaultHandler.this.neiProperties.renderRealStackSizes,
                                                widget.getPos().x + 1,
                                                widget.getPos().y + 1));
                                    }
                                } else if (widget.getMcSlot()
                                    .getItemHandler() == fluidOutputsInventory) {
                                        int i = widget.getMcSlot()
                                            .getSlotIndex();
                                        FluidStack[] outputs = GTNEIDefaultHandler.this.neiProperties.fluidOutputsGetter
                                            .apply(aRecipe);
                                        if (outputs.length > i && outputs[i] != null && outputs[i].getFluid() != null) {
                                            mOutputs.add(
                                                new FixedPositionedStack(
                                                    GTUtility.getFluidDisplayStack(outputs[i], true),
                                                    GTNEIDefaultHandler.this.neiProperties.renderRealStackSizes,
                                                    widget.getPos().x + 1,
                                                    widget.getPos().y + 1));
                                        }
                                    }
                }
            }

            // items and fluids that exceed usual count
            UIHelper.forEachSlots((i, backgrounds, pos) -> {
                if (i >= GTNEIDefaultHandler.this.uiProperties.maxItemInputs && aRecipe.mInputs[i] != null) {
                    mInputs.add(
                        new FixedPositionedStack(
                            aRecipe.mInputs[i],
                            GTNEIDefaultHandler.this.neiProperties.renderRealStackSizes,
                            pos.x + 1,
                            pos.y + 1,
                            true));
                }
            }, (i, backgrounds, pos) -> {
                if (i >= GTNEIDefaultHandler.this.uiProperties.maxItemOutputs && aRecipe.mOutputs[i] != null) {
                    mOutputs.add(
                        new FixedPositionedStack(
                            aRecipe.mOutputs[i],
                            GTNEIDefaultHandler.this.neiProperties.renderRealStackSizes,
                            pos.x + 1,
                            pos.y + 1,
                            aRecipe.getOutputChance(i),
                            GTNEIDefaultHandler.this.neiProperties.unificateOutput));
                }
            }, (i, backgrounds, pos) -> {}, (i, backgrounds, pos) -> {
                if (i >= GTNEIDefaultHandler.this.uiProperties.maxFluidInputs && aRecipe.mFluidInputs[i] != null
                    && aRecipe.mFluidInputs[i].getFluid() != null) {
                    mInputs.add(
                        new FixedPositionedStack(
                            GTUtility.getFluidDisplayStack(aRecipe.mFluidInputs[i], true),
                            GTNEIDefaultHandler.this.neiProperties.renderRealStackSizes,
                            pos.x + 1,
                            pos.y + 1));
                }
            }, (i, backgrounds, pos) -> {
                if (i >= GTNEIDefaultHandler.this.uiProperties.maxFluidOutputs && aRecipe.mFluidOutputs[i] != null
                    && aRecipe.mFluidOutputs[i].getFluid() != null) {
                    mOutputs.add(
                        new FixedPositionedStack(
                            GTUtility.getFluidDisplayStack(aRecipe.mFluidOutputs[i], true),
                            GTNEIDefaultHandler.this.neiProperties.renderRealStackSizes,
                            pos.x + 1,
                            pos.y + 1));
                }
            },
                IDrawable.EMPTY,
                IDrawable.EMPTY,
                GTNEIDefaultHandler.this.frontend.getUIProperties(),
                aRecipe.mInputs.length,
                aRecipe.mOutputs.length,
                aRecipe.mFluidInputs.length,
                aRecipe.mFluidOutputs.length,
                SteamVariant.NONE,
                WINDOW_OFFSET);
        }

        @Override
        public List<PositionedStack> getIngredients() {
            return getCycledIngredients(cycleTicksStatic / 20, this.mInputs);
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
                byte recipeTier = GTUtility
                    .getTier(recipe.mRecipe.mEUt / GTNEIDefaultHandler.this.recipeMap.getAmperage());
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
