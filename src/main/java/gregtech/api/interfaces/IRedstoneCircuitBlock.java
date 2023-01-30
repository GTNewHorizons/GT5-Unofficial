package gregtech.api.interfaces;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GT_CoverBehavior;

/**
 * Implemented by the MetaTileEntity of the Redstone Circuit Block
 */
public interface IRedstoneCircuitBlock {

    /**
     * The Output Direction the Circuit Block is Facing
     */
    byte getOutputFacing();

    /**
     * sets Output Redstone State at Side
     */
    boolean setRedstone(byte aStrength, byte aSide);

    /**
     * returns Output Redstone State at Side Note that setRedstone checks if there is a Difference between the old and
     * the new Setting before consuming any Energy
     */
    byte getOutputRedstone(byte aSide);

    /**
     * returns Input Redstone Signal at Side
     */
    byte getInputRedstone(byte aSide);

    /**
     * If this Side is Covered up and therefor not doing any Redstone
     */
    GT_CoverBehavior getCover(byte aSide);

    int getCoverID(byte aSide);

    int getCoverVariable(byte aSide);

    /**
     * returns whatever Block-ID is adjacent to the Redstone Circuit Block
     */
    Block getBlockAtSide(byte aSide);

    /**
     * returns whatever Meta-Value is adjacent to the Redstone Circuit Block
     */
    byte getMetaIDAtSide(byte aSide);

    /**
     * returns whatever TileEntity is adjacent to the Redstone Circuit Block
     */
    TileEntity getTileEntityAtSide(byte aSide);

    /**
     * returns whatever TileEntity is used by the Redstone Circuit Block
     */
    ICoverable getOwnTileEntity();

    /**
     * returns worldObj.rand.nextInt(aRange)
     */
    int getRandom(int aRange);
}
