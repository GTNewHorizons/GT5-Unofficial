package gregtech.api.interfaces;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GT_CoverBehavior;

/**
 * Implemented by the MetaTileEntity of the Redstone Circuit Block
 */
public interface IRedstoneCircuitBlock {

    /**
     * The Output Direction the Circuit Block is Facing
     */
    ForgeDirection getOutputFacing();

    /**
     * sets Output Redstone State at Side
     */
    boolean setRedstone(byte aStrength, ForgeDirection side);

    /**
     * returns Output Redstone State at Side Note that setRedstone checks if there is a Difference between the old and
     * the new Setting before consuming any Energy
     */
    byte getOutputRedstone(ForgeDirection side);

    /**
     * returns Input Redstone Signal at Side
     */
    byte getInputRedstone(ForgeDirection side);

    /**
     * If this Side is Covered up and therefor not doing any Redstone
     */
    GT_CoverBehavior getCover(ForgeDirection side);

    int getCoverID(ForgeDirection side);

    int getCoverVariable(ForgeDirection side);

    /**
     * returns whatever Block-ID is adjacent to the Redstone Circuit Block
     */
    Block getBlockAtSide(ForgeDirection side);

    /**
     * returns whatever Meta-Value is adjacent to the Redstone Circuit Block
     */
    byte getMetaIDAtSide(ForgeDirection side);

    /**
     * returns whatever TileEntity is adjacent to the Redstone Circuit Block
     */
    TileEntity getTileEntityAtSide(ForgeDirection side);

    /**
     * returns whatever TileEntity is used by the Redstone Circuit Block
     */
    ICoverable getOwnTileEntity();

    /**
     * returns worldObj.rand.nextInt(aRange)
     */
    int getRandom(int aRange);
}
