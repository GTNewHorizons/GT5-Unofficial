package gregtech.api.objects;

import java.util.Iterator;

import com.gtnewhorizon.gtnhlib.blockpos.BlockPos;
import com.gtnewhorizon.gtnhlib.util.CoordinatePacker;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongListIterator;

public class CoordinateList extends LongArrayList {

    public BlockPos getPos(int i) {
        return unpack(new BlockPos(), getLong(i));
    }

    public BlockPos removePos(int i) {
        return unpack(new BlockPos(), removeLong(i));
    }

    public void add(int x, int y, int z) {
        add(CoordinatePacker.pack(x, y, z));
    }

    public void add(BlockPos pos) {
        add(pack(pos));
    }

    public Iterator<BlockPos> fastPosIterator() {
        LongListIterator iter = super.iterator();

        BlockPos instance = new BlockPos();

        return new Iterator<>() {

            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public BlockPos next() {
                return unpack(instance, iter.nextLong());
            }
        };
    }

    public Iterator<BlockPos> posIterator() {
        LongListIterator iter = super.iterator();

        return new Iterator<>() {

            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public BlockPos next() {
                return unpack(new BlockPos(), iter.nextLong());
            }
        };
    }

    public Iterable<BlockPos> posIterable() {
        return this::fastPosIterator;
    }

    private static BlockPos unpack(BlockPos instance, long coord) {
        instance.x = CoordinatePacker.unpackX(coord);
        instance.y = CoordinatePacker.unpackY(coord);
        instance.z = CoordinatePacker.unpackZ(coord);
        return instance;
    }

    private static long pack(BlockPos instance) {
        return CoordinatePacker.pack(instance.x, instance.y, instance.z);
    }

}
