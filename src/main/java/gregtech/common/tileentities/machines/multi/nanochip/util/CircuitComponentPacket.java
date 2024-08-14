package gregtech.common.tileentities.machines.multi.nanochip.util;

import java.util.HashMap;
import java.util.Map;

public class CircuitComponentPacket {

    private final Map<CircuitComponent, Long> components = new HashMap<>();

    public CircuitComponentPacket(CircuitComponent component, long amount) {
        components.put(component, amount);
    }

    // Accept more circuit components from a new packet
    public void unifyWith(CircuitComponentPacket other) {
        for (Map.Entry<CircuitComponent, Long> entry : other.components.entrySet()) {
            components.merge(entry.getKey(), entry.getValue(), Long::sum);
        }
    }

    public Map<CircuitComponent, Long> getComponents() {
        return components;
    }
}
