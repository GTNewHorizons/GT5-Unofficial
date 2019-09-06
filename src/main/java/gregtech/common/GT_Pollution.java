package gregtech.common;

import cpw.mods.fml.common.gameevent.TickEvent;
import gregtech.GT_Mod;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static gregtech.api.objects.XSTR.XSTR_INSTANCE;
import static gregtech.common.GT_Proxy.*;

//import net.minecraft.entity.EntityLiving;

public class GT_Pollution {
	/**
	 * Pollution dispersion until effects start:
	 * Calculation: ((Limit * 0.01) + 2000) * (4 <- spreading rate)
	 * 
	 * SMOG(500k) 466.7 pollution/sec
	 * Poison(750k) 633,3 pollution/sec
	 * Dying Plants(1mio) 800 pollution/sec
	 * Sour Rain(1.5mio) 1133.3 pollution/sec
	 * 
	 * Pollution producers (pollution/sec)
	 * Bronze Boiler(20)
	 * Lava Boiler(20)
	 * High Pressure Boiler(20)
	 * Bronze Blast Furnace(50)
	 * Diesel Generator(40/80/160)
	 * Gas Turbine(20/40/80)
	 * Charcoal Pile(100)
	 * 
	 * Large Diesel Engine(320)
	 * Electric Blast Furnace(100)
	 * Implosion Compressor(2000)
	 * Large Boiler(240)
	 * Large Gas Turbine(160)
	 * Multi Smelter(100)
	 * Pyrolyse Oven(400)
	 * 
	 * Machine Explosion(100,000)
	 * 
	 * Muffler Hatch Pollution reduction:
	 * LV (0%), MV (30%), HV (52%), EV (66%), IV (76%), LuV (84%), ZPM (89%), UV (92%), MAX (95%)
	 */
	private List<ChunkCoordIntPair> pollutionList = new ArrayList<>();//chunks left to process
	private HashMap<ChunkCoordIntPair,int[]> chunkData;//link to chunk data that is saved/loaded
	private int operationsPerTick=0;//how much chunks should be processed in each cycle
	private static final short cycleLen=1200;
	private final World aWorld;
	public static int mPlayerPollution;

	public GT_Pollution(World world){
		aWorld=world;
		chunkData=dimensionWiseChunkData.get(aWorld.provider.dimensionId);
		if(chunkData==null){
			chunkData=new HashMap<>(1024);
			dimensionWiseChunkData.put(world.provider.dimensionId,chunkData);
		}
		dimensionWisePollution.put(aWorld.provider.dimensionId,this);
	}

	public static void onWorldTick(TickEvent.WorldTickEvent aEvent){//called from proxy
		//return if pollution disabled
		if(!GT_Mod.gregtechproxy.mPollution) return;
		final GT_Pollution pollutionInstance = dimensionWisePollution.get(aEvent.world.provider.dimensionId);
		if(pollutionInstance==null)return;
		pollutionInstance.tickPollutionInWorld((int)(aEvent.world.getTotalWorldTime()%cycleLen));
	}

	private void tickPollutionInWorld(int aTickID){//called from method above
		//gen data set
		if(aTickID==0){
			pollutionList = new ArrayList<>(chunkData.keySet());
			//set operations per tick
			if(pollutionList.size()>0) operationsPerTick =(pollutionList.size()/cycleLen);
			else operationsPerTick=0;//SANity
		}

		for(int chunksProcessed=0;chunksProcessed<=operationsPerTick;chunksProcessed++){
			if(pollutionList.size()==0)break;//no more stuff to do
			ChunkCoordIntPair actualPos=pollutionList.remove(pollutionList.size()-1);//faster
			//add default data if missing
			if(!chunkData.containsKey(actualPos)) chunkData.put(actualPos,getDefaultChunkDataOnCreation());
			//get pollution
			int tPollution = chunkData.get(actualPos)[GTPOLLUTION];
			//remove some
			tPollution = (int)(0.9945f*tPollution);
			//tPollution -= 2000;//This does not really matter...

			if(tPollution<=0) tPollution = 0;//SANity check
			else if(tPollution>400000){//Spread Pollution

				ChunkCoordIntPair[] tNeighbors = new ChunkCoordIntPair[4];//array is faster
				tNeighbors[0]=(new ChunkCoordIntPair(actualPos.chunkXPos+1,actualPos.chunkZPos));
				tNeighbors[1]=(new ChunkCoordIntPair(actualPos.chunkXPos-1,actualPos.chunkZPos));
				tNeighbors[2]=(new ChunkCoordIntPair(actualPos.chunkXPos,actualPos.chunkZPos+1));
				tNeighbors[3]=(new ChunkCoordIntPair(actualPos.chunkXPos,actualPos.chunkZPos-1));
				for(ChunkCoordIntPair neighborPosition : tNeighbors){
					if(!chunkData.containsKey(neighborPosition)) chunkData.put(neighborPosition,getDefaultChunkDataOnCreation());

					int neighborPollution = chunkData.get(neighborPosition)[GTPOLLUTION];
					if(neighborPollution*6 < tPollution*5){//METHEMATICS...
						int tDiff = tPollution - neighborPollution;
						tDiff = tDiff/20;
						neighborPollution = GT_Utility.safeInt((long)neighborPollution+tDiff);//tNPol += tDiff;
						tPollution -= tDiff;
						chunkData.get(neighborPosition)[GTPOLLUTION] = neighborPollution;
					}
				}


				//Create Pollution effects
				//Smog filter TODO
				if(tPollution > GT_Mod.gregtechproxy.mPollutionSmogLimit) {
					AxisAlignedBB chunk = AxisAlignedBB.getBoundingBox(actualPos.chunkXPos << 4, 0, actualPos.chunkZPos << 4, (actualPos.chunkXPos << 4) + 16, 256, (actualPos.chunkZPos << 4) + 16);
					List<EntityLivingBase> tEntitys = aWorld.getEntitiesWithinAABB(EntityLivingBase.class, chunk);
					for (EntityLivingBase tEnt : tEntitys) {
						if (!GT_Utility.isWearingFullGasHazmat(tEnt)) {
							switch (XSTR_INSTANCE.nextInt(3)) {
								default:
									tEnt.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, Math.min(tPollution / 1000, 1000), tPollution / 400000));
								case 1:
									tEnt.addPotionEffect(new PotionEffect(Potion.weakness.id, Math.min(tPollution / 1000, 1000), tPollution / 400000));
								case 2:
									tEnt.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, Math.min(tPollution / 1000, 1000), tPollution / 400000));
							}
						}
					}


