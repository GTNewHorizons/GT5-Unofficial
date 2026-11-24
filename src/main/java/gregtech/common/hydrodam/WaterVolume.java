package gregtech.common.hydrodam;

import static com.gtnewhorizon.gtnhlib.util.CoordinatePacker2D.unpackChunkX;
import static com.gtnewhorizon.gtnhlib.util.CoordinatePacker2D.unpackChunkZ;

import javax.annotation.Nonnegative;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.gtnewhorizon.gtnhlib.datastructs.space.HashSet2D;
import com.gtnewhorizon.gtnhlib.datastructs.space.HashSet3D;
import com.gtnewhorizon.gtnhlib.functional.Consumer3D;
import com.gtnewhorizon.gtnhlib.util.CoordinatePacker;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;
import it.unimi.dsi.fastutil.longs.LongIterator;

/// A connected body of dam water. The water level is uniform across the volume: every cell below the top layer is
/// full, and the top layer is filled to {@link #getLevel()}.
///
/// This class only tracks the volume's bookkeeping. It never touches the world; see {@link DamVolumes} for that.
public final class WaterVolume {

    public static final double LITRES_PER_CELL = 1000;

    private final int id;
    private double litres;

    /// y -> the (x, z) positions of this volume's cells at that y.
    private final Int2ObjectSortedMap<HashSet2D> layers = new Int2ObjectRBTreeMap<>();

    /// Open cells next to this volume that water escapes through. A leak drains while the water level is above it.
    private final HashSet3D leaks = new HashSet3D();

    /// The total size of {@link #layers}. Cached because the level is read often (e.g. for every entity in water).
    private int cellCount;

