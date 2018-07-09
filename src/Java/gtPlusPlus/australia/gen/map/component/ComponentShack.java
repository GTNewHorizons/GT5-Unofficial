package gtPlusPlus.australia.gen.map.component;

import java.util.Random;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gtPlusPlus.api.interfaces.IGeneratorWorld;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.australia.GTplusplus_Australia;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class ComponentShack extends AustraliaComponent {
	public static final int DIM_X = 9;
	public static final int DIM_Y = 10;
	public static final int DIM_Z = 9;

	public ComponentShack() {
	}

	public ComponentShack(int direction, Random random, int x, int z) {
		super(direction, random, x, z, DIM_X, DIM_Y, DIM_Z);
	}

	public boolean addComponentParts(World world, Random random) {
		BiomeGenBase biom = world.getBiomeGenForCoords(getXWithOffset(0, 0), getZWithOffset(0, 0));
		int groundAvg = calcGroundHeight(world, this.boundingBox);
		if (groundAvg < 0) {
			return true;
		}
		this.boundingBox.offset(0, groundAvg - this.boundingBox.maxY + 10 - 1, 0);
		if ((isWaterBelow(world, 0, -1, 0, this.boundingBox)) || (isWaterBelow(world, 0, -1, 6, this.boundingBox))
				|| (isWaterBelow(world, 6, -1, 0, this.boundingBox))
				|| (isWaterBelow(world, 6, -1, 6, this.boundingBox))) {
			return false;
		}
		Block mStone;
		Block groundID = Blocks.grass;
		Block undergroundID = Blocks.dirt;
		if (biom.biomeID == GTplusplus_Australia.Australian_Desert_Biome_3.biomeID) {
			groundID = Blocks.sand;
			undergroundID = Blocks.sand;
			mStone = Blocks.sandstone;
		}
		else if (biom.biomeID == GTplusplus_Australia.Australian_Outback_Biome.biomeID) {
			groundID = Blocks.hardened_clay;
			undergroundID = Blocks.stained_hardened_clay;
			mStone = Blocks.stained_hardened_clay;
		}
		else {
			mStone = Blocks.stonebrick;
		}
		
		int mWoodType = MathUtils.randInt(0, 5);
		int logID;
		Block mWoodenStairs;
		Block mLog;
		if (mWoodType == 1) {
			mWoodenStairs = Blocks.spruce_stairs;
			logID = 1;
		}
		else if (mWoodType == 2) {
			mWoodenStairs = Blocks.birch_stairs;
			logID = 2;
			
		}
		else if (mWoodType == 3) {
			mWoodenStairs = Blocks.jungle_stairs;
			logID = 3;
		}
		else if (mWoodType == 4) {
			mWoodenStairs = Blocks.acacia_stairs;
			logID = 0;			
		}
		else if (mWoodType == 5) {
			mWoodenStairs = Blocks.dark_oak_stairs;	
			logID = 1;		
		}
		else {
			mWoodenStairs = Blocks.oak_stairs;	
			logID = 0;		
		}
		if (mWoodType >= 4) {
			mLog = Blocks.log2;			
		}
		else {
			mLog = Blocks.log;
		}
		
		int mStoneMeta = MathUtils.randInt(0, mStone == Blocks.stained_hardened_clay ? 15 : mStone == Blocks.sandstone ? 2 : 3);
		
		fillWithAir(world, this.boundingBox, 0, 1, 0, 7, 7, 4);
		fillWithMetadataBlocks(world, this.boundingBox, 1, 0, 1, 7, 1, 5, mStone, mStoneMeta, mStone, mStoneMeta, false);
		fillWithMetadataBlocks(world, this.boundingBox, 1, 2, 1, 7, 3, 5, Blocks.planks, mWoodType, Blocks.planks, mWoodType, false);
		fillWithAir(world, this.boundingBox, 2, 1, 2, 6, 3, 4);

		place(mLog, logID, 1, 1, 1, this.boundingBox, world);
		place(mLog, logID, 1, 2, 1, this.boundingBox, world);
		place(mLog, logID, 1, 3, 1, this.boundingBox, world);

		place(mLog, logID, 1, 1, 5, this.boundingBox, world);
		place(mLog, logID, 1, 2, 5, this.boundingBox, world);
		place(mLog, logID, 1, 3, 5, this.boundingBox, world);

		place(mLog, logID, 7, 1, 1, this.boundingBox, world);
		place(mLog, logID, 7, 2, 1, this.boundingBox, world);
		place(mLog, logID, 7, 3, 1, this.boundingBox, world);

		place(mLog, logID, 7, 1, 5, this.boundingBox, world);
		place(mLog, logID, 7, 2, 5, this.boundingBox, world);
		place(mLog, logID, 7, 3, 5, this.boundingBox, world);

		int meta = (this.coordBaseMode == 3) || (this.coordBaseMode == 1) ? 4 : 8;
		
		place(mLog, logID, 1, 4, 2, this.boundingBox, world);
		place(mLog, logID, 1, 4, 3, this.boundingBox, world);
		place(mLog, logID, 1, 4, 4, this.boundingBox, world);
		place(mLog, logID, 7, 4, 2, this.boundingBox, world);
		place(mLog, logID, 7, 4, 3, this.boundingBox, world);
		place(mLog, logID, 7, 4, 4, this.boundingBox, world);		
		
		for (int x = 0; x < 9; x++) {
			place(mWoodenStairs, getMetadataWithOffset(Blocks.oak_stairs, 3), x, 3, 0, this.boundingBox, world);
			place(mWoodenStairs, getMetadataWithOffset(Blocks.oak_stairs, 3), x, 4, 1, this.boundingBox, world);
			place(mWoodenStairs, getMetadataWithOffset(Blocks.oak_stairs, 3), x, 5, 2, this.boundingBox, world);
			place(Blocks.planks, mWoodType, x, 5, 3, this.boundingBox, world);
			place(mWoodenStairs, getMetadataWithOffset(Blocks.oak_stairs, 2), x, 5, 4, this.boundingBox, world);
			place(mWoodenStairs, getMetadataWithOffset(Blocks.oak_stairs, 2), x, 4, 5, this.boundingBox, world);
			place(mWoodenStairs, getMetadataWithOffset(Blocks.oak_stairs, 2), x, 3, 6, this.boundingBox, world);
		}
		
		int glassMeta = Math.min(16, (coordBaseMode+MathUtils.randInt(0, 8)));
		
		place(Blocks.stained_glass_pane, glassMeta, 3, 2, 1, this.boundingBox, world);
		place(Blocks.stained_glass_pane, glassMeta, 3, 2, 5, this.boundingBox, world);
		place(Blocks.stained_glass_pane, glassMeta, 5, 2, 5, this.boundingBox, world);
		place(Blocks.stained_glass_pane, glassMeta, 1, 2, 3, this.boundingBox, world);
		place(Blocks.stained_glass_pane, glassMeta, 7, 2, 3, this.boundingBox, world);

		placeDoorAtCurrentPosition(world, this.boundingBox, random, 5, 1, 1,
				getMetadataWithOffset(Blocks.wooden_door, 1));

		place(Blocks.redstone_lamp, mWoodType, 2, 1, 4, this.boundingBox, world);
		place(Blocks.redstone_torch, 0, 2, 2, 4, this.boundingBox, world);
		place(mWoodenStairs, getMetadataWithOffset(mWoodenStairs, 1), 2, 1, 3, this.boundingBox, world);
		place(mWoodenStairs, getMetadataWithOffset(mWoodenStairs, 3), 3, 1, 4, this.boundingBox, world);
		place(Blocks.fence, 0, 3, 1, 3, this.boundingBox, world);
		place(Blocks.heavy_weighted_pressure_plate, 0, 3, 2, 3, this.boundingBox, world);
		if (!this.hasMadeChest) {
			int ic = getYWithOffset(0);
			int jc = getXWithOffset(7, 1);
			int kc = getZWithOffset(7, 1);
			if (this.boundingBox.isVecInside(jc, ic, kc)) {				
				this.hasMadeChest = true;
				generateStructureChestContents(world, this.boundingBox, random, 2, 1, 2, shackChestContents,
						1 + random.nextInt(3));
			}
		}
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				clearCurrentPositionBlocksUpwards(world, j, 6, i, this.boundingBox);
				func_151554_b(world, undergroundID, 0, j, 0, i, this.boundingBox);
			}
		}
		spawnNatives(world, this.boundingBox, 3, 2, 3, MathUtils.randInt(3, 5));

		return true;
	}

	private int nativesSpawned = 0;

	private void spawnNatives(World par1World, StructureBoundingBox par2StructureBoundingBox, int par3, int par4,
			int par5, int maxSpawned) {
		if (this.nativesSpawned < maxSpawned) {
			for (int i1 = this.nativesSpawned; i1 < maxSpawned; i1++) {
				int j1 = getXWithOffset(par3 + i1, par5);
				int k1 = getYWithOffset(par4);
				int l1 = getZWithOffset(par3 + i1, par5);
				if (!par2StructureBoundingBox.isVecInside(j1, k1, l1)) {
					break;
				}
				if (par1World.rand.nextInt(MathUtils.randInt(1, 3)) != 0) {
					EntityVillager entityvillager = new EntityVillager(par1World, 7736+(MathUtils.randInt(0, 1)));
					entityvillager.func_110163_bv();
					entityvillager.setLocationAndAngles(j1 + 0.5D, k1, l1 + 0.5D, 0.0F, 0.0F);
					par1World.spawnEntityInWorld(entityvillager);
					this.nativesSpawned += 1;
				}
			}
		}
	}

	//Min, max, Weight
	public static final WeightedRandomChestContent[] shackChestContents = {
			new WeightedRandomChestContent(ItemUtils.getItemStackOfAmountFromOreDict("dustIron", MathUtils.randInt(1, 4)), 0, MathUtils.randInt(1, 9), 50),
			new WeightedRandomChestContent(ItemUtils.getItemStackOfAmountFromOreDict("dustCopper", MathUtils.randInt(1, 4)), 0, MathUtils.randInt(1, 6), 50),
			new WeightedRandomChestContent(ItemUtils.getItemStackOfAmountFromOreDict("dustTin", MathUtils.randInt(1, 4)), 0, MathUtils.randInt(1, 6), 50),
			new WeightedRandomChestContent(ItemUtils.getItemStackOfAmountFromOreDict("dustGold", MathUtils.randInt(1, 4)), 0, MathUtils.randInt(1, 3), 30),
			new WeightedRandomChestContent(ItemUtils.getItemStackOfAmountFromOreDict("dustSilver", MathUtils.randInt(1, 4)), 0, MathUtils.randInt(1, 3), 30),
			new WeightedRandomChestContent(ItemUtils.getItemStackOfAmountFromOreDict("gemDiamond", MathUtils.randInt(1, 2)), 0, MathUtils.randInt(1, 2), 5),
			new WeightedRandomChestContent(ItemUtils.getItemStackOfAmountFromOreDict("gemEmerald", MathUtils.randInt(1, 3)), 0, MathUtils.randInt(1, 3), 5),
			new WeightedRandomChestContent(ItemUtils.getItemStackOfAmountFromOreDict("gemRuby", MathUtils.randInt(1, 4)), 0, MathUtils.randInt(1, 4), 15),
			new WeightedRandomChestContent(ItemUtils.getItemStackOfAmountFromOreDict("gemSapphire", MathUtils.randInt(1, 4)), 0, MathUtils.randInt(1, 4), 15),
			new WeightedRandomChestContent(ItemUtils.getSimpleStack(CI.electricMotor_LV, MathUtils.randInt(1, 3)), 0, MathUtils.randInt(1, 3), 5),
			new WeightedRandomChestContent(ItemUtils.getSimpleStack(CI.electricPiston_LV, MathUtils.randInt(1, 3)), 0, MathUtils.randInt(1, 3), 5),
			new WeightedRandomChestContent(ItemUtils.getSimpleStack(CI.robotArm_LV, MathUtils.randInt(1, 2)), 0, MathUtils.randInt(1, 2), 2),
			new WeightedRandomChestContent(ItemUtils.getGregtechOreStack(OrePrefixes.cableGt01, Materials.Copper, MathUtils.randInt(1, 3)), 0, MathUtils.randInt(1, 3), 25),
			new WeightedRandomChestContent(ItemUtils.getGregtechOreStack(OrePrefixes.cableGt01, Materials.Tin, MathUtils.randInt(1, 3)), 0, MathUtils.randInt(1, 3), 25),
			new WeightedRandomChestContent(ItemUtils.getGregtechOreStack(OrePrefixes.wireGt01, Materials.Copper, MathUtils.randInt(2, 5)), 0, MathUtils.randInt(2, 5), 35),
			new WeightedRandomChestContent(ItemUtils.getGregtechOreStack(OrePrefixes.wireGt01, Materials.Tin, MathUtils.randInt(2, 5)), 0, MathUtils.randInt(2, 5), 35),
			new WeightedRandomChestContent(ItemUtils.getGregtechOreStack(OrePrefixes.pipeSmall, Materials.Copper, MathUtils.randInt(1, 3)), 0, MathUtils.randInt(1, 3), 25),
			new WeightedRandomChestContent(ItemUtils.getGregtechOreStack(OrePrefixes.pipeSmall, Materials.Bronze, MathUtils.randInt(1, 3)), 0, MathUtils.randInt(1, 3), 15),
			new WeightedRandomChestContent(ItemUtils.getGregtechOreStack(OrePrefixes.pipeTiny, Materials.Steel, MathUtils.randInt(1, 3)), 0, MathUtils.randInt(1, 3), 5),
			};
	
	private boolean hasMadeChest;
	private static final String CHEST_KEY = "AUSShackChest";

	protected void func_143012_a(NBTTagCompound par1NBTTagCompound) {
		super.func_143012_a(par1NBTTagCompound);
		par1NBTTagCompound.setBoolean("AUSShackChest", this.hasMadeChest);
		par1NBTTagCompound.setInteger("AUSWCount", this.nativesSpawned);
	}

	protected void func_143011_b(NBTTagCompound par1NBTTagCompound) {
		super.func_143011_b(par1NBTTagCompound);
		this.hasMadeChest = par1NBTTagCompound.getBoolean("AUSShackChest");
		if (par1NBTTagCompound.hasKey("AUSWCount")) {
			this.nativesSpawned = par1NBTTagCompound.getInteger("AUSWCount");
		} else {
			this.nativesSpawned = 0;
		}
	}

	public static class WorldHandlerShack implements IGeneratorWorld {
		private final double chance;
		private final int range;

		public WorldHandlerShack(double chance) {
			this.chance = chance;
			this.range = 400;
		}

		public int getExtentX() {
			return 7;
		}

		public int getExtentZ() {
			return 7;
		}

		public int getRange() {
			return this.range;
		}

		public boolean generate(World world, Random random, int x, int z) {
			if ((MathUtils.randInt(0, 100) < (this.chance/5))) {
				int direction = MathUtils.randInt(0, 3);
				new ComponentShack(direction, random, x, z).addComponentParts(world, random);
				Logger.WORLD("NativeShack x: " + x + " | z: " + z + " | dir: " + direction);
				return true;
			}
			return false;
		}

		public void initiate() {
		}
	}

}
