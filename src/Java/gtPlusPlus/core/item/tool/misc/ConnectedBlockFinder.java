package gtPlusPlus.core.item.tool.misc;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.api.objects.minecraft.BlockPos;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.BaseItemWithDamageValue;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class ConnectedBlockFinder extends BaseItemWithDamageValue{

	public ConnectedBlockFinder() {
		super("item.test.connector");
		this.setTextureName("stick");
		this.setMaxStackSize(1);
		this.setMaxDamage(10000);
		setCreativeTab(AddToCreativeTab.tabTools);
		GameRegistry.registerItem(this, getUnlocalizedName());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		list.add(EnumChatFormatting.GRAY+"Finds connected blocks, turns them to Glass once found.");
		super.addInformation(stack, aPlayer, list, bool);
	}

	@Override
	public boolean doesContainerItemLeaveCraftingGrid(final ItemStack itemStack){
		return false;
	}

	@Override
	public boolean getShareTag(){
		return true;
	}

	@Override
	public boolean hasContainerItem(final ItemStack itemStack){
		return true;
	}
	@Override
	public ItemStack getContainerItem(final ItemStack itemStack){
		return itemStack;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(final ItemStack par1ItemStack){
		return EnumRarity.uncommon;
	}

	@Override
	public boolean hasEffect(final ItemStack par1ItemStack){
		return false;
	}


	@Override
	public boolean onItemUse(
			ItemStack stack, EntityPlayer player, World world,
			int x,	int y, int z, int side,
			float hitX, float hitY, float hitZ) {

		BlockPos mStartPoint = new BlockPos(x,y,z);

		Block mBlockType = world.getBlock(x, y, z);
		int mBlockMeta = mBlockType.getDamageValue(world, x, y, z);

		Set<BlockPos> mTotalIndex = new HashSet<BlockPos>();
		Set<BlockPos> mFirstSearch = new HashSet<BlockPos>();
		Set<BlockPos> mSearch_A = new HashSet<BlockPos>();
		Set<BlockPos> mSearch_B = new HashSet<BlockPos>();
		Set<BlockPos> mSearch_C = new HashSet<BlockPos>();

		for (BlockPos b : mStartPoint.getSurroundingBlocks().values()) {
			if (world.getBlock(b.xPos, b.yPos, b.zPos) == mBlockType) {
				if (mBlockType.getDamageValue(world, b.xPos, b.yPos, b.zPos) == mBlockMeta) {
					if (mFirstSearch.add(b)) {
						if (mTotalIndex.add(b)) {
							world.setBlock(b.xPos, b.yPos, b.zPos, Blocks.emerald_ore);		
						}
					}
				}
			}
		}

		for (BlockPos b : mFirstSearch) {
			if (world.getBlock(b.xPos, b.yPos, b.zPos) == mBlockType) {
				if (mBlockType.getDamageValue(world, b.xPos, b.yPos, b.zPos) == mBlockMeta) {
					if (mSearch_A.add(b)) {
						if (mTotalIndex.add(b)) {
							world.setBlock(b.xPos, b.yPos, b.zPos, Blocks.emerald_ore);		
						}
					}
				}
			}
		}

		for (BlockPos b : mSearch_A) {
			if (world.getBlock(b.xPos, b.yPos, b.zPos) == mBlockType) {
				if (mBlockType.getDamageValue(world, b.xPos, b.yPos, b.zPos) == mBlockMeta) {
					if (mSearch_B.add(b)) {
						if (mTotalIndex.add(b)) {
							world.setBlock(b.xPos, b.yPos, b.zPos, Blocks.emerald_ore);		
						}
					}
				}
			}
		}
		
		for (BlockPos b : mSearch_B) {
			if (world.getBlock(b.xPos, b.yPos, b.zPos) == mBlockType) {
				if (mBlockType.getDamageValue(world, b.xPos, b.yPos, b.zPos) == mBlockMeta) {
					if (mSearch_C.add(b)) {
						if (mTotalIndex.add(b)) {
							world.setBlock(b.xPos, b.yPos, b.zPos, Blocks.emerald_ore);		
						}
					}
				}
			}
		}

		return super.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
	}	

	@Override
	public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {
		// TODO Auto-generated method stub
		return super.onItemRightClick(p_77659_1_, p_77659_2_, p_77659_3_);
	}







}
