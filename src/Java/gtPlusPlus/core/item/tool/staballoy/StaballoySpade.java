package gtPlusPlus.core.item.tool.staballoy;

import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.player.UtilsMining;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class StaballoySpade extends ItemSpade{

	/* (non-Javadoc)
	 * @see net.minecraft.item.Item#getDurabilityForDisplay(net.minecraft.item.ItemStack)
	 */
	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		if (super.getDurabilityForDisplay(stack) > 0){
			return super.getDurabilityForDisplay(stack);}
		return 0;
	}

	protected Boolean FACING_HORIZONTAL = true;
	protected String FACING = "north";
	protected EntityPlayer localPlayer;
	protected String lookingDirection;
	protected World localWorld;
	public ItemStack thisPickaxe = null;

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
		localWorld = world;
		thisPickaxe = stack;
		return super.onItemRightClick(stack, world, aPlayer);
	}



	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int X, int Y, int Z, EntityLivingBase entity) {
		//super.onBlockDestroyed(stack, world, block, X, Y, Z, entity);
		localWorld = world;
		thisPickaxe = stack;
		//checkFacing(world);
		if (!world.isRemote){		
			GetDestroyOrientation(lookingDirection, world, X, Y, Z, stack);
		}

		return super.onBlockDestroyed(stack, world, block, X, Y, Z, entity);
	}

	public Boolean canPickaxeBlock(Block currentBlock, World currentWorld){
		String correctTool = "";
		if (!currentWorld.isRemote){			
			try {
				correctTool = currentBlock.getHarvestTool(0);
				//Utils.LOG_WARNING(correctTool);

				Utils.LOG_INFO("Tool for Block: "+correctTool+" | Current block: "+currentBlock.getLocalizedName());
				if (UtilsMining.getBlockType(currentBlock) || correctTool.equals("shovel")){
					return true;}
			} catch (NullPointerException e){
				return false;}
		}
		return false;
	}

	private void GetDestroyOrientation(String FACING, World world, int X, int Y, int Z, ItemStack heldItem){
		localWorld = world;
		float DURABILITY_LOSS = 0;
		if (!world.isRemote){

			if (FACING.equals("below") || FACING.equals("above")){
				DURABILITY_LOSS = 0;
				for(int i = -1; i < 2; i++) {
					for(int j = -1; j < 2; j++) {
						DURABILITY_LOSS = (DURABILITY_LOSS + removeBlockAndDropAsItem(world, X + i, Y, Z + j, heldItem));
					}
				}
			}

			else if (FACING.equals("facingEast") || FACING.equals("facingWest")){
				DURABILITY_LOSS = 0;
				for(int i = -1; i < 2; i++) {
					for(int j = -1; j < 2; j++) {
						DURABILITY_LOSS = (DURABILITY_LOSS + removeBlockAndDropAsItem(world, X , Y + i, Z + j, heldItem));						
					}
				}
			}

			else if (FACING.equals("facingNorth") || FACING.equals("facingSouth")){
				DURABILITY_LOSS = 0;
				for(int i = -1; i < 2; i++) {
					for(int j = -1; j < 2; j++) {
						DURABILITY_LOSS = (DURABILITY_LOSS + removeBlockAndDropAsItem(world, X + j, Y + i, Z, heldItem));
					}
				}
			}

			//int heldItemDurability = heldItem.getDamage(1);
			Utils.LOG_INFO("Total Loss: "+(int)DURABILITY_LOSS);
			//heldItem.setDamage(heldStack, DURABILITY_LOSS);
			//Utils.LOG_WARNING("|GID|Durability: "+heldItem.getItemDamage());			
			//Utils.LOG_WARNING("Durability: "+heldStack.getDamage(heldStack));
			Utils.LOG_INFO("1x: "+(heldItem.getItemDamage()));
			int itemdmg = heldItem.getItemDamage();
			int maxdmg = heldItem.getMaxDamage();
			int dodmg = (int)DURABILITY_LOSS;
			int durNow = (int) maxdmg-itemdmg;
			int durLeft = (int) ((maxdmg-itemdmg)-DURABILITY_LOSS);

			Utils.LOG_INFO(
					"Current Damage: " + itemdmg
					+ " Max Damage: " + maxdmg
					+ " Durability to be lost: " + dodmg
					+ " Current Durability: " + durNow
					+ " Remaining Durability: " + durLeft
					);


			//Break Tool
			if ((durNow-dodmg) <= (900) && itemdmg != 0){
				//TODO break tool
				Utils.LOG_INFO("Breaking Tool");
				heldItem.stackSize = 0;
			}
			//Do Damage
			else {
				//setItemDamage(heldItem, durLeft);
				Utils.LOG_INFO(""+(durNow-durLeft));
				damageItem(heldItem, (durNow-durLeft)-1, localPlayer);				
			}
			DURABILITY_LOSS = 0;

		}
	}

	public void damageItem(ItemStack item, int damage, EntityPlayer localPlayer){
		item.damageItem(damage, localPlayer);
	}

	public void setItemDamage(ItemStack item, int damage){
		item.setItemDamage(damage-1);
	}

	//Should clear up blocks quicker if I chain it.
	public int removeBlockAndDropAsItem(World world, int X, int Y, int Z, ItemStack heldItem){
		localWorld = world;
		Utils.LOG_INFO("Trying to drop/remove a block.");
		try {
			Block block = world.getBlock(X, Y, Z);
			Utils.LOG_WARNING(block.toString());
			String removalTool = "";
			removalTool = block.getHarvestTool(0);
			if (removalTool != null){
			if (removalTool.equals("shovel")){				
				if (canPickaxeBlock(block, world)){
					if((block != Blocks.bedrock) && (block.getBlockHardness(world, X, Y, Z) != -1) && (block.getBlockHardness(world, X, Y, Z) <= 100) && (block != Blocks.water) && (block != Blocks.lava)){

						int itemdmg = heldItem.getItemDamage();
						int maxdmg = heldItem.getMaxDamage();
						int dodmg = (int)100;
						int durNow = (int) maxdmg-itemdmg;
						int durLeft = (int) ((maxdmg-itemdmg)-100);

						if ((durNow-dodmg) <= (900) && itemdmg != 0){
							//Do Nothing, Tool is useless.
							return 0;
						}
						block.dropBlockAsItem(world, X, Y, Z, world.getBlockMetadata(X, Y, Z), 0);
						world.setBlockToAir(X, Y, Z);
						Utils.LOG_INFO("Adding 100 damage to item.");
						return 100;								
					}
					Utils.LOG_INFO("Incorrect Tool for mining this block. Wrong Block Water/lava/bedrock/blacklist");
					return 0;
				}
				Utils.LOG_INFO("Incorrect Tool for mining this block. Cannot Shovel this block type.");
				return 0;
			}
			Utils.LOG_INFO("Incorrect Tool for mining this block. Blocks mining tool is now Shovel.");	
			return 0;
			}
			Utils.LOG_INFO("Either the block was air or it declares an invalid mining tool.");	
			return 0;			
		} catch (NullPointerException e){
			Utils.LOG_INFO("Something Broke");
			e.printStackTrace();
			return 0;
		}
	}

	public boolean checkFacing(World world){
		localWorld = world;
		if (localPlayer != null){
			int direction = MathHelper.floor_double((double)((localPlayer.rotationYaw * 4F) / 360F) + 0.5D) & 3;
			//Utils.LOG_WARNING("Player - F: "+direction);
			//Utils.LOG_WARNING("Player - getLookVec(): "+localPlayer.getLookVec().yCoord);

			/*if (localPlayer.getLookVec().yCoord > 0){
				localPlayer.getLookVec().yCoord;
			}*/

			MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, (EntityPlayer) localPlayer, false);
			if (movingobjectposition != null){
				int sideHit = movingobjectposition.sideHit;
				String playerStandingPosition = "";
				if (movingobjectposition != null) {	       
					//System.out.println("Side Hit: "+movingobjectposition.sideHit);
				}

				if (sideHit == 0){
					playerStandingPosition = "above";
					FACING_HORIZONTAL = false;
				}
				else if (sideHit == 1){
					playerStandingPosition = "below";
					FACING_HORIZONTAL = false;
				}
				else if (sideHit == 2){
					playerStandingPosition = "facingSouth";
					FACING_HORIZONTAL = true;
				}
				else if (sideHit == 3){
					playerStandingPosition = "facingNorth";
					FACING_HORIZONTAL = true;
				}
				else if (sideHit == 4){
					playerStandingPosition = "facingEast";
					FACING_HORIZONTAL = true;
				}
				else if (sideHit == 5){
					playerStandingPosition = "facingWest";
					FACING_HORIZONTAL = true;
				}
				lookingDirection = playerStandingPosition;

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


			return true;
		}
		return false;		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {
		thisPickaxe = stack;	
		list.add(EnumChatFormatting.GOLD+"Spades a 3x3 area in the direction you are facing.");
		super.addInformation(stack, aPlayer, list, bool);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack par1ItemStack){
		return EnumRarity.rare;
	}

	@Override
	public boolean hasEffect(ItemStack par1ItemStack){
		return true;
	}


	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, int X, int Y, int Z, EntityPlayer aPlayer) {
		thisPickaxe = itemstack;
		localPlayer = aPlayer;		
		checkFacing(localPlayer.worldObj);
		return super.onBlockStartBreak(itemstack, X, Y, Z, aPlayer);
	}
	public StaballoySpade(String unlocalizedName, ToolMaterial material) {
		super(material);
		this.setUnlocalizedName(unlocalizedName);
		this.setTextureName(CORE.MODID + ":" + unlocalizedName);
		this.FACING_HORIZONTAL=true;
		this.setMaxStackSize(1);
		this.setMaxDamage(3200);
	}
}
