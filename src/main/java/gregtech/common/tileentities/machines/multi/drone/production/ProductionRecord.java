package gregtech.common.tileentities.machines.multi.drone.production;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fluids.FluidStack;

public class ProductionRecord {

    private static final long[] DURATIONS = { 10000, 60000, 600000, 3600000, 86400000 };
    private static final int[] LIMITS = { 6, 10, 6, 24 };
    private static final String[] NAMES = { "10s", "1m", "10m", "1h", "24h" };

    private boolean active;
    private long revision = 0;
    private long lastUpdateTime = 0;
    private long lastPromotionTime = 0;

    private final StatsBundle[] display = IntStream.range(0, 5)
        .mapToObj(i -> new StatsBundle())
        .toArray(StatsBundle[]::new);
    private final StatsBundle[] acc = IntStream.range(0, 4)
        .mapToObj(i -> new StatsBundle())
        .toArray(StatsBundle[]::new);
    private final int[] counters = new int[4];
    private final StatsBundle currentTotal = new StatsBundle();

    public static ProductionRecord deserialize(PacketBuffer packetBuffer) throws IOException {
        return new ProductionRecord().readFromNBT(packetBuffer.readNBTTagCompoundFromBuffer());
    }

    public static void serialize(PacketBuffer packetBuffer, ProductionRecord productionRecord) throws IOException {
        packetBuffer.writeNBTTagCompoundToBuffer(productionRecord.writeToNBT());
    }

    public void clear() {
        for (StatsBundle b : display) b.clear();
        for (StatsBundle b : acc) b.clear();
        Arrays.fill(counters, 0);
        currentTotal.clear();
    }

    public void addStack(ItemStack[] stack) {
        if (stack != null) Arrays.stream(stack)
            .forEach(this::addStack);
    }

    public void addStack(ItemStack stack) {
        if (stack != null && stack.stackSize > 0) addValue(RecordUtil.packItem(stack), stack.stackSize);
    }

    public void addFluid(FluidStack[] stack) {
        if (stack != null) Arrays.stream(stack)
            .forEach(this::addFluid);
    }

    public void addFluid(FluidStack stack) {
        if (stack != null && stack.amount > 0) addValue(RecordUtil.packFluid(stack), stack.amount);
    }

    public void addEnergy(long amount) {
        if (amount != 0) addValue(RecordUtil.packEnergy(), amount);
    }

    private void addValue(long key, long amount) {
        currentTotal.merge(key, amount);
        display[0].merge(key, amount);
    }

    public void addRecord(long energy, ItemStack[] items, FluidStack[] fluids) {
        addEnergy(energy);
        addStack(items);
        addFluid(fluids);
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
        if (lastPromotionTime == 0) {
            lastPromotionTime = now;
            return;
        }
        if (now - lastPromotionTime >= 10000) {
            promote();
            lastPromotionTime = now;
            revision++;
        }
    }

    private void promote() {
        acc[0].merge(display[0]);
        for (int i = 0; i < LIMITS.length; i++) {
            counters[i]++;
            if (counters[i] < LIMITS[i]) break;
            display[i + 1].clear();
            display[i + 1].merge(acc[i]);
            if (i + 1 < acc.length) acc[i + 1].merge(acc[i]);
            acc[i].clear();
            counters[i] = 0;
        }
        display[0].clear();
    }

    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setLong("Rev", revision);
        nbt.setLong("LastUp", lastUpdateTime);
        nbt.setLong("LastProm", lastPromotionTime);
        nbt.setBoolean("Active", active);
        nbt.setIntArray("Cnt", counters);

        List<Long> dictionary = new ArrayList<>();
        Set<Long> seen = new HashSet<>();
        collectKeys(currentTotal, seen, dictionary);
        for (StatsBundle b : display) collectKeys(b, seen, dictionary);
        for (StatsBundle b : acc) collectKeys(b, seen, dictionary);

