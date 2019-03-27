package pers.gwyog.gtneioreplugin.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DimensionHelper {

    public static String[] DimName =
            {
                    "EndAsteroid",
                    "GalacticraftCore_Moon",
                    "GalacticraftMars_Asteroids",
                    "GalacticraftMars_Mars",
                    "GalaxySpace_BarnardC",
                    "GalaxySpace_BarnardE",
                    "GalaxySpace_BarnardF",
                    "GalaxySpace_Callisto",
                    "GalaxySpace_CentauriA",
                    "GalaxySpace_Ceres",
                    "GalaxySpace_Deimos",
                    "GalaxySpace_Enceladus",
                    "GalaxySpace_Europa",
                    "GalaxySpace_Ganymede",
                    "GalaxySpace_Haumea",
                    "GalaxySpace_Io",
                    "GalaxySpace_Kuiperbelt",
                    "GalaxySpace_MakeMake",
                    "GalaxySpace_Mercury",
                    "GalaxySpace_Miranda",
                    "GalaxySpace_Oberon",
                    "GalaxySpace_Phobos",
                    "GalaxySpace_Pluto",
                    "GalaxySpace_Proteus",
                    "GalaxySpace_TcetiE",
                    "GalaxySpace_Titan",
                    "GalaxySpace_Triton",
                    "GalaxySpace_VegaB",
                    "GalaxySpace_Venus",
                    "Nether",
                    "Overworld",
                    "TheEnd",
                    "Vanilla_EndAsteroids",
                    "Twilight",
                    "Underdark"
            };

    public static String[] DimNameDisplayed =
            {// first 2 letters if one word else 1 letter of every word, execpt capital letter in name, then 1rst + capital Moon = Mo, BarnardC = BC, EndAsteroid = EA
                    "EA",
                    "Mo",
                    "As",
                    "Ma",
                    "BC",
                    "BE",
                    "BF",
                    "Ca",
                    "CA",
                    "Ce",
                    "De",
                    "En",
                    "Eu",
                    "Ga",
                    "Ha",
                    "Io",
                    "KB",
                    "MM",
                    "Me",
                    "Mi",
                    "Ob",
                    "Ph",
                    "Pl",
                    "Pr",
                    "TE",
                    "Ti",
                    "Tr",
                    "VB",
                    "Ve",
                    "Ne",
                    "Ow",
                    "EN",//End = EN bc En = Encalus
                    "VA",
                    "TF",
                    "DD"
            };

    private static HashMap<String, List<String>> tooltipBuffer = new HashMap<>();

    private static List<String> computeString(String line) {
        String[] dims = line.split(",");
        for (int j = 0; j < dims.length; j++) {
            String s = dims[j];
            s = s.replaceAll(",", "");
            s = s.trim();
            for (int i = 0; i < DimNameDisplayed.length; i++) {
                if (s.equals(DimNameDisplayed[i])) {
                    s = DimName[i].replaceAll("GalacticraftCore_", "").replaceAll("GalacticraftMars_", "").replaceAll("GalaxySpace_", "").replaceAll("Vanilla_", "Vanilla ");
                    if (s.equals("Twilight"))
                        s = "Twilight Forrest";
                    else if (s.equals("Underdark"))
                        s = "Deep Dark";
                    else if (s.equals("EndAsteroid"))
                        s = "Far End Asteroids";
                    dims[j] = s;
                }
            }
        }
        return Arrays.asList(dims);
    }

    public static List<String> convertCondensedStringToToolTip(String line) {
        return tooltipBuffer.computeIfAbsent(line, (String tmp) -> computeString(line));
    }
}