					//				Poison effects
					if (tPollution > GT_Mod.gregtechproxy.mPollutionPoisonLimit) {
						//AxisAlignedBB chunk = AxisAlignedBB.getBoundingBox(tPos.chunkPosX*16, 0, tPos.chunkPosZ*16, tPos.chunkPosX*16+16, 256, tPos.chunkPosZ*16+16);
						//List<EntityLiving> tEntitys = aWorld.getEntitiesWithinAABB(EntityLiving.class, chunk);
						for (EntityLivingBase tEnt : tEntitys) {
							if (!GT_Utility.isWearingFullGasHazmat(tEnt)) {
								switch (XSTR_INSTANCE.nextInt(4)) {
									default:
										tEnt.addPotionEffect(new PotionEffect(Potion.hunger.id, tPollution / 500000));
									case 1:
										tEnt.addPotionEffect(new PotionEffect(Potion.confusion.id, Math.min(tPollution / 2000, 1000), 1));
									case 2:
										tEnt.addPotionEffect(new PotionEffect(Potion.poison.id, Math.min(tPollution / 4000, 1000), tPollution / 500000));
									case 3:
										tEnt.addPotionEffect(new PotionEffect(Potion.blindness.id, Math.min(tPollution / 2000, 1000), 1));
								}
							}
						}


						//				killing plants
						if (tPollution > GT_Mod.gregtechproxy.mPollutionVegetationLimit) {
							int f = 20;
							for (; f < (tPollution / 25000); f++) {
								int x = (actualPos.chunkXPos << 4) + XSTR_INSTANCE.nextInt(16);
								int y = 60 + (-f + XSTR_INSTANCE.nextInt(f * 2 + 1));
								int z = (actualPos.chunkZPos << 4) + XSTR_INSTANCE.nextInt(16);
								damageBlock(aWorld, x, y, z, tPollution > GT_Mod.gregtechproxy.mPollutionSourRainLimit);
							}
						}
					}
				}
			}
			//Write new pollution to Hashmap !!!
			chunkData.get(actualPos)[GTPOLLUTION] = tPollution;
		}
	}
	
	private static void damageBlock(World world, int x, int y, int z, boolean sourRain){
		if (world.isRemote)	return;
		Block tBlock = world.getBlock(x, y, z);
		int tMeta = world.getBlockMetadata(x, y, z);
		if (tBlock == Blocks.air || tBlock == Blocks.stone || tBlock == Blocks.sand|| tBlock == Blocks.deadbush)return;
		
			if (tBlock == Blocks.leaves || tBlock == Blocks.leaves2 || tBlock.getMaterial() == Material.leaves)
				world.setBlockToAir(x, y, z);
			if (tBlock == Blocks.reeds) {
				tBlock.dropBlockAsItem(world, x, y, z, tMeta, 0);
				world.setBlockToAir(x, y, z);
			}
			if (tBlock == Blocks.tallgrass)
				world.setBlock(x, y, z, Blocks.deadbush);
			if (tBlock == Blocks.vine) {
				tBlock.dropBlockAsItem(world, x, y, z, tMeta, 0);
				world.setBlockToAir(x, y, z);
			}
			if (tBlock == Blocks.waterlily || tBlock == Blocks.wheat || tBlock == Blocks.cactus || 
				tBlock.getMaterial() == Material.cactus || tBlock == Blocks.melon_block || tBlock == Blocks.melon_stem) {
				tBlock.dropBlockAsItem(world, x, y, z, tMeta, 0);
				world.setBlockToAir(x, y, z);
			}
			if (tBlock == Blocks.red_flower || tBlock == Blocks.yellow_flower || tBlock == Blocks.carrots || 
				tBlock == Blocks.potatoes || tBlock == Blocks.pumpkin || tBlock == Blocks.pumpkin_stem) {
				tBlock.dropBlockAsItem(world, x, y, z, tMeta, 0);
				world.setBlockToAir(x, y, z);
			}
			if (tBlock == Blocks.sapling || tBlock.getMaterial() == Material.plants)
				world.setBlock(x, y, z, Blocks.deadbush);
			if (tBlock == Blocks.cocoa) {
				tBlock.dropBlockAsItem(world, x, y, z, tMeta, 0);
				world.setBlockToAir(x, y, z);
			}
			if (tBlock == Blocks.mossy_cobblestone)
				world.setBlock(x, y, z, Blocks.cobblestone);
			if (tBlock == Blocks.grass || tBlock.getMaterial() == Material.grass )
				world.setBlock(x, y, z, Blocks.dirt);	
			if(tBlock == Blocks.farmland || tBlock == Blocks.dirt){
				world.setBlock(x, y, z, Blocks.sand);					
			}
			
			if(sourRain && world.isRaining() && (tBlock == Blocks.stone || tBlock == Blocks.gravel || tBlock == Blocks.cobblestone) && 
				world.getBlock(x, y+1, z) == Blocks.air && world.canBlockSeeTheSky(x, y, z)){
				if(tBlock == Blocks.stone){world.setBlock(x, y, z, Blocks.cobblestone);	}
				else if(tBlock == Blocks.cobblestone){world.setBlock(x, y, z, Blocks.gravel);	}
				else if(tBlock == Blocks.gravel){world.setBlock(x, y, z, Blocks.sand);	}
			}
	}

	public static void addPollution(IGregTechTileEntity te, int aPollution){
		addPollution(te.getWorld().getChunkFromBlockCoords(te.getXCoord(),te.getZCoord()), aPollution);
	}

	public static void addPollution(Chunk ch, int aPollution){
		if(!GT_Mod.gregtechproxy.mPollution)return;
		HashMap<ChunkCoordIntPair,int[]> dataMap=dimensionWiseChunkData.get(ch.worldObj.provider.dimensionId);
		if(dataMap==null){
			dataMap=new HashMap<>(1024);
			dimensionWiseChunkData.put(ch.worldObj.provider.dimensionId,dataMap);
		}
		int[] dataArr=dataMap.get(ch.getChunkCoordIntPair());
		if(dataArr==null){
			dataArr=getDefaultChunkDataOnCreation();
			dataMap.put(ch.getChunkCoordIntPair(),dataArr);
		}
		dataArr[GTPOLLUTION]+=aPollution;
		if(dataArr[GTPOLLUTION]<0)dataArr[GTPOLLUTION]=0;
	}

	public static int getPollution(IGregTechTileEntity te){
		return getPollution(te.getWorld().getChunkFromBlockCoords(te.getXCoord(),te.getZCoord()));
	}

	public static int getPollution(Chunk ch){
		if(!GT_Mod.gregtechproxy.mPollution)return 0;
		HashMap<ChunkCoordIntPair,int[]> dataMap=dimensionWiseChunkData.get(ch.worldObj.provider.dimensionId);
		if(dataMap==null || dataMap.get(ch.getChunkCoordIntPair())==null) return 0;
		return dataMap.get(ch.getChunkCoordIntPair())[GTPOLLUTION];
	}

	public static int getPollution(ChunkCoordIntPair aCh, int aDim){
		if(!GT_Mod.gregtechproxy.mPollution)return 0;
		HashMap<ChunkCoordIntPair,int[]> dataMap=dimensionWiseChunkData.get(aDim);
		if(dataMap==null || dataMap.get(aCh)==null) return 0;
		return dataMap.get(aCh)[GTPOLLUTION];
	}
	
	//Add compatibility with old code
	@Deprecated /*Don't use it... too weird way of passing position*/
	public static void addPollution(World aWorld, ChunkPosition aPos, int aPollution){
		//The abuse of ChunkPosition to store block position and dim... 
		//is just bad especially when that is both used to store ChunkPos and BlockPos depending on context
		addPollution(aWorld.getChunkFromBlockCoords(aPos.chunkPosX,aPos.chunkPosZ),aPollution);
	}
}
