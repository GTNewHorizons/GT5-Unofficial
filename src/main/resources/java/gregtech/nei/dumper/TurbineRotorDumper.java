package gregtech.nei.dumper;

import static gregtech.common.items.IDMetaTool01.TURBINE;
import static gregtech.common.items.IDMetaTool01.TURBINE_HUGE;
import static gregtech.common.items.IDMetaTool01.TURBINE_LARGE;
import static gregtech.common.items.IDMetaTool01.TURBINE_SMALL;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.minecraft.item.ItemStack;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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

    public TurbineRotorDumper() {
        super("tools.dump.gt5u.rotors");
    }

    @Override
    public String[] header() {
        return new String[] { "name", "tier", "mining_speed", "base_durability", "overflow_tier", "size", "dur_mult",
            "steam_tight_eff", "steam_loose_eff", "steam_opt_flow_tight", "steam_opt_flow_loose", "steam_power_tight",
            "steam_power_loose", "gas_tight_eff", "gas_loose_eff", "gas_opt_flow_tight", "gas_opt_flow_loose",
            "gas_power_tight", "gas_power_loose", "plasma_tight_eff", "plasma_loose_eff", "plasma_opt_flow_tight",
            "plasma_opt_flow_loose", "plasma_power_tight", "plasma_power_loose" };
    }

    @Override
    public Iterable<String[]> dump(int mode) {
        List<String[]> rows = new ArrayList<>();
        for (Materials mat : collectMaterials()) {
            int overflowTier = (int) (1 + Math.min(2.0, mat.mToolQuality / 3.0));
            for (int si = 0; si < TOOL_IDS.length; si++) {
                ItemStack stack = MetaGeneratedTool01.INSTANCE.getToolWithStats(TOOL_IDS[si], 1, mat, mat, null);
                TurbineStatCalculator c = new TurbineStatCalculator(MetaGeneratedTool01.INSTANCE, stack);
                rows.add(
                    new String[] { mat.mDefaultLocalName, String.valueOf(mat.mToolQuality),
                        DumperUtils.formatDouble(mat.mToolSpeed), DumperUtils.formatDouble(mat.mDurability * 100.0),
                        String.valueOf(overflowTier), SIZE_NAMES[si], String.valueOf(si + 1),
                        DumperUtils.formatDouble(c.getSteamEfficiency()),
                        DumperUtils.formatDouble(c.getLooseSteamEfficiency()),
                        DumperUtils.formatDouble(c.getOptimalSteamFlow()),
                        DumperUtils.formatDouble(c.getOptimalLooseSteamFlow()),
                        DumperUtils.formatDouble(c.getOptimalSteamEUt()),
                        DumperUtils.formatDouble(c.getOptimalLooseSteamEUt()),
                        DumperUtils.formatDouble(c.getGasEfficiency()),
                        DumperUtils.formatDouble(c.getLooseGasEfficiency()),
                        DumperUtils.formatDouble(c.getOptimalGasFlow()),
                        DumperUtils.formatDouble(c.getOptimalLooseGasFlow()),
                        DumperUtils.formatDouble(c.getOptimalGasEUt()),
                        DumperUtils.formatDouble(c.getOptimalLooseGasEUt()),
                        DumperUtils.formatDouble(c.getPlasmaEfficiency()),
                        DumperUtils.formatDouble(c.getLoosePlasmaEfficiency()),
                        DumperUtils.formatDouble(c.getOptimalPlasmaFlow()),
                        DumperUtils.formatDouble(c.getOptimalLoosePlasmaFlow()),
                        DumperUtils.formatDouble(c.getOptimalPlasmaEUt()),
                        DumperUtils.formatDouble(c.getOptimalLoosePlasmaEUt()) });
            }
        }
        return rows;
    }

    @Override
    public String renderName() {
        return "Turbine Rotor Data";
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
        if (getMode() == 1) dumpJson(file);
        else super.dumpTo(file);
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
        result.sort(
            Comparator.comparingInt((Materials m) -> (int) m.mToolQuality)
                .reversed()
                .thenComparing(m -> m.mDefaultLocalName));
        return result;
    }

    private static JsonObject buildSizeJson(TurbineStatCalculator c, int si) {
        JsonObject obj = new JsonObject();
        obj.addProperty("steam_tight_eff", c.getSteamEfficiency());
        obj.addProperty("steam_loose_eff", c.getLooseSteamEfficiency());
        obj.addProperty("steam_opt_flow_tight", c.getOptimalSteamFlow());
        obj.addProperty("steam_opt_flow_loose", c.getOptimalLooseSteamFlow());
        obj.addProperty("steam_power_tight", c.getOptimalSteamEUt());
        obj.addProperty("steam_power_loose", c.getOptimalLooseSteamEUt());
        obj.addProperty("gas_tight_eff", c.getGasEfficiency());
        obj.addProperty("gas_loose_eff", c.getLooseGasEfficiency());
        obj.addProperty("gas_opt_flow_tight", c.getOptimalGasFlow());
        obj.addProperty("gas_opt_flow_loose", c.getOptimalLooseGasFlow());
        obj.addProperty("gas_power_tight", c.getOptimalGasEUt());
        obj.addProperty("gas_power_loose", c.getOptimalLooseGasEUt());
        obj.addProperty("plasma_tight_eff", c.getPlasmaEfficiency());
        obj.addProperty("plasma_loose_eff", c.getLoosePlasmaEfficiency());
        obj.addProperty("plasma_opt_flow_tight", c.getOptimalPlasmaFlow());
        obj.addProperty("plasma_opt_flow_loose", c.getOptimalLoosePlasmaFlow());
        obj.addProperty("plasma_power_tight", c.getOptimalPlasmaEUt());
        obj.addProperty("plasma_power_loose", c.getOptimalLoosePlasmaEUt());
        obj.addProperty("dur_mult", si + 1);
        return obj;
    }

    private void dumpJson(File file) throws IOException {
        JsonArray root = new JsonArray();
        for (Materials mat : collectMaterials()) {
            int overflowTier = (int) (1 + Math.min(2.0, mat.mToolQuality / 3.0));
            JsonObject matObj = new JsonObject();
            matObj.addProperty("name", mat.mDefaultLocalName + " (" + mat.mToolQuality + ")");
            matObj.addProperty("tier", mat.mToolQuality);
            matObj.addProperty("mining_speed", mat.mToolSpeed);
            matObj.addProperty("base_durability", mat.mDurability * 100.0);
            matObj.addProperty("overflow_tier", overflowTier);
            JsonObject sizes = new JsonObject();
            for (int si = 0; si < TOOL_IDS.length; si++) {
                ItemStack stack = MetaGeneratedTool01.INSTANCE.getToolWithStats(TOOL_IDS[si], 1, mat, mat, null);
                sizes.add(
                    SIZE_NAMES[si],
                    buildSizeJson(new TurbineStatCalculator(MetaGeneratedTool01.INSTANCE, stack), si));
            }
            matObj.add("sizes", sizes);
            root.add(matObj);
        }
        try (FileWriter w = new FileWriter(file)) {
            DumperUtils.GSON.toJson(root, w);
        }
    }
}
