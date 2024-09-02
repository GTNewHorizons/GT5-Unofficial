package galacticgreg.api;

import net.minecraft.block.Block;

/**
 * Class for a bit more advanced combinations for Asteroids, which supports Custom Blocks as base material and Values
 * required to generate Gregtech ores
 */
public class AsteroidBlockComb extends BlockMetaComb {

    private final GTOreTypes _mGTOreMaterial;

    /**
     * Create an advanced definition which uses the GregTech-OreType values for ores, and your own definition of Block
     * for the asteroid material
     *
     * @param pOreType The GregTech oreType
     * @param pBlock   Your block
     */
    public AsteroidBlockComb(GTOreTypes pOreType, Block pBlock) {
        super(pBlock, 0);
        _mGTOreMaterial = pOreType;
    }

    /**
     * Create an advanced definition which uses the GregTech-OreType values for ores, and your own definition of Block
     * for the asteroid material
     *
     * @param pOreType The GregTech oreType
     * @param pBlock   Your block
     * @param pMeta    The metavalue for your block (If required)
     */
    public AsteroidBlockComb(GTOreTypes pOreType, Block pBlock, int pMeta) {
        super(pBlock, pMeta);
        _mGTOreMaterial = pOreType;
    }

    /**
     * Create a simple definition which uses the GregTech-OreType values for both asteroidStone and ores
     *
     * @param pOreType The GregTech oreType
     */
    public AsteroidBlockComb(GTOreTypes pOreType) {
        super(pOreType.getBlock(), pOreType.getMeta());
        _mGTOreMaterial = pOreType;
    }

    /**
     * Internal function
     *
     * @return The GT Material for the oregen
     */
    public GTOreTypes getOreMaterial() {
        return _mGTOreMaterial;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof AsteroidBlockComb)) return false;
        AsteroidBlockComb otherObj = (AsteroidBlockComb) other;

        boolean tFlag = true;
        String otherName = Block.blockRegistry.getNameForObject(otherObj.getBlock());
        String thisName = Block.blockRegistry.getNameForObject(this.getBlock());
        if (otherName != null && thisName != null) {
            if (!otherName.equals(thisName)) tFlag = false;

            if (!(otherObj.getMeta() == this.getMeta())) tFlag = false;

            if (!(otherObj.getOreMaterial() == this.getOreMaterial())) tFlag = false;
        } else tFlag = false;

        return tFlag;
    }
}
