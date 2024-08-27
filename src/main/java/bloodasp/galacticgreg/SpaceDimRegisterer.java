package bloodasp.galacticgreg;

import static bloodasp.galacticgreg.api.enums.DimensionDef.Anubis;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Asteroids;
import static bloodasp.galacticgreg.api.enums.DimensionDef.BarnardC;
import static bloodasp.galacticgreg.api.enums.DimensionDef.BarnardE;
import static bloodasp.galacticgreg.api.enums.DimensionDef.BarnardF;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Callisto;
import static bloodasp.galacticgreg.api.enums.DimensionDef.CentauriAlpha;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Ceres;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Deimos;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Enceladus;
import static bloodasp.galacticgreg.api.enums.DimensionDef.EndAsteroids;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Europa;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Ganymede;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Haumea;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Horus;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Io;
import static bloodasp.galacticgreg.api.enums.DimensionDef.KuiperBelt;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Maahes;
import static bloodasp.galacticgreg.api.enums.DimensionDef.MakeMake;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Mars;
import static bloodasp.galacticgreg.api.enums.DimensionDef.MehenBelt;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Mercury;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Miranda;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Moon;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Neper;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Oberon;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Phobos;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Pluto;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Proteus;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Seth;
import static bloodasp.galacticgreg.api.enums.DimensionDef.TcetiE;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Titan;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Triton;
import static bloodasp.galacticgreg.api.enums.DimensionDef.VegaB;
import static bloodasp.galacticgreg.api.enums.DimensionDef.Venus;
import static bloodasp.galacticgreg.api.enums.ModContainers.AmunRa;
import static bloodasp.galacticgreg.api.enums.ModContainers.GalactiCraftCore;
import static bloodasp.galacticgreg.api.enums.ModContainers.GalacticraftMars;
import static bloodasp.galacticgreg.api.enums.ModContainers.GalaxySpace;
import static bloodasp.galacticgreg.api.enums.ModContainers.Vanilla;

import net.minecraft.init.Blocks;

import bloodasp.galacticgreg.api.AsteroidBlockComb;
import bloodasp.galacticgreg.api.Enums;
import bloodasp.galacticgreg.api.GTOreTypes;
import bloodasp.galacticgreg.api.ModContainer;
import bloodasp.galacticgreg.api.SpecialBlockComb;
import bloodasp.galacticgreg.registry.GalacticGregRegistry;

/**
 * In this class, you'll find everything you need in order to tell GGreg what to do and where. Everything is done in
 * here. If you're trying to use anything else, you're probably doing something wrong (Or I forgot to add it. In that
 * case, find me on github and create an issue please)
 */
public class SpaceDimRegisterer {

    public static void register() {
        GalacticGregRegistry.registerModContainer(setupVanilla());
        GalacticGregRegistry.registerModContainer(setupGalactiCraftCore());
        GalacticGregRegistry.registerModContainer(setupGalactiCraftPlanets());
        GalacticGregRegistry.registerModContainer(setupGalaxySpace());
        GalacticGregRegistry.registerModContainer(setupAmunRa());
    }

    /**
     * Vanilla MC (End Asteroids)
     */
    private static ModContainer setupVanilla() {

        // If you happen to have an asteroid dim, just skip the blocklist, and setDimensionType() to
        // DimensionType.Asteroid
        // also don't forget to add at least one asteroid type, or nothing will generate!
        EndAsteroids.modDimensionDef.addAsteroidMaterial(new AsteroidBlockComb(GTOreTypes.Netherrack));
        EndAsteroids.modDimensionDef.addAsteroidMaterial(new AsteroidBlockComb(GTOreTypes.RedGranite));
        EndAsteroids.modDimensionDef.addAsteroidMaterial(new AsteroidBlockComb(GTOreTypes.BlackGranite));
        EndAsteroids.modDimensionDef.addAsteroidMaterial(new AsteroidBlockComb(GTOreTypes.EndStone));

        // These Blocks will randomly be generated
        EndAsteroids.modDimensionDef.addSpecialAsteroidBlock(new SpecialBlockComb(Blocks.glowstone));
        EndAsteroids.modDimensionDef
            .addSpecialAsteroidBlock(new SpecialBlockComb(Blocks.lava, Enums.AllowedBlockPosition.AsteroidCore));

        Vanilla.modContainer.addDimensionDef(EndAsteroids.modDimensionDef);

        return Vanilla.modContainer;
    }

    /**
     * Mod GalactiCraft
     */
    private static ModContainer setupGalactiCraftCore() {
        GalactiCraftCore.modContainer.addDimensionDef(Moon.modDimensionDef);
        return GalactiCraftCore.modContainer;
    }

