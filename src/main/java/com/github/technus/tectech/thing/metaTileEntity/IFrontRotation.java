package com.github.technus.tectech.thing.metaTileEntity;

public interface IFrontRotation {
    boolean isFrontRotationValid(byte frontRotation, byte frontFacing);

    void rotateAroundFrontPlane(boolean direction);

    void forceSetRotationDoRender(byte frontRotation);

    byte getFrontRotation();
}
