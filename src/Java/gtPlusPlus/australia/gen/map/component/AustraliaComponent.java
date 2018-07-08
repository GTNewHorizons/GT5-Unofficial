package gtPlusPlus.australia.gen.map.component;

import java.util.Random;

import gtPlusPlus.api.objects.Logger;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraftforge.common.ChestGenHooks;

public class AustraliaComponent extends StructureComponent {
	public AustraliaComponent() {
	}

	public AustraliaComponent(int direction, Random random, int x, int z, int dimX, int dimY, int dimZ) {
		super(direction);
		this.coordBaseMode = direction;
		this.boundingBox = calcBox(direction, x + (16 - dimX) / 2, 64, z + (16 - dimZ) / 2, dimX, dimY, dimZ, 0);
	}

	public boolean addComponentParts(World world, Random random) {
		return true;
	}

	protected void func_151554_b(World par1World, Block par2, int par3, int par4, int par5, int par6,
			StructureBoundingBox par7StructureBoundingBox) {
		int j1 = getXWithOffset(par4, par6);
		int k0 = getYWithOffset(par5);
		int k1 = k0;
		int l1 = getZWithOffset(par4, par6);
		if (par7StructureBoundingBox.isVecInside(j1, k1, l1)) {
			if (par1World.isAirBlock(j1, k1, l1)) {
				return;
			}
			k1--;
			while (((par1World.isAirBlock(j1, k1, l1)) || (!par1World.getBlock(j1, k1, l1).getMaterial().isSolid())
					|| (par1World.getBlock(j1, k1, l1) == Blocks.ice)) && (k1 > 1)) {
				par1World.setBlock(j1, k1, l1, par2, par3, 2);

				k1--;
			}
		}
	}

	protected void clearCurrentPositionBlocksUpwards(World par1World, int par2, int par3, int par4,
			StructureBoundingBox par5StructureBoundingBox) {
		int l = getXWithOffset(par2, par4);
		int i1 = getYWithOffset(par3);
		int j1 = getZWithOffset(par2, par4);
		if (par5StructureBoundingBox.isVecInside(l, i1, j1)) {
			int i = 0;
			for (;;) {
				i++;
				if (((i >= 20) && (par1World.isAirBlock(l, i1, j1))) || (i1 >= 255)) {
					break;
				}
				par1World.setBlock(l, i1, j1, Blocks.air, 0, 2);
				i1++;
			}
		}
	}

	protected boolean isWaterBelow(World par1World, int par4, int par5, int par6,
			StructureBoundingBox par7StructureBoundingBox) {
		int j1 = getXWithOffset(par4, par6);
		int k1 = getYWithOffset(par5);
		int l1 = getZWithOffset(par4, par6);
		for (int i = 0; i < 10; i++) {
			Material material = par1World.getBlock(j1, k1, l1).getMaterial();
			if ((material.isLiquid()) || (material == Material.ice)) {
				return true;
			}
			if (!par1World.isAirBlock(j1, k1, l1)) {
				return false;
			}
		}
		return false;
	}

	public void setDispenser(int x, int y, int z, Random random, World world, int facing) {
		int i1 = getXWithOffset(x, z);
		int j1 = getYWithOffset(y);
		int k1 = getZWithOffset(x, z);

		world.setBlock(i1, j1, k1, Blocks.dispenser, facing, 0);
		TileEntity tileDispenser = world.getTileEntity(i1, j1, k1);
		if ((tileDispenser != null) && ((tileDispenser instanceof TileEntityDispenser))) {
			ChestGenHooks info = ChestGenHooks.getInfo("mineshaftCorridor");
			WeightedRandomChestContent.generateChestContents(random, info.getItems(random),
					(TileEntityDispenser) tileDispenser, info.getCount(random));
		} else {
			Logger.WARNING("Failed to fetch dispenser entity at (" + i1 + ", " + j1 + ", " + k1 + ")");
		}
	}

	protected void setSpawner(int x, int y, int z, String mobName, World world) {
		int i1 = getXWithOffset(x, z);
		int j1 = getYWithOffset(y);
		int k1 = getZWithOffset(x, z);

		world.setBlock(i1, j1, k1, Blocks.mob_spawner, 0, 2);
		TileEntity tileSpawner = world.getTileEntity(i1, j1, k1);
		if ((tileSpawner != null) && ((tileSpawner instanceof TileEntityMobSpawner))) {
			((TileEntityMobSpawner) tileSpawner).func_145881_a().setEntityName(mobName);
		} else {
			Logger.WARNING("Failed to fetch mob spawner entity at (" + i1 + ", " + j1 + ", " + k1 + ")");
		}
	}

	protected void setFurnace(int x, int y, int z, World world) {
		int i1 = getXWithOffset(x, z);
		int j1 = getYWithOffset(y);
		int k1 = getZWithOffset(x, z);

		world.setBlock(i1, j1, k1, Blocks.furnace, getMetadataWithOffset(Blocks.piston, 3), 2);
	}

	protected void placeAirBlockAtPos(int x, int y, int z, StructureBoundingBox bounds, World world) {
		placeBlockAtCurrentPosition(world, Blocks.air, 0, x, y, z, bounds);
	}

	protected void place(Block block, int meta, int x, int y, int z, StructureBoundingBox bounds, World world) {
		placeBlockAtCurrentPosition(world, block, meta, x, y, z, bounds);
	}

	protected StructureBoundingBox calcBox(int direction, int x, int y, int z, int xLength, int height, int zLength,
			int xShift) {
		int minX = 0;
		int maxX = 0;
		int minY = y;
		int maxY = y + height;
		int minZ = 0;
		int maxZ = 0;
		switch (direction) {
		case 0:
			minX = x - xShift;
			maxX = x - xShift + xLength;
			minZ = z;
			maxZ = z + zLength;
			break;
		case 1:
			minX = x - zLength;
			maxX = x;
			minZ = z - xShift;
			maxZ = z - xShift + xLength;
			break;
		case 2:
			minX = x - xShift;
			maxX = x - xShift + xLength;
			minZ = z - zLength;
			maxZ = z;
			break;
		case 3:
			minX = x;
			maxX = x + zLength;
			minZ = z - xShift;
			maxZ = z - xShift + xLength;
		}
		return new StructureBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
	}

	protected int calcGroundHeight(World world, StructureBoundingBox boundingBox) {
		int height = 0;
		int count = 0;
		for (int z = boundingBox.minZ; z <= boundingBox.maxZ; z++) {
			for (int x = boundingBox.minX; x <= boundingBox.maxX; x++) {
				if (boundingBox.isVecInside(x, 64, z)) {
					height += Math.max(world.getTopSolidOrLiquidBlock(x, z), world.provider.getAverageGroundLevel());
					count++;
				}
			}
		}
		if (count == 0) {
			return -1;
		}
		return height / count;
	}

	protected void func_143012_a(NBTTagCompound nbttagcompound) {
	}

	protected void func_143011_b(NBTTagCompound nbttagcompound) {
	}

	public boolean addComponentParts(World world, Random random, StructureBoundingBox structureboundingbox) {
		return true;
	}
}
