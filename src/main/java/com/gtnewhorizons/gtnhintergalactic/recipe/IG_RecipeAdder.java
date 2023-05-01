package com.gtnewhorizons.gtnhintergalactic.recipe;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizons.gtnhintergalactic.gui.IG_UITextures;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_RecipeAdder;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.common.misc.spaceprojects.SpaceProjectManager;
import gregtech.nei.NEIRecipeInfo;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;

/**
 * GT recipe adder of GTNH-Intergalactic
 *
 * @author minecraft7771
 */
public class IG_RecipeAdder extends GT_RecipeAdder {

    public static IG_RecipeAdder instance = new IG_RecipeAdder();
    public static final ItemStack[] nullItem = new ItemStack[0];
    public static final FluidStack[] nullFluid = new FluidStack[0];

    public static void init() {

    }

    public static void postInit() {

    }

    /**
     * Add a recipe to the Space Research Module
     *
     * @param aItemInputs                Needed item inputs
     * @param aFluidInputs               Needed fluid inputs
     * @param output                     Item output
     * @param computationRequiredPerSec  Required computation
     * @param duration                   Duration of the recipe
     * @param EUt                        Consumed EU per tick
     * @param neededSpaceProject         Name of the needed space project
     * @param neededSpaceProjectLocation Location of the needed space project or null if any
     * @return True if recipe was added, else false
     */
    public static boolean addSpaceResearchRecipe(ItemStack[] aItemInputs, FluidStack[] aFluidInputs, ItemStack output,
            int computationRequiredPerSec, int duration, int EUt, String neededSpaceProject,
            String neededSpaceProjectLocation) {
        if (aItemInputs == null) {
            aItemInputs = nullItem;
        }
        if (aFluidInputs == null) {
            aFluidInputs = nullFluid;
        }

        return instance.sSpaceResearchRecipes.add(
                new IG_Recipe(
                        false,
                        aItemInputs,
                        new ItemStack[] { output },
                        null,
                        null,
                        aFluidInputs,
                        null,
                        duration,
                        EUt,
                        computationRequiredPerSec,
                        neededSpaceProject,
                        neededSpaceProjectLocation))
                != null;
    }

    /**
     * Add a recipe to the Space Research Module
     *
     * @param aItemInputs                Needed item inputs
     * @param aFluidInputs               Needed fluid inputs
     * @param output                     Item output
     * @param requiredModuleTier         Required minimum module tier
     * @param duration                   Duration of the recipe
     * @param EUt                        Consumed EU per tick
     * @param neededSpaceProject         Name of the needed space project
     * @param neededSpaceProjectLocation Location of the needed space project or null if any
     * @return True if recipe was added, else false
     */
    public static boolean addSpaceAssemblerRecipe(ItemStack[] aItemInputs, FluidStack[] aFluidInputs, ItemStack output,
            int requiredModuleTier, int duration, int EUt, String neededSpaceProject,
            String neededSpaceProjectLocation) {
        if (aItemInputs == null) {
            aItemInputs = nullItem;
        }
        if (aFluidInputs == null) {
            aFluidInputs = nullFluid;
        }

        return instance.sSpaceAssemblerRecipes.add(
                new IG_Recipe(
                        false,
                        aItemInputs,
                        new ItemStack[] { output },
                        null,
                        null,
                        aFluidInputs,
                        null,
                        duration,
                        EUt,
                        requiredModuleTier,
                        neededSpaceProject,
                        neededSpaceProjectLocation))
                != null;
    }

