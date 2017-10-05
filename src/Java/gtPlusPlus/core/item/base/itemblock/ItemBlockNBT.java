package gtPlusPlus.core.item.base.itemblock;

import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.tileentities.base.TileEntityBase;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.player.PlayerUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemBlockNBT extends ItemBlock {

	public ItemBlockNBT(final Block block) {
		super(block);
	}

	@Override
	public void onCreated(ItemStack item, World world, EntityPlayer player) {
		addNBT(player, item);
		super.onCreated(item, world, player);
	}

	@Override
	public void onUpdate(ItemStack item, World world, Entity entity, int p_77663_4_, boolean p_77663_5_) {
		if (entity instanceof EntityPlayerMP) {
			EntityPlayerMP mPlayer = (EntityPlayerMP) entity;

			NBTTagCompound rNBT = item.getTagCompound();
			rNBT = ((rNBT == null) ? new NBTTagCompound() : rNBT);
			if (!rNBT.hasKey("mOwner")){
				addNBT(mPlayer, item);					
			}			
		}
		super.onUpdate(item, world, entity, p_77663_4_, p_77663_5_);
	}

	private void addNBT(EntityPlayer player, ItemStack item) {
		NBTTagCompound rNBT = item.getTagCompound();
		rNBT = ((rNBT == null) ? new NBTTagCompound() : rNBT);
		if (player != null) {
			boolean mOP = PlayerUtils.isPlayerOP(player);
			rNBT.setString("mOwner", player.getDisplayName());
			rNBT.setString("mUUID", ""+player.getUniqueID());
			rNBT.setBoolean("mOP", mOP);		
		} 
		else if (player == null) {
			rNBT.setString("mOwner", "fakeplayer");
			rNBT.setString("mUUID", "00000000");
			rNBT.setBoolean("mOP", false);
		}
		GT_Utility.ItemNBT.setNBT(item, rNBT);
	}

	@Override		
	public boolean placeBlockAt(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int side,
			float hitX, float hitY, float hitZ, int aMeta) {
		
		

		if (!(aWorld.setBlock(aX, aY, aZ, this.field_150939_a, 0, 3))) {
			return false;
		}
		if (aWorld.getBlock(aX, aY, aZ) == this.field_150939_a) {
			this.field_150939_a.onBlockPlacedBy(aWorld, aX, aY, aZ, aPlayer, aStack);
			this.field_150939_a.onPostBlockPlaced(aWorld, aX, aY, aZ, aMeta);
		}
		
		TileEntityBase tTileEntity = (TileEntityBase) aWorld.getTileEntity(aX, aY, aZ);
		if (tTileEntity != null && aPlayer != null) {
			if (tTileEntity.isServerSide()){				
				Utils.LOG_INFO("Setting Tile Entity information");
				NBTTagCompound aNBT = GT_Utility.ItemNBT.getNBT(aStack);				
				tTileEntity.setOwnerInformation(aNBT.getString("mOwner"), aNBT.getString("mUUID"), aNBT.getBoolean("mOP"));
			}
		}

		return true;
	}

}