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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.resources.I18n;

import com.github.bsideup.jabel.Desugar;

import gregtech.api.util.GTUtility;

public class DimensionHelper {

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

    @Desugar
    public record Dimension(String fullName, String internalName, String trimmedName, String abbr, String tierKey) {}

    public static final Map<String, Dimension> REGISTRY = new LinkedHashMap<>();
    public static final Map<String, String> INTERNAL_TO_ABBR = new HashMap<>();
    public static final Map<String, String> ABBR_TO_INTERNAL = new HashMap<>();

    public static final List<Dimension> ALL_DIMENSIONS = new ArrayList<>();
    public static final List<String> ALL_DIM_NAMES = new ArrayList<>();
    public static final List<String> ALL_TRIMMED_NAMES = new ArrayList<>();
    public static final List<String> ALL_DISPLAYED_NAMES = new ArrayList<>();

    static {
        // first 2 letters if one word else 1 letter of every word, except
        // capital letter in
        // name, then 1rst + capital Moon = Mo, BarnardC = BC, EndAsteroid = EA
        // Non GC dimensions in progression order instead of alphabetical
        register("Overworld", OW, "Overworld", "Ow", T0);
        register("Nether", NETHER, "Nether", "Ne", T0);
        register("Twilight", TWILIGHT_FOREST, "Twilight", "TF", T0);
        register("The End", THE_END, "The End", "ED", T0);
        register("EndAsteroid", ENDASTEROID, "EndAsteroid", "EA", T0);
        register("dimensionDarkWorld", EVERGLADES, "dimensionDarkWorld", "Eg", T0);

        // T1
        register("GalacticraftCore_Moon", MOON, "Moon", "Mo", T1);

        // T2
        register("GalaxySpace_Deimos", DEIMOS, "De", "De", T2);
        register("GalacticraftMars_Mars", MARS, "Mars", "Ma", T2);
        register("GalaxySpace_Phobos", PHOBOS, "Phobos", "Ph", T2);

        // T3
        register("GalacticraftMars_Asteroids", ASTEROIDS, "Asteroids", "As", T3);
        register("GalaxySpace_Callisto", CALLISTO, "Callisto", "Ca", T3);
        register("GalaxySpace_Ceres", CERES, "Ceres", "Ce", T3);
        register("GalaxySpace_Europa", EUROPA, "Europa", "Eu", T3);
        register("GalaxySpace_Ganymede", GANYMEDE, "Ganymede", "Ga", T3);
        register("Ross128b", ROSS128B, "Ross128b", "Rb", T3);

        // T4
        register("GalaxySpace_Io", IO, "Io", "Io", T4);
        register("GalaxySpace_Mercury", MERCURY, "Mercury", "Me", T4);
        register("GalaxySpace_Venus", VENUS, "Venus", "Ve", T4);

        // T5
        register("GalaxySpace_Enceladus", ENCELADUS, "Enceladus", "En", T5);
        register("GalaxySpace_Miranda", MIRANDA, "Miranda", "Mi", T5);
        register("GalaxySpace_Oberon", OBERON, "Oberon", "Ob", T5);
        register("GalaxySpace_Titan", TITAN, "Titan", "Ti", T5);
        register("Ross128ba", ROSS128BA, "Ross128ba", "Ra", T5);

        // T6
        register("GalaxySpace_Proteus", PROTEUS, "Proteus", "Pr", T6);
        register("GalaxySpace_Triton", TRITON, "Triton", "Tr", T6);

        // T7
        register("GalaxySpace_Haumea", HAUMEA, "Haumea", "Ha", T7);
        register("GalaxySpace_Kuiperbelt", KUIPERBELT, "Kuiperbelt", "KB", T7);
        register("GalaxySpace_MakeMake", MAKEMAKE, "MakeMake", "MM", T7);
        register("GalaxySpace_Pluto", PLUTO, "Pluto", "Pl", T7);

        // T8
        register("GalaxySpace_BarnardC", BARNARDC, "BarnardC", "BC", T8);
        register("GalaxySpace_BarnardE", BARNARDE, "BarnardE", "BE", T8);
        register("GalaxySpace_BarnardF", BARNARDF, "BarnardF", "BF", T8);
        register("GalaxySpace_CentauriA", CENTAURIBB, "CentauriA", "CB", T8);
        register("GalaxySpace_TcetiE", TCETIE, "TcetiE", "TE", T8);
        register("GalaxySpace_VegaB", VEGAB, "VegaB", "VB", T8);

        // T9
        register("GalacticraftAmunRa_Anubis", ANUBIS, "Anubis", "An", T9);
        register("GalacticraftAmunRa_Horus", HORUS, "Horus", "Ho", T9);
        register("GalacticraftAmunRa_Maahes", MAAHES, "Maahes", "Mh", T9);
        register("GalacticraftAmunRa_MehenBelt", MEHENBELT, "MehenBelt", "MB", T9);
        register("GalacticraftAmunRa_Neper", NEPER, "Neper", "Np", T9);
        register("GalacticraftAmunRa_Seth", SETH, "Seth", "Se", T9);

        // T10
        register("Underdark", DEEPDARK, "Underdark", "DD", T10);
    }

