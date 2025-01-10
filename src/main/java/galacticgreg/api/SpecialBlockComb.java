package galacticgreg.api;

import net.minecraft.block.Block;

import com.gtnewhorizon.gtnhlib.util.data.BlockMeta;

public class SpecialBlockComb extends BlockMeta {

    private final Enums.AllowedBlockPosition _mBlockPosition;

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
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((_mBlockPosition == null) ? 0 : _mBlockPosition.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        SpecialBlockComb other = (SpecialBlockComb) obj;
        if (_mBlockPosition != other._mBlockPosition) return false;
        return true;
    }
}
