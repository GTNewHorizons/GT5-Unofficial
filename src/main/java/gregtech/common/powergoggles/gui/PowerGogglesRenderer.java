package gregtech.common.powergoggles.gui;

import java.math.BigInteger;
import java.util.LinkedList;

import gregtech.common.powergoggles.PowerGogglesMeasurement;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import gregtech.common.powergoggles.PowerGogglesConstants;
import gregtech.common.powergoggles.handlers.PowerGogglesConfigHandler;

public abstract class PowerGogglesRenderer {

    protected final Minecraft mc = Minecraft.getMinecraft();
    protected LinkedList<BigInteger> legacyMeasurements = new LinkedList<>();
    protected LinkedList<PowerGogglesMeasurement> measurements = new LinkedList<>();

    protected final static int TICKS = 1;
    protected final static int SECONDS = 20 * TICKS;
    protected final static int MINUTES = 60 * SECONDS;

    protected final int ticksBetweenMeasurements = PowerGogglesConstants.TICKS_BETWEEN_MEASUREMENTS;
    protected final int measurementCount5m = PowerGogglesConstants.MEASUREMENT_COUNT_5M;
    protected final int measurementCount1h = PowerGogglesConstants.MEASUREMENT_COUNT_1H;

    protected BigInteger currentEU = BigInteger.valueOf(0);
    protected BigInteger measurement = BigInteger.valueOf(0);
    protected BigInteger highest = BigInteger.valueOf(0);
    protected long capacity = 0; // If this is higher than 0 there's a linked LSC

    protected int change5mColor = PowerGogglesConfigHandler.textOkColor;
    protected String change5mString = "";
    protected BigInteger change5m = BigInteger.valueOf(0);
    protected int change5mDiff;

    protected int change1hColor = PowerGogglesConfigHandler.textOkColor;
    protected String change1hString = "";
    protected BigInteger change1h = BigInteger.valueOf(0);
    protected int change1hDiff;

    protected String storage = "";

    public abstract void renderMainInfo(RenderGameOverlayEvent.Post event);

    public abstract void renderPowerChart();

    public abstract void drawTick();

    public abstract void clear();

    public abstract void setMeasurement(BigInteger eu, long lscCapacity);

    public LinkedList<BigInteger> getLegacyMeasurements() {
        return legacyMeasurements;
    }

    public long getCapacity() {
        return capacity;
    }

    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }

    public void setLegacyMeasurements(LinkedList<BigInteger> legacyMeasurements) {
        this.legacyMeasurements = legacyMeasurements;

    }

}
