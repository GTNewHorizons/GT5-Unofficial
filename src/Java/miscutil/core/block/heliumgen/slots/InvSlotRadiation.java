package miscutil.core.block.heliumgen.slots;

import ic2.core.block.invslot.InvSlot;
import miscutil.core.block.heliumgen.tileentity.TileEntityHeliumGenerator;
import net.minecraft.item.ItemStack;

public class InvSlotRadiation extends InvSlot
{
	public InvSlotRadiation(TileEntityHeliumGenerator base, String name1, int oldStartIndex1, int count)
	{
		super(base, name1, oldStartIndex1, InvSlot.Access.IO, count);

		setStackSizeLimit(1);
	}

	@Override
	public boolean accepts(ItemStack itemStack)
	{
		return ((TileEntityHeliumGenerator)this.base).isUsefulItem(itemStack, true);
	}

	@Override
	public int size()
	{
		//Utils.LOG_INFO("InvSlotRadiation/Size");
		return 3 * 6;
	}

	public int rawSize()
	{
		return super.size();
	}

	@Override
	public ItemStack get(int index)
	{
		return super.get(mapIndex(index));
	}

	public ItemStack get(int x, int y)
	{
		return super.get(y * 9 + x);
	}

	@Override
	public void put(int index, ItemStack content)
	{
		super.put(mapIndex(index), content);
	}

	public void put(int x, int y, ItemStack content)
	{
		super.put(y * 9 + x, content);
	}

	private int mapIndex(int index)
	{
		int size = size();
		int cols = size / 6;
		if (index < size)
		{
			int row = index / cols;
			int col = index % cols;

			return row * 9 + col;
		}
		index -= size;
		int remCols = 9 - cols;

		int row = index / remCols;
		int col = cols + index % remCols;

		return row * 9 + col;
	}

	private final int rows = 6;
	private final int maxCols = 9;
}
