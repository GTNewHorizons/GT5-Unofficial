package gtPlusPlus.nei;

import java.awt.Rectangle;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import cpw.mods.fml.common.event.FMLInterModComms;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.GasSpargingRecipe;
import gregtech.api.util.GasSpargingRecipeMap;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class GT_NEI_LFTR_Sparging extends TemplateRecipeHandler {

    public static final String mNEIName = GasSpargingRecipeMap.mNEIDisplayName;
    private SoftReference<List<GasSpargingRecipeNEI>> mCachedRecipes = null;

    public GT_NEI_LFTR_Sparging() {
        this.transferRects.add(
                new TemplateRecipeHandler.RecipeTransferRect(
                        new Rectangle(65, 13, 36, 18),
                        this.getOverlayIdentifier(),
                        new Object[0]));
        if (!NEI_GTPP_Config.sIsAdded) {
            FMLInterModComms.sendRuntimeMessage(
                    GT_Values.GT,
                    "NEIPlugins",
                    "register-crafting-handler",
                    "gregtechplusplus@" + this.getRecipeName() + "@" + this.getOverlayIdentifier());
            GuiCraftingRecipe.craftinghandlers.add(this);
            GuiUsageRecipe.usagehandlers.add(this);
        }
    }

    @Override
    public String getRecipeName() {
        return mNEIName;
    }

    @Override
    public String getGuiTexture() {
        return GasSpargingRecipeMap.mNEIGUIPath;
    }

    @Override
    public String getOverlayIdentifier() {
        return gregtech.api.util.GasSpargingRecipeMap.mUnlocalizedName;
    }

    @Override
    public int recipiesPerPage() {
        return 1;
    }

    @Override
    public void drawBackground(final int recipe) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuiDraw.changeTexture(this.getGuiTexture());
        GuiDraw.drawTexturedModalRect(-4, -8, 1, 3, 174, 68);
    }

    @Override
    public void loadTransferRects() {
        this.transferRects
                .add(new RecipeTransferRect(new Rectangle(72, 14, 22, 16), getOverlayIdentifier(), new Object[0]));
    }

    public List<GasSpargingRecipeNEI> getCache() {
        List<GasSpargingRecipeNEI> cache;
        if (mCachedRecipes == null || (cache = mCachedRecipes.get()) == null) {
            cache = GasSpargingRecipeMap.mRecipes.stream() // do not use parallel stream. This is already parallelized
                                                           // by NEI
                    .sorted().map(temp -> { return createCachedRecipe(temp); }).collect(Collectors.toList());
            // while the NEI parallelize handlers, for each individual handler it still uses sequential execution model
            // so we do not need any synchronization here
            mCachedRecipes = new SoftReference<>(cache);
        }
        return cache;
    }

    public GasSpargingRecipeNEI createCachedRecipe(GasSpargingRecipe aRecipe) {
        return new GasSpargingRecipeNEI(aRecipe);
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
        if ((tPrefixMaterial != null) && (!tPrefixMaterial.mBlackListed)
                && (!tPrefixMaterial.mPrefix.mFamiliarPrefixes.isEmpty())) {
            for (OrePrefixes tPrefix : tPrefixMaterial.mPrefix.mFamiliarPrefixes) {
                tResults.add(GT_OreDictUnificator.get(tPrefix, tPrefixMaterial.mMaterial.mMaterial, 1L));
            }
        }
        FluidStack tFluid = GT_Utility.getFluidForFilledItem(aResult, true);
        FluidStack tFluidStack;
        if (tFluid != null) {
            tFluidStack = tFluid;
            tResults.add(GT_Utility.getFluidDisplayStack(tFluid, false));
        } else tFluidStack = GT_Utility.getFluidFromDisplayStack(aResult);
        if (tFluidStack != null) {
            tResults.addAll(GT_Utility.getContainersFromFluid(tFluidStack));
        }
        for (GasSpargingRecipeNEI recipe : getCache()) {
            if (tResults.stream().anyMatch(stack -> recipe.contains(recipe.mOutputs, stack))) arecipes.add(recipe);
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
        FluidStack tFluid = GT_Utility.getFluidForFilledItem(aInput, true);
        FluidStack tFluidStack;
        if (tFluid != null) {
            tFluidStack = tFluid;
            tInputs.add(GT_Utility.getFluidDisplayStack(tFluid, false));
        } else tFluidStack = GT_Utility.getFluidFromDisplayStack(aInput);
        if (tFluidStack != null) {
            tInputs.addAll(GT_Utility.getContainersFromFluid(tFluidStack));
        }
        for (GasSpargingRecipeNEI recipe : getCache()) {
            if (tInputs.stream().anyMatch(stack -> recipe.contains(recipe.mInputs, stack))) arecipes.add(recipe);
        }
    }

    protected static void drawText(int aX, int aY, String aString, int aColor) {
        Minecraft.getMinecraft().fontRenderer.drawString(aString, aX, aY, aColor);
    }

    @Override
    public void drawExtras(int aRecipeIndex) {
        final long tEUt = ((GasSpargingRecipeNEI) this.arecipes.get(aRecipeIndex)).mRecipe.mEUt;
        final long tDuration = ((GasSpargingRecipeNEI) this.arecipes.get(aRecipeIndex)).mRecipe.mDuration;
        drawText(10, 73, "Total: " + MathUtils.formatNumbers((long) (tDuration * tEUt)) + " EU", -16777216);
        drawText(10, 83, "Usage: " + MathUtils.formatNumbers(tEUt) + " EU/t", -16777216);
        drawText(
                10,
                93,
                "Time: " + (tDuration < 20 ? "< 1" : MathUtils.formatNumbers(0.05d * tDuration)) + " secs",
                -16777216);
        drawText(10, 103, "Gas not used to sparge is", -16777216);
        drawText(10, 113, "returned alongside outputs.", -16777216);
    }

    @Override
    public List<String> handleItemTooltip(final GuiRecipe<?> gui, final ItemStack aStack, final List<String> currenttip,
            final int aRecipeIndex) {
        final TemplateRecipeHandler.CachedRecipe tObject = this.arecipes.get(aRecipeIndex);
        if ((tObject instanceof final GasSpargingRecipeNEI tRecipe)) {
            ItemStack aSpargeInput = tRecipe.mOutputs.get(0).item;
            ItemStack aSpentFuel = tRecipe.mOutputs.get(1).item;
            for (final PositionedStack tStack : tRecipe.mOutputs) {
                if (aStack == tStack.item) {
                    if (ItemList.Display_Fluid.isStackEqual(tStack.item, true, true)) {
                        if (GT_Utility.areStacksEqual(aStack, aSpentFuel, true)) {
                            break;
                        }
                        if (GT_Utility.areStacksEqual(aStack, aSpargeInput, true)) {
                            currenttip.add("The amount returned is the remainder after all other outputs.");
                        }
                        currenttip.add(
                                "Maximum Output: " + (((FixedPositionedStack) tStack).mChance / 100)
                                        + "."
                                        + ((((FixedPositionedStack) tStack).mChance % 100) < 10
                                                ? "0" + (((FixedPositionedStack) tStack).mChance % 100)
                                                : Integer.valueOf(((FixedPositionedStack) tStack).mChance % 100))
                                        + "L");
                        break;
                    }
                    break;
                }
            }
            for (final PositionedStack tStack : tRecipe.mInputs) {
                if (GT_Utility.areStacksEqual(aStack, tStack.item)) {
                    if ((gregtech.api.enums.ItemList.Display_Fluid.isStackEqual(tStack.item, true, true))
                            || (tStack.item.stackSize != 0)) {
                        break;
                    }
                    if (ItemUtils.isControlCircuit(aStack)) {
                        currenttip.add("Does not get consumed in the process");
                    }
                    break;
                }
            }
        }
        return currenttip;
    }

    public class FixedPositionedStack extends PositionedStack {

        public final int mChance;
        public boolean permutated = false;

        public FixedPositionedStack(final Object object, final int x, final int y) {
            this(object, x, y, 0);
        }

        public FixedPositionedStack(final Object object, final int x, final int y, final int aChance) {
            super(object, x, y, true);
            this.mChance = aChance;
        }

        @Override
        public void generatePermutations() {
            if (this.permutated) {
                return;
            }
            final ArrayList<ItemStack> tDisplayStacks = new ArrayList<>();
            for (final ItemStack tStack : this.items) {
                if (GT_Utility.isStackValid(tStack)) {
                    if (tStack.getItemDamage() == 32767) {
                        final List<ItemStack> permutations = codechicken.nei.ItemList.itemMap.get(tStack.getItem());
                        if (!permutations.isEmpty()) {
                            ItemStack stack;
                            for (final Iterator<ItemStack> i$ = permutations.iterator(); i$.hasNext(); tDisplayStacks
                                    .add(GT_Utility.copyAmount(tStack.stackSize, new Object[] { stack }))) {
                                stack = i$.next();
                            }
                        } else {
                            final ItemStack base = new ItemStack(tStack.getItem(), tStack.stackSize);
                            base.stackTagCompound = tStack.stackTagCompound;
                            tDisplayStacks.add(base);
                        }
                    } else {
                        tDisplayStacks.add(GT_Utility.copy(new Object[] { tStack }));
                    }
                }
            }
            this.items = (tDisplayStacks.toArray(new ItemStack[0]));
            if (this.items.length == 0) {
                this.items = new ItemStack[] { new ItemStack(Blocks.fire) };
            }
            this.permutated = true;
            this.setPermutationToRender(0);
        }
    }

    public class GasSpargingRecipeNEI extends CachedRecipe implements Comparable<CachedRecipe> {

        public final GasSpargingRecipe mRecipe;
        public final List<PositionedStack> mOutputs = new ArrayList<>();
        public final List<PositionedStack> mInputs = new ArrayList<>();

        public GasSpargingRecipeNEI(GasSpargingRecipe tRecipe) {
            super();
            this.mRecipe = tRecipe;
            int tStartIndex = 0;
            if (tRecipe.mFluidInputs.length > 0) {
                if ((tRecipe.mFluidInputs[0] != null) && (tRecipe.mFluidInputs[0].getFluid() != null)) {
                    this.mInputs.add(
                            new FixedPositionedStack(
                                    GT_Utility.getFluidDisplayStack(tRecipe.mFluidInputs[0], true),
                                    30,
                                    5));
                }
                if ((tRecipe.mFluidInputs.length > 1) && (tRecipe.mFluidInputs[1] != null)
                        && (tRecipe.mFluidInputs[1].getFluid() != null)) {
                    this.mInputs.add(
                            new FixedPositionedStack(
                                    GT_Utility.getFluidDisplayStack(tRecipe.mFluidInputs[1], true),
                                    12,
                                    5));
                }
                if ((tRecipe.mFluidInputs.length > 2) && (tRecipe.mFluidInputs[2] != null)
                        && (tRecipe.mFluidInputs[2].getFluid() != null)) {
                    this.mInputs.add(
                            new FixedPositionedStack(
                                    GT_Utility.getFluidDisplayStack(tRecipe.mFluidInputs[2], true),
                                    48,
                                    5));
                }
                if ((tRecipe.mFluidInputs.length > 3) && (tRecipe.mFluidInputs[3] != null)
                        && (tRecipe.mFluidInputs[3].getFluid() != null)) {
                    this.mInputs.add(
                            new FixedPositionedStack(
                                    GT_Utility.getFluidDisplayStack(tRecipe.mFluidInputs[3], true),
                                    12,
                                    23));
                }
                if ((tRecipe.mFluidInputs.length > 4) && (tRecipe.mFluidInputs[4] != null)
                        && (tRecipe.mFluidInputs[4].getFluid() != null)) {
                    this.mInputs.add(
                            new FixedPositionedStack(
                                    GT_Utility.getFluidDisplayStack(tRecipe.mFluidInputs[4], true),
                                    30,
                                    23));
                }
                if ((tRecipe.mFluidInputs.length > 5) && (tRecipe.mFluidInputs[5] != null)
                        && (tRecipe.mFluidInputs[5].getFluid() != null)) {
                    this.mInputs.add(
                            new FixedPositionedStack(
                                    GT_Utility.getFluidDisplayStack(tRecipe.mFluidInputs[5], true),
                                    48,
                                    23));
                }
                if ((tRecipe.mFluidInputs.length > 6) && (tRecipe.mFluidInputs[6] != null)
                        && (tRecipe.mFluidInputs[6].getFluid() != null)) {
                    this.mInputs.add(
                            new FixedPositionedStack(
                                    GT_Utility.getFluidDisplayStack(tRecipe.mFluidInputs[6], true),
                                    12,
                                    41));
                }
                if ((tRecipe.mFluidInputs.length > 7) && (tRecipe.mFluidInputs[7] != null)
                        && (tRecipe.mFluidInputs[7].getFluid() != null)) {
                    this.mInputs.add(
                            new FixedPositionedStack(
                                    GT_Utility.getFluidDisplayStack(tRecipe.mFluidInputs[7], true),
                                    30,
                                    41));
                }
                if ((tRecipe.mFluidInputs.length > 8) && (tRecipe.mFluidInputs[8] != null)
                        && (tRecipe.mFluidInputs[8].getFluid() != null)) {
                    this.mInputs.add(
                            new FixedPositionedStack(
                                    GT_Utility.getFluidDisplayStack(tRecipe.mFluidInputs[8], true),
                                    48,
                                    41));
                }
            }

            tStartIndex = 0;
            if (tRecipe.mFluidOutputs.length > 0) {
                if ((tRecipe.mFluidOutputs[0] != null) && (tRecipe.mFluidOutputs[0].getFluid() != null)) {
                    this.mOutputs.add(
                            new FixedPositionedStack(
                                    GT_Utility.getFluidDisplayStack(tRecipe.mFluidOutputs[0], false),
                                    120,
                                    5,
                                    tRecipe.getMaxOutput(tStartIndex++)));
                }
                if ((tRecipe.mFluidOutputs.length > 1) && (tRecipe.mFluidOutputs[1] != null)
                        && (tRecipe.mFluidOutputs[1].getFluid() != null)) {
                    this.mOutputs.add(
                            new FixedPositionedStack(
                                    GT_Utility.getFluidDisplayStack(tRecipe.mFluidOutputs[1], true),
                                    102,
                                    5,
                                    tRecipe.getMaxOutput(tStartIndex++)));
                }
                if ((tRecipe.mFluidOutputs.length > 2) && (tRecipe.mFluidOutputs[2] != null)
                        && (tRecipe.mFluidOutputs[2].getFluid() != null)) {
                    this.mOutputs.add(
                            new FixedPositionedStack(
                                    GT_Utility.getFluidDisplayStack(tRecipe.mFluidOutputs[2], false),
                                    138,
                                    5,
                                    tRecipe.getMaxOutput(tStartIndex++)));
                }
                if ((tRecipe.mFluidOutputs.length > 3) && (tRecipe.mFluidOutputs[3] != null)
                        && (tRecipe.mFluidOutputs[3].getFluid() != null)) {
                    this.mOutputs.add(
                            new FixedPositionedStack(
                                    GT_Utility.getFluidDisplayStack(tRecipe.mFluidOutputs[3], false),
                                    102,
                                    23,
                                    tRecipe.getMaxOutput(tStartIndex++)));
                }
                if ((tRecipe.mFluidOutputs.length > 4) && (tRecipe.mFluidOutputs[4] != null)
                        && (tRecipe.mFluidOutputs[4].getFluid() != null)) {
                    this.mOutputs.add(
                            new FixedPositionedStack(
                                    GT_Utility.getFluidDisplayStack(tRecipe.mFluidOutputs[4], false),
                                    120,
                                    23,
                                    tRecipe.getMaxOutput(tStartIndex++)));
                }
                if ((tRecipe.mFluidOutputs.length > 5) && (tRecipe.mFluidOutputs[5] != null)
                        && (tRecipe.mFluidOutputs[5].getFluid() != null)) {
                    this.mOutputs.add(
                            new FixedPositionedStack(
                                    GT_Utility.getFluidDisplayStack(tRecipe.mFluidOutputs[5], false),
                                    138,
                                    23,
                                    tRecipe.getMaxOutput(tStartIndex++)));
                }
                if ((tRecipe.mFluidOutputs.length > 6) && (tRecipe.mFluidOutputs[6] != null)
                        && (tRecipe.mFluidOutputs[6].getFluid() != null)) {
                    this.mOutputs.add(
                            new FixedPositionedStack(
                                    GT_Utility.getFluidDisplayStack(tRecipe.mFluidOutputs[6], false),
                                    102,
                                    41,
                                    tRecipe.getMaxOutput(tStartIndex++)));
                }
                if ((tRecipe.mFluidOutputs.length > 7) && (tRecipe.mFluidOutputs[7] != null)
                        && (tRecipe.mFluidOutputs[7].getFluid() != null)) {
                    this.mOutputs.add(
                            new FixedPositionedStack(
                                    GT_Utility.getFluidDisplayStack(tRecipe.mFluidOutputs[7], false),
                                    120,
                                    41,
                                    tRecipe.getMaxOutput(tStartIndex++)));
                }
                if ((tRecipe.mFluidOutputs.length > 8) && (tRecipe.mFluidOutputs[8] != null)
                        && (tRecipe.mFluidOutputs[8].getFluid() != null)) {
                    this.mOutputs.add(
                            new FixedPositionedStack(
                                    GT_Utility.getFluidDisplayStack(tRecipe.mFluidOutputs[8], false),
                                    138,
                                    41,
                                    tRecipe.getMaxOutput(tStartIndex++)));
                }
            }
        }

        @Override
        public int compareTo(CachedRecipe o) {
            boolean b = GasSpargingRecipeNEI.class.isInstance(o);
            if (b) {
                GasSpargingRecipeNEI p = (GasSpargingRecipeNEI) o;
                if (p.mOutputs.size() > this.mOutputs.size()) {
                    return 1;
                } else if (p.mOutputs.size() == this.mOutputs.size()) {
                    return 0;
                } else {
                    return -1;
                }
            }
            return 0;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj != null) {
                if (GasSpargingRecipeNEI.class.isInstance(obj)) {
                    GasSpargingRecipeNEI p = (GasSpargingRecipeNEI) obj;
                    if (p != null) {
                        if (GT_Utility.areStacksEqual(p.mInputs.get(0).item, this.mInputs.get(0).item, true)) {
                            if (p.mOutputs.size() == this.mOutputs.size()) {
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }

        @Override
        public List<PositionedStack> getIngredients() {
            return this.getCycledIngredients(GT_NEI_LFTR_Sparging.this.cycleticks / 10, this.mInputs);
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
