package gregtech.common.powergoggles.gui;

import java.math.BigInteger;
import java.util.LinkedList;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public abstract class PowerGogglesRenderer {

    protected final Minecraft mc = Minecraft.getMinecraft();
    private LinkedList<BigInteger> measurements = new LinkedList<>();

    public abstract void renderMainInfo(RenderGameOverlayEvent.Post event);

    public abstract void renderPowerChart();

    public LinkedList<BigInteger> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(LinkedList<BigInteger> measurements) {
        this.measurements = measurements;
    }
}
