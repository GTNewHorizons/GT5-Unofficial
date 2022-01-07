package gtPlusPlus.core.item.base.itemblock;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemBlockDoor extends ItemBlock {
	@SideOnly(Side.CLIENT)
	private IIcon field_150938_b;

	public ItemBlockDoor(Block p_i45328_1_) {
		super(p_i45328_1_);
	}

	/**
	 * Callback for item usage. If the item does something special on right
	 * clicking, he will have one of those. Return True if something happen and
	 * false if it don't. This is for ITEMS, not BLOCKS
	 */
	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4,
			int par5, int par6, int par7, float par8, float par9, float par10) {
		if (par7 != 1) {
			return false;
		} else {
			++par5;
			Block block;

			block = field_150939_a;

			if (par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack)
					&& par2EntityPlayer.canPlayerEdit(par4, par5 + 1, par6, par7, par1ItemStack)) {
				if (!block.canPlaceBlockAt(par3World, par4, par5, par6)) {
					return false;
				} else {
					int i1 = MathHelper.floor_double(
							(double) ((par2EntityPlayer.rotationYaw + 180.0F) * 4.0F / 360.0F) - 0.5D) & 3;
					byte b0 = 0;
					byte b1 = 0;

					if (i1 == 0) {
						b1 = 1;
					}

					if (i1 == 1) {
						b0 = -1;
					}

					if (i1 == 2) {
						b1 = -1;
					}

					if (i1 == 3) {
						b0 = 1;
					}

					int i2 = (par3World.getBlock(par4 - b0, par5, par6 - b1).isNormalCube() ? 1 : 0)
							+ (par3World.getBlock(par4 - b0, par5 + 1, par6 - b1).isNormalCube() ? 1 : 0);
					int j1 = (par3World.getBlock(par4 + b0, par5, par6 + b1).isNormalCube() ? 1 : 0)
							+ (par3World.getBlock(par4 + b0, par5 + 1, par6 + b1).isNormalCube() ? 1 : 0);
					boolean flag = par3World.getBlock(par4 - b0, par5, par6 - b1) == block
							|| par3World.getBlock(par4 - b0, par5 + 1, par6 - b1) == block;
					boolean flag1 = par3World.getBlock(par4 + b0, par5, par6 + b1) == block
							|| par3World.getBlock(par4 + b0, par5 + 1, par6 + b1) == block;
					boolean flag2 = false;

					if (flag && !flag1) {
						flag2 = true;
					} else if (j1 > i2) {
						flag2 = true;
					}

					par3World.setBlock(par4, par5, par6, block, i1, 2);
					par3World.setBlock(par4, par5 + 1, par6, block, 8 | (flag2 ? 1 : 0), 2);
					par3World.notifyBlocksOfNeighborChange(par4, par5, par6, block);
					par3World.notifyBlocksOfNeighborChange(par4, par5 + 1, par6, block);

					--par1ItemStack.stackSize;
					return true;
				}
			} else {
				return false;
			}
		}
	}

	@Override
	public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) {
		p_77624_3_.add("This is a block, you can place it by right clicking");
		super.addInformation(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
	}
}