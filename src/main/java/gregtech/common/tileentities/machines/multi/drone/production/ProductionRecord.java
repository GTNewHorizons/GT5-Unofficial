package gregtech.common.tileentities.machines.multi.drone.production;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class ProductionRecord {

    private boolean active;
    private long revision = 0;
    private long lastUpdateTime = 0;
    private final StatsBundle currentTotal = new StatsBundle();
    private final LinkedList<Snapshot> history = new LinkedList<>();

    public static ProductionRecord deserialize(PacketBuffer packetBuffer) throws IOException {
        return new ProductionRecord().readFromNBT(packetBuffer.readNBTTagCompoundFromBuffer());
    }

    public static void serialize(PacketBuffer packetBuffer, ProductionRecord productionRecord) throws IOException {
        packetBuffer.writeNBTTagCompoundToBuffer(productionRecord.writeToNBT());
    }

    public void clear() {
        currentTotal.clear();
        history.clear();
    }

    private static class Snapshot {

        long timestamp;
        StatsBundle data;

        public Snapshot(long t, StatsBundle d) {
            this.timestamp = t;
            this.data = d.copy();
        }
    }

    public void addStack(ItemStack[] stack) {
        Arrays.stream(stack)
            .forEach(this::addStack);
    }

    public void addStack(ItemStack stack) {
        if (stack == null || stack.stackSize == 0) return;
        addStack(stack, stack.stackSize);
    }

    public void addStack(ItemStack stack, long amount) {
        if (stack == null || amount == 0) return;
        currentTotal.merge(RecordUtil.packItem(stack), amount);
    }

    public void addFluid(FluidStack[] stack) {
        Arrays.stream(stack)
            .forEach(this::addFluid);
    }

    public void addFluid(FluidStack stack) {
        if (stack == null || stack.amount == 0) return;
        addFluid(stack, stack.amount);
    }

    public void addFluid(FluidStack stack, long amount) {
        if (stack == null || amount == 0) return;
        currentTotal.merge(RecordUtil.packFluid(stack), amount);
    }

    public void addEnergy(long amount) {
        if (amount == 0) return;
        currentTotal.merge(RecordUtil.packEnergy(), amount);
    }

    public void addRecord(long energy, ItemStack[] items, FluidStack[] fluids) {
        addEnergy(energy);
        if (items != null && items.length > 0) addStack(items);
        if (fluids != null && fluids.length > 0) addFluid(fluids);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void update() {
        long now = System.currentTimeMillis();
        lastUpdateTime = now;

        history.addFirst(new Snapshot(now, currentTotal));

        revision++;

        pruneHistory(now);
    }

    private void pruneHistory(long now) {
        Iterator<Snapshot> it = history.iterator();

        long lastMinuteBucket = -1;
        long lastTenMinBucket = -1;

        while (it.hasNext()) {
            Snapshot curr = it.next();
            long age = now - curr.timestamp;

            if (age < 0) {
                it.remove();
                continue;
            }

            boolean keep = false;

            if (age < 60 * 1000) {
                keep = true;

                lastMinuteBucket = curr.timestamp / 60000;
                lastTenMinBucket = curr.timestamp / (10 * 60 * 1000);
            }

            else if (age < 60 * 60 * 1000) {

                long currMinuteBucket = curr.timestamp / 60000;

                if (currMinuteBucket != lastMinuteBucket) {
                    keep = true;
                    lastMinuteBucket = currMinuteBucket;

                    lastTenMinBucket = curr.timestamp / (10 * 60 * 1000);
                }
            }

            else if (age < 24 * 60 * 60 * 1000) {

                long currTenMinBucket = curr.timestamp / (10 * 60 * 1000);

                if (currTenMinBucket != lastTenMinBucket) {
                    keep = true;
                    lastTenMinBucket = currTenMinBucket;
                }
            }

            if (!keep) {
                it.remove();
            }
        }
    }

    public boolean areEqual(ProductionRecord other) {
        return other != null && this.revision == other.revision;
    }

    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setLong("Rev", revision);
        nbt.setLong("LastUp", lastUpdateTime);
        nbt.setBoolean("Active", active);

        List<Long> dictionary = new ArrayList<>();
        Set<Long> seen = new HashSet<>();

        collectKeys(currentTotal, seen, dictionary);
        for (Snapshot snap : history) collectKeys(snap.data, seen, dictionary);

        long[] dictArr = new long[dictionary.size()];
        for (int i = 0; i < dictionary.size(); i++) dictArr[i] = dictionary.get(i);
        writeIntArrayFromLongs(nbt, "Dict", dictArr);

        nbt.setTag("Now", writeBundleCompressed(currentTotal, dictionary));

        NBTTagList histList = new NBTTagList();
        for (Snapshot snap : history) {
            NBTTagCompound s = new NBTTagCompound();
            s.setLong("T", snap.timestamp);
            s.setTag("D", writeBundleCompressed(snap.data, dictionary));
            histList.appendTag(s);
        }
        nbt.setTag("Hist", histList);
        return nbt;
    }

    public ProductionRecord readFromNBT(NBTTagCompound nbt) {
        this.revision = nbt.getLong("Rev");
        this.lastUpdateTime = nbt.getLong("LastUp");
        this.active = nbt.getBoolean("Active");

        long[] dictArr = readIntArrayToLongs(nbt, "Dict");

        this.currentTotal.data.clear();
        readBundleCompressed(nbt.getCompoundTag("Now"), dictArr, this.currentTotal);

        this.history.clear();
        NBTTagList histList = nbt.getTagList("Hist", 10);
        for (int i = 0; i < histList.tagCount(); i++) {
            NBTTagCompound s = histList.getCompoundTagAt(i);
            long ts = s.getLong("T");
            StatsBundle b = new StatsBundle();
            readBundleCompressed(s.getCompoundTag("D"), dictArr, b);
            this.history.add(new Snapshot(ts, b));
        }
        return this;
    }

    private void collectKeys(StatsBundle b, Set<Long> seen, List<Long> dict) {
        for (long k : b.data.keys()) {
            if (seen.add(k)) dict.add(k);
        }
    }

    private NBTTagCompound writeBundleCompressed(StatsBundle b, List<Long> dict) {
        NBTTagCompound tag = new NBTTagCompound();
        int size = b.data.size();
        int[] keys = new int[size];
        long[] values = new long[size];

        int idx = 0;
        for (long key : b.data.keys()) {
            keys[idx] = dict.indexOf(key);
            values[idx] = b.data.get(key);
            idx++;
        }
        tag.setIntArray("K", keys);
        writeIntArrayFromLongs(tag, "V", values);
        return tag;
    }

    private void readBundleCompressed(NBTTagCompound tag, long[] dict, StatsBundle target) {
        int[] keys = tag.getIntArray("K");
        long[] values = readIntArrayToLongs(tag, "V");
        for (int i = 0; i < keys.length; i++) {
            if (keys[i] >= 0 && keys[i] < dict.length) {
                target.data.put(dict[keys[i]], values[i]);
            }
        }
    }

    private void writeIntArrayFromLongs(NBTTagCompound tag, String baseName, long[] data) {
        int[] high = new int[data.length];
        int[] low = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            high[i] = (int) (data[i] >>> 32);
            low[i] = (int) (data[i] & 0xFFFFFFFFL);
        }
        tag.setIntArray(baseName + "H", high);
        tag.setIntArray(baseName + "L", low);
    }

    private long[] readIntArrayToLongs(NBTTagCompound tag, String baseName) {
        int[] high = tag.getIntArray(baseName + "H");
        int[] low = tag.getIntArray(baseName + "L");
        long[] res = new long[high.length];
        for (int i = 0; i < high.length; i++) {
            res[i] = (((long) high[i]) << 32) | (low[i] & 0xFFFFFFFFL);
        }
        return res;
    }

    public long getRevision() {
        return revision;
    }

    public StatsBundle getStatsInDuration(long durationMillis) {
        StatsBundle res;
        if (durationMillis <= 0) {
            res = currentTotal.copy();
        } else {
            long targetTime = lastUpdateTime - durationMillis;

            Snapshot closest = null;
            for (Snapshot snap : history) {
                if (snap.timestamp <= targetTime) {
                    closest = snap;
                    break;
                }
            }
            StatsBundle startData = (closest != null) ? closest.data : new StatsBundle();
            res = currentTotal.diff(startData);
        }
        res.setRevision(revision ^ durationMillis);
        return res;
    }

    public Map<ItemStack, Long> getItemStatsIn(long durationMillis) {
        Map<ItemStack, Long> result = new HashMap<>();
        StatsBundle bundle = getStatsInDuration(durationMillis);

        for (long key : bundle.data.keys()) {
            long value = bundle.data.get(key);
            if (RecordUtil.isItem(key)) {
                Item item = Item.getItemById(RecordUtil.getId(key));
                if (item != null) {
                    ItemStack is = new ItemStack(item, 1, RecordUtil.getMeta(key));
                    result.put(is, value);
                }
            }
        }

        return result.entrySet()
            .stream()
            .sorted(
                Map.Entry.<ItemStack, Long>comparingByValue()
                    .reversed())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public Map<FluidStack, Long> getFluidStatsIn(long durationMillis) {
        Map<FluidStack, Long> result = new HashMap<>();
        StatsBundle bundle = getStatsInDuration(durationMillis);

        for (long key : bundle.data.keys()) {
            long value = bundle.data.get(key);
            if (RecordUtil.isFluid(key)) {
                Fluid fluid = FluidRegistry.getFluid(RecordUtil.getId(key));
                if (fluid != null) {
                    FluidStack fs = new FluidStack(fluid, 1);
                    result.put(fs, value);
                }
            }
        }
        return result.entrySet()
            .stream()
            .sorted(
                Map.Entry.<FluidStack, Long>comparingByValue()
                    .reversed())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public Long getEnergyStatsIn(long durationMillis) {
        long result = 0;
        StatsBundle bundle = getStatsInDuration(durationMillis);
        for (long key : bundle.data.keys()) {
            long value = bundle.data.get(key);
            if (RecordUtil.isEnergy(key)) {
                result += value;
            }
        }
        return -result;
    }
}
