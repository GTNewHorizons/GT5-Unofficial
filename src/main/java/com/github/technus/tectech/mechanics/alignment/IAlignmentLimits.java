package com.github.technus.tectech.mechanics.alignment;
import com.github.technus.tectech.mechanics.alignment.enumerable.ExtendedFacing;
import com.github.technus.tectech.mechanics.alignment.enumerable.Flip;
import com.github.technus.tectech.mechanics.alignment.enumerable.Rotation;
import net.minecraftforge.common.util.ForgeDirection;

public interface IAlignmentLimits {

    IAlignmentLimits UNLIMITED= (direction, rotation, flip) -> true;

    boolean isNewExtendedFacingValid(ForgeDirection direction, Rotation rotation, Flip flip);

    default boolean isNewExtendedFacingValid(ExtendedFacing alignment){
        return isNewExtendedFacingValid(
                alignment.getDirection(),
                alignment.getRotation(),
                alignment.getFlip());
    }
}
