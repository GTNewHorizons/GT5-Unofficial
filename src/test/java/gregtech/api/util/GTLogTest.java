package gregtech.api.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.UUID;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

class GTLogTest {

    @Test
    void conditionalLoggerLevelSurvivesLoggerUpdates() {
        String suffix = UUID.randomUUID()
            .toString();
        Logger logger = GTLog.conditionalLogger("GTLogTest.first." + suffix);

        GTLog.conditionalLogger("GTLogTest.second." + suffix);

        assertEquals(Level.OFF, ((org.apache.logging.log4j.core.Logger) logger).getLevel());
        assertFalse(logger.isInfoEnabled());
    }
}
