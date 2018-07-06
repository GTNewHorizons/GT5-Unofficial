package gtPlusPlus.australia.world;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.IChunkProvider;
import gtPlusPlus.australia.chunk.ChunkProviderAustralia;
import gtPlusPlus.australia.dimension.Dimension_Australia;

public class AustraliaWorldProvider extends WorldProvider {

	@Override
	public void registerWorldChunkManager() {
		this.worldChunkMgr = new AustraliaWorldChunkManager(this.worldObj.getSeed(), WorldType.LARGE_BIOMES);
		this.isHellWorld = false;
		this.hasNoSky = false;
		this.dimensionId = Dimension_Australia.DIMID;
	}

	@Override
	public float getSunBrightness(float par1) {
		return (par1*2F);
	}

	@Override
	public float getStarBrightness(float par1) {
		return (par1*5F);
	}
	
	@SideOnly(Side.CLIENT)
    public Vec3 getFogColor(float par1, float par2)
    {
      return Vec3.createVectorHelper(0.8D, 0.8D, 0.8D);
    }

	@Override
	public IChunkProvider createChunkGenerator() {
		return new ChunkProviderAustralia(this.worldObj, this.worldObj.getSeed() - 15726L);
	}
    
    public boolean isSurfaceWorld()
    {
      return true;
    }
    
    public boolean canCoordinateBeSpawn(int par1, int par2)
    {
      return false;
    }
    
    public boolean canRespawnHere()
    {
      return true;
    }
    
    @SideOnly(Side.CLIENT)
    public boolean doesXZShowFog(int par1, int par2)
    {
      return false;
    }
    
    public String getDimensionName()
    {
      return "Australia";
    }
    
    protected void generateLightBrightnessTable()
    {
      float f = 0.5F;
      for (int i = 0; i <= 15; i++)
      {
        float f1 = 1.0F - i / 15.0F;
        this.lightBrightnessTable[i] = ((1.0F - f1) / (f1 * 3.0F + 1.0F) * (1.0F - f) + f);
      }
    }

}