package gregtech.api.recipe.maps;

import java.util.function.Supplier;

import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang3.tuple.Pair;

import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import gregtech.api.enums.SteamVariant;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.gui.modularui.UIHelper;

// Makes some small changes to the default recipe frontend to display custom backgrounds and progress bars
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PurificationUnitRecipeMapFrontend extends RecipeMapFrontend {

    private final int overlayHeight;
    private static final Pos2d realProgressBarPos = new Pos2d(3, 3);

    public PurificationUnitRecipeMapFrontend(int overlayHeight, BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(
            uiPropertiesBuilder.progressBarSize(new Size(0, 0))
                .progressBarPos(new Pos2d(0, 0)),
            neiPropertiesBuilder);
        this.overlayHeight = overlayHeight;
    }

    @Override
    public void addProgressBar(ModularWindow.Builder builder, Supplier<Float> progressSupplier, Pos2d windowOffset) {
        assert uiProperties.progressBarTexture != null;
        UITexture texture = uiProperties.progressBarTexture.get();
        builder.widget(
            new ProgressBar().setTexture(texture, 170)
                .setDirection(uiProperties.progressBarDirection)
                .setProgress(progressSupplier)
                .setSynced(false, false)
                .setPos(realProgressBarPos.add(windowOffset))
                .setSize(new Size(170, overlayHeight)));
    }

    @Override
    public ModularWindow.Builder createNEITemplate(IItemHandlerModifiable itemInputsInventory,
        IItemHandlerModifiable itemOutputsInventory, IItemHandlerModifiable specialSlotInventory,
        IItemHandlerModifiable fluidInputsInventory, IItemHandlerModifiable fluidOutputsInventory,
        Supplier<Float> progressSupplier, Pos2d windowOffset) {
        // Override regular createNEITemplate method, so we can remove the background texture with the ugly border.
        ModularWindow.Builder builder = ModularWindow.builder(neiProperties.recipeBackgroundSize);

        // First draw progress bar in background
        if (uiProperties.useProgressBar) {
            addProgressBar(builder, progressSupplier, windowOffset);
        }

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
            uiProperties,
            uiProperties.maxItemInputs,
            uiProperties.maxItemOutputs,
            uiProperties.maxFluidInputs,
            uiProperties.maxFluidOutputs,
            SteamVariant.NONE,
            windowOffset);

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
}
