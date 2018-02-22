package gtPlusPlus.core.item.general;

import java.util.List;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.minecraft.BlockPos;
import gtPlusPlus.core.block.base.BlockBaseOre;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.CoreItem;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.EntityUtils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < 2; i ++) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	public String getItemStackDisplayName(final ItemStack tItem) {
		String itemName = "Debug Square";
		String suffixName = "";
		if (tItem.getItemDamage() == 0){
			suffixName = " [1]";
		}
		else if (tItem.getItemDamage() == 1){
			suffixName = " [2]";
		}
		return (itemName+suffixName);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) {
		String mMode = (stack.getItemDamage() == 0 ? "Clear" : "Fill");
		list.add(EnumChatFormatting.GRAY+""+("Mode: "+mMode));
		super.addInformation(stack, player, list, bool);
	}

	private static boolean createNBT(ItemStack rStack){
		final NBTTagCompound tagMain = new NBTTagCompound();
		final NBTTagCompound tagNBT = new NBTTagCompound();
		tagNBT.setLong("Mode", 0);
		tagMain.setTag("GTPP_DEBUG", tagNBT);		
		rStack.setTagCompound(tagMain);		
		return true;
	}

	public static final long getFilterDamage(final ItemStack aStack) {
		NBTTagCompound aNBT = aStack.getTagCompound();
		if (aNBT != null) {
			aNBT = aNBT.getCompoundTag("GTPP_DEBUG");
			if (aNBT != null) {
				return aNBT.getLong("Mode");
			}
		}
		else {
			createNBT(aStack);
		}
		return 0L;
	}

	public static final boolean setFilterDamage(final ItemStack aStack, final long aDamage) {
		NBTTagCompound aNBT = aStack.getTagCompound();
		if (aNBT != null) {
			aNBT = aNBT.getCompoundTag("GTPP_DEBUG");
			if (aNBT != null) {
				aNBT.setLong("Mode", aDamage);
				return true;
			}
		}
		return false;
	}

	public boolean removeBlocks(World world, BlockPos pos){
		int x1 = pos.xPos;
		int y1 = pos.yPos;
		int z1 = pos.zPos;

		int x2 = (x1-24);
		int y2 = (y1-10);
		int z2 = (z1-24);

		removeBlockColumn(world, new BlockPos(x2, y2, z2));
		return true;
	}

	public boolean removeBlockColumn(World world, BlockPos pos){
		for (int i=0; i<50; i++){
			removeBlockRow(world, new BlockPos(pos.xPos, pos.yPos-10, pos.zPos+i));
			removeBlockRow(world, new BlockPos(pos.xPos, pos.yPos, pos.zPos+i));
			removeBlockRow(world, new BlockPos(pos.xPos, pos.yPos+10, pos.zPos+i));
		}
		return true;
	}

	public boolean removeBlockRow(World world, BlockPos pos){
		for (int j=0; j<20; j++){
			for (int i=0; i<50; i++){

				if (!(world.getBlock(pos.xPos+i, pos.yPos+j, pos.zPos) instanceof BlockBaseOre)){				
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
		}
		return true;
	}

	public boolean fillBlocks(World world, BlockPos pos){
		int x1 = pos.xPos;
		int y1 = pos.yPos;
		int z1 = pos.zPos;

		int x2 = (x1-10);
		int y2 = (y1-1);
		int z2 = (z1-10);

		fillBlockColumn(world, new BlockPos(x2, y2, z2));
		return true;
	}

	public boolean fillBlockColumn(World world, BlockPos pos){
		for (int i=0; i<21; i++){
			fillBlockRow(world, new BlockPos(pos.xPos, pos.yPos, pos.zPos+i));
		}
		return true;
	}

	public boolean fillBlockRow(World world, BlockPos pos){
		for (int j=0; j<2; j++){
			for (int i=0; i<21; i++){
				if (world.getBlock(pos.xPos+i, pos.yPos+j, pos.zPos) != Blocks.bedrock){
					world.setBlock(pos.xPos+i, pos.yPos+j, pos.zPos, Blocks.grass);
				}
			}
		}
		return true;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack thisItem, World world, EntityPlayer parEntity) {
		BlockPos groundBlock = EntityUtils.findBlockPosUnderEntity(parEntity);
		if (thisItem.getItemDamage() == 0){
			removeBlocks(world, groundBlock);			
		}
		else {
			Logger.INFO("Filling.");
			fillBlocks(world, groundBlock);
		}
		return super.onItemRightClick(thisItem, world, parEntity);
	}

}
