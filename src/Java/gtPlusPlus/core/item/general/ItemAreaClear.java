package gtPlusPlus.core.item.general;

import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.CoreItem;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.array.BlockPos;
import gtPlusPlus.core.util.entity.EntityUtils;
import gtPlusPlus.core.util.math.MathUtils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemAreaClear extends CoreItem {

	public IIcon[] mIcon = new IIcon[1];

	public ItemAreaClear() {
		super("itemDebugClearing", AddToCreativeTab.tabMachines, 1, 100, EnumChatFormatting.OBFUSCATED+"F A M C Y   N A M E", EnumRarity.rare,
				EnumChatFormatting.BOLD, false, null);
	}

	@Override
	public void registerIcons(IIconRegister reg) {
		this.mIcon[0] = reg.registerIcon(CORE.MODID + ":" + "itemLavaFilter");
	}

	@Override
	public IIcon getIconFromDamage(int meta) {
		return this.mIcon[0];
	}

	@Override
	public String getItemStackDisplayName(ItemStack tItem) {
		return "Debug Square";
	}

	public boolean removeBlocks(World world, BlockPos pos){
		int x1 = pos.xPos;
		int y1 = pos.yPos;
		int z1 = pos.zPos;

		int x2 = (x1-12);
		int y2 = (y1-5);
		int z2 = (z1-12);

		removeBlockColumn(world, new BlockPos(x2, y2, z2));
		return true;
	}

	public boolean removeBlockColumn(World world, BlockPos pos){
		for (int i=0; i<25; i++){
			removeBlockRow(world, new BlockPos(pos.xPos, pos.yPos, pos.zPos+i));
		}
		return true;
	}

	public boolean removeBlockRow(World world, BlockPos pos){
		for (int j=0; j<10; j++){
			for (int i=0; i<25; i++){
				if (!world.isAirBlock(pos.xPos+i, pos.yPos+j, pos.zPos) &&
						world.getBlock(pos.xPos+i, pos.yPos+j, pos.zPos) != Blocks.bedrock){
					int chance = MathUtils.randInt(0, 100);
					if (chance <= 0){
						if (pos.yPos+j <= 50){
							world.setBlock(pos.xPos+i, pos.yPos+j, pos.zPos, Blocks.glowstone);	
						}
					}
					else {
						if ((world.getBlock(pos.xPos+i, pos.yPos+j, pos.zPos) == Blocks.glowstone && ((pos.yPos+j) > 50)) || world.getBlock(pos.xPos+i, pos.yPos+j, pos.zPos) != Blocks.glowstone){
							world.setBlock(pos.xPos+i, pos.yPos+j, pos.zPos, Blocks.air);
						}
					}
				}
			}
		}
		return true;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack thisItem, World world, EntityPlayer parEntity) {
		BlockPos groundBlock = EntityUtils.findBlockPosUnderEntity(parEntity);
		removeBlocks(world, groundBlock);
		return super.onItemRightClick(thisItem, world, parEntity);
	}

}
