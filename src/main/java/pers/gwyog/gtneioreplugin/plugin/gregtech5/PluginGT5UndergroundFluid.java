package pers.gwyog.gtneioreplugin.plugin.gregtech5;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import gregtech.api.util.GT_Utility;
import pers.gwyog.gtneioreplugin.plugin.PluginBase;
import pers.gwyog.gtneioreplugin.plugin.item.ItemDimensionDisplay;
import pers.gwyog.gtneioreplugin.util.GT5UndergroundFluidHelper;
import pers.gwyog.gtneioreplugin.util.GT5UndergroundFluidHelper.UndergroundFluidWrapper;

public class PluginGT5UndergroundFluid extends PluginBase {

    private static final int lineSpace = 20;
    private static final int xDimensionDisplay = 30;
    private static final int halfItemLength = 16 / 2;
    private static final DecimalFormat format = new DecimalFormat("0.#");

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(getOutputId())) {
            for (Map.Entry<String, List<UndergroundFluidWrapper>> entry : GT5UndergroundFluidHelper.getAllEntries()
                .entrySet()) {
                Fluid fluid = FluidRegistry.getFluid(entry.getKey());
                if (fluid != null) {
                    this.arecipes.add(new CachedUndergroundFluidRecipe(fluid, entry.getValue()));
                }
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        Fluid fluid = null;
        FluidStack containerFluid = GT_Utility.getFluidForFilledItem(result, true);
        if (containerFluid != null) {
            fluid = containerFluid.getFluid();
        }
        if (fluid == null) {
            FluidStack displayFluid = GT_Utility.getFluidFromDisplayStack(result);
            if (displayFluid != null) {
                fluid = displayFluid.getFluid();
            }
        }
        if (fluid == null) return;

        List<UndergroundFluidWrapper> wrappers = GT5UndergroundFluidHelper.getEntry(fluid.getName());
        if (wrappers != null) {
            this.arecipes.add(new CachedUndergroundFluidRecipe(fluid, wrappers));
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        String dimension = ItemDimensionDisplay.getDimension(ingredient);
        if (dimension != null) {
            for (Map.Entry<String, List<UndergroundFluidWrapper>> entry : GT5UndergroundFluidHelper.getAllEntries()
                .entrySet()) {
                boolean found = false;
                for (UndergroundFluidWrapper wrapper : entry.getValue()) {
                    if (wrapper.dimension.equals(dimension)) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    Fluid fluid = FluidRegistry.getFluid(entry.getKey());
                    if (fluid != null) {
                        this.arecipes.add(new CachedUndergroundFluidRecipe(fluid, entry.getValue()));
                    }
                }
            }
        }
    }

    @Override
    public void drawExtras(int recipeIndex) {
        drawSeeAllRecipesLabel();

        int xChance = 85;
        int xAmount = 140;
        int yHeader = 30;
        int black = 0x404040;

        GuiDraw.drawStringC(I18n.format("gtnop.gui.nei.dimension") + ":", xDimensionDisplay, yHeader, black, false);
        GuiDraw.drawStringC(I18n.format("gtnop.gui.nei.chance") + ":", xChance, yHeader, black, false);
        GuiDraw.drawStringC(I18n.format("gtnop.gui.nei.fluidAmount") + ":", xAmount, yHeader, black, false);

        int y = 50;
        CachedUndergroundFluidRecipe recipe = (CachedUndergroundFluidRecipe) this.arecipes.get(recipeIndex);
        for (int i = 0; i < recipe.dimensionDisplayItems.size(); i++) {
            GuiDraw.drawStringC(format.format((double) recipe.chances.get(i) / 100) + "%", xChance, y, black, false);
            GuiDraw.drawStringC(
                recipe.minAmounts.get(i)
                    .toString() + "-"
                    + recipe.maxAmounts.get(i)
                        .toString(),
                xAmount,
                y,
                black,
                false);
            y += lineSpace;
        }
    }

    @Override
    public String getOutputId() {
        return "GTOrePluginUndergroundFluid";
    }

    @Override
    public String getRecipeName() {
        return I18n.format("gtnop.gui.undergroundFluid.name");
    }

    private class CachedUndergroundFluidRecipe extends CachedRecipe {

        private final PositionedStack targetFluidDisplay;
        private final List<PositionedStack> dimensionDisplayItems = new ArrayList<>();
        private final List<Integer> chances = new ArrayList<>();
        private final List<Integer> maxAmounts = new ArrayList<>();
        private final List<Integer> minAmounts = new ArrayList<>();

        private CachedUndergroundFluidRecipe(Fluid fluid, List<UndergroundFluidWrapper> wrappers) {
            targetFluidDisplay = new PositionedStack(
                GT_Utility.getFluidDisplayStack(fluid),
                getGuiWidth() / 2 - halfItemLength,
                3);
            int y = 50 - halfItemLength;
            for (UndergroundFluidWrapper wrapper : wrappers) {
                ItemStack dimensionDisplay = ItemDimensionDisplay.getItem(wrapper.dimension);
                if (dimensionDisplay != null) {
                    dimensionDisplayItems.add(
                        new PositionedStack(
                            dimensionDisplay,
                            xDimensionDisplay - halfItemLength,
                            y + GuiDraw.fontRenderer.FONT_HEIGHT / 2));
                    y += lineSpace;
                    chances.add(wrapper.chance);
                    maxAmounts.add(wrapper.maxAmount);
                    minAmounts.add(wrapper.minAmount);
                }
            }
        }

        @Override
        public PositionedStack getResult() {
            return targetFluidDisplay;
        }

        @Override
        public List<PositionedStack> getIngredients() {
            return dimensionDisplayItems;
        }
    }
}
