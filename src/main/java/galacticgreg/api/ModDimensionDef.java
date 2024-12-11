package galacticgreg.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import galacticgreg.api.Enums.DimensionType;
import gregtech.api.enums.StoneType;
import gregtech.api.interfaces.IStoneType;
import net.minecraft.block.Block;
import net.minecraft.world.chunk.IChunkProvider;

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
    private Enums.AirReplaceRule airReplaceSetting;
    private final ArrayList<ModDBMDef> replaceableBlocks;
    private DimensionType dimensionType;

    private final List<ISpaceObjectGenerator> spaceObjectGenerators;
    private final List<ISpaceObjectGenerator> spaceStructureGenerators;

    // Special Planets config settings
    private int groundOreMaxY = 64;
    private int floatingAsteroidsMinY = 128;
    // ------

    // Asteroid stuff
    private final List<AsteroidBlockComb> validAsteroidMaterials;
    private final List<SpecialBlockComb> specialBlocksForAsteroids;

    private final Random random = new Random(System.currentTimeMillis());

    /**
     * Define a new dimension
     *
     * @param pDimensionName The human-readable. Spaces will be removed
     * @param pChunkProvider The chunkprovider class that shall be observed for the oregen
     */
    public ModDimensionDef(String pDimensionName, Class<? extends IChunkProvider> pChunkProvider,
        DimensionType pDimType) {
        this(
            pDimensionName,
            pChunkProvider.getName(),
            pDimType,
            null);
    }

    /**
     * Define a new dimension
     *
     * @param pDimensionName    The human-readable. Spaces will be removed
     * @param pChunkProvider    The chunkprovider class that shall be observed for the oregen
     * @param pBlockDefinitions The list of predefined blocks to be replaced by ores
     */
    public ModDimensionDef(String pDimensionName, Class<? extends IChunkProvider> pChunkProvider,
        DimensionType pDimType, List<ModDBMDef> pBlockDefinitions) {
        this(
            pDimensionName,
            pChunkProvider.getName(),
            pDimType,
            pBlockDefinitions);
    }

    /**
     * Define a new dimension
     *
     * @param pDimensionName     The human-readable DimensionName. Spaces will be removed
     * @param pChunkProviderName The human-readable, full-qualified classname for the chunkprovider
     */
    public ModDimensionDef(String pDimensionName, String pChunkProviderName, DimensionType pDimType) {
        this(pDimensionName, pChunkProviderName, pDimType, null);
    }

    /**
     * Define a new dimension
     *
     * @param pDimensionName     The human-readable DimensionName. Spaces will be removed
     * @param pChunkProviderName The human-readable, full-qualified classname for the chunkprovider
     * @param pBlockDefinitions  The list of predefined blocks to be replaced by ores
     */
    public ModDimensionDef(String pDimensionName, String pChunkProviderName, DimensionType pDimType,
        List<ModDBMDef> pBlockDefinitions) {
        internalDimIdentifier = STR_NOTDEFINED;
        dimensionName = pDimensionName;
        chunkProviderName = pChunkProviderName;
        dimensionType = pDimType;

        replaceableBlocks = new ArrayList<>();
        if (pBlockDefinitions != null) replaceableBlocks.addAll(pBlockDefinitions);

        validAsteroidMaterials = new ArrayList<>();
        specialBlocksForAsteroids = new ArrayList<>();
        spaceObjectGenerators = new ArrayList<>();
        spaceStructureGenerators = new ArrayList<>();
    }

    /**
     * Internal function
     *
     * @return A list of possible asteroid-mixes that shall be generated
     */
    public List<AsteroidBlockComb> getValidAsteroidMaterials() {
        return validAsteroidMaterials;
    }

    // =================================================
    /**
     * Internal function The only purpose of this functions is to get a default config value for this dim, that can be
     * altered by the mod author which adds the dimension definition to his mod, but also provide the
     * modpack-author/serveradmin to change these values aswell
     */
    public int getPreConfiguredGroundOreMaxY() {
        return groundOreMaxY;
    }

    /**
     * Internal function The only purpose of this functions is to get a default config value for this dim, that can be
     * altered by the mod author which adds the dimension definition to his mod, but also provide the
     * modpack-author/serveradmin to change these values aswell
     */
    public int getPreConfiguredFloatingAsteroidMinY() {
        return floatingAsteroidsMinY;
    }

    /**
     * Register new generator for objects in space. You can register as many as you want. If you don't register
     * anything, no structures will generate and the default Asteroid-Generator will be used
     *
     * @param pSpaceObjectGenerator An instance of your own object generator
     */
    public void registerSpaceObjectGenerator(ISpaceObjectGenerator pSpaceObjectGenerator) {
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
    public ISpaceObjectGenerator getRandomSOGenerator(Enums.SpaceObjectType pTargetType) {
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

    /**
     * Define the default values for the floating asteroids and the oregen here. As both generators run in the same
     * dimension, and you probably don't want to have asteroids stuck in the ground, both generators are separated from
     * each other. Basically, you can go with the default values. If you want to change them, make sure that pOregenMaxY
     * is lower than pAsteroidMinY
     *
     * @param pOregenMaxY   The maximum Y-height where ores will be allowed to spawn. Default: 64
     * @param pAsteroidMinY The minimum Y-height that has to be reached before asteroids will spawn. Default: 128
     * @throws IllegalArgumentException if the limits are invalid
     *
     */
    public void setAsteroidAndPlanetLimits(int pOregenMaxY, int pAsteroidMinY) {
        if (pOregenMaxY >= pAsteroidMinY)
            throw new IllegalArgumentException("pOregenMaxY must be LOWER than pAsteroidMinY!");

        floatingAsteroidsMinY = pAsteroidMinY;
        groundOreMaxY = pOregenMaxY;
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
     * @return The configuration for AirBlocks
     */
    public Enums.AirReplaceRule getAirSetting() {
        return airReplaceSetting;
    }

    /**
     * Define how the oregen shall handle air-blocks. These settings should be pretty self-explandatory, but anyways:
     * NeverReplaceAir: No matter what, if there is an Air-Block found, it will not replace it. AllowReplaceAir: This
     * will generate Ores in Stones (defined by addBlockDefinition()) and air if found OnlyReplaceAir : This will not
     * generate Ores in solid blocks, but only in air
     * <p>
     * Note that "OnlyReplaceAir" is a special setting if you have a dimension that is not defined as "Asteroids" but
     * you still need/want to generate ores in midair.
     *
     * @param pSetting
     */
    public void setAirSetting(Enums.AirReplaceRule pSetting) {
        airReplaceSetting = pSetting;
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
     * Adds a new blockdefinition to this dimension. This block will then later be replaced by ores. You can add as many
     * blocks as you want. Just don't add Blocks.Air, as there is another setting for allowing Air-Replacement
     *
     * @param pBlockDef
     * @return
     */
    public boolean addBlockDefinition(ModDBMDef pBlockDef) {
        if (replaceableBlocks.contains(pBlockDef)) {
            return false;
        } else {
            replaceableBlocks.add(pBlockDef);
            return true;
        }
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
     *
     * @return A list of all defined Blocks that can be replaced while generating ores
     */
    public ArrayList<ModDBMDef> getReplaceableBlocks() {
        return replaceableBlocks;
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
     * Check if pBlock can be replaced by an ore
     *
     * @param pBlock
     * @param pMeta
     * @return
     */
    public Enums.ReplaceState getReplaceStateForBlock(Block pBlock, int pMeta) {
        Enums.ReplaceState tFlag = Enums.ReplaceState.Unknown;

        for (ModDBMDef pDef : replaceableBlocks) {
            Enums.ReplaceState tResult = pDef.blockEquals(pBlock, pMeta);
            if (tResult == Enums.ReplaceState.Unknown) continue;

            if (tResult == Enums.ReplaceState.CanReplace) {
                // GalacticGreg.Logger.trace("Targetblock found and metadata match. Replacement allowed");
                tFlag = Enums.ReplaceState.CanReplace;
            } else if (tResult == Enums.ReplaceState.CannotReplace) {
                // GalacticGreg.Logger.trace("Targetblock found but metadata mismatch. Replacement denied");
                tFlag = Enums.ReplaceState.CannotReplace;
            }
            break;
        }

        return tFlag;
    }

    /**
     * Internal function
     * <p>
     * Randomly select one material out of all defined materials
     *
     * @return
     */
    public AsteroidBlockComb getRandomAsteroidMaterial(Random rng) {
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
    public SpecialBlockComb getRandomSpecialAsteroidBlock(Random rng) {
        if (specialBlocksForAsteroids.isEmpty()) return null;

        if (specialBlocksForAsteroids.size() == 1) {
            return specialBlocksForAsteroids.get(0);
        } else {
            return specialBlocksForAsteroids.get(rng.nextInt(specialBlocksForAsteroids.size()));
        }
    }

    /**
     * Define the material the asteroid shall be made of. Limited to GT-Based Ores and their stones
     *
     * @param pMaterial
     */
    public void addAsteroidMaterial(StoneType stoneType) {
        addAsteroidMaterial(new AsteroidBlockComb(stoneType));
    }

    /**
     * Define the material the asteroid shall be made of, more advanced option to specify your own blocks
     *
     * @param pBlockComb
     */
    public void addAsteroidMaterial(AsteroidBlockComb pBlockComb) {
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

    /**
     * Internal function Called when GalacticGreg will finalize all its internal structures. You should never call this
     * yourself
     */
    public void finalizeReplaceableBlocks(String pParentModName) {
        for (ModDBMDef rpb : replaceableBlocks) {
            rpb.updateBlockName(pParentModName);
        }
    }
}
