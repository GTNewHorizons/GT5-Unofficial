package gregtech.api.util;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.RollingRandomAccessFileAppender;
import org.apache.logging.log4j.core.appender.rolling.CompositeTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.OnStartupTriggeringPolicy;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.layout.PatternLayout;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.common.config.Gregtech;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * Just a simple Logging Function. If on Server, then this will point to System.out and System.err
 */
public class GTLog {

    public static final Logger EXPLOSION_LOGGER = LogManager.getLogger("GregTech Explosions");
    public static PrintStream out = System.out;
    public static PrintStream err = System.err;
    public static PrintStream ore = Gregtech.general.loggingOreDict ? new LogBuffer() : new VoidLogger();
    public static PrintStream ico = Gregtech.debug.logRegisterIcons ? new LogBuffer() : new VoidLogger();
    public static File mLogFile;
    public static File mOreDictLogFile;
    public static File mRegisterIconsLog;

    public static void configureExplosionLogger(File parentFile) {
        org.apache.logging.log4j.core.Logger logger = (org.apache.logging.log4j.core.Logger) EXPLOSION_LOGGER;
        logger.setAdditive(false);
        logger.setLevel(Level.INFO);
        if (!Gregtech.general.loggingExplosions) return;

        Configuration configuration = logger.getContext()
            .getConfiguration();
        PatternLayout layout = PatternLayout
            .createLayout("[%d{yyyy-MM-dd HH:mm:ss}] %msg%n", configuration, null, null, null);
        CompositeTriggeringPolicy policy = CompositeTriggeringPolicy
            .createPolicy(OnStartupTriggeringPolicy.createPolicy());
        DefaultRolloverStrategy strategy = DefaultRolloverStrategy
            .createStrategy("3", null, "max", null, configuration);
        RollingRandomAccessFileAppender appender = RollingRandomAccessFileAppender.createAppender(
            new File(parentFile, "logs/explosions.log").getPath(),
            new File(parentFile, "logs/explosions-%i.log").getPath(),
            "true",
            "GregTechExplosionFile",
            "true",
            policy,
            strategy,
            layout,
            null,
            "true",
            "false",
            null,
            configuration);
        if (appender == null) throw new IllegalStateException("Failed to create explosion log appender");

        appender.start();
        logger.addAppender(appender);
    }

    public static class LogBuffer extends PrintStream {

        public final List<String> lineBuffer = new ArrayList<>();

        public LogBuffer() {
            super(new OutputStream() {

                @Override
                public void write(int arg0) {
                    /* Do nothing */
                }
            });
        }

        @Override
        public void println(String aString) {
            lineBuffer.add(aString);
        }
    }

    public static class VoidLogger extends PrintStream {

        public VoidLogger() {
            super(new OutputStream() {

                @Override
                public void write(int arg0) {
                    /* Do nothing */
                }
            });
        }

        @Override
        public void println(String aString) {
            /* Do nothing */
        }
    }

    public static void writeExplosionLog(String message) {
        if (!Gregtech.general.loggingExplosions) return;
        EXPLOSION_LOGGER.info(message);
    }

    public static void writeExplosionLog(String dimension, int x, int y, int z, String blockName, String ownerName,
        String details) {
        if (!Gregtech.general.loggingExplosions) return;
        EXPLOSION_LOGGER
            .info("DIM {} ({},{},{}): {} (built by {}) {}", dimension, x, y, z, blockName, ownerName, details);
    }

    public static void writeExplosionLog(IMetaTileEntity tileEntity, String details) {
        if (tileEntity == null) {
            writeExplosionLog(details);
            return;
        }
        IGregTechTileEntity baseTileEntity = tileEntity.getBaseMetaTileEntity();
        if (baseTileEntity == null) {
            writeExplosionLog(details);
            return;
        }
        writeExplosionLog(baseTileEntity, tileEntity.getLocalName(), details);
    }

    public static void writeExplosionLog(IGregTechTileEntity baseTileEntity, String name, String details) {
        if (baseTileEntity != null) {
            writeExplosionLog(
                baseTileEntity.getWorld().provider.getDimensionName(),
                baseTileEntity.getXCoord(),
                baseTileEntity.getYCoord(),
                baseTileEntity.getZCoord(),
                name,
                baseTileEntity.getOwnerName(),
                details);
        } else {
            writeExplosionLog(details);
        }
    }

}
