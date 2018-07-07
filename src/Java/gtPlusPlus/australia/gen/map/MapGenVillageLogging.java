package gtPlusPlus.australia.gen.map;

import java.util.Map;
import java.util.Random;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraft.world.gen.structure.StructureStart;

public class MapGenVillageLogging extends MapGenVillage {

	public MapGenVillageLogging() {
		super();
	}

	public MapGenVillageLogging(Map p_i2093_1_) {
		super(p_i2093_1_);
	}

	@Override
	public String func_143025_a() {
		return super.func_143025_a();
	}

	@Override
	protected boolean canSpawnStructureAtCoords(int p_75047_1_, int p_75047_2_) {

		return super.canSpawnStructureAtCoords(p_75047_1_, p_75047_2_);
	}

	@Override
	protected StructureStart getStructureStart(int p_75049_1_, int p_75049_2_) {
		return new MapGenVillageLogging.Start(this.worldObj, this.rand, p_75049_1_, p_75049_2_, 0);
	}

	public static class StartLogging extends Start {
		/** well ... thats what it does */
		private boolean hasMoreThanTwoComponents;
		private static final String __OBFID = "CL_00000515";

		public StartLogging() {
		}

		public StartLogging(World p_i2092_1_, Random p_i2092_2_, int p_i2092_3_, int p_i2092_4_, int p_i2092_5_) {
			super(p_i2092_1_, p_i2092_2_, p_i2092_3_, p_i2092_4_, p_i2092_5_);
		}

		/**
		 * currently only defined for Villages, returns true if Village has more than 2
		 * non-road components
		 */
		public boolean isSizeableStructure() {
			return this.hasMoreThanTwoComponents;
		}

		public void func_143022_a(NBTTagCompound p_143022_1_) {
			super.func_143022_a(p_143022_1_);
		}

		public void func_143017_b(NBTTagCompound p_143017_1_) {
			super.func_143017_b(p_143017_1_);
		}
	}
}