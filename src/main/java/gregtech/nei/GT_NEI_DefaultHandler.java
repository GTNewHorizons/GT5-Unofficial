package gregtech.nei;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.guihook.GuiContainerManager;
import codechicken.nei.guihook.IContainerInputHandler;
import codechicken.nei.guihook.IContainerTooltipHandler;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import gregtech.GT_Mod;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.gui.GT_GUIContainer;
import gregtech.api.gui.GT_GUIContainer_BasicMachine;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.gui.GT_GUIContainer_FusionReactor;
import gregtech.common.gui.GT_GUIContainer_PrimitiveBlastFurnace;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import static codechicken.nei.recipe.RecipeInfo.getGuiOffset;

public class GT_NEI_DefaultHandler extends RecipeMapHandler {
    public static final int sOffsetX = 5;
    public static final int sOffsetY = 11;
    private static final ConcurrentMap<GT_Recipe.GT_Recipe_Map, SortedRecipeListCache> CACHE = new ConcurrentHashMap<>();

    static {
        GuiContainerManager.addInputHandler(new GT_RectHandler());
        GuiContainerManager.addTooltipHandler(new GT_RectHandler());
    }

    public GT_NEI_DefaultHandler(GT_Recipe.GT_Recipe_Map aRecipeMap) {
        super(aRecipeMap);
        this.transferRects.add(new RecipeTransferRect(new Rectangle(65, 13, 36, 18), getOverlayIdentifier()));
    }

    @Deprecated
    public List<GT_Recipe> getSortedRecipes() {
        List<GT_Recipe> result = new ArrayList<>(this.mRecipeMap.mRecipeList);
        Collections.sort(result);
        return result;
    }

