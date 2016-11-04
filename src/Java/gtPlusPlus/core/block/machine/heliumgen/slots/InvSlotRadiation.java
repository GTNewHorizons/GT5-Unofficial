package gtPlusPlus.core.block.machine.heliumgen.slots;

import gtPlusPlus.core.block.machine.heliumgen.tileentity.TileEntityHeliumGenerator;
import ic2.core.block.invslot.InvSlot;
import net.minecraft.item.ItemStack;

public class InvSlotRadiation extends InvSlot {
	private final int	rows	= 6;

	private final int	maxCols	= 9;

	public InvSlotRadiation(final TileEntityHeliumGenerator base, final String name1, final int oldStartIndex1,
			final int count) {
		super(base, name1, oldStartIndex1, InvSlot.Access.IO, count);

		this.setStackSizeLimit(1);
	}

	@Override
	public boolean accepts(final ItemStack itemStack) {
		return ((TileEntityHeliumGenerator) this.base).isUsefulItem(itemStack, true);
	}

	@Override
	public ItemStack get(final int index) {
		return super.get(this.mapIndex(index));
	}

	public ItemStack get(final int x, final int y) {
		return super.get(y * 9 + x);
	}

	private int mapIndex(int index) {
		final int size = this.size();
		final int cols = size / 6;
		if (index < size) {
			final int row = index / cols;
			final int col = index % cols;

			return row * 9 + col;
		}
		index -= size;
		final int remCols = 9 - cols;

		final int row = index / remCols;
		final int col = cols + index % remCols;

		return row * 9 + col;
	}

	public void put(final int x, final int y, final ItemStack content) {
		super.put(y * 9 + x, content);
	}

	@Override
	public void put(final int index, final ItemStack content) {
		super.put(this.mapIndex(index), content);
	}

	public int rawSize() {
		return super.size();
	}
	@Override
	public int size() {
		// Utils.LOG_INFO("InvSlotRadiation/Size");
		return 3 * 6;
	}
}
