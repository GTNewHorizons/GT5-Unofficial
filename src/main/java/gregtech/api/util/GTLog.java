package gregtech.api.util;

import static gregtech.GTMod.Loggers.GT_EXPLOSION_LOGGER;
import static gregtech.GTMod.Loggers.GT_ICON_LOGGER;
import static gregtech.GTMod.Loggers.GT_ORE_DICT_LOGGER;

import java.io.File;

import org.apache.logging.log4j.Level;
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

    public static void configureExplosionLogger(File parentFile) {
        configureRollingLogger(
            GT_EXPLOSION_LOGGER,
            Gregtech.general.loggingExplosions,
            new File(parentFile, "logs/explosions.log"),
            new File(parentFile, "logs/explosions-%i.log"),
            "GregTechExplosionFile");
    }

    public static void configureOreDictLogger(File parentFile) {
        boolean configured = configureRollingLogger(
            GT_ORE_DICT_LOGGER,
            Gregtech.general.loggingOreDict,
            new File(parentFile, "logs/OreDict.log"),
            new File(parentFile, "logs/OreDict-%i.log"),
            "GregTechOreDictFile");
        if (!configured) return;

        GT_ORE_DICT_LOGGER.info("******************************************************************************");
        GT_ORE_DICT_LOGGER.info("* This is the complete log of the GT5-Unofficial OreDictionary Handler. It   *");
        GT_ORE_DICT_LOGGER.info("* processes all OreDictionary entries and can sometimes cause errors. All    *");
        GT_ORE_DICT_LOGGER.info("* entries and errors are being logged. If you see an error please raise an   *");
        GT_ORE_DICT_LOGGER.info("* issue at https://github.com/GTNewHorizons/GT-New-Horizons-Modpack/issues.  *");
        GT_ORE_DICT_LOGGER.info("******************************************************************************");
    }

    public static void configureIconLogger(File parentFile) {
        boolean configured = configureRollingLogger(
            GT_ICON_LOGGER,
            Gregtech.debug.logRegisterIcons,
            new File(parentFile, "logs/RegisterIcon.log"),
            new File(parentFile, "logs/RegisterIcon-%i.log"),
            "GregTechIconFile");
        if (!configured) return;

        GT_ICON_LOGGER.info("*****************************************************************");
        GT_ICON_LOGGER.info("* This is the log of texture icons registered in GT5-Unofficial *");
        GT_ICON_LOGGER.info("* First column R|O tells if resource is (Required or Optional)  *");
        GT_ICON_LOGGER.info("* Second column is the resource path                            *");
        GT_ICON_LOGGER.info("*****************************************************************");
    }

    private static void configureLogger(Logger apiLogger, boolean enabled) {
        org.apache.logging.log4j.core.Logger logger = (org.apache.logging.log4j.core.Logger) apiLogger;
        logger.setAdditive(false);
        logger.setLevel(enabled ? Level.INFO : Level.OFF);
    }

    public static boolean configureRollingLogger(Logger apiLogger, boolean enabled, File file, File filePattern,
        String appenderName) {
        configureLogger(apiLogger, enabled);
        if (!enabled) return false;

        org.apache.logging.log4j.core.Logger logger = (org.apache.logging.log4j.core.Logger) apiLogger;

        Configuration configuration = logger.getContext()
            .getConfiguration();
        PatternLayout layout = PatternLayout
            .createLayout("[%d{yyyy-MM-dd HH:mm:ss}] %msg%n", configuration, null, null, null);
        CompositeTriggeringPolicy policy = CompositeTriggeringPolicy
            .createPolicy(OnStartupTriggeringPolicy.createPolicy());
        DefaultRolloverStrategy strategy = DefaultRolloverStrategy
            .createStrategy("3", null, "max", null, configuration);
        RollingRandomAccessFileAppender appender = RollingRandomAccessFileAppender.createAppender(
            file.getPath(),
            filePattern.getPath(),
            "true",
            appenderName,
            "true",
            policy,
            strategy,
            layout,
            null,
            "true",
            "false",
            null,
            configuration);
        if (appender == null) throw new IllegalStateException("Failed to create " + appenderName);

        appender.start();
        logger.addAppender(appender);
        return true;
    }

    public static void writeExplosionLog(String dimension, int x, int y, int z, String blockName, String ownerName,
        String details) {
        GT_EXPLOSION_LOGGER
            .info("DIM {} ({},{},{}): {} (built by {}) {}", dimension, x, y, z, blockName, ownerName, details);
    }

    public static void writeExplosionLog(IMetaTileEntity tileEntity, String details) {
        if (tileEntity == null) {
            GT_EXPLOSION_LOGGER.info(details);
            return;
        }
        IGregTechTileEntity baseTileEntity = tileEntity.getBaseMetaTileEntity();
        if (baseTileEntity == null) {
            GT_EXPLOSION_LOGGER.info(details);
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
            GT_EXPLOSION_LOGGER.info(details);
        }
    }

}