    public List<CachedDefaultRecipe> getCache() {
        SortedRecipeListCache cacheHolder = CACHE.computeIfAbsent(mRecipeMap, m -> new SortedRecipeListCache());
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
            arecipes.addAll(getCache());
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
    public int recipiesPerPage() {
        return 1;
    }

    @Override
    public String getRecipeName() {
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
        return currenttip;
    }

    @Override
    public void drawExtras(int aRecipeIndex) {
        int tEUt = ((CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe.mEUt;
        int tDuration = ((CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe.mDuration;
        String[] recipeDesc = ((CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe.getNeiDesc();
        if (recipeDesc == null) {
            if (tEUt != 0) {
                drawText(10, 73, trans("152","Total: ") + GT_Utility.formatNumbers((long) tDuration * tEUt) + " EU", 0xFF000000);
                drawText(10, 83, trans("153","Usage: ") + GT_Utility.formatNumbers(tEUt) + " EU/t", 0xFF000000);
                if (this.mRecipeMap.mShowVoltageAmperageInNEI) {
                    int voltage = tEUt / this.mRecipeMap.mAmperage;
                    byte tier = GT_Utility.getTier(voltage);
                    if (tier < 0 || tier >= 16) {
                        drawText(10, 93, trans("154","Voltage: ") + GT_Utility.formatNumbers(voltage) + " EU", 0xFFFF0000);
//add here gt logger
                    } else {
                        drawText(10, 93, trans("154","Voltage: ") + GT_Utility.formatNumbers(voltage) + " EU (" + GT_Values.VN[tier] + ")", 0xFF000000);
                    }
                    drawText(10, 103, trans("155","Amperage: ") + GT_Utility.formatNumbers(this.mRecipeMap.mAmperage), 0xFF000000);
                } else {
                    drawText(10, 93, trans("156","Voltage: unspecified"), 0xFF000000);
                    drawText(10, 103, trans("157","Amperage: unspecified"), 0xFF000000);
                }
            }
            if (tDuration > 0) {
                drawText(10, 113, trans("158","Time: ") + GT_Utility.formatNumbers(0.05d * tDuration) + trans("161"," secs"), 0xFF000000);
            }
            int tSpecial = ((CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe.mSpecialValue;
            if (tSpecial == -100 && GT_Mod.gregtechproxy.mLowGravProcessing) {
                drawText(10, 123, trans("159","Needs Low Gravity"), 0xFF000000);
            } else if (tSpecial == -200 && GT_Mod.gregtechproxy.mEnableCleanroom) {
                drawText(10, 123, trans("160","Needs Cleanroom"), 0xFF000000);
            } else if (tSpecial == -201) {
                drawText(10, 123, trans("206","Scan for Assembly Line"), 0xFF000000);
            } else if (tSpecial == -300 && GT_Mod.gregtechproxy.mEnableCleanroom) {
                drawText(10, 123, trans("160","Needs Cleanroom & LowGrav"), 0xFF000000);
            } else if (tSpecial == -400) {
                drawText(10, 123, trans("216","Deprecated Recipe"), 0xFF000000);
            } else if ((GT_Utility.isStringValid(this.mRecipeMap.mNEISpecialValuePre)) || (GT_Utility.isStringValid(this.mRecipeMap.mNEISpecialValuePost))) {
                drawText(10, 123, this.mRecipeMap.mNEISpecialValuePre + GT_Utility.formatNumbers(tSpecial * this.mRecipeMap.mNEISpecialValueMultiplier) + this.mRecipeMap.mNEISpecialValuePost, 0xFF000000);
            }
        } else {
            int i = 0;
            for (String descLine : recipeDesc) {
                drawText(10, 73 + 10 * i, descLine, 0xFF000000);
                i++;
            }
        }
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
            return (gui instanceof GT_GUIContainer_BasicMachine && GT_Utility.isStringValid(((GT_GUIContainer_BasicMachine) gui).mNEI))
                    || (gui instanceof GT_GUIContainer_FusionReactor && GT_Utility.isStringValid(((GT_GUIContainer_FusionReactor) gui).mNEI))
                    || (gui instanceof GT_GUIContainer_PrimitiveBlastFurnace && GT_Utility.isStringValid(((GT_GUIContainer_PrimitiveBlastFurnace) gui).mNEI));
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
        public void onKeyTyped(GuiContainer gui, char keyChar, int keyID) {
        }

        @Override
        public void onMouseClicked(GuiContainer gui, int mousex, int mousey, int button) {
        }

        @Override
        public void onMouseUp(GuiContainer gui, int mousex, int mousey, int button) {
        }

        @Override
        public boolean mouseScrolled(GuiContainer gui, int mousex, int mousey, int scrolled) {
            return false;
        }

        @Override
        public void onMouseScrolled(GuiContainer gui, int mousex, int mousey, int scrolled) {
        }

        @Override
        public void onMouseDragged(GuiContainer gui, int mousex, int mousey, int button, long heldTime) {
        }
    }

    public static class FixedPositionedStack extends PositionedStack {
        public final int mChance;
        public boolean permutated = false;

        public FixedPositionedStack(Object object, int x, int y) {
            this(object, x, y, 0);
        }

        public FixedPositionedStack(Object object, int x, int y, int aChance) {
            super(GT_OreDictUnificator.getNonUnifiedStacks(object), x, y, true);
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

    public class CachedDefaultRecipe
            extends TemplateRecipeHandler.CachedRecipe {
        public final GT_Recipe mRecipe;
        public final List<PositionedStack> mOutputs;
        public final List<PositionedStack> mInputs;

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

            if (maybeIn != null && maybeOut != null) {
                mInputs = maybeIn;
                mOutputs = maybeOut;
                return;
            }

            mOutputs = new ArrayList<>();
            mInputs = new ArrayList<>();

            int tStartIndex = 0;
            switch (GT_NEI_DefaultHandler.this.mRecipeMap.mUsualInputCount) {
                case 0:
                    break;
                case 1:
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 48, 14));
                    }
                    tStartIndex++;
                    break;
                case 2:
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 30, 14));
                    }
                    tStartIndex++;
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 48, 14));
                    }
                    tStartIndex++;
                    break;
                case 3:
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 12, 14));
                    }
                    tStartIndex++;
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 30, 14));
                    }
                    tStartIndex++;
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 48, 14));
                    }
                    tStartIndex++;
                    break;
                case 4:
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 30, 5));
                    }
                    tStartIndex++;
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 48, 5));
                    }
                    tStartIndex++;
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 30, 23));
                    }
                    tStartIndex++;
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 48, 23));
                    }
                    tStartIndex++;
                    break;
                case 5:
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 12, 5));
                    }
                    tStartIndex++;
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 30, 5));
                    }
                    tStartIndex++;
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 48, 5));
                    }
                    tStartIndex++;
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 30, 23));
                    }
                    tStartIndex++;
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 48, 23));
                    }
                    tStartIndex++;
                    break;
                case 6:
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 12, 5));
                    }
                    tStartIndex++;
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 30, 5));
                    }
                    tStartIndex++;
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 48, 5));
                    }
                    tStartIndex++;
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 12, 23));
                    }
                    tStartIndex++;
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 30, 23));
                    }
                    tStartIndex++;
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 48, 23));
                    }
                    tStartIndex++;
                    break;
                case 7:
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 12, -4));
                    }
                    tStartIndex++;
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 30, -4));
                    }
                    tStartIndex++;
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 48, -4));
                    }
                    tStartIndex++;
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 12, 14));
                    }
                    tStartIndex++;
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 30, 14));
                    }
                    tStartIndex++;
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 48, 14));
                    }
                    tStartIndex++;
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 12, 32));
                    }
                    tStartIndex++;
                    break;
                case 8:
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 12, -4));
                    }
                    tStartIndex++;
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 30, -4));
                    }
                    tStartIndex++;
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 48, -4));
                    }
                    tStartIndex++;
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 12, 14));
                    }
                    tStartIndex++;
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 30, 14));
                    }
                    tStartIndex++;
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 48, 14));
                    }
                    tStartIndex++;
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 12, 32));
                    }
                    tStartIndex++;
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 30, 32));
                    }
                    tStartIndex++;
                    break;
                default:
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 12, -4));
                    }
                    tStartIndex++;
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 30, -4));
                    }
                    tStartIndex++;
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 48, -4));
                    }
                    tStartIndex++;
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 12, 14));
                    }
                    tStartIndex++;
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 30, 14));
                    }
                    tStartIndex++;
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 48, 14));
                    }
                    tStartIndex++;
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 12, 32));
                    }
                    tStartIndex++;
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 30, 32));
                    }
                    tStartIndex++;
                    if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                        this.mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 48, 32));
                    }
                    tStartIndex++;
            }
            if (aRecipe.mSpecialItems != null) {
                this.mInputs.add(new FixedPositionedStack(aRecipe.mSpecialItems, 120, 52));
            }
            tStartIndex = 0;
            switch (GT_NEI_DefaultHandler.this.mRecipeMap.mUsualOutputCount) {
                case 0:
                    break;
                case 1:
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 102, 14, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
                    break;
                case 2:
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 102, 14, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 120, 14, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
                    break;
                case 3:
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 102, 14, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 120, 14, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 138, 14, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
                    break;
                case 4:
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 102, 5, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 120, 5, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 102, 23, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 120, 23, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
                    break;
                case 5:
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 102, 5, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 120, 5, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 138, 5, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 102, 23, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 120, 23, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
                    break;
                case 6:
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 102, 5, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 120, 5, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 138, 5, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 102, 23, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 120, 23, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 138, 23, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
                    break;
                case 7:
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 102, -4, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 120, -4, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 138, -4, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 102, 14, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 120, 14, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 138, 14, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 102, 32, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
                    break;
                case 8:
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 102, -4, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 120, -4, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 138, -4, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 102, 14, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 120, 14, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 138, 14, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 102, 32, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 120, 32, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
                    break;
                default:
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 102, -4, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 120, -4, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 138, -4, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 102, 14, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 120, 14, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 138, 14, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 102, 32, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 120, 32, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
                    if (aRecipe.getOutput(tStartIndex) != null) {
                        this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(tStartIndex), 138, 32, aRecipe.getOutputChance(tStartIndex)));
                    }
                    tStartIndex++;
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
            return getCycledIngredients(GT_NEI_DefaultHandler.this.cycleticks / 10, this.mInputs);
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

    private static class SortedRecipeListCache {
        private int mCachedRecipesVersion = -1;
        @Nullable
        private SoftReference<List<CachedDefaultRecipe>> mCachedRecipes;

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
    }
}
