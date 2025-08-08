package gtneioreplugin.util;

import static galacticgreg.api.enums.DimensionDef.DimNames.ANUBIS;
import static galacticgreg.api.enums.DimensionDef.DimNames.ASTEROIDS;
import static galacticgreg.api.enums.DimensionDef.DimNames.BARNARDC;
import static galacticgreg.api.enums.DimensionDef.DimNames.BARNARDE;
import static galacticgreg.api.enums.DimensionDef.DimNames.BARNARDF;
import static galacticgreg.api.enums.DimensionDef.DimNames.CALLISTO;
import static galacticgreg.api.enums.DimensionDef.DimNames.CENTAURIA;
import static galacticgreg.api.enums.DimensionDef.DimNames.CERES;
import static galacticgreg.api.enums.DimensionDef.DimNames.DEEPDARK;
import static galacticgreg.api.enums.DimensionDef.DimNames.DEIMOS;
import static galacticgreg.api.enums.DimensionDef.DimNames.ENCELADUS;
import static galacticgreg.api.enums.DimensionDef.DimNames.ENDASTEROIDS;
import static galacticgreg.api.enums.DimensionDef.DimNames.EUROPA;
import static galacticgreg.api.enums.DimensionDef.DimNames.GANYMEDE;
import static galacticgreg.api.enums.DimensionDef.DimNames.HAUMEA;
import static galacticgreg.api.enums.DimensionDef.DimNames.HORUS;
import static galacticgreg.api.enums.DimensionDef.DimNames.IO;
import static galacticgreg.api.enums.DimensionDef.DimNames.KUIPERBELT;
import static galacticgreg.api.enums.DimensionDef.DimNames.MAAHES;
import static galacticgreg.api.enums.DimensionDef.DimNames.MAKEMAKE;
import static galacticgreg.api.enums.DimensionDef.DimNames.MARS;
import static galacticgreg.api.enums.DimensionDef.DimNames.MEHENBELT;
import static galacticgreg.api.enums.DimensionDef.DimNames.MERCURY;
import static galacticgreg.api.enums.DimensionDef.DimNames.MIRANDA;
import static galacticgreg.api.enums.DimensionDef.DimNames.MOON;
import static galacticgreg.api.enums.DimensionDef.DimNames.NEPER;
import static galacticgreg.api.enums.DimensionDef.DimNames.OBERON;
import static galacticgreg.api.enums.DimensionDef.DimNames.PHOBOS;
import static galacticgreg.api.enums.DimensionDef.DimNames.PLUTO;
import static galacticgreg.api.enums.DimensionDef.DimNames.PROTEUS;
import static galacticgreg.api.enums.DimensionDef.DimNames.ROSS128B;
import static galacticgreg.api.enums.DimensionDef.DimNames.ROSS128BA;
import static galacticgreg.api.enums.DimensionDef.DimNames.SETH;
import static galacticgreg.api.enums.DimensionDef.DimNames.TCETIE;
import static galacticgreg.api.enums.DimensionDef.DimNames.TITAN;
import static galacticgreg.api.enums.DimensionDef.DimNames.TRITON;
import static galacticgreg.api.enums.DimensionDef.DimNames.VEGAB;
import static galacticgreg.api.enums.DimensionDef.DimNames.VENUS;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.util.StatCollector;

import gregtech.common.OreMixBuilder;
import gtneioreplugin.Config;

public class DimensionHelper {

    public static final String[] DimName = {
        // Non GC dimensions in progression order instead of alphabetical
        "Overworld", "Nether", "Twilight", "TheEnd", "EndAsteroid",
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
        "GalacticraftAmunRa_MehenBelt", "GalacticraftAmunRa_Neper", "GalacticraftAmunRa_Seth",
        // T10
        "Underdark", };

    public static final String[] DimNameTrimmed = Arrays.stream(DimName)
        .map(
            n -> n.replaceAll("GalacticraftCore_", "")
                .replaceAll("GalacticraftMars_", "")
                .replaceAll("GalaxySpace_", "")
                .replaceAll("GalacticraftAmunRa_", ""))
        .toArray(String[]::new);

    public static final String[] DimNameDisplayed = { // first 2 letters if one word else 1 letter of every word, except
        // capital letter in
        // name, then 1rst + capital Moon = Mo, BarnardC = BC, EndAsteroid = EA
        // Non GC dimensions in progression order instead of alphabetical
        "Ow", // Overworld
        "Ne", // Nether
        "TF", // Twilight
        "ED", // TheEnd because En = Encalus
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
        "MB", // GalacticraftAmunRa_MehenBelt
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
                        case "Anubis", "Horus", "Maahes", "MehenBelt", "Neper", "Seth" -> "T9: " + s;
                        case "Underdark" -> "T10: " + s;
                        default -> s;
                    };

