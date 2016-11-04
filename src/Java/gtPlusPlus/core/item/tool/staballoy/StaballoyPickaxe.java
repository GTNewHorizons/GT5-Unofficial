package gtPlusPlus.core.item.tool.staballoy;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.player.UtilsMining;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class StaballoyPickaxe extends ItemPickaxe{

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

	@SuppressWarnings("static-method")
	private float calculateDurabilityLoss(World world, int X, int Y, int Z){
		float bDurabilityLoss = 0;
		Boolean correctTool = false;
		float bHardness = 0;
		if (!world.isRemote){			
			try {
				Block removalist = world.getBlock(X, Y, Z);
				//Utils.LOG_WARNING(removalist.toString());

				bHardness = removalist.getBlockHardness(world, X, Y, Z);
				Utils.LOG_WARNING("Hardness: "+bHardness);

				bDurabilityLoss = (bDurabilityLoss + bHardness);
				//Utils.LOG_WARNING("Durability Loss: "+bDurabilityLoss);

				correctTool = canPickaxeBlock(removalist, world);
				Utils.LOG_WARNING(""+correctTool);

				if (!correctTool){
					return 0;
				}

			} catch (NullPointerException e){

			}
		}
		return 100;
	}

	public Boolean canPickaxeBlock(Block currentBlock, World currentWorld){
		String correctTool = "";
		if (!currentWorld.isRemote){			
			try {
				correctTool = currentBlock.getHarvestTool(0);
				//Utils.LOG_WARNING(correctTool);
				if (UtilsMining.getBlockType(currentBlock) || correctTool.equals("pickaxe")){
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
						float dur = calculateDurabilityLoss(world, X + i, Y, Z + j);
						DURABILITY_LOSS = (DURABILITY_LOSS + dur);	
						Utils.LOG_WARNING("Added Loss: "+dur);
						removeBlockAndDropAsItem(world, X + i, Y, Z + j, heldItem);
					}
				}
			}

			else if (FACING.equals("facingEast") || FACING.equals("facingWest")){
				DURABILITY_LOSS = 0;
				for(int i = -1; i < 2; i++) {
					for(int j = -1; j < 2; j++) {
						float dur = calculateDurabilityLoss(world, X, Y + i, Z + j);
						DURABILITY_LOSS = (DURABILITY_LOSS + dur);
						Utils.LOG_WARNING("Added Loss: "+dur);
						removeBlockAndDropAsItem(world, X , Y + i, Z + j, heldItem);
					}
				}
			}

			else if (FACING.equals("facingNorth") || FACING.equals("facingSouth")){
				DURABILITY_LOSS = 0;
				for(int i = -1; i < 2; i++) {
					for(int j = -1; j < 2; j++) {
						float dur = calculateDurabilityLoss(world, X + j, Y + i, Z);
						DURABILITY_LOSS = (DURABILITY_LOSS + dur);
						Utils.LOG_WARNING("Added Loss: "+dur);
						removeBlockAndDropAsItem(world, X + j, Y + i, Z, heldItem);
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
			if ((durNow-dodmg) <= (99) && itemdmg != 0){
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
			
			
			/*if (heldItem.getItemDamage() <= ((heldItem.getMaxDamage()-heldItem.getItemDamage())-DURABILITY_LOSS)){
				Utils.LOG_INFO("2: "+DURABILITY_LOSS+" 3: "+((heldItem.getMaxDamage()-heldItem.getItemDamage())-DURABILITY_LOSS));
				setItemDamage(heldItem, (int) (heldItem.getMaxDamage()-(heldItem.getMaxDamage()-heldItem.getItemDamage())-DURABILITY_LOSS));
			}
			else {
				Utils.LOG_INFO("3: "+( heldItem.getMaxDamage()-(heldItem.getMaxDamage()-heldItem.getItemDamage())));
				setItemDamage(heldItem, heldItem.getMaxDamage()-(heldItem.getMaxDamage()-heldItem.getItemDamage()));
			}*/
			//Utils.LOG_WARNING("|GID|Durability: "+heldItem.getItemDamage());
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
	public void removeBlockAndDropAsItem(World world, int X, int Y, int Z, ItemStack heldItem){
		localWorld = world;
		try {
			Block block = world.getBlock(X, Y, Z);
			float dur = calculateDurabilityLoss(world, X, Y, Z);
			Utils.LOG_WARNING(block.toString());
			String removalTool = "";
			removalTool = block.getHarvestTool(1);
			
			if (removalTool.equals("pickaxe") || UtilsMining.getBlockType(block)){				
				if (canPickaxeBlock(block, world)){
					if((block != Blocks.bedrock) && (block.getBlockHardness(world, X, Y, Z) != -1) && (block.getBlockHardness(world, X, Y, Z) <= 100) && (block != Blocks.water) && (block != Blocks.lava)){
						
						if (heldItem.getItemDamage() <= (heldItem.getMaxDamage()-dur)){
						
						block.dropBlockAsItem(world, X, Y, Z, world.getBlockMetadata(X, Y, Z), 0);
						world.setBlockToAir(X, Y, Z);
					
						}
					
					}
				}
				else {
					Utils.LOG_WARNING("Incorrect Tool for mining this block.");
				}
			}
		} catch (NullPointerException e){

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
		list.add(EnumChatFormatting.GRAY+"Mines a 3x3 at 100 durability per block mined.");
		list.add(EnumChatFormatting.GRAY+"Durability: "+(stack.getMaxDamage()-stack.getItemDamage())+"/"+stack.getMaxDamage());
		//super.addInformation(stack, aPlayer, list, bool);
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
	public StaballoyPickaxe(String unlocalizedName, ToolMaterial material) {
		super(material);
		this.setUnlocalizedName(unlocalizedName);
		this.setTextureName(CORE.MODID + ":" + unlocalizedName);
		this.FACING_HORIZONTAL=true;
		this.setMaxStackSize(1);
		this.setMaxDamage(3200);
	}
}