    /**
     * As GalactiCraftPlanets is an optional mod, don't hardlink it here
     */
    private static ModContainer setupGalactiCraftPlanets() {
        // Overwrite ore blocks on mars with red granite ones. This will default to regular stone if not set
        Mars.modDimensionDef.setStoneType(GTOreTypes.RedGranite);
        GalacticraftMars.modContainer.addDimensionDef(Mars.modDimensionDef);

        Asteroids.modDimensionDef.addAsteroidMaterial(new AsteroidBlockComb(GTOreTypes.BlackGranite));
        Asteroids.modDimensionDef.addAsteroidMaterial(new AsteroidBlockComb(GTOreTypes.RedGranite));
        Asteroids.modDimensionDef.addAsteroidMaterial(new AsteroidBlockComb(GTOreTypes.Netherrack));
        GalacticraftMars.modContainer.addDimensionDef(Asteroids.modDimensionDef);

        return GalacticraftMars.modContainer;
    }

    /**
     * Mod GalaxySpace by BlesseNtumble
     */
    private static ModContainer setupGalaxySpace() {
        // First, we create a mod-container that will be populated with dimensions later.
        // The Name must match your ID, as it is checked if this mod is loaded, in order
        // to enable/disable the parsing/registering of dimensions
        // See enum ModContainers

        // Now lets first define a block here for our dimension. You can add the ID, but you don't have to.
        // It will automatically add the mods name that is defined in the modcontainer.
        // See enum DimensionBlockMetaDefinitionList

        // Now define the available dimensions, and their chunkprovider.
        // Same as above, to not have any dependency in your code, you can just give it a string.
        // But it's better to use the actual ChunkProvider class. The Name is used for the GalacticGreg config file.
        // The resulting config setting will be: <ModID>_<Name you give here as arg0>_false = false
        // make sure to never change this name once you've generated your config files, as it will overwrite everything!

        // 30.11.2016 GSpace v1.1.3 Stable
        GalaxySpace.modContainer.addDimensionDef(Pluto.modDimensionDef);
        GalaxySpace.modContainer.addDimensionDef(Triton.modDimensionDef);
        GalaxySpace.modContainer.addDimensionDef(Proteus.modDimensionDef);
        GalaxySpace.modContainer.addDimensionDef(Oberon.modDimensionDef);
        GalaxySpace.modContainer.addDimensionDef(Titan.modDimensionDef);
        GalaxySpace.modContainer.addDimensionDef(Callisto.modDimensionDef);
        GalaxySpace.modContainer.addDimensionDef(Ganymede.modDimensionDef);
        GalaxySpace.modContainer.addDimensionDef(Ceres.modDimensionDef);
        GalaxySpace.modContainer.addDimensionDef(Deimos.modDimensionDef);
        GalaxySpace.modContainer.addDimensionDef(Enceladus.modDimensionDef);
        GalaxySpace.modContainer.addDimensionDef(Io.modDimensionDef);
        GalaxySpace.modContainer.addDimensionDef(Europa.modDimensionDef);
        GalaxySpace.modContainer.addDimensionDef(Phobos.modDimensionDef);
        GalaxySpace.modContainer.addDimensionDef(Venus.modDimensionDef);
        GalaxySpace.modContainer.addDimensionDef(Mercury.modDimensionDef);
        GalaxySpace.modContainer.addDimensionDef(MakeMake.modDimensionDef);
        GalaxySpace.modContainer.addDimensionDef(Haumea.modDimensionDef);
        GalaxySpace.modContainer.addDimensionDef(CentauriAlpha.modDimensionDef);
        GalaxySpace.modContainer.addDimensionDef(VegaB.modDimensionDef);
        GalaxySpace.modContainer.addDimensionDef(BarnardC.modDimensionDef);
        GalaxySpace.modContainer.addDimensionDef(BarnardE.modDimensionDef);
        GalaxySpace.modContainer.addDimensionDef(BarnardF.modDimensionDef);
        GalaxySpace.modContainer.addDimensionDef(TcetiE.modDimensionDef);
        GalaxySpace.modContainer.addDimensionDef(Miranda.modDimensionDef);
        KuiperBelt.modDimensionDef.setDimensionType(Enums.DimensionType.Asteroid);
        KuiperBelt.modDimensionDef.addAsteroidMaterial(new AsteroidBlockComb(GTOreTypes.RedGranite));
        KuiperBelt.modDimensionDef.addAsteroidMaterial(new AsteroidBlockComb(GTOreTypes.BlackGranite));
        GalaxySpace.modContainer.addDimensionDef(KuiperBelt.modDimensionDef);
        return GalaxySpace.modContainer;
    }

    /**
     * Mod Amun-Ra
     */
    private static ModContainer setupAmunRa() {
        AmunRa.modContainer.addDimensionDef(Neper.modDimensionDef);
        AmunRa.modContainer.addDimensionDef(Maahes.modDimensionDef);
        AmunRa.modContainer.addDimensionDef(Anubis.modDimensionDef);
        AmunRa.modContainer.addDimensionDef(Horus.modDimensionDef);
        AmunRa.modContainer.addDimensionDef(Seth.modDimensionDef);
        MehenBelt.modDimensionDef.addAsteroidMaterial(GTOreTypes.BlackGranite);
        AmunRa.modContainer.addDimensionDef(MehenBelt.modDimensionDef);
        return AmunRa.modContainer;
    }
}