                    dims[j] = s;
                }
            }
        }

        if (dims.length > Config.maxTooltipLines) {
            dims = StringPaddingHack.stringsToSpacedColumns(
                dims,
                dims.length / Config.maxTooltipLines + (dims.length % Config.maxTooltipLines == 0 ? 0 : 1),
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

    public static Map<String, Boolean> getDims(GT5OreLayerHelper.OreLayerWrapper oreLayer) {
        Map<String, Boolean> enabledDims = new HashMap<>();
        Map<String, Boolean> origNames = oreLayer.allowedDimWithOrigNames;

        for (String dimName : origNames.keySet()) {
            String abbr = getDimAbbreviatedName(dimName);
            if (!origNames.getOrDefault(dimName, false)) {
                continue;
            }
            enabledDims.put(abbr, true);
        }
        return enabledDims;
    }

    public static Map<String, Boolean> getDims(GT5OreSmallHelper.OreSmallWrapper ore) {
        Map<String, Boolean> enabledDims = new HashMap<>();
        Map<String, Boolean> origNames = ore.allowedDimWithOrigNames;

        for (String dimName : origNames.keySet()) {
            String abbr = getDimAbbreviatedName(dimName);
            if (!origNames.getOrDefault(dimName, false)) {
                continue;
            }
            enabledDims.put(abbr, true);
        }
        return enabledDims;
    }

    public static String getDimAbbreviatedName(String dimName) {
        String abbreviatedName;
        switch (dimName) {
            case (OreMixBuilder.OW) -> abbreviatedName = "Ow"; // Overworld
            case OreMixBuilder.NETHER -> abbreviatedName = "Ne"; // Nether
            case OreMixBuilder.TWILIGHT_FOREST -> abbreviatedName = "TF"; // Twilight
            case OreMixBuilder.THE_END -> abbreviatedName = "ED"; // TheEnd because En = Encalus
            case ENDASTEROIDS -> abbreviatedName = "EA"; // EndAsteroid
            // T1
            case MOON -> abbreviatedName = "Mo"; // GalacticraftCore_Moon
            // T2
            case DEIMOS -> abbreviatedName = "De"; // GalaxySpace_Deimos
            case MARS -> abbreviatedName = "Ma"; // GalacticraftMars_Mars
            case PHOBOS -> abbreviatedName = "Ph"; // GalaxySpace_Phobos
            // T3
            case ASTEROIDS -> abbreviatedName = "As"; // GalacticraftMars_Asteroids
            case CALLISTO -> abbreviatedName = "Ca"; // GalaxySpace_Callisto
            case CERES -> abbreviatedName = "Ce"; // GalaxySpace_Ceres
            case EUROPA -> abbreviatedName = "Eu"; // GalaxySpace_Europa
            case GANYMEDE -> abbreviatedName = "Ga"; // GalaxySpace_Ganymede
            case ROSS128B -> abbreviatedName = "Rb"; // Ross128b
            // T4
            case IO -> abbreviatedName = "Io"; // GalaxySpace_Io
            case MERCURY -> abbreviatedName = "Me"; // GalaxySpace_Mercury
            case VENUS -> abbreviatedName = "Ve"; // GalaxySpace_Venus
            // T5
            case ENCELADUS -> abbreviatedName = "En"; // GalaxySpace_Enceladus
            case MIRANDA -> abbreviatedName = "Mi"; // GalaxySpace_Miranda
            case OBERON -> abbreviatedName = "Ob"; // GalaxySpace_Oberon
            case TITAN -> abbreviatedName = "Ti"; // GalaxySpace_Titan
            case ROSS128BA -> abbreviatedName = "Ra"; // Ross128ba
            // T6
            case PROTEUS -> abbreviatedName = "Pr"; // GalaxySpace_Proteus
            case TRITON -> abbreviatedName = "Tr"; // GalaxySpace_Triton
            // T7
            case HAUMEA -> abbreviatedName = "Ha"; // GalaxySpace_Haumea
            case KUIPERBELT -> abbreviatedName = "KB"; // GalaxySpace_Kuiperbelt
            case MAKEMAKE -> abbreviatedName = "MM"; // GalaxySpace_MakeMake
            case PLUTO -> abbreviatedName = "Pl"; // GalaxySpace_Pluto
            // T8
            case BARNARDC -> abbreviatedName = "BC"; // GalaxySpace_BarnardC
            case BARNARDE -> abbreviatedName = "BE"; // GalaxySpace_BarnardE
            case BARNARDF -> abbreviatedName = "BF"; // GalaxySpace_BarnardF
            case CENTAURIA -> abbreviatedName = "CB"; // GalaxySpace_CentauriA is actually α Centauri Bb
            case TCETIE -> abbreviatedName = "TE"; // GalaxySpace_TcetiE
            case VEGAB -> abbreviatedName = "VB"; // GalaxySpace_VegaB
            // T9
            case ANUBIS -> abbreviatedName = "An"; // GalacticraftAmunRa_Anubis
            case HORUS -> abbreviatedName = "Ho"; // GalacticraftAmunRa_Horus
            case MAAHES -> abbreviatedName = "Mh"; // GalacticraftAmunRa_Maahes
            case MEHENBELT -> abbreviatedName = "MB"; // GalacticraftAmunRa_MehenBelt
            case NEPER -> abbreviatedName = "Np"; // GalacticraftAmunRa_Neper
            case SETH -> abbreviatedName = "Se"; // GalacticraftAmunRa_Seth
            // T10
            case DEEPDARK -> abbreviatedName = "DD"; // Underdark
            default -> {
                throw new IllegalStateException("String: " + dimName + " has no abbredged name!");
            }
        }
        return abbreviatedName;
    }

    public static String getFullName(String dimName) {

        return switch (dimName) {
            case "Ow" -> (OreMixBuilder.OW); // Overworld
            case "Ne" -> OreMixBuilder.NETHER; // Nether
            case "TF" -> OreMixBuilder.TWILIGHT_FOREST; // Twilight
            case "ED" -> OreMixBuilder.THE_END; // TheEnd because En = Encalus
            case "EA" -> ENDASTEROIDS; // EndAsteroid
            // T1
            case "Mo" -> MOON; // GalacticraftCore_Moon
            // T2
            case "De" -> DEIMOS; // GalaxySpace_Deimos
            case "Ma" -> MARS; // GalacticraftMars_Mars
            case "Ph" -> PHOBOS; // GalaxySpace_Phobos
            // T3
            case "As" -> ASTEROIDS; // GalacticraftMars_Asteroids
            case "Ca" -> CALLISTO; // GalaxySpace_Callisto
            case "Ce" -> CERES; // GalaxySpace_Ceres
            case "Eu" -> EUROPA; // GalaxySpace_Europa
            case "Ga" -> GANYMEDE; // GalaxySpace_Ganymede
            case "Rb" -> ROSS128B; // Ross128b
            // T4
            case "Io" -> IO; // GalaxySpace_Io
            case "Me" -> MERCURY; // GalaxySpace_Mercury
            case "Ve" -> VENUS; // GalaxySpace_Venus
            // T5
            case "En" -> ENCELADUS; // GalaxySpace_Enceladus
            case "Mi" -> MIRANDA; // GalaxySpace_Miranda
            case "Ob" -> OBERON; // GalaxySpace_Oberon
            case "Ti" -> TITAN; // GalaxySpace_Titan
            case "Ra" -> ROSS128BA; // Ross128ba
            // T6
            case "Pr" -> PROTEUS; // GalaxySpace_Proteus
            case "Tr" -> TRITON; // GalaxySpace_Triton
            // T7
            case "Ha" -> HAUMEA; // GalaxySpace_Haumea
            case "KB" -> KUIPERBELT; // GalaxySpace_Kuiperbelt
            case "MM" -> MAKEMAKE; // GalaxySpace_MakeMake
            case "Pl" -> PLUTO; // GalaxySpace_Pluto
            // T8
            case "BC" -> BARNARDC; // GalaxySpace_BarnardC
            case "BE" -> BARNARDE; // GalaxySpace_BarnardE
            case "BF" -> BARNARDF; // GalaxySpace_BarnardF
            case "CB" -> CENTAURIA; // GalaxySpace_CentauriA is actually α Centauri Bb
            case "TE" -> TCETIE; // GalaxySpace_TcetiE
            case "VB" -> VEGAB; // GalaxySpace_VegaB
            // T9
            case "An" -> ANUBIS; // GalacticraftAmunRa_Anubis
            case "Ho" -> HORUS; // GalacticraftAmunRa_Horus
            case "Mh" -> MAAHES; // GalacticraftAmunRa_Maahes
            case "MB" -> MEHENBELT; // GalacticraftAmunRa_MehenBelt
            case "Np" -> NEPER; // GalacticraftAmunRa_Neper
            case "Se" -> SETH; // GalacticraftAmunRa_Seth
            // T10
            case "DD" -> DEEPDARK; // Underdark
            default -> {
                throw new IllegalStateException("String: " + dimName + " has no abbredged name!");
            }
        };
    }

    public static List<String> convertCondensedStringToToolTip(String line) {
        return tooltipBuffer.computeIfAbsent(line, (String tmp) -> computeString(line));
    }
}
