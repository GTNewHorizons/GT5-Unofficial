package gtneioreplugin.util;

import static galacticgreg.api.enums.DimensionDef.DimNames.ANUBIS;
import static galacticgreg.api.enums.DimensionDef.DimNames.ASTEROIDS;
import static galacticgreg.api.enums.DimensionDef.DimNames.BARNARDC;
import static galacticgreg.api.enums.DimensionDef.DimNames.BARNARDE;
import static galacticgreg.api.enums.DimensionDef.DimNames.BARNARDF;
import static galacticgreg.api.enums.DimensionDef.DimNames.CALLISTO;
import static galacticgreg.api.enums.DimensionDef.DimNames.CENTAURIBB;
import static galacticgreg.api.enums.DimensionDef.DimNames.CERES;
import static galacticgreg.api.enums.DimensionDef.DimNames.DEEPDARK;
import static galacticgreg.api.enums.DimensionDef.DimNames.DEIMOS;
import static galacticgreg.api.enums.DimensionDef.DimNames.ENCELADUS;
import static galacticgreg.api.enums.DimensionDef.DimNames.ENDASTEROID;
import static galacticgreg.api.enums.DimensionDef.DimNames.EUROPA;
import static galacticgreg.api.enums.DimensionDef.DimNames.EVERGLADES;
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
import static galacticgreg.api.enums.DimensionDef.DimNames.NETHER;
import static galacticgreg.api.enums.DimensionDef.DimNames.OBERON;
import static galacticgreg.api.enums.DimensionDef.DimNames.OW;
import static galacticgreg.api.enums.DimensionDef.DimNames.PHOBOS;
import static galacticgreg.api.enums.DimensionDef.DimNames.PLUTO;
import static galacticgreg.api.enums.DimensionDef.DimNames.PROTEUS;
import static galacticgreg.api.enums.DimensionDef.DimNames.ROSS128B;
import static galacticgreg.api.enums.DimensionDef.DimNames.ROSS128BA;
import static galacticgreg.api.enums.DimensionDef.DimNames.SETH;
import static galacticgreg.api.enums.DimensionDef.DimNames.TCETIE;
import static galacticgreg.api.enums.DimensionDef.DimNames.THE_END;
import static galacticgreg.api.enums.DimensionDef.DimNames.TITAN;
import static galacticgreg.api.enums.DimensionDef.DimNames.TRITON;
import static galacticgreg.api.enums.DimensionDef.DimNames.TWILIGHT_FOREST;
import static galacticgreg.api.enums.DimensionDef.DimNames.VEGAB;
import static galacticgreg.api.enums.DimensionDef.DimNames.VENUS;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.client.resources.I18n;

public class DimensionHelper {

