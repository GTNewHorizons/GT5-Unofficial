package tectech.mechanics.tesla;

import com.gtnewhorizon.structurelib.util.Vec3Impl;

public interface ITeslaConnectableSimple {

    // -128 to -1 disables capability
    // 0 means any source or target
    // 1 to 127 must match on source and target or source/target must be 0
    byte getTeslaReceptionCapability();

    // Reception Coefficient is a range extension, typical is 1
    float getTeslaReceptionCoefficient();

    boolean isTeslaReadyToReceive();

    long getTeslaStoredEnergy();

    boolean teslaInjectEnergy(long teslaVoltageInjected);

    Vec3Impl getTeslaPosition();

    Integer getTeslaDimension();
}
