package gregtech.nei;

import static gregtech.api.util.GT_Utility.trans;

import codechicken.nei.NEIClientUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.gtnewhorizons.modularui.api.GlStateManager;
import com.gtnewhorizons.modularui.api.UIInfos;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import gregtech.GT_Mod;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_WithAlt;
import gregtech.api.util.GT_Utility;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

public class GT_NEI_AssLineHandler extends RecipeMapHandler {
    public static final int sOffsetX = 5;
    public static final int sOffsetY = 11;

    /**
     * Static version of {@link TemplateRecipeHandler#cycleticks}.
     * Can be referenced from cached recipes.
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
    protected static final Pos2d WINDOW_OFFSET = new Pos2d(-5, -11);

    private String mRecipeName;

    public GT_NEI_AssLineHandler(GT_Recipe.GT_Recipe_Map aRecipeMap) { // this is called when recipes should be shown
        super(aRecipeMap);
        this.transferRects.add(new RecipeTransferRect(new Rectangle(138, 18, 18, 18), getOverlayIdentifier()));

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

    public List<GT_Recipe> getSortedRecipes() {
        List<GT_Recipe> result = new ArrayList<>(this.mRecipeMap.mRecipeList);
        Collections.sort(result);
        return result;
    }

    @Override
    public TemplateRecipeHandler newInstance() {
        NEI_GT_Config.ALH = new GT_NEI_AssLineHandler(this.mRecipeMap);
        return NEI_GT_Config.ALH;
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(getOverlayIdentifier())) {
            for (GT_Recipe tRecipe : getSortedRecipes()) {
                if (!tRecipe.mHidden) {
                    this.arecipes.add(new CachedDefaultRecipe(tRecipe));
                } else {
                    this.arecipes.remove(new CachedDefaultRecipe(tRecipe));
                }
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack aResult) {
        ItemData tPrefixMaterial = GT_OreDictUnificator.getAssociation(aResult);

        ArrayList<ItemStack> tResults = new ArrayList();
        tResults.add(aResult);
        tResults.add(GT_OreDictUnificator.get(true, aResult));
        if ((tPrefixMaterial != null)
                && (!tPrefixMaterial.mBlackListed)
                && (!tPrefixMaterial.mPrefix.mFamiliarPrefixes.isEmpty())) {
            for (OrePrefixes tPrefix : tPrefixMaterial.mPrefix.mFamiliarPrefixes) {
                tResults.add(GT_OreDictUnificator.get(tPrefix, tPrefixMaterial.mMaterial.mMaterial, 1L));
            }
        }
        FluidStack tFluid = GT_Utility.getFluidForFilledItem(aResult, true);
        if (tFluid != null) {
            tResults.add(GT_Utility.getFluidDisplayStack(tFluid, false));
            for (FluidContainerRegistry.FluidContainerData tData :
                    FluidContainerRegistry.getRegisteredFluidContainerData()) {
                if (tData.fluid.isFluidEqual(tFluid)) {
                    tResults.add(GT_Utility.copyOrNull(tData.filledContainer));
                }
            }
        }
        for (GT_Recipe tRecipe : getSortedRecipes()) {
            if (!tRecipe.mHidden) {
                CachedDefaultRecipe tNEIRecipe = new CachedDefaultRecipe(tRecipe);
                for (ItemStack tStack : tResults) {
                    if (tNEIRecipe.contains(tNEIRecipe.mOutputs, tStack)) {
                        this.arecipes.add(tNEIRecipe);
                        break;
                    }
                }
            } else {
                CachedDefaultRecipe tNEIRecipe = new CachedDefaultRecipe(tRecipe);
                for (ItemStack tStack : tResults) {
                    if (tNEIRecipe.contains(tNEIRecipe.mOutputs, tStack)) {
                        this.arecipes.remove(tNEIRecipe);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack aInput) {
        ItemData tPrefixMaterial = GT_OreDictUnificator.getAssociation(aInput);

        ArrayList<ItemStack> tInputs = new ArrayList();
        tInputs.add(aInput);
        tInputs.add(GT_OreDictUnificator.get(false, aInput));
        if ((tPrefixMaterial != null) && (!tPrefixMaterial.mPrefix.mFamiliarPrefixes.isEmpty())) {
            for (OrePrefixes tPrefix : tPrefixMaterial.mPrefix.mFamiliarPrefixes) {
                tInputs.add(GT_OreDictUnificator.get(tPrefix, tPrefixMaterial.mMaterial.mMaterial, 1L));
            }
        }
        FluidStack tFluid = GT_Utility.getFluidForFilledItem(aInput, true);
        if (tFluid != null) {
            tInputs.add(GT_Utility.getFluidDisplayStack(tFluid, false));
            for (FluidContainerRegistry.FluidContainerData tData :
                    FluidContainerRegistry.getRegisteredFluidContainerData()) {
                if (tData.fluid.isFluidEqual(tFluid)) {
                    tInputs.add(GT_Utility.copyOrNull(tData.filledContainer));
                }
            }
        }
        for (GT_Recipe tRecipe : this.mRecipeMap.mRecipeList) {
            if (!tRecipe.mHidden) {
                CachedDefaultRecipe tNEIRecipe = new CachedDefaultRecipe(tRecipe);
                for (ItemStack tStack : tInputs) {
                    if (tNEIRecipe.contains(tNEIRecipe.mInputs, tStack)) {
                        this.arecipes.add(tNEIRecipe);
                        break;
                    }
                }
            } else {
                CachedDefaultRecipe tNEIRecipe = new CachedDefaultRecipe(tRecipe);
                for (ItemStack tStack : tInputs) {
                    if (tNEIRecipe.contains(tNEIRecipe.mInputs, tStack)) {
                        this.arecipes.remove(tNEIRecipe);
                        break;
                    }
                }
            }
        }
        CachedDefaultRecipe tNEIRecipe;
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
            mRecipeName = GT_LanguageManager.getTranslation(this.mRecipeMap.mUnlocalizedName);
            updateOverrideTextColor();
        }
        return mRecipeName;
    }

    @Override
    public String getGuiTexture() {
        return this.mRecipeMap.mNEIGUIPath;
    }

    @Override
    public List<String> handleItemTooltip(
            GuiRecipe<?> gui, ItemStack aStack, List<String> currenttip, int aRecipeIndex) {
        CachedRecipe tObject = (CachedRecipe) this.arecipes.get(aRecipeIndex);
        if ((tObject instanceof CachedDefaultRecipe)) {
            CachedDefaultRecipe tRecipe = (CachedDefaultRecipe) tObject;
            for (PositionedStack tStack : tRecipe.mOutputs) {
                if (aStack == tStack.item) {
                    if ((!(tStack instanceof FixedPositionedStack))
                            || (((FixedPositionedStack) tStack).mChance <= 0)
                            || (((FixedPositionedStack) tStack).mChance == 10000)) {
                        break;
                    }
                    currenttip.add("Chance: " + ((FixedPositionedStack) tStack).mChance / 100 + "."
                            + (((FixedPositionedStack) tStack).mChance % 100 < 10
                                    ? "0" + ((FixedPositionedStack) tStack).mChance % 100
                                    : Integer.valueOf(((FixedPositionedStack) tStack).mChance % 100))
                            + "%");
                    break;
                }
            }
            for (PositionedStack tStack : tRecipe.mInputs) {
                if (aStack == tStack.item) {
                    if ((gregtech.api.enums.ItemList.Display_Fluid.isStackEqual(tStack.item, true, true))
                            || (tStack.item.stackSize != 0)) {
                        break;
                    }
                    currenttip.add("Does not get consumed in the process");
                    break;
                }
            }
        }
        return currenttip;
    }

    @Override
    public void drawExtras(int aRecipeIndex) {
        GT_Recipe recipe = ((CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe;
        int tEUt = recipe.mEUt;
        int tDuration = recipe.mDuration;
        String[] recipeDesc = recipe.getNeiDesc();
        if (recipeDesc == null) {
            if (tEUt != 0) {
                drawText(
                        10,
                        77,
                        trans("152", "Total: ") + GT_Utility.formatNumbers((long) tDuration * tEUt) + " EU",
                        0xFF000000);
                drawText(10, 87, trans("153", "Usage: ") + GT_Utility.formatNumbers(tEUt) + " EU/t", 0xFF000000);
                if (this.mRecipeMap.mShowVoltageAmperageInNEI) {
                    int voltage = tEUt / this.mRecipeMap.mAmperage;
                    byte tier = GT_Utility.getTier(voltage);
                    if (tier < 0 || tier >= 16) {
                        drawText(
                                10,
                                97,
                                trans("154", "Voltage: ") + GT_Utility.formatNumbers(voltage) + " EU",
                                0xFF000000);
                    } else {
                        drawText(
                                10,
                                97,
                                trans("154", "Voltage: ") + GT_Utility.formatNumbers(voltage) + " EU ("
                                        + GT_Values.VN[tier] + ")",
                                0xFF000000);
                    }
                    drawText(
                            10,
                            107,
                            trans("155", "Amperage: ") + GT_Utility.formatNumbers(this.mRecipeMap.mAmperage),
                            0xFF000000);
                } else {
                    drawText(10, 97, trans("156", "Voltage: unspecified"), 0xFF000000);
                    drawText(10, 107, trans("157", "Amperage: unspecified"), 0xFF000000);
                }
            }
            if (tDuration > 0) {
                drawText(
                        10,
                        117,
                        trans("158", "Time: ") + GT_Utility.formatNumbers(0.05d * tDuration) + trans("161", " secs"),
                        0xFF000000);
            }
            int tSpecial = ((CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe.mSpecialValue;
            boolean specialDrew = false;
            if (tSpecial == -100 && GT_Mod.gregtechproxy.mLowGravProcessing) {
                drawText(10, 127, trans("159", "Needs Low Gravity"), 0xFF000000);
                specialDrew = true;
            } else if (tSpecial == -200 && GT_Mod.gregtechproxy.mEnableCleanroom) {
                drawText(10, 127, trans("160", "Needs Cleanroom"), 0xFF000000);
                specialDrew = true;
            } else if (tSpecial == -201) {
                drawText(10, 127, trans("206", "Scan for Assembly Line"), 0xFF000000);
                specialDrew = true;
            } else if ((GT_Utility.isStringValid(this.mRecipeMap.mNEISpecialValuePre))
                    || (GT_Utility.isStringValid(this.mRecipeMap.mNEISpecialValuePost))) {
                drawText(
                        10,
                        127,
                        this.mRecipeMap.mNEISpecialValuePre
                                + GT_Utility.formatNumbers(tSpecial * this.mRecipeMap.mNEISpecialValueMultiplier)
                                + this.mRecipeMap.mNEISpecialValuePost,
                        0xFF000000);
                specialDrew = true;
            }
            int y = 127 + (specialDrew ? 10 : 0);
            if (GT_Mod.gregtechproxy.mNEIRecipeOwner) {
                if (recipe.owners.size() > 1) {
                    drawText(
                            10,
                            y,
                            EnumChatFormatting.ITALIC
                                    + GT_Utility.trans("273", "Original Recipe by: ")
                                    + recipe.owners.get(0).getName(),
                            0xFF000000);
                    y += 10;
                    for (int i = 1; i < recipe.owners.size(); i++) {
                        drawText(
                                10,
                                y,
                                EnumChatFormatting.ITALIC
                                        + GT_Utility.trans("274", "Modified by: ")
                                        + recipe.owners.get(i).getName(),
                                0xFF000000);
                        y += 10;
                    }
                } else if (recipe.owners.size() > 0) {
                    drawText(
                            10,
                            y,
                            EnumChatFormatting.ITALIC
                                    + GT_Utility.trans("272", "Recipe by: ")
                                    + recipe.owners.get(0).getName(),
                            0xFF000000);
                    y += 10;
                }
            }
            if (GT_Mod.gregtechproxy.mNEIRecipeOwnerStackTrace
                    && recipe.stackTraces != null
                    && !recipe.stackTraces.isEmpty()) {
                drawText(10, y, "stackTrace:", 0xFF000000);
                y += 10;
                for (StackTraceElement stackTrace : recipe.stackTraces.get(0)) {
                    drawText(10, y, stackTrace.toString(), 0xFF000000);
                    y += 10;
                }
            }
        } else {
            int i = 0;
            for (String descLine : recipeDesc) {
                drawText(10, 77 + 10 * i, descLine, 0xFF000000);
                i++;
            }
        }
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
            widget.draw(0);
            GlStateManager.popMatrix();
        }
    }

    public static int getDrawTicks() {
        return drawTicks;
    }

    public class FixedPositionedStack extends PositionedStack {
        public final int mChance;
        public boolean permutated = false;

        public FixedPositionedStack(Object object, int x, int y) {
            this(object, x, y, 0);
        }

        public FixedPositionedStack(Object object, int x, int y, int aChance) {
            super(object, x, y, true);
            this.mChance = aChance;
        }

        @Override
        public void generatePermutations() {
            if (this.permutated) {
                return;
            }
            ArrayList<ItemStack> tDisplayStacks = new ArrayList();
            for (ItemStack tStack : this.items) {
                if (GT_Utility.isStackValid(tStack)) {
                    if (tStack.getItemDamage() == 32767) {
                        List<ItemStack> permutations = codechicken.nei.ItemList.itemMap.get(tStack.getItem());
                        if (!permutations.isEmpty()) {
                            ItemStack stack;
                            for (Iterator i$ = permutations.iterator();
                                    i$.hasNext();
                                    tDisplayStacks.add(GT_Utility.copyAmount(tStack.stackSize, new Object[] {stack}))) {
                                stack = (ItemStack) i$.next();
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
            this.items = ((ItemStack[]) tDisplayStacks.toArray(new ItemStack[0]));
            if (this.items.length == 0) {
                this.items = new ItemStack[] {new ItemStack(Blocks.fire)};
            }
            this.permutated = true;
            setPermutationToRender(0);
        }
    }

    public class CachedDefaultRecipe extends CachedRecipe {
        public final GT_Recipe mRecipe;
        public final List<PositionedStack> mOutputs = new ArrayList();
        public final List<PositionedStack> mInputs = new ArrayList();

        public CachedDefaultRecipe(GT_Recipe aRecipe) {
            super();
            this.mRecipe = aRecipe;

            for (Widget child : modularWindow.getChildren()) {
                if (child instanceof SlotWidget) {
                    SlotWidget widget = (SlotWidget) child;
                    if (widget.getMcSlot().getItemHandler() == itemInputsInventory) {
                        int i = widget.getMcSlot().getSlotIndex();
                        Object obj = aRecipe instanceof GT_Recipe_WithAlt
                                ? ((GT_Recipe_WithAlt) aRecipe).getAltRepresentativeInput(i)
                                : aRecipe.getRepresentativeInput(i);
                        if (obj != null) {
                            this.mInputs.add(
                                    new FixedPositionedStack(obj, widget.getPos().x + 1, widget.getPos().y + 1));
                        }
                    } else if (widget.getMcSlot().getItemHandler() == itemOutputsInventory) {
                        int i = widget.getMcSlot().getSlotIndex();
                        if (aRecipe.mOutputs.length > i && aRecipe.mOutputs[i] != null) {
                            mOutputs.add(new GT_NEI_DefaultHandler.FixedPositionedStack(
                                    aRecipe.mOutputs[i],
                                    widget.getPos().x + 1,
                                    widget.getPos().y + 1,
                                    aRecipe.getOutputChance(i)));
                        }
                    } else if (widget.getMcSlot().getItemHandler() == specialSlotInventory) {
                        if (aRecipe.mSpecialItems != null) {
                            mInputs.add(new GT_NEI_DefaultHandler.FixedPositionedStack(
                                    aRecipe.mSpecialItems, widget.getPos().x + 1, widget.getPos().y + 1));
                        }
                    } else if (widget.getMcSlot().getItemHandler() == fluidInputsInventory) {
                        int i = widget.getMcSlot().getSlotIndex();
                        if (aRecipe.mFluidInputs.length > i
                                && aRecipe.mFluidInputs[i] != null
                                && aRecipe.mFluidInputs[i].getFluid() != null) {
                            mInputs.add(new GT_NEI_DefaultHandler.FixedPositionedStack(
                                    GT_Utility.getFluidDisplayStack(aRecipe.mFluidInputs[i], true),
                                    widget.getPos().x + 1,
                                    widget.getPos().y + 1));
                        }
                    } else if (widget.getMcSlot().getItemHandler() == fluidOutputsInventory) {
                        int i = widget.getMcSlot().getSlotIndex();
                        if (aRecipe.mFluidOutputs.length > i
                                && aRecipe.mFluidOutputs[i] != null
                                && aRecipe.mFluidOutputs[i].getFluid() != null) {
                            mOutputs.add(new GT_NEI_DefaultHandler.FixedPositionedStack(
                                    GT_Utility.getFluidDisplayStack(aRecipe.mFluidOutputs[i], true),
                                    widget.getPos().x + 1,
                                    widget.getPos().y + 1));
                        }
                    }
                }
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
}
