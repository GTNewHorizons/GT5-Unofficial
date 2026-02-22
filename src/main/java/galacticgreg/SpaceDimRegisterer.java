package galacticgreg;

import net.minecraft.init.Blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import galacticgreg.api.Enums;
import galacticgreg.api.ModContainer;
import galacticgreg.api.SpecialBlockComb;
import galacticgreg.api.enums.DimensionDef;
import galacticgreg.api.enums.ModContainers;
import galacticgreg.registry.GalacticGregRegistry;
import gregtech.api.enums.Mods;
import gregtech.api.enums.StoneType;

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
        DimensionDef.EndAsteroids.modDimensionDef.addAsteroidMaterial(StoneType.Endstone);
        DimensionDef.EndAsteroids.modDimensionDef.addAsteroidMaterial(StoneType.Marble);

        // These Blocks will randomly be generated
        DimensionDef.EndAsteroids.modDimensionDef.addSpecialAsteroidBlock(new SpecialBlockComb(Blocks.glowstone));
        if (Mods.HardcoreEnderExpansion.isModLoaded()) {
            DimensionDef.EndAsteroids.modDimensionDef.addSpecialAsteroidBlock(
                new SpecialBlockComb(
                    GameRegistry.findBlock(Mods.HardcoreEnderExpansion.ID, "ender_goo"),
                    Enums.AllowedBlockPosition.AsteroidCore));
        }

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
        ModContainers.GalacticraftMars.modContainer.addDimensionDef(DimensionDef.Mars.modDimensionDef);

        DimensionDef.Asteroids.modDimensionDef.addAsteroidMaterial(StoneType.BlackGranite);
        DimensionDef.Asteroids.modDimensionDef.addAsteroidMaterial(StoneType.Moon);
        DimensionDef.Asteroids.modDimensionDef.addAsteroidMaterial(StoneType.Asteroid);
        DimensionDef.Asteroids.modDimensionDef.addAsteroidMaterial(StoneType.PackedIce);

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
        ModContainers.GalaxySpace.modContainer.addDimensionDef(DimensionDef.CentauriBb.modDimensionDef);
        ModContainers.GalaxySpace.modContainer.addDimensionDef(DimensionDef.VegaB.modDimensionDef);
        ModContainers.GalaxySpace.modContainer.addDimensionDef(DimensionDef.BarnardC.modDimensionDef);
        ModContainers.GalaxySpace.modContainer.addDimensionDef(DimensionDef.BarnardE.modDimensionDef);
        ModContainers.GalaxySpace.modContainer.addDimensionDef(DimensionDef.BarnardF.modDimensionDef);
        ModContainers.GalaxySpace.modContainer.addDimensionDef(DimensionDef.TcetiE.modDimensionDef);
        ModContainers.GalaxySpace.modContainer.addDimensionDef(DimensionDef.Miranda.modDimensionDef);

        DimensionDef.KuiperBelt.modDimensionDef.setDimensionType(Enums.DimensionType.Asteroid);
        DimensionDef.KuiperBelt.modDimensionDef.addAsteroidMaterial(StoneType.RedGranite);
        DimensionDef.KuiperBelt.modDimensionDef.addAsteroidMaterial(StoneType.BlackGranite);
        DimensionDef.KuiperBelt.modDimensionDef.addAsteroidMaterial(StoneType.BlueIce);
        DimensionDef.KuiperBelt.modDimensionDef.addAsteroidMaterial(StoneType.PackedIce);

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

        DimensionDef.MehenBelt.modDimensionDef.addAsteroidMaterial(StoneType.BlackGranite);
        DimensionDef.MehenBelt.modDimensionDef.addAsteroidMaterial(StoneType.Horus);
        DimensionDef.MehenBelt.modDimensionDef.addAsteroidMaterial(StoneType.Asteroid);
        DimensionDef.MehenBelt.modDimensionDef.addAsteroidMaterial(StoneType.AnubisAndMaahes);
        ModContainers.AmunRa.modContainer.addDimensionDef(DimensionDef.MehenBelt.modDimensionDef);

        return ModContainers.AmunRa.modContainer;
    }
}