    public static final String[] DimName = {
        // Non GC dimensions in progression order instead of alphabetical
        "Overworld", "Nether", "Twilight", "The End", "EndAsteroid", "dimensionDarkWorld",
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
        "ED", // The End because En = Encalus
        "EA", // EndAsteroid
        "Eg", // Everglades
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

    public static Set<String> getDims(GT5OreLayerHelper.OreLayerWrapper oreLayer) {
        Set<String> enabledDims = new HashSet<>();
        Set<String> origNames = oreLayer.allowedDimWithOrigNames;

        for (String dimName : origNames) {
            String abbr = getDimAbbreviatedName(dimName);
            if (!origNames.contains(dimName)) {
                continue;
            }
            enabledDims.add(abbr);
        }
        return enabledDims;
    }

    public static String getDimAbbreviatedName(String dimName) {
        return switch (dimName) {
            case OW -> "Ow"; // Overworld
            case NETHER -> "Ne"; // Nether
            case TWILIGHT_FOREST -> "TF"; // Twilight
            case THE_END -> "ED"; // The End because En = Encalus
            case ENDASTEROID -> "EA"; // EndAsteroid
            case EVERGLADES -> "Eg";
            // T1
            case MOON -> "Mo"; // GalacticraftCore_Moon
            // T2
            case DEIMOS -> "De"; // GalaxySpace_Deimos
            case MARS -> "Ma"; // GalacticraftMars_Mars
            case PHOBOS -> "Ph"; // GalaxySpace_Phobos
            // T3
            case ASTEROIDS -> "As"; // GalacticraftMars_Asteroids
            case CALLISTO -> "Ca"; // GalaxySpace_Callisto
            case CERES -> "Ce"; // GalaxySpace_Ceres
            case EUROPA -> "Eu"; // GalaxySpace_Europa
            case GANYMEDE -> "Ga"; // GalaxySpace_Ganymede
            case ROSS128B -> "Rb"; // Ross128b
            // T4
            case IO -> "Io"; // GalaxySpace_Io
            case MERCURY -> "Me"; // GalaxySpace_Mercury
            case VENUS -> "Ve"; // GalaxySpace_Venus
            // T5
            case ENCELADUS -> "En"; // GalaxySpace_Enceladus
            case MIRANDA -> "Mi"; // GalaxySpace_Miranda
            case OBERON -> "Ob"; // GalaxySpace_Oberon
            case TITAN -> "Ti"; // GalaxySpace_Titan
            case ROSS128BA -> "Ra"; // Ross128ba
            // T6
            case PROTEUS -> "Pr"; // GalaxySpace_Proteus
            case TRITON -> "Tr"; // GalaxySpace_Triton
            // T7
            case HAUMEA -> "Ha"; // GalaxySpace_Haumea
            case KUIPERBELT -> "KB"; // GalaxySpace_Kuiperbelt
            case MAKEMAKE -> "MM"; // GalaxySpace_MakeMake
            case PLUTO -> "Pl"; // GalaxySpace_Pluto
            // T8
            case BARNARDC -> "BC"; // GalaxySpace_BarnardC
            case BARNARDE -> "BE"; // GalaxySpace_BarnardE
            case BARNARDF -> "BF"; // GalaxySpace_BarnardF
            case CENTAURIBB -> "CB"; // GalaxySpace_CentauriA
            case TCETIE -> "TE"; // GalaxySpace_TcetiE
            case VEGAB -> "VB"; // GalaxySpace_VegaB
            // T9
            case ANUBIS -> "An"; // GalacticraftAmunRa_Anubis
            case HORUS -> "Ho"; // GalacticraftAmunRa_Horus
            case MAAHES -> "Mh"; // GalacticraftAmunRa_Maahes
            case MEHENBELT -> "MB"; // GalacticraftAmunRa_MehenBelt
            case NEPER -> "Np"; // GalacticraftAmunRa_Neper
            case SETH -> "Se"; // GalacticraftAmunRa_Seth
            // T10
            case DEEPDARK -> "DD"; // Underdark
            default -> {
                throw new IllegalStateException("String: " + dimName + " has no abbredged name!");
            }
        };
    }

    public static String getFullName(String abbrDimName) {
        return switch (abbrDimName) {
            case "Ow" -> OW; // Overworld
            case "Ne" -> NETHER; // Nether
            case "TF" -> TWILIGHT_FOREST; // Twilight
            case "ED" -> THE_END; // The End because En = Encalus
            case "EA" -> ENDASTEROID; // EndAsteroid
            case "Eg" -> EVERGLADES;
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
            case "CB" -> CENTAURIBB; // GalaxySpace_CentauriA
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
                throw new IllegalStateException("String: " + abbrDimName + " has no abbredged name!");
            }
        };
    }

    private static final String T0 = "gtnop.tier.0";
    private static final String T1 = "gtnop.tier.1";
    private static final String T2 = "gtnop.tier.2";
    private static final String T3 = "gtnop.tier.3";
    private static final String T4 = "gtnop.tier.4";
    private static final String T5 = "gtnop.tier.5";
    private static final String T6 = "gtnop.tier.6";
    private static final String T7 = "gtnop.tier.7";
    private static final String T8 = "gtnop.tier.8";
    private static final String T9 = "gtnop.tier.9";
    private static final String T10 = "gtnop.tier.10";

    public static String getDimTier(String dimName) {
        return switch (dimName) {
            case OW, NETHER, TWILIGHT_FOREST, THE_END, ENDASTEROID, EVERGLADES -> T0;
            case MOON -> T1;
            case DEIMOS, MARS, PHOBOS -> T2;
            case ASTEROIDS, CALLISTO, CERES, EUROPA, GANYMEDE, ROSS128B -> T3;
            case IO, MERCURY, VENUS -> T4;
            case ENCELADUS, MIRANDA, OBERON, TITAN, ROSS128BA -> T5;
            case PROTEUS, TRITON -> T6;
            case HAUMEA, KUIPERBELT, MAKEMAKE, PLUTO -> T7;
            case BARNARDC, BARNARDE, BARNARDF, CENTAURIBB, TCETIE, VEGAB -> T8;
            case ANUBIS, HORUS, MAAHES, MEHENBELT, NEPER, SETH -> T9;
            case DEEPDARK -> T10;

            default -> T0;
        };
    }

    /**
     * Gets the lang key for a dimension's name.
     *
     * @param dimName The dimension's full name.
     */
    public static String getDimUnlocalizedName(String dimName) {
        return "gtnop.world." + dimName.replace(" ", "");
    }

    public static String getDimLocalizedName(String dimName) {
        return I18n.format(getDimUnlocalizedName(dimName));
    }
}
