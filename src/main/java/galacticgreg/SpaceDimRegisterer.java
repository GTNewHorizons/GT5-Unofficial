package galacticgreg;

import net.minecraft.init.Blocks;

import galacticgreg.api.AsteroidBlockComb;
import galacticgreg.api.Enums;
import galacticgreg.api.GTOreTypes;
import galacticgreg.api.ModContainer;
import galacticgreg.api.SpecialBlockComb;
import galacticgreg.api.enums.DimensionDef;
import galacticgreg.api.enums.ModContainers;
import galacticgreg.registry.GalacticGregRegistry;

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
        DimensionDef.EndAsteroids.modDimensionDef.addAsteroidMaterial(new AsteroidBlockComb(GTOreTypes.Netherrack));
        DimensionDef.EndAsteroids.modDimensionDef.addAsteroidMaterial(new AsteroidBlockComb(GTOreTypes.RedGranite));
        DimensionDef.EndAsteroids.modDimensionDef.addAsteroidMaterial(new AsteroidBlockComb(GTOreTypes.BlackGranite));
        DimensionDef.EndAsteroids.modDimensionDef.addAsteroidMaterial(new AsteroidBlockComb(GTOreTypes.EndStone));

        // These Blocks will randomly be generated
        DimensionDef.EndAsteroids.modDimensionDef.addSpecialAsteroidBlock(new SpecialBlockComb(Blocks.glowstone));
        DimensionDef.EndAsteroids.modDimensionDef
            .addSpecialAsteroidBlock(new SpecialBlockComb(Blocks.lava, Enums.AllowedBlockPosition.AsteroidCore));

        ModContainers.Vanilla.modContainer.addDimensionDef(DimensionDef.EndAsteroids.modDimensionDef);

        return ModContainers.Vanilla.modContainer;
    }

    /**
     * Mod GalactiCraft
     */
    private static ModContainer setupGalactiCraftCore() {
        ModContainers.GalactiCraftCore.modContainer.addDimensionDef(DimensionDef.Moon.modDimensionDef);
        return ModContainers.GalactiCraftCore.modContainer;
    }

    /**
     * As GalactiCraftPlanets is an optional mod, don't hardlink it here
     */
    private static ModContainer setupGalactiCraftPlanets() {
        // Overwrite ore blocks on mars with red granite ones. This will default to regular stone if not set
        DimensionDef.Mars.modDimensionDef.setStoneType(GTOreTypes.RedGranite);
        ModContainers.GalacticraftMars.modContainer.addDimensionDef(DimensionDef.Mars.modDimensionDef);

        DimensionDef.Asteroids.modDimensionDef.addAsteroidMaterial(new AsteroidBlockComb(GTOreTypes.BlackGranite));
        DimensionDef.Asteroids.modDimensionDef.addAsteroidMaterial(new AsteroidBlockComb(GTOreTypes.RedGranite));
        DimensionDef.Asteroids.modDimensionDef.addAsteroidMaterial(new AsteroidBlockComb(GTOreTypes.Netherrack));
        ModContainers.GalacticraftMars.modContainer.addDimensionDef(DimensionDef.Asteroids.modDimensionDef);

        return ModContainers.GalacticraftMars.modContainer;
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
        ModContainers.GalaxySpace.modContainer.addDimensionDef(DimensionDef.Pluto.modDimensionDef);
        ModContainers.GalaxySpace.modContainer.addDimensionDef(DimensionDef.Triton.modDimensionDef);
        ModContainers.GalaxySpace.modContainer.addDimensionDef(DimensionDef.Proteus.modDimensionDef);
        ModContainers.GalaxySpace.modContainer.addDimensionDef(DimensionDef.Oberon.modDimensionDef);
        ModContainers.GalaxySpace.modContainer.addDimensionDef(DimensionDef.Titan.modDimensionDef);
        ModContainers.GalaxySpace.modContainer.addDimensionDef(DimensionDef.Callisto.modDimensionDef);
        ModContainers.GalaxySpace.modContainer.addDimensionDef(DimensionDef.Ganymede.modDimensionDef);
        ModContainers.GalaxySpace.modContainer.addDimensionDef(DimensionDef.Ceres.modDimensionDef);
        ModContainers.GalaxySpace.modContainer.addDimensionDef(DimensionDef.Deimos.modDimensionDef);
        ModContainers.GalaxySpace.modContainer.addDimensionDef(DimensionDef.Enceladus.modDimensionDef);
        ModContainers.GalaxySpace.modContainer.addDimensionDef(DimensionDef.Io.modDimensionDef);
        ModContainers.GalaxySpace.modContainer.addDimensionDef(DimensionDef.Europa.modDimensionDef);
        ModContainers.GalaxySpace.modContainer.addDimensionDef(DimensionDef.Phobos.modDimensionDef);
        ModContainers.GalaxySpace.modContainer.addDimensionDef(DimensionDef.Venus.modDimensionDef);
        ModContainers.GalaxySpace.modContainer.addDimensionDef(DimensionDef.Mercury.modDimensionDef);
        ModContainers.GalaxySpace.modContainer.addDimensionDef(DimensionDef.MakeMake.modDimensionDef);
        ModContainers.GalaxySpace.modContainer.addDimensionDef(DimensionDef.Haumea.modDimensionDef);
        ModContainers.GalaxySpace.modContainer.addDimensionDef(DimensionDef.CentauriAlpha.modDimensionDef);
        ModContainers.GalaxySpace.modContainer.addDimensionDef(DimensionDef.VegaB.modDimensionDef);
        ModContainers.GalaxySpace.modContainer.addDimensionDef(DimensionDef.BarnardC.modDimensionDef);
        ModContainers.GalaxySpace.modContainer.addDimensionDef(DimensionDef.BarnardE.modDimensionDef);
        ModContainers.GalaxySpace.modContainer.addDimensionDef(DimensionDef.BarnardF.modDimensionDef);
        ModContainers.GalaxySpace.modContainer.addDimensionDef(DimensionDef.TcetiE.modDimensionDef);
        ModContainers.GalaxySpace.modContainer.addDimensionDef(DimensionDef.Miranda.modDimensionDef);
        DimensionDef.KuiperBelt.modDimensionDef.setDimensionType(Enums.DimensionType.Asteroid);
        DimensionDef.KuiperBelt.modDimensionDef.addAsteroidMaterial(new AsteroidBlockComb(GTOreTypes.RedGranite));
        DimensionDef.KuiperBelt.modDimensionDef.addAsteroidMaterial(new AsteroidBlockComb(GTOreTypes.BlackGranite));
        ModContainers.GalaxySpace.modContainer.addDimensionDef(DimensionDef.KuiperBelt.modDimensionDef);
        return ModContainers.GalaxySpace.modContainer;
    }

    /**
     * Mod Amun-Ra
     */
    private static ModContainer setupAmunRa() {
        ModContainers.AmunRa.modContainer.addDimensionDef(DimensionDef.Neper.modDimensionDef);
        ModContainers.AmunRa.modContainer.addDimensionDef(DimensionDef.Maahes.modDimensionDef);
        ModContainers.AmunRa.modContainer.addDimensionDef(DimensionDef.Anubis.modDimensionDef);
        ModContainers.AmunRa.modContainer.addDimensionDef(DimensionDef.Horus.modDimensionDef);
        ModContainers.AmunRa.modContainer.addDimensionDef(DimensionDef.Seth.modDimensionDef);
        DimensionDef.MehenBelt.modDimensionDef.addAsteroidMaterial(GTOreTypes.BlackGranite);
        ModContainers.AmunRa.modContainer.addDimensionDef(DimensionDef.MehenBelt.modDimensionDef);
        return ModContainers.AmunRa.modContainer;
    }
}
