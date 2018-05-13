package gtPlusPlus.core.util.minecraft;

import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import gregtech.common.GT_Worldgen_GT_Ore_Layer;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.util.reflect.ReflectionUtils;

public class MiningUtils {

	private static boolean durabilityDamage = false;
	private static ItemStack stack;

	public static Boolean canPickaxeBlock(final Block currentBlock, final World currentWorld){
		String correctTool = "";
		if (!currentWorld.isRemote){
			try {
				correctTool = currentBlock.getHarvestTool(0);
				//Utils.LOG_WARNING(correctTool);
				if (correctTool.equals("pickaxe")){
					return true;}
			} catch (final NullPointerException e){
				return false;}
		}
		return false;
	}

	private static void removeBlockAndDropAsItem(final World world, final int X, final int Y, final int Z){
		try {
			final Block block = world.getBlock(X, Y, Z);
			if (canPickaxeBlock(block, world)){
				if((block != Blocks.bedrock) && (block.getBlockHardness(world, X, Y, Z) != -1) && (block.getBlockHardness(world, X, Y, Z) <= 100) && (block != Blocks.water) && (block != Blocks.lava)){
					block.dropBlockAsItem(world, X, Y, Z, world.getBlockMetadata(X, Y, Z), 0);
					world.setBlockToAir(X, Y, Z);

				}
				else {
					Logger.WARNING("Incorrect Tool for mining this block.");
				}
			}
		} catch (final NullPointerException e){

		}
	}

	/*public static void customMine(World world, String FACING, EntityPlayer aPlayer){

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
//							float dur = calculateDurabilityLoss(world, X + i, Y + k, Z + j);
//							DURABILITY_LOSS = (DURABILITY_LOSS + dur);
//							Utils.LOG_WARNING("Added Loss: "+dur);
							removeBlockAndDropAsItem(world, X + i, Y + k, Z + j);
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
							float dur = calculateDurabilityLoss(world, X+k, Y + i, Z + j);
							DURABILITY_LOSS = (DURABILITY_LOSS + dur);
							Utils.LOG_WARNING("Added Loss: "+dur);
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
							float dur = calculateDurabilityLoss(world, X + j, Y + i, Z+k);
							DURABILITY_LOSS = (DURABILITY_LOSS + dur);
							Utils.LOG_WARNING("Added Loss: "+dur);
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
	}*/


	public static boolean getBlockType(final Block block, final World world, final int[] xyz, final int miningLevel){
		final String LIQUID = "liquid";
		final String BLOCK = "block";
		final String ORE = "ore";
		final String AIR = "air";
		String blockClass = "";

		if (world.isRemote){
			return false;
		}

		if (block == Blocks.end_stone) {
			return true;
		}
		if (block == Blocks.stone) {
			return true;
		}
		if (block == Blocks.sandstone) {
			return true;
		}
		if (block == Blocks.netherrack) {
			return true;
		}
		if (block == Blocks.nether_brick) {
			return true;
		}
		if (block == Blocks.nether_brick_stairs) {
			return true;
		}
		if (block == Blocks.nether_brick_fence) {
			return true;
		}
		if (block == Blocks.glowstone) {
			return true;
		}



		try {
			blockClass = block.getClass().toString().toLowerCase();
			Logger.WARNING(blockClass);
			if (blockClass.toLowerCase().contains(LIQUID)){
				Logger.WARNING(block.toString()+" is a Liquid.");
				return false;
			}
			else if (blockClass.toLowerCase().contains(ORE)){
				Logger.WARNING(block.toString()+" is an Ore.");
				return true;
			}
			else if (block.getHarvestLevel(world.getBlockMetadata(xyz[0], xyz[1], xyz[2])) >= miningLevel){
				Logger.WARNING(block.toString()+" is minable.");
				return true;
			}
			else if (blockClass.toLowerCase().contains(AIR)){
				Logger.WARNING(block.toString()+" is Air.");
				return false;
			}
			else if (blockClass.toLowerCase().contains(BLOCK)){
				Logger.WARNING(block.toString()+" is a block of some kind.");
				return false;
			}
			else {
				Logger.WARNING(block.toString()+" is mystery.");
				return false;
			}
		}
		catch(final NullPointerException e){
			return false;
		}
	}