    public static void register(String fullName, String internalName, String trimmedName, String abbr, String tierKey) {
        if (!REGISTRY.containsKey(fullName)) {
            Dimension dim = new Dimension(fullName, internalName, trimmedName, abbr, tierKey);
            REGISTRY.put(fullName, dim);
            INTERNAL_TO_ABBR.put(internalName, abbr);
            ABBR_TO_INTERNAL.put(abbr, internalName);

            ALL_DIM_NAMES.add(fullName);
            ALL_TRIMMED_NAMES.add(trimmedName);
            ALL_DISPLAYED_NAMES.add(abbr);
            ALL_DIMENSIONS.add(dim);
        }
    }

    public static int getIndex(String dimName) {
        int index = ALL_DIM_NAMES.indexOf(dimName);
        return GTUtility.max(index, 0);
    }

    public static int getIndexByAbbr(String abbr) {
        int index = ALL_DISPLAYED_NAMES.indexOf(abbr);
        return GTUtility.max(index, 0);
    }

    public static Dimension getByIndex(int index) {
        if (index < 0 || index >= ALL_DIMENSIONS.size()) return null;
        return ALL_DIMENSIONS.get(index);
    }

    public static Collection<Dimension> getAllDim() {
        return REGISTRY.values();
    }

    public static List<String> getAllDimNames() {
        return ALL_DIM_NAMES;
    }

    public static List<String> getAllTrimmedNames() {
        return ALL_TRIMMED_NAMES;
    }

    public static List<String> getAllDisplayedNames() {
        return ALL_DISPLAYED_NAMES;
    }

    public static Set<String> getDims(GT5OreLayerHelper.OreLayerWrapper oreLayer) {
        Set<String> enabledDims = new HashSet<>();
        Set<String> origNames = oreLayer.allowedDimWithOrigNames;

        for (String dimName : origNames) {
            Dimension record = REGISTRY.get(dimName);
            if (record != null) {
                enabledDims.add(record.abbr);
            }
        }
        return enabledDims;
    }

    public static String getDimAbbreviatedName(String internalName) {
        String abbr = INTERNAL_TO_ABBR.get(internalName);
        if (abbr != null) return abbr;
        throw new IllegalStateException("InternalName: " + internalName + " has no abbreviated name!");
    }

    public static String getFullName(String abbrDimName) {
        String internal = ABBR_TO_INTERNAL.get(abbrDimName);
        if (internal != null) return internal;
        throw new IllegalStateException("String: " + abbrDimName + " has no abbreviated name!");
    }

    public static String getDimTier(String dimName) {
        Dimension record = REGISTRY.get(dimName);
        return record == null ? T0 : record.tierKey;
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
