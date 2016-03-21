package miscutil.core.item.tool.staballoy;

import java.util.List;

import miscutil.core.lib.CORE;
import miscutil.core.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class StaballoyPickaxe extends ItemPickaxe{

	private Boolean FACING_HORIZONTAL = true;
	private String FACING = "north";
	private EntityPlayer localPlayer;

	/*
	 * 
	 * 
	 * 
	 *  Methods 
	 * 
	 * 
	 * 
	 */

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer aPlayer) {
		localPlayer = aPlayer;
		checkFacing(world);
		return super.onItemRightClick(stack, world, aPlayer);
	}



	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int X, int Y, int Z, EntityLivingBase entity) {
		//super.onBlockDestroyed(stack, world, block, X, Y, Z, entity);

		GetDestroyOrientation("FLAT, TOPDOWN OR BOTTOMUP", world, X, Y, Z);

		return super.onBlockDestroyed(stack, world, block, X, Y, Z, entity);
	}
	
	private void GetDestroyOrientation(String FACING, World world, int X, int Y, int Z){
		for(int i = -1; i < 2; i++) {
			for(int j = -1; j < 2; j++) {
				removeBlockAndDropAsItem(world, X + i, Y, Z + j, i, j);
			}
		}
	}

	//Should clear up blocks quicker if I chain it.
	@SuppressWarnings("static-method")
	private void removeBlockAndDropAsItem(World world, int X, int Y, int Z, int i, int j){
		Block block = world.getBlock(X, Y, Z);
		if((block != Blocks.bedrock) && (block.getBlockHardness(world, X, Y, Z) != -1)){
			block.dropBlockAsItem(world, X, Y, Z, world.getBlockMetadata(X, Y, Z), 0);
			world.setBlockToAir(X, Y, Z);
		}
	}

	private boolean checkFacing(World world){
		if (!localPlayer.equals(null) || localPlayer != null || (!localPlayer.equals(null) && localPlayer != null)){
			int direction = MathHelper.floor_double((double)((localPlayer.rotationYaw * 4F) / 360F) + 0.5D) & 3;
			//Utils.LOG_INFO("Player - F: "+direction);
			Utils.LOG_INFO("Player - getLookVec(): "+localPlayer.getLookVec().yCoord);

			/*if (localPlayer.getLookVec().yCoord > 0){
				localPlayer.getLookVec().yCoord;
			}*/

			MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, (EntityPlayer) localPlayer, false);
			int sideHit = movingobjectposition.sideHit;
			String playerStandingPosition;
			if (movingobjectposition != null) {	       
				System.out.println("Side Hit: "+movingobjectposition.sideHit);
			}

			if (sideHit == 0){
				playerStandingPosition = "below";
			}
			else if (sideHit == 1){
				playerStandingPosition = "above";
			}
			else if (sideHit == 2){
				playerStandingPosition = "facingSouth";
			}
			else if (sideHit == 3){
				playerStandingPosition = "facingNorth";
			}
			else if (sideHit == 4){
				playerStandingPosition = "facingEast";
			}
			else if (sideHit == 5){
				playerStandingPosition = "facingWest";
			}

			if (direction == 0){				
				FACING = "south";
			} 
			else if (direction == 1){				
				FACING = "west";
			}
			else if (direction == 2){				
				FACING = "north";
			} 
			else if (direction == 3){				
				FACING = "east";
			}
		
			
		}
		return false;		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {
		String facing;
		if (FACING_HORIZONTAL){
			facing = "Horizontal";
		}
		else {
			facing = "Vertical";
		}
		list.add("Mines a 3x3 area on the "+facing+" axis.");


		super.addInformation(stack, aPlayer, list, bool);
	}

	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, int X, int Y, int Z, EntityPlayer aPlayer) {

		localPlayer = aPlayer;

		return super.onBlockStartBreak(itemstack, X, Y, Z, aPlayer);
	}

	public StaballoyPickaxe(String unlocalizedName, ToolMaterial material) {
		super(material);
		this.setUnlocalizedName(unlocalizedName);
		this.setTextureName(CORE.MODID + ":" + unlocalizedName);
		this.FACING_HORIZONTAL=true;
	}

	/*public boolean onBlockDestroyed(ItemStack stack, World w, int id, int x, int y, int z, EntityLiving entity){
        super.onBlockDestroyed(stack, w, , x, y, z, entity);
        Block block = this.

    }*/

}
