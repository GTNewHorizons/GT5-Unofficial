package galacticgreg.api;

import net.minecraft.util.Vec3;

import galacticgreg.api.Enums.TargetBlockPosition;

/**
 * Structural information container. Holds X/Y/Z and block/meta information
 */
public class StructureInformation {

    private Vec3 _mCoordinates;
    private TargetBlockPosition _mBlockPosition;
    private BlockMetaComb _mBlockMetaComb;

    public TargetBlockPosition getBlockPosition() {
        return _mBlockPosition;
    }

    public int getX() {
        return (int) Math.round(_mCoordinates.xCoord);
    }

    public int getY() {
        return (int) Math.round(_mCoordinates.yCoord);
    }

    public int getZ() {
        return (int) Math.round(_mCoordinates.zCoord);
    }

    public BlockMetaComb getBlock() {
        return _mBlockMetaComb;
    }

    /**
     * Init StructureInfo only with Coords and block position
     *
     * @param pCoordinates The coords in question
     * @param pPosition    The position-enum value
     */
    public StructureInformation(Vec3 pCoordinates, TargetBlockPosition pPosition) {
        this(pCoordinates, pPosition, null);
    }

    /**
     * Init StructureInfo with Coords, block position and a populated block/meta info
     *
     * @param pCoordinates The coords in question
     * @param pPosition    The position-enum value
     * @param pTargetBlock The target block in question
     */
    public StructureInformation(Vec3 pCoordinates, TargetBlockPosition pPosition, BlockMetaComb pTargetBlock) {
        _mCoordinates = pCoordinates;
        _mBlockPosition = pPosition;
        _mBlockMetaComb = pTargetBlock;
    }

}
