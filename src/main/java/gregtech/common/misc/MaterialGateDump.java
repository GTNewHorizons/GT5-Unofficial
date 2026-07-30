package gregtech.common.misc;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.material.LegacyNameDomain;
import gregtech.api.material.MaterialUtils;

/// Per-material snapshot of the fluid slots and generation gates that recipe autogeneration reads, keyed by
/// material name. Written alongside the recipe census so a shift in which materials generate fluids or items can
/// be attributed to the gate that moved rather than inferred from the recipes it produced.
public final class MaterialGateDump {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting()
        .serializeNulls()
        .create();

    private MaterialGateDump() {}

    public static void write(File directory) {
        Map<String, Object> out = new TreeMap<>();
        for (Material material : MaterialLibAPI.getMaterials()) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("legacyNamed", LegacyNameDomain.contains(material));
            row.put("oldSubId", MaterialUtils.oldSubId(material));
            row.put("hasMolten", MaterialUtils.hasMolten(material));
            row.put("solid", name(MaterialUtils.solid(material, 1)));
            row.put("liquid", name(MaterialUtils.fluidOf(material)));
            row.put("gas", name(MaterialUtils.gasOf(material)));
            row.put("molten", name(MaterialUtils.moltenOf(material)));
            row.put("plasma", name(MaterialUtils.plasmaOf(material)));
            row.put("anyFluid", name(MaterialUtils.anyFluidOf(material)));
            row.put("legacyGtppPlasma", name(MaterialUtils.legacyGtppPlasmaOf(material)));
            for (MaterialUtils.CrackType type : MaterialUtils.CrackType.values()) {
                for (int severity = 0; severity < 3; severity++) {
                    row.put(crackedKey(type, severity), name(MaterialUtils.crackedFluid(material, type, severity)));
                }
            }
            out.put(MaterialUtils.internalName(material), row);
        }
        File file = new File(directory, "material-gates.json");
        try (Writer writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
            GSON.toJson(out, writer);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write " + file, e);
        }
    }

    public static void write() {
        File directory = new File(Launch.minecraftHome, "material-dump");
        directory.mkdirs();
        write(directory);
    }

    private static String crackedKey(MaterialUtils.CrackType type, int severity) {
        return (type == MaterialUtils.CrackType.HYDRO ? "hydroCracked" : "steamCracked") + (severity + 1);
    }

    private static String name(Fluid fluid) {
        return fluid == null ? null : fluid.getName();
    }

    private static String name(FluidStack stack) {
        return stack == null ? null
            : stack.getFluid()
                .getName();
    }
}
