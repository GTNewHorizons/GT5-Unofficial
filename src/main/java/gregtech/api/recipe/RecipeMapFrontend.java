package gregtech.api.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import org.apache.commons.lang3.tuple.Pair;

import com.gtnewhorizons.modularui.api.GlStateManager;
import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import codechicken.nei.PositionedStack;
import gregtech.api.enums.SteamVariant;
import gregtech.api.gui.GUIColorOverride;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.recipe.metadata.IRecipeMetadataStorage;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.nei.GTNEIDefaultHandler;
import gregtech.nei.RecipeDisplayInfo;

/**
 * Responsible for managing GUI tied to recipemap. It has two property objects, {@link NEIRecipeProperties} and
 * {@link BasicUIProperties}. The former is only for NEI display, while the latter is for both NEI and basic machine.
 * <p>
 * In order to bind custom frontend to recipemap, use {@link RecipeMapBuilder#frontend}.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RecipeMapFrontend {

    /**
     * Properties specific to this frontend, mainly for GUI widgets.
     */
    protected final BasicUIProperties uiProperties;
    /**
     * Properties specific to this frontend, only for NEI specific settings.
     */
    protected final NEIRecipeProperties neiProperties;

    protected final GUIColorOverride colorOverride = GUIColorOverride
        .get(GTUITextures.BACKGROUND_NEI_SINGLE_RECIPE.location);

    public RecipeMapFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        this.uiProperties = uiPropertiesBuilder.itemInputPositionsGetter(this::getItemInputPositions)
            .itemOutputPositionsGetter(this::getItemOutputPositions)
            .specialItemPositionGetter(this::getSpecialItemPosition)
            .fluidInputPositionsGetter(this::getFluidInputPositions)
            .fluidOutputPositionsGetter(this::getFluidOutputPositions)
            .build();
        this.neiProperties = neiPropertiesBuilder.build();
    }

    /**
     * @return Properties specific to this frontend, mainly for GUI widgets.
     */
    public BasicUIProperties getUIProperties() {
        return uiProperties;
    }

    /**
     * @return Properties specific to this frontend, only for NEI specific settings.
     */
    public NEIRecipeProperties getNEIProperties() {
        return neiProperties;
    }

    /**
     * Creates NEI recipe layout, except for actual items / fluids.
     */
    public ModularWindow.Builder createNEITemplate(GTNEIDefaultHandler.NEITemplateContext ctx) {
        ModularWindow.Builder builder = ModularWindow.builder(neiProperties.recipeBackgroundSize)
            .setBackground(GTUITextures.BACKGROUND_NEI_SINGLE_RECIPE);

        if (uiProperties.useProgressBar) {
            addProgressBar(builder, ctx);
        }

        UIHelper.forEachSlots(
            (i, backgrounds, pos) -> builder.widget(
                SlotWidget.phantom(ctx.itemInputsInventory, i)
                    .setBackground(backgrounds)
                    .setPos(pos)
                    .setSize(18, 18)),
            (i, backgrounds, pos) -> builder.widget(
                SlotWidget.phantom(ctx.itemOutputsInventory, i)
                    .setBackground(backgrounds)
                    .setPos(pos)
                    .setSize(18, 18)),
            (i, backgrounds, pos) -> {
                if (uiProperties.useSpecialSlot) builder.widget(
                    SlotWidget.phantom(ctx.specialSlotInventory, 0)
                        .setBackground(backgrounds)
                        .setPos(pos)
                        .setSize(18, 18));
            },
            (i, backgrounds, pos) -> builder.widget(
                SlotWidget.phantom(ctx.fluidInputsInventory, i)
                    .setBackground(backgrounds)
                    .setPos(pos)
                    .setSize(18, 18)),
            (i, backgrounds, pos) -> builder.widget(
                SlotWidget.phantom(ctx.fluidOutputsInventory, i)
                    .setBackground(backgrounds)
                    .setPos(pos)
                    .setSize(18, 18)),
            ModularUITextures.ITEM_SLOT,
            ModularUITextures.FLUID_SLOT,
            uiProperties,
            uiProperties.maxItemInputs,
            uiProperties.maxItemOutputs,
            uiProperties.maxFluidInputs,
            uiProperties.maxFluidOutputs,
            SteamVariant.NONE,
            ctx.windowOffset);

        addGregTechLogo(builder, ctx.windowOffset);

        for (Pair<IDrawable, Pair<Size, Pos2d>> specialTexture : uiProperties.specialTextures) {
            builder.widget(
                new DrawableWidget().setDrawable(specialTexture.getLeft())
                    .setSize(
                        specialTexture.getRight()
                            .getLeft())
                    .setPos(
                        specialTexture.getRight()
                            .getRight()
                            .add(ctx.windowOffset)));
        }

        return builder;
    }

    public void addProgressBar(ModularWindow.Builder builder, GTNEIDefaultHandler.NEITemplateContext ctx) {
        assert uiProperties.progressBarTexture != null;
        builder.widget(
            new ProgressBar().setTexture(uiProperties.progressBarTexture.get(), 20)
                .setDirection(uiProperties.progressBarDirection)
                .setProgress(ctx.progressSupplier)
                .setSynced(false, false)
                .setPos(uiProperties.progressBarPos.add(ctx.windowOffset))
                .setSize(uiProperties.progressBarSize));
    }

    public void addGregTechLogo(ModularWindow.Builder builder, Pos2d windowOffset) {
        builder.widget(
            new DrawableWidget().setDrawable(uiProperties.logo)
                .setSize(uiProperties.logoSize)
                .setPos(uiProperties.logoPos.add(windowOffset)));
    }

    /**
     * Overriding this method allows custom NEI stack placement
     */
    public List<Pos2d> getItemInputPositions(int itemInputCount) {
        return UIHelper.getItemInputPositions(itemInputCount);
    }

    /**
     * Overriding this method allows custom NEI stack placement
     */
    public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        return UIHelper.getItemOutputPositions(itemOutputCount);
    }

    /**
     * Overriding this method allows custom NEI stack placement
     */
    public Pos2d getSpecialItemPosition() {
        return UIHelper.getSpecialItemPosition();
    }

    /**
     * Overriding this method allows custom NEI stack placement
     */
    public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        return UIHelper.getFluidInputPositions(fluidInputCount);
    }

    /**
     * Overriding this method allows custom NEI stack placement
     */
    public List<Pos2d> getFluidOutputPositions(int fluidOutputCount) {
        return UIHelper.getFluidOutputPositions(fluidOutputCount);
    }

    public void drawDescription(RecipeDisplayInfo recipeInfo) {
        drawEnergyInfo(recipeInfo);
        drawDurationInfo(recipeInfo);
        drawSpecialInfo(recipeInfo);
        drawMetadataInfo(recipeInfo);
        drawRecipeOwnerInfo(recipeInfo);
    }

    protected void drawEnergyInfo(RecipeDisplayInfo recipeInfo) {
        recipeInfo.overclockDescriber.drawEnergyInfo(recipeInfo);
    }

    protected void drawDurationInfo(RecipeDisplayInfo recipeInfo) {
        recipeInfo.overclockDescriber.drawDurationInfo(recipeInfo);
    }

    protected void drawSpecialInfo(RecipeDisplayInfo recipeInfo) {
        String[] recipeDesc = recipeInfo.recipe.getNeiDesc();
        if (recipeDesc != null) {
            for (String s : recipeDesc) {
                recipeInfo.drawText(s);
            }
        } else {
            recipeInfo.drawTextMultipleLines(neiProperties.neiSpecialInfoFormatter.format(recipeInfo));
        }
    }

    protected void drawMetadataInfo(RecipeDisplayInfo recipeInfo) {
        IRecipeMetadataStorage metadataStorage = recipeInfo.recipe.getMetadataStorage();
        for (Map.Entry<RecipeMetadataKey<?>, Object> entry : metadataStorage.getEntries()) {
            entry.getKey()
                .drawInfo(recipeInfo, entry.getValue());
        }
    }

    protected void drawRecipeOwnerInfo(RecipeDisplayInfo recipeInfo) {
        GTRecipe recipe = recipeInfo.recipe;
        if (recipe.owners != null) {
            if (recipe.owners.size() > 1) {
                recipeInfo.drawText(
                    EnumChatFormatting.ITALIC + StatCollector.translateToLocalFormatted(
                        "GT5U.recipes.recipe_by.original",
                        recipe.owners.get(0)
                            .getName()));
                for (int i = 1; i < recipe.owners.size(); i++) {
                    recipeInfo.drawText(
                        EnumChatFormatting.ITALIC + StatCollector.translateToLocalFormatted(
                            "GT5U.recipes.recipe_by.modified",
                            recipe.owners.get(i)
                                .getName()));
                }
            } else if (!recipe.owners.isEmpty()) {
                recipeInfo.drawText(
                    EnumChatFormatting.ITALIC + StatCollector.translateToLocalFormatted(
                        "GT5U.recipes.recipe_by",
                        recipe.owners.get(0)
                            .getName()));
            }
        }
        if (recipe.stackTraces != null && !recipe.stackTraces.isEmpty()) {
            recipeInfo.drawText("stackTrace:");
            // todo: good way to show all stacktraces
            for (String stackTrace : recipe.stackTraces.get(0)) {
                recipeInfo.drawText(stackTrace);
            }
        }
    }

    public void prepareRecipe(GTNEIDefaultHandler.CachedDefaultRecipe recipe) {

    }

    public List<String> handleNEIItemTooltip(ItemStack stack, List<String> currentTip,
        GTNEIDefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        for (PositionedStack pStack : neiCachedRecipe.mInputs) {
            if (stack == pStack.item) {
                if (pStack instanceof GTNEIDefaultHandler.FixedPositionedStack fixed) {
                    currentTip = handleNEIItemInputTooltip(currentTip, fixed);
                }
                break;
            }
        }
        for (PositionedStack pStack : neiCachedRecipe.mOutputs) {
            if (stack == pStack.item) {
                if (pStack instanceof GTNEIDefaultHandler.FixedPositionedStack fixed) {
                    currentTip = handleNEIItemOutputTooltip(currentTip, fixed);
                }
                break;
            }
        }
        return currentTip;
    }

    protected List<String> handleNEIItemInputTooltip(List<String> currentTip,
        GTNEIDefaultHandler.FixedPositionedStack pStack) {
        return currentTip;
    }

    protected List<String> handleNEIItemOutputTooltip(List<String> currentTip,
        GTNEIDefaultHandler.FixedPositionedStack pStack) {
        return currentTip;
    }

    public void drawNEIOverlays(GTNEIDefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        for (PositionedStack stack : neiCachedRecipe.mInputs) {
            if (stack instanceof GTNEIDefaultHandler.FixedPositionedStack fixed) {
                drawNEIOverlayForInput(fixed);
            }
        }
        for (PositionedStack stack : neiCachedRecipe.mOutputs) {
            if (stack instanceof GTNEIDefaultHandler.FixedPositionedStack fixed) {
                drawNEIOverlayForOutput(fixed);
            }
        }
    }

    protected void drawNEIOverlayForInput(GTNEIDefaultHandler.FixedPositionedStack stack) {

    }

    protected void drawNEIOverlayForOutput(GTNEIDefaultHandler.FixedPositionedStack stack) {

    }

    @SuppressWarnings("SameParameterValue")
    protected void drawNEIOverlayText(String text, PositionedStack stack, int color, float scale, boolean shadow,
        Alignment alignment) {
        FontRenderer fontRenderer = net.minecraft.client.Minecraft.getMinecraft().fontRenderer;
        int width = fontRenderer.getStringWidth(text);
        int x = (int) ((stack.relx + 8 + 8 * alignment.x) / scale) - (width / 2 * (alignment.x + 1));
        int y = (int) ((stack.rely + 8 + 8 * alignment.y) / scale) - (fontRenderer.FONT_HEIGHT / 2 * (alignment.y + 1))
            - (alignment.y - 1) / 2;

        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1);
        fontRenderer.drawString(text, x, y, color, shadow);
        GlStateManager.popMatrix();
    }

    protected void drawNEIOverlayText(String text, PositionedStack stack) {
        drawNEIOverlayText(
            text,
            stack,
            colorOverride.getTextColorOrDefault("nei_overlay_yellow", 0xFDD835),
            0.5f,
            false,
            Alignment.TopLeft);
    }

    public static List<Supplier<Float>> splitProgress(Supplier<Float> progress, int... progressbarLengthArray) {
        float lengthSum = IntStream.of(progressbarLengthArray)
            .sum();
        List<Supplier<Float>> ret = new ArrayList<>();
        float currentLengthSum = 0;
        for (int progressbarLength : progressbarLengthArray) {
            float speed = lengthSum / progressbarLength;
            float offset = currentLengthSum / lengthSum;
            ret.add(() -> {
                float current = progress.get();
                return (current - offset) * speed;
            });
            currentLengthSum += progressbarLength;
        }
        return ret;
    }

    @FunctionalInterface
    public interface FrontendCreator {

        /**
         * @see RecipeMapFrontend#RecipeMapFrontend
         */
        RecipeMapFrontend create(BasicUIPropertiesBuilder uiPropertiesBuilder,
            NEIRecipePropertiesBuilder neiPropertiesBuilder);
    }
}
