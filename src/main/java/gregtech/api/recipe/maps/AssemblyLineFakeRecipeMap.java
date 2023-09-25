package gregtech.api.recipe.maps;

import java.awt.Rectangle;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;

import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.util.GT_Recipe;
import gregtech.common.gui.modularui.UIHelper;

public class AssemblyLineFakeRecipeMap extends GT_Recipe.GT_Recipe_Map {

    public AssemblyLineFakeRecipeMap(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName,
        String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount, int aMinimalInputItems,
        int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre, int aNEISpecialValueMultiplier,
        String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI, boolean aNEIAllowed) {
        super(
            aRecipeList,
            aUnlocalizedName,
            aLocalName,
            aNEIName,
            aNEIGUIPath,
            aUsualInputCount,
            aUsualOutputCount,
            aMinimalInputItems,
            aMinimalInputFluids,
            aAmperage,
            aNEISpecialValuePre,
            aNEISpecialValueMultiplier,
            aNEISpecialValuePost,
            aShowVoltageAmperageInNEI,
            aNEIAllowed);
        setNEITransferRect(new Rectangle(146, 26, 10, 18));
    }

    @Override
    public List<Pos2d> getItemInputPositions(int itemInputCount) {
        return UIHelper.getGridPositions(itemInputCount, 16, 8, 4);
    }

    @Override
    public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        return Collections.singletonList(new Pos2d(142, 8));
    }

    @Override
    public Pos2d getSpecialItemPosition() {
        return new Pos2d(142, 44);
    }

    @Override
    public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        return UIHelper.getGridPositions(fluidInputCount, 106, 8, 1);
    }

    @Override
    public void addProgressBarUI(ModularWindow.Builder builder, Supplier<Float> progressSupplier, Pos2d windowOffset) {
        int bar1Width = 17;
        int bar2Width = 18;
        builder.widget(
            new ProgressBar().setTexture(GT_UITextures.PROGRESSBAR_ASSEMBLY_LINE_1, 17)
                .setDirection(ProgressBar.Direction.RIGHT)
                .setProgress(() -> progressSupplier.get() * ((float) (bar1Width + bar2Width) / bar1Width))
                .setSynced(false, false)
                .setPos(new Pos2d(88, 8).add(windowOffset))
                .setSize(bar1Width, 72));
        builder.widget(
            new ProgressBar().setTexture(GT_UITextures.PROGRESSBAR_ASSEMBLY_LINE_2, 18)
                .setDirection(ProgressBar.Direction.RIGHT)
                .setProgress(
                    () -> (progressSupplier.get() - ((float) bar1Width / (bar1Width + bar2Width)))
                        * ((float) (bar1Width + bar2Width) / bar2Width))
                .setSynced(false, false)
                .setPos(new Pos2d(124, 8).add(windowOffset))
                .setSize(bar2Width, 72));
        builder.widget(
            new ProgressBar().setTexture(GT_UITextures.PROGRESSBAR_ASSEMBLY_LINE_3, 18)
                .setDirection(ProgressBar.Direction.UP)
                .setProgress(progressSupplier)
                .setSynced(false, false)
                .setPos(new Pos2d(146, 26).add(windowOffset))
                .setSize(10, 18));
    }
}
