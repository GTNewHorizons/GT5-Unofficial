package galacticgreg.api;

import net.minecraft.block.Block;

/**
 * Class used for Simple Block - Meta constructs
 */
public class BlockMetaComb {

    private int mMeta;
    private Block mBlock;

    /**
     * Creates a simple instance for a block that has no meta value
     *
     * @param pBlock The Block in question. 0 is used as meta
     */
    public BlockMetaComb(Block pBlock) {
        this(pBlock, 0);
    }

    /**
     * Creates a simple instance for a block with a meta value
     *
     * @param pBlock The Block in question
     * @param pMeta  The MetaValue in question ([block]:[meta])
     */
    public BlockMetaComb(Block pBlock, int pMeta) {
        mMeta = pMeta;
        mBlock = pBlock;
    }

    /**
     * Internal function
     *
     * @return The metadata for this block
     */
    public int getMeta() {
        return mMeta;
    }

    /**
     * Internal function
     *
     * @return The block
     */
    public Block getBlock() {
        return mBlock;
    }
}
