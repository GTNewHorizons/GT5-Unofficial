package gregtech.nei.dumper;

import static gregtech.common.items.IDMetaTool01.TURBINE;
import static gregtech.common.items.IDMetaTool01.TURBINE_HUGE;
import static gregtech.common.items.IDMetaTool01.TURBINE_LARGE;
import static gregtech.common.items.IDMetaTool01.TURBINE_SMALL;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.minecraft.item.ItemStack;

import bartworks.system.material.Werkstoff;
import codechicken.nei.config.DataDumper;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.TurbineStatCalculator;
import gregtech.common.items.MetaGeneratedTool01;

public class TurbineRotorDumper extends DataDumper {

    private static final int[] TOOL_IDS = { TURBINE_SMALL.ID, TURBINE.ID, TURBINE_LARGE.ID, TURBINE_HUGE.ID };
    private static final String[] SIZE_NAMES = { "Small", "Normal", "Large", "Huge" };
    private static final int[] DUR_MULTS = { 1, 2, 3, 4 };

    public TurbineRotorDumper() {
        super("tools.dump.gt5u.rotors");
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
        return "Turbine Rotor Data";
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
        List<Materials> materials = collectMaterials();
        materials.sort(
            Comparator.comparingInt((Materials m) -> (int) m.mToolQuality)
                .reversed()
                .thenComparing(m -> m.mDefaultLocalName));
        try (PrintWriter w = new PrintWriter(file)) {
            w.println("[");
            for (int mi = 0; mi < materials.size(); mi++) {
                Materials mat = materials.get(mi);
                String comma = (mi < materials.size() - 1) ? "," : "";
                writeMaterial(w, mat, comma);
            }
            w.println("]");
        }
    }

    private List<Materials> collectMaterials() {
        List<Materials> result = new ArrayList<>();
        for (Materials mat : GregTechAPI.sGeneratedMaterials) {
            if (mat == null) continue;
            if (mat.mDurability <= 0 || mat.mToolSpeed <= 0) continue;
            if (GTOreDictUnificator.get(OrePrefixes.turbineBlade, mat, 1L) == null) continue;
            result.add(mat);
        }
        // BartWorks materials are not in sGeneratedMaterials, iterate separately
        for (Werkstoff werkstoff : Werkstoff.werkstoffHashSet) {
            if (!werkstoff.hasItemType(OrePrefixes.turbineBlade)) continue;
            Materials mat = werkstoff.getBridgeMaterial();
            if (mat == null) continue;
            if (mat.mDurability <= 0 || mat.mToolSpeed <= 0) continue;
            result.add(mat);
        }
        return result;
    }

    private void writeMaterial(PrintWriter w, Materials mat, String trailingComma) {
        int overflowTier = (int) (1 + Math.min(2.0, mat.mToolQuality / 3.0));
        w.println("  {");
        w.printf("    \"name\": %s,%n", DumperUtils.jsonString(mat.mDefaultLocalName + " (" + mat.mToolQuality + ")"));
        w.printf("    \"tier\": %d,%n", (int) mat.mToolQuality);
        w.printf("    \"mining_speed\": %s,%n", DumperUtils.formatDouble(mat.mToolSpeed));
        w.printf("    \"base_durability\": %s,%n", DumperUtils.formatDouble(mat.mDurability * 100.0));
        w.printf("    \"overflow_tier\": %d,%n", overflowTier);
        w.println("    \"sizes\": {");

        for (int si = 0; si < TOOL_IDS.length; si++) {
            String sizeComma = (si < TOOL_IDS.length - 1) ? "," : "";
            ItemStack stack = MetaGeneratedTool01.INSTANCE.getToolWithStats(TOOL_IDS[si], 1, mat, mat, null);
            if (stack == null) {
                w.printf("      %s: null%s%n", DumperUtils.jsonString(SIZE_NAMES[si]), sizeComma);
                continue;
            }
            TurbineStatCalculator calc = new TurbineStatCalculator(MetaGeneratedTool01.INSTANCE, stack);
            writeSize(w, calc, SIZE_NAMES[si], DUR_MULTS[si], sizeComma);
        }

        w.println("    }");
        w.println("  }" + trailingComma);
    }

    private void writeSize(PrintWriter w, TurbineStatCalculator c, String sizeName, int durMult, String trailingComma) {
        w.printf("      %s: {%n", DumperUtils.jsonString(sizeName));
        w.printf("        \"steam_tight_eff\": %s,%n", DumperUtils.formatDouble(c.getSteamEfficiency()));
        w.printf("        \"steam_loose_eff\": %s,%n", DumperUtils.formatDouble(c.getLooseSteamEfficiency()));
        w.printf("        \"steam_opt_flow_tight\": %s,%n", DumperUtils.formatDouble(c.getOptimalSteamFlow()));
        w.printf("        \"steam_opt_flow_loose\": %s,%n", DumperUtils.formatDouble(c.getOptimalLooseSteamFlow()));
        w.printf("        \"steam_power_tight\": %s,%n", DumperUtils.formatDouble(c.getOptimalSteamEUt()));
        w.printf("        \"steam_power_loose\": %s,%n", DumperUtils.formatDouble(c.getOptimalLooseSteamEUt()));
        w.printf("        \"gas_tight_eff\": %s,%n", DumperUtils.formatDouble(c.getGasEfficiency()));
        w.printf("        \"gas_loose_eff\": %s,%n", DumperUtils.formatDouble(c.getLooseGasEfficiency()));
        w.printf("        \"gas_opt_flow_tight\": %s,%n", DumperUtils.formatDouble(c.getOptimalGasFlow()));
        w.printf("        \"gas_opt_flow_loose\": %s,%n", DumperUtils.formatDouble(c.getOptimalLooseGasFlow()));
        w.printf("        \"gas_power_tight\": %s,%n", DumperUtils.formatDouble(c.getOptimalGasEUt()));
        w.printf("        \"gas_power_loose\": %s,%n", DumperUtils.formatDouble(c.getOptimalLooseGasEUt()));
        w.printf("        \"plasma_tight_eff\": %s,%n", DumperUtils.formatDouble(c.getPlasmaEfficiency()));
        w.printf("        \"plasma_loose_eff\": %s,%n", DumperUtils.formatDouble(c.getLoosePlasmaEfficiency()));
        w.printf("        \"plasma_opt_flow_tight\": %s,%n", DumperUtils.formatDouble(c.getOptimalPlasmaFlow()));
        w.printf("        \"plasma_opt_flow_loose\": %s,%n", DumperUtils.formatDouble(c.getOptimalLoosePlasmaFlow()));
        w.printf("        \"plasma_power_tight\": %s,%n", DumperUtils.formatDouble(c.getOptimalPlasmaEUt()));
        w.printf("        \"plasma_power_loose\": %s,%n", DumperUtils.formatDouble(c.getOptimalLoosePlasmaEUt()));
        w.printf("        \"dur_mult\": %d%n", durMult);
        w.printf("      }%s%n", trailingComma);
    }

}
