package gtnhintergalactic.nei;

import java.awt.Rectangle;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizons.modularui.api.GlStateManager;
import com.gtnewhorizons.modularui.api.UIInfos;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.util.GTUtility;
import gtnhintergalactic.gui.IG_UITextures;
import gtnhintergalactic.recipe.GasSiphonRecipes;

/**
 * Recipe handler for the gas siphon recipes
 *
 * @author minecraft7771
 */
public class GasSiphonRecipeHandler extends TemplateRecipeHandler {

    /** Text for the button which will load all pumping recipes */
    private static final String SEE_ALL = "ig.nei.elevatorpump.see_all";
    /** Modular window used to display the recipes */
    protected final ModularWindow modularWindow;
    /** Offsets of the recipe window */
    private static final Pos2d WINDOW_OFFSET = new Pos2d(-2, -8);

    /** X coordinate of the category titles */
    private static final int CATEGORY_TITLE_X = 30;
    /** X coordinate of the category values */
    private static final int CATEGORY_VALUE_X = 85;
    /** Y coordinate of the planet type */
    private static final int PLANET_TYPE_Y = 0;
    /** Y coordinate of the gas type */
    private static final int GAS_TYPE_Y = PLANET_TYPE_Y + 15;
    /** Y coordinate of the output amount */
    private static final int OUT_AMOUNT_Y = GAS_TYPE_Y + 15;
    /** Color of all texts */
    private static final int TEXT_COLOR = 0x404040;
    /** Optional lang key for value formatting */
    private static final String VALUE_FORMAT_KEY = "ig.nei.space.custom.value";
    /** Default value format if VALUE_FORMAT_KEY is not present */
    private static final String DEFAULT_VALUE_FORMAT = "%s";

    /**
     * Initialize the handler for gas siphons recipes
     */
    public GasSiphonRecipeHandler() {
        modularWindow = ModularWindow.builder(170, 82)
            .setBackground(GTUITextures.BACKGROUND_NEI_SINGLE_RECIPE)
            .widget(
                new DrawableWidget().setDrawable(IG_UITextures.PICTURE_ELEVATOR_LOGO)
                    .setSize(17, 17)
                    .setPos(new Pos2d(147, 52)))
            .build();
        UIInfos.initializeWindow(Minecraft.getMinecraft().thePlayer, modularWindow);
    }

    /**
     * Get how many recipes should be displayed per page at most
     *
     * @return Recipe per page
     */
    @Override
    public int recipiesPerPage() {
        return 3;
    }

    /**
     * Get the texture for this GUI
     *
     * @return Texture path
     */
    @Override
    public String getGuiTexture() {
        return "galaxyspace:textures/gui/guiBase.png";
    }

    /**
     * Load the transfer rectangles of this handler
     */
    @Override
    public void loadTransferRects() {
        int stringLength = GuiDraw.getStringWidth(I18n.format(SEE_ALL));
        transferRects.add(
            new RecipeTransferRect(
                new Rectangle(getGuiWidth() - stringLength - 3, 10 + 16, stringLength, 9),
                getOutputId()));
    }

    /**
     * Get the width of this GUI
     *
     * @return GUI width
     */
    public int getGuiWidth() {
        return 166;
    }

