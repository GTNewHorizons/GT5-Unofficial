package gregtech.common.powergoggles.gui;

import java.util.LinkedList;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import gregtech.common.powergoggles.PowerGogglesMeasurement;

public abstract class PowerGogglesRenderer {

    protected final Minecraft mc = Minecraft.getMinecraft();
    protected LinkedList<PowerGogglesMeasurement> measurements = new LinkedList<>();

    public abstract void render(RenderGameOverlayEvent.Post event);

    public abstract void processMeasurement(PowerGogglesMeasurement measurement);

    public abstract void setMeasurements(LinkedList<PowerGogglesMeasurement> measurements);
}
