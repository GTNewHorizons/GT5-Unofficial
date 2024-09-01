package galacticgreg.api.enums;

import net.minecraft.world.gen.ChunkProviderEnd;

import galacticgreg.api.Enums;
import galacticgreg.api.ModDimensionDef;

public enum DimensionDef {

    EndAsteroids(new ModDimensionDef(DimNames.ENDASTEROIDS, ChunkProviderEnd.class, Enums.DimensionType.Asteroid)),
    Moon(new ModDimensionDef(
        DimNames.MOON,
        "micdoodle8.mods.galacticraft.core.world.gen.ChunkProviderMoon",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Moon.DBMDefList)),
    Mars(new ModDimensionDef(
        DimNames.MARS,
        "micdoodle8.mods.galacticraft.planets.mars.world.gen.ChunkProviderMars",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Mars.DBMDefList)),
    Asteroids(new ModDimensionDef(
        DimNames.ASTEROIDS,
        "micdoodle8.mods.galacticraft.planets.asteroids.world.gen.ChunkProviderAsteroids",
        Enums.DimensionType.Asteroid)),
    Pluto(new ModDimensionDef(
        DimNames.PLUTO,
        "galaxyspace.SolarSystem.planets.pluto.dimension.ChunkProviderPluto",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Pluto.DBMDefList)),
    Triton(new ModDimensionDef(
        DimNames.TRITON,
        "galaxyspace.SolarSystem.moons.triton.dimension.ChunkProviderTriton",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Triton.DBMDefList)),
    Proteus(new ModDimensionDef(
        DimNames.PROTEUS,
        "galaxyspace.SolarSystem.moons.proteus.dimension.ChunkProviderProteus",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Proteus.DBMDefList)),
    Oberon(new ModDimensionDef(
        DimNames.OBERON,
        "galaxyspace.SolarSystem.moons.oberon.dimension.ChunkProviderOberon",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Oberon.DBMDefList)),
    Titan(new ModDimensionDef(
        DimNames.TITAN,
        "galaxyspace.SolarSystem.moons.titan.dimension.ChunkProviderTitan",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Titan.DBMDefList)),
    Callisto(new ModDimensionDef(
        DimNames.CALLISTO,
        "galaxyspace.SolarSystem.moons.callisto.dimension.ChunkProviderCallisto",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Callisto.DBMDefList)),
    Ganymede(new ModDimensionDef(
        DimNames.GANYMEDE,
        "galaxyspace.SolarSystem.moons.ganymede.dimension.ChunkProviderGanymede",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Ganymede.DBMDefList)),
    Ceres(new ModDimensionDef(
        DimNames.CERES,
        "galaxyspace.SolarSystem.planets.ceres.dimension.ChunkProviderCeres",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Ceres.DBMDefList)),
    Deimos(new ModDimensionDef(
        DimNames.DEIMOS,
        "galaxyspace.SolarSystem.moons.deimos.dimension.ChunkProviderDeimos",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Deimos.DBMDefList)),
    Enceladus(new ModDimensionDef(
        DimNames.ENCELADUS,
        "galaxyspace.SolarSystem.moons.enceladus.dimension.ChunkProviderEnceladus",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Enceladus.DBMDefList)),
    Io(new ModDimensionDef(
        DimNames.IO,
        "galaxyspace.SolarSystem.moons.io.dimension.ChunkProviderIo",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Io.DBMDefList)),
    Europa(new ModDimensionDef(
        DimNames.EUROPA,
        "galaxyspace.SolarSystem.moons.europa.dimension.ChunkProviderEuropa",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Europa.DBMDefList)),
    Phobos(new ModDimensionDef(
        DimNames.PHOBOS,
        "galaxyspace.SolarSystem.moons.phobos.dimension.ChunkProviderPhobos",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Phobos.DBMDefList)),
    Venus(new ModDimensionDef(
        DimNames.VENUS,
        "galaxyspace.SolarSystem.planets.venus.dimension.ChunkProviderVenus",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Venus.DBMDefList)),
    Mercury(new ModDimensionDef(
        DimNames.MERCURY,
        "galaxyspace.SolarSystem.planets.mercury.dimension.ChunkProviderMercury",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Mercury.DBMDefList)),
    MakeMake(new ModDimensionDef(
        DimNames.MAKEMAKE,
        "galaxyspace.SolarSystem.planets.makemake.dimension.ChunkProviderMakemake",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.MakeMake.DBMDefList)),
    Haumea(new ModDimensionDef(
        DimNames.HAUMEA,
        "galaxyspace.SolarSystem.planets.haumea.dimension.ChunkProviderHaumea",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Haumea.DBMDefList)),
    CentauriAlpha(new ModDimensionDef(
        DimNames.CENTAURIA,
        "galaxyspace.ACentauriSystem.planets.aCentauriBb.dimension.ChunkProviderACentauri",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.CentauriAlpha.DBMDefList)),
    VegaB(new ModDimensionDef(
        DimNames.VEGAB,
        "galaxyspace.VegaSystem.planets.vegaB.dimension.ChunkProviderVegaB",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.VegaB.DBMDefList)),
    BarnardC(new ModDimensionDef(
        DimNames.BARNARDC,
        "galaxyspace.BarnardsSystem.planets.barnardaC.dimension.ChunkProviderBarnardaC",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.BarnardaC.DBMDefList)),
    BarnardE(new ModDimensionDef(
        DimNames.BARNARDE,
        "galaxyspace.BarnardsSystem.planets.barnardaE.dimension.ChunkProviderBarnardaE",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.BarnardaE.DBMDefList)),
    BarnardF(new ModDimensionDef(
        DimNames.BARNARDF,
        "galaxyspace.BarnardsSystem.planets.barnardaF.dimension.ChunkProviderBarnardaF",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.BarnardaF.DBMDefList)),
    TcetiE(new ModDimensionDef(
        DimNames.TCETIE,
        "galaxyspace.TCetiSystem.planets.tcetiE.dimension.ChunkProviderTCetiE",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.TcetiE.DBMDefList)),
    Miranda(new ModDimensionDef(
        DimNames.MIRANDA,
        "galaxyspace.SolarSystem.moons.miranda.dimension.ChunkProviderMiranda",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Miranda.DBMDefList)),
    KuiperBelt(new ModDimensionDef(
        DimNames.KUIPERBELT,
        "galaxyspace.SolarSystem.planets.kuiperbelt.dimension.ChunkProviderKuiper",
        Enums.DimensionType.Asteroid)),

