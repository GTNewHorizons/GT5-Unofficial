package gregtech.nei;

import codechicken.lib.gui.GuiDraw;
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
import gregtech.api.enums.OrePrefixes;
import gregtech.api.gui.GT_GUIContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.power.EUPower;
import gregtech.common.power.Power;
import gregtech.common.power.UnspecifiedEUPower;
import gregtech.common.blocks.GT_Item_Machines;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.Range;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import static codechicken.nei.recipe.RecipeInfo.getGuiOffset;

@SuppressWarnings("all")
public class GT_NEI_DefaultHandler extends RecipeMapHandler {
    public static final int sOffsetX = 5;
    public static final int sOffsetY = 11;
    private static final int M = 1000000;
    private static final ConcurrentMap<GT_Recipe.GT_Recipe_Map, SortedRecipeListCache> CACHE = new ConcurrentHashMap<>();

    private Power mPower;
    private String mRecipeName; // Name of the handler displayed on top
    private NEIHandlerAbsoluteTooltip mRecipeNameTooltip;
    private static final int RECIPE_NAME_WIDTH = 140;

     /**
     * Static version of {@link TemplateRecipeHandler#cycleticks}.
     * Can be referenced from cached recipes.
     */
    public static int cycleTicksStatic = Math.abs((int) System.currentTimeMillis());

    static {
        GuiContainerManager.addInputHandler(new GT_RectHandler());
        GuiContainerManager.addTooltipHandler(new GT_RectHandler());
    }

    public GT_NEI_DefaultHandler(GT_Recipe.GT_Recipe_Map aRecipeMap) {
        super(aRecipeMap);
        if ("gt.recipe.complexfusionreactor".equals(aRecipeMap.mUnlocalizedName)) {
            this.transferRects.add(new RecipeTransferRect(new Rectangle(74, 23, 18, 18), getOverlayIdentifier()));
        }
        else {
            this.transferRects.add(new RecipeTransferRect(new Rectangle(65, 13, 36, 18), getOverlayIdentifier()));
        }
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
        if (cacheHolder.getCachedRecipesVersion() != GT_Mod.gregtechproxy.getReloadCount() || (cache = cacheHolder.getCachedRecipes()) == null) {
            cache = mRecipeMap.mRecipeList.stream()  // do not use parallel stream. This is already parallelized by NEI
                    .filter(r -> !r.mHidden)
                    .sorted()
                    .map(CachedDefaultRecipe::new)
                    .collect(Collectors.toList());
            // while the NEI parallelize handlers, for each individual handler it still uses sequential execution model
            // so we do not need any synchronization here
            // even if it does break, at worst case it's just recreating the cache multiple times, which should be fine
            cacheHolder.setCachedRecipes(cache);
            cacheHolder.setCachedRecipesVersion(GT_Mod.gregtechproxy.getReloadCount());
        }
        return cache;
    }

