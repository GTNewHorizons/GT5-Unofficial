package gtPlusPlus.core.block.base;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;

public class MetaBlock extends MultiTextureBlock {

	protected MetaBlock(final String unlocalizedName, final Material material, final SoundType soundType) {
		super(unlocalizedName, material, soundType);
	}

	@Override
	public int damageDropped(final int meta) {
		return meta;
	}

	@Override
	public void getSubBlocks(final Item item, final CreativeTabs tab, final List list) {
		for (int i = 0; i < 6; i ++) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	public boolean canCreatureSpawn(final EnumCreatureType type, final IBlockAccess world, final int x, final int y, final int z) {
		return false;
	}

}