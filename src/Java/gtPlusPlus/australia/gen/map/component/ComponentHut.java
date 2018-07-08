package gtPlusPlus.australia.gen.map.component;

import java.util.Random;

import gtPlusPlus.api.interfaces.IGeneratorWorld;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.australia.GTplusplus_Australia;
import gtPlusPlus.core.util.math.MathUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class ComponentHut extends AustraliaComponent {
	public static final int DIM_X = 7;
	public static final int DIM_Y = 10;
	public static final int DIM_Z = 7;

	public ComponentHut() {
	}

	public ComponentHut(int direction, Random random, int x, int z) {
		super(direction, random, x, z, 7, 10, 7);
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
		Block groundID = Blocks.grass;
		Block undergroundID = Blocks.dirt;
		if (biom.biomeID == GTplusplus_Australia.Australian_Desert_Biome_3.biomeID) {
			groundID = Blocks.sand;
			undergroundID = Blocks.sand;
		}
		else if (biom.biomeID == GTplusplus_Australia.Australian_Outback_Biome.biomeID) {
			groundID = Blocks.hardened_clay;
			undergroundID = Blocks.stained_hardened_clay;
		}		
		
		
		fillWithAir(world, this.boundingBox, 0, 1, 0, 6, 9, 6);
		fillWithMetadataBlocks(world, this.boundingBox, 0, 0, 1, 6, 1, 5, Blocks.dirt, 0, Blocks.dirt, 1, false);
		fillWithMetadataBlocks(world, this.boundingBox, 0, 2, 1, 6, 3, 5, Blocks.cobblestone, 0, Blocks.cobblestone, 0, false);
		fillWithAir(world, this.boundingBox, 1, 1, 2, 5, 3, 4);

		int logID = MathUtils.randInt(0, 1);
		place(Blocks.dirt, MathUtils.randInt(0, 1), 0, 1, 1, this.boundingBox, world);
		place(Blocks.dirt, MathUtils.randInt(0, 1), 0, 2, 1, this.boundingBox, world);
		place(Blocks.dirt, MathUtils.randInt(0, 1), 0, 3, 1, this.boundingBox, world);

		place(Blocks.dirt, MathUtils.randInt(0, 1), 0, 1, 5, this.boundingBox, world);
		place(Blocks.dirt, MathUtils.randInt(0, 1), 0, 2, 5, this.boundingBox, world);
		place(Blocks.dirt, MathUtils.randInt(0, 1), 0, 3, 5, this.boundingBox, world);

		place(Blocks.dirt, MathUtils.randInt(0, 1), 6, 1, 1, this.boundingBox, world);
		place(Blocks.dirt, MathUtils.randInt(0, 1), 6, 2, 1, this.boundingBox, world);
		place(Blocks.dirt, MathUtils.randInt(0, 1), 6, 3, 1, this.boundingBox, world);

		place(Blocks.dirt, MathUtils.randInt(0, 1), 6, 1, 5, this.boundingBox, world);
		place(Blocks.dirt, MathUtils.randInt(0, 1), 6, 2, 5, this.boundingBox, world);
		place(Blocks.dirt, MathUtils.randInt(0, 1), 6, 3, 5, this.boundingBox, world);

		int meta = (this.coordBaseMode == 3) || (this.coordBaseMode == 1) ? 4 : 8;
		
		place(Blocks.dirt, MathUtils.randInt(0, 1), 0, 4, 2, this.boundingBox, world);
		place(Blocks.dirt, MathUtils.randInt(0, 1), 0, 4, 3, this.boundingBox, world);
		place(Blocks.dirt, MathUtils.randInt(0, 1), 0, 4, 4, this.boundingBox, world);
		place(Blocks.dirt, MathUtils.randInt(0, 1), 6, 4, 2, this.boundingBox, world);
		place(Blocks.dirt, MathUtils.randInt(0, 1), 6, 4, 3, this.boundingBox, world);
		place(Blocks.dirt, MathUtils.randInt(0, 1), 6, 4, 4, this.boundingBox, world);		
		
		for (int x = 0; x < MathUtils.randInt(5, 9); x++) {
			place(Blocks.leaves, MathUtils.randInt(0, 3), x, 3, 0, this.boundingBox, world);
			place(Blocks.leaves, MathUtils.randInt(0, 3), x, 4, 1, this.boundingBox, world);
			place(Blocks.leaves, MathUtils.randInt(0, 3), x, 5, 2, this.boundingBox, world);
			place(Blocks.leaves, MathUtils.randInt(0, 3), x, 5, 3, this.boundingBox, world);
			place(Blocks.leaves, MathUtils.randInt(0, 3), x, 5, 4, this.boundingBox, world);
			place(Blocks.leaves, MathUtils.randInt(0, 3), x, 4, 5, this.boundingBox, world);
			place(Blocks.leaves, MathUtils.randInt(0, 3), x, 3, 6, this.boundingBox, world);
		}
		
		int glassMeta = Math.min(16, (coordBaseMode+MathUtils.randInt(0, 8)));
		if (MathUtils.randInt(0, 5) > 4)
		place(Blocks.stained_glass, glassMeta, 2, 2, 1, this.boundingBox, world);
		if (MathUtils.randInt(0, 5) > 4)
		place(Blocks.stained_glass, glassMeta, 2, 2, 5, this.boundingBox, world);
		if (MathUtils.randInt(0, 5) > 4)
		place(Blocks.stained_glass, glassMeta, 4, 2, 5, this.boundingBox, world);
		if (MathUtils.randInt(0, 5) > 4)
		place(Blocks.stained_glass, glassMeta, 0, 2, 3, this.boundingBox, world);
		if (MathUtils.randInt(0, 5) > 4)
		place(Blocks.stained_glass, glassMeta, 6, 2, 3, this.boundingBox, world);

		placeDoorAtCurrentPosition(world, this.boundingBox, random, 4, 1, 1,
				getMetadataWithOffset(Blocks.wooden_door, 1));

		place(Blocks.leaves, MathUtils.randInt(0, 3), 1, 1, 4, this.boundingBox, world);
		place(Blocks.torch, 0, 1, 2, 3, this.boundingBox, world);
		place(Blocks.torch, 0, 3, 2, 2, this.boundingBox, world);
		if (!this.hasMadeChest) {
			int ic = getYWithOffset(0);
			int jc = getXWithOffset(7, 1);
			int kc = getZWithOffset(7, 1);
			if (this.boundingBox.isVecInside(jc, ic, kc)) {
				this.hasMadeChest = true;
				generateStructureChestContents(world, this.boundingBox, random, 1, 1, 2, shackChestContents,
						1 + random.nextInt(3));
			}
		}
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				clearCurrentPositionBlocksUpwards(world, j, 6, i, this.boundingBox);
				func_151554_b(world, undergroundID, 0, j, 0, i, this.boundingBox);
			}
		}
		spawnNatives(world, this.boundingBox, 4, 1, 3, MathUtils.randInt(3, 5));

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
				if (par1World.rand.nextInt(MathUtils.randInt(3, 5)) != 0) {
					EntityVillager entityvillager = new EntityVillager(par1World, 7738);
					entityvillager.func_110163_bv();
					entityvillager.setLocationAndAngles(j1 + 0.5D, k1, l1 + 0.5D, 0.0F, 0.0F);
					par1World.spawnEntityInWorld(entityvillager);
					this.nativesSpawned += 1;
				}
			}
		}
	}

	public static final WeightedRandomChestContent[] shackChestContents = {
			new WeightedRandomChestContent(Items.glass_bottle, 0, 1, 1, 10),
			new WeightedRandomChestContent(Items.bread, 0, 1, 3, 15),
			new WeightedRandomChestContent(Items.apple, 0, 1, 3, 15),
			new WeightedRandomChestContent(Items.cooked_fished, 0, 1, 3, 10),
			new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.sapling), 1, 1, 1, 15),
			// new WeightedRandomChestContent(Witchery.Items.GENERIC,
			// Witchery.Items.GENERIC.itemRowanBerries.damageValue, 1, 2, 10),
			new WeightedRandomChestContent(Items.iron_shovel, 0, 1, 1, 5),
			new WeightedRandomChestContent(Items.iron_pickaxe, 0, 1, 1, 5) };
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

	public static class WorldHandlerHut implements IGeneratorWorld {
		private final double chance;
		private final int range;

		public WorldHandlerHut(double chance) {
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
				new ComponentHut(direction, random, x, z).addComponentParts(world, random);
				Logger.WORLD("NativeHut x: " + x + " | z: " + z + " | Dir: " + direction);
				return true;
			}
			return false;
		}

		public void initiate() {
		}
	}

}