    /**
     * Add a Space Mining recipe
     *
     * @param aItemInputs               Equipment used for the mining operation
     * @param aFluidInputs              Additional input fluids
     * @param aChances                  Chances to get each ore type
     * @param ores                      Ores that should spawn in this asteroid
     * @param minSize                   Minimum size of the asteroid in stacks
     * @param maxSize                   Maximum size of the asteroid in stacks
     * @param minDistance               Minimal distance in which you will find the asteroid
     * @param maxDistance               Maximal distance in which you will find the asteroid
     * @param computationRequiredPerSec Required computation for the mining operation
     * @param minModuleTier             Minimum module tier that is required to mine this asteroid
     * @param duration                  Duration of the mining operation
     * @param EUt                       Used energy for the mining operation per tick
     * @param recipeWeight              Weight of this recipe (Used in determining which recipe to execute)
     * @return True if recipes could be added, else false
     */
    public static boolean addSpaceMiningRecipe(ItemStack[] aItemInputs, FluidStack[] aFluidInputs, int[] aChances,
            Materials[] ores, OrePrefixes orePrefixes, int minSize, int maxSize, int minDistance, int maxDistance,
            int computationRequiredPerSec, int minModuleTier, int duration, int EUt, int recipeWeight) {
        if ((aItemInputs == null && aFluidInputs == null) || ores == null) {
            return false;
        }
        if (minDistance > maxDistance || minSize > maxSize) {
            return false;
        }
        if (recipeWeight <= 0) {
            GT_Log.err.println("Weight of mining recipe for main material " + ores[0].toString() + " is 0");
        }
        if (aChances != null) {
            if (aChances.length < ores.length) {
                return false;
            } else if (aChances.length > ores.length) {
                GT_Log.err.println(
                        "Chances and outputs of mining recipe for main material " + ores[0].toString()
                                + " have different length!");
            }
            if (Arrays.stream(aChances).sum() != 10000) {
                GT_Log.err.println(
                        "Sum of chances in mining recipe for main material " + ores[0].toString()
                                + " is not 100%! This will lead to no issue but might be unintentional");
            }
        } else {
            aChances = new int[ores.length];
            Arrays.fill(aChances, 10000);
        }
        if (aItemInputs == null) {
            aItemInputs = nullItem;
        }
        if (aFluidInputs == null) {
            aFluidInputs = nullFluid;
        }
        if (orePrefixes == null) {
            return false;
        }

        // Map ores to actual items with stack minSize 64
        ItemStack[] outputs = new ItemStack[ores.length];
        for (int i = 0; i < ores.length; i++) {
            outputs[i] = GT_OreDictUnificator.get(orePrefixes, ores[i], 64);
        }

        instance.sSpaceMiningRecipes.add(
                new IG_Recipe.IG_SpaceMiningRecipe(
                        false,
                        aItemInputs,
                        outputs,
                        aFluidInputs,
                        aChances,
                        duration,
                        EUt,
                        computationRequiredPerSec,
                        minModuleTier,
                        minDistance,
                        maxDistance,
                        minSize,
                        maxSize,
                        recipeWeight));
        return true;
    }

    public static boolean addSpaceMiningRecipe(ItemStack[] aItemInputs, FluidStack[] aFluidInputs, int[] aChances,
            ItemStack[] aItemOutputs, int minSize, int maxSize, int minDistance, int maxDistance,
            int computationRequiredPerSec, int minModuleTier, int duration, int EUt, int recipeWeight) {
        if ((aItemInputs == null && aFluidInputs == null) || aItemOutputs == null) {
            return false;
        }
        if (minDistance > maxDistance || minSize > maxSize) {
            return false;
        }
        if (recipeWeight <= 0) {
            GT_Log.err.println(
                    "Weight of mining recipe for main material " + aItemOutputs[0].getUnlocalizedName() + " is 0");
        }
        if (aChances != null) {
            if (aChances.length < aItemOutputs.length) {
                return false;
            } else if (aChances.length > aItemOutputs.length) {
                GT_Log.err.println(
                        "Chances and outputs of mining recipe for main material " + aItemOutputs[0].getUnlocalizedName()
                                + " have different length!");
            }
            if (Arrays.stream(aChances).sum() != 10000) {
                GT_Log.err.println(
                        "Sum of chances in mining recipe for main material " + aItemOutputs[0].getUnlocalizedName()
                                + " is not 100%! This will lead to no issue but might be unintentional");
            }
        } else {
            aChances = new int[aItemOutputs.length];
            Arrays.fill(aChances, 10000);
        }
        if (aItemInputs == null) {
            aItemInputs = nullItem;
        }
        if (aFluidInputs == null) {
            aFluidInputs = nullFluid;
        }

        instance.sSpaceMiningRecipes.add(
                new IG_Recipe.IG_SpaceMiningRecipe(
                        false,
                        aItemInputs,
                        aItemOutputs,
                        aFluidInputs,
                        aChances,
                        duration,
                        EUt,
                        computationRequiredPerSec,
                        minModuleTier,
                        minDistance,
                        maxDistance,
                        minSize,
                        maxSize,
                        recipeWeight));

        return true;
    }

    /** Recipe map for recipes in the Space Research Module */
    public IG_Recipe_Map sSpaceResearchRecipes = (IG_Recipe_Map) new IG_Recipe_Map(
            new HashSet<>(32),
            "gt.recipe.spaceResearch",
            GCCoreUtil.translate("ig.nei.spaceresearch.name"),
            null,
            "gregtech:textures/gui/FakeAssemblyline",
            7,
            1,
            1,
            0,
            1,
            GCCoreUtil.translate("ig.nei.spacemining.computationPrefix") + " ",
            1,
            " " + GCCoreUtil.translate("ig.nei.spacemining.computationSuffix"),
            true,
            true) {

        @Override
        public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
            return Collections.singletonList(new Pos2d(142, 35));
        }

