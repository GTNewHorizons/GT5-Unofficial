package miscutil.core.util;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MiningMethods {
	
	private static boolean durabilityDamage = false;
	private static ItemStack stack;

	public static Boolean canPickaxeBlock(Block currentBlock, World currentWorld){
		String correctTool = "";
		if (!currentWorld.isRemote){			
			try {
				correctTool = currentBlock.getHarvestTool(0);
				//Utils.LOG_WARNING(correctTool);
				if (correctTool.equals("pickaxe")){
					return true;}
			} catch (NullPointerException e){
				return false;}
		}
		return false;
	}
	
	private static void removeBlockAndDropAsItem(World world, int X, int Y, int Z){
		try {
			Block block = world.getBlock(X, Y, Z);		
				if (canPickaxeBlock(block, world)){
					if((block != Blocks.bedrock) && (block.getBlockHardness(world, X, Y, Z) != -1) && (block.getBlockHardness(world, X, Y, Z) <= 100) && (block != Blocks.water) && (block != Blocks.lava)){
						block.dropBlockAsItem(world, X, Y, Z, world.getBlockMetadata(X, Y, Z), 0);
						world.setBlockToAir(X, Y, Z);

				}
				else {
					Utils.LOG_WARNING("Incorrect Tool for mining this block.");
				}
			}
		} catch (NullPointerException e){

		}
	}

	public static void customMine(World world, String FACING, EntityPlayer aPlayer){

		float DURABILITY_LOSS = 0;
		if (!world.isRemote){
			int X = 0;
			int Y = 0;
			int Z = 0;

			if (FACING.equals("below") || FACING.equals("above")){

				//Set Player Facing
				X = (int) aPlayer.posX;
				Utils.LOG_WARNING("Setting Variable X: "+X);
				if (FACING.equals("above")){
					Z = (int) aPlayer.posY + 1;
					Utils.LOG_WARNING("Setting Variable Y: "+Y);
					}
					else {
						Z = (int) aPlayer.posY - 1;
						Utils.LOG_WARNING("Setting Variable Y: "+Y);}
				Z = (int) aPlayer.posZ;
				Utils.LOG_WARNING("Setting Variable Z: "+Z);

				DURABILITY_LOSS = 0;
				for(int i = -2; i < 3; i++) {
					for(int j = -2; j < 3; j++) {
						for(int k = -2; k < 3; k++) {
/*//							float dur = calculateDurabilityLoss(world, X + i, Y + k, Z + j);
//							DURABILITY_LOSS = (DURABILITY_LOSS + dur);	
//							Utils.LOG_WARNING("Added Loss: "+dur);
*/							removeBlockAndDropAsItem(world, X + i, Y + k, Z + j);
						}
					}
				}
			}

			else if (FACING.equals("facingEast") || FACING.equals("facingWest")){

				//Set Player Facing
				Z = (int) aPlayer.posZ;
				Y = (int) aPlayer.posY;
				if (FACING.equals("facingEast")){
					X = (int) aPlayer.posX + 1;}
					else {
						X = (int) aPlayer.posX - 1;}
					

				DURABILITY_LOSS = 0;
				for(int i = -1; i < 2; i++) {
					for(int j = -1; j < 2; j++) {
						for(int k = -1; k < 2; k++) {
							/*float dur = calculateDurabilityLoss(world, X+k, Y + i, Z + j);
							DURABILITY_LOSS = (DURABILITY_LOSS + dur);
							Utils.LOG_WARNING("Added Loss: "+dur);*/
							removeBlockAndDropAsItem(world, X+k, Y + i, Z + j);
						}
					}
				}
			}

			else if (FACING.equals("facingNorth") || FACING.equals("facingSouth")){

				//Set Player Facing
				X = (int) aPlayer.posX;
				Y = (int) aPlayer.posY;
				
				if (FACING.equals("facingNorth")){
				Z = (int) aPlayer.posZ + 1;}
				else {
					Z = (int) aPlayer.posZ - 1;}
				
				DURABILITY_LOSS = 0;
				for(int i = -1; i < 2; i++) {
					for(int j = -1; j < 2; j++) {
						for(int k = -1; k < 2; k++) {
							/*float dur = calculateDurabilityLoss(world, X + j, Y + i, Z+k);
							DURABILITY_LOSS = (DURABILITY_LOSS + dur);
							Utils.LOG_WARNING("Added Loss: "+dur);*/
							removeBlockAndDropAsItem(world, X + j, Y + i, Z+k);
						}
					}
				}
			}

			//Set Durability damage to the item
			if (durabilityDamage == true){
			Utils.LOG_WARNING("Total Loss: "+(int)DURABILITY_LOSS);
			if (stack.getItemDamage() < (stack.getMaxDamage()-DURABILITY_LOSS)){
				stack.damageItem((int) DURABILITY_LOSS, aPlayer);
			}
			}
			DURABILITY_LOSS = 0;
		}
	}
	
	
	public static boolean getBlockType(Block block){
		final String LIQUID = "liquid";
		final String BLOCK = "block";
		final String ORE = "ore";
		final String AIR = "air";
		String blockClass = "";

		try {
			blockClass = block.getClass().toString().toLowerCase();
			Utils.LOG_WARNING(blockClass);
			if (blockClass.toLowerCase().contains(LIQUID)){
				Utils.LOG_WARNING(block.toString()+" is a Liquid.");
				return false;
			}
			else if (blockClass.toLowerCase().contains(ORE)){
				Utils.LOG_WARNING(block.toString()+" is an Ore.");
				return true;
			}
			else if (blockClass.toLowerCase().contains(AIR)){
				Utils.LOG_WARNING(block.toString()+" is Air.");
				return false;
			}
			else if (blockClass.toLowerCase().contains(BLOCK)){
				Utils.LOG_WARNING(block.toString()+" is a block of some kind.");
				return false;
			}
			else {
				Utils.LOG_WARNING(block.toString()+" is mystery.");
				return false;
			}
		}
		catch(NullPointerException e){
			return false;
		}
	}
	
	
}
