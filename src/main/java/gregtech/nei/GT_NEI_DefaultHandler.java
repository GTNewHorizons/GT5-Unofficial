package gregtech.nei;

import static codechicken.nei.recipe.RecipeInfo.getGuiOffset;

import java.awt.Point;
import java.awt.Rectangle;
import java.lang.ref.SoftReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
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
import net.minecraft.client.gui.inventory.GuiContainer;
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
import codechicken.nei.guihook.GuiContainerManager;
import codechicken.nei.guihook.IContainerInputHandler;
import codechicken.nei.guihook.IContainerTooltipHandler;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import codechicken.nei.recipe.ICraftingHandler;
import codechicken.nei.recipe.IUsageHandler;
import codechicken.nei.recipe.RecipeCatalysts;
import codechicken.nei.recipe.TemplateRecipeHandler;
import gregtech.GT_Mod;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SteamVariant;
import gregtech.api.gui.GT_GUIContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Item_Machines;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.common.power.Power;

public class GT_NEI_DefaultHandler extends RecipeMapHandler {

    public static final int sOffsetX = 5;
    public static final int sOffsetY = 11;

    private static final ConcurrentMap<GT_Recipe.GT_Recipe_Map, SortedRecipeListCache> CACHE = new ConcurrentHashMap<>();

    protected Power mPower;
    private String mRecipeName; // Name of the handler displayed on top
    private NEIHandlerAbsoluteTooltip mRecipeNameTooltip;
    private static final int RECIPE_NAME_WIDTH = 140;

    /**
     * Static version of {@link TemplateRecipeHandler#cycleticks}. Can be referenced from cached recipes.
     */
    public static int cycleTicksStatic = Math.abs((int) System.currentTimeMillis());
    /**
     * Basically {@link #cycleTicksStatic} but always updated even while holding shift
     */
    private static int drawTicks;

    protected static final int PROGRESSBAR_CYCLE_TICKS = 200;

    protected final ModularWindow modularWindow;
    protected final ItemStackHandler itemInputsInventory;
    protected final ItemStackHandler itemOutputsInventory;
    protected final ItemStackHandler specialSlotInventory;
    protected final ItemStackHandler fluidInputsInventory;
    protected final ItemStackHandler fluidOutputsInventory;
    protected static final Pos2d WINDOW_OFFSET = new Pos2d(-sOffsetX, -sOffsetY);

    static {
        GuiContainerManager.addInputHandler(new GT_RectHandler());
        GuiContainerManager.addTooltipHandler(new GT_RectHandler());
    }

    public GT_NEI_DefaultHandler(GT_Recipe.GT_Recipe_Map aRecipeMap) {
        super(aRecipeMap);
        Rectangle transferRect = new Rectangle(aRecipeMap.neiTransferRect);
        transferRect.translate(WINDOW_OFFSET.x, WINDOW_OFFSET.y);
        this.transferRects.add(new RecipeTransferRect(transferRect, getOverlayIdentifier()));

        ModularWindow.Builder builder = mRecipeMap.createNEITemplate(
            itemInputsInventory = new ItemStackHandler(mRecipeMap.mUsualInputCount),
            itemOutputsInventory = new ItemStackHandler(mRecipeMap.mUsualOutputCount),
            specialSlotInventory = new ItemStackHandler(1),
            fluidInputsInventory = new ItemStackHandler(mRecipeMap.getUsualFluidInputCount()),
            fluidOutputsInventory = new ItemStackHandler(mRecipeMap.getUsualFluidOutputCount()),
            () -> ((float) getDrawTicks() % PROGRESSBAR_CYCLE_TICKS) / PROGRESSBAR_CYCLE_TICKS,
            WINDOW_OFFSET);
        modularWindow = builder.build();
        UIInfos.initializeWindow(Minecraft.getMinecraft().thePlayer, modularWindow);
    }

    @Deprecated
    public List<GT_Recipe> getSortedRecipes() {
        List<GT_Recipe> result = new ArrayList<>(this.mRecipeMap.mRecipeList);
        Collections.sort(result);
        return result;
    }

