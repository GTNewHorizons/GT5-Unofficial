package gregtech.common.tileentities.machines.multi.nanochip.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import org.apache.commons.lang3.tuple.MutablePair;

public class CircuitComponentPacket {

    private final Map<CircuitComponent, List<MutablePair<String, Long>>> components = new HashMap<>();

    public CircuitComponentPacket() {}

    public CircuitComponentPacket(CircuitComponent component, long amount, String customName) {
        List<MutablePair<String, Long>> entries = new ArrayList<>();
        entries.add(MutablePair.of(customName, amount));
        components.put(component, entries);
    }

    public CircuitComponentPacket(NBTTagCompound nbt) {
        for (String key : nbt.func_150296_c()) {
            CircuitComponent component = CircuitComponent.valueOf(key);

            // Handle legacy NBT data
            if (nbt.func_150299_b(key) == Constants.NBT.TAG_LONG) {

                long amount = nbt.getLong(key);
                List<MutablePair<String, Long>> entries = new ArrayList<>();
                entries.add(MutablePair.of(null, amount));
                components.put(component, entries);
                continue;
            }

            // Handle new NBT data
            NBTTagCompound subTag = nbt.getCompoundTag(key);
            List<MutablePair<String, Long>> entries = new ArrayList<>();
            if (subTag.hasKey("Unnamed", Constants.NBT.TAG_LONG)) {
                entries.add(MutablePair.of(null, subTag.getLong("Unnamed")));
            }
            if (subTag.hasKey("NamedList", Constants.NBT.TAG_LIST)) {
                NBTTagList list = subTag.getTagList("NamedList", Constants.NBT.TAG_COMPOUND);
                for (int i = 0; i < list.tagList.size(); i++) {
                    NBTTagCompound namedSubTag = list.getCompoundTagAt(i);
                    entries.add(MutablePair.of(namedSubTag.getString("Name"), namedSubTag.getLong("Amount")));
                }
            }
            components.put(component, entries);
        }
    }

    public CircuitComponentPacket(Map<CircuitComponent, List<MutablePair<String, Long>>> components) {
        this.components.putAll(components);
    }

    // Accept more circuit components from a new packet
    public void unifyWith(CircuitComponentPacket other) {
        if (other == null) return;
        for (var entry : other.components.entrySet()) {
            CircuitComponent cc = entry.getKey();
            List<MutablePair<String, Long>> otherAmounts = entry.getValue();

            if (!this.components.containsKey(cc)) {
                this.components.put(cc, otherAmounts);
            } else for (var pair : otherAmounts) {
                unifySingle(cc, pair);
            }
        }
    }

    private void unifySingle(CircuitComponent cc, MutablePair<String, Long> pair) {
        List<MutablePair<String, Long>> amounts = this.components.get(cc);
        for (var myPair : amounts) {
            String myName = myPair.getLeft();
            String otherName = pair.getLeft();

            if (myName == null) {
                if (otherName == null) {
                    // Add to unnamed stack
                    myPair.setRight(myPair.getRight() + pair.getRight());
                    return;
                }
            } else if (myName.equals(otherName)) {
                // Add to existing named stack
                myPair.setRight(myPair.getRight() + pair.getRight());
                return;
            }
        }
        // Nothing to merge, simply add it instead
        amounts.add(pair);
    }

    public NBTTagCompound writeToNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        for (var entry : components.entrySet()) {
            String key = entry.getKey()
                .name();

            NBTTagCompound subTag = new NBTTagCompound();
            NBTTagList list = new NBTTagList();
            for (var pair : entry.getValue()) {

                if (pair.getLeft() == null) {
                    subTag.setLong("Unnamed", pair.getRight());
                } else {
                    NBTTagCompound namedSubTag = new NBTTagCompound();
                    namedSubTag.setString("Name", pair.getLeft());
                    namedSubTag.setLong("Amount", pair.getRight());
                    list.appendTag(namedSubTag);
                }
            }
            if (!list.tagList.isEmpty()) {
                subTag.setTag("NamedList", list);
            }
            tag.setTag(key, subTag);
        }
        return tag;
    }

    public Map<CircuitComponent, List<MutablePair<String, Long>>> getComponents() {
        return components;
    }

    public List<ItemStack> getItemRepresentations() {
        return getItemRepresentations(Integer.MAX_VALUE);
    }

    public List<ItemStack> getItemRepresentations(int limit) {
        ArrayList<ItemStack> stacks = new ArrayList<>();
        for (var entry : components.entrySet()) {
            CircuitComponent cc = entry.getKey();
            List<MutablePair<String, Long>> amounts = entry.getValue();

            for (var pair : amounts) {
                String name = pair.getLeft();
                long amount = pair.getRight();
                ItemStack componentStack = cc.getFakeStack((int) Math.min(limit, amount));
                if (name != null) {
                    componentStack.setStackDisplayName(name);
                }
                stacks.add(componentStack);
            }
        }
        return stacks;
    }

    /**
     * Get total amount of CCs of this type in the packet, ignoring any custom names.
     */
    public long getAmount(CircuitComponent cc) {
        List<MutablePair<String, Long>> amounts = components.get(cc);
        if (amounts == null || amounts.isEmpty()) return 0;
        return amounts.stream()
            .mapToLong(MutablePair::getRight)
            .sum();
    }

    /**
     * Get the amount of CCs of this type in the packet that are named a certain way.
     */
    public long getNamedAmount(CircuitComponent cc, String name) {
        List<MutablePair<String, Long>> amounts = components.get(cc);
        if (amounts == null || amounts.isEmpty()) return 0;
        for (var pair : amounts) {
            String amountName = pair.getLeft();
            if (amountName == null) {
                if (name == null) return pair.getRight();
            } else if (amountName.equals(name)) {
                return pair.getRight();
            }
        }
        return 0;
    }

    public void consume(CircuitComponent cc, int amount) {
        List<MutablePair<String, Long>> amounts = components.get(cc);
        if (amounts == null || amounts.isEmpty()) return;
        Iterator<MutablePair<String, Long>> itr = amounts.iterator();
        while (itr.hasNext() && amount > 0) {
            var pair = itr.next();
            if (pair.getRight() <= amount) {
                // Drain all of this type, continue on
                amount -= pair.getRight();
                itr.remove();
            } else {
                // Otherwise remove from this pair and exit
                pair.setRight(pair.getRight() - amount);
                amount = 0;
            }
        }
    }

    public boolean isEmpty() {
        return components.isEmpty();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof CircuitComponentPacket o)) return false;
        if (!this.components.keySet()
            .equals(o.components.keySet())) return false;

        for (var entry : this.components.entrySet()) {
            var thisAmounts = entry.getValue();
            var thatAmounts = o.components.get(entry.getKey());
            if (thisAmounts.size() != thatAmounts.size()) return false;

            var thisAmountsSet = new HashSet<>(thisAmounts);
            var thatAmountsSet = new HashSet<>(thatAmounts);
            if (!thisAmountsSet.equals(thatAmountsSet)) return false;
        }
        return true;
    }
}
