package tectech.recipe;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static tectech.thing.metaTileEntity.multi.godforge.MTEExoticModule.MAGMATTER_MAX_SPACE;
import static tectech.thing.metaTileEntity.multi.godforge.MTEExoticModule.MAGMATTER_MAX_TIME;
import static tectech.thing.metaTileEntity.multi.godforge.MTEExoticModule.MAGMATTER_MIN_SPACE;
import static tectech.thing.metaTileEntity.multi.godforge.MTEExoticModule.MAGMATTER_MIN_TIME;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.StackInfo;
import gregtech.api.enums.Materials;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.nei.GTNEIDefaultHandler;
import gregtech.nei.NEIScrollArea;
import gregtech.nei.RecipeDisplayInfo;
import gregtech.nei.formatter.INEISpecialInfoFormatter;
import tectech.thing.gui.TecTechUITextures;

/**
 * NEI layout of the exotic module recipes: a scrolling grid of every plasma the recipe can ask for, with weights in
 * the tooltips.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GodforgeExoticFrontend extends RecipeMapFrontend {

    private static final int INPUT_COLUMNS = 4, INPUT_ROWS = 4;
    public static final int MAX_FLUID_INPUTS = INPUT_COLUMNS * INPUT_ROWS;

    private static final int GRID_X = 7, GRID_Y = 12;
    private static final int GRID_HEIGHT = INPUT_ROWS * 18;
    private static final int OUTPUT_X = 128, OUTPUT_Y = GRID_Y + GRID_HEIGHT / 2 - 9;
    public static final int ARROW_X = 100, ARROW_Y = OUTPUT_Y;
    private static final int LOGO_X = 150, LOGO_Y = GRID_Y + GRID_HEIGHT - 18;

    public GodforgeExoticFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(
            uiPropertiesBuilder,
            neiPropertiesBuilder.recipeBackgroundSize(new Size(170, GRID_HEIGHT + 2 * GRID_Y))
                .neiSpecialInfoFormatter(new ExoticSpecialInfoFormatter()));
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder, Pos2d windowOffset) {
        builder.widget(
            new DrawableWidget().setDrawable(TecTechUITextures.PICTURE_GODFORGE_LOGO)
                .setSize(18, 18)
                .setPos(new Pos2d(LOGO_X, LOGO_Y).add(windowOffset)));
    }

    @Override
    public List<Pos2d> getItemInputPositions(int itemInputCount) {
        return Collections.emptyList();
    }

    @Override
    public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        // Positions beyond the visible slots only make NEI create the stacks; the scroll area places them
        return UIHelper.getGridPositions(fluidInputCount, GRID_X, GRID_Y, INPUT_COLUMNS, Integer.MAX_VALUE);
    }

    @Override
    public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        return Collections.emptyList();
    }

    @Override
    public List<Pos2d> getFluidOutputPositions(int fluidOutputCount) {
        return UIHelper.getGridPositions(fluidOutputCount, OUTPUT_X, OUTPUT_Y, 1, 1);
    }

    @Override
    public void prepareRecipe(GTNEIDefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        List<PositionedStack> fluidInputs = new ArrayList<>();
        for (PositionedStack stack : neiCachedRecipe.mInputs) {
            if (stack instanceof GTNEIDefaultHandler.FixedPositionedStack fixed && fixed.isFluid()) {
                fluidInputs.add(stack);
            }
        }

        neiCachedRecipe
            .addScrollArea(new NEIScrollArea(GRID_X, GRID_Y, INPUT_COLUMNS, INPUT_ROWS).setStacks(fluidInputs));
    }

    private static boolean isQuarkGluonRecipe(GTRecipe recipe) {
        return recipe.mFluidOutputs.length > 0
            && recipe.mFluidOutputs[0].isFluidEqual(Materials.QuarkGluonPlasma.getFluid(1));
    }

    @Override
    protected List<String> handleNEIItemInputTooltip(List<String> currentTip,
        GTNEIDefaultHandler.FixedPositionedStack pStack) {
        FluidStack fluid = StackInfo.getFluid(pStack.item);
        if (fluid == null) return currentTip;

        if (pStack.recipe.mRecipe.mSpecialItems instanceof ExoticPlasmaWeights weights
            && weights.getWeight(fluid) > 0) {
            currentTip.add(
                EnumChatFormatting.GRAY + StatCollector
                    .translateToLocalFormatted("tt.nei.fog_exotic.weight", formatNumber(weights.getWeight(fluid))));
            currentTip.add(
                EnumChatFormatting.GRAY + StatCollector.translateToLocalFormatted(
                    "tt.nei.fog_exotic.chance",
                    formatNumber(100 * weights.getChance(fluid))));
        } else if (fluid.isFluidEqual(Materials.Time.getMolten(1))) {
            addRangeTooltip(currentTip, MAGMATTER_MIN_TIME, MAGMATTER_MAX_TIME);
        } else if (fluid.isFluidEqual(Materials.Space.getMolten(1))) {
            addRangeTooltip(currentTip, MAGMATTER_MIN_SPACE, MAGMATTER_MAX_SPACE);
        }
        return currentTip;
    }

    private static void addRangeTooltip(List<String> currentTip, int min, int max) {
        currentTip.add(
            EnumChatFormatting.GRAY
                + StatCollector.translateToLocalFormatted("tt.nei.fog_exotic.random_amount", min, max));
    }

    @Override
    protected void drawEnergyInfo(RecipeDisplayInfo recipeInfo) {
        long eut = recipeInfo.recipe.mEUt;
        long duration = recipeInfo.recipe.mDuration;
        recipeInfo.drawText(
            StatCollector.translateToLocalFormatted("GT5U.gui.text.total_line", formatNumber(eut * duration)));

        recipeInfo.drawText(StatCollector.translateToLocalFormatted("GT5U.gui.text.usage_line", formatNumber(eut)));

        recipeInfo
            .drawText(StatCollector.translateToLocalFormatted("GT5U.gui.text.time_line", formatNumber(duration / 20)));

    }

    @Override
    protected void drawDurationInfo(RecipeDisplayInfo recipeInfo) {}

    private static class ExoticSpecialInfoFormatter implements INEISpecialInfoFormatter {

        @Override
        public List<String> format(RecipeDisplayInfo recipeInfo) {
            List<String> result = new ArrayList<>();
            if (isQuarkGluonRecipe(recipeInfo.recipe)) {
                result.add(StatCollector.translateToLocal("tt.nei.fog_exotic.picks_qgp"));
            }
            return result;
        }
    }
}
