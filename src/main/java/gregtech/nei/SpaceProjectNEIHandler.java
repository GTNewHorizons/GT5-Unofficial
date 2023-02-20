package gregtech.nei;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.gtnewhorizons.modularui.api.GlStateManager;
import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.UIInfos;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.util.GT_Utility;
import gregtech.common.misc.spaceprojects.SpaceProjectManager;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceProject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

import static gregtech.api.enums.GT_Values.RES_PATH_GUI;

public class SpaceProjectNEIHandler extends TemplateRecipeHandler {

    /** Offsets of the recipe window */
    private static final Pos2d WINDOW_OFFSET = new Pos2d(-3, -8);
    /** Y coordinate of the name */
    private static final int NAME_Y = 0;
    /** X coordinate of the name */
    private static final int NAME_X = 85;
    /** Color of all texts */
    private static final int TEXT_COLOR = 0x404040;

    /** Modular window used to display the recipes */
    protected final ModularWindow modularWindow;

    /**
     * Initialize the handler for space project recipes
     */
    public SpaceProjectNEIHandler() {
        modularWindow = ModularWindow.builder(172, 82).setBackground(ModularUITextures.VANILLA_BACKGROUND)
            .widget(
                new DrawableWidget().setDrawable(GT_UITextures.PICTURE_GT_LOGO_17x17_TRANSPARENT).setSize(17, 17)
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
        return 2;
    }

    /**
     * Get the texture for this GUI
     *
     * @return Texture path
     */
    @Override
    public String getGuiTexture() {
        return RES_PATH_GUI + "basicmachines/LCRNEI";
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
     * Get the output ID of this
     *
     * @return Output ID
     */
    public String getOutputId() {
        return "gt.recipe.spaceProject";
    }

    /**
     * Get the title of this recipe handler
     *
     * @return Title
     */
    @Override
    public String getRecipeName() {
        return I18n.format("gt.nei.spaceProject.name");
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
     * Draw extra information about the recipe
     *
     * @param recipeIndex Index of the drawn recipe
     */
    @Override
    public void drawExtras(int recipeIndex) {
        CachedSpaceProjectRecipe recipe = (CachedSpaceProjectRecipe) arecipes.get(recipeIndex);
        GuiDraw.drawStringC(EnumChatFormatting.BOLD + I18n.format(recipe.projectName), NAME_X, NAME_Y, TEXT_COLOR, false);
    }

    /**
     * Load usage recipes of this handler
     *
     * @param inputId     A String identifier representing the type of ingredients used. Eg. {"item", "fuel"}
     * @param ingredients Objects representing the ingredients that matching recipes must contain.
     */
    @Override
    public void loadUsageRecipes(String inputId, Object... ingredients) {
        if (inputId.equals(getOutputId())) {
            for (ISpaceProject project : SpaceProjectManager.getAllProjects()) {
                arecipes.add(
                    new CachedSpaceProjectRecipe(project.getUnlocalizedName(), null, project.getTotalItemsCost(), project.getTotalFluidsCost(), project.getTotalStages()));
            }
        } else {
            super.loadUsageRecipes(inputId, ingredients);
        }
    }

    /**
     * Load usage recipes for the input item
     *
     * @param ingredient The ingredient the recipes must contain.
     */
    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        Fluid fluid = null;
        FluidStack containerFluid = GT_Utility.getFluidForFilledItem(ingredient, true);
        if (containerFluid != null) {
            fluid = containerFluid.getFluid();
        }
        if (fluid == null) {
            FluidStack displayFluid = GT_Utility.getFluidFromDisplayStack(ingredient);
            if (displayFluid != null) {
                fluid = displayFluid.getFluid();
            }
        }

        for (ISpaceProject project : SpaceProjectManager.getAllProjects()) {
            if (fluid == null) {
                if (project.getTotalItemsCost() != null) {
                    for (ItemStack item : project.getTotalItemsCost()) {
                        if (item == null) continue;
                        if (item.getItem() == ingredient.getItem() && item.getItemDamage() == ingredient.getItemDamage()) {
                            arecipes.add(new CachedSpaceProjectRecipe(project.getUnlocalizedName(), null, project.getTotalItemsCost(), project.getTotalFluidsCost(), project.getTotalStages()));
                            break;
                        }
                    }
                }
            } else {
                if (project.getTotalFluidsCost() != null) {
                    for (FluidStack fluidStack : project.getTotalFluidsCost()) {
                        if (fluidStack == null) continue;
                        if (fluidStack.getFluid().equals(fluid)) {
                            arecipes.add(new CachedSpaceProjectRecipe(project.getUnlocalizedName(), null, project.getTotalItemsCost(), project.getTotalFluidsCost(), project.getTotalStages()));
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Helper class to cache project recipes
     */
    private class CachedSpaceProjectRecipe extends CachedRecipe {

        /** Stacks for displaying the input items */
        private final List<PositionedStack> inputs;
        /** Name of the project */
        private final String projectName;
        /** List of possible locations for this project */
        List<String> possibleLocations;
        /** Number of stages of this project */
        private final int stages;

        private CachedSpaceProjectRecipe(String projectName, List<String> possibleLocations, ItemStack[] inputs, FluidStack[] inputFluids, int stages) {
            int index = 0;
            this.inputs = new ArrayList<>();
            for (ItemStack item : inputs) {
                // TODO: Fill in proper coordinates
                if (item != null) {
                    this.inputs.add(new PositionedStack(item, 19 + (index++ * 25), 30));
                }
            }
            index = 0;
            for (FluidStack fluid : inputFluids) {
                // TODO: Fill in proper coordinates
                if (fluid != null) {
                    this.inputs.add(new PositionedStack(GT_Utility.getFluidDisplayStack(fluid, true), 19 + (index++ * 25), 50));
                }
            }
            this.projectName = projectName;
            this.possibleLocations = possibleLocations;
            this.stages = stages;
        }

        @Override
        public List<PositionedStack> getIngredients() {
            return inputs;
        }

        @Override
        public PositionedStack getResult() {
            return null;
        }
    }
}
