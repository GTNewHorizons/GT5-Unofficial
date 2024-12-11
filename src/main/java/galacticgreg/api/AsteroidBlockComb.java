package galacticgreg.api;

import gregtech.api.enums.StoneType;
import net.minecraft.block.Block;

/**
 * Class for a bit more advanced combinations for Asteroids, which supports Custom Blocks as base material and Values
 * required to generate Gregtech ores
 */
public class AsteroidBlockComb extends BlockMetaComb {

    private final StoneType stoneType;

    /**
     * Create an advanced definition which uses the GregTech-OreType values for ores, and your own definition of Block
     * for the asteroid material
     *
     * @param pOreType The GregTech oreType
     * @param pBlock   Your block
     */
    public AsteroidBlockComb(StoneType stoneType, Block pBlock) {
        super(pBlock, 0);
        this.stoneType = stoneType;
    }

    /**
     * Create an advanced definition which uses the GregTech-OreType values for ores, and your own definition of Block
     * for the asteroid material
     *
     * @param pOreType The GregTech oreType
     * @param pBlock   Your block
     * @param pMeta    The metavalue for your block (If required)
     */
    public AsteroidBlockComb(StoneType stoneType, Block pBlock, int pMeta) {
        super(pBlock, pMeta);
        this.stoneType = stoneType;
    }

    /**
     * Create a simple definition which uses the GregTech-OreType values for both asteroidStone and ores
     *
     * @param pOreType The GregTech oreType
     */
    public AsteroidBlockComb(StoneType stoneType) {
        super(stoneType.getStone().left(), stoneType.getStone().rightInt());
        this.stoneType = stoneType;
    }

    /**
     * Internal function
     *
     * @return The GT Material for the oregen
     */
    public StoneType getStone() {
        return stoneType;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof AsteroidBlockComb otherObj)) return false;

        boolean tFlag = true;
        String otherName = Block.blockRegistry.getNameForObject(otherObj.getBlock());
        String thisName = Block.blockRegistry.getNameForObject(this.getBlock());
        if (otherName != null && thisName != null) {
            if (!otherName.equals(thisName)) tFlag = false;

            if (!(otherObj.getMeta() == this.getMeta())) tFlag = false;

            if (!(otherObj.getStone() == this.getStone())) tFlag = false;
        } else tFlag = false;

        return tFlag;
    }
}
