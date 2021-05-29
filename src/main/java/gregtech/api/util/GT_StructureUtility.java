package gregtech.api.util;

import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.IStructureElementNoPlacement;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GT_StructureUtility {
	private GT_StructureUtility() {
		throw new AssertionError("Not instantiable");
	}

	public static <T> IStructureElementNoPlacement<T> ofHatchAdder(IGT_HatchAdder<T> IGT_HatchAdder, int textureIndex, int dots) {
		return ofHatchAdder(IGT_HatchAdder, textureIndex, StructureLibAPI.getBlockHint(), dots - 1);
	}

	public static <T> IStructureElementNoPlacement<T> ofHatchAdder(IGT_HatchAdder<T> IGT_HatchAdder, int textureIndex, Block hintBlock, int hintMeta) {
		if (IGT_HatchAdder == null || hintBlock == null) {
			throw new IllegalArgumentException();
		}
		return new IStructureElementNoPlacement<T>() {
			@Override
			public boolean check(T t, World world, int x, int y, int z) {
				TileEntity tileEntity = world.getTileEntity(x, y, z);
				return tileEntity instanceof IGregTechTileEntity && IGT_HatchAdder.apply(t, (IGregTechTileEntity) tileEntity, (short) textureIndex);
			}

			@Override
			public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
				StructureLibAPI.hintParticle(world, x, y, z, hintBlock, hintMeta);
				return true;
			}
		};
	}

	public static <T> IStructureElement<T> ofHatchAdderOptional(IGT_HatchAdder<T> IGT_HatchAdder, int textureIndex, int dots, Block placeCasing, int placeCasingMeta) {
		return ofHatchAdderOptional(IGT_HatchAdder, textureIndex, StructureLibAPI.getBlockHint(), dots - 1, placeCasing, placeCasingMeta);
	}

	public static <T> IStructureElement<T> ofHatchAdderOptional(IGT_HatchAdder<T> IGT_HatchAdder, int textureIndex, Block hintBlock, int hintMeta, Block placeCasing, int placeCasingMeta) {
		if (IGT_HatchAdder == null || hintBlock == null) {
			throw new IllegalArgumentException();
		}
		return new IStructureElement<T>() {
			@Override
			public boolean check(T t, World world, int x, int y, int z) {
				TileEntity tileEntity = world.getTileEntity(x, y, z);
				Block worldBlock = world.getBlock(x, y, z);
				return (tileEntity instanceof IGregTechTileEntity &&
						IGT_HatchAdder.apply(t, (IGregTechTileEntity) tileEntity, (short) textureIndex)) ||
						(worldBlock == placeCasing && worldBlock.getDamageValue(world, x, y, z) == placeCasingMeta);
			}

			@Override
			public boolean spawnHint(T t, World world, int x, int y, int z, ItemStack trigger) {
				StructureLibAPI.hintParticle(world, x, y, z, hintBlock, hintMeta);
				return true;
			}

			@Override
			public boolean placeBlock(T t, World world, int x, int y, int z, ItemStack trigger) {
				world.setBlock(x, y, z, placeCasing, placeCasingMeta, 2);
				return true;
			}
		};
	}
}
