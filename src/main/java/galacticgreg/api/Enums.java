package galacticgreg.api;

public class Enums {

    public enum SpaceObjectType {
        OreAsteroid,
        NonOreSchematic
    }

    public enum TargetBlockPosition {
        Invalid,
        AsteroidInnerCore,
        AsteroidCore,
        AsteroidShell,
        StructureBlock
    }

    public enum AllowedBlockPosition {
        AsteroidInnerCore,
        AsteroidCore,
        AsteroidShell,
        AsteroidCoreAndShell
    }

    public enum AirReplaceRule {
        NeverReplaceAir,
        AllowReplaceAir,
        OnlyReplaceAir
    }

    public enum ReplaceState {
        Unknown,
        Airblock,
        CanReplace,
        CannotReplace
    }

    public enum DimensionType {
        /**
         * The Dimension is a void dimension and asteroids shall be generated. They will randomly spawn bewteen 0 and
         * 250 Additional config values will be generated in worldconfig
         */
        Asteroid,

        /**
         * The Dimension is a planet, and only ores shall be generated in the ground
         */
        Planet,
    }

}