    public static void drawText(int aX, int aY, String aString, int aColor) {
        Minecraft.getMinecraft().fontRenderer.drawString(aString, aX, aY, aColor);
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
                loadTieredCraftingRecipesUpTo(mPower.getTier());
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
        if ((tPrefixMaterial != null) && (!tPrefixMaterial.mBlackListed) && (!tPrefixMaterial.mPrefix.mFamiliarPrefixes.isEmpty())) {
            for (OrePrefixes tPrefix : tPrefixMaterial.mPrefix.mFamiliarPrefixes) {
                tResults.add(GT_OreDictUnificator.get(tPrefix, tPrefixMaterial.mMaterial.mMaterial, 1L));
            }
        }
        addFluidStacks(aResult, tResults);
        for (CachedDefaultRecipe recipe : getCache()) {
            if (tResults.stream().anyMatch(stack -> recipe.contains(recipe.mOutputs, stack)))
                arecipes.add(recipe);
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

    private void loadTieredCraftingRecipesUpTo(byte upperTier) {
        arecipes.addAll(getTieredRecipes((byte) 0, upperTier));
    }

    private List<CachedDefaultRecipe> getTieredRecipes(byte lowerTier, byte upperTier) {
        List<CachedDefaultRecipe> recipes = getCache();
        if ( recipes.size() > 0 ) {
            Range<Integer> indexRange = getCacheHolder().getIndexRangeForTiers(lowerTier, upperTier);
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
            if (tInputs.stream().anyMatch(stack -> recipe.contains(recipe.mInputs, stack)))
                arecipes.add(recipe);
        }
    }

    @Override
    public IUsageHandler getUsageAndCatalystHandler(String inputId, Object... ingredients) {
        if (inputId.equals("item")) {
            ItemStack candidate = (ItemStack) ingredients[0];
            GT_NEI_DefaultHandler handler = (GT_NEI_DefaultHandler) newInstance();
            if (RecipeCatalysts.containsCatalyst(handler, candidate)) {
                IMetaTileEntity gtTileEntity = GT_Item_Machines.getMetaTileEntity(candidate);
                if (gtTileEntity instanceof GT_MetaTileEntity_BasicMachine) {
                    Power power = ((GT_MetaTileEntity_BasicMachine) gtTileEntity).getPower();
                    handler.loadCraftingRecipes(getOverlayIdentifier(), power);
                    return handler;
                } else {
                    handler.loadCraftingRecipes(getOverlayIdentifier(), (Object) null);
                    return handler;
                }
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
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuiDraw.changeTexture(getGuiTexture());
        GuiDraw.drawTexturedModalRect(-4, -8, 1, 3, 174, 78);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!NEIClientUtils.shiftKey())
            cycleTicksStatic++;
    }

    @Override
    public int recipiesPerPage() {
        return 1;
    }

    @Override
    public String getRecipeName() {
        if (mRecipeName == null) {
            mRecipeName = computeRecipeName();
        }
        return mRecipeName;
    }

    private String computeRecipeName() {
        String recipeName = GT_LanguageManager.getTranslation(this.mRecipeMap.mUnlocalizedName);
        if (mPower != null) {
            recipeName = addSuffixToRecipeName(recipeName, " (", mPower.getTierString() + ")");
        }
        return recipeName;
    }

    private String addSuffixToRecipeName(String recipeName, String separator, String suffix) {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        int recipeNameWidth = fontRenderer.getStringWidth(recipeName);
        int targetWidth = RECIPE_NAME_WIDTH - fontRenderer.getStringWidth(suffix);
        if (recipeNameWidth + fontRenderer.getStringWidth(separator) > targetWidth) {
            setupRecipeNameTooltip(recipeName + separator + suffix);
            separator = "...(";
            recipeName = shrinkRecipeName(recipeName, targetWidth - fontRenderer.getStringWidth(separator));
        }
        return recipeName + separator + suffix;
    }

    private String shrinkRecipeName(String recipeName, int targetWidth) {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        do {
            recipeName = recipeName.substring(0, recipeName.length() - 2);
        } while (fontRenderer.getStringWidth(recipeName)  > targetWidth);
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
    public List<String> handleItemTooltip(GuiRecipe gui, ItemStack aStack, List<String> currenttip, int aRecipeIndex) {
        CachedRecipe tObject = this.arecipes.get(aRecipeIndex);
        if ((tObject instanceof CachedDefaultRecipe)) {
            CachedDefaultRecipe tRecipe = (CachedDefaultRecipe) tObject;
            for (PositionedStack tStack : tRecipe.mOutputs) {
                if (aStack == tStack.item) {
                    if ((!(tStack instanceof FixedPositionedStack)) || (((FixedPositionedStack) tStack).mChance <= 0) || (((FixedPositionedStack) tStack).mChance == 10000)) {
                        break;
                    }
                    currenttip.add(GT_Utility.trans("150", "Chance: ") + ((FixedPositionedStack) tStack).mChance / 100 + "." + (((FixedPositionedStack) tStack).mChance % 100 < 10 ? "0" + ((FixedPositionedStack) tStack).mChance % 100 : Integer.valueOf(((FixedPositionedStack) tStack).mChance % 100)) + "%");
                    break;
                }
            }
            for (PositionedStack tStack : tRecipe.mInputs) {
                if (aStack == tStack.item) {
                    if ((gregtech.api.enums.ItemList.Display_Fluid.isStackEqual(tStack.item, true, true)) ||
                        (tStack.item.stackSize != 0)) {
                        break;
                    }
                    currenttip.add(GT_Utility.trans("151", "Does not get consumed in the process"));
                    break;
                }
            }
        }

        if (mRecipeNameTooltip != null) {
            mRecipeNameTooltip.handleTooltip(currenttip, aRecipeIndex);
        }
        return currenttip;
    }

    private Power getPowerFromRecipeMap() {
        // By default, assume generic EU LV power with no overclocks
        Power power;
        if (this.mRecipeMap.mShowVoltageAmperageInNEI) {
            power = new EUPower((byte) 1, this.mRecipeMap.mAmperage);
        } else {
            power = new UnspecifiedEUPower((byte) 1, this.mRecipeMap.mAmperage);
        }
        return power;
    }

    @Override
    public void drawExtras(int aRecipeIndex) {
        GT_Recipe recipe = ((CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe;
        String[] recipeDesc = recipe.getNeiDesc();
        if (recipeDesc == null) {
            drawDescription(recipe);
        } else {
            drawOverrideDescription(recipeDesc);
        }
    }

    private void drawDescription(GT_Recipe recipe) {
        if (mPower == null) {
            mPower = getPowerFromRecipeMap();
        }
        mPower.computePowerUsageAndDuration(recipe.mEUt, recipe.mDuration);

        int lineCounter = 0;
        if (mPower.getEuPerTick() > 0) {
            drawLine(lineCounter, GT_Utility.trans("152", "Total: ") + mPower.getTotalPowerString());
            lineCounter++;

            String amperage = mPower.getAmperageString();
            String powerUsage = mPower.getPowerUsageString();
            if (amperage == null || amperage.equals("unspecified") || powerUsage.contains("(OC)")){
                drawLine(lineCounter, GT_Utility.trans("153", "Usage: ") + powerUsage);
                lineCounter++;
            } else if (amperage.equals("1")) {
                drawLine(lineCounter, GT_Utility.trans("154", "Voltage: ") + mPower.getVoltageString());
                lineCounter++;
            } else {
                drawLine(lineCounter, GT_Utility.trans("153", "Usage: ") + powerUsage);
                lineCounter++;
                drawLine(lineCounter, GT_Utility.trans("154", "Voltage: ") + mPower.getVoltageString());
                lineCounter++;
                drawLine(lineCounter, GT_Utility.trans("155", "Amperage: ") + amperage);
                lineCounter++;
            }

        }
        if (mPower.getDurationTicks() > 0) {
            if(GT_Mod.gregtechproxy.mNEIRecipeSecondMode) {
                drawLine(lineCounter, GT_Utility.trans("158", "Time: ") + mPower.getDurationStringSeconds());
                lineCounter++;
            } else {
                drawLine(lineCounter, GT_Utility.trans("158", "Time: ") + mPower.getDurationStringTicks());
                lineCounter++;
            }
        }
        if (this.mRecipeMap.mNEIName.equals("gt.recipe.fusionreactor") || this.mRecipeMap.mNEIName.equals("gt.recipe.complexfusionreactor")) {
            drawOptionalLine(lineCounter, getSpecialInfo(recipe.mSpecialValue) + " " + formatSpecialValueFusion(recipe.mSpecialValue, recipe.mEUt));
        }
        drawOptionalLine(lineCounter, getSpecialInfo(recipe.mSpecialValue));
    }

    private void drawOverrideDescription(String[] recipeDesc) {
        for (int i = 0; i < recipeDesc.length; i++) {
            if (recipeDesc[i] != null) {
                drawLine(i, recipeDesc[i]);
            }
        }
    }

    private String getSpecialInfo(int specialValue) {
        String specialInfo = null;
        if (specialValue == -100 && GT_Mod.gregtechproxy.mLowGravProcessing) {
            specialInfo = GT_Utility.trans("159", "Needs Low Gravity");
        } else if (specialValue == -200 && GT_Mod.gregtechproxy.mEnableCleanroom) {
            specialInfo = GT_Utility.trans("160", "Needs Cleanroom");
        } else if (specialValue == -201) {
            specialInfo = GT_Utility.trans("206", "Scan for Assembly Line");
        } else if (specialValue == -300 && GT_Mod.gregtechproxy.mEnableCleanroom) {
            specialInfo = GT_Utility.trans("160", "Needs Cleanroom & LowGrav");
        } else if (specialValue == -400) {
            specialInfo = GT_Utility.trans("216", "Deprecated Recipe");
        } else if (hasSpecialValueFormat()) {
            specialInfo = formatSpecialValue(specialValue);
        }
        return specialInfo;
    }

    private boolean hasSpecialValueFormat() {
        return (GT_Utility.isStringValid(this.mRecipeMap.mNEISpecialValuePre))
            || (GT_Utility.isStringValid(this.mRecipeMap.mNEISpecialValuePost));
    }

    private String formatSpecialValue(int SpecialValue) {
        return this.mRecipeMap.mNEISpecialValuePre + GT_Utility.formatNumbers(
            (long) SpecialValue * this.mRecipeMap.mNEISpecialValueMultiplier)
            + this.mRecipeMap.mNEISpecialValuePost;
    }

    private String formatSpecialValueFusion(int SpecialValue, int Voltage) {
        int tier = 0;
        if (SpecialValue <= 10 * M * 16) {
            tier = 1;
        } else if (SpecialValue <= 20 * M * 16) {
            tier = 2;
        } else if (SpecialValue <= 40 * M * 16) {
            tier = 3;
        } else {
            tier = 4;
        }
        if (Voltage <= 32768) {
            tier = Math.max(tier, 1);
        } else if (Voltage <= 65536) {
            tier = Math.max(tier, 2);
        } else if (Voltage <= 131072) {
            tier = Math.max(tier, 3);
        } else {
            tier = Math.max(tier, 4);
        }
        return "(MK " + tier + ")";
    }

    private void drawOptionalLine(int lineNumber, String line, String prefix) {
        if (!"unspecified".equals(line)) {
            drawLine(lineNumber, prefix + line);
        }
    }

    private void drawOptionalLine(int lineNumber, String line) {
        if (!"unspecified".equals(line)) {
            drawLine(lineNumber, line);
        }
    }

    private void drawLine(int lineNumber, String line) {
        drawText(10, 73 + lineNumber * 10, line, 0xFF000000);
    }

    public static class GT_RectHandler
        implements IContainerInputHandler, IContainerTooltipHandler {
        @Override
        public boolean mouseClicked(GuiContainer gui, int mousex, int mousey, int button) {
            if (canHandle(gui)) {
                NEI_TransferRectHost host = (NEI_TransferRectHost) gui;
                if (hostRectContainsMouse(host, getMousePos(gui, mousex, mousey))) {
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

        private Point getMousePos(GuiContainer gui, int mousex, int mousey) {
            Point point = new Point(
                mousex - ((GT_GUIContainer) gui).getLeft() - getGuiOffset(gui)[0],
                mousey - ((GT_GUIContainer) gui).getTop() - getGuiOffset(gui)[1]);
            return point;
        }

        private boolean hostRectContainsMouse(NEI_TransferRectHost host, Point mousePos) {
            return host.getNeiTransferRect().contains(mousePos);
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
            return gui instanceof NEI_TransferRectHost && GT_Utility.isStringValid(((NEI_TransferRectHost) gui).getNeiTransferRectString());
        }

        @Override
        public List<String> handleTooltip(GuiContainer gui, int mousex, int mousey, List<String> currenttip) {
            if ((canHandle(gui)) && (currenttip.isEmpty())) {
                NEI_TransferRectHost host = (NEI_TransferRectHost) gui;
                if (hostRectContainsMouse(host, getMousePos(gui, mousex, mousey))) {
                    currenttip.add(host.getNeiTransferRectTooltip());
                }
            }
            return currenttip;
        }

        @Override
        public List<String> handleItemDisplayName(GuiContainer gui, ItemStack itemstack, List<String> currenttip) {
            return currenttip;
        }

        @Override
        public List<String> handleItemTooltip(GuiContainer gui, ItemStack itemstack, int mousex, int mousey, List<String> currenttip) {
            return currenttip;
        }

        @Override
        public boolean keyTyped(GuiContainer gui, char keyChar, int keyCode) {
            return false;
        }

        @Override
        public void onKeyTyped(GuiContainer gui, char keyChar, int keyID) {}

        @Override
        public void onMouseClicked(GuiContainer gui, int mousex, int mousey, int button) {}

        @Override
        public void onMouseUp(GuiContainer gui, int mousex, int mousey, int button) {}

        @Override
        public boolean mouseScrolled(GuiContainer gui, int mousex, int mousey, int scrolled) {
            return false;
        }

        @Override
        public void onMouseScrolled(GuiContainer gui, int mousex, int mousey, int scrolled) {}

        @Override
        public void onMouseDragged(GuiContainer gui, int mousex, int mousey, int button, long heldTime) {}
    }

    public static class FixedPositionedStack extends PositionedStack {
        public final int mChance;
        public boolean permutated = false;

        public FixedPositionedStack(Object object, int x, int y) {
            this(object, x, y, 0);
        }

        public FixedPositionedStack(Object object, int x, int y, boolean aUnificate) {
            this(object, x, y, 0, aUnificate);
        }

        public FixedPositionedStack(Object object, int x, int y, int aChance) {
            this(object, x, y, aChance, true);
        }

        public FixedPositionedStack(Object object, int x, int y, int aChance, boolean aUnificate) {
            super(aUnificate ? GT_OreDictUnificator.getNonUnifiedStacks(object) : object, x, y, true);
            this.mChance = aChance;
        }

        @Override
        public void generatePermutations() {
            if (this.permutated) {
                return;
            }
            ArrayList<ItemStack> tDisplayStacks = new ArrayList<>();
            for (ItemStack tStack : this.items) {
                if (GT_Utility.isStackValid(tStack)) {
                    if (tStack.getItemDamage() == 32767) {
                        List<ItemStack> permutations = codechicken.nei.ItemList.itemMap.get(tStack.getItem());
                        if (!permutations.isEmpty()) {
                            for (ItemStack permutation : permutations) {
                                tDisplayStacks.add(GT_Utility.copyAmount(tStack.stackSize, permutation));
                            }
                        } else {
                            ItemStack base = new ItemStack(tStack.getItem(), tStack.stackSize);
                            base.stackTagCompound = tStack.stackTagCompound;
                            tDisplayStacks.add(base);
                        }
                    } else {
                        tDisplayStacks.add(GT_Utility.copyOrNull(tStack));
                    }
                }
            }
            this.items = tDisplayStacks.toArray(new ItemStack[0]);
            if (this.items.length == 0) {
                this.items = new ItemStack[]{new ItemStack(Blocks.fire)};
            }
            this.permutated = true;
            setPermutationToRender(0);
        }
    }

    public class CachedDefaultRecipe extends TemplateRecipeHandler.CachedRecipe {
        public final GT_Recipe mRecipe;
        public final List<PositionedStack> mOutputs;
        public final List<PositionedStack> mInputs;

        // Draw a grid of fluids and items (in that order).
        public void drawNEIItemAndFluidGrid(ItemStack[] ItemArray, FluidStack[] FluidArray, int x_coord_origin, int y_coord_origin, int x_dir_max_items, int y_max_dir_max_items, GT_Recipe Recipe, boolean is_input) {
            if (ItemArray.length + FluidArray.length > x_dir_max_items * y_max_dir_max_items) {
                GT_Log.err.println("Recipe cannot be properly displayed in NEI due to too many items/fluids.");
            }

            int x_max = x_coord_origin + x_dir_max_items * 18;

            int x_coord = x_coord_origin;
            int y_coord = y_coord_origin;

            for(FluidStack fluid : FluidArray) {
                if (fluid != GT_Values.NF) {
                    if (is_input) {
                        this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(fluid, true), x_coord, y_coord, true));
                    } else {
                        this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(fluid, true), x_coord, y_coord, GT_NEI_DefaultHandler.this.mRecipeMap.mNEIUnificateOutput));
                    }
                    x_coord += 18;
                    if (x_coord == x_max) {
                        x_coord = x_coord_origin;
                        y_coord += 18;
                    }
                }
            }

            // Iterate over all items in array and display them.
            int special_counter = 0;
            for(ItemStack item : ItemArray) {
                if (item != GT_Values.NI) {
                    if (is_input) {
                        mInputs.add(new FixedPositionedStack(item, x_coord, y_coord, true));
                    } else {
                        mOutputs.add(new FixedPositionedStack(item, x_coord, y_coord, Recipe.getOutputChance(special_counter), GT_NEI_DefaultHandler.this.mRecipeMap.mNEIUnificateOutput));
                        special_counter++;
                    }
                    x_coord += 18;
                    if (x_coord == x_max) {
                        x_coord = x_coord_origin;
                        y_coord += 18;
                    }
                }
            }

        };


        // Draws a grid of items for NEI rendering.
        private void drawNEIItemGrid(ItemStack[] ItemArray, int x_coord_origin, int y_coord_origin, int x_dir_max_items, int y_max_dir_max_items, GT_Recipe Recipe, boolean is_input) {
            if (ItemArray.length > x_dir_max_items * y_max_dir_max_items) {
                GT_Log.err.println("Recipe cannot be properly displayed in NEI due to too many items.");
            }
            // 18 pixels to get to a new grid for placing a item tile since they are 16x16 and have 1 pixel buffers around them.
            int x_max = x_coord_origin + x_dir_max_items * 18;

            // Temp variables to keep track of current coords to place item at.
            int x_coord = x_coord_origin;
            int y_coord = y_coord_origin;

            // Iterate over all items in array and display them.
            int special_counter = 0;
            for(ItemStack item : ItemArray) {
                if (item != GT_Values.NI) {
                    if (is_input) {
                        mInputs.add(new FixedPositionedStack(item, x_coord, y_coord, true));
                    } else {
                        mOutputs.add(new FixedPositionedStack(item, x_coord, y_coord, Recipe.getOutputChance(special_counter), GT_NEI_DefaultHandler.this.mRecipeMap.mNEIUnificateOutput));
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

        // Draws a grid of fluids for NEI rendering.
        private void drawNEIFluidGrid(FluidStack[] FluidArray, int x_coord_origin, int y_coord_origin, int x_dir_max_fluids, int y_max_dir_max_fluids, GT_Recipe Recipe, boolean is_input) {

            if (FluidArray.length > x_dir_max_fluids * y_max_dir_max_fluids) {
                GT_Log.err.println("Recipe cannot be properly displayed in NEI due to too many fluids.");
            }

            // 18 pixels to get to a new grid for placing a fluid tile since they are 16x16 and have 1 pixel buffers around them.
            int x_max = x_coord_origin + x_dir_max_fluids * 18;

            // Temp variables to keep track of current coords to place fluid at.
            int x_coord = x_coord_origin;
            int y_coord = y_coord_origin;

            // Iterate over all fluids in array and display them.
            for(FluidStack fluid : FluidArray) {
                if (fluid != GT_Values.NF) {
                    if (is_input) {
                        this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(fluid, true), x_coord, y_coord, true));
                    } else {
                        this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(fluid, true), x_coord, y_coord, GT_NEI_DefaultHandler.this.mRecipeMap.mNEIUnificateOutput));
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
            List<PositionedStack> maybeIn;
            List<PositionedStack> maybeOut;

            try {
                maybeIn = aRecipe.getInputPositionedStacks();
            } catch (NullPointerException npe) {
                maybeIn = null;
                GT_Log.err.println("CachedDefaultRecipe - Invalid InputPositionedStacks " + aRecipe);
                npe.printStackTrace(GT_Log.err);
            }
            try {
                maybeOut = aRecipe.getOutputPositionedStacks();
            } catch (NullPointerException npe) {
                maybeOut = null;
                GT_Log.err.println("CachedDefaultRecipe - Invalid OutputPositionedStacks " + aRecipe);
                npe.printStackTrace(GT_Log.err);
            }

            if (maybeOut != null && maybeIn != null) {
                mOutputs = maybeOut;
                mInputs = maybeIn;
                return;
            }

            mOutputs = new ArrayList<>();
            mInputs = new ArrayList<>();

            // Default GT NEI handler for drawing fluids/items on screen.
            switch (GT_NEI_DefaultHandler.this.mRecipeMap.mUsualInputCount) {
                case 0:
                    break;
                case 1: // 1x1
                    drawNEIItemGrid(aRecipe.mInputs, 48, 14, 1, 1, aRecipe, true);
                    break;
                case 2: // 2x1
                    drawNEIItemGrid(aRecipe.mInputs, 30, 14, 2, 1, aRecipe, true);
                    break;
                case 3: //
                    drawNEIItemGrid(aRecipe.mInputs, 12, 14, 3, 1, aRecipe, true);
                    break;
                case 4:
                    drawNEIItemGrid(aRecipe.mInputs, 12, 14, 3, 2, aRecipe, true);
                    break;
                case 5:
                    drawNEIItemGrid(aRecipe.mInputs, 12, 14, 3, 2, aRecipe, true);
                    break;
                case 6:
                    drawNEIItemGrid(aRecipe.mInputs, 12, 5, 3, 2, aRecipe, true);
                    break;
                default:
                    drawNEIItemGrid(aRecipe.mInputs, 12, -4, 3, 3, aRecipe, true);
                }

            switch (GT_NEI_DefaultHandler.this.mRecipeMap.mUsualOutputCount) {
                case 0:
                    break;
                case 1:
                    drawNEIItemGrid(aRecipe.mOutputs, 102, 14, 1, 1, aRecipe, false);
                    break;
                case 2:
                    drawNEIItemGrid(aRecipe.mOutputs, 102, 14, 2, 1, aRecipe, false);
                    break;
                case 3:
                    drawNEIItemGrid(aRecipe.mOutputs, 102, 14, 3, 1, aRecipe, false);
                    break;
                case 4:
                    drawNEIItemGrid(aRecipe.mOutputs, 102, 5, 2, 2, aRecipe, false);
                    break;
                case 5:
                    drawNEIItemGrid(aRecipe.mOutputs, 102, 5, 3, 2, aRecipe, false);
                    break;
                case 6:
                    drawNEIItemGrid(aRecipe.mOutputs, 102, 5, 3, 2, aRecipe, false);
                    break;
                default:
                    drawNEIItemGrid(aRecipe.mOutputs, 102, -4, 3, 3, aRecipe, false);
                }

                // ??? No idea what this does. Leaving it alone.
                if (aRecipe.mSpecialItems != null) {
                    this.mInputs.add(new FixedPositionedStack(aRecipe.mSpecialItems, 120, 52));
                }

                if ((aRecipe.mFluidInputs.length > 0) && (aRecipe.mFluidInputs[0] != null) && (aRecipe.mFluidInputs[0].getFluid() != null)) {
                    this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidInputs[0], true), 48, 52));
                    if ((aRecipe.mFluidInputs.length > 1) && (aRecipe.mFluidInputs[1] != null) && (aRecipe.mFluidInputs[1].getFluid() != null)) {
                        this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidInputs[1], true), 30, 52));
                    }
                }

                if (aRecipe.mFluidOutputs.length > 1) {
                    if (aRecipe.mFluidOutputs[0] != null && (aRecipe.mFluidOutputs[0].getFluid() != null)) {
                        this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidOutputs[0], true), 120, 5));
                    }
                    if (aRecipe.mFluidOutputs[1] != null && (aRecipe.mFluidOutputs[1].getFluid() != null)) {
                        this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidOutputs[1], true), 138, 5));
                    }
                    if (aRecipe.mFluidOutputs.length > 2 && aRecipe.mFluidOutputs[2] != null && (aRecipe.mFluidOutputs[2].getFluid() != null)) {
                        this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidOutputs[2], true), 102, 23));
                    }
                    if (aRecipe.mFluidOutputs.length > 3 && aRecipe.mFluidOutputs[3] != null && (aRecipe.mFluidOutputs[3].getFluid() != null)) {
                        this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidOutputs[3], true), 120, 23));
                    }
                    if (aRecipe.mFluidOutputs.length > 4 && aRecipe.mFluidOutputs[4] != null && (aRecipe.mFluidOutputs[4].getFluid() != null)) {
                        this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidOutputs[4], true), 138, 23));
                    }
                } else if ((aRecipe.mFluidOutputs.length > 0) && (aRecipe.mFluidOutputs[0] != null) && (aRecipe.mFluidOutputs[0].getFluid() != null)) {
                    this.mOutputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidOutputs[0], true), 102, 52));
                }
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
        private Range<Integer>[] mTierIndexes;
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
            mTierIndexes = new Range[GT_Values.V.length];
            Iterator<CachedDefaultRecipe> iterator = mCachedRecipes.get().iterator();

            int index = 0;
            int minIndex = 0;
            int maxIndex = -1;
            byte previousTier = -1;
            byte lowestTier = 0;
            while(iterator.hasNext()) {
                CachedDefaultRecipe recipe = iterator.next();
                byte recipeTier = GT_Utility.getTier(recipe.mRecipe.mEUt / GT_NEI_DefaultHandler.this.mRecipeMap.mAmperage);
                if (recipeTier != previousTier) {
                    if (maxIndex != -1) {
                        mTierIndexes[previousTier] = Range.between(minIndex, maxIndex);
                    } else {
                        lowestTier = recipeTier;
                    }
                    minIndex = index;
                    previousTier = recipeTier;
                }
                maxIndex = index;
                index++;
                if (!iterator.hasNext()) {
                    mTierIndexes[recipeTier] = Range.between(minIndex, maxIndex);
                    mTierRange = Range.between(lowestTier, recipeTier);
                }
            }
        }

        private int getLowIndexForTier(byte lowerTier) {
            byte lowTier = (byte) Math.max(mTierRange.getMinimum(), lowerTier);
            while (mTierIndexes[lowTier] == null) {
                lowTier++;
            }
            return mTierIndexes[lowTier].getMinimum();
        }

        private int getHighIndexForTier(byte upperTier) {
            byte highTier = (byte) Math.min(mTierRange.getMaximum(), upperTier);
            while (mTierIndexes[highTier] == null) {
                highTier--;
            }
            return mTierIndexes[highTier].getMaximum();
        }
    }
}
