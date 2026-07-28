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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.material.MU;

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
            row.put("legacyNamed", MU.isLegacyNamed(material));
            row.put("oldSubId", MU.oldSubId(material));
            row.put("hasMolten", MU.hasMolten(material));
            row.put("liquid", name(MU.fluidOf(material)));
            row.put("gas", name(MU.gasOf(material)));
            row.put("molten", name(MU.moltenOf(material)));
            row.put("plasma", name(MU.plasmaOf(material)));
            out.put(MU.internalName(material), row);
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

    private static String name(Fluid fluid) {
        return fluid == null ? null : fluid.getName();
    }
}
