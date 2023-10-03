package com.github.technus.tectech.nei;

import static com.github.technus.tectech.recipe.TT_recipeAdder.nullItem;
import static gregtech.api.enums.ItemList.Display_Fluid;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import com.github.technus.tectech.Reference;
import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.recipe.TT_recipe;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.ItemList;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import cpw.mods.fml.common.event.FMLInterModComms;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

public class TT_NEI_ScannerHandler extends TemplateRecipeHandler {

    protected final TT_recipe.GT_Recipe_MapTT mRecipeMap;

    public TT_NEI_ScannerHandler(TT_recipe.GT_Recipe_MapTT aRecipeMap) {
        mRecipeMap = aRecipeMap;
        transferRects.add(new RecipeTransferRect(new Rectangle(65, 13, 36, 18), getOverlayIdentifier()));
        if (!NEI_TT_Config.sIsAdded) {
            FMLInterModComms.sendRuntimeMessage(
                    TecTech.instance,
                    "NEIPlugins",
                    "register-crafting-handler",
                    Reference.MODID + '@' + getRecipeName() + '@' + getOverlayIdentifier());
            GuiCraftingRecipe.craftinghandlers.add(this);
            GuiUsageRecipe.usagehandlers.add(this);
        }
    }

    public List<GT_Recipe> getSortedRecipes() {
        List<GT_Recipe> result = new ArrayList<>(mRecipeMap.mRecipeList);
        Collections.sort(result);
        return result;
    }

    public static void drawText(int aX, int aY, String aString, int aColor) {
        Minecraft.getMinecraft().fontRenderer.drawString(aString, aX, aY, aColor);
    }

