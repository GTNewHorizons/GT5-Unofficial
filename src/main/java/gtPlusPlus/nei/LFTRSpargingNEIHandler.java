package gtPlusPlus.nei;

import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.util.GTRecipeConstants.SPARGE_MAX_BYPRODUCT;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.spargeTowerRecipes;

import java.awt.Rectangle;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import cpw.mods.fml.common.event.FMLInterModComms;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class LFTRSpargingNEIHandler extends TemplateRecipeHandler {

    public static final String mNEIName = StatCollector.translateToLocal("gtpp.recipe.lftr.sparging");
    private SoftReference<List<GasSpargingRecipeNEI>> mCachedRecipes = null;

    public LFTRSpargingNEIHandler() {
        this.transferRects.add(
            new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(65, 13, 36, 18), this.getOverlayIdentifier()));
        if (!NEIGTPPConfig.sIsAdded) {
            FMLInterModComms.sendRuntimeMessage(
                GTValues.GT,
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
        return GregTech.getResourcePath("textures", "gui/basicmachines/FissionFuel.png");
    }

    @Override
    public String getOverlayIdentifier() {
        return "gtpp.recipe.lftr.sparging";
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
        this.transferRects.add(new RecipeTransferRect(new Rectangle(72, 14, 22, 16), getOverlayIdentifier()));
    }

    public List<GasSpargingRecipeNEI> getCache() {
        List<GasSpargingRecipeNEI> cache;
        if (mCachedRecipes == null || (cache = mCachedRecipes.get()) == null) {
            cache = spargeTowerRecipes.getAllRecipes()
                .stream() // do not use parallel stream. This is already parallelized
                // by NEI
                .sorted()
                .map(temp -> createCachedRecipe(temp))
                .collect(Collectors.toList());
            // while the NEI parallelize handlers, for each individual handler it still uses sequential execution model
            // so we do not need any synchronization here
            mCachedRecipes = new SoftReference<>(cache);
        }
        return cache;
    }

    public GasSpargingRecipeNEI createCachedRecipe(GTRecipe aRecipe) {
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
        FluidStack tFluid = GTUtility.getFluidForFilledItem(aResult, true);
        FluidStack tFluidStack;
        if (tFluid != null) {
            tFluidStack = tFluid;
            tResults.add(GTUtility.getFluidDisplayStack(tFluid, false));
        } else tFluidStack = GTUtility.getFluidFromDisplayStack(aResult);
        if (tFluidStack != null) {
            tResults.addAll(GTUtility.getContainersFromFluid(tFluidStack));
        }
        for (GasSpargingRecipeNEI recipe : getCache()) {
            if (tResults.stream()
                .anyMatch(stack -> recipe.contains(recipe.mOutputs, stack))) arecipes.add(recipe);
        }
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
        FluidStack tFluid = GTUtility.getFluidForFilledItem(aInput, true);
        FluidStack tFluidStack;
        if (tFluid != null) {
            tFluidStack = tFluid;
            tInputs.add(GTUtility.getFluidDisplayStack(tFluid, false));
        } else tFluidStack = GTUtility.getFluidFromDisplayStack(aInput);
        if (tFluidStack != null) {
            tInputs.addAll(GTUtility.getContainersFromFluid(tFluidStack));
        }
        for (GasSpargingRecipeNEI recipe : getCache()) {
            if (tInputs.stream()
                .anyMatch(stack -> recipe.contains(recipe.mInputs, stack))) arecipes.add(recipe);
        }
    }

    protected static void drawText(int aX, int aY, String aString, int aColor) {
        Minecraft.getMinecraft().fontRenderer.drawString(aString, aX, aY, aColor);
    }

    @Override
    public void drawExtras(int aRecipeIndex) {
        final long tEUt = ((GasSpargingRecipeNEI) this.arecipes.get(aRecipeIndex)).mRecipe.mEUt;
        final long tDuration = ((GasSpargingRecipeNEI) this.arecipes.get(aRecipeIndex)).mRecipe.mDuration;
        drawText(10, 73, "Total: " + MathUtils.formatNumbers(tDuration * tEUt) + " EU", -16777216);
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
            ItemStack aSpargeInput = tRecipe.mOutputs.get(1).item;
            ItemStack aSpentFuel = tRecipe.mOutputs.get(0).item;
            for (final PositionedStack tStack : tRecipe.mOutputs) {
                if (aStack == tStack.item) {
                    if (ItemList.Display_Fluid.isStackEqual(tStack.item, true, true)) {
                        if (GTUtility.areStacksEqual(aStack, aSpentFuel, true)) {
                            break;
                        }
                        if (GTUtility.areStacksEqual(aStack, aSpargeInput, true)) {
                            currenttip.add("The amount returned is the remainder after all other outputs.");
                        }
                        currenttip.add(
                                "Maximum Output: " + (((FixedPositionedStack) tStack).mChance)
                                        + "L");
                        break;
                    }
                    break;
                }
            }
            for (final PositionedStack tStack : tRecipe.mInputs) {
                if (GTUtility.areStacksEqual(aStack, tStack.item)) {
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

    public static class FixedPositionedStack extends PositionedStack {

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
                if (GTUtility.isStackValid(tStack)) {
                    if (tStack.getItemDamage() == 32767) {
                        final List<ItemStack> permutations = codechicken.nei.ItemList.itemMap.get(tStack.getItem());
                        if (!permutations.isEmpty()) {
                            ItemStack stack;
                            for (final Iterator<ItemStack> i$ = permutations.iterator(); i$.hasNext(); tDisplayStacks
                                .add(GTUtility.copyAmount(tStack.stackSize, new Object[] { stack }))) {
                                stack = i$.next();
                            }
                        } else {
                            final ItemStack base = new ItemStack(tStack.getItem(), tStack.stackSize);
                            base.stackTagCompound = tStack.stackTagCompound;
                            tDisplayStacks.add(base);
                        }
                    } else {
                        tDisplayStacks.add(GTUtility.copy(new Object[] { tStack }));
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

        public final GTRecipe mRecipe;
        public final List<PositionedStack> mOutputs = new ArrayList<>();
        public final List<PositionedStack> mInputs = new ArrayList<>();

        public GasSpargingRecipeNEI(GTRecipe tRecipe) {
            super();
            this.mRecipe = tRecipe;
            if (tRecipe.mFluidInputs.length > 0) {
                for (int i = 0; i < tRecipe.mFluidInputs.length; i++) {
                    if (tRecipe.mFluidInputs[i] == null) break;
                    this.mInputs.add(
                        new FixedPositionedStack(
                            GTUtility.getFluidDisplayStack(tRecipe.mFluidInputs[i], true),
                            12 + ((i % 3) * 18),
                            5 + ((i / 3) * 18)));
                }
            }

            if (tRecipe.mFluidOutputs.length > 0) {
                for (int i = 0; i < tRecipe.mFluidOutputs.length; i++) {
                    if (tRecipe.mFluidOutputs[i] == null) break;
                    this.mOutputs.add(
                        new FixedPositionedStack(
                            GTUtility.getFluidDisplayStack(tRecipe.mFluidOutputs[i], i == 0),
                            102 + ((i % 3) * 18),
                            5 + ((i / 3) * 18),
                            i == 1 ? tRecipe.mFluidInputs[0].amount : tRecipe.getMetadata(SPARGE_MAX_BYPRODUCT)));

                }
            }
        }

        @Override
        public int compareTo(CachedRecipe o) {
            if (o instanceof GasSpargingRecipeNEI p) {
                return Integer.compare(p.mOutputs.size(), this.mOutputs.size());
            }
            return 0;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj != null) {
                if (obj instanceof GasSpargingRecipeNEI p) {
                    if (GTUtility.areStacksEqual(p.mInputs.get(0).item, this.mInputs.get(0).item, true)) {
                        return p.mOutputs.size() == this.mOutputs.size();
                    }
                }
            }
            return false;
        }

        @Override
        public List<PositionedStack> getIngredients() {
            return this.getCycledIngredients(LFTRSpargingNEIHandler.this.cycleticks / 10, this.mInputs);
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
