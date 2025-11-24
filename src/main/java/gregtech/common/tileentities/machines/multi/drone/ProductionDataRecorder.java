package gregtech.common.tileentities.machines.multi.drone;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fluids.FluidStack;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ProductionDataRecorder {

    public int maxRecords;
    public final boolean ignoreNBT;
    private boolean active;

    private final ConcurrentLinkedQueue<RecordEntry> records = new ConcurrentLinkedQueue<>();
    private long contentHashCode = 0L;

    private final List<ItemStack> itemPalette = new ArrayList<>();
    private final Map<Integer, Integer> itemPaletteLookup = new HashMap<>();

    private final List<FluidStack> fluidPalette = new ArrayList<>();
    private final Map<Integer, Integer> fluidPaletteLookup = new HashMap<>();

    public ProductionDataRecorder(int maxRecords, boolean ignoreNBT) {
        this.maxRecords = Math.max(100, maxRecords);
        this.ignoreNBT = ignoreNBT;
        this.active = false;
    }

    private ProductionDataRecorder(int maxRecords, boolean ignoreNBT, boolean active, List<ItemStack> itemPalette,
        List<FluidStack> fluidPalette, ConcurrentLinkedQueue<RecordEntry> records, long hashCode) {
        this(maxRecords, ignoreNBT);
        this.active = active;
        this.records.addAll(records);
        this.contentHashCode = hashCode;
        this.itemPalette.addAll(itemPalette);
        this.fluidPalette.addAll(fluidPalette);

        for (int i = 0; i < this.itemPalette.size(); i++) {
            this.itemPaletteLookup.put(getItemStackHashCode(this.itemPalette.get(i)), i);
        }
        for (int i = 0; i < this.fluidPalette.size(); i++) {
            this.fluidPaletteLookup.put(getFluidStackHashCode(this.fluidPalette.get(i)), i);
        }
    }

    public void addRecord(long euConsumed, ItemStack[] items, FluidStack[] fluids) {
        if (!active) return;
        Map<Integer, Long> aggregatedItems = new HashMap<>();
        if (items != null) {
            for (ItemStack stack : items) {
                if (stack == null || stack.getItem() == null || stack.stackSize <= 0) continue;
                int itemIndex = findOrAddItemToPalette(stack);
                aggregatedItems.merge(itemIndex, (long) stack.stackSize, Long::sum);
            }
        }
        List<ItemProduction> itemProductions = new ArrayList<>();
        for (Map.Entry<Integer, Long> entry : aggregatedItems.entrySet()) {
            itemProductions.add(new ItemProduction(entry.getKey(), entry.getValue()));
        }

        Map<Integer, Long> aggregatedFluids = new HashMap<>();
        if (fluids != null) {
            for (FluidStack stack : fluids) {
                if (stack == null || stack.getFluid() == null || stack.amount <= 0) continue;
                int fluidIndex = findOrAddFluidToPalette(stack);
                aggregatedFluids.merge(fluidIndex, (long) stack.amount, Long::sum);
            }
        }
        List<FluidProduction> fluidProductions = new ArrayList<>();
        for (Map.Entry<Integer, Long> entry : aggregatedFluids.entrySet()) {
            fluidProductions.add(new FluidProduction(entry.getKey(), entry.getValue()));
        }

        if (euConsumed > 0 || !itemProductions.isEmpty() || !fluidProductions.isEmpty()) {
            RecordEntry newEntry = new RecordEntry(
                System.currentTimeMillis(),
                euConsumed,
                itemProductions,
                fluidProductions);
            records.add(newEntry);
            updateContentHashCode(newEntry.hashCode(), 0);

            while (records.size() > maxRecords) {
                RecordEntry removedEntry = records.poll();
                if (removedEntry != null) {
                    updateContentHashCode(0, removedEntry.hashCode());
                }
            }
        }
    }

    public boolean isEqual(ProductionDataRecorder other) {
        if (this == other) return true;
        if (other == null) return false;
        if (records.size() != other.records.size() || active != other.active) {
            return false;
        }
        return this.contentHashCode == other.contentHashCode;
    }

    private void updateContentHashCode(int addedHash, int removedHash) {
        if (removedHash != 0) {
            contentHashCode ^= (long) removedHash << 16;
        }
        if (addedHash != 0) {
            contentHashCode ^= addedHash;
            contentHashCode = Long.rotateLeft(contentHashCode, 1);
        }
    }

    public List<RecordEntry> getRecordEntries() {
        return new ArrayList<>(records);
    }

    public ItemStack getItemFromIndex(int index) {
        return (index >= 0 && index < itemPalette.size()) ? itemPalette.get(index) : null;
    }

    public FluidStack getFluidFromIndex(int index) {
        return (index >= 0 && index < fluidPalette.size()) ? fluidPalette.get(index) : null;
    }

    public void clear() {
        records.clear();
        itemPalette.clear();
        itemPaletteLookup.clear();
        fluidPalette.clear();
        fluidPaletteLookup.clear();
    }

    public void setActive(boolean active) {
        this.active = active;
        if (!active) clear();
    }

    public boolean isActive() {
        return active;
    }

    public long getEuConsumedIn(int ticks) {
        if (ticks == 0) return 0;
        long totalEu = 0;
        ArrayList<RecordEntry> entries = new ArrayList<>(records);

        if (ticks == -1) {
            for (RecordEntry entry : entries) {
                totalEu += entry.euConsumed;
            }
        } else {
            long timeHorizon = System.currentTimeMillis() - (ticks * 50L);
            for (int i = entries.size() - 1; i >= 0; i--) {
                RecordEntry entry = entries.get(i);
                if (entry.timestamp < timeHorizon) break;
                totalEu += entry.euConsumed;
            }
        }
        return -totalEu;
    }

    public LinkedHashMap<ItemStack, Long> getItemsProducedIn(int ticks) {
        if (ticks == 0) return new LinkedHashMap<>();
        Map<Integer, Long> aggregatedByIndex = new HashMap<>();
        ArrayList<RecordEntry> entries = new ArrayList<>(records);

        if (ticks == -1) {
            for (RecordEntry entry : entries) {
                for (ItemProduction item : entry.itemsProduced) {
                    aggregatedByIndex.merge(item.itemIndex, item.amount, Long::sum);
                }
            }
        } else {
            long timeHorizon = System.currentTimeMillis() - (ticks * 50L);
            for (int i = entries.size() - 1; i >= 0; i--) {
                RecordEntry entry = entries.get(i);
                if (entry.timestamp < timeHorizon) break;
                for (ItemProduction item : entry.itemsProduced) {
                    aggregatedByIndex.merge(item.itemIndex, item.amount, Long::sum);
                }
            }
        }

        HashMap<ItemStack, Long> result = new HashMap<>();
        for (Map.Entry<Integer, Long> entry : aggregatedByIndex.entrySet()) {
            ItemStack template = getItemFromIndex(entry.getKey());
            if (template != null) {
                ItemStack keyStack = template.copy();
                keyStack.stackSize = 1;
                result.put(keyStack, entry.getValue());
            }
        }
        return result.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByValue())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public LinkedHashMap<FluidStack, Long> getFluidsProducedIn(int ticks) {
        if (ticks == 0) return new LinkedHashMap<>();
        Map<Integer, Long> aggregatedByIndex = new HashMap<>();
        ArrayList<RecordEntry> entries = new ArrayList<>(records);

        if (ticks == -1) {
            for (RecordEntry entry : entries) {
                for (FluidProduction fluid : entry.fluidsProduced) {
                    aggregatedByIndex.merge(fluid.fluidIndex, fluid.amount, Long::sum);
                }
            }
        } else {
            long timeHorizon = System.currentTimeMillis() - (ticks * 50L);
            for (int i = entries.size() - 1; i >= 0; i--) {
                RecordEntry entry = entries.get(i);
                if (entry.timestamp < timeHorizon) break;
                for (FluidProduction fluid : entry.fluidsProduced) {
                    aggregatedByIndex.merge(fluid.fluidIndex, fluid.amount, Long::sum);
                }
            }
        }

        HashMap<FluidStack, Long> result = new HashMap<>();
        for (Map.Entry<Integer, Long> entry : aggregatedByIndex.entrySet()) {
            FluidStack template = getFluidFromIndex(entry.getKey());
            if (template != null) {
                FluidStack keyStack = template.copy();
                keyStack.amount = 1;
                result.put(keyStack, entry.getValue());
            }
        }
        return result.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByValue())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    private int findOrAddItemToPalette(ItemStack stack) {
        int hash = getItemStackHashCode(stack);
        Integer index = itemPaletteLookup.get(hash);
        if (index != null && areItemStacksEqual(itemPalette.get(index), stack)) return index;
        ItemStack newPaletteEntry = stack.copy();
        newPaletteEntry.stackSize = 1;
        int newIndex = itemPalette.size();
        itemPalette.add(newPaletteEntry);
        itemPaletteLookup.put(hash, newIndex);
        return newIndex;
    }

    private int findOrAddFluidToPalette(FluidStack stack) {
        int hash = getFluidStackHashCode(stack);
        Integer index = fluidPaletteLookup.get(hash);
        if (index != null && areFluidStacksEqual(fluidPalette.get(index), stack)) return index;
        FluidStack newPaletteEntry = stack.copy();
        newPaletteEntry.amount = 1;
        int newIndex = fluidPalette.size();
        fluidPalette.add(newPaletteEntry);
        fluidPaletteLookup.put(hash, newIndex);
        return newIndex;
    }

    private int getItemStackHashCode(ItemStack stack) {
        int h = stack.getItem()
            .hashCode() * 31 + stack.getItemDamage();
        if (!ignoreNBT && stack.hasTagCompound()) h = h * 31 + stack.getTagCompound()
            .hashCode();
        return h;
    }

    private boolean areItemStacksEqual(ItemStack s1, ItemStack s2) {
        if (s1.getItem() != s2.getItem() || s1.getItemDamage() != s2.getItemDamage()) return false;
        return ignoreNBT || ItemStack.areItemStackTagsEqual(s1, s2);
    }

    private int getFluidStackHashCode(FluidStack stack) {
        int h = stack.getFluid()
            .hashCode();
        if (!ignoreNBT && stack.tag != null) h = h * 31 + stack.tag.hashCode();
        return h;
    }

    private boolean areFluidStacksEqual(FluidStack s1, FluidStack s2) {
        return s1.isFluidEqual(s2) && (ignoreNBT || Objects.equals(s1.tag, s2.tag));
    }

    public static void serialize(PacketBuffer buf, ProductionDataRecorder data) throws IOException {
        if (data == null) {
            buf.writeBoolean(false);
            return;
        }
        buf.writeBoolean(true);
        buf.writeInt(data.maxRecords);
        buf.writeBoolean(data.ignoreNBT);
        buf.writeBoolean(data.active);
        buf.writeLong(data.contentHashCode);

        buf.writeVarIntToBuffer(data.itemPalette.size());
        for (ItemStack stack : data.itemPalette) buf.writeItemStackToBuffer(stack);
        buf.writeVarIntToBuffer(data.fluidPalette.size());
        for (FluidStack stack : data.fluidPalette)
            buf.writeNBTTagCompoundToBuffer(stack.writeToNBT(new NBTTagCompound()));

        List<RecordEntry> recordList = new ArrayList<>(data.records);
        buf.writeVarIntToBuffer(recordList.size());
        if (recordList.isEmpty()) return;

        long lastTimestamp = 0;
        for (RecordEntry entry : recordList) {
            buf.writeLong(lastTimestamp == 0 ? entry.timestamp : entry.timestamp - lastTimestamp);
            lastTimestamp = entry.timestamp;

            buf.writeLong(entry.euConsumed);

            buf.writeVarIntToBuffer(entry.itemsProduced.size());
            for (ItemProduction item : entry.itemsProduced) {
                buf.writeVarIntToBuffer(item.itemIndex);
                buf.writeLong(item.amount);
            }

            buf.writeVarIntToBuffer(entry.fluidsProduced.size());
            for (FluidProduction fluid : entry.fluidsProduced) {
                buf.writeVarIntToBuffer(fluid.fluidIndex);
                buf.writeLong(fluid.amount);
            }
        }
    }

    public static ProductionDataRecorder deserialize(PacketBuffer buf) throws IOException {
        if (!buf.readBoolean()) return null;
        int maxRecords = buf.readInt();
        boolean ignoreNBT = buf.readBoolean();
        boolean active = buf.readBoolean();
        long contentHashCode = buf.readLong();

        int itemPaletteSize = buf.readVarIntFromBuffer();
        List<ItemStack> itemPalette = new ArrayList<>(itemPaletteSize);
        for (int i = 0; i < itemPaletteSize; i++) itemPalette.add(buf.readItemStackFromBuffer());

        int fluidPaletteSize = buf.readVarIntFromBuffer();
        List<FluidStack> fluidPalette = new ArrayList<>(fluidPaletteSize);
        for (int i = 0; i < fluidPaletteSize; i++)
            fluidPalette.add(FluidStack.loadFluidStackFromNBT(buf.readNBTTagCompoundFromBuffer()));

        int recordCount = buf.readVarIntFromBuffer();
        ConcurrentLinkedQueue<RecordEntry> records = new ConcurrentLinkedQueue<>();
        if (recordCount > 0) {
            long lastTimestamp = 0;
            for (int i = 0; i < recordCount; i++) {
                lastTimestamp = (i == 0) ? buf.readLong() : lastTimestamp + buf.readLong();
                long eu = buf.readLong();

                int itemCount = buf.readVarIntFromBuffer();
                List<ItemProduction> items = new ArrayList<>(itemCount);
                for (int j = 0; j < itemCount; j++)
                    items.add(new ItemProduction(buf.readVarIntFromBuffer(), buf.readLong()));

                int fluidCount = buf.readVarIntFromBuffer();
                List<FluidProduction> fluids = new ArrayList<>(fluidCount);
                for (int j = 0; j < fluidCount; j++)
                    fluids.add(new FluidProduction(buf.readVarIntFromBuffer(), buf.readLong()));

                records.add(new RecordEntry(lastTimestamp, eu, items, fluids));
            }
        }
        return new ProductionDataRecorder(
            maxRecords,
            ignoreNBT,
            active,
            itemPalette,
            fluidPalette,
            records,
            contentHashCode);
    }

    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        ByteBuf buffer = Unpooled.buffer();
        try {
            serialize(new PacketBuffer(buffer), this);
            byte[] data = new byte[buffer.readableBytes()];
            buffer.readBytes(data);
            nbt.setByteArray("OptimizedData", data);
        } catch (IOException e) {
            nbt.setByteArray("OptimizedData", new byte[0]);
        } finally {
            buffer.release();
        }
        return nbt;
    }

    public static ProductionDataRecorder fromNBT(NBTTagCompound nbt) {
        if (nbt == null || !nbt.hasKey("OptimizedData")) return new ProductionDataRecorder(500, true);
        byte[] data = nbt.getByteArray("OptimizedData");
        if (data.length == 0) return new ProductionDataRecorder(500, true);

        ByteBuf buffer = Unpooled.wrappedBuffer(data);
        try {
            return deserialize(new PacketBuffer(buffer));
        } catch (IOException e) {
            return new ProductionDataRecorder(500, true);
        } finally {
            buffer.release();
        }
    }

    public static class ItemProduction {

        public final int itemIndex;
        public final long amount;

        public ItemProduction(int itemIndex, long amount) {
            this.itemIndex = itemIndex;
            this.amount = amount;
        }

        @Override
        public int hashCode() {
            return 31 * itemIndex + Long.hashCode(amount);
        }
    }

    public static class FluidProduction {

        public final int fluidIndex;
        public final long amount;

        public FluidProduction(int fluidIndex, long amount) {
            this.fluidIndex = fluidIndex;
            this.amount = amount;
        }

        @Override
        public int hashCode() {
            return 31 * fluidIndex + Long.hashCode(amount);
        }
    }

    public static class RecordEntry {

        public final long timestamp;
        public final long euConsumed;
        public final List<ItemProduction> itemsProduced;
        public final List<FluidProduction> fluidsProduced;
        private int cachedHashCode = 0;

        public RecordEntry(long timestamp, long euConsumed, List<ItemProduction> items, List<FluidProduction> fluids) {
            this.timestamp = timestamp;
            this.euConsumed = euConsumed;
            this.itemsProduced = items;
            this.fluidsProduced = fluids;
        }

        @Override
        public int hashCode() {
            if (cachedHashCode != 0) return cachedHashCode;
            int result = Long.hashCode(timestamp);
            result = 31 * result + Long.hashCode(euConsumed);
            result = 31 * result + itemsProduced.hashCode();
            result = 31 * result + fluidsProduced.hashCode();
            return this.cachedHashCode = result;
        }
    }
}
