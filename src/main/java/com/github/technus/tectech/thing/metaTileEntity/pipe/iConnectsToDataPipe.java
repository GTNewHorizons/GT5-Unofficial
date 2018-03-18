package com.github.technus.tectech.thing.metaTileEntity.pipe;

/**
 * Created by Tec on 26.02.2017.
 */
public interface iConnectsToDataPipe {
    boolean canConnect(byte side);

    iConnectsToDataPipe getNext(iConnectsToDataPipe source);

    boolean isDataInputFacing(byte side);
}