        @Override
        public List<Pos2d> getItemInputPositions(int itemInputCount) {
            List<Pos2d> inputPositions = new ArrayList<>();
            inputPositions.add(new Pos2d(80, 35));
            inputPositions.add(new Pos2d(62, 8));
            inputPositions.add(new Pos2d(80, 8));
            inputPositions.add(new Pos2d(98, 8));
            inputPositions.add(new Pos2d(62, 60));
            inputPositions.add(new Pos2d(80, 60));
            inputPositions.add(new Pos2d(98, 60));
            return inputPositions;
        }

        @Override
        public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
            return UIHelper.getGridPositions(fluidInputCount, 34, 17, 1);
        }
    }.setUsualFluidInputCount(3).setLogo(IG_UITextures.PICTURE_ELEVATOR_LOGO).setLogoSize(18, 18)
            .setNEITransferRect(new Rectangle(110, 35, 18, 18)).setProgressBarPos(110, 35)
            .setNEISpecialInfoFormatter((recipeInfo, applyPrefixAndSuffix) -> {
                List<String> specialInfo = new ArrayList<>();
                specialInfo.add(applyPrefixAndSuffix.apply(recipeInfo.recipe.mSpecialValue));
                if (recipeInfo.recipe instanceof IG_Recipe) {
                    IG_Recipe recipe = (IG_Recipe) recipeInfo.recipe;
                    String neededProject = recipe.getNeededSpaceProject();
                    String neededProjectLocation = recipe.getNeededSpaceProjectLocation();
                    if (neededProject != null && !neededProject.equals("")) {
                        specialInfo.add(
                                String.format(
                                        GCCoreUtil.translate("ig.nei.spaceassembler.project"),
                                        SpaceProjectManager.getProject(neededProject).getLocalizedName()));
                        specialInfo.add(
                                String.format(
                                        GCCoreUtil.translate("ig.nei.spaceassembler.projectAt"),
                                        neededProjectLocation == null || neededProjectLocation.equals("")
                                                ? GCCoreUtil.translate("ig.nei.spaceassembler.projectAnyLocation")
                                                : GCCoreUtil.translate(
                                                        SpaceProjectManager.getLocation(neededProjectLocation)
                                                                .getUnlocalizedName())));
                    }
                }
                return specialInfo;
            });

    /** Recipe map for recipes in the Space Assembler Module */
    public IG_Recipe_Map sSpaceAssemblerRecipes = (IG_Recipe_Map) new IG_Recipe_Map(
            new HashSet<>(32),
            "gt.recipe.spaceAssembler",
            GCCoreUtil.translate("ig.nei.spaceassembler.name"),
            null,
            "gregtech:textures/gui/FakeAssemblyline",
            16,
            1,
            1,
            0,
            1,
            GCCoreUtil.translate("ig.nei.spacemining.specialValue.0"),
            1,
            GCCoreUtil.translate("ig.nei.spacemining.specialValue.1"),
            true,
            true) {

        @Override
        public List<Pos2d> getItemInputPositions(int itemInputCount) {
            return UIHelper.getGridPositions(itemInputCount, 16, 8, 4);
        }

        @Override
        public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
            return Collections.singletonList(new Pos2d(142, 8));
        }

        @Override
        public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
            return UIHelper.getGridPositions(fluidInputCount, 106, 8, 1);
        }

        @Override
        public void addProgressBarUI(ModularWindow.Builder builder, Supplier<Float> progressSupplier,
                Pos2d windowOffset) {
            int bar1Width = 17;
            int bar2Width = 18;
            builder.widget(
                    new ProgressBar().setTexture(GT_UITextures.PROGRESSBAR_ASSEMBLY_LINE_1, 17)
                            .setDirection(ProgressBar.Direction.RIGHT)
                            .setProgress(() -> progressSupplier.get() * ((float) (bar1Width + bar2Width) / bar1Width))
                            .setSynced(false, false).setPos(new Pos2d(88, 8).add(windowOffset)).setSize(bar1Width, 72));
            builder.widget(
                    new ProgressBar().setTexture(GT_UITextures.PROGRESSBAR_ASSEMBLY_LINE_2, 18)
                            .setDirection(ProgressBar.Direction.RIGHT)
                            .setProgress(
                                    () -> (progressSupplier.get() - ((float) bar1Width / (bar1Width + bar2Width)))
                                            * ((float) (bar1Width + bar2Width) / bar2Width))
                            .setSynced(false, false).setPos(new Pos2d(124, 8).add(windowOffset))
                            .setSize(bar2Width, 72));
        }
    }.setUsualFluidInputCount(4).setLogo(IG_UITextures.PICTURE_ELEVATOR_LOGO).setLogoSize(18, 18)
            .setNEITransferRect(new Rectangle(124, 8, 16, 16)).useComparatorForNEI(true)
            .setNEISpecialInfoFormatter((recipeInfo, applyPrefixAndSuffix) -> {
                List<String> specialInfo = new ArrayList<>();
                specialInfo.add(applyPrefixAndSuffix.apply(recipeInfo.recipe.mSpecialValue));
                if (recipeInfo.recipe instanceof IG_Recipe) {
                    IG_Recipe recipe = (IG_Recipe) recipeInfo.recipe;
                    String neededProject = recipe.getNeededSpaceProject();
                    String neededProjectLocation = recipe.getNeededSpaceProjectLocation();
                    if (neededProject != null && !neededProject.equals("")) {
                        specialInfo.add(
                                String.format(
                                        GCCoreUtil.translate("ig.nei.spaceassembler.project"),
                                        SpaceProjectManager.getProject(neededProject).getLocalizedName()));
                        specialInfo.add(
                                String.format(
                                        GCCoreUtil.translate("ig.nei.spaceassembler.projectAt"),
                                        neededProjectLocation == null || neededProjectLocation.equals("")
                                                ? GCCoreUtil.translate("ig.nei.spaceassembler.projectAnyLocation")
                                                : GCCoreUtil.translate(
                                                        SpaceProjectManager.getLocation(neededProjectLocation)
                                                                .getUnlocalizedName())));
                    }
                }
                return specialInfo;
            });

    /** Recipe map for recipes in the Space Mining Module */
    public IG_Recipe_Map sSpaceMiningRecipes = (IG_Recipe_Map) new IG_Recipe_Map(
            new HashSet<>(32),
            "gt.recipe.spaceMining",
            GCCoreUtil.translate("ig.nei.spacemining.name"),
            null,
            "gregtech:textures/gui/FakeSpaceMiningModule",
            5,
            16,
            1,
            0,
            1,
            GCCoreUtil.translate("ig.nei.spacemining.specialValue.0"),
            1,
            GCCoreUtil.translate("ig.nei.spacemining.specialValue.1"),
            true,
            true) {

        @Override
        public List<Pos2d> getItemInputPositions(int itemInputCount) {
            List<Pos2d> results = new ArrayList<>();
            // assume drones are present on first input
            results.add(new Pos2d(143, 15));
            results.addAll(UIHelper.getGridPositions(itemInputCount - 1, 10, 6, 2));
            return results;
        }

        @Override
        public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
            return UIHelper.getGridPositions(itemOutputCount, 69, 6, 4);
        }

        @Override
        public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
            return UIHelper.getGridPositions(fluidInputCount, 10, 51, fluidInputCount);
        }

        @Override
        public void addProgressBarUI(ModularWindow.Builder builder, Supplier<Float> progressSupplier,
                Pos2d windowOffset) {
            builder.widget(
                    new DrawableWidget().setDrawable(IG_UITextures.PROGRESSBAR_SPACE_MINING_MODULE_ARROW)
                            .setPos(new Pos2d(46, 6).add(windowOffset)).setSize(23, 63));
        }

        @Override
        protected void drawNEISpecialInfo(NEIRecipeInfo recipeInfo) {
            recipeInfo.yPos -= 1;
            super.drawNEISpecialInfo(recipeInfo);
        }
    }.setUsualFluidInputCount(2).useComparatorForNEI(true).setLogo(IG_UITextures.PICTURE_ELEVATOR_LOGO)
            .setLogoPos(151, 58).setLogoSize(18, 18).setNEIBackgroundSize(172, 78)
            .setNEITransferRect(new Rectangle(46, 6, 23, 63))
            .setNEISpecialInfoFormatter((recipeInfo, applyPrefixAndSuffix) -> {
                List<String> result = new ArrayList<>();
                result.add(applyPrefixAndSuffix.apply(recipeInfo.recipe.mSpecialValue));
                if (recipeInfo.recipe instanceof IG_Recipe.IG_SpaceMiningRecipe) {
                    IG_Recipe.IG_SpaceMiningRecipe miningRecipe = (IG_Recipe.IG_SpaceMiningRecipe) recipeInfo.recipe;
                    result.add(
                            GCCoreUtil.translate("ig.nei.spacemining.distance") + " "
                                    + miningRecipe.minDistance
                                    + "-"
                                    + miningRecipe.maxDistance);
                    result.add(
                            GCCoreUtil.translate("ig.nei.spacemining.size") + " "
                                    + miningRecipe.minSize
                                    + "-"
                                    + miningRecipe.maxSize);
                    result.add(
                            GCCoreUtil.translate("ig.nei.spacemining.computationPrefix") + " "
                                    + GT_Utility.formatNumbers(miningRecipe.computation)
                                    + " "
                                    + GCCoreUtil.translate("ig.nei.spacemining.computationSuffix"));
                    result.add(GCCoreUtil.translate("ig.nei.spacemining.weight") + " " + miningRecipe.recipeWeight);
                }
                return result;
            });
}
