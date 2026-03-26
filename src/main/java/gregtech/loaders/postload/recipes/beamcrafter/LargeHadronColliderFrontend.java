package gregtech.loaders.postload.recipes.beamcrafter;

import codechicken.nei.PositionedStack;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizons.modularui.api.drawable.FallbackableUITexture;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.recipe.maps.PurificationUnitRecipeMapFrontend;
import gregtech.api.util.GTUtility;
import gregtech.api.util.OverclockCalculator;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.nei.GTNEIDefaultHandler;
import gregtech.nei.RecipeDisplayInfo;
import gtnhlanth.util.Util;
import net.minecraft.util.StatCollector;

import java.util.List;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gtnhlanth.common.beamline.Particle.isParticle;

public class LargeHadronColliderFrontend extends PurificationUnitRecipeMapFrontend {

    public LargeHadronColliderFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
                                             NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(
            82,
            uiPropertiesBuilder.logoPos(new Pos2d(160, 100))
                .progressBarTexture(new FallbackableUITexture(GTUITextures.PROGRESSBAR_LHC_EM))
                .logoPos(new Pos2d(152, 97)),
            neiPropertiesBuilder.recipeBackgroundSize(new Size(170, 164)));
    }

    @Override
    public void drawDescription(RecipeDisplayInfo recipeInfo) {
    }

    @Override
    public void drawEnergyInfo(RecipeDisplayInfo recipeInfo) {
    }

    @Override
    public List<Pos2d> getItemInputPositions(int itemInputCount) {
        return ImmutableList.of(new Pos2d(28,31));
    }

    @Override
    public List<Pos2d> getItemOutputPositions(int itemInputCount) {
        return ImmutableList.of(
            new Pos2d(96,15),
            new Pos2d(115,11),
            new Pos2d(134,11),
            new Pos2d(153,15),
            new Pos2d(96,55),
            new Pos2d(115,59),
            new Pos2d(134,59),
            new Pos2d(153,55));
    }

}
