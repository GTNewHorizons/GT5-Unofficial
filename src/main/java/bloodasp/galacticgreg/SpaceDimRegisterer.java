package bloodasp.galacticgreg;

import static gregtech.api.enums.Mods.GalacticraftCore;
import static gregtech.api.enums.Mods.GalacticraftMars;
import static gregtech.api.enums.Mods.GalaxySpace;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.world.gen.ChunkProviderEnd;

import bloodasp.galacticgreg.api.AsteroidBlockComb;
import bloodasp.galacticgreg.api.Enums;
import bloodasp.galacticgreg.api.GTOreTypes;
import bloodasp.galacticgreg.api.ModContainer;
import bloodasp.galacticgreg.api.ModDBMDef;
import bloodasp.galacticgreg.api.ModDimensionDef;
import bloodasp.galacticgreg.api.SpecialBlockComb;

/**
 * In this class, you'll find everything you need in order to tell GGreg what to do and where. Everything is done in
 * here. If you're trying to use anything else, you're probably doing something wrong (Or I forgot to add it. In that
 * case, find me on github and create an issue please)
 */
public class SpaceDimRegisterer {

    private static Method registerModContainer;

    /**
     * Use loose binding of the register-method. Should be enough to provide support for GGreg without the requirement
     * to have it in a modpack at all
     *
     * @param pModContainer
     */
    public static void registerModContainer(ModContainer pModContainer) {
        try {
            registerModContainer.invoke(null, pModContainer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Try to get the instance of GalacticGregs registry in order to register stuff
     *
     * @return {@code false} if GalacticGreg is not installed or something went wrong
     */
    public boolean init() {
        try {
            Class<?> gGregRegistry = Class.forName("bloodasp.galacticgreg.registry.GalacticGregRegistry");
            registerModContainer = gGregRegistry.getMethod("registerModContainer", ModContainer.class);

            return true;
        } catch (Exception e) {
            // GalacticGreg is not installed or something is wrong
            return false;
        }
    }

    public void register() {
        registerModContainer(setupVanilla());
        registerModContainer(setupGalactiCraftCore());
        registerModContainer(setupGalactiCraftPlanets());
        registerModContainer(setupGalaxySpace());
        registerModContainer(setupAmunRa());
    }

    /**
     * Vanilla MC (End Asteroids)
     */
    private ModContainer setupVanilla() {
        // --- Mod Vanilla (Heh, "mod")
        ModContainer modMCVanilla = new ModContainer("Vanilla");

        // If you happen to have an asteroid dim, just skip the blocklist, and setDimensionType() to
        // DimensionType.Asteroid
        // also don't forget to add at least one asteroid type, or nothing will generate!
        ModDimensionDef dimEndAsteroids = new ModDimensionDef(
            "EndAsteroids",
            ChunkProviderEnd.class,
            Enums.DimensionType.Asteroid);

        dimEndAsteroids.addAsteroidMaterial(new AsteroidBlockComb(GTOreTypes.Netherrack));
        dimEndAsteroids.addAsteroidMaterial(new AsteroidBlockComb(GTOreTypes.RedGranite));
        dimEndAsteroids.addAsteroidMaterial(new AsteroidBlockComb(GTOreTypes.BlackGranite));
        dimEndAsteroids.addAsteroidMaterial(new AsteroidBlockComb(GTOreTypes.EndStone));

        // These Blocks will randomly be generated
        dimEndAsteroids.addSpecialAsteroidBlock(new SpecialBlockComb(Blocks.glowstone));
        dimEndAsteroids
            .addSpecialAsteroidBlock(new SpecialBlockComb(Blocks.lava, Enums.AllowedBlockPosition.AsteroidCore));

        modMCVanilla.addDimensionDef(dimEndAsteroids);

        return modMCVanilla;
    }

    /**
     * Mod GalactiCraft
     */
    private ModContainer setupGalactiCraftCore() {
        ModContainer modGCraftCore = new ModContainer(GalacticraftCore.ID);
        ModDBMDef DBMMoon = new ModDBMDef("tile.moonBlock", 4);

        ModDimensionDef tMoonDim = new ModDimensionDef(
            "Moon",
            "micdoodle8.mods.galacticraft.core.world.gen.ChunkProviderMoon",
            Enums.DimensionType.Planet,
            Collections.singletonList(DBMMoon));
        modGCraftCore.addDimensionDef(tMoonDim);

        return modGCraftCore;
    }

    /**
     * As GalactiCraftPlanets is an optional mod, don't hardlink it here
     */
    private ModContainer setupGalactiCraftPlanets() {
        ModContainer modGCraftPlanets = new ModContainer(GalacticraftMars.ID);
        ModDBMDef DBMMars = new ModDBMDef("tile.mars", 9);
        ModDimensionDef dimMars = new ModDimensionDef(
            "Mars",
            "micdoodle8.mods.galacticraft.planets.mars.world.gen.ChunkProviderMars",
            Enums.DimensionType.Planet,
            Collections.singletonList(DBMMars));

        // Overwrite ore blocks on mars with red granite ones. This will default to regular stone if not set
        dimMars.setStoneType(GTOreTypes.RedGranite);
        modGCraftPlanets.addDimensionDef(dimMars);

        ModDimensionDef dimAsteroids = new ModDimensionDef(
            "Asteroids",
            "micdoodle8.mods.galacticraft.planets.asteroids.world.gen.ChunkProviderAsteroids",
            Enums.DimensionType.Asteroid);
        dimAsteroids.addAsteroidMaterial(new AsteroidBlockComb(GTOreTypes.BlackGranite));
        dimAsteroids.addAsteroidMaterial(new AsteroidBlockComb(GTOreTypes.RedGranite));
        dimAsteroids.addAsteroidMaterial(new AsteroidBlockComb(GTOreTypes.Netherrack));
        modGCraftPlanets.addDimensionDef(dimAsteroids);

        return modGCraftPlanets;
    }

    /**
     * Mod GalaxySpace by BlesseNtumble
     */
    private ModContainer setupGalaxySpace() {
        // First, we create a mod-container that will be populated with dimensions later.
        // The Name must match your ID, as it is checked if this mod is loaded, in order
        // to enable/disable the parsing/registering of dimensions
        ModContainer modCGalaxySpace = new ModContainer(GalaxySpace.ID);

        // Now lets first define a block here for our dimension. You can add the ID, but you don't have to.
        // It will automatically add the mods name that is defined in the modcontainer.
        final ModDBMDef DBMPhobos = new ModDBMDef("phobosblocks", 2);
        final ModDBMDef DBMDeimos = new ModDBMDef("deimosblocks", 1);
        final ModDBMDef DBMCeres = new ModDBMDef("ceresblocks", 1);
        final ModDBMDef DBMIO = new ModDBMDef("ioblocks", 2);
        final ModDBMDef DBMGanymede = new ModDBMDef("ganymedeblocks", 1);
        final ModDBMDef DBMCallisto = new ModDBMDef("callistoblocks", 1);
        final ModDBMDef DBMVenus = new ModDBMDef("venusblocks", 1);
        final ModDBMDef DBMMercury = new ModDBMDef("mercuryblocks", 2);
        final ModDBMDef DBMEnceladus = new ModDBMDef("enceladusblocks", 1);
        final ModDBMDef DBMTitan = new ModDBMDef("titanblocks", 2);
        final ModDBMDef DBMOberon = new ModDBMDef("oberonblocks", 2);
        final ModDBMDef DBMProteus = new ModDBMDef("proteusblocks", 2);
        final ModDBMDef DBMTriton = new ModDBMDef("tritonblocks", 2);
        final ModDBMDef DBMPluto = new ModDBMDef("plutoblocks", 5);
        final ModDBMDef DBMMakemake = new ModDBMDef("makemakegrunt", 1);
        final ModDBMDef DBMHaumea = new ModDBMDef("haumeablocks");
        final ModDBMDef DBMCentauriA = new ModDBMDef("acentauribbsubgrunt");
        final ModDBMDef DBMVegaB = new ModDBMDef("vegabsubgrunt");

        List<ModDBMDef> DBMbarnardaC = new ArrayList<ModDBMDef>();
        DBMbarnardaC.add(new ModDBMDef("barnardaCdirt"));
        DBMbarnardaC.add(new ModDBMDef(Blocks.stone));

        final ModDBMDef DBMbarnardaE = new ModDBMDef("barnardaEsubgrunt");
        final ModDBMDef DBMbarnardaF = new ModDBMDef("barnardaFsubgrunt");
        final ModDBMDef DBMTcetiE = new ModDBMDef("tcetieblocks", 2);
        final ModDBMDef DBMMiranda = new ModDBMDef("mirandablocks", 2);

        List<ModDBMDef> DBMEuropa = new ArrayList<ModDBMDef>();
        // DBMEuropa.add(new ModDBMDef("europagrunt")); //Europa top layer turned off bc ores are too easy to spot
        DBMEuropa.add(new ModDBMDef("europagrunt", 1)); // Europa Ice Layer ~55-65 without top layer
        DBMEuropa.add(new ModDBMDef(Blocks.water));
        DBMEuropa.add(new ModDBMDef(Blocks.flowing_water));
        DBMEuropa.add(new ModDBMDef(Blocks.ice)); // Generates directly over bedrock
        DBMEuropa.add(new ModDBMDef(Blocks.packed_ice)); // Generates directly over bedrock
        DBMEuropa.add(new ModDBMDef("europaunderwatergeyser")); // Generates directly over bedrock

        // Now define the available dimensions, and their chunkprovider.
        // Same as above, to not have any dependency in your code, you can just give it a string.
        // But it's better to use the actual ChunkProvider class. The Name is used for the GalacticGreg config file.
        // The resulting config setting will be: <ModID>_<Name you give here as arg0>_false = false
        // make sure to never change this name once you've generated your config files, as it will overwrite everything!

        // 30.11.2016 GSpace v1.1.3 Stable
        modCGalaxySpace.addDimensionDef(
            new ModDimensionDef(
                "Pluto",
                "galaxyspace.SolarSystem.planets.pluto.dimension.ChunkProviderPluto",
                Enums.DimensionType.Planet,
                Collections.singletonList(DBMPluto)));
        modCGalaxySpace.addDimensionDef(
            new ModDimensionDef(
                "Triton",
                "galaxyspace.SolarSystem.moons.triton.dimension.ChunkProviderTriton",
                Enums.DimensionType.Planet,
                Collections.singletonList(DBMTriton)));
        modCGalaxySpace.addDimensionDef(
            new ModDimensionDef(
                "Proteus",
                "galaxyspace.SolarSystem.moons.proteus.dimension.ChunkProviderProteus",
                Enums.DimensionType.Planet,
                Collections.singletonList(DBMProteus)));
        modCGalaxySpace.addDimensionDef(
            new ModDimensionDef(
                "Oberon",
                "galaxyspace.SolarSystem.moons.oberon.dimension.ChunkProviderOberon",
                Enums.DimensionType.Planet,
                Collections.singletonList(DBMOberon)));
        modCGalaxySpace.addDimensionDef(
            new ModDimensionDef(
                "Titan",
                "galaxyspace.SolarSystem.moons.titan.dimension.ChunkProviderTitan",
                Enums.DimensionType.Planet,
                Collections.singletonList(DBMTitan)));
        modCGalaxySpace.addDimensionDef(
            new ModDimensionDef(
                "Callisto",
                "galaxyspace.SolarSystem.moons.callisto.dimension.ChunkProviderCallisto",
                Enums.DimensionType.Planet,
                Collections.singletonList(DBMCallisto)));
        modCGalaxySpace.addDimensionDef(
            new ModDimensionDef(
                "Ganymede",
                "galaxyspace.SolarSystem.moons.ganymede.dimension.ChunkProviderGanymede",
                Enums.DimensionType.Planet,
                Collections.singletonList(DBMGanymede)));
        modCGalaxySpace.addDimensionDef(
            new ModDimensionDef(
                "Ceres",
                "galaxyspace.SolarSystem.planets.ceres.dimension.ChunkProviderCeres",
                Enums.DimensionType.Planet,
                Collections.singletonList(DBMCeres)));
        modCGalaxySpace.addDimensionDef(
            new ModDimensionDef(
                "Deimos",
                "galaxyspace.SolarSystem.moons.deimos.dimension.ChunkProviderDeimos",
                Enums.DimensionType.Planet,
                Collections.singletonList(DBMDeimos)));
        modCGalaxySpace.addDimensionDef(
            new ModDimensionDef(
                "Enceladus",
                "galaxyspace.SolarSystem.moons.enceladus.dimension.ChunkProviderEnceladus",
                Enums.DimensionType.Planet,
                Collections.singletonList(DBMEnceladus)));
        modCGalaxySpace.addDimensionDef(
            new ModDimensionDef(
                "Io",
                "galaxyspace.SolarSystem.moons.io.dimension.ChunkProviderIo",
                Enums.DimensionType.Planet,
                Collections.singletonList(DBMIO)));
        modCGalaxySpace.addDimensionDef(
            new ModDimensionDef(
                "Europa",
                "galaxyspace.SolarSystem.moons.europa.dimension.ChunkProviderEuropa",
                Enums.DimensionType.Planet,
                DBMEuropa));
        modCGalaxySpace.addDimensionDef(
            new ModDimensionDef(
                "Phobos",
                "galaxyspace.SolarSystem.moons.phobos.dimension.ChunkProviderPhobos",
                Enums.DimensionType.Planet,
                Collections.singletonList(DBMPhobos)));
        modCGalaxySpace.addDimensionDef(
            new ModDimensionDef(
                "Venus",
                "galaxyspace.SolarSystem.planets.venus.dimension.ChunkProviderVenus",
                Enums.DimensionType.Planet,
                Collections.singletonList(DBMVenus)));
        modCGalaxySpace.addDimensionDef(
            new ModDimensionDef(
                "Mercury",
                "galaxyspace.SolarSystem.planets.mercury.dimension.ChunkProviderMercury",
                Enums.DimensionType.Planet,
                Collections.singletonList(DBMMercury)));
        modCGalaxySpace.addDimensionDef(
            new ModDimensionDef(
                "MakeMake",
                "galaxyspace.SolarSystem.planets.makemake.dimension.ChunkProviderMakemake",
                Enums.DimensionType.Planet,
                Collections.singletonList(DBMMakemake)));
        modCGalaxySpace.addDimensionDef(
            new ModDimensionDef(
                "Haumea",
                "galaxyspace.SolarSystem.planets.haumea.dimension.ChunkProviderHaumea",
                Enums.DimensionType.Planet,
                Collections.singletonList(DBMHaumea)));
        modCGalaxySpace.addDimensionDef(
            new ModDimensionDef(
                "CentauriA",
                "galaxyspace.ACentauriSystem.planets.aCentauriBb.dimension.ChunkProviderACentauri",
                Enums.DimensionType.Planet,
                Collections.singletonList(DBMCentauriA)));
        modCGalaxySpace.addDimensionDef(
            new ModDimensionDef(
                "VegaB",
                "galaxyspace.VegaSystem.planets.vegaB.dimension.ChunkProviderVegaB",
                Enums.DimensionType.Planet,
                Collections.singletonList(DBMVegaB)));
        modCGalaxySpace.addDimensionDef(
            new ModDimensionDef(
                "BarnardC",
                "galaxyspace.BarnardsSystem.planets.barnardaC.dimension.ChunkProviderBarnardaC",
                Enums.DimensionType.Planet,
                DBMbarnardaC));
        modCGalaxySpace.addDimensionDef(
            new ModDimensionDef(
                "BarnardE",
                "galaxyspace.BarnardsSystem.planets.barnardaE.dimension.ChunkProviderBarnardaE",
                Enums.DimensionType.Planet,
                Collections.singletonList(DBMbarnardaE)));
        modCGalaxySpace.addDimensionDef(
            new ModDimensionDef(
                "BarnardF",
                "galaxyspace.BarnardsSystem.planets.barnardaF.dimension.ChunkProviderBarnardaF",
                Enums.DimensionType.Planet,
                Collections.singletonList(DBMbarnardaF)));
        modCGalaxySpace.addDimensionDef(
            new ModDimensionDef(
                "TcetiE",
                "galaxyspace.TCetiSystem.planets.tcetiE.dimension.ChunkProviderTCetiE",
                Enums.DimensionType.Planet,
                Collections.singletonList(DBMTcetiE)));
        modCGalaxySpace.addDimensionDef(
            new ModDimensionDef(
                "Miranda",
                "galaxyspace.SolarSystem.moons.miranda.dimension.ChunkProviderMiranda",
                Enums.DimensionType.Planet,
                Collections.singletonList(DBMMiranda)));

        ModDimensionDef dimKupierBelt = new ModDimensionDef(
            "Kuiperbelt",
            "galaxyspace.SolarSystem.planets.kuiperbelt.dimension.ChunkProviderKuiper",
            Enums.DimensionType.Asteroid);

        dimKupierBelt.setDimensionType(Enums.DimensionType.Asteroid);
        dimKupierBelt.addAsteroidMaterial(new AsteroidBlockComb(GTOreTypes.RedGranite));
        dimKupierBelt.addAsteroidMaterial(new AsteroidBlockComb(GTOreTypes.BlackGranite));
        modCGalaxySpace.addDimensionDef(dimKupierBelt);
        return modCGalaxySpace;
    }

    /**
     * Mod Amun-Ra
     */
    private ModContainer setupAmunRa() {
        ModContainer modAmunRa = new ModContainer("GalacticraftAmunRa");

        final List<ModDBMDef> DBMNeper = Arrays
            .asList(new ModDBMDef(Blocks.stone), new ModDBMDef("tile.baseBlockRock", 10));
        final List<ModDBMDef> DBMMaahes = Collections.singletonList(new ModDBMDef("tile.baseBlockRock", 1));
        final List<ModDBMDef> DBMAnubis = Collections.singletonList(new ModDBMDef("tile.baseBlockRock", 1));
        final List<ModDBMDef> DBMHorus = Collections.singletonList(new ModDBMDef(Blocks.obsidian));
        final List<ModDBMDef> DBMSeth = Collections.singletonList(new ModDBMDef(Blocks.hardened_clay));

        modAmunRa.addDimensionDef(
            new ModDimensionDef(
                "Neper",
                "de.katzenpapst.amunra.world.neper.NeperChunkProvider",
                Enums.DimensionType.Planet,
                DBMNeper));
        modAmunRa.addDimensionDef(
            new ModDimensionDef(
                "Maahes",
                "de.katzenpapst.amunra.world.maahes.MaahesChunkProvider",
                Enums.DimensionType.Planet,
                DBMMaahes));
        modAmunRa.addDimensionDef(
            new ModDimensionDef(
                "Anubis",
                "de.katzenpapst.amunra.world.anubis.AnubisChunkProvider",
                Enums.DimensionType.Planet,
                DBMAnubis));
        modAmunRa.addDimensionDef(
            new ModDimensionDef(
                "Horus",
                "de.katzenpapst.amunra.world.horus.HorusChunkProvider",
                Enums.DimensionType.Planet,
                DBMHorus));
        modAmunRa.addDimensionDef(
            new ModDimensionDef(
                "Seth",
                "de.katzenpapst.amunra.world.seth.SethChunkProvider",
                Enums.DimensionType.Planet,
                DBMSeth));

        ModDimensionDef dimMehenBelt = new ModDimensionDef(
            "MehenBelt",
            "de.katzenpapst.amunra.world.mehen.MehenChunkProvider",
            Enums.DimensionType.Asteroid);
        dimMehenBelt.addAsteroidMaterial(GTOreTypes.BlackGranite);
        modAmunRa.addDimensionDef(dimMehenBelt);

        return modAmunRa;
    }
}
