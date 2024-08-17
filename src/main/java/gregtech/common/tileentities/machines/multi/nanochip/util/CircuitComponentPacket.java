package gregtech.common.tileentities.machines.multi.nanochip.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class CircuitComponentPacket {

    private final Map<CircuitComponent, Integer> components = new HashMap<>();

    public CircuitComponentPacket(CircuitComponent component, int amount) {
        components.put(component, amount);
    }

    public CircuitComponentPacket(NBTTagCompound nbt) {
        for (String key : nbt.func_150296_c()) {
            CircuitComponent component = CircuitComponent.valueOf(key);
            int amount = nbt.getInteger(key);
            components.put(component, amount);
        }
    }

    // Accept more circuit components from a new packet
    public void unifyWith(CircuitComponentPacket other) {
        for (Map.Entry<CircuitComponent, Integer> entry : other.components.entrySet()) {
            components.merge(entry.getKey(), entry.getValue(), Integer::sum);
        }
    }

    public NBTTagCompound writeToNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        for (Map.Entry<CircuitComponent, Integer> entry : components.entrySet()) {
            String key = entry.getKey()
                .name();
            tag.setInteger(key, entry.getValue());
        }
        return tag;
    }

    public Map<CircuitComponent, Integer> getComponents() {
        return components;
    }

    public List<ItemStack> getItemRepresentations() {
        ArrayList<ItemStack> stacks = new ArrayList<>();
        for (Map.Entry<CircuitComponent, Integer> entry : components.entrySet()) {
            ItemStack componentStack = entry.getKey()
                .getFakeStack(entry.getValue());
            stacks.add(componentStack);
        }
        return stacks;
    }
}
