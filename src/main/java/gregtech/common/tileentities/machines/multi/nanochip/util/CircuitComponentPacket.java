package gregtech.common.tileentities.machines.multi.nanochip.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import it.unimi.dsi.fastutil.Pair;

public class CircuitComponentPacket {

    private final Map<CircuitComponent, Long> components = new HashMap<>();

    public CircuitComponentPacket() {}

    public CircuitComponentPacket(CircuitComponent component, long amount) {
        components.put(component, amount);
    }

    public CircuitComponentPacket(NBTTagCompound nbt) {
        for (String key : nbt.func_150296_c()) {
            CircuitComponent component = CircuitComponent.valueOf(key);
            long amount = nbt.getLong(key);
            components.put(component, amount);
        }
    }

    public CircuitComponentPacket(List<Pair<ItemStack, Long>> items) {
        for (Pair<ItemStack, Long> item : items) {
            components.put(CircuitComponent.getFromFakeStackUnsafe(item.left()), (long) item.right());
        }
    }

    // Accept more circuit components from a new packet
    public void unifyWith(CircuitComponentPacket other) {
        for (Map.Entry<CircuitComponent, Long> entry : other.components.entrySet()) {
            components.merge(entry.getKey(), entry.getValue(), Long::sum);
        }
    }

    public NBTTagCompound writeToNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        for (Map.Entry<CircuitComponent, Long> entry : components.entrySet()) {
            String key = entry.getKey()
                .name();
            tag.setLong(key, entry.getValue());
        }
        return tag;
    }

    public Map<CircuitComponent, Long> getComponents() {
        return components;
    }

    public List<ItemStack> getItemRepresentations() {
        return getItemRepresentations(Integer.MAX_VALUE);
    }

    public List<ItemStack> getItemRepresentations(int limit) {
        ArrayList<ItemStack> stacks = new ArrayList<>();
        for (Map.Entry<CircuitComponent, Long> entry : components.entrySet()) {
            ItemStack componentStack = entry.getKey()
                .getFakeStack((int) Math.min(limit, entry.getValue()));
            stacks.add(componentStack);
        }
        return stacks;
    }
}