    private SortedRecipeListCache getCacheHolder() {
        return CACHE.computeIfAbsent(mRecipeMap, m -> new SortedRecipeListCache());
    }

    public List<CachedDefaultRecipe> getCache() {
        SortedRecipeListCache cacheHolder = getCacheHolder();
        List<CachedDefaultRecipe> cache;
        if (cacheHolder.getCachedRecipesVersion() != GT_Mod.gregtechproxy.getReloadCount()
            || (cache = cacheHolder.getCachedRecipes()) == null) {
            cache = mRecipeMap.mRecipeList.stream() // do not use parallel stream. This is already parallelized by NEI
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
        return new GT_NEI_DefaultHandler(this.mRecipeMap);
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(getOverlayIdentifier())) {
            if (results.length > 0 && results[0] instanceof Power) {
                mPower = (Power) results[0];
                if (mRecipeMap.useComparatorForNEI) {
                    loadTieredCraftingRecipesWithPower(mPower);
                } else {
                    loadTieredCraftingRecipesUpTo(mPower.getTier());
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

    private void loadTieredCraftingRecipesWithPower(Power power) {
        arecipes.addAll(getTieredRecipes(power));
    }

    private List<CachedDefaultRecipe> getTieredRecipes(Power power) {
        List<CachedDefaultRecipe> recipes = getCache();
        if (recipes.size() > 0) {
            recipes = recipes.stream()
                .filter(
                    recipe -> power.compareTo(GT_Utility.getTier(recipe.mRecipe.mEUt), recipe.mRecipe.mSpecialValue)
                        >= 0)
                .collect(Collectors.toList());
        }
        return recipes;
    }

    private void loadTieredCraftingRecipesUpTo(byte upperTier) {
        arecipes.addAll(getTieredRecipes(upperTier));
    }

    private List<CachedDefaultRecipe> getTieredRecipes(byte upperTier) {
        List<CachedDefaultRecipe> recipes = getCache();
        if (recipes.size() > 0) {
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
                IMetaTileEntity gtTileEntity = GT_Item_Machines.getMetaTileEntity(candidate);
                Power power;
                if (gtTileEntity != null) {
                    power = gtTileEntity.getPower();
                } else {
                    power = null;
                }
                handler.loadCraftingRecipes(getOverlayIdentifier(), power);
                return handler;
            }
        }
        return this.getUsageHandler(inputId, ingredients);
    }

    @Override
    public ICraftingHandler getRecipeHandler(String outputId, Object... results) {
        GT_NEI_DefaultHandler handler = (GT_NEI_DefaultHandler) super.getRecipeHandler(outputId, results);
        if (results.length > 0 && results[0] instanceof Power) {
            handler.mPower = (Power) results[0];
        }
        return handler;
    }

    @Override
    public String getOverlayIdentifier() {
        return this.mRecipeMap.mNEIName;
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
        if (mRecipeName == null) {
            mRecipeName = computeRecipeName();
            updateOverrideTextColor();
            mRecipeMap.updateNEITextColorOverride();
        }
        return mRecipeName;
    }

    private String computeRecipeName() {
        String recipeName = GT_LanguageManager.getTranslation(this.mRecipeMap.mUnlocalizedName);
        if (mPower != null) {
            recipeName = addSuffixToRecipeName(recipeName, mPower.getTierString() + ")");
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
        mRecipeNameTooltip = new NEIHandlerAbsoluteTooltip(tooltip, new Rectangle(13, -34, RECIPE_NAME_WIDTH - 1, 11));
    }

    @Override
    public String getRecipeTabName() {
        return GT_LanguageManager.getTranslation(this.mRecipeMap.mUnlocalizedName);
    }

    @Override
    public String getGuiTexture() {
        return this.mRecipeMap.mNEIGUIPath;
    }

    @Override
    public List<String> handleItemTooltip(GuiRecipe<?> gui, ItemStack aStack, List<String> currentTip,
        int aRecipeIndex) {
        CachedRecipe tObject = this.arecipes.get(aRecipeIndex);
        if (tObject instanceof CachedDefaultRecipe) {
            currentTip = mRecipeMap.handleNEIItemTooltip(aStack, currentTip, (CachedDefaultRecipe) tObject);
        }

        if (mRecipeNameTooltip != null) {
            mRecipeNameTooltip.handleTooltip(currentTip, aRecipeIndex);
        }
        return currentTip;
    }

    @Override
    public void drawExtras(int aRecipeIndex) {
        CachedDefaultRecipe cachedRecipe = ((CachedDefaultRecipe) this.arecipes.get(aRecipeIndex));

        drawDescription(cachedRecipe);
        mRecipeMap.drawNEIOverlays(cachedRecipe);
    }

    private void drawDescription(CachedDefaultRecipe cachedRecipe) {
        GT_Recipe recipe = cachedRecipe.mRecipe;
        if (mPower == null) {
            mPower = mRecipeMap.getPowerFromRecipeMap();
        }
        mPower.computePowerUsageAndDuration(recipe.mEUt, recipe.mDuration, recipe.mSpecialValue);

        mRecipeMap
            .drawNEIDescription(new NEIRecipeInfo(recipe, mRecipeMap, cachedRecipe, mPower, getDescriptionYOffset()));
    }

    @Deprecated
    protected String getSpecialInfo(int specialValue) {
        return "";
    }

    @Deprecated
    protected void drawLine(int lineNumber, String line) {
        drawText(10, getDescriptionYOffset() + lineNumber * 10, line, 0xFF000000);
    }

    protected int getDescriptionYOffset() {
        return mRecipeMap.neiBackgroundSize.height + mRecipeMap.neiBackgroundOffset.y + WINDOW_OFFSET.y + 3;
    }

    protected void drawUI(ModularWindow window) {
        for (IDrawable background : window.getBackground()) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(
                WINDOW_OFFSET.x + mRecipeMap.neiBackgroundOffset.x,
                WINDOW_OFFSET.y + mRecipeMap.neiBackgroundOffset.y,
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

    public static class GT_RectHandler implements IContainerInputHandler, IContainerTooltipHandler {

        @Override
        public boolean mouseClicked(GuiContainer gui, int mouseX, int mouseY, int button) {
            if (canHandle(gui)) {
                NEI_TransferRectHost host = (NEI_TransferRectHost) gui;
                if (hostRectContainsMouse(host, getMousePos(gui, mouseX, mouseY))) {
                    if (button == 0) {
                        return handleTransferRectMouseClick(host, false);
                    }
                    if (button == 1) {
                        return handleTransferRectMouseClick(host, true);
                    }
                }
            }
            return false;
        }

        private Point getMousePos(GuiContainer gui, int mouseX, int mouseY) {
            return new Point(
                mouseX - ((GT_GUIContainer) gui).getLeft() - getGuiOffset(gui)[0],
                mouseY - ((GT_GUIContainer) gui).getTop() - getGuiOffset(gui)[1]);
        }

        private boolean hostRectContainsMouse(NEI_TransferRectHost host, Point mousePos) {
            return host.getNeiTransferRect()
                .contains(mousePos);
        }

        private boolean handleTransferRectMouseClick(NEI_TransferRectHost gui, boolean usage) {
            String mNEI = gui.getNeiTransferRectString();
            Object[] args = gui.getNeiTransferRectArgs();
            return usage ? GuiUsageRecipe.openRecipeGui(mNEI) : GuiCraftingRecipe.openRecipeGui(mNEI, args);
        }

        @Override
        public boolean lastKeyTyped(GuiContainer gui, char keyChar, int keyCode) {
            return false;
        }

        public boolean canHandle(GuiContainer gui) {
            return gui instanceof NEI_TransferRectHost
                && GT_Utility.isStringValid(((NEI_TransferRectHost) gui).getNeiTransferRectString());
        }

        @Override
        public List<String> handleTooltip(GuiContainer gui, int mouseX, int mouseY, List<String> currentTip) {
            if ((canHandle(gui)) && (currentTip.isEmpty())) {
                NEI_TransferRectHost host = (NEI_TransferRectHost) gui;
                if (hostRectContainsMouse(host, getMousePos(gui, mouseX, mouseY))) {
                    currentTip.add(host.getNeiTransferRectTooltip());
                }
            }
            return currentTip;
        }

        @Override
        public List<String> handleItemDisplayName(GuiContainer gui, ItemStack itemstack, List<String> currentTip) {
            return currentTip;
        }

        @Override
        public List<String> handleItemTooltip(GuiContainer gui, ItemStack itemstack, int mouseX, int mouseY,
            List<String> currentTip) {
            return currentTip;
        }

        @Override
        public boolean keyTyped(GuiContainer gui, char keyChar, int keyCode) {
            return false;
        }

        @Override
        public void onKeyTyped(GuiContainer gui, char keyChar, int keyID) {}

        @Override
        public void onMouseClicked(GuiContainer gui, int mouseX, int mouseY, int button) {}

        @Override
        public void onMouseUp(GuiContainer gui, int mouseX, int mouseY, int button) {}

        @Override
        public boolean mouseScrolled(GuiContainer gui, int mouseX, int mouseY, int scrolled) {
            return false;
        }

        @Override
        public void onMouseScrolled(GuiContainer gui, int mouseX, int mouseY, int scrolled) {}

        @Override
        public void onMouseDragged(GuiContainer gui, int mouseX, int mouseY, int button, long heldTime) {}
    }

    public static class FixedPositionedStack extends PositionedStack {

        public static final DecimalFormat chanceFormat = new DecimalFormat("##0.##%");
        public final int mChance;
        public final int realStackSize;
        public final boolean renderRealStackSize;

        @Deprecated
        public FixedPositionedStack(Object object, int x, int y) {
            this(object, true, x, y, 0, true);
        }

        @Deprecated
        public FixedPositionedStack(Object object, int x, int y, boolean aUnificate) {
            this(object, true, x, y, 0, aUnificate);
        }

        @Deprecated
        public FixedPositionedStack(Object object, int x, int y, int aChance) {
            this(object, true, x, y, aChance, true);
        }

        @Deprecated
        public FixedPositionedStack(Object object, int x, int y, int aChance, boolean aUnificate) {
            this(object, true, x, y, aChance, aUnificate);
        }

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
            return !ItemList.Display_Fluid.isStackEqual(item, true, true) && item.stackSize == 0;
        }
    }

    public class CachedDefaultRecipe extends TemplateRecipeHandler.CachedRecipe {

        public final GT_Recipe mRecipe;
        public final List<PositionedStack> mOutputs;
        public final List<PositionedStack> mInputs;

        // Draws a grid of items for NEI rendering.
        private void drawNEIItemGrid(ItemStack[] ItemArray, int x_coord_origin, int y_coord_origin, int x_dir_max_items,
            int y_max_dir_max_items, GT_Recipe Recipe, boolean is_input) {
            if (ItemArray.length > x_dir_max_items * y_max_dir_max_items) {
                GT_Log.err.println("Recipe cannot be properly displayed in NEI due to too many items.");
            }
            // 18 pixels to get to a new grid for placing an item tile since they are 16x16 and have 1 pixel buffers
            // around them.
            int x_max = x_coord_origin + x_dir_max_items * 18;

            // Temp variables to keep track of current coordinates to place item at.
            int x_coord = x_coord_origin;
            int y_coord = y_coord_origin;

            // Iterate over all items in array and display them.
            int special_counter = 0;
            for (ItemStack item : ItemArray) {
                if (item != GT_Values.NI) {
                    if (is_input) {
                        mInputs.add(
                            new FixedPositionedStack(
                                item,
                                GT_NEI_DefaultHandler.this.mRecipeMap.renderRealStackSizes,
                                x_coord,
                                y_coord,
                                true));
                    } else {
                        mOutputs.add(
                            new FixedPositionedStack(
                                item,
                                GT_NEI_DefaultHandler.this.mRecipeMap.renderRealStackSizes,
                                x_coord,
                                y_coord,
                                Recipe.getOutputChance(special_counter),
                                GT_NEI_DefaultHandler.this.mRecipeMap.mNEIUnificateOutput));
                        special_counter++;
                    }
                    x_coord += 18;
                    if (x_coord == x_max) {
                        x_coord = x_coord_origin;
                        y_coord += 18;
                    }
                }
            }
        }

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
                                    GT_NEI_DefaultHandler.this.mRecipeMap.renderRealStackSizes,
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
                                        GT_NEI_DefaultHandler.this.mRecipeMap.renderRealStackSizes,
                                        widget.getPos().x + 1,
                                        widget.getPos().y + 1,
                                        aRecipe.getOutputChance(i),
                                        GT_NEI_DefaultHandler.this.mRecipeMap.mNEIUnificateOutput));
                            }
                        } else if (widget.getMcSlot()
                            .getItemHandler() == specialSlotInventory) {
                                if (aRecipe.mSpecialItems != null) {
                                    mInputs.add(
                                        new FixedPositionedStack(
                                            aRecipe.mSpecialItems,
                                            GT_NEI_DefaultHandler.this.mRecipeMap.renderRealStackSizes,
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
                                                GT_NEI_DefaultHandler.this.mRecipeMap.renderRealStackSizes,
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
                                                    GT_NEI_DefaultHandler.this.mRecipeMap.renderRealStackSizes,
                                                    widget.getPos().x + 1,
                                                    widget.getPos().y + 1));
                                        }
                                    }
                }
            }