    /**
     * Load the crafting recipes of this handler
     *
     * @param outputId Output ID
     * @param results  Results
     */
    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(getOutputId())) {
            for (Map.Entry<String, Map<Integer, FluidStack>> entry : GasSiphonRecipes.RECIPES.entrySet()) {
                for (Map.Entry<Integer, FluidStack> innerEntry : entry.getValue()
                    .entrySet()) {
                    arecipes.add(
                        new CachedSiphonRecipe(
                            entry.getKey(),
                            innerEntry.getKey(),
                            innerEntry.getValue()
                                .getFluid(),
                            innerEntry.getValue().amount));
                }
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    /**
     * Draw the background of this
     *
     * @param recipe Drawn recipe
     */
    @Override
    public void drawBackground(int recipe) {
        for (IDrawable background : modularWindow.getBackground()) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(WINDOW_OFFSET.x, WINDOW_OFFSET.y, 0);
            GlStateManager.color(1f, 1f, 1f, 1f);
            background.draw(Pos2d.ZERO, modularWindow.getSize(), 0);
            GlStateManager.popMatrix();
        }
        for (Widget widget : modularWindow.getChildren()) {
            // NEI already did translation, so we can't use Widget#drawInternal here
            GlStateManager.pushMatrix();
            GlStateManager.translate(widget.getPos().x, widget.getPos().y, 0);
            GlStateManager.color(1, 1, 1, modularWindow.getAlpha());
            GlStateManager.enableBlend();
            // maybe we can use Minecraft#timer but none of the IDrawables use partialTicks
            widget.drawBackground(0);
            widget.draw(0);
            GlStateManager.popMatrix();
        }
    }

    /**
     * Load crafting recipes of the input item
     *
     * @param result Item of which the crafting recipe will be loaded
     */
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

        for (Map.Entry<String, Map<Integer, FluidStack>> entry : GasSiphonRecipes.RECIPES.entrySet()) {
            for (Map.Entry<Integer, FluidStack> innerEntry : entry.getValue()
                .entrySet()) {
                if (innerEntry.getValue()
                    .isFluidEqual(new FluidStack(fluid, 0))) {
                    arecipes.add(
                        new CachedSiphonRecipe(
                            entry.getKey(),
                            innerEntry.getKey(),
                            fluid,
                            innerEntry.getValue().amount));
                }
            }
        }
    }

    /**
     * Draw extra information about the recipe
     *
     * @param recipeIndex Index of the drawn recipe
     */
    @Override
    public void drawExtras(int recipeIndex) {
        GuiDraw
            .drawStringC(I18n.format("ig.nei.siphon.planet") + ":", CATEGORY_TITLE_X, PLANET_TYPE_Y, TEXT_COLOR, false);
        GuiDraw.drawStringC(I18n.format("ig.nei.siphon.depth") + ":", CATEGORY_TITLE_X, GAS_TYPE_Y, TEXT_COLOR, false);
        GuiDraw.drawStringC(
            I18n.format("ig.nei.elevatorpump.amount") + ":",
            CATEGORY_TITLE_X,
            OUT_AMOUNT_Y,
            TEXT_COLOR,
            false);

        CachedSiphonRecipe recipe = (CachedSiphonRecipe) this.arecipes.get(recipeIndex);
        GuiDraw.drawStringC(
            formatValue(GTUtility.translate(recipe.planet)),
            CATEGORY_VALUE_X,
            PLANET_TYPE_Y,
            TEXT_COLOR,
            false);
        GuiDraw.drawStringC(formatValue(recipe.depth), CATEGORY_VALUE_X, GAS_TYPE_Y, TEXT_COLOR, false);
        GuiDraw.drawStringC(
            formatValue(GTUtility.formatNumbers(recipe.amount)),
            CATEGORY_VALUE_X,
            OUT_AMOUNT_Y,
            TEXT_COLOR,
            false);

        GuiDraw.drawStringR(
            EnumChatFormatting.BOLD + I18n.format(SEE_ALL),
            getGuiWidth() - 3,
            OUT_AMOUNT_Y,
            TEXT_COLOR,
            false);
    }

    /**
     * Get the output ID of this
     *
     * @return Output ID
     */
    public String getOutputId() {
        return "galacticraft.siphon";
    }

    /**
     * Get the title of this recipe handler
     *
     * @return Title
     */
    @Override
    public String getRecipeName() {
        return I18n.format("ig.nei.siphon.name");
    }

    private String formatValue(Object value) {
        if (StatCollector.canTranslate(VALUE_FORMAT_KEY)) {
            return I18n.format(VALUE_FORMAT_KEY, value);
        }
        return String.format(DEFAULT_VALUE_FORMAT, value);
    }

    /**
     * Helper class to cache gas siphon recipes
     */
    private class CachedSiphonRecipe extends CachedRecipe {

        /** Stack for displaying the pumped fluid */
        private final PositionedStack targetFluidDisplay;
        /** Needed planet type */
        private final String planet;
        /** Needed depth */
        private final int depth;
        /** Amount that will be pumped per operation */
        private final int amount;

        /**
         * Create a new cached gas siphon recipe
         *
         * @param planet       Needed planet
         * @param depth        Needed depth
         * @param output       Output of the operation
         * @param outputAmount Output amount of the operation
         */
        private CachedSiphonRecipe(String planet, int depth, Fluid output, int outputAmount) {
            targetFluidDisplay = new PositionedStack(GTUtility.getFluidDisplayStack(output), getGuiWidth() - 19, 0);
            this.planet = planet;
            this.depth = depth;
            amount = outputAmount;
        }

        /**
         * Get the output of the recipe
         *
         * @return Recipe output
         */
        @Override
        public PositionedStack getResult() {
            return targetFluidDisplay;
        }
    }
}
