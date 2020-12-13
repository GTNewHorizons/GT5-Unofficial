package com.github.technus.tectech.mechanics.pipe;

/**
 * Created by Tec on 26.02.2017.
 */
public interface IConnectsToDataPipe {
    boolean canConnectData(byte side);

    IConnectsToDataPipe getNext(IConnectsToDataPipe source);

    boolean isDataInputFacing(byte side);

    byte getColorization();
}
