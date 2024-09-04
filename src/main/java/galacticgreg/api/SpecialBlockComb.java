package galacticgreg.api;

import net.minecraft.block.Block;

public class SpecialBlockComb extends BlockMetaComb {

    private Enums.AllowedBlockPosition _mBlockPosition;

    /**
     * Creates a simple instance for a block that has a meta value and a block position it is allowed to spawn
     *
     * @param pBlock         The Block in question
     * @param pMeta          The meta value of the block
     * @param pBlockPosition The position this block is allowed to generate
     */
    public SpecialBlockComb(Block pBlock, int pMeta, Enums.AllowedBlockPosition pBlockPosition) {
        super(pBlock, pMeta);
        _mBlockPosition = pBlockPosition;
    }

    /**
     * Creates a simple instance for a block that has no meta value but a position it is allowed to spawn
     *
     * @param pBlock         The Block in question. 0 is used as meta
     * @param pBlockPosition The position this block is allowed to generate
     */
    public SpecialBlockComb(Block pBlock, Enums.AllowedBlockPosition pBlockPosition) {
        super(pBlock, 0);
        _mBlockPosition = pBlockPosition;
    }

    /**
     * Creates a simple instance for a block that has no meta value and is allowed to spawn everywhere
     *
     * @param pBlock The Block in question. 0 is used as meta, and "CoreAndShell" is used as position
     */
    public SpecialBlockComb(Block pBlock) {
        super(pBlock, 0);
        _mBlockPosition = Enums.AllowedBlockPosition.AsteroidCoreAndShell;
    }

    /**
     * Internal function
     *
     * @return The position the block is supposed to spawn at
     */
    public Enums.AllowedBlockPosition getBlockPosition() {
        return _mBlockPosition;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof SpecialBlockComb)) return false;
        SpecialBlockComb otherObj = (SpecialBlockComb) other;

        boolean tFlag = true;
        String otherName = Block.blockRegistry.getNameForObject(otherObj.getBlock());
        String thisName = Block.blockRegistry.getNameForObject(this.getBlock());
        if (!otherName.equals(thisName)) tFlag = false;

        if (!(otherObj.getMeta() == this.getMeta())) tFlag = false;

        if (!(otherObj.getBlockPosition() == this.getBlockPosition())) tFlag = false;

        return tFlag;
    }
}
