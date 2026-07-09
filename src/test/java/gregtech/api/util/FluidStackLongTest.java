package gregtech.api.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.mockito.Answers;

public class FluidStackLongTest {
    @ParameterizedTest
    @MethodSource("provideParameters")
    public void testAmountLong(long amount) {
        FluidStackLong stack = Mockito.mock(FluidStackLong.class, Answers.CALLS_REAL_METHODS);

        stack.setAmountLong(amount);
        assertEquals(amount, stack.getAmountLong());
    }

    @ParameterizedTest
    @MethodSource("provideParameters")
    public void testAmountInt(long amount, int amountInt) {
        FluidStackLong stack = Mockito.mock(FluidStackLong.class, Answers.CALLS_REAL_METHODS);

        stack.setAmountLong(amount);
        assertEquals(amountInt, stack.amount);
    }

    @ParameterizedTest
    @MethodSource("provideParameters")
    public void testAmountIntSync(long amount) {
        FluidStackLong stack = Mockito.mock(FluidStackLong.class, Answers.CALLS_REAL_METHODS);

        stack.setAmountLong(amount);
        stack.amount -= 1000;

        assertEquals(amount-1000, stack.getAmountLong());
    }

    private static Stream<Arguments> provideParameters() {
        return Stream.of(
            Arguments.of(Long.MAX_VALUE, Integer.MAX_VALUE),
            Arguments.of(Math.powExact(2L, 48), Integer.MAX_VALUE),
            Arguments.of(Integer.MAX_VALUE, Integer.MAX_VALUE),
            Arguments.of(1000, 1000)
        );
    }
}
