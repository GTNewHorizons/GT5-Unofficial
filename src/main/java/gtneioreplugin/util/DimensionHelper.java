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

import net.minecraft.util.StatCollector;

import com.github.bsideup.jabel.Desugar;
import com.google.common.collect.ImmutableList;

import gregtech.api.enums.StoneType;
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
    public record Dimension(String fullName, String internalName, String trimmedName, String abbr, String tierKey,
        List<StoneType> stoneTypes) {}

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
        register("Overworld", OW, "Overworld", "Ow", T0, ImmutableList.of(StoneType.Stone));
        register("Nether", NETHER, "Nether", "Ne", T0, ImmutableList.of(StoneType.Netherrack));
        register("Twilight", TWILIGHT_FOREST, "Twilight", "TF", T0, ImmutableList.of(StoneType.Stone));
        register("The End", THE_END, "The End", "ED", T0, ImmutableList.of(StoneType.Endstone));
        register("EndAsteroid", ENDASTEROID, "EndAsteroid", "EA", T0, ImmutableList.of(StoneType.Stone));
        register("dimensionDarkWorld", EVERGLADES, "dimensionDarkWorld", "Eg", T0, ImmutableList.of(StoneType.Stone));

        // T1
        register("GalacticraftCore_Moon", MOON, "Moon", "Mo", T1, ImmutableList.of(StoneType.Moon));

        // T2
        register("GalaxySpace_Deimos", DEIMOS, "De", "De", T2, ImmutableList.of(StoneType.Deimos));
        register("GalacticraftMars_Mars", MARS, "Mars", "Ma", T2, ImmutableList.of(StoneType.Mars));
        register("GalaxySpace_Phobos", PHOBOS, "Phobos", "Ph", T2, ImmutableList.of(StoneType.Phobos));

        // T3
        register("GalacticraftMars_Asteroids", ASTEROIDS, "Asteroids", "As", T3, ImmutableList.of(StoneType.Asteroid));
        register("GalaxySpace_Callisto", CALLISTO, "Callisto", "Ca", T3, ImmutableList.of(StoneType.Callisto));
        register("GalaxySpace_Ceres", CERES, "Ceres", "Ce", T3, ImmutableList.of(StoneType.Ceres));
        register("GalaxySpace_Europa", EUROPA, "Europa", "Eu", T3, ImmutableList.of(StoneType.Europa));
        register("GalaxySpace_Ganymede", GANYMEDE, "Ganymede", "Ga", T3, ImmutableList.of(StoneType.Ganymede));
        register("Ross128b", ROSS128B, "Ross128b", "Rb", T3, ImmutableList.of(StoneType.Stone));

        // T4
        register("GalaxySpace_Io", IO, "Io", "Io", T4, ImmutableList.of(StoneType.Io));
        register("GalaxySpace_Mercury", MERCURY, "Mercury", "Me", T4, ImmutableList.of(StoneType.Mercury));
        register("GalaxySpace_Venus", VENUS, "Venus", "Ve", T4, ImmutableList.of(StoneType.Venus));

        // T5
        register("GalaxySpace_Enceladus", ENCELADUS, "Enceladus", "En", T5, ImmutableList.of(StoneType.Enceladus));
        register("GalaxySpace_Miranda", MIRANDA, "Miranda", "Mi", T5, ImmutableList.of(StoneType.Miranda));
        register("GalaxySpace_Oberon", OBERON, "Oberon", "Ob", T5, ImmutableList.of(StoneType.Oberon));
        register("GalaxySpace_Titan", TITAN, "Titan", "Ti", T5, ImmutableList.of(StoneType.Titan));
        register("Ross128ba", ROSS128BA, "Ross128ba", "Ra", T5, ImmutableList.of(StoneType.Stone));

        // T6
        register("GalaxySpace_Proteus", PROTEUS, "Proteus", "Pr", T6, ImmutableList.of(StoneType.Proteus));
        register("GalaxySpace_Triton", TRITON, "Triton", "Tr", T6, ImmutableList.of(StoneType.Triton));

        // T7
        register("GalaxySpace_Haumea", HAUMEA, "Haumea", "Ha", T7, ImmutableList.of(StoneType.Haumea));
        register("GalaxySpace_Kuiperbelt", KUIPERBELT, "Kuiperbelt", "KB", T7, ImmutableList.of(StoneType.Stone));
        register("GalaxySpace_MakeMake", MAKEMAKE, "MakeMake", "MM", T7, ImmutableList.of(StoneType.MakeMake));
        register("GalaxySpace_Pluto", PLUTO, "Pluto", "Pl", T7, ImmutableList.of(StoneType.Pluto));

        // T8
        register("GalaxySpace_BarnardC", BARNARDC, "BarnardC", "BC", T8, ImmutableList.of(StoneType.Stone));
        register("GalaxySpace_BarnardE", BARNARDE, "BarnardE", "BE", T8, ImmutableList.of(StoneType.BarnardaE));
        register("GalaxySpace_BarnardF", BARNARDF, "BarnardF", "BF", T8, ImmutableList.of(StoneType.BarnardaF));
        register("GalaxySpace_CentauriA", CENTAURIBB, "CentauriA", "CB", T8, ImmutableList.of(StoneType.AlphaCentauri));
        register("GalaxySpace_TcetiE", TCETIE, "TcetiE", "TE", T8, ImmutableList.of(StoneType.TCetiE));
        register("GalaxySpace_VegaB", VEGAB, "VegaB", "VB", T8, ImmutableList.of(StoneType.VegaB));

        // T9
        register("GalacticraftAmunRa_Anubis", ANUBIS, "Anubis", "An", T9, ImmutableList.of(StoneType.AnubisAndMaahes));
        register("GalacticraftAmunRa_Horus", HORUS, "Horus", "Ho", T9, ImmutableList.of(StoneType.Horus));
        register("GalacticraftAmunRa_Maahes", MAAHES, "Maahes", "Mh", T9, ImmutableList.of(StoneType.AnubisAndMaahes));
        register("GalacticraftAmunRa_MehenBelt", MEHENBELT, "MehenBelt", "MB", T9, ImmutableList.of(StoneType.Stone));
        register("GalacticraftAmunRa_Neper", NEPER, "Neper", "Np", T9, ImmutableList.of(StoneType.Stone));
        register(
            "GalacticraftAmunRa_Seth",
            SETH,
            "Seth",
            "Se",
            T9,
            ImmutableList.of(StoneType.SethIce, StoneType.SethClay));

        // T10
        register("Underdark", DEEPDARK, "Underdark", "DD", T10, ImmutableList.of(StoneType.Stone));
    }

    public static void register(String fullName, String internalName, String trimmedName, String abbr, String tierKey,
        List<StoneType> stoneTypes) {
        if (!REGISTRY.containsKey(fullName)) {
            Dimension dim = new Dimension(fullName, internalName, trimmedName, abbr, tierKey, stoneTypes);
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
        return StatCollector.translateToLocal(getDimUnlocalizedName(dimName));
    }
}
