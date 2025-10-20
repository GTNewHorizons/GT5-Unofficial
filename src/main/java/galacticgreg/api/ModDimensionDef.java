package galacticgreg.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import galacticgreg.api.Enums.DimensionType;
import gregtech.api.interfaces.IStoneType;
import gregtech.api.objects.XSTR;
import gregtech.common.config.Gregtech;

// import galacticgreg.GalacticGreg;

/**
 * Class to define a Dimension. Supposed to be added to a ModContainer
 */
public class ModDimensionDef {

    private static final String STR_NOTDEFINED = "iiznotdefined";
    private final String dimensionName;
    /** "modname_dimname" */
    private String internalDimIdentifier;
    private final String chunkProviderName;
    private DimensionType dimensionType;

    private final @NotNull List<ISpaceObjectGenerator> spaceObjectGenerators;
    private final @NotNull List<ISpaceObjectGenerator> spaceStructureGenerators;

    private int oreVeinChance = Gregtech.general.oreveinPercentage;

    // ------

    // Asteroid stuff
    private final @NotNull List<IStoneType> validAsteroidMaterials;
    private final @NotNull List<SpecialBlockComb> specialBlocksForAsteroids;

    private final XSTR random = new XSTR();

    private boolean hasEoHRecipe = true;
    private boolean canBeVoidMined = true;
    private boolean generatesOre;
    private boolean generatesAsteroids;
    private boolean respectsOreVeinHeights = true;

    /**
     * @param pDimensionName The provider dimension name (see {@link World#provider} and
     *                       {@link galacticgreg.api.enums.DimensionDef#getDimensionName(World)})
     * @param pChunkProvider The chunk provider class
     * @param pDimType       The dimension type (whether it generates asteroids or ore veins)
     */
    public ModDimensionDef(String pDimensionName, Class<? extends IChunkProvider> pChunkProvider,
        DimensionType pDimType) {
        this(pDimensionName, pChunkProvider.getName(), pDimType);
    }

    /**
     * @param pDimensionName     The provider dimension name (see {@link World#provider} and
     *                           {@link galacticgreg.api.enums.DimensionDef#getDimensionName(World)})
     * @param pChunkProviderName The chunk provider class name
     * @param pDimType           The dimension type (whether it generates asteroids or ore veins)
     */
    public ModDimensionDef(String pDimensionName, String pChunkProviderName, DimensionType pDimType) {
        internalDimIdentifier = STR_NOTDEFINED;
        dimensionName = pDimensionName;
        chunkProviderName = pChunkProviderName;
        dimensionType = pDimType;

        validAsteroidMaterials = new ArrayList<>();
        specialBlocksForAsteroids = new ArrayList<>();
        spaceObjectGenerators = new ArrayList<>();
        spaceStructureGenerators = new ArrayList<>();

        generatesOre = pDimType == DimensionType.Planet;
        generatesAsteroids = pDimType == DimensionType.Asteroid;
    }

    /**
     * Sets the chance that an ore seed chunk will have a vein
     *
     * @param chance The chance out of 100.
     */
    public ModDimensionDef setOreVeinChance(int chance) {
        oreVeinChance = chance;
        return this;
    }

    public int getOreVeinChance() {
        return oreVeinChance;
    }

    // =================================================
    /**
     * Register new generator for objects in space. You can register as many as you want. If you don't register
     * anything, no structures will generate and the default Asteroid-Generator will be used
     *
     * @param pSpaceObjectGenerator An instance of your own object generator
     */
    public void registerSpaceObjectGenerator(@NotNull ISpaceObjectGenerator pSpaceObjectGenerator) {
        Enums.SpaceObjectType tType = pSpaceObjectGenerator.getType();
        switch (tType) {
            case NonOreSchematic:
                spaceStructureGenerators.add(pSpaceObjectGenerator);
                break;
            case OreAsteroid:
                spaceObjectGenerators.add(pSpaceObjectGenerator);
                break;
            default:
                // GalacticGreg.Logger.error("registerSpaceObjectGenerator() found unhandled generator type %s. Please
                // report asap, the author was lazy!", tType.toString());
                break;

        }
    }

