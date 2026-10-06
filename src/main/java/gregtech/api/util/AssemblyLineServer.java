package gregtech.api.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class AssemblyLineServer {

    public static LinkedHashMap<String, String> lServerNames = new LinkedHashMap<>();

    public static void fillMap(FMLPreInitializationEvent aEvent) {
        final Configuration conf = GTLanguageManager.sEnglishFile;
        final ConfigCategory cat = conf.getCategory("languagefile");
        final HashMap<String, Property> internal = new HashMap<>(cat.getValues());
        final LinkedHashMap<String, String> machineNames = new LinkedHashMap<>();
        for (Map.Entry<String, Property> entry : internal.entrySet()) {
            try {
                String s = entry.getValue()
                    .getString()
                    .replace("%", "");

                if (entry.getKey()
                    .contains("blockmachines") && s.contains("material")) machineNames.put(entry.getKey(), s);
                else lServerNames.put(entry.getKey(), s);
            } catch (Exception ignored) {}
        }
        for (Map.Entry<String, String> entry : machineNames.entrySet()) {
            try {
                if (entry.getKey()
                    .contains("cable"))
                    lServerNames.put(
                        entry.getKey(),
                        entry.getValue()
                            .replace(
                                "material",
                                entry.getKey()
                                    .substring(
                                        "gt.blockmachines.cable.".length(),
                                        entry.getKey()
                                            .length() - ".01.name".length())));
                else if (entry.getKey()
                    .contains("gt_frame_"))
                    lServerNames.put(
                        entry.getKey(),
                        entry.getValue()
                            .replace(
                                "material",
                                entry.getKey()
                                    .substring(
                                        "gt.blockmachines.gt_frame_".length(),
                                        entry.getKey()
                                            .length() - ".name".length())));
                else if (entry.getKey()
                    .contains("gt_pipe_")) {
                        if (!entry.getKey()
                            .contains("_huge")
                            && !entry.getKey()
                                .contains("_large")
                            && !entry.getKey()
                                .contains("_nonuple")
                            && !entry.getKey()
                                .contains("_quadruple")
                            && !entry.getKey()
                                .contains("_small")
                            && !entry.getKey()
                                .contains("_tiny"))
                            lServerNames.put(
                                entry.getKey(),
                                entry.getValue()
                                    .replace(
                                        "material",
                                        entry.getKey()
                                            .substring(
                                                "gt.blockmachines.gt_pipe_".length(),
                                                entry.getKey()
                                                    .length() - ".name".length())));
                        else if (entry.getKey()
                            .contains("_huge")
                            || entry.getKey()
                                .contains("_tiny"))
                            lServerNames.put(
                                entry.getKey(),
                                entry.getValue()
                                    .replace(
                                        "material",
                                        entry.getKey()
                                            .substring(
                                                "gt.blockmachines.gt_pipe_".length(),
                                                entry.getKey()
                                                    .length() - "_tiny.name".length())));
                        else if (entry.getKey()
                            .contains("_large")
                            || entry.getKey()
                                .contains("_small"))
                            lServerNames.put(
                                entry.getKey(),
                                entry.getValue()
                                    .replace(
                                        "material",
                                        entry.getKey()
                                            .substring(
                                                "gt.blockmachines.gt_pipe_".length(),
                                                entry.getKey()
                                                    .length() - "_large.name".length())));
                        else if (entry.getKey()
                            .contains("_nonuple"))
                            lServerNames.put(
                                entry.getKey(),
                                entry.getValue()
                                    .replace(
                                        "material",
                                        entry.getKey()
                                            .substring(
                                                "gt.blockmachines.gt_pipe_".length(),
                                                entry.getKey()
                                                    .length() - "_nonuple.name".length())));
                        else if (entry.getKey()
                            .contains("_quadruple"))
                            lServerNames.put(
                                entry.getKey(),
                                entry.getValue()
                                    .replace(
                                        "material",
                                        entry.getKey()
                                            .substring(
                                                "gt.blockmachines.gt_pipe_".length(),
                                                entry.getKey()
                                                    .length() - "_quadruple.name".length())));
                    } else if (entry.getKey()
                        .contains("wire"))
                        lServerNames.put(
                            entry.getKey(),
                            entry.getValue()
                                .replace(
                                    "material",
                                    entry.getKey()
                                        .substring(
                                            "gt.blockmachines.wire.".length(),
                                            entry.getKey()
                                                .length() - ".01.name".length())));
                else lServerNames.put(entry.getKey(), entry.getValue());
            } catch (Exception ignored) {}
        }
    }
}
