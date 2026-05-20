package gregtech.nei.dumper;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.fluids.FluidStack;

import codechicken.nei.config.DataDumper;
import gregtech.api.enums.Materials;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;

public class TurbineFuelDumper extends DataDumper {

    public TurbineFuelDumper() {
        super("tools.dump.gt5u.fuels");
    }

    @Override
    public String[] header() {
        return new String[0];
    }

    @Override
    public Iterable<String[]> dump(int mode) {
        return java.util.Collections.emptyList();
    }

    @Override
    public String renderName() {
        return "Turbine Fuel Data";
    }

    @Override
    public String getFileExtension() {
        return ".json";
    }

    @Override
    public int modeCount() {
        return 1;
    }

    @Override
    public void dumpTo(File file) throws IOException {
        try (PrintWriter w = new PrintWriter(file)) {
            w.println("{");
            w.println("  \"gas\": [");
            writeGasFuels(w);
            w.println("  ],");
            w.println("  \"plasma\": [");
            writePlasmaFuels(w);
            w.println("  ],");
            w.println("  \"steam\": [");
            writeSteamFuels(w);
            w.println("  ]");
            w.println("}");
        }
    }

    private void writeGasFuels(PrintWriter w) {
        List<String> lines = new ArrayList<>();
        for (GTRecipe recipe : RecipeMaps.gasTurbineFuels.getAllRecipes()) {
            FluidStack fluid = getFirstFluid(recipe);
            if (fluid == null) continue;
            String name = fluid.getFluid()
                .getLocalizedName(fluid);
            double euL = recipe.mSpecialValue * 1000.0;
            boolean xlgt = !isBenzene(fluid);
            lines.add(
                String.format(
                    "    {\"name\": %s, \"eu_l\": %s, \"xlgt\": %b}",
                    DumperUtils.jsonString(name),
                    DumperUtils.formatDouble(euL),
                    xlgt));
        }
        DumperUtils.printLines(w, lines);
    }

    private void writePlasmaFuels(PrintWriter w) {
        List<String> lines = new ArrayList<>();
        for (GTRecipe recipe : RecipeMaps.plasmaFuels.getAllRecipes()) {
            FluidStack fluid = getFirstFluid(recipe);
            if (fluid == null) continue;
            String name = fluid.getFluid()
                .getLocalizedName(fluid);
            double euL = recipe.mSpecialValue * 1000.0;
            lines.add(
                String.format(
                    "    {\"name\": %s, \"eu_l\": %s}",
                    DumperUtils.jsonString(name),
                    DumperUtils.formatDouble(euL)));
        }
        DumperUtils.printLines(w, lines);
    }

    private void writeSteamFuels(PrintWriter w) {
        // Steam EU/L values are game-engine constants, not stored in RecipeMaps.
        // Factor 0.5 comes from MTELargeTurbineSteam.fluidIntoPower() (* 0.5f).
        // Dense variants are 1000x more energy-dense.
        double[] euValues = { 0.5, 1.0, 1.0, 500.0, 1000.0, 1000.0 };
        String[] names = { "Steam", "SH Steam", "SC Steam", "Dense Steam", "Dense SH Steam", "Dense SC Steam" };
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            lines.add(
                String.format(
                    "    {\"name\": %s, \"eu_l\": %s}",
                    DumperUtils.jsonString(names[i]),
                    DumperUtils.formatDouble(euValues[i])));
        }
        DumperUtils.printLines(w, lines);
    }

    private static FluidStack getFirstFluid(GTRecipe recipe) {
        if (recipe.mFluidInputs != null && recipe.mFluidInputs.length > 0
            && recipe.mFluidInputs[0] != null
            && recipe.mFluidInputs[0].getFluid() != null) {
            return recipe.mFluidInputs[0];
        }
        // Old-style recipes use item inputs (filled buckets) instead of fluid inputs
        if (recipe.mInputs != null && recipe.mInputs.length > 0 && recipe.mInputs[0] != null) {
            return GTUtility.getFluidForFilledItem(recipe.mInputs[0], true);
        }
        return null;
    }

    private static boolean isBenzene(FluidStack fluid) {
        return Materials.Benzene.mFluid != null && fluid.getFluid() == Materials.Benzene.mFluid;
    }

}
