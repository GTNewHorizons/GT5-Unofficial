package gtPlusPlus.core.util.minecraft;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import gregtech.common.GT_Worldgen_GT_Ore_Layer;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.util.reflect.ReflectionUtils;

public class MiningUtils {

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
				/*if (x.mOverworld) {
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
				}*/
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
