package bloodasp.galacticgreg.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.chunk.IChunkProvider;

import bloodasp.galacticgreg.api.Enums.AirReplaceRule;
import bloodasp.galacticgreg.api.Enums.DimensionType;
import bloodasp.galacticgreg.api.Enums.ReplaceState;
import bloodasp.galacticgreg.api.Enums.SpaceObjectType;

// import bloodasp.galacticgreg.GalacticGreg;

/**
 * Class to define a Dimension. Supposed to be added to a ModContainer
 */
public class ModDimensionDef {

    private static final String STR_NOTDEFINED = "iiznotdefined";
    private String _mDimensionName;
    private String _mInternalDimIdentifier;
    private String _mChunkProvider;
    private AirReplaceRule _mDimAirSetting;
    private ArrayList<ModDBMDef> _mReplaceableBlocks;
    private DimensionType _mDimensionType;

    private List<ISpaceObjectGenerator> _mSpaceObjectsGenerators;
    private List<ISpaceObjectGenerator> _mSpaceStructureGenerators;

    // Special Planets config settings
    private int _mGroundOreMaxY = 64;
    private int _mFloatingAsteroidsMinY = 128;
    // ------

    // Override for stonetype
    private GTOreTypes _mStoneType;

    // Asteroid stuff
    private List<AsteroidBlockComb> _mValidAsteroidMaterials;
    private List<SpecialBlockComb> _mSpecialBlocksForAsteroids;

    private Random _mRandom = new Random(System.currentTimeMillis());

    /**
     * Internal function
     *
     * @return A list of possible asteroid-mixes that shall be generated
     */
    public List<AsteroidBlockComb> getValidAsteroidMaterials() {
        return _mValidAsteroidMaterials;
    }

    // =================================================
    /**
     * Internal function The only purpose of this functions is to get a default config value for this dim, that can be
     * altered by the mod author which adds the dimension definition to his mod, but also provide the
     * modpack-author/serveradmin to change these values aswell
     */
    public int getPreConfiguratedGroundOreMaxY() {
        return _mGroundOreMaxY;
    }

    /**
     * Internal function The only purpose of this functions is to get a default config value for this dim, that can be
     * altered by the mod author which adds the dimension definition to his mod, but also provide the
     * modpack-author/serveradmin to change these values aswell
     */
    public int getPreConfiguratedFloatingAsteroidMinY() {
        return _mFloatingAsteroidsMinY;
    }