	public static int mMoonID =-99;
	public static int mMarsID = -99;
	public static int mCometsID = -99;
	public static AutoMap<GT_Worldgen_GT_Ore_Layer> getOresForDim(int dim) {
		if (dim == -1) {
			return Ores_Nether;
		}
		else if (dim == 1) {
			return Ores_End;
		}
		else if (dim == mMoonID) {
			return Ores_Moon;
		}
		else if (dim == mMarsID) {
			return Ores_Mars;
		}
		else if (dim == mCometsID) {
			return Ores_Comets;
		}
		else {
			return Ores_Overworld;			
		}
		
	}
	public static AutoMap<GT_Worldgen_GT_Ore_Layer>[] mOreMaps = new AutoMap[7];
	private static AutoMap<GT_Worldgen_GT_Ore_Layer> Ores_Overworld = new AutoMap<GT_Worldgen_GT_Ore_Layer>();
	private static AutoMap<GT_Worldgen_GT_Ore_Layer> Ores_Nether = new AutoMap<GT_Worldgen_GT_Ore_Layer>();
	private static AutoMap<GT_Worldgen_GT_Ore_Layer> Ores_End = new AutoMap<GT_Worldgen_GT_Ore_Layer>();
	private static AutoMap<GT_Worldgen_GT_Ore_Layer> Ores_Moon = new AutoMap<GT_Worldgen_GT_Ore_Layer>();
	private static AutoMap<GT_Worldgen_GT_Ore_Layer> Ores_Mars = new AutoMap<GT_Worldgen_GT_Ore_Layer>();
	private static AutoMap<GT_Worldgen_GT_Ore_Layer> Ores_Comets = new AutoMap<GT_Worldgen_GT_Ore_Layer>();
	private static AutoMap<GT_Worldgen_GT_Ore_Layer> Ores_Misc = new AutoMap<GT_Worldgen_GT_Ore_Layer>();
	
	public static boolean findAndMapOreTypesFromGT() {
		//Gets Moon ID
		try {
			if (Class.forName("micdoodle8.mods.galacticraft.core.util.ConfigManagerCore") != null) {
				mMoonID = ReflectionUtils.getField(Class.forName("micdoodle8.mods.galacticraft.core.util.ConfigManagerCore"), "idDimensionMoon").getInt(null);
			}
		}
		catch (ClassNotFoundException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException e) {}
		
		//Gets Mars ID
		try {
			if (Class.forName("micdoodle8.mods.galacticraft.planets.mars.ConfigManagerMars") != null) {
				mMarsID = ReflectionUtils.getField(Class.forName("micdoodle8.mods.galacticraft.planets.mars.ConfigManagerMars"), "dimensionIDMars").getInt(null);
			}
		}
		catch (ClassNotFoundException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException e) {}
		
		//Get Comets ID
		try {
			if (Class.forName("micdoodle8.mods.galacticraft.planets.asteroids.ConfigManagerAsteroids") != null) {
				mCometsID = ReflectionUtils.getField(Class.forName("micdoodle8.mods.galacticraft.planets.asteroids.ConfigManagerAsteroids"), "dimensionIDAsteroids").getInt(null);
			}
		}
		catch (ClassNotFoundException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException e) {}
		
		
		for (GT_Worldgen_GT_Ore_Layer x : GT_Worldgen_GT_Ore_Layer.sList) {			
			if (x.mEnabled) {
				if (x.mOverworld) {
					Ores_Overworld.put(x);
					continue;
				}
				if (x.mNether) {
					Ores_Nether.put(x);
					continue;
				}
				if (x.mEnd || x.mEndAsteroid) {
					Ores_End.put(x);
					continue;
				}
				if (x.mMoon) {
					Ores_Moon.put(x);
					continue;
				}
				if (x.mMars) {
					Ores_Mars.put(x);
					continue;
				}
				if (x.mAsteroid) {
					Ores_Comets.put(x);
					continue;
				}
				Ores_Misc.put(x);
				continue;
			}			
		}	
		mOreMaps[0] = Ores_Overworld;
		mOreMaps[1] = Ores_Nether;
		mOreMaps[2] = Ores_End;
		mOreMaps[3] = Ores_Moon;
		mOreMaps[4] = Ores_Mars;
		mOreMaps[5] = Ores_Comets;
		mOreMaps[6] = Ores_Misc;
		return true;
	}

}
