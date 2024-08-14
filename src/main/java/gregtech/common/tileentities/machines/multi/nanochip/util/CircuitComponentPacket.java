package gregtech.common.tileentities.machines.multi.nanochip.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;

public class CircuitComponentPacket {

    private final Map<CircuitComponent, Integer> components = new HashMap<>();

    public CircuitComponentPacket(CircuitComponent component, int amount) {
        components.put(component, amount);
    }

    // Accept more circuit components from a new packet
    public void unifyWith(CircuitComponentPacket other) {
        for (Map.Entry<CircuitComponent, Integer> entry : other.components.entrySet()) {
            components.merge(entry.getKey(), entry.getValue(), Integer::sum);
        }
    }

    public Map<CircuitComponent, Integer> getComponents() {
        return components;
    }

    public List<ItemStack> getItemRepresentations() {
        ArrayList<ItemStack> stacks = new ArrayList<>();
        for (Map.Entry<CircuitComponent, Integer> entry : components.entrySet()) {
            ItemStack componentStack = entry.getKey().stack.copy();
            componentStack.stackSize = entry.getValue();
            stacks.add(componentStack);
        }
        return stacks;
    }
}
