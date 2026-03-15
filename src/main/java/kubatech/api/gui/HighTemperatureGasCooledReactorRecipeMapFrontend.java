package kubatech.api.gui;

import static gregtech.api.enums.Mods.KubaTech;
import static kubatech.api.gui.KubaTechUITextures.PICTURE_KUBATECH_LOGO;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.modularui.api.drawable.FallbackableUITexture;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;

import codechicken.nei.PositionedStack;
import gregtech.api.enums.Materials;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.nei.GTNEIDefaultHandler;
import gregtech.nei.RecipeDisplayInfo;
import kubatech.loaders.HTGRLoader;

public class HighTemperatureGasCooledReactorRecipeMapFrontend extends RecipeMapFrontend {

    public static final UITexture PROGRESSBAR = UITexture.fullImage(KubaTech.ID, "gui/reactor_chart");

    public HighTemperatureGasCooledReactorRecipeMapFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(
            uiPropertiesBuilder.progressBarTexture(new FallbackableUITexture(PROGRESSBAR))
                .progressBarPos(new Pos2d(3, 3))
                .progressBarDirection(ProgressBar.Direction.DOWN)
                .logoPos(new Pos2d(145, 237))
                .logo(PICTURE_KUBATECH_LOGO)
                .logoSize(new Size(13, 15)),
            neiPropertiesBuilder.recipeBackgroundSize(new Size(170, 245)));
    }

    @Override
    public void addProgressBar(ModularWindow.Builder builder, @NotNull Supplier<Float> progressSupplier,
        @NotNull Pos2d windowOffset) {
        assert uiProperties.progressBarTexture != null;
        builder.widget(
            new ProgressBar().setTexture(uiProperties.progressBarTexture.get(), 170)
                .setDirection(uiProperties.progressBarDirection)
                .setProgress(() -> (0.31f + progressSupplier.get() * 0.26f)) // start 76 = 0.31 end 138 = 0.57 d = 0.26
                .setSynced(false, false)
                .setPos(uiProperties.progressBarPos.add(windowOffset))
                .setSize(new Size(170, 245)));
    }

    @Override
    public @NotNull List<Pos2d> getItemInputPositions(int itemInputCount) {
        ArrayList<Pos2d> positions = new ArrayList<>();
        positions.add(new Pos2d(7, 25));
        return positions;
    }

    @Override
    public @NotNull List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        ArrayList<Pos2d> positions = new ArrayList<>();
        int x = 7, y = 197;
        for (int i = 0; i < itemOutputCount; i++) {
            positions.add(new Pos2d(x, y));
            x += 18;
        }
        return positions;
    }

    @Override
    public void drawNEIOverlays(GTNEIDefaultHandler.@NotNull CachedDefaultRecipe neiCachedRecipe) {
        super.drawNEIOverlays(neiCachedRecipe);
        if (neiCachedRecipe.mInputs.size() == 1) {
            GTRecipe recipe = neiCachedRecipe.mRecipe;
            Materials material = GTOreDictUnificator.getAssociation(recipe.mInputs[0]).mMaterial.mMaterial;

            // FUEL
            int x = 26, y = -3;
            for (Pair<ItemStack, Integer> fuel : recipe.getMetadata(HTGRLoader.FUEL)) {
                neiCachedRecipe.mInputs.add(new PositionedStack(fuel.getLeft(), x, y, false));
                y += 18;
            }

            // SHELL
            ItemStack[] shells = recipe.getMetadata(HTGRLoader.SHELL);
            neiCachedRecipe.mInputs.add(new PositionedStack(shells[0], 3, 70, false));
            neiCachedRecipe.mInputs.add(new PositionedStack(shells[1], 3, 108, false));
            neiCachedRecipe.mInputs.add(new PositionedStack(shells[2], 3, 147, false));

            // outputs
            neiCachedRecipe.mOutputs
                .add(new PositionedStack(HTGRLoader.HTGR_ITEM.createTRISOMixture(material), 75, 57, false));
            neiCachedRecipe.mOutputs
                .add(new PositionedStack(HTGRLoader.HTGR_ITEM.createIncompleteBISOFuel(material), 75, 89, false));
            neiCachedRecipe.mOutputs
                .add(new PositionedStack(HTGRLoader.HTGR_ITEM.createIncompleteTRISOFuel(material), 75, 121, false));
            ItemStack ofuel = HTGRLoader.HTGR_ITEM.createTRISOFuel(material);
            neiCachedRecipe.mOutputs.add(new PositionedStack(ofuel, 75, 153, false));
            neiCachedRecipe.mOutputs.add(new PositionedStack(ofuel, 132, 15, false));
            neiCachedRecipe.mOutputs
                .add(new PositionedStack(HTGRLoader.HTGR_ITEM.createBurnedTRISOFuel(material), 132, 153, false));

            // recycling
            x = 3;
            y = 187;
            x += recipe.mOutputs.length * 18;
            for (Pair<ItemStack, Integer> fuel : recipe.getMetadata(HTGRLoader.FUEL)) {
                ItemStack stack = fuel.getLeft()
                    .copy();
                stack.stackSize = fuel.getRight();
                neiCachedRecipe.mInputs.add(new PositionedStack(stack, x, y, false));
                x += 18;
                if (x >= 75 + 18) {
                    x = 3;
                    y += 18;
                }
            }
            for (ItemStack shell : recipe.getMetadata(HTGRLoader.SHELL)) {
                neiCachedRecipe.mOutputs
                    .add(new GTNEIDefaultHandler.FixedPositionedStack(shell, true, x, y, 9500, false, false));
                x += 18;
                if (x >= 75 + 18) {
                    x = 3;
                    y += 18;
                }
            }

        }
    }

    @Override
    public void drawDescription(RecipeDisplayInfo recipeInfo) {
        drawRecipeOwnerInfo(recipeInfo);
    }
}