    public WaterVolume(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public double getLitres() {
        return litres;
    }

    void setLitres(double litres) {
        this.litres = Math.max(0, litres);
    }

    public boolean isEmpty() {
        return layers.isEmpty();
    }

    public boolean contains(int x, int y, int z) {
        HashSet2D layer = layers.get(y);

        return layer != null && layer.contains(x, z);
    }

    /// @return true if the cell was not already part of this volume
    boolean addCell(int x, int y, int z) {
        boolean added = layers.computeIfAbsent(y, ignored -> new HashSet2D())
            .add(x, z);

        if (added) cellCount++;

        return added;
    }

    /// @return true if the cell was part of this volume
    boolean removeCell(int x, int y, int z) {
        HashSet2D layer = layers.get(y);

        if (layer == null || !layer.remove(x, z)) return false;

        cellCount--;

        if (layer.isEmpty()) layers.remove(y);

        return true;
    }

    /// Removes the top layer from the bookkeeping. Does not change the stored litres.
    ///
    /// Assumption: the volume is not empty.
    @NotNull
    HashSet2D removeTopLayer() {
        HashSet2D top = layers.remove(getTopY());

        cellCount -= top.size();

        return top;
    }

    /// Assumption: the volume is not empty.
    public int getTopY() {
        return layers.lastIntKey();
    }

    /// Assumption: the volume is not empty.
    public int getBottomY() {
        return layers.firstIntKey();
    }

    public int getLayerCount() {
        return layers.size();
    }

    /// Calls the consumer with every cell of this volume, bottom layer first. The volume must not be modified while
    /// iterating.
    public void forEachCell(@NotNull Consumer3D consumer) {
        for (Int2ObjectMap.Entry<HashSet2D> entry : layers.int2ObjectEntrySet()) {
            int y = entry.getIntKey();

            entry.getValue()
                .forEach((x, z) -> consumer.accept(x, y, z));
        }
    }

    /// The (x, z) positions at the given y. Callers must not modify the returned set.
    @Nullable
    public HashSet2D getLayer(int y) {
        return layers.get(y);
    }

    @Nonnegative
    public int getLayerArea(int y) {
        HashSet2D layer = layers.get(y);

        return layer == null ? 0 : layer.size();
    }

    @Nonnegative
    public int getCellCount() {
        return cellCount;
    }

    /// This volume's leaks. Callers must not modify the returned set.
    @NotNull
    public HashSet3D getLeaks() {
        return leaks;
    }

    /// @return true if the leak was not already known
    boolean addLeak(int x, int y, int z) {
        return leaks.add(x, y, z);
    }

    /// @return true if the leak was known
    boolean removeLeak(int x, int y, int z) {
        return leaks.remove(x, y, z);
    }

    /// Whether water is escaping through the given leak. Assumption: the volume is not empty.
    public boolean isLeakActive(int leakY) {
        return getLevel() > leakY;
    }

    /// The litres held by every layer below the top layer. Assumption: the volume is not empty.
    public double getCapacityBelowTop() {
        return (getCellCount() - getLayerArea(getTopY())) * LITRES_PER_CELL;
    }

    /// The litres this volume can hold without growing. Assumption: the volume is not empty.
    public double getCapacity() {
        return getCellCount() * LITRES_PER_CELL;
    }

    /// The fraction of the top layer that is filled, in [0, 1]. Assumption: the volume is not empty.
    public double getTopLayerFill() {
        double fill = (litres - getCapacityBelowTop()) / (getLayerArea(getTopY()) * LITRES_PER_CELL);

        return Math.max(0, Math.min(1, fill));
    }

    /// The absolute y of the water's surface. Assumption: the volume is not empty.
    public double getLevel() {
        return getTopY() + getTopLayerFill();
    }

    /// The litres stored in the given cell of this volume.
    ///
    /// Assumption: the cell is part of this volume.
    @Contract(pure = true)
    public double getLitresInCell(int y) {
        int topY = getTopY();

        if (y < topY) return LITRES_PER_CELL;
        if (y == topY) return getTopLayerFill() * LITRES_PER_CELL;
        return 0;
    }

    @NotNull
    public NBTTagCompound writeToNBT() {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setInteger("id", id);
        tag.setDouble("litres", litres);

        NBTTagList layerList = new NBTTagList();

        for (Int2ObjectMap.Entry<HashSet2D> entry : layers.int2ObjectEntrySet()) {
            NBTTagCompound layerTag = new NBTTagCompound();

            layerTag.setInteger("y", entry.getIntKey());
            layerTag.setTag("xz", new NBTTagIntArray(packLayer(entry.getValue())));

            layerList.appendTag(layerTag);
        }

        tag.setTag("layers", layerList);
        tag.setTag("leaks", new NBTTagIntArray(packCells(leaks)));

        return tag;
    }

    @NotNull
    public static WaterVolume readFromNBT(@NotNull NBTTagCompound tag) {
        WaterVolume volume = new WaterVolume(tag.getInteger("id"));

        volume.litres = tag.getDouble("litres");

        NBTTagList layerList = tag.getTagList("layers", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < layerList.tagCount(); i++) {
            NBTTagCompound layerTag = layerList.getCompoundTagAt(i);

            int y = layerTag.getInteger("y");
            int[] xz = layerTag.getIntArray("xz");

            for (int j = 0; j + 1 < xz.length; j += 2) {
                volume.addCell(xz[j], y, xz[j + 1]);
            }
        }

        int[] leaks = tag.getIntArray("leaks");

        for (int i = 0; i + 2 < leaks.length; i += 3) {
            volume.addLeak(leaks[i], leaks[i + 1], leaks[i + 2]);
        }

        return volume;
    }

    /// Packs cells into [x0, y0, z0, x1, y1, z1, ...].
    @Contract(pure = true)
    private static int[] packCells(HashSet3D cells) {
        int[] xyz = new int[cells.size() * 3];
        int i = 0;

        LongIterator iter = cells.iterator();

        while (iter.hasNext()) {
            long packed = iter.nextLong();

            xyz[i++] = CoordinatePacker.unpackX(packed);
            xyz[i++] = CoordinatePacker.unpackY(packed);
            xyz[i++] = CoordinatePacker.unpackZ(packed);
        }

        return xyz;
    }

    /// Packs a layer into [x0, z0, x1, z1, ...].
    @Contract(pure = true)
    private static int[] packLayer(HashSet2D layer) {
        int[] xz = new int[layer.size() * 2];
        int i = 0;

        LongIterator iter = layer.iterator();

        while (iter.hasNext()) {
            long packed = iter.nextLong();

            xz[i++] = unpackChunkX(packed);
            xz[i++] = unpackChunkZ(packed);
        }

        return xz;
    }
}
