package pers.gwyog.gtneioreplugin.util;

import static pers.gwyog.gtneioreplugin.GTNEIOrePlugin.maxTooltipLines;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import net.minecraft.client.resources.I18n;

public class DimensionHelper {

    public static String[] DimName = {
        // Non GC dimensions in progression order instead of alphabetical
        "Overworld",
        "Nether",
        "Twilight",
        "TheEnd",
        "Vanilla_EndAsteroids",
        "EndAsteroid",
        // T1
        "GalacticraftCore_Moon",
        // T2
        "GalaxySpace_Deimos",
        "GalacticraftMars_Mars",
        "GalaxySpace_Phobos",
        // T3
        "GalacticraftMars_Asteroids",
        "GalaxySpace_Callisto",
        "GalaxySpace_Ceres",
        "GalaxySpace_Europa",
        "GalaxySpace_Ganymede",
        // T4
        "GalaxySpace_Io",
        "GalaxySpace_Mercury",
        "GalaxySpace_Venus",
        // T5
        "GalaxySpace_Enceladus",
        "GalaxySpace_Miranda",
        "GalaxySpace_Oberon",
        "GalaxySpace_Titan",
        // T6
        "GalaxySpace_Proteus",
        "GalaxySpace_Triton",
        // T7
        "GalaxySpace_Haumea",
        "GalaxySpace_Kuiperbelt",
        "GalaxySpace_MakeMake",
        "GalaxySpace_Pluto",
        // T8
        "GalaxySpace_BarnardC",
        "GalaxySpace_BarnardE",
        "GalaxySpace_BarnardF",
        "GalaxySpace_CentauriA",
        "GalaxySpace_TcetiE",
        "Underdark",
        "GalaxySpace_VegaB",
    };

    public static String[]
            DimNameDisplayed = { // first 2 letters if one word else 1 letter of every word, except capital letter in
        // name, then 1rst + capital Moon = Mo, BarnardC = BC, EndAsteroid = EA
        // Non GC dimensions in progression order instead of alphabetical
        "Ow", // Overworld
        "Ne", // Nether
        "TF", // Twilight
        "ED", // TheEnd because En = Encalus
        "VA", // Vanilla_EndAsteroids
        "EA", // EndAsteroid
        // T1
        "Mo", // GalacticraftCore_Moon
        // T2
        "De", // GalaxySpace_Deimos
        "Ma", // GalacticraftMars_Mars
        "Ph", // GalaxySpace_Phobos
        // T3
        "As", // GalacticraftMars_Asteroids
        "Ca", // GalaxySpace_Callisto
        "Ce", // GalaxySpace_Ceres
        "Eu", // GalaxySpace_Europa
        "Ga", // GalaxySpace_Ganymede
        // T4
        "Io", // GalaxySpace_Io
        "Me", // GalaxySpace_Mercury
        "Ve", // GalaxySpace_Venus
        // T5
        "En", // GalaxySpace_Enceladus
        "Mi", // GalaxySpace_Miranda
        "Ob", // GalaxySpace_Oberon
        "Ti", // GalaxySpace_Titan
        // T6
        "Pr", // GalaxySpace_Proteus
        "Tr", // GalaxySpace_Triton
        // T7
        "Ha", // GalaxySpace_Haumea
        "KB", // GalaxySpace_Kuiperbelt
        "MM", // GalaxySpace_MakeMake
        "Pl", // GalaxySpace_Pluto
        // T8
        "BC", // GalaxySpace_BarnardC
        "BE", // GalaxySpace_BarnardE
        "BF", // GalaxySpace_BarnardF
        "CB", // GalaxySpace_CentauriA is actually αCentauri Bb
        "TE", // GalaxySpace_TcetiE
        "DD", // Underdark
        "VB", // GalaxySpace_VegaB
    };

    private static final HashMap<String, List<String>> tooltipBuffer = new HashMap<>();

    private static List<String> computeString(String line) {
        String[] dims = parseDimNames(line);
        for (int j = 0; j < dims.length; j++) {
            String s = dims[j];
            for (int i = 0; i < DimNameDisplayed.length; i++) {
                if (s.equals(DimNameDisplayed[i])) {
                    String k = DimName[i]
                            .replaceAll("GalacticraftCore_", "")
                            .replaceAll("GalacticraftMars_", "")
                            .replaceAll("GalaxySpace_", "")
                            .replaceAll("Vanilla_", "Vanilla ");
                    s = I18n.format("gtnop.world." + k);
                    switch (k) {
                        case "Moon":
                            s = "T1: " + s;
                            break;
                        case "Deimos":
                        case "Mars":
                        case "Phobos":
                            s = "T2: " + s;
                            break;
                        case "Asteroids":
                        case "Callisto":
                        case "Ceres":
                        case "Europa":
                        case "Ganymede":
                            s = "T3: " + s;
                            break;
                        case "Io":
                        case "Mercury":
                        case "Venus":
                            s = "T4: " + s;
                            break;
                        case "Enceladus":
                        case "Miranda":
                        case "Oberon":
                        case "Titan":
                            s = "T5: " + s;
                            break;
                        case "Proteus":
                        case "Triton":
                            s = "T6: " + s;
                            break;
                        case "Haumea":
                        case "Kuiperbelt":
                        case "MakeMake":
                        case "Pluto":
                            s = "T7: " + s;
                            break;
                        case "BarnardC":
                        case "BarnardE":
                        case "BarnardF":
                        case "CentauriA":
                        case "TcetiE":
                        case "Underdark":
                        case "VegaB":
                            s = "T8: " + s;
                            break;
                    }

                    dims[j] = s;
                }
            }
        }

        if (dims.length > maxTooltipLines) {
            dims = StringPaddingHack.stringsToSpacedColumns(
                    dims, dims.length / maxTooltipLines + (dims.length % maxTooltipLines == 0 ? 0 : 1), 2);
        }

        return Arrays.asList(dims);
    }

    public static String[] parseDimNames(String line) {
        String[] dims = line.split(",");
        for (int j = 0; j < dims.length; j++) {
            String s = dims[j];
            s = s.replaceAll(",", "");
            s = s.trim();
            dims[j] = s;
        }
        return dims;
    }

    public static List<String> convertCondensedStringToToolTip(String line) {
        return tooltipBuffer.computeIfAbsent(line, (String tmp) -> computeString(line));
    }
}