            // items and fluids that exceed usual count
            UIHelper.forEachSlots((i, backgrounds, pos) -> {
                if (i >= GT_NEI_DefaultHandler.this.mRecipeMap.mUsualInputCount && aRecipe.mInputs[i] != null) {
                    mInputs.add(
                        new FixedPositionedStack(
                            aRecipe.mInputs[i],
                            GT_NEI_DefaultHandler.this.mRecipeMap.renderRealStackSizes,
                            pos.x + 1,
                            pos.y + 1,
                            true));
                }
            }, (i, backgrounds, pos) -> {
                if (i >= GT_NEI_DefaultHandler.this.mRecipeMap.mUsualOutputCount && aRecipe.mOutputs[i] != null) {
                    mOutputs.add(
                        new FixedPositionedStack(
                            aRecipe.mOutputs[i],
                            GT_NEI_DefaultHandler.this.mRecipeMap.renderRealStackSizes,
                            pos.x + 1,
                            pos.y + 1,
                            aRecipe.getOutputChance(i),
                            GT_NEI_DefaultHandler.this.mRecipeMap.mNEIUnificateOutput));
                }
            }, (i, backgrounds, pos) -> {}, (i, backgrounds, pos) -> {
                if (i >= GT_NEI_DefaultHandler.this.mRecipeMap.getUsualFluidInputCount()
                    && aRecipe.mFluidInputs[i] != null
                    && aRecipe.mFluidInputs[i].getFluid() != null) {
                    mInputs.add(
                        new FixedPositionedStack(
                            GT_Utility.getFluidDisplayStack(aRecipe.mFluidInputs[i], true),
                            GT_NEI_DefaultHandler.this.mRecipeMap.renderRealStackSizes,
                            pos.x + 1,
                            pos.y + 1));
                }
            }, (i, backgrounds, pos) -> {
                if (i >= GT_NEI_DefaultHandler.this.mRecipeMap.getUsualFluidOutputCount()
                    && aRecipe.mFluidOutputs[i] != null
                    && aRecipe.mFluidOutputs[i].getFluid() != null) {
                    mOutputs.add(
                        new FixedPositionedStack(
                            GT_Utility.getFluidDisplayStack(aRecipe.mFluidOutputs[i], true),
                            GT_NEI_DefaultHandler.this.mRecipeMap.renderRealStackSizes,
                            pos.x + 1,
                            pos.y + 1));
                }
            },
                null,
                null,
                GT_NEI_DefaultHandler.this.mRecipeMap,
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

    @Deprecated
    public String trans(String aKey, String aEnglish) {
        return GT_Utility.trans(aKey, aEnglish);
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
            mTierIndexes = new HashMap<>(GT_Values.V.length + 1, 1f);
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
                    .getTier(recipe.mRecipe.mEUt / GT_NEI_DefaultHandler.this.mRecipeMap.mAmperage);
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