    /**
     * Internal function Return a random generator for space objects
     */
    public @Nullable ISpaceObjectGenerator getRandomSOGenerator(Enums.@NotNull SpaceObjectType pTargetType) {
        ISpaceObjectGenerator tGen = null;
        List<ISpaceObjectGenerator> tLst = null;
        try {
            switch (pTargetType) {
                case NonOreSchematic:
                    tLst = spaceStructureGenerators;
                    break;
                case OreAsteroid:
                    tLst = spaceObjectGenerators;
                    break;
                default:
                    break;
            }

            if (tLst != null) {
                if (tLst.size() == 1) tGen = tLst.get(0);
                else if (tLst.size() > 1) tGen = tLst.get(random.nextInt(tLst.size()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tGen;
    }

    // =================================================

    /**
     * Internal function
     *
     * @return A list of all special blocks that shall be used to generate the asteroids.
     */
    public List<SpecialBlockComb> getSpecialBlocksForAsteroids() {
        return specialBlocksForAsteroids;
    }

    public List<ISpaceObjectGenerator> getSpaceObjectGenerators() {
        return spaceObjectGenerators;
    }

    /**
     * Internal function
     *
     * @return The type for this dimension
     */
    public DimensionType getDimensionType() {
        return dimensionType;
    }

    public boolean generatesOre() {
        return generatesOre;
    }

    public boolean generatesAsteroids() {
        return generatesAsteroids;
    }

    public boolean respectsOreVeinHeights() {
        return respectsOreVeinHeights;
    }

    public ModDimensionDef setGeneratesOre() {
        generatesOre = true;
        return this;
    }

    public ModDimensionDef setGeneratesAsteroids() {
        generatesAsteroids = true;
        return this;
    }

    public ModDimensionDef disableOreVeinHeightChecks() {
        respectsOreVeinHeights = false;
        return this;
    }

    /**
     * Set whether this DimensionDefinition defines an void-dimension that shall spawn asteroids instead of ores in
     * stone
     *
     * @param pType The dimensiontype to be used
     */
    public void setDimensionType(DimensionType pType) {
        dimensionType = pType;
    }

    /**
     * Internal function
     *
     * @return The dimension identifier that is used internally to identify the dimension
     */
    public String getDimIdentifier() {
        return internalDimIdentifier;
    }

    /**
     * Internal function
     *
     * @return The attached chunk-provider for this dimension
     */
    public String getChunkProviderName() {
        return chunkProviderName;
    }

    /**
     * Internal function
     *
     * @return The DimensionName in a Human-readable format
     */
    public String getDimensionName() {
        return dimensionName;
    }

    /**
     * Internal function
     * <p>
     * Do not call this function by yourself. Ever. It will cause explosions, water to blood, death of firstborn,...
     * Seriously, don't do it.
     */
    protected void setParentModName(String pModName) {
        if (internalDimIdentifier.equals(STR_NOTDEFINED)) {
            internalDimIdentifier = String.format("%s_%s", pModName, dimensionName);
        }

        // Else Don't update, we're already set

    }

    /**
     * Internal function
     * <p>
     * Randomly select one material out of all defined materials
     *
     * @return
     */
    public @Nullable IStoneType getRandomAsteroidMaterial(Random rng) {
        if (validAsteroidMaterials.isEmpty()) return null;

        if (validAsteroidMaterials.size() == 1) {
            return validAsteroidMaterials.get(0);
        } else {
            return validAsteroidMaterials.get(rng.nextInt(validAsteroidMaterials.size()));
        }
    }

    /**
     * Internal function
     * <p>
     * Randomly select one special block to be placed in the asteroids
     *
     * @return
     */
    public @Nullable SpecialBlockComb getRandomSpecialAsteroidBlock(Random rng) {
        if (specialBlocksForAsteroids.isEmpty()) return null;

        if (specialBlocksForAsteroids.size() == 1) {
            return specialBlocksForAsteroids.get(0);
        } else {
            return specialBlocksForAsteroids.get(rng.nextInt(specialBlocksForAsteroids.size()));
        }
    }

    /**
     * Define the material the asteroid shall be made of, more advanced option to specify your own blocks
     *
     * @param pBlockComb
     */
    public void addAsteroidMaterial(IStoneType pBlockComb) {
        if (!validAsteroidMaterials.contains(pBlockComb)) {
            validAsteroidMaterials.add(pBlockComb);
        }
    }

    /**
     * Adds a new material for asteroid generation. These will spawn randomly in asteroids if enabled. You can basically
     * add every block you can imagine. Be warned though, if you use Liquids (Water / Lava / ..), it can affect
     * performance if the liquid is flowing down to the void. So make sure you define "AsteroidCore" as position
     *
     * @param pBlock Block-Meta Combination that shall be used
     */
    public void addSpecialAsteroidBlock(SpecialBlockComb pBlock) {
        if (!specialBlocksForAsteroids.contains(pBlock)) {
            specialBlocksForAsteroids.add(pBlock);
        }
    }

    public ModDimensionDef disableEoHRecipe() {
        hasEoHRecipe = false;

        return this;
    }

    public boolean hasEoHRecipe() {
        return hasEoHRecipe;
    }

    public ModDimensionDef disableVoidMining() {
        canBeVoidMined = false;

        return this;
    }

    public boolean canBeVoidMined() {
        return canBeVoidMined;
    }
}
