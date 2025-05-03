package gregtech.common.gui.modularui.syncvalue;

import com.cleanroommc.modularui.api.value.IValue;

import java.math.BigInteger;

public interface IBigIntegerValue<T> extends IValue<T> {

    BigInteger getBigIntegerValue();

    void setBigIntegerValue(BigInteger val);
}