    /**
     * Register new generator for objects in space. You can register as many as you want. If you don't register
     * anything, no structures will generate and the default Asteroid-Generator will be used
     *
     * @param pSpaceObjectGenerator An instance of your own object generator
     */
    public void registerSpaceObjectGenerator(ISpaceObjectGenerator pSpaceObjectGenerator) {
        SpaceObjectType tType = pSpaceObjectGenerator.getType();
        switch (tType) {
            case NonOreSchematic:
                _mSpaceStructureGenerators.add(pSpaceObjectGenerator);
                break;
            case OreAsteroid:
                _mSpaceObjectsGenerators.add(pSpaceObjectGenerator);
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
    public ISpaceObjectGenerator getRandomSOGenerator(SpaceObjectType pTargetType) {
        ISpaceObjectGenerator tGen = null;
        List<ISpaceObjectGenerator> tLst = null;
        try {
            switch (pTargetType) {
                case NonOreSchematic:
                    tLst = _mSpaceStructureGenerators;
                    break;
                case OreAsteroid:
                    tLst = _mSpaceObjectsGenerators;
                    break;
                default:
                    break;
            }

            if (tLst != null) {
                if (tLst.size() == 1) tGen = tLst.get(0);
                else if (tLst.size() > 1) tGen = tLst.get(_mRandom.nextInt(tLst.size()));
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

        _mFloatingAsteroidsMinY = pAsteroidMinY;
        _mGroundOreMaxY = pOregenMaxY;
    }
    // =================================================

    /**
     * Internal function
     *
     * @return A list of all special blocks that shall be used to generate the asteroids.
     */
    public List<SpecialBlockComb> getSpecialBlocksForAsteroids() {
        return _mSpecialBlocksForAsteroids;
    }

    public List<ISpaceObjectGenerator> getSpaceObjectGenerators() {
        return _mSpaceObjectsGenerators;
    }

    /**
     * Internal function
     *
     * @return The type for this dimension
     */
    public DimensionType getDimensionType() {
        return _mDimensionType;
    }

    /**
     * Set whether this DimensionDefinition defines an void-dimension that shall spawn asteroids instead of ores in
     * stone
     *
     * @param pType The dimensiontype to be used
     */
    public void setDimensionType(DimensionType pType) {
        _mDimensionType = pType;
    }

    /**
     * Internal function
     *
     * @return The configuration for AirBlocks
     */
    public AirReplaceRule getAirSetting() {
        return _mDimAirSetting;
    }

    /**
     * Define how the oregen shall handle air-blocks. These settings should be pretty self-explandatory, but anyways:
     * NeverReplaceAir: No matter what, if there is an Air-Block found, it will not replace it. AllowReplaceAir: This
     * will generate Ores in Stones (defined by addBlockDefinition()) and air if found OnlyReplaceAir : This will not
     * generate Ores in solid blocks, but only in air
     *
     * Note that "OnlyReplaceAir" is a special setting if you have a dimension that is not defined as "Asteroids" but
     * you still need/want to generate ores in midair.
     *
     * @param pSetting
     */
    public void setAirSetting(AirReplaceRule pSetting) {
        _mDimAirSetting = pSetting;
    }

    /**
     * Internal function
     *
     * @return The dimension identifier that is used internally to identify the dimension
     */
    public String getDimIdentifier() {
        return _mInternalDimIdentifier;
    }

    /**
     * Set a manual override for ores that shall be generated. This setting is ignored if getIsAsteroidDimension()
     * returns true
     *
     * For example, on GalactiCraft Mars, this value is set to GTOreTypes.RedGranite, because it matches the color
     * better. If you don't set anything here, it will generate regular stone-ores.
     *
     * @param pStoneType
     */
    public void setStoneType(GTOreTypes pStoneType) {
        _mStoneType = pStoneType;
    }

    /**
     * Internal function
     *
     * @return The stone override for gregtech ores
     */
    public GTOreTypes getStoneType() {
        return _mStoneType;
    }

    /**
     * Internal function
     *
     * @return The attached chunk-provider for this dimension
     */
    public String getChunkProviderName() {
        return _mChunkProvider;
    }

    /**
     * Adds a new blockdefinition to this dimension. This block will then later be replaced by ores. You can add as many
     * blocks as you want. Just don't add Blocks.Air, as there is another setting for allowing Air-Replacement
     *
     * @param pBlockDef
     * @return
     */
    public boolean addBlockDefinition(ModDBMDef pBlockDef) {
        if (_mReplaceableBlocks.contains(pBlockDef)) {
            return false;
        } else {
            _mReplaceableBlocks.add(pBlockDef);
            return true;
        }
    }

    /**
     * Internal function
     *
     * @return The DimensionName in a Human-readable format
     */
    public String getDimensionName() {
        return _mDimensionName;
    }

    /**
     * Internal function
     *
     * @return A list of all defined Blocks that can be replaced while generating ores
     */
    public ArrayList<ModDBMDef> getReplaceableBlocks() {
        return _mReplaceableBlocks;
    }

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
            pChunkProvider.toString()
                .substring(6),
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
            pChunkProvider.toString()
                .substring(6),
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
        _mInternalDimIdentifier = STR_NOTDEFINED;
        _mDimensionName = pDimensionName;
        _mChunkProvider = pChunkProviderName;
        _mDimensionType = pDimType;

        _mReplaceableBlocks = new ArrayList<>();
        if (pBlockDefinitions != null) _mReplaceableBlocks.addAll(pBlockDefinitions);

        _mValidAsteroidMaterials = new ArrayList<>();
        _mSpecialBlocksForAsteroids = new ArrayList<>();
        _mSpaceObjectsGenerators = new ArrayList<>();
        _mSpaceStructureGenerators = new ArrayList<>();
    }

    /**
     * Internal function
     *
     * Do not call this function by yourself. Ever. It will cause explosions, water to blood, death of firstborn,...
     * Seriously, don't do it.
     */
    protected void setParentModName(String pModName) {
        if (_mInternalDimIdentifier.equals(STR_NOTDEFINED)) {
            _mInternalDimIdentifier = String.format("%s_%s", pModName, _mDimensionName);
        }

        // Else Don't update, we're already set

    }

    /**
     * Internal function
     *
     * Check if pBlock can be replaced by an ore
     *
     * @param pBlock
     * @param pMeta
     * @return
     */
    public ReplaceState getReplaceStateForBlock(Block pBlock, int pMeta) {
        ReplaceState tFlag = ReplaceState.Unknown;

        for (ModDBMDef pDef : _mReplaceableBlocks) {
            ReplaceState tResult = pDef.blockEquals(pBlock, pMeta);
            if (tResult == ReplaceState.Unknown) continue;

            if (tResult == ReplaceState.CanReplace) {
                // GalacticGreg.Logger.trace("Targetblock found and metadata match. Replacement allowed");
                tFlag = ReplaceState.CanReplace;
            } else if (tResult == ReplaceState.CannotReplace) {
                // GalacticGreg.Logger.trace("Targetblock found but metadata mismatch. Replacement denied");
                tFlag = ReplaceState.CannotReplace;
            }
            break;
        }

        return tFlag;
    }

    /**
     * Internal function
     *
     * Randomly select one material out of all defined materials
     *
     * @return
     */
    public AsteroidBlockComb getRandomAsteroidMaterial() {
        if (_mValidAsteroidMaterials.size() == 0) return null;

        if (_mValidAsteroidMaterials.size() == 1) return _mValidAsteroidMaterials.get(0);
        else {
            return _mValidAsteroidMaterials.get(_mRandom.nextInt(_mValidAsteroidMaterials.size()));
        }
    }

    /**
     * Internal function
     *
     * Randomly select one special block to be placed in the asteroids
     *
     * @return
     */
    public SpecialBlockComb getRandomSpecialAsteroidBlock() {
        if (_mSpecialBlocksForAsteroids.size() == 0) return null;

        if (_mSpecialBlocksForAsteroids.size() == 1) return _mSpecialBlocksForAsteroids.get(0);
        else {
            return _mSpecialBlocksForAsteroids.get(_mRandom.nextInt(_mSpecialBlocksForAsteroids.size()));
        }
    }

    /**
     * Define the material the asteroid shall be made of. Limited to GT-Based Ores and their stones
     *
     * @param pMaterial
     */
    public void addAsteroidMaterial(GTOreTypes pMaterial) {
        addAsteroidMaterial(new AsteroidBlockComb(pMaterial));
    }

    /**
     * Define the material the asteroid shall be made of, more advanced option to specify your own blocks
     *
     * @param pBlockComb
     */
    public void addAsteroidMaterial(AsteroidBlockComb pBlockComb) {
        if (!_mValidAsteroidMaterials.contains(pBlockComb)) {
            _mValidAsteroidMaterials.add(pBlockComb);
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
        if (!_mSpecialBlocksForAsteroids.contains(pBlock)) {
            _mSpecialBlocksForAsteroids.add(pBlock);
        }
    }

    /**
     * Internal function Called when GalacticGreg will finalize all its internal structures. You should never call this
     * yourself
     */
    public void finalizeReplaceableBlocks(String pParentModName) {
        for (ModDBMDef rpb : _mReplaceableBlocks) {
            try {
                rpb.updateBlockName(pParentModName);
                if (_mStoneType == null) _mStoneType = GTOreTypes.NormalOres;
            } catch (Exception e) {
                // GalacticGreg.Logger.error("Unable to finalize replaceable block with modname for block %s. Dimension
                // %s will probably have problems generating ores", rpb.getBlockName(), _mDimensionName);
            }
        }
    }
}