    @Override
    public TemplateRecipeHandler newInstance() {
        NEI_TT_Config.TT_SH = new TT_NEI_ScannerHandler(mRecipeMap);
        return NEI_TT_Config.TT_SH;
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(getOverlayIdentifier())) {
            for (GT_Recipe tRecipe : getSortedRecipes()) {
                if (!tRecipe.mHidden) {
                    arecipes.add(new CachedDefaultRecipe(tRecipe));
                }
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
        if (tPrefixMaterial != null && !tPrefixMaterial.mBlackListed
                && !tPrefixMaterial.mPrefix.mFamiliarPrefixes.isEmpty()) {
            for (OrePrefixes tPrefix : tPrefixMaterial.mPrefix.mFamiliarPrefixes) {
                tResults.add(GT_OreDictUnificator.get(tPrefix, tPrefixMaterial.mMaterial.mMaterial, 1L));
            }
        }
        FluidStack tFluid = GT_Utility.getFluidForFilledItem(aResult, true);
        if (tFluid != null) {
            tResults.add(GT_Utility.getFluidDisplayStack(tFluid, false));
            for (FluidContainerRegistry.FluidContainerData tData : FluidContainerRegistry
                    .getRegisteredFluidContainerData()) {
                if (tData.fluid.isFluidEqual(tFluid)) {
                    tResults.add(GT_Utility.copy(tData.filledContainer));
                }
            }
        }
        for (GT_Recipe tRecipe : getSortedRecipes()) {
            if (!tRecipe.mHidden) {
                CachedDefaultRecipe tNEIRecipe = new CachedDefaultRecipe(tRecipe);
                for (ItemStack tStack : tResults) {
                    if (tNEIRecipe.contains(tNEIRecipe.mOutputs, tStack)) {
                        arecipes.add(tNEIRecipe);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack aInput) {
        ItemData tPrefixMaterial = GT_OreDictUnificator.getAssociation(aInput);

        ArrayList<ItemStack> tInputs = new ArrayList<>();
        tInputs.add(aInput);
        tInputs.add(GT_OreDictUnificator.get(false, aInput));
        if (tPrefixMaterial != null && !tPrefixMaterial.mPrefix.mFamiliarPrefixes.isEmpty()) {
            for (OrePrefixes tPrefix : tPrefixMaterial.mPrefix.mFamiliarPrefixes) {
                tInputs.add(GT_OreDictUnificator.get(tPrefix, tPrefixMaterial.mMaterial.mMaterial, 1L));
            }
        }
        FluidStack tFluid = GT_Utility.getFluidForFilledItem(aInput, true);
        if (tFluid != null) {
            tInputs.add(GT_Utility.getFluidDisplayStack(tFluid, false));
            for (FluidContainerRegistry.FluidContainerData tData : FluidContainerRegistry
                    .getRegisteredFluidContainerData()) {
                if (tData.fluid.isFluidEqual(tFluid)) {
                    tInputs.add(GT_Utility.copy(tData.filledContainer));
                }
            }
        }
        for (GT_Recipe tRecipe : getSortedRecipes()) {
            if (!tRecipe.mHidden) {
                CachedDefaultRecipe tNEIRecipe = new CachedDefaultRecipe(tRecipe);
                for (ItemStack tStack : tInputs) {
                    if (tNEIRecipe.contains(tNEIRecipe.mInputs, tStack)) {
                        arecipes.add(tNEIRecipe);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public String getOverlayIdentifier() {
        return mRecipeMap.mNEIName;
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
        return GT_LanguageManager.getTranslation(mRecipeMap.mUnlocalizedName);
    }

    @Override
    public String getGuiTexture() {
        // return "gregtech:textures/gui/" + this.mRecipeMap.mUnlocalizedName + ".png";
        return mRecipeMap.mNEIGUIPath;
    }

    @Override
    public List<String> handleItemTooltip(GuiRecipe<?> gui, ItemStack aStack, List<String> currenttip,
            int aRecipeIndex) {
        TemplateRecipeHandler.CachedRecipe tObject = arecipes.get(aRecipeIndex);
        if (tObject instanceof CachedDefaultRecipe) {
            CachedDefaultRecipe tRecipe = (CachedDefaultRecipe) tObject;
            for (PositionedStack tStack : tRecipe.mOutputs) {
                if (aStack == tStack.item) {
                    if (!(tStack instanceof FixedPositionedStack) || ((FixedPositionedStack) tStack).mChance <= 0
                            || ((FixedPositionedStack) tStack).mChance == 10000) {
                        break;
                    }
                    currenttip.add(
                            trans("150", "Chance: ") + ((FixedPositionedStack) tStack).mChance / 100
                                    + '.'
                                    + (((FixedPositionedStack) tStack).mChance % 100 < 10
                                            ? "0" + ((FixedPositionedStack) tStack).mChance % 100
                                            : Integer.valueOf(((FixedPositionedStack) tStack).mChance % 100))
                                    + '%');
                    break;
                }
            }
            for (PositionedStack tStack : tRecipe.mInputs) {
                if (aStack == tStack.item) {
                    if (Display_Fluid.isStackEqual(tStack.item, true, true) || tStack.item.stackSize != 0) {
                        break;
                    }
                    currenttip.add(trans("151", "Does not get consumed in the process"));
                    break;
                }
            }
        }
        return currenttip;
    }

    @Override
    public void drawExtras(int aRecipeIndex) {
        int tEUt = ((CachedDefaultRecipe) arecipes.get(aRecipeIndex)).mRecipe.mEUt;
        int computation = ((CachedDefaultRecipe) arecipes.get(aRecipeIndex)).mRecipe.mDuration;
        String[] recipeDesc = ((CachedDefaultRecipe) arecipes.get(aRecipeIndex)).mRecipe.getNeiDesc();
        if (recipeDesc == null) {
            int tSpecial = ((CachedDefaultRecipe) arecipes.get(aRecipeIndex)).mRecipe.mSpecialValue;
            short ampere = (short) (tSpecial & 0xFFFF), minComputationPerSec = (short) (tSpecial >>> 16);
            if (tEUt != 0) {
                drawText(
                        10,
                        73,
                        trans("152.1", "Max EU: ") + GT_Utility.formatNumbers(
                                (1 + (computation - minComputationPerSec) / minComputationPerSec) * (long) tEUt
                                        * ampere
                                        * 20)
                                + " EU",
                        -16777216);
                drawText(
                        10,
                        83,
                        trans("153", "Usage: ") + GT_Utility.formatNumbers((long) tEUt * ampere) + " EU/t",
                        -16777216);
                if (mRecipeMap.mShowVoltageAmperageInNEI) {
                    drawText(10, 93, trans("154", "Voltage: ") + GT_Utility.formatNumbers(tEUt) + " EU", -16777216);
                    drawText(10, 103, trans("155", "Amperage: ") + GT_Utility.formatNumbers(ampere), -16777216);
                } else {
                    drawText(10, 93, trans("156", "Voltage: unspecified"), -16777216);
                    drawText(10, 103, trans("157", "Amperage: unspecified"), -16777216);
                }
            }
            drawText(10, 113, "Computation: " + GT_Utility.formatNumbers(computation), -16777216);
            drawText(10, 123, "Min Computation: " + GT_Utility.formatNumbers(minComputationPerSec) + " /s", -16777216);
        } else {
            int i = 0;
            for (String descLine : recipeDesc) {
                drawText(10, 73 + 10 * i, descLine, -16777216);
                i++;
            }
        }
    }

    public static class FixedPositionedStack extends PositionedStack {

        public final int mChance;
        public boolean permutated = false;

        public FixedPositionedStack(Object object, int x, int y) {
            this(object, x, y, 0);
        }

        public FixedPositionedStack(Object object, int x, int y, int aChance) {
            super(object, x, y, true);
            mChance = aChance;
        }

        @Override
        public void generatePermutations() {
            if (permutated) {
                return;
            }
            ArrayList<ItemStack> tDisplayStacks = new ArrayList<>();
            for (ItemStack tStack : items) {
                if (GT_Utility.isStackValid(tStack)) {
                    if (tStack.getItemDamage() == 32767) {
                        List<ItemStack> permutations = ItemList.itemMap.get(tStack.getItem());
                        if (!permutations.isEmpty()) {
                            ItemStack stack;
                            for (Iterator<ItemStack> iterator = permutations.iterator(); iterator
                                    .hasNext(); tDisplayStacks.add(GT_Utility.copyAmount(tStack.stackSize, stack))) {
                                stack = iterator.next();
                            }
                        } else {
                            ItemStack base = new ItemStack(tStack.getItem(), tStack.stackSize);
                            base.stackTagCompound = tStack.stackTagCompound;
                            tDisplayStacks.add(base);
                        }
                    } else {
                        tDisplayStacks.add(GT_Utility.copy(tStack));
                    }
                }
            }
            items = tDisplayStacks.toArray(nullItem);
            if (items.length == 0) {
                items = new ItemStack[] { new ItemStack(Blocks.fire) };
            }
            permutated = true;
            setPermutationToRender(0);
        }
    }

    public class CachedDefaultRecipe extends TemplateRecipeHandler.CachedRecipe {

        public final GT_Recipe mRecipe;
        public final List<PositionedStack> mOutputs;
        public final List<PositionedStack> mInputs;

        public CachedDefaultRecipe(GT_Recipe aRecipe) {
            mRecipe = aRecipe;

            mOutputs = new ArrayList<>();
            mInputs = new ArrayList<>();

            int tStartIndex = 0;
            if (aRecipe.getRepresentativeInput(tStartIndex) != null) {
                mInputs.add(new FixedPositionedStack(aRecipe.getRepresentativeInput(tStartIndex), 48, 14 + 9));
            }
            if (aRecipe.mSpecialItems != null) {
                mInputs.add(new FixedPositionedStack(aRecipe.mSpecialItems, 120, 52));
            }
            if (aRecipe.getOutput(tStartIndex) != null) {
                mOutputs.add(
                        new FixedPositionedStack(
                                aRecipe.getOutput(tStartIndex),
                                102,
                                14 + 9,
                                aRecipe.getOutputChance(tStartIndex)));
            }
        }

        @Override
        public List<PositionedStack> getIngredients() {
            return getCycledIngredients(cycleticks / 10, mInputs);
        }

        @Override
        public PositionedStack getResult() {
            return null;
        }

        @Override
        public List<PositionedStack> getOtherStacks() {
            return mOutputs;
        }
    }

    public String trans(String aKey, String aEnglish) {
        return GT_LanguageManager.addStringLocalization("Interaction_DESCRIPTION_Index_" + aKey, aEnglish, false);
    }
}
