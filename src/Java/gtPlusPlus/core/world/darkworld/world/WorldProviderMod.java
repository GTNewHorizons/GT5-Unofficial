package gtPlusPlus.core.world.darkworld.world;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.IChunkProvider;

import gtPlusPlus.core.world.darkworld.Dimension_DarkWorld;
import gtPlusPlus.core.world.darkworld.chunk.ChunkProviderModded;

public class WorldProviderMod extends WorldProvider {

	@Override
	public void registerWorldChunkManager() {
		this.worldChunkMgr = new WorldChunkManagerCustom(this.worldObj.getSeed(), WorldType.AMPLIFIED);
		this.isHellWorld = false;
		this.hasNoSky = false;
		this.dimensionId = Dimension_DarkWorld.DIMID;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Vec3 getFogColor(float par1, float par2) {
		return Vec3.createVectorHelper(0.01568627450980392D, 0.09019607843137255D, 0.0D);
	}

	@Override
	public IChunkProvider createChunkGenerator() {
		return new ChunkProviderModded(this.worldObj, this.worldObj.getSeed() - 1278);
	}

	@Override
	public boolean isSurfaceWorld() {
		return true;
	}

	@Override
	public boolean canCoordinateBeSpawn(int par1, int par2) {
		return false;
	}

	@Override
	public boolean canRespawnHere() {
		return true;
	}

	@Override
	public float getSunBrightness(float par1) {
		return (par1*2F);
	}

	@Override
	public float getStarBrightness(float par1) {
		return (par1*5F);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean doesXZShowFog(int par1, int par2) {
		return true;
	}

	@Override
	public String getDimensionName() {
		return "dimensionDarkWorld";
	}

}