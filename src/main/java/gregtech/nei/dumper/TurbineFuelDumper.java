package gregtech.nei.dumper;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraftforge.fluids.FluidStack;

import codechicken.nei.config.DataDumper;
import gregtech.api.enums.Materials;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;

public class TurbineFuelDumper extends DataDumper {

    /** A single fuel row: name, EU/L value, and optional xlgt flag (null = not applicable). */
    private static final class FuelEntry {

        final String name;
        final double euL;
        final Boolean xlgt;

        FuelEntry(String name, double euL, Boolean xlgt) {
            this.name = name;
            this.euL = euL;
            this.xlgt = xlgt;
        }
    }

    /** A named group of fuel entries (corresponds to one turbine type). */
    private static final class FuelGroup {

        final String label;
        final List<FuelEntry> entries;

        FuelGroup(String label, List<FuelEntry> entries) {
            this.label = label;
            this.entries = entries;
        }
    }

    // Steam EU/L values are game-engine constants, not stored in RecipeMaps.
    // Factor 0.5 comes from MTELargeTurbineSteam.fluidIntoPower() (* 0.5f).
    // Dense variants are 1000x more energy-dense.
    private static final List<FuelEntry> STEAM_ENTRIES = Arrays.asList(
        new FuelEntry("Steam", 0.5, null),
        new FuelEntry("SH Steam", 1.0, null),
        new FuelEntry("SC Steam", 1.0, null),
        new FuelEntry("Dense Steam", 500.0, null),
        new FuelEntry("Dense SH Steam", 1000.0, null),
        new FuelEntry("Dense SC Steam", 1000.0, null));

    public TurbineFuelDumper() {
        super("tools.dump.gt5u.fuels");
    }

    @Override
    public String[] header() {
        return new String[] { "type", "name", "eu_l", "xlgt" };
    }

    @Override
    public Iterable<String[]> dump(int mode) {
        List<String[]> rows = new ArrayList<>();
        for (FuelGroup group : buildGroups()) {
            for (FuelEntry entry : group.entries) {
                rows.add(
                    new String[] { group.label, entry.name, DumperUtils.formatDouble(entry.euL),
                        entry.xlgt != null ? String.valueOf(entry.xlgt) : "" });
            }
        }
        return rows;
    }

    @Override
    public String renderName() {
        return "Turbine Fuel Data";
    }

    @Override
    public String getFileExtension() {
        return getMode() == 1 ? ".json" : ".csv";
    }

    @Override
    public int modeCount() {
        return 2;
    }

    @Override
    public String modeButtonText() {
        return getMode() == 0 ? "CSV" : "JSON";
    }

    @Override
    public void dumpTo(File file) throws IOException {
        if (getMode() == 1) dumpJson(file, buildGroups());
        else super.dumpTo(file);
    }

    /**
     * Builds the list of fuel groups from RecipeMaps and hardcoded steam constants.
     * To add a new turbine fuel type, add a new entry here.
     */
    private static List<FuelGroup> buildGroups() {
        List<FuelGroup> groups = new ArrayList<>();
        groups.add(buildRecipeGroup("gas", RecipeMaps.gasTurbineFuels.getAllRecipes(), true));
        groups.add(buildRecipeGroup("plasma", RecipeMaps.plasmaFuels.getAllRecipes(), false));
        groups.add(new FuelGroup("steam", STEAM_ENTRIES));
        return groups;
    }

    private static FuelGroup buildRecipeGroup(String label, Iterable<GTRecipe> recipes, boolean hasXlgt) {
        List<FuelEntry> entries = new ArrayList<>();
        for (GTRecipe recipe : recipes) {
            FluidStack fluid = getFirstFluid(recipe);
            if (fluid == null) continue;
            String name = fluid.getFluid()
                .getLocalizedName(fluid);
            double euL = recipe.mSpecialValue;
            Boolean xlgt = hasXlgt ? !isBenzene(fluid) : null;
            entries.add(new FuelEntry(name, euL, xlgt));
        }
        return new FuelGroup(label, entries);
    }

    private static void dumpJson(File file, List<FuelGroup> groups) throws IOException {
        try (PrintWriter w = new PrintWriter(file)) {
            w.println("{");
            List<String> lines = new ArrayList<>();
            for (int gi = 0; gi < groups.size(); gi++) {
                FuelGroup group = groups.get(gi);
                boolean lastGroup = gi == groups.size() - 1;
                w.printf("  %s: [%n", DumperUtils.jsonString(group.label));
                lines.clear();
                for (FuelEntry entry : group.entries) {
                    if (entry.xlgt != null) {
                        lines.add(
                            String.format(
                                "    {\"name\": %s, \"eu_l\": %s, \"xlgt\": %b}",
                                DumperUtils.jsonString(entry.name),
                                DumperUtils.formatDouble(entry.euL),
                                entry.xlgt));
                    } else {
                        lines.add(
                            String.format(
                                "    {\"name\": %s, \"eu_l\": %s}",
                                DumperUtils.jsonString(entry.name),
                                DumperUtils.formatDouble(entry.euL)));
                    }
                }
                DumperUtils.printLines(w, lines);
                w.println(lastGroup ? "  ]" : "  ],");
            }
            w.println("}");
        }
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
