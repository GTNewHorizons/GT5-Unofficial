package gtneioreplugin.plugin.gregtech5;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import gregtech.api.util.GTUtility;
import gtneioreplugin.plugin.PluginBase;
import gtneioreplugin.plugin.item.ItemDimensionDisplay;
import gtneioreplugin.util.GT5UndergroundFluidHelper;
import gtneioreplugin.util.GT5UndergroundFluidHelper.UndergroundFluidWrapper;

public class PluginGT5UndergroundFluid extends PluginBase {

    private static final int lineSpace = 20;
    private static final int xDimensionDisplay = 30;
    private static final int halfItemLength = 16 / 2;
    private static final int fluidNameY = 4;
    private static final int fluidIconY = 14;
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
        FluidStack containerFluid = GTUtility.getFluidForFilledItem(result, true);
        if (containerFluid != null) {
            fluid = containerFluid.getFluid();
        }
        if (fluid == null) {
            FluidStack displayFluid = GTUtility.getFluidFromDisplayStack(result);
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

        int xChance = 85;
        int xAmount = 140;
        int yHeader = fluidIconY + 16 + 4; // icon bottom + 4px gap
        int black = 0x404040;
        CachedUndergroundFluidRecipe recipe = (CachedUndergroundFluidRecipe) this.arecipes.get(recipeIndex);

        GuiDraw.drawStringC(
            EnumChatFormatting.BOLD + recipe.fluidDisplayName + EnumChatFormatting.RESET,
            getGuiWidth() / 2,
            fluidNameY,
            black,
            false);

        GuiDraw.drawStringC(I18n.format("gtnop.gui.nei.dimension") + ":", xDimensionDisplay, yHeader, black, false);
        GuiDraw.drawStringC(I18n.format("gtnop.gui.nei.chance") + ":", xChance, yHeader, black, false);
        GuiDraw.drawStringC(I18n.format("gtnop.gui.nei.fluidAmount") + ":", xAmount, yHeader, black, false);

        int y = yHeader + lineSpace;
        for (int i = 0; i < recipe.dimensionDisplayItems.size(); i++) {
            String chanceValue = format.format((double) recipe.chances.get(i) / 100);
            GuiDraw.drawStringC(I18n.format("gtnop.gui.nei.chance.value", chanceValue), xChance, y, black, false);
            GuiDraw.drawStringC(
                I18n.format("gtnop.gui.nei.fluidAmount.value", recipe.minAmounts.get(i), recipe.maxAmounts.get(i)),
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
    public String getGuiTexture() {
        return "gtneioreplugin:textures/gui/nei/guiBaseFluid.png";
    }

    @Override
    public int getGuiHeight() {
        return 135;
    }

    @Override
    public String getRecipeName() {
        return I18n.format("gtnop.gui.undergroundFluid.name");
    }

    private class CachedUndergroundFluidRecipe extends CachedRecipe {

        private final PositionedStack targetFluidDisplay;
        private final String fluidDisplayName;
        private final List<PositionedStack> dimensionDisplayItems = new ArrayList<>();
        private final List<Integer> chances = new ArrayList<>();
        private final List<Integer> maxAmounts = new ArrayList<>();
        private final List<Integer> minAmounts = new ArrayList<>();

        private CachedUndergroundFluidRecipe(Fluid fluid, List<UndergroundFluidWrapper> wrappers) {
            String localizedName = fluid.getLocalizedName(new FluidStack(fluid, 1));
            fluidDisplayName = localizedName != null ? localizedName : fluid.getName();
            targetFluidDisplay = new PositionedStack(
                GTUtility.getFluidDisplayStack(fluid),
                getGuiWidth() / 2 - halfItemLength,
                fluidIconY);
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
