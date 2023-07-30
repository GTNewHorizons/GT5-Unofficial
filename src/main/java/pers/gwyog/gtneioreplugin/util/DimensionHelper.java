package pers.gwyog.gtneioreplugin.util;

import static pers.gwyog.gtneioreplugin.GTNEIOrePlugin.maxTooltipLines;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.minecraft.util.StatCollector;

public class DimensionHelper {

    public static final String[] DimName = {
            // Non GC dimensions in progression order instead of alphabetical
            "Overworld", "Nether", "Twilight", "TheEnd", "Vanilla_EndAsteroids", "EndAsteroid",
            // T1
            "GalacticraftCore_Moon",
            // T2
            "GalaxySpace_Deimos", "GalacticraftMars_Mars", "GalaxySpace_Phobos",
            // T3
            "GalacticraftMars_Asteroids", "GalaxySpace_Callisto", "GalaxySpace_Ceres", "GalaxySpace_Europa",
            "GalaxySpace_Ganymede", "Ross128b",
            // T4
            "GalaxySpace_Io", "GalaxySpace_Mercury", "GalaxySpace_Venus",
            // T5
            "GalaxySpace_Enceladus", "GalaxySpace_Miranda", "GalaxySpace_Oberon", "GalaxySpace_Titan", "Ross128ba",
            // T6
            "GalaxySpace_Proteus", "GalaxySpace_Triton",
            // T7
            "GalaxySpace_Haumea", "GalaxySpace_Kuiperbelt", "GalaxySpace_MakeMake", "GalaxySpace_Pluto",
            // T8
            "GalaxySpace_BarnardC", "GalaxySpace_BarnardE", "GalaxySpace_BarnardF", "GalaxySpace_CentauriA",
            "GalaxySpace_TcetiE", "GalaxySpace_VegaB",
            // T9
            "GalacticraftAmunRa_Anubis", "GalacticraftAmunRa_Horus", "GalacticraftAmunRa_Maahes",
            "GalacticraftAmunRa_Mehen Belt", "GalacticraftAmunRa_Neper", "GalacticraftAmunRa_Seth",
            // T10
            "Underdark", };

    public static final String[] DimNameTrimmed = Arrays.stream(DimName)
            .map(
                    n -> n.replaceAll("GalacticraftCore_", "").replaceAll("GalacticraftMars_", "")
                            .replaceAll("GalaxySpace_", "").replaceAll("GalacticraftAmunRa_", "")
                            .replaceAll("Vanilla_", "Vanilla "))
            .toArray(String[]::new);

    public static final String[] DimNameDisplayed = { // first 2 letters if one word else 1 letter of every word, except
            // capital letter in
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
            "Rb", // Ross128b
            // T4
            "Io", // GalaxySpace_Io
            "Me", // GalaxySpace_Mercury
            "Ve", // GalaxySpace_Venus
            // T5
            "En", // GalaxySpace_Enceladus
            "Mi", // GalaxySpace_Miranda
            "Ob", // GalaxySpace_Oberon
            "Ti", // GalaxySpace_Titan
            "Ra", // Ross128ba
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
            "CB", // GalaxySpace_CentauriA is actually α Centauri Bb
            "TE", // GalaxySpace_TcetiE
            "VB", // GalaxySpace_VegaB
            // T9
            "An", // GalacticraftAmunRa_Anubis
            "Ho", // GalacticraftAmunRa_Horus
            "Mh", // GalacticraftAmunRa_Maahes
            "MB", // GalacticraftAmunRa_Mehen Belt
            "Np", // GalacticraftAmunRa_Neper
            "Se", // GalacticraftAmunRa_Seth
            // T10
            "DD", // Underdark
    };

    private static final HashMap<String, List<String>> tooltipBuffer = new HashMap<>();

    private static List<String> computeString(String line) {
        String[] dims = parseDimNames(line);
        for (int j = 0; j < dims.length; j++) {
            String s = dims[j];
            for (int i = 0; i < DimNameDisplayed.length; i++) {
                if (s.equals(DimNameDisplayed[i])) {
                    String k = DimNameTrimmed[i];
                    s = StatCollector.translateToLocal("gtnop.world." + k);
                    s = switch (k) {
                        case "Moon" -> "T1: " + s;
                        case "Deimos", "Mars", "Phobos" -> "T2: " + s;
                        case "Asteroids", "Callisto", "Ceres", "Europa", "Ganymede", "Ross128b" -> "T3: " + s;
                        case "Io", "Mercury", "Venus" -> "T4: " + s;
                        case "Enceladus", "Miranda", "Oberon", "Titan", "Ross128ba" -> "T5: " + s;
                        case "Proteus", "Triton" -> "T6: " + s;
                        case "Haumea", "Kuiperbelt", "MakeMake", "Pluto" -> "T7: " + s;
                        case "BarnardC", "BarnardE", "BarnardF", "CentauriA", "TcetiE", "VegaB" -> "T8: " + s;
                        case "Anubis", "Horus", "Maahes", "Mehen Belt", "Neper", "Seth" -> "T9: " + s;
                        case "Underdark" -> "T10: " + s;
                        default -> s;
                    };

                    dims[j] = s;
                }
            }
        }

        if (dims.length > maxTooltipLines) {
            dims = StringPaddingHack.stringsToSpacedColumns(
                    dims,
                    dims.length / maxTooltipLines + (dims.length % maxTooltipLines == 0 ? 0 : 1),
                    2);
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
