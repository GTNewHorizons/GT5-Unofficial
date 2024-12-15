package gregtech.api.util;

import java.math.BigInteger;

public interface LongData {

    default double avg() {
        return sum().doubleValue() / size();
    }

    default long avgLong() {
        return sum().divide(BigInteger.valueOf(size()))
            .longValueExact();
    }

    int size();

    BigInteger sum();
}