        long[] dictArr = dictionary.stream()
            .mapToLong(l -> l)
            .toArray();
        writeIntArrayFromLongs(nbt, "Dict", dictArr);

        nbt.setTag("Total", writeBundleCompressed(currentTotal, dictionary));
        for (int i = 0; i < display.length; i++)
            nbt.setTag("D" + NAMES[i], writeBundleCompressed(display[i], dictionary));
        for (int i = 0; i < acc.length; i++) nbt.setTag("A" + NAMES[i], writeBundleCompressed(acc[i], dictionary));
        return nbt;
    }

    public ProductionRecord readFromNBT(NBTTagCompound nbt) {
        this.revision = nbt.getLong("Rev");
        this.lastUpdateTime = nbt.getLong("LastUp");
        this.lastPromotionTime = nbt.getLong("LastProm");
        this.active = nbt.getBoolean("Active");
        int[] savedCounters = nbt.getIntArray("Cnt");
        if (savedCounters.length == counters.length) System.arraycopy(savedCounters, 0, counters, 0, counters.length);

        long[] dictArr = readIntArrayToLongs(nbt, "Dict");
        readBundleCompressed(nbt.getCompoundTag("Total"), dictArr, currentTotal);
        for (int i = 0; i < display.length; i++)
            readBundleCompressed(nbt.getCompoundTag("D" + NAMES[i]), dictArr, display[i]);
        for (int i = 0; i < acc.length; i++) readBundleCompressed(nbt.getCompoundTag("A" + NAMES[i]), dictArr, acc[i]);
        return this;
    }

    private void collectKeys(StatsBundle b, Set<Long> seen, List<Long> dict) {
        for (long k : b.data.keys()) if (seen.add(k)) dict.add(k);
    }

    private NBTTagCompound writeBundleCompressed(StatsBundle b, List<Long> dict) {
        NBTTagCompound tag = new NBTTagCompound();
        long[] keys = b.data.keys();
        int[] kIdx = new int[keys.length];
        long[] values = new long[keys.length];
        for (int i = 0; i < keys.length; i++) {
            kIdx[i] = dict.indexOf(keys[i]);
            values[i] = b.data.get(keys[i]);
        }
        tag.setIntArray("K", kIdx);
        writeIntArrayFromLongs(tag, "V", values);
        return tag;
    }

    private void readBundleCompressed(NBTTagCompound tag, long[] dict, StatsBundle target) {
        target.clear();
        int[] keys = tag.getIntArray("K");
        long[] values = readIntArrayToLongs(tag, "V");
        for (int i = 0; i < keys.length; i++) {
            if (keys[i] >= 0 && keys[i] < dict.length) target.data.put(dict[keys[i]], values[i]);
        }
    }

    private void writeIntArrayFromLongs(NBTTagCompound tag, String baseName, long[] data) {
        int[] high = new int[data.length], low = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            high[i] = (int) (data[i] >>> 32);
            low[i] = (int) (data[i] & 0xFFFFFFFFL);
        }
        tag.setIntArray(baseName + "H", high);
        tag.setIntArray(baseName + "L", low);
    }

    private long[] readIntArrayToLongs(NBTTagCompound tag, String baseName) {
        int[] high = tag.getIntArray(baseName + "H"), low = tag.getIntArray(baseName + "L");
        long[] res = new long[high.length];
        for (int i = 0; i < high.length; i++) res[i] = (((long) high[i]) << 32) | (low[i] & 0xFFFFFFFFL);
        return res;
    }

    public StatsBundle getStatsInDuration(long durationMillis) {
        if (durationMillis <= 0) return currentTotal.copy();
        for (int i = DURATIONS.length - 1; i >= 0; i--) {
            if (durationMillis >= DURATIONS[i]) {
                StatsBundle res = display[i].copy();
                res.setRevision(revision ^ durationMillis);
                return res;
            }
        }
        return display[0].copy();
    }

    public long getRevision() {
        return revision;
    }

    public boolean areEqual(ProductionRecord other) {
        return other != null && this.revision == other.revision;
    }
}
