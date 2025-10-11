package gregtech.api.recipe.maps;

import static gregtech.api.util.GTRecipeConstants.*;
import static gregtech.api.util.GTUtility.formatNumbers;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.StatCollector;

import com.gtnewhorizons.modularui.api.math.Pos2d;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.nei.RecipeDisplayInfo;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ECCFFrontend extends RecipeMapFrontend {

    public ECCFFrontend(BasicUIPropertiesBuilder uiProps, NEIRecipePropertiesBuilder neiProps) {
        super(uiProps, neiProps);
    }

    @Override
    public List<Pos2d> getItemInputPositions(int c) {
        return UIHelper.getGridPositions(c, 8, 8, 4, 2);
    }

    @Override
    public List<Pos2d> getItemOutputPositions(int c) {
        return UIHelper.getGridPositions(c, 96, 8, 4, 2);
    }

    @Override
    public List<Pos2d> getFluidInputPositions(int c) {
        return UIHelper.getGridPositions(c, 8, 44, 4, 2);
    }

    @Override
    public List<Pos2d> getFluidOutputPositions(int c) {
        return UIHelper.getGridPositions(c, 96, 44, 4, 2);
    }

    @Override
    protected void drawDurationInfo(RecipeDisplayInfo r) {
        long pressure = r.recipe.getMetadataOrDefault(ECCF_PRESSURE, 101_000);
        long temp = r.recipe.getMetadataOrDefault(ECCF_TEMPERATURE, 300);
        long pressureDelta = r.recipe.getMetadataOrDefault(ECCF_PRESSURE_DELTA, 0);
        long tempDelta = r.recipe.getMetadataOrDefault(ECCF_TEMPERATURE_DELTA, 0);
        long duration = r.recipe.mDuration;

        double rp = 1.5 * Math.pow(pressure, 0.55);
        double rt = 1.5 * Math.pow(temp, 0.55);

        r.drawText(
            StatCollector.translateToLocal("GT5U.nei.time") + " "
                + formatNumbers(duration / 20d)
                + " secs"
                + (duration < 20 ? " (" + duration + " ticks)" : ""));
        r.drawText(
            StatCollector.translateToLocalFormatted(
                "GT5U.nei.pressure",
                pressure < 10_000 ? formatNumbers(pressure - rp) + " – " + formatNumbers(pressure + rp) + " Pa"
                    : formatNumbers((int) ((pressure - rp) / 1000)) + " – "
                        + formatNumbers((int) ((pressure + rp) / 1000))
                        + " kPa"));
        r.drawText(
            StatCollector.translateToLocalFormatted(
                "GT5U.nei.temperature",
                formatNumbers(temp - rt) + " – " + formatNumbers(temp + rt) + " K"));
        if (pressureDelta != 0) r
            .drawText(StatCollector.translateToLocalFormatted("GT5U.nei.pressure_delta", formatNumbers(pressureDelta)));
        if (tempDelta != 0)
            r.drawText(StatCollector.translateToLocalFormatted("GT5U.nei.temperature_delta", formatNumbers(tempDelta)));
    }
}
