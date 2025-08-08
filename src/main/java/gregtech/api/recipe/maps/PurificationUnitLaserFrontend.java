package gregtech.api.recipe.maps;

import java.util.List;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizons.modularui.api.drawable.FallbackableUITexture;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;

import codechicken.nei.PositionedStack;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.common.tileentities.machines.multi.purification.MTEPurificationUnitUVTreatment;
import gregtech.nei.GTNEIDefaultHandler;

public class PurificationUnitLaserFrontend extends PurificationUnitRecipeMapFrontend {

    public PurificationUnitLaserFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(
            120,
            uiPropertiesBuilder.logoPos(new Pos2d(147, 102))
                .progressBarTexture(new FallbackableUITexture(GTUITextures.PROGRESSBAR_UV_TREATMENT)),
            neiPropertiesBuilder.recipeBackgroundSize(new Size(170, 120)));
    }

    @Override
    public @NotNull List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        return ImmutableList.of(new Pos2d(10, 89));
    }

    @Override
    public @NotNull List<Pos2d> getFluidOutputPositions(int fluidOutputCount) {
        return ImmutableList.of(new Pos2d(147, 89));
    }

    @Override
    public void drawNEIOverlays(GTNEIDefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        final int numLenses = MTEPurificationUnitUVTreatment.LENS_ITEMS.size();
        List<Pos2d> positions = ImmutableList.of(
            new Pos2d(62, -6),
            new Pos2d(40, -1),
            new Pos2d(32, 20),
            new Pos2d(52, 27),
            new Pos2d(75, 29),
            new Pos2d(98, 27),
            new Pos2d(118, 20),
            new Pos2d(110, -1),
            new Pos2d(88, -6));

        if (neiCachedRecipe.mInputs.size() < numLenses) {
            // Put in lens items
            for (int i = 0; i < numLenses; ++i) {
                Pos2d position = positions.get(i);
                ItemStack lens = MTEPurificationUnitUVTreatment.LENS_ITEMS.get(i);
                neiCachedRecipe.mInputs.add(new PositionedStack(lens, position.x, position.y, false));
            }
        }

        super.drawNEIOverlays(neiCachedRecipe);
    }
}
