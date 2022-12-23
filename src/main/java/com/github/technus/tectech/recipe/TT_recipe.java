package com.github.technus.tectech.recipe;

import static gregtech.api.enums.GT_Values.RES_PATH_GUI;

import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.IEMDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.core.maps.EMConstantStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.maps.EMInstanceStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.maps.IEMMapRead;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.IEMStack;
import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.util.GT_Recipe;
import gregtech.common.gui.modularui.UIHelper;
import java.util.*;
import java.util.function.Supplier;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class TT_recipe extends GT_Recipe {
    public static final String E_RECIPE_ID = "eRecipeID";
    public final EMConstantStackMap[] input;
    public final IEMMapRead<? extends IEMStack>[] output;
    public final EMConstantStackMap[] eCatalyst;
    public final IAdditionalCheck additionalCheck;

    public TT_recipe(
            boolean aOptimize,
            ItemStack[] aInputs,
            ItemStack[] aOutputs,
            Object aSpecialItems,
            int[] aChances,
            FluidStack[] aFluidInputs,
            FluidStack[] aFluidOutputs,
            int aDuration,
            int aEUt,
            int aSpecialValue,
            EMConstantStackMap[] in,
            IEMMapRead<? extends IEMStack>[] out,
            EMConstantStackMap[] catalyst,
            IAdditionalCheck check) {
        super(
                aOptimize,
                aInputs,
                aOutputs,
                aSpecialItems,
                aChances,
                aFluidInputs,
                aFluidOutputs,
                aDuration,
                aEUt,
                aSpecialValue);
        input = in;
        output = out;
        eCatalyst = catalyst;
        additionalCheck = check;
    }

    public boolean EMisRecipeInputEqual(
            boolean consume, boolean doNotCheckStackSizes, ItemStack[] itemStacks, FluidStack[] fluidStacks) {
        return EMisRecipeInputEqual(consume, doNotCheckStackSizes, itemStacks, fluidStacks, null, null);
    }

    public boolean EMisRecipeInputEqual(
            boolean consume,
            boolean doNotCheckStackSizes,
            ItemStack[] itemStacks,
            FluidStack[] fluidStacks,
            EMInstanceStackMap[] in) {
        return EMisRecipeInputEqual(consume, doNotCheckStackSizes, itemStacks, fluidStacks, in, null);
    }

    public boolean EMisRecipeInputEqual(
            boolean consume,
            boolean doNotCheckStackSizes,
            ItemStack[] itemStacks,
            FluidStack[] fluidStacks,
            EMInstanceStackMap[] in,
            EMInstanceStackMap[] catalyst) {
        if (additionalCheck != null
                && !additionalCheck.check(this, consume, doNotCheckStackSizes, itemStacks, fluidStacks, in, catalyst)) {
            return false;
        }
        if (eCatalyst != null) {
            if (catalyst != null && catalyst.length >= eCatalyst.length) {
                for (int i = 0; i < eCatalyst.length; i++) {
                    if (eCatalyst[i] != null && eCatalyst[i].hasStacks()) {
                        if (catalyst[i] != null && catalyst[i].hasStacks()) {
                            if (!catalyst[i].containsAllAmounts(eCatalyst[i])) {
                                return false;
                            }
                        } else {
                            return false;
                        }
                    }
                }
            } else {
                return false;
            }
        }
        if (input != null) {
            if (in != null && in.length >= input.length) {
                for (int i = 0; i < input.length; i++) {
                    if (input[i] != null && input[i].hasStacks()) {
                        if (in[i] != null && in[i].hasStacks()) {
                            if (consume) {
                                if (!in[i].removeAllAmounts(input[i])) {
                                    return false;
                                }
                            } else {
                                if (!in[i].containsAllAmounts(input[i])) {
                                    return false;
                                }
                            }
                        } else {
                            return false;
                        }
                    }
                }
            } else {
                return false;
            }
        }
        return super.isRecipeInputEqual(consume, doNotCheckStackSizes, fluidStacks, itemStacks);
    }

    @Deprecated
    public boolean EMisRecipeInputEqualConsumeFromOne(
            boolean consume,
            boolean doNotCheckStackSizes,
            ItemStack[] itemStacks,
            FluidStack[] fluidStacks,
            EMInstanceStackMap in) {
        return EMisRecipeInputEqualConsumeFromOne(consume, doNotCheckStackSizes, itemStacks, fluidStacks, in, null);
    }

    @Deprecated
    public boolean EMisRecipeInputEqualConsumeFromOne(
            boolean consume,
            boolean doNotCheckStackSizes,
            ItemStack[] itemStacks,
            FluidStack[] fluidStacks,
            EMInstanceStackMap in,
            EMInstanceStackMap[] catalyst) {
        if (additionalCheck != null
                && !additionalCheck.check(this, consume, doNotCheckStackSizes, itemStacks, fluidStacks, in, catalyst)) {
            return false;
        }
        if (eCatalyst != null) {
            if (catalyst != null && catalyst.length >= eCatalyst.length) {
                for (int i = 0; i < eCatalyst.length; i++) {
                    if (eCatalyst[i] != null && eCatalyst[i].hasStacks()) {
                        if (catalyst[i] != null && catalyst[i].hasStacks()) {
                            if (!catalyst[i].containsAllAmounts(eCatalyst[i])) {
                                return false;
                            }
                        } else {
                            return false;
                        }
                    }
                }
            } else {
                return false;
            }
        }
        if (input != null) {
            if (in != null) {
                for (EMConstantStackMap anInput : input) {
                    if (anInput != null && anInput.hasStacks()) {
                        if (in.hasStacks()) {
                            if (consume) {
                                if (!in.removeAllAmounts(anInput)) {
                                    return false;
                                }
                            } else {
                                if (!in.containsAllAmounts(anInput)) {
                                    return false;
                                }
                            }
                        } else {
                            return false;
                        }
                    }
                }
            } else {
                return false;
            }
        }
        return super.isRecipeInputEqual(consume, doNotCheckStackSizes, fluidStacks, itemStacks);
    }

    public interface IAdditionalCheck {
        boolean check(
                TT_recipe thisRecipe,
                boolean consume,
                boolean doNotCheckStackSizes,
                ItemStack[] itemStacks,
                FluidStack[] fluidStacks,
                EMInstanceStackMap[] in,
                EMInstanceStackMap[] e);

        boolean check(
                TT_recipe thisRecipe,
                boolean consume,
                boolean doNotCheckStackSizes,
                ItemStack[] itemStacks,
                FluidStack[] fluidStacks,
                EMInstanceStackMap in,
                EMInstanceStackMap[] e);
    }

    public static class TT_Recipe_Map<T extends TT_recipe> {
        public static TT_Recipe_Map<TT_assLineRecipe> sCrafterRecipes = new TT_Recipe_Map<>();
        public static TT_Recipe_Map<TT_assLineRecipe> sMachineRecipes = new TT_Recipe_Map<>();

        private final HashMap<String, T> mRecipeMap;

        public TT_Recipe_Map() {
            mRecipeMap = new HashMap<>(16);
        }

        public T findRecipe(String identifier) {
            return mRecipeMap.get(identifier);
        }

        public T findRecipe(ItemStack dataHandler) {
            if (dataHandler == null || dataHandler.stackTagCompound == null) {
                return null;
            }
            return mRecipeMap.get(dataHandler.stackTagCompound.getString(E_RECIPE_ID));
        }

        public void add(T recipe) {
            GameRegistry.UniqueIdentifier uid = GameRegistry.findUniqueIdentifierFor(recipe.mOutputs[0].getItem());
            mRecipeMap.put(uid + ":" + recipe.mOutputs[0].getItemDamage(), recipe);
        }

        public Collection<T> recipeList() {
            return mRecipeMap.values();
        }
    }

    public static class GT_Recipe_MapTT extends GT_Recipe.GT_Recipe_Map {

        public static final GT_Recipe_Map sEyeofHarmonyRecipes = new Eye_Of_Harmony_Recipe_Map(
                        new HashSet<>(250),
                        "gt.recipe.eyeofharmony",
                        "Eye of Harmony",
                        null,
                        RES_PATH_GUI + "basicmachines/Extractor",
                        1,
                        9 * 10,
                        1,
                        0,
                        1,
                        "",
                        1,
                        "",
                        true,
                false) // Custom NEI handler means this must be false.
                .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.DOWN)
                .setUsualFluidOutputCount(18)
                .setLogoPos(10,10);

        public static GT_Recipe_MapTT sResearchableFakeRecipes = new GT_Recipe_MapTT(
                new HashSet<>(32),
                "gt.recipe.researchStation",
                "Research station",
                null,
                "gregtech:textures/gui/multimachines/ResearchFake",
                1,
                1,
                1,
                0,
                1,
                "",
                1,
                "",
                true,
                false); // nei to false - using custom handler
        public static GT_Recipe_MapTT sScannableFakeRecipes = new GT_Recipe_MapTT(
                new HashSet<>(32),
                "gt.recipe.em_scanner",
                "EM Scanner Research",
                null,
                "gregtech:textures/gui/multimachines/ResearchFake",
                1,
                1,
                1,
                0,
                1,
                "",
                1,
                "",
                true,
                false);
        public static ArrayList<GT_Recipe_AssemblyLine> sAssemblylineRecipes = new ArrayList<>();

        public GT_Recipe_MapTT(
                Collection<GT_Recipe> aRecipeList,
                String aUnlocalizedName,
                String aLocalName,
                String aNEIName,
                String aNEIGUIPath,
                int aUsualInputCount,
                int aUsualOutputCount,
                int aMinimalInputItems,
                int aMinimalInputFluids,
                int aAmperage,
                String aNEISpecialValuePre,
                int aNEISpecialValueMultiplier,
                String aNEISpecialValuePost,
                boolean aShowVoltageAmperageInNEI,
                boolean aNEIAllowed) {
            super(
                    aRecipeList,
                    aUnlocalizedName,
                    aLocalName,
                    aNEIName,
                    aNEIGUIPath,
                    aUsualInputCount,
                    aUsualOutputCount,
                    aMinimalInputItems,
                    aMinimalInputFluids,
                    aAmperage,
                    aNEISpecialValuePre,
                    aNEISpecialValueMultiplier,
                    aNEISpecialValuePost,
                    aShowVoltageAmperageInNEI,
                    aNEIAllowed);
        }
    }

    public static class TT_assLineRecipe extends TT_recipe {
        public final ItemStack mResearchItem;

        public TT_assLineRecipe(
                boolean aOptimize,
                ItemStack researchItem,
                ItemStack[] aInputs,
                ItemStack[] aOutputs,
                Object aSpecialItems,
                FluidStack[] aFluidInputs,
                int aDuration,
                int aEUt,
                int aSpecialValue,
                EMConstantStackMap[] in,
                EMConstantStackMap[] out,
                EMConstantStackMap[] catalyst,
                IAdditionalCheck check) {
            super(
                    aOptimize,
                    aInputs,
                    aOutputs,
                    aSpecialItems,
                    null,
                    aFluidInputs,
                    null,
                    aDuration,
                    aEUt,
                    aSpecialValue,
                    in,
                    out,
                    catalyst,
                    check);
            mResearchItem = researchItem;
        }

        public TT_assLineRecipe(
                boolean aOptimize,
                ItemStack researchItem,
                ItemStack[] aInputs,
                ItemStack[] aOutputs,
                Object aSpecialItems,
                FluidStack[] aFluidInputs,
                int aDuration,
                int aEUt,
                int aSpecialValue,
                EMConstantStackMap[] in) {
            this(
                    aOptimize,
                    researchItem,
                    aInputs,
                    aOutputs,
                    aSpecialItems,
                    aFluidInputs,
                    aDuration,
                    aEUt,
                    aSpecialValue,
                    in,
                    null,
                    null,
                    null);
        }
    }

    public static class TT_EMRecipe extends TT_recipe {
        public final IEMDefinition mResearchEM;
        public final GT_Recipe scannerRecipe;

        public TT_EMRecipe(
                boolean aOptimize,
                GT_Recipe scannerRecipe,
                IEMDefinition researchEM,
                ItemStack[] aInputs,
                ItemStack[] aOutputs,
                Object aSpecialItems,
                FluidStack[] aFluidInputs,
                int aDuration,
                int aEUt,
                int aSpecialValue,
                EMConstantStackMap[] in,
                EMConstantStackMap[] out,
                EMConstantStackMap[] catalyst,
                IAdditionalCheck check) {
            super(
                    aOptimize,
                    aInputs,
                    aOutputs,
                    aSpecialItems,
                    null,
                    aFluidInputs,
                    null,
                    aDuration,
                    aEUt,
                    aSpecialValue,
                    in,
                    out,
                    catalyst,
                    check);
            mResearchEM = researchEM;
            this.scannerRecipe = scannerRecipe;
        }

        public TT_EMRecipe(
                boolean aOptimize,
                GT_Recipe scannerRecipe,
                IEMDefinition researchEM,
                ItemStack[] aInputs,
                ItemStack[] aOutputs,
                Object aSpecialItems,
                FluidStack[] aFluidInputs,
                int aDuration,
                int aEUt,
                int aSpecialValue,
                EMConstantStackMap[] in) {
            this(
                    aOptimize,
                    scannerRecipe,
                    researchEM,
                    aInputs,
                    aOutputs,
                    aSpecialItems,
                    aFluidInputs,
                    aDuration,
                    aEUt,
                    aSpecialValue,
                    in,
                    null,
                    null,
                    null);
        }
    }

    public static class TT_Recipe_Map_EM<T extends TT_EMRecipe> {
        public static TT_Recipe_Map_EM<TT_EMRecipe> sCrafterRecipesEM =
                new TT_Recipe_Map_EM<>("EM Crafter Recipes", "gt.recipe.em_crafter", null);
        public static TT_Recipe_Map_EM<TT_EMRecipe> sMachineRecipesEM =
                new TT_Recipe_Map_EM<>("EM Machinert Recipe", "gt.recipe.em_machinery", null);

        private final HashMap<IEMDefinition, T> mRecipeMap;
        public final String mNEIName, mUnlocalizedName, mNEIGUIPath;

        public TT_Recipe_Map_EM(String mNEIName, String mUnlocalizedName, String mNEIGUIPath) {
            mRecipeMap = new HashMap<>(16);
            this.mNEIName = mNEIName;
            this.mUnlocalizedName = mUnlocalizedName;
            this.mNEIGUIPath = mNEIGUIPath;
        }

        public T findRecipe(IEMDefinition stack) {
            return mRecipeMap.get(stack);
        }

        public void add(T recipe) {
            mRecipeMap.put(recipe.mResearchEM, recipe);
        }

        public Collection<T> recipeList() {
            return mRecipeMap.values();
        }
    }

    public static class Eye_Of_Harmony_Recipe_Map extends GT_Recipe_Map {

        private static final int xDirMaxCount = 9;
        private static final int yOrigin = 8;

        public Eye_Of_Harmony_Recipe_Map(
                Collection<GT_Recipe> aRecipeList,
                String aUnlocalizedName,
                String aLocalName,
                String aNEIName,
                String aNEIGUIPath,
                int aUsualInputCount,
                int aUsualOutputCount,
                int aMinimalInputItems,
                int aMinimalInputFluids,
                int aAmperage,
                String aNEISpecialValuePre,
                int aNEISpecialValueMultiplier,
                String aNEISpecialValuePost,
                boolean aShowVoltageAmperageInNEI,
                boolean aNEIAllowed) {
            super(
                    aRecipeList,
                    aUnlocalizedName,
                    aLocalName,
                    aNEIName,
                    aNEIGUIPath,
                    aUsualInputCount,
                    aUsualOutputCount,
                    aMinimalInputItems,
                    aMinimalInputFluids,
                    aAmperage,
                    aNEISpecialValuePre,
                    aNEISpecialValueMultiplier,
                    aNEISpecialValuePost,
                    aShowVoltageAmperageInNEI,
                    aNEIAllowed);
            useModularUI(true);
            setLogoPos(8, yOrigin);
        }

        @Override
        public boolean usesSpecialSlot() {
            return false;
        }

        @Override
        public List<Pos2d> getItemInputPositions(int itemInputCount) {
            return UIHelper.getItemGridPositions(itemInputCount, 80, yOrigin, 1, 1);
        }

        @Override
        public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
            return UIHelper.getItemGridPositions(itemOutputCount, 8, yOrigin + 36, xDirMaxCount, 12);
        }

        @Override
        public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
            return UIHelper.getItemGridPositions(fluidInputCount, 0, 0, 0, 0);
        }

        @Override
        public List<Pos2d> getFluidOutputPositions(int fluidOutputCount) {
            return UIHelper.getItemGridPositions(fluidOutputCount, 8, yOrigin + 13 * 17 - 4, xDirMaxCount, 3);
        }

        @Override
        public ModularWindow.Builder createNEITemplate(
                IItemHandlerModifiable itemInputsInventory,
                IItemHandlerModifiable itemOutputsInventory,
                IItemHandlerModifiable specialSlotInventory,
                IItemHandlerModifiable fluidInputsInventory,
                IItemHandlerModifiable fluidOutputsInventory,
                Supplier<Float> progressSupplier,
                Pos2d windowOffset) {
            // Delay setter so that calls to #setUsualFluidInputCount and #setUsualFluidOutputCount are considered
            setNEIBackgroundSize(172, 82 + (Math.max(getItemRowCount() + getFluidRowCount() - 4, 0)) * 18);
            return super.createNEITemplate(
                    itemInputsInventory,
                    itemOutputsInventory,
                    specialSlotInventory,
                    fluidInputsInventory,
                    fluidOutputsInventory,
                    progressSupplier,
                    windowOffset);
        }

        private int getItemRowCount() {
            return (Math.max(mUsualInputCount, mUsualOutputCount) - 1) / xDirMaxCount + 1;
        }

        private int getFluidRowCount() {
            return (Math.max(getUsualFluidInputCount(), getUsualFluidOutputCount()) - 1) / xDirMaxCount + 1;
        }
    }
}
