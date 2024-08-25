package bloodasp.galacticgreg.api.enums;

import bloodasp.galacticgreg.api.Enums;
import bloodasp.galacticgreg.api.ModDimensionDef;
import net.minecraft.world.gen.ChunkProviderEnd;

public enum DimensionDef {
    EndAsteroids(new ModDimensionDef(
        "EndAsteroids",
        ChunkProviderEnd.class,
        Enums.DimensionType.Asteroid)),
    Moon(new ModDimensionDef(
        "Moon",
        "micdoodle8.mods.galacticraft.core.world.gen.ChunkProviderMoon",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Moon.DBMDefList)),
    Mars(new ModDimensionDef(
        "Mars",
        "micdoodle8.mods.galacticraft.planets.mars.world.gen.ChunkProviderMars",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Mars.DBMDefList)),
    Asteroids(new ModDimensionDef(
        "Asteroids",
        "micdoodle8.mods.galacticraft.planets.asteroids.world.gen.ChunkProviderAsteroids",
        Enums.DimensionType.Asteroid)),
    Pluto(new ModDimensionDef(
        "Pluto",
        "galaxyspace.SolarSystem.planets.pluto.dimension.ChunkProviderPluto",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Pluto.DBMDefList)),
    Triton(new ModDimensionDef(
        "Triton",
        "galaxyspace.SolarSystem.moons.triton.dimension.ChunkProviderTriton",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Triton.DBMDefList)),
    Proteus(new ModDimensionDef(
        "Proteus",
        "galaxyspace.SolarSystem.moons.proteus.dimension.ChunkProviderProteus",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Proteus.DBMDefList)),
    Oberon(new ModDimensionDef(
        "Oberon",
        "galaxyspace.SolarSystem.moons.oberon.dimension.ChunkProviderOberon",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Oberon.DBMDefList)),
    Titan(new ModDimensionDef(
        "Titan",
        "galaxyspace.SolarSystem.moons.titan.dimension.ChunkProviderTitan",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Titan.DBMDefList)),
    Callisto(new ModDimensionDef(
        "Callisto",
        "galaxyspace.SolarSystem.moons.callisto.dimension.ChunkProviderCallisto",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Callisto.DBMDefList)),
    Ganymede(new ModDimensionDef(
        "Ganymede",
        "galaxyspace.SolarSystem.moons.ganymede.dimension.ChunkProviderGanymede",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Ganymede.DBMDefList)),
    Ceres(new ModDimensionDef(
        "Ceres",
        "galaxyspace.SolarSystem.planets.ceres.dimension.ChunkProviderCeres",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Ceres.DBMDefList)),
    Deimos(new ModDimensionDef(
        "Deimos",
        "galaxyspace.SolarSystem.moons.deimos.dimension.ChunkProviderDeimos",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Deimos.DBMDefList)),
    Enceladus( new ModDimensionDef(
        "Enceladus",
        "galaxyspace.SolarSystem.moons.enceladus.dimension.ChunkProviderEnceladus",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Enceladus.DBMDefList)),
    Io(new ModDimensionDef(
        "Io",
        "galaxyspace.SolarSystem.moons.io.dimension.ChunkProviderIo",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Io.DBMDefList)),
    Europa(new ModDimensionDef(
        "Europa",
        "galaxyspace.SolarSystem.moons.europa.dimension.ChunkProviderEuropa",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Europa.DBMDefList)),
    Phobos(new ModDimensionDef(
        "Phobos",
        "galaxyspace.SolarSystem.moons.phobos.dimension.ChunkProviderPhobos",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Phobos.DBMDefList)),
    Venus(new ModDimensionDef(
        "Venus",
        "galaxyspace.SolarSystem.planets.venus.dimension.ChunkProviderVenus",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Venus.DBMDefList)),
    Mercury(new ModDimensionDef(
        "Mercury",
        "galaxyspace.SolarSystem.planets.mercury.dimension.ChunkProviderMercury",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Mercury.DBMDefList)),
    MakeMake(new ModDimensionDef(
        "MakeMake",
        "galaxyspace.SolarSystem.planets.makemake.dimension.ChunkProviderMakemake",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.MakeMake.DBMDefList)),
    Haumea(new ModDimensionDef(
        "Haumea",
        "galaxyspace.SolarSystem.planets.haumea.dimension.ChunkProviderHaumea",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Haumea.DBMDefList)),
    CentauriAlpha(new ModDimensionDef(
        "CentauriA",
        "galaxyspace.ACentauriSystem.planets.aCentauriBb.dimension.ChunkProviderACentauri",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.CentauriAlpha.DBMDefList)),
    VegaB(new ModDimensionDef(
        "VegaB",
        "galaxyspace.VegaSystem.planets.vegaB.dimension.ChunkProviderVegaB",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.VegaB.DBMDefList)),
    BarnardC(new ModDimensionDef(
        "BarnardC",
        "galaxyspace.BarnardsSystem.planets.barnardaC.dimension.ChunkProviderBarnardaC",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.BarnardaC.DBMDefList)),
    BarnardE(new ModDimensionDef(
        "BarnardE",
        "galaxyspace.BarnardsSystem.planets.barnardaE.dimension.ChunkProviderBarnardaE",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.BarnardaE.DBMDefList)),
    BarnardF(new ModDimensionDef(
        "BarnardF",
        "galaxyspace.BarnardsSystem.planets.barnardaF.dimension.ChunkProviderBarnardaF",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.BarnardaF.DBMDefList)),
    TcetiE(new ModDimensionDef(
        "TcetiE",
        "galaxyspace.TCetiSystem.planets.tcetiE.dimension.ChunkProviderTCetiE",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.TcetiE.DBMDefList)),
    Miranda(new ModDimensionDef(
        "Miranda",
        "galaxyspace.SolarSystem.moons.miranda.dimension.ChunkProviderMiranda",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Miranda.DBMDefList)),
    KuiperBelt(new ModDimensionDef(
        "Kuiperbelt",
        "galaxyspace.SolarSystem.planets.kuiperbelt.dimension.ChunkProviderKuiper",
        Enums.DimensionType.Asteroid)),

    Neper(new ModDimensionDef(
        "Neper",
        "de.katzenpapst.amunra.world.neper.NeperChunkProvider",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Neper.DBMDefList)),
    Maahes(new ModDimensionDef(
        "Maahes",
        "de.katzenpapst.amunra.world.maahes.MaahesChunkProvider",
        Enums.DimensionType.Planet,
        DimensionBlockMetaDefinitionList.Maahes.DBMDefList)),
    Anubis(new ModDimensionDef(
        "Anubis",
        "de.katzenpapst.amunra.world.anubis.AnubisChunkProvider",
        Enums.DimensionType.Planet,
           DimensionBlockMetaDefinitionList.Anubis.DBMDefList)),
    Horus(new ModDimensionDef(
        "Horus",
        "de.katzenpapst.amunra.world.horus.HorusChunkProvider",
        Enums.DimensionType.Planet, DimensionBlockMetaDefinitionList.Horus.DBMDefList)),
    Seth( new ModDimensionDef(
        "Seth",
        "de.katzenpapst.amunra.world.seth.SethChunkProvider",
        Enums.DimensionType.Planet,DimensionBlockMetaDefinitionList.Seth.DBMDefList)),
    MehenBelt(new ModDimensionDef(
        "MehenBelt",
            "de.katzenpapst.amunra.world.mehen.MehenChunkProvider",
              Enums.DimensionType.Asteroid));
    public final ModDimensionDef modDimensionDef;
    private DimensionDef(ModDimensionDef modDimDef){
        this.modDimensionDef = modDimDef;
    }
}
