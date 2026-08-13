package gregtech.api.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.UUID;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

class GTLogTest {

    @Test
    void disabledLoggerLevelSurvivesLoggerUpdates() {
        String suffix = UUID.randomUUID()
            .toString();
        Logger logger = GTLog.disabledLogger("GTLogTest.first." + suffix);

        GTLog.disabledLogger("GTLogTest.second." + suffix);

        assertEquals(Level.OFF, ((org.apache.logging.log4j.core.Logger) logger).getLevel());
        assertFalse(logger.isInfoEnabled());
    }
}
