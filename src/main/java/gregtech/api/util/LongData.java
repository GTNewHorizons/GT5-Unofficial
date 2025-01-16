package gregtech.api.util;

import java.math.BigInteger;

public interface LongData {

    default double avg() {
        int size = size();
        if (size == 0) return 0d;
        return sum().doubleValue() / size;
    }

    default long avgLong() {
        int size = size();
        if (size == 0) return 0;
        return sum().divide(BigInteger.valueOf(size))
            .longValueExact();
    }

    int size();

    BigInteger sum();
}
