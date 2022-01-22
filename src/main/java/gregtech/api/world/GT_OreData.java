package gregtech.api.world;

import gregtech.api.util.GT_ChunkAssociatedData;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


/**
 * Stores ore data.
 */
@ParametersAreNonnullByDefault
public class GT_OreData extends GT_ChunkAssociatedData<GT_OreData.Data> {
	private GT_OreData() {
		super("Ore", Data.class, 9, (byte) 0, true);
	}

	@Override
	protected void writeElement(DataOutput output, Data element, World world, int chunkX, int chunkZ) throws IOException {

	}

	@Override
	protected Data readElement(DataInput input, int version, World world, int chunkX, int chunkZ) throws IOException {
		return null;
	}

	@Override
	protected Data createElement(World world, int chunkX, int chunkZ) {
		return null;
	}

	private static class InChunkCoord {
		private final byte x, y, z;

		private InChunkCoord(int x, int y, int z) {
			this.x = (byte) (x & 0xf);
			this.y = (byte) (y & 0xffff);
			this.z = (byte) (z & 0xf);
		}

		private InChunkCoord(int compacted) {
			this.x = (byte) ((compacted >> 4) & 0xf);
			this.y = (byte) ((compacted >> 8) & 0xffff);
			this.z = (byte) (compacted & 0xf);
		}

		public byte getX() {
			return x;
		}

		public byte getY() {
			return y;
		}

		public byte getZ() {
			return z;
		}

		public short getCompactedYXZ() {
			return (short) (y << 8 | x << 4 | z);
		}
	}

	/**
	 * Used to serialize the coord of a single type of ore.
	 */
	private enum PerOreStorageFormat {
		// used for small ores, i.e. no spatial locality
		Triples {
			@Override
			void write(DataOutput output, List<InChunkCoord> coords) throws IOException {
				output.writeInt(coords.size());
				for (InChunkCoord coord : coords) {
					output.writeShort(coord.getCompactedYXZ());
				}
			}

			@Override
			void read(DataInput input, Consumer<? super InChunkCoord> sink) throws IOException {
				int count = input.readInt();
				for (int i = 0; i < count; i++) {
					sink.accept(new InChunkCoord(input.readShort()));
				}
			}
		},
		// used for normal ores, i.e. with great Y axis correlation, and tend to have >12.5% slot filled in each Y layer
		YAndBitmap {
			@Override
			void write(DataOutput output, List<InChunkCoord> coords) throws IOException {
				Map<Byte, BitSet> cache = new HashMap<>();
				for (InChunkCoord coord : coords)
					cache.computeIfAbsent(coord.getY(), ignored -> new BitSet(16 * 16)).set(coord.getX() + coord.getZ() * 16);
				output.writeByte(cache.size());
				for (Map.Entry<Byte, BitSet> entry : cache.entrySet()) {
					output.writeByte(entry.getKey());
					long[] longs = entry.getValue().toLongArray();
					int i;
					int longsLength = longs.length;
					for (i = 0; i < longsLength; i++) output.writeLong(longs[i]);
					for (; i < 4; i++) output.writeLong(0);
				}
			}

			@Override
			void read(DataInput input, Consumer<? super InChunkCoord> sink) throws IOException {
				int ys = input.readUnsignedByte();
				byte[] buffer = new byte[32];
				for (int i = 0; i < ys; i++) {
					int y = input.readUnsignedByte();
					input.readFully(buffer, 0, 32);
					BitSet bits = BitSet.valueOf(buffer);
					bits.stream()
							.mapToObj(d -> new InChunkCoord(d & 0xf, y, d >>> 4))
							.forEach(sink);
				}
			}
		};

		byte index() {
			return (byte) ordinal();
		}

		abstract void write(DataOutput output, List<InChunkCoord> coords) throws IOException;

		abstract void read(DataInput input, Consumer<? super InChunkCoord> sink) throws IOException;

		static PerOreStorageFormat choose(List<Vec3> coords) {
			return Triples;
		}

		static int composeShort(int higherByte, int lowerByte) {
			return (higherByte & 0xff) << 8 | (lowerByte & 0xff);
		}
	}

	protected static class Data implements GT_ChunkAssociatedData.IData {
		private int a;

		@Override
		public boolean isSameAsDefault() {
			return true;
		}
	}


}
