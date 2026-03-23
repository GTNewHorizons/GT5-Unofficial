package gregtech.api.util;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * Just a simple Logging Function. If on Server, then this will point to System.out and System.err
 */
public class GTLog {

    public static PrintStream out = System.out;
    public static PrintStream err = System.err;
    public static PrintStream ore = new LogBuffer();
    public static PrintStream exp = new LogBuffer();
    public static PrintStream ico = new LogBuffer();
    public static File mLogFile;
    public static File mOreDictLogFile;
    public static File mExplosionLog;
    public static File mRegisterIconsLog;

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

    public static void writeExplosionLog(String message) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime currentTime = LocalDateTime.now();
        GTLog.exp.printf("[%s]: %s%n", currentTime.format(formatter), message);
    }

    public static void writeExplosionLog(String dimension, int x, int y, int z, String blockName, String ownerName,
        String details) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime currentTime = LocalDateTime.now();
        exp.printf(
            "[%s] DIM %s (%d,%d,%d): %s (built by %s) %s%n",
            currentTime.format(formatter),
            dimension,
            x,
            y,
            z,
            blockName,
            ownerName,
            details);
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
