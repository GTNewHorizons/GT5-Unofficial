package gregtech.common.tileentities.machines.multi.nanochip.util;

import java.util.HashMap;
import java.util.Map;

public class CircuitComponentPacket {

    private final Map<CircuitComponentType, Integer> components = new HashMap<>();

    // Accept more circuit components from a new packet
    public void acceptMore(CircuitComponentPacket other) {
        for (Map.Entry<CircuitComponentType, Integer> entry : other.components.entrySet()) {
            components.merge(entry.getKey(), entry.getValue(), Integer::sum);
        }
    }
}
