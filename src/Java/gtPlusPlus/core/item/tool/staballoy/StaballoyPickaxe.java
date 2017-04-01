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
	public double getDurabilityForDisplay(final ItemStack stack) {
		return (double)stack.getItemDamageForDisplay() / (double)stack.getMaxDamage();
	}

	protected Boolean FACING_HORIZONTAL = true;
	protected String FACING = "north";
	protected EntityPlayer localPlayer;
	protected String lookingDirection;
	protected World localWorld;
	public ItemStack thisPickaxe = null;
	protected int miningLevel;

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
	public ItemStack onItemRightClick(final ItemStack stack, final World world, final EntityPlayer aPlayer) {
		this.localPlayer = aPlayer;
		this.localWorld = world;
		this.thisPickaxe = stack;
		return super.onItemRightClick(stack, world, aPlayer);
	}



	@Override
	public boolean onBlockDestroyed(final ItemStack stack, final World world, final Block block, final int X, final int Y, final int Z, final EntityLivingBase entity) {
		//super.onBlockDestroyed(stack, world, block, X, Y, Z, entity);
		this.localWorld = world;
		this.thisPickaxe = stack;
		//checkFacing(world);
		if (!world.isRemote){
			this.GetDestroyOrientation(block, this.lookingDirection, world, X, Y, Z, stack);
		}

		return super.onBlockDestroyed(stack, world, block, X, Y, Z, entity);
	}

	@SuppressWarnings("static-method")
	private float calculateDurabilityLoss(final World world, final int X, final int Y, final int Z){
		float bDurabilityLoss = 0;
		Boolean correctTool = false;
		float bHardness = 0;
		if (!world.isRemote){
			try {
				final Block removalist = world.getBlock(X, Y, Z);
				//Utils.LOG_WARNING(removalist.toString());

				bHardness = removalist.getBlockHardness(world, X, Y, Z);
				Utils.LOG_WARNING("Hardness: "+bHardness);

				bDurabilityLoss = (bDurabilityLoss + bHardness);
				//Utils.LOG_WARNING("Durability Loss: "+bDurabilityLoss);

				correctTool = this.canPickaxeBlock(removalist, world, new int[]{X,Y,Z});
				Utils.LOG_WARNING(""+correctTool);

				if (!correctTool){
					return 0;
				}

			} catch (final NullPointerException e){

			}
		}
		return 100;
	}

	public Boolean canPickaxeBlock(final Block currentBlock, final World currentWorld, final int[] xyz){
		String correctTool = "";
		if (!currentWorld.isRemote){
			try {
				correctTool = currentBlock.getHarvestTool(0);
				if (UtilsMining.getBlockType(currentBlock, currentWorld, xyz, this.miningLevel) || correctTool.equals("pickaxe") || correctTool.equals("null")){
					//Utils.LOG_WARNING(correctTool);
					return true;}
			} catch (final NullPointerException e){
				return false;}
		}
		return false;
	}

	private void GetDestroyOrientation(final Block block, final String FACING, final World world, final int X, final int Y, final int Z, final ItemStack heldItem){
		this.localWorld = world;
		float DURABILITY_LOSS = 0;
		if (!world.isRemote){

			Utils.LOG_WARNING("hardness:"+block.getBlockHardness(world, X, Y, Z));
			if (FACING.equals("below") || FACING.equals("above")){
				DURABILITY_LOSS = 0;
				for(int i = -1; i < 2; i++) {
					for(int j = -1; j < 2; j++) {
						final float dur = this.calculateDurabilityLoss(world, X + i, Y, Z + j);
						DURABILITY_LOSS = (DURABILITY_LOSS + dur);
						Utils.LOG_WARNING("Added Loss: "+dur);
						this.removeBlockAndDropAsItem(world, X + i, Y, Z + j, heldItem);
					}
				}
			}

			else if (FACING.equals("facingEast") || FACING.equals("facingWest")){
				DURABILITY_LOSS = 0;
				for(int i = -1; i < 2; i++) {
					for(int j = -1; j < 2; j++) {
						final float dur = this.calculateDurabilityLoss(world, X, Y + i, Z + j);
						DURABILITY_LOSS = (DURABILITY_LOSS + dur);
						Utils.LOG_WARNING("Added Loss: "+dur);
						this.removeBlockAndDropAsItem(world, X , Y + i, Z + j, heldItem);
					}
				}
			}

			else if (FACING.equals("facingNorth") || FACING.equals("facingSouth")){
				DURABILITY_LOSS = 0;
				for(int i = -1; i < 2; i++) {
					for(int j = -1; j < 2; j++) {
						final float dur = this.calculateDurabilityLoss(world, X + j, Y + i, Z);
						DURABILITY_LOSS = (DURABILITY_LOSS + dur);
						Utils.LOG_WARNING("Added Loss: "+dur);
						this.removeBlockAndDropAsItem(world, X + j, Y + i, Z, heldItem);
					}
				}
			}

			//int heldItemDurability = heldItem.getDamage(1);
			Utils.LOG_WARNING("Total Loss: "+(int)DURABILITY_LOSS);
			//heldItem.setDamage(heldStack, DURABILITY_LOSS);
			//Utils.LOG_WARNING("|GID|Durability: "+heldItem.getItemDamage());
			//Utils.LOG_WARNING("Durability: "+heldStack.getDamage(heldStack));
			Utils.LOG_WARNING("1x: "+(heldItem.getItemDamage()));
			final int itemdmg = heldItem.getItemDamage();
			final int maxdmg = heldItem.getMaxDamage();
			final int dodmg = (int)DURABILITY_LOSS;
			final int durNow = maxdmg-itemdmg;
			final int durLeft = (int) ((maxdmg-itemdmg)-DURABILITY_LOSS);

			Utils.LOG_WARNING(
					"Current Damage: " + itemdmg
					+ " Max Damage: " + maxdmg
					+ " Durability to be lost: " + dodmg
					+ " Current Durability: " + durNow
					+ " Remaining Durability: " + durLeft
					);


			//Break Tool
			if (((durNow-dodmg) <= (99)) && (itemdmg != 0)){
				//TODO break tool
				Utils.LOG_WARNING("Breaking Tool");
				heldItem.stackSize = 0;
			}
			//Do Damage
			else {
				//setItemDamage(heldItem, durLeft);
				Utils.LOG_WARNING(""+(durNow-durLeft));
				this.damageItem(heldItem, (durNow-durLeft)-1, this.localPlayer);
			}


			/*if (heldItem.getItemDamage() <= ((heldItem.getMaxDamage()-heldItem.getItemDamage())-DURABILITY_LOSS)){
				Utils.LOG_WARNING("2: "+DURABILITY_LOSS+" 3: "+((heldItem.getMaxDamage()-heldItem.getItemDamage())-DURABILITY_LOSS));
				setItemDamage(heldItem, (int) (heldItem.getMaxDamage()-(heldItem.getMaxDamage()-heldItem.getItemDamage())-DURABILITY_LOSS));
			}
			else {
				Utils.LOG_WARNING("3: "+( heldItem.getMaxDamage()-(heldItem.getMaxDamage()-heldItem.getItemDamage())));
				setItemDamage(heldItem, heldItem.getMaxDamage()-(heldItem.getMaxDamage()-heldItem.getItemDamage()));
			}*/
			//Utils.LOG_WARNING("|GID|Durability: "+heldItem.getItemDamage());
			DURABILITY_LOSS = 0;

		}
	}

	public void damageItem(final ItemStack item, final int damage, final EntityPlayer localPlayer){
		item.damageItem(damage, localPlayer);
	}

	public void setItemDamage(final ItemStack item, final int damage){
		item.setItemDamage(damage-1);
	}

	//Should clear up blocks quicker if I chain it.
	public final void removeBlockAndDropAsItem(final World world, final int X, final int Y, final int Z, final ItemStack heldItem){
		this.localWorld = world;
		try {
			final Block block = world.getBlock(X, Y, Z);
			final float dur = this.calculateDurabilityLoss(world, X, Y, Z);
			Utils.LOG_WARNING(block.toString());
			String removalTool = "";
			removalTool = block.getHarvestTool(1);

			Utils.LOG_WARNING("Removing.1 "+removalTool);
			/*if ((removalTool.equalsIgnoreCase("pickaxe") || removalTool.equalsIgnoreCase("null") || removalTool == null)){
				Utils.LOG_WARNING("Removing.2");
				if (UtilsMining.getBlockType(block, world, new int[]{X,Y,Z}, miningLevel))			{
					Utils.LOG_WARNING("Removing.3");	*/
			if (this.canPickaxeBlock(block, world, new int[]{X,Y,Z})){
				Utils.LOG_WARNING("Removing.4");

				if (block == Blocks.air){
					return;
				}

				if((block != Blocks.bedrock) && (block.getBlockHardness(world, X, Y, Z) >= 0) && (block.getBlockHardness(world, X, Y, Z) <= 100) && (block != Blocks.water) && (block != Blocks.lava)){

					Utils.LOG_WARNING("Removing.5");
					if (heldItem.getItemDamage() <= (heldItem.getMaxDamage()-dur)){
						
						if (X == 0 && Y == 0 && Z == 0){
							
						}
						else {
							block.dropBlockAsItem(world, X, Y, Z, world.getBlockMetadata(X, Y, Z), 0);
							world.setBlockToAir(X, Y, Z);							
						}

					}

				}
				/*}

			}*/
			}
			else {
				Utils.LOG_WARNING("Incorrect Tool for mining this block.");
			}
		} catch (final NullPointerException e){

		}
	}

	public boolean checkFacing(final World world){
		this.localWorld = world;
		if (this.localPlayer != null){
			final int direction = MathHelper.floor_double((this.localPlayer.rotationYaw * 4F) / 360F + 0.5D) & 3;
			//Utils.LOG_WARNING("Player - F: "+direction);
			//Utils.LOG_WARNING("Player - getLookVec(): "+localPlayer.getLookVec().yCoord);

			/*if (localPlayer.getLookVec().yCoord > 0){
				localPlayer.getLookVec().yCoord;
			}*/

			final MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, this.localPlayer, false);
			if (movingobjectposition != null){
				final int sideHit = movingobjectposition.sideHit;
				String playerStandingPosition = "";
				if (movingobjectposition != null) {
					//System.out.println("Side Hit: "+movingobjectposition.sideHit);
				}

				if (sideHit == 0){
					playerStandingPosition = "above";
					this.FACING_HORIZONTAL = false;
				}
				else if (sideHit == 1){
					playerStandingPosition = "below";
					this.FACING_HORIZONTAL = false;
				}
				else if (sideHit == 2){
					playerStandingPosition = "facingSouth";
					this.FACING_HORIZONTAL = true;
				}
				else if (sideHit == 3){
					playerStandingPosition = "facingNorth";
					this.FACING_HORIZONTAL = true;
				}
				else if (sideHit == 4){
					playerStandingPosition = "facingEast";
					this.FACING_HORIZONTAL = true;
				}
				else if (sideHit == 5){
					playerStandingPosition = "facingWest";
					this.FACING_HORIZONTAL = true;
				}
				this.lookingDirection = playerStandingPosition;

				if (direction == 0){
					this.FACING = "south";
				}
				else if (direction == 1){
					this.FACING = "west";
				}
				else if (direction == 2){
					this.FACING = "north";
				}
				else if (direction == 3){
					this.FACING = "east";
				}
			}


			return true;
		}
		return false;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		this.thisPickaxe = stack;
		list.add(EnumChatFormatting.GRAY+"Mines a 3x3 at 100 durability per block mined.");
		list.add(EnumChatFormatting.GRAY+"Durability: "+(stack.getMaxDamage()-stack.getItemDamage())+"/"+stack.getMaxDamage());
		//super.addInformation(stack, aPlayer, list, bool);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(final ItemStack par1ItemStack){
		return EnumRarity.rare;
	}

	@Override
	public boolean hasEffect(final ItemStack par1ItemStack){
		return true;
	}


	@Override
	public boolean onBlockStartBreak(final ItemStack itemstack, final int X, final int Y, final int Z, final EntityPlayer aPlayer) {
		this.thisPickaxe = itemstack;
		this.localPlayer = aPlayer;
		this.checkFacing(this.localPlayer.worldObj);
		return super.onBlockStartBreak(itemstack, X, Y, Z, aPlayer);
	}

	public StaballoyPickaxe(final String unlocalizedName, final ToolMaterial material) {
		super(material);
		this.setUnlocalizedName(unlocalizedName);
		this.setTextureName(CORE.MODID + ":" + unlocalizedName);
		this.FACING_HORIZONTAL=true;
		this.setMaxStackSize(1);
		this.setMaxDamage(3200);
		this.miningLevel = 5;
	}
}
