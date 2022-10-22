package gregtech.api.util;

import net.minecraft.block.Block;

public class GT_BlockSet {
	private final GT_BlockMap<Object> backing = new GT_BlockMap<>();

	public boolean add(Block block, byte meta) {
		return backing.put(block, meta, this) != this;
	}

	public boolean contains(Block block, byte meta) {
		return backing.get(block, meta) == this;
	}

	public boolean remove(Block block, byte meta) {
		return backing.remove(block, meta) == this;
	}

	public int size() {
		return backing.size();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		GT_BlockSet that = (GT_BlockSet) o;

		return backing.equals(that.backing);
	}

	@Override
	public int hashCode() {
		return backing.hashCode();
	}
}
