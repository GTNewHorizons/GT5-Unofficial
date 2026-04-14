package gregtech.api.recipe.maps;

import javax.annotation.ParametersAreNonnullByDefault;

import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.GTNEIDefaultHandler;

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
    public void addProgressBar(ModularWindow.Builder builder, GTNEIDefaultHandler.NEITemplateContext ctx) {
        assert uiProperties.progressBarTexture != null;
        UITexture texture = uiProperties.progressBarTexture.get();
        builder.widget(
            new ProgressBar().setTexture(texture, 170)
                .setDirection(uiProperties.progressBarDirection)
                .setProgress(ctx.progressSupplier)
                .setSynced(false, false)
                .setPos(realProgressBarPos.add(ctx.windowOffset))
                .setSize(new Size(170, overlayHeight)));
    }

    @Override
    public ModularWindow.Builder createNEITemplate(GTNEIDefaultHandler.NEITemplateContext ctx) {
        // Override regular createNEITemplate method, so we can remove the background texture with the ugly border.
        return super.createNEITemplate(ctx).setBackground();
    }
}
