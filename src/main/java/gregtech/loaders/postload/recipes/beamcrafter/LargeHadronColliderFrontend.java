package gregtech.loaders.postload.recipes.beamcrafter;

import java.util.List;
import java.util.function.Supplier;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.drawable.FallbackableUITexture;
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
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.nei.RecipeDisplayInfo;

public class LargeHadronColliderFrontend extends RecipeMapFrontend {

    private static final Pos2d realProgressBarPos = new Pos2d(3, 3);

    public LargeHadronColliderFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(
            uiPropertiesBuilder.logoPos(new Pos2d(152, 97))
                .progressBarTexture(new FallbackableUITexture(GTUITextures.PROGRESSBAR_LHC_EM))
                .progressBarSize(new Size(0, 0))
                .progressBarPos(new Pos2d(0, 0)),
            neiPropertiesBuilder.recipeBackgroundSize(new Size(170, 164)));

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
                .setSize(new Size(170, 82)));
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

    @Override
    public void drawDescription(RecipeDisplayInfo recipeInfo) {}

    @Override
    public void drawEnergyInfo(RecipeDisplayInfo recipeInfo) {}

    @Override
    public List<Pos2d> getItemInputPositions(int itemInputCount) {
        return ImmutableList.of(new Pos2d(28, 31));
    }

    @Override
    public List<Pos2d> getItemOutputPositions(int itemInputCount) {
        return ImmutableList.of(
            new Pos2d(96, 15),
            new Pos2d(115, 11),
            new Pos2d(134, 11),
            new Pos2d(153, 15),
            new Pos2d(96, 55),
            new Pos2d(115, 59),
            new Pos2d(134, 59),
            new Pos2d(153, 55));
    }

}