    Neper(new ModDimensionDef(
        DimNames.NEPER,
        "de.katzenpapst.amunra.world.neper.NeperChunkProvider",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Neper.DBMDefList)),
    Maahes(new ModDimensionDef(
        DimNames.MAAHES,
        "de.katzenpapst.amunra.world.maahes.MaahesChunkProvider",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Maahes.DBMDefList)),
    Anubis(new ModDimensionDef(
        DimNames.ANUBIS,
        "de.katzenpapst.amunra.world.anubis.AnubisChunkProvider",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Anubis.DBMDefList)),
    Horus(new ModDimensionDef(
        DimNames.HORUS,
        "de.katzenpapst.amunra.world.horus.HorusChunkProvider",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Horus.DBMDefList)),
    Seth(new ModDimensionDef(
        DimNames.SETH,
        "de.katzenpapst.amunra.world.seth.SethChunkProvider",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Seth.DBMDefList)),
    MehenBelt(new ModDimensionDef(
        DimNames.MEHENBELT,
        "de.katzenpapst.amunra.world.mehen.MehenChunkProvider",
        Enums.DimensionType.Asteroid));

    public static class DimNames {

        public static final String ENDASTEROIDS = "EndAsteroids";
        public static final String MOON = "Moon";
        public static final String MARS = "Mars";
        public static final String ASTEROIDS = "Asteroids";
        public static final String PLUTO = "Pluto";
        public static final String TRITON = "Triton";
        public static final String PROTEUS = "Proteus";
        public static final String OBERON = "Oberon";
        public static final String TITAN = "Titan";
        public static final String ROSS128B = "Ross128b";
        public static final String ROSS128BA = "Ross128ba";
        public static final String CALLISTO = "Callisto";
        public static final String GANYMEDE = "Ganymede";
        public static final String CERES = "Ceres";
        public static final String DEIMOS = "Deimos";
        public static final String ENCELADUS = "Enceladus";
        public static final String IO = "Io";
        public static final String EUROPA = "Europa";
        public static final String PHOBOS = "Phobos";
        public static final String VENUS = "Venus";
        public static final String MERCURY = "Mercury";
        public static final String MAKEMAKE = "MakeMake";
        public static final String HAUMEA = "Haumea";
        public static final String CENTAURIA = "CentauriA";
        public static final String VEGAB = "VegaB";
        public static final String BARNARDC = "BarnardC";
        public static final String BARNARDE = "BarnardE";
        public static final String BARNARDF = "BarnardF";
        public static final String TCETIE = "TcetiE";
        public static final String MIRANDA = "Miranda";
        public static final String KUIPERBELT = "Kuiperbelt";
        public static final String NEPER = "Neper";
        public static final String MAAHES = "Maahes";
        public static final String ANUBIS = "Anubis";
        public static final String HORUS = "Horus";
        public static final String SETH = "Seth";
        public static final String MEHENBELT = "MehenBelt";
        public static final String DEEPDARK = "Underdark";

    }

    public final ModDimensionDef modDimensionDef;

    private DimensionDef(ModDimensionDef modDimDef) {
        this.modDimensionDef = modDimDef;
    }
}
