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
import cpw.mods.fml.common.event.FMLInterModComms;
import gregtech.GT_Mod;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.gui.GT_GUIContainer_BasicMachine;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_WithAlt;
import gregtech.api.util.GT_Utility;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static gregtech.api.util.GT_Utility.trans;

public class GT_NEI_AssLineHandler
        extends TemplateRecipeHandler {
    public static final int sOffsetX = 5;
    public static final int sOffsetY = 11;

    static {
        GuiContainerManager.addInputHandler(new GT_RectHandler());
        GuiContainerManager.addTooltipHandler(new GT_RectHandler());
    }

    protected final GT_Recipe.GT_Recipe_Map mRecipeMap;

    public GT_NEI_AssLineHandler(GT_Recipe.GT_Recipe_Map aRecipeMap) {//this is called when recipes should be shown
        this.mRecipeMap = aRecipeMap;
        this.transferRects.add(new RecipeTransferRect(new Rectangle(138, 18, 18, 18), getOverlayIdentifier(), new Object[0]));
        if (!NEI_GT_Config.sIsAdded) {
            FMLInterModComms.sendRuntimeMessage(GT_Values.GT, "NEIPlugins", "register-crafting-handler", "gregtech@" + getRecipeName() + "@" + getOverlayIdentifier());
            GuiCraftingRecipe.craftinghandlers.add(this);
            GuiUsageRecipe.usagehandlers.add(this);
        }
    }

    public static void drawText(int aX, int aY, String aString, int aColor) {
        Minecraft.getMinecraft().fontRenderer.drawString(aString, aX, aY, aColor);
    }

    public TemplateRecipeHandler newInstance() {
        NEI_GT_Config.ALH=new GT_NEI_AssLineHandler(this.mRecipeMap);
        return NEI_GT_Config.ALH;
    }

    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(getOverlayIdentifier())) {
            for (GT_Recipe tRecipe : this.mRecipeMap.mRecipeList) {
                if (!tRecipe.mHidden) {
                    this.arecipes.add(new CachedDefaultRecipe(tRecipe));
                }else{
                    this.arecipes.remove(new CachedDefaultRecipe(tRecipe));
                }
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    public void loadCraftingRecipes(ItemStack aResult) {
        ItemData tPrefixMaterial = GT_OreDictUnificator.getAssociation(aResult);

        ArrayList<ItemStack> tResults = new ArrayList();
        tResults.add(aResult);
        tResults.add(GT_OreDictUnificator.get(true, aResult));
        if ((tPrefixMaterial != null) && (!tPrefixMaterial.mBlackListed) && (!tPrefixMaterial.mPrefix.mFamiliarPrefixes.isEmpty())) {
            for (OrePrefixes tPrefix : tPrefixMaterial.mPrefix.mFamiliarPrefixes) {
                tResults.add(GT_OreDictUnificator.get(tPrefix, tPrefixMaterial.mMaterial.mMaterial, 1L));
            }
        }
        FluidStack tFluid = GT_Utility.getFluidForFilledItem(aResult, true);
        if (tFluid != null) {
            tResults.add(GT_Utility.getFluidDisplayStack(tFluid, false));
            for (FluidContainerRegistry.FluidContainerData tData : FluidContainerRegistry.getRegisteredFluidContainerData()) {
                if (tData.fluid.isFluidEqual(tFluid)) {
                    tResults.add(GT_Utility.copy(new Object[]{tData.filledContainer}));
                }
            }
        }
        for (GT_Recipe tRecipe : this.mRecipeMap.mRecipeList) {
            if (!tRecipe.mHidden) {
                CachedDefaultRecipe tNEIRecipe = new CachedDefaultRecipe(tRecipe);
                for (ItemStack tStack : tResults) {
                    if (tNEIRecipe.contains(tNEIRecipe.mOutputs, tStack)) {
                        this.arecipes.add(tNEIRecipe);
                        break;
                    }
                }
            }else{
                CachedDefaultRecipe tNEIRecipe = new CachedDefaultRecipe(tRecipe);
                for (ItemStack tStack : tResults) {
                    if (tNEIRecipe.contains(tNEIRecipe.mOutputs, tStack)) {
                        this.arecipes.remove(tNEIRecipe);
                        break;
                    }
                }
            }
        }
        CachedDefaultRecipe tNEIRecipe;
    }

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
            for (FluidContainerRegistry.FluidContainerData tData : FluidContainerRegistry.getRegisteredFluidContainerData()) {
                if (tData.fluid.isFluidEqual(tFluid)) {
                    tInputs.add(GT_Utility.copy(new Object[]{tData.filledContainer}));
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
            }else{
                CachedDefaultRecipe tNEIRecipe = new CachedDefaultRecipe(tRecipe);
                for (ItemStack tStack : tInputs) {
                    if (tNEIRecipe.contains(tNEIRecipe.mInputs, tStack)) {
                        this.arecipes.remove(tNEIRecipe);
                        break;
                    }
                }
            }
        }
    }

    public String getOverlayIdentifier() {
        return this.mRecipeMap.mNEIName;
    }

    public void drawBackground(int recipe) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuiDraw.changeTexture(getGuiTexture());
        GuiDraw.drawTexturedModalRect(-4, -8, 1, 3, 174, 79);
    }

    public int recipiesPerPage() {
        return 1;
    }

    public String getRecipeName() {
        return GT_LanguageManager.getTranslation(this.mRecipeMap.mUnlocalizedName);
    }

    public String getGuiTexture() {
        return this.mRecipeMap.mNEIGUIPath;
    }

    public List<String> handleItemTooltip(GuiRecipe gui, ItemStack aStack, List<String> currenttip, int aRecipeIndex) {
        CachedRecipe tObject = (CachedRecipe) this.arecipes.get(aRecipeIndex);
        if ((tObject instanceof CachedDefaultRecipe)) {
            CachedDefaultRecipe tRecipe = (CachedDefaultRecipe) tObject;
            for (PositionedStack tStack : tRecipe.mOutputs) {
                if (aStack == tStack.item) {
                    if ((!(tStack instanceof FixedPositionedStack)) || (((FixedPositionedStack) tStack).mChance <= 0) || (((FixedPositionedStack) tStack).mChance == 10000)) {
                        break;
                    }
                    currenttip.add("Chance: " + ((FixedPositionedStack) tStack).mChance / 100 + "." + (((FixedPositionedStack) tStack).mChance % 100 < 10 ? "0" + ((FixedPositionedStack) tStack).mChance % 100 : Integer.valueOf(((FixedPositionedStack) tStack).mChance % 100)) + "%");
                    break;
                }
            }
            for (PositionedStack tStack : tRecipe.mInputs) {
                if (aStack == tStack.item) {
                    if ((gregtech.api.enums.ItemList.Display_Fluid.isStackEqual(tStack.item, true, true)) ||
                            (tStack.item.stackSize != 0)) {
                        break;
                    }
                    currenttip.add("Does not get consumed in the process");
                    break;
                }
            }
        }
        return currenttip;
    }

    public void drawExtras(int aRecipeIndex) {
        int tEUt = ((CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe.mEUt;
        int tDuration = ((CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe.mDuration;
        String[] recipeDesc = ((CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe.getNeiDesc();
        if (recipeDesc == null) {
            if (tEUt != 0) {
                drawText(10, 73, trans("152","Total: ") + ((long)tDuration * tEUt) + " EU", -16777216);
                drawText(10, 83, trans("153","Usage: ") + tEUt + " EU/t", -16777216);
                if (this.mRecipeMap.mShowVoltageAmperageInNEI) {
                    drawText(10, 93, trans("154","Voltage: ") + tEUt / this.mRecipeMap.mAmperage + " EU", -16777216);
                    drawText(10, 103, trans("155","Amperage: ") + this.mRecipeMap.mAmperage, -16777216);
                } else {
                    drawText(10, 93, trans("156","Voltage: unspecified"), -16777216);
                    drawText(10, 103, trans("157","Amperage: unspecified"), -16777216);
                }
            }
            if (tDuration > 0) {
                drawText(10, 113, trans("158","Time: ")+String.format("%.2f " + trans("161"," secs"), 0.05F * tDuration), -16777216);
            }
            int tSpecial = ((CachedDefaultRecipe) this.arecipes.get(aRecipeIndex)).mRecipe.mSpecialValue;
            if (tSpecial == -100 && GT_Mod.gregtechproxy.mLowGravProcessing) {
                drawText(10, 123, trans("159","Needs Low Gravity"), -16777216);
            } else if (tSpecial == -200 && GT_Mod.gregtechproxy.mEnableCleanroom) {
                drawText(10, 123, trans("160","Needs Cleanroom"), -16777216);
            } else if ((GT_Utility.isStringValid(this.mRecipeMap.mNEISpecialValuePre)) || (GT_Utility.isStringValid(this.mRecipeMap.mNEISpecialValuePost))) {
                drawText(10, 123, this.mRecipeMap.mNEISpecialValuePre + tSpecial * this.mRecipeMap.mNEISpecialValueMultiplier + this.mRecipeMap.mNEISpecialValuePost, -16777216);
            }
        } else {
            int i = 0;
            for (String descLine : recipeDesc) {
                drawText(10, 73 + 10 * i, descLine, -16777216);
                i++;
            }
        }
    }

    public static class GT_RectHandler
            implements IContainerInputHandler, IContainerTooltipHandler {
        public boolean mouseClicked(GuiContainer gui, int mousex, int mousey, int button) {
            //if (canHandle(gui)) {
            //    if (button == 0) {
            //        return transferRect(gui, false);
            //    }
            //    if (button == 1) {
            //        return transferRect(gui, true);
            //    }
            //}
            return false;
        }

        public boolean lastKeyTyped(GuiContainer gui, char keyChar, int keyCode) {
            return false;
        }

        public boolean canHandle(GuiContainer gui) {
            return false;
            //return (((gui instanceof GT_GUIContainer_BasicMachine)) && (GT_Utility.isStringValid(((GT_GUIContainer_BasicMachine) gui).mNEI)));
        }

        public List<String> handleTooltip(GuiContainer gui, int mousex, int mousey, List<String> currenttip) {
            //if ((canHandle(gui)) && (currenttip.isEmpty())) {
            //    if (new Rectangle(138, 18, 18, 18).contains(new Point(GuiDraw.getMousePosition().x - ((GT_GUIContainer_BasicMachine) gui).getLeft() - codechicken.nei.recipe.RecipeInfo.getGuiOffset(gui)[0], GuiDraw.getMousePosition().y - ((GT_GUIContainer_BasicMachine) gui).getTop() - codechicken.nei.recipe.RecipeInfo.getGuiOffset(gui)[1]))) {
            //        currenttip.add("Recipes");
            //    }
            //}
            return currenttip;
        }

        private boolean transferRect(GuiContainer gui, boolean usage) {
            return (canHandle(gui)) && (new Rectangle(138, 18, 18, 18).contains(new Point(GuiDraw.getMousePosition().x - ((GT_GUIContainer_BasicMachine) gui).getLeft() - codechicken.nei.recipe.RecipeInfo.getGuiOffset(gui)[0], GuiDraw.getMousePosition().y - ((GT_GUIContainer_BasicMachine) gui).getTop() - codechicken.nei.recipe.RecipeInfo.getGuiOffset(gui)[1]))) && (usage ? GuiUsageRecipe.openRecipeGui(((GT_GUIContainer_BasicMachine) gui).mNEI, new Object[0]) : GuiCraftingRecipe.openRecipeGui(((GT_GUIContainer_BasicMachine) gui).mNEI, new Object[0]));

        }

        public List<String> handleItemDisplayName(GuiContainer gui, ItemStack itemstack, List<String> currenttip) {
            return currenttip;
        }

        public List<String> handleItemTooltip(GuiContainer gui, ItemStack itemstack, int mousex, int mousey, List<String> currenttip) {
            return currenttip;
        }

        public boolean keyTyped(GuiContainer gui, char keyChar, int keyCode) {
            return false;
        }

        public void onKeyTyped(GuiContainer gui, char keyChar, int keyID) {
        }

        public void onMouseClicked(GuiContainer gui, int mousex, int mousey, int button) {
        }

        public void onMouseUp(GuiContainer gui, int mousex, int mousey, int button) {
        }

        public boolean mouseScrolled(GuiContainer gui, int mousex, int mousey, int scrolled) {
            return false;
        }

        public void onMouseScrolled(GuiContainer gui, int mousex, int mousey, int scrolled) {
        }

        public void onMouseDragged(GuiContainer gui, int mousex, int mousey, int button, long heldTime) {
        }
    }

    public class FixedPositionedStack
            extends PositionedStack {
        public final int mChance;
        public boolean permutated = false;

        public FixedPositionedStack(Object object, int x, int y) {
            this(object, x, y, 0);
        }

        public FixedPositionedStack(Object object, int x, int y, int aChance) {
            super(object, x, y, true);
            this.mChance = aChance;
        }

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
                            for (Iterator i$ = permutations.iterator(); i$.hasNext(); tDisplayStacks.add(GT_Utility.copyAmount(tStack.stackSize, new Object[]{stack}))) {
                                stack = (ItemStack) i$.next();
                            }
                        } else {
                            ItemStack base = new ItemStack(tStack.getItem(), tStack.stackSize);
                            base.stackTagCompound = tStack.stackTagCompound;
                            tDisplayStacks.add(base);
                        }
                    } else {
                        tDisplayStacks.add(GT_Utility.copy(new Object[]{tStack}));
                    }
                }
            }
            this.items = ((ItemStack[]) tDisplayStacks.toArray(new ItemStack[0]));
            if (this.items.length == 0) {
                this.items = new ItemStack[]{new ItemStack(Blocks.fire)};
            }
            this.permutated = true;
            setPermutationToRender(0);
        }
    }

    public class CachedDefaultRecipe
            extends CachedRecipe {
        public final GT_Recipe mRecipe;
        public final List<PositionedStack> mOutputs = new ArrayList();
        public final List<PositionedStack> mInputs = new ArrayList();

        public CachedDefaultRecipe(GT_Recipe aRecipe) {
            super();
            this.mRecipe = aRecipe;

            for (int i = 0; i < 16; i++) {
            	Object obj = aRecipe instanceof GT_Recipe_WithAlt ? ((GT_Recipe_WithAlt) aRecipe).getAltRepresentativeInput(i) : aRecipe.getRepresentativeInput(i);
            	if (obj != null) {
            		this.mInputs.add(new FixedPositionedStack(obj, 18 * (i % 4) + 12, 18 * (i / 4)));
            	}
            }

            if (aRecipe.mSpecialItems != null) {
                this.mInputs.add(new FixedPositionedStack(aRecipe.mSpecialItems, 138, 36));
            }
            if (aRecipe.getOutput(0) != null) {
                this.mOutputs.add(new FixedPositionedStack(aRecipe.getOutput(0), 138, 0, aRecipe.getOutputChance(0)));
            }
            if ((aRecipe.mFluidInputs.length > 0) && (aRecipe.mFluidInputs[0] != null) && (aRecipe.mFluidInputs[0].getFluid() != null)) {
                this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidInputs[0], true), 102, 0));
                if ((aRecipe.mFluidInputs.length > 1) && (aRecipe.mFluidInputs[1] != null) && (aRecipe.mFluidInputs[1].getFluid() != null)) {
                    this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidInputs[1], true), 102, 18));
                    if ((aRecipe.mFluidInputs.length > 2) && (aRecipe.mFluidInputs[2] != null) && (aRecipe.mFluidInputs[2].getFluid() != null)) {
                        this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidInputs[2], true), 102, 36));
                        if ((aRecipe.mFluidInputs.length > 3) && (aRecipe.mFluidInputs[3] != null) && (aRecipe.mFluidInputs[3].getFluid() != null)) {
                            this.mInputs.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(aRecipe.mFluidInputs[3], true), 102, 54));
                        }
                    }
                }
            }
        }

        public List<PositionedStack> getIngredients() {
            return getCycledIngredients(GT_NEI_AssLineHandler.this.cycleticks / 10, this.mInputs);
        }

        public PositionedStack getResult() {
            return null;
        }

        public List<PositionedStack> getOtherStacks() {
            return this.mOutputs;
        }
    }
}
