package gregtech.api.recipe;

import static gregtech.api.util.GT_Utility.*;
import static gregtech.api.util.GT_Utility.trans;
import static net.minecraft.util.EnumChatFormatting.GRAY;

import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import org.apache.commons.lang3.tuple.Pair;

import com.gtnewhorizons.modularui.api.GlStateManager;
import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import codechicken.nei.PositionedStack;
import gregtech.GT_Mod;
import gregtech.api.enums.SteamVariant;
import gregtech.api.gui.GT_GUIColorOverride;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.common.power.EUPower;
import gregtech.common.power.Power;
import gregtech.common.power.UnspecifiedEUPower;
import gregtech.nei.GT_NEI_DefaultHandler;
import gregtech.nei.NEIRecipeInfo;

/**
 * Responsible for managing GUI tied to recipemap. GUI can be NEI or basic machine.
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

    protected final GT_GUIColorOverride colorOverride = GT_GUIColorOverride
        .get(GT_UITextures.BACKGROUND_NEI_SINGLE_RECIPE.location);
    private int neiTextColorOverride = -1;

    public RecipeMapFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        this.uiProperties = uiPropertiesBuilder.build();
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
    public ModularWindow.Builder createNEITemplate(IItemHandlerModifiable itemInputsInventory,
        IItemHandlerModifiable itemOutputsInventory, IItemHandlerModifiable specialSlotInventory,
        IItemHandlerModifiable fluidInputsInventory, IItemHandlerModifiable fluidOutputsInventory,
        Supplier<Float> progressSupplier, Pos2d windowOffset) {
        ModularWindow.Builder builder = ModularWindow.builder(neiProperties.recipeBackgroundSize)
            .setBackground(GT_UITextures.BACKGROUND_NEI_SINGLE_RECIPE);

        UIHelper.forEachSlots(
            (i, backgrounds, pos) -> builder.widget(
                SlotWidget.phantom(itemInputsInventory, i)
                    .setBackground(backgrounds)
                    .setPos(pos)
                    .setSize(18, 18)),
            (i, backgrounds, pos) -> builder.widget(
                SlotWidget.phantom(itemOutputsInventory, i)
                    .setBackground(backgrounds)
                    .setPos(pos)
                    .setSize(18, 18)),
            (i, backgrounds, pos) -> {
                if (uiProperties.useSpecialSlot) builder.widget(
                    SlotWidget.phantom(specialSlotInventory, 0)
                        .setBackground(backgrounds)
                        .setPos(pos)
                        .setSize(18, 18));
            },
            (i, backgrounds, pos) -> builder.widget(
                SlotWidget.phantom(fluidInputsInventory, i)
                    .setBackground(backgrounds)
                    .setPos(pos)
                    .setSize(18, 18)),
            (i, backgrounds, pos) -> builder.widget(
                SlotWidget.phantom(fluidOutputsInventory, i)
                    .setBackground(backgrounds)
                    .setPos(pos)
                    .setSize(18, 18)),
            ModularUITextures.ITEM_SLOT,
            ModularUITextures.FLUID_SLOT,
            this,
            uiProperties.maxItemInputs,
            uiProperties.maxItemOutputs,
            uiProperties.maxFluidInputs,
            uiProperties.maxFluidOutputs,
            SteamVariant.NONE,
            windowOffset);

        addProgressBar(builder, progressSupplier, windowOffset);
        addGregTechLogo(builder, windowOffset);

        for (Pair<IDrawable, Pair<Size, Pos2d>> specialTexture : uiProperties.specialTextures) {
            builder.widget(
                new DrawableWidget().setDrawable(specialTexture.getLeft())
                    .setSize(
                        specialTexture.getRight()
                            .getLeft())
                    .setPos(
                        specialTexture.getRight()
                            .getRight()
                            .add(windowOffset)));
        }

        return builder;
    }

    public void addProgressBar(ModularWindow.Builder builder, Supplier<Float> progressSupplier, Pos2d windowOffset) {
        assert uiProperties.progressBarTexture != null;
        builder.widget(
            new ProgressBar().setTexture(uiProperties.progressBarTexture.get(), 20)
                .setDirection(uiProperties.progressBarDirection)
                .setProgress(progressSupplier)
                .setSynced(false, false)
                .setPos(uiProperties.progressBarPos.add(windowOffset))
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

    public void drawNEIDescription(NEIRecipeInfo recipeInfo) {
        drawNEIEnergyInfo(recipeInfo);
        drawNEIDurationInfo(recipeInfo);
        drawNEISpecialInfo(recipeInfo);
        drawNEIRecipeOwnerInfo(recipeInfo);
    }

    protected void drawNEIEnergyInfo(NEIRecipeInfo recipeInfo) {
        GT_Recipe recipe = recipeInfo.recipe;
        Power power = recipeInfo.power;
        if (power.getEuPerTick() > 0) {
            drawNEIText(recipeInfo, trans("152", "Total: ") + power.getTotalPowerString());

            String amperage = power.getAmperageString();
            String powerUsage = power.getPowerUsageString();
            if (amperage == null || amperage.equals("unspecified") || powerUsage.contains("(OC)")) {
                drawNEIText(recipeInfo, trans("153", "Usage: ") + powerUsage);
                if (GT_Mod.gregtechproxy.mNEIOriginalVoltage) {
                    Power originalPower = createPower();
                    if (!(originalPower instanceof UnspecifiedEUPower)) {
                        originalPower.computePowerUsageAndDuration(recipe.mEUt, recipe.mDuration);
                        drawNEIText(recipeInfo, trans("275", "Original voltage: ") + originalPower.getVoltageString());
                    }
                }
                if (amperage != null && !amperage.equals("unspecified") && !amperage.equals("1")) {
                    drawNEIText(recipeInfo, trans("155", "Amperage: ") + amperage);
                }
            } else if (amperage.equals("1")) {
                drawNEIText(recipeInfo, trans("154", "Voltage: ") + power.getVoltageString());
            } else {
                drawNEIText(recipeInfo, trans("153", "Usage: ") + powerUsage);
                drawNEIText(recipeInfo, trans("154", "Voltage: ") + power.getVoltageString());
                drawNEIText(recipeInfo, trans("155", "Amperage: ") + amperage);
            }
        }
    }

    protected void drawNEIDurationInfo(NEIRecipeInfo recipeInfo) {
        Power power = recipeInfo.power;
        if (power.getDurationTicks() > 0) {
            String textToDraw = trans("158", "Time: ");
            if (GT_Mod.gregtechproxy.mNEIRecipeSecondMode) {
                textToDraw += power.getDurationStringSeconds();
                if (power.getDurationSeconds() <= 1.0d) {
                    textToDraw += String.format(" (%s)", power.getDurationStringTicks());
                }
            } else {
                textToDraw += power.getDurationStringTicks();
            }
            drawNEIText(recipeInfo, textToDraw);
        }
    }

    protected void drawNEISpecialInfo(NEIRecipeInfo recipeInfo) {
        String[] recipeDesc = recipeInfo.recipe.getNeiDesc();
        if (recipeDesc != null) {
            for (String s : recipeDesc) {
                drawOptionalNEIText(recipeInfo, s);
            }
        } else {
            drawNEITextMultipleLines(recipeInfo, neiProperties.neiSpecialInfoFormatter.format(recipeInfo));
        }
    }

    protected void drawNEIRecipeOwnerInfo(NEIRecipeInfo recipeInfo) {
        GT_Recipe recipe = recipeInfo.recipe;
        if (GT_Mod.gregtechproxy.mNEIRecipeOwner) {
            if (recipe.owners.size() > 1) {
                drawNEIText(
                    recipeInfo,
                    EnumChatFormatting.ITALIC + trans("273", "Original Recipe by: ")
                        + recipe.owners.get(0)
                            .getName());
                for (int i = 1; i < recipe.owners.size(); i++) {
                    drawNEIText(
                        recipeInfo,
                        EnumChatFormatting.ITALIC + trans("274", "Modified by: ")
                            + recipe.owners.get(i)
                                .getName());
                }
            } else if (!recipe.owners.isEmpty()) {
                drawNEIText(
                    recipeInfo,
                    EnumChatFormatting.ITALIC + trans("272", "Recipe by: ")
                        + recipe.owners.get(0)
                            .getName());
            }
        }
        if (GT_Mod.gregtechproxy.mNEIRecipeOwnerStackTrace && recipe.stackTraces != null
            && !recipe.stackTraces.isEmpty()) {
            drawNEIText(recipeInfo, "stackTrace:");
            // todo: good way to show all stacktraces
            for (StackTraceElement stackTrace : recipe.stackTraces.get(0)) {
                drawNEIText(recipeInfo, stackTrace.toString());
            }
        }
    }

    protected void drawNEIText(NEIRecipeInfo recipeInfo, String text) {
        drawNEIText(recipeInfo, text, 10);
    }

    /**
     * Draws text on NEI recipe.
     *
     * @param yShift y position to shift after this text
     */
    @SuppressWarnings("SameParameterValue")
    protected void drawNEIText(NEIRecipeInfo recipeInfo, String text, int yShift) {
        drawNEIText(recipeInfo, text, 10, yShift);
    }

    /**
     * Draws text on NEI recipe.
     *
     * @param xStart x position to start drawing
     * @param yShift y position to shift after this text
     */
    @SuppressWarnings("SameParameterValue")
    protected void drawNEIText(NEIRecipeInfo recipeInfo, String text, int xStart, int yShift) {
        net.minecraft.client.Minecraft.getMinecraft().fontRenderer
            .drawString(text, xStart, recipeInfo.yPos, neiTextColorOverride != -1 ? neiTextColorOverride : 0x000000);
        recipeInfo.yPos += yShift;
    }

    protected void drawOptionalNEIText(NEIRecipeInfo recipeInfo, @Nullable String text) {
        if (isStringValid(text) && !text.equals("unspecified")) {
            drawNEIText(recipeInfo, text, 10);
        }
    }

    protected void drawNEITextMultipleLines(NEIRecipeInfo recipeInfo, List<String> texts) {
        for (String text : texts) {
            drawNEIText(recipeInfo, text, 10);
        }
    }

    public List<String> handleNEIItemTooltip(ItemStack stack, List<String> currentTip,
        GT_NEI_DefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        for (PositionedStack pStack : neiCachedRecipe.mInputs) {
            if (stack == pStack.item) {
                if (pStack instanceof GT_NEI_DefaultHandler.FixedPositionedStack) {
                    currentTip = handleNEIItemInputTooltip(
                        currentTip,
                        (GT_NEI_DefaultHandler.FixedPositionedStack) pStack);
                }
                break;
            }
        }
        for (PositionedStack pStack : neiCachedRecipe.mOutputs) {
            if (stack == pStack.item) {
                if (pStack instanceof GT_NEI_DefaultHandler.FixedPositionedStack) {
                    currentTip = handleNEIItemOutputTooltip(
                        currentTip,
                        (GT_NEI_DefaultHandler.FixedPositionedStack) pStack);
                }
                break;
            }
        }
        return currentTip;
    }

    protected List<String> handleNEIItemInputTooltip(List<String> currentTip,
        GT_NEI_DefaultHandler.FixedPositionedStack pStack) {
        if (pStack.isNotConsumed()) {
            currentTip.add(GRAY + trans("151", "Does not get consumed in the process"));
        }
        return currentTip;
    }

    protected List<String> handleNEIItemOutputTooltip(List<String> currentTip,
        GT_NEI_DefaultHandler.FixedPositionedStack pStack) {
        if (pStack.isChanceBased()) {
            currentTip.add(GRAY + trans("150", "Chance: ") + pStack.getChanceText());
        }
        return currentTip;
    }

    public void drawNEIOverlays(GT_NEI_DefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        for (PositionedStack stack : neiCachedRecipe.mInputs) {
            if (stack instanceof GT_NEI_DefaultHandler.FixedPositionedStack) {
                drawNEIOverlayForInput((GT_NEI_DefaultHandler.FixedPositionedStack) stack);
            }
        }
        for (PositionedStack stack : neiCachedRecipe.mOutputs) {
            if (stack instanceof GT_NEI_DefaultHandler.FixedPositionedStack) {
                drawNEIOverlayForOutput((GT_NEI_DefaultHandler.FixedPositionedStack) stack);
            }
        }
    }

    protected void drawNEIOverlayForInput(GT_NEI_DefaultHandler.FixedPositionedStack stack) {
        if (stack.isNotConsumed()) {
            drawNEIOverlayText("NC", stack);
        }
    }

    protected void drawNEIOverlayForOutput(GT_NEI_DefaultHandler.FixedPositionedStack stack) {
        if (stack.isChanceBased()) {
            drawNEIOverlayText(stack.getChanceText(), stack);
        }
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

    public void updateNEITextColorOverride() {
        neiTextColorOverride = colorOverride.getTextColorOrDefault("nei", -1);
    }

    public Power createPower() {
        // By default, assume generic EU LV power with no overclocks
        Power power;
        if (neiProperties.showVoltageAmperage) {
            power = new EUPower((byte) 1, uiProperties.amperage);
        } else {
            power = new UnspecifiedEUPower((byte) 1, uiProperties.amperage);
        }
        return power;
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
