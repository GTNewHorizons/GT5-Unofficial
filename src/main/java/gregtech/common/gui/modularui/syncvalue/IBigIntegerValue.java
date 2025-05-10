package gregtech.common.gui.modularui.syncvalue;

import java.math.BigInteger;

import com.cleanroommc.modularui.api.value.IValue;

public interface IBigIntegerValue<T> extends IValue<T> {

    BigInteger getBigIntegerValue();

    void setBigIntegerValue(BigInteger val);
}
